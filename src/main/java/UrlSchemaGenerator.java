import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class UrlSchemaGenerator extends AnAction {

    private String[] packageNameAsArray;

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

        String packageName = readAndroidPackage(project.getBasePath());
        System.out.println("Package Name - " + packageName);

        if (graphQlUrl != null) {
            createGraphQlPackage(project, graphQlUrl, packageName.split("\\."));
        }
    }

    public String readAndroidPackage(String basePath) {
        try {
            File fXmlFile = new File(basePath + "/app/src/main/AndroidManifest.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("manifest");
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            String packageName = eElement.getAttribute("package");
            System.out.println("Package name : " + packageName);
            return packageName;
        } catch (SAXException saxexception) {
            saxexception.printStackTrace();
            return null;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        } catch (ParserConfigurationException parserConfigurationException) {
            parserConfigurationException.printStackTrace();
            return null;
        }
    }

    public void createGraphQlPackage(Project project, String graphQlUrl, String[] packageName) {
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
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", packageName[0])});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", packageName[0], packageName[1])});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", packageName[0], packageName[1], packageName[2])});
        cmds.add(new String[]{"mkdir", combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", packageName[0], packageName[1], packageName[2], "graphql")});

        try {
            for (String[] command : cmds) {
                new ProcessBuilder(command).start().waitFor();
            }

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder = processBuilder.directory(new File(combineDirectoryPath(project.getBasePath(), "app", "src", "main", "graphql", packageName[0], packageName[1], packageName[2], "graphql")));
            processBuilder.command(new String[]{"apollo", "schema:download", "--endpoint", graphQlUrl}).start().waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
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
