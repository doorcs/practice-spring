package hello.core.singleton;

public class StatefulService {

    private int price; // 상태를 가지는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price; // 여기가 문제다!!
    }

    public int getPrice() {
        return price;
    }
}
