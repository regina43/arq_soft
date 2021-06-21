package cargarregistros.view;

import monitor.Registro;
import monitor.Sintoma;
import monitor.Sintomas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NuevoRegistro extends JFrame implements ActionListener{
  private final Sintomas sintomas;
  private final MenuRegistros menuRegistros;
  private List<JCheckBox> checkSintomas;
  private List<Sintoma> sintomasMostrados;
  private JButton registrar;

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

    JLabel aviso3= new JLabel("Si no tuvo ningún síntoma, presione \"Agregar\"");
    aviso3.setBounds(50,80,300,20);
    panel.add(aviso3);

    int altura = 120;
    checkSintomas = new ArrayList<>();

    sintomasMostrados = new ArrayList<>();
    for (Sintoma sintoma: sintomas) {
      JCheckBox jCheckBox = new JCheckBox();
      jCheckBox.setText(sintoma.toString());
      jCheckBox.setBounds(50, altura, 150, 20);
      checkSintomas.add(jCheckBox);
      panel.add(jCheckBox);
      sintomasMostrados.add(sintoma);
      altura += 25;
    }

    this.registrar = new JButton("Agregar");
    this.registrar.setBounds(250, 300, 100, 20);
    this.registrar.addActionListener(this);
    panel.add(this.registrar);
  }

  private void guardarSintomas() {
    int indice = 0;
    Sintomas sintomasSeleccionados = new Sintomas();
    for (JCheckBox jCheckBox: checkSintomas){
      if (jCheckBox.isSelected()) {
        sintomasSeleccionados.add(sintomasMostrados.get(indice));
      }
      indice++;
    }
    Registro registro = new Registro(new Date(), sintomasSeleccionados);
    menuRegistros.actualizarTabla(registro);
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
