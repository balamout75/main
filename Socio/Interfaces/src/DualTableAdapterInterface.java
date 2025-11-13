/*
 * Interface.java
 *
 * Created on 7 Сентябрь 2004 г., 9:22
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.io.Serializable;
//import java.sql.*;
import java.util.Vector;


/**
 *
 * @author  riac
 */
public interface DualTableAdapterInterface extends Remote  {
  public Vector getVector() throws RemoteException;
  public String getItem(int XRow,int YRow, int mode) throws RemoteException;
  public Vector Reload() throws RemoteException;
  public boolean changeDimension(RInterface aRQX, RInterface aRQY) throws RemoteException;
  public void executeQuery() throws RemoteException;
  public int getColumnCount() throws RemoteException;
  public int getRowCount() throws RemoteException;
  public DictionaryInterface getDictionary() throws RemoteException;
  public  INIACRMIInterface getServer() throws RemoteException;
  public void setMode(int aMode) throws RemoteException;
  public void setClearence(boolean aClearence) throws RemoteException;

}
