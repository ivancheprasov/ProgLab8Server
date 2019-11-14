import sample.Story.*;
import sample.Command;
import sample.LocMessage;
import sample.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Date;

public class CommandHandler {
    private InetSocketAddress clientAddress;
    private DatagramChannel datagramChannel;
    private List<InteractWithThings> myCollection;
    private Command fullCommand;
    private Thread thread;
    private String login;
    private String password;
    private String mail;
    private JavaMail javaMail;
    private Connection connection;
    private Date date;
    public CommandHandler(InetSocketAddress clientAddress, DatagramChannel udpChannel, Command command, List<InteractWithThings> myCollection, Connection connection, Date date){
        this.clientAddress=clientAddress;
        this.datagramChannel=udpChannel;
        this.fullCommand=command;
        this.myCollection=myCollection;
        this.connection=connection;
        this.date=date;
    }

    public void setCommand(Command command) {
        this.fullCommand = command;
    }

    public Command getCommand() {
        return fullCommand;
    }
    public void serve(){
        thread = new Thread(this::serveClient);
        thread.start();
    }
    public void serveClient(){
        ByteBuffer buffer =ByteBuffer.allocate(8192);
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            Response response = handleCommand();
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
            buffer.clear();
            buffer.put(byteArrayOutputStream.toByteArray());
            buffer.flip();
            datagramChannel.send(buffer, clientAddress);
        } catch (IOException e) {
            System.out.println("Ой");
        }
        thread.interrupt();
    }
    public Response handleCommand(){
            String command=fullCommand.getCommand();
            Object data = fullCommand.getData();
            login= fullCommand.getLogin();
            password=MD2.encryptThisString("Обожаю"+fullCommand.getPassword()+"Пиццу");
            switch (command){
                case "connecting":
                    return new Response("",LocMessage.CONNECTED);
                case "reg":
                    if(loginCheck()) return new Response("",LocMessage.LOGIN_USED);
                    mail=(String)data;
                    Thread thread = new Thread(this::registration);
                    thread.start();
                    return new Response("",LocMessage.REG);
                    //return "Попытка регистрации. Теперь вы можете войти, используя логин и полученный пароль.".getBytes();
            }
             Response buffer=null;
            if(passwordCheck()){
                switch(command){
                    case "log":
                        buffer=new Response(myCollection,LocMessage.LOG);
                        //message="Добро пожаловать.".getBytes();
                        break;
                    case "show":
                        buffer = show();
                        break;
                    case "add":
                        buffer = add((InteractWithThings) data);
                        break;
                    case "add_if_min":
                        buffer = add_if_min( (InteractWithThings) data);
                        break;
                    case "import":
                        buffer =importFile((String) data);
                        break;
                    case "info":
                        buffer = info();
                        break;
                    case "remove":
                        buffer = remove( (int) data);
                        break;
                    case "remove_element":
                        buffer = remove((InteractWithThings) data);
                        break;
                    /*case "help":
                        buffer = help();
                        break;*/
                    case "update":
                        buffer=update((InteractWithThings)data);
                        break;
                }
                return buffer;
            }else{
                return new Response("",LocMessage.LOG_NOT);
                //return "Вы не можете взаимодействовать с коллекцией, пока не пройдёте идентификацию.".getBytes();
            }
    }
    private boolean passwordCheck(){
        String selectData = "SELECT USER_LOGIN, USER_PASSWORD FROM USERS";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet =statement.executeQuery(selectData);
            while(resultSet.next()){
                if(!resultSet.getString("USER_LOGIN").equals(login)) continue;
                if(resultSet.getString("USER_PASSWORD").equals(password)) return true;
            }
            statement.close();
            return false;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    private boolean loginCheck(){
        String selectData ="SELECT USER_LOGIN FROM USERS";
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery(selectData);
            while (resultSet.next()){
                if(resultSet.getString("USER_LOGIN").equals(login)) return true;
            }
            statement.close();
            return false;
        }catch (SQLException e) {
            System.out.println("Не удалось выполнить запрос к БД.");
            return false;
        }
    }
    private String generatePassword(){
        StringBuilder stringBuilder=new StringBuilder();
        String s="abcdefghijklmnopkrstuvexyzABCDEFJHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int k=0;k<6;k++)  stringBuilder.append(s.toCharArray()[(int)(Math.random()*61)]);
        return stringBuilder.toString();
    }
    private void registration(){
        String purePassword=generatePassword();
        javaMail=new JavaMail(purePassword);
        javaMail.registration(mail,login);
        String secretPassword=MD2.encryptThisString("Обожаю"+purePassword+"Пиццу");
        insertUser(secretPassword);
        thread.interrupt();
    }
    private void insertUser(String password){
        String insertUser = "INSERT INTO USERS"+
                "(USER_LOGIN, USER_MAIL, USER_PASSWORD)"+" VALUES "+
                "( ?,?,?)";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(insertUser);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,mail);
            preparedStatement.setString(3,password);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Пользователь добавлен.");
        }catch(SQLException e){
            System.out.println("Не удалось добавить пользователя в список пользователей.");
        }
    }
    private Response update(InteractWithThings element){
        if(checkPresenceAndOwner(element)){
            try{
                PreparedStatement preparedStatement=connection.prepareStatement("UPDATE LOCATION SET (X,Y) =(?,?) WHERE THING=?");
                PreparedStatement preparedStatement1=connection.prepareStatement("UPDATE OBJECTS SET (OBJECT_TYPE,OBJECT_STATE,OBJECT_COST)=(?,CAST(? AS STATE),?) " +
                        "WHERE OBJECT_KEY=?");
                preparedStatement.setInt(1,element.getX());
                preparedStatement.setInt(2,element.getY());
                preparedStatement.setString(3,element.getThing()+login);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                preparedStatement1.setString(1,element.getClass().toString().replace("class sample.Story.",""));
                preparedStatement1.setString(2,element.getState().toString());
                preparedStatement1.setInt(3,element.getCost());
                preparedStatement1.setString(4,element.getThing()+login);
                preparedStatement1.executeUpdate();
                preparedStatement1.close();
                switch(element.getClass().toString().replace("class sample.Story.","")){
                    case "Pants":
                        PreparedStatement preparedStatement2=connection.prepareStatement("UPDATE ZIPPER SET (FASTENED) =(?) WHERE THING=?");
                        Pants pants=(Pants)element;
                        preparedStatement2.setBoolean(1,pants.zipper.checkFasteness());
                        preparedStatement2.setString(2,element.getThing()+login);
                        preparedStatement2.executeUpdate();
                        preparedStatement2.close();
                        break;
                    case "Door": case "Window":
                        PreparedStatement preparedStatement3=connection.prepareStatement("UPDATE OPENNESS SET (OPENED) =(?) WHERE THING=?");
                        preparedStatement3.setBoolean(1,((House) element).getOpenness());
                        preparedStatement3.setString(2,element.getThing()+login);
                        preparedStatement3.executeUpdate();
                        preparedStatement3.close();
                        break;
                }
                try{
                    Statement statement=connection.createStatement();
                    myCollection.clear();
                    ResultSet resultSet =statement.executeQuery("select * from objects " +
                            "inner join location on object_key=location.thing " +
                            "inner join zipper on object_key=zipper.thing " +
                            "inner join openness on object_key=openness.thing");
                    while(resultSet.next()){
                        InteractWithThings thing=Server.createObject(resultSet);
                        if(thing!=null){
                            myCollection.add(thing);
                        }
                     }
                }catch (SQLException e){
                    System.out.println("Не удалось начать работу с БД.");
                    System.exit(1);
                }catch (NullPointerException e){
                    System.out.println("Не удалось проинициализировать объект.");
                 }
                return new Response(element.getThing(), LocMessage.UPDATE);
            }catch (SQLException e){
                e.printStackTrace();
                return new Response("",LocMessage.COLLECTION);
            }
        }else{
            if(checkName(element)){
                return new Response(element.getThing(),LocMessage.FOREIGN);
            }else{
                return new Response(element.getThing(),LocMessage.REMOVE_NOT);
            }
        }
    }
    private void insertObject(String key,String name,String type, State state, int cost, Location location, LocalDateTime localDateTime, boolean fastened, boolean opened){
        try {
            PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO OBJECTS (OBJECT_KEY,OWNER,OBJECT_NAME,OBJECT_TYPE," +
                    "OBJECT_STATE,OBJECT_COST,OBJECT_DATE)" +
                    " VALUES (?,?,?,?,CAST(? AS STATE),?,?)");
            preparedStatement.setString(1,key);
            preparedStatement.setString(2,login);
            preparedStatement.setString(3,name);
            preparedStatement.setString(4,type);
            preparedStatement.setString(5,state.toString());
            preparedStatement.setInt(6,cost);
            preparedStatement.setObject(7,localDateTime);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            switch (type){
                case "Pants":
                    preparedStatement=connection.prepareStatement("INSERT INTO ZIPPER (THING,FASTENED) VALUES (?,?)");
                    preparedStatement.setString(1,key);
                    preparedStatement.setBoolean(2,fastened);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    preparedStatement=connection.prepareStatement("INSERT INTO OPENNESS (THING) VALUES (?)");
                    preparedStatement.setString(1,key);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    break;
                case "Window": case "Door":
                    preparedStatement=connection.prepareStatement("INSERT INTO OPENNESS (THING,OPENED) VALUES (?,?)");
                    preparedStatement.setString(1,key);
                    preparedStatement.setBoolean(2,opened);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    preparedStatement=connection.prepareStatement("INSERT INTO ZIPPER (THING) VALUES (?)");
                    preparedStatement.setString(1,key);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    break;
                default:
                    preparedStatement=connection.prepareStatement("INSERT INTO ZIPPER (THING) VALUES (?)");
                    preparedStatement.setString(1,key);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    preparedStatement=connection.prepareStatement("INSERT INTO OPENNESS (THING) VALUES (?)");
                    preparedStatement.setString(1,key);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
            }
            preparedStatement=connection.prepareStatement("INSERT INTO LOCATION (X,Y,THING) VALUES (?,?,?)");
            preparedStatement.setInt(1,location.getX());
            preparedStatement.setInt(2,location.getY());
            preparedStatement.setString(3,key);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (SQLException e){
            System.out.println("Не удалось добавить вещь в список вещей");
        }
    }
    private String getOwner(String key){
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM OBJECTS");
            while (resultSet.next()){
                if (!resultSet.getString("OBJECT_KEY").equals(key)) continue;
                return resultSet.getString("OWNER");
            }
            statement.close();
        }catch (SQLException e){
            System.out.println("Не удалось вычислить хозяина вещи.");
        }
        return null;
    }
    private boolean checkPresenceAndOwner(InteractWithThings element){
        boolean ban=false;
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM OBJECTS");
            while(resultSet.next()){
                if((element.getThing()+login).equals(resultSet.getString("OBJECT_KEY"))) ban=true;
            }
            statement.close();
        }catch (SQLException e){
            System.out.println("Не удалось сделать запрос к БД.");
        }
        return ban;
    }
    private Response add(InteractWithThings element){
        if(!checkName(element)){
            element.setOwner(login);
            myCollection.add(element);
            if(element.getClass().toString().contains("Pants")){
                insertObject(element.getThing()+login,element.getThing(),element.getClass().toString().replace("class sample.Story.",""),
                        element.getState(),element.getCost(),element.getLocation(),element.getTime(),
                        ((Pants)element).zipper.checkFasteness(),false);
            }else if((element.getClass().toString().contains("Door"))||(element.getClass().toString().contains("Window"))){
                insertObject(element.getThing()+login,element.getThing(),element.getClass().toString().replace("class sample.Story.",""),
                        element.getState(),element.getCost(),element.getLocation(),element.getTime(),
                        false,((House)element).getOpenness());
            }else{
                insertObject(element.getThing()+login,element.getThing(),element.getClass().toString().replace("class sample.Story.",""),
                        element.getState(),element.getCost(),element.getLocation(),element.getTime(),false,false);
            }
            sort(myCollection);
            return new Response(element.getThing(),LocMessage.ADD);
        }else{
            return new Response(element.getThing(),LocMessage.ADD_NOT);
            //return "Вы уже добавляли идентичный объект в БД и коллекцию.".getBytes();
        }
    }
    private Response remove(InteractWithThings element){
        if(checkPresenceAndOwner(element)){
            try {
                String key=element.getThing()+login;
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM LOCATION WHERE THING=?");
                preparedStatement.setString(1,key);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM ZIPPER WHERE THING=?");
                preparedStatement2.setString(1,key);
                preparedStatement2.executeUpdate();
                preparedStatement2.close();
                PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM OPENNESS WHERE THING=?");
                preparedStatement3.setString(1,key);
                preparedStatement3.executeUpdate();
                preparedStatement3.close();
                PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM OBJECTS WHERE OBJECT_KEY=?");
                preparedStatement1.setString(1,key);
                preparedStatement1.executeUpdate();
                preparedStatement1.close();
                if(myCollection.remove(element)){
                    return new Response(element.getThing(),LocMessage.REMOVE);
                    //return (element.getThing()+" убран из коллекции").getBytes();
                }else{
                    return new Response(element.getThing(),LocMessage.REMOVE_NOT);
                    //return (element.getThing()+" не находился в коллекции").getBytes();
                }
            }catch (SQLException e){
                System.out.println("Не удалось сделать запрос к БД.");
                return null;
            }
        }else{
            if(checkPresence(element)){
                return new Response(element.getThing(),LocMessage.FOREIGN);
                //return "Вы не можете удалить чужую вещь из коллекции.".getBytes();
            }else{
                return new Response(element.getThing(),LocMessage.REMOVE_NOT);
            }
        }
    }
    private boolean checkPresence(InteractWithThings element){
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from objects " +
                    "inner join location on object_key=location.thing " +
                    "inner join zipper on object_key=zipper.thing " +
                    "inner join openness on object_key=openness.thing");
            while (resultSet.next()){
                if(element.equals(Server.createObject(resultSet))){
                    return true;
                }
            }
        }catch (SQLException e) {
            System.out.println("Не удалось сделать запрос к БД.");
        }
        return false;
    }
    private boolean checkName(InteractWithThings element){
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("select * from objects");
            while (resultSet.next()){
                if(element.getThing().equals(resultSet.getString("OBJECT_NAME"))){
                    return true;
                }
            }
        }catch (SQLException e) {
            System.out.println("Не удалось сделать запрос к БД.");
        }
        return false;
    }
    private Response remove(int index){
        try{
            InteractWithThings element = myCollection.get(index);
            return remove(element);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }
    private Response add_if_min(InteractWithThings element){
        long count = myCollection.stream()
                                    .filter(object -> (object.getCost()<=element.getCost())).count();
        if(count>0) return new Response(element.getThing(),LocMessage.ADD_IF_MIN_NOT);
        //return ("Стоимость вещи не минимальна, поэтому ей не место в коллекции.").getBytes();
        else return add(element);
    }
    private Response info(){
        /*return ("Информация о коллекции\n" +
                "Тип коллекции: " + myCollection.getClass() + "\n" +
                "Количество элементов в коллекции: " + myCollection.size()+
                "\nДата инициализации коллекции: "+this.date).getBytes();
        */
        return new Response(myCollection.getClass()+","+myCollection.size()+","+this.date,LocMessage.INFO);
    }
    private Response help(){
        return new Response(">>Доступные команды>>\n" +
                "info - вывести в стандартный поток вывода информацию о коллекции\n" +
                "show - вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add{element} -  добавить новый элемент в коллекцию\n" +
                "add_if_min{element} - добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции (длина имени)\n" +
                "remove <int index> -  удалить элемент, находящийся в заданной позиции коллекции\n" +
                "remove{element} - удалить элемент из коллекции по его значению\n" +
                "import{String path} - добавить в коллекцию все данные из файла\n" +
                "help - вывод доступных команд\n" +
                "exit - выход из интерактивного режима\n"+
                "reg <mail> <login> - регистрация для доступа к Базе Данных\n"+
                "log <login> <password> - вход зарегистрированного пользователя.",LocMessage.INFO);
    }
    private Response show(){
        sort(myCollection);
        /*StringBuilder showLine =new StringBuilder("В коллекции находятся:\n");
        myCollection.stream()
                .forEach(element -> showLine.append("Имя вещи: "+element.getThing()+"; класс: "
                        +element.getClass().toString().replace("class ","").replace("Story.","")
                        +"; стоимость: "+element.getCost()
                        +"; состояние: "+element.getState().toString().replace("State.","")
                        +element.getLocation().getStringLocation().replace("Р","; р")+"\n"));
        return showLine.substring(0,showLine.length()-1).getBytes();*/

        return new Response(this.myCollection,LocMessage.COLLECTION);
    }
    private Response importFile(String line){
        String[] lines=line.split("\n");
        StringBuilder importLine = new StringBuilder();
        Arrays.stream(lines).forEach(element ->{
            InteractWithThings elementObject=Helper.objectFromCsv(element);
            if(elementObject!=null){
                if(!checkPresenceAndOwner(elementObject)){
                    add(Helper.objectFromCsv(element));
                    importLine.append(Helper.objectFromCsv(element).getThing()+",");//добавлен в коллекцию\n");
                }
            }//else{
               // importLine.append("Не удалось добавить объект, возможно, проблема в строке файла\n");
            //}
        });
        if(importLine.length()==0){
            return new Response("",LocMessage.IMPORT);
        }
        return new Response(importLine.substring(0,importLine.length()-1),LocMessage.IMPORT);
    }
    private static void sort(List<InteractWithThings> myCollection){
        Collections.sort(myCollection, new Comparator<InteractWithThings>() {
            @Override
            public int compare(InteractWithThings o1, InteractWithThings o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
