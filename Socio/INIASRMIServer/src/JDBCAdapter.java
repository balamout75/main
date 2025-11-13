
/**
 * An adaptor, transforming the JDBC interface to the TableModel interface.
 *
 * @version 1.20 09/25/97
 * @author Philip Milne
 */

import java.util.Vector;
import java.sql.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelEvent;
//import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
//import java.rmi.Remote;

public class JDBCAdapter extends AbstractTableModel {
    //Connection          connection;
    Statement           statement;
    ResultSet           resultSet;
    String[]            columnNames = {};
    Vector		rows = new Vector();
    ResultSetMetaData   metaData;
    TableAdapterInterface   Controller;
    Vector              values = new Vector();
    DictionaryInterface Dictionary;

    public JDBCAdapter(TableAdapterInterface aController) {
        Controller = aController;
        try {
            Dictionary = Controller.getDictionary();
            Controller.executeQuery();
            values=Controller.FirstRow() ;
        }
        catch (Exception ex) {
            System.err.println(ex);
        }
       
    }

    

    public void close() throws SQLException {
        System.out.println("Closing db connection");
        resultSet.close();
        statement.close();
        //connection.close();
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    protected void FirstRow() throws Exception {
        values=Controller.FirstRow();
        //fireTableDataChanged();
        fireTableChanged(null);
        //this.fireTableDataChanged()
    }
    
    protected void LastRow() throws Exception {
        values=Controller.LastRow();
        this.fireTableChanged(null);
    }
    
    protected void NextRow() throws Exception {
        values=Controller.NextRow();
        fireTableDataChanged();
        fireTableChanged(null);
    }
    
    protected void PrevRow() throws Exception {
        values=Controller.PrevRow();
        this.fireTableChanged(null);
    }
    
    protected void DelRow() throws Exception {
        values=Controller.InsertRow();
    }
    
    protected void InsertRow() throws Exception {
        values=Controller.InsertRow();
        fireTableChanged(null);
    }


    public String getColumnName(int column) {
        String Value="Ghj";
        try {
            switch(column) {
            case 0: {Value="N"; break;}
            case 1: {Value="Вопрос"; break;}
            case 2: {Value="Ответ"; break;}
            case 3: {Value="Ответ"; break;}
            }
        } catch (Exception ex) {
            System.err.println("Что то глюк в колумнНаме");
        }
        return Value;
    } 

    public int getColumnCount() {
        System.err.println(values.size());
        if (values==null) {return 2;} else {return 3;}
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
            switch(aColumn) {
            case 0: {Value=new Integer(aRow); break;}
            //case 1: {Value=Dictionary.GetQuestion(aRow).GetName(); break;}
            case 1: {Value=new Integer(aRow); break;}
            //case 2: {Value=new String("а"); break;}
            case 2: {Value=values.elementAt(aRow+1).toString(); break;}
            }
            
        } catch (Exception ex) {
            System.err.println("Ошибка в отбражении значений ячеек ");
        }
        return Value;
    }
    
    public boolean Reload() throws RemoteException {
        fireTableChanged(null);
        values = Controller.getRow(1);
        //resultSet = Controller.executeQuery();
        //Controller.Reload();
        System.err.println("Перечитываем");
        return true;
    }
    
    public void setValueAt(Object value, int row, int column) {
        try {
            //System.err.println("Чейто редактируем"+row+" "+Controller.getColumnName(row)+" "+values.elementAt(row).toString());
            Controller.updateTable(value.toString(),row+1, 2);
            //System.err.println("Чейто редактируем"+row+" "+Controller.getColumnName(row)+" "+values.elementAt(row+1).toString()+" "+value);
            values.setElementAt(value,row+1);
            System.err.println("Печатаем что то в таблицу");
        } catch (Exception e){
            System.err.println(e);
        }
        

    }
    
    public boolean isCellEditable(int row, int column) {
        boolean Flag = false;
        if (column>1) {Flag=true;};
        return Flag;
        
    }

    
    
}
