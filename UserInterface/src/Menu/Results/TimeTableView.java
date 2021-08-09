package Menu.Results;

import Base.Raw;
import Base.TimeTable;
import EvolutionaryTimeTable.DayHour;
import EvolutionaryTimeTable.OptionalSolution;

import java.util.*;

public abstract class TimeTableView {
    protected Map<DayHour, List<Raw>> table;
    protected String[] headerDays;
    protected TimeTable timeTable;
    protected OptionalSolution theBestSolution;
    private static final String HORIZONTAL_SEP = "-";
    private String verticalSep;
    private String joinSep;
    private String[] headers;
    private List<String[]> rows = new ArrayList<>();
    private boolean rightAlign;

    public TimeTableView(Map<DayHour, List<Raw>> table, TimeTable timeTable, OptionalSolution theBestSolution) {
        this.headerDays = new String[timeTable.getDays() + 1];
        this.theBestSolution = theBestSolution;
        this.table = table;
        this.timeTable = timeTable;
        setShowVerticalLines(true);
        setHeadersByDays();
    }

    private void setHeadersByDays()
    {
        this.headerDays[0] = "Hours";
        for (int i=1; i <= timeTable.getDays(); i++){
            headerDays[i] = "Day " + i;
        }
        setHeaders(this.headerDays);
    }

    public abstract void showTable();

    protected void clearTableParams(){
        this.rows.clear();
    }

    public void setRightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
    }

    public void setShowVerticalLines(boolean showVerticalLines) {
        verticalSep = showVerticalLines ? "|" : "";
        joinSep = showVerticalLines ? "+" : " ";
    }

    public void setHeaders(String... headers) {
        this.headers = headers;
    }

    public void addRow(String... cells) {
        rows.add(cells);
    }

    public void print() {
        int[] maxWidths = headers != null ?
                Arrays.stream(headers).mapToInt(String::length).toArray() : null;

        for (String[] cells : rows) {
            if (maxWidths == null) {
                maxWidths = new int[cells.length];
            }
            if (cells.length != maxWidths.length) {
                throw new IllegalArgumentException("Number of row-cells and headers should be consistent");
            }
            for (int i = 0; i < cells.length; i++) {
                maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
            }
        }

        if (headers != null) {
            printLine(maxWidths);
            printRow(headers, maxWidths);
            printLine(maxWidths);
        }
        for (String[] cells : rows) {
            printRow(cells, maxWidths);
        }
        if (headers != null) {
            printLine(maxWidths);
        }
    }

    private void printLine(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            String line = String.join("", Collections.nCopies(columnWidths[i] +
                    verticalSep.length() + 1, HORIZONTAL_SEP));
            System.out.print(joinSep + line + (i == columnWidths.length - 1 ? joinSep : ""));
        }
        System.out.println();
    }

    private void printRow(String[] cells, int[] maxWidths) {
        for (int i = 0; i < cells.length; i++) {
            String s = cells[i];
            String verStrTemp = i == cells.length - 1 ? verticalSep : "";
            if (rightAlign) {
                System.out.printf("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
            } else {
                System.out.printf("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
            }
        }
        System.out.println();
    }
}
