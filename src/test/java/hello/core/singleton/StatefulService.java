package hello.core.singleton;

public class StatefulService {

//    private int price; // 상태를 유지하는 필드

    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
//        this.price = price; // 다중 클라이언트가 사용하는 프라이빗한 저장소는 전역변수로 두면 꼬여버려서 지역변수로 두고 사용
        return price;
    }

//    public int getPrice() {
//        return price;
//    }
}
