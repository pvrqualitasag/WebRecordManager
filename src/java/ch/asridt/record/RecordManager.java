/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.record;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author pvr
 */
public interface RecordManager {
    public void createRecordItem(RecordItem item, InputStream content) throws IOException;
    
    public void insertNewRecordItem(RecordItem item) throws IOException;
    
    public RecordItem getRecordItem(String id) throws FileNotFoundException ;
    
    public void updateRecordItem(RecordItem item) throws FileNotFoundException;
    
    public void deleteRecordItem(String id) throws FileNotFoundException;
    
    public List<RecordGroup> listRecordItems(RecordQualifier filter) throws FileNotFoundException;    
        
}
