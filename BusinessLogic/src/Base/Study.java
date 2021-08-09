package Base;

import java.util.Objects;

public class Study {
    private final int hours;
    private final Subject subject;
    private int subjectID;

    public Study(int hours, Subject subject, int subjectId) {
        this.hours = hours;
        this.subject = subject;
        this.subjectID = subjectId;
    }

    public Study(Study study) {
        this.hours = study.getHours();
        this.subject = new Subject(study.getSubject());
        this.subjectID = study.getSubjectID();
    }

    public int getHours() {
        return hours;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getSubjectID() {
        return subjectID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Study study = (Study) o;
        return hours == study.hours && Objects.equals(subject, study.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, subject);
    }

    @Override
    public String toString() {
        return "studies:\n" +
                "hours: " + hours +
                " | subject=" + subject;
    }
}
