/**
 * SecurityActor is a representation of a security check in
 * an airport's security process.
 * 
 * @author Justin Cotner
 * @author Andrew Popovich (ajp7560@rit.edu)
 */

import java.util.HashMap;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;


public class SecurityActor extends UntypedActor{
	
	/** Line number for this security actor */
	private int lineNum;
	
	/** Reference to the jail actor */
	private ActorRef jail;
	
	/** Private state containing the results of each scanner for each passenger 
	 *  Key - Passenger ID, Value - array of 2 booleans, one for each scanner result
	 */
	private HashMap<String, Boolean[]> results = new HashMap<String, Boolean[]>();
	
	/** number of EndOfDay messages received. Must receive two, one 
	 *  from body scanner and one from bag scanner. 
	 */
	private int numEndOfDays = 0; 
	
	public SecurityActor(int lineNum, ActorRef jail) {
		this.lineNum = lineNum;
		this.jail = jail;
	}

	/**
	 * onReceive processes messages sent to the SecurityActor. If the message
	 * is a result for a  passenger then the SecurityActor will keep track of it
	 * and appropriately send the passenger along (to the plane or jail) once both
	 * results are in for each scanner. If the message is the end of day message then 
	 * the SecurityActor will propagate it to the system.
	 * 
	 * @param message - Passenger or EndOfDay message
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Result){
			System.out.println("---Received Message: Result - Passenger " + ((Messages.Result)message).getPassengerId() + ", From: Body Scan or Bag Scan Actors for line "+lineNum+", To: Security Actor "+lineNum+"---");
			String passengerId = ((Messages.Result) message).getPassengerId();
			
			//If the passenger is not known to the security actor
			if(!results.containsKey(passengerId)){
				Boolean[] scannerResults = new Boolean[2];
				scannerResults[((Messages.Result) message).getBagOrBody()] = ((Messages.Result) message).getResult();
				results.put(passengerId, scannerResults);
			}
			
			else{
				Boolean[] scannerResults = results.get(((Messages.Result) message).getPassengerId());
				scannerResults[((Messages.Result) message).getBagOrBody()] = ((Messages.Result) message).getResult();
				
				//Check results from both scanners for the passenger
				if(scannerResults[0] && scannerResults[1]){
					System.out.println("\nPassenger " + ((Messages.Result)message).getPassengerId() + " passed through Security Actor "+lineNum+". They board their plane.\n");
				}
				
				else{
					System.out.println("\nPassenger " + ((Messages.Result)message).getPassengerId() + " is sent to jail from Security Actor "+lineNum+".\n");
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
