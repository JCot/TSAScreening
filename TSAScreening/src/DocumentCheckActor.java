import java.util.Random;

import akka.actor.UntypedActor;

public class DocumentCheckActor extends UntypedActor{

	private Random rand = new Random();
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
		if(message instanceof Messages.Passenger){
			int result = rand.nextInt(101);
			
			System.out.println("Document Check Actor received a Passenger message.");
			
			//Passenger did not pass
			if(result <= 20){
				System.out.println("Passenger did not pass document check.");
			}
			
			else{
				//TODO message to queue actor
			}
			
			
		}
		
		if(message instanceof Messages.EndOfDay){
			
		}
	}

}
