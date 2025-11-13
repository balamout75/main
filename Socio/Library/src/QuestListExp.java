import javax.swing.*;
public class QuestListExp extends JList {
  DictionaryInterface Obj = null;
  DictionaryInterface oObj = null;
  public QuestListExp() {
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
