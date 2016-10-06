package com.timekiller.wannaseemovie.ui.fragment.dummy;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MovieDummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        String url = "http://image18.poco.cn/mypoco/myphoto/20160921/20/178343444201609212006402253816500138_000.jpg";
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i,url));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position,String url) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position),url);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;
        public final String url;

        public DummyItem(String id, String content, String details,String url) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.url = url;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
