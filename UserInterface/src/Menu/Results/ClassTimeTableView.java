package Menu.Results;

import Base.Raw;
import Base.TimeTable;
import EvolutionaryTimeTable.DayHour;
import EvolutionaryTimeTable.OptionalSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassTimeTableView extends TimeTableView{
    public ClassTimeTableView(Map<DayHour, List<Raw>> table, TimeTable timeTable, OptionalSolution theBestSolution) {
        super(table, timeTable, theBestSolution);
    }

    @Override
    public void showTable() {
        System.out.println(this.theBestSolution.evaluationToString());
        for(int c = 1; c< timeTable.getClasses().size(); c++) {
            System.out.println("The table for class: " + timeTable.getClasses().get(c).toStringIdName());
            String row;
            List<String> rows = new ArrayList<>();
            for (Integer i = 1; i <= this.timeTable.getHours(); i++) {
                rows.clear();
                rows.add(i.toString());
                for (int j = 1; j <= this.timeTable.getDays(); j++) {
                    DayHour dayHour = new DayHour(j, i);
                    row = "";
                    if (this.table.get(dayHour) != null) {
                        for (Raw raw : table.get(dayHour)) {
                            if(raw.getSchoolClass().getId() == timeTable.getClasses().get(c).getId()) {
                                row += "Teacher: " + raw.getTeacherID() + ", Subject: " + raw.getSubjectID() + "  ";
                            }
                        }
                    }
                    rows.add(row);
                }
                String[] rowsToPrint = new String[rows.size()];
                rows.toArray(rowsToPrint);
                addRow(rowsToPrint);
            }
            print();
            clearTableParams();
        }
    }
}
