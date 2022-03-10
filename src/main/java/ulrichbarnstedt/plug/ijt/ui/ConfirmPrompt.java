package ulrichbarnstedt.plug.ijt.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ex.MultiLineLabel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ConfirmPrompt extends DialogWrapper {
    String content;

    public ConfirmPrompt (String title, String content, String confirm, String cancel) {
        super(true);
        this.content = content;

        this.setTitle(title);
        this.setOKButtonText(confirm);
        this.setCancelButtonText(cancel);

        this.init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel () {
        JPanel contentPanel = new JPanel(new BorderLayout());

        MultiLineLabel content = new MultiLineLabel(this.content);
        contentPanel.add(content);

        return contentPanel;
    }

    public boolean query () {
        return this.showAndGet();
    }
}
