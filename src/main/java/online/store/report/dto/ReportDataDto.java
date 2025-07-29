package online.store.report.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDataDto {

    private UUID id;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;

}
