package jp.kobespiral.tomorrow.todo.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.kobespiral.tomorrow.todo.dto.LoginForm;
import jp.kobespiral.tomorrow.todo.dto.ToDoForm;
import jp.kobespiral.tomorrow.todo.dto.UserDetailsImpl;
import jp.kobespiral.tomorrow.todo.entity.Member;
import jp.kobespiral.tomorrow.todo.entity.ToDo;
import jp.kobespiral.tomorrow.todo.exeption.ToDoAppException;
import jp.kobespiral.tomorrow.todo.service.MemberService;
import jp.kobespiral.tomorrow.todo.service.ToDoService;

@Controller
public class ToDoController {
    @Autowired
    MemberService mService;
    @Autowired
    ToDoService tService;

    /**
     * トップページ
     */
    @GetMapping("/sign_in")
    String showIndex(@RequestParam Map<String, String> params, @ModelAttribute LoginForm form, Model model) {
        //パラメータ処理．ログアウト時は?logout, 認証失敗時は?errorが帰ってくる（WebSecurityConfig.java参照） 
		if (params.containsKey("sign_out")) {
			model.addAttribute("message", "サインアウトしました");
		} else if (params.containsKey("error")) {
			model.addAttribute("message", "サインインに失敗しました");
		} 
        //model.addAttribute("loginForm", loginForm);
        return "signin";
    }

    /**
     * ログイン処理．midの存在確認をして，ユーザページにリダイレクト
     */
    @GetMapping("/sign_in_success")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m  = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (m.getRole().equals("ADMIN")) {
            return "redirect:/admin/register";
        }
        return "redirect:/" + m.getMid() + "/todos";
    }

    /**
     * ユーザのToDoリストのページ
     */
    @GetMapping("/{mid}/todos")
    String showToDoList(@PathVariable String mid, @ModelAttribute(name = "ToDoForm") ToDoForm form, Model model) {
        checkIdentity(mid);

        Member m = mService.getMember(mid);
        model.addAttribute("member", m);
        model.addAttribute("ToDoForm", form);
        List<ToDo> todos = tService.getToDoList(mid);
        model.addAttribute("todos", todos);
        List<ToDo> dones = tService.getDoneList(mid);
        model.addAttribute("dones", dones);
        return "list";
    }

    /**
     * 全員のToDoリストのページ
     */
    @GetMapping("/{mid}/todos/all")
    String showAllToDoList(@PathVariable String mid, Model model) {
        checkIdentity(mid);
        Member m = mService.getMember(mid);
        model.addAttribute("member", m);
        List<ToDo> todos = tService.getToDoList();
        model.addAttribute("todos", todos);
        List<ToDo> dones = tService.getDoneList();
        model.addAttribute("dones", dones);
        return "alllist";
    }

    /**
     * ToDoの作成．作成処理後，ユーザページへリダイレクト
     */
    @PostMapping("/{mid}/todos")
    String createToDo(@PathVariable String mid, @Validated @ModelAttribute(name = "ToDoForm") ToDoForm form,
            BindingResult bindingResult, Model model) {
        checkIdentity(mid);

        if (bindingResult.hasErrors()) {
            return showToDoList(mid, form, model);
        }
        tService.createToDo(mid, form);
        return "redirect:/" + mid + "/todos";
    }

    /**
     * ToDoの完了．完了処理後，ユーザページへリダイレクト
     */
    @GetMapping("/{mid}/todos/{seq}/done")
    String doneToDo(@PathVariable String mid, @PathVariable Long seq, Model model) {
        checkIdentity(mid);
        tService.done(mid, seq);
        return "redirect:/" + mid + "/todos";
    }


    /**
     * 認可チェック．与えられたmidがログイン中のmidに等しいかチェックする
     */
    private void checkIdentity(String mid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member m  = ((UserDetailsImpl) auth.getPrincipal()).getMember();
        if (!mid.equals(m.getMid())) {
            throw new ToDoAppException(ToDoAppException.INVALID_TODO_OPERATION, 
            m.getMid() + ": not authorized to access resources of " + mid);
        }
    }
   }


// package jp.kobespiral.tomorrow.todo.Controller;

// import java.util.List; 

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import jp.kobespiral.tomorrow.todo.dto.MemberForm;
// import jp.kobespiral.tomorrow.todo.dto.LoginForm;
// import jp.kobespiral.tomorrow.todo.dto.ToDoForm;
// import jp.kobespiral.tomorrow.todo.entity.Member;
// import jp.kobespiral.tomorrow.todo.service.MemberService;
// import jp.kobespiral.tomorrow.todo.service.ToDoService;

// @Controller
// public class ToDoController {
//    @Autowired
//    MemberService mService;
//    @Autowired
//    ToDoService toDoService;

//    @Autowired
//    PageController pageController;
   

//    @GetMapping("/login")
//    String login(@Validated LoginForm midForm,BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model){
//       if (bindingResult.hasErrors()) {
//          // GETリクエスト用のメソッドを呼び出して、ユーザー登録画面に戻る
//          // redirectAttributes.addFlashAttribute("midForm",midForm);
//          // return "redirect:/";
         
//          return pageController.showIndexPage(midForm, model);
//      }
//     String mid = midForm.getMid();
//     return "redirect:/"+mid+"/todos";
//    }
//    @PostMapping("/{mid}/register")
//    String registerToDo(@Validated @ModelAttribute(name = "todoForm")ToDoForm form,
//    BindingResult bindingResult,@PathVariable String mid,RedirectAttributes redirectAttributes,
//    Model model){
//       if (bindingResult.hasErrors()) {
         
         
//          return pageController.showToDosPage(form, mid,model);
//      }
//     toDoService.createToDo(mid, form);
//     return "redirect:/"+ mid + "/todos";
//    }

//    @PostMapping("/{mid}/{seq}/done")
//    String done(@PathVariable String mid,@PathVariable Long seq,Model model){

//     System.out.println("mid:"+mid+"seq:" + seq);
//     toDoService.toDone(seq);
//     return "redirect:/"+ mid + "/todos";
//    }
   

   

  

   
// //    /**
// //     * 管理者用・ユーザ登録確認ページを表示 HTTP-POST /admin/check
// //     * @param form
// //     * @param model
// //     * @return
// //     */
// //    @PostMapping("/{mid}/todos") 
// //    String checkUserForm(@ModelAttribute(name = "ToDoForm") ToDoForm form,  Model model) {
// //        model.addAttribute("MidForm", new MidForm());
// //        return "check";
// //    }
// //    /**
// //     * 管理者用・ユーザ登録処理 -> 完了ページ HTTP-POST /admin/register
// //     * @param form
// //     * @param model
// //     * @return
// //     */
// //    @PostMapping("/register")
// //    String createUser(@ModelAttribute(name = "MemberForm") MemberForm form,  Model model) {
// //        Member m =  mService.createMember(form);
// //        model.addAttribute("MemberForm", m);
// //        return "registered";
// //    }
// //    /**
// //     * 管理者用・ユーザ削除処理　HTTP-GET /admin/delete/{mid}
// //     * @param mid
// //     * @param model
// //     * @return
// //     */
// //    @GetMapping("/delete/{mid}")
// //    String deleteUser(@PathVariable String mid, Model model) {
// //        mService.deleteMember(mid);
// //        return showUserForm(model);
// //    }
// }