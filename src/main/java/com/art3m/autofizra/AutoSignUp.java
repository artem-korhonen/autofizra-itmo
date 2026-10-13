package com.art3m.autofizra;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import api.myitmo.MyItmoApi;
import api.myitmo.model.ResultResponse;
import api.myitmo.model.sport.SportLesson;

public class AutoSignUp {
    private List<SportLesson> sportLessons = new CopyOnWriteArrayList<SportLesson>();
    private MyItmoApi api;
    private Thread worker;
    private Runnable onUpdate;

    public AutoSignUp(Runnable onUpdate) {
        this.onUpdate = onUpdate;
        
        this.worker = new Thread(this::loop);
        this.worker.setDaemon(true);
        this.worker.start();
    }

    public List<SportLesson> getSportLessons() {
        return this.sportLessons;
    }

    public void setSportLessons(List<SportLesson> sportLessons) {
        this.sportLessons = sportLessons;
    }

    public void addSportLesson(SportLesson sportLesson) {
        this.sportLessons.add(sportLesson);
        
        synchronized (this) {
            notifyAll();
        }
    }

    public void removeSportLesson(SportLesson sportLesson) {
        this.sportLessons.remove(sportLesson);
    }

    private void autoRemoveSportLessons(List<Long> sportLessonsIds) {
        boolean changed = sportLessons.removeIf(sl -> sportLessonsIds.contains(sl.getId()));

        if (changed && onUpdate != null) {
            onUpdate.run();
        }
    }

    public void clearSportLessons() {
        this.sportLessons.clear();
    }

    private void loop() {
        while (true) {
            try {
                synchronized (this) {
                    while (sportLessons.isEmpty()) {
                        wait();
                    }
                }

                List<Long> sportLessonsIds = this.sportLessons.stream().map(SportLesson::getId).toList();
                ResultResponse<List<Long>> response = this.api.signInLessons(sportLessonsIds).execute().body();

                if (response != null && response.getResult() != null) {
                    List<Long> result = response.getResult();
                    autoRemoveSportLessons(result);
                }

                Thread.sleep(1000);
            } catch (IOException e) {
                continue;
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public MyItmoApi getApi() {
        return this.api;
    }

    public void setApi(MyItmoApi api) {
        this.api = api;
    }

    public Thread getWorker() {
        return this.worker;
    }

    public void setWorker(Thread worker) {
        this.worker = worker;
    }

    public Runnable getOnUpdate() {
        return this.onUpdate;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }
}