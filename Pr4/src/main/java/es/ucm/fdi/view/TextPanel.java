package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.RoadMap;

/**
 * TextPanel es utilizado para los JPanel arriba a la derecha y a la izquierda de la ventana
 * principal. Arriba a la derecha se cargan los eventos y arriba a la izquierda se imprimen los
 * informes, para ello se dan varias opciones al usuario mediante el click derecho sobre cualquiera
 * de las dos ventanas: cargar los eventos desde un fichero, guardar los eventos en un fichero,
 * limpiar de texto la ventana y añadir una plantilla en formato ini donde solo haga falta completar
 * los parámetros para introducir un nuevo evento
 */
public class TextPanel extends JPanel {
	private JTextArea eventsEditor; // editor de eventos
	private JFileChooser fc;
	private HashMap<Command, SimulatorAction> actions;
	private JPopupMenu rightClick;
	private final String TEMPLATES = "templates.ini";

	public TextPanel(HashMap<Command, SimulatorAction> actions, String borderTitle,
			boolean editable) throws IOException {
		super();
		fc = new JFileChooser();
		eventsEditor = new JTextArea(24, 30);
		eventsEditor.setEditable(editable);
		eventsEditor.setLineWrap(true);
		eventsEditor.setWrapStyleWord(true);
		this.actions = actions;
		
		JScrollPane scroll = new JScrollPane(eventsEditor);
		scroll.setBorder(new TitledBorder(borderTitle));
		
		add(scroll);

		if (editable) {
			addRightClick();
		}
	}

	public void addRightClick() throws IOException {
		rightClick = new JPopupMenu();

		rightClick.add(actions.get(Command.LoadEvents));
		rightClick.add(actions.get(Command.SaveEvents));
		rightClick.add(actions.get(Command.CleanEvents));
		JMenu subMenu = new JMenu("Add template");
		rightClick.add(subMenu);
		// Leemos fichero .ini
		
		InputStream s = this.getClass().getClassLoader().getResourceAsStream(TEMPLATES);
		Ini templates = new Ini(s);

		for (IniSection sec : templates.getSections()) {
			JMenuItem menuItem = new JMenuItem(sec.getValue("_name"));
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					eventsEditor.insert(sec.toString(),
							eventsEditor.getCaretPosition());
				}
			});
			subMenu.add(menuItem);
		}

		// connect the popup menu to the text area _editor
		eventsEditor.addMouseListener(new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				showPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopup(e);
			}

			private void showPopup(MouseEvent e) {
				if (e.isPopupTrigger() && rightClick.isEnabled()) {
					rightClick.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	public void loadEvents() throws FileNotFoundException {
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				eventsEditor.setText(readFile(file));
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException(
						"No se ha encontrado el archivo: "
								+ file.getAbsolutePath());
			}
		}
	}

	public static String readFile(File file) throws FileNotFoundException {
		String s = "";
		try {
			s = new Scanner(file).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(
					"No se ha encontrado el archivo " + file.getAbsolutePath());
		}

		return s;
	}

	public void saveEvents() throws FileNotFoundException {
		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				writeFile(file, eventsEditor.getText());
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException(
						"No se ha podido encontrar el archivo: "
								+ file.getAbsolutePath());
			}
		}
	}

	private void writeFile(File file, String content)
			throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(file);
		pw.print(content);
		pw.close();
	}

	public void clearEvents() {
		eventsEditor.setText("");
	}

	public String getText() {
		return eventsEditor.getText();
	}

	public void setText(String text) {
		eventsEditor.setText(text);
	}

	public void append(String text) {
		eventsEditor.append(text);
	}
}
