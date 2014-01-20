package net.mindlevel.client;

import java.util.Date;

import net.mindlevel.client.pages.Admin;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.Normalizer;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HandyTools {
    private static boolean isLoggedIn = false;

    /**
     * Create a remote service proxy to talk to the server-side token
     * service.
     */
    private final static UserServiceAsync userService = GWT
            .create(UserService.class);

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static Button showDialogBox(String title, HTML text) {
        Mindlevel.forceFocus = false;
        final DialogBox db = new DialogBox();
        db.setText(title);
        final Button closeButton = new Button("Ok");
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                db.hide();
            }
        });
        VerticalPanel dbPanel = new VerticalPanel();
        text.addStyleName("dialogBoxHTML");
        dbPanel.add(text);
        dbPanel.add(closeButton);
        db.add(dbPanel);
        db.center();
        closeButton.setFocus(true);
        return closeButton;
    }

    public static void setLoggedIn(User user) {
        Mindlevel.user = user;
        Date date = new Date();
        long nowLong = date.getTime();
        nowLong = nowLong + (1000 * 60 * 60 * 24 * 7);//seven days
        date.setTime(nowLong);
        Cookies.setCookie("mindlevel", user.getToken(), date);
        setRightView(true, Normalizer.capitalizeName(user.getUsername()));
        if(user.isAdmin()) {
            new Admin(Mindlevel.getAppArea(true));
            //Mindlevel.forceFocus = false;
        }
    }

    public static void setLoggedOff() {
        Cookies.removeCookie("mindlevel");
        setRightView(false, "");
    }

    public static void keepLoggedIn(String token) {
        userService.getUserFromToken(token, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                HandyTools.setLoggedOff();
            }

            @Override
            public void onSuccess(User user) {
                setLoggedIn(user);
            }
        });
    }

    private static void setRightView(boolean logIn, String username) {
//        RootPanel.get("hidelogin").setVisible(!logIn);
//        RootPanel.get("hidelogout").setVisible(logIn);
//        RootPanel.get("hideregister").setVisible(!logIn);
//        RootPanel.get("hideprofile").setVisible(logIn);
        RootPanel.get("hidelogin").setStyleName("superhidden", logIn);
        RootPanel.get("hidelogout").setStyleName("superhidden", !logIn);
        RootPanel.get("hideregister").setStyleName("superhidden", logIn);
        RootPanel.get("profile").getElement().setInnerHTML(username);
        RootPanel.get("hideprofile").setStyleName("superhidden", !logIn);
        RootPanel.get("hidechat").setStyleName("superhidden", !logIn);
        if(Mindlevel.user.isAdmin()) {
            RootPanel.get("adminmenu").setStyleName("superhidden", !logIn);
            RootPanel.get("apparea").setStyleName("adminbar", logIn);
        }

//        else if(!logIn)
//            RootPanel.get("adminmenu").setStyleName("superhidden", !logIn);
        isLoggedIn = logIn;
    }

    public static void debugMsg(String msg) {
        Element debug = RootPanel.get("debug").getElement();
        debug.setInnerHTML(debug.getInnerHTML() + "</br>" + msg);
    }

    //Unix time to a readable date
    public static String unixToDate(long unixtime) {
        java.util.Date date = new java.util.Date(unixtime*1000);
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dtf = new DateTimeFormat("EEE, d MMM yyyy HH:mm:ss", info) {};
        return dtf.format(date);
    }
}
