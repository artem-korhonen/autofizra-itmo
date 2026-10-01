package com.art3m.autofizra;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import api.myitmo.MyItmo;
import api.myitmo.MyItmoApi;
import api.myitmo.model.ResultResponse;
import api.myitmo.model.sport.CanSignIn;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;
import retrofit2.Response;

public class Main {
    private static MyItmo auth(Scanner scanner) {
        MyItmo myItmo = new MyItmo();
        String isu = scanner.nextLine();
        String password = scanner.nextLine();

        myItmo.auth(isu, password);

        return myItmo;
    }

    private static SportType getSportById(Long id) {
        for (SportType sport : SportType.values()) {
            if (sport.getId() == id.intValue()) {
                return sport;
            }
        }

        return null;
    }

    private static Long selectSport(MyItmoApi api, Scanner scanner) throws IOException {
        for (SportType sport : SportType.values()) {
            System.out.println(
                    sport.getName() + " (" + sport.getId() + ")");
        }

        System.out.println();

        while (true) {
            String input = scanner.nextLine();

            Long inputSportId;

            try {
                inputSportId = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода SportId");
                continue;
            }

            SportType chosenSportType = getSportById(inputSportId);

            if (chosenSportType == null) {
                System.out.println("Неверный SportId");
                continue;
            }

            System.out.println(chosenSportType.getName() + " выбран");

            return inputSportId;
        }
    }

    private static List<SportSchedule> getSportSchedules(MyItmoApi api, Long sportId) throws IOException {
        ResultResponse<List<SportSchedule>> sportScheduleResponse = api
                .getSportSchedule(LocalDate.now(), LocalDate.now().plusDays(20), null, List.of(sportId), null).execute()
                .body();

        if (sportScheduleResponse == null || sportScheduleResponse.getErrorMessage() != null) {
            throw new RuntimeException(
                    sportScheduleResponse == null ? "Ответ 2 не получен" : sportScheduleResponse.getErrorMessage());
        }

        List<SportSchedule> sportScheduleResult = sportScheduleResponse.getResult();

        return sportScheduleResult;
    }

    private static void printSportSchedule(List<SportSchedule> sportSchedules) {
        for (SportSchedule sportSchedule : sportSchedules) {
            List<SportLesson> sportLessons = sportSchedule.getLessons();

            if (sportLessons != null) {
                System.out.println(sportSchedule.getDate().toString());

                for (SportLesson sportLesson : sportLessons) {
                    Long id = sportLesson.getId();
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
                                && unavailableReasons.contains("Выбрано 1 занятие в этот день")) {
                            sign = "? ";
                        } else if (unavailableReasons != null
                                && unavailableReasons.contains("Вы уже записаны на это занятие")) {
                            sign = "✅ ";
                        } else {
                            sign = "❌ ";
                        }
                    }

                    if (isCanSignIn || sign.equals("✅ ")) {
                        System.out.println(sign
                                + timeStart
                                + "-"
                                + timeEnd
                                + " - "
                                + teacherFio
                                + "("
                                + available.toString()
                                + "/"
                                + limit.toString()
                                + ") (lessonId="
                                + id.toString()
                                + ")"
                                + (dopsa ? " (dopsa)" : ""));
                    }
                }

                System.out.println();
            }
        }
    }

    private static void waitAndSignIn(
            MyItmoApi api,
            Scanner scanner,
            Long sportId) throws Exception {

        System.out.print("Введите lessonId для отслеживания: ");
        Long lessonId = Long.parseLong(scanner.nextLine());

        while (true) {
            List<SportSchedule> schedules = getSportSchedules(api, sportId);

            for (SportSchedule schedule : schedules) {
                if (schedule.getLessons() == null) {
                    continue;
                }

                for (SportLesson lesson : schedule.getLessons()) {
                    if (!((Long) lesson.getId()).equals(lessonId)) {
                        continue;
                    }

                    Long available = lesson.getAvailable();
                    CanSignIn canSignIn = lesson.getCanSignIn();

                    System.out.println(
                            LocalDateTime.now()
                                    + " | available=" + available
                                    + " | canSignIn=" + canSignIn.isCanSignIn());

                    if (available != null
                            && available > 0
                            && canSignIn.isCanSignIn()) {

                        System.out.println("Место появилось! Пытаемся записаться...");

                        Response<ResultResponse<List<Long>>> response = api.signInLessons(List.of(lessonId)).execute();

                        System.out.println("HTTP: " + response.code());

                        if (response.isSuccessful()) {
                            System.out.println("Успешно записались!");
                            return;
                        }

                        if (response.errorBody() != null) {
                            System.out.println(
                                    "Ошибка: " + response.errorBody().string());
                        }
                    }

                    break;
                }
            }

            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        MyItmo myItmo = auth(scanner);
        MyItmoApi api = myItmo.getApi();

        try {
            Long sportId = selectSport(api, scanner);
            List<SportSchedule> sportSchedules = getSportSchedules(api, sportId);

            printSportSchedule(sportSchedules);
            waitAndSignIn(api, scanner, sportId);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
