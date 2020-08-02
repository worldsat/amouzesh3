package com.makancompany.assistant.Kernel.Bll;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsBll {

    private final Context context;
    private final SharedPreferences preferences;
    private final String DEFAULT_URL = "http://makansystem.com/makan/makan";
    private final String DEFAULT_PORT = "0";
    private final String DEFAULT_PORT2 = "0";


    public SettingsBll(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    public String getUrlAddress() {
        return preferences.getString("Api", DEFAULT_URL);
    }

    public void setUrlAddress(String address) {
        preferences.edit().putString("Api", address).apply();
    }

    public String getPort() {
        return preferences.getString("Port", DEFAULT_PORT);
    }

    public void setPort(String port) {
        preferences.edit().putString("Port", port).apply();
    }

    public String getPort2() {
        return preferences.getString("Port2", DEFAULT_PORT2);
    }

    public void setPort2(String port) {
        preferences.edit().putString("Port2", port).apply();
    }

    public void setMode(boolean isOnline) {
        if (isOnline) {
            preferences.edit().putBoolean("Mode", true).apply();
        } else preferences.edit().putBoolean("Mode", false).apply();
    }

    public boolean isOnline() {
        return preferences.getBoolean("Mode", true);
    }

    public String getTicket() {
        return preferences.getString("Token", null);
    }

    public void setTicket(String token) {
        preferences.edit().putString("Token", token).apply();
    }

    public String getUserId() {
        return preferences.getString("UserId", null);
    }

    public void setUserId(String token) {
        preferences.edit().putString("UserId", token).apply();
    }

    public void logout() {
        preferences.edit().putString("Token", null).apply();
        setLoging(false);
    }

    public String getUserPostId() {
        return preferences.getString("UserPostId", null);
    }

    public void setUserPostId(String token) {
        preferences.edit().putString("UserPostId", token).apply();
    }

    public String getUserName() {
        return preferences.getString("UserName", null);
    }

    public void setUserName(String userName) {
        preferences.edit().putString("UserName", userName).apply();
    }

    public boolean getUserPosts(String userPostId) {

        boolean UserPost = false;
        String[] userPostIds = getUserPostId().split(",");

        for (int i = 0; i < userPostIds.length; i++) {
            if (userPostIds[i].equals(userPostId)) {
                UserPost = true;
            }
        }
        return UserPost;
    }
    public boolean getUserId(String userPostId) {

        boolean UserId = false;
        String[] userIds = getUserId().split(",");

        for (int i = 0; i < userIds.length; i++) {
            if (userIds[i].equals(userPostId)) {
                UserId = true;
            }
        }
        return UserId;
    }
    public Boolean getLoging() {
        return preferences.getBoolean("login", false);
    }

    public void setLoging(Boolean status) {
        preferences.edit().putBoolean("login", status).apply();
    }


    public String getCompanyId() {
        return preferences.getString("CompanyId", null);
    }

    public void setCompanyId(String userName) {
        preferences.edit().putString("CompanyId", userName).apply();
    }
}
