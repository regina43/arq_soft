package cargarregistros.tools;

import javax.swing.table.AbstractTableModel;
import java.io.Serial;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

public abstract class BaseTableModel<T> extends AbstractTableModel
        implements Iterable<T> {

  @Serial
  private static final long serialVersionUID = -3559749545223591549L;
  private final Vector<String> columns;
  private final Vector<T> items = new Vector<>();

  public BaseTableModel(String... columns) {
    this.columns = convertVector(columns);
  }

  @SafeVarargs
  public static <V> Vector<V> convertVector(V... array) {
    if (array == null) return new Vector<>();
    Vector<V> v = new Vector<>(array.length);
    for (V o : array) v.addElement(o);
    return v;
  }

  @Override public int getColumnCount() {
    return columns.size();
  }

  @Override public String getColumnName(int columnIndex) {
    return columns.get(columnIndex);
  }

  public void addColumn(String columnName) {
    columns.addElement(columnName);
    fireTableStructureChanged();
  }

  public ArrayList<String> getColumns() {
    return new ArrayList<>(columns);
  }

  @Override public int getRowCount() {
    return items.size();
  }

  public T getItem(int rowIndex) {
    return items.get(rowIndex);
  }

  public void setItem(int rowIndex, T item) {
    items.set(rowIndex, item);
    items.setSize(getRowCount());
    fireTableRowsUpdated(rowIndex, rowIndex);
  }

  public void addItem(T item) {
    addItem(getRowCount(), item);
  }

  public void addItem(int rowIndex, T item) {
    items.insertElementAt(item, rowIndex);
    items.setSize(getRowCount());
    fireTableRowsInserted(rowIndex, rowIndex);
  }

  public boolean containsItem(T item) {
    return items.contains(item);
  }

  public T removeItem(int rowIndex) {
    try {
      return items.remove(rowIndex);
    } finally {
      fireTableRowsDeleted(rowIndex, rowIndex);
    }
  }

  public T removeItem(T item) {
    int index = items.indexOf(item);
    if (index != -1) return removeItem(index);
    return null;
  }

  public boolean removeAllItems(Collection<T> toRemove) {
    try {
      return items.removeAll(toRemove);
    } finally {
      fireTableDataChanged();
    }
  }

  public void clearItems() {
    items.clear();
    fireTableDataChanged();
  }

  public ArrayList<T> getItems() {
    return new ArrayList<>(items);
  }

  public void setItems(Collection<T> vector) {
    items.clear();
    items.addAll(vector);
    this.fireTableDataChanged();
  }

  @Override public Class<?> getColumnClass(int col) {
    if (getRowCount() > 0) {
      Object cell = getValueAt(0, col);
      if (cell != null) return cell.getClass();
    }
    return super.getColumnClass(col);
  }

  public T[] toArray(Class<T> clazz) {
    T[] genericArray = (T[]) Array.newInstance(clazz, items.size());
    return items.toArray(genericArray);
  }

  @Override public Iterator<T> iterator() {
    return new ArrayList<>(items).iterator();
  }

  public void notifyDataSetChanged() {
    if (getRowCount() > 0) {
      fireTableRowsUpdated(0, getRowCount()-1);
    }
  }
}
