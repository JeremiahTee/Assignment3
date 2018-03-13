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
			for(int i = 0; i < openErrorIndex; i++){
				sc[i].close();
			}
			//Exit program
			System.exit(0);
		}
		
		
		//Creating all files, might throw a FileNotFoundException
		try{
			for(int i = 1; i <= files.length; i++){
				//If files exists, create output file
				if(files[i].exists()){
					String IEEEname = "IEEE" + i + ".json";
					pwIEEE[i] = new PrintWriter(path + IEEEname);
				}else{
					
				}
				
				if(files[i].exists()){
					String ACMname = "ACM" + i + ".json";
					pwACM[i] = new PrintWriter(path + ACMname);
				}else{
					
				}
				
				if(files[i].exists()){
					String NJname = "NJ" + i + ".json";
					pwNJ[i] = new PrintWriter(path + NJname);
				}else{
					
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("");
		} 
	}
}
