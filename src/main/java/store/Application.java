package store;

import store.config.Config;
import store.controller.OrderController;

public class Application {
    public static void main(String[] args) {
        Config config = new Config();
        OrderController orderController = config.orderController();
        orderController.run();
    }
}
