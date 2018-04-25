package in.radioactivegames.sekkah.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TrainPOJO extends RealmObject implements Serializable {


    private String id;

    private String nameen;

    private String namear;

    private String number;

    private String depStation;

    private String getDepStationtime;

    private String finalStation;

    private String finalStationDepStationtime;

    private RealmList<String> stationPOJOS;

    private RealmList<String> tsList;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public String getNamear() {
        return namear;
    }

    public void setNamear(String namear) {
        this.namear = namear;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDepStation() {
        return depStation;
    }

    public void setDepStation(String depStation) {
        this.depStation = depStation;
    }

    public String getGetDepStationtime() {
        return getDepStationtime;
    }

    public void setGetDepStationtime(String getDepStationtime) {
        this.getDepStationtime = getDepStationtime;
    }

    public String getFinalStation() {
        return finalStation;
    }

    public void setFinalStation(String finalStation) {
        this.finalStation = finalStation;
    }

    public String getFinalStationDepStationtime() {
        return finalStationDepStationtime;
    }

    public void setFinalStationDepStationtime(String finalStationDepStationtime) {
        this.finalStationDepStationtime = finalStationDepStationtime;
    }

    public RealmList<String> getStationPOJOS() {
        return stationPOJOS;
    }

    public void setStationPOJOS(RealmList<String> stationPOJOS) {
        this.stationPOJOS = stationPOJOS;
    }

    public RealmList<String> getTsList() {
        return tsList;
    }

    public void setTsList(RealmList<String> tsList) {
        this.tsList = tsList;
    }

    @Override
    public String toString() {
        return "TrainPOJO{" +
                "id='" + id + '\'' +
                ", nameen='" + nameen + '\'' +
                ", namear='" + namear + '\'' +
                ", number='" + number + '\'' +
                ", depStation='" + depStation + '\'' +
                ", getDepStationtime='" + getDepStationtime + '\'' +
                ", finalStation='" + finalStation + '\'' +
                ", finalStationDepStationtime='" + finalStationDepStationtime + '\'' +
                ", stationPOJOS=" + stationPOJOS +
                ", tsList=" + tsList +
                '}';
    }
}
