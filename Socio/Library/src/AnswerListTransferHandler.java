import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.table.*;
import java.util.*;

public class AnswerListTransferHandler extends AnswerStringTransferHandler {
//public class TableTransferHandler1 extends StringTransferHandler1 {
    private int[] rows = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added.
    //private ArrayList QIArray=new ArrayList();
    private AnswerMarkTransferable AMT;
    private int[] indices = null;
    private DataFlavor MyFlavor;
            
    //Bundle up the selected items in the list
    //as a single string, for export.
    
    protected AnswerMarkTransferable exportString(JComponent c) {
        JList list = (JList)c;
        Object value = list.getSelectedValue();
        indices = list.getSelectedIndices();
        if (value!=null) {
            AMT=new AnswerMarkTransferable((AnswerMark)value);
        }
        else {
            AMT=null;
        }
        AMT.setSource(c);
        return AMT;
    }
      
    protected void importString(JComponent c, Transferable transferable) {
        AnswerListExp list = (AnswerListExp)c;
        try {
            if (transferable.isDataFlavorSupported (MyFlavor)){
                AnswerMarkTransferable TB = (AnswerMarkTransferable) transferable.getTransferData(MyFlavor) ;
                JComponent list1=(JComponent)TB.getSource();
                if (TB.getSource().equals(c)) {
                    DefaultListModel listModel = (DefaultListModel)list.getModel();
                    int index = list.getSelectedIndex();
                    AnswerMark AM = (AnswerMark) TB.GetAnswerMark();
                    //AnswerInterface AI= AM.getAI();
                    //list.getDict().AddQuest(QI);
                    //listModel.add(index,QM);
                    if (index<indices[0]) {
                        listModel.add(index,AM);
                        listModel.remove(indices[0]+1);
                        list.setSelectedIndex(index);
                    } else {
                        listModel.remove(indices[0]);
                        listModel.add(index,AM);
                        list.setSelectedIndex(index);
                    }
                    //listModel.add(index, values[i]
                } else {/*
                    for (int i = 0; i < TB.GetArray().size(); i++) {
                        QuestMark QM = (QuestMark) TB.GetArray().get(i);
                        QuestionInterface QI= list.getMainDict().GetQuestionID(QM.getID());
                        list.getDict().AddQuest(QI);
                        list.addElement(QI.GetName());
                    }    */
                }
            } else{
                System.out.println("“ип не тот");
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }
    
    
    protected void cleanup(JComponent c, boolean remove) {

    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=AnswerMarkTransferable";
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
