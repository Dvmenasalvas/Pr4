package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.SimulatorAction;

public class EventsEditorPanel extends JPanel{
	private JTextArea eventsEditor; // editor de eventos
	private JFileChooser fc;
	private HashMap<Command, SimulatorAction> actions;
	
	public EventsEditorPanel(HashMap<Command, SimulatorAction> actions) {
		super();
		fc = new JFileChooser();
		eventsEditor = new JTextArea(24, 30);
		eventsEditor.setEditable(true);
		eventsEditor.setLineWrap(true);
		eventsEditor.setWrapStyleWord(true);
		this.actions = actions;
		this.add(eventsEditor);
		
		TitledBorder controlBorder = new TitledBorder("Editor de eventos");
	    this.setBorder(controlBorder);
	   
	    addRightClick();
	}
	
	
	
	public void addRightClick() {
		 JPopupMenu rightClick = new JPopupMenu();
			
			rightClick.add(actions.get(Command.LoadEvents));
			rightClick.add(actions.get(Command.SaveEvents));
			rightClick.add(actions.get(Command.CleanEvents));
			
			//Falta añadir los templates
			
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

	public void loadEvents(){
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
}
