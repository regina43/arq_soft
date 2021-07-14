package diagnosticos;

import monitor.Seguimiento;
import monitor.SerializadorSeguimiento;
import monitor.FuncionDiagnostico;
import monitor.Sintoma;
import monitor.Sintomas;
import monitor.Registros;
import monitor.Registro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiagnosticoPorFases extends FuncionDiagnostico {

  private Map<Sintoma, Integer> pesos;
  private Seguimiento seguimiento;
  private final SerializadorSeguimiento serializador;
  private final int porcentaje_50_PF;
  private final int porcentaje_50_SF;

  public DiagnosticoPorFases(Sintomas ls) {
    super(ls);
    pesos = new HashMap<>();
    int cantPrimeraFase = 0;
    int cantSegundaFase = 0;
    for (Sintoma s : ls) {
      pesos.put(s, s.peso());
      if (s.getClass().getSimpleName().equals("PrimeraFase")) {
        cantPrimeraFase++;
      } else {
        cantSegundaFase++;
      }
    }
    this.porcentaje_50_PF = cantPrimeraFase / 2;
    this.porcentaje_50_SF = cantSegundaFase / 2;
    this.serializador = new SerializadorSeguimiento("seguimiento.dat", "");
    cargarSeguimiento();
  }

  private void cargarSeguimiento() {
    this.seguimiento = new Seguimiento();
    if (serializador.existeArchivo()) {
      this.seguimiento = serializador.deserializar();
    }
  }

  @Override
  public int diagnostico(Registros registros) {
    int recomendacion = 0;
    if (!registros.isEmpty()) {
      recomendacion = hacerSeguimientoo(registros);
    }
    guardarSeguimiento();
    return recomendacion;
  }

  private int hacerSeguimientoo(Registros registros) {
    int recomendacion = 0;
    if (!this.seguimiento.iniciado()) {
      Registro registro = registros.peek();
      if (esPosibleIniciar(registro)) {
        recomendacion = 1;//inicio seguimiento
        seguimiento.iniciarPF();
        seguimiento.registrarUltimoRegistro(registro);
      }
    } else if (!seguimiento.terminado()){
      recomendacion = realizarSeguimiento(registros);//continuar
    }else {
      recomendacion = 6;
    }
    return recomendacion;
  }

  private boolean esPosibleIniciar(Registro registro) {
    boolean posible;
    Sintomas sintomas = registro.getSintomas();
    int peso = calcularPeso("PrimeraFase", sintomas);
    posible = peso >= porcentaje_50_PF;
    return posible;
  }

  private int realizarSeguimiento(Registros registros) {
    int recomendacion = 0;
    if (this.seguimiento.estaPrimeraFase()) {
      recomendacion = realizarSeguimientoPF(registros);
    } else if (this.seguimiento.estaSegundaFase()) {
      recomendacion = realizarSeguimientoSF(registros);
    }
    return recomendacion;
  }

  private int realizarSeguimientoSF(Registros registros) {
    int recomendacion;
    Registro actual = registros.peek();
    Registro ultimo = seguimiento.getUltimo();
    if (diferenciaDias(actual.getFecha(), ultimo.getFecha()) == 0){//cambiar
      Sintomas sactual = actual.getSintomas();
      if (porcentajeValidoSintomas(sactual)){
        if (this.seguimiento.getDias() == 4) {
          recomendacion = 4; //inicio de sf
          this.seguimiento.aumentarDias();
          seguimiento.registrarUltimoRegistro(actual);
        } else {
          recomendacion = 5;//dentro de sf
          this.seguimiento.aumentarDias();
          if (this.seguimiento.esUltimoDiaSF()){
            this.seguimiento.terminarSeguimiento();
            recomendacion = 6;//terminado
          }
          seguimiento.registrarUltimoRegistro(actual);
        }
      }else {
        recomendacion = 1; //reinicio de pf
        this.seguimiento.iniciarSF();
        seguimiento.registrarUltimoRegistro(actual);
      }
    }else {
      recomendacion = 1; //reinicio de pf
      this.seguimiento.iniciarSF();
      seguimiento.registrarUltimoRegistro(actual);
    }
    return recomendacion;
  }

  private int realizarSeguimientoPF(Registros registros) {
    int recomendacion;
    Registro actual = registros.peek();
    Registro ultimo = seguimiento.getUltimo();
    if (diferenciaDias(actual.getFecha(), ultimo.getFecha()) == 0) {//cambiar
      Sintomas sactual = actual.getSintomas();
      if (porcentajeValidoSintomas(sactual)) {
        recomendacion = 2;//dentro de pf
        this.seguimiento.aumentarDias();
        if (this.seguimiento.esUltimoDiaPF()) {
          this.seguimiento.iniciarSF();
          seguimiento.registrarUltimoRegistro(actual);
        }
      } else {
        recomendacion = 1; //reinicio de pf
        this.seguimiento.iniciarPF();
        seguimiento.registrarUltimoRegistro(actual);
      }
    } else {
      recomendacion = 1; //reinicio de pf
      this.seguimiento.iniciarPF();
      seguimiento.registrarUltimoRegistro(actual);
    }
    return recomendacion;
  }

  private boolean porcentajeValidoSintomas(Sintomas sactual) {
    boolean valido = false;
    int peso = calcularPeso(sactual);
    if (seguimiento.estaPrimeraFase()) {
      valido = peso >= porcentaje_50_PF;
    } else if (seguimiento.estaSegundaFase()) {
      valido = peso >= porcentaje_50_SF;
    }
    return valido;
  }

  private int calcularPeso(Sintomas sintomas) {
    int peso;
    if (this.seguimiento.estaPrimeraFase()) {
      peso = calcularPeso("PrimeraFase", sintomas);
    } else {
      peso = calcularPeso("SegundaFase", sintomas);
    }
    return peso;
  }

  private int calcularPeso(String fase, Sintomas sintomas) {
    int peso = 0;
    for (Sintoma sintoma : sintomas) {
      String className = sintoma.getClass().getSimpleName();
      if (className.equals(fase)) {
        peso++;
      }
    }
    return peso;
  }

  private void guardarSeguimiento() {
    this.serializador.serializar(seguimiento);
  }

  private int diferenciaDias(Date fechaI, Date fechaF) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String fecha1 = dateFormat.format(fechaI);
    String fecha2 = dateFormat.format(fechaF);

    Date fechaInicial = null;
    try {
      fechaInicial = dateFormat.parse(fecha1);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date fechaFinal = null;
    try {
      fechaFinal = dateFormat.parse(fecha2);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    int dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
    return dias;
  }
}

