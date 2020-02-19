package curso.springboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {
	
	/*retorna pdf em byte para donwload no navegador*/
	public byte[] gerarRelatorio(List listDados,  String relatorio,ServletContext servletContext) throws Exception {
		
		/*cria a lista de dados para o relatorio com nossa lista de objetos para imprimir*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDados);
		
		/* carregar o caminho do arquivo compilado */
		
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + relatorio + ".jasper";
		
		/*Carrega o arquivo jasper passando os dados*/
		
		JasperPrint impressoraJasper =  JasperFillManager.fillReport(caminhoJasper, new HashMap(), jrbcds);
		
		/*Exporta para btye[] fazer download do pdf*/
		return JasperExportManager.exportReportToPdf(impressoraJasper);
		
		
	}

}
