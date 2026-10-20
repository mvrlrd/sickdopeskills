#!/usr/bin/env python3
"""Сверяет ассеты, которые просит код, с теми, что реально лежат в репозитории.

Проект умер именно на этом шве: код грузил регионы атласов, которых не давал
ни один закоммиченный файл, и ничто не поймало это до рантайма.

Запуск из корня репозитория:
    python3 tools/check_assets.py [--verbose]

По умолчанию проверяется репозиторий, в котором лежит сам скрипт,
поэтому рабочий каталог значения не имеет.

Код возврата: 0 — всё на месте, 1 — чего-то не хватает.
"""

import argparse
import os
import re
import sys
from collections import defaultdict

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCE_DIR = os.path.join("core", "src")
ASSETS_DIR = os.path.join("android", "assets")

RE_CONST = re.compile(r'static\s+final\s+String\s+(\w+)\s*=\s*"([^"]*)"')
RE_FIND_REGION = re.compile(r'findRegion\s*\(\s*([^),]+?)\s*[),]')
RE_ATLAS = re.compile(r'new\s+TextureAtlas\s*\(\s*"([^"]+)"')
RE_INTERNAL = re.compile(r'Gdx\.files\.internal\s*\(\s*"([^"]+)"')
RE_FONT = re.compile(r'new\s+Font\s*\(\s*"([^"]+)"\s*,\s*"([^"]+)"')
RE_STRING_LITERAL = re.compile(r'^"([^"]*)"$')


def strip_comments(text):
    """Убирает // и /* */, чтобы закомментированный код не считался запросом."""
    text = re.sub(r'/\*.*?\*/', '', text, flags=re.S)
    return re.sub(r'//[^\n]*', '', text)


def java_files(root):
    for dirpath, _, filenames in os.walk(root):
        for name in sorted(filenames):
            if name.endswith(".java"):
                yield os.path.join(dirpath, name)


def parse_atlas(path):
    """Возвращает (страницы, регионы) из .atlas в формате libGDX.

    Пустая строка разделяет страницы; первая строка файла и первая строка
    после пустой — имя файла страницы. Прочие строки без отступа — регионы.
    """
    pages, regions = [], []
    expect_page = True
    with open(path, encoding="utf-8", errors="replace") as handle:
        for raw in handle:
            line = raw.rstrip("\n")
            if not line.strip():
                expect_page = True
                continue
            if line[0].isspace():
                continue
            if expect_page:
                pages.append(line.strip())
                expect_page = False
            elif ":" not in line:
                regions.append(line.strip())
    return pages, regions


def collect_requests(source_root):
    """Собирает из кода запрошенные пути, регионы и неразрешённые аргументы."""
    paths = defaultdict(list)
    regions = defaultdict(list)
    unresolved = []

    for java_path in java_files(source_root):
        with open(java_path, encoding="utf-8", errors="replace") as handle:
            text = strip_comments(handle.read())

        constants = dict(RE_CONST.findall(text))
        for lineno, line in enumerate(text.splitlines(), start=1):
            where = f"{java_path}:{lineno}"

            for match in RE_ATLAS.finditer(line):
                paths[match.group(1)].append(where)
            for match in RE_INTERNAL.finditer(line):
                paths[match.group(1)].append(where)
            for match in RE_FONT.finditer(line):
                paths[match.group(1)].append(where)
                paths[match.group(2)].append(where)

            for match in RE_FIND_REGION.finditer(line):
                arg = match.group(1).strip()
                literal = RE_STRING_LITERAL.match(arg)
                if literal:
                    regions[literal.group(1)].append(where)
                elif arg in constants:
                    regions[constants[arg]].append(where)
                else:
                    unresolved.append((arg, where))

    return paths, regions, unresolved


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--repo", default=REPO_ROOT, help="корень репозитория")
    parser.add_argument("--verbose", action="store_true", help="печатать и то, что найдено")
    args = parser.parse_args()

    repo = os.path.abspath(args.repo)
    source_root = os.path.join(repo, SOURCE_DIR)
    assets_root = os.path.join(repo, ASSETS_DIR)

    for required in (source_root, assets_root):
        if not os.path.isdir(required):
            print(f"ОШИБКА: нет каталога {required}", file=sys.stderr)
            return 2

    paths, regions, unresolved = collect_requests(source_root)

    atlases = {}
    for dirpath, _, filenames in os.walk(assets_root):
        for name in sorted(filenames):
            if name.endswith(".atlas"):
                full = os.path.join(dirpath, name)
                atlases[os.path.relpath(full, assets_root)] = parse_atlas(full)

    failures = []

    # 1. Пути, которые код передаёт загрузчику ассетов.
    for rel_path in sorted(paths):
        if not os.path.isfile(os.path.join(assets_root, rel_path)):
            failures.append(f"нет файла: {rel_path}  <- {', '.join(paths[rel_path])}")
        elif args.verbose:
            print(f"  ok  файл      {rel_path}")

    # 2. Картинки-страницы, на которые ссылаются сами атласы.
    for rel_atlas, (pages, _) in sorted(atlases.items()):
        atlas_dir = os.path.dirname(rel_atlas)
        for page in pages:
            page_rel = os.path.normpath(os.path.join(atlas_dir, page))
            if not os.path.isfile(os.path.join(assets_root, page_rel)):
                failures.append(f"нет страницы атласа: {page_rel}  <- {rel_atlas}")
            elif args.verbose:
                print(f"  ok  страница  {page_rel}")

    # 3. Регионы: ищем в атласах, которые код действительно грузит.
    loaded = {p for p in paths if p.endswith(".atlas")}
    searchable = {rel: data for rel, data in atlases.items() if rel in loaded}
    if not searchable:
        searchable = atlases

    provider = {}
    for rel_atlas, (_, region_names) in searchable.items():
        for region in region_names:
            provider.setdefault(region, []).append(rel_atlas)

    for region in sorted(regions):
        if region not in provider:
            failures.append(f"нет региона: \"{region}\"  <- {', '.join(regions[region])}")
        elif args.verbose:
            print(f"  ok  регион    \"{region}\" в {', '.join(provider[region])}")

    # Неразрешённые аргументы — предупреждение, решает человек.
    for arg, where in unresolved:
        print(f"ВНИМАНИЕ: findRegion({arg}) — аргумент не вычисляется статически, "
              f"проверьте вручную  <- {where}")

    print()
    print(f"Атласов найдено: {len(atlases)}; из них грузится кодом: {len(loaded)}")
    print(f"Запрошено путей: {len(paths)}; регионов: {len(regions)}")

    if failures:
        print(f"\nНЕ ХВАТАЕТ ({len(failures)}):")
        for failure in failures:
            print(f"  - {failure}")
        return 1

    print("\nВсё на месте.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
