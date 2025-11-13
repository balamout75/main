public class QuestListContent {
    public RInterface RQI;
    public QuestionInterface  QI;
    
    public QuestListContent(RInterface aRQI, QuestionInterface aQI) {
        RQI=aRQI;
        QI=aQI;
    }
    public String toString() {
        String S="";
        try {
            S=QI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return S;
        
    }
    
    public RInterface getRInterface() {
        return RQI; 
    }
    
    public Integer getPos() {
        Integer Res=null;
        try {
            Res=RQI.getPos();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return Res; 
    }
}    