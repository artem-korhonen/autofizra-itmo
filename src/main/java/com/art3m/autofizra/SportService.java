package com.art3m.autofizra;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import api.myitmo.MyItmoApi;
import api.myitmo.model.ResultResponse;
import api.myitmo.model.sport.SportSchedule;

public class SportService {
    public static List<SportSchedule> getSportSchedules(MyItmoApi api, Long sportId) throws IOException {
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
}
