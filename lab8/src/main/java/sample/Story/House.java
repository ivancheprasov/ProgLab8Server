package sample.Story;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class House implements InteractWithThings,Comparable<InteractWithThings>, Serializable {
    private static final long serialVersionUID = 6885365571214696713L;
	private String name;
	private State state;
	private boolean openness=false;
	private int cost;
	private LocalDateTime date;
	private Location location;
	private String owner;
	public House(String name){
		this.name=name;
		this.date= LocalDateTime.now();
	}
	@Override
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	public House(String name,int x,int y){
		this.name=name;
		this.location=new Location(x,y);
		this.date=LocalDateTime.now();
	}
	public House(String name, State state, Location location, int cost, boolean openness){
		this.name=name;
		this.state=state;
		this.location=location;
		this.cost=cost;
		this.openness=openness;
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
		if(getOpenness())return "+";
		return "-";
	}
	public String key(){
		return state.toString()+cost+name+location.getX()+location.getY()+openness;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(int x, int y){
		this.location=new Location(x,y);
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public LocalDateTime getTime() {
		return date;
	}
	public void setTime(LocalDateTime date) {
		this.date = date;
	}

	public String getThing(){
		return this.name;
	}
	public State getState(){
		return this.state;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof House)) return false;
		House house = (House) o;
		return openness == house.openness &&
				cost == house.cost &&
				name.equals(house.name) &&
				state == house.state &&
				Objects.equals(location, house.location);
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, state, openness, cost, location);
	}

	public void setState(State state){
		if ((state.equals(State.BAD))||(state.equals(State.GOOD))){
			this.state=state;
		}else{
			System.out.println("ОШИБКА. Некорректно задано качество получившейся вещи (facepalm, поставлю его как GOOD, только ради тебя).");
			this.state= State.GOOD;
		}
	}
	public boolean getOpenness(){
		return this.openness;
	}
	public void setOpenness(){
			this.openness=!this.openness;
	}
	public void setOpenness(boolean openness){
		this.openness=openness;
	}
	public String place(State state){
		switch(state){
			case GOOD:
				if (getOpenness()){
					return ", всё получилось, "+getThing()+" стал открытым.";
				}else{
					return ", всё получилось, "+getThing()+" стал закрытым.";
				}
			case BAD:
				return ", ничего не получилось, так как наш персонаж забыл, что в каталажке нельзя свободно взаимодействовать с "+getThing()+".";
			default:
				return "";
		}
	}
	public void deadlyThing(){
		System.out.println(getThing()+" находится в таком положении, что об него обязательно кто-то споткнётся или ударится о него.");
	}

    public int compareTo(InteractWithThings thing){
        return this.getCost()-thing.getCost();
    }
    public int compare(InteractWithThings thing1, InteractWithThings thing2 ){
        return thing1.compareTo(thing2);
    }
}

