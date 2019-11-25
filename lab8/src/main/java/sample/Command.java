package sample;

import java.io.Serializable;

public class Command implements Serializable {
    private String command;
    private Object data;
    private String login;
    private String password;
    public Command(String command){
        this.command=command;
        this.data="";
    }
    public Command(String command, Object data, String login, String password){
        this.command=command;
        this.data=data;
        this.login=login;
        this.password=password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    @Override
    public String toString(){
        return "Команда{"+command+","+data+"}";
    }
}
