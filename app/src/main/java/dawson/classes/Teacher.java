package dawson.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import java.net.URLDecoder;

/**
 * Created by Jacob on 2017-11-28.
 */

public class Teacher implements Parcelable {
    private String first_name;
    private String last_name;
    private String full_name;
    private String email;
    private String office;
    private String local;
    private String website;
    private String bio;
    private String image;

    public Teacher(){
    }

    public Teacher(String first_name, String last_name, String full_name, String email, String office, String local, String website, String bio, String image) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.full_name = full_name;
        this.email = email;
        this.office = office;
        this.local = local;
        this.website = website;
        this.bio = bio;
        this.image = image;
    }

    protected Teacher(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        full_name = in.readString();
        email = in.readString();
        office = in.readString();
        local = in.readString();
        website = in.readString();
        bio = in.readString();
        image = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    public String getFirst_name() {
        if (first_name == null || first_name.equals("") || first_name.equals("null"))
            return "";
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        if (last_name == null || last_name.equals("") || last_name.equals("null"))
            return "";
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFull_name() {
        if (full_name == null || full_name.equals("") || full_name.equals("null"))
            return "";
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        if (email == null || email.equals("") || email.equals("null"))
            return "";
        return Html.fromHtml(email).toString();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOffice() {
        if (office == null||office.equals("") || office.equals("null"))
            return "";
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getLocal() {
        if (local == null||local.equals("") || local.equals("null"))
            return "";
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getWebsite() {
        if (website == null || website.equals("") || website.equals("null"))
            return "";
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBio() {
        if (bio == null || bio.equals("") || bio.equals("null"))
            return "";
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        if (image == null || image.equals("") || image.equals("null"))
            return "";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.first_name);
        parcel.writeString(this.last_name);
        parcel.writeString(this.full_name);
        parcel.writeString(this.email);
        parcel.writeString(this.office);
        parcel.writeString(this.local);
        parcel.writeString(this.website);
        parcel.writeString(this.bio);
        parcel.writeString(this.image);
    }

    public String teacherInfo(){
        return first_name + "\n" + last_name + "\n" + full_name + "\n" + email + "\n" + office + "\n" + " " + local + "\n" + website + "\n" + bio + "\n" + image;

    }
}
