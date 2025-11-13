/*
 * InputPanel.java
 *
 * Created on 29 Март 2004 г., 20:34
 */
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.ComboBoxModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;



/**
 *
 * @author  Иван
 */
public class ExportPanel extends javax.swing.JPanel  implements InputPanelInterface {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    
    static int table     = 1;
    static int box       = 2;
    static int list      = 3;
    
    //private InputTableModel myModel;
    private JDBCAdapter TModel;
    //private DataModule RedoDate;
    INIACRMIInterface Server;
    DataModuleInterface DataModule;
    TableAdapterInterface TM;
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
    
    //JComboBox comboBox = new JComboBox();
    //ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
    
public ArrayList ParseString(String Str)  {    
    int i=0;
    String NewStr="";
    ArrayList ResultArray=new ArrayList();
    while (i<Str.length()) {
        if ((Str.charAt(i)!=';')&(Str.charAt(i)!=' ')) {
            NewStr=NewStr+Str.charAt(i);
            i++;
        } else {
            ResultArray.add(NewStr);
            NewStr="";
            i++;
        }
    }
    //if (ResultArray.size()==0) {ResultArray.add(NewStr);}
    ResultArray.add(NewStr);
    return ResultArray;
}



public ExportPanel(DataModuleInterface aDataModule) throws RemoteException {
        DataModule = aDataModule;
        Server = DataModule.getServer();
        ChangeFactor=table;
        Refresher=new InputPanelRefresh(this);
     
        initComponents();
        try {
            /*if (DataModule.existTable() == false) {
                DataModule.createTable();
            }*/
            TM = DataModule.getTable();
            //TM.setInputPanel(Refresher);
            DataModuleSizeOut();
            Dictionary = Server.getDictionary(DataModule.getDictionary());
            LimTable=Dictionary.getSize();
            TModel = new JDBCAdapter(TM);
            jList1.setModel(LM);            
            rowSM = jTable1.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    try {
                        if ((selectedRow!=lsm.getMinSelectionIndex())&(ChangeFactor==table)) {
                            selectedRow = lsm.getMinSelectionIndex();
                            RInterface RQI=Dictionary.getByPos(new Integer(selectedRow));
                            DictionaryInterface DI=Server.getMainDictionary();
                            Integer I=RQI.getID();
                            QI=DI.getQuestion(I);
                            System.out.println("Вопрос "+QI.getName());
                            ArrayList AnswerArray= QI.getKeys();
                            int i=0;
                            LM.removeAllElements();
                            AnswerInterface A;
                            RInterface RA;
                            while (i<AnswerArray.size()) {
                                RA = (RInterface)AnswerArray.get(i);
                                A = Server.getMainQuestion().getAnswer(RA.getID());
                                LM.addElement(A.getName());
                                i++;
                            };
                            jTextField1.setText(TModel.getValueAt(selectedRow,2).toString());
                            if (QI.getQuestionType()==alter) {
                                Integer Num = new Integer(TModel.getValueAt(selectedRow,2).toString());
                                jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                if (Num.intValue()<=jList1.getModel().getSize()){
                                   jList1.setSelectedIndex(Num.intValue()-1);
                                   jList1.ensureIndexIsVisible(Num.intValue()-1);
                                };
                                //TModel.setValueAt(jTextField1.getText(), selectedRow , 2);
                            }   else if (QI.getQuestionType()==nonalter) {
                                    Str=TModel.getValueAt(selectedRow,2).toString();
                                    jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                                    ArrayList ListSel=ParseString(Str);
                                    i=0;
                                    int [] d;
                                    d = new int[ListSel.size()];
                                    while (i<ListSel.size()) {
                                        Integer Num = new Integer(ListSel.get(i).toString());
                                        d[i] = Num.intValue()-1;
                                        jList1.setSelectedIndices(d);
                                        //jList1.setSelectedIndex(Num.intValue()-1);
                                        //jList1.getSelectionModel().addSelectionInterval(Num.intValue()-1, Num.intValue()-1);
                                        System.out.println("hjk"+ListSel.get(i));
                                        i++;
                                    }
                                }   else if (QI.getQuestionType()==free) {
                                        Integer Num = new Integer(TModel.getValueAt(selectedRow,2).toString());
                                        jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                                        jList1.setSelectedIndex(Num.intValue()-1);
                                    } 
                        }    
                    } catch (Exception ex) {
                        System.out.println(ex);
                        jList1.clearSelection();
                        jTextField1.setText("");
                    }    
                }
             }
            });
            jTable1.setModel(TModel);
            jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            initColumnSizes(jTable1, TModel);
            selectedRow=0;
            initFocusValue();
            DataModuleSizeOut();
            CurrentRecOut();
            //createTableColumn(jTable1, TModel);
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void DataModuleSizeOut()  {
        try { 
            //jLabel2.setText("Всего записей : "+TModel.getRowCount());
            jLabel2.setText("Всего записей : "+TM.Size());
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }
    
    public void CurrentRecOut()  {
        try { 
            //jLabel2.setText("Всего записей : "+TModel.getRowCount());
            int i=TM.getCurrRow()+1;
            jTextField2.setText(""+i);
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }
    
    public void initFocusValue()  {
        try {    
            RInterface RQI=Dictionary.getByPos(new Integer(selectedRow));
            DictionaryInterface DI=Server.getMainDictionary();
            Integer I=RQI.getID();
            QI=DI.getQuestion(I);
            System.out.println("Вопрос "+QI.getName());
            ArrayList AnswerArray= QI.getKeys();
            int i=0;
            String Str="";
            try {
                Object O=TModel.getValueAt(selectedRow,2).toString();
                Str=O.toString();
            } catch (Exception ex) {
                System.out.println(ex);
            }     
            jTextField1.setText(Str);
            LM.removeAllElements();
            AnswerInterface A;
            RInterface RA;
            while (i<AnswerArray.size()) {
                RA = (RInterface)AnswerArray.get(i);
                A = Server.getMainQuestion().getAnswer(RA.getID());
                LM.addElement(A.getName());
                i++;
            };
            if (QI.getQuestionType()==alter) {
                    Integer Num = new Integer(Str);
                    jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    if (Num.intValue()<=jList1.getModel().getSize()){
                        jList1.setSelectedIndex(Num.intValue()-1);
                        jList1.ensureIndexIsVisible(Num.intValue()-1);
                    } else {jList1.clearSelection();};
                    //TModel.setValueAt(jTextField1.getText(), selectedRow , 2);
            }   else if (QI.getQuestionType()==nonalter) {
                    //Str=TModel.getValueAt(selectedRow,2).toString();
                    jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                    ArrayList ListSel=ParseString(Str);
                    i=0;
                    int [] d;
                    d = new int[ListSel.size()];
                    while (i<ListSel.size()) {
                        Integer Num = new Integer(ListSel.get(i).toString());
                        d[i] = Num.intValue()-1;
                        jList1.setSelectedIndices(d);
                        System.out.println("hjk"+ListSel.get(i));
                        i++;
                    }
            }   else if (QI.getQuestionType()==free) {
                    Integer Num = new Integer(TModel.getValueAt(selectedRow,2).toString());
                    jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                    jList1.setSelectedIndex(Num.intValue()-1);
            }   
            rowSM.setSelectionInterval(selectedRow, selectedRow);
        } catch (Exception ex) {
            rowSM.setSelectionInterval(selectedRow, selectedRow);
            System.out.println(ex);
        }    
    }

    private void UpdateTextList() {
         Integer Num = new Integer(TModel.getValueAt(selectedRow,2).toString());
         jList1.setSelectedIndex(Num.intValue()-1);
         TModel.setValueAt(jTextField1.getText(), selectedRow , 2);
    }
    
    private void createTableColumn(JTable table, JDBCAdapter model) {
        /*
        TableColumn sportColumn = table.getColumnModel().getColumn(2);
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Snowboarding");
        comboBox.addItem("Rowing");
        comboBox.addItem("Chasing toddlers");
        comboBox.addItem("Speed reading");
        comboBox.addItem("Teaching high school");
        comboBox.addItem("None");
        comboBox.setEditable(true);
        */
        
    }

    private void initColumnSizes(JTable table, JDBCAdapter model) {
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        int cellWidth = 0;
        //Object[] longValues = model.longValues;

        for (int i = 0; i < 3; i++) {
            column = table.getColumnModel().getColumn(i);

            /*try {
                comp = column.getHeaderRenderer().
                                 getTableCellRendererComponent(
                                     null, column.getHeaderValue(), 
                                     false, false, 0, 0);
                headerWidth = comp.getPreferredSize().width;
            } catch (NullPointerException e) {
                System.err.println("Null pointer exception!");
                System.err.println("  getHeaderRenderer returns null in 1.3.");
                System.err.println("  The replacement is getDefaultRenderer.");
            }

            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                             getTableCellRendererComponent(
                                 table, new String ("eeeee"),
                                 false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;
            //XXX: Before Swing 1.1 Beta 2, use setMinWidth instead.*/
            //column.setPreferredWidth(Math.max(headerWidth, 100));
            column.setPreferredWidth(100);
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

        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(550, 300));
        setPreferredSize(new java.awt.Dimension(550, 300));
        setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Статистика"));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setText("Текущая запись : <not calculated>");
        jPanel5.add(jLabel1);

        jLabel2.setText("Всего записей : <not calculated>");
        jPanel5.add(jLabel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel5, gridBagConstraints);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ввод данных"));
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
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTable1CaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Детализация данных"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jTextField1, gridBagConstraints);

        jList1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jList1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jList1FocusLost(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jScrollPane2, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jSplitPane1, gridBagConstraints);

        jButton1.setText("<<");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setText("<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jTextField2.setText("<not aviable>");
        jTextField2.setAlignmentX(0.0F);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jTextField2);

        jButton3.setText(">");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setText(">>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton6.setText("New");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton6);

        jButton5.setText("Del");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setLabel("Экспорт");
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton7);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jToolBar1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jList1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jList1FocusLost
// TODO add your handling code here:
        JList1Focus = false;
    }//GEN-LAST:event_jList1FocusLost

    private void jList1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jList1FocusGained
// TODO add your handling code here:
        JList1Focus = true;
    }//GEN-LAST:event_jList1FocusGained

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        /*
        Есть несколько вариантов этого компонента
        1. Если неальтернативный то в зависимости от выбранного варианта впечатываем 
        в текстовую форму значение кода.
        2. Если альтернативный то впечатываем несколько вариантов
        3. Если свободный то впечатываем значение
        */
        try {
            ChangeFactor=box;
            if (QI.getQuestionType()==alter) {
                Integer Num = new Integer(jTextField1.getText());
                //jList1.setSelectedIndex(Num.intValue()-1);
                TModel.setValueAt(jTextField1.getText(), selectedRow , 2);
                selectedRow ++;
                if (LimTable<=selectedRow) {
                    if (TM.getCurrRow()>=TM.Size()) {
                        NewRecord();
                    } else {
                        NextRecord();
                        selectedRow=0;
                    }
                } else {
                    //rowSM.setSelectionInterval(selectedRow, selectedRow);
                    //rowSM.
                    //jTable1.e
                    //initFocusValue();
                }    
                initFocusValue();
                //rowSM.setLeadSelectionIndex(selectedRow);
                System.out.println("О "+rowSM.getMaxSelectionIndex());
            } else if (QI.getQuestionType()==nonalter) {
                //Str=jTextField1.getText();
                TModel.setValueAt(jTextField1.getText(), selectedRow , 2);
                //jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                //ArrayList ListSel=ParseString(jTextField1.getText());
                //int i=0;
                //int [] d;
                //d = new int[ListSel.size()];
                //while (i<ListSel.size()) {
                //  Integer Num = new Integer(ListSel.get(i).toString());
                //  d[i] = Num.intValue()-1;
                //  jList1.setSelectedIndices(d);
                //   i++;
                //}
                selectedRow ++;
                if (LimTable<=selectedRow) {
                    if (TM.getCurrRow()>=TM.Size()) {
                        NewRecord();
                    } else {
                        NextRecord();
                        selectedRow=0;
                    }
                } else {
                    //rowSM.setSelectionInterval(selectedRow, selectedRow);
                    //rowSM.
                    //jTable1.e
                    //initFocusValue();
                }    
                initFocusValue();
                //System.out.println("Все типтоп ");
            } else if (QI.getQuestionType()==free) {
                //Integer Num = new Integer(TModel.getValueAt(selectedRow,2).toString());
                //jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                //jList1.setSelectedIndex(Num.intValue()-1);
            } 
            ChangeFactor=table;
        } catch (Exception ex) {
            System.out.println("Ошибка в обработке jTextField1");
            ChangeFactor=1;
            initFocusValue();
        } 
        System.out.println("Набрано что то в jTextField1");
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        // TODO add your handling code here:
        /*
        Есть несколько вариантов этого компонента
        1. Если неальтернативный то в зависимости от выбранного варианта впечатываем 
        в текстовую форму значение кода.
        2. Если альтернативный то впечатываем несколько вариантов
        3. Если свободный то впечатываем значение
        */
        if (JList1Focus) {
        try {
            ChangeFactor=list;
            if (QI.getQuestionType()==alter) {
                Integer Num = new Integer(jList1.getMinSelectionIndex()+1);
                //comboBox.setSelectedIndex(Num.intValue()-1);
                jTextField1.setText(Num.toString());
                TModel.setValueAt(Num.toString(), selectedRow, 2);
                rowSM.setSelectionInterval(selectedRow, selectedRow);
            } else if (QI.getQuestionType()==nonalter) {
                int i=0;
                int[] trans = jList1.getSelectedIndices();
                int lim=trans.length;
                String ValStr="";
                String Pos="";
                while (i<lim) {
                  ValStr=ValStr+Pos+(trans[i]+1);
                  Pos=" ";
                  i++;
                }
                TModel.setValueAt(ValStr, selectedRow , 2);
                jTextField1.setText(ValStr);
                //selectedRow ++;
                rowSM.setSelectionInterval(selectedRow, selectedRow);
                //initFocusValue();
                //System.out.println("Все типтоп ");
            } else if (QI.getQuestionType()==free) {
                //Integer Num = new Integer(TModel.getValueAt(selectedRow,2).toString());
                //jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
                //jList1.setSelectedIndex(Num.intValue()-1);
            } 
            ChangeFactor=table;
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
            ChangeFactor=table;
        } 
        System.out.println("Выбрано что то в jList1");
        }
    }//GEN-LAST:event_jList1ValueChanged

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

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try {
            TModel.DelRow();
            CurrentRecOut();
            initFocusValue();
            //TModel.Reload(); 
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            TModel.LastRow();
            DataModuleSizeOut();
            CurrentRecOut();
            initFocusValue();
            //TModel.Reload(); 
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            TModel.PrevRow();
            CurrentRecOut();
            initFocusValue();
            //TModel.Reload(); 
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            TModel.FirstRow();
            CurrentRecOut();
            initFocusValue();
            //TModel.Reload(); 
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void NewRecord() {                                         
        // TODO add your handling code here:
        try {
            TModel.InsertRow();
            DataModuleSizeOut();
            CurrentRecOut();
            selectedRow=0;
            //TModel.Reload(); 
            TModel.fireTableStructureChanged();
            initFocusValue();
              
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }        
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        try {
           NewRecord();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed"+ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    /*public void setLabel () {
        jLabel1.setText("Всего записей :"+RecordCount);
        jLabel2.setText("Текущая запись :"+CurrentRecord);
    }*/
    
    private void NextRecord() {                                         
        // TODO add your handling code here:
        try {
            TModel.NextRow();
            CurrentRecOut();
            initFocusValue();
            //TModel.Reload(); 
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed"+ex);
        }
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try {
            NextRecord();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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

private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
// TODO add your handling code here:
    try {
            TModel.getRow(new Integer(jTextField2.getText()).intValue());
            //CurrentRecOut();
            //TModel.Reload(); 
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
            CurrentRecOut();
    }
}//GEN-LAST:event_jTextField2ActionPerformed

private void ExportDaFile(String FileName) {
    System.out.println("Вроде что то сохраняю");
    try {
            ArrayList AnswerArray;
            AnswerInterface A;
            RInterface RA;
            BufferedWriter out = new BufferedWriter(new FileWriter(FileName));
            out.write(DataModule.getName());
            out.newLine();
            for (int i = 0;  i< Dictionary.getSize(); i++) {
                RInterface RQI=Dictionary.getByPos(i);
                DictionaryInterface DI=Server.getMainDictionary();
                QI=DI.getQuestion(RQI.getID());
                if (QI.getQuestionType()==alter) {
                    out.write("//	Альтернативная текстовая переменная");
                    out.newLine();
                    out.write((i+1)+".at\t");
                } else {
                    out.write("//	Неальтернативная текстовая переменная");
                    out.newLine();
                    out.write((i+1)+".nt\t");
                }
                out.write(QI.getName());
                out.newLine();
                AnswerArray= QI.getKeys();
                for (int j = 0;  j<AnswerArray.size(); j++) {
                    RA = (RInterface)AnswerArray.get(j);
                    A = Server.getMainQuestion().getAnswer(RA.getID());
                    out.write("\t\t"+(j+1)+".");
                    out.write(A.getName());
                    out.newLine();
                };
            } 
            out.write("*");
            out.newLine();
            int i=1;
            try {
               while (i<=TM.Size()) {
                   Vector V=TM.getRow(i);
                   for (int j = 1; j < V.size(); j++) {
                       Object O=V.get(j);
                       RInterface RQI=Dictionary.getByPos(j-1);
                       DictionaryInterface DI=Server.getMainDictionary();
                       QI=DI.getQuestion(RQI.getID());
                       if (QI.getQuestionType()==alter) {
                            if (O!=null) {out.write(O.toString());}
                            else {out.write("#");}
                       } else {
                            if (O!=null) {out.write("{ "+O.toString()+" }");}
                            else {out.write("#");}
                       }
                       out.write(",");
                   }
                   out.newLine();
                   i++;
               }
            } catch (Exception e2) {
                System.out.println("Неудается записать "+e2);
            }
            out.close();
    } catch (Exception e2) {
            System.out.println("Can't open and write file "+e2.toString());
    }
}

private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
// TODO add your handling code here:
    ExportDaFile("e:/exportdafile.txt");
}//GEN-LAST:event_jButton7ActionPerformed
    
    public boolean setCurrent(int S) throws RemoteException {
        System.out.println("Текущий номер записи "+S);
        jLabel1.setText("Текущий номер записи "+S);
        return true;
    };
    public boolean setSize(int S) throws RemoteException {
        System.out.println("Всего записей "+S);
        return true;
    };
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    
}
