package sample.Story;
import java.io.Serializable;
import java.util.Objects;

public class CakeLayer extends Food implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public CakeLayer(String name){
		super(name);
	}
	public CakeLayer(String name, State state, Location location, int cost){
		super(name,state,location,cost);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CakeLayer)) return false;
		CakeLayer cakeLayer = (CakeLayer) o;
		return  getCost() == cakeLayer.getCost() &&
				getThing().equals(cakeLayer.getThing()) &&
				getState() == cakeLayer.getState() &&
				Objects.equals(getLocation(), cakeLayer.getLocation());
	}
	@Override
	public int hashCode() {
		return Objects.hash(getThing(), getState(), getCost(), getLocation());
	}
}