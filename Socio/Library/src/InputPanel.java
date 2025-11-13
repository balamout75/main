/*
 * InputPanel.java
 *
 * Created on 29 Март 2004 г., 20:34
 */

import javax.swing.*;
import java.rmi.RemoteException;
import javax.swing.table.TableColumn;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 *
 * @author  Иван
 */
public class InputPanel extends javax.swing.JPanel  implements InputPanelInterface {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    
    static int table     = 1;
    static int box       = 2;
    static int list      = 3;
    
    private JDBCAdapter TModel;
    INIACRMIInterface Server;
    RInterface RDMI;
    RInterface ParentRDMI;
    DataModuleInterface DataModule;
    DataModuleInterface ParentDMI;
    TableAdapterInterface TM;
    DictionaryInterface Dictionary;
    QuestionInterface   QI;
    RInterface   RQI;
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
    boolean thiswindow=false;
    Library Lib;
    public JTextField InputField;
    public int [] h;
    
public ArrayList ParseString(String Str)  {
    ArrayList ResultArray=new ArrayList();    
    int i=0;
    Integer Num;
    String [] s=Str.split(" ");
    while (i<s.length) {
        try {
            Integer.parseInt(s[i]);
            ResultArray.add(s[i]);
            i++;
        } catch (Exception ex) {
            System.out.println("Прошло игнорированное текстовое сообщение"+ex);
            i++;
        }
    }
    return ResultArray;
}

    public class InternalFrameEventDemo implements FocusListener {


        public void focusGained(FocusEvent e) {
            //if
            Lib.DetailFormAutomat(14);
            //CurrentRecOut();
            //System.out.println("Internal frame GainedFocus");
        }

        public void focusLost(FocusEvent e) {
            //System.out.println("Internal frame LostFocus");
        }

    }

    public DataModuleInterface getParentDMI() {
        return ParentDMI;
    }
    
    public RInterface getParentRDMI() {
        return ParentRDMI;
    }    
    
    public void getCTRLAction() {
        
    }              

