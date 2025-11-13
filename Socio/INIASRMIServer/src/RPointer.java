import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

public class RPointer extends UnicastRemoteObject implements RInterface, Serializable {
    public Integer AnswerPos;
    public Integer AnswerID;
    public int Rang;
    
    public RPointer(Integer ID) throws java.rmi.RemoteException  {
        AnswerID=ID;
    }
    
    public Integer getPos() throws java.rmi.RemoteException {
        return AnswerPos;
    };
    
    public void setPos(Integer Pos) throws java.rmi.RemoteException {
        AnswerPos=Pos;
    };

    public Integer getID() throws java.rmi.RemoteException {
        return AnswerID;
    };

    public void setID(Integer ID) throws java.rmi.RemoteException {
        AnswerID=ID;
    };
    
}
