/*
 * BlockLoader.java
 *
 * Created on 26 Апрель 2005 г., 10:22
 */
//import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
//import java.sql.Types;
import javax.swing.*;
//import javax.swing.filechooser.*;

/**
 *
 * @author riac
 */
public class BlockLoader {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    
    private DictionaryInterface DI;
    private DataModuleInterface DM;
    private INIACRMIInterface Server;
    private String FileName="C:\f.opr";
    private String StartStr="//\t\t\t\t\t\t\t\t\t\tДА-система 4.0 - Словарь переменных";
    private QuestionInterface CurrQuest;
    private int DataRow;
    private DictionaryInterface CD;
    //private TableAdapterInterface TAI;
    private DataModuleInterface DMI;
    private boolean FirstRecord=true;
    private Integer Key;
    private LibraryInterface Lib;
    JFileChooser fc;
    
    
    /** Creates a new instance of BlockLoader */
    public BlockLoader(DataModuleInterface aDMI, LibraryInterface aLib) {
        try {
            DMI=aDMI;
            Server = DMI.getServer();
            Key = Server.getKey();
            Lib=aLib;
            CD=Server.newDictionary("Новый словарь 222",Key, true);
         
            AnalyseStructure();
            //DMI.setDictionary(Server.GetDictionaryIndexOf(CD));
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
        };
        
    }
   
    private boolean CreateRec(String Str) {
        System.out.println("Строка данных "+Str );	
        //TAI.executeQuery()
        try {
            if (FirstRecord) {
                //DMI = DMI.AddRoot("Новый блок данных",3);
                DMI.setDictionary(Key,Lib.getID(),true);
                FirstRecord = false;
            }
            String StrSQL=DMI.makeInsertSQL();
            System.err.println("Blockloader - Строка добавления данных"+StrSQL);
            Server.executeSQL(StrSQL,null);
        } catch (Exception E) {
            System.err.println("Blockloader - Ошибка в выполнении добавления "+E);	
        };    
        return true;
    }
    
    private boolean DataModule(String Str) {
        int i=0;
        int Col=1;
        String DataSql  ="";
        String Header   ="";
        String S        ="";
        boolean res;

        try {
            while ((Str.charAt(i)!=',')&(Str.charAt(i)!=9)&(i<Str.length())) {
                S=S+Str.charAt(i);
                i++;
            }
            if (S.charAt(0)!='{') {Integer.parseInt(S);}
            i=0;
            S="";
            while (i<Str.length()) {
                if ((Str.charAt(i)!=',')&(Str.charAt(i)!=9)) {
                    S=S+Str.charAt(i);
                } else {
                    if (S.charAt(0)!='{') {Integer.parseInt(S);}
                    else {
                        S=S.substring(2,S.indexOf(" }"));
                    };
                    DataSql=DataSql+Header+"'"+S+"'";
                    Header=",";
                    Col ++;
                    S="";
                };
                i++;
            }
            DataRow ++;
            res=true;            
            CreateRec(DataSql);
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
            res = false;
        };
        return res;
    }
    
    private boolean FirstNum(String Str) {
        int i=1;
        String S="";
        boolean res;
        try {
            while ((Str.charAt(i-1)!='.')&(i<Str.length())) {
                S=S+Str.charAt(i-1);
                i++;
            }
            Integer.parseInt(S);
            res = true;
        } catch (Exception E) {
            System.out.println("Это не номер "+E);	
            res = false;
        };
        return res;
        
    }
    private boolean Quest(String Str) {
        int i=1;
        boolean res=false;
        while ((Str.charAt(i-1)!=9)&(i<Str.length())) {
            i++;
        }
        if ((i<2)&(!Str.startsWith("*") )) res=true;
        return res;
    }
    
    private boolean CheckAlterQuest(String Str) {
        int i=1;
        String S="";
        boolean res=false;
        while ((Str.charAt(i-1)!='.')&(i<Str.length())) {
            S=S+Str.charAt(i-1);
            i++;
        }
        if (i<Str.length()) if (Str.charAt(i)=='a') {
            res=true;
        }
        return res; 
    }
    
    private boolean NewQuest(String Str, boolean T) {
        try {
            Integer Key = Server.getKey();
            //int FistTab=Str.indexOf("\t");
            Str=Str.substring(Str.indexOf("\t"));
            int i=0;
            while (Str.startsWith("\t")) {
                Str=Str.substring(1);
                i++;
            }
            //Str=Str.indexOf(Str) substring(nonalter)
            CurrQuest=Server.getMainDictionary().newQuestion(Str,Key,true);
            //Str=Str.substring(nonalter)
            if (T) CurrQuest.setQuestionType(1, true); 
               else CurrQuest.setQuestionType(2, true);
            CD.addElement(Key,Lib.getID(),true);
        } catch (Exception E) {
            System.out.println("Какая то проблема в NewQuest при загрузке файла "+E);	
        };
        return true;
    }
    private boolean NewAnswer(String Str) {
        try {
            Integer Key = Server.getKey();
            Str=Str.substring(Str.indexOf(".")+1);
            Server.getMainQuestion().newAnswer(Str,Key,true);
            CurrQuest.addElement(Key,Lib.getID(),true);
            //Server.getMainDictionary().getQuestion() .getQuestion(QuestID).addAnswer(Key);
            
        } catch (Exception E) {
            System.out.println("Какая то проблема в NewAnswer при загрузке файла"+E);	
        };
        return true;
    }
    
    private void AnalyseStructure() {
        try {
            String DataStr;
            //String Key;
            //String Value;
            //String QuestName;
            fc = new JFileChooser();
            //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //boolean Flag=false;
                BufferedReader in = new BufferedReader( new FileReader(file));
                DataStr = in.readLine();
                while (DataStr!=null) {
                    if (FirstNum(DataStr)) {
                        NewQuest(DataStr,CheckAlterQuest(DataStr));
                        DataStr = in.readLine();
                        while (Quest(DataStr)) {
                            NewAnswer(DataStr);
                            DataStr = in.readLine();
                        }
                    } else  if (DataModule(DataStr)) {
                                DataStr = in.readLine();
                                System.out.println("Строка данных "+DataStr);	
                            } else DataStr = in.readLine();
                }
                in.close();
            }
        }   catch( FileNotFoundException e){
            System.out.println("File not found");
    	}   catch( Exception e){
            System.out.println(e);
    	}
    }
    
    public DictionaryInterface getDictionary() {
        return DI;
    }
    
    public DataModuleInterface getData() {
        return DM;
    }
    
    
}
