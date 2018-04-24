package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.view.SimulatorTablePanel.Describable;

public class MainWindow extends JFrame implements SimulatorListener {
	private Controller controller;

	private JSplitPane mainPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JPanel bottomLeftPanel;

	private ToolBar toolBar;
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

	// private ReportDialog reportDialog; // opcional

	public MainWindow(Controller controller, String inFileName, int timeLimit) {
		super("Traffic Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ctrl.setOutputStream(reportsOutputStream); // ver sección 8

		this.controller = controller;
		initGUI(timeLimit, inFileName);
		controller.addSimulatorListener(this);

		setSize(1250, 1000);
		setLocationRelativeTo(null);
		setVisible(true);
		mainPanel.setDividerLocation(.5);
	}

	private void initGUI(int timeLimit, String inFileName) {
		// Split de ventanas
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		bottomLeftPanel = new JPanel();
		bottomLeftPanel.setLayout(new BoxLayout(bottomLeftPanel, BoxLayout.Y_AXIS));

		bottomPanel.add(bottomLeftPanel);

		mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
		add(mainPanel);

		addCommands();

		toolBar = new ToolBar(actions, timeLimit);
		add(toolBar, BorderLayout.NORTH);

		addMenu();
		addEventsEditor(inFileName);
		addEventsView();
		addReportsArea();
		addVehiclesTable();
		addRoadsTable();
		addJunctionsTable();
		addMap();
		addStatusBar();
	}

	private void addCommands() {
		actions = new HashMap<Command, SimulatorAction>();

		actions.put(Command.LoadEvents,
				new SimulatorAction("Cargar Eventos", "open.png", "Cargar eventos de un fichero",
						KeyEvent.VK_L, "control L", () -> eventsEditor.loadEvents()));
		actions.put(Command.SaveEvents,
				new SimulatorAction("Guardar Eventos", "save.png", "Guardar eventos en un fichero",
						KeyEvent.VK_S, "control S", () -> eventsEditor.saveEvents()));
		actions.put(Command.CleanEvents,
				new SimulatorAction("Limpiar Eventos", "clear.png", "Limpiar la lista de eventos",
						KeyEvent.VK_C, "control C", () -> eventsEditor.clearEvents()));
		actions.put(Command.InsertEvents,
				new SimulatorAction("Insertar Eventos", "events.png",
						"Insertar eventos en el simulador", KeyEvent.VK_I, "control I",
						() -> controller.insertarEventos(eventsEditor.getText())));
		actions.put(Command.Execute,
				new SimulatorAction("Ejecutar", "play.png", "Ejecutar el simulador", KeyEvent.VK_E,
						"control E", () -> controller.ejecuta(toolBar.getSpinnerValue(), null)));
		actions.put(Command.Reset, new SimulatorAction("Reiniciar", "reset.png",
				"Reiniciar el simulador", KeyEvent.VK_R, "control R", () -> controller.reset()));
		actions.put(Command.GenerateReports,
				new SimulatorAction("Generar", "report.png", "Generar informes", KeyEvent.VK_P,
						"control P", () -> reportsAreaPanel.setText(controller.generateReports())));
		actions.put(Command.CleanReports,
				new SimulatorAction("Limpiar", "delete_report.png", "Limpiar informes",
						KeyEvent.VK_L, "control L", () -> reportsAreaPanel.clearEvents()));
		actions.put(Command.SaveReports,
				new SimulatorAction("Guardar", "save_report.png", "Guardar informes", KeyEvent.VK_A,
						"control A", () -> reportsAreaPanel.saveEvents()));
		actions.put(Command.Exit, new SimulatorAction("Salir", "exit.png", "Salir de la aplicacion",
				KeyEvent.VK_A, "control shift X", () -> System.exit(0)));
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

		reportsMenu = new JMenu("Informes");
		reportsMenu.add(actions.get(Command.GenerateReports));
		reportsMenu.add(actions.get(Command.CleanReports));

		menu = new JMenuBar();
		menu.add(fileMenu);
		menu.add(simulatorMenu);
		menu.add(reportsMenu);
		setJMenuBar(menu);
	}

	private void addEventsEditor(String inFileName) {
		eventsEditor = new TextPanel(actions, true);

		eventsEditor.setBorder(new TitledBorder("Informe"));

		topPanel.add(new JScrollPane(eventsEditor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

		if (inFileName != null) {
			eventsEditor.setText(TextPanel.readFile(new File(inFileName)));
		}
	}

	private void addEventsView() {
		String[] columnas = { "#", "Tiempo", "Tipo" };
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
		String[] columnas = { "ID", "Carretera", "Localizacion", "Velocidad", "Km",
				"Unidades de averia", "Itinerario" };
		List<Describable> events = new ArrayList<Describable>();
		vehiclesTable = new SimulatorTablePanel(events, columnas);

		TitledBorder controlBorder = new TitledBorder("Vehiculos");
		vehiclesTable.setBorder(controlBorder);

		bottomLeftPanel.add(new JScrollPane(vehiclesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

	}

	private void addRoadsTable() {
		String[] columnas = { "ID", "Inicio", "Final", "Longitud", "Maxima Velocidad",
				"Vehiculos" };
		List<Describable> events = new ArrayList<Describable>();
		roadsTable = new SimulatorTablePanel(events, columnas);

		TitledBorder controlBorder = new TitledBorder("Carreteras");
		roadsTable.setBorder(controlBorder);

		bottomLeftPanel.add(new JScrollPane(roadsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}

	private void addJunctionsTable() {
		String[] columnas = { "ID", "Verde", "Rojo" };
		List<Describable> events = new ArrayList<Describable>();
		junctionsTable = new SimulatorTablePanel(events, columnas);

		TitledBorder controlBorder = new TitledBorder("Cruces");
		junctionsTable.setBorder(controlBorder);

		bottomLeftPanel
				.add(new JScrollPane(junctionsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

	}

	private void addMap() {
		RoadMap rm = new RoadMap();
		map = new RoadMapPanel(rm);

		map.setBorder(new TitledBorder("RoadMap"));

		bottomPanel.add(new JScrollPane(map, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
	}
	
	private void addStatusBar() {
		 JPanel statusBar = new JPanel();
		 statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
		 JLabel statusBarText = new JLabel("Welcome to the simulator!");
		 statusBar.add(statusBarText);
		 add(statusBar,BorderLayout.SOUTH);
		}

	private void refreshInfo(UpdateEvent ue) {
		map.setRoadMap(ue.getRoadMap());
		map.generateGraph();
		eventsTable.setElements(ue.getEventsQueue());
		vehiclesTable.setElements(ue.getRoadMap().getVehicles());
		roadsTable.setElements(ue.getRoadMap().getRoads());
		junctionsTable.setElements(ue.getRoadMap().getJunctions());
		toolBar.setTimeValue(ue.getCurrentTime());
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
