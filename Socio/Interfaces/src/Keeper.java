import java.rmi.Remote;
import java.rmi.RemoteException;
//import java.lang.String;
//import java.io.Serializable;
//import java.io.*;
import java.util.ArrayList;



/**
 *
 * @author  Administrator
 */
public interface Keeper extends Remote  {
     public void clearChild() throws RemoteException;
     public DQAInterface newChild(String Str, Integer Key, boolean Flag) throws RemoteException;
     public DQAInterface getChild(Integer AnswerID) throws RemoteException;
}
