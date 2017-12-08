package dawson.classes;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FeedParser{
    // don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        Log.d("MYTAG", "PARSING NOW");
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
        // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String description = null;
        String course = null;
        String teacher = null;
        String notes = null;
        String pubDate = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "description":
                    description = readDescription(parser);
                    break;
                case "course":
                    course = readCourse(parser);
                    break;
                case "teacher":
                    teacher = readTeacher(parser);
                    break;
                case "notes":
                    notes = readNotes(parser);
                    break;
                case "pubDate":
                    pubDate = readPubDate(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Entry(title, description, course, teacher, notes, pubDate);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    // Processes course tags in the feed.
    private String readCourse(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "course");
        String course = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "course");
        return course;
    }

    // Processes teacher tags in the feed.
    private String readTeacher(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "teacher");
        String teacher = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "teacher");
        return teacher;
    }

    // Processes notes tags in the feed.
    private String readNotes(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "notes");
        String notes = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "notes");
        return notes;
    }

    // Processes pubDate tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    // Extracts the text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
