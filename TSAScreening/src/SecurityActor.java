import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class SecurityActor extends UntypedActor{
	
	private int lineNum;
	private ActorRef jail;
	private HashMap<String, Boolean[]> results = new HashMap<String, Boolean[]>();
	private int numEndOfDays = 0; //number of EndOfDay messages received. Must receive two, one from body scanner and one from bag scanner.
	

	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Result){
			String passengerId = ((Messages.Result) message).getPassengerId();
			
			if(!results.containsKey(passengerId)){
				Boolean[] scannerResults = new Boolean[2];
				scannerResults[((Messages.Result) message).getBagOrBody()] = ((Messages.Result) message).getResult();
				results.put(passengerId, scannerResults);
			}
			
			else{
				Boolean[] scannerResults = results.get(((Messages.Result) message).getPassengerId());
				scannerResults[((Messages.Result) message).getBagOrBody()] = ((Messages.Result) message).getResult();
				
				if(scannerResults[0] && scannerResults[1]){
					System.out.println("Passenger " + ((Messages.Result)message).getPassengerId() + " passed the body and bag scan. They board their plane.");
				}
				
				else{
					System.out.println("Passenger " + ((Messages.Result)message).getPassengerId() + " is sent to jail");
					jail.tell(new Messages.Passenger(((Messages.Result)message).getPassengerId()), self());
				}
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			numEndOfDays++;
			
			if(numEndOfDays == 2){
				jail.tell((Messages.EndOfDay)message, self());
			}
		}
		
	}

}
