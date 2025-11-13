import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.ImageIcon;

//class AnswerCheckBoxNodeRenderer extends EditTreeCellRenderer implements TreeCellRenderer {
class AnswerCheckBoxNodeRenderer implements TreeCellRenderer {
  private JCheckBox leafRenderer = new JCheckBox();
  private CheckBoxAnswerMark AM=null;
  public  boolean rendereractioninited=false;
  ImageIcon Icon1 = new ImageIcon("images/gif/05.gif");
  ImageIcon Icon2 = new ImageIcon("images/gif/06.gif");
  ImageIcon Icon3 = new ImageIcon("images/gif/07.gif");

  private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

  Color selectionBorderColor, selectionForeground, selectionBackground,
      textForeground, textBackground;

  protected JCheckBox getLeafRenderer() {
    return leafRenderer;
  }

  public CheckBoxAnswerMark getAM() {
    return AM;
  }

  public AnswerCheckBoxNodeRenderer() {
    Font fontValue;
    fontValue = UIManager.getFont("Tree.font");
    if (fontValue != null) {
      leafRenderer.setFont(fontValue);
    }
    Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
    leafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));
    selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
    selectionForeground = UIManager.getColor("Tree.selectionForeground");
    selectionBackground = UIManager.getColor("Tree.selectionBackground");
    textForeground = UIManager.getColor("Tree.textForeground");
    textBackground = UIManager.getColor("Tree.textBackground");
    //ImageIcon Icon3 = new ImageIcon("images/gif/10.gif");
  }

  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {

    Component returnValue;
    nonLeafRenderer.setIcon(Icon3);
    nonLeafRenderer.setLeafIcon(Icon3);
    nonLeafRenderer.setOpenIcon(Icon3);
    nonLeafRenderer.setClosedIcon(Icon3);
    //leafRenderer.setIcon(Icon1);
    if (leaf) {
      String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
      leafRenderer.setText(stringValue);
      leafRenderer.setSelected(false);

      leafRenderer.setEnabled(tree.isEnabled());
      //leafRenderer.setIcon(Icon3);

      if (selected) {
        leafRenderer.setForeground(selectionForeground);
        leafRenderer.setBackground(selectionBackground);
      } else {
        leafRenderer.setForeground(textForeground);
        leafRenderer.setBackground(textBackground);
      }

      if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value) .getUserObject();
            if (userObject instanceof CheckBoxAnswerMark) {
                CheckBoxAnswerMark node = (CheckBoxAnswerMark) userObject;
                AM=node;
                leafRenderer.setText(node.getName());
                leafRenderer.setSelected(node.isSelected());
            } else if (userObject instanceof QuestMark) {
                        QuestMark QM = (QuestMark) userObject;
                        try {
                            switch (QM.getQI().getQuestionType()) {
                                case 1:{
                                    nonLeafRenderer.setOpenIcon(Icon1);
                                    nonLeafRenderer.setClosedIcon(Icon1);
                                    break;
                                }
                                case 2:{
                                    nonLeafRenderer.setOpenIcon(Icon2);
                                    nonLeafRenderer.setClosedIcon(Icon2);
                                    break;
                                }
                                case 3:{
                                    nonLeafRenderer.setOpenIcon(Icon3);
                                    nonLeafRenderer.setClosedIcon(Icon3);
                                    break;
                                }
                            }
                        } catch (Exception E) {
                            nonLeafRenderer.setOpenIcon(Icon1);
                            nonLeafRenderer.setClosedIcon(Icon1);
                            System.out.println("Ошибка в назначении иконки"+E);
                        }
                        returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,value, selected, expanded, leaf, row, hasFocus);
                   }
      }
      returnValue = leafRenderer;
    } else {
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value) .getUserObject();
            if (userObject instanceof QuestMark) {
                QuestMark QM = (QuestMark) userObject;
                try {
                    switch (QM.getQI().getQuestionType()) {
                        case 1:{
                            nonLeafRenderer.setOpenIcon(Icon1);
                            nonLeafRenderer.setClosedIcon(Icon1);
                            break;
                        }
                        case 2:{
                            nonLeafRenderer.setOpenIcon(Icon2);
                            nonLeafRenderer.setClosedIcon(Icon2);
                            break;
                        }
                        case 3:{
                            nonLeafRenderer.setOpenIcon(Icon3);
                            nonLeafRenderer.setClosedIcon(Icon3);
                            break;
                        }

                    }
                } catch (Exception E) {
                    nonLeafRenderer.setOpenIcon(Icon1);
                    nonLeafRenderer.setClosedIcon(Icon1);
                    System.out.println("Ошибка в назначении иконки"+E);
                }
            }
        } 
        returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree,value, selected, expanded, leaf, row, hasFocus);
    }
    return returnValue;
  }
}
