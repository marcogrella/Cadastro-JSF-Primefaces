package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@SuppressWarnings("unused")
@Component
public class ReportUtil implements Serializable{


	private static final long serialVersionUID = 1L;
	
	
	/* vari�vels para a cria��o do relat�rio */
	
	
	private static final String UNDERLINE = "_";
	private static final String FORLDER_RELATORIOS = "/relatorios";
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
	private static final String EXTENSION_ODS = "ods";
	private static final String EXTENSION_XLS = "xls";
	private static final String EXTENSION_HTML = "html";
	private static final String EXTENSION_PDF = "pdf";
	private String SEPARATOR = File.separator;
	private static final int RELATORIO_PDF = 1;
	private static final int RELATORIO_EXCEL = 2;
	private static final int RELATORIO_HTML = 3;
	private static final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;
	private static final String PONTO = ".";
	private StreamedContent arquivoRetorno = null;
	private String caminhoArquivoRelatorio = null; 
	private JRExporter tipoArquivoExportado = null;
	private String extensaoArquivoExportado = "";
	private String caminhoSubreport_dir = ""; 
	private File arquivoGerado = null;
	
	
	/* m�todo do primefaces para gerar o relat�rio */
	public StreamedContent gerarRelatorio(List<?> listDataBeanCollectionReport,
			HashMap parametrosRelatorio, String nomeRelatorioJasper, 
			String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/* Cria a lista de collectionDataSource de benas que carregam os dados para o relatorio */
		JRBeanCollectionDataSource jrdbcds = new JRBeanCollectionDataSource(listDataBeanCollectionReport);
		
		/* Fornece o caminho f�sico at� a pasta que cont�m os relat�rios compilados .jasper */
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
		String caminhoRelatorio = scontext.getRealPath(FORLDER_RELATORIOS);
		
		/* cria o caminho do relat�rio */
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper" );
		
		/* aqui � uma vefifica��o caso a aplica��o n�o esteja rodando no eclipse, ou seja, em 
		 * um servidor externo. No caso ir� pegar o caminho do relat�rio pelo contexto da aplica��o */
		if(caminhoRelatorio == null 
				|| (caminhoRelatorio != null && caminhoRelatorio.isEmpty())
				|| !file.exists()) {
			/* Caminho dentro do servidor */
			caminhoRelatorio = this.getClass().getResource(FORLDER_RELATORIOS).getPath();
			SEPARATOR = "";
			
		}
		
		/* caminho par imagens para ser inserido no relat�rio */
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);
		
		/* gerar o caminho completo para o relat�rio (passando por uma das duas condi��es acima)  */
		String caminhoArquivojasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "japer";
		
		/* Faz o carregamento do relat�rio indicado */
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivojasper);
		
		/* Seta par�metros SUBREPORT_DIR como caminho f�sico para sub relat�rios. */
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);
		
		/* Carrega o arquivo compilado par a mem�ria */
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, jrdbcds);
		
		/* formato que o tipo do relat�rio ir� receber */
		switch (tipoRelatorio) {
			
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();
			extensaoArquivoExportado = EXTENSION_HTML;
			break;	
			
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();
			extensaoArquivoExportado = EXTENSION_XLS;
			break;		
		
		case RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();
			extensaoArquivoExportado = EXTENSION_ODS;
			break;		
			
		default: 
			tipoArquivoExportado = new JRPdfExporter();
			extensaoArquivoExportado = EXTENSION_PDF;
			break;
		
		}
		
		/* montar o nome do relat�rio */
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();
		
		/* caminho do relat�rio exportado */
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida 
				+ PONTO + extensaoArquivoExportado;
		
		/* at� aqui a cria��o foi feita em mem�ria. Devemos exportar o arquivo gerado  */
		arquivoGerado = new File(caminhoArquivoRelatorio);
		
		/* impress�o a ser preparada */
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
		
		/* nome do arquivo f�osico a ser exportado */
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/* executa a exporta��o */
		tipoArquivoExportado.exportReport();
		
		/* quando o arquivo for gerado e baixado � exclu�do do servidor para n�o ficar 
		 * consumindo recurso */
		arquivoGerado.deleteOnExit();
		
		/* cria o inputStream para ser utilizado pelo PrimeFaces */
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/* Faz o retorno para a aplicaca��o */
		
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/"+extensaoArquivoExportado,
				nomeRelatorioSaida + PONTO + extensaoArquivoExportado);
		
		
		return arquivoRetorno;
		
		
	}
	
	
}
