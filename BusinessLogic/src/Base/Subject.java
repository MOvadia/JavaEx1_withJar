package Base;

import generated.ETTSubject;

import java.util.Objects;

public class Subject {
    private final int id;
    private final String name;

    public Subject(ETTSubject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
    }

    public Subject(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id && name.equals(subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "id: " + id +
                " | name: " + name + '\n';
    }
}
