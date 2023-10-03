import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<>(4);
        String addResult = hashMap.put("1","AAA");
        addResult = hashMap.put("2","BBB");
        addResult = hashMap.put("3","DDD");
        addResult = hashMap.put("4","FFFF");
        addResult = hashMap.put("5","BBB");
        addResult = hashMap.put("6","BBB");
        addResult = hashMap.put("7","BBB");
        addResult = hashMap.put("8","BBB");
        addResult = hashMap.put("9","BBB");
        addResult = hashMap.put("10","BBB");


        System.out.println(hashMap);


        for (HashMap.Entity ent :
                hashMap) {
            System.out.println(ent);
        }



    }
}