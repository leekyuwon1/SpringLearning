package hello.core.order;

public class Order {

    private Long memberId;
    private String itemName;
    private int itemPrice;
    private int discountPrice;

    public Order(Long memberId, String itemName, int itemPrice, int discountPrice) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.discountPrice = discountPrice;
    }

    public int calculatePrice() {
        return itemPrice - discountPrice;
    }
    public Long getMemberId() {
        return memberId;
    }

    public Order setMemberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public Order setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public Order setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
        return this;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public Order setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "memberId=" + memberId +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", discountPrice=" + discountPrice +
                '}';
    }

}
