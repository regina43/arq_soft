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
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MenuSintomas extends JFrame implements ActionListener{
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

  public MenuSintomas(String titulo, CargarSintomas cargarSintomas, Sintomas sintomas) throws IOException {
    super(titulo);
    lectorDePaquete = new LectorDePaquete();
    this.cargarSintomas = cargarSintomas;
    this.sintomas = sintomas;
    this.controlSinonimos = new ControlSinonimos();
    this.setSize(400,400);
    iniciarComponentes();
    addWindowListener(new SintomasListener(this));
    this.setVisible(true);
    synchronized(this) {
      try {
        this.wait();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      }
    }
  }

  public void iniciarComponentes() throws IOException {
    panel = new JPanel();
    panel.setLayout(null);
    this.getContentPane().add(panel);

    JLabel titulo = new JLabel();
    titulo.setText("Agregar nuevo sintoma");
    titulo.setBounds(40,30,150,20);
    panel.add(titulo);

    JLabel nombre = new JLabel();
    nombre.setText("Nombre: ");
    nombre.setBounds(40,70,100,20);
    panel.add(nombre);

    JLabel tipo =  new JLabel();
    tipo.setText("Tipo:");
    tipo.setBounds(60,100,80,20);
    panel.add(tipo);

    textoNombre = new JTextField();
    textoNombre.setBounds(100, 70, 150, 20);
    textoNombre.setFocusable(true);
    panel.add(textoNombre);


    tipos = new JComboBox<>();
    tipos.setBounds(100, 100, 150,20);
    panel.add(tipos);

    List<String> listaClases = lectorDePaquete.getNombresClases("sintomas");
    for (String nombreClase: listaClases) {
      tipos.addItem(nombreClase);
    }

    sintomasTableModel=  new SintomaTableModel();
    sintomasTableModel.actualizarLista(sintomas);
    table = new JTable(sintomasTableModel);
    JScrollPane jScrollPane = new JScrollPane(table);
    jScrollPane.setBounds(20,200,340, 150);
    panel.add(jScrollPane);

    this.guardar = new JButton("Guardar");
    this.guardar.setBounds(160, 150,100,20);
    this.guardar.addActionListener(this);
    this.panel.add(guardar);

  }

  private void addSintoma() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    String nombre = textoNombre.getText().toLowerCase().replaceAll("\\s{2,}", " "). trim();
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
    this.setVisible(false);
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
  }
}

