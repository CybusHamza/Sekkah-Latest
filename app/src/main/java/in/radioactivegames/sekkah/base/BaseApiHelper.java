package in.radioactivegames.sekkah.base;

import android.content.Context;

import in.radioactivegames.sekkah.data.callbacks.JSONCallback;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public interface BaseApiHelper
{
    void registerUser(String username,
                      String firstName,
                      String lastName,
                      String email,
                      long dateOfBirth,
                      String password,
                      String passwordConfirmation,
                      final JSONCallback callback);

    void loginUser(String username,
                   String password,
                   final JSONCallback callback);
}
