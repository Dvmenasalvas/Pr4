package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.event.Event;
import es.ucm.fdi.model.event.MakeVehicleFaulty;
import es.ucm.fdi.view.SimulatorTablePanel.Describable;

public class MainWindow extends JFrame implements SimulatorListener {
	private Controller controller;
	
	private JSplitPane mainPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JPanel bottomLeftPanel;
	
	private TextPanel eventsEditor;
	private SimulatorTablePanel eventsTable;
	private TextPanel reportsAreaPanel;
	private SimulatorTablePanel vehiclesTable; // tabla de vehiculos
	private SimulatorTablePanel roadsTable; // tabla de carreteras
	private SimulatorTablePanel junctionsTable; // tabla de cruces
	private RoadMapPanel map;
	
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenu simulatorMenu;
	private JMenu reportsMenu;
	private HashMap<Command, SimulatorAction> actions;
	
	private JSpinner stepsSpinner;
	private JTextField timeViewer;
	private JToolBar toolBar;
	private JTextArea reportsArea;
	
	private File currentFile;
	

	
	//private ReportDialog reportDialog; // opcional
	
	public MainWindow(Controller controller, String inFileName, int timeLimit) {
			 super("Traffic Simulator");
			 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 
			 currentFile = inFileName != null ? new File(inFileName) : null;
			 //ctrl.setOutputStream(reportsOutputStream); // ver sección 8
			 
			this.controller = controller;
			initGUI(timeLimit);
			controller.getSimulator().addSimulatorListener(this);
			 
			setSize(1250, 1000);
			setLocationRelativeTo(null);
			setVisible(true);
			mainPanel.setDividerLocation(.5);
	}
	
