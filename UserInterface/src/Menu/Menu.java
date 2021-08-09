package Menu;

import Base.*;
import EvolutionaryTimeTable.DayHour;
import Exceptions.*;
import Menu.Results.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private SystemEngine systemEngine = new Engine();
    private Scanner userInput = new Scanner(System.in);
    int operation;
    private String errNum = "Input is invalid, pleas enter a integer\n";
    private String errRange = "Input is invalid, please enter a number between 1 to";
    private String errLoadXML = "You must first upload an XML file\n";
    private int numOfGenerations;
    private int whenToShow;
  //  private boolean confirm = false;

    public void startMenu() {
        Boolean toContinue = true;
        while (toContinue) {
            System.out.println("Please choose number from the menu:\n" + "1. Read from Xml file\n" +
                    "2. View system settings and properties\n" + "3. Activate the evolution algorithm\n" +
                    "4. View the best solution\n" + "5. View the algorithm process\n" + "6. Exit\n");
            operation = stringToInt();
            switch (operation) {
                case 1:
                    getXmlPathFromUser();
                    break;
                case 2:
                    viewSettingsAndProperties();
                    break;
                case 3:
                    checkAndStartEvolutionAlgorithm();
                    break;
                case 4:
                    showBestSolution();
                    break;
                case 5:
                    showAlgorithmProcess();
                    break;
                case 6:
                    System.out.println(systemEngine.exit().getMessage());
                    toContinue = false;
                    break;
                default:
                    System.out.println(errRange + " 6\n");
            }
        }
    }

    public void getXmlPathFromUser() {
        System.out.println("Please enter XML full path");
        String xmlPath = userInput.nextLine();
        boolean toContinue = true;
        boolean confirm = false;
        while (toContinue) {
            try {
                DataTransferObject dto = systemEngine.readXML(xmlPath, confirm);
                System.out.println(dto.getMessage());
                toContinue = false;
            } catch (UserMustConfirmException e) {
                System.out.println(e.getMessage());
                confirm = confirmStartEvolutionAlgorithm();
                toContinue = confirm;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("operation failed.\n");
                toContinue = false;
            }
        }
    }

    public void viewSettingsAndProperties()
    {
        try {
            System.out.println(systemEngine.showSettingAndProperties());
        }
        catch (SomethingDoesntExistException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void checkAndStartEvolutionAlgorithm() {
        boolean toContinue = true;
        boolean confirm = false;
        while (toContinue) {
            System.out.println("please enter the number of generations (must be a integer > " + this.systemEngine.getMinNumGeneration() + "): ");
            numOfGenerations = stringToInt();
            System.out.println("Please enter how many generations would you like the information to be displayed? (must be a integer > 0): ");
            whenToShow = stringToInt();
            try {
                systemEngine.startEvolutionAlgorithm(numOfGenerations,whenToShow, confirm);
                toContinue = false;
                System.out.println("The algorithm started running in the background successfully.\n");
            } catch (WrongValueException e) {
                System.out.println(e.getMessage());
            }
            catch (UserMustConfirmException e)
            {
                System.out.println(e.getMessage());
                confirm = confirmStartEvolutionAlgorithm();
                toContinue = confirm;
            }
            catch (SomethingDoesntExistException e)
            {
                System.out.println(e.getMessage());
                toContinue = false;
            }
        }
    }

    public boolean confirmStartEvolutionAlgorithm() {
        boolean retVal = false;
            System.out.println("Do you want to restart the algorithm?\n" + "choose number from the list:\n"
                    + "1 - I confirm\n" + "2 - I don't confirm, return to the main menu\n");
            operation = stringToInt();
            switch (operation) {
                case 1:
                    retVal = true;
                    break;
                case 2:
                    retVal = false;
                    System.out.println("Your reply has been received. Return to the main menu.\n");
                    break;
                default:
                    System.out.println(errRange + " 2\n");
        }
        return retVal;
    }

    public void showBestSolution()
    {
        System.out.println("Select a number from the list regarding how to present the solution:\n"
                + "1 - by Raw\n" + "2 - by Teacher\n" + "3 - by Class\n");
        operation = stringToInt();
        Map<DayHour, List<Raw>> table;
        try {
            switch (operation)
            {
                case (1):
                    System.out.println(systemEngine.showBestSolution("RAW").getMessage());
                    break;
                case (2):
                    table = systemEngine.getBestSolutionTable("TEACHER").getTable();
                    TimeTableView timeTableViewForTeacher = new TeacherTimeTableView(table, systemEngine.getTimeTable().getTimeTable(), systemEngine.getBestSolutionTable().getTheBestSolution());
                    timeTableViewForTeacher.showTable();
                    break;
                case (3):
                    table = systemEngine.getBestSolutionTable("CLASS").getTable();
                    TimeTableView timeTableViewForClass = new ClassTimeTableView(table, systemEngine.getTimeTable().getTimeTable(), systemEngine.getBestSolutionTable().getTheBestSolution());
                    timeTableViewForClass.showTable();
                    break;
                default:
                    System.out.println(errRange + " 3\n");
            }
        }
        catch (SomethingDoesntExistException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void showAlgorithmProcess()
    {
        try {
            System.out.println(systemEngine.showAlgorithmProcess().getMessage());
        }
        catch (SomethingDoesntExistException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public int stringToInt() {
        boolean valid = false;
        int num = 0;
        while (!valid) {
            String choose = userInput.nextLine();
            try {
                num = Integer.parseInt(choose);
                valid = true;
            } catch (Exception e) {
                System.out.println(errNum);
                valid = false;
            }
        }
        return num;
    }
}

