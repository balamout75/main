/*
 * JavaModuleInterface.java
 *
 * Created on 13 Март 2004 г., 18:43
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.rmi.RemoteException;
//import java.util.ArrayList;


/**
 *
 * @author  Иван
 */
public interface UserInterface  extends Remote{
    public Integer  getRootID(int i)                        throws RemoteException;
    public boolean  addRootID(Integer ID, boolean Flag)     throws RemoteException;
    public boolean  delRootID(Integer ID)                   throws RemoteException;
    public RInterface addElement(Integer IDKey, Integer IDLib, boolean Flag) throws RemoteException;
    
    public String   getName()                               throws RemoteException;
    public boolean  setName(String Name, boolean Flag)      throws RemoteException;
    
    public String   getPassword()                           throws RemoteException;
    public boolean  setPassword(String Name, boolean Flag)  throws RemoteException;    
    
    public int      getRole()                               throws RemoteException;
    public boolean  setRole(int Name, boolean Flag)         throws RemoteException;       
    
    public int      getType()                               throws RemoteException;
    public boolean  setType(int Name, boolean Flag)         throws RemoteException;   

    public String   getComment()                            throws RemoteException;
    public boolean  setComment(String Name, boolean Flag)   throws RemoteException;
    
    public Integer  getParentID ()                          throws RemoteException;
    public boolean  setParentID (Integer ID, boolean Flag)  throws RemoteException;
    
    public boolean              setServer(INIACRMIInterface aServer)    throws RemoteException;
    public INIACRMIInterface    getServer()             throws RemoteException;
    
    public boolean  setID(Integer ID, boolean Flag)         throws RemoteException;
    public Integer  getID()                                 throws RemoteException;
}
