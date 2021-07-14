package diagnosticos.view;

import cargarregistros.view.MenuRegistros;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

public class DiagnosticoListener extends WindowAdapter {
  final MenuDiagnostico menuDiagnostico;

  public DiagnosticoListener(MenuDiagnostico menuDiagnostico) {
    this.menuDiagnostico = menuDiagnostico;
  }

  public void windowClosing(WindowEvent event) {
    try {
      this.menuDiagnostico.exit();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
