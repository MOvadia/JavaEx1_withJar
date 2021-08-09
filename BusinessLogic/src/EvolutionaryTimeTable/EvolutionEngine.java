package EvolutionaryTimeTable;

import Base.Raw;
import Base.TimeTable;
import generated.*;

import java.util.*;
import java.util.stream.Collectors;

public class EvolutionEngine {
    private final int INITIAL_POPULATION;
    private Selection selection;
    private Crossover crossover;
    private List<Mutation> mutations = new ArrayList<>();
    private Map<Integer, Population> generations = new HashMap<>();
    private OptionalSolution theBestSolution;
    Random rnd = new Random();

    public EvolutionEngine(ETTEvolutionEngine evolutionEngine) {
        this.INITIAL_POPULATION = evolutionEngine.getETTInitialPopulation().getSize();
        this.selection = new Selection(evolutionEngine.getETTSelection());
        this.crossover = new Crossover(evolutionEngine.getETTCrossover());

        for (ETTMutation i : evolutionEngine.getETTMutations().getETTMutation()) {
            Mutation newMutation = new Mutation(i);
            mutations.add(newMutation);
        }
    }

    public Map<Integer, Population> getGenerations() {
        return generations;
    }

    public void startCrossover(TimeTable timeTable, Population theElected){
        OptionalSolution parent1, parent2;
        int randomIndex1,randomIndex2;
        Crossover crossover = getCrossover();
        List<OptionalSolution> childrenSolutions = new ArrayList<>();
        Population newGeneration = new Population(getInitialPopulation(), childrenSolutions);

        for(int i = 0; i < generations.get(1).getINITIAL_POPULATION(); i = i+2) {
            do {
                randomIndex1 = rnd.nextInt(theElected.getSolutions().size());
                parent1 = theElected.getSolutions().get(randomIndex1);
            } while (parent1 == null);

            do {
                do {
                    randomIndex2 = rnd.nextInt(theElected.getSolutions().size());
                } while (randomIndex1 == randomIndex2);
                parent2 = theElected.getSolutions().get(randomIndex2);
            } while (parent2 == null);

            Map<Integer, Raw> parent1byExistIndex = crossover.getName().sortParent(parent1.getOptionalSolution().values().stream().collect(Collectors.toList()), timeTable);
            Map<Integer, Raw> parent2byExistIndex = crossover.getName().sortParent(parent2.getOptionalSolution().values().stream().collect(Collectors.toList()), timeTable);
            int optionSize = timeTable.getDays() * timeTable.getHours() * timeTable.getTeachers().size() * timeTable.getClasses().size() * timeTable.getSubjects().size();
            List<Integer> cuttingPoints = randomCuttingPoints(optionSize);
            OptionalSolution children1 = new OptionalSolution(crossover.createChildren(parent1byExistIndex, parent2byExistIndex, cuttingPoints, optionSize), parent1.getRules(), parent1.getClasses(), parent1.getHARD_RULES_WEIGHT(), parent1.getTimeTable());
            OptionalSolution children2 = new OptionalSolution(crossover.createChildren(parent2byExistIndex, parent1byExistIndex, cuttingPoints, optionSize), parent1.getRules(), parent1.getClasses(), parent1.getHARD_RULES_WEIGHT(), parent1.getTimeTable());
            newGeneration.solutions.add(children1);
            newGeneration.solutions.add(children2);
        }
        checkAndsSetTheBestSolution(theElected.getTheBestSolution());
        this.generations.get(generations.size()).setTheBestSolution(theElected.getTheBestSolution());
        this.generations.get(generations.size()).clearSolutions();
        this.generations.put(generations.size()+1, newGeneration);
    }

    private List<Integer> randomCuttingPoints(int range)
    {
        List<Integer> cuttingPoints = new LinkedList<>();
        int rndCuttingPoints;
        Crossover crossover = getCrossover();

        for (int i = 0; i < crossover.getCuttingPoints(); i++)
        {
            do {
                rndCuttingPoints = rnd.nextInt(range);
            }while (cuttingPoints.contains(rndCuttingPoints));
            cuttingPoints.add(rndCuttingPoints);
        }
        cuttingPoints = cuttingPoints.stream().sorted().collect(Collectors.toList());

        return cuttingPoints;
    }

    public void startMutation(TimeTable timeTable)
    {
        for(int i = 0; i < generations.get(generations.size()).getSolutions().size(); i++)
        {
            for (Mutation mutation:mutations) {
                if (rnd.nextDouble() < mutation.getProbability()) {
                    mutation.mutation(generations.get(generations.size()).getSolutions().get(i), timeTable);
                }
            }
        }
    }

    public Population startSelection()
    {
        Population newPopulation = null;
        if(selection.getType() == Selection.Type.TRUNCATION)
        {
            int num = Integer.parseInt(selection.getConfiguration().get("TopPercent"));
            List<OptionalSolution> theElected = selection.getType().selection(num,INITIAL_POPULATION,generations.get(generations.size()).getSolutions());
            newPopulation = new Population(INITIAL_POPULATION,theElected);
            newPopulation.setTheBestSolution(theElected.get(0));
        }
        return newPopulation;
    }

    public OptionalSolution getTheBestSolution() {
        return theBestSolution;
    }

    public void checkAndsSetTheBestSolution(OptionalSolution theBestSolution) {
        if (this.theBestSolution == null)
        {
            this.theBestSolution = theBestSolution;
        }
        else if(this.theBestSolution.getFitness() < theBestSolution.getFitness())
        {
            this.theBestSolution = theBestSolution;
        }
    }

    public void calculateFitness(TimeTable timeTable)
    {
        for (OptionalSolution os: generations.get(generations.size()).getSolutions()) {
            if(os!= null)
            {
                os.calculateFitness(timeTable);
            }
        }
    }

    public int getInitialPopulation() {
        return INITIAL_POPULATION;
    }

    public Selection getSelection() {
        return selection;
    }

    public Crossover getCrossover() {
        return crossover;
    }

    public List<Mutation> getMutations() {
        return mutations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvolutionEngine that = (EvolutionEngine) o;
        return INITIAL_POPULATION == that.INITIAL_POPULATION && Objects.equals(selection, that.selection) && Objects.equals(crossover, that.crossover) && Objects.equals(mutations, that.mutations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(INITIAL_POPULATION, selection, crossover, mutations);
    }

    @Override
    public String toString() {
        String st =  "-- population size -- \n" + "population size: " + INITIAL_POPULATION + "\n" +
                "\n-- selection technique -- \n" + selection + '\n' +
                "-- crossover type --\n" + crossover + '\n' +
                "-- mutations --\n" + mutations + '\n';
        String formatter = st.replace("{", " ")
                .replace("}", "")
                .replace("]", "")
                .replace("[", "")
                .replace(",", "")
                .replace('=', '.')
                .trim();
        return  formatter;
    }
}
