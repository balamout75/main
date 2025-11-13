import java.rmi.registry.*;
import java.rmi.Naming;
import javax.swing.tree.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.RemoteException;
import javax.swing.event.*;
//import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.*;



/*
 * Library.java
 *
 * Created on 10 Март 2004 г., 10:09
 */

/**
 *
 * @author  Administrator
 */
public class Library extends javax.swing.JPanel  implements LibraryInterface {

    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;

    static int datafolder     = 1;
    static int datamodule     = 2;
    static int datablock      = 3;
    static int datafilter     = 4;
    static int data           = 5;
    static int dictionary     = 6;
    static int question       = 7;
    static int answer         = 8;
    private static final int PREFERRED_WIDTH = 720;
    private static final int PREFERRED_HEIGHT = 640;

    public INIACRMIInterface Server;
    private LibraryRefreshClient Refresher;
    //private DataModuleInterface DataRoot;
    private RInterface RootPoint;

    private SocioInput SocioInputFrame;
    private JPopupMenu jTreeMenuItem;
    private JPopupMenu jTreeMenuItemDataBlock;
    private JPopupMenu jTreeMenuItemDataFolder;
    private JPopupMenu jTreeMenuItemDataModule;
    private JPopupMenu jTreeMenuItemDataFilter;
    private JPopupMenu jTreeMenuItemData;
    //QEP
    //private JPopupMenu jTreeMenuItemRoot;
    private JPopupMenu jTreeMenuItemDictionary;
    private JPopupMenu jTreeMenuItemQuestion;
    private JPopupMenu jTreeMenuItemAnswer;
    private JPopupMenu jTreeMenuItemFree;
    
    private int DictionaryIndex;

    private DQAMarkInterface nodeInfo;
    private DefaultMutableTreeNode node;
    private JMenu jTreeQuestType;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;
    public javax.swing.JPanel DetailPanel2 = new javax.swing.JPanel();
    //QEP

    

    public JDesktopPane desktop = null;
    private DataModuleInterface DMI;
    private RInterface RDMI;

    public  DataModuleEditorPanel DMEP;
    public  DictionaryEditorPanel DEP;
    public  EditorTreePanel ETP;
    public  ConstrainsQuestion      QuestConstr;
    public  ConstrainsAnswer        AnswerConstr;
    public  ConstrainsDataModule    DataModuleConstr;
    public  ConstrainsDataBlock     DataBlockConstr;
    public  ConstrainsFolder        FolderConstr;
    public  ConstrainsDictionary    DictionaryConstr;
    //private int TypeofToolbar;
    private boolean ETPInited;
    public  InputPanel CurrentInputPanel;
    public  DimensionPanel CurrentDimensionPanel;
    public  Library2Applet applet;
    private JFrame frame = null;
    int DetailFormAutomatState=0;
    int LastState=0;
    private String ServerName   = "tower";
    private String RMIName      = "INIACRMIInterface";
    private int RMIPort      = 1099;

    /** Creates new form Library */
    public boolean isApplet() {
        return (applet != null);
    }

    public static JFrame createFrame(GraphicsConfiguration gc) {
        JFrame frame = new JFrame(gc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }

    public JFrame getFrame() {
        return frame;
    }
    
    public void localRefresher(DQAInterface DQA, int type) {
        /*
         новая процедура ...
         соображения        
         * 
         */
    }    

    public void showLibrary() {
        if(!isApplet() && getFrame() != null) {
            JFrame f = getFrame();
            f.setTitle("Социостудия");

            f.getContentPane().add(this, BorderLayout.CENTER);
            f.pack();

            Rectangle screenRect = f.getGraphicsConfiguration().getBounds();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                    f.getGraphicsConfiguration());
            int centerWidth = screenRect.width < f.getSize().width ?
                    screenRect.x :
                    screenRect.x + screenRect.width/2 - f.getSize().width/2;
            int centerHeight = screenRect.height < f.getSize().height ?
                    screenRect.y :
                    screenRect.y + screenRect.height/2 - f.getSize().height/2;

            centerHeight = centerHeight < screenInsets.top ?
                    screenInsets.top : centerHeight;

            f.setLocation(centerWidth, centerHeight);
            f.show();

	}
    }

    public Library (Library2Applet applet) {
        this(applet, null);
    }
    
    protected void initConnectParametrs() {
    	try {
            
            String DataStr;
            String Key;
            String Value;

            //BufferedReader in = new BufferedReader( new FileReader("params.ini"));
            java.io.BufferedReader in = new java.io.BufferedReader( new java.io.FileReader("params.ini"));
            //d:/work/socio/store.dat
            while((DataStr = in.readLine())!= null) {
                if (DataStr.indexOf("=")!=-1) {
                    Key=DataStr.substring(0,DataStr.indexOf("="));
                    System.err.println(Key);
                    Value=DataStr.substring(DataStr.indexOf("=")+1);
                    System.err.println(Value);
                    if (Key.equalsIgnoreCase("RMIServer"))  ServerName = Value;
                    else if (Key.equalsIgnoreCase("RMIName"))  RMIName = Value;
                        else if (Key.equalsIgnoreCase("RMIPort"))  RMIPort = (new Integer(Value)).intValue();
                }
            }
            in.close();
        }   catch( Exception e){
            System.out.println("File not found");
    	}
        //RestoreCollections();
        
        
        
    }

    public Library  (Library2Applet applet, GraphicsConfiguration gc) {
        this();
        this.applet = applet;

        if (!isApplet()) {
            frame = createFrame(gc);
            frame.getContentPane().add(this);
            frame.setJMenuBar(jMenuBar1);
        } else {
            applet.setJMenuBar(jMenuBar1);
        }

        //setLayout(new BorderLayout());
        //setPreferredSize(new Dimension(PREFERRED_WIDTH,PREFERRED_HEIGHT));


        // Show the demo. Must do this on the GUI thread using invokeLater.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            showLibrary();
            }
        });
    }

    public Library() {
        try	{
            initConnectParametrs();
            System.out.print("Connecting.....");
            System.out.println("//"+ServerName+"/"+RMIName);
            //Registry registry = LocateRegistry.createRegistry(RMIPort);
            Server = (INIACRMIInterface)Naming.lookup("//"+ServerName+"/"+RMIName);	
            
            Refresher=new LibraryRefreshClient(this,Server.getKey());
            //Refresher.Register(this);            
            Server.Register(Refresher);
            System.out.println("....Ok");
            System.out.println( "Сохранили словарь" );	   
            System.out.println( "Ибо обещал" );
	}
	catch( Exception e )
	{
	    System.out.println("....Failed");
	    System.out.println( e.toString() );
	    System.exit(1);
	}
        initComponents();
        tuneComponent();
    }
    
    

    public class myInternalFrame extends JInternalFrame {
        private InputPanel inputPanel;

        public myInternalFrame(InputPanel aInputPanel) throws RemoteException {
            inputPanel = aInputPanel;
            try {
                setClosable(true);
                setMaximizable(true);
                setResizable(true);
                add(inputPanel);
                setBounds(20, 20, 400, 340);
                setSelected(true);
            } catch( Exception re ) {
                System.out.println("Library Ошибка при открытии формы визарда нового измерения"+re.toString());	   
            }
        }
        public InputPanel getInputPanel() throws RemoteException {
            return inputPanel;
        }

    }

public class DimensionInternalFrame extends JInternalFrame {
        private DimensionPanel DimPanel;

