package ulrichbarnstedt.plug.ijt;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AnAction {
    @Override
    public void actionPerformed (@NotNull AnActionEvent e) {

        //BrowserUtil.browse("https://github.com/ulrich-barnstedt");

        //Notification notf = new Notification("testGroup", "test title", "test content", NotificationType.INFORMATION);
        //notf.notify(e.getProject());

    }
}
