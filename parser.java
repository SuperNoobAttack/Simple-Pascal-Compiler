//Jacob Longar
/**
* @author	Jacob Longar
* @version	1.0
*/
import java.io.*;
import java.lang.*;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* a class created to hold data for our symbols / identifiers
* fed to this program through the languageScanner program.
* @param Identifier holds identifier information from languageScanner
* @param Symbol holds the actual character found in languageScanner
* @param Level  integer value indicating what scope a value is in
* @param ProcedureName this value paired with level let us know 
* which scope a symbol exists in.
* @param DataType if the symbol / Identifier is a variable, 
* this parameter lets us know which data type that variable is.
* (useful mainly in final part of code for compiler project)
* @param Value if the symbol / Identifier is a variable, this 
* parameter tells us what value the variable is associated with.
* @param Line tells us which line was being executed if an error
* appears.
*/
class CharacterStruct 
{
	public String Identifier; 	//1st argument
    public String Symbol;  		//2nd argument
    public int    Level;		//3rd argument
    public String ProcedureName;//4rth argument
    public String DataType;		//5th argument
    public String Value;		//6th argument
    public int    Line;			//7th argument

    public CharacterStruct (String Identifier, String Symbol, int Level, String ProcedureName, String DataType, String Value, int Line)
    {
    	this.Identifier = Identifier;
    	this.Symbol = Symbol;
    	this.Level = Level;
    	this.ProcedureName = ProcedureName;
    	this.DataType = DataType;
    	this.Value = Value;
    	this.Line = Line;
    }
};

public class parser 
{
	/**
	* This program reads in data from languageScanner.java
	* and also performs error checking and parsing analysis
	* of a pascal program through the use of functions 
	* documented below.
	* @param isError if an error occurs while parsing, then this boolean
	* value will become true. If this value is true, no code will finish 
	* compiling.
	* @throws IOException  If an input or output 
	*                      exception occurred
	*/

	//used for creating the parsing
	//tree later in this program.
	static StringTokenizer st;
    static String curr;

    //checking for any kind of errors while parsing.
    //if we happen to get any errors, this boolean will
    //change to false, and the program will produce no 
    //output.
	public static boolean isError = false; 

	//default output file in case the user does not specify
	//an output file to use.
	public static String outputfile = "default.asm";
//           _____                    _____                    _____                    _____          
//          /\    \                  /\    \                  /\    \                  /\    \         
//         /::\____\                /::\    \                /::\    \                /::\____\        
//        /::::|   |               /::::\    \               \:::\    \              /::::|   |        
//       /:::::|   |              /::::::\    \               \:::\    \            /:::::|   |        
//      /::::::|   |             /:::/\:::\    \               \:::\    \          /::::::|   |        
//     /:::/|::|   |            /:::/__\:::\    \               \:::\    \        /:::/|::|   |        
//    /:::/ |::|   |           /::::\   \:::\    \              /::::\    \      /:::/ |::|   |        
//   /:::/  |::|___|______    /::::::\   \:::\    \    ____    /::::::\    \    /:::/  |::|   | _____  
//  /:::/   |::::::::\    \  /:::/\:::\   \:::\    \  /\   \  /:::/\:::\    \  /:::/   |::|   |/\    \ 
// /:::/    |:::::::::\____\/:::/  \:::\   \:::\____\/::\   \/:::/  \:::\____\/:: /    |::|   /::\____\
// \::/    / ~~~~~/:::/    /\::/    \:::\  /:::/    /\:::\  /:::/    \::/    /\::/    /|::|  /:::/    /
//  \/____/      /:::/    /  \/____/ \:::\/:::/    /  \:::\/:::/    / \/____/  \/____/ |::| /:::/    / 
//              /:::/    /            \::::::/    /    \::::::/    /                   |::|/:::/    /  
//             /:::/    /              \::::/    /      \::::/____/                    |::::::/    /   
//            /:::/    /               /:::/    /        \:::\    \                    |:::::/    /    
//           /:::/    /               /:::/    /          \:::\    \                   |::::/    /     
//          /:::/    /               /:::/    /            \:::\    \                  /:::/    /      
//         /:::/    /               /:::/    /              \:::\____\                /:::/    /       
//         \::/    /                \::/    /                \::/    /                \::/    /        
//          \/____/                  \/____/                  \/____/                  \/____/         
                                                                                                    
