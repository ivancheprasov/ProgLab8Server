package sample.Story;
public class OutOfRoomsException extends IndexOutOfBoundsException {
    public OutOfRoomsException(String message){
        super(message);
    }
}