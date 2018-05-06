package es.ucm.fdi.view;

import java.io.IOException;
import java.io.OutputStream;

public class CustomizedOut extends OutputStream {
	TextPanel textPanel;

	public CustomizedOut(TextPanel tp) {
		super();
		textPanel = tp;
	}

	@Override
	public void write(int arg0) throws IOException {
		int[] bytes = { arg0 };
		textPanel.append(new String(bytes, 0, bytes.length));
	}

}
