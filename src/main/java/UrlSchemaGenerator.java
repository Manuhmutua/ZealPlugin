import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UrlSchemaGenerator extends AnAction {
    public UrlSchemaGenerator() {
        super("Hello");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);

        if (project.getBasePath() == null) {
            Messages.showErrorDialog("Unable to find project", "Error");
            return;
        }

        String graphQlUrl = Messages.showInputDialog(project, "Enter target GraphQl server URL", "Enter Server URL", Messages.getQuestionIcon());

        if (graphQlUrl != null && !graphQlUrl.startsWith("http")) {
            Messages.showErrorDialog("The URL provided should start with \"http://\" or \"https://\"", "Invalid URL");
            return;
        }

        if (graphQlUrl != null) {
            createGraphQlPackage(project, graphQlUrl);
        }
    }

    public void createGraphQlPackage(Project project, String graphQlUrl) {

        ArrayList<String[]> cmds = new ArrayList<>();

        if (!directoryExists(combineDirectoryPath(project.getBasePath(), "app"))) {
            throwDirectoryMissingError(combineDirectoryPath(project.getBasePath(), "app"));
            return;
        }

        if (!directoryExists(combineDirectoryPath(project.getBasePath(), "app", "src"))) {
            throwDirectoryMissingError(combineDirectoryPath(project.getBasePath(), "app", "src"));
            return;
        }

        if (!directoryExists(combineDirectoryPath(project.getBasePath(), "app", "src", "main"))) {
            throwDirectoryMissingError(combineDirectoryPath(project.getBasePath(), "app", "src", "main"));
            return;
        }

        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql")});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", "com")});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", "com", "sample")});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", "com", "sample", "app")});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", "com", "sample", "app", "graphql")});

        try {
            for (String[] command : cmds) {
                new ProcessBuilder(command).start();
            }

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder = processBuilder.directory(new File(combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", "com", "sample", "app", "graphql")));
            processBuilder.command(new String[]{"apollo", "schema:download", "--endpoint", graphQlUrl}).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean directoryExists(String path) {
        File dir = new File(path);
        return dir.exists();
    }

    public String combineDirectoryPath(String parent, String... paths) {
        Path resolvedPath = Paths.get(parent, paths);
        assert resolvedPath != null;
        return resolvedPath.toString();
    }

    public void throwDirectoryMissingError(String directoryName) {
        Messages.showErrorDialog(String.format("Unable to find directory %s", directoryName), "Error");
    }
}
