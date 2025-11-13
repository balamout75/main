import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;



public class DQATreeCellEditor extends DefaultTreeCellEditor {
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
    LibraryInterface Lib;

    //DQATreeCellRenderer      renderer;
    //DQATreeCellRenderer      renderer;

    public DQATreeCellEditor(JTree tree, LibraryInterface Lib) {
        this(tree, (DefaultTreeCellRenderer)tree.getCellRenderer(), null, Lib);
    }

    public DQATreeCellEditor(JTree tree,  DQATreeCellRenderer renderer, LibraryInterface Lib) {
        this(tree, renderer, null, Lib);
    }

    public DQATreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer,TreeCellEditor editor, LibraryInterface aLib) {
        super(tree,renderer,editor);
        Lib=aLib;
    }

   public Component getTreeCellEditorComponent(JTree tree, Object value1,
						boolean isSelected,
						boolean expanded,
						boolean leaf, int row) {
        setTree(tree);
        lastRow = row;
        ABSortTreeNode node = (ABSortTreeNode) value1;
        DQAMarkInterface DQA = (DQAMarkInterface) node.getUserObject();
        try {
            Object value = (Object)DQA.getName();
            determineOffset(tree, value, isSelected, expanded, leaf, row);
            if (editingComponent != null) {
                editingContainer.remove(editingComponent);
            }
            editingComponent = realEditor.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
        } catch (Exception ex) {
        }
        TreePath        newPath = tree.getPathForRow(row);
        canEdit = (lastPath != null && newPath != null &&
		lastPath.equals(newPath));
        Font            font = getFont();

        if(font == null) {
            if(renderer != null)
            font = renderer.getFont();
            if(font == null) font = tree.getFont();
        }
        editingContainer.setFont(font);
        prepareForEditing();
        return editingContainer;
    }
   
    public Object getCellEditorValue() {
	//return realEditor.getCellEditorValue();
        DQAMarkInterface DQA=null;
        try {
            DefaultMutableTreeNode node = (ABSortTreeNode) tree.getLastSelectedPathComponent();
            DQA = (DQAMarkInterface)node.getUserObject();
            DQA.setName(realEditor.getCellEditorValue().toString(),Lib.getID(),true);
        } catch (Exception ex) {
            System.out.println("Ошибка в именовании "+ex);
        }
        return DQA;
    }
   protected void determineOffset(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {

        if(renderer != null) {
            try {
             DQAMarkInterface DQA=(DQAMarkInterface)((ABSortTreeNode) tree.getLastSelectedPathComponent()).getUserObject();
             switch (DQA.getType()) {
                case 1:{
                    editingIcon=Icon1;
                    break;
                } case 2:{
                    editingIcon=Icon2;
                    break;
                } case 3:{
                    editingIcon=Icon3;
                    break;
                } case 4:{//datafilter
                    editingIcon=Icon4;
                    break;
                } case 5: { //datafil
                    editingIcon=Icon5;
                    break;
                } case 6: {//D
                    editingIcon=Icon6;
                    break;
                } case 7: {//Q
                    QuestMark QM = (QuestMark)DQA;
                    try {
                        switch (QM.getQI().getQuestionType()) {
                                case 1:{
                                    editingIcon=IconQ1;
                                    break;
                                }
                                case 2:{
                                    editingIcon=IconQ2;
                                    break;
                                }
                                case 3:{
                                    editingIcon=IconQ3;
                                    break;
                                }
                        }
                    } catch (Exception E) {
                            editingIcon=IconQ1;
                            System.out.println("Ошибка в назначении иконки"+E);
                    }
                    break;
                } case 8: {//A
                    editingIcon=Icon8;
                    break;
                }
             }
            } catch (Exception E) {
                System.out.println("Ошибка в назначении иконки 2"+E);
            }
            if(editingIcon != null)
                offset = renderer.getIconTextGap() +
		        editingIcon.getIconWidth();
            else
                offset = renderer.getIconTextGap();
        }	else {
                editingIcon = null;
                offset = 0;
        }
    }
   }
