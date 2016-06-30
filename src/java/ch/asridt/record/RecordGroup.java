package ch.asridt.record;

import java.util.ArrayList;
import java.util.List;

public class RecordGroup {
    
    private final String title;
    private final List<RecordItem> items = new ArrayList<>();
    
    public RecordGroup(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public List<RecordItem> getItems() {
        return items;
    }
}