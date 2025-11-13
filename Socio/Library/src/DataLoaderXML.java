/*
 * BlockLoader.java
 *
 * Created on 26 Апрель 2005 г., 10:22
 */
//import java.io.InputStreamReader;
//import java.io.BufferedReader;
import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


/**
 *
 * @author riac
 */
public class DataLoaderXML {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    

    private INIACRMIInterface Server;
    private int DataRow;
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
        int getType() {
            return QuestType;
        } 
        
    }
    
    
    /** Creates a new instance of BlockLoader */
    public DataLoaderXML(DataModuleInterface aDMI, File aFile) {
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
   
    /*private String CreateRec(String Str) {
        System.out.println("Строка данных "+Str );	
        String Res="";
        try {
            String StrSQL=DMI.makeInsertSQL();
            System.err.println("Blockloader - Строка добавления данных"+StrSQL);
            Res=StrSQL.substring(StrSQL.indexOf("values (")+8,StrSQL.lastIndexOf(")") );
            Server.executeSQL(StrSQL,null);
        } catch (Exception E) {
            System.err.println("Blockloader - Ошибка в выполнении добавления "+E);	
        }
        return Res;
    }*/
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
                Integer CurrValue=new Integer(StrValues[i]);
                int j=0;
                boolean SearchFlag=false;
                QuestRecord QR = null;
                //QuestionInterface QI=null;
                while ((j<QuestMetaData.size())&!SearchFlag) {
                    QR= (QuestRecord) QuestMetaData.get(j);
                    SearchFlag=(CurrValue.compareTo(QR.getBaseNum())>0)&(CurrValue.compareTo(QR.getLastNum())<=0);
                    j++;
                }
                if (SearchFlag) {
                    int k=0;
                    boolean Flag=false;
                    Integer Result = CurrValue-QR.getBaseNum();
                    String QuestName="Quest"+QR.QuestID;
                    while ((k<QuestNames.size())&!Flag) {
                        Flag=QuestName.equalsIgnoreCase((String) QuestNames.get(k));
                        k++;
                    }
                    if (Flag) {
                        String StrValue=(String) Values.get(k-1);
                        StrValue = StrValue+" "+Result.toString();
                        Values.set((k-1),StrValue);
                    } else {
                        QuestNames.add(QuestName);
                        Values.add("'"+Result.toString()+"'");
                    }
                }
                i++;
            }
            int k=0;
            String Names = "";
            String Value = "";
            while (k<QuestNames.size()) {
                Names=Names+QuestNames.get(k)+",";
                Value=Value+"'"+Values.get(k)+"',";
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
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("a");
                int i = nList.getLength();
                for (int j=0;j<i;j++) {
                    String DataStr="";
                    Node ND=nList.item(j);
                    NodeList nQList=ND.getChildNodes();
                    int ii= nQList.getLength();
                    for (int jj=0; jj<ii; jj++) {
                        Node Question=nQList.item(jj);
                        if (Question.getNodeType()==Node.ELEMENT_NODE) {
                            Element qEl = (Element) Question;
                            String cQCode=qEl.getAttribute("c");
                            NodeList nAList = Question.getChildNodes();
                            int iii=nAList.getLength();
                            for (int jjj=0; jjj<iii; jjj++) {
                                Node Answer=nAList.item(jjj);
                                if (Answer.getNodeType()==Node.ELEMENT_NODE) {
                                    Element aEl = (Element) Answer;
                                    String cACode=aEl.getAttribute("c");
                                    DataStr=DataStr+cACode+",";
                                }
                            }
                        }
                    }
                    DataStr=DataStr+"999";
                    //System.out.println("Строка на загрузку:"+DataStr);
                    System.out.println(j+" cтрока данных "+DataStr);
                    if (DataModule(DataStr)) {
                        System.out.println("Успешно");	
                    }
                }    
            }
        } catch( Exception e){
            System.out.println("Ошибка в XMLAnalyseStructure"); 
        } 
    }
}
