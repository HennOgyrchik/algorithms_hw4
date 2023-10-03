import java.util.Iterator;

public class HashMap<K, V> implements Iterable<HashMap.Entity>{

    //region Публичные методы
    public V put(K key, V value){
        if (buckets.length * LOAD_FACTOR <= size){
            recalculate();
        }
        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket==null){
            bucket = new Bucket();
            buckets[index] = bucket;
        }
        Entity entity = new Entity();
        entity.key = key;
        entity.value = value;

        V buf = (V) bucket.add(entity);

        if (buf == null){
            size++;
        }
        return buf;
    }

    public V get(K key){
        int index =calculateBucketIndex(key);
        Bucket bucket = buckets[index];
        if (bucket ==null){
            return null;
        }
        return (V) bucket.get(key);
    }

    public V remove (K key){
        int index = calculateBucketIndex(key);
        Bucket bucket = buckets[index];

        if (bucket == null){
            return null;
        }
        V buf = (V) bucket.remove(key);
        if (buf != null){
            size--;
        }
        return buf;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Bucket bucket: buckets) {
            if (bucket != null){
                stringBuilder.append(bucket);
            }

        }
        return stringBuilder.toString();
    }

    //endregion


    //region Методы
    private int calculateBucketIndex(K key){
        return  Math.abs(key.hashCode()) % buckets.length;

    }

    private void recalculate(){
        size = 0;
        Bucket<K, V>[] old = buckets;
        buckets = new Bucket[old.length * 2];
        for (int i = 0; i < old.length; i++) {
            Bucket<K, V> bucket = old[i];
            if (bucket == null){
                continue;
            }

            Bucket.Node node = bucket.head;
            while (node != null){
                put((K) node.value.key, (V) node.value.value);
                node = node.next;
            }
        }
    }

    //endregion

    //region Конструкторы
    public HashMap(){
        buckets = new Bucket[INIT_BUCKET_COUNT];
    }

    public HashMap(int capacity){
        buckets = new Bucket[capacity];
    }
    //endregion

    //region Поля
    private Bucket[] buckets;
    private int size;

    //endregion

    //region Константы
    private static final int INIT_BUCKET_COUNT = 16;
    private static final double LOAD_FACTOR = 0.5;




    //endregion

    //region Вспомогательные структуры
    //элемент хэш таблицы
    class Entity{
        K key;
        V value;

        @Override
        public String toString() {
            return key+" - "+value;
        }
    }

    //связный список
    class Bucket<K, V>{
        private  Node head;

        class Node{
            Node next;
            Entity value;

            @Override
            public String toString() {
                return value.toString();
            }
        }

        public V add (Entity entity){
            Node node = new Node();
            node.value = entity;

            if (head == null){
                head = node;
                return null;
            }

            Node current = head;

            while (true){
                if(current.value.key.equals(entity.key)){
                    V buf = (V) current.value.value;
                    current.value.value = entity.value;
                    return buf;
                }

                if (current.next != null){
                    current = current.next;
                }else {
                    current.next=node;
                    return null;
                }
            }
        }

        public V get(K key) {
            Node node = head;
            while (node != null){
                if (node.value.key.equals(key))
                    return (V) node.value.value;
                node = node.next;

            }
            return null;
        }

        public V remove (K key){
            if (head == null)
                return null;
            if (head.value.key.equals(key)){
                V buf = (V) head.value.value;
                head = head.next;
                return buf;
            }else {
                Node current = head;
                while (current.next != null){
                    if (current.next.value.key.equals(key)){
                        V buf = (V) current.next.value.value;
                        current.next = current.next.next;
                        return buf;
                    }
                    current = current.next;
                }
                return null;
            }

        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Node current = head;
            while (current !=null){
                stringBuilder.append(current);
                stringBuilder.append("\n");
                current = current.next;
            }
            return stringBuilder.toString();
        }
    }

   @Override
    public Iterator<HashMap.Entity> iterator() {
        return new HashMapIterator();
    }

    class HashMapIterator implements Iterator<HashMap.Entity>{
        private int bucketIndex=0;
        private Bucket.Node prev;

        @Override
        public boolean hasNext() {
            Bucket.Node temp = prev;
            int tempIndex=bucketIndex;
            if (next() != null){
                bucketIndex = tempIndex;
                prev=temp;
                return true;
            }
            return false;
        }

        @Override
        public Entity next() {
            while (bucketIndex<buckets.length) {

                if (buckets[bucketIndex] != null ) {
                    if (prev == null){
                        prev = buckets[bucketIndex].head;
                        return prev.value;
                    }else {
                        if (prev.next == null){
                            prev = null;
                        }else {
                            prev = prev.next;
                            return prev.value;
                        }
                    }
                }
                bucketIndex++;

            }

            return null;
        }
    }
    //endregion


}
