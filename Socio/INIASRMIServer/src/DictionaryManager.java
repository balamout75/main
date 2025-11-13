/*
 * DictionaryManager.java
 *
 * Created on 15 Январь 2004 г., 19:37
 */
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
//import java.rmi.Remote;
//import java.rmi.RMISecurityManager;
//import java.rmi.Naming;
//import java.rmi.registry.LocateRegistry;

//import java.net.InetAddress;

//import java.io.InputStreamReader;
//import java.io.BufferedReader;
//import java.io.Serializable;
import java.util.ArrayList;
//import java.util.List;
import java.util.Hashtable;
import java.io.*;


/**
 *
 * @author  Administrator
 */

/*
    Hashtable numbers = new Hashtable();
    numbers.put("one", new Integer(1));
    numbers.put("two", new Integer(2));
    numbers.put("three", new Integer(3));
    To retrieve a number, use the following code: 
    Integer n = (Integer)numbers.get("two");
    if (n != null) {
         System.out.println("two = " + n);
    }
*/

public class DictionaryManager extends UnicastRemoteObject implements DictionaryInterface, Serializable, DQAInterface {
    
    /* Creates a new instance of DictionaryManager */
    private ArrayList ClientCollection;
    private transient SocioRefreshInterface ClientRefresher;
    private ArrayList QuestCollection;
   //private ArrayList QuestNumCollection;
    //private Hashtable Questions;
    private String DictionaryName;
    private Integer DictionaryID;
    private Integer DictionaryPos;
    public INIACRMIInterface Server;
    
    public DictionaryManager(INIACRMIInterface aServer, String Name,Integer ID, boolean Flag) throws RemoteException {
        Server=aServer;
        DictionaryID = ID;
        QuestCollection = new ArrayList();
        //QuestNumCollection = new ArrayList();
        ClientCollection = new ArrayList();
        DictionaryName = Name;
        if (Flag) {
            Server.StoreDQА(this, 0);
            Server.StoreDQА(this, 1);
        }
    }

    public int getType() throws RemoteException {
        return 0;
    }   
    
    public Integer getID() throws RemoteException {
        return DictionaryID;
    };

    public void setServer(INIACRMIInterface aServer) throws RemoteException {
        Server = aServer;
    };
    
    public Integer getPos() throws RemoteException {
        return DictionaryPos;
    };
    
    /*public int setPos(Integer Pos, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {  
            DictionaryPos=Pos;
        }
        return ResultFlag;
    };*/
        
    public QuestionInterface newQuestion(String Str,Integer IDKey, boolean Flag) throws RemoteException {
        QuestionManager CurrQuest = new QuestionManager(Server, Str, IDKey, Flag);
        QuestCollection.add(CurrQuest);
        System.out.println(QuestCollection.size());
        if (Flag) {
            Server.StoreDQА(CurrQuest, 0);
            Server.StoreDQА(CurrQuest, 1);
        }
        return CurrQuest;
    };
    
    
    public RInterface getByPos(Integer Pos) throws RemoteException {
        int index=0;
        boolean SimFlag=false;
        RInterface QI=null;
        while ((index<QuestCollection.size())&!SimFlag){
            QI=(RInterface)QuestCollection.get(index);
            //System.out.println("Номер позиции вопроса  "+QI.getPos());
            SimFlag=((Pos.compareTo(QI.getPos()))==0); 
            ++index;
         }
        if (!SimFlag) QI=null;
         //QuestionInterface Quest = (QuestionInterface)QuestCollection.get(QuestionIndex);
        return QI;
    }
    
    public RInterface getRQuestionID(Integer IDKey) throws RemoteException {
        int index=0;
        boolean SimFlag=false;
        RInterface QI=null;
        while ((index<QuestCollection.size())&!SimFlag){
            QI=(RInterface)QuestCollection.get(index);
            //System.out.println("Номер позиции вопроса  "+QI.getID());
            SimFlag=((IDKey.compareTo(QI.getID()))==0); 
            ++index;
         }
        if (!SimFlag) QI=null;
         //QuestionInterface Quest = (QuestionInterface)QuestCollection.get(QuestionIndex);
        return QI;
    }
    
