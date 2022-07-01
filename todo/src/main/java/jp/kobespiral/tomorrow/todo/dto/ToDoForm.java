package jp.kobespiral.tomorrow.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jp.kobespiral.tomorrow.todo.entity.Member;
import lombok.Data;
@Data
public class ToDoForm {
    @Size(min = 1, max = 64)
    @NotBlank
    String title; //todoのタイトル
}