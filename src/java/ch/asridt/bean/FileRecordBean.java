/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.bean;

import ch.asridt.record.FileRecordManager;
import ch.asridt.record.RecordGroup;
import ch.asridt.record.RecordItem;
import ch.asridt.record.RecordManager;
import ch.asridt.record.RecordOrder;
import ch.asridt.record.RecordQualifier;
import ch.asridt.record.RecordType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tmcginn
 */
public class FileRecordBean {

    private static final Logger logger = Logger.getLogger("ch.asridt.bean.FileRecordBean");
    private final String appURL = "http://localhost:8080/WebRecordManager/fxrecord/";
    
    private RecordManager mm;
    private int myCount, fiCount;
    private List<RecordGroup> groups = new ArrayList<>();
    private RecordQualifier mq = new RecordQualifier();

    public FileRecordBean(String path) {
        mm = new FileRecordManager(new File(path), FileRecordManager.IdFormat.WEB);
        mq.setSortOrder(RecordOrder.TITLE_ASC);
        mq.setTypes(RecordType.MY, RecordType.FI);
    }
    
    // Use the default RecordQualifier object
    public void loadData() {
        loadData(mq);
    }

    public void loadData(RecordQualifier mq) {
        myCount = 0;
        fiCount = 0;
        try {
            groups = mm.listRecordItems(mq);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "FileRecordManager failed to access its files", ex);
        }
        // Update the counts
        for (RecordGroup group : groups) {
            for (RecordItem recordItem : group.getItems()) {
                switch (recordItem.getType()) {
                    case "my":
                        myCount++;
                        break;
                    case "fi":
                        fiCount++;
                        break;
                }
                recordItem.setUri(appURL + recordItem.getId());
            }
        }
    }

    public void logData(){
        logger.log(Level.INFO, "MY-count: {0}", getMyCount());
        logger.log(Level.INFO, "FI-count: {0}", getFiCount());
        for (RecordGroup group : groups) {
            for (RecordItem recordItem : group.getItems()) {
                logger.log(Level.INFO, "Record item: {0}", recordItem.toString());
            }
        }
    }
    
    public int getMyCount() {
        return myCount;
    }

    public int getFiCount() {
        return fiCount;
    }

    public List<RecordGroup> getGroups() {
        return groups;
    }
    
    public RecordItem getRecordItem(String id) throws FileNotFoundException {
        RecordItem item = mm.getRecordItem(id);
        item.setUri(appURL + item.getId());
        return item;
    }

    public RecordQualifier getQualifier() {
        return mq;
    }

    public RecordManager getRecordManager() {
        return mm;
    }
}
