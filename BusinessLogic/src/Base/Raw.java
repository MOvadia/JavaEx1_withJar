package Base;

import java.util.Objects;

public class Raw {
    private Integer day;
    private Integer hour;
    private Teacher teacher;
    private Subject subject;
    private SchoolClass schoolClass;

    public Raw(Integer day, Integer hour, Teacher teacher, Subject subject, SchoolClass schoolClass) {
        this.day = day;
        this.hour = hour;
        this.teacher = teacher;
        this.subject = subject;
        this.schoolClass = schoolClass;
    }

    public Raw(Raw raw) {
        this.day = raw.getDay();
        this.hour = raw.getHour();
        this.teacher = new Teacher(raw.getTeacher());
        this.subject = new Subject(raw.getSubject());
        this.schoolClass = new SchoolClass(raw.getSchoolClass());
    }

    public Integer getDay() {
        return day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public int getTeacherID() {
        return teacher.getId();
    }

    public int getSubjectID() {
        return subject.getId();
    }

    public Subject getSubject() {
        return subject;
    }

    public int getSchoolClassID() {
        return schoolClass.getId();
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raw raw = (Raw) o;
        return day == raw.day && hour == raw.hour && teacher.equals(raw.teacher) && subject.equals(raw.subject) && schoolClass.equals(raw.schoolClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, teacher, subject, schoolClass);
    }

    @Override
    public String toString() {
        return "<" +
                "day=" + day +
                ", hour=" + hour +
                "\nTeacher=" + teacher +
                "\nSubject=" + subject +
                "\nSchoolClass=" + schoolClass +
                '}' + "\n";
    }
}
