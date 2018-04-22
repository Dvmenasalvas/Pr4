package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.HashMap;

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
		eventsEditor = new JTextArea(40, 30);
		this.actions = actions;
		
		this.add(new JScrollPane(eventsEditor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
		this.add(eventsEditor);
		TitledBorder controlBorder = new TitledBorder("Editor de eventos");
	    this.setBorder(controlBorder);
	    
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
	
	private String readFile(File file) {
		String out = null;
		 try {
			out = new String(Files.readAllBytes(file.toPath()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
	
	public void saveEvents() {
		int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(eventsEditor.getText());
				bw.close();
			} catch (IOException e) {
				//throw new IOException("Error al guardar el archivo en: " + file.getAbsolutePath(), e);
			}
            
        }
	}
	
	public void clearEvents() {
		eventsEditor.setText("");
	}
}
