import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.io.Serializable;
/*
 * AnswerInterface.java
 *
 * Created on 29 Февраль 2004 г., 20:06
 */

/**
 *
 * @author  Administrator
 */
public interface AnswerInterface extends Remote, DQAInterface {
    /*public String getName() throws RemoteException;
    public Integer getID() throws RemoteException;
    public boolean setName(String aName) throws RemoteException;
    public Integer getPos() throws RemoteException;
    public void setPos(Integer Pos) throws RemoteException;*/
    public void setTextable(boolean Textable, boolean Flag) throws RemoteException;
    public boolean getTextable() throws RemoteException;
}
