package ulrichbarnstedt.plug.ijt.util;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class StdRunner {
    public static boolean execute (
        BiConsumer<String, String> log,
        Path cwd,
        String globalTag,
        String subTag,
        String preError,
        String postError,
        String exe,
        String ... args
    ) {
        String subTagPrefix = subTag.equals("") ? "" : "[" + subTag + "] ";

        GeneralCommandLine cmd = new GeneralCommandLine();
        cmd.setCharset(StandardCharsets.UTF_8);
        cmd.setExePath(exe);
        cmd.setWorkDirectory(cwd.toString());
        cmd.addParameters(args);

        ProcessHandler runner;
        try {
            runner = new OSProcessHandler(cmd);
        } catch (ExecutionException e) {
            log.accept(globalTag, preError);
            log.accept(globalTag + "-STACKTRACE", e.toString() + "\n");
            return false;
        }

        runner.startNotify();
        runner.addProcessListener(new ProcessListener() {
            @Override
            public void startNotified (@NotNull ProcessEvent event) {}

            @Override
            public void processTerminated (@NotNull ProcessEvent event) {}

            @Override
            public void onTextAvailable (@NotNull ProcessEvent event, @NotNull Key outputType) {
                log.accept(globalTag, subTagPrefix + event.getText());
            }
        });

        runner.waitFor();
        if (runner.getExitCode() != 0) {
            log.accept(globalTag, postError);
            return false;
        }

        return true;
    }
}
