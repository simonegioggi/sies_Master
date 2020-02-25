<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="beneficio" scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<%
//==============================================================================
//Form utilizzata nella VISUALIZZAZIONE dei BENEFICI REVOCATI
//(In particolare  SOSPENSIONE CONDIZIONALE in ALTRO PROVVEDIMENTO e 
//					NON MENZIONE in ALTRO PROVVEDIMENTO) 
//==============================================================================

  	FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>
<html>
	<head>
		<title>[S.I.E.S.] - Dettaglio Beneficio Revocato (di tipo Sospensione Condizionale) </title>

		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
			<script language="JavaScript" src="/html/conferma.js"></script>
	</head>
	<body class="corpo">
		<FORM name="comandiRSC" >
    	<table>
      		<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        		<td class="LBG">
        		<font class="label">Funzione :</font>&nbsp;
        		<font class="campo">Dettaglio Beneficio Revocato - Sospensiome/Non Menzione</font>
      			</td>
      
      			<td class="LBG">
        			<jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO%>">
          			<jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
          			<jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
          			<jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
        			</jsp:include>
      			</td>
    		</tr>
  		</table>
  		<br>
    		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  		<br>
		</FORM>
	 
	 	<table cellspacing=2 cellpadding=2 width=70%>
			<tr>
				<td class="l">Natura Beneficio</td>
				<td class="l"><font class="campo"><%=beneficio.getDescrNaturaBeneficio() %></font></td>
			</tr>
			
			<tr>
				<td class="l">Tipologia Beneficio</td>
		    	<td class="l"><font class="campo"><%=beneficio.getDescrTipoBeneficio() %></font></td>
			</tr>
<%			
// New		
%>			
			<tr>
				<td class="l">Inserito il </td>
				<td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficio.getDataInserimento(),"dd-MM-yyyy"))%></font></td>
			</tr>			

        	<tr>
				<td class="l">Tipo Provvedimento </td>
				<td class="l"><font class="campo"><%=beneficio.getDescrTipoProvvedimento() %></font></td>
			</tr>
		
			<tr>
				<td class="l">Data Provvedimento</td>
				<td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficio.getRifDataProvvedimento(),"dd-MM-yyyy"))%></font></td>
			</tr>
<%			
// New	
%>	
			<tr>
				<td class="l">Emesso da</td>
				<td class=l><font class="campo"><%=beneficio.getDescrTipoAutoritaEmittente()%> 
				di 
				<%=beneficio.getDescrLuogoAutoritaEmittente()%></font></td>
			</tr>
		<%
// New		
				String Sezio = beneficio.getRifNumSezioneAutoEmittente();
				if (Sezio != null)
				{  
		%>
					<tr>
						<td class="l">Sezione</td>
						<td class=l><font class="campo"><%=beneficio.getRifNumSezioneAutoEmittente()%> 
						</font></td>
					</tr>					
		<%
   				}
 		%>
 		
			<tr>
				<td class="l">Irrevocabile il</td>
				<td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficio.getRifDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
			</tr> 		
		</table>
	</body>
</html>