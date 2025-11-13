/*
 * Main.java
 *
 * Created on 6 Январь 2004 г., 15:41
 */
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.sql.*;
import java.io.*;
import java.util.Locale;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceImpl;

    
public class INIACRMIServer extends UnicastRemoteObject implements INIACRMIInterface{    

    private String  RMIServer = "localhost";
    private String  RMIName = "INIACRMIInterface2";
    private int     RMIPort = 1099;	//Default RMI port
    private DictionaryManager Dictionary;
    private ArrayList   LibraryList;
    private transient Connection	connection;
    private transient Statement 	statement;
    public  String      User;    
    public  String	Password;
    public  String	Server;
    public  String	Driver;
    private ArrayList   TableConnectors;
    private KeeperOfAnswer      AnswerKeeper    =new KeeperOfAnswer(this);
    private KeeperOfQuestion    QuestionKeeper  =new KeeperOfQuestion(this);
    
    
    
    
    //public INIACRMIInterface Servers;

    public class INIACSessionClass implements Serializable {
        public ArrayList DictionaryCollection;
        public ArrayList DataModuleCollection;
        public ArrayList UserCollection;
        public DictionaryManager    MainDictionary;
        public QuestionManager      MainQuestion;    
        public ManagerUser          MainUserManager; 
        public RPointer             DMIPointer;
        public DataModuleInterface  DataRoot;
        public INIACRMIServer       Server;
        
        public INIACSessionClass(ArrayList aDictColl, DataModuleInterface aDataRoot, INIACRMIServer aServer)  throws RemoteException {
            Server = aServer;
            DictionaryCollection = aDictColl;
            DataModuleCollection = new ArrayList();
            DataRoot = aDataRoot;
            MainUserManager =new ManagerUser(Server,false);
            MainDictionary  =new DictionaryManager(Server, "SS", new Integer(0), false);
            MainDictionary.setServer(Server);
            MainQuestion=new QuestionManager(Server,"SS", new Integer(0),false);
            MainQuestion.setServer(Server);
        }
        
        public INIACSessionClass() {
            DictionaryCollection = new ArrayList();
            DataRoot = null;
        }
        public ArrayList getDictionaryCollection() {
            return DictionaryCollection;
        }
        public DataModuleInterface getDataModule() {
            return DataRoot;
        }

        public DictionaryInterface getMainDictionary() {
            return MainDictionary;
        }

        public QuestionInterface getMainQuestion() {
            return MainQuestion;
        }
        
        public ManagerUser getUserManager() {
            return MainUserManager;
        }
    }    
    public INIACSessionClass INIACSession;
    
    
    //private QuestionManager     question;
    //private Dictionary   TList;
    
    
    String outMessage;			//Our outgoing message
    
    /** Creates a new instance of Main */
    public INIACRMIServer() throws RemoteException {
       	//inMessage = new String();
        outMessage = new String();
        TableConnectors = new ArrayList();
        LibraryList = new ArrayList();
        initConnectParametrs();
        initRMI();
        connect();
        //createConnectForm();
        
        //DataRoot = new DataModule();
        
    }
    public void setConnection(Connection	aConnection) {
        connection = aConnection;   
    }
    
    public int askArbitr(DQAInterface DQA, int type, Integer LibID) throws RemoteException {
        int result = 0;
        return result;
    };    

    public void Informer(DQAInterface DQA, int type, Integer LibID) throws RemoteException {
        int i=0;
        while (i<LibraryList.size()) {
            LibraryRefreshInterface Lib = (LibraryRefreshInterface)LibraryList.get(i);
            try {
              if (Lib.getID().compareTo(LibID)==0) Lib.RefreshLocalClient(); else Lib.Refresh();
            } catch( Exception re ) {
                System.err.println("Ошибка "+re.getMessage());
            }
            i++;
        }
    };        
    
    public Connection getConnection() {
        return connection;   
    }
    
    public void setStatement(Statement 	aStatement) {

        statement = aStatement;
    }
      
    public TableAdapterInterface getTable(DataModuleInterface DMI) throws RemoteException {
        
        TableAdapter TA = new TableAdapter(DMI, this);
        TableConnectors.add(TA);
        return TA;
    }
  
    public Integer getKey () throws RemoteException {
        Integer Res=null;
        try {
            //Statement stmt = connection.createStatement();
            String SeqSql="select orders.nextval from dual";
            ResultSet RS=statement.executeQuery(SeqSql);
            String ResS="";
            while (RS.next()) {
                ResS = RS.getString(1);
            }
            Res=new Integer(ResS);
            //stmt.close();
        } catch (Exception E) {
            System.out.println("Ошибка создания Скрипта3 создания новой записи "+E);
        }
        return Res;
    };
    
    public RInterface   getDMIRootPointer() throws RemoteException {
        return INIACSession.DMIPointer;
    };      
      
    public String MakeTable() throws RemoteException {
//      String SQLCreateString;
        String TableName=null;
        try{
            Statement statement=connection.createStatement();

            System.out.println( "Создаем новую таблицу" );	
            String SeqSql="select orders.nextval from dual";
            ResultSet resultSet=statement.executeQuery(SeqSql);
            while (resultSet.next()) {
                System.err.println("Ключ "+resultSet.getInt(1));
                TableName="Table"+resultSet.getInt(1); 
//                SQLCreateString=SqlCreate();
//                System.out.println(SQLCreateString);	
//                statement.executeQuery(SQLCreateString);
            }
//            statement.executeQuery("commit");
        }   catch( Exception e){
            System.out.println("Ошибка создания новой строки"+e);
    	}
        return TableName;
    }
    
    
    protected void RestoreCollections(){
        //Восстановление коллекции
    }
    
