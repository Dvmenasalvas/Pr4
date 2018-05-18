package es.ucm.fdi.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;
import es.ucm.fdi.model.simulatedobject.SimObject;
import es.ucm.fdi.model.simulatedobject.Vehicle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class ReportWindow extends JDialog {

	private static final long serialVersionUID = 1L;

	JPanel mainPanel;
	JPanel contentPanel;
	
	private SimObjectListModel<String> vehiclesListModel;
	private SimObjectListModel<String> roadsListModel;
	private SimObjectListModel<String> junctionsListModel;

	private int status;
	private JList<String> vehiclesList;
	private JList<String> roadsList;
	private JList<String> junctionsList;

	static final private char _clearSelectionKey = 'c';
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black,
			2);

	public ReportWindow(Frame parent) {
		super(parent, true);
		initGUI();
	}

	private void initGUI() {
		status = 0;

		setTitle("Generar Informe");
		mainPanel = new JPanel(new BorderLayout());

		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		JPanel vehiclesPanel = new JPanel(new BorderLayout());
		JPanel roadsPanel = new JPanel(new BorderLayout());
		JPanel junctionsPanel = new JPanel(new BorderLayout());
		
		addPanel(vehiclesPanel,"Vehiculos");
		addPanel(roadsPanel,"Carreteras");
		addPanel(junctionsPanel,"Cruzes");

		vehiclesListModel = new SimObjectListModel<>();
		roadsListModel = new SimObjectListModel<>();
		junctionsListModel = new SimObjectListModel<>();

		vehiclesList = new JList<>(vehiclesListModel);
		roadsList = new JList<>(roadsListModel);
		junctionsList = new JList<>(junctionsListModel);
		
		addCleanSelectionListener(vehiclesList);
		addCleanSelectionListener(roadsList);
		addCleanSelectionListener(junctionsList);

		vehiclesPanel.add(
				new JScrollPane(vehiclesList),
				BorderLayout.CENTER);

		roadsPanel.add(
				new JScrollPane(roadsList),
				BorderLayout.CENTER);
		
		junctionsPanel.add(
				new JScrollPane(junctionsList),
				BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 0;
				ReportWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status = 1;
				ReportWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		mainPanel.add(infoPanel, BorderLayout.PAGE_START);

		infoPanel
				.add(new JLabel("Selecciona los objetos de los que quieres obtener el report."));
		infoPanel.add(new JLabel(
				"Usa '" + _clearSelectionKey + "' para deseleccionar todos."));
		infoPanel.add(new JLabel("Usa Ctrl+A para seleccionar todos"));
		infoPanel.add(new JLabel(" "));

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		setVisible(false);
	}
	
	private void addPanel(JPanel panel, String borderTitle) {
		contentPanel.add(panel);
		panel.setBorder(BorderFactory.createTitledBorder(_defaultBorder,
				borderTitle, TitledBorder.LEFT, TitledBorder.TOP));
		panel.setMinimumSize(new Dimension(100, 100));
	}

	private void addCleanSelectionListener(JList<?> list) {
		list.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == _clearSelectionKey) {
					list.clearSelection();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

	}

	public void setData(List<Vehicle> vehicles, List<Road> roads, List<Junction> junctions) {
		vehiclesListModel.setList(toStringList(vehicles));
		roadsListModel.setList(toStringList(roads));
		junctionsListModel.setList(toStringList(junctions));
	}
	
	public List<String> toStringList(List<? extends SimObject> l) {
		List<String> salida = new ArrayList<String>();
		for(SimObject s : l) {
			salida.add(s.getId());
		}
		return salida;
	}

	public List<String> getSelectedVehicles() {
		int[] indices = vehiclesList.getSelectedIndices();
		List<String> vehicles = new ArrayList<String>();
		for (int i = 0; i < indices.length; i++) {
			vehicles.add(vehiclesListModel.getElementAt(indices[i]));
		}
		return vehicles;
	}
	
	public List<String> getSelectedRoads() {
		int[] indices = roadsList.getSelectedIndices();
		List<String> roads = new ArrayList<String>();
		for (int i = 0; i < indices.length; i++) {
			roads.add(roadsListModel.getElementAt(indices[i]));
		}
		return roads;
	}
	
	public List<String> getSelectedJunctions() {
		int[] indices = junctionsList.getSelectedIndices();
		List<String> junctions = new ArrayList<String>();
		for (int i = 0; i < indices.length; i++) {
			junctions.add(junctionsListModel.getElementAt(indices[i]));
		}
		return junctions;
	}

	public int open() {
		setLocation(getParent().getLocation().x + 50,
				getParent().getLocation().y + 50);
		pack();
		setVisible(true);
		return status;
	}


}
