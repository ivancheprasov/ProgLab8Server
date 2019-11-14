package sample.Story;
import java.io.Serializable;
import java.util.Objects;

public class Socks extends Clothes implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public Socks(String name) {
		super(name);
	}
	public Socks(String name, State state, Location location, int cost){
		super(name,state,location,cost);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Socks)) return false;
		Socks socks = (Socks) o;
		return getCost() == socks.getCost() &&
				getThing().equals(socks.getThing()) &&
				getState() == socks.getState() &&
				Objects.equals(getLocation(), socks.getLocation());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getThing(), getState(), getCost(), getLocation());
	}
}