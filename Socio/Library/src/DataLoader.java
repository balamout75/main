/*
 * BlockLoader.java
 *
 * Created on 26 Апрель 2005 г., 10:22
 */
//import java.io.InputStreamReader;
//import java.io.BufferedReader;
import java.io.*;

/**
 *
 * @author riac
 */

public class DataLoader {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    

    private INIACRMIInterface Server;
    //private int DataRow;
    private DataModuleInterface DMI;
    private File file;
    java.util.ArrayList QuestMetaData    =   new java.util.ArrayList();
    
    class QuestRecord {
        private Integer QuestID;
        private int     BaseNum;
        private int     FirstNum;
        private int     LastNum;
        private int     QuestType;
        
        QuestRecord (Integer aQuestID, int aBaseNum, int aAnswerCount, int aQuestType){
            QuestID     = aQuestID;
            BaseNum     = aBaseNum;  
            FirstNum    = aBaseNum+1;
            LastNum     = aBaseNum+aAnswerCount;
            QuestType   = aQuestType;
        }
        Integer getQuestID() {
            return QuestID;
        } 
        
        int getBaseNum() {
            return BaseNum;
        } 
        int getFirstNum() {
            return FirstNum;
        } 
        int getLastNum() {
            return LastNum;
        } 

        int getQuestType() {
            return QuestType;
        }         
        
    }    
    
    /** Creates a new instance of BlockLoader */
    public DataLoader(DataModuleInterface aDMI, File aFile) {
        try {
            DMI=aDMI;
            file=aFile;
            Server = DMI.getServer();
            AnalyseStructure();
            AnalyseData();
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
        };
        
    }
   
    private void AnalyseStructure () {
        try {
            DictionaryInterface DI=Server.getDictionary(DMI.getDictionary());
            Integer QuestID=null;
            QuestionInterface QI=null;
            int j=0;
            while (j<DI.getSize()) {
                QuestID=Server.getDictionary(DMI.getDictionary()).getByPos(j).getID();
                QI=Server.getMainDictionary().getQuestion(QuestID);
                QuestMetaData.add(new QuestRecord(QI.getID(),QI.getBase(),QI.getSize(),QI.getQuestionType()));
                j++;
            }
        } catch (Exception E) {
            System.err.println("Ошибка в AnalyseStructure()"+E);	
        }   
    }
    
    private boolean DataModule(String Str) {
        java.util.ArrayList QuestNames    =   new java.util.ArrayList();
        java.util.ArrayList Values        =   new java.util.ArrayList();
        boolean res=true;
        try {
            //String NumS=Server.getDictionary(DMI.getDictionary()).getByPos(Col).getID().toString();
            String [] StrValues = Str.split(",");
            int i=0;
            while (i<StrValues.length) {
                String CurrValue=StrValues[i];
                QuestRecord QR = null;
                QR= (QuestRecord) QuestMetaData.get(i);
                String QuestName="Quest"+QR.QuestID;
                if (QR.getQuestType() == nonalter) {
                    String ThisValue="";
                    String [] MultipleStringValues = CurrValue.split(" ");
                    int j=0; 
                    while (j<MultipleStringValues.length) {
                        if (!MultipleStringValues[j].equalsIgnoreCase("{")&&!MultipleStringValues[j].equalsIgnoreCase("}")) 
                            //тут возможен лексический контроль
                            ThisValue=ThisValue+" "+MultipleStringValues[j];
                        j++;
                    }
                    CurrValue=ThisValue.trim();
                } else { //отработка альтернативной переменной
                    //тут возможен лексический контроль
                }
                QuestNames.add(QuestName);
                Values.add("'"+CurrValue+"'");
                i++;
            }
            int k=0;
            String Names = "";
            String Value = "";
            while (k<QuestNames.size()) {
                Names=Names+QuestNames.get(k)+",";
                Value=Value+Values.get(k)+",";
                k++;
            }
            Names=Names+"Row_id";
            Value=Value+Server.getKey().toString();
            String SQLStr = "INSERT INTO "+DMI.getTableName()+" ("+Names+") VALUES ("+Value+")";
            System.out.println("Запрос   "+SQLStr);	
            Server.executeSQL(SQLStr, null);
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
            res = false;
        }
        return res;
    }
    
