package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.model.EventType;

public class StatusBar extends JPanel {
    JLabel statusBarText;
	
	public StatusBar() {
		super();
		statusBarText = new JLabel();
	}
	
	public void setStatusBar(EventType et) {
		String s = "Nuevo evento: ";
        s += et.toString();
        statusBarText.setText(s);
		add(statusBarText);
	}
}
