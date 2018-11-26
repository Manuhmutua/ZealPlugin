package manuh.graphzeal.myToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class MyToolWindow {
    private JPanel myToolWindowContent;
    public String url;

    public MyToolWindow(ToolWindow toolWindow) {
        toolWindow.setTitle("GraphZeal");
    }

    JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
    }
}
