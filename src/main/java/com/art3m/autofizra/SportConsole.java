package com.art3m.autofizra;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import api.myitmo.model.sport.CanSignIn;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;

public class SportConsole {
    private Scanner scanner;

    public SportConsole(Scanner scanner) {
        this.scanner = scanner;
    }

    public long selectSport() {
        for (SportType sport : SportType.values()) {
            System.out.println(
                    sport.getName() + " (" + sport.getId() + ")");
        }

        System.out.println();

        while (true) {
            System.out.print("Введи SportId: ");
            String input = this.scanner.nextLine();

            Long inputSportId;

            try {
                inputSportId = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода SportId");
                continue;
            }

            SportType chosenSportType = SportType.getSportById(inputSportId.intValue());

            if (chosenSportType == null) {
                System.out.println("Неверный SportId");
                continue;
            }

            System.out.println(chosenSportType.getName() + " выбран\n");

            return inputSportId;
        }
    }

    public List<SportLesson> selectSportLessons(List<SportSchedule> sportSchedules) {
        Integer counter = 0;

        for (SportSchedule sportSchedule : sportSchedules) {
            LocalDate date = sportSchedule.getDate();
            counter += 1;

            WeekDay dayOfWeek = WeekDay.valueOf(date.getDayOfWeek().name());

            System.out.println(date.toString() + " " + dayOfWeek.getName() + " (" + counter.toString() + ")");
        }

        System.out.println();

        while (true) {
            System.out.print("Введи DateId: ");
            String input = this.scanner.nextLine();

            int inputDateId;

            try {
                inputDateId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода DateId");
                continue;
            }

            if (inputDateId > sportSchedules.size() || inputDateId < 1) {
                System.out.println("Неверный DateId");
                continue;
            }

            SportSchedule choosedSportSchedule = sportSchedules.get(inputDateId - 1);
            LocalDate sportScheduleDate = choosedSportSchedule.getDate();

            System.out
                    .println("Выбрана дата - " + sportScheduleDate.toString() + " " + sportScheduleDate.getDayOfWeek() + "\n");

            return choosedSportSchedule.getLessons();
        }
    }

    public SportLesson selectSportLesson(List<SportLesson> sportLessons) {
        Integer counter = 0;

        if (sportLessons == null) {
            System.out.println("На этот день записей нет");
            return null;
        }

        for (SportLesson sportLesson : sportLessons) {
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
                    sign = "🔀 ";
                }
            } else {
                List<String> unavailableReasons = sportLesson.getCanSignIn().getUnavailableReasons();
                if (unavailableReasons != null
                        && unavailableReasons.contains("Вы уже записаны на это занятие")) {
                    sign = "✅ ";
                } else {
                    sign = "❌ ";
                }
            }

            counter += 1;
            System.out.println(sign
                    + timeStart
                    + "-"
                    + timeEnd
                    + " - "
                    + teacherFio
                    + " ("
                    + available.toString()
                    + "/"
                    + limit.toString()
                    + ")"
                    + (dopsa ? " (dopsa)" : "")
                    + " ("
                    + counter.toString()
                    + ")"
                    + (isCanSignIn ? "" : " (" + canSignIn.getUnavailableReasons().get(0) + ")")
                );
        }

        System.out.println();

        while (true) {
            System.out.print("Введи LessonId: ");
            String input = this.scanner.nextLine();

            int inputLessonId;

            try {
                inputLessonId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода LessonId");
                continue;
            }

            if (inputLessonId > sportLessons.size() || inputLessonId < 1) {
                System.out.println("Неверный DateId");
                continue;
            }

            SportLesson choosedSportLesson = sportLessons.get(inputLessonId - 1);

            return choosedSportLesson;
        }
    }
}
