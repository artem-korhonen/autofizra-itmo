package com.art3m.autofizra.gui;

import com.art3m.autofizra.AuthService;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import api.myitmo.MyItmo;
import api.myitmo.MyItmoApi;

public class LoginPanel extends Panel {
    public LoginPanel(AppWindow app) {
        super(new GridLayout(2));

        app.updateTitle("Вход");
        GridLayout layout = (GridLayout) getLayoutManager();
        layout.setHorizontalSpacing(3);
        layout.setVerticalSpacing(1);

        addComponent(new Label("ИСУ:"));
        TextBox isuBox = new TextBox();
        addComponent(isuBox);

        addComponent(new Label("Пароль:"));
        TextBox passwordBox = new TextBox();
        passwordBox.setMask('*');
        addComponent(passwordBox);

        Button loginButton = new Button("Войти", () -> {
            String isu = isuBox.getText();
            String password = passwordBox.getText();

            try {
                MyItmo myItmo = AuthService.auth(isu, password);
                MyItmoApi api = myItmo.getApi();

                app.setApi(api);
                app.setScreen(new SportsPanel(app));
            } catch (RuntimeException e) {
                MessageDialog.showMessageDialog(app.getGui(), "Ошибка", "Неверный ИСУ или пароль");
            }
        });

        
        addComponent(loginButton);
    }
}
