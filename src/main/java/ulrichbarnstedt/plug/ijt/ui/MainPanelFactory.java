package ulrichbarnstedt.plug.ijt.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class MainPanelFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent (@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MainPanel mp = new MainPanel(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mp.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
