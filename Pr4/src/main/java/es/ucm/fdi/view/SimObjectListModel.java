package es.ucm.fdi.view;

import java.util.List;

import javax.swing.DefaultListModel;

public class SimObjectListModel<E> extends DefaultListModel<E> {

	private static final long serialVersionUID = 1L;
	List<E> list;

	public SimObjectListModel() {
		list = null;
	}

	public void setList(List<E> l) {
		list = l;
		refresh();
	}

	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E getElementAt(int index) {
		return list.get(index);
	}

	@Override
	public int getSize() {
		return list == null ? 0 : list.size();
	}

	public void refresh() {
		fireContentsChanged(this, 0, list.size());
	}

}
