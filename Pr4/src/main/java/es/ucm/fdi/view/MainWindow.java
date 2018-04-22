package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimWindow;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.event.Event;

public class MainWindow extends JFrame implements SimulatorListener {
	private Controller ctrl; // la vista usa el controlador
	//private RoadMap map; // para los métodos update de Observer
	//private int time; // para los métodos update de Observer
	//private List<Event> events; // para los métodos update de Observer
	private OutputStream reportsOutputStream;
	
	private JSplitPane mainPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	
	private EventsEditorPanel eventsEditor;
	private SimulatorTablePanel eventsTable;
	private JPanel reportsAreaPanel;
	
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu simulatorMenu;
	private JMenu reportsMenu;
	private HashMap<Command, SimulatorAction> actions;
	
	private JSpinner stepsSpinner;
	private JTextField timeViewer;
	private JToolBar toolBar;
	
	private File currentFile;

	private JTextArea reportsArea; // zona de informes
	private JTable vehiclesTable; // tabla de vehiculos
	private JTable roadsTable; // tabla de carreteras
	private JTable junctionsTable; // tabla de cruces
	//private ReportDialog reportDialog; // opcional
	
	public MainWindow() {
		super("Simulador de trafico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initGUI();
		
		setSize(1000, 1000);	
		setVisible(true);
		mainPanel.setDividerLocation(.5);
	}
	
	public MainWindow(TrafficSimulator model, String inFileName, Controller ctrl) {
			 super("Traffic Simulator");
			 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 
			 this.ctrl = ctrl;
			 currentFile = inFileName != null ? new File(inFileName) : null;
			 //reportsOutputStream = new JTextAreaOutputStream(reportsArea,null);
			 //ctrl.setOutputStream(reportsOutputStream); // ver sección 8
			 
			 initGUI();
			 model.addSimulatorListener(this);
			 
			setSize(1000, 1000);	
			setVisible(true);
			mainPanel.setDividerLocation(.5);
			}

	@Override
	public void update(UpdateEvent ue, String error) {
		// TODO Auto-generated method stub

	}
	
	private void initGUI() {
		//Split de ventanas
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel,
				BoxLayout.X_AXIS));
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel,
				BoxLayout.X_AXIS));
		
		mainPanel = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				topPanel, bottomPanel);
		add(mainPanel);
		
		
		 
		 addBars(); // barras de menu y herramientas
		 addEventsEditor(); // editor de eventos
		 addEventsView(); // cola de eventos
		 addReportsArea(); // zona de informes
		 /*
		 addVehiclesTable(); // tabla de vehiculos
		 addRoadsTable(); // tabla de carreteras
		 addJunctionsTable(); // tabla de cruces
		 addMap(); // mapa de carreteras
		 addStatusBar(); // barra de estado
		  */
		
	}

	private void addBars() {
		// instantiate actions
		actions = new HashMap<Command, SimulatorAction>();
		
		actions.put(Command.LoadEvents ,new SimulatorAction(
				"Cargar Eventos", "open.png", "Cargar eventos de un fichero",
				KeyEvent.VK_L, "control L", 
				()-> eventsEditor.loadEvents()
				));
		actions.put(Command.SaveEvents, new SimulatorAction(
				"Guardar Eventos", "save.png", "Guardar eventos en un fichero",
				KeyEvent.VK_S, "control S", 
				()-> eventsEditor.saveEvents()
				));
		actions.put(Command.CleanEvents, new SimulatorAction(
				"Limpiar Eventos", "clear.png", "Limpiar la lista de eventos",
				KeyEvent.VK_C, "control C", 
				()-> eventsEditor.clearEvents()));
		actions.put(Command.InsertEvents, new SimulatorAction(
				"Insertar Eventos", "events.png", "Insertar eventos en el simulador",
				KeyEvent.VK_I, "control I", 
				()-> System.err.println("guardando...")));
		actions.put(Command.Execute, new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar el simulador",
				KeyEvent.VK_E, "control E", 
				()-> System.exit(0)));
		actions.put(Command.Reset, new SimulatorAction(
				"Reiniciar", "reset.png", "Reiniciar el simulador",
				KeyEvent.VK_R, "control R", 
				()-> System.err.println("guardando...")));
		actions.put(Command.GenerateReports, new SimulatorAction(
				"Generar", "report.png", "Generar informes",
				KeyEvent.VK_P, "control P", 
				()-> System.err.println("guardando...")));
		actions.put(Command.CleanReports, new SimulatorAction(
				"Limpiar", "delete_report.png", "Limpiar informes",
				KeyEvent.VK_L, "control L", 
				()-> System.err.println("guardando...")));
		actions.put(Command.CleanReports, new SimulatorAction(
				"Guardar", "save_report.png", "Guardar informes",
				KeyEvent.VK_A, "control A", 
				()-> System.err.println("guardando...")));
		actions.put(Command.Exit, new SimulatorAction(
				"Salir", "exit.png", "Salir de la aplicacion",
				KeyEvent.VK_A, "control shift X", 
				()-> System.exit(0)));
		
		stepsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		timeViewer = new JTextField();
		timeViewer.setPreferredSize(new Dimension(50, 60));
		
		
		// add actions to toolbar, and bar to window
		toolBar = new JToolBar();
		toolBar.add(actions.get(Command.LoadEvents));
		toolBar.add(actions.get(Command.SaveEvents));
		toolBar.add(actions.get(Command.CleanEvents));
		toolBar.add(actions.get(Command.InsertEvents));
		toolBar.add(actions.get(Command.Execute));
		toolBar.add(actions.get(Command.Exit));
		
		toolBar.add(new JLabel("Pasos: "));
		toolBar.add(stepsSpinner);
		stepsSpinner.setToolTipText(
	                "<html>Determina el numero de pasos que se <br>"
	                + "dan en la simulacion");
	
		toolBar.add(new JLabel("Tiempo: "));
		toolBar.add(timeViewer);
		
		toolBar.add(actions.get(Command.GenerateReports));
		toolBar.add(actions.get(Command.CleanReports));
		toolBar.add(actions.get(Command.SaveReports));
		toolBar.add(actions.get(Command.Exit));
		
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
		
		add(toolBar, BorderLayout.NORTH);

		// add actions to menubar, and bar to window
		fileMenu = new JMenu("Archivo");
		fileMenu.add(actions.get(Command.LoadEvents));		
		fileMenu.add(actions.get(Command.SaveEvents));
		fileMenu.addSeparator();
		fileMenu.add(actions.get(Command.SaveReports));
		fileMenu.addSeparator();
		fileMenu.add(actions.get(Command.Exit));	
		
		simulatorMenu = new JMenu("Simulador");
		simulatorMenu.add(actions.get(Command.Execute));
		simulatorMenu.add(actions.get(Command.Reset));
		//simulator.add(menuItem);	Redirect Output...
		
		reportsMenu = new JMenu("Informes");
		reportsMenu.add(actions.get(Command.GenerateReports));
		reportsMenu.add(actions.get(Command.CleanReports));
		
		menu = new JMenuBar();
		menu.add(fileMenu);
		menu.add(simulatorMenu);
		menu.add(reportsMenu);
		setJMenuBar(menu);
	}
	
	private void addEventsEditor() {
		eventsEditor = new EventsEditorPanel(actions);
		topPanel.add(eventsEditor);
	}

	private void addEventsView() {
		eventsTable = new SimulatorTablePanel(null, null);
		topPanel.add(eventsTable);

	}
	
	private void addReportsArea() {
		reportsArea = new JTextArea();
		topPanel.add(reportsArea);
		TitledBorder controlBorder = new TitledBorder("Informe");
	    reportsArea.setBorder(controlBorder);
	}
	
	
	public static void main(String ... args) {
		SwingUtilities.invokeLater(() -> new MainWindow());
	}
}
