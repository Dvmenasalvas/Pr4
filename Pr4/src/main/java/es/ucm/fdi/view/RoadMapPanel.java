package es.ucm.fdi.view;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import es.ucm.fdi.extra.graphlayout.Dot;
import es.ucm.fdi.extra.graphlayout.Edge;
import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.Node;
import es.ucm.fdi.model.RoadMap;
import es.ucm.fdi.model.simulatedobject.Junction;
import es.ucm.fdi.model.simulatedobject.Road;

public class RoadMapPanel extends JFrame{
	private GraphComponent _graphComp;
	private RoadMap roadMap;
	
	protected void generateGraph() {

		Graph g = new Graph();
		Map<Junction, Node> js = new HashMap<>();
		for (Junction j : roadMap.getJunctions()) {
			Node n = new Node(j.getId());
			js.put(j, n); // <-- para convertir Junction a Node en aristas
			g.addNode(n);
		}
		
		int i = 0;
		for (Road r : roadMap.getRoads()) {
			Edge e = 
			     new Edge("e"+i, 
			    		 g.getNodes().get(g.getNodes().indexOf(r.getSrc())), 
			    		 g.getNodes().get(g.getNodes().indexOf(r.getDest())), 
			    		 r.getLength());
			int numDots = r.numVehicles();
			List<Integer> p = r.vehiclesPosition();
			for(int j=0; j<p.size(); j++) {
			e.addDot( new Dot("d"+j, p.get(j)));
			}
			g.addEdge(e);
			++i;
		}
	}
}
