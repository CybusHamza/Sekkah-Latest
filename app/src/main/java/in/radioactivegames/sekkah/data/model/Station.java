package in.radioactivegames.sekkah.data.model;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by AntiSaby on 12/5/2017.
 * www.radioactivegames.in
 */

public class Station extends RealmObject
{
    public String  id;
    public String ts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
