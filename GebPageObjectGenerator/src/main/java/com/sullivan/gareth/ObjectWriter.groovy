package com.sullivan.gareth

import java.io.File;
import java.io.FilenameFilter;

import spock.lang.Specification;

abstract class ObjectWriter {
    
    abstract List populateTemplate();
    
    /**
     * Converts html file(s) to geb, and writes them to disk as .groovy files
     */
    void writeGebFiles() {
        List completedTemplates = populateTemplate()
        makeGebDirectory()
        for (int i = 0; i  < completedTemplates.size(); i ++) {
            File file = new File(GEB_DIR + File.separator + completedTemplates[i].fileName + '.groovy')
            file.write(completedTemplates[i].gebString)
        }
    }
    
    /**
     * If it doesn't already exist, creates a geb directory to store output groovy files
     */
    private void makeGebDirectory() {
        File gebDir = new File(GEB_DIR)
        if (!gebDir.exists()) {
            gebDir.mkdir()
        }
    }
    
    /**
     * Filename filter to search for *.htm or *.html files
     *
     * @author GSULLIVA
     *
     */
    protected final class HtmlFileFilter implements FilenameFilter {
        /**
         * Returns true if filename has a .htm/.hmtl suffix, false otherwise
         *
         * @return  true if filename has a .htm/.hmtl suffix, false otherwise
         */
        boolean accept(File f, String filename) {
                 filename.toLowerCase().endsWith('.htm') || filename.toLowerCase().endsWith('.html')
            }
        }
    
    /**
     * Returns an array of File objects representing htm/html files in the directory
     * specified when creating this object
     *
     * @return array of html File objects
     */
    protected getFileNamesFromDirectory(String filePath) {
        new File(filePath).listFiles(new HtmlFileFilter())
    }
    
    /**
     * Checks whether html path argument passed in on command line is a directory
     *
     * @return true if directory, false otherwise
     */
    protected boolean isProcessingDir(def filePath) {
        new File(filePath).isDirectory()
    }

    static final String GEB_DIR = 'geb'
}