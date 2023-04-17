package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {
    
    private int Id;
    private String title;
    private String category;
    private String duration;
    private String author;
    private String publishDate;

    // Constructor vacio
    public Model() {}

    // Constructor
    public Model(int Id, String title, String category, String duration, String author, String publishDate) {
        this.Id = Id;
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.author = author;
        this.publishDate = publishDate;
    }

    // Constructor de sobrecarga
    public Model(Model model) {
        this.Id = model.Id;
        this.title = model.title;
        this.category = model.category;
        this.duration = model.duration;
        this.author = model.author;
        this.publishDate = model.publishDate;
    }
    
    /*
        Methods Getter and Setter
    */

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
   
    /*
        Métodos del Modelo relacionados con las listas
    */
    
    /*
        Este método crea un archivo de texto dentro del proyecto,
        primero verfica si ya existe porque si lo sobre escribe
        se pierden los datos
    */
    public void CreateFiles() {
        File OfileVideos = new File("FileVideos.txt");

        if (!OfileVideos.exists()) {
            try {
                PrintWriter salida = new PrintWriter("FileVideos.txt");
                salida.close();
                System.out.println("Archivo creado");
            } catch (FileNotFoundException ex) {
                System.out.println("Error al crear el archivo" + ex);
            }
        }
    }
    
    /*
        Este el metodo que dice si el archivo esta vacio,
        validando su longitud, es util a la hora de leer el archivo.
    */
    public boolean EmptyFile() {
        File OfileVideos = new File("FileVideos.txt");
        boolean empty = OfileVideos.length() == 0;
        return empty;
    }

    /*
        Este método crea la liste el inserta el primer objeto,
        cual debe ser pasado como un parametro.
        Adicionalmente escribe la lista en el archivo para cuando se lea, 
        utilizar la informacioón
    */
    public void CreateList(Model model) {
        ArrayList<Model> listVideos = new ArrayList<>();
        listVideos.add(new Model(
                model.getId(), 
                model.getTitle(),
                model.getCategory(),
                model.getDuration(),
                model.getAuthor(), 
                model.getPublishDate()
        ));
        WriteFileUsers(listVideos);
    }

    /*
        El método para leer archivos primero verifca si esta vacio, en dicho 
        caso devuelve una lista vacia. De lo contrario decodifica el contenido
        del archivo y vuelve a ensamblar la lista de videos, y la retorna
    */
    public ArrayList<Model> ReadFileUsers() {
        ArrayList<Model> listVideos = new ArrayList<>();
        try {
            if (EmptyFile()) {
                return listVideos;
            }
            FileInputStream fis = new FileInputStream("FileVideos.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Model> listAux = (ArrayList<Model>) ois.readObject();
            ois.close();
            fis.close();
            return listAux;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error al leer la lista" + ex);
        }
        System.out.println("Esto jamas se ejecuta, se supone :)");
        return listVideos;
    }

    /*
        Este método sobre escribe la lista que tenemos en el archivo
        con la lista que le pasemos como parametro
    */
    public void WriteFileUsers(ArrayList<Model> listVideos) {
        try {
            FileOutputStream fos = new FileOutputStream("FileVideos.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listVideos);
            oos.close();
            fos.close();
        } catch (IOException ex) {
            System.out.println("Error al guardar la lista: " + ex);
        }
    }
}