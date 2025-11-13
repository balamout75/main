/*
 * InputPanelRefresherInterface.java
 *
 * Created on 14 Сентябрь 2004 г., 16:43
 */
import java.rmi.RemoteException;
import java.rmi.Remote;


/**
 *
 * @author  riac
 */
public interface InputPanelRefresherInterface extends Remote{
    //public void InputPanelRefresher(InputPanelInterface aParent) throws RemoteException;
    public void setCurrent(int Curr) throws RemoteException;
    public void setSize(int Size) throws RemoteException;    
    public void Refresh() throws RemoteException;    
}
