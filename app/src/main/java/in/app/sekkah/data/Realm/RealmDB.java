package in.app.sekkah.data.Realm;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import in.app.sekkah.data.model.StationPOJO;
import in.app.sekkah.data.model.TrainPOJO;
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
        RealmResults<TrainPOJO> result = realm.where(TrainPOJO.class).findAll();
        result.deleteAllFromRealm();
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

    public String getStationbyName(String stationName,Realm realm) {

        String stationId;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("nameen", stationName)
                .findAll();

        if(result.size() >0){
            stationId=  result.get(0).getId();
        }
        else {
            stationId =  "" ;
        }

        return stationId;
    }

    public String getStationbyNameAr(String stationName,Realm realm) {

        String stationId;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("namear", stationName)
                .findAll();

        if(result.size() >0){
            stationId=  result.get(0).getId();
        }
        else {
            stationId =  "" ;
        }

        return stationId;
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

    public int getSationNumber(String stationName , Realm realm) {

        int  stationNum;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("nameen", stationName)
                .findAll();

        if(result.size() >0){
            stationNum=  result.get(0).getStationNum();
        }
        else {
            stationNum= 0;
        }

        return stationNum;
    }

    public int getSationNumberAr(String stationName, Realm realm) {

        int  stationNum;
        RealmResults<StationPOJO> result= realm.where(StationPOJO.class)
                .equalTo("namear", stationName)
                .findAll();


        if(result.size() >0){
            stationNum=  result.get(0).getStationNum();
        }
        else {
            stationNum= 0;
        }

        return stationNum;
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

    public RealmResults<StationPOJO> getStationInfobyId(String stationName, Realm realm){

        RealmResults<StationPOJO> result = realm.where(StationPOJO.class)
                .equalTo("id", stationName)
                .findAll();;

        return result;
    }

    public ArrayList<TrainPOJO> getTrainListfromStation(String fromStation,String toStaion,Realm realm){

        String direction;
        ArrayList<TrainPOJO> resultToSend = new ArrayList<>() ;

       int from = getSationNumber(fromStation,realm);
       int to =  getSationNumber(toStaion,realm);
       int dif = from - to;

       if(dif>0){
           direction = "n2s";
       }else {
           direction = "s2n";
       }

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
            .equalTo("direction",direction);

        fromStation = getStationbyName(fromStation,realm);
        toStaion = getStationbyName(toStaion,realm);

        RealmResults<TrainPOJO> trains = query.findAll();

        TrainPOJO pojo =  null;

        for(int i = 0 ; i < trains.size() ; i++) {

            TrainPOJO trainPOJO = trains.get(i);

            RealmList<String> stations = trainPOJO.getStationPOJOS();

            int found = 0;

            for(int j=0;j<stations.size();j++){
                if(stations.get(j).equals(toStaion)/* || stations.get(j).equals(fromStation)*/){
                    found++;
                    pojo=trainPOJO;
                }else if(stations.get(j).equals(fromStation)){
                    found++;
                    pojo=trainPOJO;
                }
            }

            if(found>=2){
                resultToSend.add(pojo);
            }

            found =0;
            pojo = null;

        }

        return resultToSend;

    }


    public ArrayList<TrainPOJO> getTrainListfromStationAr(String fromStation,String toStaion,Realm realm){
        String direction;

        ArrayList<TrainPOJO> resultToSend = new ArrayList<>() ;


        int from = getSationNumberAr(fromStation,realm);
        int to =  getSationNumberAr(toStaion,realm);
        int dif = from - to;

        if(dif>0){

            direction = "s2n";
        }else {
            direction = "n2s";
        }

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("direction",direction);

        fromStation = getStationbyName(fromStation,realm);
        toStaion = getStationbyName(toStaion,realm);

        RealmResults<TrainPOJO> result1 = query.findAll();

        for(int i = 0 ; i < result1.size() ; i++) {

            TrainPOJO trainPOJO = result1.get(i);

            RealmList<String> stringRealmList = trainPOJO.getStationPOJOS();

            if(stringRealmList.get(i).equals(toStaion) && stringRealmList.get(i).equals(fromStation) ){
                resultToSend.add(trainPOJO);
            }

        }

        return resultToSend;

    }

    public ArrayList<StationPOJO> getTrainStations(Realm realm, String trainId){

        ArrayList<StationPOJO> StationList =new ArrayList<>();

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("id", trainId);

        RealmResults<TrainPOJO> result1 = query.findAll();

        TrainPOJO trainPOJO = result1.get(0);

        RealmList<String> stringRealmList = trainPOJO.getStationPOJOS();

        int count =0;

        for(int i = 0 ; i < stringRealmList.size() ; i++) {

            RealmResults<StationPOJO> Station = getStations(stringRealmList.get(i), realm);

            for (StationPOJO user : Station) {
                StationPOJO stationPOJO = new StationPOJO();
                stationPOJO.setNameen(user.getNameen());
                stationPOJO.setNamear(user.getNamear());
                stationPOJO.setDistance(user.getDistance());
                stationPOJO.setLat(user.getLat());
                stationPOJO.setLng(user.getLng());
                stationPOJO.setTs(trainPOJO.getTsList().get(count));
                count++;
                StationList.add(stationPOJO);
            }

        }

        return StationList;
    }

    public TrainPOJO getTrainbyId(Realm realm, String trainId){
        ArrayList<StationPOJO> StationList =new ArrayList<>();

        RealmQuery<TrainPOJO> query = realm.where(TrainPOJO.class)
                .equalTo("id", trainId);

        RealmResults<TrainPOJO> result1 = query.findAll();

        TrainPOJO trainPOJO = result1.get(0);

        return trainPOJO;
    }

}
