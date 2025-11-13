/*
 * SocioInputInterface.java
 *
 * Created on 17 Январь 2004 г., 19:23
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author  Administrator
 */
public interface LibraryRefreshInterface  extends Remote {
    public void     Register (LibraryInterface aParent) throws RemoteException;
    public void     Refresh() throws RemoteException;
    public Integer  getID() throws RemoteException;
    public void     RefreshDataList() throws RemoteException;
    public void     RefreshLocalClient() throws RemoteException;
    public void     RefreshQuestionList() throws RemoteException;
    public void     RefreshDimensionList() throws RemoteException;

 
}
