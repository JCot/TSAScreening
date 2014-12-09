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
			//TODO need to make queue and add bag to queue?
			int result = rand.nextInt(101);

			System.out.println("Bag Scan Actor received a Bag message.");

			//Bag did not pass
			if(result <= 20){
				System.out.println("Passenger " + ((Messages.Bag)message).getPassengerId() + "'s bag failed inspection.");
				security.tell(new Messages.Result(((Messages.Bag)message).getPassengerId(), false, 0), self());
			}

			else{
				System.out.println("Passenger " + ((Messages.Bag)message).getPassengerId() + "'s bag passed inspection.");
				security.tell(new Messages.Result(((Messages.Bag)message).getPassengerId(), true, 0), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			security.tell((Messages.EndOfDay)message, self());
		}
		
	}

}
