import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.io.Serializable;

/*
 * DataModul.java
 *
 * Created on 13 Март 2004 г., 18:41
 */

/**
 *
 * @author  Иван
 */
public class DataBlock extends UnicastRemoteObject implements DataModuleInterface, Serializable {
    private String Name="";
    private String Comment="";
    String TableName="";
    String Condition="";
    private ArrayList DataCollection;
    private Integer ParentID;
    private transient LibraryRefreshInterface Library;
    private transient INIACRMIInterface Server;
    private transient ArrayList   TableConnectors;
    private Integer DictionaryID=-1;
    private boolean Editable=true;
    private Integer ID;
    
    private String  Annotation="";
    private String  StartDate="";
    private String  EndDate="";
    private Integer Quota=null;

    public int setAnnotation        (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,7,LibID);
        if (ResultFlag==0) {
            Annotation=S;
            if (Flag) Server.StoreDMI(ID, 7);
        }
        return ResultFlag;
    };
    
    public int setStartDate         (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,8,LibID);
        if (ResultFlag==0) {
            StartDate=S;
            if (Flag) Server.StoreDMI(ID, 8);        
        }
        return ResultFlag;            
    };
    public int setEndDate           (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,8,LibID);        
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

    public int setParent(Integer aParentID, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,12,LibID);        
        if (ResultFlag==0) {          
            ParentID=aParentID;
            if (Flag) Server.StoreDMI(ID, 12); 
        }
        return ResultFlag;                                
    };
    
    public Integer   getParent() throws RemoteException {
        return ParentID;
    };

    
    /** Creates a new instance of DataModul */
       
    /*public DataBlock(INIACRMIInterface aServer, Integer aParentID, boolean Flag) throws RemoteException{
           this(aServer,aParentID,aServer.getKey(), Flag);
           Server.DMIStore(ID, 0);        
    }*/

    public DataBlock(INIACRMIInterface aServer, Integer aParentID, Integer ID, boolean Flag) throws RemoteException{
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
            //if (Flag) Server.DMIStore(ID, 0);        
        } catch (Exception e) {
                System.out.println("Ошибка создания DataBlock" +e.getMessage());            
        }            
            //MakeTable();
    }    
    
    /*public void AddRoot(DataModuleInterface DMI) throws RemoteException {
    }    
    */
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
    
    public String makeCreateSQL() throws RemoteException {
        String Str = "create table "+TableName+" (Row_id integer primary key ";
        try { 
                if (DictionaryID>-1) {
                    DictionaryInterface DI=Server.getDictionary(DictionaryID);
                    for (int i = 0; i < DI.getSize(); i++) {
                        RInterface RQM=DI.getByPos(new Integer(i));
                        Str = Str+", Quest"+RQM.getID()+" VarChar(256) DEFAULT ''";
                    }
                    Str = Str+")";
                }
                System.out.println("Datamodule - Строка создания таблицы " + Str);            
                
                Server.executeSQL(Str,null);
                Server.executeSQL(this.makeInsertSQL(),null);
                Server.executeSQL("commit",null);
                return Str;
        } catch (Exception e) {
                System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
                //e.printStackTrace();
                return "";
        }
    }
    
    public String makeMesureSQL(Integer Q1, int A1, Integer Q2, int A2) throws RemoteException {
        String Str = new String();
        try { 
            if (DictionaryID>-1) {
                DictionaryInterface DI=Server.getDictionary(DictionaryID);
                RInterface RQ1=DI.getByPos(Q1);
                RInterface RQ2=DI.getByPos(Q2);
                QuestionInterface QI1=Server.getMainDictionary().getQuestion(RQ1.getID());
                QuestionInterface QI2=Server.getMainDictionary().getQuestion(RQ2.getID());
                Str = "Select count(*) from "+TableName;
                String Name1="Quest"+RQ1.getID();
                String Name2="Quest"+RQ2.getID();
                String suffix=" where ";
                if (A1<=QI1.getSize()) {
                 if (QI1.getQuestionType()==1) {
                    Str=Str+suffix+Name1+"='"+A1+"'";
                    suffix=" and ";
                 } else  if (QI1.getQuestionType()==2) {  
                            Str=Str+suffix+"("+Name1+" like '% "+A1+"'"+" or "+Name1+" like '"+A1+" %'"+" or "+Name1+" like '% "+A1+" %'"+"or "+Name1+" = '"+A1+"')";
                            suffix=" and ";
                        }
                }
                if (A2<=QI2.getSize()) {
                 if (QI2.getQuestionType()==1) {
                    Str=Str+suffix+Name2+"='"+A2+"'";
                 } else  if (QI2.getQuestionType()==2) {
                            Str=Str+suffix+"("+Name2+"  like '% "+A2+"'"+" or "+Name2+" like '"+A2+" %'"+" or "+Name2+" like '% "+A2+" %'"+"or "+Name2+" = '"+A2+"')";
                        }
                }
            }
            return Str;
        } catch (Exception e) {
            System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
            return "";
	}
    }
    
    public String makeSelectSQL() throws RemoteException {
        String Str  ="Select ROW_ID";
        try { 
            if (DictionaryID>-1) {
                DictionaryInterface DI=Server.getDictionary(DictionaryID);
                int index=0;
                while (index<DI.getSize()){
                    RInterface RQ1=DI.getByPos(index);
                    Str=Str+", Quest"+RQ1.getID();
                    index++;
                }
                Str = Str+" from "+TableName+" order by Row_ID";
                
            } else Str="";
        } catch (Exception e) {
            System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
            Str="";
	}    
        return Str;
    }

    public void setEditable (boolean aEditable) {
        Editable=aEditable;
    }

    public boolean isEditable () {
        return Editable;
        //private boolean Editable=true;
    }
    
    public int setID(Integer ID, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,12,LibID);        
        if (ResultFlag==0) {          
            this.ID = ID;
        }
        return ResultFlag;
    }

    public Integer getID() throws RemoteException {
        return ID;
    }

    //Добавить новое значение в таблицу

    public RInterface addElement(Integer Key, Integer LibID, boolean Flag) throws RemoteException {
        RPointer RA=new RPointer(Key);
        RA.setPos(new Integer(DataCollection.size()));
        DataCollection.add(RA);
        if (Flag) Server.StoreDMI(ID, 2);        
        return RA;
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
    
    /*public DataModuleInterface AddRoot(String aName, int type) throws RemoteException {
        if (type==4) {
            DataFilter DM = new DataFilter(Server,ID);
            DM.setDictionary(DictionaryID);
            DM.setName(aName);
            DM.createTable();
            DataCollection.add(DM);
            System.out.println("Добавили "+aName);
            return DM;
         } else return null;   
    }    */
    
    public int getType() throws RemoteException {
        return 3;
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

    public boolean createTable() throws RemoteException {
        System.out.println("Datamodule - правильный SetDictionary");	
        TableName = Server.MakeTable();        
        makeCreateSQL();
        Server.StoreDMI(ID, 6);        
        return true;
    }    
    
    public Integer getDictionary() throws RemoteException {
        System.out.println("Что то ведь делается");	
        return DictionaryID;
        
    }
    
    public DataModuleInterface getModule(int ModuleIndex) throws RemoteException {
        return (DataModuleInterface) DataCollection.get(ModuleIndex);
    }
    
    public int getSize() throws RemoteException {
        System.out.println("Size"+DataCollection.size());	
        return DataCollection.size(  );
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
    
    public String getName()  {
        return Name;
    }
    
    public int setCondition(String S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,1,LibID);        
        if (ResultFlag==0) {          
            Condition = S;
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
        int ResultFlag=Server.askArbitr(this,1,LibID);        
        if (ResultFlag==0) {             
            TableName = aTableName;
        }
        return ResultFlag;                
    }

    public void delTableAdapter(TableAdapterInterface TAI) throws RemoteException {
        TableConnectors.remove(TAI);
    }
    
    public String makeInsertSQL() throws RemoteException {
        String query="";
        try {
            query="insert into "+ TableName +"(ROW_ID) values (";
            String Key = Server.getKey().toString();
            query=query+Key+")";
        } catch (Exception E) {
            System.err.println(query);
            System.err.println("Ошибка создания Скрипта1 создания новой записи "+E);	
        }
        System.out.println(query);
        return query;
    }
    
    public String makeUpdateSQL(String Value, String ColumnName, String id_Key) throws RemoteException {
        String query =
                "update "+TableName+
                " set "+ColumnName+" = '"+Value+
                "' where Row_id = "+id_Key;
        return query;
        
    }
    
    public String makeFixSQL(String Value, String ColumnName, String id_Key) throws RemoteException {
        String query ="alter table "+TableName+" MODIFY ("+ColumnName+" DEFAULT '"+Value+"')";
        return query;
    }
    
}
