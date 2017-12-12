import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class languageScanner 
{
   public static void main(String args[]) throws IOException 
   {  
      try 
      {
         //prompting the user for input file name
         System.out.print("Please enter the name of the input file: ");
         Scanner input = new Scanner(System.in);
         String inputVal = input.nextLine();

         //file IO
         FileReader fileReader = new FileReader(inputVal);
         BufferedReader bufferedReader = new BufferedReader(fileReader);
         StringBuffer stringBuffer = new StringBuffer();

         String line;
         while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
         }
         fileReader.close();
         String fileStuff = stringBuffer.toString();
         System.out.println(fileStuff);

         //regex variables
         final String regex = "(\\()|(\\))|(\\[)|(\\])|(\\.)|(\\+)|(\\-)|(\\*)|(<=)|(>=)|(=)|(<>)|(<)|(>)|(:=)|(:)|(;)|(,)|(and)|(array)|(begin)|(do)|(char)|(chr)|(div)|(else)|(end)|(if)|(integer)|(mod)|(not)|(of)|(or)|(ord)|(procedure)|(program)|(read)|(readln)|(then)|(var)|(while)|(write)|(writeln)|(function)|(\\{.*\\})|\"(.*)\"|\'(.*)\'|([a-zA-Z]\\w+)|([a-zA-Z])|([0-9]+\\.[0-9]+)|([0-9]+)|(\n)";
         final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
         final Matcher matcher = pattern.matcher(fileStuff);
         //matcher is now ready for regex regognition in the while loop below the output file declaration

         //output file declaration
         File fout = new File("a.out");
         FileOutputStream fileOutputStream = new FileOutputStream(fout);

         BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

         //initializing line counter
         int linecount = 1; 
         System.out.println("Writing character stream to file...");

         //using this as a buffer
         String temp;

         //checking each input and matching it to a character type
         while (matcher.find()) 
         {
            for (int i = 1; i <= matcher.groupCount(); i++) 
            {
               if (matcher.group(i) != null)
               {                  //System.out.println("Group " + i + ": " + matcher.group(i));
                  if (i == 1)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "LPAREN", matcher.group(), linecount)); //matcher.group(1)
                  if (i == 2)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "RPAREN", matcher.group(), linecount));
                  if (i == 3)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "LBRACKET", matcher.group(), linecount));
                  if (i == 4)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "RBRACKET", matcher.group(), linecount));
                  if (i == 5)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "PERIOD", matcher.group(), linecount));
                  if (i == 6)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "PLUS", matcher.group(), linecount));
                  if (i == 7)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "MINUS", matcher.group(), linecount));
                  if (i == 8)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "TIMES", matcher.group(), linecount));
                  // if (i == 9)
                  //    System.out.println("<lparen>, " + matcher.group(), linecount); //doesn't require a token to be created.
                  if (i == 9)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "LESSEQUAL", matcher.group(), linecount));
                  if (i == 10)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "GREATEREQUAL", matcher.group(), linecount));      
                  if (i == 11)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "EQUAL", matcher.group(), linecount));    
                  if (i == 12)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "NOTEQUAL", matcher.group(), linecount));      
                  if (i == 13)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "LESSTHAN", matcher.group(), linecount));
                  if (i == 14)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "GREATERTHAN", matcher.group(), linecount));
                  if (i == 15)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "ASSIGNMENT", matcher.group(), linecount));
                  if (i == 16)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "COLON", matcher.group(), linecount));
                  if (i == 17)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "SEMICOLON", matcher.group(), linecount));
                  if (i == 18)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "COMMA", matcher.group(), linecount));
                  if (i == 19)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "AND", matcher.group(), linecount));
                  if (i == 20)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "ARRAY", matcher.group(), linecount));
                  if (i == 21)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "BEGIN", matcher.group(), linecount));
                  if (i == 22)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "DO", matcher.group(), linecount));
                  if (i == 23)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "CHAR", matcher.group(), linecount));
                  if (i == 24)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "CHR", matcher.group(), linecount));
                  if (i == 25)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "DIVIDE", matcher.group(), linecount));
                  if (i == 26)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "ELSE", matcher.group(), linecount));
                  if (i == 27)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "END", matcher.group(), linecount));
                  if (i == 28)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "IF", matcher.group(), linecount));
                  if (i == 29)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "INTEGER", matcher.group(), linecount));
                  if (i == 30)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "MOD", matcher.group(), linecount));
                  if (i == 31)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "NOT", matcher.group(), linecount));
                  if (i == 32)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "OF", matcher.group(), linecount));
                  if (i == 33)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "OR", matcher.group(), linecount));
                  if (i == 34)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "ORD", matcher.group(), linecount));
                  if (i == 35)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "PROCEDURE", matcher.group(), linecount));
                  if (i == 36)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "PROGRAM", matcher.group(), linecount));
                  if (i == 37)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "READ", matcher.group(), linecount));
                  if (i == 38)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "READLN", matcher.group(), linecount));
                  if (i == 39)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "THEN", matcher.group(), linecount));
                  if (i == 40)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "VAR", matcher.group(), linecount));
                  if (i == 41)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "WHILE", matcher.group(), linecount));
                  if (i == 42)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "WRITE", matcher.group(), linecount));
                  if (i == 43)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "WRITELN", matcher.group(), linecount));
                  if (i == 44)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "FUNCTION", matcher.group(), linecount));
                  //(I == 45) tells us that this line is a comment so we don't include it in the stream
                  if (i == 46 || i == 47) 
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "STRING", matcher.group(), linecount));
                  if (i == 48 || i == 49)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "ID", matcher.group(), linecount));
                  if (i == 50)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "FLOATNUMBER", matcher.group(), linecount));
                  if (i == 51)
                     bufferedWriter.write(String.format("%-20s%-20s%-20s\n" , "NUMBER", matcher.group(), linecount));
                  if (i == 52)
                     linecount++;
               }
            }
         }
         System.out.println("Character stream successfully written to file.");
         bufferedWriter.close();
      }
      //if our file IO fails
      catch (IOException e)
      {
         System.out.println("\nInput file does not exist.");
         System.out.println("Please enter an existing file name and run the program again.");
      }
      getsym();
   }

   //in case we are not able to create our output file for the character stream
   public static void getsym()
   {
      try 
      {
         FileReader fin = new FileReader("a.out");
         Scanner src = new Scanner(fin);
      }
      catch(IOException e)
      {
         System.out.println("Lister file was not found.");
      }
   }
}