import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.*;


/**
 * Clase VentanaDepart: Esta clase dibuja la ventana
 * para la gestión de los datos del fichero AleatorioDep.dat
 * @author AARRMM 2013
 * @version 1-2013
 * */
public class VentanaDepart extends JFrame implements ActionListener, iVentanaDepart  {

	private static final long serialVersionUID = 1L;

	JTextField num=new JTextField(10);
	JTextField nombre=new JTextField(25);
	JTextField loc=new JTextField(25);


	JLabel mensaje=new JLabel(" ----------------------------- ");
	JLabel titulo=new JLabel ("GESTIÓN DE DEPARTAMENTOS.");

	JLabel lnum = new JLabel ("NUMERO DEPARTAMENTO:");
	JLabel lnom = new JLabel ("NOMBRE:");
	JLabel lloc = new JLabel ("LOCALIDAD:");

	JButton balta= new JButton("Insertar Depar.t");
	JButton consu= new JButton("Consultar Depart.");
	JButton borra= new JButton("Borrar Depart.");
	JButton breset=new JButton("Limpiar datos.");
	JButton modif=new JButton("Modificar Departamento.");
	JButton ver=new JButton("Ver por consola.");
	JButton fin=new JButton("CERRAR");
	Color c; //para poner colores
	// WHITE,LIGHTGRAY,GRAY,DARKGRAY,BLUE,BLACK,RED,MAGENTA,PINK,ORANGE,CYAN,GREEN,YELLOW

	private String exixteDepart = " DEPARTAMENTO EXISTEEE.";

	private String deparError = " DEPARTAMENTO ERRÓNEO";

	/**
	 * Constructor. Dibuja la ventana 
	 * @param f, el frame donde la va a dibujar.
	 */

