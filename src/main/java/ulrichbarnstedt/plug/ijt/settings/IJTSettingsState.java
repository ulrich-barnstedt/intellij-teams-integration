package ulrichbarnstedt.plug.ijt.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "ulrichbarnstedt.plug.ijt.settings.TestSetting",
    storages = @Storage("IJTeamsSettings.xml")
)
public class IJTSettingsState implements PersistentStateComponent<IJTSettingsState> {
    public String teamID = "";
    public String repositoryName = "";

    @Override
    public @Nullable IJTSettingsState getState () {
        return this;
    }

    public static IJTSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(IJTSettingsState.class);
    }

    @Override
    public void loadState (@NotNull IJTSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
