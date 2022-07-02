import java.util.HashSet;

public class HyperChannel implements Comparable {
    private Planet to;
    private Planet from;
    private Double cost;

    public HyperChannel(Planet to, Planet from, Double cost) {
        this.to = to;
        this.from = from;
        this.cost = cost;
    }

    public Planet getTo() {
        return to;
    }

    public Planet getFrom() {
        return from;
    }

    public Double getWeight() {
        return cost;
    }

    @Override
    public int compareTo(Object o) {
        double ownCost =this.getWeight();
        double oCost = ((HyperChannel)o).getWeight();
        return java.lang.Double.compare(ownCost,oCost);
    }

    @Override
    public String toString() {
        String res = String.format("From "+from+ " To "+to +" Cost %.6f",cost);
        return res;
    }

}
