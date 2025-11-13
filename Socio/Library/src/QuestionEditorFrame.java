/*
 * QuestionEditorFrame.java
 *
 * Created on 18 Апрель 2005 г., 16:07
 */
import java.rmi.RemoteException;
/**
 *
 * @author  riac
 */
public class QuestionEditorFrame extends javax.swing.JFrame implements MainFramable {
    
    QuestionEditorPanel QEP;
    INIACRMIInterface Server;
    
    /** Creates new form QuestionEditorFrame */
    public QuestionEditorFrame(INIACRMIInterface aServer) {
        Server = aServer;
        initComponents();
        tuneComponent();
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        questionInputPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        questionInputPanel.setLayout(new java.awt.BorderLayout());

        questionInputPanel.setPreferredSize(new java.awt.Dimension(550, 300));
        getContentPane().add(questionInputPanel, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel questionInputPanel;
    // End of variables declaration//GEN-END:variables
  
    private void tuneComponent() {
        questionInputPanel=new QuestionEditorPanel(null, Server);
        java.awt.GridBagConstraints gridBagConstraints;
        getContentPane().setLayout(new java.awt.GridBagLayout());
        //jPanel1.setBorder(new javax.swing.border.TitledBorder("\u0430\u0430\u0430\u0430\u0430\u0430"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(questionInputPanel, gridBagConstraints);
    }  
    
    public String GetStringValue(String DefStr, String LabelStr) {
        String Res = DefStr;
        try {
            CustomDialog customDialog = new CustomDialog(this,DefStr,LabelStr);
            customDialog.pack();
            customDialog.setLocationRelativeTo(this);
            customDialog.setVisible(true);
            String s = customDialog.getValidatedText();
            System.out.println("Что то там ввели новое" );	
            if (s != null) Res = s;	   
            
        } catch(Exception re ) {
            System.out.println(re.toString());	   
        } 
        return Res;
    }
    
    public void setDictionary(DictionaryInterface Dictionary) throws RemoteException {
        QEP= (QuestionEditorPanel)questionInputPanel;
        QEP.setDictionary(Dictionary);
    }
    public void setServer(INIACRMIInterface aServer) throws RemoteException {
        QuestionEditorPanel QEP= (QuestionEditorPanel)questionInputPanel;
        //QEP.setDictionary(Dictionary);
    }
    
}
