package cargarregistros;

import cargarregistros.tools.SerializadorRegistros;
import cargarregistros.view.MenuRegistros;
import monitor.Registro;
import monitor.Registros;
import monitor.Sintomas;

import java.util.Date;

public class CargarRegistros {
  private final SerializadorRegistros serializador;
  private Registros registros;

  public CargarRegistros(Sintomas sintomas) {
    this.serializador = new SerializadorRegistros("registros.dat", "");
    this.cargarRegistros();
    MenuRegistros menuRegistros = new MenuRegistros("Registros", this, sintomas, this.registros);
  }

  private void cargarRegistros() {
    this.registros = new Registros();
    if (serializador.existeArchivo()) {
      this.registros = serializador.deserializar();
    }
  }

  public Registro getRegistro() {
    Registro registro;
    if (registros.isEmpty()) {
      Sintomas nsintomas = new Sintomas();
      registro = new Registro(new Date(), nsintomas);
    } else {
      registro = registros.peek();
    }
    return registro;
  }

  public Registros getRegistros() {
    return registros;
  }

  public void saveFile() {
    this.serializador.serializar(registros);
  }

}