    protected void initConnectParametrs() {
    	try {
            String DataStr;
            String Key;
            String Value;
    			 
            //BufferedReader in = new BufferedReader( new FileReader("params.ini"));
            BufferedReader in = new BufferedReader( new FileReader("params.ini"));
            //d:/work/socio/store.dat
            while((DataStr = in.readLine())!= null) {
                if (DataStr.indexOf("=")!=-1) {
                    Key=DataStr.substring(0,DataStr.indexOf("="));
                    System.err.println(Key);
                    Value=DataStr.substring(DataStr.indexOf("=")+1);
                    System.err.println(Value);
                    if (Key.equalsIgnoreCase("User"))  User = Value;
                    else if (Key.equalsIgnoreCase("Password"))  Password = Value;
                    	else if (Key.equalsIgnoreCase("Server"))  Server = Value;
                            else if (Key.equalsIgnoreCase("Driver"))  Driver = Value;
                                else if (Key.equalsIgnoreCase("RMIServer"))  RMIServer = Value;
                                    else if (Key.equalsIgnoreCase("RMINamе" ))  RMIName = Value;
                                        else if (Key.equalsIgnoreCase("RMIPort")) RMIPort = (new Integer(Value)).intValue();
                            
                }
            }
            in.close();
        }   catch( FileNotFoundException e){
            System.out.println("File not found");
    	}   catch( Exception e){
            System.out.println(e);
    	}
        //RestoreCollections();
        
        
        
    }
    
    public void connect() {
        try {
            Locale.setDefault(Locale.ENGLISH); 
            String UCP_POOL = "UCP-pool";
            PoolDataSource pds = new PoolDataSourceImpl();
            System.out.println("Opening db connection");
            pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
            pds.setURL(Server);
            pds.setUser(User);
            pds.setPassword(Password);
            pds.setConnectionPoolName(UCP_POOL);
            pds.setConnectionWaitTimeout(10);
            connection = pds.getConnection();
            statement = connection.createStatement();
            ReadDictionaries();
            System.err.println("AllRight");
        }   catch (Exception ex) {
            System.err.println("Cannot initialize the db connection.");
            System.err.println(ex.getMessage ());
        }
    }    
    
    protected void createConnectForm() {
        ConnectWindow frame = new ConnectWindow(this);
        //frame.setLocationRelativeTo(this);
        frame.setVisible(true);
        frame.setTitle("true");
    }

    
    /**
     * @param args the command line arguments
     */
    
    //Работа со словарем
    
    /*public void NewQuest(String Str) throws RemoteException { 
        INIACSession.getMainDictionary().New(Str);
        Library.RefreshQuestionList();
        
        //Dictionary.New("sss");
    }*/
    
    /*public AnswerInterface NewAnswer(String Str) throws RemoteException { 
        //INIACSession.getMainDictionary().New(Str);
        INIACSession.getMainQuestion().New(Str);

        
        //Dictionary.New("sss");
    }*/
    
    
    public String getQuestName(int QuestNum) throws RemoteException {
        return "3";
    }
    public void setQuestName(int QuestNum, String Name) throws RemoteException {    }
    
    //Работа с вопросои
    
    
    public void delAnswer(int QuestNum, int AnswerNum) throws RemoteException {  }
    
    public String getAnswer() throws RemoteException {
        return "1";
    }
    public String getAnswers(int QuestNum) throws RemoteException {
        return "2";
    }
    public void setAnswer(int QuestNum, int AnswerNum, String Name) throws RemoteException {    }
    public void setAnswers(int QuestNum, String Name) throws RemoteException {   }
    
    public static void main(String[] args) {
	try {
            new INIACRMIServer();
        } catch( RemoteException re ) {
            System.err.println(re.getMessage());
            System.exit(1); //fail - exit 
	}
    }    

    public void initRMI() {
	if (System.getSecurityManager() == null) {
            System.setSecurityManager( new RMISecurityManager() );
	}
	System.out.println("RMI security manager installed.");
        try {
            //Registry registry = LocateRegistry.createRegistry(RMIPort);
            LocateRegistry.createRegistry(RMIPort);
            System.out.println("RMI registry installed 1. "+RMIPort);
            //registry.rebind("//"+RMIServer+"/"+RMIName, this);
            Naming.rebind("//"+RMIServer+"/"+RMIName, this);
	} catch( Exception re ) {
            System.err.println("Ошибка "+re.getMessage());
            re.printStackTrace();    
            System.exit(1); //fail - exit 
	}

    }    
    
    
    public void Register(LibraryRefreshInterface aLib) throws RemoteException {
        LibraryList.add(aLib);
        System.out.println("Прошла регистрация");
    }
    
    /*public void pingLibraries() throws RemoteException {
        int i=0;
        while (i<LibraryList.size()) {
            LibraryRefreshInterface Lib = (LibraryRefreshInterface)LibraryList.get(i);
            try {
              Lib.Refresh();  
            } catch( Exception re ) {
                System.err.println("Ошибка "+re.getMessage());
            }
            i++;
        }
    }*/
    
        
    public DictionaryInterface GetEntity(SocioRefreshInterface Refresher) throws RemoteException {
        //Precedure will return for client session class
        Dictionary.RegisterClient(Refresher);
        return null;
    }
    
    public UserInterface autorizeUser (String Name,String Password) throws RemoteException {
        UserInterface UI=INIACSession.getUserManager().getUser(Name);
        if (UI==null) {
            UI=readUser(Name);
        }
        if (UI!=null) {
            if (!UI.getPassword().equalsIgnoreCase(Password)) UI=null;
        }
        return UI;
    }; 
    
    public int GetDictionarySize() throws RemoteException {
        return INIACSession.getDictionaryCollection().size();
    }
    
    public DictionaryInterface getDictionary (Integer DictionaryIndex) throws RemoteException {
        DictionaryInterface Dictionary=null;
        ArrayList AL = INIACSession.getDictionaryCollection();
        int index=0;
        boolean Flag=true;
        while ((index<AL.size())&Flag) {
            DictionaryInterface DI= (DictionaryInterface) AL.get(index);
            if (DictionaryIndex.compareTo(DI.getID())==0) {
                Dictionary=DI;
                Flag = false;
            }
            index++;
        };
        return Dictionary;
    }

