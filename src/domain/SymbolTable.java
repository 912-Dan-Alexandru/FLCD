package domain;

import java.util.ArrayList;

public class SymbolTable {
    private final ArrayList<ArrayList<String>> items;
    private final int size;

    public SymbolTable(int size) {
        this.size = size;
        this.items = new ArrayList<>();
        for(int i=0;i<size;++i) this.items.add(new ArrayList<>());
    }

    public int getSize() {
        return size;
    }

    private int hashfunc(String key) {
        int sum = 0;
        for(int i=0;i< key.length();i++){
            sum += key.charAt(i);
        }
        return sum % size;
    }

    public boolean add(String key){

        int hashValue = hashfunc(key);

        if(!items.get(hashValue).contains(key)){
            items.get(hashValue).add(key);
            return true;
        }
        return false;
    }

    public boolean contains(String key){
        int hashValue = hashfunc(key);

        return items.get(hashValue).contains(key);
    }

    public Pair<Integer, Integer> getPosition(String key){
        if (this.contains(key)){
            int pos = this.hashfunc(key);
            int idx = 0;
            for(String el:this.items.get(pos)) {
                if (!el.equals(key))
                    idx++;
                else
                    break;
            }

            return new Pair<>(pos, idx);
        }
        return new Pair<>(-1, -1);
    }

    public void remove(String key){
        int hashValue = hashfunc(key);

        items.get(hashValue).remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i=0; i<size; ++i) {
            result.append(i).append(": [");
            String separator = "";
            for(String item: items.get(i)){
                result.append(separator);
                separator = ", ";
                result.append(item);
            }
            result.append("]\n");
        }
        return result.toString();
    }
}
