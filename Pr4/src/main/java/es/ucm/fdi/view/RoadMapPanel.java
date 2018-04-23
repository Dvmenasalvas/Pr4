package es.ucm.fdi.view;

import java.util.List;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
	
	public RoadMapPanel() {
		super();
		initGUI();
	}

	private void initGUI() {

		JPanel mainPanel = new JPanel(new BorderLayout() );
		
		_graphComp = new GraphComponent();
		mainPanel.add(_graphComp, BorderLayout.CENTER);

		JButton newGraph = new JButton("New Graph");
		newGraph.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateGraph();
			}
		});
		
		mainPanel.add(newGraph,BorderLayout.PAGE_START);
		
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}
	
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
			List<Integer> p = r.vehiclesPosition();
			for(int j=0; j<p.size(); j++) {
			e.addDot( new Dot("d"+j, p.get(j)));
			}
			g.addEdge(e);
			++i;
		}
	}
}
