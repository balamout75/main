
/**
 * An adaptor, transforming the JDBC interface to the TableModel interface.
 *
 * @version 1.20 09/25/97
 * @author Philip Milne
 */

import java.util.Vector;
import java.util.ArrayList;
import java.util.HashSet;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelEvent;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.text.NumberFormat;

//import java.rmi.Remote;

public class DualTableAdapter1 extends UnicastRemoteObject implements DualTableAdapterInterface , Serializable {
//public class TableAdapter extends UnicastRemoteObject {
    Connection          connection;
    Statement           statement;
    ResultSet           resultSet;
    String[]            columnNames = {};
    Vector		rows = new Vector();
    ResultSetMetaData   metaData;
    DataModuleInterface Owner;
    DictionaryInterface Dictionary;
    InputPanelRefresherInterface inputPanelInterface;
    QuestionInterface   Q1,Q2;
    RInterface  RQX=null,RQY=null;
    int                 CurrRow=0;
    int                 RowCount=0;   
    int                 ColumnCount=0;    
    ArrayList           QuestIndex;
    String              SQLSelectString;
    INIACRMIInterface   Server;
    int                 TypeOfTable;
    ArrayList Headers   =new ArrayList();
    ArrayList Questions =new ArrayList();
    
    ArrayList Rows      =new ArrayList();
    ArrayList Row       =new ArrayList();
    Record    RootBranch=null;
    ArrayList XHeader   =new ArrayList();
    ArrayList YHeader   =new ArrayList();
    NumberFormat        myNumberFormat;
    int                 Mode=0;
    
    

    public DualTableAdapter1(DataModuleInterface aOwner,INIACRMIInterface aServer) throws RemoteException {
        Server=aServer;
        statement=Server.getStatement();
        Owner = aOwner;
        Dictionary = Server.getDictionary(Owner.getDictionary());
        myNumberFormat = NumberFormat.getPercentInstance();
        myNumberFormat.setMinimumFractionDigits(1);
      
    }

    public void setMode(int aMode) throws RemoteException {
       Mode=aMode;
    }    
    
    public boolean changeDimension(RInterface aRQX, RInterface aRQY) throws RemoteException {
       try {
            RQX=aRQX;
            RQY=aRQY;
            //executeQuery();
            return true;
       } catch (Exception ex) {
            System.err.println(ex);
            return false;
       }
    }

