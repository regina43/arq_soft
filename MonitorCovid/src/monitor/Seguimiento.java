package monitor;

import java.io.Serializable;

public class Seguimiento implements Serializable {
  private final int ULTIMO_DIA_PF = 3;
  private final int ULTIMO_DIA_SF = 8;
  private Registro ultimo;
  private int dia;
  private boolean primeraFase;
  private boolean segundaFase;
  private boolean iniciado;
  private boolean terminado;

  public Seguimiento() {
    this.ultimo = null;
    this.dia = 0;
    this.primeraFase = false;
    this.segundaFase = false;
    this.iniciado = false;
  }

  public void setDia(int ndia) {
    this.dia = ndia;
  }

  public boolean estaPrimeraFase(){
    return this.primeraFase;
  }

  public boolean estaSegundaFase(){
    return this.segundaFase;
  }

  public boolean iniciado() {
    return this.iniciado;
  }

  public boolean terminado(){
    return this.terminado;
  }

  public Registro getUltimo(){
    return this.ultimo;
  }

  public void registrarUltimoRegistro(Registro registro){
    this.ultimo = registro;
  }

  public void aumentarDias() {
    dia++;
  }

  public boolean esUltimoDiaPF() {
    return dia == ULTIMO_DIA_PF;
  }

  public int getDias(){
    return dia;
  }

  public void iniciarSF() {
    this.dia = 4;
    this.primeraFase = false;
    this.segundaFase = true;
  }

  public void iniciarPF() {
    this.iniciado = true;
    this.primeraFase = true;
    this.dia = 1;
  }

  public boolean esUltimoDiaSF() {
    return dia == ULTIMO_DIA_SF;
  }

  public void terminarSeguimiento() {
    this.primeraFase = false;
    this.segundaFase = false;
    this.terminado = true;
  }

  public void reiniciarSeguimiento() {
    if (primeraFase) {
      iniciarPF();
    }else if (segundaFase){
      iniciarSF();
    }
  }
}
