/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.rest;

import ch.asridt.bean.FileRecordBean;
import ch.asridt.record.RecordGroup;
import ch.asridt.record.RecordItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author pvr
 */
@Path("record")
public class RecordResource {
    @Context
    private ServletContext context;
    // private fields for context, logging and bean
    private static final Logger logger = Logger.getLogger("ch.asridt.rest.RecordResource");
    private FileRecordBean fmm;
    
    @PostConstruct
    public void init() {
        String realPath = context.getRealPath("fxrecord");
        fmm = new FileRecordBean(realPath);
        fmm.loadData();
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public RecordList getRecord(){
        // instance of RecordList
        RecordList recordList = new RecordList();
        // list of RecordGroup objects
        List<RecordGroup> groups = fmm.getGroups();
        // loop through groups and add id to the list
        for (RecordGroup group : groups){
            for (RecordItem item : group.getItems()){
                recordList.recordId.add(item.getId());
            }
        }
        return recordList;
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public RecordItem getRecordItem(@PathParam("id") String id){
        RecordItem item = null;
        // get the item given by id
        try{
            item = fmm.getRecordManager().getRecordItem(id);
            item.setUri("http://localhost:8080/WebRecordManager/fxrecord/" + item.getId());
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "FileRecordManager did not find a record item with id: " + id, ex);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return item;
    }
    
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteRecordItem(@PathParam("id") String id){
        // delcare a response builder
        ResponseBuilder responseBuilder;
        // the item to be deleted
        RecordItem item = null;
        // trying to delete the item given by id
        try {
            fmm.getRecordManager().deleteRecordItem(id);
            responseBuilder = Response.ok("Deleted item: " + id);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "FileRecordManager cannot delete item: " + id, ex);
            responseBuilder = Response.status(Response.Status.NOT_FOUND);
        }
                
        return responseBuilder.build();
    }
    
    @PUT
    @Path("{fileName}")
    public Response addRecordItem(@PathParam("fileName") String fileName, File file){
        // delcare a response builder
        ResponseBuilder responseBuilder;
        // create a new RecordItem which is supposed to be added
        int periodIndex = fileName.lastIndexOf(".");
        String title = fileName.substring(0, periodIndex);
        RecordItem item = new RecordItem(title, fileName, new Date());
        // try to upload
        try {
            fmm.getRecordManager().createRecordItem(item, new FileInputStream(file));
            responseBuilder = Response.ok("Uploaded item: " + fileName);
        } catch(IOException ex) {
            logger.log(Level.SEVERE, "FileRecordManager failed to access file: " + fileName, ex);
            responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }
}
