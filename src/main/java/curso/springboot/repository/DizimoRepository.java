package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Dizimo;

@Repository
@Transactional
public interface DizimoRepository extends CrudRepository<Dizimo, Long> {
	
    @Query("select d from Dizimo d where d.pessoa.id = ?1")
	public List<Dizimo> getDizimos(Long pessoaid);
    
    @Query("select d from Dizimo d")
   	public List<Dizimo> gettotalDizimos(Long pessoaid);
    
    @Query("select d from Dizimo d where d.data like %?1% ")
    public List<Dizimo> getdataDizimos(String data);
}
