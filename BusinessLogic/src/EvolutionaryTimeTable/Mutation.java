package EvolutionaryTimeTable;

import Base.*;
import generated.ETTMutation;

import java.util.*;

public class Mutation {
    public enum Name {Flipping , Sizer};

    private double probability;
    private Map<String,String> configuration;
    private Name name;
    Random rnd;

    public Mutation(ETTMutation mutation) {
        String configuration = mutation.getConfiguration();
        this.name = Name.valueOf(mutation.getName());
        this.probability = mutation.getProbability();
        this.configuration = StringToMap.convert(configuration);
        rnd = new Random();
    }

    public void mutation(OptionalSolution population, TimeTable timeTable)
    {
        int rndIndx;
        int count = rnd.nextInt(Integer.parseInt(configuration.get("MaxTupples"))) + 1;
        List<Integer> mutations = new LinkedList<>();

        for (int i = 0; i < count; i++)
        {
            do {
                rndIndx = rnd.nextInt(population.getOptionalSolution().size()) + 1;
            }while (mutations.contains(rndIndx));
            mutations.add(rndIndx);
        }

        for (int i = 0; i < count ; i++)
        {
            switch (configuration.get("Component"))
            {
                case("D"):
                    int D = timeTable.randomDay();
                    population.getOptionalSolution().get(mutations.get(i)).setDay(D);
                break;
                case ("H"):
                    int H = timeTable.randomHour();
                    population.getOptionalSolution().get(mutations.get(i)).setHour(H);
                    break;
                case ("T"):
                    Teacher T = timeTable.randomTeacher();
                    population.getOptionalSolution().get(mutations.get(i)).setTeacher(T);
                    break;
                case ("S"):
                    Subject S = timeTable.randomSubject();
                    population.getOptionalSolution().get(mutations.get(i)).setSubject(S);
                    break;
                case ("C"):
                    SchoolClass C = timeTable.randomSchoolClass();
                    population.getOptionalSolution().get(mutations.get(i)).setSchoolClass(C);
                    break;
            }
        }
    }

    public double getProbability() {
        return probability;
    }

    public Name getName() {
        return name;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mutation mutation = (Mutation) o;
        return Double.compare(mutation.probability, probability) == 0 && name.equals(mutation.name) && Objects.equals(configuration, mutation.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(probability, name, configuration);
    }

    @Override
    public String toString() {
       return  "probability: " + probability + " | configuration: " + configuration + "\n" ;
    }
}
