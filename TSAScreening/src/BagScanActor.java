import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class BagScanActor extends UntypedActor{

	private Random rand = new Random();
	private int lineNum;
	private ActorRef security;
	
	public BagScanActor(int lineNum, ActorRef security) {
		this.lineNum = lineNum;
		this.security = security;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Bag){
			int result = rand.nextInt(101);
			System.out.println("---Received Message: Bag, From: Queue Actor "+lineNum+", To: Bag Scan Actor "+lineNum+"---");
			System.out.println("Bag Scan Actor "+lineNum+" is scanning "+ ((Messages.Bag)message).getPassengerId() + "'s bag.");

			//Bag did not pass
			if(result <= 20){
				System.out.println("Passenger " + ((Messages.Bag)message).getPassengerId() + "'s bag failed inspection.");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Bag)message).getPassengerId() + ", From: Bag Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Bag)message).getPassengerId(), false, 0), self());
			}

			else{
				System.out.println("Passenger " + ((Messages.Bag)message).getPassengerId() + "'s bag passed inspection.");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Bag)message).getPassengerId() + ", From: Bag Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Bag)message).getPassengerId(), true, 0), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: Queue Actor "+lineNum+", To: Bag Scan Actor "+lineNum+"---");
			System.out.println("---Sent Message: End of Day, From: Bag Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
			security.tell((Messages.EndOfDay)message, self());
			((ActorRef) this.self()).stop();
		}
		
	}

}
