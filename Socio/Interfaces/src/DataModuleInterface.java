/*
 * JavaModuleInterface.java
 *
 * Created on 13 Март 2004 г., 18:43
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 *
 * @author  Иван
 */
public interface DataModuleInterface  extends Remote, DQAInterface {
    //public int getType() throws RemoteException;
    public boolean                  createTable() throws RemoteException;
    public void                     setRefresher(LibraryRefreshInterface aLib) throws RemoteException;
    public DataModuleInterface      getRoot(int i) throws RemoteException;
    public String                   makeSelectSQL() throws RemoteException;
    public String                   makeInsertSQL() throws RemoteException;
    public String                   makeCreateSQL() throws RemoteException;
    public String                   makeMesureSQL (Integer Q1, int A1, Integer Q2, int A2) throws RemoteException;
    public String                   makeUpdateSQL(String Value, String ColumnName, String id_Key ) throws RemoteException;
    public String                   makeFixSQL   (String Value, String ColumnName, String id_Key ) throws RemoteException;
    public void                     setEditable (boolean aEditable) throws RemoteException;
    public boolean                  isEditable () throws RemoteException;
    //public void setComment(String aComment) throws RemoteException;
    public String                   getComment() throws RemoteException;
    //public void     setCondition(String aCondition) throws RemoteException;
    public String                   getCondition() throws RemoteException;
    //public void     setParent(Integer aparentID) throws RemoteException;
    public Integer                  getParent() throws RemoteException;
    public String                   getTableName() throws RemoteException;
    public String                   getRealTableName() throws RemoteException;
    //public void setTableName(String TableName) throws RemoteException;    
    public void                     setServer(INIACRMIInterface aServer) throws RemoteException;
    public INIACRMIInterface        getServer() throws RemoteException;
    public TableAdapterInterface    getTable() throws RemoteException;
    public DualTableAdapterInterface getDualTable() throws RemoteException;
    public void                     delTableAdapter(TableAdapterInterface TAI) throws RemoteException;
    public Integer                  getDictionary() throws RemoteException;
    public void                     initModules(INIACRMIInterface aServer) throws RemoteException;

    //public void setName(String aComment, boolean Flag) throws RemoteException;
    public int setComment(String aComment, Integer LibID, boolean Flag) throws RemoteException;
    public int setCondition(String aCondition, Integer LibID, boolean Flag) throws RemoteException;
    public int setParent(Integer aparentID, Integer LibID, boolean Flag) throws RemoteException;
    
    public int setTableName    (String TableName, Integer LibID, boolean Flag) throws RemoteException;    
    
    public int setID          (Integer ID, Integer LibID, boolean Flag) throws RemoteException;
    public int setDictionary   (Integer S, Integer LibID, boolean Flag) throws RemoteException;    
    public int setAnnotation   (String  S, Integer LibID, boolean Flag) throws RemoteException;
    public int setStartDate    (String  S, Integer LibID, boolean Flag) throws RemoteException;
    public int setEndDate      (String  S, Integer LibID, boolean Flag) throws RemoteException;
    public int setQuota        (Integer S, Integer LibID, boolean Flag) throws RemoteException;
    
    public String   getAnnotation   () throws RemoteException;
    public String   getStartDate    () throws RemoteException;
    public String   getEndDate      () throws RemoteException;
    public Integer  getQuota        () throws RemoteException;

}
