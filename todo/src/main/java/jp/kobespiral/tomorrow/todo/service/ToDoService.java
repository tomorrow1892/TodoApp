package jp.kobespiral.tomorrow.todo.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.kobespiral.tomorrow.todo.TodoApplication;
import jp.kobespiral.tomorrow.todo.dto.ToDoForm;
import jp.kobespiral.tomorrow.todo.entity.ToDo;
import jp.kobespiral.tomorrow.todo.exeption.ToDoAppException;
import jp.kobespiral.tomorrow.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ToDoRepository toDoRepository;

    public ToDo createToDo(String mid, ToDoForm form) {
        ToDo newToDo = new ToDo(null, form.getTitle(), mid, false, new Date(), null);
        toDoRepository.save(newToDo);
        return newToDo;
    }

    public ToDo getToDo(Long seq) {
        ToDo t = toDoRepository.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_TODO_EXISTS, seq + ": No such member exists"));
        return t;
    }

    public List<ToDo> getToDoList(String mid) {
        /* midのToDoリストを取得 */
        return toDoRepository.findByMidAndDone(mid, false);
    }

    public List<ToDo> getDoneList(String mid) {
        /* midのDoneリストを取得 */
        return toDoRepository.findByMidAndDone(mid, true);
    }

    public List<ToDo> getToDoList() {
        /* 全員のToDoリストを取得 */
        return toDoRepository.findByDone(false);
    }

    public List<ToDo> getDoneList() {
        /* 全員のDoneリストを取得 */
        return toDoRepository.findByDone(true);
    }

    public void deleteToDo(Long seq){
        toDoRepository.deleteById(seq);
    }

    public ToDo updateToDo(String mid, Long seq,ToDoForm form){
        ToDo todo = getToDo(seq);
        todo.setTitle(form.getTitle());
        toDoRepository.save(todo);
        return todo;
    }
    public ToDo done(String mid,Long seq){
        ToDo todo = toDoRepository.findById(seq).get();
        todo.setDone(true);
        todo.setDoneAt(new Date());
        toDoRepository.save(todo);
        return todo;
    }
}
