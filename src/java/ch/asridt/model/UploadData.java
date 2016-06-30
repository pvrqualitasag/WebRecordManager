/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.asridt.model;

/**
 *
 * @author pvr
 */
public class UploadData {
    
    private final String fileName;
    private final String title;
    private final String extension;

    
    public UploadData(String fileName, String title, String extension) {
        this.fileName = fileName;
        this.title = title;
        this.extension = extension;
    }

    
    /**
     * Get the value of extension
     *
     * @return the value of extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the value of fileName
     *
     * @return the value of fileName
     */
    public String getFileName() {
        return fileName;
    }

}
