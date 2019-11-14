package sample.Story;

import java.util.Arrays;

public abstract class Hotel{
    private static int numberOfRooms;
    private static int occupiedRooms;
    private static String[] rooms;
    public static int getNumberOfRooms() {
        return numberOfRooms;
    }
    protected static void setNumberOfRooms(int number) {
        numberOfRooms = number;
    }

    public static int getOccupiedRooms() {
        return occupiedRooms;
    }

    protected static void setOccupiedRooms(int number) {
        occupiedRooms=number>0?number: RoomAmountException.roomAmount(number);
    }
	
    public static void createHotel(int n,int o){
        setOccupiedRooms(o);
        setNumberOfRooms(n);
		try{
			rooms = new String[getNumberOfRooms()];
		}catch(NegativeArraySizeException e){ 
			setNumberOfRooms(RoomAmountException.roomAmount(getNumberOfRooms()));
			System.out.println(new RoomAmountException("Некорректно указано количество комнат (отрицательное число), теперь оно равно "+getNumberOfRooms()).getMessage());
			rooms = new String[getNumberOfRooms()];		
		}
		for(int i=0;i<getOccupiedRooms();i++){
			try{
				rooms[i]="Неизвестный постоялец";
			}catch(ArrayIndexOutOfBoundsException e){
                System.out.println(new RoomAmountException("Некорректно указано количество комнат (общее меньше занятых), теперь оно равно "+getOccupiedRooms()).getMessage());
				setNumberOfRooms(RoomAmountException.roomAmount(getNumberOfRooms()));
				rooms = new String[getOccupiedRooms()];
				rooms[i]="Неизвестный постоялец";
			}
        }
	}
    public static void nameOfGuests(){
        System.out.println("В отеле находятся "+Arrays.toString(rooms).replace("null","Комната не занята"));
    }
    public static void giveKeys(Residents a) throws OutOfRoomsException {
        if(a.getCondition().equals(Condition.DEAD)){
            System.out.println(a.getName()+" мёртв и не может поселиться в отеле.");
        }else{
        if (getNumberOfRooms()<=getOccupiedRooms()){
            throw new OutOfRoomsException("Все номера в отеле заняты, поэтому "+a.getName()+" не смог получить номер. "+a.getName()+" взбунтовался и остановил выполнение программы.");
        }else{
            rooms[getOccupiedRooms()]=a.getName();
            a.rentRoom(getOccupiedRooms()+1);
            setOccupiedRooms(getOccupiedRooms()+1);
        }
    }}

}