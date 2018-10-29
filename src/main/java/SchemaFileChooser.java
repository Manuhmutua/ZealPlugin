import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

public class SchemaFileChooser extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        e.getPresentation().setEnabledAndVisible(project != null);
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        assert project != null;
        VirtualFile file = FileChooser.chooseFile(descriptor, project, project.getBaseDir());
        if (Objects.isNull(file)) {
            Messages.showInfoMessage("Cancel ", "Error");

        } else {
            ArrayList<String> cmds = new ArrayList<>();
            cmds.add("./gradlew");

            GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
            generalCommandLine.setCharset(Charset.forName("UTF-8"));
            generalCommandLine.setWorkDirectory(project.getBasePath());
            ProcessHandler processHandler = null;
            try {
                processHandler = new OSProcessHandler(generalCommandLine);
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }
            assert processHandler != null;
            processHandler.startNotify();
            String txt = file.getPath();
            Messages.showInfoMessage(txt, "File Name");
        }


    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
