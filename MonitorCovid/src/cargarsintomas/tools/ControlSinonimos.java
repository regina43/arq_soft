package cargarsintomas.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ControlSinonimos {
  Map<String, String[]> sinonimos;
  String path;

  public ControlSinonimos(){
    this.sinonimos = new HashMap<>();
    this.path = getPath("sinonimos","cargarsintomas");
    leerSinonimos();
  }

  public void leerSinonimos() {
    File archivo;
    FileReader fr = null;
    BufferedReader br;
    try {
      archivo = new File (this.path);
      fr = new FileReader (archivo);
      br = new BufferedReader(fr);

      String linea;
      while((linea=br.readLine())!=null) {
        String [] linessep = linea.split("-");
        String usado = linessep[0];
        String [] palabras = linessep[1].split(",");
        this.sinonimos.put(usado, palabras);
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }finally{
      try{
        if( null != fr ){
          fr.close();
        }
      }catch (Exception e2){
        e2.printStackTrace();
      }
    }

  }

  public boolean usado(String palabra) {
    boolean usado = false;
    String esUsado = usadoLlave(palabra);
    switch (esUsado) {
      case "no" -> {
        this.sinonimos.get(palabra)[0] = "si";
        usado = false;
      }
      case "si" -> usado = true;
      case "ne" -> usado = existeLista(palabra);
    }
    return usado;
  }

  private boolean existeLista(String palabra) {
    String usado = null;
    boolean existe = false;
    for (Map.Entry<String, String[]> entry : sinonimos.entrySet()) {
      String [] palabras = entry.getValue();
      if (existe(palabra, palabras)) {
        usado = palabras [0];
        if (usado.equals("no")) {
          entry.getValue()[0] = "si";
          existe = false;
        } else {
          existe = true;
        }
        break;
      } else {
        usado = "ne";
      }
    }
    if (usado.equals("ne")) {
      existe = false;
    }
    return existe;
  }

  private boolean existe(String palabra, String[] palabras) {
    boolean existe = false;
    for (String pal: palabras) {
      if (palabra.equals(pal)) {
        existe = true;
        break;
      }
    }
    return existe;
  }

  private String usadoLlave(String palabra) {
    String usado = "ne";
    String [] palabras = sinonimos.get(palabra);
    if (palabras != null) {
      usado = palabras[0];
    }
    return usado;
  }

  public void escribirSinonimos() {
    FileWriter fichero = null;
    PrintWriter pw;
    try
    {
      fichero = new FileWriter(this.path);
      pw = new PrintWriter(fichero);

      PrintWriter finalPw = pw;
      this.sinonimos.forEach((k, v) -> finalPw.println(k + "-" + separarPorComas(v)));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != fichero)
          fichero.close();
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
  }

  private String separarPorComas(String[] v) {
    StringBuilder cadena = new StringBuilder();
    int pos = 0;
    for(String s: v) {
      cadena.append(s);
      if (pos <= v.length - 1)
        cadena.append(",");
      pos++;
    }
    return cadena.toString();
  }

  private String getPath(String nombreArchivo, String nombrePaquete){
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
}
