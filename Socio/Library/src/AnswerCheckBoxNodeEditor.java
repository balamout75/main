import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.event.ItemEvent;


class AnswerCheckBoxNodeEditor extends AbstractCellEditor implements TreeCellEditor {

  AnswerCheckBoxNodeRenderer renderer = new AnswerCheckBoxNodeRenderer();
  CheckBoxAnswerMark checkBoxNode;
  EditorTreePanel  ETP;
  //public RInterface lastRAI=null;
  CheckBoxAnswerMark lastCheckBoxNode=null;
  QuestionInterface QI;
  //RInterface lastRAI=null;
  
  boolean          lastStatus=false;
  public    boolean alter    =false;
  public    int AnswerCount   =0;
  //CheckBoxAnswerMark CB;
  //CheckBoxNode checkBoxNode;
  //CheckBoxNode CB;

  ChangeEvent changeEvent = null;
  Component editor;
  //JCheckBox mycheckbox;

  public  JTree tree;

  public AnswerCheckBoxNodeEditor(JTree tree, EditorTreePanel ETP, boolean alter) {
    this.tree   = tree;
    this.ETP    = ETP;
    this.alter  = alter;
    QI=ETP.getQI();
  }

  public Object getCellEditorValue() {
    JCheckBox checkbox = renderer.getLeafRenderer();
    CheckBoxAnswerMark CB = renderer.getAM();
    checkBoxNode = new CheckBoxAnswerMark(CB.RAI,CB.AI,CB.RQI);
    checkBoxNode.setSelected(checkbox.isSelected());
    return checkBoxNode;
  }

  public boolean isCellEditable(EventObject event) {
    boolean returnValue = false;
    if (event instanceof MouseEvent) {
      MouseEvent mouseEvent = (MouseEvent) event;
      TreePath path = tree.getPathForLocation(mouseEvent.getX(),
          mouseEvent.getY());
      if (path != null) {
        Object node = path.getLastPathComponent();
        if ((node != null) && (node instanceof DefaultMutableTreeNode)) {
          DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
          Object userObject = treeNode.getUserObject();
          returnValue = ((treeNode.isLeaf()) && (userObject instanceof CheckBoxAnswerMark));
        }
      }
    }
    return returnValue;
  }

  public void Action() {
    try {   
        if (true) {          
          if (alter) {
            if (checkBoxNode.isSelected()) {
                if (lastCheckBoxNode!=null) lastCheckBoxNode.setSelected(false);
                System.out.println("Расселектили"+lastCheckBoxNode);
                ETP.EtpChanged(checkBoxNode);
            } else {
                ETP.EtpChanged(checkBoxNode);
            }
          } else {
            if (checkBoxNode.isSelected()) {
                if ((QI.getMaxAnswerCount()==0)|(QI.getMaxAnswerCount()>AnswerCount)) {
                    AnswerCount++;
                    ETP.EtpChanged(checkBoxNode);
                } else {
                    //if (AnswerCount>0) AnswerCount--;
                    //checkBoxNode.setSelected(false);
                    checkBoxNode.setSelected(false);
                    System.out.println("Приготовимся");
                }
            } else {
                if (AnswerCount>0) AnswerCount--;
                ETP.EtpChanged(checkBoxNode);
            }  
          }
          tree.updateUI();
          lastCheckBoxNode=checkBoxNode;
          lastStatus=checkBoxNode.isSelected();
        }
    } catch (Exception ex) {
        System.out.println("Генеральная ошибка 2"+ex);
    }  
  }
  
  public Component getTreeCellEditorComponent(JTree atree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {

    editor = renderer.getTreeCellRendererComponent(atree, value, true, expanded, leaf, row, true);
    // editor always selected / focused
    ItemListener itemListener = new ItemListener() {
      public void itemStateChanged(ItemEvent itemEvent) {
       try {   
          /*
          CheckBoxAnswerMark source = (CheckBoxAnswerMark)itemEvent.getItemSelectable();
          if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
          }*/
          
          System.out.println("событие "+itemEvent.getStateChange());
          if (stopCellEditing()) {
            
            if (lastCheckBoxNode==null) Action();
            else if (!lastCheckBoxNode.AI.equals(checkBoxNode.AI)) Action();
                 else if (lastStatus!=checkBoxNode.isSelected()) Action();
            cancelCellEditing();
          }
       } catch (Exception ex) {
            System.out.println("Генеральная ошибка 1"+ex);
       }
      }
    };
      if (!renderer.rendereractioninited) {
        if (editor instanceof JCheckBox) {
            ((JCheckBox) editor).addItemListener(itemListener);
            renderer.rendereractioninited=true;
        }
    }
    return editor;
  }
}
