package sample.Story;
public class InspirationException extends Exception{
	public InspirationException(Object o){
		super(((Residents)o).getName()+" похоже сошёл с ума, так как пытается подбодрить вещь.");
	}
}