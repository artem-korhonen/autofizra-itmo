package com.art3m.autofizra.gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.art3m.autofizra.AutoSignUp;
import com.art3m.autofizra.SportService;
import com.art3m.autofizra.SportType;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;

import api.myitmo.MyItmoApi;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;

public class AppWindow extends BasicWindow {
    private final MultiWindowTextGUI gui;
    private MyItmoApi api;
    
    private SportType sportType;
    private List<SportSchedule> sportSchedules;
    private LocalDate date;
    private List<SportLesson> sportLessons;
    private SportLesson sportLesson;
    private AutoSignUp autoSignUp;
    
    public AppWindow(MultiWindowTextGUI gui) {
        super("autofizra");
        this.gui = gui;

        setHints(Arrays.asList(Window.Hint.CENTERED));

        this.autoSignUp = new AutoSignUp(this::updateCurrentPanel);
    }

    public void updateTitle(String title) {
        super.setTitle(title);
    }

    public void setScreen(Component screen) {
        setComponent(screen);
    }

    public MultiWindowTextGUI getGui() {
        return this.gui;
    }

    public MyItmoApi getApi() {
        return this.api;
    }

    public void setApi(MyItmoApi api) {
        this.api = api;
        this.autoSignUp.setApi(api);
    }

    public SportType getSportType() {
        return this.sportType;
    }

    public void setSportType(SportType sportType) {
        this.sportType = sportType;
    }

    public List<SportSchedule> getSportSchedules() {
        return this.sportSchedules;
    }

    public void setSportSchedules(List<SportSchedule> sportSchedules) {
        this.sportSchedules = sportSchedules;
    }

    public List<SportLesson> getSportLessons() {
        return this.sportLessons;
    }

    public void setSportLessons(List<SportLesson> sportLessons) {
        this.sportLessons = sportLessons;
    }

    public SportLesson getSportLesson() {
        return this.sportLesson;
    }

    public void setSportLesson(SportLesson sportLesson) {
        this.sportLesson = sportLesson;
    }

    public AutoSignUp getAutoSignUp() {
        return this.autoSignUp;
    }

    public void setAutoSignUp(AutoSignUp autoSignUp) {
        this.autoSignUp = autoSignUp;
    }

    public void updateCurrentPanel() {
        this.gui.getGUIThread().invokeLater(() -> {
            Component current = getComponent();

            if (current instanceof LessonPanel || current instanceof LessonsPanel) {
                updateSportSchedule();
                this.setScreen(new LessonsPanel(this));
            }
        });
    }

    public void updateSportSchedule() {
        try {
            List<SportSchedule> sportSchedules = SportService.getSportSchedules(this.api, this.sportType.getId());
            this.setSportSchedules(sportSchedules);
            
            for (SportSchedule sportSchedule: sportSchedules) {
                if (sportSchedule.getDate().equals(this.date)) {
                    this.setSportLessons(sportSchedule.getLessons());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            MessageDialog.showMessageDialog(this.gui, "Ошибка", "Не удалось получить расписание");
        }
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}