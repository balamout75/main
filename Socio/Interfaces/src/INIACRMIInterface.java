/*
 * INIACRMIInterface.java
 *
 * Created on 6 Январь 2004 г., 14:19
*/

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;
import java.io.Serializable;
import java.util.ArrayList;

public interface INIACRMIInterface extends Remote, Serializable {
    //declare public methods. Remote methods must throw RemoteException
    //declaring the methods that the moving Dictionari Engine
    
    public int askArbitr(DQAInterface DQA, int type, Integer LibID) throws RemoteException;
    public void Register (LibraryRefreshInterface aLib) throws RemoteException;
    public DictionaryInterface GetEntity (SocioRefreshInterface Refresher) throws RemoteException;
    public UserInterface autorizeUser (String Name,String Password) throws RemoteException;
    
    
    public DictionaryInterface newDictionary (String Name, boolean Flag) throws RemoteException;
    public DictionaryInterface newDictionary (String Name,Integer IDKey, boolean Flag) throws RemoteException;
    //public boolean ReleaseDictionary (int DictionaryIndex) throws RemoteException;
    //public boolean ReleaseDictionary (DictionaryInterface DI) throws RemoteException;
    public boolean DeleteDictionary (int DictionaryIndex) throws RemoteException;
    //public boolean DeleteDictionary (DictionaryInterface DI) throws RemoteException;

    public DataModuleInterface getDMI (Integer DictionaryIndex) throws RemoteException;
    public ArrayList getDMIKeys() throws RemoteException;
    //public  void addDMI (DataModuleInterface DMI) throws RemoteException;
    public abstract  DataModuleInterface newDMI (String aName, Integer ID, int type, boolean Flag) throws RemoteException;

    
    //public DictionaryInterface GetDictionary (int DictionaryIndex) throws RemoteException;
    public DictionaryInterface getDictionary (Integer DictionaryIndex) throws RemoteException;
    public int GetDictionarySize () throws RemoteException;
    public boolean SetRefresher (SocioRefreshInterface Refresher) throws RemoteException;
    public DataModuleInterface getDataModule () throws RemoteException;
    
    public String getQuestName (int QuestNum) throws RemoteException;
    public String getAnswers (int QuestNum) throws RemoteException;
    public String getAnswer () throws RemoteException;
    //public void NewQuest (String Name) throws RemoteException;
    //public AnswerInterface NewAnswer (String Name) throws RemoteException;
    
    public void setQuestName (int QuestNum, String Name) throws RemoteException;
    public void setAnswers (int QuestNum, String Name) throws RemoteException;
    public void setAnswer (int QuestNum, int AnswerNum, String Name) throws RemoteException;
    
    public DictionaryInterface getMainDictionary()  throws RemoteException;
    public QuestionInterface getMainQuestion()  throws RemoteException;
    public ArrayList getDictionaryKeys() throws RemoteException;
   
    
    //public void delQuest(int QuestNum) throws RemoteException;
    public void delAnswer(int QuestNum, int AnswerNum) throws RemoteException;
    //public void RecodeQuestion() throws RemoteException;
    //public void RecodeAnswers() throws RemoteException;
    //declaring the methods that the moving Record Entring
    //public void WriteDictionaries() throws RemoteException;
    public void StoreAllBase() throws RemoteException;
    public void ReadSQLDictionaries() throws RemoteException;

    public void         ReadDictionaries() throws RemoteException;
    public int          GetDictionaryIndexOf(DictionaryInterface Object)  throws RemoteException;
    public Statement    getStatement() throws RemoteException;
    public ResultSet    executeQuery(String Query, Statement statement) throws RemoteException;
    public boolean      executeSQL(String Query, Statement statement) throws RemoteException;
    public Integer      getKey() throws RemoteException;
    public String       MakeTable() throws RemoteException;
    public void         StoreDMI(Integer ID, int type) throws RemoteException;
    public void         StoreDMI(DataModuleInterface DMI, int type) throws RemoteException;
    public void         StoreDQА(DQAInterface DQA, int type) throws RemoteException;
    public RInterface   getDMIRootPointer() throws RemoteException;    
    
    
    //public void         DQАStore(Integer ID, int type) throws RemoteException;

    //public String MakeInsertSQL(String TableName) throws RemoteException;
    //public String MakeInsertSQL(String TableName, String StrSQL) throws RemoteException;
}
