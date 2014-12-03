import java.util.ArrayList;

import akka.actor.UntypedActor;


public class JailActor extends UntypedActor{

	private ArrayList<Messages.Passenger> jailedPassengers = new ArrayList<Messages.Passenger>();
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			System.out.println("Jail Actor received a Passenger message.");
			System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " is sent to jail.");
			
			jailedPassengers.add((Messages.Passenger)message);
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("Jail Actor has received an End of Day message.");
			System.out.println("Jail Actor sends passengers in jail to permanent detention.");
			
			for(int i = 0; i < jailedPassengers.size(); i++){
				System.out.println("Passenger " + jailedPassengers.get(i).getPassengerId() + " is sent to pernament detention.");
			}
		}
		
	}

}
