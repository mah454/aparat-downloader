package ir.moke.aparat;

public enum QUALITY {
    LEVEL_1(144),
    LEVEL_2(240),
    LEVEL_3(360),
    LEVEL_4(480),
    LEVEL_5(720);

    private int id;

    QUALITY(int id) {
        this.id = id;
    }

    public static QUALITY getQuality(int id) {
        switch (id) {
            case 144:
                return LEVEL_1;
            case 240:
                return LEVEL_2;
            case 360:
                return LEVEL_3;
            case 720:
                return LEVEL_5;
            default:
                return LEVEL_4;
        }
    }

    public static QUALITY getQuality(String idStr) {
        int id = Integer.parseInt(idStr);
        return getQuality(id);
    }
}
