/**
 * BagScanActor is a representation of a bag scanner in 
 * an airport's security process.
 * 
 * @author Justin Cotner
 * @author Andrew Popovich (ajp7560@rit.edu)
 */
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class BagScanActor extends UntypedActor{

	/** Random utility object to create random numbers */
	private Random rand = new Random();
	
	/** Line number for the line the Bag Scanner is in */
	private int lineNum;
	
	/** Reference to the Security Actor that is in the same line */
	private ActorRef security;
	
	/**
	 * Constructor for a BagScanActor. Sets the line number and Security Actor
	 * reference.
	 * 
	 * @param lineNum - Integer representing the line number
	 * @param security - Reference to the Security Actor
	 */
	public BagScanActor(int lineNum, ActorRef security) {
		this.lineNum = lineNum;
		this.security = security;
	}
	
	/**
	 * onReceive processes messages sent to the BagScanActor. If the message
	 * is a passenger's bags the BagScanActor will scan the bags. If the message is
	 * the end of day message then the BagScanActor will propagate it to the system.
	 * 
	 * @param message - Passenger or EndOfDay message
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Bag){
			int result = rand.nextInt(101);
			System.out.println("---Received Message: Bag, From: Queue Actor "+lineNum+", To: Bag Scan Actor "+lineNum+"---");
			System.out.println("\nBag Scan Actor "+lineNum+" is scanning "+ ((Messages.Bag)message).getPassengerId() + "'s bag.\n");

			//Bag did not pass
			if(result <= 20){
				System.out.println("\nPassenger " + ((Messages.Bag)message).getPassengerId() + "'s bag failed inspection.\n");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Bag)message).getPassengerId() + ", From: Bag Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Bag)message).getPassengerId(), false, 0), self());
			}

			else{
				System.out.println("\nPassenger " + ((Messages.Bag)message).getPassengerId() + "'s bag passed inspection.\n");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Bag)message).getPassengerId() + ", From: Bag Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Bag)message).getPassengerId(), true, 0), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: Queue Actor "+lineNum+", To: Bag Scan Actor "+lineNum+"---");
			System.out.println("---Sent Message: End of Day, From: Bag Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
			security.tell((Messages.EndOfDay)message, self());
			((ActorRef) this.self()).stop();
		}
		
	}

}
