/*
 * TableTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */
import javax.swing.*;
//import javax.swing.table.*;
//import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.event.TreeSelectionListener;
//import javax.swing.event.TreeSelectionEvent;
//import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;
import java.util.*;

public class TreeTransferHandler extends StringTransferHandler1 {
    private ArrayList QIArray=new ArrayList();
    private JTree source;
    public  Library Lib;
    private DataFlavor MyFlavor;
    
    
    protected Transferable exportString(JComponent c) {
        System.out.println("Что то драгает");
        Transferable transferable=null;
        try {
            source = (JTree)c;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) source.getLastSelectedPathComponent();
            DQAMarkInterface nodeInfo = (DQAMarkInterface)node.getUserObject();
            int i=nodeInfo.getType();
            //Transferable transferable=null;
            QIArray.clear();
            switch (i) {
                case 1: {
                    DataModuleMark DM = (DataModuleMark)nodeInfo;
                    QIArray.add(DM);
                    DataModuleTransferable mytransferable=new DataModuleTransferable(QIArray);
                    mytransferable.setSource(source);
                    System.err.println("Что то дропает 1 "+DM.toString());
                    transferable=mytransferable;
                    break;
                }
                case 2: {
                    DataModuleMark DM = (DataModuleMark)nodeInfo;
                    QIArray.add(DM);
                    DataModuleTransferable mytransferable=new DataModuleTransferable(QIArray);
                    mytransferable.setSource(source);
                    System.err.println("Что то дропает 2 "+DM.toString());
                    transferable=mytransferable;
                    break;
                }
                case 6: {
                    DictionaryMark DM = (DictionaryMark)nodeInfo;
                    QIArray.add(DM);
                    DictionaryMarkTransferable mytransferable=new DictionaryMarkTransferable(QIArray);
                    mytransferable.setSource(source);
                    System.err.println("Что то дропает 6 "+DM.toString());
                    transferable=mytransferable;
                    break;
                }
                case 7: {
                    QuestMark DM = (QuestMark)nodeInfo;
                    QIArray.add(DM);
                    QuestMarkTransferable mytransferable=new QuestMarkTransferable(QIArray);
                    mytransferable.setSource(source);
                    System.err.println("Что то дропает 7 "+DM.toString());
                    transferable=mytransferable;
                    break;
                }
                case 8: {
                    AnswerMark DM = (AnswerMark)nodeInfo;
                    //QIArray.add(DM);
                    AnswerMarkTransferable mytransferable=new AnswerMarkTransferable(DM);
                    mytransferable.setSource(source);
                    System.err.println("Что то дропает 8 "+DM.toString());
                    transferable=mytransferable;
                    break;
                }
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }  
        /*QIArray.clear();
        JTree tree = (JTree)c;
        rows=tree.getSelectionPaths();
        for (int i = 0; i < rows.length; i++) {
            //DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree. getLastSelectedPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rows[i].getLastPathComponent();
            Obj = node.getUserObject();
            //if (Obj.getClass().equals(new QuestMark(null).getClass())) {
            //if (Obj.getClass() equals(QuestMark.class)) {
            try {
                QIArray.add((QuestMark)Obj);
            } catch (Exception exception) {
                System.err.println( "Ошибка типов " + exception.getMessage());
            }
        }
        QMT=new QuestMarkTransferable(QIArray);
        QMT.setSource(c);
        return QMT;*/
        return transferable;
    }

