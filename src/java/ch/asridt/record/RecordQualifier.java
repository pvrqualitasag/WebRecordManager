package ch.asridt.record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordQualifier {
    
    private final List<RecordType> types = new ArrayList<>();
    private RecordOrder sortOrder = RecordOrder.TITLE_ASC;
    
    public List<RecordType> getTypes() {
        return types;
    }

    public RecordQualifier setTypes(RecordType... typeArray) {
        types.clear();
        types.addAll(Arrays.asList(typeArray));
        return this;
    }

    public RecordOrder getSortOrder() {
        return sortOrder;
    }

    public RecordQualifier setSortOrder(RecordOrder sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }
       
    @Override
    public String toString() {
        return types.toString() + ":" + sortOrder;
    }
}