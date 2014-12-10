/**
 * BodyScanActor is a representation of a body scanner in 
 * an airport's security process.
 * 
 * @author Justin Cotner
 * @author Andrew Popovich (ajp7560@rit.edu)
 */

import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class BodyScanActor extends UntypedActor{

	/** Configurable constant for the time it takes to scan a passenger */
	private static final int delayTime = 500;
	
	/** Random utility object to create random numbers */
	private Random rand = new Random();
	
	/** Line number for the line the Bag Scanner is in */
	private int lineNum;
	
	/** Reference to the Security Actor that is in the same line */
	private ActorRef security;
	
	/**
	 * Constructor for a BodyScanActor. Sets the line number and Security Actor
	 * reference.
	 * 
	 * @param lineNum - Integer representing the line number
	 * @param security - Reference to the Security Actor
	 */
	public BodyScanActor(int lineNum, ActorRef security) {
		this.lineNum = lineNum;
		this.security = security;
	}
	
	/**
	 * onReceive processes messages sent to the BodyScanActor. If the message
	 * is a passenger then the BodyScanActor will scan the passenger. If the message is
	 * the end of day message then the BodyScanActor will propagate it to the system.
	 * 
	 * @param message - Passenger or EndOfDay message
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			int result = rand.nextInt(101);
			System.out.println("---Received Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Queue Actor "+lineNum+", To: Body Scan Actor "+lineNum+"---");
			System.out.println("\nBody Scan Actor "+lineNum+" is scanning " + ((Messages.Passenger)message).getPassengerId() + ".\n");

			//Put some arbitrary time in for processing
			try {
				Thread.sleep(delayTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//Passenger did not pass
			if(result <= 20){
				System.out.println("\nPassenger " + ((Messages.Passenger)message).getPassengerId() + " failed the body inspection.\n");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Body Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Passenger)message).getPassengerId(), false, 1), self());
			}

			else{
				System.out.println("\nPassenger " + ((Messages.Passenger)message).getPassengerId() + " passed the body inspection.\n");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Body Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Passenger)message).getPassengerId(), true, 1), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: Queue Actor "+lineNum+", To: Body Scan Actor "+lineNum+"---");
			System.out.println("---Sent Message: End of Day, From: Body Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
			security.tell((Messages.EndOfDay)message, self());
			
		}
		
	}

}
