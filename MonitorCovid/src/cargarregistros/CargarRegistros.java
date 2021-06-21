package cargarregistros;

import cargarregistros.tools.SerializadorRegistros;
import cargarregistros.view.MenuRegistros;
import monitor.Registro;
import monitor.Registros;
import monitor.Sintomas;

public class CargarRegistros {
  private final SerializadorRegistros<Registros> serializador;
  private Registros registros;

  public CargarRegistros(Sintomas sintomas) {
    this.serializador = new SerializadorRegistros<>("registros.dat", "cargarregistros");
    this.cargarRegistros();
    MenuRegistros menuRegistros = new MenuRegistros(this, sintomas, this.registros);
  }

  private void cargarRegistros() {
    this.registros = new monitor.Registros();
    if (serializador.existeArchivo()) {
      this.registros = serializador.deserializar();
    }
  }

  public Registro getRegistro() {
    return registros.peek();
  }

  public void saveFile() {
    this.serializador.serializar(registros);
  }

}
