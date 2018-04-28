package in.radioactivegames.sekkah.data.Realm;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import in.radioactivegames.sekkah.data.model.Station;
import in.radioactivegames.sekkah.data.model.StationPOJO;
import in.radioactivegames.sekkah.data.model.TrainPOJO;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class RealmDB {

    static  RealmDB realmDB;
    private RealmDB(){

    }

    public static RealmDB getinstance(){
        if(realmDB != null){

            return realmDB;

        }else {
            return new RealmDB();
        }
    }

    public  void clearAll(Realm  realm){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    public String getStation(String stationId,Realm realm) {

        String stationname;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("id", stationId)
                .findAll();

        if(result.size() >0){
            stationname=  result.get(0).getNameen();
        }
        else {
            stationname =  "" ;
        }

        return stationname;
    }

    public String getStationAr(String stationId,Realm realm) {

        String stationname;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("id", stationId)
                .findAll();

        if(result.size() >0){
            stationname=  result.get(0).getNamear();
        }
        else {
            stationname =  "" ;
        }

        return stationname;
    }

    public LatLng getStationLatLng(String stationId, Realm realm) {

        LatLng latlng;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("id", stationId)
                .findAll();

        if(result.size() >0){
            latlng=  new LatLng(result.get(0).getLat(),result.get(0).getLng());
        }
        else {
            latlng=  new LatLng(0,0 );
        }

        return latlng;
    }

    public RealmResults<StationPOJO> getStations(String stationId, Realm realm) {

        LatLng latlng;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("id", stationId)
                .findAll();

        return result;
    }

    public RealmResults<StationPOJO> getAllStation(Realm realm) {

        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .findAll();

        return result;
    }

    public RealmResults<TrainPOJO> getTrains(String fromStation,String toStaion,Realm realm){

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("depStation", fromStation)
                .equalTo("finalStation", toStaion);

        RealmResults<TrainPOJO> result1 = query.findAll();

        return result1;
    }

    public RealmResults<TrainPOJO> getTrainsAr(String fromStation,String toStaion,Realm realm){

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("depStationAr", fromStation)
                .equalTo("finalStationAr", toStaion);

        RealmResults<TrainPOJO> result1 = query.findAll();

        return result1;
    }
    public RealmResults<TrainPOJO> getTrains(Realm realm){

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class);

        RealmResults<TrainPOJO> result1 = query.findAll();

        return result1;
    }

    public ArrayList<LatLng> getTrainStationsLatLng(Realm realm, String trainId){

        ArrayList<LatLng> latLngArrayList =new ArrayList<>();

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("id", trainId);

        RealmResults<TrainPOJO> result1 = query.findAll();

        TrainPOJO trainPOJO = result1.get(0);

        RealmList<String> stringRealmList = trainPOJO.getStationPOJOS();

           for(int i = 0 ; i < stringRealmList.size() ; i++) {

               LatLng latLng = getStationLatLng(stringRealmList.get(i), realm);

               latLngArrayList.add(latLng);

           }

        return latLngArrayList;
    }


    public ArrayList<StationPOJO> getTrainStations(Realm realm, String trainId){

        ArrayList<StationPOJO> StationList =new ArrayList<>();

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("id", trainId);

        RealmResults<TrainPOJO> result1 = query.findAll();

        TrainPOJO trainPOJO = result1.get(0);

        RealmList<String> stringRealmList = trainPOJO.getStationPOJOS();

        for(int i = 0 ; i < stringRealmList.size() ; i++) {

            RealmResults<StationPOJO> Station = getStations(stringRealmList.get(i), realm);

            for (StationPOJO user : Station) {
                StationPOJO stationPOJO = new StationPOJO();
                stationPOJO.setNameen(user.getNameen());
                stationPOJO.setNamear(user.getNamear());
                stationPOJO.setDistance(user.getDistance());
                stationPOJO.setLat(user.getLat());
                stationPOJO.setLng(user.getLng());
                stationPOJO.setTs(trainPOJO.getTsList().get(i));

                StationList.add(stationPOJO);
            }

        }

        return StationList;
    }

}
