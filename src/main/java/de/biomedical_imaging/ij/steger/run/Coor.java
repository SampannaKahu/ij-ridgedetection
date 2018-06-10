package de.biomedical_imaging.ij.steger.run;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author sampanna.kahu.
 */
public class Coor {
    private int x;
    private int y;

    public Coor(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("x", x)
                .append("y", y)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Coor)) {
            return false;
        }

        Coor coor = (Coor) o;

        return new EqualsBuilder()
                .append(x, coor.x)
                .append(y, coor.y)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(x)
                .append(y)
                .toHashCode();
    }
}
