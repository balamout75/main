/*
 * SocioRefreshClient.java
 *
 * Created on 17 январь 2004 г., 19:49
 */
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import java.net.InetAddress;

import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 *
 * @author  Administrator
 */
public class SocioRefreshClient extends UnicastRemoteObject implements SocioRefreshInterface {
    private SocioInputInterface Parent;
    
    /** Creates a new instance of SocioRefreshClient */
    public SocioRefreshClient() throws RemoteException {
        //Parent.Functionality();
    }
    
    
    public void SocioRefreshClient(SocioInputInterface aParent) throws RemoteException {
        Parent=aParent;
    }
     
        public void Register(SocioInputInterface aParent) throws RemoteException {
        Parent=aParent;
    }
    
    public void Refresh() throws RemoteException {
        //Parent.Functionality();
    }
    
    public void Refresh2() throws RemoteException {
        //Parent.Functionality2();
    }
    
}
