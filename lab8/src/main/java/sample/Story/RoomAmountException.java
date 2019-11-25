package sample.Story;
public class RoomAmountException extends Exception {
	public RoomAmountException(String message){
		super(message);
	}
	public static int roomAmount(int number){
		number=number<0?Math.abs(number):5;
		return number;
	}
}