import sample.Story.*;
import sample.Command;

import java.io.*;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Server {
    private DatagramChannel udpChannel;
    private int port;
    private List<InteractWithThings> myCollection;
    private String filename;
    Connection connection;
    Statement statement;
     private Date date;
    public Server(int port,List<InteractWithThings> myCollection) throws IOException{
        this.port=port;
        udpChannel= DatagramChannel.open().bind(new InetSocketAddress("localhost",port));
        this.setMyCollection(myCollection);
        connection=сonnectionWithDataBase();
        createObjectTable();
        System.out.println("Сервер запущен.");
        System.out.println("Адрес: "+ InetAddress.getLocalHost());
        System.out.println("Порт: "+this.port);
    }
    public List<InteractWithThings> getMyCollection() {
        return myCollection;
    }
    public void setMyCollection(List<InteractWithThings> myCollection) {
        this.myCollection = myCollection;
        this.date=new Date();
    }
    public Date getDate() {
        return date;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename){
        this.filename=filename;
    }
    public Connection сonnectionWithDataBase() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            Connection connection = DriverManager.getConnection(url, "postgres", "rgptk2009");
            statement=connection.createStatement();
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось подключиться к базе данных.");
        }
        return null;
    }
    public void createObjectTable(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS( "+
                                "USER_ID SERIAL PRIMARY KEY,"+
                                "USER_LOGIN text unique,"+
                                "USER_MAIL text unique,"+
                                "USER_PASSWORD text)";

        String createObjectTable = "CREATE TABLE IF NOT EXISTS OBJECTS("+
                                    "OBJECT_KEY text unique PRIMARY KEY,"+
                                    "OWNER text,"+
                                    "OBJECT_NAME text, OBJECT_TYPE text,"+
                                    "OBJECT_STATE STATE, OBJECT_COST int,"+
                                    "OBJECT_DATE TIMESTAMP," +
                                    "FOREIGN KEY(OWNER) REFERENCES USERS(USER_LOGIN))";
        String createLocationTable = "CREATE TABLE IF NOT EXISTS LOCATION("+
                                    "X integer,Y integer, THING text,"+
                                    "FOREIGN KEY (THING) REFERENCES OBJECTS(OBJECT_KEY))";
        String createZipperTable = "CREATE TABLE IF NOT EXISTS ZIPPER("+
                                    "THING text, FASTENED boolean,"+
                                    "FOREIGN KEY (THING) REFERENCES OBJECTS(OBJECT_KEY))";
        String createOpennessTable = "CREATE TABLE IF NOT EXISTS OPENNESS("+
                                    "THING text, OPENED boolean,"+
                                    "FOREIGN KEY (THING) REFERENCES OBJECTS(OBJECT_KEY))";
        try{
            statement.execute(createTableSQL);
            statement.execute(createObjectTable);
            statement.execute(createLocationTable);
            statement.execute(createZipperTable);
            statement.execute(createOpennessTable);
            ResultSet resultSet =statement.executeQuery("select * from objects " +
                    "inner join location on object_key=location.thing " +
                    "inner join zipper on object_key=zipper.thing " +
                    "inner join openness on object_key=openness.thing");
            while(resultSet.next()){
                InteractWithThings thing=createObject(resultSet);
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
    }
    public static InteractWithThings createObject(ResultSet resultSet){
        try{
            String name=resultSet.getString("OBJECT_NAME");
            String type=resultSet.getString("OBJECT_TYPE");
            int cost=resultSet.getInt("OBJECT_COST");
            State state = Helper.valueOf(resultSet.getString("OBJECT_STATE"));
            LocalDateTime localDateTime =resultSet.getObject("OBJECT_DATE",LocalDateTime.class);
            Location location= new Location(resultSet.getInt("X"),resultSet.getInt("Y"));
            boolean fastened = resultSet.getBoolean("FASTENED");
            boolean openness = resultSet.getBoolean("OPENED");
            InteractWithThings thing=null;
            switch(type.replace("sample.Story.","")){
                case "CakeLayer":
                    thing=new CakeLayer(name,state,location,cost);
                    break;
                case "Door":
                    thing=new Door(name,state,location,cost,openness);
                    break;
                case "Pants":
                    thing=new Pants(name,state,location,cost,fastened);
                    break;
                case "Potato":
                    thing=new Potato(name,state,location,cost);
                    break;
                case "Socks":
                    thing=new Socks(name,state,location,cost);
                    break;
                case "Soup":
                    thing=new Soup(name,state,location,cost);
                    break;
                case "Window":
                    thing=new Window(name,state,location,cost,openness);
                    break;
            }
            thing.setTime(localDateTime);
            thing.setOwner(resultSet.getString("OWNER"));
            return thing;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Не удалось выполнить запрос к БД.");
            return null;
        }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Не удалось проинициализировать объект.");
            return null;
        }
    }
    public void load(){
        System.out.println("Загрузка коллекции.");
        String fileLines="";
        String line;
        try{
            File file = new File(filename .replace(" ",""));
            if (!(file.exists())) throw new FileNotFoundException("Файл для импортирования не найден.");
            if (!file.canRead()) throw new SecurityException("Доступ запрещён. Файл для импортирования защищен от чтения");
            FileReader fileReader = new FileReader(file);
            Scanner fileScanner = new Scanner(fileReader);
            while(fileScanner.hasNextLine()){
                line=fileScanner.nextLine();
                if(line!=null){
                    fileLines=fileLines.concat(line)+"\n";
                }
            }
            fileReader.close();
        }catch(SecurityException | IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        if(!fileLines.equals("")) {
            fileLines=fileLines.substring(0,fileLines.length()-1);
            String[] lineArray=fileLines.split("\n");
            for (String object:lineArray) {
                InteractWithThings element=Helper.objectFromCsv(object);
                if(element!=null){
                    myCollection.add(element);
                    System.out.println(element.getThing()+" добавлен в коллекцию.");
                }else{
                    System.out.println("Невозможно добавить объект.");
                }
            }
        }
    }
    private void listen() throws Exception{
        System.out.println("Сервер запущен "+InetAddress.getLocalHost());
        CommandHandler handler;
        while(true){
            ByteBuffer buffer =ByteBuffer.allocate(8192);
            buffer.clear();
            InetSocketAddress clientAddress = (InetSocketAddress)udpChannel.receive(buffer);
            Command command;
            try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer.array());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)){
                command=(Command)objectInputStream.readObject();
                String message = "Введённая команда: "+command.getCommand();
                if(!message.contains("show")) System.out.println(message);
                handler=new CommandHandler(clientAddress, udpChannel, command, myCollection, connection, date);
                handler.serve();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Ой");
            }
        }
    }
    public static void showUsage() {
        System.out.println("Чтобы запустить сервер введите нужные данные в качестве агрументов.");
        System.out.println("java18 -jar Server.jar <порт>");
        System.exit(1);
    }
    public static void save(List<InteractWithThings> myCollection, String path){
        StringBuilder lines=new StringBuilder();
        myCollection.stream()
                .forEach(element->lines.append(Helper.objectToCsv(element)+"\n"));
        String stringCollection = lines.substring(0,lines.length()-1);
        try (FileOutputStream stream = new FileOutputStream(path)) {
            byte[] buffer = stringCollection.getBytes();
            stream.write(buffer);
            stream.flush();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {
        List<InteractWithThings> myCollection = Collections.synchronizedList(new LinkedList<>());
        int input_port = -1;
        if (args.length == 0) {
            showUsage();
        }
        try {
            input_port = Integer.parseInt(args[0]);
        } catch (IllegalArgumentException e) {
            showUsage();
        }
        try {
            Server server = new Server(input_port,myCollection);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    server.statement.close();
                    server.connection.close();
                } catch (Exception e1) {
                    System.out.println("Вау :)");
                }
            }));
            server.listen();
        }catch (BindException e){
            System.out.println("Порт канала уже занят, используйте другой.");
        }
    }
}
