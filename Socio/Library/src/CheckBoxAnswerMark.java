import javax.swing.JCheckBox;

//public class CheckBoxAnswerMark implements DQAMarkInterface {
public class CheckBoxAnswerMark  {
    public AnswerInterface  AI;
    public RInterface RAI;
    public RInterface RQI;
    public boolean selected;
    public boolean Mixed=false;
    public String text;
    //private JCheckBox CheckBox;
    //public Integer QuestionID;
    
    public CheckBoxAnswerMark(RInterface aRAI,AnswerInterface aAI, RInterface aRQI) {
        AI=aAI;
        RAI=aRAI;
        RQI=aRQI;
        try {
            text=AI.getName();
        } catch (Exception Ex) {
            System.out.println(Ex.toString());
        }

    }
    
    public int getType() {
        return 8;
    };
    
    public RInterface getRQI() {
        return RQI;
    };

    public DQAInterface getContent() {
        return (DQAInterface)AI;
    }
    
    public String toString() {
        String Res="";
        try {
            //Res=(RAI.getPos()+1)+"."+AI.getName();
            //System.out.println("Нормальный toString()");
            Res= getClass().getName() + "[" + text + "/" + selected + "]";
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
        
    }

    public String getName() {
        String Res="";
        try {
            Res=AI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
        return Res;

    }
    
    public Integer getID() {
        Integer Res=null;
        try {
            Res=AI.getID();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
    }
    
    public AnswerInterface getAI() {
        return AI;
    }

    public void setName(String s, Integer LibID, boolean Flag) {
        try {
            AI.setName(s,LibID, Flag);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }
    public boolean isSelected() {
        return selected ;
    }

    public void setSelected(boolean newValue) {
        selected=newValue;
    }
    
    public DQAMarkInterface makeNewChildMark(Integer ID, RInterface RI, INIACRMIInterface Server) {
        return null;
    };

    public RInterface getRContent() {
        return RAI;
    }

    public boolean getMixed() {
        return Mixed;
    }

    public void setMixed(boolean aMixed) {
        Mixed=aMixed;
    }

}
