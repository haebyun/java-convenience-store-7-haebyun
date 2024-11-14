package store.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.domain.order.vo.OrderLine;
import store.domain.order.vo.OrderResult;
import store.domain.order.vo.PromotionLine;

public class OrderTests {
    @ParameterizedTest
    @MethodSource("provideCreateOrderResultData")
    @DisplayName("OrderResult 리스트가 올바르게 생성되는지 확인")
    void testsCreateOrderResultSuccessfully(List<OrderLine> orderLines, List<PromotionLine> promotionLines, List<OrderResult> expectedOrderResults) {
        Order order = Order.of(orderLines, promotionLines);

        List<OrderResult> orderResults = order.createOrderResult();

        assertThat(orderResults).isEqualTo(expectedOrderResults);
    }

    static Stream<Arguments> provideCreateOrderResultData() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                OrderLine.of("사이다", 2, 1500),
                                OrderLine.of("요아정", 1, 2500)
                        ),
                        Arrays.asList(
                                PromotionLine.of("사이다", 1500, 1, 1),
                                PromotionLine.of("요아정", 2500, 0, 0)
                        ),
                        Arrays.asList(
                                OrderResult.of("사이다", 2),
                                OrderResult.of("요아정", 1)
                        )
                ),
                Arguments.of(
                        List.of(
                                OrderLine.of("콜라", 3, 2000)
                        ),
                        Collections.emptyList(),
                        List.of(
                                OrderResult.of("콜라", 3)
                        )
                ),
                Arguments.of(
                        Collections.emptyList(),
                        Collections.emptyList(),
                        Collections.emptyList()
                )
        );
    }
}
