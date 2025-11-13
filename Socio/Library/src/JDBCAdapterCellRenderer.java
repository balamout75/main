//import javax.swing.BorderFactory;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Component;

public class JDBCAdapterCellRenderer extends DefaultTableCellRenderer
                           implements TableCellRenderer {
    ImageIcon IconQ1 = new ImageIcon("images/gif/05.gif");
    ImageIcon IconQ2 = new ImageIcon("images/gif/06.gif");
    ImageIcon IconQ3 = new ImageIcon("images/gif/07.gif");
    DefaultTableCellRenderer renderer=new DefaultTableCellRenderer();

    DictionaryInterface     Dictionary;
    INIACRMIInterface       Server;
    Color Color1 = new Color(255,255,255);
    Color Color2 = new Color(248,181,255);
    Color Color3 = new Color(171,127,255);
    Color Color4 = new Color(0,0,0);
    Color Color5 = new Color(255,0,255);
    Color Color6 = new Color(0,0,255);

    public JDBCAdapterCellRenderer(INIACRMIInterface aServer, DictionaryInterface aDictionary) {
        Dictionary  =   aDictionary;
        Server      =   aServer;
        setOpaque(true); //MUST do this for background to show up.
    }

    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {
        if (isSelected) {
          return renderer.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
        } else {
          try {
            RInterface RQI=Dictionary.getByPos(new Integer(row));
            QuestionInterface QI = Server.getMainDictionary().getQuestion(RQI.getID()) ;
            switch(QI.getQuestionType()) {
            case 1: {this.setForeground(Color4); break;}
            case 2: {this.setForeground(Color5); break;}
            case 3: {this.setForeground(Color6); break;}
            }
            this.setText(value.toString());
          } catch (Exception ex) {
            System.err.println("Что то глюк в JDBCAdapterCellRenderer "+ex);
          }
          return this;
        }
    }
}