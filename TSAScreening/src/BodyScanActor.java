import java.util.Random;

import akka.actor.UntypedActor;


public class BodyScanActor extends UntypedActor{

	private Random rand = new Random();
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Passenger){
			int result = rand.nextInt(101);

			System.out.println("Body Scan Actor received a Passenger message.");

			//Passenger did not pass
			if(result <= 20){
				System.out.println("Passenger failed inspection.");
				//TODO message security actor.
			}

			else{
				//TODO message to queue actor
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			
		}
		
	}

}
