package sample;

public class Holder {
    public static int id;
    public static String level;
    public static String  tempStudent;
    public static String tempModule;

    public static String getLevel() {
        return level;
    }

    public static void setLevel(String level) {
        Holder.level = level;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Holder.id = id;
    }


}
