package ulrichbarnstedt.plug.ijt.backend;

import java.nio.file.Path;
import java.util.function.BiConsumer;

public class Setup {
    private Setup () {}

    public static void run (BiConsumer<String, String> log, Path pluginDir) {
        log.accept("PRE-EX", "Downloading required files ... \n");

        //TODO: git clone in plugin wd

        log.accept("PRE-EX", "Finished downloading. \n");
    }
}
