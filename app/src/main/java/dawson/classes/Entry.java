package dawson.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean used for parsing the RSS Feed for the cancelled classes.
 * @author Isaak
 */
public class Entry implements Parcelable
{
    public final String title;
    public final String description;
    public final String course;
    public final String teacher;
    public final String notes;
    public final String pubDate;

    public Entry() {
        this.title = "";
        this.description =  "";
        this.course =  "";
        this.teacher =  "";
        this.notes =  "";
        this.pubDate =  "";
    }

    public Entry(String title, String description, String course,
                 String teacher, String notes, String pubDate) {
        this.title = title;
        this.description = description;
        this.course = course;
        this.teacher = teacher;
        this.notes = notes;
        this.pubDate = pubDate;
    }

        public Entry(Parcel in) {
            this.title = in.readString();
            this.description = in.readString();
            this.course = in.readString();
            this.teacher = in.readString();
            this.notes = in.readString();
            this.pubDate = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(description);
            dest.writeString(course);
            dest.writeString(teacher);
            dest.writeString(notes);
            dest.writeString(pubDate);
        }

    public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {

        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

}
