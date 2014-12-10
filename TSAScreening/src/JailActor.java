/**
 * JailActor is a representation of jail in 
 * an airport's security process.
 * 
 * @author Justin Cotner
 * @author Andrew Popovich (ajp7560@rit.edu)
 */

import java.util.ArrayList;

import akka.actor.Actors;
import akka.actor.UntypedActor;


public class JailActor extends UntypedActor{

	/** List of all passengers sent to jail */
	private ArrayList<Messages.Passenger> jailedPassengers = new ArrayList<Messages.Passenger>();
	
	/** Total number of lines for passengers to go through */
	private int numLines;
	
	/** Count for the number of security actors sending the EndOfDay message */
	private int numSecurityDone = 0;
	
	/** 
	 * Constructor for a JailActor. Sets the total number of lines.
	 * 
	 * @param numLines - Total number of lines
	 */
	public JailActor(int numLines){
		this.numLines = numLines; 
	}
	
	/**
	 * onReceive processes messages sent to the JailActor. If the message
	 * is a passenger then the JailActor will place the passenger in jail. 
	 * If the message is the end of day message then the JailActor will 
	 * shutdown the system once everything is done.
	 * 
	 * @param message - Passenger or EndOfDay message
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			System.out.println("---Received Message: Passenger "+((Messages.Passenger)message).getPassengerId()+", From: a Security Actor, To: Jail Actor---");
			System.out.println("\nJail Actor sends Passenger "+((Messages.Passenger) message).getPassengerId()+" to temporary holding.\n");
			
			jailedPassengers.add((Messages.Passenger)message);
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: a Security Actor, To: Jail Actor---");
			
			//If this was the last shutdown message
			//Otherwise wait for everything to shutdown
			if((numSecurityDone+1) == numLines) {
				System.out.println("\nJail Actor has received an End of Day message.");
				System.out.println("Jail Actor sends passengers in jail to permanent detention.");
				
				for(int i = 0; i < jailedPassengers.size(); i++){
					System.out.println("Passenger " + jailedPassengers.get(i).getPassengerId() + " is sent to permanent detention.");
				}
				System.out.println("\n---Shutting Down All Actors---");
				Actors.registry().shutdownAll();
			} else {
				numSecurityDone++;
			}
		}
		
	}

}
