#!/usr/bin/env python3
"""Проверяет утверждения, на которых держится консолидация репозитория.

Девять старых ветвей удаляются из репозитория. Удаление необратимо, поэтому
перед ним нужно доказательство, а не память: каждая вершина архивируемой ветки
лежит под тегом `archive/<name>` локально и на origin, `develop` содержит все
ассеты, которые грузит код, модуля `ios` нет.

Запуск из любого каталога (проверяется репозиторий, в котором лежит скрипт):
    python3 tools/check_repo_state.py [--verbose]

Нужна сеть: теги сверяются с origin, иначе «запушено» остаётся на честном слове.

Код возврата: 0 — всё сошлось, 1 — есть нарушения, 2 — проверить не удалось.
"""

import argparse
import os
import subprocess
import sys

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# Вершины девяти архивируемых ветвей, снятые с origin перед созданием тегов
# (тикет 10, 2026-10-21). Зашиты намеренно: после удаления ветвей сверять
# теги будет не с чем, а проверка должна продолжать работать.
ARCHIVED_TIPS = {
    "master": "a8419f0170241a57bef7569c380b81c1ae7321ef",
    "makingSingleton": "6497901fd44c34efdd199f9a9b9ec0a100d7b1d7",
    "flags": "93b6e1d18186a484a7db12a361f75c5b90f8bfa7",
    "checkCol": "e6446fe6c52a99c0f3d34135cb8e1d7c4a137131",
    "reCheckCol": "2801cdea72dec8436baef651807d9b1b9a6478a9",
    "doHardRefactor": "66188242171cf72dec2b6488c5410bdfe7942b76",
    "cleanTheCode": "f1897dd10fc88b4d5b2c8a3f921d7389134036d6",
    "vectorSpriteTest": "0fd662f30b3f640589726dd82cb0cb5ad0c6122e",
    "vectorSpriteTest-local": "acf1843aa614572cf58e5cf45d446c4f9f1ed080",
}

SURVIVING_BRANCH = "develop"


def git(repo, *args):
    """Возвращает (код возврата, stdout)."""
    result = subprocess.run(
        ["git", "-C", repo, *args],
        capture_output=True, text=True,
    )
    return result.returncode, result.stdout.strip()


def check_local_tags(repo, failures, verbose):
    """Возвращает refs тегов, которые реально существуют."""
    existing = []
    for name, expected in sorted(ARCHIVED_TIPS.items()):
        tag = f"archive/{name}"
        code, sha = git(repo, "rev-list", "-n", "1", f"refs/tags/{tag}")
        if code != 0:
            failures.append(f"нет локального тега {tag}")
            continue
        existing.append(f"refs/tags/{tag}")
        if sha != expected:
            failures.append(f"{tag} указывает на {sha[:9]}, ожидалось {expected[:9]}")
        elif verbose:
            print(f"  ok  тег       {tag} -> {expected[:9]}")
    return existing


def check_remote_tags(repo, failures, verbose):
    code, out = git(repo, "ls-remote", "--tags", "origin")
    if code != 0:
        print("ОШИБКА: не удалось прочитать теги с origin", file=sys.stderr)
        return False

    remote = {}
    for line in out.splitlines():
        sha, _, ref = line.partition("\t")
        # Аннотированный тег даёт две строки: сам объект тега и ^{} с коммитом.
        if ref.startswith("refs/tags/") and not ref.endswith("^{}"):
            remote[ref[len("refs/tags/"):]] = sha
        elif ref.endswith("^{}"):
            remote[ref[len("refs/tags/"):-len("^{}")]] = sha

    for name, expected in sorted(ARCHIVED_TIPS.items()):
        tag = f"archive/{name}"
        if tag not in remote:
            failures.append(f"тег {tag} не запушен на origin")
        elif remote[tag] != expected:
            failures.append(
                f"на origin {tag} указывает на {remote[tag][:9]}, ожидалось {expected[:9]}"
            )
        elif verbose:
            print(f"  ok  origin    {tag} -> {expected[:9]}")
    return True


