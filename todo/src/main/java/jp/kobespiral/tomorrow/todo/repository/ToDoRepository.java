package jp.kobespiral.tomorrow.todo.repository;//変える
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import jp.kobespiral.tomorrow.todo.entity.Member;//変える
import jp.kobespiral.tomorrow.todo.entity.ToDo;
@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long>{
    List<ToDo> findAll();
    List<ToDo> findByMid(String mid);
    List<ToDo> findByMidAndDone(String mid,boolean done); 
    List<ToDo> findByDone(boolean done); 
    
}