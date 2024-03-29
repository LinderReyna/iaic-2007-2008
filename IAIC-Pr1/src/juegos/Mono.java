package juegos;

import java.util.Vector;
import java.util.Enumeration;

import programa.Controlador;

import aima.search.Successor;

/** <b>Juego del Mono:</b><br>
 * Hay un mono en la puerta de una habitaci�n. En el centro de la habitaci�n hay un
 * pl�tano colgado del techo. El mono est� hambriento y quiere conseguir el pl�tano
 * pero no alcanza porque est� muy alto. En la habitaci�n tambi�n hay una ventana y
 * debajo de ella hay una caja que le permitir�a alcanzar el pl�tano si se subiera a ella.
 * El mono puede realizar las siguientes acciones: andar por el suelo, subirse a la caja,
 * empujar la caja (si el mono est� en la misma posici�n que la caja) y coger el pl�tano
 * (si est� subido encima de la caja y la caja est� justo debajo del pl�tano).<br>
 * \t0: el mono/caja se encuentran en la puerta<br>
 * \t1: el mono/caja se encuentran en la centro<br>
 * \t2: el mono/caja se encuentran en el ventana
 * @author Miguel Angel D�az
 * @author David Mart�n
 * @author Alberto Vaquero
 */
public class Mono extends Juego{

	/**
	 * Posicion del mono
	 */
	private int pos;
	
	/**
	 * Posicion de la caja
	 */
	private int caja;
	
	/**
	 * Si esta sobre la caja
	 */
	private boolean sobreCaja;
	
	/**
	 * Si ha cogido el platano
	 */
	private boolean platano;
	
	/**
	 * Crea un estado del juego del mono segun el avance del juego
	 * @param pos Posicion del mono
	 * @param subido Si esta subido a la caja
	 * @param caja Posicion de la caja
	 * @param platano Si tiene el platano
	 * @param c Controlador. Poner null para imprimir por pantalla
	 */
	public Mono(int pos, boolean subido, int caja, boolean platano, Controlador c){
		this.pos = pos;
		this.sobreCaja = subido;
		this.caja = caja;
		this.platano = platano;
		cont = c;
	}	
	
	/**
	 * Indica si un estado es solucion
	 * @return true si es final el estado
	 */
	public boolean isGoal(){
		return platano == true;
	}
	
	/**
	 * Genera los sucesores del estado actual
	 * @return Enumeration con los sucesores generados
	 */
	public Enumeration<Successor> successors(){

	 	Vector<Successor> successorVec = new Vector<Successor>();

	 	String operador = "";
	 	nodosExpandidos++;

	 	if (pos != 2 && !sobreCaja){
	 		operador = "andaHaciaVentana";
	 		Mono nuevoEstado = new Mono(pos+1,sobreCaja,caja,platano,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos != 0 && !sobreCaja){
	 		operador = "andaHaciaPuerta";
	 		Mono nuevoEstado = new Mono(pos-1,sobreCaja,caja,platano,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos == caja && sobreCaja && pos != 2){
	 		operador = "empujaCajaHaciaVentana";
	 		Mono nuevoEstado = new Mono(pos+1,sobreCaja,caja+1,platano,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos == caja && sobreCaja && pos !=0){
	 		operador = "empujaCajaHaciaPuerta";
	 		Mono nuevoEstado = new Mono(pos-1,sobreCaja,caja-1,platano,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos == caja && !sobreCaja){
	 		operador ="subeCaja";
	 		Mono nuevoEstado = new Mono(pos,true,caja,platano,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}
	 	
	 	if (pos == caja && sobreCaja){
	 		operador ="bajaCaja";
	 		Mono nuevoEstado = new Mono(pos,false,caja,platano,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}
	 	
	 	if (sobreCaja && caja == 1 && !platano){
	 		operador ="cogePlatano";
	 		Mono nuevoEstado = new Mono(pos,sobreCaja,caja,true,cont);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}
	 		 
	 	return successorVec.elements();
	}
	
	/**
	 * Genera la heuristica del estado en el que se encuentra
	 * @return devuelve la heuristica correspondiente al estado actual
	 */
	public float h() {
		if (pos == 0) return 5;
		if (pos == 2 && caja != 2) return 5;
		if (pos == 1 && caja != 1) return 4;
		if (pos == 2 && caja == 2 && !sobreCaja) return 3;
		if (pos == 2 && caja == 2 && sobreCaja) return 2;
		if (pos == 1 && sobreCaja && !platano) return 1;
		if (pos == 1 && sobreCaja && platano) return 0;
		return 6;
	}
	
	/**
	 * Genera un mensaje del estado en el que se encuentra
	 * @return un string con el mensaje que especifica en el estado que se encuentra
	 */
	public String toString() {
		return "\t(Pos:" + pos + ", Subido:" + (sobreCaja?1:0) + ", C:" + caja + ", Pl:" + (platano?1:0) + ")";
	}
	
	/**
	 * Prueba el problema del mono con todas las estrategias
	 * @param args
	 */
    public static void main(String[] args){
		Mono m = new Mono(0, false, 2, false, null);
		System.out.println(m);
		for (int i=1; i<=6; i++)
			m.resolver(i);
	}
	 
}

