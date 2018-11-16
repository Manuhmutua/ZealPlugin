package myToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class MyToolWindow {
    private JPanel myToolWindowContent;

    public MyToolWindow(ToolWindow toolWindow) {
        toolWindow.setTitle("GraphZeal");
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
    }
}