    private void  FindAndRemove(DataModuleInterface t,DataModuleInterface root){
        try {
            int i=0;
            while (i<root.getSize()) {
                RInterface RDMI=root.getByPos(new Integer(i));
                DataModuleInterface DMI=Lib.Server.getDMI(RDMI.getID());
                if (DMI.equals(t)) 
                    root.delByPos(new Integer(i),Lib.getID(),true);
                //else FindAndRemove(t,DMI);
                i++;
            };
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }

    protected void changePos (JTree tree, DefaultMutableTreeNode node1, DefaultMutableTreeNode node2) {
       DQAMarkInterface nodeInfo1 = (DQAMarkInterface)node1.getUserObject();
       DQAMarkInterface nodeInfo2 = (DQAMarkInterface)node2.getUserObject();
       try {
        if (nodeInfo1.getType()<=2) {
            //DataModuleMark DMM = (DataModuleMark)nodeInfo1;
            DefaultMutableTreeNode OldParent=Lib.findBranch((DefaultMutableTreeNode)tree.getModel().getRoot(),nodeInfo1.getRContent().getID(),true);
            if (OldParent.equals((DefaultMutableTreeNode)node1.getParent())) {
                int j=0;
                int i=0;
                boolean Flag=true;
                javax.swing.tree.DefaultMutableTreeNode child=null;
                javax.swing.tree.DefaultMutableTreeNode root=(javax.swing.tree.DefaultMutableTreeNode)node1.getParent();
                while ((j<root.getChildCount())&Flag) {
                    child=(javax.swing.tree.DefaultMutableTreeNode)root.getChildAt(j);
                    DataModuleMark DM2 = (DataModuleMark)child.getUserObject();
                    Flag=(DM2.getID().compareTo(nodeInfo1.getRContent().getID())!=0);
                    j++;
                }
                if (!Flag) {
                    //i=root.getIndex(child); // откуда
                    i=root.getIndex(node2); // откуда
                    j=root.getIndex(node1);  // куда
                    javax.swing.tree.DefaultTreeModel DTM=(javax.swing.tree.DefaultTreeModel)tree.getModel();
                    javax.swing.tree.DefaultMutableTreeNode parent=(javax.swing.tree.DefaultMutableTreeNode)child.getParent();
                    DataModuleMark DM1=(DataModuleMark)parent.getUserObject();
                    if (i<j) {
                        DTM.removeNodeFromParent(node2);
                        DTM.insertNodeInto(node2, root,j);
                        tree.setSelectionPath(new TreePath(node2.getPath()));
                        DM1.setMixed(true);
                    } else {
                        DTM.removeNodeFromParent(node2);
                        DTM.insertNodeInto(node2, root,j);
                        tree.setSelectionPath(new TreePath(node2.getPath()));
                        DM1.setMixed(true);
                    }
                }
            }

        }
       }catch( Exception re ) {
            System.out.println(re.toString());
       }
    }

    protected void importString(JComponent c, Transferable transferable) {

        System.err.println("Что то дропает");
        JTree tree = (JTree)c;
        try {
            if (true){
                int MessageType=0;
                DataFlavor DF=transferable.getTransferDataFlavors()[0];
                String localArrayListType4 = DataFlavor.javaJVMLocalObjectMimeType +";class=DataModuleTransferable";
                DataFlavor DataModuleFlavor=new DataFlavor(localArrayListType4);
                if (DataModuleFlavor.equals(DF)) {
                    MessageType=1;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                DQAMarkInterface nodeInfo = (DQAMarkInterface)node.getUserObject();
                switch (MessageType) {
                        case 1: { /*datafolder*/
                            Object Obj=transferable.getTransferData(DataModuleFlavor);
                            DataModuleTransferable DMTr=(DataModuleTransferable) Obj;
                            DataModuleMark DIfo=(DataModuleMark)DMTr.GetArray().get(0);
                            switch (DIfo.getType()) {
                                case 1: { // была ухвачена папка
                                    switch (nodeInfo.getType()) {
                                        case 1: { // папка положена в папку
                                            Object[] options = {"Да","Нет","Отмена"};
                                            int n = JOptionPane.showOptionDialog(null,"Вы желаете перенести папку?","Вопрос",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
                                            //if (DIfo.getParent().equals(((DataModuleMark)nodeInfo).getParent())) { //если родители папки одинаковые
                                            /*if (n==1) { //если родители папки одинаковые
                                                DefaultMutableTreeNode newnode=  Lib.findBranch((DefaultMutableTreeNode)tree.getModel().getRoot(),DIfo.getID(),false);
                                                changePos(tree,node,newnode);
                                            } else */
                                            if (n==0) { //если нет то переписываем
                                                    DefaultMutableTreeNode OldParent=Lib.findBranch((DefaultMutableTreeNode)tree.getModel().getRoot(),DIfo.getID(),true);
                                                    DefaultMutableTreeNode OldNode=  Lib.findBranch((DefaultMutableTreeNode)tree.getModel().getRoot(),DIfo.getID(),false);
                                                    DataModuleInterface OldFolder=((DataModuleMark)OldParent.getUserObject()).getDMI();
                                                    if (Lib.findBranch(OldNode,((DataModuleMark)nodeInfo).getDMI().getID(),true)==null) {
                                                        DataModuleInterface NewFolder = ((DataModuleMark) nodeInfo).getDMI();
                                                        NewFolder.addElement(DIfo.getDMI().getID(),Lib.getID(),true);
                                                        Lib.refreshBranch(node,2);
                                                        //tree.expandPath(tree.getLeadSelectionPath());
                                                        FindAndRemove(DIfo.getDMI(),OldFolder);
                                                        Lib.refreshBranch(OldParent,1);
                                                        tree.expandPath(tree.getLeadSelectionPath());
                                                        tree.updateUI();
                                                    }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case 2: { // была ухвачен опрос
                                      switch (nodeInfo.getType()) {
                                        case 1: { // папка положена в папку
                                            DefaultMutableTreeNode OldParent=Lib.findBranch((DefaultMutableTreeNode)tree.getModel().getRoot(),DIfo.getID(),true);
                                            DefaultMutableTreeNode OldNode=  Lib.findBranch((DefaultMutableTreeNode)tree.getModel().getRoot(),DIfo.getID(),false);
                                            DataModuleInterface OldFolder=((DataModuleMark)OldParent.getUserObject()).getDMI();
                                            if (Lib.findBranch(OldNode,((DataModuleMark)nodeInfo).getDMI().getID(),true)==null) {
                                                DataModuleInterface NewFolder = ((DataModuleMark) nodeInfo).getDMI();
                                                NewFolder.addElement(DIfo.getDMI().getID(),Lib.getID(),true);
                                                Lib.refreshBranch(node,2);
                                                tree.expandPath(tree.getLeadSelectionPath());
                                                FindAndRemove(DIfo.getDMI(),OldFolder);
                                                Lib.refreshBranch(OldParent,1);
                                            }

                                            break;
                                        }
                                   }
                                    break;
                                }
                            }
                        }
                }
            } else{
                System.out.println("Тип не тот");
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
    }
    protected void cleanup(JComponent c, boolean remove) {
        /*
        JTable source = (JTable)c;
        if (remove && rows != null) {
            DefaultTableModel model =
                 (DefaultTableModel)source.getModel();
            if (addCount > 0) {
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i] > addIndex) {
                        rows[i] += addCount;
                    }
                }
            }
            for (int i = rows.length - 1; i >= 0; i--) {
                model.removeRow(rows[i]);
            }
        }
        rows = null;
        addCount = 0;
        addIndex = -1;
        */
    }
    
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        //String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMarkTransferable";
        try {
            //MyFlavor=new DataFlavor(localArrayListType);
            for (int i = 0; i < flavors.length; i++) {
                if (DataFlavor.stringFlavor.equals(flavors[i])) {
                return true;}
            }
        }  catch( Exception re ) {
            System.out.println(re.toString());	   
        }
        return false;
    }
}
