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
	
	
	/* variávels para a criação do relatório */
	
	
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
	
	
	/* método do primefaces para gerar o relatório */
	public StreamedContent gerarRelatorio(List<?> listDataBeanCollectionReport,
			HashMap parametrosRelatorio, String nomeRelatorioJasper, 
			String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/* Cria a lista de collectionDataSource de benas que carregam os dados para o relatorio */
		JRBeanCollectionDataSource jrdbcds = new JRBeanCollectionDataSource(listDataBeanCollectionReport);
		
		/* Fornece o caminho físico até a pasta que contém os relatórios compilados .jasper */
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
		String caminhoRelatorio = scontext.getRealPath(FORLDER_RELATORIOS);
		
		/* cria o caminho do relatório */
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper" );
		
		/* aqui é uma vefificação caso a aplicação não esteja rodando no eclipse, ou seja, em 
		 * um servidor externo. No caso irá pegar o caminho do relatório pelo contexto da aplicação */
		if(caminhoRelatorio == null 
				|| (caminhoRelatorio != null && caminhoRelatorio.isEmpty())
				|| !file.exists()) {
			/* Caminho dentro do servidor */
			caminhoRelatorio = this.getClass().getResource(FORLDER_RELATORIOS).getPath();
			SEPARATOR = "";
			
		}
		
		/* caminho par imagens para ser inserido no relatório */
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);
		
		/* gerar o caminho completo para o relatório (passando por uma das duas condições acima)  */
		String caminhoArquivojasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "japer";
		
		/* Faz o carregamento do relatório indicado */
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivojasper);
		
		/* Seta parâmetros SUBREPORT_DIR como caminho físico para sub relatórios. */
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);
		
		/* Carrega o arquivo compilado par a memória */
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, jrdbcds);
		
		/* formato que o tipo do relatório irá receber */
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
		
		/* montar o nome do relatório */
		nomeRelatorioSaida += UNDERLINE + DateUtils.getDateAtualReportName();
		
		/* caminho do relatório exportado */
		caminhoArquivoRelatorio = caminhoRelatorio + SEPARATOR + nomeRelatorioSaida 
				+ PONTO + extensaoArquivoExportado;
		
		/* até aqui a criação foi feita em memória. Devemos exportar o arquivo gerado  */
		arquivoGerado = new File(caminhoArquivoRelatorio);
		
		/* impressão a ser preparada */
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);
		
		/* nome do arquivo fíosico a ser exportado */
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/* executa a exportação */
		tipoArquivoExportado.exportReport();
		
		/* quando o arquivo for gerado e baixado é excluído do servidor para não ficar 
		 * consumindo recurso */
		arquivoGerado.deleteOnExit();
		
		/* cria o inputStream para ser utilizado pelo PrimeFaces */
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/* Faz o retorno para a aplicacação */
		
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/"+extensaoArquivoExportado,
				nomeRelatorioSaida + PONTO + extensaoArquivoExportado);
		
		
		return arquivoRetorno;
		
		
	}
	
	
}
