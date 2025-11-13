/*
 * BlockLoader.java
 *
 * Created on 26 Апрель 2005 г., 10:22
 */
//import java.io.InputStreamReader;
//import java.io.BufferedReader;
import java.io.*;
//import java.sql.Types;
//import javax.swing.*;
//import javax.swing.filechooser.*;

/**
 *
 * @author riac
 */
public class DictionaryLoader {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    
    //private DictionaryInterface DI;
    //private DataModuleInterface DM;
    private INIACRMIInterface Server;
    //private String FileName="C:\f.opr";
    //private String StartStr="//\t\t\t\t\t\t\t\t\t\tДА-система 4.0 - Словарь переменных";
    private QuestionInterface CurrQuest;
    //private int DataRow;
    private DictionaryInterface CD;
    //private TableAdapterInterface TAI;
    //private DataModuleInterface DMI;
    //private boolean FirstRecord=true;
    private Integer Key;
    private File file;
    
    
    /** Creates a new instance of BlockLoader */
    public DictionaryLoader(INIACRMIInterface aServer, File aFile) {
        try {
            Server = aServer;
            Key = Server.getKey();
            file =aFile;
            CD=Server.newDictionary("Новый словарь ))",Key, true);
            AnalyseStructure();
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке словаря "+E);	
        };
        
    }

    public DictionaryInterface getDictionary() {
        return CD;
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
            res = false;
        }
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
            Str=Str.substring(Str.indexOf("\t"));
            int i=0;
            while (Str.startsWith("\t")) {
                Str=Str.substring(1);
                i++;
            }
            CurrQuest=Server.getMainDictionary().newQuestion(Str,Key,true);
            if (T) CurrQuest.setQuestionType(1, true); 
               else CurrQuest.setQuestionType(2, true);
            CD.addElement(Key,0,true);
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
            CurrQuest.addElement(Key,0,true);
        } catch (Exception E) {
            System.out.println("Какая то проблема в NewAnswer при загрузке файла"+E);	
        };
        return true;
    }
    
    private void AnalyseStructure() {
        try {
            String DataStr;
            //fc = new JFileChooser();
            //int returnVal = fc.showOpenDialog(null);
            //if (returnVal == JFileChooser.APPROVE_OPTION) {
            //    File file = fc.getSelectedFile();
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
                    } else DataStr = in.readLine();
                }
                in.close();
            //}
        }   catch( FileNotFoundException e){
            System.out.println("File not found");
    	}   catch( Exception e){
            System.out.println(e);
    	}
    }
}
