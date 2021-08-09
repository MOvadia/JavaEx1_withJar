package EvolutionaryTimeTable;

import Base.Raw;
import Base.StringToMap;
import Base.TimeTable;
import generated.ETTCrossover;

import java.util.*;
import java.util.stream.Collectors;

public class Crossover {

    public enum Name{
        DayTimeOriented {
            @Override
            public Map<Integer, Raw> sortParent(List<Raw> parent, TimeTable timeTable)
            {
                Map<Integer,Raw> parentMap = new HashMap<>();
                List<Raw> parentList = parent.stream().sorted(Comparator.comparing(Raw::getDay).thenComparing(Raw::getHour).
                        thenComparing(Raw::getTeacherID).thenComparing(Raw::getSubjectID).
                        thenComparing(Raw::getSchoolClassID)).collect(Collectors.toList());
                int r = 0, index = 1;
                for(int d = 1; d <= timeTable.getDays() ; d++)
                {
                    for(int h = 1; h <= timeTable.getHours() ; h++)
                    {
                        for(int t = 1; t <= timeTable.getTeachers().size() ; t++)
                        {
                            for(int s = 1; s <= timeTable.getSubjects().size() ; s++)
                            {
                                for(int c = 1; c <= timeTable.getClasses().size() ; c++)
                                {
                                    if(r < parentList.size())
                                    {
                                        for(int i = r; i<parentList.size(); i++) {
                                            if (parentList.get(i).getDay() == d && parentList.get(i).getHour() == h && parentList.get(i).getTeacherID() == t && parentList.get(i).getSubjectID() == s && parentList.get(i).getSchoolClassID() == c) {
                                                parentMap.put(index, parentList.get(i));
                                                r = i + 1;
                                                break;
                                            } else if (parentList.get(i).getDay() > d || parentList.get(i).getHour() > h || parentList.get(i).getTeacherID() > t || parentList.get(i).getSubjectID() > s || parentList.get(i).getSchoolClassID() > c) {
                                                break;
                                            }
                                        }
                                    }
                                    index++;
                                }
                            }
                        }
                    }
                }
                return parentMap;
            }
        },

        AspectOriented{
           /* @Override
            public  List<Raw>[][] createTwoDimensionTable(OptionalSolution optionalSolution, int days, int hours) {
                return new List[0][];
            } */
            @Override
            public  Map<Integer, Raw> sortParent(List<Raw> parent, TimeTable timeTable)
            {return new HashMap<>();}
        };
      //  public abstract  List<Raw>[][] createTwoDimensionTable(OptionalSolution optionalSolution, int days, int hours);
      public abstract  Map<Integer, Raw> sortParent(List<Raw> parent, TimeTable timeTable);
    }

    private Map<String,String> configuration;
    private int cuttingPoints;
    private Name name;

    public Crossover(ETTCrossover ettCrossover) {
        String configuration = ettCrossover.getConfiguration();
        this.cuttingPoints = ettCrossover.getCuttingPoints();
        this.name = Name.valueOf(ettCrossover.getName());
        this.configuration = StringToMap.convert(configuration);
    }

    public Map<String,String> getConfiguration() {
        return configuration;
    }

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    public Name getName() {
        return name;
    }

    public Map<Integer, Raw> createChildren(Map<Integer, Raw> parent1, Map<Integer, Raw> parent2, List<Integer> indexes, int maxSize) {
        Map<Integer, Raw> children = new HashMap<>();
        Map<Integer, Raw> currentParent;
        int untilIndex, currentIndex = 0, indexChildren = 1;
        for (int i = 0; i <= indexes.size(); i++) {
            if (i % 2 == 0) {
                currentParent = parent1;
            } else {
                currentParent = parent2;
            }
            if(i >= indexes.size()) {
                untilIndex = maxSize;
            } else {
                untilIndex = indexes.get(i);
            }
            currentIndex++;
            for (int j = currentIndex; j <= untilIndex; j++) {
                if (currentParent.get(j) != null) {
                    Raw childrenRaw = new Raw(currentParent.get(j));
                    children.put(indexChildren, childrenRaw);
                    indexChildren++;
                }
                currentIndex = j;
            }
        }
        return children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crossover crossover = (Crossover) o;
        return cuttingPoints == crossover.cuttingPoints && Objects.equals(configuration, crossover.configuration) && name == crossover.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuration, cuttingPoints, name);
    }

    @Override
    public String toString() {
        return ",name: " + name +
                " | cuttingPoints: " + cuttingPoints + '\n';
    }


}