        public DimensionInternalFrame(DimensionPanel aDimPanel) throws RemoteException {
            DimPanel = aDimPanel;
            try {
                setClosable(true);
                setMaximizable(true);
                setResizable(true);
                add(DimPanel);
                setBounds(20, 20, 400, 340);
                setSelected(true);
            } catch( Exception re ) {
                System.out.println("Library Ошибка при открытии формы визарда нового измерения"+re.toString());
            }
        }
        public DimensionPanel getPanel() throws RemoteException {
            return DimPanel;
        }

    }


    public class MyTreeFocusListner implements  FocusListener {
        public void focusGained(FocusEvent e) {
            //setTypeOfToolbar(1);
        }
        public void focusLost(FocusEvent e) {
        }
    
    }

    public class myInternalFrameEvent implements  InternalFrameListener {
        public void internalFrameClosing(InternalFrameEvent e) {
        }
        public void internalFrameDeactivated(InternalFrameEvent e) {
        }

        public void internalFrameClosed(InternalFrameEvent e) {
            DetailFormAutomat(0);
        }

        public void internalFrameOpened(InternalFrameEvent e) {
            myInternalFrame myFrame = (myInternalFrame)e.getInternalFrame();
            try {
                System.out.println(" internalFrameOpened ");
                CurrentInputPanel=myFrame.getInputPanel();
                DetailPanel.removeAll();
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                DetailPanel.add(CurrentInputPanel.InputField, gridBagConstraints);
                DetailPanel.updateUI();
                CurrentInputPanel.CurrentRecOut();

            } catch( Exception re ) {
                System.out.println("myInternalFrameEvent "+re.toString());
            }
        }

        public void internalFrameIconified(InternalFrameEvent e) {
	    }

        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

        public void internalFrameActivated(InternalFrameEvent e) {
            myInternalFrame myFrame = (myInternalFrame)e.getInternalFrame();
            try {
                CurrentInputPanel=myFrame.getInputPanel();
                DetailPanel.removeAll();
                java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                DetailPanel.add(CurrentInputPanel.InputField, gridBagConstraints);
                DetailPanel.updateUI();
                CurrentInputPanel.CurrentRecOut();
                DetailFormAutomat(8);
            } catch( Exception re ) {
                System.out.println("myInternalFrameEvent "+re.toString());
            }

	    }

    }

    public class myInternalFrameEvent2 implements  InternalFrameListener {
        public void internalFrameClosing(InternalFrameEvent e) {
        }
        public void internalFrameDeactivated(InternalFrameEvent e) {
        }

        public void internalFrameClosed(InternalFrameEvent e) {
        }

        public void internalFrameOpened(InternalFrameEvent e) {
            JInternalFrame myFrame = (JInternalFrame)e.getInternalFrame();
            try {
                System.out.println(" internalFrameOpened ");
            } catch( Exception re ) {
                System.out.println("myInternalFrameEvent "+re.toString());
            }
        }

        public void internalFrameIconified(InternalFrameEvent e) {
	    }

        public void internalFrameDeiconified(InternalFrameEvent e) {
        }

        public void internalFrameActivated(InternalFrameEvent e) {
            DimensionInternalFrame myFrame = (DimensionInternalFrame)e.getInternalFrame();
            try {
                CurrentDimensionPanel=myFrame.getPanel();
                DetailFormAutomat(9);
            } catch( Exception re ) {
                System.out.println("myInternalFrameEvent "+re.toString());
            }

	    }

    }


    
    public void setServer(INIACRMIInterface aServer) {
        Server=aServer;
    }

    public void reinitETP () {
        try {
            if (CurrentInputPanel.selectedRow!=-1) {
                    if (!ETPInited) {
                        System.out.println("Смена рабочей панели");
                        MainEditionPanel.removeAll();
                        java.awt.GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
                        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
                        gridBagConstraints1.weightx = 1.0;
                        gridBagConstraints1.weighty = 1.0;
                        MainEditionPanel.setLayout(new java.awt.GridBagLayout());
                        MainEditionPanel.add(ETP,gridBagConstraints1);
                        MainEditionPanel.updateUI();

                        ETPInited=true;
                    }
                    ETP.setQuestion(CurrentInputPanel.Dictionary,CurrentInputPanel.RQI);
                    ETP.setSelectionRows(CurrentInputPanel.h);
                    
            }
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());
        }
    }

    public javax.swing.tree.DefaultMutableTreeNode findBranch(javax.swing.tree.DefaultMutableTreeNode top, Integer DMIID1, boolean mode) {
        javax.swing.tree.DefaultMutableTreeNode Res=null;
        try {
            DQAMarkInterface DQA=(DQAMarkInterface)top.getUserObject();
            if (DQA.getType()<3) {
                Integer DMIID2;
                if (DQA.getRContent()!=null) DMIID2= DQA.getRContent().getID();
                else DMIID2=0;
                if (DMIID2.compareTo(DMIID1)==0) {
                    if (mode) Res=(DefaultMutableTreeNode)top.getParent(); else Res=(DefaultMutableTreeNode)top;
                } else {
                    int j=0;
                    boolean Flag=true;
                    while (j<top.getChildCount()&Flag) {
                        DefaultMutableTreeNode node=(DefaultMutableTreeNode)top.getChildAt(j);
                        Res=findBranch(node,DMIID1,mode);
                        Flag=(Res==null);
                        j++;
                    }
                }
            } else Res=null;
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());
        }
        return Res;
    }

    public void refreshBranch(MutableTreeNode TM, int operation) {
        try {
            ABSortTreeNode TM2=(ABSortTreeNode) TM;
            DQAMarkInterface DQABranch1=(DQAMarkInterface)TM2.getUserObject();
            //type = DQABranch1.getType();
            switch (operation) {
                    case 1: { //удаление
                       int i=0;
                       int j=0;
                       javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                       while (i<TM2.getChildCount()) {
                           javax.swing.tree.DefaultMutableTreeNode DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(i);
                           DQAMarkInterface DQABranch2=(DQAMarkInterface)DMT.getUserObject();
                           j=0;
                           boolean Flag=true;
                           while (j<DQABranch1.getContent().getSize()&Flag) {
                               Flag=(DQABranch2.getContent().getID().compareTo(DQABranch1.getContent().getByPos(j).getID())!=0);
                               j++;
                           }
                           if (Flag) {
                               DTM.removeNodeFromParent(DMT);
                           } else {
                               j--;
                               if (i!=j) {
                                    RInterface RI=DQABranch1.getContent().getByPos(j);
                                    DQABranch1.getContent().getByPos(i).setPos(j);
                                    RI.setPos(i);
                               }
                               i++;
                           }
                       }
                       //TM2.nodeStructureChanged();
                       jTree1.updateUI();
                       break;
                    }
                    case 2: { //добавление
                       int i=0;
                       int j=0;
                       javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                       while (i<DQABranch1.getContent().getSize()){
                           j=0;
                           boolean Flag=true;
                           javax.swing.tree.DefaultMutableTreeNode DMT=null;
                           RInterface RDMI=DQABranch1.getContent().getByPos(i);
                           while ((j<TM2.getChildCount())&Flag) {
                                DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(j);
                                DQAMarkInterface DQABranch2=(DQAMarkInterface)DMT.getUserObject();
                                Flag=(DQABranch2.getContent().getID().compareTo(DQABranch1.getContent().getByPos(i).getID())!=0);
                                j++;
                           }
                           i++;
                           if (Flag) {
                               DQAMarkInterface DMM=DQABranch1.makeNewChildMark(RDMI.getID(),RDMI,Server);
                               //DefaultMutableTreeNode root=new DefaultMutableTreeNode(DMM);
                               //DefaultMutableTreeNode root=getBranch(DMM.getRContent(),(DataModuleMark)DQABranch1);
                               //DTM.insertNodeInto(root, TM2, i-1);
                               //DefaultMutableTreeNode DMTN =findBranch((DefaultMutableTreeNode)jTree1.getModel().getRoot(),RDMI.getID(),false);
                               //DefaultMutableTreeNode DMTN =findBranch((DefaultMutableTreeNode)jTree1.getModel().getRoot(),DMM.getRContent().getID(),false);
                               ABSortTreeNode DMTN =getBranch(DMM.getRContent(),(DataModuleMark)TM2.getUserObject());
                               DTM.insertNodeInto(DMTN, TM2, i-1);
                               DMTN.nodeChanged();

                           }
                       }
                       //TM2.nodeStructureChanged();
                       jTree1.updateUI();
                       break;
                    }
            }
        } catch(Exception re ) {
            System.out.println("Фигня какая то refreshBranch "+re.toString());
        }


    }


    public JPanel setPanelContent(JPanel child, JPanel parent, String title) {
        parent.removeAll();
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        parent.setLayout(new java.awt.GridBagLayout());
        parent.add(child, gridBagConstraints);
        if (!title.isEmpty()) child.setBorder(javax.swing.BorderFactory.createTitledBorder("Детализация данных"));
        else child.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        parent.updateUI();
        return parent;
    }
    
    public Integer getID() {
        Integer Res=0;
        try {
            Res=Refresher.getID();
        } catch( Exception re ) {
            System.out.println("Ошибка в getID()"+re.toString());
        }
        return Res;
    }
    

    
    /*public JPanel DetailDataModule() {
        return setPanelContent(DataModuleConstr,DetailPanel2,"Детализация данных");
    }*/
    
    public void DetailFormAutomat(int Stimul) {
        //LastState=DetailFormAutomatState;
        switch(DetailFormAutomatState) {
             case 0: {
                 //начальное состояние. Есть только закладка Библиотека
                 switch(Stimul) {
                     
                    case 0: {
                        //добавляем окно Детали в верхнее окно
                        //в дереве рабочей папки выбран узел - модуль данных
                        jTabbedPane1.addTab("Детали",setPanelContent(DataModuleConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 1: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        jTabbedPane1.addTab("Детали", setPanelContent(DataModuleConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 10: {
                        //Выбрана папка
                        jTabbedPane1.addTab("Детали", setPanelContent(FolderConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        //в дереве рабочей папки выбран узел - модуль данных
                        //setPanelContent(FolderConstr,LowPanel,"");
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }                    
                    case 2: {
                        //в дереве рабочей папки выбран узел - словарь
                        jTabbedPane1.addTab("Детали", setPanelContent(DictionaryConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 3: {
                        //в дереве рабочей папки выбран узел - вопрос
                        jTabbedPane1.addTab("Детали", setPanelContent(QuestConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 4: {
                        //в дереве рабочей папки выбран узел - ответ
                        jTabbedPane1.addTab("Детали", setPanelContent(AnswerConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }

                    case 6: {
                        //в дереве рабочей папки выбран узел - фильтр блок данных
                        jTabbedPane1.addTab("Детали", setPanelContent(DEP,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        //непонятно ... или ссылка 
                        DetailFormAutomatState=1;
                        setToolbarMode(3);
                        break;
                    }
                     case 8: {
                        //Выбрано окно ввода
                        jTabbedPane2.addTab("Детали", setPanelContent(ETP,DetailPanel2,"Детализация данных"));
                        jTabbedPane2.setSelectedComponent(DetailPanel2);
                        try {
                            ETP.setQuestion(CurrentInputPanel.Dictionary,CurrentInputPanel.RQI);
                            ETP.setSelectionRows(CurrentInputPanel.h);

                            if (!DMEP.getDMI().equals(CurrentInputPanel.getParentDMI())){
                                DMEP.initTree(CurrentInputPanel.getParentRDMI());
                            }
                            setPanelContent(DMEP,LowPanel,"");
                            
                        } catch( Exception re ) {
                            System.out.println("Ошибка в конечном автомате "+re.toString());
                        }
                        DetailFormAutomatState=3;
                        setToolbarMode(2);
                        break;
                    }
                    case 9: {
                        //Выбрано окно распределений
                        try {
                            DEP.setDictionary(CurrentDimensionPanel.Dictionary);
                            jTabbedPane2.addTab("Детали", setPanelContent(DEP,DetailPanel2,"Детализация данных"));
                            jTabbedPane2.setSelectedComponent(DetailPanel2);
                            if (!DMEP.getDMI().equals(CurrentDimensionPanel.getParentDMI())){
                                DMEP.initTree(CurrentDimensionPanel.getParentRDMI());
                            }
                            setPanelContent(DMEP,LowPanel,"Детализация данных");
                        } catch( Exception re ) {
                            System.out.println("Ошибка в конечном автомате "+re.toString());
                        }
                        DetailFormAutomatState=3;
                        setToolbarMode(1);
                        System.out.println("setToolbarMode(1)");
                        break;
                    }
                    case 13: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        setPanelContent(DMEP,LowPanel,"");
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                 }
                 break;
             }
             case 1: {
                 //Открыта закладка Детали в детализации данных
                 //активно окно свойств словаря
                 switch(Stimul) {
                     case 1: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        //if (DetailFormAutomatState==3) {jTabbedPane2.remove(DetailPanel2);}                         
                        setPanelContent(DataModuleConstr,DetailPanel2,"Детализация данных");
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 2: {
                        //в дереве рабочей папки выбран узел - словарь
                        jTabbedPane1.addTab("Детали", setPanelContent(DictionaryConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 3: {
                        //в дереве рабочей папки выбран узел - вопрос
                        jTabbedPane1.addTab("Детали", setPanelContent(QuestConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 4: {
                        //в дереве рабочей папки выбран узел - ответ
                        setPanelContent(AnswerConstr,DetailPanel2,"Детализация данных");
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    //xxx
                    case 5: {
                        //в дереве рабочей папки выбран узел - блок данных
                        jTabbedPane1.remove(DetailPanel2);
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                        break;
                    }
                    case 6: {
                        //в дереве рабочей папки выбран узел - фильтр блок данных
                        jTabbedPane1.addTab("Детали", setPanelContent(DEP,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(3);
                        break;
                    }
                    case 7: {
                        //в дереве рабочей папки выбран узел - сумматор блоков данных
                        jTabbedPane1.remove(DetailPanel2);
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                        break;
                    }
                    case 8: {
                        //Выбрано окно ввода
                        jTabbedPane2.addTab("Детали", setPanelContent(ETP,DetailPanel2,"Детализация данных"));
                        jTabbedPane2.setSelectedComponent(DetailPanel2);
                        jTabbedPane1.remove(DetailPanel2);
                        try {
                            ETP.setQuestion(CurrentInputPanel.Dictionary,CurrentInputPanel.RQI);
                            ETP.setSelectionRows(CurrentInputPanel.h);
                            if (!DMEP.getDMI().equals(CurrentInputPanel.getParentDMI())){
                                DMEP.initTree(CurrentInputPanel.getParentRDMI());
                            }
                            setPanelContent(DMEP,LowPanel,"");
                            
                        } catch( Exception re ) {
                            System.out.println("Ошибка в конечном автомате "+re.toString());
                        }
                        DetailFormAutomatState=3;
                        setToolbarMode(2);
                        break;

                        
                    }
                    case 9: {
                        //Выбрано окно распределений
                        try {
                            DEP.setDictionary(CurrentDimensionPanel.Dictionary);
                            jTabbedPane2.addTab("Детали", setPanelContent(DEP,DetailPanel2,"Детализация данных"));
                            jTabbedPane2.setSelectedComponent(DetailPanel2);
                            jTabbedPane1.remove(DetailPanel2);
                            if (!DMEP.getDMI().equals(CurrentDimensionPanel.getParentDMI())){
                                DMEP.initTree(CurrentDimensionPanel.getParentRDMI());
                            }
                            setPanelContent(DMEP,LowPanel,"Детализация данных");
                        } catch( Exception re ) {
                            System.out.println("Ошибка в конечном автомате "+re.toString());
                        }
                        DetailFormAutomatState=3;
                        setToolbarMode(1);
                        System.out.println("setToolbarMode(1)");
                        break;
                    }
                    /*case 10: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        setPanelContent(FolderConstr,DetailPanel2,"Детализация данных");
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1;
                        setToolbarMode(1);                        
                    }
                    case 11: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        //jTabbedPane2.remove(DetailPanel2);
                        setPanelContent(DataModuleConstr,DetailPanel2,"");
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        //setPanelContent(DataModuleConstr,DetailPanel2,"");
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                        break;
                    }*/
                    case 13: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        //jTabbedPane2.remove(DetailPanel2);
                        setPanelContent(DMEP,LowPanel,"");
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                        break;
                    }

                 }
                 break;
             }
             case 3: {
                 //Открыты детали в библиотеке
                 //активно окно свойств словаря
                 switch(Stimul) {
                     case 1: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        jTabbedPane2.remove(DetailPanel2);                         
                        jTabbedPane1.addTab("Детали", setPanelContent(DataModuleConstr,DetailPanel2,"Детализация данных"));
                        jTabbedPane1.setSelectedComponent(DetailPanel2);
                        DetailFormAutomatState=1; 
                        setToolbarMode(1);
                        break;
                    }
                    case 2: {
                        //в дереве рабочей папки выбран узел - словарь
                        jTabbedPane2.remove(DetailPanel2);                         
                        jTabbedPane1.addTab("Детали", setPanelContent(DictionaryConstr,DetailPanel2,"Детализация данных"));                        
                        jTabbedPane1.setSelectedComponent(DetailPanel2);                        
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 3: {
                        //в дереве рабочей папки выбран узел - вопрос
                        jTabbedPane2.remove(DetailPanel2);                         
                        jTabbedPane1.addTab("Детали", setPanelContent(QuestConstr,DetailPanel2,"Детализация данных"));                        
                        jTabbedPane1.setSelectedComponent(DetailPanel2);                           
                        DetailFormAutomatState=1;
                        setToolbarMode(1);
                        break;
                    }
                    case 4: {
                        //в дереве рабочей папки выбран узел - ответ
                        jTabbedPane2.remove(DetailPanel2);                         
                        jTabbedPane1.addTab("Детали", setPanelContent(AnswerConstr,DetailPanel2,"Детализация данных"));                        
                        jTabbedPane1.setSelectedComponent(DetailPanel2);                           
                        DetailFormAutomatState=1;                        
                        setToolbarMode(1);
                        break;
                    }
                    case 5: {
                        //в дереве рабочей папки выбран узел - блок данных
                        jTabbedPane2.remove(DetailPanel2);
                        DetailFormAutomatState=0;
                        break;
                    }
                    case 6: {
                        //в дереве рабочей папки выбран узел - фильтр блок данных
                        setPanelContent(DEP,DetailPanel2,"Детализация данных");
                        DetailFormAutomatState=3;
                        setToolbarMode(3);
                        break;
                    }
                    case 7: {
                        //в дереве рабочей папки выбран узел - сумматор блоков данных
                        jTabbedPane2.remove(DetailPanel2);
                        DetailFormAutomatState=0;
                        break;
                    }
                    case 8: {
                        
                        try {
                            setPanelContent(ETP,DetailPanel2,"Детализация данных");
                            ETP.setQuestion(CurrentInputPanel.Dictionary,CurrentInputPanel.RQI);
                            ETP.setSelectionRows(CurrentInputPanel.h);

                            if (!DMEP.getDMI().equals(CurrentInputPanel.getParentDMI())){
                                DMEP.initTree(CurrentInputPanel.getParentRDMI());
                            }
                            setPanelContent(DMEP,LowPanel,"");

                        } catch( Exception re ) {
                            System.out.println("Ошибка в конечном автомате "+re.toString());
                        }
                        DetailFormAutomatState=3;
                        setToolbarMode(2);
                        break;
                    }
                    case 9: {
                        //Выбрано окно распределений
                        try {
                            DEP.setDictionary(CurrentDimensionPanel.Dictionary);
                            setPanelContent(DEP,DetailPanel2,"Детализация данных");
                            jTabbedPane2.setSelectedComponent(DetailPanel2);
                            if (!DMEP.getDMI().equals(CurrentDimensionPanel.getParentRDMI())){
                                DMEP.initTree(CurrentDimensionPanel.getParentRDMI());
                            }
                            setPanelContent(DMEP,LowPanel,"");
                        } catch( Exception re ) {
                            System.out.println("Ошибка в конечном автомате "+re.toString());
                        }
                        DetailFormAutomatState=3;
                        setToolbarMode(1);
                        break;
                    }
                    /*case 10: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        jTabbedPane2.remove(DetailPanel2);
                        setPanelContent(FolderConstr,LowPanel,"Детализация данных");
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                    }
                    case 11: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        jTabbedPane2.remove(DetailPanel2);
                        setPanelContent(DataModuleConstr,LowPanel,"Детализация данных");
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                        break;
                    }*/
                    case 13: {
                        //в дереве рабочей папки выбран узел - модуль данных
                        jTabbedPane2.remove(DetailPanel2);
                        setPanelContent(DMEP,LowPanel,"Детализация данных");
                        DetailFormAutomatState=0;
                        setToolbarMode(1);
                        break;
                    }

                 }
                 break;
             }
        }
        
    }


    public void CurrentRecOut(int i, int j)  {
        jTextField2.setText(i+" из "+j);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel4 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        MainEditionPanel = new javax.swing.JPanel();
        LEP = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel2 = new javax.swing.JPanel();
        LowPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        DetailPanel = new javax.swing.JPanel();
        DetailField = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jFilterAndButton1 = new javax.swing.JButton();
        jFilterOrButton1 = new javax.swing.JButton();
        jFilterNotButton1 = new javax.swing.JButton();
        jFilterOpenButton1 = new javax.swing.JButton();
        jFilterCloseButton1 = new javax.swing.JButton();
        jFilterFormulaTextField1 = new javax.swing.JTextField();
        jFilterAcceptButton = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        FirstRecordButton = new javax.swing.JButton();
        PrevRecordButton = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        NextRecordButton = new javax.swing.JButton();
        LastRecordButton = new javax.swing.JButton();
        NewRecordButton = new javax.swing.JButton();
        DelRecordButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();

        setMinimumSize(new java.awt.Dimension(640, 480));
        setLayout(new java.awt.GridBagLayout());

        jPanel4.setPreferredSize(new java.awt.Dimension(1100, 700));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jSplitPane3.setDividerSize(3);

        jPanel1.setPreferredSize(new java.awt.Dimension(370, 300));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jSplitPane1.setDividerSize(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        MainEditionPanel.setPreferredSize(new java.awt.Dimension(400, 300));
        MainEditionPanel.setLayout(new java.awt.GridBagLayout());

        LEP.setBorder(javax.swing.BorderFactory.createTitledBorder("Библиотека"));
        LEP.setPreferredSize(new java.awt.Dimension(200, 300));
        LEP.setVerifyInputWhenFocusTarget(false);
        LEP.setLayout(new java.awt.GridBagLayout());

        jTree1.setEditable(true);
        jScrollPane1.setViewportView(jTree1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        LEP.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        MainEditionPanel.add(LEP, gridBagConstraints);
        LEP.getAccessibleContext().setAccessibleName("Результаты исследований");

        jTabbedPane2.addTab("Библиотека", MainEditionPanel);
        MainEditionPanel.getAccessibleContext().setAccessibleParent(jSplitPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jTabbedPane2, gridBagConstraints);

        jSplitPane1.setTopComponent(jPanel6);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        LowPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Рабочий стол"));
        LowPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(LowPanel, gridBagConstraints);

        jSplitPane1.setBottomComponent(jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jSplitPane1, gridBagConstraints);

        jSplitPane3.setLeftComponent(jPanel1);

        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel3, gridBagConstraints);

        DetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Значения"));
        DetailPanel.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        DetailPanel.add(DetailField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(DetailPanel, gridBagConstraints);

        jTabbedPane1.addTab("Лаборатория", jPanel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel8.add(jTabbedPane1, gridBagConstraints);

        jSplitPane3.setRightComponent(jPanel8);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jSplitPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel4, gridBagConstraints);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setMinimumSize(new java.awt.Dimension(211, 25));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jToolBar3.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/save_0.gif"))); // NOI18N
        jButton1.setToolTipText("Импорт");
        jButton1.setBorder(null);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/save_2.gif"))); // NOI18N
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/save_1.gif"))); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/load_0.gif"))); // NOI18N
        jButton2.setToolTipText("Экспорт");
        jButton2.setBorder(null);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/load_2.gif"))); // NOI18N
        jButton2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/load_1.gif"))); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/print_0.gif"))); // NOI18N
        jButton3.setToolTipText("Печать");
        jButton3.setBorder(null);
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/copy_0.gif"))); // NOI18N
        jButton4.setToolTipText("copy");
        jButton4.setBorder(null);
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/cut_0.gif"))); // NOI18N
        jButton5.setToolTipText("cut");
        jButton5.setBorder(null);
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/paste_0.gif"))); // NOI18N
        jButton6.setToolTipText("paste");
        jButton6.setBorder(null);
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton6);

        jPanel9.add(jToolBar3, new java.awt.GridBagConstraints());

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jFilterAndButton1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jFilterAndButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/and_0.gif"))); // NOI18N
        jFilterAndButton1.setBorder(null);
        jFilterAndButton1.setFocusable(false);
        jFilterAndButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jFilterAndButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/and_2.gif"))); // NOI18N
        jFilterAndButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/and_1.gif"))); // NOI18N
        jFilterAndButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jFilterAndButton1);

        jFilterOrButton1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jFilterOrButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/or_0.gif"))); // NOI18N
        jFilterOrButton1.setBorder(null);
        jFilterOrButton1.setFocusable(false);
        jFilterOrButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jFilterOrButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/or_2.gif"))); // NOI18N
        jFilterOrButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/or_1.gif"))); // NOI18N
        jFilterOrButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jFilterOrButton1);

        jFilterNotButton1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jFilterNotButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/not_0.gif"))); // NOI18N
        jFilterNotButton1.setBorder(null);
        jFilterNotButton1.setFocusable(false);
        jFilterNotButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jFilterNotButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/not_2.gif"))); // NOI18N
        jFilterNotButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/not_1.gif"))); // NOI18N
        jFilterNotButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jFilterNotButton1);

        jFilterOpenButton1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jFilterOpenButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/lskob_0.gif"))); // NOI18N
        jFilterOpenButton1.setBorder(null);
        jFilterOpenButton1.setFocusable(false);
        jFilterOpenButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jFilterOpenButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/lskob_2.gif"))); // NOI18N
        jFilterOpenButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/lskob_1.gif"))); // NOI18N
        jFilterOpenButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jFilterOpenButton1);

        jFilterCloseButton1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jFilterCloseButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/rskob_0.gif"))); // NOI18N
        jFilterCloseButton1.setBorder(null);
        jFilterCloseButton1.setFocusable(false);
        jFilterCloseButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jFilterCloseButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/rskob_2.gif"))); // NOI18N
        jFilterCloseButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/rskob_1.gif"))); // NOI18N
        jFilterCloseButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jFilterCloseButton1);
        jToolBar1.add(jFilterFormulaTextField1);

        jFilterAcceptButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jFilterAcceptButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/rskob_0.gif"))); // NOI18N
        jFilterAcceptButton.setBorder(null);
        jFilterAcceptButton.setFocusable(false);
        jFilterAcceptButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jFilterAcceptButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/rskob_2.gif"))); // NOI18N
        jFilterAcceptButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/rskob_1.gif"))); // NOI18N
        jFilterAcceptButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jFilterAcceptButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jToolBar1, gridBagConstraints);

        jToolBar2.setRollover(true);
        jToolBar2.setEnabled(false);

        FirstRecordButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        FirstRecordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/first_0.gif"))); // NOI18N
        FirstRecordButton.setToolTipText("Первая запись");
        FirstRecordButton.setBorder(null);
        FirstRecordButton.setFocusable(false);
        FirstRecordButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        FirstRecordButton.setName("FistRecordButton"); // NOI18N
        FirstRecordButton.setPreferredSize(new java.awt.Dimension(35, 21));
        FirstRecordButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/first_2.gif"))); // NOI18N
        FirstRecordButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/first_1.gif"))); // NOI18N
        FirstRecordButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(FirstRecordButton);

        PrevRecordButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        PrevRecordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/prev_0.gif"))); // NOI18N
        PrevRecordButton.setToolTipText("Предыдущая запись");
        PrevRecordButton.setBorder(null);
        PrevRecordButton.setFocusable(false);
        PrevRecordButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        PrevRecordButton.setPreferredSize(new java.awt.Dimension(35, 21));
        PrevRecordButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/prev_2.gif"))); // NOI18N
        PrevRecordButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/prev_1.gif"))); // NOI18N
        PrevRecordButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(PrevRecordButton);

        jTextField2.setText("0");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jTextField2);

        NextRecordButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        NextRecordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/next_0.gif"))); // NOI18N
        NextRecordButton.setToolTipText("Следующая запись");
        NextRecordButton.setBorder(null);
        NextRecordButton.setFocusable(false);
        NextRecordButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NextRecordButton.setPreferredSize(new java.awt.Dimension(35, 21));
        NextRecordButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/next_2.gif"))); // NOI18N
        NextRecordButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/next_1.gif"))); // NOI18N
        NextRecordButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(NextRecordButton);

        LastRecordButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        LastRecordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/last_0.gif"))); // NOI18N
        LastRecordButton.setToolTipText("Последняя запись");
        LastRecordButton.setBorder(null);
        LastRecordButton.setFocusable(false);
        LastRecordButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LastRecordButton.setPreferredSize(new java.awt.Dimension(35, 21));
        LastRecordButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/last_2.gif"))); // NOI18N
        LastRecordButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/last_1.gif"))); // NOI18N
        LastRecordButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(LastRecordButton);

        NewRecordButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        NewRecordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/add_0.gif"))); // NOI18N
        NewRecordButton.setToolTipText("Новая анкета");
        NewRecordButton.setBorder(null);
        NewRecordButton.setFocusable(false);
        NewRecordButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        NewRecordButton.setPreferredSize(new java.awt.Dimension(35, 21));
        NewRecordButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/add_2.gif"))); // NOI18N
        NewRecordButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/add_1.gif"))); // NOI18N
        NewRecordButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(NewRecordButton);

        DelRecordButton.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        DelRecordButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/del_0.gif"))); // NOI18N
        DelRecordButton.setToolTipText("Удалить анкету");
        DelRecordButton.setBorder(null);
        DelRecordButton.setFocusable(false);
        DelRecordButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DelRecordButton.setPreferredSize(new java.awt.Dimension(35, 21));
        DelRecordButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/del_2.gif"))); // NOI18N
        DelRecordButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gif/button/del_1.gif"))); // NOI18N
        DelRecordButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(DelRecordButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jToolBar2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(jPanel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel9, gridBagConstraints);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel1.setText("Нажмите F2 для редактирования компонента");
        jPanel7.add(jLabel1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel7, gridBagConstraints);

        jMenu2.setText("File");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Сохранить");
        jMenuItem1.setEnabled(false);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Считать");
        jMenuItem2.setEnabled(false);
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem14.setText("Save Data");
        jMenuItem14.setEnabled(false);
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem15.setText("Restore");
        jMenuItem15.setEnabled(false);
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jMenuItem17.setText("Загрузить ЛВ");
        jMenuItem17.setEnabled(false);
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem17);

        jMenuItem3.setText("Сохранить модуль данных в базу");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Загрузить из СУБД");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu5.setText("Окна");
        jMenuBar1.add(jMenu5);

        add(jMenuBar1);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        try {
            System.out.println("Считываю новый словарь ");
            BlockLoader2 BL=new BlockLoader2(Server);
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }    
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        try {
            Server.ReadDictionaries();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        try {
            //Server.WriteDictionaries();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        } 
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    
    
    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
        try {
            CurrentInputPanel.SetCurrentRow(new Integer(jTextField2.getText()).intValue());
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }  
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        OpenImportForm();
        try {
            OpenImportForm();
        } catch( Exception re ) {
            System.out.println("Ошибка импорта "+re.toString());	   
        }     
            
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        //TODO add your handling code here:
        //Вроде как сохранение чего то ..
        try {
            //Server.WriteDictionaries();
            System.out.println("Сохраняю новый словарь ");	   
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }     
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    /*public void addDataBlock (DataModuleInterface DMI) {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
            DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(new DataModuleMark(DMI,(DataModuleMark)nodeInfo));
            javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
            DTM.insertNodeInto(newnode, node, node.getChildCount());
            Server.WriteDictionaries();
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }                 
    }*/
    
    private void CreateDataBlock (int type) {
        try {
            CreateDataBlock (type, jTree1);
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }     
    }

    /*public void RecodeNode() {
        try {
            refreshBranch(node,3,1);
            System.out.println("ддддддддддд");
            DQAMarkInterface DQI = (DQAMarkInterface)node.getUserObject();
            DQI.setMixed(false);
            JMenuItem JP=(JMenuItem)jTreeMenuItem.getComponent(3);
            JP.setEnabled(false);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };*/

    public void CreateDataBlock (int type, JTree jTree){
        try {
            String s="";
            DataModuleMark Data = (DataModuleMark) nodeInfo;            
            switch(type) {
                case 1: {
                    s="New folder";
                    break;
                }
                case 2: {
                    s="New datamodule";
                    break;
                }
                case 3: {
                    s="New datablock";
                    break;
                }
                case 4: {
                    s="New filter";
                    break;
                }
            }
            ABSortTreeNode node1 = (ABSortTreeNode) jTree.getLastSelectedPathComponent();
            Integer Key=Server.getKey();
            DataModuleInterface DMI1 = Server.newDMI(s,Key,type,true);
            DMI1.setDictionary(Data.getDMI().getDictionary(),this.getID(),true);            
            Data.getDMI().addElement(DMI1.getID(),this.getID(),true);
            DMI1.createTable();
            DMI1.setParent(Data.getDMI().getID(),this.getID(),true);
            refreshBranch(node1,2);
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());
        }
    }
    
    private void RenameDataBlock () {
        try {
            CustomDialog customDialog = new CustomDialog(frame,DMI.getName(),"Имя блока данных");
            customDialog.pack();
            customDialog.setLocationRelativeTo(this);
            customDialog.setVisible(true);
            String s = customDialog.getValidatedText();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
            Object nodeInfo = node.getUserObject();
            DataModuleMark Data = (DataModuleMark) nodeInfo;
            Data.getDMI().setName(s,this.getID(),true);
            //Server.WriteDictionaries();
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }     
    }
    
    private void  CheckBranch(DataModuleInterface t,DataModuleInterface root){
        try {
            int i=0;
            root.delByID(t.getID(),this.getID(),true);
            /*while (i<root.getModuleSize()) {
                DataModuleInterface DMI = root.getModule(i);
                if (DMI.equals(t)) {root.delModule(i);} else {CheckBranch(t,DMI);};
                i++;
            };*/
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }
  
    public void DeleteDataBlock (JTree jTree) {
        try {
            if (jTree==null) jTree=jTree1;
            ABSortTreeNode node = (ABSortTreeNode) jTree.getLastSelectedPathComponent();
            ABSortTreeNode parentNode = (ABSortTreeNode) node.getParent();
            DataModuleMark Data   = (DataModuleMark)node.getUserObject();
            DataModuleMark Parent = (DataModuleMark)parentNode.getUserObject();
            CheckBranch(Data.getDMI(),Parent.getDMI());
            refreshBranch(parentNode,1);
        } catch( Exception re ) {
            System.out.println("Ошибка "+re.toString());	   
        }     
    }

    public void OpenEditForm(RInterface RDMI, RInterface Parent) {
        System.out.println("Ввод данных типа начинается");
        try {
            DataModuleInterface MyDMI=Server.getDMI(RDMI.getID());
            InputPanel inputPanel=new InputPanel(RDMI, Parent, this);
            java.awt.GridBagConstraints gridBagConstraints;
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            myInternalFrameEvent IFE=new myInternalFrameEvent();
            myInternalFrame jif = new myInternalFrame(inputPanel);
            jif.addInternalFrameListener(IFE);
            if (MyDMI.getType()==3) {jif.setTitle("Таблица '"+MyDMI.getName()+"'");}
            else {jif.setTitle("Фильтр '"+MyDMI.getName()+"'");}
            
            jif.show();
            desktop.add(jif);
            jif.updateUI();
            try {
                jif.setSelected(true);
            } catch (Exception e2) {
                System.err.println("Ошибка инициализации окана 2"+e2.toString());
            }
            jif.show();
            jif.updateUI();
            
            
            
        } catch( Exception re ) {
            System.err.println("Ошибка инициализации окана 2"+re.toString());
        }     
    }

    public void OpenExportForm(DataModuleInterface DMI) {
        System.out.println("Экспорт данных типа начинается");
        try {
            ExportDialog frame = new ExportDialog (this,true,DMI);
            frame.setLocationRelativeTo(this);
            frame.setTitle("Экспорт данных");
            frame.setVisible(true);
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
    }
    
    private void OpenImportForm() {
        System.out.println("Импорт данных типа начинается");
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
            Object nodeInfo = node.getUserObject();
            DataModuleMark Data = (DataModuleMark) nodeInfo;
            try {
                System.out.println("Считываю новый словарь ");
                BlockLoader BL=new BlockLoader(Data.getDMI(),this);
            } catch( Exception re ) {
                System.out.println("Ошибка импорта"+re.toString());	   
            }     
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
    }
    
    public void OpenModuleImportForm(DataModuleInterface DMI, int type) {
        System.out.println("Импорт данных типа начинается "+type);
        try {
            ImportDialog frame = new ImportDialog (this,DMEP,type,DMI);
            frame.setLocationRelativeTo(this);
            frame.setTitle("Импорт данных");
            frame.setVisible(true);
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
    }
    
    public void OpenDimensionForm(RInterface RDMI, RInterface ParentRDMI) {
        System.out.println("Построение измерений");
        try {
            DataModuleInterface MyDMI=Server.getDMI(RDMI.getID());
            DimensionPanel DimensionPanel=new DimensionPanel(RDMI,ParentRDMI,this);
            java.awt.GridBagConstraints gridBagConstraints;
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;

            myInternalFrameEvent2  IFE  =   new myInternalFrameEvent2();
            DimensionInternalFrame jif  =   new DimensionInternalFrame(DimensionPanel);

            jif.addInternalFrameListener(IFE);

            jif.setClosable(true);
            jif.setMaximizable(true);
            jif.setResizable(true);
            jif.add(DimensionPanel);

            jif.setBounds(20, 20, 200, 200);
            if (MyDMI.getType()==3) {jif.setTitle("Распределение таблицы '"+MyDMI.getName()+"'");}
            else {jif.setTitle("Распределение фильтра '"+MyDMI.getName()+"'");}
            jif.show();
            desktop.add(jif);

            jif.updateUI();
            try {
                jif.setSelected(true);
            } catch (Exception e2) {
                System.err.println("Ошибка инициализации окана 2"+e2.toString());
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
    }
    
    private void makeAnswerBranches(MutableTreeNode top,RInterface RQI) {
        try {
            QuestionInterface QI=Server.getMainDictionary().getQuestion(RQI.getID());
            AnswerMark AM;
            for (int i = 0; i < QI.getSize(); i++) {
                RInterface RAI=QI.getByPos(new Integer(i));
                AM=new AnswerMark(RAI,Server.getMainQuestion().getAnswer(RAI.getID()),RQI);
                ABSortTreeNode root=new ABSortTreeNode(AM);
                ((ABSortTreeNode)top).add(root);
            }
        } catch(Exception re ) {
            System.out.println("Фигня какая то в  makeAnswerBranches "+re.toString());
        }
    }

    private void makeQuestBranches(MutableTreeNode top,DictionaryInterface DI) {
        try {
            QuestMark QM;
            for (int i = 0; i < DI.getSize(); i++) {
                RInterface RQM=DI.getByPos(new Integer(i));
                QM=new QuestMark(RQM,Server.getMainDictionary().getQuestion(RQM.getID()),DI.getID());
                ABSortTreeNode root=new ABSortTreeNode(QM);
                makeAnswerBranches(root,RQM);
                ((ABSortTreeNode)top).add(root);
            }
        } catch(Exception re ) {
            System.out.println("Фигня какая то в makeQuestBranches "+re.toString());
        }
    }

    private ABSortTreeNode getBranch(RInterface RDMI, DataModuleMark Parent){
        ABSortTreeNode jTreeRoot=null;
        ABSortTreeNode jTreeNode=null;
        try {
            DataModuleInterface root;
            if (RDMI!=null) root=Server.getDMI(RDMI.getID());
            else root=Server.getDataModule();
            DataModuleMark DMM= new DataModuleMark(RDMI,root,Parent);
            jTreeRoot = new ABSortTreeNode(DMM);
            jTreeNode=jTreeRoot;
            if (root.getType()==2) {
                ABSortTreeNode top;
                if (root.getDictionary()!=-1) {
                    DictionaryInterface DI = Server.getDictionary(root.getDictionary());
                    top = (ABSortTreeNode) new ABSortTreeNode(new DictionaryMark(DI));
                    makeQuestBranches(top,DI);
                    jTreeRoot.add(top);
                }
            }
            int i=0;
            while (i<root.getSize()) {
                RInterface RI=(RInterface)root.getByPos(i);
                jTreeNode.add(getBranch(RI,DMM));
                i++;
            };
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
        return jTreeRoot;
    }
    
    
   

    /*private DefaultMutableTreeNode getBranch(DataModuleInterface root, DataModuleMark parent){
        DefaultMutableTreeNode jTreeRoot=null;
        try {
            if (root.getType()<3) {
                DataModuleMark DInfo=new DataModuleMark(null,root, parent);
                jTreeRoot = new DefaultMutableTreeNode(DInfo);
                for (int i = 0; i < root.getModuleSize(); i++) {
                    RInterface RDMI=root.getModulePos(new Integer(i));
                    DataModuleInterface nDMI=Server.getDMI(RDMI.getID());
                    DefaultMutableTreeNode newBranch=getBranch(nDMI,DInfo);
                    if (newBranch!=null)jTreeRoot.add(newBranch);
                }
            }
        } catch( Exception re ) {
            System.out.println("ошибка в getBranch() "+re.toString());	   
        }
        return jTreeRoot;
    }*/

    public void setToolbarMode(int i) {
        switch (i) {
            case (1) : { //базовое значение
                jFilterAndButton1.setEnabled(false);
                jFilterCloseButton1.setEnabled(false);
                jFilterFormulaTextField1.setEnabled(false);
                jFilterFormulaTextField1.setText("");
                jFilterNotButton1.setEnabled(false);
                jFilterOpenButton1.setEnabled(false);
                jFilterOrButton1.setEnabled(false);
                jFilterAcceptButton.setEnabled(false);
                NewRecordButton.setEnabled(false);
                LastRecordButton.setEnabled(false);
                NextRecordButton.setEnabled(false);
                DelRecordButton.setEnabled(false);
                PrevRecordButton.setEnabled(false);
                FirstRecordButton.setEnabled(false);
                jTextField2.setEnabled(false);
                jTextField2.setText("");
                break;
            }
            case (2) : { //значение при активном окне ввода
                jFilterAndButton1.setEnabled(false);
                jFilterCloseButton1.setEnabled(false);
                jFilterFormulaTextField1.setEnabled(false);
                jFilterFormulaTextField1.setText("");
                jFilterNotButton1.setEnabled(false);
                jFilterOpenButton1.setEnabled(false);
                jFilterOrButton1.setEnabled(false);
                jFilterAcceptButton.setEnabled(false);
                NewRecordButton.setEnabled(true);
                LastRecordButton.setEnabled(true);
                NextRecordButton.setEnabled(true);
                DelRecordButton.setEnabled(true);
                PrevRecordButton.setEnabled(true);
                FirstRecordButton.setEnabled(true);
                jTextField2.setEnabled(true);
                //jTextField2.setText("");
                break;
            }
            case (3) : { //значение для редактора фильтров
                jFilterAndButton1.setEnabled(true);
                jFilterCloseButton1.setEnabled(true);
                jFilterFormulaTextField1.setEnabled(true);
                jFilterFormulaTextField1.setText("");
                jFilterNotButton1.setEnabled(true);
                jFilterOpenButton1.setEnabled(true);
                jFilterOrButton1.setEnabled(true);
                jFilterAcceptButton.setEnabled(false);
                NewRecordButton.setEnabled(false);
                LastRecordButton.setEnabled(false);
                NextRecordButton.setEnabled(false);
                DelRecordButton.setEnabled(false);
                PrevRecordButton.setEnabled(false);
                FirstRecordButton.setEnabled(false);
                jTextField2.setEnabled(false);
                jTextField2.setText("");
                break;
            }
        }
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Library.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void makeTree() {
        try {

            
            DefaultTreeModel TreeMоdel=new DefaultTreeModel(getBranch(null,null));
            
            //AutomatedTreeNode  TreeNоde,TreeNоde1,TreeNоde2;
            /*TreeNоde1=new AutomatedTreeNode(new String(""));
            AutomatedTreeModel TreeMоdel=new AutomatedTreeModel(TreeNоde1);
            TreeNоde1.add(getBranch(Server.getDMIRootPointer(),null));
            
            TreeNоde=new AutomatedTreeNode(new String("Управление правами пользователей"));
            TreeNоde1.add(TreeNоde);
            TreeNоde1=new AutomatedTreeNode(new String("Роли"));
            //TreeNоde .add(TreeNоde1);
            TreeNоde2 =new AutomatedTreeNode(new String("Оператор архива"));
            TreeNоde1.add(TreeNоde2);
            TreeNоde2 =new AutomatedTreeNode(new String("Администратор опроса"));
            TreeNоde1.add(TreeNоde2);            
            TreeNоde2 =new AutomatedTreeNode(new String("Супервизор"));
            TreeNоde1.add(TreeNоde2); 
            
            TreeNоde1=new AutomatedTreeNode(new String("Пользователи"));    
            TreeNоde .add(TreeNоde1);
            TreeNоde2=new AutomatedTreeNode(new String("Все"));
            TreeNоde1.add(TreeNоde2);
            TreeNоde2=new AutomatedTreeNode(new String("Никто"));
            TreeNоde1.add(TreeNоde2);            
            TreeNоde2=new AutomatedTreeNode(new String("Вася"));
            TreeNоde1.add(TreeNоde2); 
            jTree1.setModel(TreeMоdel);
            
            TreeNоde1=new AutomatedTreeNode(new String("Группы"));    
            TreeNоde .add(TreeNоde1);
            TreeNоde2=new AutomatedTreeNode(new String("Иркутск"));
            TreeNоde1.add(TreeNоde2);
            TreeNоde2=new AutomatedTreeNode(new String("Красноярск"));
            TreeNоde1.add(TreeNоde2);            
            TreeNоde2=new AutomatedTreeNode(new String("Новосибирск"));
            TreeNоde1.add(TreeNоde2); 
            jTree1.setModel(TreeMоdel);*/

            jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            jTree1.setModel(TreeMоdel);
            jTree1.setDragEnabled(true);
            TreeTransferHandler TTH=new TreeTransferHandler();
            TTH.Lib=this;
            jTree1.setTransferHandler(TTH);


            //MyEditTreeCellRenderer renderer = new MyEditTreeCellRenderer();
            DQATreeCellRenderer renderer = new DQATreeCellRenderer();
            jTree1.setCellRenderer(renderer);



            DQATreeCellEditor TCE = new DQATreeCellEditor(jTree1,this);
            jTree1.setCellEditor(TCE);

            MouseListener ml = new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    DefaultMutableTreeNode node2;
                    DQAMarkInterface nodeInfo2;
                    int selRow = jTree1.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = jTree1.getPathForLocation(e.getX(), e.getY());
                    if(selRow != -1) {
                        if(e.getClickCount() == 2) {
                            node2 = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                            if (node2 == null) return;
                            try {
                                nodeInfo2=(DQAMarkInterface)node2.getUserObject();
                                int i=nodeInfo2.getType();
                                switch (i) {
                                    case (1) : { //datafolder
                                        DetailFormAutomat(10);
                                        break;
                                    }  case 2: { //datamodule
                                        DataModuleMark DIfo=(DataModuleMark)nodeInfo2;
                                        DMEP.initTree(DIfo.getRContent());
                                        System.out.println("Двойной клик на опросе "+nodeInfo2.getName());
                                        DetailFormAutomat(13);
                                        break;
                                    } case 3: { //datablock
                                        break;
                                    } case 4: { //datafilter
                                    setToolbarMode(3);
                                        break;
                                    } case 5: { //datafil
                                        break;
                                    }
                                    case 0: {
                                        break;
                                    }
                                    case 7: {
                                        jTreeMenuItem=jTreeMenuItemFree ;
                                        QuestMark QM = (QuestMark)nodeInfo;
                                        RefreshQuestType(QM.getQI());
                                        break;
                                    }
                                    case 8: {
                                        jTreeMenuItem=jTreeMenuItemFree ;
                                        break;
                                    }
                               }
                            } catch(Exception re ) {
                                System.out.println("Фигня какая то в словарях "+re.toString());
                            }
                        }

                    }
                }
            };
            jTree1.addMouseListener(ml);

            TreeSelectionModel rowSM = jTree1.getSelectionModel();
            rowSM.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    TreeSelectionModel lsm =(TreeSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                    } else {
                        node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
                        if (node == null) return;
                        try {
                            //jTreeMenu=jTreeMenuItemRoot;
                            nodeInfo = (DQAMarkInterface)node.getUserObject();
                            int i=nodeInfo.getType();
                            switch (i) {
                                case (1) : { //datafolder
                                    //setToolbarMode(1);
                                    DataModuleMark Difo=(DataModuleMark) nodeInfo;
                                    //DataModuleInterface DMI=(DataModuleInterface) Difo.getDMI();
                                    jTreeMenuItem=jTreeMenuItemDataFolder;
                                    DetailFormAutomat(10);
                                    JMenuItem JP=(JMenuItem)jTreeMenuItem.getComponent(3);
                                    JP.setEnabled(Difo.getMixed());
                                    //setTypeOfToolbar(1);
                                    break;
                                }  case 2: { //datamodule
                                    //setToolbarMode(1);
                                    DataModuleMark Difo=(DataModuleMark) nodeInfo;
                                    DataModuleInterface DMI=(DataModuleInterface) Difo.getDMI();
                                    DataModuleConstr.setDMI(DMI);
                                    DetailFormAutomat(11);
                                    //jTreeMenuItem=jTreeMenuItemFree;
                                    jTreeMenuItem=jTreeMenuItemDataModule;
                                    break;
                                } case 3: { //datablock
                                    setToolbarMode(1);
                                    jTreeMenuItem=jTreeMenuItemFree;
                                    break;
                                } case 4: { //datafilter
                                    setToolbarMode(3);
                                    jTreeMenuItem=jTreeMenuItemFree ;
                                    DataModuleMark DM = (DataModuleMark)nodeInfo;
                                    RDMI=DM.getRContent();
                                    DMI = DM.getDMI();
                                    jFilterFormulaTextField1.setText(DMI.getComment());
                                    break;
                                } case 5: { //datafil
                                    setToolbarMode(1);
                                    jTreeMenuItem=jTreeMenuItemFree ;
                                    break;
                                }
                                case 0: {
                                    jTreeMenuItem=jTreeMenuItemFree ;
                                    DictionaryMark DM = (DictionaryMark)nodeInfo;
                                    DictionaryIndex = DM.getID();
                                    break;
                                }
                                case 7: {
                                    jTreeMenuItem=jTreeMenuItemFree ;
                                    QuestMark QM = (QuestMark)nodeInfo;
                                    RefreshQuestType(QM.getQI());
                                    break;
                                }
                                case 8: {
                                    jTreeMenuItem=jTreeMenuItemFree ;
                                    break;
                                }
                            }

                        } catch(Exception re ) {
                            if (!node.getUserObject().toString().equalsIgnoreCase("Все вопросы")) {
                                System.out.println("Фигня какая то в словарях "+re.toString());
                            }
                        }

                    }
                }
            });


        } catch(Exception re ) {

            System.out.println("Фигня какая то в словарях "+re.toString());
        }
    }
        
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        try {
            System.out.println("пишем в базу");
            Server.StoreAllBase();
        } catch(Exception re ) {
            System.out.println("Фигня какая то в словарях "+re.toString());
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
         try {
            System.out.println("читаем ");
            Server.ReadSQLDictionaries();
        } catch(Exception re ) {
            System.out.println("Фигня какая то чтении словарей "+re.toString());
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Create SwingSet on the default monitor
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        Library library = new Library(null, GraphicsEnvironment.
                                             getLocalGraphicsEnvironment().
                                             getDefaultScreenDevice().
                                             getDefaultConfiguration());
        //}
        MeetDialog MD=new MeetDialog(library.getFrame(),true);
        MD.pack();
        MD.setLibrary(library);
        MD.setLocationRelativeTo(library);
        MD.setVisible(true);
        //new Library().setVisible(true);
    }
     
    public boolean register (String User, String Password) {
        UserInterface UI=null;
        try {
            UI=Server.autorizeUser(User, Password);
        } catch(Exception re ) {
            System.out.println("Фигня какая то чтении словарей "+re.toString());
        }    
        if (UI==null) return false; else return true;    
    }
    
    private void tuneComponent() {

        jFilterAndButton1.setEnabled(false);
        jFilterCloseButton1.setEnabled(false);
        jFilterFormulaTextField1.setEnabled(false);
        jFilterFormulaTextField1.setText("");
        jFilterNotButton1.setEnabled(false);
        jFilterOpenButton1.setEnabled(false);
        jFilterOrButton1.setEnabled(false);
        jFilterAcceptButton.setEnabled(false);
        NewRecordButton.setEnabled(false);
        LastRecordButton.setEnabled(false);
        NextRecordButton.setEnabled(false);
        DelRecordButton.setEnabled(false);
        PrevRecordButton.setEnabled(false);
        FirstRecordButton.setEnabled(false);
        jTextField2.setEnabled(false);
        jTextField2.setText("");
        
   
        
        jFilterFormulaTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                if (!jFilterAcceptButton.isEnabled()) jFilterAcceptButton.setEnabled(true);
            }

            public void removeUpdate(DocumentEvent e) {
                if (!jFilterAcceptButton.isEnabled()) jFilterAcceptButton.setEnabled(true);
            }

            public void changedUpdate(DocumentEvent e) {
                //System.out.println("changeUpdate");	   
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
                       
        jFilterAcceptButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) DMEP.jTree1.getLastSelectedPathComponent();
                    Object nodeInfo = node.getUserObject();
                    DataModuleMark Data = (DataModuleMark) nodeInfo; 
                    Data.getDMI().setComment(jFilterFormulaTextField1.getText(),getID(),true);
                    Data.getDMI().makeCreateSQL();
                    jFilterAcceptButton.setEnabled(false);
                } catch( Exception re ) {
                    System.out.println(re.toString());	   
                }
            }
        });

        jFilterAndButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterFormulaTextField1.setText(jFilterFormulaTextField1.getText()+"&" );
            }
        });

        jFilterOrButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterFormulaTextField1.setText(jFilterFormulaTextField1.getText()+"|" );
            }
        });

        jFilterNotButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterFormulaTextField1.setText(jFilterFormulaTextField1.getText()+"^" );
            }
        });

        jFilterOpenButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterFormulaTextField1.setText(jFilterFormulaTextField1.getText()+"(" );
            }
        });

        jFilterCloseButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFilterFormulaTextField1.setText(jFilterFormulaTextField1.getText()+")" );
            }
        });        

        FirstRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentInputPanel.ButtonPerformedFirst();
            }
        });
        PrevRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentInputPanel.ButtonPerformedPrev();
            }
        });
        NextRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentInputPanel.ButtonPerformedNext();
            }
        });
        LastRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentInputPanel.ButtonPerformedLast();
            }
        });
        NewRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentInputPanel.ButtonPerformedNew();
            }
        });
        DelRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CurrentInputPanel.ButtonPerformedDel();
            }
        });

        jFilterFormulaTextField1.setDragEnabled(true);
        jFilterFormulaTextField1.setTransferHandler(new FormulaComponentHandler());
        
        LowPanel.removeAll();
        //DictEP=new DataModuleEditorPanel(this,Server);
        DMEP=new DataModuleEditorPanel(this,Server);
        DEP=new DictionaryEditorPanel(this,Server);
        ETP=new EditorTreePanel(this);
        QuestConstr=new ConstrainsQuestion(this);
        AnswerConstr=new ConstrainsAnswer(this);
        DataModuleConstr=new ConstrainsDataModule(this);
        FolderConstr=new ConstrainsFolder(this);
        DataBlockConstr=new ConstrainsDataBlock(this);
        DictionaryConstr=new ConstrainsDictionary(this);


        java.awt.GridBagConstraints gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.weighty = 1.0;
        LowPanel.setLayout(new java.awt.GridBagLayout());
        LowPanel.add(DMEP,gridBagConstraints1);
        LowPanel.updateUI();
        
        desktop = new JDesktopPane();
        jPanel3.setLayout(new java.awt.BorderLayout());
        jPanel3.add(desktop);
        jPanel3.updateUI();
        //jComboBox1.setModel(DictionaryList);
        makeTree();
        //jTree1.setDragEnabled(true);
        //jTree1.setTransferHandler(new TreeTransferHandler());
        createPopupMenu();
    }
    
    public void Functionality() throws RemoteException {
        //ListDictionary();
    }

    public void RefreshDimensionList() throws RemoteException {
        //ReloadDataList();
        System.out.println( "Кто то кнопку 4" );	
    }
    
    public void RefreshDataList() throws RemoteException {
        
       // ReloadDataList();
        System.out.println( "Кто то кнопку 4" );	
    }
    
    public void RefreshDictionaryList() throws RemoteException {
        //ListDictionary();
        //SocioInputFrame.RefreshDictionaryList();
        System.out.println( "Обновляем список словарей 1234213" );	
    }
    
    public void RefreshQuestionList() throws RemoteException {
        SocioInputFrame.RefreshQuestionList();
        System.out.println( "Обновляем список вопросов" );	
    }
    
    /*private class AddListener1 {
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem)(e.getSource());
            String s = source.getText();
            System.out.println(s);	   
        }
    } */   
    
    public void createPopupMenu() {
        //JMenu menuItem;
        JMenuItem menuItem1;
        
        jTreeMenuItemDataFolder =new JPopupMenu();
        jTreeMenuItemDataModule =new JPopupMenu();
        jTreeMenuItemDataBlock  =new JPopupMenu();
        jTreeMenuItemDataFilter =new JPopupMenu();
        jTreeMenuItemData       =new JPopupMenu();
        jTreeMenuItemDictionary =new JPopupMenu();
        jTreeMenuItemQuestion   =new JPopupMenu();
        jTreeMenuItemAnswer     =new JPopupMenu();
        jTreeQuestType =new JMenu("Тип вопроса:");

        jTreeMenuItemFree       =new JPopupMenu();

    
        
        //Create the popup menu.
        //JPopupMenu popup = new JPopupMenu();
        
        //Операции с папкой
        menuItem1 = new JMenuItem("Создать папку");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 CreateDataBlock(1);
                 System.out.println("Создать");	                 
             }
        });     
        jTreeMenuItemDataFolder.add(menuItem1);
        menuItem1 = new JMenuItem("Создать опрос");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 CreateDataBlock(2);
                 System.out.println("Создать");	                 
             }
        });     
        jTreeMenuItemDataFolder.add(menuItem1);
        //popup.add(menuItem1);
        menuItem1 = new JMenuItem("Удалить");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 DeleteDataBlock(null);
                 System.out.println("Удаляем блок данных");	                 
             }
        });  
        jTreeMenuItemDataFolder.add(menuItem1);
        //popup.add(menuItem1);
        menuItem1 = new JMenuItem("Переименовать");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 RenameDataBlock();
                 System.out.println("Переименовываем блок данных");	                 
             }
        });  
        //jTreeMenuItemDataFolder.add(menuItem1);
        menuItem1 = new JMenuItem("Перекодировать");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 /*RecodeNode();*/
                 System.out.println("Перекодировать");
             }
        });
        jTreeMenuItemDataFolder.add(menuItem1);
        
        //Операции с опросом

        menuItem1 = new JMenuItem("Новый словарь");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 //NewDictionary();
                 System.out.println("Создать");
             }
        });
        jTreeMenuItemDataModule.add(menuItem1);

                
        menuItem1 = new JMenuItem("Удалить");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 DeleteDataBlock(null);
                 System.out.println("Удаляем блок данных");	                 
             }
        });  
        jTreeMenuItemDataModule.add(menuItem1);

       
        menuItem1 = new JMenuItem("Импорт");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Импорт");	                 
                //OpenModuleImportForm();
             }
        });
        jTreeMenuItemDataModule.add(menuItem1);


        // операция с множеством даных

        menuItem1 = new JMenuItem("Создать блок данных");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 CreateDataBlock(3);
                 System.out.println("Создать");
             }
        });
        jTreeMenuItemData.add(menuItem1);

        menuItem1 = new JMenuItem("Создать фильтр");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 CreateDataBlock(4);
                 System.out.println("Создать");
             }
        });
        jTreeMenuItemData.add(menuItem1);


        menuItem1 = new JMenuItem("Распределение");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                //OpenDimensionForm();
                //System.out.println("Распределение");
             }
        });
        jTreeMenuItemData.add(menuItem1);

        menuItem1 = new JMenuItem("Импорт");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Импорт");
                //OpenModuleImportForm();
             }
        });
        jTreeMenuItemData.add(menuItem1);
        
        //Операции с блоком данных 
        menuItem1 = new JMenuItem("Новый фильтр");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 CreateDataBlock(4);
                 System.out.println("Создать");	                 
             }
        });     
        jTreeMenuItemDataBlock.add(menuItem1);
        
        //popup.add(menuItem1);
        menuItem1 = new JMenuItem("Удалить");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 DeleteDataBlock(null);
                 System.out.println("Удаляем блок данных");	                 
             }
        });  
        jTreeMenuItemDataBlock.add(menuItem1);
        //popup.add(menuItem1);
        menuItem1 = new JMenuItem("Переименовать");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 RenameDataBlock();
                 System.out.println("Переименовываем блок данных");	                 
             }
        });  
        //jTreeMenuItemDataBlock.add(menuItem1);
        
        menuItem1 = new JMenuItem("Импорт");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Считываю новый словарь ");
                //OpenModuleImportForm();
             }
        });
        jTreeMenuItemDataBlock.add(menuItem1);        

        menuItem1 = new JMenuItem("Экспорт");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Экспорт");	                 
                //OpenExportForm();
             }
        });
        jTreeMenuItemDataBlock.add(menuItem1);
        
        menuItem1 = new JMenuItem("Ввод данных");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Ввод данных");	                 
                //OpenEditForm();
             }
        });
        
        jTreeMenuItemDataBlock.add(menuItem1);

        
        menuItem1 = new JMenuItem("Распределение");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                //OpenDimensionForm();
                //System.out.println("Распределение");	                 
             }
        });
        jTreeMenuItemDataBlock.add(menuItem1);
        
         //Операции с блоком данных 
        
        menuItem1 = new JMenuItem("Удалить");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 //DeleteDataBlock();
                 System.out.println("Удаляем блок данных");	                 
             }
        });  
        jTreeMenuItemDataFilter.add(menuItem1);
        menuItem1 = new JMenuItem("Переименовать");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 RenameDataBlock();
                 System.out.println("Переименовываем блок данных");	                 
             }
        });  
        jTreeMenuItemDataFilter.add(menuItem1);
        
        menuItem1 = new JMenuItem("Экспорт");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Экспорт");	                 
                //OpenExportForm();
             }
        });
        jTreeMenuItemDataFilter.add(menuItem1);
        
        menuItem1 = new JMenuItem("Ввод данных");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                System.out.println("Ввод данных");	                 
                //OpenEditForm();
             }
        });
        jTreeMenuItemDataFilter.add(menuItem1);
        
        menuItem1 = new JMenuItem("Распределение");
        menuItem1.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                //OpenDimensionForm();
                //System.out.println("Распределение");	                 
             }
        });
        jTreeMenuItemDataFilter.add(menuItem1);

        
        ///from Qep
        

        
    
        JMenuItem menuItem;
        menuItem = new JMenuItem("Новый словарь");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 //NewDictionary();
                 System.out.println("Новый словарь");	                 
             }
        });

        //jTreeMenuItemRoot.add(menuItem);
        
        menuItem = new JMenuItem("Новый вопрос");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 //NewDictionary();
                 //NewQuestion();
                 System.out.println("Добавить вопрос");	                 
             }
        });  
        jTreeMenuItemDictionary.add(menuItem);
        
        menuItem = new JMenuItem("Удалить словарь");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 try {
                    //DeleteDictionary();
                    System.out.println("Удаляем словарь");	                 
                 } catch( Exception re ) {
                    System.out.println(re.toString());	   
                 }   
             }
        });  
        jTreeMenuItemDictionary.add(menuItem);
        jTreeMenuItemDictionary.addSeparator();
        
        menuItem = new JMenuItem("Перекодировать словарь");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 try {
                    //ecodeDictionary();
                    /*RecodeNode();*/
                    System.out.println("Перекодируем словарь");	 
                 } catch( Exception re ) {
                    System.out.println(re.toString());	   
                 }   
             }
        });  
        menuItem.setEnabled(false);
        jTreeMenuItemDictionary.add(menuItem);
        
        menuItem = new JMenuItem("Новый ответ");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 //NewAnswer();
                 System.out.println("Добавить ответ");	                 
             }
        });  
        jTreeMenuItemQuestion.add(menuItem);
        
        menuItem = new JMenuItem("Удалить вопрос");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 try {
                    //DeleteQuest();
                    System.out.println("Удаляем вопрос");	                 
                 } catch( Exception re ) {
                    System.out.println(re.toString());	   
                 }   
             }
        });  
        jTreeMenuItemQuestion.add(menuItem);
        jTreeMenuItemQuestion.addSeparator();
      
        menuItem = new JMenuItem("Перекодировать вопрос");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 try {
                    System.out.println("Перекодируем вопрос");
                    /*RecodeNode();*/
                 } catch( Exception re ) {
                    System.out.println(re.toString());	   
                 }   
             }
        });  
        menuItem.setEnabled(false);
        jTreeMenuItemQuestion.add(menuItem);
        jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Альтернативный");
        jTreeQuestType.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Неальтернативный");
        jTreeQuestType.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Иной");
        jTreeQuestType.add(jRadioButtonMenuItem3);
        jTreeMenuItemQuestion.add(jTreeQuestType);
        
        menuItem = new JMenuItem("Удалить ответ");
        menuItem.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e)  {
                 try {
                    //DeleteAnswer();
                    System.out.println("Удаляем ответ");	                 
                 } catch( Exception re ) {
                    System.out.println(re.toString());	   
                 }   
             }
        });  
        jTreeMenuItemAnswer.add(menuItem);


        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new PopupListener(jTreeMenuItem);
        jTree1.addMouseListener(popupListener);
    }
    
    class PopupListener extends MouseAdapter {
        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                 jTreeMenuItem.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DelRecordButton;
    public javax.swing.JTextField DetailField;
    public javax.swing.JPanel DetailPanel;
    private javax.swing.JButton FirstRecordButton;
    private javax.swing.JPanel LEP;
    private javax.swing.JButton LastRecordButton;
    private javax.swing.JPanel LowPanel;
    private javax.swing.JPanel MainEditionPanel;
    private javax.swing.JButton NewRecordButton;
    private javax.swing.JButton NextRecordButton;
    private javax.swing.JButton PrevRecordButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    public javax.swing.JButton jFilterAcceptButton;
    private javax.swing.JButton jFilterAndButton1;
    private javax.swing.JButton jFilterCloseButton1;
    public javax.swing.JTextField jFilterFormulaTextField1;
    private javax.swing.JButton jFilterNotButton1;
    private javax.swing.JButton jFilterOpenButton1;
    private javax.swing.JButton jFilterOrButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables


    ///from QEP
    private void RefreshQuestType(QuestionInterface QI) {
        try {
                int Value = QI.getQuestionType();
                if (Value==alter) {
                    jRadioButtonMenuItem1.setSelected(true);
                    jRadioButtonMenuItem2.setSelected(false);
                    jRadioButtonMenuItem3.setSelected(false);
                    jTreeQuestType.setText("Альтернативный");
                } else  if (Value==nonalter) {
                        jRadioButtonMenuItem2.setSelected(true);
                        jRadioButtonMenuItem1.setSelected(false);
                        jRadioButtonMenuItem3.setSelected(false);
                        jTreeQuestType.setText("Неальтернативный");
                    } else {
                            jRadioButtonMenuItem3.setSelected(true);
                            jRadioButtonMenuItem1.setSelected(false);
                            jRadioButtonMenuItem2.setSelected(false);
                            jTreeQuestType.setText("Иной");
                }
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };

/*        public void DeleteAnswer() {
        try {
            DQAInterface DQABranch1=(DQAInterface)node.getUserObject();
            AnswerMark AM = (AnswerMark)DQABranch1;
            DefaultMutableTreeNode Parent = (DefaultMutableTreeNode)node.getParent();
            QuestMark QM =(QuestMark)Parent.getUserObject();
            QM.getQI().delAnswer(AM.getID());
            refreshBranch(Parent,3,1);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };


    public void DeleteQuest() {
        try {
            DQAInterface DQABranch1=(DQAInterface)node.getUserObject();
            QuestMark QM = (QuestMark)DQABranch1;
            DefaultMutableTreeNode Parent = (DefaultMutableTreeNode)node.getParent();
            DictionaryMark DM =(DictionaryMark)Parent.getUserObject();
            Server.getDictionary(DM.getID()).delQuestion(QM.getID());
            refreshBranch(Parent,2,1);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };

    public void DeleteDictionary() {
        try {
            Server.DeleteDictionary(DictionaryIndex);
            refreshBranch((DefaultMutableTreeNode)jTree1.getModel().getRoot(),1,1);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };*/


    public void RenameAnswer() {
        try {
            //String AnswerName = MF.GetStringValue(Question.getAnswer(AnswerID).getName(),"Введите формулировку ответа");
            //Question.getAnswer(AnswerID).setName(AnswerName);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };

    /*public void RecodeQuestion() {
        try {
            refreshBranch(node,3,1);
            QuestMark QM = (QuestMark)node.getUserObject();
            QM.setMixed(false);
            JMenuItem JP=(JMenuItem)jTreeMenuItem.getComponent(3);
            JP.setEnabled(false);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };

    public void RecodeDictionary() {
        try {
            refreshBranch(node,2,1);
            DictionaryMark DM = (DictionaryMark)node.getUserObject();
            DM.setMixed(false);
            JMenuItem JP=(JMenuItem)jTreeMenuItem.getComponent(3);
            JP.setEnabled(false);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    };*/

/*        public void NewAnswer(){
        try {
            refreshBranch(node,3,2);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }

    public void CheckDictionaryTree(){
        try {
            refreshBranch(node,1,2);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }*/

/*    public void NewDictionary(){
        try {
            Integer Key = Server.getKey();
            Server.newDictionary("New dictionary",Key);
            DataInfo DI = (DataInfo)nodeInfo;
            DI.getData().setDictionary(Key);
            CheckDictionaryTree();
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }*/

    /*public void NewQuestion(){
        try {
            refreshBranch(node,2,2);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }*/

    /*private void refreshBranch(javax.swing.tree.DefaultMutableTreeNode TM2,int type, int operation) {
        try {
            if (type!=4) {
                switch (type) {
                 case 1: {
                   switch (operation) {
                    case 1: { //удаление перестановка
                       int i=0;
                       int j=0;
                       ArrayList Keys=Server.getDictionaryKeys();
                       while (i<TM2.getChildCount()) {
                           javax.swing.tree.DefaultMutableTreeNode DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(i);
                           DQAInterface DQABranch1=(DQAInterface)DMT.getUserObject();
                           DictionaryMark QM2 = (DictionaryMark)DQABranch1;
                           j=0;
                           boolean Flag=true;
                           while ((j<Keys.size())&(Flag)) {
                               Flag=(QM2.getID().compareTo((Integer)Keys.get(j))!=0);
                               j++;
                           }
                           i++;
                           javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                           if (Flag) {
                               DTM.removeNodeFromParent(DMT);
                           } else if (i!=j) {
                               DTM.removeNodeFromParent(DMT);
                               DTM.insertNodeInto(DMT,DMT,j);
                           }
                       }
                       jTree1.updateUI();
                       break;
                    }
                    case 2: { //удаление перестановка

                       int i=0;
                       int j=0;
                       ArrayList Keys=Server.getDictionaryKeys();
                       while (i<Keys.size()) {
                           j=0;
                           boolean Flag=true;
                           System.out.println("Словарей "+TM2.getChildCount());
                           //QM2=null;
                           while ((j<TM2.getChildCount()-1)&Flag) {
                                javax.swing.tree.DefaultMutableTreeNode DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(j);
                                DQAInterface DQABranch1=(DQAInterface)DMT.getUserObject();
                                DictionaryMark QM2 = (DictionaryMark)DQABranch1;
                                Flag=(QM2.getID().compareTo((Integer)Keys.get(i))!=0);
                                j++;
                           }
                           javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                           if (Flag) {
                               DictionaryInterface DI=Server.getDictionary((Integer)Keys.get(i));
                               javax.swing.tree.DefaultMutableTreeNode root=new DefaultMutableTreeNode(new DictionaryMark(DI));
                               makeQuestBranches(root,DI);
                               DTM.insertNodeInto(root, TM2, TM2.getChildCount()-1);
                           }
                           i++;
                       }
                       jTree1.updateUI();
                       break;
                    }
                   }
                   break;
                   }
                 case 2: {
                   switch (operation) {
                    case 1: { //удаление перестановка в словаре
                       int i=0;
                       int j=0;
                       DQAInterface DQABranch1=(DQAInterface)TM2.getUserObject();
                       DictionaryMark DM = (DictionaryMark)DQABranch1;
                       javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                       while (i<TM2.getChildCount()) {
                           javax.swing.tree.DefaultMutableTreeNode DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(i);
                           DQABranch1=(DQAInterface)DMT.getUserObject();
                           QuestMark QM2 = (QuestMark)DQABranch1;
                           j=0;
                           boolean Flag=true;
                           while ((j<DM.getQI().getQuestSize())&Flag) {
                               Flag=(QM2.getID().compareTo(DM.getQI().getQuestionPos(j).getID())!=0);
                               j++;
                           }
                           if (Flag) {
                               DTM.removeNodeFromParent(DMT);
                           } else {
                               j--;
                               if (i!=j) {
                                    RInterface RQI=DM.getQI().getQuestionPos(j);
                                    DM.getQI().getQuestionPos(i).setPos(j);
                                    RQI.setPos(i);
                               }
                               i++;
                           };
                       }
                       jTree1.updateUI();
                       break;
                    }
                    case 2: { //добавление
                       DQAInterface DQABranch1=(DQAInterface)TM2.getUserObject();
                       DictionaryMark DM = (DictionaryMark)DQABranch1;
                       Integer Key = Server.getKey();
                       QuestionInterface CurrQuest=Server.getMainDictionary().newQuestion("New question",Key);
                       CurrQuest.setType(1);
                       RInterface RQI=DM.getQI().addQuestion(Key);
                       javax.swing.tree.DefaultMutableTreeNode root=new DefaultMutableTreeNode(new QuestMark(RQI,CurrQuest, DM.getID()));
                       makeAnswerBranches(root,RQI);
                       int i=0;
                       int j=0;
                       while (i<DM.getQI().getQuestSize()) {
                           j=0;
                           boolean Flag=true;
                           javax.swing.tree.DefaultMutableTreeNode DMT=null;
                           while ((j<TM2.getChildCount())&Flag) {
                                DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(j);
                                DQABranch1=(DQAInterface)DMT.getUserObject();
                                QuestMark QM2 = (QuestMark)DQABranch1;
                                Flag=(QM2.getID().compareTo(DM.getQI().getQuestionPos(i).getID())!=0);
                                j++;
                           }
                           i++;
                           javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                           if (Flag) {
                               DTM.insertNodeInto(root, TM2, TM2.getChildCount());
                           }
                       }
                       jTree1.updateUI();
                       break;
                    }
                   }
                   break;
                   }
                 case 3: {
                   switch (operation) {
                    case 1: { //удаление перестановка в словаре
                       int i=0;
                       int j=0;
                       DQAInterface DQABranch1=(DQAInterface)TM2.getUserObject();
                       QuestMark QM = (QuestMark)DQABranch1;
                       javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                       while (i<TM2.getChildCount()) {
                           javax.swing.tree.DefaultMutableTreeNode DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(i);
                           DQABranch1=(DQAInterface)DMT.getUserObject();
                           AnswerMark AM2 = (AnswerMark)DQABranch1;
                           j=0;
                           boolean Flag=true;
                           while (j<QM.getQI().GetAnswerSize()&Flag) {
                               Flag=(AM2.getID().compareTo(QM.getQI().getAnswerPos(j).getID())!=0);
                               j++;
                           }
                           if (Flag) {
                               DTM.removeNodeFromParent(DMT);
                           } else {
                               j--;
                               if (i!=j) {
                                    RInterface RAI=QM.getQI().getAnswerPos(j);
                                    QM.getQI().getAnswerPos(i).setPos(j);
                                    RAI.setPos(i);
                               }
                               i++;
                           }
                       }
                       jTree1.updateUI();
                       break;
                    }
                    case 2: { //добавление
                       DQAInterface DQABranch1=(DQAInterface)TM2.getUserObject();
                       QuestMark QM = (QuestMark)DQABranch1;
                       Integer Key = Server.getKey();
                       Server.getMainQuestion().newAnswer("New аnswer",Key);
                       QuestionInterface QI = QM.getQI();
                       RInterface RAI=QI.addAnswer(Key);
                       AnswerMark AM=new AnswerMark(RAI,Server.getMainQuestion().getAnswer(Key),QM.getRQI());
                       DefaultMutableTreeNode root=new DefaultMutableTreeNode(AM);
                       int i=0;
                       int j=0;
                       javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)jTree1.getModel();
                       while (i<QM.getQI().GetAnswerSize()) {
                           j=0;
                           boolean Flag=true;
                           javax.swing.tree.DefaultMutableTreeNode DMT=null;
                           RAI=QI.getAnswerPos(i);
                           while ((j<TM2.getChildCount())&Flag) {
                                DMT=(javax.swing.tree.DefaultMutableTreeNode)TM2.getChildAt(j);
                                DQABranch1=(DQAInterface)DMT.getUserObject();
                                AnswerMark AM2 = (AnswerMark)DQABranch1;
                                Flag=(AM2.getID().compareTo(QM.getQI().getAnswerPos(i).getID())!=0);
                                j++;
                           }
                           i++;
                           if (Flag) {
                               DTM.insertNodeInto(root, TM2, TM2.getChildCount());
                           }
                       }
                       jTree1.updateUI();
                       break;
                    }
                   }
                   break;
                   }
                 }
                }

        } catch(Exception re ) {
            System.out.println("Фигня какая то в словарях и их ответах "+re.toString());
        }


    }*/



}
