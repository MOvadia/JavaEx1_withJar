package Base;

import EvolutionaryTimeTable.DayHour;
import EvolutionaryTimeTable.EvolutionEngine;
import EvolutionaryTimeTable.OptionalSolution;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataTransferObject {
    private TimeTable timeTable;
    private EvolutionEngine evolutionEngine;
    private String message;

    public OptionalSolution getTheBestSolution() {
        return theBestSolution;
    }

    private OptionalSolution theBestSolution;
    private Map<DayHour, List<Raw>> table;

    public DataTransferObject(OptionalSolution theBestSolution) {
        this.theBestSolution = theBestSolution;
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public DataTransferObject(Map<DayHour, List<Raw>> table, TimeTable timeTable) {
        this.table = table;
        this.timeTable = timeTable;
    }

    public DataTransferObject(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public DataTransferObject(String string)
    {
        message = string;
    }

    public DataTransferObject(TimeTable timeTable,EvolutionEngine evolutionEngine) {

        this.timeTable = timeTable;
        this.evolutionEngine = evolutionEngine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataTransferObject that = (DataTransferObject) o;
        return Objects.equals(timeTable, that.timeTable) && Objects.equals(evolutionEngine, that.evolutionEngine) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeTable, evolutionEngine, message);
    }

    @Override
    public String toString() {
        return "- Time Table details -\n" + timeTable +
                "\n\n- Evolution Engine details -\n" + evolutionEngine + '\n';
    }

    public String getMessage() {
        return message;
    }

    public Map<DayHour, List<Raw>> getTable() {
        return table;
    }

}
