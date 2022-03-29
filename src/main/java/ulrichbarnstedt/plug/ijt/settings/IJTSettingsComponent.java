package ulrichbarnstedt.plug.ijt.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class IJTSettingsComponent {
    private final JPanel mainPanel;
    private final JBTextField idTextField = new JBTextField();
    private final JBTextField repoTextField = new JBTextField();

    public IJTSettingsComponent () {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Teams ID:"), idTextField, 1, false)
            .addLabeledComponent(new JBLabel("Repository name: (<full-repo-name>/src/<master-branch>/POS3)"), repoTextField, 1, true)
            .getPanel();
    }

    public JPanel getMainPanel () {
        return mainPanel;
    }

    public String getIdTextField () {
        return idTextField.getText();
    }

    public void setIdTextField (String ID) {
        idTextField.setText(ID);
    }

    public String getRepoTextField () {
        return repoTextField.getText();
    }

    public void setRepoTextField (String name) {
        repoTextField.setText(name);
    }
}
