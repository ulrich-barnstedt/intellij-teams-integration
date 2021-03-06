package ulrichbarnstedt.plug.ijt.backend;

import ulrichbarnstedt.plug.ijt.settings.IJTSettingsState;
import ulrichbarnstedt.plug.ijt.util.StdRunner;

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
    String taskStatus;
    String repoName;
    boolean shouldSubmit;

    public Wrapper (Path pluginDir, Path projectDir, IJTSettingsState state, String project, String status, String taskStatus, boolean shouldSubmit) {
        this.pluginDir = pluginDir;
        this.projectDir = projectDir;
        this.id = state.teamID;
        this.repoName = state.repositoryName;
        this.project = project;
        this.status = status;
        this.taskStatus = taskStatus;
        this.shouldSubmit = shouldSubmit;
    }

    public void run (BiConsumer<String, String> log) {
        log.accept("INTELLIJ", "Starting runner ... \n");

        String[] args = {
            "index.js",
            this.projectDir.toString(),
            this.id,
            this.project.equals("") ? "no_project" : this.project,
            this.status.equals("") ? "no_status" : this.status,
            this.taskStatus,
            this.repoName,
            this.shouldSubmit ? "true" : "false"
        };

        if (!StdRunner.execute(
            log,
            pluginDir,
            "NODE",
            "",
            "Error attempting to start Node.js. Exception: \n",
            "Error in Node.js process. There is most likely more information above.\n",
            "node",
            args
        )) {
            log.accept("INTELLIJ", "Upload failed. There is most likely an error log above.\n\n");
            return;
        };

        log.accept(
            "INTELLIJ",
            String.format("Finished uploading (%s)\n\n", new SimpleDateFormat("hh:mm:ss").format(new Date()))
        );
    }
}