    public ArrayList getDictionaryKeys() throws RemoteException {
        ArrayList Res=new ArrayList();
        ArrayList AL = INIACSession.getDictionaryCollection();
        try {
            for (int i = 0; i < AL.size(); i++) {
                Object O=AL.get(i);
                Res.add(((DictionaryInterface)O).getID());
            }
        } catch( Exception e){
            System.out.println("Ошибка "+e.toString());
    	}     
        return Res;
    }    
    
    public DataModuleInterface getDMI (Integer DictionaryIndex) throws RemoteException {
        DataModuleInterface DMI=null;
        ArrayList AL = INIACSession.DataModuleCollection;
        int index=0;
        boolean Flag=true;
        while ((index<AL.size())&Flag) {
            DataModuleInterface CurrDMI= (DataModuleInterface) AL.get(index);
            if (DictionaryIndex.compareTo(CurrDMI.getID())==0) {
                DMI=CurrDMI;
                Flag = false;
            }
            index++;
        };
        return DMI;
    }
    
  
    

    /*public  void addDMI (DataModuleInterface DMI) throws RemoteException {
        INIACSession.DataModuleCollection.add(DMI);
        System.out.println("Добавили модуль данных");
        try {
            Library.RefreshDictionaryList();
            System.out.println("Добавили модуль данных");
            WriteDictionaries();
        } catch (Exception e2) {
            System.out.println("Ошибка в addDMI"+e2);
        }
    }*/

    public  DataModuleInterface newDMI (String aName, Integer ID, int type, boolean Flag) throws RemoteException {
        //Integer Key=this.getKey();
        DataModuleInterface DM=null;
        switch (type) {
            case 1: {
                DM = new DataFolder(this,null,ID,Flag);
                if (Flag) StoreDMI(DM, 0);        
                if (Flag) StoreDMI(DM, 4); 
                INIACSession.DataModuleCollection.add(DM);
                DM.setName(aName,0,Flag);
                System.out.println("Добавили "+aName);
                break;
            }
            case 2: {
                DM = new DataModule(this,null,ID,Flag);
                if (Flag) StoreDMI(DM, 0);        
                if (Flag) StoreDMI(DM, 4);                 
                //DM.setID(Key);
                //DM.setServer(this);
                INIACSession.DataModuleCollection.add(DM);
                DM.setName(aName,0,Flag);                
                System.out.println("Добавили "+aName);
                break;
            }
            case 3: {
                DM = new DataBlock(this,null,ID,Flag);
                if (Flag) StoreDMI(DM, 0);        
                if (Flag) StoreDMI(DM, 4);                 
                INIACSession.DataModuleCollection.add(DM);
                DM.setName(aName,0,Flag);                
                System.out.println("Добавили "+aName);
                break;
            }
            case 4: {
                DM = new DataFilter(this,null,ID,Flag);
                if (Flag) StoreDMI(DM, 0);        
                if (Flag) StoreDMI(DM, 4);                 
                INIACSession.DataModuleCollection.add(DM);
                DM.setName(aName,0,Flag);
                System.out.println("Добавили "+aName);
                break;
            }
            case 5: {
                DM = new DataFilter(this,null,ID,Flag);
                if (Flag) StoreDMI(DM, 0);        
                if (Flag) StoreDMI(DM, 4);                 
                //DM.setDictionary(DictionaryID);
                //DM.setServer(this);
                //DM.setID(Key);
                //DM.createTable();
                DM.setEditable(false);
                INIACSession.DataModuleCollection.add(DM);
                DM.setName(aName,0,Flag);
                System.out.println("Добавили "+aName);
                break;
            }
            
        }
        return DM;

    }

    public DataModuleInterface getDataModule() throws RemoteException {
        return INIACSession.getDataModule();
    }

    public boolean SetRefresher(SocioRefreshInterface Refresher) throws RemoteException {
        return true;
    }
    
    public DictionaryInterface newDictionary(String Name, boolean Flag) throws RemoteException {
        System.out.println("Создаем словарь 1");
        DictionaryInterface DI=newDictionary(Name,getKey(),Flag);
        return DI;
    }
    
    public DictionaryInterface newDictionary (String Name,Integer IDKey, boolean Flag) throws RemoteException {
        DictionaryInterface DI=null;
        try {
            DI = new DictionaryManager(this, Name,IDKey,Flag);
            INIACSession.getDictionaryCollection().add(DI);
            System.out.println("Создали словарь 3");
        } catch (Exception e2) {
            System.out.println("Неудается записать 2 "+e2);
        }
            
        return DI;
    }
    
    public ManagerUser getUserManager()  throws RemoteException {
        return INIACSession.getUserManager();
    }    
    
    public DictionaryInterface getMainDictionary()  throws RemoteException {
        return INIACSession.getMainDictionary();
    }
    
    public QuestionInterface getMainQuestion()  throws RemoteException {
        return INIACSession.getMainQuestion();
    }

    public void RecurrentStore(DataModuleInterface DMI, Integer parentID, Statement statement) throws RemoteException {
        String StartSQLStr = "Insert into Library values (";
        try {
                String SQLStr=StartSQLStr+DMI.getID()+",1,0,'"+DMI.getName()+"',"+parentID+")";
                statement.executeUpdate(SQLStr);
                System.out.println(SQLStr);
                SQLStr=StartSQLStr+DMI.getID()+",1,1,'"+DMI.getName()+"',0)";
                statement.executeUpdate(SQLStr);
                System.out.println(SQLStr);

                SQLStr=StartSQLStr+DMI.getID()+",1,3,'"+DMI.getDictionary()+"',0)";
                statement.executeUpdate(SQLStr);
                System.out.println(SQLStr);
                if (DMI.getType()==2) if (DMI.getDictionary().intValue()>-1) writeDictionary(DMI.getDictionary(), statement);

                SQLStr=StartSQLStr+DMI.getID()+",1,4,'"+DMI.getType()+"',0)";
                statement.executeUpdate(SQLStr);
                System.out.println(SQLStr);
                if (!DMI.getComment().isEmpty()) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,5,'"+DMI.getComment()+"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }
                if (!DMI.getTableName().isEmpty()) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,6,'"+DMI.getTableName()+"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }
                if (!DMI.getAnnotation().isEmpty()) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,7,'"+DMI.getAnnotation()+"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }
                if (!DMI.getStartDate().isEmpty()) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,8,'"+DMI.getStartDate()+"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }
                if (!DMI.getEndDate().isEmpty()) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,9,'"+DMI.getEndDate()+"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }
                if (!(DMI.getQuota()!=null)) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,10,'"+DMI.getQuota() +"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }
                if (!(DMI.getCondition()!=null)) {
                    SQLStr=StartSQLStr+DMI.getID()+",1,11,'"+DMI.getCondition() +"',0)";
                    statement.executeUpdate(SQLStr);
                    System.out.println(SQLStr);
                }



                int i = DMI.getSize();
                int j=0;
                while (j<i) {
                    RInterface RDMI=DMI.getByPos(j);
                    DataModuleInterface ChildDMI=this.getDMI(RDMI.getID());
                    SQLStr=StartSQLStr+DMI.getID()+",1,2,'"+RDMI.getPos()+"',"+RDMI.getID()+")";
                    System.out.println(SQLStr);
                    statement.executeUpdate(SQLStr);
                    RecurrentStore(ChildDMI,DMI.getID(),statement);
                    j++;
                }

        } catch (Exception e2) {
                System.out.println("\n Неудается записать 3 "+e2);
        }
    }


