public class DictionaryListContent {
    //public RDictionaryInterface RQI;
    public DictionaryInterface  DI;
    
    public DictionaryListContent(DictionaryInterface aDI) {
        DI=aDI;
    }
    public String toString() {
        String S="";
        try {
            S=DI.getName();
        } catch( Exception re ) {
            System.out.println(re.toString());	   
        }     
        return S;
        
    }
    
    public DictionaryInterface getDictionariInterface() {
        return DI; 
    }
  
}    