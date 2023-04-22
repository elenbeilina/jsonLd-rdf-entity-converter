package com.aqualen.springjsonldrdfdtoconverter.constants;

public class LogConstants {

    public static final String STARTED_PROCESS_JSONLD = "Started processing jsonLd.";
    public static final String FINISHED_PROCESS_JSONLD = "Finished processing jsonLd in: {} ms.";
    public static final String STARTED_SPARQL_PROCESS = "Starting sparql process.";
    public static final String FINISHED_SPARQL_PROCESS = "Finished sparql process in: {} ms.";
    public static final String STARTED_MAPPING_PROCESS = "Starting mapping to Entity process.";

    private LogConstants() {
        throw new IllegalStateException("Logs class");
    }
}
