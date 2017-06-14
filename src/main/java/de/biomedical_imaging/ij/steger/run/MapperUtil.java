package de.biomedical_imaging.ij.steger.run;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author prajit.pn.
 */
public class MapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
