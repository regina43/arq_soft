package diagnosticos.view;

import monitor.Registro;
import monitor.Registros;
import monitor.Sintoma;
import monitor.Sintomas;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SeguimientoTableModel extends AbstractTableModel {
  private List<Registro> registros;
  private final String[] columnNames = new String[]{"Fecha", "Sintoma"};

  public SeguimientoTableModel() {
    this.registros = new ArrayList<>();
  }

  public void actualizarLista(Stack<Registro> registros) {
    for (Registro registro : registros) {
      this.registros.add(0, registro);
    }
  }

  public int getRowCount() {
    return registros.size();
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  public Object getValueAt(int row, int colum) {
    String res;
    Registro registro = registros.get(row);
    if (colum == 0) {
      res = registro.getFecha().toString();
    } else {
      res = listarSintomasPresentes(registro.getSintomas());
    }
    return res;
  }

  private String listarSintomasPresentes(Sintomas sintomas) {
    StringBuilder listaSintomas = new StringBuilder();
    for (Sintoma sintoma : sintomas) {
      listaSintomas.append(sintoma.toString());
      listaSintomas.append(" | ");
    }
    if (listaSintomas.isEmpty()) {
      listaSintomas.append("No se registraron sintomas");
    }
    return listaSintomas.toString();
  }
}

