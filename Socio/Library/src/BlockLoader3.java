/*
 * BlockLoader.java
 *
 * Created on 26 Апрель 2005 г., 10:22
 */
//import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.*;

    
//import java.util.List;

/**
 *
 * @author riac
 */
public class BlockLoader3 {
    private DictionaryInterface DI;
    private DataModuleInterface DM;
    private INIACRMIInterface Server;
    private String FileName="C:\f.opr";
    private String StartStr="//\t\t\t\t\t\t\t\t\t\tДА-система 4.0 - Словарь переменных";
    private QuestionInterface CurrQuest;
    private int DataRow;
    private DictionaryInterface CD;
    private TableAdapterInterface TAI;
    private DataModuleInterface DMI;
    private boolean FirstRecord=true;
    BufferedReader in;
    int i=1;
    boolean Header=true;
    String DataStr="";
    String DataRes="";
    String Key;
    String Value;
    String QuestName;
    String Cell="";
    int    CellNum;
    int    StringPos;
    int    StringNum;
    int    FileNum=1;
    boolean FirstFile;
    boolean EndStrFlag=false;
    int     QuestCount;
    int     AnswerCount;
    ArrayList  Base=new ArrayList();
    //boolean FirstFile;
    JFileChooser fc;
    File file;
    String Path;
    
    
    /** Creates a new instance of BlockLoader */
    public BlockLoader3(INIACRMIInterface aServer) {
        Server = aServer;
        try {
            CD=Server.newDictionary("Новый словарь 222",Server.getKey(),true);
            TAI = DMI.getTable();
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
                System.err.println("Blockloader - Создаем структуру");
                //DMI.setDictionary(Server.GetDictionaryIndexOf(CD));
                FirstRecord = false;
            }
            String StrSQL=DMI.makeInsertSQL();
            System.err.println("Blockloader - Строка добавления данных"+StrSQL);
            //DMI.
            
            //DMI.getStatement().execute(StrSQL);
            //TAI.execSQL(StrSQL);
            Server.executeSQL(StrSQL,null);
            //DMI.execSQL(StrSQL);
            //TAI.execSQL("commit");
            
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
        //while ((Str[i]<>',')and(Str[i]<>#9))and(i<length(Str)) do begin S:=S+Str[i];inc(i);end;
        try {
            while ((Str.charAt(i)!=',')&(Str.charAt(i)!=9)&(i<Str.length())) {
                S=S+Str.charAt(i);
                i++;
            }
            if (S.charAt(0)!='{') Integer.parseInt(S);
            i=0;
            S="";
            while (i<Str.length()) {
                if ((Str.charAt(i)!=',')&(Str.charAt(i)!=9)) {
                    S=S+Str.charAt(i);
                } else {
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
    
    private String FirstNum(String Str) {
        int i=1;
        String S="";
        String res="";
        try {
            while ((Str.charAt(i-1)!='.')&(i<Str.length())) {
                S=S+Str.charAt(i-1);
                i++;
            }
            Integer.parseInt(S);
            res = S;
        } catch (Exception E) {
            System.out.println("Это не номер "+E);	
            res = "";
        };
        return res;
        
    }
    private boolean Quest(String Str) {
        int i=1;
        boolean res=false;
        while ((Str.charAt(i-1)!=9)&(i<Str.length())) {
            i++;
        }
        if (i<2) res=true;
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
            //CurrQuest=CD.New(Str);
            //CurrQuest=Server.getMainDictionary().New(Str);
            //CD.AddQuest(CurrQuest);
            
        } catch (Exception E) {
            System.out.println("Какая то проблема в NewQuest при загрузке файла "+E);	
        };
        return true;
    }
    private boolean NewAnswer(String Str) {
        try {
            Integer Key = Server.getKey();
            CurrQuest.newAnswer(Str, Key,true);
        } catch (Exception E) {
            System.out.println("Какая то проблема в NewAnswer при загрузке файла");	
        };
        return true;
    }
    
    private boolean NextRow() {
        return true;
    }

    private boolean checkExistCell(String Name) {
        return true;
    }
    
    private boolean EndOfStr() {
        boolean EoS=false;
        int i=0;
        if ((FirstFile)|(StringPos>=DataStr.length())) {
           try {
                if (!FirstFile) {in.close();EoS=true;};
                String FileName=Path+FileNum+".csv";
                System.out.println("Файл открывается "+FileName);	
                in = new BufferedReader( new FileReader(FileName));
                while (i<StringNum) {
                    DataStr = in.readLine();
                    i++;
                }
                StringPos=0;
                FileNum++;
                FirstFile=false;
           } catch (Exception E) {
                System.out.println("Файл закончился");	
                Header=false;
                EndStrFlag=true;
                EoS=true;
                FirstFile=true;
                FileNum=1;
           };
        };
        return EoS;
    }
    
    private boolean NextCell() {
        boolean EndFile=true;
        boolean Result=true;
        String  S="";
        try {
            while ((!EndOfStr())&(DataStr.charAt(StringPos)!=';')) {
                S=S+DataStr.charAt(StringPos);
                StringPos++;
            }
            if (DataStr.charAt(StringPos)==';') {StringPos++;};
            Cell=S;
        } catch (Exception E) {
            System.out.println("Какая то фигня "+E);	 
            System.out.println("Наверное строка закончилась"+E);	 
            //Header=false;
        };
        
        return !EndStrFlag;
    } 
    
    
    private String getQuestName(String Str) {
        //Vector aa = [1,2,3,4];
        i=0;
        String Num="";
        try {
            while ((i<=Str.length())&!((Str.charAt(i)>='1')&(Str.charAt(i)<='9'))) {
                i++;
            }
            if (i<=Str.length()) {
                while ((i<=Str.length())&(Str.charAt(i)>='1')&(Str.charAt(i)<='9')) {
                    Num=Num+Str.charAt(i);
                    i++;
                }
            }
        } catch (Exception E) {
            System.out.println("Ошибка в getQuestName "+E);
            if (!((Str.charAt(i)>='1')&(Str.charAt(i)<='9'))) {Num=Str;}
        };
        return Num;
    }
    private boolean CheckQuest(String Name) {
        boolean Res=false;
        try {
            if (CurrQuest!=null) {
                Res=CurrQuest.getName().equalsIgnoreCase(Name);
            } 
        } catch (Exception E) {
            System.out.println("Ошибка в CheckQuest "+E);	
        };
        return !Res;
    }
    
    private void AnalyseStructure() {
      try {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          Path=file.getPath()+"/";
          //This is where a real application would open the file.  
          int i=1;
        //boolean 
        Header=true;
        String Key;
        String Value="";
        QuestionInterface Quest;
        DictionaryInterface Dict;
        StringNum=1;
        FirstFile=true;
        QuestCount=0;
        AnswerCount=0;
        int QuestionIndex=1;
        int CellCount=0;
        boolean fData=true;
        while (NextRow()) {
            EndStrFlag=false;
            CellNum=0;
            StringPos=0;
            while (NextCell()) {
                    CellNum++;
                    if (Header) {
                        System.out.println(Cell);
                        QuestName=getQuestName(Cell);
                        System.out.println(QuestName);
                        if (CheckQuest(QuestName)) { 
                            //CurrQuest=Server.getMainDictionary().New(QuestName);
                            //CD.AddQuest(CurrQuest);
                            CurrQuest.newAnswer(Cell, Server.getKey(),true);
                            AnswerCount=0;
                        } else {
                            CurrQuest.newAnswer(Cell, Server.getKey(),true);
                            AnswerCount++;
                        }
                        
                    } else {
                        CellCount++;
                        if (fData) {
                            //Server.getMainDictionary().GetQuestion(0);
                            fData=false;
                            //DataStr="";
                        };
                        //System.out.println(CurrQuest.GetAnswerSize());
                        //System.out.println(CurrQuest.GetAnswerArray().size());
                        if (CellCount>CurrQuest.getSize()) {
                            DataRes=DataRes+"{"+Value+" },";
                            CellCount=1;
                            //CurrQuest=Server.getMainDictionary().GetQuestion(QuestionIndex);
                            QuestionIndex++;
                            Value="";
                            if ((!Cell.equalsIgnoreCase(""))&(!Cell.equalsIgnoreCase("0"))) {Value = Value+" "+CellCount;};
                        } else {
                            if ((!Cell.equalsIgnoreCase(""))&(!Cell.equalsIgnoreCase("0"))) {Value = Value+" "+CellCount;};
                        }    
                    }
            }
            System.out.println("Все сработало как надо "+DataRes); 
            DataRes="";
            //печатаем полученую строку записываем ее в файл и записываем в таблицу
            System.out.println(DataStr);
            StringNum++;
    	}
            }
      } catch (Exception E) {
                System.out.println("Генеральная ошибка экспорта "+E);	
      };  
    }
    
    public DictionaryInterface getDictionary() {
        return DI;
    }
    
    public DataModuleInterface getData() {
        return DM;
    }
    
    
}
