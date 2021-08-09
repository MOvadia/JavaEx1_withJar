package EvolutionaryTimeTable;

import Base.StringToMap;
import generated.ETTSelection;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Selection {
    public enum Type {
        TRUNCATION {
            @Override
            public List<OptionalSolution> selection(int topPercent, int population, List<OptionalSolution> os) {
                Double percent = new Double((topPercent * population) / 100);
                int limit = percent.intValue();
                List<OptionalSolution> newPopulation = os.stream().sorted((a,b)->b.getFitness().compareTo(a.getFitness())).limit(limit).collect(Collectors.toList());
                return newPopulation;
            }
        },

        ROULETTEWHEEL {
            @Override
            public List<OptionalSolution> selection(int topPercent, int population, List<OptionalSolution> os) {
                return os;
            }};

            public abstract List<OptionalSolution> selection(int num, int population, List<OptionalSolution> os);
    }

    private Type type;
    private Map<String,String> configuration;

    public Selection(ETTSelection selection) {
        String configuration = selection.getConfiguration();
        this.type = Type.valueOf(selection.getType().toUpperCase());
        this.configuration = StringToMap.convert(configuration);
    }

    public Type getType() {
        return type;
    }

    public Map<String,String> getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Selection selection = (Selection) o;
        return type == selection.type && Objects.equals(configuration, selection.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, configuration);
    }

    @Override
    public String toString() {
        return "type: " + type +
                " | configuration:" + configuration + '\n';
    }
}