    public void writeAnswer(Integer QuestID, Integer AnswerID, Statement statement) {
        try {
            if (statement==null) statement=this.getStatement();
            String StartSQLStr = "Insert into Library values (";
            AnswerInterface AI=this.getMainQuestion().getAnswer(AnswerID);
            String SQLStr=StartSQLStr+AI.getID()+",7,0,'"+AI.getName()+"',"+QuestID+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+AI.getID()+",7,1,'"+AI.getName()+"',"+AI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+AI.getID()+",7,2,'"+AI.getTextable()+"',"+AI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
        } catch (Exception e2) {
                System.out.println("Ошибка в writeDictionary "+e2);
        }
    }

    public void writeQuestion(Integer DictID, Integer QuestID, Statement statement) {
        try {
            if (statement==null) statement=this.getStatement();
            String StartSQLStr = "Insert into Library values (";            
            QuestionInterface QI=this.getMainDictionary().getQuestion(QuestID);
            String SQLStr=StartSQLStr+QI.getID()+",6,0,'"+QI.getName()+"',"+DictID+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+QI.getID()+",6,1,'"+QI.getName()+"',"+QI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+QI.getID()+",6,2,'"+QI.getQuestionType()+"',"+QI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+QI.getID()+",6,4,'"+QI.getBase()+"',"+QI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+QI.getID()+",6,5,'"+QI.getMaxAnswerCount()+"',"+QI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            /*
            SQLStr=StartSQLStr+QI.getID()+"0,6,3,'"+QI. getType()+"',"+QI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            */
            
            int i=0;
            while (i<QI.getSize()) {
                RInterface RAI= QI.getByPos(i);
                SQLStr=StartSQLStr+QI.getID()+",6,3,'"+RAI.getPos()+"',"+RAI.getID()+")";
                statement.executeUpdate(SQLStr);
                writeAnswer(QuestID, RAI.getID(), statement);
                i++;
            }    
        } catch (Exception e2) {
                System.out.println("Ошибка в writeDictionary "+e2);
        }            
        
    }

    public void writeDictionary(Integer ID, Statement statement) {
        try {
            String StartSQLStr = "Insert into Library values (";
            DictionaryInterface DI=this.getDictionary(ID);
            String SQLStr=StartSQLStr+ID+",5,0,'"+DI.getName()+"',0)";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            SQLStr=StartSQLStr+ID+",5,1,'"+DI.getName()+"',"+DI.getID()+")";
            System.out.println(SQLStr);
            statement.executeUpdate(SQLStr);
            //ArrayList Keys = DI.getQuestKeys();
            int i=0;
            while (i<DI.getSize()) {
                RInterface RQI= DI.getByPos(i);
                SQLStr=StartSQLStr+DI.getID()+",5,2,'"+RQI.getPos()+"',"+RQI.getID()+")";
                statement.executeUpdate(SQLStr);
                writeQuestion(ID,RQI.getID(),statement);
                i++;
            }
        } catch (Exception e2) {
                System.out.println("Ошибка в writeDictionary "+e2);
        }
    };

    private void Registerer (Integer ID,int type, Object Value) {
        try {
            String query =
                "update Library set Name ='" +Value+
                "' where Row_id = "+ID+
                "' and Object_type = "+type;
                statement.executeUpdate(query);
                System.out.println("Записываем "+query);

        } catch (Exception e2) {
                System.out.println("Ошибка в Registerer "+e2);
        }
    }



    public void StoreAllBase() throws RemoteException {
        System.out.println( "Создаем новую таблицу" );
        try {
               Statement statement=connection.createStatement();
               String SQLStr="Delete from Library";
               statement.executeUpdate(SQLStr);
               RecurrentStore(INIACSession.DataRoot,0,statement);
        } catch (Exception e2) {
                System.out.println("Ошибка в WriteSQLDictionaries() "+e2);
        }
    }
    
    public ArrayList getDMIKeys() throws RemoteException {
        ArrayList Res=new ArrayList();
        ArrayList AL = INIACSession.DataModuleCollection;
        try {
            for (int i = 0; i < AL.size(); i++) {
                Object O=AL.get(i);
                Res.add(((DataModuleInterface)O).getID());
            }
        } catch( Exception e){
            System.out.println("Ошибка в getDMIKeys"+e.toString());
    	}
        return Res;
    }    

    public UserInterface readUser (String Name) throws RemoteException {
        UserInterface UI=null;
        ArrayList AL = INIACSession.UserCollection;
        int index=0;
        boolean Flag=true;
        while ((index<AL.size())&Flag) {
            UserInterface CurrUI= (UserInterface) AL.get(index);
            if (Name.equalsIgnoreCase(CurrUI.getName())) {
                UI=CurrUI;
                Flag = false;
            }
            index++;
        };
        return UI;
    }
    
