/*
 * QuestMark.java
 *
 * Created on 7 Сентябрь 2006 г., 15:54
 */
//import java.awt.*;
//import java.io.*;

public class DictionaryMark implements DQAMarkInterface, Comparable {
    //DataFlavor localArrayListFlavor;
    //String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMark";
    private DictionaryInterface DI;
    private boolean Mixed=false;
    Integer LocalPos;
    //private ArrayList QIArray;
    //public DataFlavor[] DF;
    
    public DictionaryMark(DictionaryInterface aDI) {
        DI=aDI;
        LocalPos=new Integer(0);
        /*try {
            DF = new DataFlavor[2];
            DF[0] = new DataFlavor(localArrayListType);
            DF[1] = DataFlavor.stringFlavor;
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }*/
    }

    public Integer getLocalPos() {
        return LocalPos;
    };
    
    public void setLocalPos(Integer aLocalPos) {
        LocalPos=aLocalPos;
    };    
    
    public int compareTo(Object o) {
            int res=0;
            try {
                DQAMarkInterface DMM=(DQAMarkInterface)o;
                if (DMM.getType()==getType()) res=getName().compareTo(DMM.getName());
                else res=new Integer(getType()).compareTo(DMM.getType());
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
        return res;
    }

    public String toString() {
        String Res=null;
        try {
            Res=DI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
    }

    public String getName() {
        String Res="";
        try {
            Res=DI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
        
    }

    public int getType() {
        return 0;
    };
    
    public boolean getMixed() {
        return Mixed;
    }
    
    public void setMixed(boolean aMixed) {
        Mixed=aMixed;
    }     
    
    public Integer getID() {
        Integer Res=null;
        try {
            Res=DI.getID();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
    }
    
    public DictionaryInterface getQI() {
        return DI;
    }

    public DQAInterface getContent() {
        return (DQAInterface)DI;
    }

    public void setName(String s, Integer LibID, boolean Flag) {
        try {
            DI.setName(s, LibID, Flag);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }

    public DQAMarkInterface makeNewChildMark(Integer ID, RInterface RI, INIACRMIInterface Server) {
        try {
            return new QuestMark(RI,Server.getMainDictionary().getQuestion(ID),getID());
        } catch( Exception re ) {
            System.out.println(re.toString());
            return null;
        }
    };

    public RInterface getRContent() {
            return null;
    }
   
}
