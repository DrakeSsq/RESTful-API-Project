package online.store.report.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Builder
@Getter
public final class ReportDataDto {
    private final UUID id;
    private final BigDecimal oldPrice;
    private final BigDecimal newPrice;
}

