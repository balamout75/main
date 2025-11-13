/*
 * SocioRefreshClient.java
 *
 * Created on 17 Январь 2004 г., 19:49
 */
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
/*import java.rmi.Remote;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.net.InetAddress;
import java.io.InputStreamReader;
import java.io.BufferedReader;*/

/**
 *
 * @author  Administrator
 */
public class LibraryRefreshClient extends UnicastRemoteObject implements LibraryRefreshInterface {
    private Library Parent;
    private Integer IDKey;
    
    
    public LibraryRefreshClient() throws RemoteException {
        //Parent.Functionality();
    }
    
    public LibraryRefreshClient(Library Library, Integer aIDKey) throws RemoteException {
        Parent = Library;
        IDKey  = aIDKey;
        //Parent.Functionality();
    }    
    
    public Integer getID() throws RemoteException {
        return IDKey;
        //Parent.Functionality();
    }
    
    public void Refresh() throws RemoteException {
        System.out.println( "Refresher check" );	
        //Object[] options = {"Да","Нет","Отмена"};
        //int n = JOptionPane.showOptionDialog(null,"Вы желаете перенести?","Вопрос",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);                
        //Parent.Functionality();
    }
       
    public void RefreshLocalClient() throws RemoteException {
        System.out.println( "Refresher LOCAL check" );	        
        //Object[] options = {"Да","Нет","Отмена"};
        //JOptionPane.showMessageDialog(Parent, "ПРивет");
        //int n = JOptionPane.showOptionDialog(null,"Вы желаете перенести папку?","Вопрос",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);        
        //Parent.Functionality();
    }
    
    public void RefreshQuestionList() throws RemoteException {
        System.out.println( "Обновляем Набор Словарей" );	
        Parent.RefreshQuestionList();
        //Parent.Functionality();
    }
      
    public void Register(LibraryInterface aParent) throws RemoteException {
        //Parent=aParent;
    }    
    
    public void RefreshDataList() throws RemoteException {
        System.out.println( "Кто то кнопку 31 нажал" );	
        Parent.RefreshDataList();
        System.out.println( "Кто то кнопку 32 нажал" );	
        
    }
    
    public void RefreshDimensionList() throws RemoteException {
        System.out.println( "Кто то кнопку 31 нажал" );	
        Parent. RefreshDimensionList();
        System.out.println( "Кто то кнопку 32 нажал" );	
    }
}
