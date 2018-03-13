package bibCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class BibCreator {
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
		
		
		//Opening all files, might throw a FileNotFoundException
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
		
		
		//Creating all files, might throw a FileNotFoundException
		try{
			for(int i = 0; i <  files.length; i++){
				String IEEEname = "", ACMname = "", NJname = "";
				//If files exists, create output file
				if(files[i].exists()){
					IEEEname = "IEEE" + (i + 1) + ".json";
					pwIEEE[i] = new PrintWriter(path + IEEEname);
				}else{
					System.out.println(IEEEname + "could not be created.");
				}
				
				if(files[i].exists()){
					ACMname = "ACM" + (i + 1) + ".json";
					pwACM[i] = new PrintWriter(path + ACMname);
				}else{
					System.out.println(ACMname + "could not be created.");
				}
				
				if(files[i].exists()){
					NJname = "NJ" + (i + 1) + ".json";
					pwNJ[i] = new PrintWriter(path + NJname);
				}else{
					System.out.println(NJname + "could not be created.");
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
