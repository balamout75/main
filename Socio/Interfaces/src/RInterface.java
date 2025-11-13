import java.rmi.Remote;
//import java.rmi.RemoteException;
public interface RInterface extends Remote{
    public void setPos(Integer Pos) throws java.rmi.RemoteException ;
    public Integer getPos() throws java.rmi.RemoteException ;
    public void setID(Integer ID) throws java.rmi.RemoteException ;
    //public void setRang(int Rang) throws java.rmi.RemoteException ;
    public Integer getID() throws java.rmi.RemoteException ;
    //public int getRang() throws java.rmi.RemoteException ;
}
