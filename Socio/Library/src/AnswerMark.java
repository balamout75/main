public class AnswerMark implements DQAMarkInterface, Comparable {
    public AnswerInterface  AI;
    public RInterface RAI;
    public RInterface RQI;
    public boolean Mixed=false;
    //private JCheckBox leafRenderer = new JCheckBox();
    public Integer LocalPos;
    
    public AnswerMark(RInterface aRAI,AnswerInterface aAI, RInterface aRQI) {
            try {
                AI=aAI;
                RAI=aRAI;
                RQI=aRQI;
                if (aRAI!=null) LocalPos=aRAI.getPos(); 
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

    public int getType() {
        return 8;
    };
    
    public RInterface getRQI() {
        return RQI;
    };
    
    public String toString() {
        String Res="";
        try {
            if (AI!=null) Res=(LocalPos+1)+"."+AI.getName(); else Res= LocalPos+".   < Нет ответа >";
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res;
        
    }

    public String getName() {
        String Res="";
        try {
            if (AI!=null) Res=AI.getName(); else Res="Нет ответа";
            //Res=AI.getName();
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

    public DQAInterface getContent() {
        return (DQAInterface)AI;
    }

    public void setName(String s, Integer LibID, boolean Flag) {
        try {
            AI.setName(s, LibID, Flag);
        } catch( Exception re ) {
            System.out.println(re.toString());
        }
    }

    public DQAMarkInterface makeNewChildMark (Integer ID, RInterface RI, INIACRMIInterface Server) {
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
