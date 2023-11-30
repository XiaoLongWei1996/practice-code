package org.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/**
 * @description: 执行工具类
 * @Title: ExecuteUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2023/11/29 18:32
 */
public class ExecuteUtil {

    private static String osName = System.getProperty("os.name");

    public static void main(String[] args) {
        String s = ExecuteUtil.submit("dirs");
        System.out.println(s.isEmpty());
    }

    /**
     * 执行命令
     * @param command 命令
     */
    public static void exec(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd = preHandle(command);
        processBuilder.command(cmd);
        Process p = null;
        try {
            processBuilder.redirectErrorStream(true);
            p = processBuilder.inheritIO().start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static String submit(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String[] cmd = preHandle(command);
        processBuilder.command(cmd);
        Process p = null;
        try {
//            processBuilder.redirectErrorStream(true);
            p = processBuilder.start();
            FutureTask<String> task1 = new FutureTask<String>(new ProcessCall(p.getInputStream()));
            Thread t1 = new Thread(task1);
            t1.start();
            FutureTask<String> task2 = new FutureTask<String>(new ProcessCall(p.getErrorStream()));
            Thread t2 = new Thread(task2);
            t2.start();
            p.waitFor();
//            System.out.println(task1.get());
//            System.out.println(task2.get());
            return task1.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static void exec(String ...command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process p = null;
        try {
            processBuilder.redirectErrorStream(true);
            p = processBuilder.inheritIO().start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static void exec(List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process p = null;
        try {
            processBuilder.redirectErrorStream(true);
            p = processBuilder.inheritIO().start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static void asynExec(String ...command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process p = null;
        try {
            p = processBuilder.start();
            Thread t1 = new Thread(new ProcessTask(p.getInputStream()));
            t1.start();
            Thread t2 = new Thread(new ProcessTask(p.getErrorStream()));
            t2.start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static void asynExec(List<String> command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        Process p = null;
        try {
            p = processBuilder.start();
            Thread t1 = new Thread(new ProcessTask(p.getInputStream()));
            t1.start();
            Thread t2 = new Thread(new ProcessTask(p.getErrorStream()));
            t2.start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static void execScript(String path) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (osName.startsWith("Windows")) {
            processBuilder.command(path);
        } else {
            processBuilder.command("sh", path);
        }
        Process p = null;
        try {
            p = processBuilder.start();
            Thread t1 = new Thread(new ProcessTask(p.getInputStream()));
            t1.start();
            Thread t2 = new Thread(new ProcessTask(p.getErrorStream()));
            t2.start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    private static String[] preHandle(String command) {
        String[] cmd = new String[3];
        if (osName.startsWith("Windows")) {
            cmd[0] = "cmd.exe";
            cmd[1] = "/c";
            cmd[2] = command;
        } else if (osName.startsWith("Linux")) {
            cmd[0] = "sh";
            cmd[1] = "-c";
            cmd[2] = command;
        }
        return cmd;
    }

    private static class ProcessTask implements Runnable {

        private InputStream inputStream;

        public ProcessTask(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                reader.lines().forEach(System.out::println);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class ProcessCall implements Callable<String> {

        private InputStream inputStream;

        public ProcessCall(InputStream inputStream) {
            this.inputStream = inputStream;
        }
        @Override
        public String call() throws Exception {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                return reader.lines().collect(Collectors.joining("\r\n"));
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
