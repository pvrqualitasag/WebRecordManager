/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.record;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 *
 * @author pvr
 */
public class FileRecordManager implements RecordManager{
    // logger for manager
    private static final Logger logger = Logger.getLogger(FileRecordManager.class.getName()); 
    // directory where record files are
    private File dir;
    
    private String hiddenLocationPrefix = null;

    public enum IdFormat {

        WEB,
        FX;
    }
    
    // constructor with a directory
    public FileRecordManager(File dir, IdFormat idFormat) {
        if (dir == null || !dir.isDirectory()){
            logger.log(Level.SEVERE, "FileRecordManager must be created with a directory: {0}", dir);
            throw new IllegalArgumentException("Argument must be a directory");
        }
        // assign field
        this.dir = dir;
        
        if (idFormat == IdFormat.WEB) {
            //hiddenLocationPrefix = dir.getParent();
            hiddenLocationPrefix = dir.toString();
            
        }
        logger.log(Level.INFO, "FileRecordManager created with directory: {0}", dir);
    }

    @Override
    public void insertNewRecordItem(RecordItem item) throws IOException {
        Files.write(Paths.get(dir + "/" + item.getId()), item.toTsv().getBytes());
        
    }

    @Override
    public void createRecordItem(RecordItem item, InputStream content) throws IOException {
        File f = new File(dir, item.getId());
        String realId = generateRecordId(f);
        item.setId(realId);
        Files.copy(content, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public RecordItem getRecordItem(String id) throws FileNotFoundException {
        File f = new File(hiddenLocationPrefix, id);
        if (f == null || !f.isFile()) {
            throw new FileNotFoundException(id);
        }
        RecordItem recordItem = null;
        if (recordItem == null) {
            String title = f.getName();
            recordItem = new RecordItem(title, id, new Date(f.lastModified()));
            try {
                String fileContent = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
                String[] tokens = fileContent.split("\t");
                recordItem.setRecordType(tokens[0]);
                recordItem.setDateOfRecording(tokens[1]);
                recordItem.setRecordValue(Double.parseDouble(tokens[2]));
                recordItem.setRecordUnit(tokens[3]);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "IOException while reading file: " + id, ex);
            }
        }
        return recordItem;
    }

    @Override
    public void updateRecordItem(RecordItem item) throws FileNotFoundException {
        File f = new File(hiddenLocationPrefix, item.getId());
        if (f == null || !f.isFile()) {
            throw new FileNotFoundException(item.getId());
        }
        f.setLastModified(item.getDate().getTime());
    }

    @Override
    public void deleteRecordItem(String id) throws FileNotFoundException {
        File f = new File(hiddenLocationPrefix, id);
        if (f != null && f.isFile()) {
            f.delete();
        } else {
            throw new FileNotFoundException(id);
        }
    }

    @Override
    public List<RecordGroup> listRecordItems(RecordQualifier filter) throws FileNotFoundException {
        List<RecordGroup> groups = new ArrayList<>();

        // RecordTypeFilenameFilter uses ids(filenames) to restrict the type of items listed
        File[] files = dir.listFiles(new RecordTypeFilenameFilter(filter));

        List<RecordItem> items = new ArrayList<>();
        if (files != null) {
            for (File f : files) {
                //Log at the finest level the absolute path of the file using a message parameter
                logger.log(Level.FINEST, "Found file: {0}", f.getAbsoluteFile());
                String id = generateRecordId(f);
                RecordItem recordItem = getRecordItem(id);
                items.add(recordItem);
            }
            switch (filter.getSortOrder()) {
                case DATE_DESC:
                    sortItemsByDescDate(items);
                    groupItemsByDate(groups, items);
                    break;
                case DATE_ASC:
                    sortItemsByAscDate(items);
                    groupItemsByDate(groups, items);
                    break;
                case TITLE_DESC:
                    sortItemsByDescTitle(items);
                    groupItemsByTitle(groups, items);
                    break;
                case TITLE_ASC:
                    sortItemsByAscTitle(items);
                    groupItemsByTitle(groups, items);
                    break;
            }
        }
        return groups;
    }
    
    private String generateRecordId(File f) {
        String id = f.getAbsolutePath();
        //when ids are relative paths they always use forwards slashes
        if (hiddenLocationPrefix != null) {
            id = id.replaceFirst(Matcher.quoteReplacement(hiddenLocationPrefix), "");
            id = id.replaceAll(Matcher.quoteReplacement(File.separator), "");
        }
        return id;
    }    
    
    
    private void sortItemsByDescDate(List<RecordItem> items) {
        Collections.sort(items, new Comparator<RecordItem>() {

            @Override
            public int compare(RecordItem o1, RecordItem o2) {
                if (o1.getDate().getTime() > o2.getDate().getTime()) {
                    return -1;
                } else if (o1.getDate().getTime() < o2.getDate().getTime()) {
                    return 1;
                } else {
                    return o1.getId().compareTo(o2.getId());
                }
            }
        });
    }

    private void sortItemsByAscDate(List<RecordItem> items) {
        Collections.sort(items, new Comparator<RecordItem>() {

            @Override
            public int compare(RecordItem o1, RecordItem o2) {
                if (o1.getDate().getTime() < o2.getDate().getTime()) {
                    return -1;
                } else if (o1.getDate().getTime() > o2.getDate().getTime()) {
                    return 1;
                } else {
                    return o1.getId().compareTo(o2.getId());
                }
            }
        });
    }

    private void groupItemsByDate(List<RecordGroup> groups, List<RecordItem> items) {
        String previousDate = "notequal";
        RecordGroup group = null;
        for (RecordItem recordItem : items) {
            String date = SimpleDateFormat.getDateInstance().format(recordItem.getDate());
            if (!previousDate.equals(date)) {
                previousDate = date;
                group = new RecordGroup(date);
                groups.add(group);
            }
            group.getItems().add(recordItem);
        }
    }

    private void sortItemsByAscTitle(List<RecordItem> items) {
        Collections.sort(items, new Comparator<RecordItem>() {

            @Override
            public int compare(RecordItem o1, RecordItem o2) {
                int order = o1.getTitle().compareToIgnoreCase(o2.getTitle());
                if (order == 0) {
                    return o1.getId().compareTo(o2.getId());
                }
                return order;
            }
        });
    }

    private void sortItemsByDescTitle(List<RecordItem> items) {
        Collections.sort(items, new Comparator<RecordItem>() {

            @Override
            public int compare(RecordItem o1, RecordItem o2) {
                int order = o1.getTitle().compareToIgnoreCase(o2.getTitle());
                if (order == 0) {
                    return o1.getId().compareTo(o2.getId());
                }
                if (order > 0) {
                    order = -1;
                } else {
                    order = 1;
                }
                return order;
            }
        });
    }

    private void groupItemsByTitle(List<RecordGroup> groups, List<RecordItem> items) {
        char previousLetter = '\u0000';
        RecordGroup group = null;
        for (RecordItem recordItem : items) {
            char letter = recordItem.getTitle().toUpperCase().charAt(0);
            if (previousLetter != letter) {
                previousLetter = letter;
                group = new RecordGroup(Character.toString(letter));
                groups.add(group);
            }
            group.getItems().add(recordItem);
        }
    }

}
