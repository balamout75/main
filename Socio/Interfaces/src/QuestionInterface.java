import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.lang.String;
//import java.io.Serializable;
//import java.io.*;
import java.util.ArrayList;



/**
 *
 * @author  Administrator
 */
public interface QuestionInterface extends Remote, DQAInterface  {
     public void ClearAnswer() throws RemoteException;
     //public void delAnswer(Integer AnswerID) throws RemoteException;
     public AnswerInterface newAnswer(String Str, Integer Key, boolean Flag) throws RemoteException;
     public void setQuestionType(int Type, boolean Flag) throws RemoteException;
     public int getQuestionType() throws RemoteException;
     public AnswerInterface getAnswer(Integer AnswerID) throws RemoteException;
     public Integer getBase() throws java.rmi.RemoteException;
     public void setBase(Integer aBase, boolean Flag) throws java.rmi.RemoteException;
     public Integer getMaxAnswerCount() throws java.rmi.RemoteException;
     public void setMaxAnswerCount(Integer aMaxAnswerCount, boolean Flag) throws java.rmi.RemoteException;
     public void setRepetition(int Type, boolean Flag) throws RemoteException;
     public int getRepetitionType() throws RemoteException;
}
