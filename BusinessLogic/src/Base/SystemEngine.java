package Base;

import Exceptions.SomethingDoesntExistException;
import Exceptions.UserMustConfirmException;
import Exceptions.WrongValueException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface SystemEngine {

    public Base.DataTransferObject readXML(String xmlPath) throws JAXBException, FileNotFoundException;
    public Base.DataTransferObject showSettingAndProperties();
    public void startEvolutionAlgorithm(int generationNum, int fitness, boolean confirm) throws WrongValueException, SomethingDoesntExistException, UserMustConfirmException;//TODO: return value must be Data TransferObject
    public Base.DataTransferObject showBestSolution(String solutionToString) throws SomethingDoesntExistException;
    public Base.DataTransferObject getBestSolutionTable(String solutionToString) throws SomethingDoesntExistException;
    public Base.DataTransferObject showAlgorithmProcess() throws SomethingDoesntExistException;
    public Base.DataTransferObject exit();

    public int getMinNumGeneration();

    public Base.DataTransferObject getTimeTable();

    public Base.DataTransferObject getBestSolutionTable();
}
