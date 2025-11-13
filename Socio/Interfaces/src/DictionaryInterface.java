import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DictionaryInterface extends Remote, DQAInterface {
      public void ClearQuest() throws RemoteException;
      public void RegisterClient(SocioRefreshInterface Refresher)  throws RemoteException;
      public QuestionInterface newQuestion(String Str, Integer IDKey, boolean Flag) throws RemoteException;

      public RInterface getRQuestionID(Integer IDKey) throws RemoteException;      
      public ArrayList qetArray() throws RemoteException;
      public QuestionInterface  getQuestion(Integer QuestionID) throws RemoteException;
      public String getSQLConstructor() throws RemoteException;
      //public void delQuestion(Integer QuestionID) throws RemoteException;

};

