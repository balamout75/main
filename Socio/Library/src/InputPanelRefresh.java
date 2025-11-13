import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Remote;


public class InputPanelRefresh extends UnicastRemoteObject implements InputPanelRefresherInterface {
    private InputPanelInterface Parent;

    public InputPanelRefresh(InputPanelInterface aParent) throws RemoteException {
        Parent = aParent;
    }
    
    public void setCurrent(int Curr) throws RemoteException {
        Parent.setCurrent(Curr);
    }
    
    public void setSize(int Size) throws RemoteException {
        Parent.setSize(Size);
    }
    
    public void Refresh (int Size) throws RemoteException {
        System.out.println("Произошли изменения");
    }
    
    public void Refresh () throws RemoteException {
        System.out.println("Произошли изменения");
    }
    
    public void InputPanelRefresher(InputPanelInterface aParent) throws RemoteException {
    }
    
}
