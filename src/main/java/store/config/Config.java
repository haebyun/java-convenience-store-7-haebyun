package store.config;


import store.controller.OrderController;
import store.domain.membership.MembershipCalculator;
import store.loader.ProductDataLoader;
import store.loader.PromotionDataLoader;
import store.view.input.ConsoleInputView;
import store.view.input.InputView;
import store.view.output.ConsoleOutputView;
import store.view.output.OutputView;

public class Config {
    public OrderController orderController() {
        return new OrderController(
                inputView(),
                outputView(),
                new ProductDataLoader(),
                new PromotionDataLoader(),
                new MembershipCalculator()
        );
    }

    public InputView inputView() {
        return new ConsoleInputView();
    }

    public OutputView outputView() {
        return new ConsoleOutputView();
    }
}
