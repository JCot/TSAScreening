import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class QueueActor extends UntypedActor{

	private ArrayList<Messages.Passenger> passengers = new ArrayList<Messages.Passenger>();
	private boolean dayOver = false;
	private int lineNum;
	private ActorRef bagScanner;
	private ActorRef bodyScanner;
	
	public QueueActor(int lNum, ActorRef bagScanner, ActorRef bodyScanner) {
		lineNum = lNum;
		this.bagScanner = bagScanner;
		this.bodyScanner = bodyScanner;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " has gotten in line " + lineNum + ".");
			passengers.add((Messages.Passenger)message);
			
			bagScanner.tell(new Messages.Bag(((Messages.Passenger)message).getPassengerId()), self());
			
			bodyScanner.tell((Messages.Passenger)message, self());
		}
		
		//BodyScanReady requires a circular dependency that's hard to create/start
		//so we could just rely on the BodyScan message queue to hold passengers
		//until it's ready for the next one
		if(message instanceof Messages.BodyScanReady){
			System.out.println("Body Scan Ready received");
			if(!dayOver || passengers.size() != 0){
				//Resend the BodyScanready message??
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
