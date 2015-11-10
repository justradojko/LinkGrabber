package com.radojko.linkGrabber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Stack;

public class ReadTerms implements Runnable{
	private Stack<String> terms = new Stack<String>();
	
	private void fillStack(){
		try (BufferedReader br = new BufferedReader(new FileReader(new File("terms.txt")))){
			String line;
			while( (line = br.readLine()) != null){
				terms.push(line);
			}
			System.out.println("Number of read terms is: " + terms.size());
		} catch (Exception e) {
			System.out.println("File 'terms.txt' cannot be located");
			e.printStackTrace();
		}
	}
	
	public int getSizeOfStack(){
		return terms.size();
	}
	
	public String grabTerm(){
		return (terms.empty() == true ?  null : terms.pop());
	}
	
	public void run(){
		fillStack();
	}
}
