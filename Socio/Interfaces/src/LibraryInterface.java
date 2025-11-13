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
public interface LibraryInterface extends Remote{
    public void Functionality ()  throws RemoteException;
    public void RefreshDataList () throws RemoteException;
    public void RefreshDictionaryList() throws RemoteException;
    public void RefreshQuestionList() throws RemoteException;
    public void RefreshDimensionList() throws RemoteException;
    public Integer getID() throws RemoteException;
}
