import java.util.Random;

import akka.actor.UntypedActor;


public class BagScanActor extends UntypedActor{

	private Random rand = new Random();
	
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Bag){
			int result = rand.nextInt(101);

			System.out.println("Bag Scan Actor received a Bag message.");

			//Bag did not pass
			if(result <= 20){
				System.out.println("Bag failed inspection.");
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
