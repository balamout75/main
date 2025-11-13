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
public interface TableAdapterInterface extends Remote  {
  //public int getRowCount() throws RemoteException;
  //public int getColumnCount() throws RemoteException;
  //public Object getValueAt(int row, int column) throws RemoteException;
  public Vector InsertRow() throws RemoteException;
  public Vector DeleteRow() throws RemoteException;
  public Vector NextRow() throws RemoteException;
  public Vector PrevRow() throws RemoteException;
  public Vector FirstRow() throws RemoteException;
  public Vector LastRow() throws RemoteException;
  public Vector Reload() throws RemoteException;
  public int Size() throws RemoteException;
  public void executeQuery() throws RemoteException;
  public boolean getFree() throws RemoteException;
  //public void execSQL(String Str) throws RemoteException;
  public Vector getRow(int Row) throws RemoteException;
  public int getCurrRow() throws RemoteException;
  public DictionaryInterface getDictionary() throws RemoteException;
  public INIACRMIInterface getServer() throws RemoteException;
  public String getColumnNames(int Row) throws RemoteException;
  public void updateTable(String Value, int FieldNum, int fixMode) throws RemoteException;
  public boolean setInputPanel(InputPanelRefresherInterface IPI) throws RemoteException;
}
