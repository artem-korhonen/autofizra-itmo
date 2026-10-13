package com.art3m.autofizra.gui;

import com.art3m.autofizra.SportType;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;

public class SportsPanel extends Panel {
    public SportsPanel(AppWindow app) {
        app.updateTitle("Виды спорта");

        for (SportType sport : SportType.values()) {
            Button button = new Button(sport.getName(), () -> {
                app.setSportType(sport);
                app.updateSportSchedule();
                app.setScreen(new SchedulePanel(app));
            });

            addComponent(button);
        }

        Button backButton = new Button("Назад", () -> {
            app.setScreen(new LoginPanel(app));
            app.getAutoSignUp().clearSportLessons();
        });

        addComponent(new EmptySpace());
        addComponent(backButton);
    }
}
