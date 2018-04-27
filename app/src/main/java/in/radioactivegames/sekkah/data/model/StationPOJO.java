package in.radioactivegames.sekkah.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StationPOJO extends RealmObject  {

    private String id;

    private String namear;

    private String nameen;

    private Double lat;

    private Double lng;

    private Integer distance;

    private String ts;

    public StationPOJO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamear() {
        return namear;
    }

    public void setNamear(String namear) {
        this.namear = namear;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "StationPOJO{" +
                "id='" + id + '\'' +
                ", namear='" + namear + '\'' +
                ", nameen='" + nameen + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", distance=" + distance +
                ", ts='" + ts + '\'' +
                '}';
    }
}
