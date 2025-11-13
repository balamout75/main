import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
//import java.rmi.Remote;
//import java.rmi.RMISecurityManager;
//import java.rmi.Naming;
//import java.rmi.registry.LocateRegistry;

//import java.net.InetAddress;

//import java.io.InputStreamReader;
//import java.io.BufferedReader;
import java.util.ArrayList;
//import java.util.List;
//import javax.swing.JTree;
import java.io.Serializable;
//import java.sql.*;

/*
 * DataModul.java
 *
 * Created on 13 Март 2004 г., 18:41
 */

/**
 *
 * @author  Иван
 */
public class DataFolder extends UnicastRemoteObject implements DataModuleInterface, Serializable {
    public String  Name="";
    public String  Comment="";
    public Integer ID;
    public Integer Level;
    public String TableName="";
    public String Condition="";
    public ArrayList DataCollection;
    public Integer ParentID;
    public Integer DictionaryID=-1;
    public String  Annotation="";
    public String  StartDate="";
    public String  EndDate="";
    public Integer Quota=null;
    
    private transient LibraryRefreshInterface Library;
    private transient INIACRMIInterface Server;
    private transient ArrayList   TableConnectors;

    private boolean Editable=true;
    


    public int setID(Integer ID, Integer LibID, boolean Flag) throws RemoteException {   
        int ResultFlag=Server.askArbitr(this,0,LibID);
        if (ResultFlag==0) {
            this.ID = ID;
        }
        return ResultFlag;
    }
    
