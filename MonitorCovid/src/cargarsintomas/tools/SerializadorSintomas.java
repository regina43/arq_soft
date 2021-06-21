package cargarsintomas.tools;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class SerializadorSintomas<T> {

  private final String nombrePaquete;
  private String nombreArchivo;

  public SerializadorSintomas(String nombreArchivo, String nombrePaquete) {
    this.nombreArchivo = nombreArchivo;
    this.nombrePaquete = nombrePaquete;
    this.nombreArchivo = getPath();
  }

  public void serializar(T shapes) {
    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nombreArchivo));
      out.writeObject(shapes);
      out.close();
      System.out.println("Datos guardados en: " + nombreArchivo);
    } catch (IOException e) {
      System.out.println("No se puede guardar el archivo: " + nombreArchivo);
    }
  }

  private String getPath(){
    File miDir = new File (".");
    String dir="";
    String path;
    String separador = System.getProperty("file.separator");
    try {
      dir= miDir.getCanonicalPath();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    boolean desarrollo = false;
    File file2 = new File(dir);
    String[] a = file2.list();

    for(int i=0; i<a.length; i++){
      if(a[i].equals("src")){
        desarrollo=true;
      }
    }

    if (!desarrollo){
      path = dir+separador+nombrePaquete+separador+nombreArchivo;
    } else {
      path = dir+separador+"src"+separador+nombrePaquete+separador+nombreArchivo;
    }
    return path;
  }

  public T deserializar() {
    T objeto = null;
    try {
      final FileInputStream fis = new FileInputStream(nombreArchivo);
      try (ObjectInputStream ois = new ObjectInputStream(fis)) {
        objeto = (T) ois.readObject();
        ois.close();
      }
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("\t[ El objeto no se pudo leer ]" + e);
    }
    return objeto;
  }
}