    public void cleanQuestionPos() throws RemoteException {
        RInterface Q=null;
        int suffix=0;
        int index=0;
        while (index<QuestCollection.size()-suffix){
            Q=getByPos(index);
            if (Q==null) --suffix;
            else
                if (suffix!=0) Q.setPos(new Integer(Q.getPos().intValue()+suffix));
            ++index;
        }
    }

    public int delByPos(Integer QuestionPos, Integer LibID, boolean Fl) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {  
          int index=0;
          RInterface Q=null;
          while ((index<QuestCollection.size())){
            Q=getByPos(index);
            if (Q!=null)
                if (Q.getPos().compareTo(QuestionPos)==0) {
                    QuestCollection.remove(Q);
                    --index;
                    cleanQuestionPos();
                    if (Fl) {
                        Server.StoreDQА(this, 2);
                        Server.StoreDQА(Server.getMainDictionary().getQuestion(Q.getID()), 13);
                    }
                    //Server.StoreDQА(this, 2);
                }
            ++index;
          }
        }
        return ResultFlag;  
    };
    
    public int delByID(Integer QuestID, Integer LibID, boolean Fl) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {  
          int index=0;
          RInterface Q=null;
          while ((index<QuestCollection.size())){
            Q=getByPos(index);
            if (Q!=null)
                if (Q.getID().compareTo(QuestID)==0) {
                    QuestCollection.remove(Q);
                    cleanQuestionPos();
                    if (Fl) {
                        Server.StoreDQА(this, 2);
                        Server.StoreDQА(Server.getMainDictionary().getQuestion(Q.getID()), 13);
                    }
                    --index;
                }
            ++index;
          }
        }
        return ResultFlag;
    };
    
    
    public void ClearQuest() throws RemoteException {
        QuestCollection.clear();
    };
    
    public int setName(String aName, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {  
            DictionaryName=aName;
            if (Flag) Server.StoreDQА(this, 1);
        }
        return ResultFlag;
    };
    
    public QuestionInterface getQuestion(Integer IDKey) throws RemoteException {
        int index=0;
        boolean SimFlag=false;
        QuestionInterface QI=null;
        while ((index<QuestCollection.size())&!SimFlag){
            QI=(QuestionInterface)QuestCollection.get(index);
            SimFlag=((QI.getID().compareTo(IDKey))==0); 
            ++index;
         }
        if (!SimFlag) QI=null;
         //QuestionInterface Quest = (QuestionInterface)QuestCollection.get(QuestionIndex);
        return QI;
    }
    
    public ArrayList getKeys() throws RemoteException {
        ArrayList Res=new ArrayList();
        Class CL=new RPointer(null).getClass();
        try {
            for (int i = 0; i < QuestCollection.size(); i++) {
                Object O=QuestCollection.get(i);
                if (O.getClass().equals(CL)) {
                    Res.add(((RInterface)O).getID());
                } else {
                    QuestionInterface QI=(QuestionInterface)O;
                    Res.add(QI.getID());
                }
            }
        } catch( Exception e){
            System.out.println("Ошибка "+e.toString());
    	}     
        return Res;
    }
    
    public RInterface addElement(Integer Key, Integer LibID, boolean Flag) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        RPointer RQM=null;
        if (ResultFlag==0) {         
            RQM=new RPointer(Key);
            RQM.setPos(new Integer(QuestCollection.size()));
            QuestCollection.add(RQM);
            if (Flag) Server.StoreDQА(this, 2);
        }
        return RQM;
    }
    
    public void RegisterClient(SocioRefreshInterface Refresher) throws RemoteException {
        ClientRefresher=Refresher;
        ClientCollection.add(Refresher);
    }
    
    public ArrayList qetArray() throws RemoteException {
        return QuestCollection;
    }
    
    public int getSize() throws RemoteException {
        //System.out.println("Size"+QuestCollection.size());
        return QuestCollection.size();
    }
    
    public String getName() throws RemoteException {
        return DictionaryName;
    }
    
    public String getSQLConstructor() throws RemoteException {
        int index=0;
        String SQL="";
        while (index<QuestCollection.size()) {
            QuestionInterface Quest = (QuestionInterface)QuestCollection.get(index);
            SQL=SQL+Quest.getName();
            ++index;
        }
        return SQL;
    }    
    

;
    
}
