/*
 * QuestMarkTransferable.java
 *
 * Created on 11 Сентябрь 2006 г., 19:40
 */
import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.io.*;
import java.util.*;

/**
 *
 * @author Ivan
 */
public class DictionaryMarkTransferable implements Transferable, ClipboardOwner {
    DataFlavor localArrayListFlavor;
    String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=DictionaryMarkTransferable";
    private DictionaryInterface DI;
    private DictionaryMark DM;
    private ArrayList DIArray;
    public DataFlavor[] DF;
    public Object Source;
    
    /** Creates a new instance of QuestMarkTransferable */
    public ArrayList GetArray() {
        return DIArray;
    }
    
    public void setSource (Object aSource){
        Source=aSource;
    }
    
    public Object getSource (){
        return Source;
    }
    
    public DictionaryMarkTransferable(ArrayList aDIArray) {
        DIArray=aDIArray;
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
	// JCK Test StringSelection0007: if 'flavor' is null, throw NPE
	if (flavor.equals(DF[1])) {
	    //return QI.GetName();
            String Res="";
            //Res=Res=QI.GetName()+"\n";
            for (int i = 0; i < DIArray.size(); i++) {
                DM=(DictionaryMark)DIArray.get(i);
                DI=DM.getQI();
                Res=Res+DI.getName()+"\n";
                for (int j = 0; j < DI.getSize(); j++) {
                    //Res=Res+QI.GetAnswer(j).getName()+"\n";
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
