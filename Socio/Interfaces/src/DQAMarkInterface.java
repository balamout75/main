
/**
 *
 * @author  Administrator
 */
public interface DQAMarkInterface  {
     public String getName() ;
     public String toString();
     public DQAInterface getContent();
     public RInterface getRContent();     
     public int getType();
     public Integer getLocalPos();
     public void setName(String S, Integer LibID, boolean Flag);
     public DQAMarkInterface makeNewChildMark(Integer ID, RInterface RI, INIACRMIInterface Server);
     public boolean getMixed();
     public void setMixed(boolean aMixed);
}
