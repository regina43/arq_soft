package diagnosticos.view;

import monitor.Registro;
import monitor.Seguimiento;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Stack;

public class MenuDiagnostico extends JFrame implements ActionListener {
  private Stack<Registro> registros;
  private JTable tablaRegistros;
  private JPanel panel;
  private JButton salir;
  private SeguimientoTableModel registroTableModel;
  private Notificador notificador;
  private JButton btnSi;
  private JButton btnNo;
  private JLabel notify;
  private Seguimiento seguimiento;

  public MenuDiagnostico(String titulo, int notificacion, Stack<Registro> registros, Seguimiento seguimiento) {
    super(titulo);
    this.registros = registros;
    this.seguimiento = seguimiento;
    this.notificador = new Notificador(notificacion);
    this.setSize(500, 530);
    this.addWindowListener(new DiagnosticoListener(this));
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

  public void iniciarComponentes() {
    panel = new JPanel();
    panel.setLayout(null);
    this.getContentPane().add(panel);

    JLabel mensaje1 = new JLabel("Recomendacion: ");
    mensaje1.setBounds(20, 20, 200, 20);
    this.panel.add(mensaje1);

    JPanel panelNot = notificador.getRecomendacion();
    panelNot.setBounds(20, 50, 445, 130);
    panel.add(panelNot);

    JButton [] botones = notificador.getBotones();
    if (botones != null) {
      this.btnSi = botones[0];
      this.btnSi.setBounds(150, 180, 50, 20);
      this.btnSi.addActionListener(this);
      panel.add(this.btnSi);

      this.btnNo = botones[1];
      this.btnNo.setBounds(250, 180, 50, 20);
      this.btnNo.addActionListener(this);
      panel.add(this.btnNo);
    }

    notify = new JLabel();
    notify.setBounds(100, 215, 300, 20);
    notify.setVisible(false);
    this.panel.add(notify);

    registroTableModel = new SeguimientoTableModel();
    registroTableModel.actualizarLista(registros);

    tablaRegistros = new JTable(registroTableModel);
    tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(300);
    tablaRegistros.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
    JScrollPane jScrollPane = new JScrollPane(tablaRegistros);
    jScrollPane.setBounds(20, 250, 445, 200);
    panel.add(jScrollPane);

    this.salir = new JButton("Salir");
    this.salir.setBounds(300, 450, 100, 20);
    this.salir.addActionListener(this);
    this.panel.add(salir);
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

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object btnPulsado = e.getSource();
    if (btnPulsado == this.btnSi) {
      this.seguimiento.reiniciarSeguimiento();
      notify.setText("Que bueno que haya seguido las recomendaciones.");
      notify.setVisible(true);
    }

    if (btnPulsado == this.btnNo) {
      notify.setText("Es importante que lo haga. Puede estar en riesgo.");
      notify.setVisible(true);
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


