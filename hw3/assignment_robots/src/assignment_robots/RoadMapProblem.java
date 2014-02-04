package assignment_robots;

import java.util.*;

import assignment_robots.RoadMapProblem.AdjacentCfg;
import assignment_robots.SearchProblem.SearchNode;
import assignment_robots.InformedSearchProblem;

public class RoadMapProblem extends InformedSearchProblem {
	
	ArmRobot startArm, goalArm;
	HashSet<ArmRobot> samplings = new HashSet<>(); // world sampling
	HashMap<ArmRobot, PriorityQueue<AdjacentCfg>> roadmap = new HashMap<>(); // roadmap, a graph
	World map;
	int k_neighbour;
	
	public RoadMapProblem(World m, double[] config1, double[] config2, int density, int K) {
		k_neighbour = K;
		map = m;
		startArm = new ArmRobot(config1);
		goalArm = new ArmRobot(config2);
		startNode = new RoadMapNode(startArm, 0);
		getSampling(density);
		getConnected();

		// TODO Auto-generated constructor stub
	}
	
	public class AdjacentCfg implements Comparable<AdjacentCfg> {
		public ArmRobot ar;
		public double dis;
		
		AdjacentCfg(ArmRobot a, double d){
			ar = a;
			dis = d;
		}
		
		@Override
		public int compareTo(AdjacentCfg other)  {
			// sorted from large to small
			return (int) Math.signum(other.dis - dis);
		}
	}
	
	public void getSampling(int density) {
		while(density > 0) {
			double [] rConfig = getRandCfg(startArm.links, map);
			ArmRobot toBeAdded = new ArmRobot(rConfig);
			if(!map.armCollision(toBeAdded)) {
				samplings.add(toBeAdded);
				density--;
			}
		}
		samplings.add(startArm);
		samplings.add(goalArm);
	}
	
	private double[] getRandCfg(int num, World map) {
		Random rd = new Random();
		double[] cfg = new double[2 * num + 2];
		// randomize the start position
		cfg[0] = rd.nextDouble() * map.getW();
		cfg[1] = rd.nextDouble() * map.getH();
		for (int i = 1; i <= num; i++) {
			// the length of each arm remains the same
			cfg[2 * i] = startArm.config[2 * i];
			// randomize the angle
			cfg[2 * i + 1] = rd.nextDouble() * Math.PI * 2;
		}
		return cfg;
	}
	
	public void getConnected() {
		// initiate the connecting with start arm
		ArmLocalPlanner ap = new ArmLocalPlanner();
		PriorityQueue<AdjacentCfg> tmpq;
		for(ArmRobot ar : samplings) 
			roadmap.put(ar, new PriorityQueue<AdjacentCfg>());
		
		for(ArmRobot ar : samplings) {
			for(ArmRobot arOther : samplings) {
				int base_conn = roadmap.get(ar).size();
				if(ar != arOther) {
					double dis = ap.moveInParallel(ar.config, arOther.config);
					if(!map.armCollisionPath(ar, ar.config, arOther.config)) {
						tmpq = roadmap.get(ar);
						tmpq.add(new AdjacentCfg(arOther, dis));
						if(tmpq.size() > k_neighbour + base_conn)
							tmpq.poll();
						roadmap.get(arOther).add(new AdjacentCfg(ar, dis));
					}
				}
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////
	
	
	public class RoadMapNode implements SearchNode {
		private ArmRobot arm;
		private double cost;
		
		// construct the connected graph
		public RoadMapNode(ArmRobot inAr, double c) {
			arm = inAr;
			cost = c;
		}
		
		public ArrayList<SearchNode> getSuccessors() {
			ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
			for(AdjacentCfg adj : roadmap.get(arm)) {
				successors.add(new RoadMapNode(adj.ar, adj.dis + cost));
			}
			return successors;
		}
		
		// override functions
		@Override
		public boolean equals(Object other) {
			return arm == ((RoadMapNode) other).arm;
		}

		@Override
		public String toString() {
			return arm.toString();
		}

		@Override
		public int compareTo(SearchNode o) {
			return (int) Math.signum(priority() - o.priority());
		}

		@Override
		public boolean goalTest() {
			// TODO Auto-generated method stub
			return goalArm == arm;
		}

		@Override
		public double getCost() {
			// TODO Auto-generated method stub
			return cost;
		}

		@Override
		public double heuristic() {
			// TODO Auto-generated method stub
			ArmLocalPlanner ap = new ArmLocalPlanner();
			return ap.moveInParallel(arm.config, goalArm.config);
		}

		@Override
		public double priority() {
			// TODO Auto-generated method stub
			return heuristic() + getCost();
		}
	}

}
