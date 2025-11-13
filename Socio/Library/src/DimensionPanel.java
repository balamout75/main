/*
 * InputPanel.java
 *
 * Created on 29 Март 2004 г., 20:34
 */
//import java.awt.event.KeyEvent;
import javax.swing.*;
import java.rmi.RemoteException;

//import javax.swing.event.*;
import java.awt.event.*;


//import java.util.ArrayList;



/**
 *
 * @author  Иван
 */
public class DimensionPanel extends javax.swing.JPanel  {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    
    static int table     = 1;
    static int box       = 2;
    static int list      = 3;

    private String MessageX = "Ось Х";
    private String MessageY = "Ось Y";
    
    //private InputTableModel myModel;
    private DimensionAdapter TModel;
    //private DataModule RedoDate;
    INIACRMIInterface Server;
    DataModuleInterface DMI,ParentDMI;
    RInterface RDMI,ParentRDMI;

    DualTableAdapterInterface TM;
    DictionaryInterface Dictionary;
    QuestionInterface   QI;
    int RecordCount;
    int CurrentRecord;
    int LimTable=0;
    InputPanelRefresh Refresher;
    DefaultComboBoxModel LM= new DefaultComboBoxModel();
    int selectedRow = -1;
    String Str;
    ListSelectionModel rowSM;
    boolean JList1Focus;
    int ChangeFactor=1;
    private Integer QuestIndex;
    private DefaultListModel listQuestion1;
    private DefaultListModel listQuestion2;
    public RInterface RQX=null,RQY=null;
    public Library Lib;

    

public DimensionPanel(RInterface aRDMI, RInterface aParentRDMI, Library aLib) throws RemoteException {
        RDMI            =   aRDMI;
        ParentRDMI      =   aParentRDMI;
        ChangeFactor    =   table;
        Lib=aLib;
        Server=aLib.Server;
        DMI=Server.getDMI(RDMI.getID());
        ParentDMI =Server.getDMI(ParentRDMI.getID());
        initComponents();
        //listAnswer = new DefaultListModel();
        buttonGroup1.add(jRadioButton1);
        buttonGroup1.add(jRadioButton2);
        jRadioButton1.setSelected(true);
        jRadioButton1.setEnabled(false);
        jRadioButton2.setEnabled(false);
        InternalFrameEventDemo IFED = new InternalFrameEventDemo();
        this.addFocusListener(IFED);
        try {
            TM = DMI.getDualTable();
            Dictionary = Server.getDictionary(DMI.getDictionary());
            TModel = new DimensionAdapter(TM);
            jTable1.setModel(TModel);
            jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            listQuestion1 = new DefaultListModel();
            listQuestion1.clear();
            listQuestion1.addElement(MessageX);
            jList1.setDragEnabled(true);
            jList1.setModel(listQuestion1);
            
            jList1.setTransferHandler(new DimensionListHandler(this,true));

            listQuestion2 = new DefaultListModel();
            listQuestion2.clear();
            listQuestion2.addElement(MessageY);
            jList2.setDragEnabled(true);
            jList2.setModel(listQuestion2);
            jList2.setTransferHandler(new DimensionListHandler(this,false));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public RInterface getParentRDMI() {
        return ParentRDMI;
    }
    
    public DataModuleInterface getParentDMI() {
        return ParentDMI;
    }
    
    public class InternalFrameEventDemo implements FocusListener {


        public void focusGained(FocusEvent e) {
            //if
            try {
                System.out.println("Listener was ..");
                Lib.DEP.setDictionary(Server.getDictionary(DMI.getDictionary()));
                Lib.DetailFormAutomat(9);
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
        }

        public void focusLost(FocusEvent e) {
            System.out.println("Internal frame LostFocus");
        }

    }

    public void changePercentMode() {
        try {
            TModel.setPercent(jCheckBox1.isSelected(), jRadioButton1.isSelected());
            jTable1.updateUI();

        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }
    
    public void changeClearenceMode() {
        try {
            TModel.setClearence(jCheckBox2.isSelected());
            jTable1.updateUI();
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }    
    
    public void RefreshTable() throws RemoteException {
        System.out.println( "Обновление списка вопросов" );	   
        try {
            //TM.changeDimension(RQX, RQY);
            TModel.changeDimension(RQX, RQY);
        } catch( Exception re ) {
        System.out.println(re.toString());	   }
    }
    
    public void DataModuleSizeOut()  {
        try { 
            //jLabel2.setText("Всего записей : "+TModel.getRowCount());
            //jLabel2.setText("Всего записей : "+TM.Size());
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }
    
    public void CurrentRecOut()  {
        try { 
            //jLabel2.setText("Всего записей : "+TModel.getRowCount());
            //int i=TM.getCurrRow()+1;
            //jTextField2.setText(""+i);
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }
    



    /*public void setUpSportColumn(TableColumn sportColumn) {
        //Set up the editor for the sport cells.
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Snowboarding");
        comboBox.addItem("Rowing");
        comboBox.addItem("Chasing toddlers");
        comboBox.addItem("Speed reading");
        comboBox.addItem("Teaching high school");
        comboBox.addItem("None");
        //sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);

        //Set up tool tip for the sport column header.
        TableCellRenderer headerRenderer = sportColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer)headerRenderer).setToolTipText(
                     "Click the sport to see a list of choices");
        } 
    }*/
    
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jList1 = new javax.swing.JList();
        jList2 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();

        setMinimumSize(new java.awt.Dimension(550, 300));
        setPreferredSize(new java.awt.Dimension(550, 300));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Распределение"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jTable1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTable1CaretPositionChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Детализация данных"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Строки"));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Ось Y" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jList1, gridBagConstraints);

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Ось X" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jList2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(jPanel7, gridBagConstraints);

        jLabel1.setText("Строки");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel4.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel3, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Обновить");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel5, gridBagConstraints);

        jCheckBox1.setText("В процентах");
        jCheckBox1.setFocusable(false);
        jCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel6.add(jCheckBox1);

        jRadioButton1.setText("X");
        jRadioButton1.setFocusable(false);
        jRadioButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jRadioButton1);

        jRadioButton2.setText("Y");
        jRadioButton2.setFocusable(false);
        jRadioButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jRadioButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel6, gridBagConstraints);

        jCheckBox2.setText("Свернуть");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel8.add(jCheckBox2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost
        // TODO add your handling code here:
        //System.out.println("Выбрано 1");
    }//GEN-LAST:event_jTable1FocusLost

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
        // TODO add your handling code here:
        //System.out.println("Выбрано 2");
    }//GEN-LAST:event_jTable1FocusGained

    private void jTable1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTable1CaretPositionChanged
// TODO add your handling code here:
        
    }//GEN-LAST:event_jTable1CaretPositionChanged

   
    /*public void setLabel () {
        jLabel1.setText("Всего записей :"+RecordCount);
        jLabel2.setText("Текущая запись :"+CurrentRecord);
    }*/
    
    
    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange
        // TODO add your handling code here:
        System.out.println("Изменился контент таблицы ");
        //Что надоо сделать.. Берем из словаря информацию о столбце
        //Создаем скрипт /insert
        //Берем из Сервера JDBC адаптер и выполняем скрипт
        //Переделваем датамодель
        //Сегодня !!!
        //Надо сделать что-то с дата модулем ))) Дома сделаю
        
    }//GEN-LAST:event_jTable1PropertyChange

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    try {
        RefreshTable();
    } catch (Exception ex) {
            System.out.println("Ошибка в jButton1 ctionPerformed");
    }
}//GEN-LAST:event_jButton1ActionPerformed

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    // TODO add your handling code here:
    jRadioButton1.setEnabled(jCheckBox1.isSelected());
    jRadioButton2.setEnabled(jCheckBox1.isSelected());
    changePercentMode();

}//GEN-LAST:event_jCheckBox1ActionPerformed

