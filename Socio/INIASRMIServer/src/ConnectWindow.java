//import javax.swing.JInternalFrame;
import javax.swing.JRootPane;
import javax.swing.JPanel;
//import javax.swing.JTable;
import javax.swing.JLayeredPane;
//import javax.swing.border.Border;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.JButton;

import java.awt.event.*;
//import javax.swing.event.*;
import java.sql.*;
//import oracle.ucp.UniversalConnectionPoolException;
//import oracle.ucp.UniversalConnectionPoolStatistics;
//import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceImpl;
//import Subs;

public class ConnectWindow extends javax.swing.JFrame {
    public final static String UCP_POOL = "UCP-pool";
    private final PoolDataSource pds = new PoolDataSourceImpl();


    JRootPane JRootPane3 ;
    JPanel glassPane3 ;
    JLayeredPane JLayeredPane3 ;
    JPanel contentPane3 ;
    JPanel Dialog ;
    INIACRMIServer mainFrame;
    
    JPanel NickPanel ;
    JLabel NickLabel ;
    JTextField NickField ;
    
    JPanel ServerPanel ;
    JLabel ServerLabel ;
    JTextField ServerField ;
    
    JPanel DriverPanel ;
    JLabel DriverLabel ;
    JTextField DriverField ;
    
    JPanel PassPanel ;
    JLabel PassLabel ;
    JTextField PassField ;
    
    JPanel Tool ;
    JButton JButton1 ;
    JButton JButton2 ;
    //Попытка сделать нечто, подобное примерчику
    //Table 	sorter;
    //JDBCAdapter 	dataBase;
    
    
public void connect() {
    //mainFrame.sorter = new TableSorter();    				
    //начало его 
    try {
        //Class.forName(DriverField.getText());
        System.out.println("Opening db connection");
        //*************
        pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        pds.setURL(ServerField.getText());
        pds.setUser(NickField.getText());
        pds.setPassword(PassField.getText());
        pds.setConnectionPoolName(UCP_POOL);
        //pds.setMinPoolSize(20);
        //pds.setMaxPoolSize(1000);
        pds.setConnectionWaitTimeout(10);
        //pds.setInitialPoolSize(20);
        Connection con = pds.getConnection();
        Statement stmt = con.createStatement();
        mainFrame.setStatement(stmt);
        mainFrame.setConnection(con);

        mainFrame.ReadDictionaries();
        
        System.err.println("AllRight");
    }   catch (Exception ex) {
        System.err.println("Cannot initialize the db connection.");
     	System.err.println(ex.getMessage ());
    }
    
}

public  ConnectWindow(INIACRMIServer aMainFrame) {

        //sorter = aMainFrame.sorter;
    			//dataBase = aMainFrame.dataBase;
    	mainFrame = aMainFrame;    
        setSize(500,155);
        setLocation(100, 50);
        JRootPane3 = this.getRootPane();
        glassPane3 = (JPanel)JRootPane3.getGlassPane();
        JLayeredPane3 = JRootPane3.getLayeredPane();
        contentPane3 = (JPanel)JRootPane3.getContentPane();
        Dialog = new JPanel();
        Dialog.setBorder( javax.swing.BorderFactory.createBevelBorder( 0, new java.awt.Color(0,0,0), new java.awt.Color(64,64,64), new java.awt.Color(0,0,0), new java.awt.Color(64,64,64) ) );
        java.awt.GridLayout gridLayout8 = new GridLayout(  );
        gridLayout8.setColumns( 1 );
        gridLayout8.setRows( 0 );
        Dialog.setLayout( gridLayout8 )	;
        //Поле ввода имени пользователя
        NickPanel = new JPanel();
        NickPanel.setLayout( new java.awt.GridLayout() );
        NickLabel = new JLabel();
        NickLabel.setText( "Пользователь" );
        NickField = new JTextField(mainFrame.User);
        NickPanel.add(NickLabel, null, -1);
        NickPanel.add(NickField, null, -1);
        //Поле ввода пароля
        PassPanel = new JPanel();
        PassPanel.setLayout( new java.awt.GridLayout() );
        PassLabel = new JLabel();
        PassLabel.setText( "Пароль" );
        PassField = new JTextField(mainFrame.Password);
        PassPanel.add(PassLabel, null, -1);
        PassPanel.add(PassField, null, -1);
        //Поле ввода Сервера имени
        ServerPanel = new JPanel();
        ServerPanel.setLayout( new java.awt.GridLayout() );
        ServerLabel = new JLabel();
        ServerLabel.setText( "Сервер" );
        ServerField = new JTextField(mainFrame.Server);
        ServerPanel.add(ServerLabel, null, -1);
        ServerPanel.add(ServerField, null, -1);
        //Поле ввода драйвера имени
        DriverPanel = new JPanel();
        DriverPanel.setLayout( new java.awt.GridLayout() );
        DriverLabel = new JLabel();
        DriverLabel.setText("Драйвер");
        DriverField = new JTextField(mainFrame.Driver);
        DriverPanel.add(DriverLabel, null, -1);
        DriverPanel.add(DriverField, null, -1);

        Dialog.add(NickPanel, null, -1);
        Dialog.add(PassPanel, null, -1);
        Dialog.add(ServerPanel, null, -1);
        Dialog.add(DriverPanel, null, -1);

        Tool = new JPanel();
        Tool.setBorder( new javax.swing.border.SoftBevelBorder( 0, new java.awt.Color(0,0,0), new java.awt.Color(64,64,64), new java.awt.Color(0,0,0), new java.awt.Color(64,64,64) ) );
        java.awt.FlowLayout flowLayout14 = new FlowLayout(  );
        flowLayout14.setHgap(1);
        flowLayout14.setVgap(1);
        Tool.setLayout( flowLayout14 );
        JButton1 = new JButton();
        
        JButton1.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	       		
	       		//DriverManager.registerDriver (new org.firebirdsql.jdbc.FBDriver());	
	       		connect();
	          	dispose();
	       		
	       }}	
	);
	      
	JButton1.setText( "Соединить" );
	JButton2 = new JButton();
        JButton2.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e) {
	      		dispose();
 	      
	       }}	
	);
        JButton2.setText( "Выход" );
        Tool.add(JButton1, null, -1);
        Tool.add(JButton2, null, -1);
        {
            String strConstraint;
            strConstraint = "North";
            contentPane3.add(Dialog, strConstraint, -1);
            strConstraint = "South";
            contentPane3.add(Tool, strConstraint, -1);
        }
    }
    
}

