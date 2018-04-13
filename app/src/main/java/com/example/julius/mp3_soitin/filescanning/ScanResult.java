package com.example.julius.mp3_soitin.filescanning;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable object that represents the result of a file scan
 */

public class ScanResult {

    private int successfulReads, failedReads;
    private final List<BadFile> badFiles = new ArrayList<BadFile>();

    public ScanResult(int successfulReads, int failedReads, List<BadFile> badFiles) {
        this.successfulReads = successfulReads;
        this.failedReads = failedReads;
        this.badFiles.addAll(badFiles);
    }

    public int getSuccessfulReads() {
        return successfulReads;
    }

    public List<BadFile> getBadFiles(){
        return badFiles;
    }

    public int getFailedReads() {
        return failedReads;
    }
}
