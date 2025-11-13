/*
 * InputTableModel.java
 *
 * Created on 28 Июль 2004 г., 10:25
 */
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
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

/**
 *
 * @author  riac
 */
public class InputTableModel extends AbstractTableModel {
    private boolean DEBUG = true;
    private DictionaryInterface Dictionary;
    

    
    /** Creates a new instance of InputTableModel */
        final String[] columnNames = {"First Name", 
                                      "Last Name",
                                      "Sport"};
        final Object[][] data = {
            {"Mary", "Campione", 
             "Snowboarding", new Integer(5), new Boolean(false)},
            {"Alison", "Huml", 
             "Rowing", new Integer(3), new Boolean(true)},
            {"Kathy", "Walrath",
             "Chasing toddlers", new Integer(2), new Boolean(false)},
            {"Sharon", "Zakhour",
             "Speed reading", new Integer(20), new Boolean(true)},
            {"Angela", "Lih",
             "Teaching high school", new Integer(4), new Boolean(false)}
        };
        public final Object[] longValues = {"Angela", "Andrews","Teaching high school",new Integer(20), Boolean.TRUE};
        JComboBox CB = new JComboBox(longValues);
       
        public InputTableModel(DictionaryInterface aDictionary)  throws RemoteException {
            Dictionary = aDictionary;
            System.out.println(Dictionary.getName());
        }
        
        public int getColumnCount() {
            return 4;
        }
        
        public int getRowCount() {
            /*while (i<Dictionary.GetQuestSize()) {
                System.out.println(i+   Dictionary.GetQuestion(i).GetName());
                listQuestion.addElement(Dictionary.GetQuestion(i).GetName());
                i++;
            };*/
            int res=0;
            try {
                res = Dictionary.getSize();
            } catch( Exception re ) {
                System.out.println(re.toString());	   
            }
            return res;
            //return 3;
        }

        /*public String getColumnName(int col) {
            return columnNames[col];
        }*/

        public Object getValueAt(int row, int col) {
            String Num = "1";
            Object O=null;
            try {
                if (col==0) { O = "1";} else {
                if (col==1) { O = new Integer(row);} else {
                //if (col==2) { O = Dictionary.GetQuestion(row).GetName();} 
                if (col==2) { O = "Переделаю позже";} 
                
                if (col==3) { O = "";} 
                }}
            } catch( Exception re ) {
                System.out.println(re.toString());	   
            }
            return O;
            //return CB;
            
            
        }
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 3) { 
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            
        }
        
    }
    

