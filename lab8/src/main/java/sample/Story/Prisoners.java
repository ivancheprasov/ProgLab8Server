package sample.Story;
import java.util.Objects;

public class Prisoners implements Human{
	private Condition condition;
	private String name;
	private BreathTechnique technique= BreathTechnique.NOSE;
	public Prisoners(String name){
		this.name=name;
		this.condition= Condition.HEALTHY;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Prisoners prisoners = (Prisoners) o;
		return condition == prisoners.condition;
	}
	@Override
	public int hashCode() {
		return Objects.hash(condition);
	}
	public static void comparePrisoners(Prisoners a, Prisoners b){
		if (a.equals(b)){
			System.out.println("У заключённых "+a.getName()+" и "+b.getName()+" одинаковые показатели здоровья.");
		} else{
			System.out.println("У заключённых "+a.getName()+" и "+b.getName()+" разные показатели здоровья.");
		}
	}
	public BreathTechnique getTechnique() {
		return technique;
	}

	public void setTechnique(BreathTechnique technique) {
		this.technique = technique;
	}
	public void setCondition(Condition c){
		if (this.condition.equals(Condition.DEAD)==false){
			switch(c){
				case HEALTHY:
					this.condition= Condition.HEALTHY;
					break;
				case ILL:
					if (this.condition.equals(Condition.HEALTHY)){
						this.condition= Condition.ILL;
					}else{
						this.condition= Condition.DEAD;
					}
					break;
			}
		}analyze();
	}
	public Condition getCondition(){
		return this.condition;
	}
	public String getName(){
		return this.name;
	}
	public void analyze(){
		 switch(getCondition()){
			case HEALTHY: 
				System.out.println(getName()+" чувствует себя нормально, по меркам каталажки, конечно.");
				break;
			case ILL:
				System.out.println("У "+getName()+" перехватило дыхание и помутилось в глазах.");
				System.out.println("Также, почувтсвовав головокружение, "+getName()+" зашатался и принялся хвататься руками за стенку.");
				System.out.println(getName()+" побледнел.");
				break;
			case DEAD:
				System.out.println(getName()+" умер. PRESS F TO PAY RESPECTS.");
				break;
		}
	}
	public void languish(){
			 System.out.println(getName()+" томится в каталажке.");
	}
    public void getHurt(Object a){
        if (getCondition().equals(Condition.DEAD)){
            System.out.println(getName()+" уже мёртв и не может получить серьёзных увечий в каталажке.");
        }else{
            if(a instanceof Food) {
                System.out.println("Удушливый запах от еды " + ((Food)a).getThing() + " пагубно сказывается на здоровье заключённого " + getName() + ".");
            }else if(a instanceof Clothes){
                System.out.println("Удушливый запах от одежды " + ((Clothes)a).getThing() + " пагубно сказывается на здоровье заключённого " + getName() + ".");
            }else if(a instanceof House){
                System.out.println(getName()+" ударился о "+((House)a).getThing()+" и повредил руку");
            }
            setCondition(Condition.ILL);
        }
    }
    public void bake(Potato a){
        a.setState(State.GOOD);
        if (getCondition().equals(Condition.DEAD)){
            System.out.println(getName()+" мёртв и не может скрытно печь "+a.getThing()+".");
        }else{
            System.out.println(getName()+" скрытно печёт "+a.getThing()+a.place(State.BAKE));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", который царит в каталажке, так как дышит ртом.");
            }
        }
    }
    public void boil(Soup a){
        a.setState(State.GOOD);
        if (getCondition().equals(Condition.DEAD)){
            System.out.println(getName()+" мёртв и не может скрытно варить "+a.getThing()+".");
        }else{
            System.out.println(getName()+" скрытно варит "+a.getThing()+a.place(State.BOIL));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", который царит в каталажке, так как дышит ртом.");
            }
        }
    }
    public void roast(CakeLayer a){
        a.setState(State.GOOD);
        if (getCondition().equals(Condition.DEAD)){
            System.out.println(getName()+" мёртв и не может скрытно жарить "+a.getThing()+".");
        }else{
            System.out.println(getName()+" скрытно жарит "+a.getThing()+a.place(State.ROAST));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", который царит в каталажке, так как дышит ртом.");
            }
        }
    }
    public void dry(Clothes a){
        a.setState(State.GOOD);
        if (getCondition().equals(Condition.DEAD)){
            System.out.println(getName()+" мёртв и не может скрытно сушить "+a.getThing()+".");
        }else{
            System.out.println(getName()+" скрытно сушит "+a.getThing()+a.place(State.DRY));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", который царит в каталажке, так как дышит ртом.");
            }
        }
    }
	public void inspire(Prisoners a){
		a.setCondition(Condition.HEALTHY);
		if (a.getCondition().equals(Condition.DEAD)){
			System.out.println("Мёртвого "+a.getName()+" трудно подбодрить, "+getName()+" делал всё, что мог.");
		}else{
			System.out.println(getName()+" подбодрил заключённого "+a.getName()+", сказав, что всё пройдёт за исключением каталажки.");
		}
	}
	public void open(House a){
		a.setState(State.BAD);
		if (getCondition().equals(Condition.DEAD)){
			System.out.println(getName()+" мёртв и не может взаимодействовать с "+a.getThing()+".");
		}else{
			System.out.println(getName()+" попытался взаимодействовать "+a.getThing()+a.place(a.getState()));
		}
	}
	class Mouth{
		private Mouth(){}
		public void startBreathing() {
			Prisoners.this.setCondition(Condition.HEALTHY);
			Prisoners.this.setTechnique(BreathTechnique.MOUTH);
		}
	}
	class Nose{
		private Nose(){}
		public void startBreathing() {
			Prisoners.this.setTechnique(BreathTechnique.NOSE);
		}
	}
	public void changeBreathTechnique() {
		if(getCondition().equals(Condition.DEAD)){
			System.out.println(getName() + " мёртв и не может поменять дыхательную технику.");
		}else {
			if (getTechnique().equals(BreathTechnique.MOUTH)) {
				new Nose().startBreathing();
				System.out.println(getName() + " теперь дышит носом в каталажке.");
			} else {
				new Mouth().startBreathing();
				System.out.println(getName() + " теперь дышит ртом в каталажке.");
				if (getCondition().equals(Condition.ILL)) {
					System.out.println(getName() + " понемногу отдышался и пришел в себя.");
				}
			}
		}
	}
	public void wearPants(Pants p, boolean action){
		System.out.println("Несмотря на огромное желание заключённого "+getName()+", в каталажке нельзя просто так надевать и снимать штаны, когда вздумается... тебя могут посчитать сумасшедшим... или и того хуже. ");
	}
}