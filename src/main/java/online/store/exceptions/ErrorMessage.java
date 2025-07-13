package online.store.exceptions;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private String message;
    private Integer status;
    private LocalDateTime time;

    @PrePersist
    public void onCreate() {
        this.time = LocalDateTime.now();
    }

}
