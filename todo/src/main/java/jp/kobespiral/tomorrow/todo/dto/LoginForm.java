package jp.kobespiral.tomorrow.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jp.kobespiral.tomorrow.todo.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @NotBlank
    String mid;
    @NotBlank
    @Size(min = 8)
    String password;
}