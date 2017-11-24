package dawson.dawsondangerousclub;

public class Entry{
    public final String title;
    public final String description;
    public final String course;
    public final String teacher;
    public final String notes;
    public final String pubDate;

    public Entry(String title, String description, String course,
                 String teacher, String notes, String pubDate) {
        this.title = title;
        this.description = description;
        this.course = course;
        this.teacher = teacher;
        this.notes = notes;
        this.pubDate = pubDate;
    }
}
