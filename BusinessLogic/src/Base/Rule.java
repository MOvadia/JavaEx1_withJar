package Base;

import generated.ETTRule;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Rule {
    public enum Type { HARD, SOFT}
    public enum Id {TeacherIsHuman, Singularity, Knowledgeable, Satisfactory}

    private Id id;
    private Map<String,String> configuration = new HashMap<>();
    private Type type;
    private int ruleScore = 0;

    public Rule(ETTRule rule){
     //   List<String> temp= new ArrayList<>();
        //String configuration = rule.getETTConfiguration();
        this.type = Type.valueOf(rule.getType().toUpperCase());
        this.id = Id.valueOf(rule.getETTRuleId());
       // this.configuration = StringToMap.convert(configuration);
    }

    public int getRuleScore() {
        return ruleScore;
    }

    public void setRuleScore(int ruleScore) {
        this.ruleScore = ruleScore;
    }

    public Id getId() {
        return id;
    }

   // public List<String> getConfiguration() {
    //    return configuration;
   // }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return id == rule.id && Objects.equals(configuration, rule.configuration) && type == rule.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configuration, type);
    }

    @Override
    public String toString() {
        return "name: " + id.name() +
                " | type: " + type + "\n";
    }
}
