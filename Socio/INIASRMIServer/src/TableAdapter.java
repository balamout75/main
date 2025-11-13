
/**
 * An adaptor, transforming the JDBC interface to the TableModel interface.
 *
 * @version 1.20 09/25/97
 * @author Philip Milne
 */

import java.util.Vector;
import java.util.ArrayList;
import java.sql.*;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.event.TableModelEvent;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;

//import java.rmi.Remote;

public class TableAdapter extends UnicastRemoteObject implements TableAdapterInterface , Serializable {
    //public class TableAdapter extends UnicastRemoteObject {
    //Connection          connection;
    Statement           statement;
    ResultSet           resultSet;
    String[]            columnNames = {};
    Vector		rows = new Vector();
    ResultSetMetaData   metaData;
    DataModuleInterface Owner;
    DictionaryInterface Dictionary;
    InputPanelRefresherInterface inputPanelInterface;
    int                 CurrRow=0;
    ArrayList           QuestIndex;
    String              SQLSelectString;
    INIACRMIInterface   Server;
    boolean             free;

    public TableAdapter(DataModuleInterface aOwner,INIACRMIInterface aServer) throws RemoteException {
        Server=aServer;
        free=true;
        Owner = aOwner;
        try {
            Dictionary = Server.getDictionary(Owner.getDictionary());
            statement=Server.getStatement();
            SQLSelectString = Owner.makeSelectSQL();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        System.out.println(SQLSelectString);
        System.out.println("Сделали  SQL запрос");
      }

    public void executeQuery() {
        try {
            System.err.println("TableAdapter - Исполняю запрос "+SQLSelectString);
            SQLSelectString = Owner.makeSelectSQL();
            resultSet=statement.executeQuery(SQLSelectString);
            metaData = resultSet.getMetaData();
            int numberOfColumns =  metaData.getColumnCount();
            columnNames = new String[numberOfColumns];
            for(int column = 0; column < numberOfColumns; column++) {
                columnNames[column] = metaData.getColumnLabel(column+1);
            }
            rows = new Vector();
            while (resultSet.next()) {
                Vector newRow = new Vector();
                for (int i = 1; i <= getColumnCount(); i++) {
	            newRow.addElement(resultSet.getObject(i));
                }
                rows.addElement(newRow);
                free=false;
            }
        } catch (java.sql.SQLException E){
            String ErrorMSG      =   E.getLocalizedMessage();
            if (E.getErrorCode()==904) {
                try {
                    String VarName  = ErrorMSG.substring(ErrorMSG.indexOf('"')+1, ErrorMSG.lastIndexOf('"'));
                    String AlterSQL = "ALTER TABLE "+ Owner.getTableName()+" ADD "+VarName+" VARCHAR(256)";
                    System.out.println("Добавляем столбец "+AlterSQL);
                    statement.execute(AlterSQL);
                    executeQuery();
                } catch (Exception ex) {
                    System.err.println("Ошибка при добавлении колонки "+ex);
                }
            }
        } catch (Exception ex) {
            System.err.println("Ошибка в TableAdapter.executeQuery()"+ex);
        }
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public boolean getFree() {
        return free;
    }

    public int getRowCount() {
        return rows.size();
    }
    
    public void close() throws SQLException {
        System.out.println("TableAdapter - Closing db connection");
        resultSet.close();
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    public Vector FirstRow() throws RemoteException {
        Vector Res = null;
        try {
            CurrRow=0;
            Res=(Vector)rows.get(CurrRow);
        } catch (Exception Ex) {
            System.err.println("TableAdapter - ошибка в первой строке "+Ex);
        }
        
        return Res;
    }
    
    public Vector InsertRow() throws RemoteException {
        Vector Res = null;
        String InsertSql="";
        try {
            InsertSql = Owner.makeInsertSQL();
            statement.execute(InsertSql);
            executeQuery();
            CurrRow=getRowCount()-1;
            Res=(Vector)rows.get(CurrRow);
        } catch (Exception Ex) {
            System.err.println("TableAdapter - Ошибка в добавлении строки "+Ex);
        }
        return Res;
    }       
        
    public Vector DeleteRow() throws RemoteException {
        Vector Res = null;
        String DeleteSql="";
        try {
            DeleteSql = "delete from "+Owner.getTableName()+" where Row_ID="+((Vector)rows.get(CurrRow)).get(0);
            statement.execute(DeleteSql);
            rows.remove(CurrRow);
            if (CurrRow>=getRowCount()-1) {
                CurrRow--;
            }
            Res=(Vector)rows.get(CurrRow);
        } catch (Exception Ex) {
            System.err.println("TableAdapter - Ошибка в добавлении строки "+Ex);
        }
        return Res;
    }
    
    public Vector NextRow() throws RemoteException {
        Vector Res = null;
        try {
            if (CurrRow<(getRowCount()-1)) {CurrRow++;};
            Res=(Vector)rows.get(CurrRow);
        } catch (Exception Ex) {
            System.err.println(Ex);
        }
        return Res;
    }
    
    public Vector LastRow() throws RemoteException {
        Vector Res = null;
        try {
            CurrRow=getRowCount()-1;
            Res=(Vector)rows.get(CurrRow);
        } catch (Exception Ex) {
            System.err.println(Ex);
        }
        return Res;
    }
    
    public Vector PrevRow() throws RemoteException {
        Vector Res = null;
        try {
            if (CurrRow>0) {CurrRow--;};
            Res=(Vector)rows.get(CurrRow);
        } catch (Exception Ex) {
            System.err.println(Ex);
        }
        return Res;
    }
         
    public Vector Reload() throws RemoteException {
        executeQuery();
        return null;
    }
    
    public Vector getRow(int Row) throws RemoteException{
        Vector Res=null;
        try {
            if ((Row>=1)&(Row<=getRowCount())) {
                Res=(Vector)rows.get(Row-1);
                CurrRow=Row-1;
            }    
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return Res;
    }
    
    public int getCurrRow() throws RemoteException{
        return CurrRow;
    }
    
    public int Size() throws RemoteException {
        try {
            return rows.size();
        } catch (Exception E) {
            System.out.println("Ошибка создания Скрипта3 создания новой записи "+E);
            return 0;
        }
    }
    
    public DictionaryInterface getDictionary() throws RemoteException {
        return Dictionary;
    }
    
    public INIACRMIInterface getServer() throws RemoteException {
        return Server;
    }
    
    public String getColumnNames(int Column) throws RemoteException {
        String Res = "";
        try {
            Res=columnNames[Column-1];
        } catch (Exception e){
            System.err.println("Колонки " + Column);
            System.err.println("Гдюк в колумннаме" + e);
        }
        return Res;
    }
    
    public void updateTable(String Value, int FieldNum, int fixMode) throws RemoteException {
        try {
            boolean execute = statement.execute(Owner.makeUpdateSQL(Value, getColumnNames(FieldNum+1),(String)((Object)((Vector)rows.get(CurrRow)).get(0).toString())));
            if (fixMode==1) {
                String query=Owner.makeFixSQL(Value, getColumnNames(FieldNum+1),(String)((Object)((Vector)rows.get(CurrRow)).get(0).toString()));
                System.err.println("Ответ фиксируем " + query);
                statement.execute(query);
            }
            ((Vector)rows.get(CurrRow)).set(FieldNum,Value);
        } catch (Exception E){
            System.err.println("Ошибка при проведении апдейта "+E);
        }
    }
    
    public boolean setInputPanel(InputPanelRefresherInterface IPI) throws RemoteException {
        inputPanelInterface=IPI;
        return true;
    }
 

;
    
}

