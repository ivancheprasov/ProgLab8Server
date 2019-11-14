package sample;


import java.io.Serializable;

public class Response implements Serializable {
    private Object response;
    private LocMessage message;
    public Response(Object response,LocMessage message) {
        this.response = response;
        this.message=message;
    }
    public LocMessage getMessage(){
        return message;
    }
    public Object getResponse() {
        return response;
    }

}