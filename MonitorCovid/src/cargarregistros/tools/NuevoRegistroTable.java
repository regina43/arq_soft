package cargarregistros.tools;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class NuevoRegistroTable extends DefaultTableModel {

  public NuevoRegistroTable() {
    super(new String[]{"Sintoma", "Seleccion"}, 0);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    Class clazz = String.class;
    switch (columnIndex) {
      case 0:
        clazz = Integer.class;
        break;
      case 1:
        clazz = Boolean.class;
        break;
    }
    return clazz;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return column == 1;
  }

  @Override
  public void setValueAt(Object aValue, int row, int column) {
    if (aValue instanceof Boolean && column == 1) {
      Vector rowData = (Vector)getDataVector().get(row);
      rowData.set(1, (boolean)aValue);
      fireTableCellUpdated(row, column);
    }
  }

}