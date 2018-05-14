package es.ucm.fdi.view;

import java.util.List;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.extra.graphlayout.Dot;
import es.ucm.fdi.extra.graphlayout.Edge;
import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.Node;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;

/**
 * RoadMapPanel es el JPanel que se ubica abajo a la derecha en la ventana principal, consta de un
 * grafo que se dibuja ateniéndose a los parámetros de cada objeto(vehiculos, carreteras y cruces)
 * del roadmap
 */

public class RoadMapPanel extends JPanel {
	private GraphComponent graphComp;
	private RoadMap rm;

	public RoadMapPanel(RoadMap rm) {
		super();
		this.rm = rm;
		
		graphComp = new GraphComponent();
		JScrollPane scroll =
				new JScrollPane(graphComp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scroll.setBorder(new TitledBorder("Mapa"));
		add(scroll);
	}

	public void setRoadMap(RoadMap rm) {
		this.rm = rm;
	}

	protected void generateGraph() {

		Graph g = new Graph();
		Map<Junction, Node> js = new HashMap<>();
		for (Junction j : rm.getJunctions()) {
			Node n = new Node(j.getId());
			// <-- para convertir Junction a Node en aristas
			js.put(j, n); 
			g.addNode(n);
		}

		int i = 0;
		for (Road r : rm.getRoads()) {
			boolean v = r.getDest().getConcreteSemaforo(r);
			Edge e = new Edge("e" + i, js.get(r.getSrc()), js.get(r.getDest()),
					r.getLength(), v);
			List<Integer> p = r.vehiclesPosition();
			for (int h = 0; h < p.size(); ++h) {
				e.addDot(new Dot("d" + h, p.get(h)));
			}
			g.addEdge(e);
			++i;
		}

		graphComp.setGraph(g);
	}
}
