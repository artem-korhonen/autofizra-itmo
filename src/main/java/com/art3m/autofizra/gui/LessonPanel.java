package com.art3m.autofizra.gui;

import com.art3m.autofizra.AutoSignUp;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;

import api.myitmo.model.sport.SportLesson;

public class LessonPanel extends Panel {
    private Button autoButton;
    
    public LessonPanel(AppWindow app) {
        super(new GridLayout(2));

        app.updateTitle("Занятие");
        GridLayout layout = (GridLayout) getLayoutManager();
        layout.setHorizontalSpacing(3);
        layout.setVerticalSpacing(1);

        SportLesson sportLesson = app.getSportLesson();
        AutoSignUp autoSignUp = app.getAutoSignUp();

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

        autoButton = new Button(
                autoSignUp.getSportLessons().contains(sportLesson)
                        ? "Убрать из очереди"
                        : "Добавить в очередь",
                () -> {
                    if (autoSignUp.getSportLessons().contains(sportLesson)) {
                        autoSignUp.removeSportLesson(sportLesson);
                        autoButton.setLabel("Добавить в очередь");
                    } else {
                        autoSignUp.addSportLesson(sportLesson);
                        autoButton.setLabel("Убрать из очереди");
                    }
                }
        );

        Button backButton = new Button("Назад", () -> {
            app.setComponent(new LessonsPanel(app));
        });
        
        addComponent(new EmptySpace());
        addComponent(autoButton);
        addComponent(backButton);
    }
}
