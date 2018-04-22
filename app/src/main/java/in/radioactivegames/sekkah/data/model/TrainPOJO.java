package in.radioactivegames.sekkah.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class TrainPOJO extends RealmObject{


    private String id;

    private String nameen;

    private String namear;

    private Double lat;

    private Double lng;

    private String updatedAt;

    private String delay;

    private String depStation;

    private String getDepStationtime;

    private String finalStation;

    private String finalStationDepStationtime;


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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
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

    @Override
    public String toString() {
        return "TrainPOJO{" +
                "id='" + id + '\'' +
                ", nameen='" + nameen + '\'' +
                ", namear='" + namear + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", updatedAt='" + updatedAt + '\'' +
                ", delay='" + delay + '\'' +
                ", depStation='" + depStation + '\'' +
                ", getDepStationtime='" + getDepStationtime + '\'' +
                ", finalStation='" + finalStation + '\'' +
                ", finalStationDepStationtime='" + finalStationDepStationtime + '\'' +
                '}';
    }
}
