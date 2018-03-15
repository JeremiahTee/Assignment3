package bibCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BibCreator {
	public static int invalidCounter;
	public static int validCounter;
	public static String lineSeperator = "\n===================================================\n";
	public static String articleSeperator = "\n-----------------------------------------------\n";
	public static int numArticles = 0;

	// TASK 5
	public static void processFilesForValidation(Scanner[] sc, File[] files, PrintWriter[] pwIEE, PrintWriter[] pwACM,
			PrintWriter[] pwNJ, File[] outputFiles) {
		// Regex pattern for @ARTICLE{
		Pattern startArticle = Pattern.compile("\\@ARTICLE\\{\\s*");
		// Regex pattern for single }
		Pattern endArticle = Pattern.compile("^\\}\\s*");
		String currentLine = "";
		Matcher m1, m2;
		String author = " ", journal = " ", title = " ", year = " ", volume = " ", number = " ", pages = " ", doi = " ",
				month = " ";
		List<String> authors = null;
		// Loop through array of Scanners
		// read file until the end
		for (int i = 0; i < sc.length; i++) {
			//Reset number of article 'seen' so far for next file
			numArticles = 0; 
			System.out.println("Reading file " + files[i].getName());
			outerloop: // label for the outerloop
			while (sc[i].hasNextLine()) {
				currentLine = sc[i].nextLine();
				m1 = startArticle.matcher(currentLine);
				// marks the beginning of the article
				if (m1.matches()) {
					numArticles++; //first article has been accessed, increase counter
					while (sc[i].hasNextLine()) {
						currentLine = sc[i].nextLine();
						// assign '}' matcher
						m2 = endArticle.matcher(currentLine);
						// break out of while loop when line starts with '}' (end of article)
						if (m2.matches()) {
							break;
						} else { // if we're inside the article, process it
							if (currentLine.contains("author")) {
								author = parse(currentLine);
								if (author.isEmpty()) {
									processInvalid(files, "author", i, outputFiles);
									break outerloop;
								} else {
									authors = Arrays.asList(author.split(" and "));
								}
							}

							if (currentLine.contains("journal")) {
								journal = parse(currentLine);
								if (journal.isEmpty()) {
									processInvalid(files, "journal", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("title")) {
								title = parse(currentLine);
								if (title.isEmpty()) {
									processInvalid(files, "title", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("year")) {
								year = parse(currentLine);
								if (year.isEmpty()) {
									processInvalid(files, "year", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("volume")) {
								volume = parse(currentLine);
								if (volume.isEmpty()) {
									processInvalid(files, "volume", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("number")) {
								number = parse(currentLine);
								if (number.isEmpty()) {
									processInvalid(files, "number", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("pages")) {
								pages = parse(currentLine);
								if (number.isEmpty()) {
									processInvalid(files, "pages", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("doi")) {
								doi = parse(currentLine);
								if (number.isEmpty()) {
									processInvalid(files, "doi", i, outputFiles);
									break outerloop;
								}
							}

							if (currentLine.contains("month")) {
								month = parse(currentLine);
								if (month.isEmpty()) {
									processInvalid(files, "month", i, outputFiles);
									break outerloop;
								}
							}
						}
					}
					////////Y-S NEW FORMATTING
					// IEEE format
					System.out.println("IEEE format");
					for (int j = 0; j < authors.size() - 1; j++) {
						System.out.print(authors.get(j) + ", ");
					}
					System.out.print(authors.get(authors.size() - 1)+ ". \"" + title + "\", " + journal + ", vol. " + volume + ", no. "
							+ number + ", p. " + pages + ", " + month + " " + year + ".");
					System.out.println("\n----------------------------------");

					// ACM format
					System.out.println("ACM format");
					System.out.print("[" + numArticles + "]" + authors.get(0) + " et al. " + year + ". " + title + ". " + journal
							+ ". " + volume + ", " + number + " (" + year + "), " + pages + ". DOI:https://doi.org/" + doi + ".");
					System.out.println("\n----------------------------------");

					// NJ format
					System.out.println("NJ format");
					for (int j = 0; j < authors.size() - 1; j++) {
						System.out.print(authors.get(j) + " & ");
					}
					System.out.print(authors.get(authors.size() - 1) + ". " + title + ". " + journal + ". " + volume + ", "
							+ pages + "(" + year + ").");
					System.out.println("\n----------------------------------");
				} // this brace marks the end of article
			} // this brace marks the end of the file
			System.out.println(articleSeperator + "End of the file." + lineSeperator);
		}
	}

	public static void processInvalid(File[] inputFiles, String field, int index, File[] outputFiles) {
		try {
			invalidCounter++;
			delete(outputFiles, index);
			System.out.println("Error: Detected Empty Field!\n================================");
			throw new FileInvalidException("Problem detected with input file " + inputFiles[index].getName()
					+ "\nFile is invalid: Field " + "\"" + field + "\" is Empty"
					+ "Processing stopped at this point. Other empty fields may be present as well!\n");
		} catch (FileInvalidException e) {
			System.out.println(e.getMessage());
		}
	}

	public static String parse(String s) {
		int start = s.indexOf('{');
		int end = s.indexOf('}');
		s = s.substring(start + 1, end);
		return s;
	}

	public static void delete(File[] outputFiles, int index) {
		for(int i = 0; i < outputFiles.length; i++) {
			if (outputFiles[i].getName().contains(Integer.toString(index))) {
				//Trying to rename every bad file to "toDelete"
				//This doesn't work somehow... if you can find out why... you're the MVP
				File toBeDeleted = new File("E:\\Programs\\eclipse-workspace\\Assignment3\\output files\\toDelete");
				outputFiles[i].renameTo(toBeDeleted);
			}
		}
	}

	public static void main(String[] args) {
		Scanner[] sc = null;
		PrintWriter[] pwIEEE = null;
		PrintWriter[] pwACM = null;
		PrintWriter[] pwNJ = null;
		File inputFolder = new File("E:\\Programs\\eclipse-workspace\\Assignment3\\files");
		String pathOutput = "E:\\Programs\\eclipse-workspace\\Assignment3\\output files";

		// Creating an array of input files from the folder
		File[] inputFiles = inputFolder.listFiles();
		// Creating array of Scanner for reading & array of PrintWriter for output
		sc = new Scanner[inputFiles.length];
		pwIEEE = new PrintWriter[inputFiles.length];
		pwACM = new PrintWriter[inputFiles.length];
		pwNJ = new PrintWriter[inputFiles.length];
		int openErrorIndex = 0;

		// TASK 3: Opening all input files, might throw a FileNotFoundException
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
			System.out.println("Could not open input file" + inputFiles[openErrorIndex].getName() + " for reading. Please"
					+ " check if file exists! Program will terminate after closing any opened files.");
			// Closing all opened input files
			for (int i = 0; i < openErrorIndex; i++) {
				sc[i].close();
			}
			// Exit program
			System.exit(0);
		}

		// TASK 4: Creating all output files, might throw a FileNotFoundException
		try {
			for (int i = 0; i < inputFiles.length; i++) {
				String IEEEname = "", ACMname = "", NJname = "";
				// If files exists, create output file
				if (inputFiles[i].exists()) {
					IEEEname = "IEEE" + (i + 1) + ".json";
					pwIEEE[i] = new PrintWriter(pathOutput + "\\" +  IEEEname);
				} else {
					System.out.println(IEEEname + "could not be created.");
					throw new FileNotFoundException();
				}

				if (inputFiles[i].exists()) {
					ACMname = "ACM" + (i + 1) + ".json";
					pwACM[i] = new PrintWriter(pathOutput + "\\" +  ACMname);
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
			// Deleting all files
			for (File file : inputFiles)
				if (!file.isDirectory())
					file.delete();

			for (Scanner scan : sc) {
				scan.close();
			}
		}
		
		//At this point, all output files have been created, making array of File[] so we can use delete()
		File outputFolder = new File(pathOutput);
		File[] outputFiles = outputFolder.listFiles();
		
		//Execution of core
		System.out.println("Welcome to Bib Creator! Programmed by Jeremiah Tiongson & Yun Shi Lin" + lineSeperator);
		processFilesForValidation(sc, inputFiles, pwIEEE, pwACM, pwNJ, outputFiles);
		validCounter = sc.length - invalidCounter;
		System.out.println(
				"A total of " + invalidCounter + " files were invalid, and could not be processed. All other + "
						+ validCounter + " \"Valid\" files have been created.");
		
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
		
		System.out.println("Printing output file names: ");
		for(File file : outputFiles) {
			System.out.println(file.getName());
		}
		
		for(int i = 0; i < outputFiles.length; i++) {
			if(outputFiles[i].getName().equals("toDelete")) {
				System.out.println("Deleting " + outputFiles[i].getName());
				System.out.println(outputFiles[i].delete());
			}
		}
	}
}
