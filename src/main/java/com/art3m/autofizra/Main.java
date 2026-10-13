package com.art3m.autofizra;

import java.io.IOException;

import com.art3m.autofizra.gui.AppWindow;
import com.art3m.autofizra.gui.LoginPanel;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class Main {
    public static void main(String[] args) throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        terminal.setBackgroundColor(TextColor.ANSI.GREEN);
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        screen.clear();
        screen.refresh();

        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);

        AppWindow app = new AppWindow(gui);
        app.setScreen(new LoginPanel(app));
        gui.addWindowAndWait(app);

        screen.stopScreen();
    }
}
