package cargarsintomas.tools;

import monitor.Sintoma;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class LectorDePaquete {

  public List<String> getNombresClases(String nombrePaquete) throws IOException {
    List<String> list = new ArrayList<>();
    Thread thread = Thread.currentThread();
    ClassLoader classLoader = thread.getContextClassLoader();
    Enumeration<URL> urls = classLoader.getResources(nombrePaquete);
    while (urls.hasMoreElements()) {
      URL url = urls.nextElement();
      Class<?> clase = Sintoma.class;
      String nombreFile = url.getFile();
      File file = new File(nombreFile);
      File [] files = file.listFiles((dirr, name) -> name.endsWith(".class"));
      for (File archivo: Objects.requireNonNull(files)) {
        String nombreClase = archivo.getName().split("\\.")[0];
        try {
          Class.forName(nombrePaquete + "." + nombreClase).asSubclass(clase);
          list.add(nombreClase);
        } catch (Exception ignored) {}
      }
    }
    return list;
  }
}

