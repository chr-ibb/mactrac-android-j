package chribb.mactrac.data;

public class MacroOrder {
    //TODO delete this
    long id;
    int order;

    public MacroOrder(long id, int order) {
        this.id = id;
        this.order = order;
    }

    public long getId() { return id; }

    public int getOrder() { return order; }

}