	private void initGUI(int timeLimit) {
		//Split de ventanas
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel,
				BoxLayout.X_AXIS));
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel,
				BoxLayout.X_AXIS));
		
		bottomLeftPanel = new JPanel();
		bottomLeftPanel.setLayout(new BoxLayout(bottomLeftPanel,
				BoxLayout.Y_AXIS));

		bottomPanel.add(bottomLeftPanel);
		
		mainPanel = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				topPanel, bottomPanel);
		add(mainPanel);
		
		
		addCommands();
		addToolbar(timeLimit);
		addMenu();
		addEventsEditor();
		addEventsView();
		addReportsArea();
		addVehiclesTable();
		addRoadsTable(); 
		addJunctionsTable(); 
		addMap();
		/*
		addStatusBar(); // barra de estado
		*/
		
	}

	private void addCommands() {
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
				()-> {
					try {
						insertEvents();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}));
		actions.put(Command.Execute, new SimulatorAction(
				"Ejecutar", "play.png", "Ejecutar el simulador",
				KeyEvent.VK_E, "control E", 
				()-> ejecuta()));
		actions.put(Command.Reset, new SimulatorAction(
				"Reiniciar", "reset.png", "Reiniciar el simulador",
				KeyEvent.VK_R, "control R", 
				()-> controller.getSimulator().reset()));
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
		
	}
	
	private void ejecuta() {
		try {
			controller.getSimulator().ejecuta((Integer) stepsSpinner.getValue(),  new FileOutputStream("src/salida.ini"));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void insertEvents() throws IOException  {
		Ini ini = eventsEditor.getEvents();
		for(IniSection sec : ini.getSections()) {
			Event e;
			try {
				e = Controller.parseSec(sec);
				controller.getSimulator().insertaEvento(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

	private void addToolbar(int timeLimit) {
		// instantiate actions
		stepsSpinner = new JSpinner(new SpinnerNumberModel(timeLimit, 1, 100, 1));
		timeViewer = new JTextField();
		timeViewer.setText("0");
		timeViewer.setEditable(false);
		timeViewer.setPreferredSize(new Dimension(50, 60));
		
		// add actions to toolbar, and bar to window
		toolBar = new JToolBar();
		toolBar.add(actions.get(Command.LoadEvents));
		toolBar.add(actions.get(Command.SaveEvents));
		toolBar.add(actions.get(Command.CleanEvents));
		toolBar.addSeparator();
		
		toolBar.add(actions.get(Command.InsertEvents));
		toolBar.add(actions.get(Command.Execute));
		toolBar.add(actions.get(Command.Reset));
		
		toolBar.add(new JLabel("Pasos: "));
		toolBar.add(stepsSpinner);
		stepsSpinner.setToolTipText(
	                "<html>Determina el numero de pasos que se <br>"
	                + "dan en la simulacion");
	
		toolBar.add(new JLabel("Tiempo: "));
		toolBar.add(timeViewer);
		toolBar.addSeparator();
		
		toolBar.add(actions.get(Command.GenerateReports));
		toolBar.add(actions.get(Command.CleanReports));
		toolBar.add(actions.get(Command.SaveReports));
		toolBar.addSeparator();
		
		toolBar.add(actions.get(Command.Exit));
		
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
		
		add(toolBar, BorderLayout.NORTH);
	}
	
	private void addMenu() {
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
		eventsEditor = new TextPanel(actions, true);
		topPanel.add(new JScrollPane(eventsEditor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		if(currentFile != null) {
			 eventsEditor.setText(TextPanel.readFile(currentFile));
		 }
	}

	private void addEventsView() {
		String[] columnas = {"#" , "Tiempo" , "Tipo"};
		List<Describable> events = new ArrayList<Describable>();
		eventsTable = new SimulatorTablePanel(events, columnas);
		
		TitledBorder controlBorder = new TitledBorder("Editor de eventos");
	    eventsTable.setBorder(controlBorder);
		
		topPanel.add(new JScrollPane(eventsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}
	
	private void addReportsArea() {
		reportsAreaPanel = new TextPanel(actions, false);
		
		TitledBorder border = new TitledBorder("Informe");
	    reportsAreaPanel.setBorder(border);
	    
		topPanel.add(new JScrollPane(reportsAreaPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
	}
	
	private void addVehiclesTable() {
		String[] columnas = {"ID", "Carretera", "Localizacion", "Velocidad", "Km", "Unidades de averia", "Itinerario"};
		List<Describable> events = new ArrayList<Describable>();
		vehiclesTable = new SimulatorTablePanel(events, columnas);
		
		TitledBorder controlBorder = new TitledBorder("Vehiculos");
	    vehiclesTable.setBorder(controlBorder);
		
		bottomLeftPanel.add(new JScrollPane(vehiclesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
	}

	private void addRoadsTable() {
		String[] columnas = {"ID", "Inicio", "Final", "Longitud", "Maxima Velocidad", "Vehiculos"};
		List<Describable> events = new ArrayList<Describable>();
		roadsTable = new SimulatorTablePanel(events, columnas);
		
		TitledBorder controlBorder = new TitledBorder("Carreteras");
	    roadsTable.setBorder(controlBorder);
		
		bottomLeftPanel.add(new JScrollPane(roadsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);	
	}
	
	private void addJunctionsTable() {
		String[] columnas = {"ID", "Verde", "Rojo"};
		List<Describable> events = new ArrayList<Describable>();
		junctionsTable = new SimulatorTablePanel(events, columnas);
		
		TitledBorder controlBorder = new TitledBorder("Cruces");
	    junctionsTable.setBorder(controlBorder);
		
		bottomLeftPanel.add(new JScrollPane(junctionsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
	}
	
	private void addMap() {
		RoadMap rm = new RoadMap();
        map = new RoadMapPanel(rm);
		
		TitledBorder border = new TitledBorder("RoadMap");
	    map.setBorder(border);
	    
		topPanel.add(new JScrollPane(reportsAreaPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}
	
	private void refreshInfo(UpdateEvent ue) {
		eventsTable.setElements(ue.getEventsQueue());
		vehiclesTable.setElements(ue.getRoadMap().getVehicles());
		roadsTable.setElements(ue.getRoadMap().getRoads());
		junctionsTable.setElements(ue.getRoadMap().getJunctions());
		timeViewer.setText(Integer.toString(ue.getCurrentTime()));
	}
	
	@Override
	public void registered(UpdateEvent ue) {
		refreshInfo(ue);
	}

	@Override
	public void reset(UpdateEvent ue) {
		refreshInfo(ue);
	}

	
	@Override
	public void newEvent(UpdateEvent ue) {
		eventsTable.setElements(ue.getEventsQueue());
	}

	@Override
	public void advanced(UpdateEvent ue) {
		refreshInfo(ue);
	}

	@Override
	public void error(UpdateEvent ue, String error) {
		// TODO Auto-generated method stub
		
	}
}
