<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.nio.charset.Charset"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.io.ByteArrayOutputStream"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="java.io.IOException"%>
<%@ page import="org.apache.tika.metadata.*"%>
<%@ page import="org.apache.tika.io.*"%>
<%@ page import="f3b.web.util.TikaParser"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.util.F3BException"%>
<%@ page language="java" session="true" errorPage="../ErrorPage.jsp" %>

<%@ page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>

<%
/*
 * !!!!!!!!!!!!!!!!ATTENZIONE!!!!! 
 * Nelle jsp che richiamano il metodo response.getOutputStream();
 * occorre eliminare qualsiasi carattere, compresi gli spazi e gli a capo, 
 * al di fuori dei tag jsp. 
 * In questo modo si evita l'eccezione lanciata 
 * da Tomcat java.lang.IllegalStateException.
 * Questo perchè secondo le specifiche java non è possibile scrivere 2 volte sullo stream
 * che costituirà la pagina di ritorno, e Tomcat chiama in ogni jsp il metodo in questione...
 * Se non viene scritto alcun carattere al di fuori dei tag jsp, non si accorge che lo stream
 * è già stato chiamato e non si genera l'errore.
 */
ByteArrayOutputStream report = (ByteArrayOutputStream)request.getAttribute("report");

String lDispositionFile = IWebConstants.INLINE_DISPOSITION_FILE; // valore default è inline
if (request.getAttribute(IWebConstants.DISPOSITION_FIELD) != null)
	lDispositionFile = (String)request.getAttribute(IWebConstants.DISPOSITION_FIELD);

String lContentType = null;
String lExtension = null;

// Esegue il controllo formale del documento in download.
TikaParser lTikaParser = new TikaParser(report.toByteArray(), getServletContext(), null);

try {
	lTikaParser.autoDetect(); // Esegue Parsing del file.

   	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   	siesLogger.debug(">> [ DownloadDocument.jsp ] - lTikaParser.getMetadata()  : " + lTikaParser.getMetadata() + " <<" );
	lContentType = lTikaParser.getMetadata().get(Metadata.CONTENT_TYPE);
	lExtension = lTikaParser.getExtension(lContentType);
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug(">> [ DownloadDocument.jsp ] - lExtension : " + lExtension + " <<" );
} catch (Exception ioex) {
	// Controllo errore di eccezione, per documenti RTF, precedenti alla modifica della sovrascrittura del BLOB
	// in TableOracleDAO.update(); del 2009-09-01.
 	// TaggedIOException lIOEx = new TaggedIOException((IOException)ioex,lTikaParser);
   	if (ioex.getMessage().indexOf("org.apache.tika.parser.rtf.RTFParser") > -1) {
   		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
     	siesLogger.warn("Attenzione, errore di formato non valido in documento RTF pregresso . : " + ioex);
     	lContentType = getServletContext().getMimeType(".rtf");
   		lExtension = lTikaParser.getExtension(lContentType);
   	} else
   		throw new F3BException(F3BException.USER_MESSAGE,ioex.getMessage());
   	/*
   	if (ioex.getMessage().equals("Too many close-groups in RTF text")) {
		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
  		siesLogger.warn("Attenzione, errore in documento RTF pregresso di formato non valido. : " + ioex);
  		lContentType = getServletContext().getMimeType(".rtf");
		lExtension = lTikaParser.getExtension(lContentType);
	} else
		throw new F3BException(F3BException.USER_MESSAGE,ioex.getMessage());
	*/
}
 
// Invia il file nella response.
response.reset();
response.setContentType(lContentType);
response.setContentLength(report.toByteArray().length);
response.setHeader("Content-Disposition", lDispositionFile + "; filename=\"Documento." + lExtension + "\"");
// response.getOutputStream().write(report.toByteArray());
// response.getOutputStream().flush();
// [SG] Ticket#20200818015 - errori JBWEB000236 su server.log
String s = new String(report.toByteArray(), Charset.defaultCharset());
char[] c = s.toCharArray();
response.getWriter().write(c);
response.getWriter().flush();
response.getWriter().close();
%>