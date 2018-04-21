package in.radioactivegames.sekkah.base;

import in.radioactivegames.sekkah.data.model.User;

/**
 * Created by AntiSaby on 1/16/2018.
 * www.radioactivegames.in
 */

public interface BaseSharedPrefsHelper
{
    public void setCurrentUser(User user);
    public User getCurrentUser();
}