	public static void main(String args[]) throws IOException 
	{
         //prompting the user for output file name
         System.out.print("Please enter the name of the output file (default.asm is the  default output): ");
         Scanner input = new Scanner(System.in);
         outputfile = input.nextLine();

		String inputVal = "a.out";

		//file IO
		FileReader fileReader = new FileReader(inputVal);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer lineReader = new StringBuffer();

		//reading lines from the file outputted by the languageScanner code
		String line;
		while ((line = bufferedReader.readLine()) != null) 
		{
			lineReader.append(line);
			lineReader.append("\n");
		}

		fileReader.close();

		//creating a tokenizer to read each input from the language Scanner
		StringTokenizer tokenizer = new StringTokenizer(lineReader.toString());

		//initializing our array of structures
		ArrayList<CharacterStruct> cStruct = new ArrayList<CharacterStruct>();

		//assigning the identifier and symbol for each struct in the ArrayList
		//and assigning default values for all other values for the time being
		int tokenCount = 0;

		//buffer variable for strings later on.
		String toktemp = "";
		String tokbuf = "";

		while (tokenizer.hasMoreTokens())
		{
			//we have to make a special case for strings because 
			//strings can contain multiple symbols, which messes with
			//our organization.
			tokbuf = tokenizer.nextToken();
			if (tokbuf.contains("STRING"))
			{
				//we go to the next token and remove the first double
				//or single quotation mark.
				tokbuf = tokenizer.nextToken();
				tokbuf = removeQuotationMarks(tokbuf);
				while (true)
				{
					if (tokbuf.contains("\"") || tokbuf.contains("'"))
					{
						tokbuf = removeQuotationMarks(tokbuf);
						toktemp += tokbuf;
						break;
					}
					else
					{
						toktemp += tokbuf;
						toktemp += " ";
						tokbuf = tokenizer.nextToken();
					}
				}
				cStruct.add(new CharacterStruct("STRING", toktemp , -1,"default","default","default",Integer.parseInt(tokenizer.nextToken())));
				toktemp = "";
				tokenCount++;
			}
			else
			{
				cStruct.add(new CharacterStruct(tokbuf, tokenizer.nextToken(), -1,"default","default","default",Integer.parseInt(tokenizer.nextToken())));
				tokenCount++;
			}
		}

		//initializing our data structure
		CharacterStruct characterstruct;

		//used for conversion from CharacterStruct to String
		//so we can use string functions on values from data input.
		String temp; 

		//creating a large loop to test for any errors while parsing.
		//This section will also act as starter code to the final part of //the project.
		//This will be the main part of project #2 as our output to the 
		//final part of the project will simply be an array of structs. 
		for (int u = 0; u < tokenCount; u++)
		{
			//setting our structvalue equal to the current data value
			characterstruct = cStruct.get(u);
			temp = characterstruct.Identifier;

			if (u == 0)
			{
				isProgram(cStruct);
			}

			if (temp.contains("LPAREN"))
			{
				isParenth(cStruct, u, tokenCount);
			}
			if (temp.contains("RBRACKET"))
			{
				isSquareBrack(cStruct, u, tokenCount);
			}
			if (temp.contains("MINUS") || temp.contains("PLUS") || temp.contains("TIMES") || temp.contains("DIVIDE") || temp.contains("MOD") || temp.contains("ASSIGNMENT"))
			{
				isEquation(cStruct, u, tokenCount);
			}
			if (temp.contains("VAR"))
			{
				isInitialize(cStruct, u, tokenCount);
				initialize(cStruct, u, tokenCount);
			}
			if (temp.contains("PROGRAM") || temp.contains("PROCEDURE") || temp.contains("FUNCTION"))
			{
				isBegin(cStruct, u, tokenCount);
			}
			if (temp.contains("WRITE"))
			{
				iswrite(cStruct, u, tokenCount);
			}
			//for two from the end
			if (u == (tokenCount - 2))
			{
				isEnd(cStruct,u, tokenCount);
			}
		}

	}                                                        
//           _____                    _____            _____                    _____                  _______                  _____                   _____          
//          /\    \                  /\    \          /\    \                  /\    \                /::\    \                /\    \                 /\    \         
//         /::\____\                /::\    \        /::\    \                /::\____\              /::::\    \              /::\    \               /::\    \        
//        /::::|   |               /::::\    \       \:::\    \              /:::/    /             /::::::\    \            /::::\    \             /::::\    \       
//       /:::::|   |              /::::::\    \       \:::\    \            /:::/    /             /::::::::\    \          /::::::\    \           /::::::\    \      
//      /::::::|   |             /:::/\:::\    \       \:::\    \          /:::/    /             /:::/~~\:::\    \        /:::/\:::\    \         /:::/\:::\    \     
//     /:::/|::|   |            /:::/__\:::\    \       \:::\    \        /:::/____/             /:::/    \:::\    \      /:::/  \:::\    \       /:::/__\:::\    \    
//    /:::/ |::|   |           /::::\   \:::\    \      /::::\    \      /::::\    \            /:::/    / \:::\    \    /:::/    \:::\    \      \:::\   \:::\    \   
//   /:::/  |::|___|______    /::::::\   \:::\    \    /::::::\    \    /::::::\    \   _____  /:::/____/   \:::\____\  /:::/    / \:::\    \   ___\:::\   \:::\    \  
//  /:::/   |::::::::\    \  /:::/\:::\   \:::\    \  /:::/\:::\    \  /:::/\:::\    \ /\    \|:::|    |     |:::|    |/:::/    /   \:::\ ___\ /\   \:::\   \:::\    \ 
// /:::/    |:::::::::\____\/:::/__\:::\   \:::\____\/:::/  \:::\____\/:::/  \:::\    /::\____\:::|____|     |:::|    /:::/____/     \:::|    /::\   \:::\   \:::\____\
// \::/    / ~~~~~/:::/    /\:::\   \:::\   \::/    /:::/    \::/    /\::/    \:::\  /:::/    /\:::\    \   /:::/    /\:::\    \     /:::|____\:::\   \:::\   \::/    /
//  \/____/      /:::/    /  \:::\   \:::\   \/____/:::/    / \/____/  \/____/ \:::\/:::/    /  \:::\    \ /:::/    /  \:::\    \   /:::/    / \:::\   \:::\   \/____/ 
//              /:::/    /    \:::\   \:::\    \  /:::/    /                    \::::::/    /    \:::\    /:::/    /    \:::\    \ /:::/    /   \:::\   \:::\    \     
//             /:::/    /      \:::\   \:::\____\/:::/    /                      \::::/    /      \:::\__/:::/    /      \:::\    /:::/    /     \:::\   \:::\____\    
//            /:::/    /        \:::\   \::/    /\::/    /                       /:::/    /        \::::::::/    /        \:::\  /:::/    /       \:::\  /:::/    /    
//           /:::/    /          \:::\   \/____/  \/____/                       /:::/    /          \::::::/    /          \:::\/:::/    /         \:::\/:::/    /     
//          /:::/    /            \:::\    \                                   /:::/    /            \::::/    /            \::::::/    /           \::::::/    /      
//         /:::/    /              \:::\____\                                 /:::/    /              \::/____/              \::::/    /             \::::/    /       
//         \::/    /                \::/    /                                 \::/    /                ~~                     \::/____/               \::/    /        
//          \/____/                  \/____/                                   \/____/                                         ~~                      \/____/         

