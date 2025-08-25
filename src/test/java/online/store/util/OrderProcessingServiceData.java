package online.store.util;

import online.store.dto.CustomDto;

import java.util.List;
import java.util.UUID;

public class OrderProcessingServiceData {
    public static List<CustomDto> getData() {
        return  List.of(
                new CustomDto(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        1L,
                        "CREATED",
                        "Test Address",
                        5,
                        "login1",
                        "test@email.com"
                )
        );
    }
}
