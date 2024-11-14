package store.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Repeater {
    public static <T> T executeWithRetry(Supplier<T> supplier, Consumer<String> errorHandler) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                errorHandler.accept(e.getMessage());
            }
        }
    }

    public static void executeWhileTrue(Runnable action, Supplier<Boolean> orderStop) {
        do {
            action.run();
        } while (orderStop.get());
    }
}