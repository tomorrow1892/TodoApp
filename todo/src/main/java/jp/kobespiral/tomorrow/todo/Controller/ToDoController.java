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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.kobespiral.tomorrow.todo.dto.MemberForm;
import jp.kobespiral.tomorrow.todo.dto.MidForm;
import jp.kobespiral.tomorrow.todo.dto.ToDoForm;
import jp.kobespiral.tomorrow.todo.entity.Member;
import jp.kobespiral.tomorrow.todo.service.MemberService;
import jp.kobespiral.tomorrow.todo.service.ToDoService;

@Controller
public class ToDoController {
   @Autowired
   MemberService mService;
   @Autowired
   ToDoService toDoService;
   

   @GetMapping("/login")
   String login(MidForm form,RedirectAttributes redirectAttributes,Model model){
    String mid = form.getMid();
    return "redirect:/"+mid+"/todos";
   }
   @PostMapping("/{mid}/register")
   String registerToDo(ToDoForm form,@PathVariable String mid,RedirectAttributes redirectAttributes,
   Model model){
    toDoService.createToDo(mid, form);
    return "redirect:/"+ mid + "/todos";
   }

   @PostMapping("/{mid}/{seq}/done")
   String done(@PathVariable String mid,@PathVariable Long seq,Model model){

    System.out.println("mid:"+mid+"seq:" + seq);
    toDoService.toDone(seq);
    return "redirect:/"+ mid + "/todos";
   }
   

   

  

   
//    /**
//     * 管理者用・ユーザ登録確認ページを表示 HTTP-POST /admin/check
//     * @param form
//     * @param model
//     * @return
//     */
//    @PostMapping("/{mid}/todos") 
//    String checkUserForm(@ModelAttribute(name = "ToDoForm") ToDoForm form,  Model model) {
//        model.addAttribute("MidForm", new MidForm());
//        return "check";
//    }
//    /**
//     * 管理者用・ユーザ登録処理 -> 完了ページ HTTP-POST /admin/register
//     * @param form
//     * @param model
//     * @return
//     */
//    @PostMapping("/register")
//    String createUser(@ModelAttribute(name = "MemberForm") MemberForm form,  Model model) {
//        Member m =  mService.createMember(form);
//        model.addAttribute("MemberForm", m);
//        return "registered";
//    }
//    /**
//     * 管理者用・ユーザ削除処理　HTTP-GET /admin/delete/{mid}
//     * @param mid
//     * @param model
//     * @return
//     */
//    @GetMapping("/delete/{mid}")
//    String deleteUser(@PathVariable String mid, Model model) {
//        mService.deleteMember(mid);
//        return showUserForm(model);
//    }
}