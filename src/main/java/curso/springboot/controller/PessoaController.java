package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Despesas;
import curso.springboot.model.Dizimo;
import curso.springboot.model.Pagamento;
import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.DespesasRepository;
import curso.springboot.repository.DizimoRepository;
import curso.springboot.repository.PagamentoRepository;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;
	@Autowired
	private TelefoneRepository telefoneRepository;
    @Autowired
	private ReportUtil reportUtil;
    @Autowired                
	private PagamentoRepository  pagamentoRepository;
	@Autowired
	private DizimoRepository dizimoRepository;

	@Autowired
	private DespesasRepository despesasRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());
		Iterable<Pessoa> pessoasIT = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasIT);
		
		modelAndView.addObject("pagamentos",  pagamentoRepository.findAll());
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView Salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

		pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId()));
		
		pessoa.setDepesas(despesasRepository.getDespesas(pessoa.getId()));
		
		pessoa.setDizimos(dizimoRepository.getDizimos(pessoa.getId()));
		

		if (bindingResult.hasErrors()) {

			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			Iterable<Pessoa> pessoasIT = pessoaRepository.findAll();
			modelAndView.addObject("pessoas", pessoasIT);
			modelAndView.addObject("pessoaobj", pessoa);

			List<String> msg = new ArrayList<String>();

			for (ObjectError obejectError : bindingResult.getAllErrors()) {

				msg.add(obejectError.getDefaultMessage());

			}

			modelAndView.addObject("msg", msg);

			return modelAndView;

		}

		pessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIT = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIT);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;

	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "**/salvarpagamento")
	public ModelAndView CArregarPagamento(@Valid Pagamento pagamento) {

	
		ModelAndView andView = new ModelAndView("cadastro/pagamento");
		

		
		Iterable<Pagamento> pagamentoIT = pagamentoRepository.findAll();
		andView.addObject("pagamentos", pagamentoIT);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;

	}
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpagamento")
	public ModelAndView SalvarPagamento(@Valid Pagamento pagamento) {

	
		ModelAndView andView = new ModelAndView("cadastro/pagamento");
		pagamentoRepository.save(pagamento);

		
		Iterable<Pagamento> pagamentoIT = pagamentoRepository.findAll();
		andView.addObject("pagamentos", pagamentoIT);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIT = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIT);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;

	}

	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", pessoa.get());

		return modelAndView;

	}

	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

		pessoaRepository.deleteById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		Iterable<Pessoa> pessoasIT = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasIT);
		
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;

	}
	
	
	@GetMapping("/removerpagamento/{idpagamento}")
	public ModelAndView excluirpagamento(@PathVariable("idpagamento") Long idpagamento) {

		pagamentoRepository.deleteById(idpagamento);

		ModelAndView modelAndView = new ModelAndView("cadastro/pagamento");
		
		Iterable<Pagamento> pagamentoIT = pagamentoRepository.findAll();
		modelAndView.addObject("pagamentos", pagamentoIT);
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;
	}

	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;

	}
	
	
	
	
	
	@GetMapping("**/pesquisarpessoa")/*gera o relatorio de pessoa*/
	public void imprimePdf(@RequestParam("nomepesquisa") String nomepesquisa,HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		if(nomepesquisa !=null && !nomepesquisa.isEmpty()) {/* busca apenas por nome*/
			
			pessoas = pessoaRepository.findPessoaByName(nomepesquisa);
			
			
		}else {/*busca todos*/
			Iterable<Pessoa> iterator = pessoaRepository.findAll();
			
			for(Pessoa pessoa : iterator) {
				
				pessoas.add(pessoa);
			}
			
		}
		
		/*chama o serviço que faz a geração do relatorio*/
		
		byte[] pdf = reportUtil.gerarRelatorio(pessoas, "pessoa",request.getServletContext());
		
		/*tamanho da resposta*/
		response.setContentLength(pdf.length);
		
		
		/*definir o tipo de arquivo*/
		response.setContentType("application/octet-stream");
		/*Definir o cabeçalho da resposta*/
		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"","relatorio.pdf");
		response.setHeader(headerKey, headerValue);
		
		/*finaliza a resposta para o navegador*/
		
		response.getOutputStream().write(pdf);
		
	}
	
	
	
	
	@PostMapping("**/pesquisardata")
	public ModelAndView pesquisardizimo(@RequestParam("pesquisardata") String pesquisardata) {

		ModelAndView modelAndView = new ModelAndView("cadastro/total");
		modelAndView.addObject("dizimostotal", dizimoRepository.getdataDizimos(pesquisardata));
		modelAndView.addObject("despesastotal",despesasRepository.getdataDespesas(pesquisardata));
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;

	}
	
	
	
	
	
	
	
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));

		return modelAndView;

	}

	@PostMapping("**/addfonePessoa/{pessoaid}")
	public ModelAndView addfonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {

		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();

		if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {

			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			List<String> msg = new ArrayList<String>();

			if (telefone.getNumero().isEmpty()) {
				msg.add("Número deve ser Informado!!");
			}

			if (telefone.getTipo().isEmpty()) {
				msg.add("Tipo deve ser Informado!!");
			}

			modelAndView.addObject("msg", msg);
			return modelAndView;

		}

		telefone.setPessoa(pessoa);
		telefoneRepository.save(telefone);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return modelAndView;

	}

	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {

		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();

		telefoneRepository.deleteById(idtelefone);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));

		return modelAndView;

	}

	@GetMapping("/dizimos/{idpessoa}")
	public ModelAndView dizimos(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/dizimos");
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("dizimos", dizimoRepository.getDizimos(idpessoa));
		modelAndView.addObject("maquinas", pagamentoRepository.findAll());
		modelAndView.addObject("porcentagem", pagamentoRepository.findAll());
		return modelAndView;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping("/dizimostotal/{idpessoa}")
	public ModelAndView dizimostotal(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/total");
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("dizimostotal", dizimoRepository.gettotalDizimos(idpessoa));
		modelAndView.addObject("despesastotal", despesasRepository.gettotalDespesas(idpessoa));
		
		return modelAndView;

	}
	
	
	

	@RequestMapping(method = RequestMethod.GET, value = "/listapessoasdizimos")
	public ModelAndView dizimos() {

		ModelAndView andView = new ModelAndView("cadastro/dizimos");
		Iterable<Pessoa> pessoasIT = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIT);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;

	}

	@PostMapping("**/adddizimoPessoa/{pessoaid}")
	public ModelAndView adddizimoPessoa(Dizimo dizimo, @PathVariable("pessoaid") Long pessoaid) {

		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();

		ModelAndView modelAndView = new ModelAndView("cadastro/dizimos");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("dizimos", dizimoRepository.getDizimos(pessoaid));
		List<String> msg = new ArrayList<String>();

		dizimo.setPessoa(pessoa);
		dizimoRepository.save(dizimo);
		ModelAndView ficar = new ModelAndView("cadastro/dizimos");

		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("dizimos", dizimoRepository.getDizimos(pessoaid));
		modelAndView.addObject("maquinas", pagamentoRepository.findAll());
		
		return modelAndView;

	}

	@GetMapping("/removerdizimo/{iddizimo}")
	public ModelAndView removerdizimo(@PathVariable("iddizimo") Long iddizimo) {

		Pessoa pessoa = dizimoRepository.findById(iddizimo).get().getPessoa();

		dizimoRepository.deleteById(iddizimo);

		ModelAndView modelAndView = new ModelAndView("cadastro/dizimos");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("dizimos", dizimoRepository.getDizimos(pessoa.getId()));
		modelAndView.addObject("maquinas", pagamentoRepository.findAll());
		
		return modelAndView;

	}

	@PostMapping("**/addespesasPessoa/{pessoaid}")
	public ModelAndView addespesasPessoa(Despesas despesa, @PathVariable("pessoaid") Long pessoaid) {

		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();

		ModelAndView modelAndView = new ModelAndView("cadastro/despesas");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("despesas", despesasRepository.getDespesas(pessoaid));
		List<String> msg = new ArrayList<String>();

		despesa.setPessoa(pessoa);
		despesasRepository.save(despesa);
		ModelAndView ficar = new ModelAndView("cadastro/despesas");

		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("despesas", despesasRepository.getDespesas(pessoaid));
		return modelAndView;

	}

	@GetMapping("/despesas/{idpessoa}")
	public ModelAndView despesas(@PathVariable("idpessoa") Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/despesas");
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("despesas", despesasRepository.getDespesas(idpessoa));
		return modelAndView;
	}
	
	
	@GetMapping("/removerdespesa/{iddespesa}")
	public ModelAndView removerdespesa(@PathVariable("iddespesa") Long iddespesa) {

		Pessoa pessoa = despesasRepository.findById(iddespesa).get().getPessoa();

		despesasRepository.deleteById(iddespesa);

		ModelAndView modelAndView = new ModelAndView("cadastro/despesas");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("despesas", despesasRepository.getDespesas(pessoa.getId()));

		return modelAndView;

	}
	
}
