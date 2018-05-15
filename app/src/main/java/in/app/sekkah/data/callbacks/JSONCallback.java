package in.app.sekkah.data.callbacks;

import org.json.JSONObject;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public interface JSONCallback
{
    void onSuccess(JSONObject jsonObject);
    void onFail(String errorMessage);
}
