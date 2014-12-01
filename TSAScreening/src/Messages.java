/**
 * 
 * @author Justin
 * 
 * Contains the static classes for the different messages
 * sent between actors.
 */

public class Messages {
	
	static class Passenger{
		
		private final String name;
		
		public Passenger(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
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
		
	}
	
	static class BodyScanReady{}
}
