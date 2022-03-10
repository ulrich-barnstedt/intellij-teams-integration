package ulrichbarnstedt.plug.ijt.backend;

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

    public Wrapper (Path pluginDir, Path projectDir, String id, String project, String status) {
        this.pluginDir = pluginDir;
        this.projectDir = projectDir;
        this.id = id;
        this.project = project;
        this.status = status;
    }

    public void run (BiConsumer<String, String> log) {
        log.accept("INTELLIJ", "Starting runner ... \n");

        if (!StdRunner.execute(
            log,
            pluginDir,
            "NODE",
            "",
            "Error attempting to start Node.js. Exception: \n",
            "Upload failed. There is most likely more information above.\n",
            "node",
            "index.js", this.projectDir.toString(), this.id,
            this.project.equals("") ? "no_project" : this.project,
            this.status.equals("") ? "no_status" : this.status
        )) return;

        log.accept(
            "INTELLIJ",
            String.format("Finished uploading (%s)\n\n", new SimpleDateFormat("hh:mm:ss").format(new Date()))
        );
    }
}
