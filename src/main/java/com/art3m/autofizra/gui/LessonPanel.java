package com.art3m.autofizra.gui;

import java.time.LocalDateTime;
import java.util.List;

import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

import api.myitmo.model.ResultResponse;
import api.myitmo.model.sport.CanSignIn;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;
import okio.Buffer;

public class LessonPanel extends Panel {
    public LessonPanel(AppWindow app) {
        app.updateTitle("Занятие");

        SportLesson sportLesson = app.getSportLesson();

        CanSignIn canSignIn = sportLesson.getCanSignIn();
        Boolean isCanSignIn = canSignIn.isCanSignIn();
        String timeStart = sportLesson.getTimeSlotStart();
        String timeEnd = sportLesson.getTimeSlotEnd();
        String teacherFio = sportLesson.getTeacherFio();
        Long available = sportLesson.getAvailable();
        Long limit = sportLesson.getLimit();
        Boolean dopsa = sportLesson.getTypeId() == 5L;

        Label label = new Label(String.format("%s\n%s\n%s-%s\nДоступно %s из %s",
                dopsa ? "[допса]" : "",
                teacherFio,
                timeStart,
                timeEnd,
                available,
                limit));
        addComponent(label);

        Button autoButton = new Button("Автозапись", () -> {
            
        });

        Button backButton = new Button("Назад", () -> {
            app.setComponent(new LessonsPanel(app));
        });

        addComponent(new EmptySpace());
        addComponent(backButton);
    }
}
