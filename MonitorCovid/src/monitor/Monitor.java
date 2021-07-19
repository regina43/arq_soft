package monitor;

import cargarregistros.CargarRegistros;
import cargarsintomas.CargarSintomas;
import diagnosticos.DiagnosticoPorFases;

import java.util.Scanner;

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
    System.out.println("Resultado: " + resultadoDiagnostico);
    cargarSeguimiento();
    String res = "";
    boolean consulta = false;
    switch (resultadoDiagnostico) {
      case 1:
        res = "Se ha iniciado el seguimiento de sus sintomas.\nEs importante que se haga un control diario y visite al medico.";
        consulta = false;
        break;
      case 2:
        res = "Anteriormente se le recomendo que visite al medico.\n¿Ha seguido las recomendaciones?";
        consulta = true;
        break;
      case 4:
        res = "Se ha iniciado el monitoreo de sintomas en segunda fase.\nEs urgente que vaya al medico y se haga una prueba rapida de covid.";
        break;
      case 5:
        res = "Anteriormente se le recomendo que visite al medico de manera urgente.\nTambien se le recomendo realizarse una prueba rapida de covid.\n¿Ha seguido las recomendaciones?";
        consulta = true;
        break;
      case 6:
        res = "El seguimiento se ha terminado.";
        consulta = false;
        break;
      default:
        res = "Actualmente, no hay nada que sugiera que tiene covid.\nSi presenta cualquier sintoma de covid use de nuevo este verificador.";
    }
    int dia = this.nseguimiento.getDias();
    if (dia >=1 && dia <=3){
      System.out.println("Primera Fase\nDia: "+dia);
    }else if (dia >= 4 && dia <= 8){
      System.out.println("Segunda Fase\nDia: "+dia);
    }
    System.out.println(res);
    if (consulta){
      mostrarConsulta();
    }
  }

  private void mostrarConsulta() {
    Scanner lector = new Scanner(System.in);
    System.out.println("1.- Si\n2.- No");
    int num2 = lector.nextInt();
    if (num2 == 1){
      this.nseguimiento.reiniciarSeguimiento();
      System.out.println("Que bueno que haya seguido las recomendaciones.");
    }else{
      System.out.println("Es importante que lo haga. Puede estar en riesgo.");
    }
    guardarSeguimiento();
  }

  private void guardarSeguimiento() {
    this.nserializador.serializar(nseguimiento);
  }

  public int getResultado() {
    return resultadoDiagnostico;
  }
}
