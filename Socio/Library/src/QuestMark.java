/*
 * QuestMark.java
 *
 * Created on 7 Сентябрь 2006 г., 15:54
 */

public class QuestMark implements DQAMarkInterface, Comparable {
    //DataFlavor localArrayListFlavor;
    //String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +";class=QuestMark";
    private RInterface RQI;
    private QuestionInterface  QI;
    private boolean Mixed=false;
    private Integer DictionaryID;
    private Integer LocalPos;
    //private ArrayList QIArray;
    //public DataFlavor[] DF;

    public int compareTo(Object o) {
            int res=0;
            try {
                DQAMarkInterface DMM=(DQAMarkInterface)o;
                if (DMM.getType()==getType()) res=getLocalPos().compareTo(DMM.getLocalPos());
                else res=new Integer(getType()).compareTo(DMM.getType());
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
            return res;
    }
    
    public QuestMark(RInterface aRQI,QuestionInterface aQI,Integer aDictionaryID) {
            try {
                RQI=aRQI;
                QI=aQI;
                DictionaryID=aDictionaryID;
                LocalPos=RQI.getPos();
            } catch( Exception re ) {
                System.out.println(re.toString());
            }    
    }
    
    public Integer getLocalPos() {
        return LocalPos;
    };
    
    public void setLocalPos(Integer aLocalPos) {
        LocalPos=aLocalPos;
    };
    

    public DQAInterface getContent() {
        return (DQAInterface)QI;
    }
    
    public String toString() {
        String Res=null;
        try {
            Res=(RQI.getPos()+1)+"."+QI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
    }

    public String getName() {
        String Res=null;
        try {
            Res=QI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
        return Res;
    }
    
    public boolean getMixed() {
        return Mixed;
    }
    
    public void setMixed(boolean aMixed) {
        Mixed=aMixed;
    }     
    
    public int getType() {
        return 7;
    };
    
    public Integer getID() {
        Integer Res=null;
        try {
            Res=QI.getID();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
    }
    
    public QuestionInterface getQI() {
        return QI;
    }
    
    public RInterface getRContent() {
        return RQI;
    }

    public void setName(String s, Integer LibID,boolean Flag) {
        try {
            QI.setName(s, LibID, Flag);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }

    public DQAMarkInterface makeNewChildMark(Integer ID, RInterface RI, INIACRMIInterface Server) {
        try {
            return new AnswerMark(RI,Server.getMainQuestion().getAnswer(ID),this.getRContent());
        } catch( Exception re ) {
            System.out.println(re.toString());
            return null;
        }
    };
   
}
