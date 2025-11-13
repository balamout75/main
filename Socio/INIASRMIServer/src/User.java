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
public class User implements UserInterface, DQAInterface {
    public Integer      ID;
    public String       Name    ="";
    public String       Comments="";
    public String       Password="";
    public ArrayList    DataCollection;
    public Integer      ParentID;
    public INIACRMIInterface Server;
    
    

    public Integer getRootID(int i) throws RemoteException {
        return ID;
    }

    public boolean addRootID(Integer ID, boolean Flag) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean delRootID(Integer ID) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() throws RemoteException {
        return Name;
    }

    public boolean setName(String aName, boolean Flag) throws RemoteException {
        Name=aName;
        if (Flag) Server.StoreDQА(this, 1);        
        return true;        
    }

    public Integer getParentID() throws RemoteException {
        return ParentID;
    }

    public boolean setParentID(Integer ID, boolean Flag) throws RemoteException {
        ParentID=ID;
        return true;
    }

    public boolean setServer(INIACRMIInterface aServer) throws RemoteException {
        Server=aServer;
        return true;
    }

    public INIACRMIInterface getServer() throws RemoteException {
        return Server;
    }

    public boolean setID(Integer ID, boolean Flag) throws RemoteException {
        this.ID=ID;
        return true;
    }

    public Integer getID() throws RemoteException {
        return ID;
    }

    public String getComment() throws RemoteException {
        return Comments;
    }

    public boolean setComment(String Comments, boolean Flag) throws RemoteException {
        this.Comments = Comments;
        return true;
    }

    public String getPassword() throws RemoteException {
        return Password;
    }

    public boolean setPassword(String Password, boolean Flag) throws RemoteException {
        this.Password = Password;
        if (Flag) Server.StoreDQА(this, 3);
        return true;
    }

    public int setName(String aName, Integer IDLib, boolean Flag) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getType() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList getKeys() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSize() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RInterface getByPos(Integer Pos) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int delByPos(Integer Pos, Integer IDLib, boolean Flag) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int delByID(Integer ID, Integer IDLib, boolean Flag) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RInterface addElement(Integer IDKey, Integer IDLib, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,2,IDLib);
        RPointer RA=null;
        if (ResultFlag==0) {  
            RA=new RPointer(IDKey);
            RA.setPos(new Integer(DataCollection.size()));
            DataCollection.add(RA);
            if (Flag) Server.StoreDQА(this, 3);
        }
        return RA;
    }

    public int getRole() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setRole(int Name, boolean Flag) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setType(int Name, boolean Flag) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
   
}
