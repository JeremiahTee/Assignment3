//-----------------------------------------------------
//Assignment #3
//TASK 2
//Written by: 
//Jeremiah Tiongson, 40055477
//Yun Shi Lin, 40055867
//-----------------------------------------------------

package bibCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class reads any amount of JSON formatted files (for example, .bib files) from a folder (specify path at inputFolder)
 * and creates three corresponding output files for each input file with these respective formats: IEEE, ACM, NJ. 
 * For example, a Latex1.bib input file containing one or more articles will be opened, read, and then processed
 * to create the corresponding IEEE1.json, ACM1.json, NJ1.json formatted output files.
 * <p>
 * Note: Input files containing empty fields are processed until the first occurence of empty field, then they are deleted.
 * @author Jeremiah Tiongson
 * @author Yun Shi
 * @version 1.0
 * @since 2018-03-12
 */
public class BibCreator {
	
	/** Keeps track of the number of invalid files*/
	public static int invalidCounter = 0;
	
	/** Keeps track of the number of valid files*/
	public static int validCounter;
	
	/** Keeps track of the number of articles in a file*/
	public static int numArticles = 0;

	/**TASK 5:
	 * This method processes an array of opened input files one by one using a for loop.
	 * The outer while loop reads the file at index i until it has no more lines.
	 * The inner while loop is entered when an article is found. When a closing curly brace is found,
	 * the inner while loop is broken. If an article contains an empty field, the outerloop is broken
	 * and we are back to the for loop to process the next file.
	 * 
	 * @param sc Array of Scanners representing opened files which will be processed for validation
	 * @param files	Passed as a parameter for processInvalid method
	 * @param pwIEE	Writes to IEEE formatted output files
	 * @param pwACM	Writes to ACM formatted output files
	 * @param pwNJ	Writes to NJ formatted output files
	 * @param badOutputList	Keeps track of indexes of invalid files that will eventually be deleted
	 */
	public static void processFilesForValidation(Scanner[] sc, File[] files, PrintWriter[] pwIEE, PrintWriter[] pwACM,
			PrintWriter[] pwNJ, List<Integer> badOutputList) {
		/** Regex pattern for @ARTICLE{*/
		Pattern startArticle = Pattern.compile("\\@ARTICLE\\{\\s*");
		/**Regex pattern for single }*/
		Pattern endArticle = Pattern.compile("^\\}\\s*");
		String currentLine = "";
		/**Matchers for beginning and end of article*/
		Matcher m1, m2;
		String author = " ", journal = " ", title = " ", year = " ", volume = " ", number = " ", pages = " ", doi = " ",
				month = " ";
		String[] authors = null;
		// Loop through array of Scanners
		// read file until the end
		for (int i = 0; i < sc.length; i++) {
			// Reset number of article 'seen' so far for next file
			numArticles = 0;
			outerloop: // label for the outer while loop
			while (sc[i].hasNextLine()) {
				currentLine = sc[i].nextLine();
				m1 = startArticle.matcher(currentLine);
				// marks the beginning of the article
				if (m1.matches()) {
					numArticles++; // first article has been accessed, increase counter, this is for the ACM format
					while (sc[i].hasNextLine()) {
						currentLine = sc[i].nextLine();
						// assign '}' matcher
						m2 = endArticle.matcher(currentLine);
						// break out of inner while loop when line starts with '}' (end of article)
						if (m2.matches()) {
							break;
						} else { // we're inside the article, retrieve the fields
							if (currentLine.contains("author")) {
								author = parse(currentLine);
								if (author.isEmpty()) {
									processInvalid(files, "author", i, badOutputList);
									break outerloop;
								} else {
									authors = author.split(" and ");
								}
							}
							if (currentLine.contains("journal")) {
								journal = parse(currentLine);
								if (journal.isEmpty()) {
									processInvalid(files, "journal", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("title")) {
								title = parse(currentLine);
								if (title.isEmpty()) {
									processInvalid(files, "title", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("year")) {
								year = parse(currentLine);
								if (year.isEmpty()) {
									processInvalid(files, "year", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("volume")) {
								volume = parse(currentLine);
								if (volume.isEmpty()) {
									processInvalid(files, "volume", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("number")) {
								number = parse(currentLine);
								if (number.isEmpty()) {
									processInvalid(files, "number", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("pages")) {
								pages = parse(currentLine);
								if (number.isEmpty()) {
									processInvalid(files, "pages", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("doi")) {
								doi = parse(currentLine);
								if (number.isEmpty()) {
									processInvalid(files, "doi", i, badOutputList);
									break outerloop;
								}
							}
							if (currentLine.contains("month")) {
								month = parse(currentLine);
								if (month.isEmpty()) {
									processInvalid(files, "month", i, badOutputList);
									break outerloop;
								}
							}
						}
					}
					// Printing to output files
					// IEEE format
					for (int j = 0; j < authors.length - 1; j++) {
						pwIEE[i].print(authors[j] + ", ");
					}
					pwIEE[i].print(authors[authors.length - 1] + ". \"" + title + "\", " + journal + ", vol. " + volume
							+ ", no. " + number + ", p. " + pages + ", " + month + " " + year + ".");
					pwIEE[i].println("\n");
					// ACM format
					pwACM[i].print("[" + numArticles + "] " + authors[0] + " et al. " + year + ". " + title + ". "
							+ journal + ". " + volume + ", " + number + " (" + year + "), " + pages
							+ ". DOI:https://doi.org/" + doi + ".");
					pwACM[i].println("\n");
					// NJ format
					for (int j = 0; j < authors.length - 1; j++) {
						pwNJ[i].print(authors[j] + " & ");
					}
					pwNJ[i].print(authors[authors.length - 1] + ". " + title + ". " + journal + ". " + volume + ", "
							+ pages + "(" + year + ").");
					pwNJ[i].println("\n");
				} // this brace marks the end of article
			} // this brace marks the end of the file
		}
	}

	/**
	 * This method processes an invalid input file, taking its index and adding it to the List of Integers badOutputlist.
	 *
	 * @param inputFiles Used to display the name of the invalid input file
	 * @param field Used to display the invalid field
	 * @param index Stored in the badOutputlist
	 * @param badOutputList the bad output list
	 * @exception Throws FileInvalidException and displays the error message indicating the corresponding invalid input file
	 */
	public static void processInvalid(File[] inputFiles, String field, int index, List<Integer> badOutputList) {
		try {
			invalidCounter++;
			badOutputList.add(index);
			System.out.println("Error: Detected Empty Field!\n================================");
			throw new FileInvalidException("Problem detected with input file " + inputFiles[index].getName()
					+ "\nFile is invalid: Field " + "\"" + field + "\" is Empty"
					+ "Processing stopped at this point. Other empty fields may be present as well!");
		} catch (FileInvalidException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This methods parses a line into the content of the field by using the String methods indexOf and substring.
	 *
	 * @param s The line that needs to be parsed
	 * @return The content of the field
	 */
	public static String parse(String s) {
		int start = s.indexOf('{');
		int end = s.indexOf('}');
		s = s.substring(start + 1, end);
		return s;
	}

	/**
	 * In the main method, Task 3, Task 4, and Task 7 are performed.
	 */
	public static void main(String[] args) {
		Scanner[] sc = null;
		PrintWriter[] pwIEEE = null;
		PrintWriter[] pwACM = null;
		PrintWriter[] pwNJ = null;
		File inputFolder = new File("C:\\Users\\Dabris\\Documents\\GitHub\\Assignment3\\input files");
		String pathOutput = "C:\\Users\\Dabris\\Documents\\GitHub\\Assignment3\\output files";

		// Creating an array of input files from the folder
		File[] inputFiles = inputFolder.listFiles();
		// Creating array of Scanner for reading & array of PrintWriter for output
		sc = new Scanner[inputFiles.length];
		pwIEEE = new PrintWriter[inputFiles.length];
		pwACM = new PrintWriter[inputFiles.length];
		pwNJ = new PrintWriter[inputFiles.length];
		// Creating Array List to track indexes of bad output (for Task 6)
		List<Integer> badOutputList = new ArrayList<>();
		int openErrorIndex = 0;

		/* TASK 3: Opening all input files, might throw a FileNotFoundException */
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				// If the file exist, open it
				if (inputFiles[i].exists()) {
					sc[i] = new Scanner(inputFiles[i]);
				} else {
					// will get the index of the last attempted opened file
					openErrorIndex = i;
					throw new FileNotFoundException();
				}
			}
		} catch (FileNotFoundException e) {
			System.out
					.println("Could not open input file" + inputFiles[openErrorIndex].getName() + " for reading. Please"
							+ " check if file exists! Program will terminate after closing any opened files.");
			// Closing all opened input files
			for (int i = 0; i < openErrorIndex; i++) {
				sc[i].close();
			}
			// Exit program
			System.exit(0);
		}

		/* TASK 4: Creating all output files, might throw a FileNotFoundException */
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				String IEEEname = "", ACMname = "", NJname = "";
				// If files exists, create output file
				if (inputFiles[i].exists()) {
					IEEEname = "IEEE" + (i + 1) + ".json";
					pwIEEE[i] = new PrintWriter(pathOutput + "\\" + IEEEname);
				} else {
					System.out.println(IEEEname + "could not be created.");
					throw new FileNotFoundException();
				}

				if (inputFiles[i].exists()) {
					ACMname = "ACM" + (i + 1) + ".json";
					pwACM[i] = new PrintWriter(pathOutput + "\\" + ACMname);
				} else {
					System.out.println(ACMname + "could not be created.");
					throw new FileNotFoundException();
				}

				if (inputFiles[i].exists()) {
					NJname = "NJ" + (i + 1) + ".json";
					pwNJ[i] = new PrintWriter(pathOutput + "\\" + NJname);
				} else {
					System.out.println(NJname + "could not be created.");
					throw new FileNotFoundException();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("There was an error creating files. Processing has stopped.");
			// Closing all scanners (MUST close before deleting)
			for (Scanner scan : sc) {
				scan.close();
			}
			// Deleting all files
			for (File file : inputFiles) {
				if (!file.isDirectory())
					file.delete();
			}
		}
		// At this point, all output files have been created, making array of File[] so
		// we can use delete()
		File outputFolder = new File(pathOutput);
		File[] outputFiles = outputFolder.listFiles();

		// Execution of core
		System.out.println("Welcome to Bib Creator! Programmed by Jeremiah Tiongson & Yun Shi Lin");
		processFilesForValidation(sc, inputFiles, pwIEEE, pwACM, pwNJ, badOutputList);
		validCounter = sc.length - invalidCounter;

		// Closing all scanners
		for (int i = 0; i < sc.length; i++) {
			sc[i].close();
		}
		// Closing all pwIEEE
		for (int i = 0; i < pwIEEE.length; i++) {
			pwIEEE[i].close();
		}
		// Closing all pwACM
		for (int i = 0; i < pwIEEE.length; i++) {
			pwACM[i].close();
		}
		// Closing all pwNJ
		for (int i = 0; i < pwIEEE.length; i++) {
			pwNJ[i].close();
		}
		/* TASK 6: Deleting bad output files */
		for (int i = 0; i < badOutputList.size(); i++) {
			for (int j = 0; j < outputFiles.length; j++) {
				if (outputFiles[j].getName().contains(Integer.toString(badOutputList.get(i)))) {
					// Uncomment for console output
					// System.out.println("Deleting " + outputFiles[j].getName() + " : ");
					// System.out.print(outputFiles[j].delete() + "\n");
					outputFiles[j].delete();
				}
			}
		}
		System.out.println("A total of " + invalidCounter + " files were invalid, and could not be processed. All other"
				+ validCounter + " \"Valid\" files have been created.");
		/* TASK 7 */
		// Scanner for user input
		Scanner keyboard = new Scanner(System.in);
		// BufferedReader for the file to display
		BufferedReader display = null;
		String toDisplay = null;
		// Trying to open a output file specified by user
		try {
			System.out.print("Please enter the name of one of the files that you need to review: ");
			toDisplay = keyboard.next();
			File userChoice = new File(pathOutput + "\\" + toDisplay);
			display = new BufferedReader(new FileReader(userChoice));
			String line;
			System.out.println("Here are the contents of the successfully created .json file: " + userChoice);
			while ((line = display.readLine()) != null) {
				System.out.println(line);
			}
			display.close();
			keyboard.close();
		} catch (FileNotFoundException exc) {
			exc.getMessage();
			System.out.println("Could not open input file. File does not exist; possibly it could not be created!"
					+ "\n\nHowever, you will be allowed another chance to enter another file name.");
			toDisplay = keyboard.nextLine();
			try {
				System.out.print("Please enter the name of one of the files that you need to review: ");
				toDisplay = keyboard.nextLine();
				File userChoice = new File(pathOutput + "\\" + toDisplay);
				display = new BufferedReader(new FileReader(userChoice));
				String line;
				System.out.println("Here are the contents of the successfully created .json file: " + userChoice);
				while ((line = display.readLine()) != null) {
					System.out.println(line);
				}
				display.close();
				keyboard.close();
			} catch (FileNotFoundException ex) {
				System.out.println(
						"\nCould not open input file again! Either file does not exist or could not be created."
								+ "\n Sorry! I am unable to display your desired files! Program will exit!");
				System.exit(0);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Goodbye! Hope you have enjoyed creating the needed files using BibCreator.");
	}
}
