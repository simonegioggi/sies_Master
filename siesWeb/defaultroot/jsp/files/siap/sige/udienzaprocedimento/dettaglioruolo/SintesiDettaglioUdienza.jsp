<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.Utils" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="udienza" scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="ruoloMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProcuratore" scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
	</head>
	
	<body>

	
<% 
		String lCodTipoUff =  
		  UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
%>
		<table cellspacing="2" cellpadding="2">
	  	<tr>
	    	<td class="l">Data Udienza</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy"),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
<% 
		// Verifica se trattasi di una udienza Collegiale.
		if( udienza.getCollegio() != null ) {
%>
      <tr>
      	<td class="l">Numero Collegio</td>
        <td class="l">
        	<font class="campo"><%=udienza.getCollegio().getCodCollegio()%></font>
        </td>
				
      	<td class="l">Sezione</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(udienza.getCollegio().getSezione().getDescrizione(), "-")%>
					</font>
        </td>
      </tr>
<%
			if (udienza.getCollegio().getCollegioMagistrati() != null
					&& udienza.getCollegio().getCollegioMagistrati().length > 0) {
%>
					<tr>
						<td class="l"><%="Presidente"%></td>
						<td class="L">
							<%=udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getCognome()%>
							&nbsp;
							<%=udienza.getCollegio().getCollegioMagistrati()[0].getMagistrato().getNome()%>
						</td>		

					<!-- tr-->
						<td class="l"><%=ruoloMagistrato%></td>
						<td class="L">
<%			
					for( int i=1; i<udienza.getCollegio().getCollegioMagistrati().length; i++ ){	
%>		
						<%=udienza.getCollegio().getCollegioMagistrati()[i].getMagistrato().getCognome()%>
						&nbsp;
						<%=udienza.getCollegio().getCollegioMagistrati()[i].getMagistrato().getNome()%><br>								
<%				}	%>
					</td>
				</tr>
<%			}	
			} else {
%>
	  	<tr>
	    	<td class="l">Giudice</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienza.getDescrMagistratoAss(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
<% 		}%>

	  	<tr>
	    	<td class="l"><%=tipoProcuratore%></td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienza.getDescrProcuratore(),"&nbsp;")%>
					</font>&nbsp;
				</td>
				
	    	<td class="l">Cancelliere</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienza.getDescrIdAssistente(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- 20090402 Come richiesto dall' amministrazione
	  	<tr>
	    	<td class="l">Num. Max. Fascicoli</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getNumeroMaxFascicoli(),"-")%>
					</font>&nbsp;
				</td>
	  	</tr>
--%>
	  	<tr>
	    	<td class="l">Luogo</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienza.getLuogoUdienza(),"-")%>
					</font>&nbsp;
				</td>
	  	</tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- 
	  	<tr>
	    	<td class="l">Orario Inizio</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienza.getOraInizio(),"--")%>:
						<%=StringUtils.toStringJSP(udienza.getMinInizio(),"--")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Orario Fine</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienza.getOraFine(),"--")%>:
						<%=StringUtils.toStringJSP(udienza.getMinFine(),"--")%>
					</font>&nbsp;
				</td>
	  	</tr>
--%>
		</table>

</body>
</html>