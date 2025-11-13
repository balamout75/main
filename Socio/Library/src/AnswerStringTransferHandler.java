/*
 * StringTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */
import java.awt.datatransfer.*;
import javax.swing.*;
import java.io.IOException;

public abstract class AnswerStringTransferHandler extends TransferHandler {
    protected abstract AnswerMarkTransferable exportString(JComponent c);
    protected abstract void importString(JComponent c, Transferable t);
    protected abstract void cleanup(JComponent c, boolean remove);
    public abstract boolean canImport(JComponent c, DataFlavor[] flavors);
    protected Transferable createTransferable(JComponent c) {
        //return new StringSelection(exportString(c));
        return exportString(c);
    }
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    
    public boolean importData(JComponent c, Transferable t) {
        try {
            importString(c, t);
            return true;
        } catch (Exception ioe) {
        }
        return false;
    }
    
    protected void exportDone(JComponent c, Transferable data, int action) {
        cleanup(c, action == MOVE);
    }
    
    /*
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        
        for (int i = 0; i < flavors.length; i++) {
            if (DataFlavor.stringFlavor.equals(flavors[i])) {
                //return true;
            }
        }
        return true;
    }*/
}
