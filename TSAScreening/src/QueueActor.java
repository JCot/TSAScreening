import java.util.ArrayList;

import akka.actor.UntypedActor;

public class QueueActor extends UntypedActor{

	private ArrayList<Messages.Passenger> passengers = new ArrayList<Messages.Passenger>();
	private boolean dayOver = false;
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			passengers.add((Messages.Passenger)message);
			
			//TODO send message w/ bag to BagCheckActor
		}
		
		if(message instanceof Messages.BodyScanReady){
			if(!dayOver || passengers.size() != 0){
				//TODO send Passenger message to BodyScanActor
			}
			
			else{
				//TODO send EndOfDay to BodyScanActor
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			dayOver = true;
		}
		
	}

}
