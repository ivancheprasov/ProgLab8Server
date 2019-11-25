package sample.Story;
import java.io.Serializable;
import java.util.Objects;

public class Door extends House implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public Door(String name){
		super(name);
	}
	public Door(String name, State state, Location location, int cost, boolean openness){
		super(name,state,location,cost,openness);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Door)) return false;
		Door door = (Door) o;
		return getOpenness() == door.getOpenness() &&
				getCost() == door.getCost() &&
				getThing().equals(door.getThing()) &&
				getState() == door.getState() &&
				Objects.equals(getLocation(), door.getLocation());
	}
	@Override
	public int hashCode() {
		return Objects.hash(getThing(), getState(), getOpenness(), getCost(), getLocation());
	}
}