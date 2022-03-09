package ulrichbarnstedt.plug.ijt.ui;

import com.intellij.openapi.application.PathManager;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;

public class MainPanel {
    private JPanel content;
    private JButton uploadButton;
    private JTextArea logPane;
    private JBTextField projectField;
    private JBTextField statusField;

    public MainPanel () {
        uploadButton.addActionListener(e -> this.handleButton());

        projectField.getEmptyText().setText("Project");
        statusField.getEmptyText().setText("Status");
    }

    private void handleButton () {
        logPane.append(PathManager.getPluginsPath() + "/IJ_teams/\n");
        logPane.append(projectField.getText() + "/" + statusField.getText() + "\n");
    }

    public JPanel getContent () {
        return content;
    }
}
