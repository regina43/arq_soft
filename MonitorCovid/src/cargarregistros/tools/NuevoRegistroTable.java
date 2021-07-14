package cargarregistros.tools;

import javax.swing.table.DefaultTableModel;

public class NuevoRegistroTable extends DefaultTableModel {
  private int columnas;

  public NuevoRegistroTable(int columnas) {
    this.columnas = columnas;
    if (this.columnas == 2) {
      this.addColumn("Sintoma");
      this.addColumn("Seleccion");
    } else {
      this.addColumn("Sintoma");
      this.addColumn("Categoria");
      this.addColumn("Seleccion");
    }

  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    Class<?> clazz;
    if (this.columnas == 2) {
      if (columnIndex == 1) {
        clazz = Boolean.class;
      } else {
        clazz = String.class;
      }
    }else {
      if (columnIndex == 2) {
        clazz = Boolean.class;
      } else {
        clazz = String.class;
      }
    }
    return clazz;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    boolean isEditable = false;
    if (this.columnas == 2){
      isEditable = column == 1;
    }else {
      isEditable = column == 2;
    }
    return isEditable;
  }

}