    public UserInterface readUser (Integer UserID) throws RemoteException {
        try {
            String Name="";
            UserInterface U = this.getUserManager().newUser();
            Statement Substmt=connection.createStatement();
            String SQLStr="Select * from Library where Row_ID='"+UserID+"'";
            ResultSet subRS=Substmt.executeQuery(SQLStr);
            while (subRS.next()) {
                switch (new Integer(subRS.getString(3)).intValue()) {
                case 1: {
                    Name=subRS.getString(4);
                    U.setName(Name,false);
                    break;
                }
                case 2: {
                    Integer aUserID    = new Integer(subRS.getString(5));
                    Integer UserPOS    = new Integer(subRS.getString(4));
                    RInterface RQI = U.addElement(aUserID,0,false);
                    RQI.setPos(UserPOS);
                    readUser(aUserID);
                 }
                 case 3: {
                    Name=subRS.getString(4);
                    U.setPassword(Name,false);
                 }   
                }
            }
            subRS.close();
            Substmt.close();
        } catch (Exception e2) {
                System.out.println("Ошибка в readDictionary "+e2);
        }    

        return null;
    }


    
    public void readDictionary(Integer DictID) {
        try {
            if ((DictID.intValue()>-1)&(this.getDictionary(DictID)==null)) {
                String Name="";
                DictionaryInterface DI=this.newDictionary("New dictionary",DictID, false);
                Statement Substmt=connection.createStatement();
                String SQLStr="Select * from Library where Row_ID='"+DictID+"'";
                ResultSet subRS=Substmt.executeQuery(SQLStr);
                while (subRS.next()) {
                    switch (new Integer(subRS.getString(3)).intValue()) {
                        case 1: {
                            Name=subRS.getString(4);
                            DI.setName(Name,0,false);
                            break;
                        }
                        case 2: {
                            Integer QuestID     = new Integer(subRS.getString(5));
                            Integer QuestPOS    = new Integer(subRS.getString(4));
                            RInterface RQI = DI.addElement(QuestID,0,false);
                            RQI.setPos(QuestPOS);
                            readQuestion(QuestID);
                        }
                    }
                }
                subRS.close();
                Substmt.close();
            }
        } catch (Exception e2) {
                System.out.println("Ошибка в readDictionary "+e2);
        }
    }
    
    public void readQuestion(Integer QuestID) {
        try {
            String Name="";
            QuestionInterface QI=this.getMainDictionary().newQuestion("Новый вопрос readQuestion", QuestID, false);
            Statement Substmt=connection.createStatement();
            String SQLStr="Select * from Library where Row_ID='"+QuestID+"'";
            ResultSet subRS=Substmt.executeQuery(SQLStr);

            while (subRS.next()) {
                switch (new Integer(subRS.getString(3)).intValue()) {
                        case 1: {
                            Name=subRS.getString(4);
                            QI.setName(Name,0,false);
                            break;
                        }
                        case 3: {
                            Integer AnswerID     = new Integer(subRS.getString(5));
                            Integer AnswerPOS    = new Integer(subRS.getString(4));
                            RInterface RAI = QI.addElement(AnswerID,0,false);
                            RAI.setPos(AnswerPOS);
                            readAnswer(AnswerID);
                            break;
                        }
                        case 2: {
                            QI.setQuestionType      (new Integer(subRS.getString(4)).intValue(), false);
                            break;
                        }
                        case 4: {
                            QI.setBase              (new Integer(subRS.getString(4)).intValue(), false);
                            break;
                        }
                        case 5: {
                            QI.setMaxAnswerCount    (new Integer(subRS.getString(4)).intValue(), false);
                            break;
                        }
                        case 6: {
                            QI.setRepetition        (new Integer(subRS.getString(4)).intValue(), false);
                            break;
                        }    
                }
            }
            subRS.close();
            Substmt.close();
        } catch (Exception e2) {
                System.out.println("Ошибка в ReadQuestion "+e2);
        }
    }
    
    

    public void readAnswer(Integer AnswerID) {
        try {
            String Name="";
            AnswerInterface AI=this.getMainQuestion().newAnswer("Новый ответ", AnswerID, false);
            Statement Substmt=connection.createStatement();
            String SQLStr="Select * from Library where Row_ID='"+AnswerID+"'";
            ResultSet subRS=Substmt.executeQuery(SQLStr);

            while (subRS.next()) {
                switch (new Integer(subRS.getString(3)).intValue()) {
                        case 1: {
                            Name=subRS.getString(4);
                            AI.setName(Name,0,false);
                            break;
                        }
                        case 2: {
                            boolean flag=subRS.getString(4).equalsIgnoreCase("true");
                            AI.setTextable(flag, false);
                            //другие типы
                        }
                }
            }
            subRS.close();
            Substmt.close();
        } catch (Exception e2) {
                System.out.println("Ошибка в ReadAnswer "+e2);
        }
    }

    
    public void StoreDictionary (DQAInterface DQA,int type) throws RemoteException {
        try {
            Statement stmt=connection.createStatement();
            String StartSQLStr="Insert into Library values (";
            Integer ID=DQA.getID();
            //String SQLStr="Select * from Library where Row_ID='"+ID+"'";
            switch (type) {
                    case 0: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=0";
                        int res=stmt.executeUpdate(SQLStr);                        
                        SQLStr=StartSQLStr+ID+",5,0,'"+DQA.getName()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                        
                    case 1: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=1";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",5,1,'"+DQA.getName()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getName()+"' where Row_ID="+ID+" and Object_Type=1";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }
                    case 2: {
                        String DeleteSQLStr="delete from Library where Row_ID="+ID+" and Object_Type=2";
                        int res=statement.executeUpdate(DeleteSQLStr);
                        int i = DQA.getSize();
                        int j=0;
                        //StartSQLStr = "Insert into Library values (";
                        while (j<i) {
                            RInterface RDMI=DQA.getByPos(j);
                            String SQLStr=StartSQLStr+ID+",5,2,'"+RDMI.getPos()+"',"+RDMI.getID()+")";
                            res=statement.executeUpdate(SQLStr);
                            j++;
                        }
                        break;
                    }
                    case 13: {
                        String SQLStr="delete from Library where Row_ID="+ID;
                        int res=stmt.executeUpdate(SQLStr);                        
                        break;
                    }      
            }
            stmt.execute("commit");
            stmt.close();
        } catch (Exception e2) {
            System.out.println("Ошибка в DMIStore() 2"+e2);
        }    
    }
    
