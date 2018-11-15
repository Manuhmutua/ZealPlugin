package myToolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class MyToolWindow {
    private JPanel myToolWindowContent;
    private JTextField welcomeToGraphZealPluginTextField;

    public MyToolWindow(ToolWindow toolWindow) {
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

}
