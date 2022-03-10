package ulrichbarnstedt.plug.ijt.backend;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.BiConsumer;

public class Wrapper {
    Path pluginDir;
    Path projectDir;
    String id;
    String project;
    String status;

    public Wrapper (Path pluginDir, Path projectDir, String id, String project, String status) {
        this.pluginDir = pluginDir;
        this.projectDir = projectDir;
        this.id = id;
        this.project = project;
        this.status = status;
    }

    public void run (BiConsumer<String, String> log) {
        log.accept("INTELLIJ", "Starting runner ... \n");

        GeneralCommandLine cmd = new GeneralCommandLine();
        cmd.setCharset(StandardCharsets.UTF_8);
        cmd.setWorkDirectory(this.pluginDir.toString());
        cmd.setExePath("node");
        cmd.addParameters("index.js", this.projectDir.toString(), this.id);
        cmd.addParameter(this.project.equals("") ? "no_project" : this.project);
        cmd.addParameter(this.status.equals("") ? "no_status" : this.status);

        ProcessHandler runner;
        try {
            runner = new OSProcessHandler(cmd);
        } catch (ExecutionException e) {
            log.accept("INTELLIJ", "Error attempting to create runner. Exception:\n");
            log.accept("INTELLIJ-STACKTRACE", e.toString());
            return;
        }

        runner.startNotify();
        runner.addProcessListener(new ProcessListener() {
            @Override
            public void startNotified (@NotNull ProcessEvent event) {}

            @Override
            public void processTerminated (@NotNull ProcessEvent event) {}

            @Override
            public void onTextAvailable (@NotNull ProcessEvent event, @NotNull Key outputType) {
                log.accept("NODE", event.getText());
            }
        });

        runner.waitFor();
        if (runner.getExitCode() != 0) {
            log.accept("INTELLIJ", "Upload failed. There are most likely errors above detailing any problems.");
            return;
        }

        log.accept(
            "INTELLIJ",
            String.format("Finished uploading (%s)\n\n", new SimpleDateFormat("hh:mm:ss").format(new Date()))
        );
    }
}
