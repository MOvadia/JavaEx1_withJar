package Base;

import Exceptions.DuplicateValException;
import Exceptions.IDSequenceException;
import Exceptions.NotExistValException;
import Exceptions.OutOfRangeException;
import generated.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class TimeTable {
    private int days;
    private int hours;
    private final int HARD_RULES_WEIGHT;
    private Map<Integer, Teacher> teachers = new HashMap<>();
    private Map<Integer, Subject> subjects = new HashMap<>();
    private Map<Integer, SchoolClass> classes = new HashMap<>();
    private Map<Rule.Id, Rule> rules = new HashMap<>();
    private Random rnd = new Random();

    public TimeTable(ETTDescriptor descriptor) {
        this.days = descriptor.getETTTimeTable().getDays();
        this.hours = descriptor.getETTTimeTable().getHours();
        this.HARD_RULES_WEIGHT = descriptor.getETTTimeTable().getETTRules().getHardRulesWeight();
        checkAndInsertSubjects(descriptor.getETTTimeTable().getETTSubjects().getETTSubject());
        checkAndInsertTeachers(descriptor.getETTTimeTable().getETTTeachers().getETTTeacher());
        checkAndInsertClasses(descriptor.getETTTimeTable().getETTClasses().getETTClass());
        checkAndInsertRules(descriptor.getETTTimeTable().getETTRules().getETTRule());
    }

    public int getHardRulesWeight() {
        return HARD_RULES_WEIGHT;
    }

    public Map<Integer, Teacher> getTeachers() {
        return teachers;
    }

    public Map<Integer, Subject> getSubjects() {
        return subjects;
    }

    public Map<Integer, SchoolClass> getClasses() {
        return classes;
    }

    public Map<Rule.Id, Rule> getRules() {
        return rules;
    }

    public Teacher getTeacherByKey(Integer key) {
        return teachers.get(key);
    }

    public SchoolClass getClassByKey(Integer key) {
        return classes.get(key);
    }

    public Subject getSubjectByKey(Integer key) {
        return subjects.get(key);
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public int getMaxCT()
    {
        if (this.teachers.size()>=this.classes.size())
        {
            return this.teachers.size();
        }
        else
        {
            return this.classes.size();
        }
    }

    public int getMinCT()
    {
        if (this.teachers.size()<=this.classes.size())
        {
            return this.teachers.size();
        }
        else
        {
            return this.classes.size();
        }
    }

    public void subjectsAreExist(List<Integer> subjectsToCheck) throws IDSequenceException {
        for (int i : subjectsToCheck) {
            if(!this.subjects.containsKey(i))
            {
                throw new NotExistValException(i, ETTSubjects.class);
            }
        }
    }

    public void checkIDSequence(List<Integer> toCheck, Class className) throws IDSequenceException {

        for (int i = 1; i<= toCheck.size(); i++) {
            if (!toCheck.contains(i))
            {
                throw new IDSequenceException(i, className);
            }
        }
    }

    private void checkAndInsertRules(List<ETTRule> rule) throws DuplicateValException {
        for (ETTRule i : rule) {
            if (this.rules.containsKey(Rule.Id.valueOf(i.getETTRuleId()))) {
                throw new DuplicateValException(i.getETTRuleId(), Rule.class);
            } else {
                Rule newRule = new Rule(i);
                this.rules.put(newRule.getId(), newRule);
            }
        }
    }


    private void checkAndInsertTeachers(List<ETTTeacher> teachers) throws DuplicateValException{

        for (ETTTeacher i : teachers) {
            if (this.teachers.containsKey(i.getId())) {
                throw new DuplicateValException(i.getId());
            } else {
                Teacher newTeacher = new Teacher(i, subjects);
                subjectsAreExist(newTeacher.getSubjects().keySet().stream().collect(Collectors.toList()));
                this.teachers.put(newTeacher.getId(), newTeacher);
            }
        }
        checkIDSequence(this.teachers.keySet().stream().collect(Collectors.toList()), ETTTeacher.class);
    }

    private void checkSumStudyHours(SchoolClass schoolClass) throws OutOfRangeException
    {
        if(schoolClass.sumStudyHours() > days * hours)
        {
            throw new OutOfRangeException("Class number " + schoolClass.getId() + " studies for a total of " + schoolClass.sumStudyHours()
                    + " hours and must study for a maximum of " + days*hours + " hours.\n");
        }
    }

    private void checkAndInsertClasses(List<ETTClass> classes) throws DuplicateValException{

        for (ETTClass i : classes) {
            if (this.classes.containsKey(i.getId())) {
                throw new DuplicateValException(i.getId());
            } else {
                SchoolClass newClass = new SchoolClass(i,subjects);
                subjectsAreExist(newClass.getSubjects().keySet().stream().collect(Collectors.toList()));
                checkSumStudyHours(newClass);
                this.classes.put(newClass.getId(), newClass);
            }
        }
        checkIDSequence(this.classes.keySet().stream().collect(Collectors.toList()), ETTClass.class);
    }

    private void checkAndInsertSubjects(List<ETTSubject> subjects)throws DuplicateValException{
        for (ETTSubject i : subjects) {
            if (this.subjects.containsKey(i.getId())) {
                throw new DuplicateValException(i.getId());
            } else {
                Subject newSubject = new Subject(i);
                this.subjects.put(newSubject.getId(), newSubject);
            }
        }
        checkIDSequence(this.subjects.keySet().stream().collect(Collectors.toList()), ETTSubject.class);
    }

    public int randomDay()
    {
        rnd = new Random();
        return rnd.nextInt(getDays()) + 1;
    }

    public int randomHour() {
        rnd = new Random();
        return rnd.nextInt(getHours()) + 1;
    }

    public Teacher randomTeacher()
    {
        rnd = new Random();
        int randomIndex = rnd.nextInt(getTeachers().size());
        return getTeacherByKey(randomIndex+1);
    }

    public Subject randomSubject()
    {
        rnd = new Random();
        int randomIndex = rnd.nextInt(getSubjects().size());
        return getSubjectByKey(randomIndex + 1);
    }

    public SchoolClass randomSchoolClass()
    {
        rnd = new Random();
        int randomIndex = rnd.nextInt(getClasses().size());
        return getClassByKey(randomIndex+1);
    }

    @Override
    public String toString() {
        String st = "-- subjects --\n" + subjects +
                "\n-- teachers --\n" + teachers +
                "-- classes --\n" + classes+
                "-- rules --\n" + rules  + '\n';
        String formatter = st.replace("{", " ")
                .replace("}", "")
                .replace(",", "")
                .replace("]", "")
                .replace("[", "")
                .replace('=', '.')
                .replace("TeacherIsHuman.","")
                .replace("Knowledgeable.","")
                .replace("Singularity.","")
                .replace("Satisfactory.","")
                .trim();
        return formatter;
    }
}
