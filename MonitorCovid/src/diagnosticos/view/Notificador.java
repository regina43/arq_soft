package diagnosticos.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Notificador {
  private JPanel panel;
  private JButton[] botones;

  public Notificador(int notify){
    this.panel = new JPanel();
    this.botones = null;
    construirNotificacion(notify);
  }

  private void construirBotones() {
    JButton btnSi = new JButton("Si");
    btnSi.setBounds(365, 238, 98, 20);
    panel.add(btnSi);

    JButton btnNo = new JButton("No");
    btnNo.setBounds(365, 238, 98, 20);
    panel.add(btnNo);

    botones = new JButton[2];
    botones[0] = btnSi;
    botones[1] = btnNo;
  }

  private void construirNotificacion(int notify) {
    switch (notify) {
      case 1 -> {
        inicioPF();
        botones = null;
      }
      case 2 -> {
        seguimientoPF();
        construirBotones();
      }
      case 3 -> {
        inicioSF();
        botones = null;
      }
      case 4 -> {
        seguimientoSF();
        construirBotones();
      }
      case 5 -> {
        terminado();
        construirBotones();
      }
      default -> ningunSintoma();
    }
  }

  private void terminado() {
    JLabel mensaje0 = new JLabel("Se ha terminado con el seguimiento");
    mensaje0.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje0);

    JLabel mensaje1 = new JLabel("Anteriormente se le recomendo que visite al medico de manera urgente.");
    mensaje1.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje1);

    JLabel mensaje2 = new JLabel("Tambien se le recomendo realizarse una prueba rapida de covid.");
    mensaje2.setBounds(20, 30, 200, 20);
    this.panel.add(mensaje2);

    JLabel mensaje3 = new JLabel("¿Ha seguido las recomendaciones?");
    mensaje3.setBounds(20, 50, 200, 20);
    this.panel.add(mensaje3);
  }

  private void seguimientoSF() {
    JLabel mensaje1 = new JLabel("Anteriormente se le recomendo que visite al medico de manera urgente.");
    mensaje1.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje1);

    JLabel mensaje2 = new JLabel("Tambien se le recomendo realizarse una prueba rapida de covid.");
    mensaje2.setBounds(20, 30, 200, 20);
    this.panel.add(mensaje2);

    JLabel mensaje3 = new JLabel("¿Ha seguido las recomendaciones?");
    mensaje3.setBounds(20, 50, 200, 20);
    this.panel.add(mensaje3);
  }

  private void inicioSF() {
    JLabel mensaje1 = new JLabel("Se ha iniciado el monitoreo de sintomas en segunda fase");
    mensaje1.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje1);

    JLabel mensaje2 = new JLabel("Es urgente que vaya al medico y se haga una prueba rapida de covid.");
    mensaje2.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje2);
  }

  private void seguimientoPF() {
    JLabel mensaje1 = new JLabel("Anteriormente se le recomendo que visite al medico.");
    mensaje1.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje1);

    JLabel mensaje2 = new JLabel("¿Ha seguido las recomendaciones?");
    mensaje2.setBounds(20, 30, 200, 20);
    this.panel.add(mensaje2);
  }

  private void inicioPF() {
    JLabel mensaje1 = new JLabel("Se ha iniciado el seguimiento de sus sintomas.");
    mensaje1.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje1);

    JLabel mensaje2 = new JLabel("Es importante que se haga un control diario y visite al medico.");
    mensaje2.setBounds(20, 30, 200, 20);
    this.panel.add(mensaje2);
  }

  private void ningunSintoma() {
    JLabel mensaje1 = new JLabel("Actualmente, no hay nada que sugiera que tiene covid.");
    mensaje1.setBounds(20, 10, 200, 20);
    this.panel.add(mensaje1);

    JLabel mensaje2 = new JLabel("Si presenta cualquier sintoma de covid use de nuevo este verificador.");
    mensaje2.setBounds(20, 30, 200, 20);
    this.panel.add(mensaje2);
  }

  public JPanel getRecomendacion() {
    return panel;
  }

  public JButton[] getBotones() {
    return this.botones;
  }
}
