package net.mindlevel.client;

import java.util.Date;

import net.mindlevel.client.pages.Admin;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.Normalizer;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;


public class UserTools {

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    final static UserServiceAsync userService = GWT
            .create(UserService.class);

    public static boolean isLoggedIn() {
        return Mindlevel.user != null;
    }

    public static boolean isAdmin() {
        return isLoggedIn() && Mindlevel.user.isAdmin();
    }

    public static boolean isModerator() {
        return isLoggedIn() && Mindlevel.user.isModerator();
    }

    public static void setLoggedIn(User user) {
        Mindlevel.user = user;
        Date date = new Date();
        long nowLong = date.getTime();
        nowLong = nowLong + (1000 * 60 * 60 * 24 * 7);//seven days
        date.setTime(nowLong);
        Cookies.setCookie("mindlevel", user.getToken(), date);
        HandyTools.setRightView(true, Normalizer.capitalizeName(user.getUsername()));
        if(user.isAdmin()) {
            new Admin(Mindlevel.getAppArea(true));
            //Mindlevel.forceFocus = false;
        }
    }

    public static void setLoggedOff() {
        Cookies.removeCookie("mindlevel");
        Mindlevel.user = null;
        HandyTools.setRightView(false, "");
    }

    public static void keepLoggedIn(String token) {
        userService.getUserFromToken(token, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                setLoggedOff();
            }

            @Override
            public void onSuccess(User user) {
                setLoggedIn(user);
            }
        });
    }

}