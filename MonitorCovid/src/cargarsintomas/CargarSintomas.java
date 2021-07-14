package cargarsintomas;

import cargarsintomas.tools.ControlSinonimos;
import cargarsintomas.tools.SerializadorSintomas;
import cargarsintomas.view.MenuSintomas;
import monitor.Sintoma;
import monitor.Sintomas;

import java.io.IOException;

public class CargarSintomas {
  private final SerializadorSintomas<Sintomas> serializadorSintomas;
  private ControlSinonimos controlSinonimos;
  private Sintomas sintomas;

  public CargarSintomas() {
    this.serializadorSintomas = new SerializadorSintomas<>("sintomas.dat", "");
    this.controlSinonimos = new ControlSinonimos();
    recuperarSintomas();
    iniciarMenuSintomas();
  }

  private void iniciarMenuSintomas() {
    try {
      MenuSintomas menuSintomas = new MenuSintomas("Sintomas", this, sintomas, this.controlSinonimos);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void recuperarSintomas() {
    this.sintomas = new Sintomas();
    if (serializadorSintomas.existeArchivo()) {
      this.sintomas = serializadorSintomas.deserializar();
    }
  }

  public void addSintoma(Sintoma sintoma) {
    this.sintomas.add(sintoma);
  }

  public Sintomas getSintomas() {
    return sintomas;
  }

  public void saveFile() {
    serializadorSintomas.serializar(sintomas);
  }
}