    public void executeQuery() {
        try {
            System.err.println("начинаем executeQuery()");
            Q1=null; Q2=null;
            Questions.clear();
            Headers.clear();
            String QuestBlock ="";
            String SQLStr     ="Select ";
            String NameX      ="";
            String NameY      ="";
            int Rang          =0;

            
            if ((RQX==null)&(RQY==null)) {
                TypeOfTable=0;
            }   else if (RQX==null) {
                    Q1 = Server.getMainDictionary().getQuestion(RQY.getID()); 
                    NameY = "Quest"+RQY.getID();
                    QuestBlock =NameY;
                    Questions.add(Q1);
                    Rang = 1;

                    TypeOfTable=1;
                }   else if (RQY==null) {
                        Q1 = Server.getMainDictionary().getQuestion(RQX.getID()); 
                        NameX = "Quest"+RQX.getID();
                        QuestBlock =NameX;
                        Questions.add(Q1);
                        Rang = 1;

                        TypeOfTable=2;
                    }   else {
                            Q1 = Server.getMainDictionary().getQuestion(RQX.getID()); 
                            Q2 = Server.getMainDictionary().getQuestion(RQY.getID()); 
                            NameX = "Quest"+RQX.getID();
                            NameY = "Quest"+RQY.getID();                            
                            QuestBlock =NameX+","+NameY;
                            Questions.add(Q1);
                            Questions.add(Q2);
                            Rang = 2;

                            TypeOfTable=3;
                        }
            SQLStr=SQLStr+QuestBlock+",count(*) from "+Owner.getTableName()+" group by "+QuestBlock+" order by "+QuestBlock;
            System.err.println("TableAdapter - Исполняю запрос "+SQLStr);
            resultSet=statement.executeQuery(SQLStr);
            RootBranch=new Record("root",0);
            while (resultSet.next()) {
                 int Value = resultSet.getInt(Rang+1);
                 AnalyzeRow(1,Rang,Value,RootBranch);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка в построении Двумерного распределения "+ex);
        }
        System.out.println("Все сделали ");
    }
    
    public void AnalyzeRow(int Current, int Rang, int Summ, Record Branch) {
        Record CurrentBranch=null;
        ArrayList DimensiomHeads=null;
        try {

            HeadRecord CurrentHead=null;
            String Dimension = resultSet.getString(Current); if (Dimension==null) Dimension="";
            if (true) {
                if (!(Current>Headers.size())) DimensiomHeads=(ArrayList)Headers.get(Current-1);
                
                if (DimensiomHeads==null) {
                    CurrentHead = new HeadRecord(Dimension);
                    DimensiomHeads = new ArrayList();
                    DimensiomHeads.add(CurrentHead);
                    Headers.add(DimensiomHeads);
                } else {
                    int i=0;
                    boolean SearchFlag=false;
                    while ((i<DimensiomHeads.size())&!SearchFlag) {
                        CurrentHead=(HeadRecord)DimensiomHeads.get(i);
                        SearchFlag=CurrentHead.Name.equalsIgnoreCase(Dimension);
                        i++;
                    }
                    if (!SearchFlag) {
                        CurrentHead=new HeadRecord(Dimension);
                        DimensiomHeads.add(CurrentHead);
                    }
                }
                int i=0;
                boolean SearchFlag=false;
                while ((i<Branch.Branches.size())&!SearchFlag) {
                    CurrentBranch=(Record)Branch.Branches.get(i);
                    SearchFlag=CurrentBranch.Name.equalsIgnoreCase(Dimension);
                    i++;
                }
                if (!SearchFlag) {
                    CurrentBranch = new Record(Dimension);
                    Branch.Branches.add(CurrentBranch);
                }
                //CurrentHead.Count=CurrentHead.Count+Summ;
                if (Current<Rang) AnalyzeRow(Current+1,Rang,Summ,CurrentBranch);
                else CurrentBranch.Count=CurrentBranch.Count+Summ;
            }   
        } catch (Exception ex) {
            System.err.println("Ошибка в AnalyzeRow "+ex);
        }    
    }

    public void setClearence(boolean aClearence) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
            
    
    class HeadRecord {
        //int     Header=0;
        int     Count =0;
        String  Name;
        public  HeadRecord (String aName) {
            Name=aName;
        }
    }
    
    class Record {
        String      Name  ="";
        int         Count =0;
        int         Summ  =0;
        ArrayList   Branches=new ArrayList();
        public  Record (String aName,int aCount) {
            Name    =aName;
            Count   =aCount;
        }
        public  Record (String aName) {
            Name    =aName;
        }
        
    }    
    
    public int getColumnCount() throws RemoteException{
        int Res = 2;
        try {
            ArrayList AL=(ArrayList) Headers.get(1);
            Res=AL.size()+2;
        } catch (Exception ex) {
            
        } 
        return Res;
    }

    public int getRowCount() throws RemoteException{
        int Res = 2;
        try {
            ArrayList AL=(ArrayList) Headers.get(0);
            Res=AL.size()+2;
        } catch (Exception ex) {
            
        } 
        return Res;        
    }

    public String getItem(int XRow,int YRow)  throws RemoteException{
        return "";
    }    
    
    public String getItem(int XRow,int YRow, int mode)  throws RemoteException{
        ArrayList RequestList = new ArrayList();
        RequestList.add(new Integer(XRow));
        RequestList.add(new Integer(YRow));
        return doSurf(RootBranch,RequestList,1);
    }
    
    public String getHeader(ArrayList RequestList,int Dimension)  throws RemoteException{
        String ResultStr="";
        int                 Coord           = ((Integer)RequestList.get(Dimension-1)).intValue();
        int Range=0;
        QuestionInterface   QI             =null;
        ArrayList           DimensionHead  = null;
        try {
            DimensionHead   = (ArrayList)Headers.get(Dimension-1);
            QI              = (QuestionInterface)Questions.get(Dimension-1);
            Range = DimensionHead.size();
        } catch (Exception ex) {
            
        }        
        ResultStr="";
        if (Coord==0) {
            //"Заголовок столбцов"
        } else if (Coord==(Range+1)) {
                    ResultStr="Всего";
               } else {
                    HeadRecord HR = (HeadRecord)DimensionHead.get(Coord-1);
                    RInterface RI = QI.getByPos(new Integer(HR.Name).intValue()-1);
                    ResultStr=Server.getMainQuestion().getAnswer(RI.getID()).getName();
               }
        
        return ResultStr;
    }
    
    public int getHeaderSumm(ArrayList DimensionHead) {
        int Summ=0;
        int i=0;
        while (i<DimensionHead.size()) {
            Summ=Summ+((HeadRecord)DimensionHead.get(i)).Count;
            i++;
        }
        return Summ;
    }
    
    
    public String getHeaderCount(ArrayList RequestList,int Dimension)  throws RemoteException{
        int                 Coord           = ((Integer)RequestList.get(Dimension-1)).intValue();
        ArrayList           DimensionHead   = null;
        int Range = 0;
        try {
            DimensionHead   = (ArrayList)Headers.get(Dimension-1);
            Range = DimensionHead.size();
        } catch (Exception ex) {
            DimensionHead   = (ArrayList)Headers.get(Dimension-2);
            Range = 0;
        }  
        String ResultStr="";
        if (Coord==0) {
            ResultStr="Всего";
        } else if (Coord==(Range+1)) {
                    if (Dimension==2) ResultStr=new Integer(getHeaderSumm(DimensionHead)).toString();
               } else {
                    HeadRecord HR = (HeadRecord)DimensionHead.get(Coord-1);
                    ResultStr=new Integer(HR.Count).toString();
               }
        return ResultStr;
    }
    
    public String doSurf(Record Branch, ArrayList RequestList,int Dimension)  throws RemoteException{
        String ResultStr="";
        int                 Coord           = ((Integer)RequestList.get(Dimension-1)).intValue();
        ArrayList           DimensionHead   = null;
        int                 Range           =0;
        try {
            DimensionHead   = (ArrayList)Headers.get(Dimension-1);
            Range = DimensionHead.size();
        } catch (Exception ex) {
        }    
        if (Coord==0) {
            try {
                ResultStr=getHeader(RequestList,(3-Dimension));
            } catch (Exception ex) {
                ResultStr="#";
            }
        } else 
            if (Coord==(Range+1)) {
                try {
                    ResultStr=getHeaderCount(RequestList,(3-Dimension));
                } catch (Exception ex) {
                    ResultStr="#";
                }
            } else {
                Record CurrentBranch=(Record)Branch.Branches.get(Coord-1);
                if (Dimension==2) {
                    Integer znamen=1;
                    switch (Mode) {
                            case 1: {
                                ResultStr=Integer.toString(CurrentBranch.Count);
                                break;
                            }
                            case 2: {
                                znamen=new Integer(getHeaderCount(RequestList,(2)));
                                double Res=(double)CurrentBranch.Count/znamen;
                                ResultStr=myNumberFormat.format(Res);                                
                                break;
                            }
                            case 3: {
                                znamen=new Integer(getHeaderCount(RequestList,(1)));
                                double Res=(double)CurrentBranch.Count/znamen;
                                ResultStr=myNumberFormat.format(Res);
                                break;
                            }
                    }  
                    
                    
                } else {
                    ResultStr=doSurf(CurrentBranch, RequestList,Dimension+1);
                }
            }
        return ResultStr;
    }
      
    public void close() throws SQLException {
        System.out.println("TableAdapter - Closing db connection");
        resultSet.close();
    }

    public Vector Reload() throws RemoteException {
        executeQuery();
        return rows;
    }
    
    public Vector getVector() throws RemoteException{
        return rows;
    }
    
    public DictionaryInterface getDictionary() throws RemoteException {
        return Dictionary;
    }
    
    public INIACRMIInterface getServer() throws RemoteException {
        return Server;
    }
    
;
    
}

