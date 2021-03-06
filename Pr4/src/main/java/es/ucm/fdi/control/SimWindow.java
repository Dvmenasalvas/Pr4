package es.ucm.fdi.control;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * Esto es sólo para empezar a jugar con las interfaces de la P5.
 * 
 * El código <i>no</i> está bien organizado, y meter toda la funcionalidad aquí sería un disparate
 * desde un punto de vista de mantenibilidad.
 */

public class SimWindow extends JFrame {
	public SimWindow() {
		super("Prueba 123");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addBars();

		setSize(1000, 1000);
		setVisible(true);
	}

	private void addBars() {
		// split window
		Component componenteIzquierdo = null;
		Component componenteDerecho = null;
		JSplitPane bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				componenteIzquierdo, componenteDerecho);
		Component componenteDeArriba = null;
		Component componenteDeAbajo = null;
		JSplitPane topSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				componenteDeArriba, componenteDeAbajo);
		// da posición inicial a los componentes
		setVisible(true);
		bottomSplit.setDividerLocation(.5); // = 50% para cada lado
		// instantiate actions
		SimulatorAction salir = new SimulatorAction("Salir", "exit.png",
				"Salir de la aplicacion", KeyEvent.VK_A, "control shift X",
				() -> System.exit(0));
		SimulatorAction guardar = new SimulatorAction("Guardar", "save.png",
				"Guardar cosas", KeyEvent.VK_S, "control S",
				() -> System.err.println("guardando..."));

		// add actions to toolbar, and bar to window
		JToolBar bar = new JToolBar();
		bar.add(salir);
		bar.add(guardar);
		add(bar, BorderLayout.NORTH);

		// add actions to menubar, and bar to window
		JMenu file = new JMenu("File");
		file.add(guardar);
		file.add(salir);
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		setJMenuBar(menu);
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(() -> new SimWindow());
	}
}
