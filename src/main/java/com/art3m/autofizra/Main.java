package com.art3m.autofizra;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import api.myitmo.MyItmo;
import api.myitmo.MyItmoApi;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;

public class Main {
    

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        MyItmo myItmo = AuthService.auth(scanner);
        MyItmoApi api = myItmo.getApi();

        try {
            SportConsole sportConsole = new SportConsole(scanner);
            SportService sportService = new SportService(api);

            Long sportId = sportConsole.selectSport();
            List<SportSchedule> sportSchedules = sportService.getSportSchedules(sportId);
            List<SportLesson> sportLessons = sportConsole.selectSportLessons(sportSchedules);
            SportLesson sportLesson = sportConsole.selectSportLesson(sportLessons);

            if (sportLesson != null) {
                sportService.waitAndSignIn(sportId, sportLesson.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