    public void StoreQuestion (DQAInterface DQA,int type) throws RemoteException {
        try {
            Statement stmt=connection.createStatement();
            String StartSQLStr="Insert into Library values (";
            QuestionInterface QI=(QuestionInterface)DQA;
            Integer ID=DQA.getID();
            //String SQLStr="Select * from Library where Row_ID='"+ID+"'";
            switch (type) {
                    case 0: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=0";
                        int res=stmt.executeUpdate(SQLStr);                        
                        SQLStr=StartSQLStr+ID+",6,0,'"+QI.getName()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                        
                    case 1: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=1";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",6,1,'"+QI.getName()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }
                    case 2: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=2";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",6,2,'"+QI.getQuestionType()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                    
                    case 3: {
                        String DeleteSQLStr="delete from Library where Row_ID="+ID+" and Object_Type=3";
                        int res=statement.executeUpdate(DeleteSQLStr);
                        int i = QI.getSize();
                        int j=0;
                        while (j<i) {
                            RInterface RDMI=QI.getByPos(j);
                            String SQLStr=StartSQLStr+ID+",6,3,'"+RDMI.getPos()+"',"+RDMI.getID()+")";
                            res=statement.executeUpdate(SQLStr);
                            j++;
                        }
                        break;
                    }
                    case 4: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=4";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",6,4,'"+QI.getBase()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                    
                    case 5: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=5";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",6,5,'"+QI.getMaxAnswerCount()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                    
                    case 6: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=6";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",6,6,'"+QI.getRepetitionType()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                          
                    case 13: {
                        String SQLStr="delete from Library where Row_ID="+ID;
                        int res=stmt.executeUpdate(SQLStr);                        
                        break;
                    }      
            }
            stmt.execute("commit");
            stmt.close();
        } catch (Exception e2) {
            System.out.println("Ошибка в StoreDictionary"+e2);
        }    
    }

