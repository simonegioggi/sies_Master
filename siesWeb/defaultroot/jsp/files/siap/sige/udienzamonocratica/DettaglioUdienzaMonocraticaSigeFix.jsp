<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="ruoloMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzamonocraticasige" scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="descSezione" scope="request" class="java.lang.String"/>
<!-- inizio intervento per 11.1.2 -->
<jsp:useBean id="descrMagAssegnatario" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrProcuratore" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrAssistente" scope="request" class="java.lang.String"/>
<jsp:useBean id="numProcePerUdienza" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<!-- fine intervento per 11.1.2 -->

<html>
  	<head>
    <title>[S.I.E.S.] - Dettaglio Udienza Monocratica Fix </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    
    function confermaBack() {
    	try {
	        var dataUdienza='<%=DateUtils.getDateToString(udienzamonocraticasige.getDataUdienza(), "dd-MM-yyyy")%>';
	        var idUdienza=<%=udienzamonocraticasige.getIdUdienzaSige()%>;
	        <%-- 20171016: [SG] se esiste considero l'ID sezione e lo passo nella funzione setUdienza --%>
	    	var idSezione = '<%=(udienzamonocraticasige.getCodIdSezioneUdienza() != null ? udienzamonocraticasige.getCodIdSezioneUdienza() : "-")%>';
	    	<%-- intervento per 11.2.1 --%>
	    	var codiceMagistrato = '<%=CodMagistrato%>';
	    	
	    	<%-- var idAula = '<%=(udienzamonocraticasige.getCodIdAulaUdienza() != null ? udienzamonocraticasige.getCodIdAulaUdienza() : "")%>';
	    	var descrAula = '<%=(udienzamonocraticasige.getDescrizioneAula() != null ? udienzamonocraticasige.getDescrizioneAula() : "")%>';
	    	var ingresso = '<%=(udienzamonocraticasige.getDescrizioneIngresso() != null ? udienzamonocraticasige.getDescrizioneIngresso() : "")%>';
	    	var piano = '<%=(udienzamonocraticasige.getNumeroPiano() != null ? udienzamonocraticasige.getNumeroPiano() : "")%>';
	    	var oraInizio = '<%=(udienzamonocraticasige.getOraInizio() != null ? udienzamonocraticasige.getOraInizio() : "")%>';
	    	var minutoInizio = '<%=(udienzamonocraticasige.getMinInizio() != null ? udienzamonocraticasige.getMinInizio() : "")%>';
	    	var oraFine = '<%=(udienzamonocraticasige.getOraFine() != null ? udienzamonocraticasige.getOraFine() : "")%>';
	    	var minutoFine = '<%=(udienzamonocraticasige.getMinFine() != null ? udienzamonocraticasige.getMinFine() : "")%>';
	    	var luogoUdi = '<%=(udienzamonocraticasige.getLuogoUdienza() != null ? udienzamonocraticasige.getLuogoUdienza() : "")%>';
	    	
	    	window.opener.setUdienza(idUdienza, dataUdienza, '', idSezione, codiceMagistrato, idAula, descrAula,ingresso, piano,oraInizio,minutoInizio,oraFine,minutoFine, luogoUdi); --%>
	    	
	    	window.opener.setUdienza(idUdienza, dataUdienza, '', idSezione, codiceMagistrato);
	       
	    	self.close();
    	} catch (e) { 
        	history.back();	
        }
    }
    </script>    
</head>
    
<%--
//     function init() {
// if (NumeroUdienzeMagistrato!=null && !"".equals(NumeroUdienzeMagistrato)) {
// 	String mess = "";
// 	if (NumeroUdienzeMagistrato.trim().equals("1")){
// 		mess = "Udienza del magistrato assegnatario trovata";
// 	} else {
// 		mess = "Trovate "+NumeroUdienzeMagistrato+" udienze del magistrato assegnatario, è stata selezionata la più recente";
// 	}
alert ("<%=mess%>");
// }
// 	}
--%>

