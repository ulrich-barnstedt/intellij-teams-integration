package ulrichbarnstedt.plug.ijt.backend;

import com.intellij.execution.configurations.GeneralCommandLine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

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

    public String fmtCmd () {
        return String.format("node index.js \"%s\" %s %s %s", this.projectDir, this.id, this.project, this.status);
    }

    public void run (Consumer<String> log) {
        log.accept("[IJ] Starting runner ... \n");

        GeneralCommandLine cmd = new GeneralCommandLine(this.fmtCmd());
        cmd.setCharset(StandardCharsets.UTF_8);
        cmd.setWorkDirectory(this.pluginDir.toString());

        //TODO: execute

        log.accept(String.format("[IJ] Finished uploading (%s)\n\n", new SimpleDateFormat("hh:mm:ss").format(new Date())));
    }
}
