package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Despesas;

@Repository
@Transactional
public interface DespesasRepository extends CrudRepository<Despesas, Long> {
	
    @Query("select d from Despesas d where d.pessoa.id = ?1")
	public List<Despesas> getDespesas(Long pessoaid);
    
    @Query("select d from Despesas d")
   	public List<Despesas> gettotalDespesas(Long pessoaid);
    
    @Query("select d from Despesas d where d.data like %?1% ")
   	public List<Despesas> getdataDespesas(String data);
	
}
