/*
 * SocioInputInterface.java
 *
 * Created on 17 январь 2004 г., 19:53
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author  Administrator
 */
public interface QuestInputInterface extends Remote{
    public void RefreshDictionaryList ()  throws RemoteException;
    public void Functionality2 ()  throws RemoteException;
    public void RefreshQuestionList ()  throws RemoteException;
    public void RefreshAnswerList ()  throws RemoteException;
}
