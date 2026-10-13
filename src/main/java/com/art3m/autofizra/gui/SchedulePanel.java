package com.art3m.autofizra.gui;

import java.time.LocalDate;
import java.util.List;

import com.art3m.autofizra.WeekDay;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;

public class SchedulePanel extends Panel {
    public SchedulePanel(AppWindow app) {
        app.updateTitle("Расписание - " + app.getSportType().getName());

        for (SportSchedule sportSchedule: app.getSportSchedules()) {
            LocalDate date = sportSchedule.getDate();
            WeekDay dayOfWeek = WeekDay.valueOf(date.getDayOfWeek().name());

            Button button = new Button(date.toString() + " " + dayOfWeek.getName(), () -> {
                List<SportLesson> sportLessons = sportSchedule.getLessons();

                if (sportLessons == null || sportLessons.size() == 0) {
                    MessageDialog.showMessageDialog(app.getGui(), "Ошибка", "Занятий в этот день не найдено");
                } else {
                    app.setDate(date);
                    app.setSportLessons(sportLessons);
                    app.setScreen(new LessonsPanel(app));
                }
            });

            addComponent(button);
        }

        Button backButton = new Button("Назад", () -> {
            app.setScreen(new SportsPanel(app));;
        });

        addComponent(new EmptySpace());
        addComponent(backButton);
    }
}
