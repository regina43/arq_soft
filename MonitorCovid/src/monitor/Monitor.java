package monitor;

import cargarregistros.CargarRegistros;
import cargarsintomas.CargarSintomas;
import diagnosticos.DiagnosticoPorFases;

public class Monitor {

  private Sintomas sintomas;
  private Registros registros;
  private FuncionDiagnostico funcion;
  private CargarRegistros cargarRegistros;
  private int resultadoDiagnostico;
  private Seguimiento nseguimiento;
  private SerializadorSeguimiento nserializador;

  public Monitor() {
    CargarSintomas cargarSintomas = new CargarSintomas();
    sintomas = cargarSintomas.getSintomas();
    funcion = new DiagnosticoPorFases(sintomas);
    registros = new Registros();
    nserializador = new SerializadorSeguimiento("seguimiento.dat", "");
    cargarSeguimiento();
    filtrarSintomas();
    cargarRegistros = new CargarRegistros(sintomas);
  }

  private void filtrarSintomas() {
    if (!this.nseguimiento.iniciado() || this.nseguimiento.estaPrimeraFase()){
      sintomas = sintomas.getSintomasFase("PrimeraFase");
    }
  }

  private Sintomas filtrarPrimeraFase(Sintomas ls) {
    Sintomas sintomas = new Sintomas();
    for (Sintoma sintoma: ls) {
      if (sintoma.peso() == 10) {
        sintomas.add(sintoma);
      }
    }
    return sintomas;
  }

  private void cargarSeguimiento() {
    this.nseguimiento = new Seguimiento();
    if (nserializador.existeArchivo()) {
      this.nseguimiento = nserializador.deserializar();
    }
  }

  public void monitorear() {
    registros = cargarRegistros.getRegistros();
    resultadoDiagnostico = funcion.diagnostico(registros);
    mostrarDiaFase(resultadoDiagnostico);
  }

  private void mostrarDiaFase(int resultadoDiagnostico){
    System.out.println(resultadoDiagnostico);
    String res = "";
    switch (resultadoDiagnostico) {
      case 1:
        res = "Se ha iniciado el seguimiento de sus sintomas.\nEs importante que se haga un control diario y visite al medico.";
        break;
      case 2:
        res = "Anteriormente se le recomendo que visite al medico.\n¿Ha seguido las recomendaciones?";
        break;
      case 4:
        res = "Se ha iniciado el monitoreo de sintomas en segunda fase.\nEs urgente que vaya al medico y se haga una prueba rapida de covid.";
        break;
      case 5:
        res = "Anteriormente se le recomendo que visite al medico de manera urgente.\nTambien se le recomendo realizarse una prueba rapida de covid.\n¿Ha seguido las recomendaciones?";
        break;
      case 6:
        res = "El seguimiento se ha terminado.";
        break;
      default:
        res = "Actualmente, no hay nada que sugiera que tiene covid.\nSi presenta cualquier sintoma de covid use de nuevo este verificador.";
    }
    System.out.println(res);
  }

  public int getResultado() {
    return resultadoDiagnostico;
  }
}
