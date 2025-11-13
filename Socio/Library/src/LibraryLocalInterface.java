/*
 * SocioInputInterface.java
 *
 * Created on 17 Январь 2004 г., 19:53
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author  Administrator
 */
public interface LibraryLocalInterface extends Remote{
    public void addDataBlock(DataModuleInterface DMI) throws RemoteException;
}
