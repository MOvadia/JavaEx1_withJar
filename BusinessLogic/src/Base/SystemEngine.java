package Base;

import Exceptions.SomethingDoesntExistException;
import Exceptions.UserMustConfirmException;
import Exceptions.WrongValueException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface SystemEngine {

    public DataTransferObject readXML(String xmlPath, boolean confirm) throws JAXBException, FileNotFoundException;
    public DataTransferObject showSettingAndProperties();
    public void startEvolutionAlgorithm(int generationNum, int whenToShow, boolean confirm) throws WrongValueException, SomethingDoesntExistException, UserMustConfirmException;//TODO: return value must be Data TransferObject
    public DataTransferObject showBestSolution(String solutionToString) throws SomethingDoesntExistException;
    public DataTransferObject getBestSolutionTable(String solutionToString) throws SomethingDoesntExistException;
    public DataTransferObject showAlgorithmProcess() throws SomethingDoesntExistException;
    public DataTransferObject exit();

    public int getMinNumGeneration();

    public DataTransferObject getTimeTable();

    public DataTransferObject getBestSolutionTable();
}
