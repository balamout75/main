/*
 * SocioInputInterface.java
 *
 * Created on 17 январь 2004 г., 19:23
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author  Administrator
 */
public interface SocioRefreshInterface  extends Remote {
    public void Register (SocioInputInterface aParent) throws RemoteException;
    public void Refresh() throws RemoteException;
    public void Refresh2() throws RemoteException;
 
}
