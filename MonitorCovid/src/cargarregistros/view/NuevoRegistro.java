package cargarregistros.view;

import cargarregistros.tools.NuevoRegistroTable;
import monitor.Registro;
import monitor.Sintoma;
import monitor.Sintomas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuevoRegistro extends JFrame implements ActionListener{
  private final Sintomas sintomas;
  private final MenuRegistros menuRegistros;
  private List<Sintoma> sintomasMostrados;
  private JButton registrar;
  private JTable table;

  public NuevoRegistro(Sintomas sintomas, MenuRegistros menuRegistros) {
    this.sintomas = sintomas;
    this.menuRegistros = menuRegistros;
    this.setSize(400,400);
    iniciarComponentes();
  }

  private void iniciarComponentes() {
    JPanel panel = new JPanel();
    panel.setLayout(null);
    this.getContentPane().add(panel);

    JLabel registrarSintomas = new JLabel("Registrar sintomas");
    registrarSintomas.setBounds(50,20,150,20);
    panel.add(registrarSintomas);

    JLabel aviso = new JLabel("¿Cuál de los  siguientes  síntomas ha tenido en las");
    aviso.setBounds(50,40,300,20);
    panel.add(aviso);

    JLabel aviso2 = new JLabel("últimas 24 horas? (selección múltiple posible)");
    aviso2.setBounds(50,55,300,20);
    panel.add(aviso2);

    JLabel aviso3= new JLabel("Si no tuvo ningún síntoma, presione \"Registrar\"");
    aviso3.setBounds(50,80,300,20);
    panel.add(aviso3);

    NuevoRegistroTable myModel = new NuevoRegistroTable();
    sintomasMostrados = new ArrayList<>();
    for (Sintoma sintoma: sintomas) {
      myModel.addRow(new Object[]{sintoma.toString(), false});
      sintomasMostrados.add(sintoma);
    }

    table = new JTable(myModel);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    table.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
    JScrollPane jScrollPane = new JScrollPane(table);
    jScrollPane.setBounds(20, 130, 330, 195);
    panel.add(jScrollPane);

    this.registrar = new JButton("Registrar");
    this.registrar.setBounds(250, 330, 98, 20);
    this.registrar.addActionListener(this);
    panel.add(this.registrar);
  }

  private void guardarSintomas() {
    Sintomas sintomasSeleccionados = new Sintomas();
    for (int i = 0; i < table.getRowCount(); i++) {
      boolean checked = Boolean.valueOf(table.getValueAt(i,1).toString());
      if(checked){
        sintomasSeleccionados.add(sintomasMostrados.get(i));
      }
    }
    Registro registro = new Registro(new Date(), sintomasSeleccionados);
    this.menuRegistros.actualizarTabla(registro);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object btnPulsado = e.getSource();
    if (btnPulsado == this.registrar) {
      guardarSintomas();
      setVisible(false);
    }
  }
}
