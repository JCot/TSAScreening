/**
 * QueueActor is a representation of a queue, or line, in 
 * an airport's security process.
 * 
 * @author Justin Cotner
 * @author Andrew Popovich (ajp7560@rit.edu)
 */

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class QueueActor extends UntypedActor{

	/** Passengers in the line */
	private ArrayList<Messages.Passenger> passengers = new ArrayList<Messages.Passenger>();
	
	/** Line number for the line */
	private int lineNum;
	
	/** Reference to the Bag Scan Actor for the line */
	private ActorRef bagScanner;
	
	/** Reference to the Body Scan Actor for the line */
	private ActorRef bodyScanner;
	
	/** 
	 * Constructor for a QueueActor. Sets the line number, and references
	 * to scanners in the line
	 * 
	 * @param lNum - line number for this line
	 * @param bagScanner - Reference to BagScanActor for this line
	 * @param bodyScanner - Reference to BodyScanActor for this line
	 */
	public QueueActor(int lNum, ActorRef bagScanner, ActorRef bodyScanner) {
		lineNum = lNum;
		this.bagScanner = bagScanner;
		this.bodyScanner = bodyScanner;
	}
	
	/**
	 * onReceive processes messages sent to the QueueActor. If the message
	 * is a passenger then the QueueActor will place the passenger in a queue
	 * for each scanner. If the message is the end of day message then 
	 * the DocumentCheckActor will propagate it to the system.
	 * 
	 * @param message - Passenger or EndOfDay message
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			System.out.println("---Received Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + " From: Document Check Actor, To: Queue Actor "+lineNum+"---");
			System.out.println("\nPassenger " + ((Messages.Passenger)message).getPassengerId() + " has gotten in line " + lineNum + ".\n");
			passengers.add((Messages.Passenger)message);
			
			System.out.println("---Sent Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Queue Actor "+lineNum+", To: Bag Scan Actor "+lineNum+"---");
			bagScanner.tell(new Messages.Bag(((Messages.Passenger)message).getPassengerId()), self());
			
			System.out.println("---Sent Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Queue Actor "+lineNum+", To: Body Scan Actor "+lineNum+"---");
			bodyScanner.tell((Messages.Passenger)message, self());
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: Document Check Actor, To: Queue Actor "+lineNum+"---");
			System.out.println("---Sent Message: End of Day, From: Queue Actor "+lineNum+", To: Bag Scan Actor "+lineNum+"---");
			bodyScanner.tell(new Messages.EndOfDay(), self());
			System.out.println("---Sent Message: End of Day, From: Queue Actor "+lineNum+", To: Body Scan Actor "+lineNum+"---");
			bagScanner.tell(new Messages.EndOfDay(), self());
			
		}
		
	}

}
