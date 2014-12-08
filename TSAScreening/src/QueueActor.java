import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class QueueActor extends UntypedActor{

	private ArrayList<Messages.Passenger> passengers = new ArrayList<Messages.Passenger>();
	private boolean dayOver = false;
	private int lineNum;
	private ActorRef bagScanner;
	private ActorRef bodyScanner;
	
	public QueueActor(int lNum) {
		lineNum = lNum;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " has gotten in line " + lineNum + ".");
			passengers.add((Messages.Passenger)message);
			
			bagScanner.tell(new Messages.Bag(((Messages.Passenger)message).getPassengerId()), self());
		}
		
		if(message instanceof Messages.BodyScanReady){
			if(!dayOver || passengers.size() != 0){
				bodyScanner.tell((Messages.Passenger)message, self());
			}
			
			else{
				bodyScanner.tell(new Messages.EndOfDay(), self());
				bagScanner.tell(new Messages.EndOfDay(), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			dayOver = true;
		}
		
	}

}
