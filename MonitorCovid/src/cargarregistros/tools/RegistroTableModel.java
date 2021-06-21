package cargarregistros.tools;

import monitor.Registro;
import monitor.Registros;
import monitor.Sintoma;
import monitor.Sintomas;

import java.util.*;

public class RegistroTableModel extends BaseTableModel<Registro> {
  List<Registro> registros;

  public RegistroTableModel() {
    this.registros = new ArrayList<>();
  }

  public void actualizarLista(Registros registros) {
    for (Registro registro : registros) {
      this.registros.add(0, registro);
    }
  }

  public void columnas(Sintomas sintomas) {
    addColumn("Fechas");
    for (Sintoma sintoma : sintomas) {
      addColumn(sintoma.toString());
    }
  }

  public void aniadirItem(Registro sintoma) {
    registros.add(0, sintoma);
  }

  public int getRowCount() {
    return registros.size();
  }

  public Object getValueAt(int row, int colum) {
    String res = "";
    Registro registro = registros.get(row);
    if (colum == 0) {
      res = registro.getFecha().toString().replace("BOT 2021", "");
    } else {
      String nombreColumna = getColumnName(colum);
      if (existe(registro.getSintomas(), nombreColumna)) {
        res = "X";
      }
    }
    return res;
  }

  private boolean existe(Sintomas sintomas, String nombreColumna) {
    boolean existe = false;
    for (Sintoma sintoma : sintomas) {
      if (sintoma.toString().equals(nombreColumna)) {
        existe = true;
        break;
      }
    }
    return existe;
  }
}

