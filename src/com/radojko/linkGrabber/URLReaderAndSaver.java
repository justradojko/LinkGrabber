package com.radojko.linkGrabber;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class URLReaderAndSaver implements Runnable {	
	private static int termsProcessed = 0;
	private final String BASE = "http://www.nature.com/opensearch/request?httpAccept=application/json&query=";
	private String term;
	
    public static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.configure();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return configureSessionFactory();
    }
    
    

	public URLReaderAndSaver(String term) {
		this.term = term;
	}
	
	private synchronized void increaseTermsProcessed(){
		termsProcessed++;
	}

	private void readURL() {
		try {
			increaseTermsProcessed();
			
			System.out.println("Getting " + termsProcessed + ". link for '" + term + "' term");
			
			URL url = new URL(BASE + term);

			StringBuilder stringBuilder = new StringBuilder();
			String line;

			BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));

			while ((line = br.readLine()) != null) {
				stringBuilder.append(line);
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(stringBuilder.toString());

			String link = jsonNode.get("feed").get("entry").get(0).get("link").toString();
//			System.out.println("JsonNod: " + link);
			
			saveLinkAndTermToFileAndDatabase(new TermsLinks(term, link));
		} catch (MalformedURLException e) {
			System.out.println("URL is not defined properly");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problem grabbing content from URL");
			e.printStackTrace();
		}
	}
	
	private synchronized void saveLinkAndTermToFileAndDatabase(TermsLinks o){
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("termsAndLinks.txt"), true))){
			bufferedWriter.write("Link for term " + o.getTerm() + ": " + o.getLink() + "\n");
		} catch (Exception e) {
			System.out.println("Cannot open File for writing");
		}
		
		Session session = null; 
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
			System.out.println("Term and link is saved to Database");
		} catch (Exception e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		} finally{
			System.out.println("Cloasing session");
			session.close();
		}
	}

	public void run() {
		readURL();
	}
}













