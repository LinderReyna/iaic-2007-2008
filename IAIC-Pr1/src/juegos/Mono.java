package juegos;

import aima.search.AStarSearch;
import aima.search.BreadthFirstSearch;
import aima.search.DepthBoundedSearch;
import aima.search.GreedySearch;
import aima.search.Heuristic;
import aima.search.IteratedDeepeningSearch;
import aima.search.SearchNode;
import aima.search.State;
import aima.search.Successor;
import aima.search.UniformCostSearch;

import java.util.ArrayList;
import java.util.Vector;
import java.util.Enumeration;

/** <b>Problema del Mono:</b><br>
 * Hay un mono en la puerta de una habitaci�n. En el centro de la habitaci�n hay un
 * pl�tano colgado del techo. El mono est� hambriento y quiere conseguir el pl�tano
 * pero no alcanza porque est� muy alto. En la habitaci�n tambi�n hay una ventana y
 * debajo de ella hay una caja que le permitir�a alcanzar el pl�tano si se subiera a ella.
 * El mono puede realizar las siguientes acciones: andar por el suelo, subirse a la caja,
 * empujar la caja (si el mono est� en la misma posici�n que la caja) y coger el pl�tano
 * (si est� subido encima de la caja y la caja est� justo debajo del pl�tano).<br>
 * \t0: el mono/caja se encuentran en la puerta.<br>
 * \t1: el mono/caja se encuentran en la centro.<br>
 * \t2: el mono/caja se encuentran en el ventana.
 */
public class Mono implements State,Heuristic{

	private static int nodosExpandidos = 0;
	
	private int pos;
	
	private int caja;
	
	private boolean sobreCaja;
	
	private boolean platano;
	
	/**
	 * Crea un estado del juego del mono segun el avance del juego
	 */
	public Mono(int pos, boolean subido, int caja, boolean platano){
		this.pos = pos;
		this.sobreCaja = subido;
		this.caja = caja;
		this.platano = platano;
	}	
	
	/**
	 * Modifica la posicion del mono encima de la caja o en el suelo
	 * @param p indica la posicion del mono
	 */
	 public void ponPos(int p){
		this.pos = p;
	 }	
	 
	 /**
	  * Indica la posicion de la ventana
	  * @param p
	  */
	public void ponSubido(boolean s){
		this.sobreCaja = s;
	}
	
	public void ponCaja(int p){
		this.caja = p;
	}	
	
	public void ponPlatano(boolean p){
		this.platano = p;
	}
	
	public int damePos(){
		return this.pos;
	}
	
	public boolean dameSubido(){
		return this.sobreCaja;
	}
	
	public int damePosCaja(){
		return this.caja;
	}
	
	public boolean damePlatano(){
		return this.platano;
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
	 		Mono nuevoEstado = new Mono(pos+1,sobreCaja,caja,platano);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos != 0 && !sobreCaja){
	 		operador = "andaHaciaPuerta";
	 		Mono nuevoEstado = new Mono(pos-1,sobreCaja,caja,platano);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos == caja && sobreCaja && pos != 2){
	 		operador = "empujaCajaHaciaVentana";
	 		Mono nuevoEstado = new Mono(pos+1,sobreCaja,caja+1,platano);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos == caja && sobreCaja && pos !=0){
	 		operador = "empujaCajaHaciaPuerta";
	 		Mono nuevoEstado = new Mono(pos-1,sobreCaja,caja-1,platano);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}

	 	if (pos == caja && !sobreCaja){
	 		operador ="subeCaja";
	 		Mono nuevoEstado = new Mono(pos,true,caja,platano);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}
	 	
	 	if (pos == caja && sobreCaja){
	 		operador ="bajaCaja";
	 		Mono nuevoEstado = new Mono(pos,false,caja,platano);
		 	successorVec.addElement(new Successor(nuevoEstado, operador, 1));
		}
	 	
