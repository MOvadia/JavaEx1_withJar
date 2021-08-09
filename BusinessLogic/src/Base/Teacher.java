package Base;

import generated.ETTTeacher;
import generated.ETTTeaches;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Teacher {
    private final int id;
    private final String name;
    private Map<Integer, Base.Subject> subjects;

    public Teacher(ETTTeacher teacher, Map<Integer, Base.Subject> subjects) {
        this.id = teacher.getId();
        this.name = teacher.getETTName();
        this.subjects = new HashMap<>();

        for (ETTTeaches i: teacher.getETTTeaching().getETTTeaches())
        {
           Base.Subject newSubject = subjects.get(i.getSubjectId()) ;
           this.subjects.put(i.getSubjectId(),newSubject);
        }
    }

    public Teacher(Teacher teacher) {
        this.id = teacher.getId();
        this.subjects = teacher.getSubjects();
        this.name = teacher.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Base.Subject> getSubjects() {
        return subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id && name.equals(teacher.name) && Objects.equals(subjects, teacher.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subjects);
    }

    @Override
    public String toString() {
        return "id: " + id +
                "\n subjects: \n" + subjects +"\n";
    }

    public String toStringIdName() {
        return "id: " + id +
                " | name: " + name + '\n';
    }
}
