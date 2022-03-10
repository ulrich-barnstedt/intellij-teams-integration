package ulrichbarnstedt.plug.ijt.ui;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextField;
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
    private ConfirmPrompt userAgreement;
    private final Project currentProject;

    public MainPanel (Project project) {
        this.currentProject = project;

        uploadButton.addActionListener(e -> {
            this.uploadButton.setEnabled(false);
            this.handleButton();
            this.uploadButton.setEnabled(true);
        });

        projectField.getEmptyText().setText("Project");
        statusField.getEmptyText().setText("Status");
    }

    private void handleButton () {
        String assumedLocation = currentProject.getPresentableUrl();
        if (assumedLocation == null) {
            logPane.append("ERROR: Cannot start upload. Project directory could not be located. (Are you in a Intellij Project?)\n");
            return;
        }

        Path pluginDirectory = Paths.get(PathManager.getPluginsPath() + "/IJ_teams/backend");
        Path projectDirectory = Paths.get(assumedLocation);

        if (Files.notExists(pluginDirectory)) {
            if (userAgreement == null) {
                userAgreement = new ConfirmPrompt("First upload run",
                    "The files required for the plugin to run have not been downloaded yet, as this is the first upload being run.\nAbout 300mb of files will be downloaded.",
                    "Download", "Cancel");
            }

            if (userAgreement.query()) {
                Setup.run(logPane::append, pluginDirectory);
            } else return;
        }

        logPane.append(String.format("[IJ] Starting upload (%s)\n", new SimpleDateFormat("hh:mm:ss").format(new Date())));

        Wrapper runWrapper = new Wrapper(pluginDirectory, projectDirectory, IJTSettingsState.getInstance().teamID, projectField.getText(), statusField.getText());
        runWrapper.run(logPane::append);
    }

    public JPanel getContent () {
        return content;
    }
}
