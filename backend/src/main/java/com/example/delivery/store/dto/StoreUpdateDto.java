package com.example.delivery.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreUpdateDto {

    @Schema(description = "변경할 가게 이름", example = "맛있는 김밥집")
    private String storeName;

    @Schema(description = "변경할 사업자 이름", example = "홍길동")
    private String storeOwnerName;

    @Schema(description = "변경할 가게 위치", example = "서울특별시 강남구")
    private String storeLocation;

    @Schema(description = "영업 상태 (true: 영업 중, false: 영업 종료)", example = "true")
    private boolean opened;

    @Schema(description = "변경할 카테고리 이름", example = "한식")
    private String categoryName;

}
