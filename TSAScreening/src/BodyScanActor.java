import java.util.Random;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class BodyScanActor extends UntypedActor{

	private Random rand = new Random();
	private int lineNum;
	private ActorRef security;
	
	public BodyScanActor(int lineNum, ActorRef security) {
		this.lineNum = lineNum;
		this.security = security;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			int result = rand.nextInt(101);

			System.out.println("Body Scan Actor received a Passenger message.");

			//Passenger did not pass
			if(result <= 20){
				System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " failed inspection.");
				security.tell(new Messages.Result(((Messages.Passenger)message).getPassengerId(), false, 1), self());
			}

			else{
				System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " passed inspection.");
				security.tell(new Messages.Result(((Messages.Passenger)message).getPassengerId(), true, 1), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			security.tell((Messages.EndOfDay)message, self());
		}
		
	}

}
