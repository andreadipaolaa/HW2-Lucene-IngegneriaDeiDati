package it.adp.idd;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static String docsPath= System.getProperty("user.dir") + "/docs";
    public static String indexPath= System.getProperty("user.dir") + "/index";


    public static void main(String[] args) {
        Indexer indexer=new Indexer();
        try{
            indexer.indexDocs(indexPath, null, docsPath);
        }
        catch (Exception e){
            System.out.println("C'è stato un errore durante l'indicizzazione dei files: " + e.getMessage());
        }
        System.out.println("Il programma ha indicizzato i file txt nella cartella docs. Ora puoi effettuare delle queries per testare il sistema");
        Searcher searcher= new Searcher();
        Scanner scanner = new Scanner(System.in);
        String input;
        try{
            while(true){
                System.out.println("inserisci di seguito la tua query (es. contenuto/titolo: frase o termine chiave) oppure exit se vuoi uscire");
                input = scanner.nextLine();
                Query query= null;
                if (input.startsWith("titolo:")) {
                    String titolo = input.substring(7);
                    query= new QueryParser("titolo", new SimpleAnalyzer()).parse(titolo);
                } else if (input.startsWith("contenuto:")) {
                    String contenuto = input.substring(10);
                    query= new QueryParser("contenuto", new StandardAnalyzer()).parse(contenuto);
                } else if (input.equals("exit")) {
                    System.out.println("Uscita dal programma.");
                    break;
                } else {
                    System.out.println("Input non valido. Riprova.");
                }

                if(query!=null)
                    searcher.executeQuery(query,indexPath);

            }
        }
        catch (Exception e){
            System.out.println("C'è stato un errore durante la ricerca: " + e.getMessage());

        }
        scanner.close();



    }

}