/**
 * Driver program for the airport security simulation using actors.
 * 
 * @author Shannon Trudeau
 * @author Andrew Popovich (ajp7560@rit.edu)
 */

import java.util.ArrayList;

import akka.actor.*;
import static akka.actor.Actors.*;

public class Main {

	/**
	 * Main method that creates all of the actors, passengers, and runs the program.
	 * 
	 * @param args - Index 0 - Total number of lines
	 *               Index 1 - Total number of passengers
	 */
	public static void main(String[] args) {
		if(args.length <= 1) {
			System.err.println("Error - Integer argument for number of lines requried.");
			System.err.println("use: java Main [numberOfLines] [numberOfPassengers]");
			System.exit(0);
		}
		
		final int numLines = Integer.parseInt(args[0]);
		final int numPass = Integer.parseInt(args[1]);
		final ArrayList<ActorRef> queues = new ArrayList<ActorRef>();
		
		//Construct each line (from queue to security station) as a unit,
		//and when all lines are created,
		//pass these in a collection of some kind to the document check.
		System.out.println("---Starting Actors---");
		//create Jail Actor
		final ActorRef jail = actorOf(
				new UntypedActorFactory(){
					public UntypedActor create(){
						return new JailActor(numLines);
					}
				}); 
		System.out.println("Starting Jail Actor");
		jail.start();
		
		//For each line 
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
			System.out.println("Starting Security Actor for line " + i);
			
			//Create BagScan
			final ActorRef bag = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new BagScanActor(lineNum, sec);
						}
					});
			bag.start();
			System.out.println("Starting Bag Scan Actor for line " + i);
			
			//create bodyScan
			final ActorRef body = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new BodyScanActor(lineNum, sec);
						}
					});
			body.start();
			System.out.println("Starting Body Scan Actor for line " + i);
			
			//create QueueActor
			final ActorRef q = actorOf(
					new UntypedActorFactory(){
						public UntypedActor create(){
							return new QueueActor(lineNum, bag, body);
						}
					});
			q.start();
			System.out.println("Starting Queue Actor for line " + i);
			
			queues.add(q);//add to queues
			
		}
		
		
		//create Document Check
		ActorRef docCheck = actorOf(
				new UntypedActorFactory(){
					public UntypedActor create(){
						return new DocumentCheckActor(queues); //pass queues to document check
					}
				});
		
		
		//Create and send passenger messages and end of day message
		ArrayList<Messages.Passenger> passengers = new ArrayList<Messages.Passenger>();
		for(int i = 0; i < numPass; i++) {
			passengers.add(new Messages.Passenger(String.valueOf(i)));
		}
		Messages.EndOfDay end = new Messages.EndOfDay();
		docCheck.start();
		System.out.println("Starting Document Check Actor");
		System.out.println("---Startup Complete---\n");
		
		for(int i = 0; i < numPass; i++) {
			System.out.println("---Sent Message: Passenger " + i + ", From: Main, To: Document Check Actor---");
			docCheck.tell(passengers.get(i));
		}
		
		//send shutdown message when all passengers are processed.
		System.out.println("---Sent Message: End of Day, From: Main, To: Document Check Actor---");
		docCheck.tell(end);
		

	}

}
