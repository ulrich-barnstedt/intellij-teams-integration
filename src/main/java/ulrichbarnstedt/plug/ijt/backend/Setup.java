package ulrichbarnstedt.plug.ijt.backend;

import java.nio.file.Path;
import java.util.function.Consumer;

public class Setup {
    private Setup () {}

    public static void run (Consumer<String> log, Path pluginDir) {
        log.accept("Downloading required files ... \n");

        //TODO: git clone in plugin wd

        log.accept("Finished downloading. \n");
    }
}
