package es.ucm.fdi.view;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

import es.ucm.fdi.control.SimulatorAction;

public class ToolBar extends JToolBar {
	private HashMap<Command, SimulatorAction> actions;
	private JSpinner stepsSpinner;
	private JTextField timeViewer;

	public ToolBar(HashMap<Command, SimulatorAction> actions, int timeLimit) {
		super();
		this.actions = actions;
		initToolBar(timeLimit);
	}

	private void initToolBar(int timeLimit) {
		stepsSpinner = new JSpinner(new SpinnerNumberModel(timeLimit, 1, 100, 1));
		timeViewer = new JTextField();
		timeViewer.setText("0");
		timeViewer.setEditable(false);
		timeViewer.setPreferredSize(new Dimension(50, 60));

		// add actions to toolbar, and bar to window
		add(actions.get(Command.LoadEvents));
		add(actions.get(Command.SaveEvents));
		add(actions.get(Command.CleanEvents));
		addSeparator();

		add(actions.get(Command.InsertEvents));
		add(actions.get(Command.Execute));
		add(actions.get(Command.Reset));

		add(new JLabel("Pasos: "));
		add(stepsSpinner);
		stepsSpinner.setToolTipText(
				"<html>Determina el numero de pasos que se <br>" + "dan en la simulacion");

		add(new JLabel("Tiempo: "));
		add(timeViewer);
		addSeparator();

		add(actions.get(Command.GenerateReports));
		add(actions.get(Command.CleanReports));
		add(actions.get(Command.SaveReports));
		addSeparator();

		add(actions.get(Command.Exit));

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

	}

	public int getSpinnerValue() {
		return (int) stepsSpinner.getValue();
	}

	public void setTimeValue(int time) {
		timeViewer.setText(Integer.toString(time));
	}
}
