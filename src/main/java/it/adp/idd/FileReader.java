package it.adp.idd;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    private List<File> readFilesTxt(String path) throws Exception{
        File directory = new File(path);
        List<File> output= new ArrayList<>();
        // Verifica se il percorso esiste ed è una directory
        if (directory.exists() && directory.isDirectory()) {
            // Ottieni elenco dei file nella directory
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        // Leggi e stampa il nome del file .txt
                        System.out.println("File .txt trovato: " + file.getName());
                        output.add(file);
                    }
                }
            } else {
                throw new Exception("La directory è vuota.");
            }
        } else {
            throw new Exception("Percorso non valido o non è una directory.");
        }
        if(output.isEmpty()){
            throw new Exception("La directory non contiene file txt");
        }
        return output;
    }

    public List<Document> getDocuments(String path) throws Exception {
        List<File> allFileTxt= readFilesTxt(path);
        List<Document> allDocuments= new ArrayList<>();
        for (File file : allFileTxt){
            Document doc = new Document();
            doc.add(new TextField("titolo", file.getName(), Field.Store.YES));
            String contenuto = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            doc.add(new TextField("contenuto", contenuto, Field.Store.YES));
            allDocuments.add(doc);
        }
        return allDocuments;
    }
}
