package ulrichbarnstedt.plug.ijt.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IJSettingsConfigurable implements Configurable {
    private IJTSettingsComponent component;
    private IJTSettingsState state;

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName () {
        return "Intellij Teams Integration Settings";
    }

    @Override
    public @Nullable JComponent createComponent () {
        component = new IJTSettingsComponent();
        state = IJTSettingsState.getInstance();
        return component.getMainPanel();
    }

    @Override
    public boolean isModified () {
        return !state.teamID.equals(component.getIdTextField());
    }

    @Override
    public void apply () throws ConfigurationException {
        IJTSettingsState state = IJTSettingsState.getInstance();
        state.teamID = component.getIdTextField();
    }

    @Override
    public void reset () {
        IJTSettingsState state = IJTSettingsState.getInstance();
        component.setIdTextField(state.teamID);
    }

    @Override
    public void disposeUIResources () {
        component = null;
    }
}
