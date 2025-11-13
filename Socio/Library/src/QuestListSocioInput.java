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




public class QuestListSocioInput extends JList {
    

  /**
   * enables this component to be a dropTarget
   */
  
  

  /**
   * enables this component to be a Drag Source
   */

  /**
   * constructor - initializes the DropTarget and DragSource.
   */
  DictionaryInterface Obj = null;
  DictionaryInterface oObj = null;
 
  public QuestListSocioInput() {
    //Obj = aObj;
  }
  
  public void setMainDictionary(DictionaryInterface aObj) {
    oObj = aObj;
  }
  public void setDictionary(DictionaryInterface aObj) {
    Obj = aObj;
  }
  
  public DictionaryInterface getMainDict() {
    return oObj;
  }
  
  public DictionaryInterface getDict() {
    return Obj;
  }
  
  public void addElement( Object s ){
        (( DefaultListModel )getModel()).addElement (s);
  }

  /**
   * removes an element from itself
   */
   
  public void removeElement(){
    (( DefaultListModel)getModel()).removeElement( getSelectedValue());
  }
  
}
