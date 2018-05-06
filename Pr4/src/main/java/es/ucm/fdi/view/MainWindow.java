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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.model.EventType;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.TrafficSimulator.SimulatorListener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;
import es.ucm.fdi.model.simulatedobject.Vehicle;
import es.ucm.fdi.view.SimulatorTablePanel.Describable;

/**
 * MainWindow es la ventana principal, consta de varias tablas, zonas de texto, 
 * un grafo y una barra de herramientas. Su misión principal es escuchar los eventos
 * que le lanza el simulador y refrescar cada ventana de acuerdo con el evento que ha
 * sucedido, para ello en ocasiones tiene que pedirle al simulador una cola de eventos
 * o el roadmap ya que algunas ventanas necesitan estos parámetros para actualizar las 
 * tablas o los grafos
 */

public class MainWindow extends JFrame implements SimulatorListener, ItemListener {
	private Controller controller;
	private OutputStream out = null;
	private CustomizedOut customizedOut;
	
	private JSplitPane mainPanel;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JPanel bottomLeftPanel;

	private ToolBar toolBar;
	private TextPanel eventsEditor;
	private SimulatorTablePanel eventsTable;
	private TextPanel reportsAreaPanel;
	
	private SimulatorTablePanel vehiclesTable; 
	private SimulatorTablePanel roadsTable; 
	private SimulatorTablePanel junctionsTable; 
	private RoadMapPanel map;
	private StatusBar statusBar;
	private ReportWindow reportWindow;

	private JMenuBar menu;
	private JCheckBoxMenuItem autoReports;
	private JMenu fileMenu;
	private JMenu simulatorMenu;
	private JMenu reportsMenu;
	private HashMap<Command, SimulatorAction> actions;
	

	// private ReportDialog reportDialog; // opcional

