package online.store.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class CustomerInfo {
    private Long id;
    private String login;
    private String accountNumber;
    private String email;
    private String inn;
}
