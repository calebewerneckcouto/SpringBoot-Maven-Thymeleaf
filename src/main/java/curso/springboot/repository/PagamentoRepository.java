package curso.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pagamento;
import curso.springboot.model.Pessoa;
@Repository
@Transactional
public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {
    @Query("select p from Pagamento p where p.descricao like %?1% ")
	List<Pagamento> pagamento(String descricao);
	
   
	
}
