package de.biomedical_imaging.ij.steger.run;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by sampanna.kahu on 03/06/17.
 */
public class LineDetectionConfig {
    private double  sigma;
    private double  upperThreshold;
    private double  lowerThreshold;
    private double  minLength;
    private double  maxLength;
    private boolean isDarkLine;
    private boolean doCorrectPosition;
    private boolean doEstimateWidth;
    private boolean doExtendLine;

    public LineDetectionConfig() {
    }

    public LineDetectionConfig(double sigma, double upperThreshold, double lowerThreshold, double minLength, double maxLength, boolean isDarkLine, boolean doCorrectPosition, boolean doEstimateWidth, boolean doExtendLine) {
        this.sigma = sigma;
        this.upperThreshold = upperThreshold;
        this.lowerThreshold = lowerThreshold;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.isDarkLine = isDarkLine;
        this.doCorrectPosition = doCorrectPosition;
        this.doEstimateWidth = doEstimateWidth;
        this.doExtendLine = doExtendLine;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getUpperThreshold() {
        return upperThreshold;
    }

    public void setUpperThreshold(double upperThreshold) {
        this.upperThreshold = upperThreshold;
    }

    public double getLowerThreshold() {
        return lowerThreshold;
    }

    public void setLowerThreshold(double lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }

    public double getMinLength() {
        return minLength;
    }

    public void setMinLength(double minLength) {
        this.minLength = minLength;
    }

    public double getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(double maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isDarkLine() {
        return isDarkLine;
    }

    public void setDarkLine(boolean darkLine) {
        isDarkLine = darkLine;
    }

    public boolean isDoCorrectPosition() {
        return doCorrectPosition;
    }

    public void setDoCorrectPosition(boolean doCorrectPosition) {
        this.doCorrectPosition = doCorrectPosition;
    }

    public boolean isDoEstimateWidth() {
        return doEstimateWidth;
    }

    public void setDoEstimateWidth(boolean doEstimateWidth) {
        this.doEstimateWidth = doEstimateWidth;
    }

    public boolean isDoExtendLine() {
        return doExtendLine;
    }

    public void setDoExtendLine(boolean doExtendLine) {
        this.doExtendLine = doExtendLine;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sigma", sigma)
                .append("upperThreshold", upperThreshold)
                .append("lowerThreshold", lowerThreshold)
                .append("minLength", minLength)
                .append("maxLength", maxLength)
                .append("isDarkLine", isDarkLine)
                .append("doCorrectPosition", doCorrectPosition)
                .append("doEstimateWidth", doEstimateWidth)
                .append("doExtendLine", doExtendLine)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof LineDetectionConfig)) {
            return false;
        }

        LineDetectionConfig that = (LineDetectionConfig) o;

        return new EqualsBuilder()
                .append(sigma, that.sigma)
                .append(upperThreshold, that.upperThreshold)
                .append(lowerThreshold, that.lowerThreshold)
                .append(minLength, that.minLength)
                .append(maxLength, that.maxLength)
                .append(isDarkLine, that.isDarkLine)
                .append(doCorrectPosition, that.doCorrectPosition)
                .append(doEstimateWidth, that.doEstimateWidth)
                .append(doExtendLine, that.doExtendLine)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sigma)
                .append(upperThreshold)
                .append(lowerThreshold)
                .append(minLength)
                .append(maxLength)
                .append(isDarkLine)
                .append(doCorrectPosition)
                .append(doEstimateWidth)
                .append(doExtendLine)
                .toHashCode();
    }
}
