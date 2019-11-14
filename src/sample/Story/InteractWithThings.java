package sample.Story;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface InteractWithThings extends Serializable {
	String key();
	String place(State state);
	void deadlyThing();
    int getCost();
    Location getLocation();
	String getThing();
    State getState();
	void setLocation(int x, int y);
	void setCost(int cost);
	void setState(State state);
	int compareTo(InteractWithThings thing);
	int compare(InteractWithThings thing1, InteractWithThings thing2);
	void setTime(LocalDateTime time);
	LocalDateTime getTime();
	String getState1();
	String getOptional();
	int getX();
	int getY();
	default String getType(){
		String type=this.getClass().toString().replace("class sample.Story.","");
		return type;
	}
	String getOwner();
	void setOwner(String owner);
}