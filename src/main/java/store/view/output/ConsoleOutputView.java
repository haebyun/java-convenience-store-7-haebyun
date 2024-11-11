package store.view.output;

public class ConsoleOutputView implements OutputView{
    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage + "\n");
    }
}
