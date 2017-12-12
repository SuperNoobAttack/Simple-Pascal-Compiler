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
	public static boolean isError = false; 
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
				initialize(cStruct, u, tokenCount);
			}
			if (temp.contains("PROGRAM") || temp.contains("PROCEDURE") || temp.contains("FUNCTION"))
			{
				isBegin(cStruct, u, tokenCount);
			}
			if (temp.contains("WRITE"))
			{
				write(cStruct, u, tokenCount);
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

	/**
	* This program will ensure that values are initilized 
	* before they are used in a Pascal program
	* @param cStruct the ArrayList populated with data from languageScanner
	* @param u counter from the main program so we know where we are in the program
	* @param tokenCount from the main program, used so we know what the max
	* limit of the array is (this is primarily done to avoid using an  
	* arbitrary number to signify the end of the loop used here).
	*/
	public static void initialize(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
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

	public static void write(ArrayList<CharacterStruct> cStruct, int u, int tokenCount)
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
			//something like this...
			writeToData();
		}
	}

	/**
	* This function will write any variables 
	* or non-declared values to the top of the 
	* .data portion of the MARS MIPS assembler
	* @var uses the global "outputfile" variable 
	* in order to select the location of the outgoing
	* file as specified by the user.
	*/
	public static void writeToData()
	{
		try 
		{
			byte[] b = {1};

			// create a new RandomAccessFile with filename test
			RandomAccessFile raf = new RandomAccessFile(outputfile, "rw");

			// write a byte in the file
			raf.write(b);

			// set the file pointer at 0 position
			raf.seek(0);

			// print the Byte
			System.out.println("" + raf.readByte());
         
		} 
		catch (IOException ex) 
		{
        	ex.printStackTrace();
        }
	}
}