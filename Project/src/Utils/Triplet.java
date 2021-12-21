package Utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Triplet
 * @param <X> First element
 * @param <Y> Second element
 * @param <Z> Third element
 */
public class Triplet<X, Y, Z> implements Serializable {
    /**
     * First element
     */
    public X x;

    /**
     * Second element
     */
    public Y y;

    /**
     * Third element
     */
    public Z z;

    /**
     * Instantiate Triplet
     * @param x First element
     * @param y Second element
     * @param z Third element
     */
    public Triplet(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Instantiate Triplet
     * @param triplet Triplet's object
     */
    public Triplet(Triplet<X, Y, Z> triplet){
        this.x = triplet.getX();
        this.y = triplet.getY();
        this.z = triplet.getZ();
    }

    /**
     * Get first element
     * @return First element
     */
    public X getX() {
        return x;
    }

    /**
     * Get second element
     * @return Second element
     */
    public Y getY() {
        return y;
    }

    /**
     * Get third element
     * @return Third element
     */
    public Z getZ() {
        return z;
    }

    /**
     * Set first element
     * @param x First element
     */
    public void setX(X x) {
        this.x = x;
    }

    /**
     * Set second element
     * @param y Second element
     */
    public void setY(Y y) {
        this.y = y;
    }

    /**
     * Set third element
     * @param z Third element
     */
    public void setZ(Z z) {
        this.z = z;
    }

    /**
     * Clone this object
     * @return Triplet's object
     */
    @Override
    public Triplet<X,Y,Z> clone(){
        return new Triplet<>(this);
    }

    /**
     * Check the equality between this object and a given object
     * @param o Object
     * @return Equality veracity
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(x, triplet.x) && Objects.equals(y, triplet.y) && Objects.equals(z, triplet.z);
    }

    /**
     * String representation of this object
     * @return String representation of Triplet's object
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Triplet{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append('}');
        return sb.toString();
    }
}
