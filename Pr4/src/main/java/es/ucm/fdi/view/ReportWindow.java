package es.ucm.fdi.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.extra.dialog.MyListModel;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;
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

	private MyListModel<String> _vehiclesListModel;
	private MyListModel<String> _roadsListModel;
	private MyListModel<String> _junctionsListModel;

	private int _status;
	private JList<String> _vehiclesList;
	private JList<String> _roadsList;
	private JList<String> _junctionsList;
	private RoadMap _rm;

	static final private char _clearSelectionKey = 'c';
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black,
			2);

	public ReportWindow(Frame parent, RoadMap rm) {
		super(parent, true);
		_rm = rm;
		initGUI();
	}

	private void initGUI() {

		_status = 0;

		setTitle("Generate Report");
		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		JPanel vehiclesPanel = new JPanel(new BorderLayout());
		JPanel roadsPanel = new JPanel(new BorderLayout());
		JPanel junctionsPanel = new JPanel(new BorderLayout());

		contentPanel.add(vehiclesPanel);
		contentPanel.add(roadsPanel);
		contentPanel.add(junctionsPanel);

		vehiclesPanel.setBorder(BorderFactory.createTitledBorder(_defaultBorder,
				"Vehicles", TitledBorder.LEFT, TitledBorder.TOP));
		roadsPanel.setBorder(BorderFactory.createTitledBorder(_defaultBorder,
				"Roads", TitledBorder.LEFT, TitledBorder.TOP));
		junctionsPanel.setBorder(BorderFactory.createTitledBorder(_defaultBorder,
				"Junctions", TitledBorder.LEFT, TitledBorder.TOP));

		vehiclesPanel.setMinimumSize(new Dimension(100, 100));
		roadsPanel.setMinimumSize(new Dimension(100, 100));
		junctionsPanel.setMinimumSize(new Dimension(100, 100));
		
		vehiclesPanel.setMaximumSize(new Dimension(100, 100));
		roadsPanel.setMaximumSize(new Dimension(100, 100));
		junctionsPanel.setMaximumSize(new Dimension(100, 100));

		_vehiclesListModel = new MyListModel<>();
		_roadsListModel = new MyListModel<>();
		_junctionsListModel = new MyListModel<>();

		_vehiclesList = new JList<>(_vehiclesListModel);
		_roadsList = new JList<>(_roadsListModel);
		_junctionsList = new JList<>(_junctionsListModel);
		
		addCleanSelectionListner(_vehiclesList);
		addCleanSelectionListner(_roadsList);
		addCleanSelectionListner(_junctionsList);

		vehiclesPanel.add(
				new JScrollPane(_vehiclesList,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		roadsPanel.add(
				new JScrollPane(_roadsList,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		
		junctionsPanel.add(
				new JScrollPane(_roadsList,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ReportWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 1;
				ReportWindow.this.setVisible(false);
			}
		});
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		mainPanel.add(infoPanel, BorderLayout.PAGE_START);

		infoPanel
				.add(new JLabel("Select items for which you want to process."));
		infoPanel.add(new JLabel(
				"Use '" + _clearSelectionKey + "' to deselect all."));
		infoPanel.add(new JLabel("Use Ctrl+A to select all"));
		infoPanel.add(new JLabel(" "));

		setContentPane(mainPanel);
		setMinimumSize(new Dimension(100, 100));
		setVisible(false);
	}

	private void addCleanSelectionListner(JList<?> list) {
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

	public void setData() {
		List<String> _vehicles = new ArrayList<>();
		List<String> _roads = new ArrayList<>();
		List<String> _junctions = new ArrayList<>();
		for (Vehicle v : _rm.getVehicles()) {
			_vehicles.add(v.getId());
		}
		for (Road r : _rm.getRoads()) {
			_roads.add(r.getId());
		}
		for (Junction j : _rm.getJunctions()) {
			_junctions.add(j.getId());
		}
		_vehiclesListModel.setList(_vehicles);
		_roadsListModel.setList(_roads);
		_junctionsListModel.setList(_junctions);
	}

	public List<String> getSelectedVehicles() {
		int[] indices = _vehiclesList.getSelectedIndices();
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			items.set(i, _vehiclesListModel.getElementAt(indices[i]));
		}
		return items;
	}
	
	public List<String> getSelectedRoads() {
		int[] indices = _vehiclesList.getSelectedIndices();
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			items.set(i, _roadsListModel.getElementAt(indices[i]));
		}
		return items;
	}
	
	public List<String> getSelectedJunctions() {
		int[] indices = _junctionsList.getSelectedIndices();
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			items.set(i, _junctionsListModel.getElementAt(indices[i]));
		}
		return items;
	}

	public int open() {
		setLocation(getParent().getLocation().x + 50,
				getParent().getLocation().y + 50);
		pack();
		setVisible(true);
		return _status;
	}

	public void setRoadMap(RoadMap roadMap) {
		_rm = roadMap;
	}

}
