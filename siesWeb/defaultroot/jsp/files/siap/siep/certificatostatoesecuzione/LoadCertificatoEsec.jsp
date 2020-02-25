<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel"%>
<%@ page import="siap.siep.certificatostatoesecuzione.action.ICostantiCertificatoStatoEsec"%>

<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="UfficioCompetenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoCertificati" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Stampa certificato Esecuzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
	<script language="JavaScript">
		function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2,valoreordinamento){
			var documentoRegistrato = a_entityname2;
		  	if (window.confirm('Confermi la cancellazione ?')){
				str = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.certificatostatoesecuzione.action.ActCancellaCertificatoStatoEsec&" 
					+a_parameter +"=" + a_entityname;
			    window.location.href=str;			    
		  	}
		}
		//controllo che venga allegato un file
		function salvaFile(){			
			if(document.comandi.<%=ICostantiCertificatoStatoEsec.CAMPO_BLOB%>.value!=''){
				document.comandi.<%=ICostantiCertificatoStatoEsec.CAMPO_FLAG_UPLOAD%>.value='1';
				document.comandi.submit();
			}
			else{
				alert("E' obbligatorio allegare un file!");
				return false;
			}
		}	
	</script>
  </head>

 <body class="corpo" >
	<table>
	    <tr>
	    	<td class="LBG">
	    		<a href="Javascript:window.print();">
	    			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
	    		</a>
	    	</td>
			<td class="LBG">
				<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Stampa Certificato Esecuzione</font>
			</td>			
		<%
		//pulsante per la stampa in formato RTF
		if(UfficioCompetenza.equals("1")){%>
			<!-- BOTTONE DI STAMPA RTF -->
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.certificatostatoesecuzione.action.ActStampaCertificatoEsec"%>"/>
			</jsp:include>
			<!-- FINE BOTTONE -->
		<%}
		else{%>
			<!-- BOTTONE DI STAMPA PDF -->
			<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_PDF_SIEP%>">
				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.certificatostatoesecuzione.action.ActStampaCertificatoEsecPdf"%>"/>
			</jsp:include>
			<!-- FINE BOTTONE -->
		<%}%>
		</tr>
	</table>  
	<br>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  	<br>
  <%if(UfficioCompetenza.equals("1") && ElencoCertificati.size()>0){%>
  <table cellpadding="2" cellspacing="2">
	<tr>
		<td class="int" width="12%">Data Certificato</td>
		<td class="int">Operatore</td>
		<td class="int">Annotazioni</td>
		<td class="int" width="5%"></td>
    </tr>    
  <%
  	String certificati = "";  
	Iterator itx = ElencoCertificati.iterator();
	while ( itx.hasNext())
	{		
		certificati = "";
		CertificatoStatoEsecModel nCert = (CertificatoStatoEsecModel)itx.next();
		
		String dataIns = DateUtils.getDateToString(nCert.getDataInserimento(),"dd-MM-yyyy");

		certificati = "<tr><td class=\"c\">" + dataIns + "</td>";
		certificati += "<td class=\"c\">" + StringUtils.toStringJSP(nCert.getCodOperatoreInserimento()) + "</td>";
		certificati += "<td class=\"c\">" + StringUtils.toStringJSP(nCert.getAnnotazioni()) + "&nbsp;</td>";		
  	%>      
		<%=certificati%>
			<td class="c">
				<!-- Inserimento bottoni per la visualizzione, la modifica e la cancellazione -->
				<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
		           	<jsp:param name="CampoIdEntita" value="<%=ICostantiCertificatoStatoEsec.CAMPO_ID_CERTIFICATO_STATO_ESEC%>" />
					<jsp:param name="ValoreIdEntita" value="<%=nCert.getIdCertificatoStatoEsec()%>" />					
		       	</jsp:include>
	       	</td>
       	</tr>
	<%} %>
    </table>
    <%}%>

  <br>  
  <div align=left style="visibility:hidden" id="upld">
  <%if(UfficioCompetenza.equals("1")){ %>
    <FORM name="comandi" enctype="multipart/form-data" method="post">
      <table>
          <tr>
          <td class="L">Annotazioni</td>
          <td class="L">
            <textarea cols="50" rows="5" name="<%=ICostantiCertificatoStatoEsec.CAMPO_ANNOTAZIONI%>"></textarea>
          </td>
        </tr>
        <tr>
          <td class="l" rowspan=2>Indica il percorso locale del documento da salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiCertificatoStatoEsec.CAMPO_BLOB%>"></font>            
          </td>
        </tr>
        <tr>
          <td class="L">
            <input  class=bottone  type="button" value="Conferma" onclick="salvaFile()">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.certificatostatoesecuzione.action.ActUploadCertSE">           
            <input type="HIDDEN" name="<%=ICostantiCertificatoStatoEsec.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.certificatostatoesecuzione.action.ActLoadCertificatoEsec">
          </td>
        </tr>
      </table>
      <input name="<%=ICostantiCertificatoStatoEsec.CAMPO_FLAG_UPLOAD%>" type="hidden" value=0>     
    </FORM> 
    <%} %>      
  </div>
</body>
</html>