import domain.SymbolTable;


public class Main {

    public static void main(String[] args) {
	SymbolTable st = new SymbolTable(13);
    st.add("a");
    st.add("abc");
    System.out.println(st.contains("a"));
    System.out.println(st);
    st.remove("a");
    System.out.println(st.contains("a"));
    st.add("goat");
    System.out.println(st);
    }
}
