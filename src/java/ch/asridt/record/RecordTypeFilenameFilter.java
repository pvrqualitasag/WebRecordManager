package ch.asridt.record;

import java.io.File;
import java.io.FilenameFilter;

public class RecordTypeFilenameFilter implements FilenameFilter {

    private RecordQualifier recordQualifier = null;
    
    public RecordTypeFilenameFilter(RecordQualifier recordQualifier) {
        this.recordQualifier = recordQualifier;
    }
    
    @Override
    public boolean accept(File dir, String name) {
        StringBuilder regexParts = new StringBuilder();
        for (RecordType type : recordQualifier.getTypes()) {
            switch (type) {
                case MY:
                    regexParts.append(".*my$|");
                    break;
                case FI:
                    regexParts.append(".*fi$|");
                    break;
            }
        }
        regexParts.deleteCharAt(regexParts.length() - 1);
        
        if (name.toLowerCase().matches(regexParts.toString())) {
            File f = new File(dir, name);
            if (f.isFile()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}