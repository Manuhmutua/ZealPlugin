package myToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class MyToolWindow {
    private JPanel myToolWindowContent;

    MyToolWindow(ToolWindow toolWindow) {
        toolWindow.setTitle("GraphZeal");
    }

    JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
    }
}
