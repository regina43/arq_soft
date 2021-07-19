package diagnosticos.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ContadorDeDias {
  public ContadorDeDias(){}

  public int contarDias(Date fechaI, Date fechaF) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String fecha1 = dateFormat.format(fechaI);
    String fecha2 = dateFormat.format(fechaF);

    Date fechaInicial = null;
    try {
      fechaInicial = dateFormat.parse(fecha1);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date fechaFinal = null;
    try {
      fechaFinal = dateFormat.parse(fecha2);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    int dias = (int) ((fechaInicial.getTime() - fechaFinal.getTime()) / 86400000);
    return dias;
  }
}
