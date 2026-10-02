package com.art3m.autofizra.gui;

import java.util.Arrays;
import java.util.List;

import com.art3m.autofizra.SportType;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;

import api.myitmo.MyItmoApi;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;

public class AppWindow extends BasicWindow {
    private final MultiWindowTextGUI gui;
    private MyItmoApi api;
    
    private SportType sportType;
    private List<SportSchedule> sportSchedules;
    private List<SportLesson> sportLessons;
    private SportLesson sportLesson;
    
    public AppWindow(MultiWindowTextGUI gui) {
        super("autofizra");
        this.gui = gui;

        setHints(Arrays.asList(Window.Hint.CENTERED));
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

}
