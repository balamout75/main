
/**
 * An adaptor, transforming the JDBC interface to the TableModel interface.
 *
 * @version 1.20 09/25/97
 * @author Philip Milne
 */

import java.util.Vector;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
//import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
//import java.rmi.Remote;

public class DimensionAdapter extends AbstractTableModel {
    INIACRMIInterface       Server;
    Statement               statement;
    ResultSet               resultSet;
    String[]                columnNames = {};
    //Vector                  rows = new Vector();
    ResultSetMetaData       metaData;
    QuestionInterface       Q1,Q2;
    DualTableAdapterInterface   Controller;
    //Vector                  values = new Vector();
    DictionaryInterface     Dictionary;
    boolean                 Percent;
    boolean                 PercentX;
    NumberFormat            myNumberFormat;

    public DimensionAdapter(DualTableAdapterInterface aController) {
        try {
            Controller = aController;
            Dictionary = Controller.getDictionary();
            Server=Controller.getServer();
            Percent=false;
            PercentX=true;
            myNumberFormat = NumberFormat.getPercentInstance();
            myNumberFormat.setMinimumFractionDigits(1);
            //myNumberFormat.setParseIntegerOnly(false);


            //RInterface RQI1=Dictionary.getQuestionPos(new Integer(0));
            //DictionaryInterface DI=Server.getMainDictionary();
            //Integer I=RQI1.getID();
            //QuestionInterface QI1=DI.getQuestion(I);
            //System.out.println("Вопрос "+QI1.getName());
            
            
        }
        catch (Exception ex) {
            System.err.println("фигня какая то"+ex);
        }
       
    }

    public void setPercent(boolean p1, boolean p2) throws SQLException {
        Percent     =p1;
        PercentX    =p2;
        try {
            if (!p1) setMode(1);
            else if (p2) setMode(2);
                 else setMode(3);
        }    
        catch (Exception ex) {
            System.err.println("фигня какая то"+ex);
        }    
    }
    
    public void setClearence(boolean p1) throws SQLException {
        try {
            Controller.setClearence(p1);
        }    
        catch (Exception ex) {
            System.err.println("фигня какая то"+ex);
        }    
    }    
    
    
    public boolean changeDimension(RInterface RQX, RInterface RQY) throws SQLException {    
        try {
            if (RQX!=null) Q1 = Server.getMainDictionary().getQuestion(RQX.getID()); else Q1=null;
            if (RQY!=null) Q2 = Server.getMainDictionary().getQuestion(RQY.getID()); else Q2=null;
            Controller.changeDimension(RQX, RQY);
            Controller.executeQuery();
            fireTableChanged(null);
            return true;
        }
        catch (Exception ex) {
            System.err.println("фигня какая то"+ex);
            return false;
        }
    }

    public int getColumnCount() {
        int i=1;
        try {
            i=Controller.getColumnCount();
        } catch (Exception ex) {
            System.err.println("Что то глюк в ColumnCount");
        }
        return i;
        //System.err.println(values.size());
        //if (Q1==null) {return Q1.GetAnswerSize();} else {return 3;}
    }

    // Data methods
    public int getRowCount() {
        int i=1;
        try {
            i=Controller.getRowCount();
        } catch (Exception ex) {
            System.err.println("Что то глюк RowCount");
        }
        return i;
    }
    
    public void setMode(int aMode) throws RemoteException {
       Controller.setMode(aMode);
    }  
    
    public Object getValueAt(int aRow, int aColumn) {
        Object Value=null;
        try {
                Value = (Object) Controller.getItem(aRow, aColumn, 1);

        } catch (Exception ex) {
            System.err.println("Ошибка в отбражении значений ячейки "+ex.toString());
            Value=new String("x");
        }    
        return Value;
    }
    
    /*
    public Object getValueAt(int aRow, int aColumn) {
        Object Value=null;
        try {
            if ((aRow==0)&(aColumn==0)) {Value=new String("");}
            else
            if (aRow==0) {
                if (aColumn!=(getColumnCount()-1)) {
                    RInterface RAI=Q1.getByPos(aColumn-1);
                    String S = Server.getMainQuestion().getAnswer(RAI.getID()).getName();
                    Value=S;
                } else {Value="Всего";}    
            } else if (aColumn==0) {
                       if (aRow!=(getRowCount()-1)) {
                            RInterface RAI=Q2.getByPos(aRow-1);
                            String S = Server.getMainQuestion().getAnswer(RAI.getID()).getName();
                            Value=S;
                       } else {Value="Всего";}         
                   } else {
                        try {
                            if (!Percent) {
                                Vector Row=(Vector) values.elementAt(aColumn-1);
                                Value = Row.elementAt(aRow-1);
                            } else if (PercentX) {
                                        Vector Row=(Vector) values.elementAt(aColumn-1);
                                        int val = new Integer((String)Row.elementAt(aRow-1)).intValue();
                                        int summ = new Integer((String)Row.elementAt(getRowCount()-2)).intValue();
                                        double Res=(double)val/summ;
                                        Value = myNumberFormat.format(Res);
                                   } else {
                                        Vector Row1=(Vector) values.elementAt(aColumn-1);
                                        Vector Row2=(Vector) values.elementAt(getColumnCount()-2);
                                        int val     = new Integer((String)Row1.elementAt(aRow-1)).intValue();
                                        int summ    = new Integer((String)Row2.elementAt(aRow-1)).intValue();
                                        double Res=(double)val/summ;
                                        Value = myNumberFormat.format(Res);
                                   }
                        } catch (Exception ex) {
                            System.err.println("Ошибка в отбражении значений ячейки "+ex.toString());
                            Value=new String("x");
                        }    
                   }
            
        } catch (Exception ex) {
            System.err.println("Ошибка в отбражении значений ячеек "+ex.toString());
            Value=new String("ww");
        }
        return Value;
    }
    */
    
    public boolean Reload() {
        fireTableChanged(null);
        //values = Controller.getRow(1);
        //resultSet = Controller.executeQuery();
        //Controller.Reload();
        System.err.println("Перечитываем");
        return true;
    }
    

    
    /*public boolean isCellEditable(int row, int column) {
        return false;
        
    }*/

    
    
}
