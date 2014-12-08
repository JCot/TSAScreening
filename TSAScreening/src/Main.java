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
			QueueActor q = new QueueActor(i);
			BagScanActor b = new BagScanActor();//Create BagScan
			//create bodyScan
			//create securityActor
			queues.add((ActorRef) q);//add to queues
			
		}
		//create Document Check
		DocumentCheckActor docCheck = new DocumentCheckActor(queues); //pass queues to document check
		//create Jail
		JailActor jail = new JailActor();
		
		
		//process passengers...how do you know how many passengers are in each line?
		//docCheck.tell() this doesn't work?
		
		//send shutdown message when all passengers are processed.  
		
		

	}

}
