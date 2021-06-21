package cargarsintomas;

import cargarsintomas.tools.SerializadorSintomas;
import cargarsintomas.view.MenuSintomas;
import monitor.Sintomas;

import java.io.IOException;

public class CargarSintomas {
  private final SerializadorSintomas<Sintomas> serializadorSintomas;
  private Sintomas sintomas;

  public CargarSintomas(){
    serializadorSintomas = new SerializadorSintomas<>("sintomas.dat","cargarsintomas");
    recuperarSintomas();
    iniciarMenuSintomas();
  }

  private void iniciarMenuSintomas(){
    try {
      MenuSintomas menuSintomas = new MenuSintomas("Sintomas", this, sintomas);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void recuperarSintomas() {
    this.sintomas = new Sintomas();
    if (serializadorSintomas.deserializar() != null) {
      sintomas = serializadorSintomas.deserializar();
    }
  }

  public Sintomas getSintomas() {
    return sintomas;
  }

  public void saveFile() {
    serializadorSintomas.serializar(sintomas);
  }
}
