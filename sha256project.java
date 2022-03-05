import java.io.*;
import java.security.MessageDigest;
import java.util.Random;
import java.util.Scanner;


public class sha256project {

    public static String [] theArray;
    public static void main(String[] args){
        
        Dictionary mydictionary = new Dictionary();

        Scanner sc = new Scanner(System.in);
        System.out.println("What Score are you looking for? (>=)");
        int score = sc.nextInt();
        System.out.println("How many sentences would you like to make?");
        int length = sc.nextInt();
        System.out.println("");         
        
        String[] theArray = new String[length];     //Create an array for sentences.
        String[] theShaArray=new String[length];    //Create an array for Hashes.

        for(int i=0;i<length;i++) { 
            theArray[i]=assemble(mydictionary);     //Fill the array with random sentences by calling the assemble method.
            theShaArray[i]=sha256(theArray[i]);     //Fill the hash array with hashes of the contents of the first array.
        }

        String firstSentence = "";                  //Blank String which will be updated further down - firstSentence.
        String secondSentence = "";                 //Blank String which will be updated further down - secondSentence.
        int count = 0;                              //empty counter that will be updated further down.
            
            //The purpose of the nested for loop is to fill the first and second slot with hashes that can then be sent to the check method.
            //The loops will iterate through both arrays until the length has finished.
        
            for(int i=0; i<length;i++) {
                String shaFirst = theShaArray[i];                   //get the contents of the first slot of the array and cast into a string.
                    for(int j=i+1; j<length; j++){
                        String shaSecond = theShaArray[j];          //repeat the process for the second slot so we have two sentences to compare. 
      
                        int results = check(shaFirst,shaSecond);    //get a result of the two strings compare together.

                        if(results>count){                          //This will update each time a sentence is compared ensuring the highest score always remains at the top.
                            count=results;
                            firstSentence = theArray[i];            //Update the strings with the highest scores so they can be printed
                            secondSentence = theArray[j];
                            
                            if(results >= score){                   //Useful If statement that will provide a selection of high scoring results.
                                System.out.println(firstSentence);
                                System.out.println(secondSentence);
                                System.out.println(count);
                                System.out.println(""); 
                            }
                        }
                    }
                }
            System.out.println("Tests are now complete");       
    }
        
        //Simple check method to iterate through the current strings and tally the results.
        public static int check(String shaFirst, String shaSecond){

            int total=0;
            int hash = 64;

            for(int i = 0; i < hash; i++){
                if(shaFirst.charAt(i) == shaSecond.charAt(i)){
                    total++;
                    }
                }
                return total;
            }
        
        //The assemble method constructs the string by calling two smaller methods. 
        //Important to note a random boolean will determine if the sentence gets an ! or not.
        public static String assemble(Dictionary mydictionary){

            Random rand = new Random();
            boolean eitherOr = rand.nextBoolean();

            if(eitherOr == true){
                String sentence = (getNP(mydictionary) + " " + getVP(mydictionary)+".");
                return sentence;
            }else{
                String sentence = (getNP(mydictionary) + " " + getVP(mydictionary)+"!");
                return sentence;
            }
        }

        //The NP method will construct the first half of the sentence.
        //Important to note an additional random boolean will determine if the sentence starts with 'an' or 'a'.
        public static String getNP(Dictionary mydictionary){

            Random rand = new Random();
            int nounInt = rand.nextInt(0,586);
            boolean eitherOr = rand.nextBoolean();

            if(eitherOr == true){
                String NP = checkNP(mydictionary.getWord(nounInt));
                return NP;
                }
                else{
                    String NP = ("The " + mydictionary.getWord(nounInt));
                    return NP;
                    }
                }
        
        //Check NP will check the noun to determine if 'An' or 'A' is used.
        public static String checkNP(String NP){

            if(NP.charAt(0) == 'a' || NP.charAt(0) == 'A'
                || NP.charAt(0) == 'e' || NP.charAt(0) == 'E'
                    || NP.charAt(0) == 'i' || NP.charAt(0) == 'I'
                        || NP.charAt(0) == 'o' || NP.charAt(0) == 'O'
                            || NP.charAt(0) == 'u' || NP.charAt(0) == 'U'){
                                String checkNP = new String("An " + NP);
                                return checkNP;   
                            }
                            else{
                                String checkNP = new String("A " + NP);
                                return checkNP;
                            }
                        }
        //The VP method will construct the first second of the sentence.
        public static String getVP(Dictionary mydictionary){

            Random rand = new Random();
            int verbInt = rand.nextInt(587, 31388);
            int nounInt = rand.nextInt(0,586);
            int adjectiveInt = rand.nextInt(31389, 59867);
            
            //a checkVB method is called on each verb to determine if they get an 's' or not.
            String verb = checkVB(mydictionary.getWord(verbInt));

            String VP = new String ( verb + " the " + mydictionary.getWord(adjectiveInt) + " " + mydictionary.getWord(nounInt));
            return VP;
         }

         //To add an 's' to the verb we check if it ends with 's', if it does it is skipped.
         public static String checkVB(String verb){

            if(!verb.endsWith("s")){
                String checkVerb = new String (verb+"s");
                return checkVerb;
            }else return verb;
         } 

        public static String sha256 (String input){
            try{
                MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
                byte[] salt = "CS210+" .getBytes("UTF-8");
                mDigest.update(salt);
                byte[] data = mDigest.digest(input.getBytes("UTF-8"));
                StringBuffer sb = new StringBuffer();
                    
                for(int i=0; i<data.length; i++){
                        sb.append(Integer.toString((data[i]&0xff)+0x100,16).substring(1));
                    }
                    return sb.toString();
                }catch(Exception e){
                    return(e.toString());
                }
            }

        public int getSize() {
            return 0;
        }

        public char[] getWord(int number) {
            return null;
        }
    }

 class Dictionary{
     
    private String input[]; 

    public Dictionary(){
        input = load("words.txt");  
    }
    
    public int getSize(){
        return input.length;
    }
    
    public String getWord(int n){
        return input[n];
    }
    
    private String[] load(String file) {
        File aFile = new File(file);     
        StringBuffer contents = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader( new FileReader(aFile) );
            String line = null; 
            int i = 0;
            while (( line = input.readLine()) != null){
                contents.append(line);
                i++;
                contents.append(System.getProperty("line.separator"));
            }
        }catch (FileNotFoundException ex){
            System.out.println("Can't find the file - are you sure the file is in this location: "+file);
            ex.printStackTrace();
        }catch (IOException ex){
            System.out.println("Input output exception while processing file");
            ex.printStackTrace();
        }finally{
            try {
                if (input!= null) {
                    input.close();
                }
            }catch (IOException ex){
                System.out.println("Input output exception while processing file");
                ex.printStackTrace();
            }
        }
        String[] array = contents.toString().split("\n");
        for(String s: array){
            s.trim();
        }
        return array;
    }
}
    

