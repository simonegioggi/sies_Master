<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="idFascicolo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoFascicolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"        scope="request" class="siap.sico.evento.model.EventoModel"/>

<html>
	<head>
		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
		<title> Visualizza Certificato Penale </title>
	    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
	    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

		<script language="JavaScript">
	      function VisualizzaCertificato(lAzione){
	    	  var  hrefStampa = lAzione;
	    	  var lIndice = hrefStampa.indexOf("?");
	          var parametri = hrefStampa.substring(lIndice+1,lAzione.length);
	          stampa2("/jsp/files/Stampa.jsp", parametri);
	          
	          lookUpload();
	  	  }
  		</script>
	</head>

	<body class="corpo" onLoad="VisualizzaCertificato('/jsp/Main.jsp?Action=siap.sico.webservice.action.ActLoadCertificatoCasellarioGiudiziale&IDFascicolo=<%=idFascicolo%>&TipoFascicolo=<%=tipoFascicolo%>')"> 

	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="VisualizzaCertificatoPenale">
	<table>
	    <tr>
		    <td class="LBG">
		    	<a href="Javascript:window.print();">
		    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    	</a>
		    </td>
		    <td class="LBG">
		    	<font class="label">Funzione :</font>&nbsp;&nbsp;
		    	<font class="campo">Risultato Richiesta Certificato Penale</font>
			</td>
    	</tr>
	</table>

<%
	if(tipoFascicolo != null && tipoFascicolo.equals("SIEP")){
%>
	  <br>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	  <br>
<%		
	} else {
%>
	  <br>
	   <table>
	      <tr>
	        <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
	      </tr>
	   </table>
	  <br>
<%		
	} 
%>
		<FORM method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="VisualizzaCertificato"> 
			<input type="hidden" name="IDFascicolo" value="<%=idFascicolo %>" />
			<input type="hidden" name="TipoFascicolo" value="<%=tipoFascicolo%>" />
			<input type="hidden" name="idEvento" value="<%=evento.getIdEvento()%>" />

			<div align=center id="visualizzaCertificato" style="visibility:visible;position:absolute;top:150px;left:250px">
     			<table bgcolor="#EEEEEE">
       				<tr>
       					<td>
         					<font size=+1 color=navy>La Richiesta del Certificato Penale è andata a buon fine.</font>
       					</td>
       				</tr>
     			</table>
	   		</div> 	
		</FORM>

<%
	if(tipoFascicolo != null && tipoFascicolo.equals("SIEP")){
%>
		<div align=left style="visibility:hidden; position:relative; top:130px;" id="upld">
	         <FORM name="comandi" enctype="multipart/form-data" method="post">
	            <table>
	          	   <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
	               <tr>
	                  <td class="L">
		                 <input  class=bottone  type="submit" value="Conferma">
		                 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
		                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%= evento.getIdEvento() %>">
		                 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.istruttoria.action.ActDettaglioCertificatoPenale">
	                  	 <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_CK_WARNING%>" value="S">
	                  </td> 
	               </tr>
	            </table>
	
	          </FORM>
	    </div>
	    <br>
<%		
	} 
%>		    
	</body>
</html>