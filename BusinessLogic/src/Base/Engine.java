package Base;

import DataLoader.XmlLoader;
import EvolutionaryTimeTable.EvolutionEngine;
import EvolutionaryTimeTable.OptionalSolution;
import EvolutionaryTimeTable.Population;
import Exceptions.SomethingDoesntExistException;
import Exceptions.UserMustConfirmException;
import Exceptions.WrongValueException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Engine implements SystemEngine{
    private TimeTable timeTable;
    private EvolutionEngine evolutionEngine;
    private XmlLoader xml;
    private int generationNum = 0;
    private Boolean isExistsFullSolution = new Boolean(false);
    private int whenToShow;
    private final int MIN_NUM_OF_GENERATION = 100;
    Thread thread = new EvolutionAlgorithmTread();
    Boolean toStop = new Boolean(false);

    public class EvolutionAlgorithmTread extends Thread{
        @Override
        public void run() {
            isExistsFullSolution = false;
            initialPopulation();
            for(int i = 0; i < generationNum; i++) {
                synchronized (toStop){
                    if(toStop == true)
                    {
                        break;
                    }
                }
                evolutionEngine.calculateFitness(timeTable);
                Population theElected = evolutionEngine.startSelection();
                evolutionEngine.startCrossover(timeTable, theElected);
                evolutionEngine.startMutation(timeTable);
            }
            synchronized (toStop) {
                if (toStop == false) {
                    isExistsFullSolution = true;
                }
            }
        }
    }

    @Override
    public DataTransferObject readXML(String xmlPath, boolean confirm) throws JAXBException, FileNotFoundException {
        if (thread.getState() == Thread.State.RUNNABLE && !confirm)
        {
            throw new UserMustConfirmException(false,true);
        }
        else if(thread.getState() == Thread.State.RUNNABLE && confirm)
        {
            synchronized (toStop) {
                toStop = true;
            }

            while (thread.getState() == Thread.State.RUNNABLE)
            {
                //busy wait....
            }
        }
        XmlLoader tmpXML = new XmlLoader();
        tmpXML.setXmlPath(xmlPath);
        TimeTable tmpT = new TimeTable(tmpXML.getDescriptor());
        EvolutionEngine tmpE = new EvolutionEngine(tmpXML.getDescriptor().getETTEvolutionEngine());
        xml = tmpXML;
        timeTable = tmpT;
        evolutionEngine = tmpE;
        isExistsFullSolution = new Boolean(false);
        return new DataTransferObject("The XML file was successfully uploaded to the system.\n");
    }

    @Override
    public DataTransferObject showSettingAndProperties() throws SomethingDoesntExistException {
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        return new DataTransferObject(timeTable,evolutionEngine);
    }

    @Override
    public void startEvolutionAlgorithm(int generationNum, int whenToShow, boolean confirm) throws WrongValueException, SomethingDoesntExistException, UserMustConfirmException {
        if (thread.getState() == Thread.State.RUNNABLE && !confirm)
        {
            throw new UserMustConfirmException(false,true);
        }
        if(isExistsFullSolution && !confirm)
        {
            throw new UserMustConfirmException(true,false);
        }
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        if(generationNum <=100)
        {
            throw new WrongValueException(generationNum);
        }
        if(whenToShow < 1)
        {
            throw new WrongValueException(whenToShow);
        }
        this.generationNum = generationNum;
        this.whenToShow = whenToShow;

       if(thread.getState() == Thread.State.RUNNABLE)
       {
           synchronized (toStop) {
               toStop = true;
           }
       }

       while (thread.getState() == Thread.State.RUNNABLE)
       {
           //busy wait....
       }

        if(thread.getState() == Thread.State.TERMINATED) {
            thread = new EvolutionAlgorithmTread();
        }
        if (thread.getState() == Thread.State.NEW)
       {
           toStop = false;
           evolutionEngine.initialize();
           thread.start();
       }
    }

    private void initialPopulation()
    {
        List<OptionalSolution> initialPopulation = new LinkedList<>();
        for (int i = 1; i <= evolutionEngine.getInitialPopulation() ; i++)
        {
            OptionalSolution solution = creatOptionalSolution();
            initialPopulation.add(solution);
        }
        Population population= new Population(evolutionEngine.getInitialPopulation(),initialPopulation);
        synchronized (evolutionEngine.getGenerations()) {
            int numOfGenerations = evolutionEngine.getGenerations().size() + 1;
            evolutionEngine.getGenerations().put(numOfGenerations, population);
        }
    }

    private OptionalSolution creatOptionalSolution()
    {
        Random rnd = new Random();
        Map<Integer, Raw> optionalSolution = new HashMap<>();
        int optionalSolutionSize = rnd.nextInt(timeTable.getDays() * timeTable.getHours() * timeTable.getMaxCT());
        optionalSolutionSize = optionalSolutionSize + (timeTable.getDays() * timeTable.getHours() * timeTable.getMinCT());

        for(int i = 1; i <= optionalSolutionSize; i++)
        {
            Raw raw = creatRaw();
            if(!optionalSolution.containsValue(raw))
            {
                optionalSolution.put(i,raw);
            }
            else{
                i--;
            }
        }

        OptionalSolution solution = new OptionalSolution(optionalSolution, this.timeTable.getRules(), this.timeTable.getClasses(), this.timeTable.getHardRulesWeight(), this.timeTable);
        return solution;
    }

    private Raw creatRaw()
    {
        int rndDay = this.timeTable.randomDay();
        int rndHour = this.timeTable.randomHour();
        Teacher rndTeacher = this.timeTable.randomTeacher();
        Subject rndSubject = this.timeTable.randomSubject();
        SchoolClass rndClass = this.timeTable.randomSchoolClass();

        Raw raw = new Raw(rndDay,rndHour,rndTeacher,rndSubject,rndClass);
        return raw;
    }


    @Override
    public DataTransferObject showBestSolution(String solutionToString) throws SomethingDoesntExistException{
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        synchronized (evolutionEngine.getGenerations()) {
            if (evolutionEngine.getGenerations().size() == 0) {
                throw new SomethingDoesntExistException("solution");
            }
        }

        evolutionEngine.getTheBestSolution().setSolutionToString(solutionToString);
        return new DataTransferObject(evolutionEngine.getTheBestSolution().toString());
    }


    public DataTransferObject getBestSolutionTable(String solutionToString) throws SomethingDoesntExistException{
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        synchronized (evolutionEngine.getGenerations()) {
            if (evolutionEngine.getGenerations().size() == 0) {
                throw new SomethingDoesntExistException("solution");
            }
        }

        evolutionEngine.getTheBestSolution().setSolutionToString(solutionToString);
        return new DataTransferObject(evolutionEngine.getTheBestSolution().getTablePerIdentifier(), this.timeTable);
    }

    @Override
    public DataTransferObject showAlgorithmProcess() throws SomethingDoesntExistException {
        if (!isExistsXML()) {
            throw new SomethingDoesntExistException("XML file");
        }
        synchronized (evolutionEngine.getGenerations()) {
            if (evolutionEngine.getGenerations().size() == 0) {
                throw new SomethingDoesntExistException("generations");
            }
        }
        List<String> strings = new LinkedList<>();
        int size = 0;
        List<Population> generationsToPrint;
        synchronized (isExistsFullSolution) {
            synchronized (evolutionEngine.getGenerations()) {
                size = evolutionEngine.getGenerations().size() - 1;
                generationsToPrint = evolutionEngine.getGenerations().values().stream().limit(size).collect(Collectors.toList());
                if(isExistsFullSolution)
                {
                    generationsToPrint = generationsToPrint.stream().filter(population -> population.getTheBestSolution().getGeneration() % whenToShow == 0).collect(Collectors.toList());
                }
                else
                {
                    generationsToPrint = generationsToPrint.stream().filter(population -> population.getTheBestSolution().getGeneration() % whenToShow == 0)
                            .sorted((a,b)->b.getTheBestSolution().getGeneration().compareTo(a.getTheBestSolution().getGeneration())).
                                limit(10).collect(Collectors.toList());
                    generationsToPrint = generationsToPrint.stream().sorted((b,a)->b.getTheBestSolution().getGeneration().compareTo(a.getTheBestSolution().getGeneration()))
                            .collect(Collectors.toList());
                }
            }
        }

        DecimalFormat df = new DecimalFormat("###.#");
        for (int i = 0; i < generationsToPrint.size(); i++) {
            Double newScore = generationsToPrint.get(i).getTheBestSolution().getFitness();
            String st = "Generation: " + generationsToPrint.get(i).getTheBestSolution().getGeneration() +
                    "\n" + "Fitness score: " + df.format(newScore);
            if (i != 0) {
                Double lastScore = generationsToPrint.get(i - 1).getTheBestSolution().getFitness();
                Double res = new Double(newScore - lastScore);
                if (res > 0.0) {
                    st += "\nThe score improved by: " + df.format(res);
                } else if (res < 0.0) {
                    st += "\nThe score decreased by: " + df.format(res);
                } else {
                    st += "\nThere is no improvement from the previous generation";
                }
            }
            strings.add(st);
            strings.add("-----------------------");
        }

        if(strings.size() == 0)
        {
            strings.add("The number: " + this.whenToShow + " you have chosen is greater than the current number of generations: " +
                    size + "\n");
        }
        String st = strings.toString();
        String formatter = st.replace("{", "")
                .replace("}", "")
                .replace("]", "")
                .replace(",", "\n")
                .replace("[", "")
                .trim();
        return new DataTransferObject(formatter);
    }


    @Override
    public DataTransferObject exit() {
        if(thread.getState() == Thread.State.RUNNABLE)
        {
            synchronized (toStop) {
                toStop = true;
            }

            while (thread.getState() == Thread.State.RUNNABLE)
            {
                //busy wait....
            }
        }
        return new DataTransferObject("bye bye...");
    }

    public boolean isExistsXML()
    {
        return (timeTable != null);
    }

    @Override
    public int getMinNumGeneration() {
        return MIN_NUM_OF_GENERATION;
    }

    @Override
    public DataTransferObject getTimeTable() {
        return new DataTransferObject(this.timeTable);
    }
    @Override
    public DataTransferObject getBestSolutionTable(){
        return new DataTransferObject(this.evolutionEngine.getTheBestSolution());
    }
}