def check_nothing_orphaned(repo, tags, failures, verbose):
    """Ни один коммит архивируемых ветвей не остался вне тегов.

    Проверяются все ветки, кроме develop: это ловит и локальную ветку, которая
    разошлась с origin и чья вершина под тегом не лежит.
    """
    # for-each-ref, а не branch: у branch в выводе есть псевдострока
    # «(HEAD detached at ...)», которая не является ref и ломает rev-list.
    code, out = git(repo, "for-each-ref", "--format=%(refname)", "refs/heads")
    if code != 0:
        print("ОШИБКА: не удалось перечислить локальные ветки", file=sys.stderr)
        return False

    branches = [ref for ref in out.splitlines()
                if ref != f"refs/heads/{SURVIVING_BRANCH}"]
    if not branches:
        if verbose:
            print("  ok  ветки     кроме develop ветвей нет")
        return True

    if not tags:
        failures.append("покрытие ветвей проверить нечем: ни одного архивного тега")
        return True

    code, out = git(repo, "rev-list", *branches, "--not", *tags)
    if code != 0:
        print("ОШИБКА: не удалось обойти историю ветвей", file=sys.stderr)
        return False

    orphans = out.splitlines() if out else []
    if orphans:
        failures.append(
            f"коммитов вне архивных тегов: {len(orphans)} "
            f"(например {orphans[0][:9]})"
        )
    elif verbose:
        print(f"  ok  ветки     {len(branches)} шт. целиком покрыты тегами")
    return True


def check_no_ios(repo, failures, verbose):
    code, out = git(repo, "ls-files", "--", "ios")
    if code != 0:
        print("ОШИБКА: не удалось спросить git о каталоге ios", file=sys.stderr)
        return False
    if out:
        failures.append(f"модуль ios всё ещё в индексе: {len(out.splitlines())} файлов")
    elif verbose:
        print("  ok  ios       отслеживаемых файлов нет")

    settings = os.path.join(repo, "settings.gradle.kts")
    if not os.path.isfile(settings):
        failures.append("нет settings.gradle.kts")
        return True
    with open(settings, encoding="utf-8") as handle:
        text = handle.read()
    if '"ios"' in text:
        failures.append("settings.gradle.kts всё ещё подключает модуль ios")
    elif verbose:
        print("  ok  ios       не подключён в settings.gradle.kts")
    return True


def check_assets(repo, failures, verbose):
    """Переиспользует шов из тикета 03 вместо того, чтобы дублировать логику."""
    script = os.path.join(repo, "tools", "check_assets.py")
    if not os.path.isfile(script):
        # Скрипт живёт на develop. Если его нет, рабочее дерево не на develop,
        # и утверждение про ассеты здесь просто нечем проверить.
        failures.append(
            "ассеты не проверены: нет tools/check_assets.py, "
            "рабочее дерево не на develop"
        )
        return

    command = [sys.executable, script, "--repo", repo]
    if verbose:
        command.append("--verbose")
    result = subprocess.run(command, capture_output=True, text=True)
    if result.returncode != 0:
        failures.append("проверка ассетов не прошла (вывод ниже)")
        print(result.stdout.rstrip())
        print(result.stderr.rstrip(), file=sys.stderr)
    elif verbose:
        print(result.stdout.rstrip())
        print("  ok  ассеты    всё, что грузит код, лежит в develop")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--repo", default=REPO_ROOT, help="корень репозитория")
    parser.add_argument("--verbose", action="store_true", help="печатать и то, что сошлось")
    args = parser.parse_args()

    repo = os.path.abspath(args.repo)
    if not os.path.isdir(os.path.join(repo, ".git")):
        print(f"ОШИБКА: {repo} не похож на git-репозиторий", file=sys.stderr)
        return 2

    failures = []
    local_tags = check_local_tags(repo, failures, args.verbose)
    if not check_remote_tags(repo, failures, args.verbose):
        return 2
    if not check_nothing_orphaned(repo, local_tags, failures, args.verbose):
        return 2
    if not check_no_ios(repo, failures, args.verbose):
        return 2
    check_assets(repo, failures, args.verbose)

    print()
    print(f"Архивных тегов ожидается: {len(ARCHIVED_TIPS)}")

    if failures:
        print(f"\nНАРУШЕНИЙ ({len(failures)}):")
        for failure in failures:
            print(f"  - {failure}")
        return 1

    print("\nСостояние репозитория сошлось: архив полон, ассеты на месте, ios нет.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
