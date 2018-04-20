package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimWindow;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;

public class MainWindow extends JFrame implements SimulatorListener {
	private Controller ctrl; 
	private OutputStream reportsOutputStream;
	private JPanel mainPanel;
	private JPanel contentPanel_1; // tantos como areas en la ventana
	private JFileChooser fc;
	private File currentFile;
	private JTextArea eventsEditor;
	private JTable eventsTable; 
	private JTextArea reportsArea; 
	private JTable vehiclesTable; 
	private JTable roadsTable; 
	private JTable junctionsTable; 
	//private ReportDialog reportDialog; -> opcional
	
	public MainWindow() {
		super("Simulador de trafico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addBars();
		initGUI();
		
		setSize(1000, 1000);		
		setVisible(true);
	}
	
	public MainWindow(TrafficSimulator model, String inFileName, Controller ctrl) {
			 super("Traffic Simulator");
			 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 
			 this.ctrl = ctrl;
			 currentFile = inFileName != null ? new File(inFileName) : null;
			 //reportsOutputStream = new JTextAreaOutputStream(reportsArea,null);
			 //ctrl.setOutputStream(reportsOutputStream); // ver sección 8
			 
			 addBars();
			 initGUI();
			 model.addSimulatorListener(this);
			 
			 setVisible(true);
			}

	@Override
	public void update(UpdateEvent ue, String error) {
		// TODO Auto-generated method stub

	}
	
	private void initGUI() {
		//Split de ventanas
		Component componenteIzquierdo = new JPanel();
		Component componenteDerecho = new JPanel();
		JSplitPane bottomSplit = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				componenteIzquierdo, componenteDerecho);
		
		Component componenteDeArriba = new JPanel();
		Component componenteDeAbajo = new JPanel();
		JSplitPane topSplit = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				componenteDeArriba, componenteDeAbajo);
		
		add(topSplit,BorderLayout.CENTER);
		add(bottomSplit, BorderLayout.SOUTH);
		
		setVisible(true);
		bottomSplit.setDividerLocation(.5);
		
	}
	
	private void addBars() {
		// instantiate actions
		SimulatorAction cargarEventos = new SimulatorAction(
				"Cargar Eventos", "open.png", "Cargar eventos de un fichero",
				KeyEvent.VK_L, "control L", 
				()-> System.exit(0));
		SimulatorAction guardarEventos = new SimulatorAction(
				"Guardar Eventos", "save.png", "Guardar eventos en un fichero",
				KeyEvent.VK_S, "control S", 
				()-> System.err.println("guardando..."));
		SimulatorAction limpiarEventos = new SimulatorAction(
				"Limpiar Eventos", "clear.png", "Limpiar la lista de eventos",
				KeyEvent.VK_C, "control C", 
				()-> System.exit(0));
		SimulatorAction insertarEventos = new SimulatorAction(
				"Insertar Eventos", "events.png", "Insertar eventos en el simulador",
				KeyEvent.VK_I, "control I", 
				()-> System.err.println("guardando..."));
		SimulatorAction ejecutar = new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar el simulador",
				KeyEvent.VK_E, "control E", 
				()-> System.exit(0));
		SimulatorAction reiniciar = new SimulatorAction(
				"Reiniciar", "reset.png", "Reiniciar el simulador",
				KeyEvent.VK_R, "control R", 
				()-> System.err.println("guardando..."));
		SimulatorAction generarInformes = new SimulatorAction(
				"Generar", "report.png", "Generar informes",
				KeyEvent.VK_P, "control P", 
				()-> System.err.println("guardando..."));
		SimulatorAction limpiarInformes = new SimulatorAction(
				"Limpiar", "delete_report.png", "Limpiar informes",
				KeyEvent.VK_L, "control L", 
				()-> System.err.println("guardando..."));
		SimulatorAction guardarInformes = new SimulatorAction(
				"Guardar", "save_report.png", "Guardar informes",
				KeyEvent.VK_A, "control A", 
				()-> System.err.println("guardando..."));
		SimulatorAction salir = new SimulatorAction(
				"Salir", "exit.png", "Salir de la aplicacion",
				KeyEvent.VK_A, "control shift X", 
				()-> System.exit(0));
		
		JSpinner stepsSpinner = new JSpinner();
		JTextField timeViewer = new JTextField();
		
		// add actions to toolbar, and bar to window
		JToolBar bar = new JToolBar();
		bar.add(cargarEventos);
		bar.add(guardarEventos);
		bar.add(limpiarEventos);
		bar.add(insertarEventos);
		bar.add(ejecutar);
		bar.add(reiniciar);
		bar.add(stepsSpinner);
		bar.add(timeViewer);
		bar.add(generarInformes);
		bar.add(limpiarInformes);
		bar.add(guardarInformes);
		bar.add(salir);
		add(bar, BorderLayout.NORTH);

		// add actions to menubar, and bar to window
		JMenu file = new JMenu("Archivo");
		file.add(cargarEventos);		
		file.add(guardarEventos);
		file.addSeparator();
		file.add(guardarInformes);
		file.addSeparator();
		file.add(salir);	
		
		JMenu simulator = new JMenu("Simulador");
		simulator.add(ejecutar);
		simulator.add(reiniciar);
		//simulator.add(menuItem);	Redirect Output...
		
		JMenu reports = new JMenu("Informes");
		reports.add(generarInformes);
		reports.add(limpiarInformes);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(simulator);
		menu.add(reports);
		setJMenuBar(menu);
	}
	
	public static void main(String ... args) {
		SwingUtilities.invokeLater(() -> new MainWindow());
	}
}
