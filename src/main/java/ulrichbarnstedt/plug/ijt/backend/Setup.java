package ulrichbarnstedt.plug.ijt.backend;

import com.intellij.openapi.application.PathManager;
import ulrichbarnstedt.plug.ijt.util.StdRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public class Setup {
    private Setup () {}

    public static void run (BiConsumer<String, String> log, Path pluginDir) {
        log.accept("PRE-EX", "Downloading required files ... \n");

        if (!StdRunner.execute(
            log,
            Paths.get(PathManager.getPluginsPath() + "/IJ_teams/"),
            "PRE-EX",
            "GIT",
            "Error attempting to clone repository. Exception:\n",
            "Something went wrong cloning the repository. There is most likely more information above.\n",
            "git",
            "clone", "https://github.com/ulrich-barnstedt/intellij-teams-integration-backend.git", "backend"
        )) return;

        if (!StdRunner.execute(
            log,
            pluginDir,
            "PRE-EX",
            "NODE",
            "Error attempting to start NPM. Exception: \n",
            "Something went wrong installing packages. There is most likely more information above.\n",
            "npm",
            "install"
        )) return;

        log.accept("PRE-EX", "Finished downloading. \n");
    }
}
