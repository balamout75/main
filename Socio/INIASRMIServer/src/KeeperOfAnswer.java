import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


/*
 * Question.java
 *
 * Created on 12 Январь 2004 г., 15:35
 */

/**
 *
 * @author  Administrator
 */
public class KeeperOfAnswer extends UnicastRemoteObject implements Keeper {
    public ArrayList Answers;
    public String Name;
    public int Type;
    public int RepetitionType=1;
    public INIACRMIInterface Server;
   
    /** Creates a new instance of Question */
    public void ClearAnswer() throws java.rmi.RemoteException {
        Answers.clear();
    };
    
    public DQAInterface newChild(String Str, Integer Key, boolean Flag) throws java.rmi.RemoteException {
        Answer CurrAnswer = null;
        try {
            System.out.println( "Новый ответ" );
            CurrAnswer = new Answer(Server, Str, Key, Flag);
            CurrAnswer.setServer(Server);
            Answers.add(CurrAnswer);
            if (Flag) {
                Server.StoreDQА(CurrAnswer, 0);
                Server.StoreDQА(CurrAnswer, 1);
            }
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }
        return CurrAnswer;
    }    
    
    public KeeperOfAnswer(INIACRMIInterface aServer) throws java.rmi.RemoteException  {
        Server          =   aServer;
        Answers         =   new ArrayList();
    }

    public void clearChild() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DQAInterface getChild(Integer IDKey) throws RemoteException {
        int index=0;
        boolean SimFlag=false;
        DQAInterface AI=null;
        while ((index<Answers.size())&!SimFlag){
            AI=(DQAInterface)Answers.get(index);
            SimFlag=((AI.getID().compareTo(IDKey))==0); 
            ++index;
         }
        if (!SimFlag) AI=null;
        return AI;
    }

}
