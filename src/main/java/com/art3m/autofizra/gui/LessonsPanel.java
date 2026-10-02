package com.art3m.autofizra.gui;

import java.util.List;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import api.myitmo.model.sport.CanSignIn;
import api.myitmo.model.sport.SportLesson;

public class LessonsPanel extends Panel {
    public LessonsPanel(AppWindow app) {
        app.updateTitle("Занятия");

        for (SportLesson sportLesson: app.getSportLessons()) {
            CanSignIn canSignIn = sportLesson.getCanSignIn();
            Boolean isCanSignIn = canSignIn.isCanSignIn();
            String timeStart = sportLesson.getTimeSlotStart();
            String timeEnd = sportLesson.getTimeSlotEnd();
            String teacherFio = sportLesson.getTeacherFio();
            Long available = sportLesson.getAvailable();
            Long limit = sportLesson.getLimit();
            Boolean intersection = sportLesson.getIntersection();
            String sign = "";
            Boolean dopsa = sportLesson.getTypeId() == 5L;

            if (isCanSignIn) {
                if (intersection) {
                    sign = "[<->] ";
                }
            } else {
                List<String> unavailableReasons = sportLesson.getCanSignIn().getUnavailableReasons();
                if (unavailableReasons != null
                        && unavailableReasons.contains("Вы уже записаны на это занятие")) {
                    sign = "[V] ";
                } else {
                    sign = "[X] ";
                }
            }

            String label = String.format("%s %s-%s — %s (%s/%s)%s",
                    sign,
                    timeStart,
                    timeEnd,
                    teacherFio,
                    available,
                    limit,
                    dopsa ? " [допса]" : ""
            );

            Button button = new Button(label, () -> {
                if (!isCanSignIn) {
                    MessageDialog.showMessageDialog(app.getGui(), "Ошибка", canSignIn.getUnavailableReasons().get(0));
                } else {
                    app.setSportLesson(sportLesson);
                    app.setScreen(new LessonPanel(app));
                }
            });
            
            addComponent(button);
        }

        Button backButton = new Button("Назад", () -> {
            app.setScreen(new SchedulePanel(app));
        });

        addComponent(new EmptySpace());
        addComponent(backButton);
    }
}