    public void StoreAnswer (DQAInterface DQA,int type) throws RemoteException {
        try {
            Statement stmt=connection.createStatement();
            String StartSQLStr="Insert into Library values (";
            AnswerInterface AI=(AnswerInterface)DQA;
            Integer ID=AI.getID();
            //String SQLStr="Select * from Library where Row_ID='"+ID+"'";
            switch (type) {
                    case 0: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=0";
                        int res=stmt.executeUpdate(SQLStr);                        
                        SQLStr=StartSQLStr+ID+",7,0,'"+AI.getName()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                        
                    case 1: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=1";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",7,1,'"+AI.getName()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }
                    case 2: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=2";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",7,2,'"+AI.getTextable()+"',0)";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                    
                    case 13: {
                        String SQLStr="delete from Library where Row_ID="+ID;
                        int res=stmt.executeUpdate(SQLStr);                        
                        break;
                    }      

            }
            stmt.execute("commit");
            stmt.close();
        } catch (Exception e2) {
            System.out.println("Ошибка в StoreAnswer"+e2);
        }    
    }
    
    
    public void StoreDQА(DQAInterface DQA, int type) throws RemoteException {
        try {
            //String SQLStr="Select * from Library where Row_ID='"+ID+"'";
            switch (DQA.getType()) {
                    case 0: {
                        StoreDictionary(DQA, type);
                        break;
                    }        
                    case 7: {
                        StoreQuestion(DQA, type);
                        break;
                    }        
                    case 8: {
                        StoreAnswer(DQA, type);
                        break;
                    }        
                    
            }
            //pingLibraries();
        } catch (Exception e2) {
            System.out.println("Ошибка в StoreDQA"+e2);
        }                     
    }
    
    /*public void DQAStore(Integer ID, int type) throws RemoteException {
        try {
            DQAInterface OLDDMI=this.getDictionary(ID);
            DQAStore(OLDDMI,type);
        } catch (Exception e2) {
            System.out.println("Ошибка в DQAStore() 1"+e2);
        }
    }  */  
    
    public void StoreDMI(DataModuleInterface OLDDMI, int type) throws RemoteException {
        try {
            Statement stmt=connection.createStatement();
            Integer ID=OLDDMI.getID();
            String StartSQLStr="Insert into Library values (";
            //String SQLStr="Select * from Library where Row_ID='"+ID+"'";
            switch (type) {
                    case 0: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=0";
                        int res=stmt.executeUpdate(SQLStr);                        
                        SQLStr=StartSQLStr+ID+",1,0,'новый модуль',"+OLDDMI.getParent()+")";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                        
                    case 1: {
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=1";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,1,'"+OLDDMI.getName()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getName()+"' where Row_ID="+ID+" and Object_Type=1";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }
                    case 2: {
                                
                        String DeleteSQLStr="delete from Library where Row_ID="+ID+" and Object_Type=2";
                        int res=statement.executeUpdate(DeleteSQLStr);
                        int i = OLDDMI.getSize();
                        int j=0;
                        //StartSQLStr = "Insert into Library values (";
                        while (j<i) {
                            RInterface RDMI=OLDDMI.getByPos(j);
                            String SQLStr=StartSQLStr+ID+",1,2,'"+RDMI.getPos()+"',"+RDMI.getID()+")";
                            res=statement.executeUpdate(SQLStr);
                            j++;
                        }
                        break;
                    }
                    case 3: {
                        //словарь
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=3";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,3,'"+OLDDMI.getDictionary()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getDictionary()+"' where Row_ID="+ID+" and Object_Type=3";
                        res=stmt.executeUpdate(SQLStr);
                        break;
                    }                    
                    case 4: {
                        //тип блока данных
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=4";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,4,'"+OLDDMI.getType()+"',0)";                        
                        //String SQLStr="update Library set '"+OLDDMI.getType()+"' where Row_ID="+ID+" and Object_Type=4";
                        res=stmt.executeUpdate(SQLStr);                        
                        break;
                    }
                    case 5: {
                        //комментарий 
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=5";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,5,'"+OLDDMI.getComment()+"',0)";                        
                        //String SQLStr="update Library set '"+OLDDMI.getComment()+"' where Row_ID="+ID+" and Object_Type=5";
                        res=stmt.executeUpdate(SQLStr);                                                
                        break;
                    }
                    case 6: {
                        //имя таблицы
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=6";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,6,'"+OLDDMI.getTableName()+"',0)";                                                
                        //String SQLStr="update Library set '"+OLDDMI.getTableName()+"' where Row_ID="+ID+" and Object_Type=6";
                        res=stmt.executeUpdate(SQLStr);                                                
                        break;
                    }
                    case 7: {
                        //аннотации
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=7";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,7,'"+OLDDMI.getAnnotation()+"',0)";                                                
                        //String SQLStr="update Library set '"+OLDDMI.getAnnotation()+"' where Row_ID="+ID+" and Object_Type=7";
                        res=stmt.executeUpdate(SQLStr);                                                                        
                        break;
                    }
                    case 8: {
                        //Дата начала
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=8";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,8,'"+OLDDMI.getStartDate()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getStartDate()+"' where Row_ID="+ID+" and Object_Type=8";
                        res=stmt.executeUpdate(SQLStr);                                                                                                
                        break;
                    }
                    case 9: {
                        //дата завершения
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=9";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,9,'"+OLDDMI.getEndDate()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getEndDate()+"' where Row_ID="+ID+" and Object_Type=9";
                        res=stmt.executeUpdate(SQLStr);                                                                                                                        
                        break;
                    }
                    case 10: {
                        //квота
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=10";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,10,'"+OLDDMI.getQuota()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getQuota()+"' where Row_ID="+ID+" and Object_Type=10";
                        res=stmt.executeUpdate(SQLStr);                                                                                                                        
                        break;
                    }
                    case 11: {
                        //Условия
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=11";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,11,'"+OLDDMI.getCondition()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getCondition() +"' where Row_ID="+ID+" and Object_Type=11";
                        res=stmt.executeUpdate(SQLStr);                                                                                                                                                
                        break;
                    }
                    case 12: {
                        //предки
                        String SQLStr="delete from Library where Row_ID="+ID+" and Object_Type=12";
                        int res=stmt.executeUpdate(SQLStr);
                        SQLStr=StartSQLStr+ID+",1,12,'"+OLDDMI.getParent()+"',0)";
                        //String SQLStr="update Library set '"+OLDDMI.getParent() +"' where Row_ID="+ID+" and Object_Type=12";
                        res=stmt.executeUpdate(SQLStr);                                                        
                        break;
                    }
                    case 13: {
                        String SQLStr="Select * from Library where Link="+ID;
                        ResultSet RS=stmt.executeQuery(SQLStr);
                        if (!RS.next()) {
                            SQLStr="delete from Library where Row_ID="+ID;
                            stmt.executeUpdate(SQLStr);                        
                        }
                        break;
                    }      

            }
            stmt.execute("commit");
            stmt.close();
            //pingLibraries();
        } catch (Exception e2) {
            System.out.println("Ошибка в DMIStore(). Что то возможно не записалось"+e2);
        }        
    }
    
    public void StoreDMI(Integer ID, int type) throws RemoteException {    
        try {
            DataModuleInterface OLDDMI = this.getDMI(ID);
            this.StoreDMI(OLDDMI, type);
        } catch (Exception e2) {
            System.out.println("Ошибка в DMIStore() 1"+e2);
        }
    }    
    
    public DataModuleInterface RecurrentRestore(Integer ID) throws RemoteException {
        DataModuleInterface DMI=null;
        String  Name="";
        String  Comment="";
        String  Condition="";
        String  TableName="";
        String  Annotation="";
        String  StartDate="";
        String  EndDate="";
        Integer Quota=0;
        Integer Parent=-1;
        int     type=1;
        Integer DictID=-1;
        ArrayList Child=new ArrayList();
        try {
           Statement stmt=connection.createStatement();
           String SQLStr="Select * from Library where Row_ID="+ID+" and object_type=0";
           ResultSet RS=stmt.executeQuery(SQLStr);
           if (RS.next()){
            SQLStr="Select * from Library where Row_ID='"+ID+"' order by object_type";
            RS=stmt.executeQuery(SQLStr);
            while (RS.next()) {
                switch (new Integer(RS.getString(3)).intValue()) {
                    case 1: {
                        Name=RS.getString(4);
                        break;
                    }
                    case 2: {
                        Integer ChildID=new Integer(RS.getString(5));
                        Integer ChildPos=new Integer(RS.getString(4));
                        RInterface RI=new RPointer(ChildID);
                        RI.setPos(ChildPos);
                        Child.add(RI);
                        break;
                    }
                    case 3: {
                        //словарь
                        DictID=new Integer(RS.getString(4));
                        if (this.getDictionary(DictID)==null) readDictionary(DictID);
                        break;
                    }                    
                    case 4: {
                        //тип блока данных
                        type=new Integer(RS.getString(4)).intValue();
                        break;
                    }
                    case 5: {
                        //комментарий 
                        Comment=RS.getString(4);
                        break;
                    }
                    case 6: {
                        //таблица
                        TableName=RS.getString(4);
                        break;
                    }
                    case 7: {
                        //тип блока данных
                        Annotation=RS.getString(4);
                        break;
                    }
                    case 8: {
                        //комментарий
                        StartDate=RS.getString(4);
                        break;
                    }
                    case 9: {
                        //комментарий
                        StartDate=RS.getString(4);
                        break;
                    }
                    case 10: {
                        //комментарий
                        String S=RS.getString(4);
                        if (S!=null) {
                            try {
                                Quota=new Integer(S);    
                            }  catch (Exception e2) {
                                System.out.println("Сбой преобразоования даты"+e2);
                            }
                        }
                        
                        break;
                    }
                    case 11: {
                        //комментарий
                        Condition=RS.getString(4);
                        break;
                    }
                    case 12: {
                        //комментарий
                        String S=RS.getString(4);
                        if (S!=null) {
                            try {
                                Parent=new Integer(S);
                            }  catch (Exception e2) {
                                System.out.println("Сбой преобразоования предка"+e2);
                            }
                        }

                        break;
                    }

                }
            }
            DMI=this.newDMI(Name,ID,type,false);
            if (!Comment.isEmpty())     DMI.setComment(Comment,0,false);
            if (!TableName.isEmpty())   DMI.setTableName(TableName,0,false);
            if (!Annotation.isEmpty())  DMI.setAnnotation(Annotation,0,false);
            if (!StartDate .isEmpty())  DMI.setStartDate(StartDate,0,false);
            if (!EndDate.isEmpty())     DMI.setEndDate(EndDate,0,false);
            if (!(Quota==0))            DMI.setQuota(Quota,0,false);
            if (!(Parent==0))           DMI.setParent(Parent,0,false);
            if (!Condition.isEmpty())   DMI.setCondition(Condition,0,false);
            DMI.setServer(this);

            for (int i = 0; i < Child.size(); i++) {
                RInterface RI= (RInterface)Child.get(i);
                RecurrentRestore(RI.getID());
                RInterface RDMI=DMI.addElement(RI.getID(),0,false);
                RDMI.setPos(RI.getPos());
            }
            DMI.setDictionary(DictID,0,false);
            RS.close();
            stmt.close();
           } 
        } catch (Exception e2) {
            System.out.println("Ошибка в RecurrentRestore()"+e2);
        }
        return DMI;
    }

    public void ReadSQLDictionaries() throws RemoteException {
        System.out.println( "Создаем новый корень" );
        try {
            INIACSession.DataRoot=null;
            INIACSession.DictionaryCollection.clear();
            INIACSession.MainDictionary.qetArray().clear();
            INIACSession.MainQuestion.GetAnswerArray().clear();
            //DataModuleInterface DMI= this.newDMI("dddd",new Integer(0),1);
            //DMI.setID(0);
            DataModuleInterface DMI = RecurrentRestore(0);
            //DataModuleInterface DMI = RecurrentRestore(304742);
            if (DMI==null) {
                DMI=this.newDMI("Корень", 0, 1, true);
                DMI.setDictionary(new Integer(-1),0,false);
            }
            
            INIACSession.DataRoot=DMI;
            INIACSession.DMIPointer=new RPointer(0);
            INIACSession.DMIPointer.setPos(0);
            
        } catch (Exception e2) {
                System.out.println("Ошибка в ReadSQLDictionaries(), возможно пустая база");
        }
    }

    public void ReadDictionaries() throws RemoteException  {
        System.out.println("Создается новый модуль сессии ");
        //DataModuleInterface DM = newDMI("Ddd",0,1);
        //DataModuleInterface DM= new DataFolder(this,null);
        //DM.setDictionary(new Integer(-1));
        INIACSession = new INIACSessionClass(new ArrayList(), null, this);
        ReadSQLDictionaries();
        //ReadAnswerKeeper();
        //ReadQuestionKeeper();
        //ReadDataModuleKeeper();
        //ReadUserKeeper();
        //ReadUserGroupKeeper();
    }

    
    public void setAnswer() throws RemoteException {
        
    }    
         
    public int GetDictionaryIndexOf(DictionaryInterface Object) throws RemoteException {
        return INIACSession.getDictionaryCollection().indexOf(Object);
    }    
    
    public Statement getStatement() throws RemoteException {

        return statement;
    }     
    
    public ResultSet executeQuery(String Query, Statement aStatement) throws RemoteException {
        ResultSet RS=null;
        Statement stmt=aStatement;
        try {
            if (stmt==null) {
                stmt = statement;
            }
            System.out.println("Выполняем запрос "+Query);
            RS = stmt.executeQuery(Query);
        } catch (Exception e2) {
            System.out.println("Ошибка в executeQuery"+e2);
        }
        return RS;

    }
    
    public boolean executeSQL(String Query, Statement aStatement) throws RemoteException {
        boolean Flag = false;
        Statement stmt=aStatement;
        try {
            if (stmt == null) {
                stmt = statement;
            }
            System.out.println("Выполняем запрос "+Query);
            Flag=stmt.execute(Query);
        } catch (Exception e2) {
                System.out.println("Ошибка в executeSQL"+e2);
        }
        return Flag;
    }
    
    public void RefreshDictionaryList() throws RemoteException {
        
    }
    
    public void RefreshQuestionList() throws RemoteException {
        
    }
    /*public boolean ReleaseDictionary (int DictionaryIndex) throws RemoteException {
        INIACSession.getDictionaryCollection().remove(DictionaryIndex);
        Library.RefreshDictionaryList(); 
        return true;
    };
    public boolean ReleaseDictionary (DictionaryInterface DI) throws RemoteException {
        INIACSession.getDictionaryCollection().remove(DI);
        Library.RefreshDictionaryList(); 
        return true;
    };*/
    public boolean DeleteDictionary (int DictionaryIndex) throws RemoteException {
        System.out.println("Удаляем словарь");
        try {
            for (int i = 0; i < INIACSession.getDictionaryCollection().size(); i++) {
                DictionaryInterface DI=(DictionaryInterface)INIACSession.getDictionaryCollection().get(i);
                if (DI.getID().compareTo(new Integer(DictionaryIndex))==0) {
                    INIACSession.getDictionaryCollection().remove(i);
                    System.out.println("Удалили словарь");
                } 
            }
        } catch( Exception e){
            System.out.println("Ошибка в удалении словаря"+e.toString());
    	}
        return true;
    };
    
    /*public boolean DeleteDictionary (DictionaryInterface DI) throws RemoteException {
        INIACSession.getDictionaryCollection().remove(DI);
        Library.RefreshDictionaryList(); 
        return true;
    };*/
}
 