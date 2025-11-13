/*
 * QuestMarkTransferable.java
 *
 * Created on 11 Сентябрь 2006 г., 19:40
 */
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Ivan
 */
public class QuestMarkTransferable implements Transferable, ClipboardOwner {
    DataFlavor localArrayListFlavor;
    String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMarkTransferable";
    private QuestionInterface QI;
    private QuestMark QM;
    private ArrayList QIArray;
    public DataFlavor[] DF;
    public Object Source;
    
    /** Creates a new instance of QuestMarkTransferable */
    public ArrayList GetArray() {
        return QIArray;
    }
    
    public void setSource (Object aSource){
        Source=aSource;
    }
    
    public Object getSource (){
        return Source;
    }
    
    public QuestMarkTransferable(ArrayList aQIArray) {
        QIArray=aQIArray;
        try {
            DF = new DataFlavor[2];
            DF[0] = new DataFlavor(localArrayListType);
            DF[1] = DataFlavor.stringFlavor;
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }
     public Object getTransferData(DataFlavor flavor)  throws UnsupportedFlavorException, IOException
    {
	if (flavor.equals(DF[1])) {
            String Res="";
            for (int i = 0; i < QIArray.size(); i++) {
                QM=(QuestMark)QIArray.get(i);
                QI=QM.getQI();
                Res=Res+QI.getName()+"\n";
                for (int j = 0; j < QI.getSize(); j++) {
                }
            }
            return Res;
	} else if (flavor.equals(DF[0])) {
	    return this;
	} else {
	    throw new UnsupportedFlavorException(flavor);
	}
    }
    
    public DataFlavor[] getTransferDataFlavors() {
        //DataFlavor DF = new DataFlavor(DataFlavor.javaRemoteObjectMimeType);
        return DF;
    }
    
    public boolean isDataFlavorSupported(DataFlavor aDF) {
        for (int i = 0; i < DF.length; i++) {
            if (aDF.equals(DF[i])) {
                return true;
            }
        }
        return false;
    }
    
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