    public int setAnnotation   (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,7,LibID);
        if (ResultFlag==0) {        
            if (Flag) Server.StoreDMI(ID, 7);        
            Annotation=S;
        }
        return ResultFlag;            
    };
    
    public int setStartDate    (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,8,LibID);
        if (ResultFlag==0) {           
            StartDate=S;
            if (Flag) Server.StoreDMI(ID, 8);        
        }
        return ResultFlag;                        
    };
    
    public int setEndDate      (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,9,LibID);
        if (ResultFlag==0) {                   
            EndDate=S;
            if (Flag) Server.StoreDMI(ID, 9);
        }
        return ResultFlag;                            
    };
    
    public int setQuota        (Integer I, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,10,LibID);
        if (ResultFlag==0) {                           
            Quota=I;
            if (Flag) Server.StoreDMI(ID, 10);        
        }
        return ResultFlag;                            
    };
    
    public int setParent(Integer aParentID, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,7,LibID);
        if (ResultFlag==0) {                                   
            ParentID=aParentID;
            if (Flag) Server.StoreDMI(ID, 12);        
        }
        return ResultFlag;                                        
    };
    
    public String   getAnnotation   () throws RemoteException {
        return Annotation;
    };
    public String   getStartDate    () throws RemoteException {
        return StartDate;
    };
    public String   getEndDate      () throws RemoteException {
        return EndDate;
    };
    public Integer  getQuota        () throws RemoteException{
        return Quota;
    };
    public Integer   getParent() throws RemoteException {
        return ParentID;
    };    


    /** Creates a new instance of DataModul */
       
    /*public DataFolder(INIACRMIInterface aServer, Integer aParentID) throws RemoteException{
           this(aServer,aParentID,aServer.getKey());
           Server.DMIStore(ID, 0);  
    }*/

    public DataFolder(INIACRMIInterface aServer, Integer aParentID, Integer ID, boolean Flag) throws RemoteException{
        try { 
            Server=aServer;
            ParentID=aParentID;
            DataCollection = new ArrayList();
            Name = "Блоки данных";
            System.out.println( "Новый блок/группа" );
            TableConnectors = new ArrayList();
            this.ID=ID;
            //if (Flag) Server.StoreDMI(this, 0);        
            //if (Flag) Server.StoreDMI(this, 4);  
        } catch (Exception e) {
                System.out.println("Ошибка создания DataBlock" +e.getMessage());            
        }            
            //MakeTable();
    }    
    
    

    
    public Integer getID() throws RemoteException {    
        return ID;
    }    
    
    public void setLevel(Integer Level) throws RemoteException {    
        this.Level = Level;
    }
    
    public Integer getLevel() throws RemoteException {    
        return Level;
    }

    public void setServer(INIACRMIInterface aServer) throws RemoteException {
        Server=aServer;
        int index=0;
        DataModuleInterface DTI=null;
        while (index<DataCollection.size()){
            DTI=(DataModuleInterface)DataCollection.get(index);
            DTI.setServer(Server);
            ++index;
        }
    };
    
    public void initModules(INIACRMIInterface aServer) throws RemoteException {
        Server=aServer;
        int index=0;
        DataModuleInterface DTI=null;
        TableConnectors = new ArrayList();
        while (index<DataCollection.size()){
            DTI=(DataModuleInterface)DataCollection.get(index);
            DTI.initModules(Server);
            ++index;
        }
    };
    
    public String SqlCreate() throws RemoteException {
        return "";
    }
    
    public String makeMesureSQL(Integer Q1, int A1, Integer Q2, int A2) throws RemoteException {
        return "";        
    }

    public String makeCreateSQL() throws RemoteException {
        return "";        
    }

    public String makeSelectSQL() throws RemoteException {
        String Str  ="Select ";
        try { 
            if (DictionaryID>-1) {
                DictionaryInterface DI=Server.getDictionary(DictionaryID);
                int index=0;
                while (index<DI.getSize()){
                    RInterface RQ1=DI.getByPos(index);
                    Str=Str+"Quest"+RQ1.getID()+",";
                    index++;
                }
                Str=Str.substring(0,Str.length()-1);
                Str = Str+" from "+TableName+" order by Row_ID";
                
            } else Str="";
        } catch (Exception e) {
            System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
            Str="";
	}    
        return Str;
    }

    /*public int setParent(Integer aParentID, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,7,LibID);
        if (ResultFlag==0) {                                   
            ParentID=aParentID;
            if (Flag) Server.StoreDMI(ID, 12);        
        }
        return ResultFlag;                                        
    };*/    
    
    public RInterface addElement(Integer Key, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        RPointer Root=null;
        if (ResultFlag==0) {                                   
            Root=new RPointer(Key);
            Root.setPos(new Integer(DataCollection.size()));
            DataCollection.add(Root);
            if (Flag) Server.StoreDMI(ID, 2);                
        }
        return Root;
    }

    public RInterface getByPos(Integer Pos) throws java.rmi.RemoteException {
        int index=0;
        boolean Flag=false;
        RInterface RAI=null;
        while ((index<DataCollection.size())&!Flag){
            RAI=(RInterface)DataCollection.get(index);
            Flag=((Pos.compareTo(RAI.getPos()))==0);
            ++index;
         }
        if (!Flag) RAI=null;
        return RAI;
    }

    public void cleanModulePos() throws java.rmi.RemoteException {
        RInterface Q=null;
        int suffix=0;
        int index=0;
        while (index<(DataCollection.size()-suffix)){
            Q=getByPos(index);
            if (Q==null) --suffix;
            else
                if (suffix!=0) Q.setPos(new Integer(Q.getPos().intValue()+suffix));
            ++index;
        }
    }

    public int delByPos(Integer QuestionPos, Integer LibID, boolean Fl) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {    
          RPointer Root=null;
          int index=0;
          RInterface Q=null;
          Boolean Flag=true;
          while ((index<DataCollection.size())&Flag){
            Q=getByPos(index);
            if (Q!=null)
                if (Q.getPos().compareTo(QuestionPos)==0) {
                    DataCollection.remove(Q);
                    if (Fl) {
                        Server.StoreDMI(this.ID, 2);
                        Server.StoreDMI(Q.getID(), 13); 
                    }
                    System.out.println("Удалили успешно 1");
                    Flag=false;
                    --index;
                    cleanModulePos();
                }
            ++index;
          }
        }
        return ResultFlag;
    };


    public int delByID(Integer ID, Integer LibID, boolean Fl) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {          
          int index=0;
          RInterface Q=null;
          while ((index<DataCollection.size())){
            Q=getByPos(index);
            if (Q!=null)
                if (Q.getID().compareTo(ID)==0) {
                    DataCollection.remove(Q);
                    if (Fl) {
                        Server.StoreDMI(this.ID, 2);
                        Server.StoreDMI(Q.getID(), 13); 
                    }
                    System.out.println("Удалили успешно 2");
                    cleanModulePos();
                    --index;
                }
            ++index;
          }
        }
        return ResultFlag;          
    };

    public ArrayList getKeys() throws java.rmi.RemoteException {
        ArrayList Res=new ArrayList();
        Class CL=new RPointer(null).getClass();
        try {
            for (int i = 0; i < DataCollection.size(); i++) {
                Object O=DataCollection.get(i);
                if (O.getClass().equals(CL)) {
                    Res.add(O);
                } else {
                    AnswerInterface AI=(AnswerInterface)O;
                    Res.add(AI.getID());
                }
            }
        } catch( Exception e){
            System.out.println("Ошибка "+e.toString());
    	}
        return Res;
    }

   
    public int getType() throws RemoteException {
        return 1;
    }  
    
    public DataModuleInterface getRoot(int i) throws RemoteException {
        return (DataModuleInterface) DataCollection.get(i);
    }
    
    public void delModule(int ModuleIndex) throws RemoteException {
        DataCollection.remove(ModuleIndex);
    };
        
    public int setDictionary(Integer aS, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,3,LibID);
        if (ResultFlag==0) {          
            int S=aS.intValue();
            if ((S>-1)&(S!=DictionaryID)) {
                DictionaryID = S;
                if (Flag) Server.StoreDMI(ID, 3);        
            };
        }
        return ResultFlag;
    }
    
    
    public Integer getDictionary() throws RemoteException {
        System.out.println("Что то ведь делается");	
        TableName = "V"+Server.MakeTable();        
        return DictionaryID;
        
    }
    
    public boolean createTable() throws RemoteException {
        return false;
    }        
    
    public DataModuleInterface getModule(int ModuleIndex) throws RemoteException {
        return (DataModuleInterface) DataCollection.get(ModuleIndex);
    }
    
    public int getSize() throws RemoteException {
        System.out.println("Size"+DataCollection.size());	
        return DataCollection.size(  );
    }
    
    public void setRefresher(LibraryRefreshInterface aLib) throws RemoteException {
        Library = aLib;
        int index=0;
        DataModuleInterface DTI=null;
        while (index<DataCollection.size()){
            DTI=(DataModuleInterface)DataCollection.get(index);
            DTI.setRefresher(Library);
            ++index;
        }
    }

    public void setEditable (boolean aEditable) {
        Editable=aEditable;
    }

    public boolean isEditable () {
        return Editable;
        //private boolean Editable=true;
    }
    
    public int setComment(String aComment, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,5,LibID);
        if (ResultFlag==0) {  
            Comment = aComment;
            if (Flag) Server.StoreDMI(ID, 5);                
        }
        return ResultFlag;
    }
    
    public String getComment() throws RemoteException {
        return Comment;
    }
    
    public int setName(String S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,1,LibID);
        if (ResultFlag==0) {         
            Name = S;
            if (Flag) Server.StoreDMI(ID, 1);                        
        }
        return ResultFlag;
    }
    
    public String getName() throws RemoteException {
        return Name;
    }
    
    public int setCondition(String S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,11,LibID);
        if (ResultFlag==0) {         
            Condition = S;
            if (Flag) Server.StoreDMI(ID, 11);        
        }
        return ResultFlag;
    }
    
    public String getCondition() throws RemoteException {
        return Condition;
    }
    
    public INIACRMIInterface getServer() throws RemoteException {
        return Server;
    }
    
    public TableAdapterInterface getTable() throws RemoteException {
        
        TableAdapter TA = new TableAdapter(this, Server);
        TableConnectors.add(TA);
        return TA;
    }
    
    public DualTableAdapterInterface getDualTable() throws RemoteException {
        DualTableAdapter TA = new DualTableAdapter(this, Server);
        //TableConnectors.add(TA);
        return TA;
    }    
    
    public String getTableName() throws RemoteException {
        return TableName;
    }
    
    public String getRealTableName() throws RemoteException {
        return TableName;
    }    

    public int setTableName(String aTableName, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,6,LibID);
        if (ResultFlag==0) {          
            TableName = aTableName;
            if (Flag) Server.StoreDMI(ID, 6);        
        }
        return ResultFlag;
    }
  
    public void delTableAdapter(TableAdapterInterface TAI) throws RemoteException {
        TableConnectors.remove(TAI);
    }
    
    public String makeInsertSQL() throws RemoteException {
        String query="insert into "+ TableName +"(ROW_ID) values (";
        try {
            String Key = Server.getKey().toString();
            query=query+Key+")";
        } catch (Exception E) {
            System.out.println(query);
            System.out.println("Ошибка создания Скрипта1 создания новой записи "+E);	
        }
        
        return query;
    }
    
    public String makeUpdateSQL(String Value, String ColumnName, String id_Key) throws RemoteException {
        return "";
    }
    
    public String makeFixSQL(String Value, String ColumnName, String id_Key) throws RemoteException {
        return "";
    }    
    
}
