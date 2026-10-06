package org.example.entities;

public class AutoEcoleInfo {
    private String autoEcoleName;
    private String autoEcoleDescription;
    private String autoEcoleWebsite;
    private Integer autoEcolePhone;
    private String autoEcoleEmail;
    private String autoEcoleAddress;
    private String autoEcolelogo ="";
    public AutoEcoleInfo() {}
    public AutoEcoleInfo(String autoEcoleName,String autoEcoleDescription,String autoEcoleUrl,Integer autoEcolePhone,String autoEcoleEmail,String autoEcoleAddress) {
        this.autoEcoleName = autoEcoleName;
        this.autoEcoleDescription = autoEcoleDescription;
        this.autoEcoleWebsite = autoEcoleUrl;
        this.autoEcolePhone = autoEcolePhone;
        this.autoEcoleEmail = autoEcoleEmail;
        this.autoEcoleAddress = autoEcoleAddress;
    }

    public void setAutoEcolelogo(String autoEcolelogo) {
        this.autoEcolelogo = autoEcolelogo;
    }

    public String getAutoEcolelogo() {
        return autoEcolelogo;
    }

    public void setAutoEcoleName(String autoEcoleName) {
        this.autoEcoleName = autoEcoleName;
    }

    public void setAutoEcoleDescription(String autoEcoleDescription) {
        this.autoEcoleDescription = autoEcoleDescription;
    }

    public void setAutoEcoleWebsite(String autoEcoleWebsite) {
        this.autoEcoleWebsite = autoEcoleWebsite;
    }

    public void setAutoEcolePhone(Integer autoEcolePhone) {
        this.autoEcolePhone = autoEcolePhone;
    }

    public void setAutoEcoleEmail(String autoEcoleEmail) {
        this.autoEcoleEmail = autoEcoleEmail;
    }

    public void setAutoEcoleAddress(String autoEcoleAddress) {
        this.autoEcoleAddress = autoEcoleAddress;
    }

    public String getAutoEcoleName() {
        return autoEcoleName;
    }

    public String getAutoEcoleDescription() {
        return autoEcoleDescription;
    }

    public String getAutoEcoleWebsite() {
        return autoEcoleWebsite;
    }

    public Integer getAutoEcolePhone() {
        return autoEcolePhone;
    }

    public String getAutoEcoleEmail() {
        return autoEcoleEmail;
    }

    public String getAutoEcoleAddress() {
        return autoEcoleAddress;
    }
    public String toString() {
        return "["+
                autoEcoleName+","+
                autoEcoleDescription+","+
                autoEcoleWebsite +","
                +autoEcolePhone+","
                +autoEcoleEmail+","
                +autoEcoleAddress+"]";
    }
}
