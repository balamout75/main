                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             /*
 * DNDCompInterface.java
 *
 * Created on 20 Апрель 2005 г., 10:20
 */
//import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 *
 * @author riac
 */
public interface DNDCompInterface {
 public void addElement(QuestionInterface QI)  throws RemoteException ;
 public void removeElement(int i) throws RemoteException ;
}