	/**
	* this method finds the next value in our parser
	* by referencing the next token found in the file
	* outputted by the lexical analyser.
	*/
    static void next() 
    {
		// try 
		// {
		    curr=st.nextToken().intern();
		// } 
		// catch( NoSuchElementException e) 
		// {
		//     curr=null;
		// }
    }

    //the following functions with "parse"
    //in their function headers will make the 
    //parse tree for our compiler.

    /**
    *this parser method references parseF(),
    *and parseT1() to complete partial parsing
    * @return returns 1 or 0 to represent the number
    * passed into this function
    * @param value passed from parseT
    */
    static int parseT1(int x) {
		if (curr=="*") 
		{
		    next();
		    int y=parseF();
		    return parseT1(x*y);
		} 

		else if(curr=="+" || curr==")" || curr=="$") 
		{
		    return x;
		} 
		else 
		{
		    System.out.println("Unexpected :"+curr);
		    return x; // to make compiler happy
		}
    }

    /**
    * this function takes the value from 
    * parseE1 and outputs that value
    * in case we needed to get the value 
    * directly.
    * @return output value from parseE1
    */
     static int parseE() 
    {
		// E -> T E1
		int x=parseT();
		return parseE1(x);
    }

    /**
    *this parser method references parseT(),
    *and parseE1() to complete partial parsing
    * @return returns 1 or 0 to represent the number
    * passed into this function
    * @param value passed from parseT
    */
    static int parseE1(int x) 
    {
		if (curr=="+") 
		{
		    next();
		    int y = parseT();
		    return parseE1(x+y);
		} 
		else if(curr==")" || curr=="$" ) 
		{
		    return x;
		} 
		else 
		{
		    System.out.println("Unexpected :"+curr);
		    return x;
		}
    }

