package com.zaioro.mechanicservice.util.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDetails {
    private String recipient;
    @NotBlank(message = "Field cannot be blank")
    private String msgBody;
    @NotBlank(message = "Field cannot be blank")
    private String subject;
}
