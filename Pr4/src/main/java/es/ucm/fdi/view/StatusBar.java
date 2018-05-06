package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.ucm.fdi.model.EventType;

/**
 * StatusBar es la barra de estado ubicada en la
 * parte inferior de la ventana principal,
 * sencillamente imprime el mensaje correspondiente
 * cada vez que hay un nuevo evento en el simulador
 */
public class StatusBar extends JPanel {
    JLabel statusBarText;
	
	public StatusBar() {
		super();
		statusBarText = new JLabel();
	}
	
	public void setStatusBar(EventType et) {
        String s = et.toString();
        statusBarText.setText(s);
		add(statusBarText);
	}
}
