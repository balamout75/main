import javax.swing.*;
//import javax.swing.table.*;
//import java.awt.*;
import java.awt.datatransfer.*;

import javax.swing.tree.MutableTreeNode;
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.event.TreeSelectionListener;
//import javax.swing.event.TreeSelectionEvent;
//import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;
import java.util.*;

public class DataModuleTreeTransferHandler extends StringTransferHandler1 {
    //private  TreePath[] rows = null;
    private ArrayList QIArray=new ArrayList();
    private QuestMarkTransferable QMT;
    private DataFlavor MyFlavor;
    private DataModuleEditorPanel DMEP;
    private JTree source;

    public void setParent(DataModuleEditorPanel aDMEP) {
        DMEP=aDMEP;
    }

    protected Transferable exportString(JComponent c) {
        System.out.println("Что то драгает");
        Transferable transferable=null;
        try {
            source = (JTree)c;
            ABSortTreeNode node = (ABSortTreeNode)((MutableTreeNode) source.getLastSelectedPathComponent());
            DQAMarkInterface nodeInfo = (DQAMarkInterface)node.getUserObject();
            int i=nodeInfo.getType();
            //Transferable transferable=null;
            QIArray.clear();
            switch (i) {
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
        return transferable;
    }

    protected void importString(JComponent c, Transferable transferable) {
        System.err.println("Что то дропает");
        JTree tree = (JTree)c;
        //jTree list = (QuestListExp)c;
        try {
            //if (transferable.isDataFlavorSupported (MyFlavor)){
            if (true){
                int MessageType=0;
                DataFlavor DF=transferable.getTransferDataFlavors()[0];
                String localArrayListType1 = DataFlavor.javaJVMLocalObjectMimeType +";class=DictionaryMarkTransferable";
                DataFlavor DictionaryFlavor=new DataFlavor(localArrayListType1);
                String localArrayListType2 = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMarkTransferable";
                DataFlavor QuestionFlavor=new DataFlavor(localArrayListType2);
                String localArrayListType3 = DataFlavor.javaJVMLocalObjectMimeType +";class=AnswerMarkTransferable";
                DataFlavor AnswerFlavor=new DataFlavor(localArrayListType3);
                String localArrayListType4 = DataFlavor.javaJVMLocalObjectMimeType +";class=DataModuleTransferable";
                DataFlavor DataModuleFlavor=new DataFlavor(localArrayListType4);
                if (DictionaryFlavor.equals(DF)) {
                    MessageType=1;
                } else if (QuestionFlavor.equals(DF)) {
                        MessageType=2;
                    } else if (AnswerFlavor.equals(DF)) {
                            MessageType=3;
                        } else if (DataModuleFlavor.equals(DF)) {
                                MessageType=4;
                           }
                ABSortTreeNode node=null;
                DQAMarkInterface nodeInfo=null;
                try {
                    node = (ABSortTreeNode) tree.getLastSelectedPathComponent();
                    nodeInfo = (DQAMarkInterface)node.getUserObject();
                } catch( Exception re ) {
                    System.err.println("Пустое поле в пытошной "+re.toString());
                    System.err.println(re.toString());
                }
                switch (MessageType) {
                        case 1: {
                            Object Obj=transferable.getTransferData(DictionaryFlavor);
                            DictionaryMarkTransferable DMTr=(DictionaryMarkTransferable) Obj;
                            if (DMEP.DItop==null) {
                                DictionaryMark DM1=(DictionaryMark)DMTr.GetArray().get(0);
                                System.out.println("В пытошной новый словарь "+DM1.getName());
                                DictionaryInterface OldDI = DM1.getQI();
                                DictionaryInterface NewDI = DMEP.getServer().newDictionary(DM1.getName(),true);
                                int i=0;
                                while (i<OldDI.getSize()) {
                                    RInterface RQI=OldDI.getByPos(i);
                                    QuestionInterface OldQI=DMEP.getServer().getMainDictionary().getQuestion(RQI.getID());
                                    Integer Key = DMEP.getServer().getKey();
                                    QuestionInterface NewQI = DMEP.getServer().getMainDictionary().newQuestion(OldQI.getName(),Key,true);
                                    NewQI.setQuestionType(OldQI.getQuestionType(), true);
                                    NewDI.addElement(Key,DMEP.getLibrary().getID(),true);
                                    int j=0;
                                    while (j<OldQI.getSize()) {
                                        RInterface RAI=OldQI.getByPos(j);
                                        AnswerInterface AI = DMEP.getServer().getMainQuestion().getAnswer(RAI.getID());
                                        Key = DMEP.getServer().getKey();
                                        DMEP.getServer().getMainQuestion().newAnswer(AI.getName(),Key,true);
                                        NewQI.addElement(Key,DMEP.getLibrary().getID(),true);
                                        j++;
                                    }
                                    i++;
                                }
                                DMEP.setDictionary(NewDI);
                            } 
                            break;
                        }
                        case 2: {
                            System.out.println("В пытошной новый вопрос ");
                            Object Obj=transferable.getTransferData(QuestionFlavor);
                            QuestMarkTransferable DMTr=(QuestMarkTransferable) Obj;
                            if (DMTr.getSource().equals(tree)) { ////если перемещения внутренние
                              if (nodeInfo.getType()==7) {
                                QuestMark QMGuest=(QuestMark)DMTr.GetArray().get(0);
                                QuestMark QMHome=(QuestMark)node.getUserObject();
                                ABSortTreeNode root=(ABSortTreeNode)node.getParent();
                                int GuestIndex=QMGuest.getLocalPos(); // откуда
                                int HomeIndex =QMHome .getLocalPos(); // куда
                                javax.swing.tree.TreeNode[] TN = node.getPath();
                                DictionaryMark DM=(DictionaryMark)root.getUserObject();
                                DM.setMixed(true);
                                Vector Collection=new Vector();
                                if (GuestIndex>HomeIndex) {
                                    int k=HomeIndex+1;
                                    while (k<root.getChildCount()) {
                                        ABSortTreeNode CurrentBranch =(ABSortTreeNode)root.getChildAt(k);
                                        QuestMark QMCurrent=(QuestMark)CurrentBranch.getUserObject();
                                        QMCurrent.setLocalPos(k+1);
                                        Collection.add(CurrentBranch);
                                        k++;
                                    }
                                    QMGuest.setLocalPos(HomeIndex+1);
                                } else 
                                    if (HomeIndex>GuestIndex) {
                                        int k=GuestIndex+1;
                                        while (k<=HomeIndex) {
                                            ABSortTreeNode CurrentBranch =(ABSortTreeNode)root.getChildAt(k);
                                            QuestMark QMCurrent=(QuestMark)CurrentBranch.getUserObject();
                                            QMCurrent.setLocalPos(k-1);
                                            Collection.add(CurrentBranch);
                                            k++;
                                        }
                                        QMGuest.setLocalPos(HomeIndex);
                                    }
                                DMEP.getServer().StoreDQА(DM.getQI(),2);
                                root.sortChildren(Collection.toArray());
                                tree.setSelectionPath(new javax.swing.tree.TreePath(TN));
                                tree.updateUI();
                              } 
                            } else {
                                System.out.println("В пытошной вопрос из другого опроса");
                                if (nodeInfo.getType()==0) {
                                    QuestMark QM=(QuestMark)DMTr.GetArray().get(0);
                                    QuestionInterface QI = DMEP.getServer().getMainDictionary().getQuestion(QM.getID());
                                    DictionaryMark DM=(DictionaryMark)nodeInfo;
                                    Integer Key = DMEP.getServer().getKey();
                                    QuestionInterface CurrQuest=DMEP.getServer().getMainDictionary().newQuestion(QM.getName(),Key,true);
                                    CurrQuest.setQuestionType(QI.getQuestionType(), true);
                                    DM.getQI().addElement(Key,DMEP.getLibrary().getID(),true);
                                    int i=0;
                                    while (i<QI.getSize()) {
                                        RInterface RAI=QI.getByPos(i);
                                        AnswerInterface AI = DMEP.getServer().getMainQuestion().getAnswer(RAI.getID());
                                        Key = DMEP.getServer().getKey();
                                        DMEP.getServer().getMainQuestion().newAnswer(AI.getName(),Key,true);
                                        CurrQuest.addElement(Key,DMEP.getLibrary().getID(),true);
                                        i++;
                                    }
                                    DMEP.refreshBranch(node, 2);
                                } else if (nodeInfo.getType()==7) {
                                        QuestMark OldQM=(QuestMark)nodeInfo;
                                        QuestMark NewQM=(QuestMark)DMTr.GetArray().get(0);
                                        ABSortTreeNode parent=(ABSortTreeNode)node.getParent();
                                        DictionaryMark DM=(DictionaryMark)parent.getUserObject();
                                        Integer Index=OldQM.getRContent().getPos();
                                        Integer i=DM.getQI().getSize()-1;
                                        while (i>Index) {
                                            RInterface RQI=DM.getQI().getByPos(i);
                                            RQI.setPos(i+1);
                                            i--;
                                        }
                                        Integer Key = DMEP.getServer().getKey();
                                        DMEP.getServer().getMainDictionary().newQuestion(NewQM.getName(),Key,true);
                                        RInterface RQI=DM.getQI().addElement(Key,DMEP.getLibrary().getID(),false);
                                        QuestionInterface CurQI = DMEP.getServer().getMainDictionary().getQuestion(RQI.getID());
                                        QuestionInterface NewQI = DMEP.getServer().getMainDictionary().getQuestion(NewQM.getRContent().getID());
                                        RQI.setPos(Index+1);
                                        i=0;
                                        while (i<NewQI.getSize()) {
                                            RInterface RAI=NewQI.getByPos(i);
                                            AnswerInterface AI = DMEP.getServer().getMainQuestion().getAnswer(RAI.getID());
                                            Key = DMEP.getServer().getKey();
                                            DMEP.getServer().getMainQuestion().newAnswer(AI.getName(),Key,true);
                                            CurQI.addElement(Key,DMEP.getLibrary().getID(),true);
                                            i++;
                                        }
                                        DMEP.getServer().StoreDQА(DM.getQI(),2);
                                        DMEP.refreshBranch(parent, 2);
                                        //сохраняем словарь
                                    } 
                            }
                            break;
                        }
                        case 3: {
                            System.out.println("В пытошной новый ответ");
                            Object Obj=transferable.getTransferData(AnswerFlavor);
                            AnswerMarkTransferable DMTr=(AnswerMarkTransferable) Obj;
                            if (DMTr.getSource().equals(tree)) {
                              if (nodeInfo.getType()==8) {
                                AnswerMark AMGuest=(AnswerMark)DMTr.GetAnswerMark();
                                AnswerMark AMHome =(AnswerMark)node.getUserObject();
                                ABSortTreeNode root=(ABSortTreeNode)node.getParent();
                                int GuestIndex=AMGuest.getLocalPos(); // откуда
                                int HomeIndex =AMHome .getLocalPos(); // куда
                                javax.swing.tree.TreeNode[] TN = node.getPath();
                                QuestMark QM=(QuestMark)root.getUserObject();
                                QM.setMixed(true);
                                Vector Collection=new Vector();
                                if (GuestIndex>HomeIndex) {
                                    int k=HomeIndex+1;
                                    while (k<root.getChildCount()) {
                                        ABSortTreeNode CurrentBranch =(ABSortTreeNode)root.getChildAt(k);
                                        AnswerMark AMCurrent=(AnswerMark)CurrentBranch.getUserObject();
                                        AMCurrent.setLocalPos(k+1);
                                        Collection.add(CurrentBranch);
                                        k++;
                                    }
                                    AMGuest.setLocalPos(HomeIndex+1);
                                } else 
                                    if (HomeIndex>GuestIndex) {
                                        int k=GuestIndex+1;
                                        while (k<=HomeIndex) {
                                            ABSortTreeNode CurrentBranch =(ABSortTreeNode)root.getChildAt(k);
                                            AnswerMark AMCurrent=(AnswerMark)CurrentBranch.getUserObject();
                                            AMCurrent.setLocalPos(k-1);
                                            Collection.add(CurrentBranch);
                                            k++;
                                        }
                                        AMGuest.setLocalPos(HomeIndex);
                                    }
                                DMEP.getServer().StoreDQА(QM.getQI(),3);
                                root.sortChildren(Collection.toArray());
                                tree.setSelectionPath(new javax.swing.tree.TreePath(TN));
                                tree.updateUI();
                              }
                            } else {
                                System.out.println("В пытошной ответ из другого опроса");
                                if (nodeInfo.getType()==7) {
                                    AnswerMark AM=(AnswerMark)DMTr.GetAnswerMark();
                                    QuestMark QM=(QuestMark)nodeInfo;
                                    Integer Key = DMEP.getServer().getKey();
                                    DMEP.getServer().getMainQuestion().newAnswer(AM.getName(),Key,true);
                                    QM.getQI().addElement(Key,DMEP.getLibrary().getID(),true);
                                    DMEP.refreshBranch(node, 2);
                                } else if (nodeInfo.getType()==8) {
                                        AnswerMark OldAM=(AnswerMark)nodeInfo;
                                        AnswerMark NewAM=(AnswerMark)DMTr.GetAnswerMark();
                                        ABSortTreeNode parent=(ABSortTreeNode)node.getParent();
                                        QuestMark QM=(QuestMark)parent.getUserObject();
                                        Integer Index=OldAM.RAI.getPos();
                                        Integer i=QM.getQI().getSize()-1;
                                        while (i>Index) {
                                            RInterface RAI=QM.getQI().getByPos(i);
                                            RAI.setPos(i+1);
                                            i--;
                                        }
                                        Integer Key = DMEP.getServer().getKey();
                                        DMEP.getServer().getMainQuestion().newAnswer(NewAM.getName(),Key,false);
                                        RInterface RAI=QM.getQI().addElement(Key,DMEP.getLibrary().getID(),false);
                                        RAI.setPos(Index+1);
                                        DMEP.getServer().StoreDQА(QM.getQI(),3);
                                        //сохраняем ответ
                                        DMEP.refreshBranch(parent, 2);
                                    }
                            }
                            break;
                        }
                        case 4: {
                            System.out.println("Бросили в пытошную модуль данных");
                            Object Obj=transferable.getTransferData(DataModuleFlavor);
                            DataModuleTransferable DMTr=(DataModuleTransferable) Obj;
                            DataModuleMark DM1= (DataModuleMark)DMTr.GetArray().get(0);
                            DMEP.getLibrary().DetailFormAutomat(0);
                            System.out.println("В пытошной модуль данных "+DM1.getName());
                            DMEP.initTree(DM1.getRContent());
                            break;
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

    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMarkTransferable";
        try {
            MyFlavor=new DataFlavor(localArrayListType);
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
