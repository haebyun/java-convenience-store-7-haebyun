# java-convenience-store-precourse

## 편의점 과제 핵심 기능

- 구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
    - 총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출한다.
- 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
- 영수증 출력 후 추가 구매를 진행할지 또는 종료할지를 선택한다.

## 편의점 과제 세부 기능

- 재고
    - 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
    - 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.
    - 재고를 차감함으로써 시스템은 최신 재고 상태를 유지하며, 다음 고객이 구매할 때 정확한 재고 정보를 제공한다.

- 프로모션
    - 오늘 날짜가 프로모션 기간 내에 포함된 경우 할인을 적용한다.
    - 프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행된다.
    - 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
    - 프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
    - 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
    - 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.

- 멤버십
    - 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
    - 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
    - 멤버십 할인의 최대 한도는 8,000원이다.

- 영수증
    - 영수증은 고객의 구매 내역과 할인을 요약하여 출력한다.
    - 구매 상품 내역: 구매한 상품명, 수량, 가격
    - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - 금액 정보
        - 총구매액: 구매한 상품의 총 수량과 총 금액
        - 행사할인: 프로모션에 의해 할인된 금액
        - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - 내실돈: 최종 결제 금액

=== 기능 구현 목록 ===

- 상품(Product)
    - [x] 상품은 이름, 가격(Price), 적용 프로모션 이름을 갖는다.
        - [x] 상품의 가격은 0보다 커야 한다.
    - [x] 재고 수량(Stock)을 필드로 갖는다.
        - [x] 재고 수량은 재고의 갯수를 나타내는 필드를 갖는다.
        - [x] 재고 수량은 생성될 때, 0 이상이여야 한다.
        - [x] 구매하는 수가 재고 수량보다 많다면 예외를 발생한다.
    - [x] 재고 감소 로직을 갖는다.

- 상품 관련 매니저(Products)
    - [x] Product List 형태를 필드로 갖는다.
    - [x] 각 상품의 재고 수량을 고려하여 결제 가능 여부를 결정한다.
    - [x] 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 최신 상태로 관리한다.
    - [x] 주문 상품명에 따른 Promotion이 적용된 상품이 존재하는 지 확인한다.
    - [x] 프로모션이 존재하는 상품 이름으로 PromotionProductInfo라는 조회용 데이터를 만들어서 반환한다.
    - [x] 상품 이름을 파라미터로 주면 상품의 가격을 반환할 수 있는 기능이 존재한다.

- 프로모션(Promotion)
    - [x] 프로모션 이름과 기간(Period), N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 PromotionType을 갖는다.
        - [x] PromotionType으로 무료 증정 갯수를 계산할 수 있다.
    - [x] 상품 주문 날짜에 따라 프로모션이 적용되는 지 확인할 수 있다.
    - [x] N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태(PromotionType)를 갖는다.
    - [x] 주문 날짜에 맞춰 프로모션이 적용 가능한지에 대한 판단할 수 있다.

- 프로모션 매니저(Promotions)
    - [x] 상품 이름, 가격, 프로모션 적용된 재고, 무료 증정 갯수를 필드로 가지는 PromotionResult를 반환한다.
    - [x] 무료 증정을 위한 추가적으로 필요한 갯수, 모든 금액을 내야하는 갯수를 필드로 가지는 PromotionData라는 객체를 만들어서 반환한다.
        - [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있다는 사실을 파악할 수 있다.
        - [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 파악할 수 있다.

- 멤버십(MembershipCalculator)
    - [x] 프로모션 적용 후 남은 금액에 대해 30%를 할인을 적용한다.
    - [x] 할인의 최대 한도는 8,000원이다.

- 주문(Order)
    - [x] 고객의 구매 상품 내역(구매한 상품명, 수량, 가격)을 갖는다.
    - [x] 증정 상품 내역을 갖는다.
    - [x] 총 주문 금액을 계산할 수 있다.
    - [x] 총 증정 혜택 금액을 계산할 수 있다.
    - [x] 프로모션이 적용된 상품의 갯수를 계산할 수 있다.
    - [x] 멤버십 할인의 대상이 되는 상품의 갯수를 계산할 수 있다.
    - [x] 총 주문 갯수를 계산할 수 있다.

- 상품 정보 로더(ProductDataLoader)
    - [x] src/main/resources/products.md 파일을 이용하여 상품 정보를 받는다.
        - [x] List<ProductDTO> 형태로 반환하는 것을 목표로 한다.
        - [x] 만약 PromotionName이 'null'인 경우 "No Promotion"을 저장한다.

- 프로모션 정보 로더(PromotionDataLoader)
  - [x] src/main/resources/promotions.md 파일을 이용하여 프로모션 정보를 받는다.
    - [x] List<PromotionDTO> 형태로 반환하는 것을 목표로 한다.


- 입력
    - [x] 구매할 상품과 수량을 입력 받는다. 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
    - [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다. (Y/N)
    - [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다. (Y/N)
    - [x] 멤버십 할인 적용 여부를 입력 받는다. (Y/N)
    - [x] 추가 구매 여부를 입력 받는다. (Y/N)

- 출력
    - [x] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 만약 재고가 0개라면 재고 없음을 출력한다.
    - [x] 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.
    - [x] 에러 메시지를 출력한다.

```
ex)
===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000
```

## 예외 처리할 사항

- 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`을 발생시킨 후 애플리케이션을 종료한다.
    - 파일 입출력 관련
        - products.md 및 buyNGetOneFreePromotions.md 파일에서 숫자가 들어와야할 곳에 이상한 값이 입력된 경우
        - products.md 파일에서 재고가 음수인 경우
        - products.md 파일에서 가격이 0 이하인 경우
        - products.md 파일에서 상품의 프로모션이 promotions.md에 속하지 않는 경우
        - promotions.md 파일에서 날짜가 정해진 형식이 아닌 경우
    - 구매 상품 및 수량 입력 관련
        - 존재하지 않는 상품을 입력한 경우
        - 수량을 숫자가 아닌 값을 입력한 경우
        - 수량을 0이하로 입력한 경우
        - 주어진 형식에 맞지 않게 입력한 경우
        - 구매 수량이 재고 수량을 초과한 경우
    - 사용자의 선택지 관련
        - Y 혹은 N이 아닌 값을 입력한 경우
