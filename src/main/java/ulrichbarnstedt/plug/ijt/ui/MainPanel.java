package ulrichbarnstedt.plug.ijt.ui;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.concurrency.SwingWorker;
import ulrichbarnstedt.plug.ijt.backend.Setup;
import ulrichbarnstedt.plug.ijt.backend.Wrapper;
import ulrichbarnstedt.plug.ijt.settings.IJTSettingsState;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainPanel {
    private JPanel content;
    private JButton uploadButton;
    private JTextArea logPane;
    private JBTextField projectField;
    private JBTextField statusField;
    private final Project currentProject;

    public MainPanel (Project project) {
        this.currentProject = project;

        projectField.getEmptyText().setText("Project");
        statusField.getEmptyText().setText("Status");

        uploadButton.addActionListener(e -> {
            this.uploadButton.setEnabled(false);
            this.handleButton();
        });

    }

    public void addLog (String tag, String content) {
        logPane.append(String.format("[%s] %s", tag, content));
    }

    private void handleButton () {
        String assumedLocation = currentProject.getPresentableUrl();
        if (assumedLocation == null) {
            this.addLog("PRE-EX", "ERROR: Cannot start upload. Project directory could not be located. (Are you in a Intellij Project?)\n");
            this.uploadButton.setEnabled(true);
            return;
        }

        Path pluginDirectory = Paths.get(PathManager.getPluginsPath() + "/IJ_teams/backend");
        Path projectDirectory = Paths.get(assumedLocation);

        boolean setup = false;
        if (Files.notExists(pluginDirectory)) {
            ConfirmPrompt userAgreement = new ConfirmPrompt(
                "First upload run",
                "The files required for the plugin to run have not been downloaded yet, as this is the first upload being run.\nAbout 300mb of files will be downloaded.",
                "Download", "Cancel"
            );

            if (userAgreement.query()) {
                setup = true;
            } else {
                this.uploadButton.setEnabled(true);
                return;
            }
        }

        this.runOutsideEDT(setup, pluginDirectory, projectDirectory);
    }

    private void runOutsideEDT (boolean setup, Path pluginDirectory, Path projectDirectory) {
        MainPanel outer = this;

        SwingWorker<Void> bgRunner = new SwingWorker<Void>() {
            @Override
            public Void construct () {
                if (setup) {
                    Setup.run(outer::addLog, pluginDirectory);
                }

                outer.addLog(
                    "INTELLIJ",
                    String.format("Starting upload (%s)\n", new SimpleDateFormat("hh:mm:ss").format(new Date()))
                );

                Wrapper runWrapper = new Wrapper(pluginDirectory, projectDirectory, IJTSettingsState.getInstance().teamID, projectField.getText(), statusField.getText());
                runWrapper.run(outer::addLog);

                outer.uploadButton.setEnabled(true);
                return null;
            }
        };

        bgRunner.start();
    }

    public JPanel getContent () {
        return content;
    }
}
