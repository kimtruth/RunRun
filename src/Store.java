import java.util.Random;

public class Store {
    private String[] products = {"모히또", "블루레몬", "위대한 탄생", "와플", "에낙", "알로에"};
    private String ment = "[주인 할머니] : 항상 고마워 ~! ";
    private String wantToBuy = "";

    Store() {
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < products.length; i++) {
            wantToBuy += products[rand.nextInt(products.length)];
            if (products.length != i + 1)
                wantToBuy += ", ";
        }
        wantToBuy += " 주세요!";
    }

    public String getMent() {
        return ment;
    }

    public String getWantToBuy() {
        return wantToBuy;
    }
}