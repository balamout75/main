import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
//import javax.swing.JTree;
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
public class DataFilter extends UnicastRemoteObject implements DataModuleInterface, Serializable {
    public String Name="";
    public String Comment="";
    public String TableName="";
    public String Condition="";
    public ArrayList DataCollection;
    public Integer   ParentID;
    public transient LibraryRefreshInterface Library;
    public transient INIACRMIInterface Server;
    public transient ArrayList   TableConnectors;
    public Integer DictionaryID=-1;
    public int FilterAnalyzeI;
    public boolean Editable=true;
    public Integer ID;
    /** Creates a new instance of DataModul */
    public String  Annotation="";
    public String  StartDate="";
    public String  EndDate="";
    public Integer Quota=null;

    public int setAnnotation   (String  S, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,7,LibID);
        if (ResultFlag==0) {
            Annotation=S;
            if (Flag) Server.StoreDMI(ID, 7);
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

    public int     setParent(Integer aParentID, Integer LibID, boolean Flag) throws RemoteException {
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

    public DataFilter(INIACRMIInterface aServer, Integer aParentID, Integer ID, boolean Flag) throws RemoteException{
        try { 
            Server=aServer;
            ParentID=aParentID;
            DataCollection = new ArrayList();
            Name = "Блоки данных";
            System.out.println( "Новый блок/группа" );
            TableConnectors = new ArrayList();
            this.ID=ID;
        } catch (Exception e) {
                System.out.println("Ошибка создания DataBlock" +e.getMessage());            
        }            
            //MakeTable();
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

    /*public void AddRoot(DataModuleInterface DMI) throws RemoteException {
    }   */
    
    ///////////// анализатор строки фильтра
    private boolean SkipSpace(String Str) {
        boolean res;
        try {
            while ((Str.charAt(FilterAnalyzeI)==' ')&(FilterAnalyzeI<=Str.length()-1)) {
                FilterAnalyzeI++;
            }
            res=(FilterAnalyzeI<=Str.length()-1);
        } catch (Exception E) {
            res = false;
        }
        return res;
    }
    
    private boolean FirstBracket1 (String Str) {
        if (Str.charAt(FilterAnalyzeI)=='(') {
            FilterAnalyzeI++;
            return true;
        } else return false;
    }
    
    private boolean FirstBracket2 (String Str) {
        if (Str.charAt(FilterAnalyzeI)=='{') {
            FilterAnalyzeI++;
            return true;
        } else return false;
    }
    
    
    private boolean QuestBegin (String Str) {
        if (Str.charAt(FilterAnalyzeI)=='@') {
            FilterAnalyzeI++;
            return true;
        } else return false;
    }
    
    private boolean FirstNum (String Str) {
        char Ch=Str.charAt(FilterAnalyzeI);
        if ((Ch=='0')|(Ch=='1')|(Ch=='2')|(Ch=='3')|(Ch=='4')|(Ch=='5')|(Ch=='6')|(Ch=='7')|(Ch=='8')|(Ch=='9')) {
            FilterAnalyzeI++;
            return true;
        } else return false;        
    }
    
    private boolean FirstFree (String Str) {
        if (Str.charAt(FilterAnalyzeI)=='#') {
            FilterAnalyzeI++;
            return true;
        } else return false;        
    }

    private boolean CloseBracket2 (String Str) {
        if (Str.charAt(FilterAnalyzeI)=='}') {
            FilterAnalyzeI++;
            return true;
        } else return false;                
    }

    private boolean CloseBracket1 (String Str) {
        if (Str.charAt(FilterAnalyzeI)==')') {
            FilterAnalyzeI++;
            return true;
        } else return false;                        
    }
    
    private boolean DualOperand (String Str) {
        char Ch=Str.charAt(FilterAnalyzeI);
        if ((Ch=='&')|(Ch=='|')) {
            FilterAnalyzeI++;
            return true;
        } else return false;        
    }
    
    private boolean SingleOperand (String Str) {
        if (Str.charAt(FilterAnalyzeI)=='^') {
            FilterAnalyzeI++;
            return true;
        } else return false;                        
    }

    public void setEditable (boolean aEditable) {
        Editable=aEditable;
    }

    public boolean isEditable () {
        return Editable;
    }

    public int setID(Integer ID, Integer LibID, boolean Flag) throws RemoteException {
        int ResultFlag=Server.askArbitr(this,0,LibID);
        if (ResultFlag==0) {         
            this.ID = ID;
        }
        return ResultFlag;             
        
    }

    public Integer getID() throws RemoteException {
        return ID;
    }

    private Integer getQuestID (Integer QIndex) {
        Integer QID=0;
        try {
            QID=Server.getDictionary(getDictionary()).getByPos(QIndex).getID();
        } catch (Exception E) {
            System.out.println( "Ошибка в преобразовании QuestName" );
        }
        return QID;
    }
   
    public String AnalyseFilterString(String S) {
      try {  
        String Str="";
        String Code="";
        int OpenCounter=0;
        boolean Error=false;
        int State=0; //начальная стадия
        FilterAnalyzeI=new Integer(0);
        do {
            switch(State) {
                case 0: { //Начало. Может последовать либо скобка, либо символ вопроса
                    if (SkipSpace(S)) {
                        if (FirstBracket1(S)) {
                            OpenCounter++;
                            Str=Str+"(";
                        } else if (SingleOperand(S)) { //отрицание
                                    Str=Str+" not ";
                               } else if (QuestBegin(S)) {
                                         State=1;
                                         Code="";
                                       } else {Error=true; State=7;}
                    } else State=7;
                    break;
                }
                case 1: {//Состоялся символ вопроса. Считываем его код
                    if (SkipSpace(S)) {
                        if (FirstNum(S)) {
                            Code=""+S.charAt(FilterAnalyzeI-1);
                            State=2;
                        } else {Error=true; State=7;}
                    } else State=7;    
                    break;
                }
                case 2: {//Состоялся один из символов номера вопроса
                    if (SkipSpace(S)) {
                        if (FirstNum(S)) {
                            System.out.println( "Новый блок/группа" );
                            Code=Code+S.charAt(FilterAnalyzeI-1);
                        } else if (FirstBracket2(S)) {
                                //Str=Str+"Contains (Quest"+getQuestID(Integer.parseInt(Code)-1);
                                Str=Str+"Quest"+getQuestID(Integer.parseInt(Code)-1);
                                //Тут можно проанализировать что за вопрос
                                State=3;
                               } else {Error=true; State=7;}
                    } else State=7;        
                    break;
                }
                case 3: {//Состоялся символ ответа. Считываем его код
                    if (SkipSpace(S)) {
                        if (FirstNum(S)) {
                            Code=""+S.charAt(FilterAnalyzeI-1);
                            State=4;
                        } else if (FirstFree(S)) {
                                State=5;
                                Code="#";
                               } else {Error=true; State=7;}
                    } else State=7;               
                    break;
                }
                case 4: { //Состоялся один из символов номера ответа 
                    if (SkipSpace(S)) {
                        if (FirstNum(S)) {
                            Code=Code+S.charAt(FilterAnalyzeI-1);
                        } else if (CloseBracket2(S)) {
                                if (Code.equalsIgnoreCase("0")) Str=Str+" is null "; 
                                //else Str=Str+",'"+Code+"') ";
                                else Str=Str+"='"+Code+"' ";
                                //else Str=" Contains("+Code++"%' ";
                                State=6;
                               } else {Error=true; State=7;}
                    } else State=7;                                       
                    break;
                }
                case 5: { //Состоялся символ пустого ответа 
                    if (SkipSpace(S)) {                    
                        if (CloseBracket2(S)) {
                            if (Code.equalsIgnoreCase("0")) Str=Str+" is null "; 
                            else Str=Str+",'"+Code+"') ";
                            State=6;
                        } else {Error=true; State=7;}
                    } else State=7;                                                               
                    break;
                }
                case 6: { //Состоялось закрытие символа ответа
                    if (SkipSpace(S)) {                                        
                        if (CloseBracket1(S)) {
                            Str=Str+")";
                            OpenCounter--;
                            State=6;
                        } else if (DualOperand(S)) {
                                if (S.charAt(FilterAnalyzeI-1)=='&') Str=Str+"and ";
                                else Str=Str+"or ";
                                State=0;
                               } else {Error=true; State=7;}
                    } else State=7;                             
                    break;
                }
                
            }
        } while (State!=7);
        System.out.println(Str);	   
        return Str;
      } catch (Exception E) {
          System.out.println("Ошибка в строке условий "+E.toString());                    
          return "";
      }  
    };
    ///////////// анализатор строки фильтра завершение
    
    
    
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
        Condition = AnalyseFilterString(Comment);
        if (!Condition.equalsIgnoreCase("")) {
            if (!TableName.equalsIgnoreCase("")) {
                System.out.println("Удаляем старую таблицу");
            }
        }
        TableName = "V"+Server.MakeTable();
        Server.StoreDMI(ID, 6);
        System.out.println("Создаем таблицу");                    
        String Str = "create view "+TableName+" as select * from "+Server.getDMI(ParentID).getTableName();
        if (!Comment.equalsIgnoreCase("")) Str=Str+" where ("+Condition+")";
        try {
            System.out.println("Datamodule - Строка создания таблицы " + Str);            
            Server.executeSQL(Str,null);
            Server.executeSQL("commit",null);
            return Str;
        } catch (Exception e) {
            System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
            e.printStackTrace();
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
                            Str=Str+suffix+"("+Name2+" like '% "+A2+"'"+" or "+Name2+" like '"+A2+" %'"+" or "+Name2+" like '% "+A2+" %'"+"or "+Name2+" = '"+A2+"')";
                        }
                }
            }
            return Str;
        } catch (Exception e) {
            System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
            return "";
	}
        //ПОлучаем уникальный номер
        //Соллекция вопросов формирующая DataModel
        //return Str;
    }
    
    public String CreateView(String aStr) throws RemoteException {
        String Str = "Create view "+TableName+" "+aStr;
        try { 
            System.out.println("Datamodule - Строка создания View "+Str);            
            Server.executeSQL(Str,null);
            Server.executeSQL("commit",null);
            Server.StoreDMI(ID, 6);
            return Str;
        } catch (Exception e) {
            System.out.println("Datamodule - Ошибка создания имени файла данных" + 	e.getMessage());            
            e.printStackTrace();
            return "";
	}
        //ПОлучаем уникальный номер
        //Соллекция вопросов формирующая DataModel
        //return Str;
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
    
    public String makeMesureSQL () throws RemoteException {
        String Str = "Select count(*) from "+TableName;
        return Str;
    }
    
    //Добавить новое значение в таблицу
    
    public RInterface addElement(Integer Key, Integer LibID, boolean Flag) throws RemoteException {
        RPointer Root=null;
        int ResultFlag=Server.askArbitr(this,2,LibID);
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
                    cleanModulePos();
                    System.out.println("Удалили успешно 2");
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
        return 4;
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
            DictionaryID = aS;
            if (Flag) Server.StoreDMI(ID, 3);        
        }
        return ResultFlag;
    }
    
    public boolean createTable() throws RemoteException {
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
        DataModuleInterface DMI = Server.getDMI(this.getParent());
        String Res=DMI.getRealTableName();
        return Res;
        //return TableName;
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
            /*String SeqSql="select orders_seq.nextval from dual";
            ResultSet RS=Server.getStatement().executeQuery(SeqSql);
            while (RS.next()) {
		Key = RS.getString(1);
            }*/
            query=query+Key+")";
            //Server.getStatement().executeQuery("commit");
            
        } catch (Exception E) {
            System.out.println(query);
            System.out.println("Ошибка создания Скрипта1 создания новой записи "+E);	
        }
        
        return query;
    }
    
    public String makeUpdateSQL(String Value, String ColumnName, String id_Key) throws RemoteException {
        String query =
                "update "+this.getRealTableName()+
                " set "+ColumnName+" = '"+Value+
                "' where Row_id = "+id_Key;
        return query;
    }

    public String makeFixSQL(String Value, String ColumnName, String id_Key) throws RemoteException {
        String query ="alter table "+TableName+" MODIFY ("+ColumnName+" DEFAULT '"+Value+"')";
        return query;
        
    }    
    
}
