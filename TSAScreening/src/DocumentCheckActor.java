import java.util.ArrayList;
import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class DocumentCheckActor extends UntypedActor{

	private Random rand = new Random();
	private ArrayList<ActorRef> queues;
	private int nextLine = 0;
	private int numLines;
	
	public DocumentCheckActor(ArrayList<ActorRef> q){
		queues = q;
		numLines = queues.size();
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
		if(message instanceof Messages.Passenger){
			int result = rand.nextInt(101);
			System.out.println("---Received Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Main, To: Document Check Actor---");
			System.out.println("Document Check Actor is processing Passenger " + ((Messages.Passenger)message).getPassengerId());
			
			//Passenger did not pass
			if(result <= 20){
				System.out.println("Passenger "+ ((Messages.Passenger)message).getPassengerId() +  " did not pass document check.");
			}
			
			else{
				//Circular indexing for accessing the next line
				if((nextLine+1) == numLines) {
					nextLine = 0;
				} else {
					nextLine++;
				}
				System.out.println("---Sent Message: Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Document Check Actor, To: Queue Actor "+nextLine+"---");
				queues.get(nextLine).tell((Messages.Passenger)message, self());
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
