package org.fun7.model;

public class ServicesStatus {

    public enum EStatus {
        Disabled("disabled"), Enabled("enabled");

        public final String statusValue;

        EStatus(String value) {
            statusValue = value;
        }
    }

    private String multiplayer;
    private String userSupport;
    private String ads;

    public String getMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(EStatus multiplayer) {
        this.multiplayer = multiplayer.statusValue;
    }

    public String getUserSupport() {
        return userSupport;
    }

    public void setUserSupport(EStatus userSupport) {
        this.userSupport = userSupport.statusValue;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(EStatus ads) {
        this.ads = ads.statusValue;
    }
}
