package cargarsintomas.view;

import cargarsintomas.CargarSintomas;
import cargarsintomas.tools.ControlSinonimos;
import cargarsintomas.tools.LectorDePaquete;
import cargarsintomas.tools.SintomaTableModel;
import cargarsintomas.tools.SintomasListener;
import monitor.Sintoma;
import monitor.Sintomas;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MenuSintomas extends JFrame implements ActionListener {
  private final CargarSintomas cargarSintomas;
  private final Sintomas sintomas;
  private final LectorDePaquete lectorDePaquete;
  private ControlSinonimos controlSinonimos;
  private JComboBox<String> tipos;
  private JTextField textoNombre;
  private SintomaTableModel sintomasTableModel;
  private JTable table;
  private JPanel panel;
  private JButton guardar;
  private JButton salir;

  public MenuSintomas(String titulo, CargarSintomas cargarSintomas, Sintomas sintomas, ControlSinonimos controlSinonimos) throws IOException {
    super(titulo);
    lectorDePaquete = new LectorDePaquete();
    this.cargarSintomas = cargarSintomas;
    this.sintomas = sintomas;
    this.controlSinonimos = controlSinonimos;
    this.setSize(500, 530);
    iniciarComponentes();
    addWindowListener(new SintomasListener(this));
    this.setVisible(true);
    synchronized (this) {
      try {
        this.wait();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      }
    }
  }

  public void iniciarComponentes() {
    panel = new JPanel();
    panel.setLayout(null);
    this.getContentPane().add(panel);

    JLabel titulo = new JLabel();
    titulo.setText("Agregar nuevo sintoma");
    titulo.setBounds(40, 30, 150, 20);
    panel.add(titulo);

    JLabel nombre = new JLabel();
    nombre.setText("Nombre: ");
    nombre.setBounds(40, 70, 100, 20);
    panel.add(nombre);

    JLabel tipo = new JLabel();
    tipo.setText("Tipo:");
    tipo.setBounds(60, 100, 80, 20);
    panel.add(tipo);

    textoNombre = new JTextField();
    textoNombre.setBounds(100, 70, 300, 20);
    textoNombre.setFocusable(true);
    panel.add(textoNombre);


    tipos = new JComboBox<>();
    tipos.setBounds(100, 100, 300, 20);
    panel.add(tipos);

    List<String> listaClases = lectorDePaquete.getNombresClases();
    for (String nombreClase : listaClases) {
      tipos.addItem(nombreClase);
    }

    sintomasTableModel = new SintomaTableModel();
    sintomasTableModel.actualizarLista(sintomas);
    table = new JTable(sintomasTableModel);
    table.getColumnModel().getColumn(0).setPreferredWidth(250);

    TableRowSorter<SintomaTableModel> sorTable = new TableRowSorter<>(sintomasTableModel);
    table.setRowSorter(sorTable);

    JScrollPane jScrollPane = new JScrollPane(table);
    jScrollPane.setBounds(20, 200, 445, 230);
    panel.add(jScrollPane);

    this.guardar = new JButton("Guardar");
    this.guardar.setBounds(300, 150, 100, 20);
    this.guardar.addActionListener(this);
    this.panel.add(guardar);

    this.salir = new JButton("Salir");
    this.salir.setBounds(300, 450, 100, 20);
    this.salir.addActionListener(this);
    this.panel.add(salir);
  }

  private void addSintoma() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    String nombre = textoNombre.getText().toLowerCase().replaceAll("\\s{2,}", " ").trim();
    String nombreClase = "sintomas." + tipos.getSelectedItem();
    Class<?> cl = Class.forName(nombreClase);
    Constructor<?> constructor = cl.getConstructor(String.class);
    if (!nombre.equals("") && !controlSinonimos.usado(nombre)) {
      String nombreFinal = normalizarCadena(nombre);
      Sintoma sintoma = (Sintoma) (constructor.newInstance(new Object[]{nombreFinal}));
      if (!existe(sintoma, sintomas)) {
        sintomas.add(sintoma);
        sintomasTableModel.addItem(sintoma);
        table.setModel(sintomasTableModel);
        sintomasTableModel.fireTableDataChanged();
        table.setVisible(true);
      }
    }
  }

  private String normalizarCadena(String nombre) {
    String primeraLetra = nombre.substring(0, 1).toUpperCase();
    String restoDeLaCadena = nombre.substring(1);
    return primeraLetra + restoDeLaCadena;
  }

  private boolean existe(Sintoma sintoma, Sintomas sintomas) {
    boolean existe = false;
    for (Sintoma actual : sintomas) {
      if (actual.toString().equals(sintoma.toString())) {
        existe = true;
        break;
      }
    }
    return existe;
  }

  public void exit() throws FileNotFoundException {
    saveFile();
    synchronized (this) {
      this.notify();
    }
    this.setVisible(false);
    this.dispose();
  }

  private void saveFile() {
    cargarSintomas.saveFile();
    controlSinonimos.escribirSinonimos();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object btnPulsado = e.getSource();
    if (btnPulsado == this.guardar) {
      try {
        addSintoma();
      } catch (IOException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException ioException) {
        ioException.printStackTrace();
      }
    }
    if (btnPulsado == this.salir) {
      try {
        this.exit();
      } catch (FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
      }
    }
  }
}

