package ulrichbarnstedt.plug.ijt.backend;

import com.intellij.openapi.application.PathManager;
import ulrichbarnstedt.plug.ijt.util.StdRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public class Setup {
    private Setup () {}

    private static void pull () {

    }

    private static boolean npm (BiConsumer<String, String> log, Path pluginDir, String group) {
        return StdRunner.execute(
            log,
            pluginDir,
            group,
            "NPM",
            "Error attempting to start NPM. Exception: \n",
            "Something went wrong installing packages. There is most likely more information above.\n",
            "node",
            "install.js"
        );
    }

    public static void update () {

    }

    public static void install (BiConsumer<String, String> log, Path pluginDir) {
        log.accept("PRE-EX", "Downloading required files ... \n");
        log.accept("PRE-EX", "Cloning repository ... \n");

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

        log.accept("PRE-EX", "Repository cloned. Starting download of packages ... \n");
        if (!npm(log, pluginDir, "PRE-EX")) return;
        log.accept("PRE-EX", "Finished all downloads. \n");
    }
}