<%-- 20171013: [SG] rimosso tale messaggio: onload="init()" --%>
  <body class="corpo">
    <FORM name="DettaglioUdienzaMonocratica">
      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Udienza Monocratica</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_FIX%>">
            	<jsp:param name="funzioni" value="si"/>
            	<jsp:param name="tipoRito" value="M"/>
            </jsp:include>
            <a href="javascript:confermaBack()">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>
            
          </td>
        </tr>
      </table>
      
 <input type="HIDDEN" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_COD_MAGISTRATO%>" value="<%=CodMagistrato%>">
 <input type="HIDDEN" name="DescrizioneAula" value="<%=udienzamonocraticasige.getDescrizioneAula()!=null?udienzamonocraticasige.getDescrizioneAula():""%>">

 
 
    </FORM>

		<table cellspacing=4 cellpadding=4>
	  	<tr>
	    	<td class="l">Data Udienza</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(udienzamonocraticasige.getDataUdienza(),"dd-MM-yyyy"),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

      <tr>
      	<td class="l">Sezione</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(descSezione, "&nbsp;")%>
			</font>&nbsp;
        </td>
      </tr>

	  	<tr>
	    	<td class="l">Giudice Udienza</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrGiudice(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

<!--  intervento per 11.2.1 -->
	  	<%if(!"".equals(descrMagAssegnatario)){ %>
	  	<tr>
	    	<td class="l">Giudice Assegnatario</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(descrMagAssegnatario, "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
	  	<%} %>
        <!--  intervento per 11.2.1: il procuratore della repubblica va recuperato dalla tabella Magistrato Assegnatario -->
	  	<tr>
	    	<td class="l">Procuratore della Repubblica</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(descrProcuratore, "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
<!--  intervento per 11.2.1: il Cancelliere va recuperato dalla tabella Magistrato Assegnatario -->
	  	<tr>
	    	<td class="l">Cancelliere</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(descrAssistente, "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	
<!--  intervento per 11.2.1: dettaglio del luogo udienza -->
<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getDescrLuogoUdienza()!=null && !"".equals(udienzamonocraticasige.getDescrLuogoUdienza())){
	%>
	  	<tr>
    	<td class="l">Luogo Udienza</td>
	    	<td class="l">
					<font class="campo">
					<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrLuogoUdienza(), "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
<%} %> 		  		  	

<!--  intervento per 11.2.1: dettaglio di Aula ed Orario -->
	<tr>
	<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getDescrizioneAula()!=null && !"".equals(udienzamonocraticasige.getDescrizioneAula())){
	%>
	
	    	<td class="l">Aula</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrizioneAula(), "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	
	<%} %> 	
	
	<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getDescrizioneIngresso()!=null && !"".equals(udienzamonocraticasige.getDescrizioneIngresso())){
	%>
	
	    	<td class="l">Ingresso</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrizioneIngresso(), "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	
	<%} %> 	
	
		<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getDescrizioneStanza()!=null && !"".equals(udienzamonocraticasige.getDescrizioneStanza())){
	%>
	
	    	<td class="l">Stanza</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrizioneStanza(), "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	
	<%} %> 
	
	<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getNumeroPiano()!=null && !"".equals(udienzamonocraticasige.getNumeroPiano())){
	%>
	
	    	<td class="l">Piano</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getNumeroPiano(), "&nbsp;")%>
					</font>&nbsp;
				</td>
	  	
	<%} %> 
	<tr>	
<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getOraInizio()!=null){
	%>
	
	    	<td class="l">Ora inizio</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getOraInizio(), "&nbsp;")%>
					</font>&nbsp;
			
			
			<%if(udienzamonocraticasige!= null && udienzamonocraticasige.getMinInizio()!=null){
	        %>       
				<font class="campo">
					<%=StringUtils.toStringJSP(":" + udienzamonocraticasige.getMinInizio(), "&nbsp;")%>
				</font>&nbsp;
			</td>
	        <%} %> 
	  	
	<%} %> 	
	
	<%
	if(udienzamonocraticasige!= null && udienzamonocraticasige.getOraFine()!=null){
	%>	
	    	<td class="l">Ora Fine</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getOraFine() , "&nbsp;")%>
					</font>&nbsp;
				
				
				<%if(udienzamonocraticasige!= null && udienzamonocraticasige.getMinFine()!=null){
	        %>
       
				<font class="campo">
					<%=StringUtils.toStringJSP(":" + udienzamonocraticasige.getMinFine(), "&nbsp;")%>
				</font>&nbsp;
			</td>
	        <%} %> 
	        
	  
	<%} %> 	
		</tr>
		</table>

  </body>
</html>