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
			System.out.println("---Received Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + " From: Document Check Actor, To: Queue Actor "+lineNum+"---");
			System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " has gotten in line " + lineNum + ".");
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
