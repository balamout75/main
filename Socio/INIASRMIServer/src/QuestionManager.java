import java.util.ArrayList;
import java.util.List;
import java.rmi.server.UnicastRemoteObject;
import java.io.Serializable;

/*
 * Question.java
 *
 * Created on 12 Январь 2004 г., 15:35
 */

/**
 *
 * @author  Administrator
 */
public class QuestionManager extends UnicastRemoteObject implements QuestionInterface, DQAInterface, Serializable {
    static int alter     = 1;
    static int nonalter  = 2;
    static int free      = 3;
    
    private SocioRefreshInterface ClientRefresher;
    public transient List ClientCollection;
    public ArrayList Answers;
    public String Name;
    public int Type;
    public int RepetitionType=0;
    public Answer CurrentAnswer;
    public Integer Key;
    public Integer QuestionPos;
    public Integer Base;
    public Integer MaxAnswerCount;
    public INIACRMIInterface Server;
    

    public Integer getBase() throws java.rmi.RemoteException {
        return Base;
    };

    public void setBase(Integer aBase, boolean Flag) throws java.rmi.RemoteException {
        Base=aBase;
        if (Flag) Server.StoreDQА(this, 4);
    };

    public Integer getMaxAnswerCount() throws java.rmi.RemoteException {
        return MaxAnswerCount;
    };

    public void setMaxAnswerCount(Integer aMaxAnswerCount, boolean Flag) throws java.rmi.RemoteException {
        MaxAnswerCount=aMaxAnswerCount;
        if (Flag) Server.StoreDQА(this, 5);
    };

    public Integer getPos() throws java.rmi.RemoteException {
        return QuestionPos;
    };
    
    /*public void setPos(Integer Pos) throws java.rmi.RemoteException {
        QuestionPos=Pos;
    };*/

    public RInterface getByPos(Integer Pos) throws java.rmi.RemoteException {
        int index=0;
        boolean SimFlag=false;
        RInterface RAI=null;
        while ((index<Answers.size())&!SimFlag){
            RAI=(RInterface)Answers.get(index);
            SimFlag=((Pos.compareTo(RAI.getPos()))==0); 
            ++index;
         }
        if (!SimFlag) RAI=null;
         //QuestionInterface Quest = (QuestionInterface)QuestCollection.get(QuestionIndex);
        return RAI;
    }

    public void cleanAnswersPos() throws java.rmi.RemoteException {
        RInterface Q=null;
        int suffix=0;
        int index=0;
        while (index<(Answers.size()-suffix)){
            Q=getByPos(index);
            if (Q==null) --suffix;
            else
                if (suffix!=0) Q.setPos(new Integer(Q.getPos().intValue()+suffix));
            ++index;
        }
    }

