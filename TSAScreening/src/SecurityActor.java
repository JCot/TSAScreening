import akka.actor.UntypedActor;


public class SecurityActor extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Exception {
		
		if(message instanceof Messages.Result){
			
		}
		
		if(message instanceof Messages.EndOfDay){
			
		}
		
	}

}
