/**
 * This is an example of a component, which serves as a DragSource as 
 * well as Drop Target.
 * To illustrate the concept, JList has been used as a droppable target
 * and a draggable source.
 * Any component can be used instead of a JList.
 * The code also contains debugging messages which can be used for 
 * diagnostics and understanding the flow of events.
 * 
 * @version 1.0
 */

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import java.io.*;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.DefaultListModel;


import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreeSelectionModel;




public class AnswerListExp extends JList {
    

  /**
   * enables this component to be a dropTarget
   */
  
  

  /**
   * enables this component to be a Drag Source
   */

  /**
   * constructor - initializes the DropTarget and DragSource.
   */
  QuestionInterface Obj = null;
  QuestionInterface oObj = null;
 
  public AnswerListExp() {
    //Obj = aObj;
  }
  
  public void setMainQuest(QuestionInterface aObj) {
    oObj = aObj;
  }
  public void setCurrQuest(QuestionInterface aObj) {
    Obj = aObj;
  }
  
  public QuestionInterface getMainQuest() {
    return oObj;
  }
  
  public QuestionInterface getCurrQuest() {
    return Obj;
  }
  
  public void addElement( Object s ){
        (( DefaultListModel )getModel()).addElement (s.toString());
  }

  /**
   * removes an element from itself
   */
   
  public void removeElement(){
    (( DefaultListModel)getModel()).removeElement( getSelectedValue());
  }
  
}
