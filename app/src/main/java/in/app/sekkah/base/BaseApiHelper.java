package in.app.sekkah.base;

import in.app.sekkah.data.callbacks.JSONCallback;
import in.app.sekkah.data.network.request.Reminder;

/**
 * Created by AntiSaby on 1/6/2018.
 * www.radioactivegames.in
 */

public interface BaseApiHelper {
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

    void forgotPassword(String email,
                        String password,
                        String passwordConfirmation,
                        final JSONCallback callback);

    void getProfile(String auth, final JSONCallback callbac);


    void contactUs(String auth,
                   String subject,
                   String type,
                   String message,
                   final JSONCallback callback);

    void pntoken(String auth,
                 String pnToken,
                 final JSONCallback callback);

    void settings(String auth,
                  final JSONCallback callback);

    void getReminders(String auth,
                      final JSONCallback callback);


    void setsettings(String auth,
                     boolean getNotifications,
                     final JSONCallback callback);

    void setreminders(String auth,
                      Reminder Reminder,
                      final JSONCallback callback);


    void userroute(String auth,
                   String source,
                   String destination,
                   String currentLocation,
                   String selectedLocation,
                   String trainId,
                   JSONCallback callback);

    }
