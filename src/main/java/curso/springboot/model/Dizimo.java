package curso.springboot.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@Entity
public class Dizimo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private Double dizimo;
	private Double missoes;
	private Double beneficencia;
	private Double construcao;
	private Double oferta;
	private Double outras;
	private Double total;
	private String data;
	private String tipomissoes;
	private Double contribuicao;
	private Double encargo;
	private Double porcentagem;
	
	
	

	
	
	public Double getContribuicao() {
		return contribuicao;
	}

	public void setContribuicao(Double contribuicao) {
		this.contribuicao = contribuicao;
	}

	public Double getEncargo() {
		return encargo;
	}

	public void setEncargo(Double encargo) {
		this.encargo = encargo;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public String getNome() {
		return nome;
	}
	
 public void setNome(String nome) {
	this.nome = nome;
}
 
 
 
	
	public String getTipomissoes() {
		return tipomissoes;
	}
	
	public void setTipomissoes(String tipomissoes) {
		this.tipomissoes = tipomissoes;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Double getTotal() {
		return total;
	}
	
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getData() {
		return data;
	}
	
	
	
	
	public void setDizimo(Double dizimo) {
		this.dizimo = dizimo;
	}
	
	public Double getDizimo() {
		return dizimo;
	}
	public Double getMissoes() {
		return missoes;
	}
	public void setMissoes(Double missoes) {
		this.missoes = missoes;
	}
	public Double getBeneficencia() {
		return beneficencia;
	}
	public void setBeneficencia(Double beneficencia) {
		this.beneficencia = beneficencia;
	}
	public Double getConstrucao() {
		return construcao;
	}
	public void setConstrucao(Double construcao) {
		this.construcao = construcao;
	}
	public Double getOferta() {
		return oferta;
	}
	public void setOferta(Double oferta) {
		this.oferta = oferta;
	}
	public Double getOutras() {
		return outras;
	}
	public void setOutras(Double outras) {
		this.outras = outras;
	}
	@ManyToOne
	@ForeignKey(name="pessoa_id")
	private Pessoa pessoa;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	
	

}
