
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.table.*;
import java.util.*;

public class SocioInputListHandler extends StringTransferHandler1 {
//public class TableTransferHandler1 extends StringTransferHandler1 {
    private int[] rows = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    private ArrayList QIArray=new ArrayList();
    private QuestMarkTransferable QMT;
    private int[] indices = null;
    private DataFlavor MyFlavor;
            
    //Bundle up the selected items in the list
    //as a single string, for export.
    
    protected QuestMarkTransferable exportString(JComponent c) {
        QIArray.clear();
        JList list = (JList)c;
        indices = list.getSelectedIndices();
        Object[] values = list.getSelectedValues();
        for (int i = 0; i < values.length; i++) {
            Object val = values[i];
            QIArray.add(val);
        }
        QMT=new QuestMarkTransferable(QIArray);
        QMT.setSource(c);
        return QMT;
    }
    
    protected void importString(JComponent c, Transferable transferable) {
        QuestListExp list = (QuestListExp)c;
        try {
            if (transferable.isDataFlavorSupported (MyFlavor)){
                QuestMarkTransferable TB = (QuestMarkTransferable) transferable.getTransferData(MyFlavor) ;
                JComponent list1=(JComponent)TB.getSource();
                if (TB.getSource().equals(c)) {
                    DefaultListModel listModel = (DefaultListModel)list.getModel();
                    int index = list.getSelectedIndex();
                    //QuestMark QM = (QuestMark) TB.GetArray().get(0);
                    QuestListContent QM = (QuestListContent)  TB.GetArray().get(0);
                    //QuestionInterface QI= list.getMainDict().getQuestion(QM.getID());
                    if (index<indices[0]) {
                        listModel.add(index,QM);
                        listModel.remove(indices[0]+1);
                        list.setSelectedIndex(index);
                    } else {
                        listModel.remove(indices[0]);
                        listModel.add(index,QM);
                        list.setSelectedIndex(index);
                    }
                    //listModel.add(index, values[i]
                } else {
                    for (int i = 0; i < TB.GetArray().size(); i++) {
                        QuestMark QM = (QuestMark) TB.GetArray().get(i);
                        QuestionInterface QI= QM.getQI();
                        RInterface RQI = list.getDict().addElement(QI.getID(),0,true);
                        QuestListContent QLC=new QuestListContent(RQI,QM.getQI());
                        list.addElement(QLC);
                    }    
                }
            } else{
                System.out.println("Тип не тот");
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

