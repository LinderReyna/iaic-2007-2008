package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import programa.*;

public class Vista extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenuItem jugar;
	
	private Controlador controlador;
	
	private JTextArea area;
	
	private String mensaje;
	
	private Solicitud solicitud2;
	
	/**
	 * crea una ventana principal del juego que hace de interfaz con el usuario
	 *
	 */
	public Vista() {
		this.setSize(1025,735);
                 setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		OyenteJuego oyenteJuego=new OyenteJuego();
		OyenteAbrir oyenteAbrir=new OyenteAbrir();
		JPanel panel= new JPanel();
		area=new JTextArea();
		area.setEditable(false);
		JScrollPane scroll=new JScrollPane(area);
		panel.add(scroll);
		panel.setVisible(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(0,10,10,10));
		add(panel);
		JMenuBar menu=new JMenuBar();
		JMenu opciones= new JMenu("Archivo");
		jugar= new JMenuItem("EmpezarJuego");
		JMenuItem cargar= new JMenuItem("Abrir");
		jugar.addActionListener(oyenteJuego);
		cargar.addActionListener(oyenteAbrir);
		jugar.setEnabled(false);
		opciones.add(jugar);
		opciones.add(cargar);
		menu.add(opciones);
		setJMenuBar(menu);
		setVisible(true);
	}
	
	/**
	 * permite al usuario cargar el archivo que quiera
	 * @author Usuario
	 *
	 */
	class OyenteAbrir implements ActionListener{
		public void actionPerformed(ActionEvent e){
			JFileChooser selector= new JFileChooser();
			selector.setCurrentDirectory(new File(".."));
			selector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        int opcion=selector.showOpenDialog(null);
	        if(opcion==JFileChooser.APPROVE_OPTION){
	        	File archivo=selector.getSelectedFile();
	        	String ruta=archivo.getAbsolutePath();	        	
	        	Vector habitaciones=new Vector();
	        	Fichero f=new Fichero();
	        	try{
	        		habitaciones=f.cargarTablero(ruta);
	        		jugar.setEnabled(true);
	        		controlador.cargar(habitaciones);
	        	}catch(Exception ex){
	        		mostrar("Imposible abrir el archivo");
	        	}
	        	
	        	
	        }
		}		
	}
	
	/**
	 * asocia un controlador a la vista actual para que haya una comunicacion entre ellos
	 * @param controlador que se asocia a la vista
	 */
	public void asociarControlador(Controlador controlador){
		this.controlador=controlador;
	}
	
	/**
	 * genera una solicitud de estrategia la usuarioa para resolver cada una de las habitaciones
	 * @param b
	 * @return el nuemro de estrategia
	 */
	public int solicitud(boolean b){
		if (b){
			solicitud2=new Solicitud();
		}else{		
			solicitud2=new Solicitud(b);
		}
		int juego=9;
		if(JOptionPane.showConfirmDialog(null,solicitud2
				,"Seleccione Busqueda",JOptionPane.OK_OPTION
				,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){
			boolean cuatro=false;
			boolean seis=false;
			boolean uno = solicitud2.profundidad.isSelected();
			boolean dos = solicitud2.anchura.isSelected();
			boolean tres = solicitud2.aEstrella.isSelected();
			if(b)cuatro = solicitud2.uniforme.isSelected();
			boolean cinco = solicitud2.profIterativa.isSelected();
			if(b) seis = solicitud2.escalada.isSelected();
			if(uno&&!dos&&!tres&&!cuatro&&!cinco&&!seis){
				juego=1;				
				//aplicacion ejecutar con busqueda 1
			}else
				if(!uno&&dos&&!tres&&!cuatro&&!cinco&&!seis){
					juego=2;
					//aplicacion ejecutar con busqueda 2
				}else
					if(!uno&&!dos&&tres&&!cuatro&&!cinco&&!seis){
						juego=3;
						//aplicacion ejecutar con busqueda 3
					}
					else
						if(!uno&&!dos&&!tres&&cuatro&&!cinco&&!seis){
							juego=4;
							//aplicacion ejecutar con busqueda 4
						}
						else
							if(!uno&&!dos&&!tres&&!cuatro&&cinco&&!seis){
								//aplicacion ejecutar con busqueda 5
								juego=5;
							}
							else
								if(!uno&&!dos&&!tres&&!cuatro&&!cinco&&seis){
									//aplicacion ejecutar con busqueda 6
									juego=6;
								}else{
									Message panel=new Message("Selccione una opcion");
									JOptionPane.showMessageDialog(null,"Seleccione una estrategia de busqueda"
						                    ,"AVISO",JOptionPane.WARNING_MESSAGE);	
									juego = solicitud(b);
									
								}
			}else{
				juego=solicitud(b);
			}
			return juego;
	}
	
	/**
	 * muestra un mensjae por pantalla
	 * @param s mensaje que se muestra por pantalla
	 */
	public void mostrar(String s){		
		mensaje+=s;
		area.setText(mensaje);
	}
	
	/**
	 * limpia la consola de la interfaz
	 *
	 */
	public void limpiar(){
		area.cut();
		mensaje="";
	}
	
	/**
	 * solicita una busqueda para solucionar el laberinto
	 *
	 */
	class OyenteJuego implements ActionListener{
		public void actionPerformed(ActionEvent e){
			limpiar();
			recursion();
		}			
		
	}//oyenteJuego
	
	private void recursion(){
		Solicitud solicitud = new Solicitud();
		if (JOptionPane.showConfirmDialog(null, solicitud,
									"Seleccione Busqueda", JOptionPane.OK_OPTION,
									JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){
			boolean uno = solicitud.profundidad.isSelected();
			boolean dos = solicitud.anchura.isSelected();
			boolean tres = solicitud.aEstrella.isSelected();
			boolean cuatro = solicitud.uniforme.isSelected();
			boolean cinco = solicitud.profIterativa.isSelected();
			boolean seis = solicitud.escalada.isSelected();
			//aplicacion ejecutar con busqueda 1
			if (uno&&!dos&&!tres&&!cuatro&&!cinco&&!seis){
				mostrar("EMPIEZA EL LABERINTO");
				mostrar("busqueda en profundidad");
				controlador.jugar(1);
				
			//aplicacion ejecutar con busqueda 2
			} else if(!uno&&dos&&!tres&&!cuatro&&!cinco&&!seis){
				mostrar("EMPIEZA EL LABERINTO\n");
				mostrar("busqueda en anchura\n");
				controlador.jugar(2);
			
			//aplicacion ejecutar con busqueda 3
			} else if(!uno&&!dos&&tres&&!cuatro&&!cinco&&!seis){
				mostrar("EMPIEZA EL LABERINTO\n");
				mostrar("busqueda en A*\n");
				controlador.jugar(3);
			
			//aplicacion ejecutar con busqueda 4
			} else if(!uno&&!dos&&!tres&&cuatro&&!cinco&&!seis){
				mostrar("EMPIEZA EL LABERINTO\n");
				mostrar("busqueda uniforme\n");
				controlador.jugar(4);
				
			//aplicacion ejecutar con busqueda 5
			} else if(!uno&&!dos&&!tres&&!cuatro&&cinco&&!seis){
				mostrar("EMPIEZA EL LABERINTO\n");
				mostrar("busqueda en profundidad iterativa\n");
				controlador.jugar(5);
				
			//aplicacion ejecutar con busqueda 6
			} else if(!uno&&!dos&&!tres&&!cuatro&&!cinco&&seis){
				mostrar("EMPIEZA EL LABERINTO\n");
				mostrar("busqueda en escalada\n");
				// TODO no seria un 6?
				controlador.jugar(2);
			} else{
				Message panel = new Message("Selccione una opcion");
				JOptionPane.showMessageDialog(null, new JPanel(), "aviso", JOptionPane.WARNING_MESSAGE);
				recursion();
			}
		}
	}

}
