/*
 * TableTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */
import javax.swing.*;
//import javax.swing.table.*;
//import java.awt.*;
import java.awt.datatransfer.*;
import java.util.*;

public class TableTransferHandler1 extends StringTransferHandler1 {
    private int[] rows = null;
    private ArrayList QIArray=new ArrayList();
    private QuestMarkTransferable QMT;
    private DataFlavor MyFlavor;

    protected QuestMarkTransferable exportString(JComponent c) {
        QIArray.clear();
        QuestMark val=null;
        JTable table = (JTable)c;
        rows = table.getSelectedRows();
        String Res=null;
        for (int i = 0; i < rows.length; i++) {
                QIArray.add((QuestMark)table.getValueAt(rows[i], 2));
        }
        QMT=new QuestMarkTransferable(QIArray);
        QMT.setSource(c);
        return QMT;
    }

    protected void importString(JComponent c, Transferable t) {
        /*
        JTable target = (JTable)c;
        DefaultTableModel model = (DefaultTableModel)target.getModel();
        int index = target.getSelectedRow();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving rows #4,#5,#6 and #7 and
        //attempts to insert the rows after row #5, this would
        //be problematic when removing the original rows.
        //So this is not allowed.
        if (rows != null && index >= rows[0] - 1 &&
              index <= rows[rows.length - 1]) {
            rows = null;
            return;
        }

        int max = model.getRowCount();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        String[] values = str.split("\n");
        addCount = values.length;
        int colCount = target.getColumnCount();
        for (int i = 0; i < values.length && i < colCount; i++) {
            model.insertRow(index++, values[i].split(","));
        }*/
    }
    protected void cleanup(JComponent c, boolean remove) {
        /*JTable source = (JTable)c;
        if (remove && rows != null) {
            DefaultTableModel model =
                 (DefaultTableModel)source.getModel();

            //If we are moving items around in the same table, we
            //need to adjust the rows accordingly, since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i] > addIndex) {
                        rows[i] += addCount;
                    }
                }
            }
            for (int i = rows.length - 1; i >= 0; i--) {
                model.removeRow(rows[i]);
            }
        }
        rows = null;
        addCount = 0;
        addIndex = -1;*/
    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMarkTransferable";
        try {
            MyFlavor=new DataFlavor(localArrayListType);
            for (int i = 0; i < flavors.length; i++) {
                if (DataFlavor.stringFlavor.equals(flavors[i])) {
                return true;}
            }
        }  catch( Exception re ) {
            System.out.println(re.toString());	   
        }
        return false;
    }
}
