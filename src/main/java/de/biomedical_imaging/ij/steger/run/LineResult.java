package de.biomedical_imaging.ij.steger.run;

import de.biomedical_imaging.ij.steger.Lines;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sampanna.kahu on 03/06/17.
 */
public class LineResult {
    private Lines               lines;
    private LineDetectionConfig lineDetectionConfig;

    public LineResult() {
    }

    public LineResult(Lines lines, LineDetectionConfig lineDetectionConfig) {
        this.lines = lines;
        this.lineDetectionConfig = lineDetectionConfig;
    }

    public Lines getLines() {
        return lines;
    }

    public void setLines(Lines lines) {
        this.lines = lines;
    }

    public LineDetectionConfig getLineDetectionConfig() {
        return lineDetectionConfig;
    }

    public void setLineDetectionConfig(LineDetectionConfig lineDetectionConfig) {
        this.lineDetectionConfig = lineDetectionConfig;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lines", lines)
                .append("lineDetectionConfig", lineDetectionConfig)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof LineResult)) {
            return false;
        }

        LineResult that = (LineResult) o;

        return new EqualsBuilder()
                .append(lines, that.lines)
                .append(lineDetectionConfig, that.lineDetectionConfig)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(lines)
                .append(lineDetectionConfig)
                .toHashCode();
    }
}
