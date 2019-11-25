package sample.Story;
import java.util.Objects;

public class Residents implements Human{
	private String name;
	private Condition condition;
	private BreathTechnique technique= BreathTechnique.NOSE;
	public Residents(String name){
		this.name=name;
		this.condition=Condition.HEALTHY;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Residents residents = (Residents) o;
		return condition == residents.condition;
	}
	@Override
	public int hashCode() {
		return Objects.hash(condition);
	}
	public static void compareResidents(Residents a, Residents b){
		if (a.equals(b)){
			System.out.println("У местных "+a.getName()+" и "+b.getName()+" одинаковые показатели здоровья.");
		} else{
			System.out.println("У местных "+a.getName()+" и "+b.getName()+" разные показатели здоровья.");
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
						this.condition=Condition.HEALTHY;
						break;
					case ILL:
						if (this.condition.equals(Condition.HEALTHY)){
							this.condition=Condition.ILL;
						}else{
							this.condition=Condition.DEAD;
						}
						break;
				}
			}analyze();
		}
	public String getName(){
		return this.name;
	}
	public Condition getCondition(){
			return this.condition;
	}
	public void analyze(){
		 switch(getCondition()){
			case HEALTHY: 
				System.out.println(getName()+" чувствует себя нормально.");
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
	protected void feelFree() {
        if (getCondition().equals(Condition.DEAD)) {
            System.out.println(getName() + " мёртв.");
        } else {
            System.out.println(getName() + " пользуется своей свободой.");
        }
    }
    public void getHurt(Object a){
	 if (getCondition().equals(Condition.DEAD)){
         System.out.println(getName()+" уже мёртв и не может получить серьёзных увечий.");
     }else{
        if(a instanceof Food) {
            System.out.println("Удушливый запах от еды " + ((Food)a).getThing() + " пагубно сказывается на здоровье " + getName() + ".");
        }else if(a instanceof Clothes){
            System.out.println("Удушливый запах от одежды " + ((Clothes)a).getThing() + " пагубно сказывается на здоровье " + getName() + ".");
        }else if(a instanceof House){
            System.out.println(getName()+" ударился о "+((House)a).getThing()+" и повредил руку");
        }
        setCondition(Condition.ILL);
	 }
    }
	public void bake(Potato a){
		a.setState(State.GOOD);
		if (getCondition().equals(Condition.DEAD)){
			System.out.println(getName()+" мёртв и не может печь "+a.getThing()+".");
		}else{
			System.out.println(getName()+" печёт "+a.getThing()+a.place(State.BAKE));
			a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
               getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", так как дышит ртом.");
            }
		}
	}
	public void boil(Soup a){
		a.setState(State.GOOD);
		if (getCondition().equals(Condition.DEAD)){
			System.out.println(getName()+" мёртв и не может варить "+a.getThing()+".");
		}else{
			System.out.println(getName()+" варит "+a.getThing()+a.place(State.BOIL));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", так как дышит ртом.");
            }
		}
	}
	public void roast(CakeLayer a){
		a.setState(State.GOOD);
		if (getCondition().equals(Condition.DEAD)){
			System.out.println(getName()+" мёртв и не может жарить "+a.getThing()+".");
		}else{
			System.out.println(getName()+" жарит "+a.getThing()+a.place(State.ROAST));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", так как дышит ртом.");
            }
		}
	}
	public void dry(Clothes a){
		a.setState(State.GOOD);
		if (getCondition().equals(Condition.DEAD)){
			System.out.println(getName()+" мёртв и не может сушить "+a.getThing()+".");
		}else{
			System.out.println(getName()+" сушит "+a.getThing()+a.place(State.DRY));
            a.deadlyThing();
            if(getTechnique().equals(BreathTechnique.NOSE)){
                getHurt(a);
            }else{
                System.out.println(getName()+" неуязвим к удушливому запаху "+a.getThing()+", так как дышит ртом.");
            }
		}
	}
	public void inspire(Object a){
		try{if(!(a instanceof Residents ||a instanceof Prisoners)){
			throw new InspirationException(this);
		}else if(a instanceof Residents){
			if (((Residents)a).getCondition().equals(Condition.DEAD)){
			    System.out.println("Мёртвого "+((Residents)a).getName()+" трудно подбодрить, "+getName()+" делал всё, что мог.");
                System.out.println(getName()+" впал в депрессию");
			}else{
				System.out.println(getName()+" подбодрил "+((Residents)a).getName()+", сказав, что всё пройдёт.");
                ((Residents)a).setCondition(Condition.HEALTHY);
			}
		}else{
			if (((Prisoners)a).getCondition().equals(Condition.DEAD)){
				System.out.println("Мёртвого "+((Prisoners)a).getName()+" трудно подбодрить, "+getName()+" делал всё, что мог.");
                System.out.println(getName()+" впал в депрессию");
			}else{
				System.out.println(getName()+" подбодрил "+((Prisoners)a).getName()+", прислав ему весточку в каталажку и передав ценный подарок.");
                ((Prisoners)a).setCondition(Condition.HEALTHY);
			}
		}
		}catch(InspirationException e){System.out.println(e.getMessage());}
	}
	public void open(House a){
		a.setState(State.GOOD);
		a.setOpenness();
		if (getCondition().equals(Condition.DEAD)){
			System.out.println(getName()+" мёртв и не может взаимодействовать с "+a.getThing()+".");
		}else{
			System.out.println(getName()+" попытался взаимодействовать "+a.getThing()+a.place(a.getState()));
			a.deadlyThing();
		}
	}
	public class Mouth{
		private Mouth(){}
		public void startBreathing() {
			Residents.this.setCondition(Condition.HEALTHY);
			Residents.this.setTechnique(BreathTechnique.MOUTH);
		}
	}
	public class Nose{
		private Nose(){}
		public void startBreathing() {
			Residents.this.setTechnique(BreathTechnique.NOSE);
		}
	}
	public void changeBreathTechnique() {
		if(getCondition().equals(Condition.DEAD)){
			System.out.println(getName() + " мёртв и не может поменять дыхательную технику.");
		}else {
			if (getTechnique().equals(BreathTechnique.MOUTH)) {
				new Nose().startBreathing();
				System.out.println(getName() + " теперь дышит носом.");
			} else {
				new Mouth().startBreathing();
				System.out.println(getName() + " теперь дышит ртом.");
				if (getCondition().equals(Condition.ILL)) {
					System.out.println(getName() + " понемногу отдышался и пришел в себя.");
				}
			}
		}
	}
	public void rentRoom(int number){
			System.out.println(getName()+" протянул в окошечко деньги и получил жетон, на котором были выбиты цифры: "+number);
		}
	public void selfExpression(String name, Residents a){
		if(getCondition().equals(Condition.DEAD)){
			System.out.println(getName() + " мёртв и не может творить.");
		}else {
		class Art {
			Art(){
				this.startExisting();
			}
			void startExisting() {
				System.out.println(name + " наконец создали, " + name + " теперь несомненно будет радовать всех вокруг.");
			}
			void pleasure(Residents a){
				a.setCondition(Condition.HEALTHY);
				if(getCondition().equals(Condition.DEAD)){
					System.out.println("Увы, искусство не способно воскрешать мёртвых, и даже " + name +" не справится с этой задачей.");
				}else{
				System.out.println(a.getName() + " посмотрел на " + name + ", его настроение поднялось и ему стало лучше.");
				}
			}
		}
		Art something =new Art();
		something.pleasure(a);
	}}
	public void depression(){
		if(getCondition().equals(Condition.DEAD)){
			System.out.println(getName() + " мёртв и не может впасть в депрессию.");
		}else {
		Residents sadPerson=new Residents("Грустный человек"){protected void feelFree(){
			System.out.println("Грустный человек находится в глубокой депрессии, он больше не способен радоваться чему-либо, даже тому, что он не в каталжке. Всё тлен.");
		} };
		System.out.println(getName()+" видит незнакомца, который явно чем-то опечален.");
		sadPerson.feelFree();
		System.out.println(getName()+" проникся его положением и тоже расстроился.");
	}}
	public void wearPants(Pants pants, boolean action){
		if(pants.zipperExists()) {
			if (action) {
				if (pants.zipper.checkFasteness()) {
					pants.zipper.interractWithZipper(false);
					System.out.println(this.getName() + " расстегнул молнию и смог надеть "+pants.getThing()+".");
					pants.zipper.interractWithZipper(true);
				} else {
					System.out.println(this.getName() + " легко надел "+pants.getThing()+", так как молния была уже расстёгнута.");
					pants.zipper.interractWithZipper(true);
				}
				System.out.println("Чтобы не смущать других, " + this.getName() + " застегнул молнию на "+pants.getThing()+".");
			} else {
				if (pants.zipper.checkFasteness()) {
					pants.zipper.interractWithZipper(false);
					System.out.println(this.getName() + " расстегнул молнию и снял "+pants.getThing()+".");
				} else {
					System.out.println(this.getName() + " легко снял "+pants.getThing()+", так как молния была уже расстёгнута.");
				}
			}
		}else{
			if(action){
				System.out.println(this.getName()+" легко надел штаны, не прилагая лишних усилий, так как у "+pants.getThing()+" нет молнии.");
			}else{
				System.out.println(this.getName()+" легко снял штаны, не прилагая лишних усилий, так как у "+pants.getThing()+" нет молнии.");
			}
		}
	}
}