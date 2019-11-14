package sample.Story;
import java.io.Serializable;
import java.util.Objects;

public class Pants extends Clothes implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public Zipper zipper=null;
	public Pants(String name){
		super(name);
	}
	public Pants(String name, Zipper zipper){
		super(name);
		this.zipper=zipper;
		this.zipper.interractWithZipper(true);
	}
	public Pants(String name, State state, Location location, int cost){
		super(name,state,location,cost);
		this.zipper=new Zipper();
		this.zipper.interractWithZipper(true);
	}
	public Pants(String name, State state, Location location, int cost,boolean zipper){
		super(name,state,location,cost);
		this.zipper=new Zipper();
		this.zipper.interractWithZipper(zipper);
	}
	public String getOptional(){
		if(this.zipper.checkFasteness())return "+";
		return "-";
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pants)) return false;
		if (!super.equals(o)) return false;
		Pants pants = (Pants) o;
		if(this.zipperExists()){
			return zipper.checkFasteness()==pants.zipper.checkFasteness();
		}else{
			return true;
		}
	}
	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), zipper);
	}
	public String key(){
		return getState().toString()+getCost()+getThing()+getLocation().getX()+getLocation().getY()+zipper.checkFasteness();
	}
	public boolean zipperExists(){
		if(this.zipper==null){
			return false;
		}else{
			return true;
		}
	}
}