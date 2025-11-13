//import javax.swing.BorderFactory;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.util.ArrayList;

public class JDBCAdapterCellRenderer2 extends DefaultTableCellRenderer
                           implements TableCellRenderer {
    ImageIcon IconQ1 = new ImageIcon("images/gif/16X16/05.gif");
    ImageIcon IconQ2 = new ImageIcon("images/gif/16X16/06.gif");
    ImageIcon IconQ3 = new ImageIcon("images/gif/16X16/07.gif");
    DefaultTableCellRenderer renderer=new DefaultTableCellRenderer();

    DictionaryInterface     Dictionary;
    INIACRMIInterface       Server;
    AnswerInterface         CurrentAI;
    Color Color1 = new Color(255,255,255);
    Color Color2 = new Color(248,181,255);
    Color Color3 = new Color(171,127,255);
    Color Color4 = new Color(0,0,0);
    Color Color5 = new Color(255,0,255);

    public JDBCAdapterCellRenderer2(INIACRMIInterface aServer, DictionaryInterface aDictionary) {
        Dictionary  =   aDictionary;
        Server      =   aServer;
        setOpaque(true); //MUST do this for background to show up.
    }

    private int CheckPair(Object[] S, QuestionInterface QI) {
        int ResultCode=0;
        try {
            if (true) {
                String code=(String)S[0];
                Integer i = new Integer(code);
                Integer AnswerID=QI.getByPos(i-1).getID();
                CurrentAI=Server.getMainQuestion().getAnswer(AnswerID);
                if (!(CurrentAI.getTextable()|(S.length==1))) ResultCode=3; //Недопустимые текстовые данные 
            }
        } catch (Exception e2) {
            ResultCode=2; // Несуществую
        }
        return ResultCode;
    }    

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setIcon(null);
        Component ReturnComponent=this;
        try {
            RInterface RQI=Dictionary.getByPos(new Integer(row));
            QuestionInterface QI = Server.getMainDictionary().getQuestion(RQI.getID()) ;
            String Str=value.toString();
            Str.trim();
            Str=Str.replace("  "," ");
            String[] s= Str.split(" ");
            String ResultString="";
            int ResultCode=0;
            if (Str.isEmpty()) ResultCode=4; 
            else if (QI.getQuestionType()==1) {
                    try {
                        int h=new Integer(s[0]);
                        ResultCode=CheckPair(s, QI);
                        ResultString=Str + ": "+CurrentAI.getName();
                    } catch (Exception e2) {
                        ResultCode=1; //нет значения кода ответа
                    }
                } else if (QI.getQuestionType()==2) {
                    ArrayList Current = new ArrayList();
                    int k = 0;
                    while ((k < s.length)&(ResultCode==0)) {
                        try {
                            int h=new Integer(s[k]);
                            if (!Current.isEmpty()) {
                                ResultCode=CheckPair(Current.toArray(), QI);
                                Current.clear();
                            }
                            Current.add(s[k]);
                        } catch (Exception e2) {
                            if (Current.isEmpty()) ResultCode=1; else Current.add(s[k]);
                        }
                        k++;
                    }
                    if (ResultCode==0) if (!Current.isEmpty()) ResultCode=CheckPair(Current.toArray(), QI);
                    if (ResultCode==0) ResultString=Str;
                } 
            switch (ResultCode) {
                case 0: {   
                    this.setForeground(Color4);
                    break;
                }       
                case 1: {   
                    //"Нет значения кода ответа"
                }                       
                case 2: {   
                    //"Введен код несуществующего ответа"
                }                                       
                case 3: {   
                    //"Введено недопустимое текстовое значение"                    
                    this.setForeground(Color5);                    
                    ResultString=Str;
                    break;
                }                                       
                case 4: {   
                    this.setForeground(Color5); 
                    ResultString="< нет ответа >";
                    break;
                } 
            } 
            if (isSelected) ReturnComponent=renderer.getTableCellRendererComponent(table,ResultString,isSelected,hasFocus,row,column);
            else this.setText(ResultString);
            
        } catch (Exception ex) {
            System.out.println("Ошибка в обработке InputField "+ex);
        }
        return ReturnComponent;
    }



}