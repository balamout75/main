/*
 * TransferBlock.java
 *
 * Created on 22 јпрель 2005 г., 9:19
 */

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
//import java.awt.*; 
/**
 *
 * @author riac
 */
public class TransferBlock implements Transferable {
    private QuestionInterface QI;
    public DataFlavor[] DF;
    
    /** Creates a new instance of TransferBlock */
    public TransferBlock(QuestionInterface aQI) {
        QI = aQI;
        try {
            //DF[1] =new DataFlavor(DataFlavor.javaRemoteObjectMimeType);
            DF = new DataFlavor[1];
            DF[0] =new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }
    public QuestionInterface GetQuestion() {
        return QI;
    }
    
    public Object getTransferData(DataFlavor DFi) throws UnsupportedFlavorException,IOException {
        if (!DFi.equals(DF[0])) throw new UnsupportedFlavorException(DFi);
        return this;
    }
    
    public DataFlavor[] getTransferDataFlavors() {
        //DataFlavor DF = new DataFlavor(DataFlavor.javaRemoteObjectMimeType);
        return DF;
    }
    
    public boolean isDataFlavorSupported(DataFlavor aDF) {
        boolean ss = aDF.equals(DF[0]);
        return ss;
    }
    
}
