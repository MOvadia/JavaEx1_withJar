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
import java.util.*;

public class Engine implements SystemEngine{
    private Base.TimeTable timeTable;
    private EvolutionEngine evolutionEngine;
    private XmlLoader xml;
    private int generationNum = 0;
    private boolean isExistsSolution = false;
    private int whenToShow;
    private final int MIN_NUM_OF_GENERATION = 100;

    @Override
    public Base.DataTransferObject readXML(String xmlPath) throws JAXBException, FileNotFoundException {
        XmlLoader tmpXML = new XmlLoader();
        tmpXML.setXmlPath(xmlPath);
        Base.TimeTable tmpT = new Base.TimeTable(tmpXML.getDescriptor());
        EvolutionEngine tmpE = new EvolutionEngine(tmpXML.getDescriptor().getETTEvolutionEngine());
        xml = tmpXML;
        timeTable = tmpT;
        evolutionEngine = tmpE;
        return new Base.DataTransferObject("The XML file was successfully uploaded to the system.\n");
    }

    @Override
    public Base.DataTransferObject showSettingAndProperties() throws SomethingDoesntExistException {
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        return new Base.DataTransferObject(timeTable,evolutionEngine);
    }

    @Override
    public void startEvolutionAlgorithm(int generationNum, int whenToShow, boolean confirm) throws WrongValueException, SomethingDoesntExistException, UserMustConfirmException {
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        if(isExistsSolution && !confirm)
        {
            throw new UserMustConfirmException();
        }
     /*   if(generationNum <=100)
        {
            throw new WrongValueException(generationNum);
        } */
        if(whenToShow < 1)
        {
            throw new WrongValueException(whenToShow);
        }
        this.generationNum = generationNum;
        this.whenToShow = whenToShow;

        initialPopulation();
        for(int i = 0; i < generationNum; i++) {
            this.evolutionEngine.calculateFitness(timeTable);
            Population theElected = this.evolutionEngine.startSelection();
            this.evolutionEngine.startCrossover(this.timeTable, theElected);
            this.evolutionEngine.startMutation(this.timeTable);
        }
        isExistsSolution = true;
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
        int numOfGenerations = evolutionEngine.getGenerations().size() + 1;
        evolutionEngine.getGenerations().put(numOfGenerations,population);
    }

    private OptionalSolution creatOptionalSolution()
    {
        Random rnd = new Random();
        Map<Integer, Base.Raw> optionalSolution = new HashMap<>();
        int optionalSolutionSize = rnd.nextInt(timeTable.getDays() * timeTable.getHours() * timeTable.getMaxCT());
        optionalSolutionSize = optionalSolutionSize + (timeTable.getDays() * timeTable.getHours() * timeTable.getMinCT());

        for(int i = 1; i <= optionalSolutionSize; i++)
        {
            Base.Raw raw = creatRaw();
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

    private Base.Raw creatRaw()
    {
        int rndDay = this.timeTable.randomDay();
        int rndHour = this.timeTable.randomHour();
        Base.Teacher rndTeacher = this.timeTable.randomTeacher();
        Base.Subject rndSubject = this.timeTable.randomSubject();
        Base.SchoolClass rndClass = this.timeTable.randomSchoolClass();

        Base.Raw raw = new Base.Raw(rndDay,rndHour,rndTeacher,rndSubject,rndClass);
        return raw;
    }


    @Override
    public Base.DataTransferObject showBestSolution(String solutionToString) throws SomethingDoesntExistException{
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        if(!isExistsSolution)
        {
            throw new SomethingDoesntExistException("solution");
        }

        evolutionEngine.getTheBestSolution().setSolutionToString(solutionToString);
        return new Base.DataTransferObject(evolutionEngine.getTheBestSolution().toString());
    }

    public Base.DataTransferObject getBestSolutionTable(String solutionToString) throws SomethingDoesntExistException{
        if(!isExistsXML())
        {
            throw new SomethingDoesntExistException("XML file");
        }
        if(!isExistsSolution)
        {
            throw new SomethingDoesntExistException("solution");
        }

        evolutionEngine.getTheBestSolution().setSolutionToString(solutionToString);
        return new Base.DataTransferObject(evolutionEngine.getTheBestSolution().getTablePerIdentifier(), this.timeTable);
    }

    @Override
    public Base.DataTransferObject showAlgorithmProcess() throws SomethingDoesntExistException {
        if (!isExistsXML()) {
            throw new SomethingDoesntExistException("XML file");
        }
        if (evolutionEngine.getGenerations().size() == 0) {
            throw new SomethingDoesntExistException("generations");
        }

        List<String> strings = new LinkedList<>();
        for (int i = 1; i <= generationNum; i++) {
            if (i % this.whenToShow == 0) {
                Double newScore = this.evolutionEngine.getGenerations().get(i).getTheBestSolution().getFitness();
                String st = "Fitness score: " + newScore;
                if (this.evolutionEngine.getGenerations().get(i - 1) != null) {
                    Double lastScore = this.evolutionEngine.getGenerations().get(i - 1).getTheBestSolution().getFitness();
                    if (newScore - lastScore >= 0) {
                        double improvement = newScore - lastScore;
                        st += "\nThe score improved by: " + improvement;
                    } else if (newScore - lastScore < 0) {
                        double decreased = newScore - lastScore;
                        st += "\nThe score decreased by: " + decreased;
                    }
                }
                strings.add(st);
                strings.add("-----------------------");
            }
        }
        String st = strings.toString();
        String formatter = st.replace("{", "")
                .replace("}", "")
                .replace("]", "")
                .replace(",", "\n")
                .replace("[", "")
                .trim();
        return new Base.DataTransferObject(formatter);
    }


    @Override
    public Base.DataTransferObject exit() {
        return new Base.DataTransferObject("bye bye...");
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
    public Base.DataTransferObject getTimeTable() {
        return new Base.DataTransferObject(this.timeTable);
    }
    @Override
    public Base.DataTransferObject getBestSolutionTable(){
        return new Base.DataTransferObject(this.evolutionEngine.getTheBestSolution());
    }
}