    private void AnalyseData() {
        try {
            if (DMI.getDictionary()!=null) {
                String DataStr;
                BufferedReader in = new BufferedReader( new FileReader(file));
                DataStr = in.readLine();
                int i=0;
                while (DataStr!=null) {
                    DataStr=DataStr.trim();
                    System.out.println(i+" cтрока данных "+DataStr);	
                    if (DataModule(DataStr)) {
                        //System.out.println(i+" cтрока данных "+DataStr);	
                    }
                    DataStr = in.readLine();
                }
                in.close();
            } else {System.out.println("Словарь не задан");}  
        } catch( FileNotFoundException e){
            System.out.println("File not found");
    	} catch( Exception e){
            System.out.println(e);
    	}
    }
    
    
    

}

/*public class DataLoader {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    

    private INIACRMIInterface Server;
    private int DataRow;
    private DataModuleInterface DMI;
    private File file;
    
    
    // Creates a new instance of BlockLoader 
    public DataLoader(DataModuleInterface aDMI, File aFile) {
        try {
            DMI=aDMI;
            file=aFile;
            Server = DMI.getServer();
            AnalyseStructure();
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
        };
        
    }
   
    private String CreateRec(String Str) {
        System.out.println("Строка данных "+Str );	
        String Res="";
        try {
            String StrSQL=DMI.makeInsertSQL();
            System.err.println("Dataloader - Строка добавления данных"+StrSQL);
            Res=StrSQL.substring(StrSQL.indexOf("values (")+8,StrSQL.lastIndexOf(")") );
            Server.executeSQL(StrSQL,null);
        } catch (Exception E) {
            System.err.println("Dataloader - Ошибка в выполнении добавления "+E);	
        }
        return Res;
    }
    
    private boolean DataModule(String Str) {
        int i=0;
        int Col=0;
        String DataSql  ="";
        String S        ="";
        String Key      ="";
        boolean res;
        try {
            boolean FirstVal=true;
            while ((Str.charAt(i)!=',')&(Str.charAt(i)!=9)&(i<=Str.length())) {
                S=S+Str.charAt(i);
                i++;
            }
            if (S.charAt(0)!='{') {//Integer.parseInt(S);}
            i=0;
            S="";
            while (i<=Str.length()) {
                if (i>82) 
                     System.err.println("здесь");
                //if ((i<Str.length())&(Str.charAt(i)!=',')) {
                if ((i>=Str.length())|(Str.charAt(i)==',')) {
                    S=S+Str.charAt(i);
                } else {
                    if (S.charAt(0)=='#') {S="";} else
                        if (S.charAt(0)!='{') {//Integer.parseInt(S);}
                        else {
                            S=S.substring(2,S.indexOf(" }"));
                        }
                    if (FirstVal) {
                        Key = CreateRec(DataSql);
                        FirstVal=false;
                    }
                    String NumS=Server.getDictionary(DMI.getDictionary()).getByPos(Col).getID().toString();
                    //System.err.println("Quest"+NumS);
                    DataSql=DMI.makeUpdateSQL(S,"Quest"+NumS,Key);
                    Server.executeSQL(DataSql,null);
                    //System.err.println(DataSql);
                    Col ++;
                    S="";
                }
                i++;
            }
            DataRow ++;
            res=true;            
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
            res = false;
        }
        return res;
    }
    
    private void AnalyseStructure() {
        try {
            if (DMI.getDictionary()!=null) {
                String DataStr;
                BufferedReader in = new BufferedReader( new FileReader(file));
                DataStr = in.readLine();
                while (DataStr!=null) {
                    if (DataModule(DataStr)) {
                        DataStr = in.readLine();
                        System.out.println("Строка данных "+DataStr);	
                    } else DataStr = in.readLine();
                }
                in.close();
            } else {System.out.println("Словарь не задан");}  
        }   catch( FileNotFoundException e){
            System.out.println("File not found");
    	}   catch( Exception e){
            System.out.println(e);
    	}
    }
}*/
