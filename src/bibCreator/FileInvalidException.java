package bibCreator;

//-----------------------------------------------------
//Assignment #3
//TASK 2
//Written by: 
//Jeremiah Tiongson, 40055477
//Yun Shi Lin, 40055867
//-----------------------------------------------------

/**
 * This class extends the Exception class and contains a default constructor, containing a default message
 * and a parametrized constructor taking a String as a parameter and calling the Exception class' parametrized constructor
 * through super.
 * @author Jeremiah Tiongson
 * @author Yun Shi Lin
 *
 */
public class FileInvalidException extends Exception{
	/**
	 * Default FileInvalidException constructor with default error message
	 */
	public FileInvalidException(){
		super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc.)");
	}
	
	/**
	 * Parametrized FileInvalidException constructor with parameter message for custom error
	 */
	public FileInvalidException(String message){
		super(message);
	}
}
