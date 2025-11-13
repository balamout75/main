import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.io.*;

public class ManagerUserGroup extends UnicastRemoteObject {
   
    private ArrayList UserCollection;
    private INIACRMIInterface Server;
    
    public ManagerUserGroup(INIACRMIInterface aServer, boolean Flag) throws RemoteException {
        Server=aServer;
        UserCollection = new ArrayList();
        if (Flag) {
        }        
    }

    
    public void ClearUsers() throws RemoteException {
        UserCollection.clear();
    };
    
    public UserInterface newUser(String Str,Integer IDKey, boolean Flag) throws RemoteException {
        User CurrUser = new User();
        CurrUser.setName(Str,Flag);
        CurrUser.setID  (IDKey,Flag);
        UserCollection.add(CurrUser);
        return CurrUser;
    };
    
    public UserInterface newUser() throws RemoteException {
        User CurrUser = new User();
        UserCollection.add(CurrUser);
        return CurrUser;
    };    
    
    
    public UserInterface getUser (Integer UserIndex) throws RemoteException {
        UserInterface UI=null;
        int index=0;
        boolean Flag=true;
        while ((index<UserCollection.size())&Flag) {
            UserInterface CurrUI= (UserInterface) UserCollection.get(index);
            if (UserIndex.compareTo(CurrUI.getID())==0) {
                UI=CurrUI;
                Flag = false;
            }
            index++;
        };
        return UI;
    }
    
    public UserInterface getUser (String Name) throws RemoteException {
        UserInterface UI=null;
        int index=0;
        boolean Flag=true;
        while ((index<UserCollection.size())&Flag) {
            UserInterface CurrUI= (UserInterface) UserCollection.get(index);
            if (Name.equalsIgnoreCase(CurrUI.getName())) {
                UI=CurrUI;
                Flag = false;
            }
            index++;
        };
        return UI;
    }
    
     public ArrayList qetArray() throws RemoteException {
        return UserCollection;
    }
    
    public int getSize() throws RemoteException {
        //System.out.println("Size"+QuestCollection.size());
        return UserCollection.size();
    }
    

;
    
}