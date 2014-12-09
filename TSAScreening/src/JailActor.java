import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.UntypedActor;


public class JailActor extends UntypedActor{

	private ArrayList<Messages.Passenger> jailedPassengers = new ArrayList<Messages.Passenger>();
	private int numLines;
	private int numSecurityDone = 0;
	
	public JailActor(int numLines){
		this.numLines = numLines; 
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			System.out.println("Jail Actor received a Passenger message.");
			System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " is sent to jail.");
			
			jailedPassengers.add((Messages.Passenger)message);
		}
		
		if(message instanceof Messages.EndOfDay){
			//If this was the last shutdown message
			//Otherwise wait for eveything to shutdown
			if((numSecurityDone+1) == numLines) {
				System.out.println("Jail Actor has received an End of Day message.");
				System.out.println("Jail Actor sends passengers in jail to permanent detention.");
				
				for(int i = 0; i < jailedPassengers.size(); i++){
					System.out.println("Passenger " + jailedPassengers.get(i).getPassengerId() + " is sent to pernament detention.");
				}
				Actors.registry().shutdownAll();
			} else {
				numSecurityDone++;
			}
		}
		
	}

}
