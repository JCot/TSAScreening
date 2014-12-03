/**
 * 
 * @author Justin
 * 
 * Contains the static classes for the different messages
 * sent between actors.
 */

public class Messages {
	
	static class Passenger{
		
		private final String id;
		
		public Passenger(String name){
			this.id = name;
		}
		
		public String getPassengerId(){
			return id;
		}
		
	}
	
	static class EndOfDay{}
	
	static class BagScanReady{}
	
	static class Bag{
		private final String passengerId;
		
		public Bag(String passengerId){
			this.passengerId = passengerId;
		}
		
		public String getPassengerId(){
			return passengerId;
		}
	}
	
	static class Result{
		private final String passengerId;
		private final boolean result;
		private final int bagOrBody; // 0 for bag, 1 for body
		
		public Result(String passengerId, boolean result, int bagOrBody){
			this.passengerId = passengerId;
			this.result = result;
			this.bagOrBody = bagOrBody;
		}
		
		public String getPassengerId(){
			return passengerId;
		}
		
		public boolean getResult(){
			return result;
		}
		
		public int getBagOrBody(){
			return bagOrBody;
		}
	}
	
	static class BodyScanReady{}
}
