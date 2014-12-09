import java.util.ArrayList;

import akka.actor.*;
import static akka.actor.Actors.*;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length <= 0) {
			System.err.println("Error - Integer argument for number of lines requried.");
			System.exit(0);
		}
		final int numLines = Integer.parseInt(args[0]);
		final ArrayList<ActorRef> queues = new ArrayList<ActorRef>();
		
		//Construct each line (from queue to security station) as a unit,
		//and when all lines are created,
		//pass these in a collection of some kind to the document check.
		
		
		//create Jail Actor
		final ActorRef jail = actorOf(
				new UntypedActorFactory(){
					public UntypedActor create(){
						return new JailActor(numLines);
					}
				}); 
		jail.start();
		
		for (int i = 0; i < numLines; i++) {
			final int lineNum = i;
			
			//create securityActor
			final ActorRef sec = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new SecurityActor(lineNum, jail);
						}
					});
			sec.start();
			
			//Create BagScan
			final ActorRef bag = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new BagScanActor(lineNum, sec);
						}
					});
			bag.start();
			
			//create bodyScan
			final ActorRef body = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new BodyScanActor(lineNum, sec);
						}
					});
			body.start();
			
			//create QueueActor
			final ActorRef q = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new QueueActor(lineNum, bag, body);
						}
					});
			q.start();
			
			queues.add(q);//add to queues
			
		}
		
		//create Document Check
		ActorRef docCheck = actorOf(
				new UntypedActorFactory(){
					public UntypedActor create(){
						return new DocumentCheckActor(queues); //pass queues to document check
					}
				});
		
		
		
		//process passengers...how do you know how many passengers are in each line?
		//How many passengers do we need? What determines an "End Of Day"?
		Messages.Passenger pass = new Messages.Passenger("Pop");
		Messages.EndOfDay end = new Messages.EndOfDay();
		docCheck.start();
		docCheck.tell(pass);
		
		//send shutdown message when all passengers are processed. 
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Shutting down");
		docCheck.tell(end);
		

	}

}
