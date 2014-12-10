/**
 * DocumentCheckActor is a representation of a document checker in 
 * an airport's security process.
 * 
 * @author Justin Cotner
 * @author Andrew Popovich (ajp7560@rit.edu)
 */

import java.util.ArrayList;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class DocumentCheckActor extends UntypedActor{

	/** Random utility object to create random numbers */
	private Random rand = new Random();
	
	/** List that contains all of the QueueActors */
	private ArrayList<ActorRef> queues;
	
	/** Index to queues for the next queue a passenger will enter */
	private int nextLine = 0;
	
	/** Integer for the total number of lines */
	private int numLines;
	
	/** 
	 * Constructor for a DocumentCheckActor. Sets the queues that the
	 * DocumentCheckActor knows about.
	 * 
	 * @param q - queues to be set within this actor
	 */
	public DocumentCheckActor(ArrayList<ActorRef> q){
		queues = q;
		numLines = queues.size();
	}
	
	/**
	 * onReceive processes messages sent to the DocumentCheckActor. If the message
	 * is a passenger then the DocumentCheckActor will place the passenger in a queue
	 * if they pass the check. If the message is the end of day message then 
	 * the DocumentCheckActor will propagate it to the system.
	 * 
	 * @param message - Passenger or EndOfDay message
	 */
	@Override
	public void onReceive(Object message) throws Exception {		
		if(message instanceof Messages.Passenger){
			int result = rand.nextInt(101);
			System.out.println("---Received Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Main, To: Document Check Actor---");
			System.out.println("\nDocument Check Actor is processing Passenger " + ((Messages.Passenger)message).getPassengerId() + ".\n");
			
			//Passenger did not pass
			if(result <= 20){
				System.out.println("\nPassenger "+ ((Messages.Passenger)message).getPassengerId() +  " did not pass document check.\n");
			}
			
			else{
				System.out.println("---Sent Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Document Check Actor, To: Queue Actor "+nextLine+"---");
				queues.get(nextLine).tell((Messages.Passenger)message, self());
				
				//Circular indexing for accessing the next line
				if((nextLine+1) == numLines) {
					nextLine = 0;
				} else {
					nextLine++;
				}
			}
			
			
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: Main, To: Document Check Actor---");
			for(int i = 0; i < queues.size(); i++){
				System.out.println("---Sent Message: End of Day, From: Document Check Actor, To: Queue Actor "+i+"---");
				queues.get(i).tell((Messages.EndOfDay)message, self());
			}
			
		}
	}

}
