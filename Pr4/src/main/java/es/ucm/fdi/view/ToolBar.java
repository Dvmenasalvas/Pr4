package es.ucm.fdi.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

import es.ucm.fdi.control.SimulatorAction;

/**
 * Es la barra de herramientas arriba en la ventana principal consta de varios botones asociados a
 * distintas acciones que puede realizar el simulador, un JTextFiel en el que se imprime el turno
 * del simulador y un JSpinner en el que se configura el numero de pasos que va a dar el simulador
 * cada vez que se ejecute el comando execute
 */

public class ToolBar extends JToolBar {
	private HashMap<Command, SimulatorAction> actions;
	private JSpinner stepsSpinner;
	private JSpinner delaySpinner;
	private JTextField timeViewer;

	public ToolBar(HashMap<Command, SimulatorAction> actions, int timeLimit) {
		super();
		this.actions = actions;
		initToolBar(timeLimit);
	}

	private void initToolBar(int timeLimit) {
		stepsSpinner = new JSpinner(
				new SpinnerNumberModel(timeLimit, 1, 100, 1));
		stepsSpinner.setMaximumSize(new Dimension(60,30));
		JFormattedTextField textField = ((JSpinner.NumberEditor) stepsSpinner.getEditor()).getTextField();
		((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
		
		delaySpinner = new JSpinner(
				new SpinnerNumberModel(50, 0, 1000, 50));
		delaySpinner.setMaximumSize(new Dimension(60,30));
		textField = ((JSpinner.NumberEditor) delaySpinner.getEditor()).getTextField();
		((NumberFormatter) textField.getFormatter()).setAllowsInvalid(false);
		
		timeViewer = new JTextField("0");
		timeViewer.setEditable(false);
		timeViewer.setMaximumSize(new Dimension(65, 30));
		timeViewer.setBackground(Color.WHITE);

		// add actions to toolbar, and bar to window
		add(actions.get(Command.LoadEvents));
		add(actions.get(Command.SaveEvents));
		add(actions.get(Command.CleanEvents));
		addSeparator();

		add(actions.get(Command.InsertEvents));
		add(actions.get(Command.Execute));
		add(actions.get(Command.Stop));
		add(actions.get(Command.Reset));
		
		add(new JLabel("Delay: "));
		add(delaySpinner);
		delaySpinner
				.setToolTipText("<html>Determina el numero de milisegundos que <br>"
						+ "tarda en avanzar al siguiente paso");

		add(new JLabel("Pasos: "));
		add(stepsSpinner);
		stepsSpinner
				.setToolTipText("<html>Determina el numero de pasos que se <br>"
						+ "dan en la simulacion");

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

	public int getSteps() {
		return (int) stepsSpinner.getValue();
	}
	
	public int getDelay() {
		return (int) delaySpinner.getValue();
	}

	public void setTimeValue(int time) {
		timeViewer.setText(Integer.toString(time));
	}
}
