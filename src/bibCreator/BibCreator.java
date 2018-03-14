package bibCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BibCreator {
	public static int invalidCounter;
	
	//TASK 5
	public static void processFilesForValidation(Scanner[] sc) {
		//regex pattern for @ARTICLE{
		Pattern startArticle = Pattern.compile("\\@ARTICLE\\{\\s*");
		//regex pattern for single { 
		Pattern endArticle = Pattern.compile("^\\{");
		String currentLine = "";
		Matcher m1, m2;
		String author = " ", journal = " ", title = " ", year = " ", volume = " ", number = " ", pages = " ",
				doi = " ", month = " ";
		//Loop through array of Scanners
		for(int i = 0; i < sc.length; i++) {
			//read file until the end
			while(sc[i].hasNextLine()){
				currentLine = sc[i].nextLine();
				m1 = startArticle.matcher(currentLine);
				//marks the beginning of the article
				if(m1.matches()){
					while(true){
						currentLine = sc[i].nextLine();
						//assign '{' matcher
						m2 = endArticle.matcher(currentLine);
						//break out of while loop when line starts with '{'  (end of article)
						if(m2.matches()) {
							break;
						}else { //if we're inside the article, process it
							if(currentLine.contains("author")) {
								//chop authors
								//make authors array
								//use array to build String, use StringBuilder (?)
							}else if(currentLine.contains("journal")) {
								if(currentLine.isEmpty()) {
									
								}else {
									journal = parse(currentLine);
								}
							}else if(currentLine.contains("title")) {
								if(currentLine.isEmpty()) {
									
								}else {
									title = parse(currentLine);
								}
							}else if(currentLine.contains("year")) {
								if(currentLine.isEmpty()) {
									
								}else {
									year = parse(currentLine);
								}
							}else if(currentLine.contains("volume")) {
								if(currentLine.isEmpty()) {
									
								}else {
									volume = parse(currentLine);
								}
							}else if(currentLine.contains("number")) {
								if(currentLine.isEmpty()) {
									
								}else {
									number = parse(currentLine);
								}
							}else if(currentLine.contains("pages")) {
								if(currentLine.isEmpty()) {
									
								}else {
									pages = parse(currentLine);
								}
							}else if(currentLine.contains("doi")) {
								if(currentLine.isEmpty()) {
									
								}else {
									doi = parse(currentLine);
								}
							}else if(currentLine.contains("month")) {
								if(currentLine.isEmpty()) {
									
								}else {
									month = parse(currentLine);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean isEmpty(String s){
		return s.equals("");
	}
	
	public static String parse(String s) {
		int start = s.indexOf('{');
		int end = s.indexOf('}');
		s  = s.substring(start + 1, end);
		return s;
	}
	
	public static void main(String[] args) {
		Scanner[] sc = null;
		PrintWriter[] pwIEEE = null;
		PrintWriter[] pwACM = null;
		PrintWriter[] pwNJ = null;
 		File folder = new File("C:\\Users\\Jeremiah\\workspace\\Assignment3\\files");
 		String path = "C:\\Users\\Jeremiah\\workspace\\Assignment3\\output files\\";
		
		//Creating an array of files from the folder
		File[] files = folder.listFiles();
		//Creating array of Scanner for reading & array of PrintWriter for output
		sc = new Scanner[files.length];
		pwIEEE = new PrintWriter[files.length];
		pwACM = new PrintWriter[files.length];
		pwNJ = new PrintWriter[files.length];
		int openErrorIndex = 0;
		//Fields used
		String author, journal, title, volume, pages, doi, month;
		int year;
		
		//TASK 3: Opening all input files, might throw a FileNotFoundException
		try{
			for(int i = 0; i < files.length; i++){
				//If the file exist, open it
				if(files[i].exists()){
					sc[i] = new Scanner(files[i]);
				}else{
					//will get the index of the last attempted opened file
					openErrorIndex = i;
					throw new FileNotFoundException();
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Could not open input file" + files[openErrorIndex].getName() + " for reading. Please"
					+ " check if file exists! Program will terminate after closing any opened files.");
			//Closing all opened input files
			for(int i = 0; i < openErrorIndex; i++){
				sc[i].close();
			}
			//Exit program
			System.exit(0);
		}
		
		//TASK 4: Creating all output files, might throw a FileNotFoundException
		try{
			for(int i = 0; i <  files.length; i++){
				String IEEEname = "", ACMname = "", NJname = "";
				//If files exists, create output file
				if(files[i].exists()){
					IEEEname = "IEEE" + (i + 1) + ".json";
					pwIEEE[i] = new PrintWriter(path + IEEEname);
				}else{
					System.out.println(IEEEname + "could not be created.");
					throw new FileNotFoundException();
				}
				
				if(files[i].exists()){
					ACMname = "ACM" + (i + 1) + ".json";
					pwACM[i] = new PrintWriter(path + ACMname);
				}else{
					System.out.println(ACMname + "could not be created.");
					throw new FileNotFoundException();
				}
				
				if(files[i].exists()){
					NJname = "NJ" + (i + 1) + ".json";
					pwNJ[i] = new PrintWriter(path + NJname);
				}else{
					System.out.println(NJname + "could not be created.");
					throw new FileNotFoundException();
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("There was an error creating files. Processing has stopped.");
			//Deleting all files
			for(File file: files) 
			    if (!file.isDirectory()) 
			        file.delete();
			
			for(Scanner scan : sc) {
				scan.close();
			}
		}
	}
}