private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
    // TODO add your handling code here:
    changePercentMode();
}//GEN-LAST:event_jRadioButton1ActionPerformed

private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
    // TODO add your handling code here:
    changePercentMode();
}//GEN-LAST:event_jRadioButton2ActionPerformed

private void jList1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyPressed
    // TODO add your handling code here:
    try {
        if (evt.getKeyCode()==KeyEvent.VK_DELETE) {
            listQuestion1.clear();
            listQuestion1.addElement(MessageX);
            RQX=null;
            RefreshTable();
        }
    } catch (Exception ex) {
            System.out.println("jList1KeyPressed"+ex.toString());
    }
}//GEN-LAST:event_jList1KeyPressed

private void jList2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList2KeyPressed
    // TODO add your handling code here:
    try {
        if (evt.getKeyCode()==KeyEvent.VK_DELETE) {
            listQuestion2.clear();
            listQuestion2.addElement(MessageY);
            RQY=null;
            RefreshTable();
        }
    } catch (Exception ex) {
            System.out.println("jList2KeyPressed"+ex.toString());
    }
}//GEN-LAST:event_jList2KeyPressed

private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
    // TODO add your handling code here:
    // TODO add your handling code here:
    changeClearenceMode();
}//GEN-LAST:event_jCheckBox2ActionPerformed


    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
