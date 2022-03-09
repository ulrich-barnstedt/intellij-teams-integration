package ulrichbarnstedt.plug.ijt.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

public class IJTSettingsComponent {
    private final JPanel mainPanel;
    private final JBTextField idTextField = new JBTextField();

    public IJTSettingsComponent () {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Enter teams ID:"), idTextField, 1, false)
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
}
