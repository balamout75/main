/*
 * InputPanelInterface.java
 *
 * Created on 14 Сентябрь 2004 г., 10:50
 */

/**
 *
 * @author  riac
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.*;

public interface InputPanelInterface extends Remote {
    public boolean setCurrent(int S) throws RemoteException;
    public boolean setSize(int S) throws RemoteException;
}
