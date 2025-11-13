
import javax.swing.*;
import java.awt.datatransfer.*;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;


public class DimensionListHandler extends StringTransferHandler1 {
//public class TableTransferHandler1 extends StringTransferHandler1 {
    private DataFlavor MyFlavor;
    DimensionPanel  DP;
    boolean         Xmode;
            
    //Bundle up the selected items in the list
    //as a single string, for export.

    public DimensionListHandler(DimensionPanel aDP, boolean aXmode) {
        super();
        DP    = aDP;
        Xmode = aXmode;

    }
    
    protected QuestMarkTransferable exportString(JComponent c) {
        return null;
    }
    
    protected void importString(JComponent c, Transferable transferable) {
        JList list = (JList)c;
        try {
            if (transferable.isDataFlavorSupported (MyFlavor)){
                QuestMarkTransferable TB = (QuestMarkTransferable) transferable.getTransferData(MyFlavor) ;
                //JComponent list1=(JComponent)TB.getSource();
                //if (TB.getSource().equals(c)) {
                for (int i = 0; i < TB.GetArray().size(); i++) {
                        QuestMark QM = (QuestMark) TB.GetArray().get(i);
                        QuestionInterface QI= QM.getQI();
                        DefaultListModel LM = (DefaultListModel)list.getModel();
                        LM.clear();
                        LM.addElement(QI.getName());
                        if (Xmode) DP.RQX=QM.getRContent(); else DP.RQY=QM.getRContent();
                        DP.RefreshTable();
                    }    
            } else{
                System.out.println("Тип не тот");
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }

    protected void cleanup(JComponent c, boolean remove) {
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