	public MainWindow(Controller controller, String inFileName, int timeLimit) {
		super("Traffic Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.controller = controller;
		initGUI(timeLimit, inFileName);
		controller.addSimulatorListener(this);

		setSize(1250, 1000);
		setLocationRelativeTo(null);
		setVisible(true);
		mainPanel.setDividerLocation(.5);
	}

	private void initGUI(int timeLimit, String inFileName) {
		//Windows split
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
		
		//Inicialice commands
		addCommands();

		//ToolBar
		toolBar = new ToolBar(actions, timeLimit);
		add(toolBar, BorderLayout.NORTH);

		//Menu
		addMenu();
		
		//EventsEditor
		eventsEditor = new TextPanel(actions, true);
		addTextArea(eventsEditor, "Editor de eventos", inFileName, topPanel);
		
		//Events table
		List<Describable> events = new ArrayList<Describable>();
		String[] cEvents = { "#", "Tiempo", "Tipo" };
		eventsTable = new SimulatorTablePanel(events, cEvents);
		addTable(eventsTable, "Editor de eventos", topPanel);
		
		//ReportArea
		reportsAreaPanel = new TextPanel(actions, true);
		addTextArea(reportsAreaPanel, "Informe", null, topPanel);
		customizedOut = new CustomizedOut(reportsAreaPanel);
		
		// Junctions table
		String[] cJunctions = { "ID", "Verde", "Rojo" };
		junctionsTable = new SimulatorTablePanel(events, cJunctions);
		addTable(junctionsTable, "Cruces", bottomLeftPanel);

		// Vehicles table
		String[] cVehicles = { "ID", "Carretera", "Localizacion", "Velocidad",
				"Km", "Unidades de averia", "Itinerario" };
		vehiclesTable = new SimulatorTablePanel(events, cVehicles);
		addTable(vehiclesTable, "Vehiculos", bottomLeftPanel);

		// Roads table
		String[] cRoads = { "ID", "Inicio", "Final", "Longitud",
				"Maxima Velocidad", "Vehiculos" };
		roadsTable = new SimulatorTablePanel(events, cRoads);
		addTable(roadsTable, "Carreteras", bottomLeftPanel);

		addMap();
		addStatusBar();
	}

	private void addCommands() {
		actions = new HashMap<Command, SimulatorAction>();

		//Events related actions
		actions.put(Command.LoadEvents,
				new SimulatorAction("Cargar Eventos", "open.png",
						"Cargar eventos de un fichero", KeyEvent.VK_L,
						"control L", () -> eventsEditor.loadEvents()));
		actions.put(Command.SaveEvents,
				new SimulatorAction("Guardar Eventos", "save.png",
						"Guardar eventos en un fichero", KeyEvent.VK_S,
						"control S", () -> {
							try {
								eventsEditor.saveEvents();
							} catch (FileNotFoundException e2) {
								JOptionPane.showMessageDialog(this, e2.getMessage(), "Error de escritura", JOptionPane.ERROR_MESSAGE);
							}
						}));
		actions.put(Command.CleanEvents,
				new SimulatorAction("Limpiar Eventos", "clear.png",
						"Limpiar la lista de eventos", KeyEvent.VK_C,
						"control C", () -> eventsEditor.clearEvents()));
		actions.put(Command.InsertEvents, new SimulatorAction(
				"Insertar Eventos", "events.png",
				"Insertar eventos en el simulador", KeyEvent.VK_I, "control I",
				() -> {
					try {
						controller.insertarEventos(eventsEditor.getText());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(this, e1.getMessage(), "Error de formato de evento", JOptionPane.ERROR_MESSAGE);
					}
				}));
		
		//Simulator related actions
		actions.put(Command.Execute, new SimulatorAction("Ejecutar", "play.png",
				"Ejecutar el simulador", KeyEvent.VK_E, "control E",
				() -> {
					try {
						controller.ejecuta(toolBar.getSpinnerValue(), out);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(this, e.getMessage(), "Error de escritura", JOptionPane.ERROR_MESSAGE);
					}
				}));
		actions.put(Command.Reset,
				new SimulatorAction("Reiniciar", "reset.png",
						"Reiniciar el simulador", KeyEvent.VK_R, "control R",
						() -> controller.reset()));
		
		//Reports related actions
		actions.put(Command.GenerateReports, new SimulatorAction("Generar",
				"report.png", "Generar informes", KeyEvent.VK_P, "control P",
				() -> initGUIReportPanel()));
		// reportsAreaPanel.setText(controller.generateReports())
		actions.put(Command.CleanReports,
				new SimulatorAction("Limpiar", "delete_report.png",
						"Limpiar informes", KeyEvent.VK_L, "control L",
						() -> reportsAreaPanel.clearEvents()));
		actions.put(Command.SaveReports,
				new SimulatorAction("Guardar", "save_report.png",
						"Guardar informes", KeyEvent.VK_A, "control A",
						() -> {
							try {
								reportsAreaPanel.saveEvents();
							} catch (FileNotFoundException e) {
								JOptionPane.showMessageDialog(this, e.getMessage(), "Error de escritura", JOptionPane.ERROR_MESSAGE);
							}
						}));
		
		//Exit
		actions.put(Command.Exit,
				new SimulatorAction("Salir", "exit.png",
						"Salir de la aplicacion", KeyEvent.VK_A,
						"control shift X", () -> System.exit(0)));
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
		autoReports = new JCheckBoxMenuItem("Informes automaticos");
		autoReports.addItemListener(this);
		reportsMenu.add(autoReports);

		menu = new JMenuBar();
		menu.add(fileMenu);
		menu.add(simulatorMenu);
		menu.add(reportsMenu);
		setJMenuBar(menu);
	}
	
	private void initGUIReportPanel() {
        RoadMap rm = controller.getTrafficSimulator().getRoadMap();
		reportWindow = new ReportWindow(this, rm);
		reportWindow.setData();
		
		int status = reportWindow.open();
		if (status == 0) {
			setReportsAreaPanel("Canceled");
		} else {
		    String s = "";
		    List<Vehicle> vehicleList = new ArrayList<Vehicle>();
            for(String v : reportWindow.getSelectedVehicles()) {
            	vehicleList.add(controller.getTrafficSimulator().getRoadMap().getVehicle(v));
            }
            s += controller.getTrafficSimulator().report(vehicleList).toString();
            List<Road> roadList = new ArrayList<Road>();
            for(String r : reportWindow.getSelectedRoads()) {
            	roadList.add(controller.getTrafficSimulator().getRoadMap().getRoad(r));
            }
            s += controller.getTrafficSimulator().report(roadList).toString();
            List<Junction> junctionList = new ArrayList<Junction>();
            for(String j : reportWindow.getSelectedJunctions()) {
            	junctionList.add(controller.getTrafficSimulator().getRoadMap().getJunction(j));
            }
            s += controller.getTrafficSimulator().report(junctionList).toString();
			setReportsAreaPanel(s);
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == autoReports) {
			if(e.getStateChange() == 1) {
				out = customizedOut;
			} else {
				out = null;
			}
		}
	}
	
	private void addTextArea(TextPanel textArea, String borderName, String inFileName, JPanel container) {
		textArea.setBorder(new TitledBorder(borderName));

		container.add(
				new JScrollPane(textArea,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		
		if (inFileName != null) {
			textArea.setText(TextPanel.readFile(new File(inFileName)));
		}
	}

	private void addTable(SimulatorTablePanel sPanel,
			String borderName, JPanel container) {
		TitledBorder controlBorder = new TitledBorder(borderName);
		sPanel.setBorder(controlBorder);

		container.add(
				new JScrollPane(sPanel,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
	}

	private void addMap() {
		RoadMap rm = new RoadMap();
		map = new RoadMapPanel(rm);

		map.setBorder(new TitledBorder("RoadMap"));

		bottomPanel.add(
				new JScrollPane(map, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
	}

	private void addStatusBar() {
		 statusBar = 
				 new StatusBar();
		 statusBar.setBorder(BorderFactory.createLineBorder(Color.black));
		 add(statusBar,BorderLayout.SOUTH);
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
	}
	
	public void setReportsAreaPanel(String s) {
		reportsAreaPanel.setText(s);
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
		JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
}