    public int delByPos(Integer QuestionPos, Integer LibID, boolean Flag) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {  
          int index=0;
          RInterface Q=null;
          while ((index<Answers.size())){
            Q=getByPos(index);
            if (Q!=null)
                if (Q.getPos().compareTo(QuestionPos)==0) {
                    Answers.remove(Q);
                    --index;
                    cleanAnswersPos();
                    if (Flag) {
                        Server.StoreDQА(this, 3);
                        Server.StoreDQА(Server.getMainQuestion().getAnswer(Q.getID()), 13);
                    }
                }
            ++index;
          }
        }
        return ResultFlag;
    };

    public int delByID(Integer QuestID, Integer LibID, boolean Flag) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) {  
          int index=0;
          RInterface Q=null;
          while ((index<Answers.size())){
            Q=getByPos(index);
            if (Q!=null)
                if (Q.getID().compareTo(QuestID)==0) {
                    Answers.remove(Q);
                    cleanAnswersPos();
                    --index;
                    if (Flag) {
                        Server.StoreDQА(this, 3);
                        Server.StoreDQА(Server.getMainQuestion().getAnswer(Q.getID()), 13);
                    }
                }
            ++index;
          }
        }
        return ResultFlag;
    };


    public void setQuestionType(int aType, boolean Flag) throws java.rmi.RemoteException {
        Type = aType;
        if (Flag) Server.StoreDQА(this, 2);
    };
    
    public int getQuestionType() throws java.rmi.RemoteException {
        return Type;
    };
    
    public void setRepetition(int aType, boolean Flag) throws java.rmi.RemoteException {
        RepetitionType = aType;
        if (Flag) Server.StoreDQА(this, 6); 
        
    };
    
    public int getRepetitionType() throws java.rmi.RemoteException {
        return RepetitionType;
    };
    
    public int getType() throws java.rmi.RemoteException {
        return 7;
    }       
    
    public Integer getID() throws java.rmi.RemoteException {
        return Key;
    };
    
    /** Creates a new instance of Question */
    public void ClearAnswer() throws java.rmi.RemoteException {
        Answers.clear();
    };

    public String getName() throws java.rmi.RemoteException {
        return Name;
    }
    
    public AnswerInterface newAnswer(String Str, Integer Key, boolean Flag) throws java.rmi.RemoteException {
        Answer CurrAnswer = null;
        try {
            System.out.println( "Новый ответ" );
            //Integer Keys = Server.getKey();
            CurrAnswer = new Answer(Server, Str, Key, Flag);
            CurrAnswer.setServer(Server);
            Answers.add(CurrAnswer);
            if (Flag) {
                Server.StoreDQА(CurrAnswer, 0);
                Server.StoreDQА(CurrAnswer, 1);
            }
            //UpdateClient();
            //ClientRefresher.Refresh();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
        return CurrAnswer;
    }    
    
    public RInterface addElement(Integer Key, Integer LibID, boolean Flag) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        RPointer RA=null;
        if (ResultFlag==0) {  
            RA=new RPointer(Key);
            RA.setPos(new Integer(Answers.size()));
            Answers.add(RA);
            if (Flag) Server.StoreDQА(this, 3);
        }
        return RA;
    }

    public void setServer(INIACRMIInterface aServer) throws java.rmi.RemoteException {
        Server = aServer;
    };
    
    public ArrayList GetAnswerArray() throws java.rmi.RemoteException {
        return Answers;
    }
    
    public QuestionManager(INIACRMIInterface aServer, String aName, Integer ID, boolean Flag) throws java.rmi.RemoteException  {
        Name=aName;
        Server=aServer;
        System.out.println("QuestNew" );
        Key=ID;
        Type=alter;
        Answers         =   new ArrayList();
        MaxAnswerCount  =   new Integer(0);
        Base            =   new Integer(0);
    }
    
    public int getSize() throws java.rmi.RemoteException {
        return Answers.size();
    }
    public AnswerInterface GetAnswer(int AnswerIndex) throws java.rmi.RemoteException {
        return (Answer)Answers.get(AnswerIndex);
    }
    
    public boolean releaseAnswer(AnswerInterface AI) throws java.rmi.RemoteException {
        Answers.remove(AI);
        return true;
    };
    public boolean releaseAnswer(int AnswerIndex) throws java.rmi.RemoteException {
        Answers.remove(AnswerIndex);
        return true;
    };
    
    public int setName(String Str, Integer LibID, boolean Flag) throws java.rmi.RemoteException {
        int ResultFlag=Server.askArbitr(this,2,LibID);
        if (ResultFlag==0) { 
            Name = Str;
            if (Flag) Server.StoreDQА(this, 1);
        } return ResultFlag;
    };
    
    public ArrayList getKeys() throws java.rmi.RemoteException {
        ArrayList Res=new ArrayList();
        Class CL=new RPointer(null).getClass();
        try {
            for (int i = 0; i < Answers.size(); i++) {
                Object O=Answers.get(i);
                if (O.getClass().equals(CL)) {
                    Res.add(O);
                } else {
                    AnswerInterface AI=(AnswerInterface)O;
                    Res.add(AI.getID());
                }
            }
        } catch( Exception e){
            System.out.println("Ошибка "+e.toString());
    	}     
        return Res;
    }
   
    public AnswerInterface getAnswer(Integer IDKey) throws java.rmi.RemoteException {
        int index=0;
        boolean SimFlag=false;
        AnswerInterface AI=null;
        while ((index<Answers.size())&!SimFlag){
            AI=(AnswerInterface)Answers.get(index);
            SimFlag=((AI.getID().compareTo(IDKey))==0); 
            ++index;
         }
        if (!SimFlag) AI=null;
         //QuestionInterface Quest = (QuestionInterface)QuestCollection.get(QuestionIndex);
        return AI;
    }

}