    public InputPanel(RInterface aRDMI, RInterface aParent, Library aLib) throws RemoteException {

        //Звуковой сигнал
        //java.awt.Toolkit.getDefaultToolkit().beep();


        Lib = aLib;
        Server = Lib.Server;        
        RDMI = aRDMI;
        DataModule = Server.getDMI(RDMI.getID());
        ParentRDMI  = aParent;
        ParentDMI  = Server.getDMI(ParentRDMI.getID());
        ChangeFactor=table;
        Refresher=new InputPanelRefresh(this);
        initComponents();
        InternalFrameEventDemo IFED = new InternalFrameEventDemo();

        InputField = new javax.swing.JTextField();
        InputField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputFieldActionPerformed(false);
            }
        });
        
        //Обработка Контрол Ентер
        Action doNothing = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                InputFieldActionPerformed(true);
                System.out.println("Кто Контрол ентер нажал? ");
            }
        };
        
        InputMap inputMap = InputField.getInputMap();
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, java.awt.event.InputEvent.CTRL_MASK);
        inputMap.put(key, doNothing);
        
        try {
            TM = DataModule.getTable();
            Dictionary = Server.getDictionary(DataModule.getDictionary());
            LimTable=Dictionary.getSize();
            TModel = new JDBCAdapter(TM);

            rowSM = jTable1.getSelectionModel();
            jTable1.addFocusListener(IFED);
            rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)  {
                if (e.getValueIsAdjusting()) return;
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                } else {
                    try {
                        if ((selectedRow!=lsm.getMinSelectionIndex())&(ChangeFactor==table)) {
                            selectedRow = lsm.getMinSelectionIndex();
                            initFocusValue();
                        }    
                    } catch (Exception ex) {
                        System.err.println("Какая то ошибка "+ex);
                    }    
                }
             }
            });
            jTable1.setModel(TModel);
            jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            initColumnSizes(jTable1);
            jTable1.getColumnModel().getColumn(0).setCellRenderer(new JDBCAdapterCellRenderer(Server,Dictionary));
            //jTable1.getColumnModel().getColumn(1).setCellRenderer(new JDBCAdapterCellRenderer(Server,Dictionary));
            jTable1.getColumnModel().getColumn(2).setCellRenderer(new JDBCAdapterCellRenderer(Server,Dictionary));
            jTable1.getColumnModel().getColumn(3).setCellRenderer(new JDBCAdapterCellRenderer2(Server,Dictionary));
            rowSM.setSelectionInterval(selectedRow, selectedRow);
            initFocusValue();
            CurrentRecOut();
        } catch (Exception ex) {
            System.out.println("Ошибка в принципе "+ex);
        }
    }

    public void SetCurrentRow(Integer Index) {                                            
        try {
            TModel.getRow(Index);
            CurrentRecOut();
            initFocusValue();
            jTable1.updateUI();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed "+ex.toString());
        }
    }

    public void CurrentRecOut()  {
        try { 
            int i=TM.getCurrRow()+1;
            //jTextField2.setText(i+" из "+TM.Size());
            //Lib.
            Lib.CurrentRecOut(i,TM.Size());
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }
    
    public void initFocusValue()  {
        try {    
            RQI=Dictionary.getByPos(new Integer(selectedRow));
            DictionaryInterface DI=Server.getMainDictionary();
            Integer I=RQI.getID();
            QI=DI.getQuestion(I);
            System.out.println("Вопрос "+QI.getName());
            int i=0;
            String Str="";
            try {
                Object O=TModel.getValueAt(selectedRow,3).toString();
                Str=O.toString();
            } catch (Exception ex) {
                System.out.println(ex);
                Str="";
            }
            //InputField.setText(Str);
            if (QI.getQuestionType()==alter) {
                if (Str.isEmpty()) {
                    h = new int[0];
                    InputField.setText(Str);
                } else {
                    String[] s=Str.split(" ");
                    Integer Num = new Integer(s[0]);
                    InputField.setText(Str);
                    h = new int[1];
                    h[0]=Num.intValue();
                }
             }   else if (QI.getQuestionType()==nonalter) {
                            if (Str.isEmpty()) {
                                h = new int[0];
                                InputField.setText(Str);
                            } else {
                                ArrayList ListSel=ParseString(Str);
                                i=0;
                                InputField.setText(Str);
                                h = new int[ListSel.size()];
                                while (i<ListSel.size()) {
                                    Integer Num = new Integer(ListSel.get(i).toString());
                                    h[i] = Num.intValue();
                                    i++;
                                }
                                InputField.setText(Str);
                            }
                        }   else if (QI.getQuestionType()==free) {
                                    InputField.setText(Str);
                                  }
            Lib.DetailFormAutomat(8);
            //Lib.reinitETP();
            //rowSM.setLeadSelectionIndex(selectedRow);
            rowSM.setSelectionInterval(selectedRow, selectedRow);
            java.awt.Rectangle cellBounds = jTable1.getCellRect(selectedRow, 2, true); //getCellBounds(index, index);
            if (cellBounds != null) {jTable1.scrollRectToVisible(cellBounds);}
            //this.updateUI();
        } catch (Exception ex) {
            rowSM.setSelectionInterval(selectedRow, selectedRow);
            java.awt.Rectangle cellBounds = jTable1.getCellRect(selectedRow, 2, true); //getCellBounds(index, index);
            if (cellBounds != null) {jTable1.scrollRectToVisible(cellBounds);}
            System.out.println(ex);
        }    
    }
    private void initColumnSizes(JTable table) {
        TableColumn column = null;
        column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(30);
        column.setResizable(false);
        column = table.getColumnModel().getColumn(1);
        column.setPreferredWidth(300);
        column = table.getColumnModel().getColumn(2);
        column.setMaxWidth(50);
        column.setPreferredWidth(50);
        column.setResizable(false);
    } 

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(550, 300));
        setPreferredSize(new java.awt.Dimension(550, 300));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ввод данных"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private boolean CheckQuestValue(QuestionInterface QI, String Value) {
        boolean Res=true;
        return Res;
    }

    private int CheckPair(Object[] S, QuestionInterface QI) {
        int ResultCode=0;
        try {
            String code=(String)S[0];
            Integer i = new Integer(code);
            Integer AnswerID=QI.getByPos(i-1).getID();
            AnswerInterface AI=Server.getMainQuestion().getAnswer(AnswerID);
            if (!(AI.getTextable()|(S.length==1))) ResultCode=3; //Недопустимые текстовые данные 
        } catch (Exception e2) {
            ResultCode=2; // Несуществую
        }
        return ResultCode;
    }
            
    private void NextStep(String Str, boolean Booster) {
        try {
            TModel.setValueAt(Str, selectedRow , QI.getRepetitionType());
            selectedRow ++;
            if ((LimTable<=selectedRow)|Booster) {
                if (TM.getCurrRow()+1>=TM.Size()) {
                    NewRecord();
                    CurrentRecOut();
                } else {
                    NextRecord();
                    CurrentRecOut();
                    selectedRow=0;
                }
            } 
            initFocusValue();        
        } catch (Exception e2) {
            System.out.println("Ошибка в NextStep "+e2);
        }    
    }
    
    private void InputFieldActionPerformed(boolean Booster) {
        try {
            String Str=InputField.getText();
            ChangeFactor=box;
            Str.trim();
            Str=Str.replace("  "," ");
            String[] s= Str.split(" ");
            int ResultCode=0;
            if (Str.isEmpty()) ResultCode=4; 
            else if (QI.getQuestionType()==alter) {
                    try {
                        int h=new Integer(s[0]);
                        ResultCode=CheckPair(s, QI);
                    } catch (Exception e2) {
                        System.out.println("Не число"+e2);
                        ResultCode=1; //нет значения кода ответа
                    }
                } else if (QI.getQuestionType()==nonalter) {
                    ArrayList Current = new ArrayList();
                    int k = 0;
                    while ((k < s.length)&(ResultCode==0)) {
                        try {
                            int n = 0;
                            String ThisStr=s[k];
                            //if ExistingTerm(ThisStr) 
                            while ((n < Current.size())&(ResultCode==0)) {
                                String CurrStr=(String)Current.get(n);
                                if (CurrStr.equalsIgnoreCase(ThisStr)) ResultCode=6; else n++;
                            }
                            Integer h=new Integer(ThisStr);
                            Current.add(ThisStr);
                        } catch (Exception e2) {
                            if (Current.isEmpty()) ResultCode=1; else Current.add(s[k]);
                        }
                        k++;
                    }
                    //if (ResultCode==0) if (!Current.isEmpty()) ResultCode=CheckPair(Current.toArray(), QI);
                    
                } else if (QI.getQuestionType()==free) TModel.setValueAt(InputField.getText(), selectedRow , 2); 
            
            switch (ResultCode) {
                case 4 : {
                    NextStep(Str,Booster);
                    break;
                }       
                case 0 : {
                    NextStep(Str,Booster);
                    break;
                }       
                case 1: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Нет значения кода ответа", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    break;
                }                       
                case 2: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Введен код несуществующего ответа", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    break;
                }                                       
                case 3: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Введено недопустимое текстовое значение", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);                    
                    break;
                }                                       
                case 5: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Превышено допустимое число ответов", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);                    
                    break;
                }     
                case 6: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Ответ содержит повторяющиеся значения", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);                    
                    break;
                }                         
            } 
            ChangeFactor=table;
        } catch (Exception ex) {
            System.out.println("Ошибка в обработке InputField "+ex);
            ChangeFactor=1;
            initFocusValue();
        }
    }
    
    /*private void InputFieldActionPerformed(boolean Booster) {
        try {
            String Str=InputField.getText();
            ChangeFactor=box;
            Str.trim();
            Str=Str.replace("  "," ");
            String[] s= Str.split(" ");
            int ResultCode=0;
            if (Str.isEmpty()) ResultCode=4; 
            else if (QI.getQuestionType()==alter) {
                    try {
                        int h=new Integer(s[0]);
                        ResultCode=CheckPair(s, QI);
                    } catch (Exception e2) {
                        System.out.println("Не число"+e2);
                        ResultCode=1; //нет значения кода ответа
                    }
                } else if (QI.getQuestionType()==nonalter) {
                    ArrayList Current = new ArrayList();
                    int k = 0;
                    while ((k < s.length)&(ResultCode==0)) {
                        try {
                            int h=new Integer(s[k]);
                            if (!Current.isEmpty()) {
                                ResultCode=CheckPair(Current.toArray(), QI);
                                Current.clear();
                            }
                            Current.add(s[k]);
                        } catch (Exception e2) {
                            if (Current.isEmpty()) ResultCode=1; else Current.add(s[k]);
                        }
                        k++;
                    }
                    QI.
                    if (ResultCode==0) if (!Current.isEmpty()) ResultCode=CheckPair(Current.toArray(), QI);
                    
                } else if (QI.getQuestionType()==free) TModel.setValueAt(InputField.getText(), selectedRow , 2); 
            
            switch (ResultCode) {
                case 4 : {
                    NextStep(Str,Booster);
                    break;
                }       
                case 0 : {
                    NextStep(Str,Booster);
                    break;
                }       
                case 1: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Нет значения кода ответа", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    break;
                }                       
                case 2: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Введен код несуществующего ответа", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                    break;
                }                                       
                case 3: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Введено недопустимое текстовое значение", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);                    
                    break;
                }                                       
                case 5: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Превышено допустимое число ответов", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);                    
                    break;
                }     
                case 6: {   
                    //NextStep(Str);
                    JOptionPane.showMessageDialog(this, "Ответ содержит повторяющиеся значения", "Ошибка ввода", JOptionPane.ERROR_MESSAGE);                    
                    break;
                }                         
            } 
            ChangeFactor=table;
        } catch (Exception ex) {
            System.out.println("Ошибка в обработке InputField "+ex);
            ChangeFactor=1;
            initFocusValue();
        }
    }*/



    public void setValue(String Str) {
        ChangeFactor=list;
        TModel.setValueAt(Str, selectedRow , 2);
        InputField.setText(Str);
        rowSM.setSelectionInterval(selectedRow, selectedRow);
        ChangeFactor=table;
        //jTable1.updateUI();
    }
    public void ButtonPerformedDel() {
        try {
            TModel.DelRow();
            CurrentRecOut();
            initFocusValue();
            jTable1.updateUI();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }


    public void ButtonPerformedLast() {
        try {
            TModel.LastRow();
            CurrentRecOut();
            initFocusValue();
            jTable1.updateUI();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }

    public void ButtonPerformedPrev() {
        try {
            TModel.PrevRow();
            CurrentRecOut();
            initFocusValue();
            jTable1.updateUI();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }


    public void ButtonPerformedFirst() {
        try {
            TModel.FirstRow();
            CurrentRecOut();
            initFocusValue();
            jTable1.updateUI();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }


    public void ButtonPerformedNext() {
        try {
            NextRecord();
            CurrentRecOut();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }

    public void ButtonPerformedNew() {
        try {
           NewRecord();
           CurrentRecOut();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed"+ex);
        }
    }

    private void NewRecord() {                                         
        try {
            if (TModel.Controller.getFree()) {
                TModel.InsertRow();
                //CurrentRecOut();
                selectedRow=0;
                initFocusValue();
                TModel.fireTableStructureChanged();
                initColumnSizes(jTable1);
                jTable1.updateUI();
        } else {
                TModel.InsertRow();
                //CurrentRecOut();
                selectedRow=0;
                initFocusValue();
                jTable1.updateUI();
        }
            
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed");
        }
    }        
    
    private void NextRecord() {                                         
        try {
            TModel.NextRow();
            //CurrentRecOut();
            initFocusValue();
            jTable1.updateUI();
        } catch (Exception ex) {
            System.out.println("Ошибка в jButton3ActionPerformed"+ex);
        }
    }
        
    public boolean setCurrent(int S) throws RemoteException {
        System.out.println("Текущий номер записи "+S);
        return true;
    };
    
    public boolean setSize(int S) throws RemoteException {
        System.out.println("Всего записей "+S);
        return true;
    };
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
