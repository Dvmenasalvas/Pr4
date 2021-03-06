package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.simulatedobject.SimObject;
import es.ucm.fdi.view.SimulatorTablePanel.Describable;

/**
 * MainWindow es la ventana principal, consta de varias tablas, zonas de texto, un grafo y una barra
 * de herramientas. Su misión principal es escuchar los eventos que le lanza el simulador y
 * refrescar cada ventana de acuerdo con el evento que ha sucedido, para ello en ocasiones tiene que
 * pedirle al simulador una cola de eventos o el roadmap ya que algunas ventanas necesitan estos
 * parámetros para actualizar las tablas o los grafos
 */

public class MainWindow extends JFrame
		implements SimulatorListener, ItemListener {
	private Controller controller;
	private OutputStream out = null;
	private CustomizedOut customizedOut;

	private JSplitPane mainPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JPanel bottomLeftPanel;

	private ToolBar toolBar;
	private ReportWindow reportsSelector;
	private TextPanel eventsEditor;
	private SimulatorTablePanel eventsTable;
	private TextPanel reportsAreaPanel;

	private SimulatorTablePanel vehiclesTable;
	private SimulatorTablePanel roadsTable;
	private SimulatorTablePanel junctionsTable;
	private RoadMapPanel map;
	private StatusBar statusBar;

	private JMenuBar menu;
	private JCheckBoxMenuItem autoReports;
	private JMenu fileMenu;
	private JMenu simulatorMenu;
	private JMenu reportsMenu;
	private HashMap<Command, SimulatorAction> actions;
	
	private Stepper stepper;

	public MainWindow(Controller controller, String inFileName, int timeLimit) {
		super("Traffic Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.controller = controller;
		initGUI(timeLimit, inFileName);
		controller.addSimulatorListener(this);
		
		stepper = new Stepper(() -> changeAll(false), 
				() -> controller.ejecuta(1, out),
				() -> changeAll(true));

		setSize(1250, 1050);
		setLocationRelativeTo(null);
		setVisible(true);
		mainPanel.setDividerLocation(.45);
	}

	private void initGUI(int timeLimit, String inFileName) {
		// Windows split
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		bottomLeftPanel = new JPanel();
		bottomLeftPanel
				.setLayout(new BoxLayout(bottomLeftPanel, BoxLayout.Y_AXIS));

		bottomPanel.add(bottomLeftPanel);

		mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel,
				bottomPanel);
		add(mainPanel);

		// Inicialice commands
		addCommands();

		// ToolBar
		toolBar = new ToolBar(actions, timeLimit);
		add(toolBar, BorderLayout.NORTH);
		reportsSelector = new ReportWindow(this);

		// Menu
		addMenu();

		// EventsEditor
		try {
			eventsEditor = new TextPanel(actions, "Editor de eventos", true);
		} catch (IOException e1) {
			showException(e1, "No se ha podido leer correctamente el archivos de templates.",
					"Error de lectura");
		}
		addTextArea(eventsEditor,inFileName, topPanel);

		// Events table
		List<Describable> events = new ArrayList<Describable>();
		String[] cEvents = { "#", "Tiempo", "Tipo" };
		eventsTable = new SimulatorTablePanel(events, cEvents, "Eventos");
		addTable(eventsTable, topPanel);

		// ReportArea
		try {
			reportsAreaPanel = new TextPanel(actions,"Informes", false);
		} catch (IOException e) {
			// Esta excepcion nunca se lanza
		}
		addTextArea(reportsAreaPanel, null, topPanel);
		customizedOut = new CustomizedOut(reportsAreaPanel);

		// Junctions table
		String[] cJunctions = { "ID", "Verde", "Rojo" };
		junctionsTable = new SimulatorTablePanel(events, cJunctions, "Cruzes");
		addTable(junctionsTable, bottomLeftPanel);

		// Vehicles table
		String[] cVehicles = { "ID", "Carretera", "Localizacion", "Velocidad",
				"Km", "Unidades de averia", "Itinerario" };
		vehiclesTable = new SimulatorTablePanel(events, cVehicles,"Vehiculos");
		addTable(vehiclesTable, bottomLeftPanel);

		// Roads table
		String[] cRoads = { "ID", "Inicio", "Final", "Longitud",
				"Maxima Velocidad", "Vehiculos" };
		roadsTable = new SimulatorTablePanel(events, cRoads, "Carreteras");
		addTable(roadsTable, bottomLeftPanel);

		addMap();
		addStatusBar();
	}

	private void addCommands() {
		actions = new HashMap<Command, SimulatorAction>();

		// Events related actions
		actions.put(Command.LoadEvents,
				new SimulatorAction("Cargar Eventos", "open.png",
						"Cargar eventos de un fichero", KeyEvent.VK_L,
						"control L", () -> {
							try {
								eventsEditor.loadEvents();
							} catch (FileNotFoundException e3) {
								showException(e3,"", "Error de lectura");
							}
						}));
		actions.put(Command.SaveEvents,
				new SimulatorAction("Guardar Eventos", "save.png",
						"Guardar eventos en un fichero", KeyEvent.VK_S,
						"control S", () -> {
							try {
								eventsEditor.saveEvents();
							} catch (FileNotFoundException e2) {
								showException(e2,"","Error de escritura");
							}
						}));
		actions.put(Command.CleanEvents,
				new SimulatorAction("Limpiar Eventos", "clear.png",
						"Limpiar la lista de eventos", KeyEvent.VK_C,
						"control C", () -> eventsEditor.clearEvents()));
		actions.put(Command.InsertEvents,
				new SimulatorAction("Insertar Eventos", "events.png",
						"Insertar eventos en el simulador", KeyEvent.VK_I,
						"control I", () -> {
							try {
								controller.insertarEventos(
										eventsEditor.getText());
							} catch (IOException e1) {
								showException(e1, "","Error de formato de evento");
							}
						}));

		// Simulator related actions
		actions.put(Command.Execute, new SimulatorAction("Ejecutar", "play.png",
				"Ejecutar el simulador", KeyEvent.VK_E, "control E",
				() -> stepper.start(toolBar.getSteps(), toolBar.getDelay())));
		actions.put(Command.Reset,
				new SimulatorAction("Reiniciar", "reset.png",
						"Reiniciar el simulador", KeyEvent.VK_R, "control R",
						() -> controller.reset()));
		
		actions.put(Command.Stop,
				new SimulatorAction("Parar", "stop.png",
						"Parar la simulación", KeyEvent.VK_D,
						"control shift D", () -> stepper.stop()));

		// Reports related actions
		actions.put(Command.GenerateReports, new SimulatorAction("Generar",
				"report.png", "Generar informes", KeyEvent.VK_P, "control P",
				() -> generateReports()));
		actions.put(Command.CleanReports,
				new SimulatorAction("Limpiar", "delete_report.png",
						"Limpiar informes", KeyEvent.VK_L, "control L",
						() -> reportsAreaPanel.clearEvents()));
		actions.put(Command.SaveReports,
				new SimulatorAction("Guardar", "save_report.png",
						"Guardar informes", KeyEvent.VK_A, "control A", () -> {
							try {
								reportsAreaPanel.saveEvents();
							} catch (FileNotFoundException e) {
								showException(e, null, "Error de escritura");
							}
						}));

		// Exit
		actions.put(Command.Exit,
				new SimulatorAction("Salir", "exit.png",
						"Salir de la aplicacion", KeyEvent.VK_X,
						"control shift X", () -> System.exit(0)));
	}

	private void generateReports() {
		int status = reportsSelector.open();
		if (status != 0) {
		    List<SimObject> so = new ArrayList<SimObject>();
            for(String v : reportsSelector.getSelectedVehicles()) {
            	so.add(controller.getTrafficSimulator().getRoadMap().getSimObject(v));
            }
            for(String r : reportsSelector.getSelectedRoads()) {
            	so.add(controller.getTrafficSimulator().getRoadMap().getSimObject(r));
            }
            for(String j : reportsSelector.getSelectedJunctions()) {
            	so.add(controller.getTrafficSimulator().getRoadMap().getSimObject(j));
            }
           
            reportsAreaPanel.setText(controller.getTrafficSimulator().report(so).toString());
		}
	}
	
	private void changeAll(boolean state) {
		actions.get(Command.Stop).setEnabled(!state);
		
		actions.get(Command.LoadEvents).setEnabled(state);
		actions.get(Command.InsertEvents).setEnabled(state);
		actions.get(Command.SaveEvents).setEnabled(state);
		actions.get(Command.CleanEvents).setEnabled(state);
		actions.get(Command.InsertEvents).setEnabled(state);
		actions.get(Command.Execute).setEnabled(state);
		actions.get(Command.Reset).setEnabled(state);
		actions.get(Command.GenerateReports).setEnabled(state);
		actions.get(Command.CleanReports).setEnabled(state);
		actions.get(Command.SaveReports).setEnabled(state);
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
		simulatorMenu.add(actions.get(Command.Stop));
		simulatorMenu.add(actions.get(Command.Reset));

		reportsMenu = new JMenu("Informes");
		reportsMenu.add(actions.get(Command.GenerateReports));
		reportsMenu.add(actions.get(Command.CleanReports));
		autoReports = new JCheckBoxMenuItem("Informes automaticos");
		autoReports.addItemListener(this);
		reportsMenu.add(autoReports);

		menu = new JMenuBar();
		menu.add(fileMenu);
		menu.add(simulatorMenu);
		menu.add(reportsMenu);
		setJMenuBar(menu);
	}

	
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == autoReports) {
			if (e.getStateChange() == 1) {
				out = customizedOut;
			} else {
				out = null;
			}
		}
	}

	private void addTextArea(TextPanel textArea,
			String inFileName, JPanel container) {
		container.add(textArea);

		if (inFileName != null) {
			try {
				textArea.setText(TextPanel.readFile(new File(inFileName)));
			} catch (FileNotFoundException e) {
				showException(e, "No se ha podido encontrar el archivo: " + inFileName,
							"Error de lectura");
			}
		}
	}

	private void addTable(SimulatorTablePanel sPanel,
			JPanel container) {
		container.add(sPanel);
	}

	private void addMap() {
		RoadMap rm = new RoadMap();
		map = new RoadMapPanel(rm);

		bottomPanel.add(map);
	}

	private void addStatusBar() {
		statusBar = new StatusBar();
		statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
		add(statusBar, BorderLayout.SOUTH);
	}
	
	private void showException(Exception e, String msg, String title) {
		String msgF = msg == null ? "" : msg;
		msgF += '\n' + exceptionString(e);
		JOptionPane.showMessageDialog(this,
				msgF, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	private String exceptionString(Exception e) {
		StringBuilder out = new StringBuilder();
		while(e != null) {
			out.append(e.getMessage() + '\n');
			e = (Exception) e.getCause();
		}
		return out.toString();
	}

	private void refreshInfo(UpdateEvent ue) {
		map.setRoadMap(ue.getRoadMap());
		//reportWindow.setRoadMap(ue.getRoadMap());
		map.generateGraph();
		eventsTable.setElements(ue.getEventsQueue());
		vehiclesTable.setElements(ue.getRoadMap().getVehicles());
		roadsTable.setElements(ue.getRoadMap().getRoads());
		junctionsTable.setElements(ue.getRoadMap().getJunctions());
		toolBar.setTimeValue(ue.getCurrentTime());
		statusBar.setStatusBar(ue.getEvent());
		reportsSelector.setData(ue.getRoadMap().getVehicles(), ue.getRoadMap().getRoads(), ue.getRoadMap().getJunctions());
	}

	private void reset() {
		actions.get(Command.Execute).setEnabled(false);
		actions.get(Command.Reset).setEnabled(false);
		actions.get(Command.GenerateReports).setEnabled(false);
		actions.get(Command.CleanReports).setEnabled(false);
		actions.get(Command.SaveReports).setEnabled(false);
		actions.get(Command.Stop).setEnabled(false);
	}
	@Override
	public void registered(UpdateEvent ue) {
		refreshInfo(ue);
		reset();
	}

	@Override
	public void reset(UpdateEvent ue) {
		refreshInfo(ue);
		reset();
	}

	@Override
	public void newEvent(UpdateEvent ue) {
		eventsTable.setElements(ue.getEventsQueue());
		actions.get(Command.Execute).setEnabled(true);
		actions.get(Command.Reset).setEnabled(true);
	}

	@Override
	public void advanced(UpdateEvent ue) {
		refreshInfo(ue);
	}

	@Override
	public void error(UpdateEvent ue, String error) {
		JOptionPane.showMessageDialog(this, error, "Error",
				JOptionPane.ERROR_MESSAGE);
	}
}