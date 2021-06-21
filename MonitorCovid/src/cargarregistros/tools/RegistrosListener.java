package cargarregistros.tools;

import cargarregistros.view.MenuRegistros;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

public class RegistrosListener extends WindowAdapter {
  final MenuRegistros menuRegistros;

  public RegistrosListener(MenuRegistros menuRegistros){
    this.menuRegistros = menuRegistros;
  }

  public void windowClosing(WindowEvent event) {
    try {
      this.menuRegistros.exit();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
