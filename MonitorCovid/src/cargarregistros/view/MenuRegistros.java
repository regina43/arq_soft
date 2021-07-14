package cargarregistros.view;

import cargarregistros.CargarRegistros;
import cargarregistros.tools.NuevoRegistroTable;
import cargarregistros.tools.RegistroTableModel;
import cargarregistros.tools.RegistrosListener;
import monitor.Registro;
import monitor.Sintoma;
import monitor.Sintomas;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.TableRowSorter;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuRegistros extends JFrame implements ActionListener {

  private final CargarRegistros cargarRegistros;
  private final Sintomas sintomas;
  private final monitor.Registros registros;
  private RegistroTableModel registroTableModel;
  private List<Sintoma> sintomasMostrados;
  private NuevoRegistroTable sintomasModel;
  private JButton registrar;
  private JPanel panel;
  private JTable tablaRegistros;
  private JTable tablaSintomas;
  private JButton salir;
  private boolean primeraFase;

  public MenuRegistros(String titulo, CargarRegistros cargarRegistros, Sintomas sintomas, monitor.Registros registros) {
    super(titulo);
    this.cargarRegistros = cargarRegistros;
    this.sintomas = sintomas;
    this.registros = registros;
    this.primeraFase = false;
    this.setSize(500, 530);
    this.addWindowListener(new RegistrosListener(this));
    iniciarComponentes();

    this.setVisible(true);
    synchronized (this) {
      try {
        this.wait();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
      }
    }
  }

  private void iniciarComponentes() {
    this.panel = new JPanel();
    this.panel.setLayout(null);
    this.getContentPane().add(panel);

    JLabel nuevoRegistro = new JLabel("Monitoreo de sintomas");
    nuevoRegistro.setBounds(20, 10, 200, 20);
    this.panel.add(nuevoRegistro);

    JLabel aviso = new JLabel("¿Cuál de los  siguientes  síntomas ha tenido en las últimas 24 horas?");
    aviso.setBounds(20, 35, 400, 20);
    panel.add(aviso);

    JLabel aviso2 = new JLabel("Si no tuvo ningún síntoma, presione \"Registrar\"");
    aviso2.setBounds(20, 55, 300, 20);
    panel.add(aviso2);

    if (esPrimeraFase()){
      sintomasModel = new NuevoRegistroTable(2);
    }else{
      sintomasModel = new NuevoRegistroTable(3);
    }

    if (primeraFase) {
      sintomasMostrados = new ArrayList<>();
      for (Sintoma sintoma : sintomas) {
        sintomasModel.addRow(new Object[]{sintoma.toString(), false});
        sintomasMostrados.add(sintoma);
      }
    }else{
      sintomasMostrados = new ArrayList<>();
      for (Sintoma sintoma : sintomas) {
        sintomasModel.addRow(new Object[]{sintoma.toString(), sintoma.getClass().getSimpleName(), false});
        sintomasMostrados.add(sintoma);
      }
    }

    tablaSintomas = new JTable(sintomasModel);
    tablaSintomas.getColumnModel().getColumn(0).setPreferredWidth(300);
    tablaSintomas.getColumnModel().getColumn(1).setPreferredWidth(90);
    tablaSintomas.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());

    TableRowSorter<NuevoRegistroTable> sorTable = new TableRowSorter<>(sintomasModel);
    tablaSintomas.setRowSorter(sorTable);


    JScrollPane jScrollPane1 = new JScrollPane(tablaSintomas);
    jScrollPane1.setBounds(20, 80, 445, 150);
    panel.add(jScrollPane1);

    this.registrar = new JButton("Registrar");
    this.registrar.setBounds(365, 238, 98, 20);
    this.registrar.addActionListener(this);
    panel.add(this.registrar);

    registroTableModel = new RegistroTableModel();
    registroTableModel.actualizarLista(registros);

    tablaRegistros = new JTable(registroTableModel);
    tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(300);
    tablaRegistros.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
    JScrollPane jScrollPane = new JScrollPane(tablaRegistros);
    jScrollPane.setBounds(20, 270, 445, 180);
    panel.add(jScrollPane);

    this.salir = new JButton("Salir");
    this.salir.setBounds(365, 455, 100, 20);
    this.salir.addActionListener(this);
    this.panel.add(salir);
  }

  private boolean esPrimeraFase() {
    boolean pesosIguales = true;
    String primeraFase = "PrimeraFase";
    for (Sintoma sintoma: sintomas){
      String className = sintoma.getClass().getSimpleName();
      if (!primeraFase.equals(className)){
        pesosIguales = false;
        break;
      }
    }
    if (pesosIguales){
      this.primeraFase = true;
    }
    return pesosIguales;
  }

  public void actualizarTabla(Registro registro) {
    this.registros.push(registro);
    this.registroTableModel.aniadirItem(registro);
    this.tablaRegistros.setModel(registroTableModel);
    this.registroTableModel.fireTableDataChanged();
    limpiarChecks();
  }

  private void limpiarChecks() {
    if (primeraFase) {
      for (int i = 0; i < tablaSintomas.getRowCount(); i++) {
        sintomasModel.setValueAt(false, i, 1);
      }
    }else{
      for (int i = 0; i < tablaSintomas.getRowCount(); i++) {
        sintomasModel.setValueAt(false, i, 2);
      }
    }
  }

  public void exit() throws FileNotFoundException {
    saveFile();
    synchronized (this) {
      this.notify();
    }
    this.setVisible(false);
    this.dispose();
  }

  public void saveFile() {
    this.cargarRegistros.saveFile();
  }

  private void guardarSintomas() {
    Sintomas sintomasSeleccionados = new Sintomas();
    for (int i = 0; i < tablaSintomas.getRowCount(); i++) {
      boolean checked;
      if (primeraFase){
        checked = Boolean.valueOf(tablaSintomas.getValueAt(i, 1).toString());
      }else{
        checked = Boolean.valueOf(tablaSintomas.getValueAt(i, 2).toString());
      }
      if (checked) {
        String nombre = String.valueOf(tablaSintomas.getValueAt(i,0).toString());
        Sintoma sintoma = buscarSintoma(nombre);
        if (sintoma != null){
          sintomasSeleccionados.add(sintoma);
        }
      }
    }
    Registro registro = new Registro(new Date(), sintomasSeleccionados);
    actualizarTabla(registro);
  }

  private Sintoma buscarSintoma(String nombre) {
    Sintoma buscado = null;
    for (Sintoma sintoma: sintomas){
      if (sintoma.toString().equals(nombre)){
        buscado = sintoma;
        break;
      }
    }
    return buscado;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object btnPulsado = e.getSource();
    if (btnPulsado == this.registrar) {
      guardarSintomas();
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
