package ulrichbarnstedt.plug.ijt.ui;

import java.awt.*;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBTextField;
import com.intellij.uiDesigner.core.*;
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
    private final Project currentProject;
    private final Path pluginDirectory;

    public MainPanel (Project project) {
        initComponents();
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
                "The files required for the plugin to run have not been downloaded yet, as this is the first upload being run.\nAbout 300mb of files will be downloaded. This might take a while depending on your machine.",
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

    // ------------------------- JFD plugin generations

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        content = new JPanel();
        projectField = new JBTextField();
        statusSeperator = new JLabel();
        statusField = new JBTextField();
        uploadButton = new JButton();
        taskStatusLabel = new JLabel();
        taskStatusArrowLabel = new JLabel();
        taskStatusDropdown = new JComboBox<>();
        spacerLabel = new JLabel();
        logLabel = new JLabel();
        scrollPane = new JScrollPane();
        logPane = new JTextArea();
        updateBackendButton = new JButton();

        //======== content ========
        {
            content.setLayout(new GridLayoutManager(6, 4, new Insets(5, 5, 5, 5), -1, -1));
            content.add(projectField, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //---- statusSeperator ----
            statusSeperator.setText("/");
            content.add(statusSeperator, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));
            content.add(statusField, new GridConstraints(0, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //---- uploadButton ----
            uploadButton.setText("Upload");
            content.add(uploadButton, new GridConstraints(0, 3, 2, 1,
                GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //---- taskStatusLabel ----
            taskStatusLabel.setText("Task Status (Document)");
            content.add(taskStatusLabel, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //---- taskStatusArrowLabel ----
            taskStatusArrowLabel.setText("->");
            content.add(taskStatusArrowLabel, new GridConstraints(1, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //---- taskStatusDropdown ----
            taskStatusDropdown.setModel(new DefaultComboBoxModel<>(new String[] {
                "Done",
                "InProgress"
            }));
            content.add(taskStatusDropdown, new GridConstraints(1, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //---- spacerLabel ----
            spacerLabel.setText(" ");
            content.add(spacerLabel, new GridConstraints(2, 0, 1, 4,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- logLabel ----
            logLabel.setText("Log");
            content.add(logLabel, new GridConstraints(3, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));

            //======== scrollPane ========
            {

                //---- logPane ----
                logPane.setEditable(false);
                scrollPane.setViewportView(logPane);
            }
            content.add(scrollPane, new GridConstraints(4, 0, 1, 4,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                null, null, null));

            //---- updateBackendButton ----
            updateBackendButton.setText("Update");
            content.add(updateBackendButton, new GridConstraints(5, 3, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                null, null, null));
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel content;
    private JBTextField projectField;
    private JLabel statusSeperator;
    private JBTextField statusField;
    private JButton uploadButton;
    private JLabel taskStatusLabel;
    private JLabel taskStatusArrowLabel;
    private JComboBox<String> taskStatusDropdown;
    private JLabel spacerLabel;
    private JLabel logLabel;
    private JScrollPane scrollPane;
    private JTextArea logPane;
    private JButton updateBackendButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
