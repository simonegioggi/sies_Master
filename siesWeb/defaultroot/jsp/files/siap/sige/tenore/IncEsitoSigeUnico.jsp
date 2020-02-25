<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>
<%@ page import="siap.sige.richiesta.model.RichiestaSigeModel"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeModel"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sige.tenore.model.TenoreSigeEstesoModel"%>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>
<%@ page import="f3b.web.html.Option"%>

<jsp:useBean id="esiti" scope="request" class="f3b.web.html.Option"/>
<jsp:useBean id="modifica" scope="request" class="java.lang.String"/>


<html>
<head>
<title>[S.I.A.P.] - Dettaglio Tenori Sige - IncEsitoSigeUnico </title>
</head>

<%
    String tipo=(String)request.getParameter("tipo_esito");
    String idTenore=(String)request.getAttribute("id_tenore");
    String lTipoEsito = request.getParameter("tipo_esito");

	// Elenco dei TenoriEstesoModel
	Vector tenori = null;
	// Possono essere passati attraverso la request
	if (request.getAttribute("tenori") != null)
		tenori = (Vector) request.getAttribute("tenori");
	// Oppure attraverso la session
	else if (session.getAttribute("tenori") != null)
		tenori = (Vector) session.getAttribute("tenori");

	boolean modalita_modifica = false;
	if (modifica != null && modifica.length() > 0)
	    modalita_modifica = true;
	
	String isTitoliEsecutivi=request.getParameter("isTitoliEsecutivi");
	
	if (isTitoliEsecutivi == null)
		isTitoliEsecutivi="false";	
	
  if (tenori != null &&  tenori.size() == 0 )
  {
%>
<font class="campo"> Nessun oggetto associato all'Atto </font>
<%
  } 
  else
  {
%>
 <table  width="85%">

<%

String lCodOggetto = "";
String lIdTenore = "";
String lIdSentenza = "";
String lDescOggetto = "";
//Codice Esito unico
String lCodEsito = "";
//Codice Esito differenziato X Reati
String lCodEsitoTenSenRea = "-";

	   Iterator itx = tenori.iterator();
       while ( itx.hasNext()) {
    	TenoreSigeEstesoModel  lTenore = (TenoreSigeEstesoModel)itx.next();
    	// Esito differenziato X Reati
		if (lTipoEsito.equalsIgnoreCase ("unico") && lTenore.getTenoreSige().getIdTenoreSige().toString().equals(idTenore)){
			//lIdTenore = lTenore.getTenoreSige().getIdTenoreSige().toString();
			lCodEsitoTenSenRea = lTenore.getTenoreSige().getCodEsitoSige();
    		lCodEsito = lTenore.getTenoreSige().getCodEsitoSige();
			lCodOggetto = lTenore.getTenoreSige().getCodOggettoSige();
			lDescOggetto = lTenore.getTenoreSige().getDescrOggettoSige();
			lIdSentenza = "";
%>      
 			<tr>
  			<td class="l" >
  			<font class="campo">&nbsp;<%=lTenore.getTenoreSige().getDescrOggettoSige()%></font>
  			</td>
   			</tr>
<%	if (lTipoEsito.equalsIgnoreCase ("unico")) {
	 
		 if (modalita_modifica && lCodEsito != null && lCodEsito.length() > 1) {
			 esiti.setSelected(lCodEsito);
		 }
%>		
 			<tr> 			
            <td>
           <select Title="Cod Esito" name="<%=ICostantiTenoreSige.CAMPO_COD_ESITO_TENORE_SIGE%>">
            <option value = "-"  />-
            <%=esiti.toString()%>
          </select>
        </td>			
   		<tr>	
<%
 	} // endif Tipo Esito
		} // Endif sul cambio del tenore
			// Sentenza
			
			if (lTipoEsito.equalsIgnoreCase ("unico") && !lTenore.getTenoreSige().getIdTenoreSige().toString().equals(idTenore) && isTitoliEsecutivi.equalsIgnoreCase("false")) {
				continue;
			}
			
			if (!lTenore.getSentenza().getIdSentenza().toString().equalsIgnoreCase(lIdSentenza))
			{
				lIdSentenza = lTenore.getSentenza().getIdSentenza().toString();
%>
 			<tr>
  			<td class="l" width=29%>
  			    <%=lTenore.getSentenza().getCellSentenza() %>
				
  			</td>
  			</tr>
<%		
			} // endif sul cambio sentenza per lo stesso tenore
			
			//Reati
			if(lTenore.getReato() != null) {
		
				// Si ricava ReatoCircostanzaModel dal tenore
				ReatoCircostanzaModel lReatoCircostanza = new ReatoCircostanzaModel(lTenore.getReato());
				
			    // Si passa nella request ReatoCircostanzaModel per renderlo accessibile alla jsp dell'include
			    request.setAttribute("lReatoCircostanza", lReatoCircostanza);
%>
				<tr>
					<td class="l">
					       <jsp:include page="<%=ICostantiDecodifiche.PG_DETTAGLIO_REATO_SIGE%>"/>
					</td>
				</tr>
<%
			} // endif Reato
			
  			if (lTipoEsito.compareTo("unico")!= 0) {
				if (modalita_modifica && lCodEsitoTenSenRea != null && lCodEsitoTenSenRea.length() > 1)
				{
				 	esiti.setSelected(lCodEsitoTenSenRea);
				}
%>		
	 			<tr> 			
		            <td>
			          <select title="Codice Esito" name="<%=ICostantiTenoreSige.CAMPO_COD_ESITO_TEN_SEN_REA%>">
			              <option value = "-"  />-
			              <%=esiti.toString()%>
			          </select> 
		        	</td>
	        	</tr>			
        		<input value=<%=lTenore.getTenoreSige().getTenSenReaId()%> type="hidden" name="<%=ICostantiTenoreSige.CAMPO_ID_TEN_SEN_REA%>" > 
 <%		
  			} // endif Tipo Esito
  	   } // endwhile
 %>
       </table>
 <%
  }  // endif provvedimenti.size()
%>

</html>