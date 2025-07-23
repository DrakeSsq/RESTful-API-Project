package online.store.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private String message;
    private Integer status;

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

}
