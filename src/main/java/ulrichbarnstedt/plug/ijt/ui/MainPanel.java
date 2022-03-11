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
    private JButton updateBackendButton;
    private JComboBox<String> taskStatusDropdown;

    private final Project currentProject;
    private final Path pluginDirectory;

    public MainPanel (Project project) {
        this.currentProject = project;
        this.pluginDirectory = Paths.get(PathManager.getPluginsPath() + "/IJ_teams/backend");

        projectField.getEmptyText().setText("Project");
        statusField.getEmptyText().setText("Status");

        uploadButton.addActionListener(e -> {
            this.uploadButton.setEnabled(false);
            this.handleButton();
        });

        updateBackendButton.addActionListener(e -> {
            this.updateBackendButton.setEnabled(false);
            this.handleUpdate();
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
        Path projectDirectory = Paths.get(assumedLocation);

        boolean setup = false;
        if (Files.notExists(pluginDirectory)) {
            ConfirmPrompt userAgreement = new ConfirmPrompt(
                "Missing files",
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

        this.runOutsideEDT(setup, projectDirectory);
    }

    private void runOutsideEDT (boolean setup, Path projectDirectory) {
        MainPanel outer = this;

        SwingWorker<Void> bgRunner = new SwingWorker<Void>() {
            @Override
            public Void construct () {
                if (setup) {
                    Setup.install(outer::addLog, pluginDirectory);
                }

                outer.addLog(
                    "INTELLIJ",
                    String.format("Starting upload (%s)\n", new SimpleDateFormat("hh:mm:ss").format(new Date()))
                );

                Wrapper runWrapper = new Wrapper(
                    outer.pluginDirectory,
                    projectDirectory, IJTSettingsState.getInstance().teamID,
                    projectField.getText(),
                    statusField.getText(),
                    taskStatusDropdown.getSelectedItem().toString()
                );
                runWrapper.run(outer::addLog);

                outer.uploadButton.setEnabled(true);
                return null;
            }
        };

        bgRunner.start();
    }

    private void handleUpdate () {
        MainPanel that = this;

        SwingWorker<Void> sw = new SwingWorker<Void>() {
            @Override
            public Void construct () {
                if (Setup.update(that::addLog, pluginDirectory)) {
                    that.addLog("UPDATE", "Completed update. \n\n");
                } else {
                    that.addLog("UPDATE", "Update failed. See information above. \n\n");
                }

                updateBackendButton.setEnabled(true);
                return null;
            }
        };

        sw.start();
    }

    public JPanel getContent () {
        return content;
    }
}
