package cargarregistros.tools;

import monitor.Registros;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class SerializadorRegistros {

  private final String nombrePaquete;
  private String nombreArchivo;
  private final String nombreSimple;

  public SerializadorRegistros(String nombreArchivo, String nombrePaquete) {
    this.nombreSimple = nombreArchivo;
    this.nombreArchivo = nombreArchivo;
    this.nombrePaquete = nombrePaquete;
    this.nombreArchivo = getPath();
  }

  public void serializar(Registros registros) {
    try {
      ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(nombreArchivo));
      outputStream.writeObject(registros);
      outputStream.close();
    } catch (IOException ioException) {
      System.out.println("No se puede guardar el archivo: " + nombreSimple);
    }
  }

  private String getPath() {
    File miDir = new File(".");
    String dir = "";
    String path;
    String separador = System.getProperty("file.separator");
    try {
      dir = miDir.getCanonicalPath();
    } catch (Exception e) {
      e.printStackTrace();
    }

    boolean desarrollo = false;
    File file2 = new File(dir);
    String[] a = file2.list();

    if (a != null) {
      for (String s : a) {
        if (s.equals("src")) {
          desarrollo = true;
          break;
        }
      }
    }

    if (!desarrollo) {
      path = dir + separador + nombrePaquete + separador + nombreArchivo;
    } else {
      path = dir + separador + "src" + separador + nombrePaquete + separador + nombreArchivo;
    }
    return path;
  }

  public Registros deserializar() {
    Registros registros = null;
    try {
      final FileInputStream fis = new FileInputStream(nombreArchivo);
      try (ObjectInputStream ois = new ObjectInputStream(fis)) {
        registros = (Registros) ois.readObject();
        ois.close();
      }
    } catch (IOException | ClassNotFoundException exception) {
      System.err.println("\t[ El objeto no se pudo leer ]");
    }
    return registros;
  }

  public boolean existeArchivo() {
    File file = new File(this.nombreArchivo);
    return file.exists();
  }
}
