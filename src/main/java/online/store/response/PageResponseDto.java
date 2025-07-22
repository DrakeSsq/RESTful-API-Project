package online.store.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import online.store.dto.ProductDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PageResponseDto {
    private List<ProductDto> content;
    private Integer offset;
    private Integer limit;
}
