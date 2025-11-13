import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author riac
 */
public interface DQAInterface  extends Remote  {
    public String getName() throws RemoteException;
    public Integer getID() throws RemoteException;
    public int setName(String aName, Integer IDLib, boolean Flag) throws RemoteException;
    public int getType() throws RemoteException;
    //public Integer getPos() throws RemoteException;
    //public void setPos(Integer Pos) throws RemoteException;
    public ArrayList getKeys() throws RemoteException;
    public int getSize() throws RemoteException;
    public RInterface getByPos(Integer Pos) throws RemoteException;
    public int delByPos(Integer Pos, Integer IDLib, boolean Flag) throws RemoteException;
    public int delByID(Integer ID, Integer IDLib, boolean Flag) throws RemoteException;
    //public RInterface addElement(Integer IDKey) throws RemoteException;
    public RInterface addElement(Integer IDKey, Integer IDLib, boolean Flag) throws RemoteException;
}
