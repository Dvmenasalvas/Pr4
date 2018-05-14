package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

/**
 * SimulatorTablePanel es utilizado para crear las tablas de la ventana principal, para ello utiliza
 * un array fieldNames en el que se ubicarán los nombres identificadores de lo que va a haber en
 * cada columna de la tabla y un Describable que lo que hace es pedir a un objeto cualquiera que
 * describa sus parámetros para así poderlos imprimir en las casillas correspondientes de la
 * tabla(en la misma fila que el identificador del objeto que se está describiendo y en la misma
 * columna del nombre del atributo que se se está describiendo)
 */

public class SimulatorTablePanel extends JPanel {
	ListOfMapsTableModel tableModel;
	JTable eventsTable;
	List<? extends Describable> elements;
	String[] fieldNames;
	Map<String, String> lastDescription;
	int lastRow = -1;

	public SimulatorTablePanel(List<? extends Describable> elements,
			String[] fieldNames, String borderTitle) {
		super(new BorderLayout());
		this.elements = elements;
		this.fieldNames = fieldNames;

		tableModel = new ListOfMapsTableModel();
		eventsTable = new JTable(tableModel);
		
		JScrollPane scroll = new JScrollPane(eventsTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Border b = BorderFactory.createLineBorder(Color.gray, 2);
		scroll.setBorder(BorderFactory.createTitledBorder(b,borderTitle));
		
		add(scroll);
	}

	public void setElements(List<? extends Describable> elements) {
		this.elements = elements;
		tableModel.fireTableDataChanged();
	}

	private class ListOfMapsTableModel extends AbstractTableModel {
		@Override // fieldNames es un String[] con nombrs de col.
		public String getColumnName(int columnIndex) {
			return fieldNames[columnIndex];
		}

		@Override // elements contiene la lista de elementos
		public int getRowCount() {
			return elements.size();
		}

		@Override
		public int getColumnCount() {
			return fieldNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (fieldNames[columnIndex] == "#") {
				return rowIndex;
			}
			if (rowIndex != lastRow) {
				lastRow = rowIndex;
				lastDescription = new HashMap<String, String>();
				elements.get(rowIndex).describe(lastDescription);
			}
			return lastDescription.get(fieldNames[columnIndex]);
		}
	}

	public interface Describable {
		/**
		 * @param out
		 *            - a map to fill in with key- value pairs
		 * @return the passed- in map, with all fields filled out.
		 */
		void describe(Map<String, String> out);
	}
}
