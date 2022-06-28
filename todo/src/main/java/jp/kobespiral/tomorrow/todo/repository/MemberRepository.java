package jp.kobespiral.tomorrow.todo.repository;//変える
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import jp.kobespiral.tomorrow.todo.entity.Member;//変える
@Repository
public interface MemberRepository extends CrudRepository<Member, String>{
    List<Member> findAll();
}