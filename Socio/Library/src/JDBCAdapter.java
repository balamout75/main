
/**
 * An adaptor, transforming the JDBC interface to the TableModel interface.
 *
 * @version 1.20 09/25/97
 * @author Philip Milne
 */

import java.util.Vector;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.ImageIcon;
//import javax.swing.event.TableModelEvent;
//import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
//import javax.swing.JComboBox;

//import java.rmi.Remote;

public class JDBCAdapter extends AbstractTableModel {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    INIACRMIInterface       Server;
    //Statement               statement;
    //ResultSet               resultSet;
    String[]                columnNames = {};
    Vector                  rows = new Vector();
    ResultSetMetaData       metaData;
    TableAdapterInterface   Controller;
    Vector                  values = new Vector();
    DictionaryInterface     Dictionary;
    ImageIcon IconQ1 = new ImageIcon("images/gif/05.gif");
    ImageIcon IconQ2 = new ImageIcon("images/gif/06.gif");
    ImageIcon IconQ3 = new ImageIcon("images/gif/07.gif");


    public JDBCAdapter(TableAdapterInterface aController) {
        Controller = aController;
        try {
            Dictionary = Controller.getDictionary();
            Server = Controller.getServer();
            Controller.executeQuery();
            values=Controller.FirstRow() ;
        }
        catch (Exception ex) {
            System.err.println("таблица пуста "+ex);
            if (values==null) {values = new Vector();
                values.add(new String ("пусто"));
            }
        }
    }

    

    public void close() throws SQLException {
        System.out.println("Closing db connection");
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    protected void FirstRow() throws Exception {
        values=Controller.FirstRow();
    }
    
    protected void LastRow() throws Exception {
        values=Controller.LastRow();
        //this.fireTableChanged(null);
    }
    
    protected void NextRow() throws Exception {
        values=Controller.NextRow();
        //fireTableDataChanged();
        //fireTableChanged(null);
    }
    
    protected void PrevRow() throws Exception {
        values=Controller.PrevRow();
        //this.fireTableChanged(null);
    }
    
    protected void DelRow() throws Exception {
        values=Controller.DeleteRow();
        //fireTableChanged(null);
    }
    
    protected void InsertRow() throws Exception {
        values=Controller.InsertRow();
    }

    protected void getRow(int i) throws Exception {
        values=Controller.getRow(i);
    }
    
    public String getColumnName(int column) {
        String Value="Ghj";
        try {
            switch(column) {
            case 0: {Value="N"; break;}
            case 1: {Value="Вопрос"; break;}
            case 2: {Value="Тип"; break;}
            case 3: {Value="Ответ"; break;}
            }
        } catch (Exception ex) {
            System.err.println("Что то глюк в колумнНаме");
        }
        return Value;
    } 

    public int getColumnCount() {
        System.err.println(values.size());
        if (values==null) {return 4;} else {return 4;}
    }

    // Data methods
    public int getRowCount() {
        int Count=1;
        try {
            Count = Dictionary.getSize();
        } catch (Exception ex){
            System.err.println("Ошибка в поиске колонок ");    
        }
        return Count;
    }
    
    public Class getColumnClass(int column) {
        return String.class;
    }

    public Object getValueAt(int aRow, int aColumn) {
        Object Value=null;
        try {
            RInterface RQI=Dictionary.getByPos(new Integer(aRow));
            QuestionInterface QI = Server.getMainDictionary().getQuestion(RQI.getID()) ;
            switch(aColumn) {
            case 0: {
                      Value=new Integer(aRow+1); break;
                    }
            case 1: {
                      Value=QI.getName(); break;
                    }
            case 2: {
                      switch(QI.getQuestionType()) {
                        case 1: {Value=new String("Текст А"); break;}
                        case 2: {Value=new String("Текст Н"); break;}
                        case 3: {Value=new String("Другое"); break;}
                      }
                      break;
                    }
            case 3: {
                      try {
                        Object Obj=values.elementAt(aRow+1);
                        if (Obj!=null) {
                            String Str=Obj.toString();
                            Value=Str;
                        } else Value="";
                      } catch (Exception ex) {
                        System.err.println("Ошибка в отбражении значения переменной "+ex.toString());
                      }
                      break;
                    }
            }
        } catch (Exception ex) {
            System.err.println("Ошибка в отбражении значений ячеек 2 "+ex.toString());
            Value="";
        }
        return Value;
    }
    
    public boolean Reload() throws RemoteException {
        values = Controller.getRow(1);
        System.err.println("Перечитываем");
        return true;
    }
    
    public void setValueAt(Object value, int row, int fixMode) {
        try {
            Controller.updateTable(value.toString(),row+1,fixMode);
            values.setElementAt(value,row+1);
            System.err.println("Печатаем что то в таблицу");
        } catch (Exception e){
            System.err.println(e);
        }
        

    }
    
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    
    
}
