/*
 * ImportPanel.java
 *
 * Created on 6 Март 2009 г., 11:19
 */
import javax.swing.*;
import java.awt.event.*;


/**
 *
 * @author  Иван
 */
public class ImportPanel extends javax.swing.JPanel {
    private DataModuleInterface DMI;
    private INIACRMIInterface Server;
    private java.io.File file;
    private JDialog dialog;
    //private LibraryLocalInterface parent;
    private DataModuleEditorPanel parent;
    int typeofExport=1;
    int type=0;
    
    //public ImportPanel(LibraryLocalInterface aParent,DataModuleInterface aDataModule, JDialog aDialog) {
    public ImportPanel(DataModuleEditorPanel aParent,DataModuleInterface aDataModule, JDialog aDialog, int aType) {
        type = aType;
        DMI=aDataModule;
        parent=aParent;
        dialog = aDialog;
        try {
            Server = DMI.getServer();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        } 
        initComponents();
        tuneComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Формат файла:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 1, 1);
        jPanel1.add(jLabel1, gridBagConstraints);

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text (Windows ANSI)" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 1, 5);
        jPanel1.add(jComboBox1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Имя файла:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 4, 4, 3);
        jPanel1.add(jLabel2, gridBagConstraints);

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 1, 5);
        jPanel1.add(jTextField1, gridBagConstraints);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("Обзор");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 5, 5);
        jPanel1.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setEnabled(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Что вы хотите импортировать:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 1, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jRadioButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jRadioButton1.setText("Только данные");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 5);
        jPanel2.add(jRadioButton1, gridBagConstraints);

        jRadioButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jRadioButton2.setText("Только словарь");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(jRadioButton2, gridBagConstraints);

        jRadioButton3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jRadioButton3.setText("Словарь и данные");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel2.add(jRadioButton3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setEnabled(false);
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Разделитель элементов данных");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 1, 0);
        jPanel3.add(jLabel4, gridBagConstraints);

        jComboBox2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Пробел", "Табуляция", "Конец строки" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 1, 0);
        jPanel3.add(jComboBox2, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Разделитель документов");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 1, 0);
        jPanel3.add(jLabel5, gridBagConstraints);

        jComboBox3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Конец строки", "Пробел", "Табуляция" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 1, 0);
        jPanel3.add(jComboBox3, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Разделитель в формате даты");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 1, 0);
        jPanel3.add(jLabel6, gridBagConstraints);

        jComboBox4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Точка", "/" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 1, 0);
        jPanel3.add(jComboBox4, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Разделитель в дробной части числа");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 1, 0);
        jPanel3.add(jLabel7, gridBagConstraints);

        jComboBox5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Точка", "Запятая" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 1, 0);
        jPanel3.add(jComboBox5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel3, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jButton2.setText("Готово");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2, new java.awt.GridBagConstraints());

        jButton3.setText("Отменить");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton3, new java.awt.GridBagConstraints());

        jButton4.setText("Из опроса 3Д");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel7.add(jButton4, gridBagConstraints);

        jButton5.setText("Общая загрузка");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel7.add(jButton5, gridBagConstraints);

        jButton6.setText("XML Парча");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel7.add(jButton6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel7, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    class RadioListener implements ItemListener { 
        public void itemStateChanged(ItemEvent e) {
            Object source = e.getItemSelectable();
            if (source == jRadioButton1) {
                typeofExport=1;
                //...make a note of it...
            } else if (source == jRadioButton2) {
                typeofExport=2;
                //...make a note of it...
            } else if (source == jRadioButton3) {
                typeofExport=3;
                //...make a note of it...
            }
        }
    }    
    
private void tuneComponents() {                                                
   jRadioButton1.setSelected(true);
   RadioListener myRadioListener=new RadioListener();
   jRadioButton1.addItemListener(myRadioListener);
   jRadioButton2.addItemListener(myRadioListener);
   jRadioButton3.addItemListener(myRadioListener);
   buttonGroup1.add(jRadioButton1);
   buttonGroup1.add(jRadioButton2);
   buttonGroup1.add(jRadioButton3);   
   try {
       Server = DMI.getServer();
       switch(type) {
       case 1: {
            //"Свойства папки "
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(true);
            jRadioButton2.setSelected(true);
            jRadioButton3.setEnabled(false);
            break;
        }
        case 2: {
            //" Модуль данных"
            jRadioButton1.setEnabled(true);
            jRadioButton1.setSelected(true);            
            jRadioButton2.setEnabled(false);
            jRadioButton3.setEnabled(false);
            break;
        }
        case 3: {
            //" Блок данных "
            jRadioButton1.setEnabled(true);
            jRadioButton2.setEnabled(false);
            jRadioButton3.setEnabled(false);
            break;
        }
        case 4: {
            //" Фильтр данных "
            break;
        }
       }
   } catch( Exception re ) {
            System.out.println(re.toString());	   
   } 
}
    
private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField1ActionPerformed

private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRadioButton2ActionPerformed

private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jComboBox2ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     javax.swing.JFileChooser fc = new  javax.swing.JFileChooser();
     int returnVal = fc.showOpenDialog(null);
     if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
         file=fc.getSelectedFile();
         jTextField1.setText(fc.getSelectedFile().getPath());
         jButton2.setEnabled(true);
     }
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    dialog.setVisible(false);
    dialog.dispose();
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    System.out.println("начинаем отработку"+typeofExport);
    jButton2.setEnabled(false);
    switch(type) {
       case 2: {
           try {
                if (DMI.getDictionary()!=null) {
                    if (DMI.getType()==2) {
                        Integer Key=Server.getKey();
                        DataModuleInterface newDMI = Server.newDMI("Новый блок данных",Key,3,true);
                        DMI.addElement(Key,parent.getLibrary().getID(),true);
                        newDMI.setDictionary(DMI.getDictionary(),parent.getLibrary().getID(),true);
                        newDMI.createTable();
                        //parent.addDataBlock(newDMI);
                        new DataLoader(newDMI,file);
                        //Server.WriteDictionaries();
                    } 
                    
                }    
           } catch( Exception re ) {
                System.out.println(re.toString());	   
           } 
           break;
       }
       case 1: {
           try {
                DictionaryLoader DL=new DictionaryLoader(Server,file);
                DictionaryInterface DI = DL.getDictionary();
                //Server.WriteDictionaries();
                parent.setDictionary(DI);
                //parent.DMEP.CheckDictionaryTree();
           } catch( Exception re ) {
            System.out.println(re.toString());	   
           } 
            break;           
       }
       case 3: {
           try {
                //new DictionaryLoader(Server,file);
                new DataLoader(DMI,file);
                parent.CheckDictionaryTree();
                //Server.WriteDictionaries();
           } catch( Exception re ) {
                System.out.println(re.toString());	   
           } 
           break;           
       }
       
    }   
    dialog.setVisible(false);
    dialog.dispose();
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
// TODO add your handling code here:
    System.out.println("начинаем отработку"+typeofExport);
    jButton2.setEnabled(false);
    switch(type) {
       case 2: {
           try {
                if (DMI.getDictionary()!=null) {
                    if (DMI.getType()==2) {
                        Integer Key=Server.getKey();
                        DataModuleInterface newDMI = Server.newDMI("Новый блок данных",Key,3,true);
                        DMI.addElement(Key,parent.getLibrary().getID(),true);
                        newDMI.setDictionary(DMI.getDictionary(),parent.getLibrary().getID(),true);
                        newDMI.createTable();
                        DataLoader2 DL2=new DataLoader2(newDMI,file);
                    } 
                    
                }    
           } catch( Exception re ) {
                System.out.println(re.toString());	   
           } 
           break;
       }
       case 1: {
           break;           
       }
       case 3: {
           //new DictionaryLoader(Server,file);
           new DataLoader2(DMI,file);
           //parent.CheckDictionaryTree();
           break;           
       }
       
    }   
    dialog.setVisible(false);
    dialog.dispose();
}//GEN-LAST:event_jButton4ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
// TODO add your handling code here:
        System.out.println("начинаем отработку"+typeofExport);
    jButton2.setEnabled(false);
    switch(type) {
       case 3: {
           //new DictionaryLoader(Server,file);
           new DataLoader3(DMI);
           //parent.CheckDictionaryTree();
           break;           
       }
       
    }   
    dialog.setVisible(false);
    dialog.dispose();
}//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
// TODO add your handling code here:
    //загрузка из Парчи
    System.out.println("начинаем отработку по Парче"+typeofExport);
    jButton2.setEnabled(false);
    switch(type) {
       case 2: {
           try {
                if (DMI.getDictionary()!=null) {
                    if (DMI.getType()==2) {
                        Integer Key=Server.getKey();
                        DataModuleInterface newDMI = Server.newDMI("Новый блок данных для загрузки из парчи",Key,3,true);
                        DMI.addElement(Key,parent.getLibrary().getID(),true);
                        newDMI.setDictionary(DMI.getDictionary(),parent.getLibrary().getID(),true);
                        newDMI.createTable();
                        DataLoaderXML DL3=new DataLoaderXML(newDMI,file);
                    } 
                    
                }    
           } catch( Exception re ) {
                System.out.println(re.toString());	   
           } 
           break;
       }
       case 1: {
           break;           
       }
       case 3: {
           //new DictionaryLoader(Server,file);
           new DataLoaderXML(DMI,file);
           //parent.CheckDictionaryTree();
           break;           
       }
       
    }   
    dialog.setVisible(false);
    dialog.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