    /**
    * calls parseT1() to do further parsing
    * @return value outputted by parseT1
    */
    static int parseT() 
    {
		int x=parseF();
		return parseT1(x);
    }
    
    /**
    * calls parseE for further parsing
    * @return returns -1 if an error occurs
    * while parsing. 
    */
    static int parseF() 
    {
		if (curr=="(") 
		{
		    next();
		    int x=parseE();
		    if(curr==")") 
		    {
				next();
				return x;
		    } 
		    else 
		    {
				System.out.println(") expected.");
				return -1;
		    }
		} 

		else 
		{
			try 
			{
			    int x=Integer.valueOf(curr).intValue();
			    next();
			    return x;
			} 
			catch(NumberFormatException e) 
			{
			    System.out.println("Number expected.");
			    return -1; // to make compiler happy
			}
		}
	}

	/**
	* this method removes any existing quotation marks 
	* from string values
	* @param tokbuf the token buffer used so we can remove
	* quotation marks from the string
	**/
	public static String removeQuotationMarks(String tokbuf)
	{
		//first we check to see which type of quotation marks
		//we have, then we remove and return them.
		if (tokbuf.contains("\""))
		{
			tokbuf = tokbuf.replace("\"", "");
			return tokbuf;
		}
		if (tokbuf.contains("'"))
		{
			tokbuf = tokbuf.replace("'", "");
			return tokbuf;
		}
		//empty string if nothing was found
		return "";
	}

	/**
	* this program checks to see if the pascal program's first
	* non-comment command is the word 'program' followed by an ID
	* @param cStruct the ArrayList populated with data from languageScanner
	**/
	public static void isProgram(ArrayList<CharacterStruct> cStruct)
	{
		CharacterStruct characterstruct = cStruct.get(0);
		String temp = characterstruct.Identifier;
		if (temp.contains("PROGRAM"))
		{
			characterstruct = cStruct.get(1);
			// System.out.print("The name of the program is ");
			// System.out.println(characterstruct.Symbol);
		}
		else
		{
			isError = true;
			System.out.println("Please specify the name of the program");
			System.exit(0);
		}
	}

