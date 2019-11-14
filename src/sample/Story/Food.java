package sample.Story;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Food implements InteractWithThings,Comparable<InteractWithThings>,Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	private String name;
	private State state;
	private int cost;
	private LocalDateTime date;
	private Location location;
	private String owner;
	public Food(String name){
		this.name=name;
		this.date=LocalDateTime.now();
	}
	public Food(String name,int x,int y){
		this.name=name;
		this.location=new Location(x,y);
		this.date=LocalDateTime.now();
	}
	@Override
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Food(String name, State state, Location location, int cost){
		this.name=name;
		this.state=state;
		this.location=location;
		this.cost=cost;
		this.date=LocalDateTime.now();
	}
	public String getState1(){
		switch (getState()){
			case GOOD:
				return "GOOD";
			case BAD:
				return "BAD";
		}
		return null;
	}
	public int getX(){
		return this.location.getX();
	}
	public int getY(){
		return this.location.getY();
	}
	public String getOptional(){
		return "";
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Food)) return false;
		Food food = (Food) o;
		return cost == food.cost &&
				name.equals(food.name) &&
				state == food.state &&
				Objects.equals(location, food.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, state, cost, location);
	}
	public String key(){
		return state.toString()+cost+name+location.getX()+location.getY();
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(int x, int y){
		this.location=new Location(x,y);
	}

	public LocalDateTime getTime() {
		return date;
	}
	public void setTime(LocalDateTime date) {
		this.date = date;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setState(State state){
		if ((state.equals(State.BAD))||(state.equals(State.GOOD))){
			this.state=state;
		}else{
			System.out.println("ОШИБКА. Некорректно задано качество получившейся вещи (facepalm, поставлю его как GOOD, только ради тебя).");
			this.state= State.GOOD;
		}
	}
	public String getThing(){
		return this.name;
	}
	public State getState(){
		return this.state;
	}
	public void deadlyThing() {
		System.out.println(getThing()+" издаёт удушливый запах.");
	}
	public String place(State state){
		switch(state){
			case BOIL:
				if (getState().equals(State.GOOD)){
					return " в жестянках из-под консервов.";
				}else{
					return " в самодельной, хлипкой кострюле.";
				}
			case BAKE:
				if (getState().equals(State.GOOD)){
					return " в золе.";
				}else{
					return " в раскалённой трубке (не ясно, почему её до сих пор не отобрали надзиратели).";
				}
			case ROAST:
				if (getState().equals(State.GOOD)){
					return ", где хочет.";
				}else{
					return ", где может, постоянно озираясь в поисках надзирателей.";
				}
			default:
				return "ОШИБКА. Некорректно задано место готовки. Oops)";
		}
	}
	public int compareTo(InteractWithThings thing){
		return this.getCost()-thing.getCost();
	}
	public int compare(InteractWithThings thing1,InteractWithThings thing2 ){
		return thing1.compareTo(thing2);
	}
}