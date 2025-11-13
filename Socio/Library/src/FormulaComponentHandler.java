
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.table.*;
import java.util.*;

public class FormulaComponentHandler extends StringTransferHandler1 {
//public class TableTransferHandler1 extends StringTransferHandler1 {
    private int[] rows = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    private ArrayList QIArray=new ArrayList();
    private QuestMarkTransferable QMT;
    private int[] indices = null;
    private String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=AnswerMarkTransferable";
    private DataFlavor MyFlavor;
            
    //Bundle up the selected items in the list
    //as a single string, for export.
    
    protected QuestMarkTransferable exportString(JComponent c) {
        return null;
    }
    
    protected void importString(JComponent c, Transferable transferable) {
        JTextField list = (JTextField)c;
        try {
            MyFlavor=new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +";class=AnswerMarkTransferable");
            if (true){
                AnswerMarkTransferable TB = (AnswerMarkTransferable) transferable.getTransferData(MyFlavor) ;
                AnswerMark AM=TB.GetAnswerMark();
                if (AM.RAI!=null) list.setText(list.getText()+" @"+(AM.getRQI().getPos()+1)+"{"+(AM.RAI.getPos()+1)+"}");
                else list.setText(list.getText()+" @"+(AM.getRQI().getPos()+1)+"{0}");
            } else{
                System.out.println("“ип не тот");
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }

    protected void cleanup(JComponent c, boolean remove) {
        /*if (remove && indices != null) {
            JList source = (JList)c;
            DefaultListModel model  = (DefaultListModel)source.getModel();
            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly, since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }
            for (int i = indices.length - 1; i >= 0; i--) {
                model.remove(indices[i]);
            }
        }
        indices = null;
        addCount = 0;
        addIndex = -1;*/
    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        //String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=AnswerMarkTransferable";
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

