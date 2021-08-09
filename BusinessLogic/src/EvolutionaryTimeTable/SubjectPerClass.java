package EvolutionaryTimeTable;

import Base.SchoolClass;

import java.util.Objects;

public class SubjectPerClass {
    private SchoolClass schoolClass;
    private Integer subjectId;

    public SubjectPerClass(SchoolClass schoolClass, Integer subjectId) {
        this.schoolClass = schoolClass;
        this.subjectId = subjectId;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectPerClass that = (SubjectPerClass) o;
        return schoolClass.equals(that.schoolClass) && subjectId.equals(that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schoolClass, subjectId);
    }
}
