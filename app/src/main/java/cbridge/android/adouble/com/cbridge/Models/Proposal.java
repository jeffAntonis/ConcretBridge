package cbridge.android.adouble.com.cbridge.Models;

public class Proposal {
    private String idService, idUser, value, time, observation;

    public  Proposal(){
    }

    public Proposal(String idService, String idUser, String value, String time, String observation) {
        this.idService = idService;
        this.idUser = idUser;
        this.value = value;
        this.time = time;
        this.observation = observation;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
