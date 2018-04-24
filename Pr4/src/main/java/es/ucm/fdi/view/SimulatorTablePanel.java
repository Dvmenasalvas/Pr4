package es.ucm.fdi.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

public class SimulatorTablePanel extends JPanel {
	ListOfMapsTableModel tableModel;
	JTable eventsTable;
	List<? extends Describable> elements;
	String[] fieldNames;

	public SimulatorTablePanel(List<? extends Describable> elements, String[] fieldNames) {
		super();
		this.elements = elements;
		this.fieldNames = fieldNames;

		TitledBorder controlBorder = new TitledBorder("Cola de eventos");
		this.setBorder(controlBorder);

		initTable();
	}

	public void setElements(List<? extends Describable> elements) {
		this.elements = elements;
		tableModel.fireTableDataChanged();
	}

	private void initTable() {
		tableModel = new ListOfMapsTableModel();
		eventsTable = new JTable(tableModel);
		this.add(new JScrollPane(eventsTable));
		eventsTable.setFillsViewportHeight(true);
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
			Map<String, String> description = new HashMap<String, String>();
			elements.get(rowIndex).describe(description);
			return description.get(fieldNames[columnIndex]);
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
