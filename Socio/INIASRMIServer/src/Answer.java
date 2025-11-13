import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
/*
 * Answer.java
 *
 * Created on 12 ßíâàðü 2004 ã., 15:35
 */

/**
 *
 * @author  Administrator
 */

public class Answer extends UnicastRemoteObject implements AnswerInterface, Serializable {
    
    private String  Name;
    private Integer Key;
    private Integer AnswerPos;
    private boolean Textable=false;
    private INIACRMIInterface Server=null;
    
    
    /** Creates a new instance of Answer */
    public void setTextable(boolean Textable, boolean Flag) throws RemoteException {
        this.Textable=Textable;
        if (Flag) Server.StoreDQÀ(this, 2);
    };

    public boolean getTextable() throws RemoteException {
        return Textable;
    };

    public boolean RenameAnswer() throws RemoteException {   
        return true;
    };
    public Answer(INIACRMIInterface aServer, String aName, Integer aKey, boolean Flag) throws RemoteException{
        Server=aServer;
        Name=aName;
        Key = aKey;
    }
    
    public String getName() throws RemoteException{
        return Name;
    }
    
    public void setServer(INIACRMIInterface aServer) throws java.rmi.RemoteException {
        Server = aServer;
    };    
    
    public Integer getID() throws RemoteException{
        return Key;
    }
    
    public Integer getPos() throws java.rmi.RemoteException {
        return AnswerPos;
    };
    
    public void setPos(Integer Pos) throws java.rmi.RemoteException {
        AnswerPos=Pos;
    };
    
    public int setName(String aName, Integer LibID, boolean Flag) throws RemoteException{
        int ResultFlag=Server.askArbitr(this,2,LibID);        
        if (ResultFlag==0) {
            Name = aName;
            if (Flag) Server.StoreDQÀ(this, 1);
        }
        return ResultFlag;
    }
    
    public RInterface addElement(Integer Key, Integer LibID, boolean Flag) throws RemoteException {
        return null;
    }

    public int getType() throws RemoteException {
        return 8;
    }    
    
    public RInterface getByPos(Integer Key) throws RemoteException {
        return null;
    }

    public int delByPos (Integer QuestionPos, Integer LibID, boolean Flag) throws RemoteException {
        return 0;
    };

    public int delByID  (Integer QuestionPos, Integer LibID, boolean Flag) throws RemoteException {
        return 0;
    };
    
    public int getSize() throws RemoteException {
        return 0;
    }
    public ArrayList getKeys() throws RemoteException {
        return null;
    }



}
