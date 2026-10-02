package com.art3m.autofizra.gui;

import java.io.IOException;
import java.util.List;

import com.art3m.autofizra.SportService;
import com.art3m.autofizra.SportType;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import api.myitmo.model.sport.SportSchedule;

public class SportsPanel extends Panel {
    public SportsPanel(AppWindow app) {
        app.updateTitle("Виды спорта");

        for (SportType sport : SportType.values()) {
            Button button = new Button(sport.getName(), () -> {
                SportService sportService = new SportService(app.getApi());
                try {
                    List<SportSchedule> sportSchedules = sportService.getSportSchedules(sport.getId());

                    app.setSportSchedules(sportSchedules);
                    app.setScreen(new SchedulePanel(app));
                } catch (IOException e) {
                    e.printStackTrace();
                    MessageDialog.showMessageDialog(app.getGui(), "Ошибка", "Не удалось оплучить расписание");
                }
            });

            addComponent(button);
        }

        Button backButton = new Button("Назад", () -> {
            app.setScreen(new LoginPanel(app));
        });

        addComponent(new EmptySpace());
        addComponent(backButton);
    }
}
