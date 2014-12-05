import java.util.ArrayList;

import akka.actor.ActorRef;


public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int numLines = Integer.parseInt(args[0]);
		ArrayList<ActorRef> queues = new ArrayList<ActorRef>();
		
		//Construct each line (from queue to security station) as a unit,
		//and when all lines are created,
		//pass these in a collection of some kind to the document check.
		for (int i = 0; i < numLines; i++) {
			//create QueueActor
			QueueActor q = new QueueActor();
			BagScanActor b = new BagScanActor();//Create BagScan
			//create bodyScan
			//create securityActor
			//add to queues
		}
		//create Document Check
		//create Jail
		//pass queues to document check
		
		
		//process pasengers...how do you know how many passengers are in each line?
		
		
		//send shutdown message when all passengers are processed.  
		
		

	}

}
