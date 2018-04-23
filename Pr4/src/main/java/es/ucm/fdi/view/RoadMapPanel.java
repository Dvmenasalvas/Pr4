package es.ucm.fdi.view;

import java.util.List;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import es.ucm.fdi.extra.graphlayout.Dot;
import es.ucm.fdi.extra.graphlayout.Edge;
import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.Node;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;

public class RoadMapPanel extends JPanel {
	private GraphComponent _graphComp;
	private RoadMap rm;

	public RoadMapPanel(RoadMap rm) {
		super();
		this.rm = rm;
		initGUI();
	}

	public void setRoadMap(RoadMap rm) {
		this.rm = rm;
	}

	private void initGUI() {
		_graphComp = new GraphComponent();
		add(_graphComp, BorderLayout.CENTER);
	}

	protected void generateGraph() {

		Graph g = new Graph();
		Map<Junction, Node> js = new HashMap<>();
		for (Junction j : rm.getJunctions()) {
			Node n = new Node(j.getId());
			js.put(j, n); // <-- para convertir Junction a Node en aristas
			g.addNode(n);
		}

		int i = 0;
		for (Road r : rm.getRoads()) {
			Edge e = new Edge("e" + i, g.getNodes().get(g.getNodes().indexOf(r.getSrc())),
					g.getNodes().get(g.getNodes().indexOf(r.getDest())), r.getLength());
			List<Integer> p = r.vehiclesPosition();
			for (int j = 0; j < p.size(); j++) {
				e.addDot(new Dot("d" + j, p.get(j)));
			}
			g.addEdge(e);
			++i;
		}
	}
	/*
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new Runnable() { public
	 * void run() { new RoadMapPanel(rm); } }); }
	 */
}
