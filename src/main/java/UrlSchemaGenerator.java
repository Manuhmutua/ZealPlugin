import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class UrlSchemaGenerator extends AnAction {
    public UrlSchemaGenerator() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String txt= Messages.showInputDialog(project, "Enter Server URL", "Enter Server URL", Messages.getQuestionIcon());
        Messages.showMessageDialog(project, "Hello, We got " + txt + " From you!\n I am glad ou are using GraphZeal", "Information", Messages.getInformationIcon());
    }
}
