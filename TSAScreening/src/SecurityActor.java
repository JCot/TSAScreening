import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class SecurityActor extends UntypedActor{
	
	private int lineNum;
	private ActorRef jail;
	private HashMap<String, Boolean[]> results = new HashMap<String, Boolean[]>();
	private int numEndOfDays = 0; //number of EndOfDay messages received. Must receive two, one from body scanner and one from bag scanner.
	
	public SecurityActor(int lineNum, ActorRef jail) {
		this.lineNum = lineNum;
		this.jail = jail;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Result){
			System.out.println("---Received Message: Result - Passenger " + ((Messages.Result)message).getPassengerId() + ", From: Body Scan or Bag Scan Actors for line "+lineNum+", To: Security Actor "+lineNum+"---");
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
					System.out.println("Passenger " + ((Messages.Result)message).getPassengerId() + " passed through Security Actor "+lineNum+". They board their plane.");
				}
				
				else{
					System.out.println("Passenger " + ((Messages.Result)message).getPassengerId() + " is sent to jail from Security Actor "+lineNum+".");
					System.out.println("---Sent Message: Result - Passenger " + ((Messages.Result)message).getPassengerId() + ", From: Security Actor "+lineNum+", To: Jail Actor---");
					jail.tell(new Messages.Passenger(((Messages.Result)message).getPassengerId()), self());
				}
			}
		}
		
		if(message instanceof Messages.EndOfDay){
			numEndOfDays++;
			System.out.println("---Received Message: End of Day, From: Body Scan or Bag Scan Actors for line "+lineNum+", To: Security Actor "+lineNum+"---");
			if(numEndOfDays == 2){
				System.out.println("---Sent Message: End of Day, From: Security Actor "+lineNum+", To: Jail Actor---");
				jail.tell((Messages.EndOfDay)message, self());
			}
			
		}
		
	}

}
