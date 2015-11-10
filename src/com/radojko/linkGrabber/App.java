package com.radojko.linkGrabber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {

	public static void main(String[] args) {
		URLReaderAndSaver.sessionFactory = URLReaderAndSaver.getSessionFactory();
		
		ReadTerms readTerms = new ReadTerms();
		
		Thread threadFillStack = new Thread( readTerms);
		threadFillStack.start();
		
		try {
			threadFillStack.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(readTerms.getSizeOfStack());
		
		ExecutorService executor = Executors.newFixedThreadPool(3);
		String term;
		
		while( (term = readTerms.grabTerm()) != null ){
			executor.submit(new URLReaderAndSaver(term));
		}
		
		executor.shutdown();
		try {
			executor.awaitTermination(30, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
