package cargarsintomas.tools;

import monitor.Sintoma;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LectorDePaquete {

  private final String nombrePaquete = "sintomas";

  public List<String> getNombresClases() {
    List<String> classNames;
    File[] classes = this.getFilesPaquete();
    if (classes == null) {
      classNames = getNombresClasesJar();
    } else {
      classNames = getNombresClasesClass(classes);
    }
    return classNames;
  }

  private List<String> getNombresClasesClass(File[] classes) {
    List<String> classNames = new ArrayList<>();
    Class<Sintoma> sintomaClass = Sintoma.class;
    for (File clase : classes) {
      try {
        String nombreClase = clase.getName().split("\\.")[0];
        Class.forName(nombrePaquete + "." + nombreClase).asSubclass(sintomaClass);
        classNames.add(nombreClase);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    return classNames;
  }

  private List<String> getNombresClasesJar() {
    List<String> classNames = new ArrayList<>();
    try {
      ZipInputStream zip = new ZipInputStream(new FileInputStream("home.jar"));
      for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
          String className = entry.getName().replace('/', '.');
          if (className.split("\\.")[0].equals("sintomas")) {
            classNames.add(className.split("\\.")[1]);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return classNames;
  }

  private File[] getFilesPaquete() {
    File dir = null;
    File[] archivos = null;
    try {
      Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
              .getResources(nombrePaquete);
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        dir = new File(url.getFile());
      }
      archivos = Objects.requireNonNull(dir).listFiles();
    } catch (IOException | NullPointerException e) {

    }
    return archivos;
  }

}

