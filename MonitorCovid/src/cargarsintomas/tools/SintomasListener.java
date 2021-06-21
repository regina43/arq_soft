package cargarsintomas.tools;

import cargarsintomas.view.MenuSintomas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

public class SintomasListener extends WindowAdapter {
  final MenuSintomas menuSintomas;

  public SintomasListener(MenuSintomas menuSintomas){
    this.menuSintomas = menuSintomas;
  }

  public void windowClosing(WindowEvent event) {
    try {
      this.menuSintomas.exit();
      synchronized (this.menuSintomas){
        this.menuSintomas.notify();
      }
      this.menuSintomas.setVisible(false);
      this.menuSintomas.dispose();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
