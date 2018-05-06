package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

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
import es.ucm.fdi.model.RoadMap;

/**
 * TextPanel es utilizado para los JPanel arriba a la derecha y a la izquierda de la
 * ventana principal. Arriba a la derecha se cargan los eventos y arriba a la izquierda 
 * se imprimen los informes, para ello se dan varias opciones al usuario mediante
 * el click derecho sobre cualquiera de las dos ventanas: cargar los eventos desde un fichero,
 * guardar los eventos en un fichero, limpiar de texto la ventana y añadir una plantilla
 * en formato ini donde solo haga falta completar los parámetros para introducir un nuevo evento 
 */
public class TextPanel extends JPanel {
	private JTextArea eventsEditor; // editor de eventos
	private JFileChooser fc;
	private HashMap<Command, SimulatorAction> actions;

	public TextPanel(HashMap<Command, SimulatorAction> actions,
			boolean editable) {
		super();
		fc = new JFileChooser();
		eventsEditor = new JTextArea(24, 30);
		eventsEditor.setEditable(editable);
		eventsEditor.setLineWrap(true);
		eventsEditor.setWrapStyleWord(true);
		this.actions = actions;
		this.add(eventsEditor);

		if (editable) {
			addRightClick();
		}
	}

	public void addRightClick() {
		JPopupMenu rightClick = new JPopupMenu();

		rightClick.add(actions.get(Command.LoadEvents));
		rightClick.add(actions.get(Command.SaveEvents));
		rightClick.add(actions.get(Command.CleanEvents));
		JMenu subMenu = new JMenu("Add template");
		rightClick.add(subMenu);
		String[] templates = { "New RR Junction", "New MC Junction", "New Junction",
				"New Dirt Road", "New Lanes Road", "New Road", "New Bike", "New Car", "New Vehicle",
				"New Vehicle Faulty" };

		for (String s : templates) {
			JMenuItem menuItem = new JMenuItem(s);
			menuItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					switch (s) {
					case ("New RR Junction"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New MC Junction"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New Junction"): {
						eventsEditor.insert(newJunctionTemplate(),
								eventsEditor.getCaretPosition());
					}
						break;
					case ("New Dirt Road"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New Lanes Road"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New Road"): {
						eventsEditor.insert(newRoadTemplate(),
								eventsEditor.getCaretPosition());
					}
						break;
					case ("New Bike"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New Car"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New Vehicle"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					case ("New Vehicle Faulty"): {
						eventsEditor.insert(s, eventsEditor.getCaretPosition());
					}
						break;
					}

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

	public void loadEvents() {
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			eventsEditor.setText(readFile(file));
		}
	}

	public static String readFile(File file) {
		String s = "";
		try {
			s = new Scanner(file).useDelimiter("\\A").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return s;
	}

	public void saveEvents() {
		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			writeFile(file, eventsEditor.getText());
		}
	}

	private void writeFile(File file, String content) {
		try {
			PrintWriter pw = new PrintWriter(file);
			pw.print(content);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private String newJunctionTemplate() {
		String s = "[new_junction]\n";
		s += "time =\n";
		s += "id =\n";
		return s;
	}
	
	private String newMCJunctionTemplate() {
		String s = "[new_junction]\n";
		s += "time =\n";
		s += "id =\n";
		s += "type = mc\n";
		return s;
	}
	
	private String newRRJunctionTemplate() {
		String s = "[new_junction]\n";
		s += "time =\n";
		s += "id =\n";
		s += "type = rr\n";
		return s;
	}
	
	private String newRoadTemplate() {
		String s = "[new_road]\n";
		s += "time =\n";
		s += "id =\n";
		s += "src =\n";
		s += "dest =\n";
		s += "max_speed =\n";
		s += "length =\n";
		return s;
	}
	
	private String newLanesRoadTemplate() {
		String s = "[new_road]\n";
		s += "time =\n";
		s += "id =\n";
		s += "src =\n";
		s += "dest =\n";
		s += "max_speed =\n";
		s += "length =\n";
		s += "lanes =\n";
		return s;
	}

	private String newVehicleTemplate() {
		String s = "[new_vehicle]\n";
		s += "time =\n";
		s += "id =\n";
		s += "itinerary =\n";
		s += "max_speed =\n";
		return s;
	}
	
	private String newBikeTemplate() {
		String s = "[new_vehicle]\n";
		s += "time =\n";
		s += "id =\n";
		s += "itinerary =\n";
		s += "max_speed =\n";
		 s+= "type = bike\n";
		return s;
	}
	
	private String newCarTemplate() {
		String s = "[new_vehicle]\n";
		s += "time =\n";
		s += "id =\n";
		s += "itinerary =\n";
		s += "max_speed =\n";
		 s+= "type = car\n";
		return s;
	}
	
	private String newVehicleFaultyTemplate() {
		String s = "[make_vehicle_faulty]\n";
		s += "time =\n";
		s += "vehicles =\n";
		s += "duration =\n";
		return s;
	}
}
