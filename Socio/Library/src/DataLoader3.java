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
public class DataLoader3 {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    

    private INIACRMIInterface Server;
    private int DataRow;
    private DataModuleInterface DMI;
    private File file;
    
    
    /** Creates a new instance of BlockLoader */
    public DataLoader3(DataModuleInterface aDMI) {
        try {
            DMI=aDMI;
            Server = DMI.getServer();
            File file1 =new File("D:/Proceed/in/M1/res.");
            File file2 =new File("D:/Proceed/in/M2/res.");
            File file3 =new File("D:/Proceed/in/M3/res.");
            File file4 =new File("D:/Proceed/in/M4/res.");
            File file5 =new File("D:/Proceed/in/M5/res.");
            File file6 =new File("D:/Proceed/in/N1/res.");
            File file7 =new File("D:/Proceed/in/N2/res.");
            File file8 =new File("D:/Proceed/in/N3/res.");
            File file9 =new File("D:/Proceed/in/N4/res.");
            File file10=new File("D:/Proceed/in/N5/res.");            
            String TableName="Table126791";
            //Server.executeSQL("Delete from "+TableName, null);            
            
            System.err.println("D:/Proceed/in/M1/res.");	
            AnalyseStructure(file1,TableName,1);
            System.err.println("D:/Proceed/in/M2/res.");	
            AnalyseStructure(file2,TableName,2);
            System.err.println("D:/Proceed/in/M3/res.");	
            AnalyseStructure(file3,TableName,3);            
            System.err.println("D:/Proceed/in/M4/res.");	
            AnalyseStructure(file4,TableName,4);
            System.err.println("D:/Proceed/in/M5/res.");	
            AnalyseStructure(file5,TableName,5);
            System.err.println("D:/Proceed/in/N1/res.");	
            AnalyseStructure(file6,TableName,6);
            System.err.println("D:/Proceed/in/N2/res.");	
            AnalyseStructure(file7,TableName,7);
            System.err.println("D:/Proceed/in/N3/res.");	
            AnalyseStructure(file8,TableName,8);
            System.err.println("D:/Proceed/in/N4/res.");	
            AnalyseStructure(file9,TableName,9);            
            System.err.println("D:/Proceed/in/N5/res.");	
            AnalyseStructure(file10,TableName,10);            
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
        };
        
    }
   
   
    private boolean DataModule(String Str, String TableName, int DeviceNum) {
        java.util.ArrayList QuestNames    =   new java.util.ArrayList();
        java.util.ArrayList Values        =   new java.util.ArrayList();
        boolean res=true;
        try {
            //String NumS=Server.getDictionary(DMI.getDictionary()).getByPos(Col).getID().toString();
            String [] StrValues = Str.split(",");
            DictionaryInterface DI=Server.getDictionary(DMI.getDictionary());
            int i=0;
            while (i<StrValues.length) {
                Integer CurrValue=new Integer(StrValues[i]);
                int j=0;
                boolean SearchFlag=false;
                Integer QuestID=null;
                QuestionInterface QI=null;
                while ((j<DI.getSize())&!SearchFlag) {
                    QuestID=Server.getDictionary(DMI.getDictionary()).getByPos(j).getID();
                    QI=Server.getMainDictionary().getQuestion(QuestID);
                    SearchFlag=(CurrValue.compareTo(QI.getBase())>0)&(CurrValue.compareTo(QI.getBase()+QI.getSize()+1)<0);
                    j++;
                }
                if (SearchFlag) {
                    int k=0;
                    boolean Flag=false;
                    Integer Result = CurrValue-QI.getBase();
                    String QuestName="Quest"+QuestID;
                    while ((k<QuestNames.size())&!Flag) {
                        Flag=QuestName.equalsIgnoreCase((String) QuestNames.get(k));
                        k++;
                    }
                    if (Flag) {
                        String StrValue=(String) Values.get(k-1);
                        StrValue = StrValue+" "+Values;
                    } else {
                        QuestNames.add(QuestName);
                        Values.add(Result.toString());
                    }
                }
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
            
            Names=Names+"Quest126748, Row_id";
            Value=Value+DeviceNum+","+Server.getKey().toString();
            String SQLStr = "INSERT INTO "+TableName+" ("+Names+") VALUES ("+Value+")";
            //System.out.println("Запрос   "+SQLStr);	
            Server.executeSQL(SQLStr, null);
        } catch (Exception E) {
            System.err.println("Ошибка в загрузке циферок "+E);	
            res = false;
        }
        return res;
    }
    
    private void AnalyseStructure(File file, String TableName, int DeviceNum) {
        try {
            if (DMI.getDictionary()!=null) {
                String DataStr;
                String AnalyseStr="";
                BufferedReader in = new BufferedReader( new FileReader(file));
                DataStr = in.readLine();
                while (DataStr!=null) {
                    DataStr=DataStr.trim();
                    //AnalyseStr=AnalyseStr+DataStr; временно отключаем
                    AnalyseStr=DataStr;
                    if (AnalyseStr.endsWith(",999")) {
                        if (DataModule(AnalyseStr,TableName,DeviceNum)) {
                        }
                        AnalyseStr="";
                    }
                    DataStr = in.readLine();
                }
                in.close();
            } else {System.out.println("Словарь не задан");}  
        } catch( FileNotFoundException e){
            System.out.println("File not found  "+e);
    	} catch( Exception e){
            System.out.println(e);
    	}
    }
}
