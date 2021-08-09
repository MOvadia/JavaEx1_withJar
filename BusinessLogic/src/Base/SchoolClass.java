package Base;

import generated.ETTClass;
import generated.ETTStudy;

import java.util.*;

public class SchoolClass {
    private final int id;
    private final String name;
    private List<Study> studies = new ArrayList<>();

    public SchoolClass(ETTClass ettClass, Map<Integer,Subject> subjects) {
        this.id = ettClass.getId();
        this.name = ettClass.getETTName();
        for (ETTStudy i: ettClass.getETTRequirements().getETTStudy()){
            Study newStudy = new Study(i.getHours(),subjects.get(i.getSubjectId()),i.getSubjectId());
            studies.add(newStudy);
        }
    }

    public SchoolClass(SchoolClass schoolClass) {
        this.id = schoolClass.getId();
        this.name = schoolClass.getName();
        this.studies = schoolClass.getStudies();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Study> getStudies() {
        return studies;
    }

    public Map<Integer,Subject> getSubjects()
    {
        Map<Integer,Subject> subjects= new HashMap<>();
        for (Study study: studies) {
            subjects.put(study.getSubjectID(),study.getSubject());
        }
        return subjects;
    }

    public int sumStudyHours()
    {
        int sum = 0;
        for (Study study: studies) {
            sum += study.getHours();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolClass aClass = (SchoolClass) o;
        return id == aClass.id && name.equals(aClass.name) && Objects.equals(studies, aClass.studies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studies);
    }

    @Override
    public String toString() {
        return "id: " + id +
                "\n" + studies + '\n';
    }

    public String toStringIdName() {
        return "id: " + id +
                " | name: " + name + '\n';
    }
}
