//-----------------------------------------------------
//Assignment #3
//COMP249
//Written by: 
//Jeremiah Tiongson, 40055477
//Yun Shi Lin, 40055867
//Due date: 19/03/2018
//-----------------------------------------------------

package bibCreator;
/**
 * FileInvalidException class
 * @author Jeremiah Tiongson & Yun Shi Lin
 * @version 1.0
 * @since 2018-03-12
 */
public class FileInvalidException extends Exception{
	
	/**
	 * Default FileInvalidException constructor with default error message.
	 */
	public FileInvalidException(){
		super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc.)");
	}
	
	/**
	 * Parametrized FileInvalidException constructor with parameter message for custom error.
	 * @param message: the error message
	 */
	public FileInvalidException(String message){
		super(message);
	}
}
