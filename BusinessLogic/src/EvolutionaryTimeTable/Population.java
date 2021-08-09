package EvolutionaryTimeTable;

import java.util.List;
import java.util.Objects;

public class Population {
    private final int INITIAL_POPULATION;
    List<OptionalSolution> solutions;
    private OptionalSolution theBestSolution;

    public Population(int initialPopulation, List<OptionalSolution> solutions) {
        this.INITIAL_POPULATION = initialPopulation;
        this.solutions = solutions;
    }

    public int getINITIAL_POPULATION() {
        return INITIAL_POPULATION;
    }

    public List<OptionalSolution> getSolutions() {
        return solutions;
    }

    public OptionalSolution getTheBestSolution() {
        return theBestSolution;
    }

    public void setTheBestSolution(OptionalSolution theBestSolution) {
        this.theBestSolution = theBestSolution;
    }

    public void clearSolutions()
    {
        solutions.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Population that = (Population) o;
        return INITIAL_POPULATION == that.INITIAL_POPULATION && Objects.equals(solutions, that.solutions) && Objects.equals(theBestSolution, that.theBestSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(INITIAL_POPULATION, solutions, theBestSolution);
    }


}
