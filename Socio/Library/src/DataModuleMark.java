
public class DataModuleMark implements DQAMarkInterface, Comparable{
        public DataModuleMark Parent;
        public RInterface RDMI;
        public DataModuleInterface DMI;
        boolean Virtual=false;
        boolean Mixed  =false;
        Integer LocalPos;

        public int compareTo(Object o) {
            int res=0;
            try {
                DQAMarkInterface DMM=(DQAMarkInterface)o;
                if (DMM.getType()==DMI.getType()) res=DMI.getName().compareTo(DMM.getName());
                else res=new Integer(DMI.getType()).compareTo(DMM.getType());
            } catch( Exception re ) {
                System.out.println(re.toString());
            }



            return res;

        }

        public DataModuleMark(RInterface ‡RDMI, DataModuleInterface aDMI, DataModuleMark aParent) {
            try {            
                Parent = aParent;
                DMI  = aDMI;
                RDMI = ‡RDMI;
                LocalPos=RDMI.getPos();
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
        
        public String getName() {
            String name="";
            try {
                name=DMI.getName();
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
            return name;

        }

        public DataModuleMark getParent() {
            return Parent;

        }

        public void setVirtual(boolean aVirtual) {
            Virtual = aVirtual;
        }

        public int getType(){
            int type=0;
            try {
                type=DMI.getType();
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
            if (!Virtual) {return type;}
            else {return 5;}
        }
        
        public void setName(String S, Integer LibID, boolean Flag) {
            try {
                DMI.setName(S, LibID, Flag);
            } catch( Exception re ) {
                System.out.println(re.toString());
            }

        }

        public DQAInterface getContent() {
            return (DQAInterface)DMI;
        }

        public String toString() {
            String Res=null;
            try {
                Res=DMI.getName();
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
            return Res;
        }
        
        public Integer getID() {
            Integer Res=null;
            try {
                Res=DMI.getID();
            } catch( Exception re ) {
                System.out.println(re.toString());
            }
            return Res;
        }

        public DataModuleInterface getDMI() {
            return DMI;
        }

        public RInterface getRContent() {
            return RDMI;
        }

        public DQAMarkInterface makeNewChildMark(Integer ID, RInterface RI, INIACRMIInterface Server) {
            try {
                return new DataModuleMark(RI,(DataModuleInterface)Server.getDMI(ID),this);
            } catch( Exception re ) {
                System.out.println(re.toString());
                return null;
            }
        };

        public boolean getMixed() {
            return Mixed;
        }

        public void setMixed(boolean aMixed) {
            Mixed=aMixed;
        }

}