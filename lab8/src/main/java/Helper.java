import sample.Story.*;

public abstract class Helper {
    private static String[]CsvLine;
    public static State valueOf(String state){
        switch(state){
            case "GOOD":
                return State.GOOD;
            case "BAD":
                return State.BAD;
            default:
                System.out.println("Эх, ты. Поставлю состояние вещи GOOD. Только ради тебя :)");
                return State.GOOD;
        }
    }
    public static InteractWithThings objectFromCsv(String line){
        CsvLine=line.split(",");
        if (CsvLine.length<6){
            System.out.println("Недостаточное кол-во параметров объекта.");
            return null;
        }
        switch (CsvLine[0].replace("Story.","")){
            case "CakeLayer":
                CakeLayer cakeLayer = new CakeLayer(CsvLine[1]);
                return setCharacteristics(cakeLayer);
            case "Door":
                if(CsvLine.length<7){
                    System.out.println("Недостаточное кол-во аргументов");
                    return null;
                }
                Door door = new Door(CsvLine[1]);
                setCharacteristics(door);
                return setOpenness(door);
            case "Pants":
                if (CsvLine.length>6) {
                    Pants pants = new Pants(CsvLine[1], new Zipper());
                    if((!CsvLine[6].equals("true"))&&(!CsvLine[6].equals("false"))){
                        System.out.println("Некорректно задано состояние молнии.");
                        return null;
                    }
                    pants.zipper.interractWithZipper(Boolean.parseBoolean(CsvLine[6]));
                    return setCharacteristics(pants);
                } else {
                    Pants pants = new Pants(CsvLine[1]);
                    return setCharacteristics(pants);
                }
            case "Potato":
                Potato potato = new Potato(CsvLine[1]);
                return setCharacteristics(potato);
            case "Socks":
                Socks socks = new Socks(CsvLine[1]);
                return setCharacteristics(socks);
            case "Soup":
                Soup soup = new Soup(CsvLine[1]);
                return setCharacteristics(soup);
            case "Window":
                if(CsvLine.length<7){
                    System.out.println("Недостаточное кол-во аргументов");
                    return null;
                }
                Window window = new Window(CsvLine[1]);
                setCharacteristics(window);
                return setOpenness(window);
            default:
                System.out.println("Некорректно задан класс вещи.");
                return null;
        }
    }

    private static InteractWithThings setCharacteristics(InteractWithThings element) {
        boolean success=true;
        switch (CsvLine[2]){
            case "GOOD":
                element.setState(State.GOOD);
                break;
            case "BAD":
                element.setState(State.BAD);
                break;
            default:
                System.out.println("Некорректно задано состояние вещи.");
                success=false;
        }
        try{
            int cost=Integer.parseInt(CsvLine[3]);
            element.setCost(cost);
        }catch (NumberFormatException e){
            System.out.println("Неверно указана стоимость объекта");
            success=false;
        }
        try{
            element.setLocation(Integer.parseInt(CsvLine[4]),Integer.parseInt(CsvLine[5]));
        }catch (NumberFormatException e){
            System.out.println("Неверно указано местонахождение объекта");
            success=false;
        }
        if(success){
            return element;
        }else{
            return null;
        }
    }
    public static String objectToCsv(InteractWithThings element){
        String line=element.getClass().toString().replace("class ","").replace("Story.","")+","+
                element.getThing()+","+element.getState().toString().replace("State.","")+","+
                element.getCost()+","+element.getLocation().getX()+","+element.getLocation().getY();
        if(element instanceof Door ||element instanceof Window){
            if(element instanceof Door){
                Door door = (Door) element;
                line=line.concat(","+door.getOpenness());
            }else{
                Window window=(Window) element;
                line=line.concat(","+window.getOpenness());
            }
        }
        if(element instanceof Pants){
            Pants pants =(Pants) element;
            if(pants.zipperExists()){
                line=line.concat(","+pants.zipper.checkFasteness());
            }
        }
        return line;
    }
    private static InteractWithThings setOpenness(InteractWithThings element){
        if(element==null)return null;
        if((!CsvLine[6].equals("true"))&&(!CsvLine[6].equals("false")))return null;
        if(element instanceof Door){
            Door door = (Door) element;
            door.setOpenness(Boolean.parseBoolean(CsvLine[6]));
            return door;
        }else{
            Window window = (Window) element;
            window.setOpenness(Boolean.parseBoolean(CsvLine[6]));
            return window;
        }
    }
}
