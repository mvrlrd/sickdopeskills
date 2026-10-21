/*
 * Copyright 2020 damios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Лицензия и копирайт выше относятся только к этому файлу.
package ru.ibelykh.sickdopeskills.desktop;

import org.lwjgl.system.macosx.LibC;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

/**
 * На macOS LWJGL3 умеет работать только на главном потоке JVM, а тот отдаётся GLFW
 * лишь при запуске с {@code -XstartOnFirstThread}. Положить флаг в манифест jar нельзя,
 * поэтому лончер перезапускает JVM сам.
 *
 * <p>Стандартный приём libGDX, взят из шаблонов gdx-liftoff без изменений по сути.
 */
public final class StartupHelper {

    private static final String JVM_RESTARTED_ARG = "jvmIsRestarted";

    private StartupHelper() {
    }

    /**
     * @return {@code true}, если JVM была перезапущена и текущий процесс должен просто выйти.
     */
    public static boolean startNewJvmIfRequired() {
        if (!System.getProperty("os.name", "").toLowerCase().contains("mac")) {
            return false;
        }

        long pid = LibC.getpid();

        // Флаг уже стоит — перезапускать нечего.
        if ("1".equals(System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid))) {
            return false;
        }

        // Защита от бесконечного перезапуска: если мы уже рестартовали, а флаг так и не виден,
        // дальше всё равно падать, но хотя бы не плодя процессы.
        if ("true".equals(System.getProperty(JVM_RESTARTED_ARG))) {
            System.err.println("Не удалось определить, запущена ли JVM с -XstartOnFirstThread.");
            return false;
        }

        String separator = System.getProperty("file.separator", "/");
        String javaExecPath = System.getProperty("java.home") + separator + "bin" + separator + "java";
        if (!new File(javaExecPath).exists()) {
            System.err.println("Java не найдена; запускай вручную с аргументом -XstartOnFirstThread.");
            return false;
        }

        String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
        if (mainClass == null) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            if (trace.length == 0) {
                System.err.println("Не удалось определить главный класс.");
                return false;
            }
            mainClass = trace[trace.length - 1].getClassName();
        }

        ArrayList<String> jvmArgs = new ArrayList<String>();
        jvmArgs.add(javaExecPath);
        jvmArgs.add("-XstartOnFirstThread");
        jvmArgs.add("-D" + JVM_RESTARTED_ARG + "=true");
        jvmArgs.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        jvmArgs.add("-cp");
        jvmArgs.add(System.getProperty("java.class.path"));
        jvmArgs.add(mainClass);

        try {
            Process process = new ProcessBuilder(jvmArgs).redirectErrorStream(true).start();
            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = output.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            System.err.println("Не удалось перезапустить JVM.");
            e.printStackTrace();
        }

        return true;
    }
}
