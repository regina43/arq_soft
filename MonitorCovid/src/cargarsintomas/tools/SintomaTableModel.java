package cargarsintomas.tools;

import monitor.Sintoma;
import monitor.Sintomas;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SintomaTableModel extends AbstractTableModel {

  private final String[] columnNames  = new String[] {"Sintoma","Categoria"};
  private final List<Sintoma> sintomas;

  public SintomaTableModel() {
    this.sintomas = new ArrayList<>();
  }

  public void actualizarLista(Sintomas sintomas) {
    for (Sintoma actual : sintomas) {
      this.sintomas.add(actual);
    }
  }

  public void addItem(Sintoma sintoma) {
    sintomas.add(sintoma);
  }

  public int getRowCount() {
    return sintomas.size();
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public Object getValueAt(int row, int colum) {
    Sintoma personObj = sintomas.get(row);
    return switch (colum) {
      case 0 -> personObj.toString();
      case 1 -> getNombreClase(personObj.getClass().getName());
      default -> null;
    };
  }

  private Object getNombreClase(String name) {
    String [] nombreClase = name.split("\\.");
    return nombreClase[1];
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }
}