	 	if (sobreCaja && caja == 1 && !platano){
	 		operador ="cogePlatano";
	 		Mono nuevoEstado = new Mono(pos,sobreCaja,caja,true);
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
		return "(Pos:" + pos + ", Subido:" + (sobreCaja?1:0) + ", C:" + caja + ", Pl:" + (platano?1:0) + ")";
	}
	
	/**
	 * Devuelve el numero de nodos expandidos
	 * @return  devuelve el numero de nodos expandidos
	 */
	public int dameNodosExpandidos(){
		 return nodosExpandidos;
	}
	
	/**
	 * Actualiza el numero de nodos expandidos
	 * @param n actualiza el numero de nodos expandidos a n
	 */
	public void ponNodosExpandidos(int n){
		nodosExpandidos = n;
	}
	
	/**
 	 * resuleve el problema del mono
 	 * @param e indica la estrategia usada para resilverlo
 	 * @return true en casa de tener solucion false en caso contrario
 	 */
    private boolean resolver(int e){
 		   
 		boolean resuelto = true;
 		switch(e){
	 		case 1:
	 			System.out.println("Primero en profundidad (profunidad m�xima 7):\n");
	 			resuelto=listPath((new DepthBoundedSearch(this,7)).search());
	 			System.out.println("NodosExpandidos: "+nodosExpandidos+"\n");
	 			nodosExpandidos = 0;
				System.out.println("\n");break;
	 	
	 		case 2:
	 			System.out.println("Primero en anchura:\n");
	 			resuelto=listPath((new BreadthFirstSearch(this)).search());
	 			System.out.println("NodosExpandidos: "+nodosExpandidos+"\n");
	 			nodosExpandidos = 0;
				System.out.println("\n");break;
	 	
	 		case 4:
	 			System.out.println("Coste Uniforme:\n");
	 			resuelto = listPath((new UniformCostSearch(this)).search());
	 			System.out.println("NodosExpandidos: "+nodosExpandidos+"\n");
	 			nodosExpandidos = 0;
				System.out.println("\n");break;
	 	
	 		case 5:
	 			System.out.println("Profundidad iterativa:\n");
	 			resuelto=listPath((new IteratedDeepeningSearch(this)).search());
	 			System.out.println("NodosExpandidos: "+nodosExpandidos+"\n");
	 			nodosExpandidos = 0;
				System.out.println("\n");break;
	 	
	 		case 3:
	 			System.out.println("Busqueda A*:\n");
	 			resuelto=listPath((new AStarSearch(this)).search());
	 			System.out.println("NodosExpandidos: "+nodosExpandidos+"\n");
	 			nodosExpandidos = 0;
				System.out.println("\n");break;
	 	
	 		case 6:
	 			System.out.println("Escalada:\n");
	 			resuelto = listPath((new GreedySearch(this)).search());
	 			System.out.println("NodosExpandidos: "+nodosExpandidos+"\n");
	 			nodosExpandidos = 0;
				System.out.println("\n");break;
	 		}
 		
 		return resuelto;
 	}
    
    private boolean listPath(SearchNode node) {
        ArrayList<String> camino = new ArrayList<String>();
 	    if (node == null) {
 		    System.out.println("No hay soluci�n");
 		    return false;
 	    }
 	    String linea = "";
 	    while (node.getParent()!=null) {
 		    linea =  "Estado: " + node.getState() +
            				  " Profundidad: " + node.getDepth() +
            				  " Coste: " + node.getPathCost() +
            				  " Operador: " + node.getAppliedOp();
 		    camino.add("\n"+linea);
 		    node = node.getParent();
 	    }
 	  
 	    linea = ( "\nESTADO INICIAL: " + node.getState());  
 	    camino.add(linea);
 	    for (int j=camino.size()-1; j>=0;j--){
 	    	System.out.print((String)camino.get(j));
 	    }
 	    System.out.println("\n");
 	    return true;
    }
	 
	/**
	 * Prueba el problema del mono con todas las estrategias
	 * @param args
	 */
    public static void main(String[] args){
		Mono m = new Mono(0,false,2,false);
		System.out.println(m);
		for (int i=1; i<=6; i++)
			m.resolver(i);
	}
	 
}

