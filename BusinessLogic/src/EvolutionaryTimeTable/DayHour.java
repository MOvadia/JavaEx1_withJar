package EvolutionaryTimeTable;

import java.util.Objects;

public class DayHour {
    private int day;
    private int hour;

    public DayHour(int day, int hour) {
        this.day = day;
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayHour dayHour = (DayHour) o;
        return day == dayHour.day && hour == dayHour.hour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour);
    }
}
