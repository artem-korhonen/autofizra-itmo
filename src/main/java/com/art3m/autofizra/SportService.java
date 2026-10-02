package com.art3m.autofizra;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import api.myitmo.MyItmoApi;
import api.myitmo.model.ResultResponse;
import api.myitmo.model.sport.CanSignIn;
import api.myitmo.model.sport.SportLesson;
import api.myitmo.model.sport.SportSchedule;
import retrofit2.Response;

public class SportService {
    MyItmoApi api;

    public SportService(MyItmoApi api) {
        this.api = api;
    }

    public List<SportSchedule> getSportSchedules(Long sportId) throws IOException {
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

    public void waitAndSignIn(Long sportId, Long lessonId) throws Exception {
        while (true) {
            List<SportSchedule> schedules = getSportSchedules(sportId);

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
                    String[] datetime = LocalDateTime.now().toString().split("T");

                    System.out.println(
                            datetime[0]
                                    + " | "
                                    + datetime[1]
                                    + " | available="
                                    + available
                                    + " | canSignIn="
                                    + canSignIn.isCanSignIn());

                    if (available != null
                            && available > 0
                            && canSignIn.isCanSignIn()) {

                        System.out.println("Место появилось! Пытаемся записаться...");

                        Response<ResultResponse<List<Long>>> response = api.signInLessons(List.of(lessonId)).execute();

                        if (response.isSuccessful()) {
                            System.out.println("Успешно записались!");
                            return;
                        }

                        if (response.errorBody() != null) {
                            System.out.println("Ошибка: " + response.errorBody().string());
                        }
                    }

                    break;
                }
            }

            Thread.sleep(1000);
        }
    }
}
