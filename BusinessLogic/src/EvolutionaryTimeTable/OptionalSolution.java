package EvolutionaryTimeTable;

import Base.*;

import java.text.DecimalFormat;
import java.util.*;

public class OptionalSolution {
    public enum SolutionToString{
        RAW {
            @Override
            public String toString(Map<Integer, Raw>  optionalSolution, TimeTable timeTable)
            {
                List<String> strings = new ArrayList<>();
                for (Raw raw : optionalSolution.values()) {
                    strings.add(rawToString(raw));
                }
                String st = strings.toString();
                return st;
            }

            public String rawToString(Raw raw) {
                return "< day: " + raw.getDay() +
                        " ,hour: " + raw.getHour() +
                        " ,class: " + raw.getSchoolClass().getId() +
                        " ,teacher: " + raw.getTeacher().getId() +
                        " ,subject: " + raw.getSubject().getId() +
                        " >" + "\n";
            }
        },
        TEACHER {
            @Override
            public String toString(Map<Integer, Raw>  optionalSolution, TimeTable timeTable)
            {
                Map<Integer, List<String>> timeTableByTeacher = new HashMap<>();
                String teacherTimeTableStr = "";
                for (Teacher teacher: timeTable.getTeachers().values()) {
                    String title = "Teacher ID: "  + teacher.getId() + " name:" + teacher.getName() + "\n";
                    List<String> teacherTimeTable = new ArrayList<>();
                    teacherTimeTable.add(title);
                    timeTableByTeacher.put(teacher.getId(), teacherTimeTable);
                }
                for (Raw raw : optionalSolution.values()) {
                    timeTableByTeacher.get(raw.getTeacherID()).add(rawTeacherToSting(raw));
                }
                for (Integer teacherId : timeTableByTeacher.keySet()) {
                    teacherTimeTableStr += timeTableByTeacher.get(teacherId).toString();
                }

                return teacherTimeTableStr;
            }
            private String rawTeacherToSting(Raw raw){
                return  "day=" + raw.getDay() +
                        ", hour=" + raw.getHour() +
                        "\nSubject=" + raw.getSubject() +
                        "SchoolClass=" + raw.getSchoolClass().toStringIdName() +
                        '}' + "\n";
            }
        },
        CLASS {
            @Override
            public String toString(Map<Integer, Raw>  optionalSolution, TimeTable timeTable)
            {
                Map<Integer, List<String>> timeTableByClass = new HashMap<>();
                String classTimeTableStr = "";
                for (SchoolClass schoolClass: timeTable.getClasses().values()) {
                    String title = "Class ID: "  + schoolClass.getId() + " name:" + schoolClass.getName() + "\n";
                    List<String> schoolClassTimeTable = new ArrayList<>();
                    schoolClassTimeTable.add(title);
                    timeTableByClass.put(schoolClass.getId(), schoolClassTimeTable);
                }
                for (Raw raw : optionalSolution.values()) {
                    timeTableByClass.get(raw.getSchoolClass().getId()).add(rawSchoolClassToSting(raw));
                }
                for (Integer schoolClassId : timeTableByClass.keySet()) {
                    classTimeTableStr += timeTableByClass.get(schoolClassId).toString();
                }

                return classTimeTableStr;
            }

            private String rawSchoolClassToSting(Raw raw){
                return  "day=" + raw.getDay() +
                        ", hour=" + raw.getHour() +
                        "\nSubject=" + raw.getSubject() +
                        "Teacher=" + raw.getTeacher().toStringIdName() +
                        '}' + "\n";
            }
        };
        public abstract String toString(Map<Integer, Raw>  optionalSolution, TimeTable timeTable);
    }
    private final int HARD_RULES_WEIGHT;
    private Map<Integer, Raw>  optionalSolution;
    private Map<Rule.Id, Rule> rules;
    private Map<Integer, SchoolClass> classes;
    private TimeTable timeTable;
    private Map<SubjectPerClass,Integer> hoursAmount;
    private double fitness;
    private SolutionToString solutionToString;
    private double avgHardRules;
    private double avgSoftRules;
    private Integer generation;
    private Map<Rule, Double> fitnessForRule;

    public OptionalSolution(Map<Integer, Raw> optionalSolution, Map<Rule.Id, Rule> rules, Map<Integer, SchoolClass> classes, int hardRulesWeight, TimeTable timeTable) {
        this.optionalSolution = optionalSolution;
        this.rules = rules;
        this.classes = classes;
        this.hoursAmount = initialHourAmountMap();
        this.HARD_RULES_WEIGHT = hardRulesWeight;
        this.solutionToString = SolutionToString.RAW;
        this.timeTable = timeTable;
        this.fitnessForRule = new HashMap<>();
    }

    public Map<Rule.Id, Rule> getRules() {
        return rules;
    }

