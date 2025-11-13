
import javax.swing.*;
import java.awt.datatransfer.*;
import java.util.*;

public class DictionaryListHandler extends StringTransferHandler1 {
//public class TableTransferHandler1 extends StringTransferHandler1 {
    private ArrayList QIArray=new ArrayList();
    private DictionaryMarkTransferable DMT;
    private int[] indices = null;
    private DataFlavor MyFlavor;
            
    //Bundle up the selected items in the list
    //as a single string, for export.
    
    protected DictionaryMarkTransferable exportString(JComponent c) {
        QIArray.clear();
        JList list = (JList)c;
        indices = list.getSelectedIndices();
        Object[] values = list.getSelectedValues();
        for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            QIArray.add(val);
        }
        DMT=new DictionaryMarkTransferable(QIArray);
        DMT.setSource(c);
        return DMT;
    }
    
    protected void importString(JComponent c, Transferable transferable) {
        //QuestListExp list = (QuestListExp)c;
        JList list = (JList)c;
        try {
            if (transferable.isDataFlavorSupported (MyFlavor)){
                DictionaryMarkTransferable TB = (DictionaryMarkTransferable) transferable.getTransferData(MyFlavor) ;
                JComponent list1=(JComponent)TB.getSource();
                DictionaryMark DM = (DictionaryMark) TB.GetArray().get(0);
                DictionaryInterface DI= DM.getQI();
                DictionaryListContent QLC=new DictionaryListContent(DI);
                DefaultListModel listModel = (DefaultListModel)list.getModel();
                listModel.clear();
                listModel.addElement(QLC);
            }    
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }

    protected void cleanup(JComponent c, boolean remove) {

    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=DictionaryMarkTransferable";
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