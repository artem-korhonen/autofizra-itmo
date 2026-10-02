package com.art3m.autofizra;

public enum SportType {
    SPORT_TURIZM("Спортивный туризм", 36),
    BADMINTON("Бадминтон", 6),
    AFK_LFK("АФК и ЛФК", 2),
    BILLIARD("Бильярд", 9),
    CHECKERS("Шашки", 45),
    CHESS("Шахматы", 44),
    FITNESS("Фитнес", 39);

    private final String name;
    private final long id;

    SportType(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public static SportType getSportById(long id) {
        for (SportType sport : SportType.values()) {
            if (sport.getId() == id) {
                return sport;
            }
        }

        return null;
    }
}