    public void setSolutionToString(SolutionToString solutionToString) {
        this.solutionToString = solutionToString;
    }

    public int getHARD_RULES_WEIGHT() {
        return HARD_RULES_WEIGHT;
    }

    public Map<Integer, SchoolClass> getClasses() {
        return classes;
    }

    public Map<SubjectPerClass, Integer> getHoursAmount() {
        return hoursAmount;
    }

    public void setSolutionToString(String solutionToString) {
        this.solutionToString = SolutionToString.valueOf(solutionToString);
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public SolutionToString getSolutionToString() {
        return solutionToString;
    }

    public void calculateFitness(TimeTable timeTable){
        for (Rule rule: this.rules.values()) {
            if (optionalSolution.size() < timeTable.getDays() * timeTable.getHours() * timeTable.getMinCT())
            {
                fitnessForRule.put(rule, 100.0);
            }
            else {
                switch (rule.getId()) {
                    case Satisfactory:
                        fitnessForRule.put(rule, calculateFitnessSatisfactory(rule));
                        break;
                    case Knowledgeable:
                        fitnessForRule.put(rule, calculateFitnessKnowledgeable(rule));
                        break;
                    case Singularity:
                        fitnessForRule.put(rule, calculateFitnessSingularity(rule));
                        break;
                    case TeacherIsHuman:
                        fitnessForRule.put(rule, calculateFitnessTeacherIsHuman(rule));
                        break;
                }
            }
        }
        calculateFitnessFromPenalty();
    }

    public double getAvgHardRules() {
        return avgHardRules;
    }

    public double getAvgSoftRules() {
        return avgSoftRules;
    }

    private void calculateFitnessFromPenalty() {
        double sumRulesPenaltyHard = 0;
        double sumRulesPenaltySoft = 0;
        int countHardRules = 0;
        int countSoftRules = 0;
        for (Rule rule: fitnessForRule.keySet()) {
            if (rule.getType() == Rule.Type.HARD) {
                sumRulesPenaltyHard += fitnessForRule.get(rule);
                countHardRules++;
            } else {
                sumRulesPenaltySoft += fitnessForRule.get(rule);
                countSoftRules++;
            }
        }
        avgHardRules = 100 - (sumRulesPenaltyHard / countHardRules);
        avgSoftRules = 100 - (sumRulesPenaltySoft / countSoftRules);
        double hardPercent = HARD_RULES_WEIGHT/100.0;
        double softPercent = (100-HARD_RULES_WEIGHT)/100.0;
        double avg = ((hardPercent) * avgHardRules) + (softPercent * avgSoftRules);
         if(avg <= 0)
         {
             fitness = 0;
         }
        else
         {
             fitness = avg;
         }
    }

    private double calculateFitnessTeacherIsHuman(Rule rule) {
        double penalty = 0, total = 0;
        for (int i = 1 ; i <= this.optionalSolution.size() ; i++){
            for (int j = i+1 ; j <= this.optionalSolution.size() ; j++) {
                if(this.optionalSolution.get(i) != null && this.optionalSolution.get(j) != null) {
                    if (this.optionalSolution.get(i).getDay() == this.optionalSolution.get(j).getDay() && this.optionalSolution.get(i).getHour() == this.optionalSolution.get(j).getHour()) {
                        total++;
                        if (this.optionalSolution.get(i).getTeacher().equals(this.optionalSolution.get(j).getTeacher())) {
                            penalty++;
                        }
                    }
                }
            }
        }
        penalty = ((penalty/total) * 100);
        return penalty;
    }

    private double calculateFitnessSingularity(Rule rule) {
        double penalty = 0, total = 0;
        for (int i = 1 ; i <= this.optionalSolution.size() ; i++){
            for (int j = i+1 ; j <= this.optionalSolution.size() ; j++) {
                if (this.optionalSolution.get(i) != null && this.optionalSolution.get(j) != null) {
                    if (this.optionalSolution.get(i).getDay() == this.optionalSolution.get(j).getDay() && this.optionalSolution.get(i).getHour() == this.optionalSolution.get(j).getHour()) {
                        total++;
                        if (this.optionalSolution.get(i).getSchoolClass().equals(this.optionalSolution.get(j).getSchoolClass()) &&
                                (this.optionalSolution.get(i).getSubject() != this.optionalSolution.get(j).getSubject() || this.optionalSolution.get(i).getTeacher() != this.optionalSolution.get(j).getTeacher())) {
                            penalty++;
                        }
                    }
                }
            }
        }
        penalty = ((penalty/total) * 100);
        return penalty;
    }

    private double calculateFitnessKnowledgeable(Rule rule) {
        double penalty = 0, total = 0;
        for (Raw raw: this.optionalSolution.values()) {
            if(raw != null) {
                total++;
                if (!raw.getTeacher().getSubjects().containsValue(raw.getSubject())) {
                    penalty++;
                }
            }
        }
        penalty = ((penalty/total) * 100);
        return penalty;
    }

    private double calculateFitnessSatisfactory(Rule rule) {
        double penalty = 0;
        calculateHoursPerClass();
        double totalFitness = 0;
        for (SchoolClass c: this.classes.values()) {
            penalty = 0;
            for (Study s: c.getStudies()) {
                SubjectPerClass subjectPerClass = new SubjectPerClass(c, s.getSubject().getId());
                if(this.hoursAmount.get(subjectPerClass) < s.getHours())
                {
                    penalty += (((double)this.hoursAmount.get(subjectPerClass) / s.getHours()) * 100);
                }
                else if(this.hoursAmount.get(subjectPerClass) > s.getHours()){
                    penalty += (((double) s.getHours() / this.hoursAmount.get(subjectPerClass)) * 100);
                }
            }
            penalty = penalty / c.getStudies().size();
            totalFitness += penalty;
        }
        totalFitness = totalFitness / this.classes.size();
        return totalFitness;
    }

    private Map<SubjectPerClass,Integer> initialHourAmountMap() {
        Map<SubjectPerClass,Integer> hoursAmount = new HashMap<>();
        for (SchoolClass c: this.classes.values()) {
            for (Study study: c.getStudies()){
                SubjectPerClass subPerClass = new SubjectPerClass(c, study.getSubject().getId());
                hoursAmount.put(subPerClass,0);
            }
        }
        return hoursAmount;
    }

    private int calculateHoursPerClass(){
        int val;
        int schoolClassHaveNoSuchSubject = 0;
        Map<SchoolClass, Integer> subPerClass = new HashMap<>();
        for (int i = 1 ; i <= this.optionalSolution.size() ; i++) {
            if(this.optionalSolution.get(i) != null) {
                SubjectPerClass subjectPerClass = new SubjectPerClass(this.optionalSolution.get(i).getSchoolClass(), this.optionalSolution.get(i).getSubject().getId());
                if (this.hoursAmount.get(subjectPerClass) != null) {
                    val = this.hoursAmount.get(subjectPerClass);
                    val++;
                    this.hoursAmount.put(subjectPerClass, val);
                }
                else{
                    schoolClassHaveNoSuchSubject++;
                }
            }
        }
        return schoolClassHaveNoSuchSubject;
    }

    public Map<DayHour, List<Raw>> getTablePerIdentifier(Map<Integer, Raw>  optionalSolution){
        Map<DayHour, List<Raw>> table = new HashMap<>();
        for (Raw raw : optionalSolution.values()) {
            DayHour dayHour = new DayHour(raw.getDay(),raw.getHour());
            if(table.get(dayHour) != null) {
                table.get(dayHour).add(raw);
            }
            else{
                List<Raw> lRaw = new ArrayList<>();
                lRaw.add(raw);
                table.put(dayHour, lRaw);
            }
        }
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionalSolution that = (OptionalSolution) o;
        return Double.compare(that.avgHardRules, avgHardRules) == 0 && Double.compare(that.avgSoftRules, avgSoftRules) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(avgHardRules, avgSoftRules);
    }

    public Double getFitness() {
        return new Double(fitness);
    }

    public Map<Integer, Raw> getOptionalSolution() {
        return optionalSolution;
    }

    public String rulesFitnessToString(){
        DecimalFormat df = new DecimalFormat("###.#");
        String ruleStr = "Generation: " + this.getGeneration() + "\nThe Rules that tested:\n";
        for (Rule rule: this.fitnessForRule.keySet()) {
            Double ruleScore = new Double(this.fitnessForRule.get(rule));
            Double score = 100.0 - ruleScore;
            ruleStr += rule.toString() + "rule score: " + df.format(score) + "\n";
        }
        return ruleStr;
    }

    public String evaluationToString(){
        DecimalFormat df = new DecimalFormat("###.#");
        String title = "fitness score: " + df.format(this.getFitness()) + '\n' + "Average hard rules: " + df.format(this.avgHardRules)
                + '\n' + "Average soft rules: " + df.format(this.avgSoftRules) + '\n' + rulesFitnessToString();
        return title;
    }

    @Override
    public String toString() {
        List<String> strings = new LinkedList<>();
        String title = evaluationToString();
        strings.add(title);
        strings.add(solutionToString.toString(this.optionalSolution, this.timeTable));
        String st = strings.toString();
        String formatter = st.replace("{", " ")
                .replace("}", "")
                .replace("]", "")
                .replace("[", "")
                .replace(", ", "")
                .trim();
        return formatter;
    }

    public Map<DayHour, List<Raw>> getTablePerIdentifier(){
        return getTablePerIdentifier(this.optionalSolution);
    }
}
