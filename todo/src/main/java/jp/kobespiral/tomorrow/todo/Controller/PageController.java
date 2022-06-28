package jp.kobespiral.tomorrow.todo.Controller;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.kobespiral.tomorrow.todo.TodoApplication;
import jp.kobespiral.tomorrow.todo.dto.MemberForm;
import jp.kobespiral.tomorrow.todo.dto.MidForm;
import jp.kobespiral.tomorrow.todo.dto.ToDoForm;
import jp.kobespiral.tomorrow.todo.entity.Member;
import jp.kobespiral.tomorrow.todo.service.MemberService;
import jp.kobespiral.tomorrow.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {
   private final ToDoService toDoService;
   private final MemberService memberService;
    @GetMapping("/")
   String showIndexPage(Model model) {
    model.addAttribute("MidForm",new MidForm());
       return "index";
   }

   @GetMapping("/{mid}/todos")
   String showToDosPage(@PathVariable String mid,Model model){
      Member m = memberService.getMember(mid);
    model.addAttribute("todos",toDoService.getToDoList(mid));
    model.addAttribute("dones",toDoService.getDoneList(mid));
    model.addAttribute("todoForm",new ToDoForm());

    model.addAttribute("mid",m.getMid());
    model.addAttribute("name", m.getName());

    return "todos";
   }

   @GetMapping("/{mid}/allToDos")
   String showAllToDosPage(@PathVariable String mid,Model model){
    model.addAttribute("todos",toDoService.getToDoList());
    model.addAttribute("dones",toDoService.getDoneList());
    model.addAttribute("mid",memberService.getMember(mid).getMid());

    return "allToDos";
   }

}