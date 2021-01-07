package ir.moke.aparat;

public interface Calculator {
    static long percentage(long total, long obtain) {
        return (obtain * 100) / total;
    }
}
