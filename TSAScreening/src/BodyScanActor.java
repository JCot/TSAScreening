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
			System.out.println("---Received Message: Passenger, From: Queue Actor "+lineNum+", To: Body Scan Actor "+lineNum+"---");
			System.out.println("Body Scan Actor "+lineNum+" is scanning " + ((Messages.Passenger)message).getPassengerId() + ".");

			//Put some arbitrary time in for processing
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//Passenger did not pass
			if(result <= 20){
				System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " failed the body inspection.");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Body Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Passenger)message).getPassengerId(), false, 1), self());
			}

			else{
				System.out.println("Passenger " + ((Messages.Passenger)message).getPassengerId() + " passed the body inspection.");
				System.out.println("---Sent Message: Result - Passenger " + ((Messages.Passenger)message).getPassengerId() + ", From: Body Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
				security.tell(new Messages.Result(((Messages.Passenger)message).getPassengerId(), true, 1), self());
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			System.out.println("---Received Message: End of Day, From: Queue Actor "+lineNum+", To: Body Scan Actor "+lineNum+"---");
			System.out.println("---Sent Message: End of Day, From: Body Scan Actor "+lineNum+", To: Security Actor "+lineNum+"---");
			security.tell((Messages.EndOfDay)message, self());
			
		}
		
	}

}