	/**
	* This program checks to see if there are a correct number of 
	* parantheses on each line of the program.
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	*  arbitrary number to signify the end of the loop used here).
	*/
	public static void isParenth(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		int parencount = 1;
		CharacterStruct characterstruct = cStruct.get(u);
		for (u = u; u < tokenCount; u++)
		{
			characterstruct = cStruct.get(u);
			temp = characterstruct.Identifier;
			if (parencount == 0)
			{
				break;
			}
			if (temp.contains("SEMICOLON"))
			{
				System.out.print("Missing right parenthese on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
			if (temp.contains("LPAREN"))
			{
				parencount++;
			}
			if (temp.contains("RPAREN"))
			{
				//not sure why, but this needs to be subtracted twice
				parencount--;
				parencount--;
				if (parencount == 0)
				{
					//System.out.println("there are a correct number of parentheses on this line");
					break;
				}
			}
		}
	}


		/**
	* This program checks to see if there are a correct number of 
	* square brackets on each line of the program.
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void isSquareBrack(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		int bracketcount = 1;
		CharacterStruct characterstruct = cStruct.get(u);
		for (u = u; u < tokenCount; u++)
		{
			characterstruct = cStruct.get(u);
			temp = characterstruct.Identifier;
			if (bracketcount == 0)
			{
				break;
			}
			if (temp.contains("SEMICOLON"))
			{
				System.out.print("Missing right parenthese on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
			if (temp.contains("LBRACKET"))
			{
				bracketcount++;
			}
			if (temp.contains("RBRACKET"))
			{
				//not sure why, but this needs to be subtracted twice
				bracketcount--;
				bracketcount--;
				if (bracketcount == 0)
				{
					//System.out.println("there are a correct number of parentheses on this line");
					break;
				}
			}
		}
	}

	/**
	* This function checks to see if an equation is valid
	* based off of the right and left operands of a math function
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void isEquation(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;
		int tempcount = u;

		while(true)
		{
			tempcount++;
			characterstruct = cStruct.get(tempcount);
			temp = characterstruct.Identifier;
			if (temp.contains("LBRACKET") || temp.contains("LPAREN"))
			{
				System.out.println("we continue searching");
				//we continue
			}
			if (temp.contains("NUMBER") || temp.contains("ID"))
			{
				//We want to find a number or ID so we break the loop
				System.out.println("breaking the loop");
				break;
			}
			else
			{
				System.out.print("Could not find a right values to the operand on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
		}

		//creating an infinite loop until broken
		while (true)
		{
			u--;
			characterstruct = cStruct.get(u);
			temp = characterstruct.Identifier;
			if (temp.contains("RBRACKET") || temp.contains("RPAREN"))
			{
				//we continue
			}
			if (temp.contains("NUMBER") || temp.contains("ID"))
			{
				//We want to find a number or ID so we break the loop
				break;
			}
			else
			{
				System.out.print("Could not find a left value to the operand on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
		}
	}

	public static void isIf(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;

		while (1)
		{
			
		}
	}

	/**
	* This program will ensure that values are initilized 
	* before they are used in a Pascal program
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void isInitialize(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		ArrayList initialized = new ArrayList();
		String temp;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;

		while(true)
		{
			u++;
			characterstruct = cStruct.get(u);
			temp = characterstruct.Identifier;
			if (!temp.contains("ID"))
			{
				System.out.print("Please provide an ID on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
			else
			{
				initialized.add(characterstruct.Symbol);

				u++;
				characterstruct = cStruct.get(u);
				temp = characterstruct.Identifier;
				if (temp.contains("COLON"))
				{
					u++;
					characterstruct = cStruct.get(u);
					temp = characterstruct.Identifier;
					if (temp.contains("INTEGER") || temp.contains("CHAR") || temp.contains("STRING") || temp.contains("CHR"))
					{
						u--;
						characterstruct = cStruct.get(u);
						initialized.add(characterstruct.DataType);
					}
				}
			}
		}
	}

	/**
	* once a variable is initialized, this function
	* checks to see what kind of type the variable becomes
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	*/
	public static void initialize (ArrayList<CharacterStruct> cStruct, int u,  int tokenCount)
	{
		String temp;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;

		//in theory we've already found the "VAR" in the main
		//so we don't need to have an if statement for it here.

		int varCount = 0;
		while(true)
		{
			//jump one ahead in our structure.
			u++;
			characterstruct = cStruct.get(u);
			temp = characterstruct.Symbol;

			if (temp.contains("ID"))
			{
				u++;
				characterstruct = cStruct.get(u);
				temp = characterstruct.Symbol;
				if (!temp.contains("COMMA") || !temp.contains("COLON"))
				{
					System.out.print("Expected separator between variables on line ");
					System.out.println(characterstruct.Line);
					isError = true;
					break;
				}
				//resetting our u value in case we don't get an error.
				u--;
				characterstruct = cStruct.get(u);
				temp = characterstruct.Symbol;
			}

			if (temp.contains("COMMA"))
			{
				//we do +2 so when we go back, we can skip the 
				//comma every time. 
				varCount = varCount + 2; 
			}

			if (temp.contains("COLON"))
			{
				u++;
				characterstruct = cStruct.get(u);
				temp = characterstruct.Symbol;
				if (!temp.contains("INTEGER") || !temp.contains("ARRAY") || !temp.contains("CHAR"))
				{
					System.out.print("Expected variable declaation here on line ");
					System.out.println(characterstruct.Line);
					isError = true;
					break;
				}
				//resetting our u value in case we don't get an error.
				u--;
				characterstruct = cStruct.get(u);
				temp = characterstruct.Symbol;
			}
			if (temp.contains("INTEGER") || temp.contains("ARRAY") || temp.contains("CHAR"))
			{
				if (temp.contains("INTEGER"))
				{
					u = u-2;
					cStruct.set(u, new CharacterStruct(characterstruct.Identifier, characterstruct.Symbol, characterstruct.Level, characterstruct.ProcedureName, "INTEGER", characterstruct.Value, characterstruct.Line));	
				}
				if (temp.contains("ARRAY"))
				{
					u = u-2;
					cStruct.set(u, new CharacterStruct(characterstruct.Identifier, characterstruct.Symbol, characterstruct.Level, characterstruct.ProcedureName, "ARRAY", characterstruct.Value, characterstruct.Line));	
				}
				if (temp.contains("CHAR"))
				{
					u = u-2;
					cStruct.set(u, new CharacterStruct(characterstruct.Identifier, characterstruct.Symbol, characterstruct.Level, characterstruct.ProcedureName, "CHAR", characterstruct.Value, characterstruct.Line));	
				}
			}
			else
			{
				System.out.print("Expected either type declaration or another variable on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
		}
	}

	/**
	* this function checks to see if the program and procedures
	* both have "Begin" statements
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void isBegin (ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;

		//get to the end of the line
		//assuming the user ended the line with a semicolon
		while(true)
		{
			u++;
			characterstruct = cStruct.get(u);
			temp = characterstruct.Identifier;
			if (temp.contains("PERIOD") || temp.contains("END"))
			{
				System.out.print("program or procedure does not have a \"begin\" statement on line ");
				System.out.println(characterstruct.Line);
				isError = true;
				break;
			}
			if (temp.contains("BEGIN"))
			{
				//we want to break and continue without continuing
				break;
			}
		}
		// u++;
		// characterstruct = cStruct.get(u);
		// temp = characterstruct.Identifier;
		// if (!temp.contains("BEGIN"))
		// {
		// 	System.out.print("program or procedure does not have a \"begin\" statement on line ");
		// 	System.out.println(characterstruct.Line);
		// 	isError = true;
		// }
	}
	
	/**
	* This function will check to see if the user
	* calls the "End." command at the end of the program
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void isEnd(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;

		if (!temp.contains("END"))
		{
			System.out.print("Program does not have an \"End\" statement on line ");
			System.out.println(characterstruct.Line);
			isError = true;
		}
		u++;
		characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;
		if (!temp.contains("PERIOD"))
		{
			System.out.print("\"End\" statement is missing a period on line ");
			System.out.println(characterstruct.Line);
			isError = true;
		}
	}

	/**
	* This function will write to the file
	* using the writeToText and writeTofile functions
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void iswrite(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		String temp;
		u++;
		CharacterStruct characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;

		//if a right parentheses does not exist
		if (!temp.contains("LPAREN"))
		{
			System.out.println("parentheses are required for write function on line ");
			System.out.print(characterstruct.Line);
			isError = true;
		}

		//right now we're not going to do any more error checking,
		//but hopefully we can check for file type and variables in the 
		//near future.
		u++;
		characterstruct = cStruct.get(u);
		temp = characterstruct.Identifier;
		if (temp.contains("STRING"))
		{
			characterstruct = cStruct.get(u);
			temp = characterstruct.Symbol;
			writeToData(temp);
		}
	}

	/**
	* This function will write any variables 
	* or non-declared values to the top of the 
	* .data portion of the MARS MIPS assembler
	* this current iteration only works for small programs
	* @var uses the global "outputfile" variable 
	* in order to select the location of the outgoing
	* file as specified by the user.
	*/
	public static void writeToData(String inputString)
	{
		File tmpDir = new File(outputfile);
		boolean exists = tmpDir.exists();
		BufferedWriter buffer = null;
		if (!tmpDir.isDirectory())
		{
			System.out.println(outputfile);
			System.out.println("File does not exist yet.");
			System.out.println("Creating file...");
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
		}
		else
		{
			System.out.println(outputfile);
			//at some point I plan on splitting this up into multiple methods that print
			//both the .data values and actual code separately. Including
			//checking types and actually making thsi code dynamic.
			try
			{
				buffer = new BufferedWriter(new FileWriter(outputfile, true));
				buffer.write("\t.data");
				buffer.newLine();
				buffer.write("string1:\t.asciiz\t\"");
				buffer.write(inputString);
				buffer.write("\\n\"");
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
			finally 
			{
				if (buffer != null) 
				{
					try 
					{
						buffer.close();
					}
					catch (IOException ioe2)
					{
						//ignore it
					}
				}
			}			
		}
	}

	/**
	* This function will output 
	* p-code to either the file specified
	* by the user, or to 'default.asm'.
	* Only assember code below the '.text'
	* market should be printed in this function.
	*/
	public static void writeToText(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
	{
		File tmpDir = new File(outputfile);
		boolean exists = tmpDir.exists();
		BufferedWriter buffer = null;
		if (!tmpDir.isDirectory())
		{
			System.out.println(outputfile);
			System.out.println("File does not exist yet.");
			System.out.println("Creating file...");
			PrintWriter writer = new PrintWriter(outputfile, "UTF-8");
		}
		else
		{
			try
			{
				String temp;
				CharacterStruct characterstruct = cStruct.get(u);
				temp = characterstruct.Identifier;	
				if (temp.contains("STRING"))
				{
					//specific code for printing something out
					buffer.write("\tli\t$v0, 4\n");
					buffer.write("\tla\t$a0, ");
					buffer.write(characterstruct.Symbol);
					buffer.newLine();
					buffer.write("\tsyscall");
					buffer.newLine();
					buffer.newLine();
				}

				if (temp.contains("LASTCHAR"))
				{
					//always use the following snippit to teminate the program.
					buffer.write("\tli\t$v0, 10");
					buffer.write("\t\nsyscall");
					buffer.flush();	
				}
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
			finally 
			{
				if (buffer != null) 
				{
					try 
					{
						buffer.close();
					}
					catch (IOException ioe2)
					{
						//ignore it
					}
				}
			}
		}
	}
}