import javax.swing.tree.*;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.ImageIcon;


//public class DQATreeCellRenderer implements TreeCellRenderer {
public class DQATreeCellRenderer extends DefaultTreeCellRenderer {
    ImageIcon Icon1 = new ImageIcon("images/gif/18x16/08.gif");
    ImageIcon Icon2 = new ImageIcon("images/gif/18x16/01.gif");
    ImageIcon Icon3 = new ImageIcon("images/gif/18x16/10.gif");
    ImageIcon Icon4 = new ImageIcon("images/gif/18x16/09.gif");
    ImageIcon Icon5 = new ImageIcon("images/gif/18x16/12.gif");

    ImageIcon Icon6 = new ImageIcon("images/gif/18x16/03.gif");
    ImageIcon IconQ1 = new ImageIcon("images/gif/18x16/05.gif");
    ImageIcon IconQ2 = new ImageIcon("images/gif/18x16/06.gif");
    ImageIcon IconQ3 = new ImageIcon("images/gif/18x16/07.gif");
    ImageIcon Icon8 = new ImageIcon("images/gif/18x16/04.gif");

    private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

    public DQATreeCellRenderer() {
    }


    public Component getTreeCellRendererComponent(JTree tree, Object value,
						  boolean sel,
						  boolean expanded,
						  boolean leaf, int row,
						  boolean hasFocus) {
        Component returnValue;
        ImageIcon icon=null;
        try {
            DQAMarkInterface DQA=(DQAMarkInterface)((DefaultMutableTreeNode)value).getUserObject();
            switch (DQA.getType()) {
                case 1:{
                    icon=Icon1;
                    break;
                }
                case 2:{
                    icon=Icon2;
                    break;
                }
                case 3:{
                    icon=Icon3;
                    break;
                }
                case 4:{//datafilter
                    icon=Icon4;
                    break;
                } case 5: { //datafil
                    icon=Icon5;
                    break;
                }
                case 0: {//D
                    icon=Icon6;
                    break;
                }
                case 8: {//A
                    icon=Icon8;
                    break;
                }
                case 7: {//Q
                    QuestMark QM = (QuestMark)DQA;
                    try {
                        switch (QM.getQI().getQuestionType()) {
                                case 1:{
                                    icon=IconQ1;
                                    break;
                                }
                                case 2:{
                                    icon=IconQ2;
                                    break;
                                }
                                case 3:{
                                    icon=IconQ3;
                                    break;
                                }
                            }
                    } catch (Exception E) {
                            icon=IconQ1;
                            System.out.println("Ошибка в назначении иконки 1 "+E);
                    }
                    break;
                }
               
              }
        } catch (Exception E) {
            icon=IconQ1;
            System.out.println("Ошибка в назначении иконки 2 "+E);
        }
        if (icon!=null) {
            nonLeafRenderer.setOpenIcon(icon);
            nonLeafRenderer.setClosedIcon(icon);
            nonLeafRenderer.setLeafIcon(icon);
        }
        returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,value, sel, expanded, leaf, row, hasFocus);
        return returnValue;
    }

    /**
      * Paints the value.  The background is filled based on selected.
      */
    }
