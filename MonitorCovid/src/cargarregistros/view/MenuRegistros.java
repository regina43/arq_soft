package cargarregistros.view;

import cargarregistros.CargarRegistros;
import cargarregistros.tools.RegistroTableModel;
import cargarregistros.tools.RegistrosListener;
import monitor.Registro;
import monitor.Sintomas;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class MenuRegistros extends JFrame implements ActionListener{

  private final CargarRegistros cargarRegistros;
  private final Sintomas sintomas;
  private final monitor.Registros registros;
  private RegistroTableModel registroTableModel;
  private JPanel panel;
  private JTable table;
  private JButton iniciarnRegistro;

  public MenuRegistros(CargarRegistros cargarRegistros, Sintomas sintomas, monitor.Registros registros) {
    this.cargarRegistros = cargarRegistros;
    this.sintomas = sintomas;
    this.registros = registros;
    this.setSize(400, 400);
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
    nuevoRegistro.setBounds(50, 20, 200, 20);
    this.panel.add(nuevoRegistro);

    this.iniciarnRegistro = new JButton("Iniciar");
    this.iniciarnRegistro.setBounds(150, 60, 100, 20);
    this.iniciarnRegistro.addActionListener(this);
    this.panel.add(iniciarnRegistro);

    registroTableModel = new RegistroTableModel();
    registroTableModel.columnas(sintomas);
    registroTableModel.actualizarLista(registros);

    table = new JTable(registroTableModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    table.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
    JScrollPane jScrollPane = new JScrollPane(table);
    jScrollPane.setBounds(20, 130, 340, 220);
    panel.add(jScrollPane);
  }

  public void actualizarTabla(Registro registro) {
    this.registros.push(registro);
    this.registroTableModel.aniadirItem(registro);
    this.table.setModel(registroTableModel);
    this.registroTableModel.fireTableDataChanged();
    this.table.setVisible(true);
  }

  public void exit() throws FileNotFoundException {
      saveFile();
      synchronized (this){
        this.notify();
      }
      this.setVisible(false);
      this.dispose();
      System.exit(0);
  }

  public void saveFile() {
    this.cargarRegistros.saveFile();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object btnPulsado = e.getSource();
    if (btnPulsado == this.iniciarnRegistro) {
      NuevoRegistro nuevoRegistro = new NuevoRegistro(sintomas, this);
      nuevoRegistro.setVisible(true);
    }
  }
}