	public VentanaDepart(JFrame f )	{ 	
		setTitle("GESTIÓN DE DEPARTAMENTOS.");

		JPanel p0 = new JPanel();
		c = Color.CYAN;
		p0.add(titulo);
		p0.setBackground(c);

		JPanel p1 = new JPanel();
		p1.setLayout (new FlowLayout());
		p1.add(lnum);
		p1.add(num);p1.add(consu);

		JPanel p2 = new JPanel();
		p2.setLayout (new FlowLayout());
		p2.add(lnom);
		p2.add(nombre);

		JPanel p3 = new JPanel();
		p3.setLayout (new FlowLayout());
		p3.add(lloc);
		p3.add(loc);

		JPanel p4 = new JPanel();
		p4.setLayout (new FlowLayout());
		c = Color.YELLOW;
		p4.add(balta);	 p4.add(borra);p4.add(modif);
		p4.setBackground(c);

		JPanel p5 = new JPanel();
		p4.setLayout (new FlowLayout());
		c = Color.PINK;
		p5.add(breset);p5.add(ver);
		p5.add(fin);p5.setBackground(c);

		JPanel p7 = new JPanel();
		p7.setLayout (new FlowLayout());
		p7.add(mensaje);

		// para ver la ventana y colocar los controles verticalmente
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); 
		// añadir los panel al frame
		add(p0);add(p1);add(p2);add(p3);add(p4);add(p5);add(p7);
		pack(); //hace que se coloquen alineados los elementos de cada JPanel

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		balta.addActionListener(this);
		breset.addActionListener(this);
		fin.addActionListener(this);
		consu.addActionListener(this);
		borra.addActionListener(this);
		modif.addActionListener(this);
		ver.addActionListener(this);
	}

	/** Método actionPerformed
	 * @param e Objeto del tipo ActionEvent, recibe el evento
	 * @throws java.lang.NumberFormatException
	 * @throws IOException.
	 *
	 */

	public void actionPerformed(ActionEvent e) 	{   
		int dep, confirm;
		alta(e);

		consultas(e);

		String NOEXISTE_DEPART = " DEPARTAMENTO NO EXISTEEE.";
		baja(e, NOEXISTE_DEPART);
		modificacion(e, NOEXISTE_DEPART);
		if (e.getSource() == fin) { //SE PULSA EL BOTON salir 	
			System.exit(0);	
			//dispose();   	
		}
		if (e.getSource() == ver) { //SE PULSA EL BOTON  ver por consola  	
			try {
				mensaje.setText("Visualizando el fichero por la consolaa.....");    
				verporconsola();
			} catch (IOException e1) {
				System.out.println("ERRROR AL LEEERRRRRR AleatorioDep.dat");    
				//e1.printStackTrace();
			}	
		}
		if (e.getSource() == breset) { //SE PULSA EL BOTON  limpiar  	
			mensaje.setText(" has pulsado el boton limpiar..");    
			num.setText(" ");nombre.setText(" ");
			loc.setText(" ");
		}
	}

	@Override
	public void modificacion(ActionEvent e, String NOEXISTE_DEPART) {
		int dep;
		int confirm;
		if (e.getSource() == modif) { //SE PULSA EL BOTON  modificar  	
			mensaje.setText(" has pulsado el boton Modificar.");   
			try {
				dep=Integer.parseInt(num.getText());
				if (dep >0)
					if (consultar(dep)) {
						mensaje.setText(exixteDepart);  
						confirm=JOptionPane.showConfirmDialog(this, "ESTAS SEGURO DE MODIFICAR...", "AVISO MODIFICACIÓN.", 
								JOptionPane.OK_CANCEL_OPTION);	  
						// si devuelve 0 es OK
						//mensaje.setText(" has pulsado el boton Borrar "+ confirm);   
						if (confirm==0)  {
							modificar(dep);
							mensaje.setText(" REGISTRO MODIFICADO: " + dep);	
						}
					} 
					else {
						mensaje.setText(NOEXISTE_DEPART);	
						nombre.setText(" "); loc.setText(" ");
					}
				else mensaje.setText(" DEPARTAMENTO DEBE SER MAYOR QUE 0");	

			} catch(java.lang.NumberFormatException ex) //controlar el error del Integer.parseInt
			{mensaje.setText(deparError);} 
			catch (IOException ex2) 
			{mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (MODIFICAR)");} 
		}
	}

	@Override
	public void baja(ActionEvent e, String NOEXISTE_DEPART) {
		int dep;
		int confirm;
		if (e.getSource() == borra) {  //SE PULSA EL BOTON  borrar  	
			mensaje.setText(" has pulsado el boton Borrar");   
			try {
				dep=Integer.parseInt(num.getText());
				if (dep >0)
					if (consultar(dep)) {
						mensaje.setText(exixteDepart);   
						visualiza(dep);
						confirm=JOptionPane.showConfirmDialog(this, "ESTAS SEGURO DE BORRAR...", "AVISO BORRADO.", 
								JOptionPane.OK_CANCEL_OPTION);	  
						// si devuelve 0 es OK
						//mensaje.setText(" has pulsado el boton Borrar "+ confirm);   
						if (confirm==0)  {
							borrar(dep);
							mensaje.setText(" REGISTRO BORRADOO: " + dep);	
							nombre.setText(" "); loc.setText(" ");
						}
					} 
					else {
						mensaje.setText(NOEXISTE_DEPART);	
						nombre.setText(" "); loc.setText(" ");
					}
				else mensaje.setText(" DEPARTAMENTO DEBE SER MAYOR QUE 0");	

			} catch(java.lang.NumberFormatException ex) { //controlar el error del Integer.parseInt
				mensaje.setText(deparError);} 
			catch (IOException ex2) {
				mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (BORRAR)");} 
		}
	}

	@Override
	public void alta(ActionEvent e) {
		int dep;
		if (e.getSource() == balta) { //SE PULSA EL BOTON alta   	
			mensaje.setText(" has pulsado el boton alta");   
			try {
				dep=Integer.parseInt(num.getText());
				if (dep >0)
					if (consultar(dep))
						mensaje.setText(exixteDepart);   
					else {
					 mensaje.setText(" NUEVO DEPARTAMENTO");	
					grabar(dep, nombre.getText(), loc.getText());
					mensaje.setText(" NUEVO DEPARTAMENTO GRABADO");	
					}
				else mensaje.setText(" DEPARTAMENTO DEBE SER MAYOR QUE 0");	

			} catch(java.lang.NumberFormatException ex) { //controlar el error del Integer.parseInt
				mensaje.setText(deparError);} 
			catch (IOException ex2) {
				mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (ALTA)");
				// lo creo
			} 
		}
	}

	public void consultas(ActionEvent e) {
		int dep;
		if (e.getSource() == consu) { //SE PULSA EL BOTON  consultar  	
			mensaje.setText(" has pulsado el boton alta");   
			try {
				dep=Integer.parseInt(num.getText());
				if (dep >0)
					if (consultar(dep)) {
						mensaje.setText(" DEPARTAMENTO EXISTEEE.");   
						visualiza(dep);
					}
					else {
						mensaje.setText(" DEPARTAMENTO NO EXISTEEE.");	
						nombre.setText(" "); loc.setText(" ");
					}
				else mensaje.setText(" DEPARTAMENTO DEBE SER MAYOR QUE 0");	

			} catch(java.lang.NumberFormatException ex) //controlar el error del Integer.parseInt
			{mensaje.setText(" DEPARTAMENTO ERRÓNEO");}
			catch (IOException ex2) 
			{mensaje.setText(" ERRORRR EN EL FICHERO. Fichero no existe. (ALTA)");} 
		}
	}


	/**
	 * Método verporconsola
	 * Este método visualiza los datos del fichero en la consola.
	 * @throws IOException
	 */
	public static void verporconsola() throws IOException {     
		String  nom="",loc=""; int dep=0; long pos;
		File fichero = new File("AleatorioDep.dat");
		RandomAccessFile file = new RandomAccessFile(fichero, "r");
		char cad[] = new char[10], aux; 
		if (file.length()>0 ){
			pos=0;  //para situarnos al principio
			System.out.println(" ------------------------------------------");  
			System.out.println(" - - - VISUALIZO POR CONSOLAAAAA ");      
			for(;;){  //recorro el fichero, visualiza también las posiciones vacías
				file.seek(pos); 
				dep=file.readInt();   // obtengo el dep	  	  
				for (int i = 0; i < cad.length; i++) {
					aux = file.readChar();//recorro uno a uno los caracteres del apellido 
					cad[i] = aux;    //los voy guardando en el array
				}     
				nom= new String(cad);//convierto a String el array
				for (int i = 0; i < cad.length; i++) {
					aux = file.readChar();
					cad[i] = aux;  
				}     
				loc= new String(cad);//convierto a String el array
				System.out.println("DEP: " + dep + ", Nombre: "+  nom + ", Localidad: "+ loc);        
				pos= pos + 44;                
				//Si he recorrido todos los bytes salgo del for	 	  
				if (file.getFilePointer()==file.length())break;

			}//fin bucle for 
			file.close();  //cerrar fichero 
			System.out.println(" ------------------------------------------");  
		}
		else  //esto sólo sale la primera vez
			System.out.println(" ---------FICHERO VACIÍIOOOO --------------------");
	}// fin verporconsola

	/**
	 * Método consultar
	 * Este método consulta los datos de un departamento que se recibe como parámetro.
	 * @param dep: es el número del departamento a consultar.
	 * @return true o false dependiendo de si existe o no
	 * @throws IOException
	 */
	boolean consultar(int dep) throws IOException {		 
		long pos; int depa;
		File fichero = new File("AleatorioDep.dat");
		RandomAccessFile file = new RandomAccessFile(fichero, "r");
		// Calculo del reg a leer
		try { pos=44 * (dep-1);
		if (file.length()==0) { // si está vacío
			file.close();
			return false; 
		}
		file.seek(pos); 
		depa=file.readInt();   
		file.close(); 
		System.out.println("Depart leido:" + depa);   
		if (depa>0) return true;
		else return false;
		}catch (IOException ex2) {
			//System.out.println(" ERRORRR al leerrrrr..");
			return false;
		} 
	} // fin consultar

	void visualiza(int dep) {
		String nom="",loca=""; 
		long pos; int depa;
		File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "r");
			// Calculo del reg a leer
			pos=44 * (dep-1);
			file.seek(pos); 
			depa=file.readInt();   
			System.out.println("Depart leido:" + depa);   
			char nom1[]= new char[10], aux,  loc1[]= new char[10];
			for (int i=0;i<10;i++) {
				aux=file.readChar();
				nom1[i]=aux;
			}
			for (int i=0;i<10;i++) { 
				aux=file.readChar();
				loc1[i]=aux;
			}
			nom=new String (nom1);
			loca=new String (loc1);
			System.out.println("DEP: " + dep + ", Nombre: "+  nom + ", Localidad: "+ loca);  
			nombre.setText(nom);
			loc.setText(loca);
			file.close(); 
		}catch (IOException e1) {
			System.out.println("ERRROR AL LEEERRRRRR AleatorioDep.dat");    
			e1.printStackTrace();
		}	
	} // fin visualiza

	void borrar(int dep) {
		 // con borrar ponemos a 0 el dep que se quiere borrar
		// y a blancos el nombre y la localidad
		String nom="",loca="";  StringBuffer buffer = null;
		long pos; 
		File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			// Calculo del reg a leer
			pos=44 * (dep-1);
			file.seek(pos); 
			int depp=0;
			file.writeInt(depp);       
			buffer = new StringBuffer( nom );      
			buffer.setLength(10); 
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(loca);      
			buffer.setLength(10); 
			file.writeChars(buffer.toString());
			System.out.println("----REGISTRO BORRADO--------");
			file.close(); 
		}catch (IOException e1) {
			System.out.println("ERRROR AL BORRARRR AleatorioDep.dat");    
			e1.printStackTrace();
		}	
	} // fin borrar

	void modificar(int dep) {
		// con modificar asignamos los datos tecleados
		String nom="",loca="";  StringBuffer buffer = null;
		long pos; 
		try {
			RandomAccessFile file = new RandomAccessFile(new File("AleatorioDep.dat"), "rw");
			// Calculo del reg a leer
			pos=44 * (dep-1);
			file.seek(pos); 
			file.writeInt(dep);      
			nom=nombre.getText();
			loca=loc.getText();
			buffer = new StringBuffer(nom);      
			buffer.setLength(10); 
			file.writeChars(buffer.toString());
			buffer = new StringBuffer(loca);      
			buffer.setLength(10); 
			file.writeChars(buffer.toString());
			System.out.println("----REGISTRO MODIFICADOOO--------");
			file.close(); 
		}catch (IOException e1) {
			System.out.println("ERRROR AL MODIFICARRR AleatorioDep.dat");    
			e1.printStackTrace();
		}	
	} // fin modificar

	void grabar(int dep, String nom, String loc) { 
		long pos; StringBuffer buffer = null;
		File fichero = new File("AleatorioDep.dat");
		try {
			RandomAccessFile file = new RandomAccessFile(fichero, "rw");
			// Calculo del reg a leer
			pos=44 * (dep-1);
			//if (file.length()==0) return false; // si está vacío

			file.seek(pos); 
			file.writeInt(dep);       
			buffer = new StringBuffer( nom );      
			buffer.setLength(10); 
			file.writeChars(buffer.toString());//insertar nombre
			buffer = new StringBuffer( loc );      
			buffer.setLength(10); 
			file.writeChars(buffer.toString());//insertar loc
			file.close(); 
			System.out.println(" GRABADOOO el "+dep);  
		}catch (IOException e1) {
			System.out.println("ERRROR AL grabarr AleatorioDep.dat");    
			e1.printStackTrace();
		}	
	} // fin grabar

}//fin clase
