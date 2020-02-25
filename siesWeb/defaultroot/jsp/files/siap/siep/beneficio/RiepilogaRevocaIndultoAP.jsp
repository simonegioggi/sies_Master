<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="benefici" 				scope="request" class="java.util.Vector"/>
<%
  	FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
   	String lAzione = null;
%>

<html>
	<head>
		<title>[S.I.E.S.] - Elenco Revoca Beneficio - Indulto  </title>

		<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
			<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
			 <script language="JavaScript" src="/html/conferma.js"></script>
			<script language="JavaScript">
			</script>
			
	</head>

  <body class="corpo">
  <FORM method="POST" name="RiepilogaRevocaIndultoAP" action="<%=IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
    	<table>
  		<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      		<td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
     		<font class="campo">ELENCO REVOCHE INDULTO</font>
			</td>
		</tr>
	</table>
    
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
  <div align=center>
  <table>
    <tr>
      <td class="int" width=5%>Natura</td>
      <td class="int">Tipo</td>
      <td class="int">Tipo Provvedimento Concesso</td>
      <td class="int">Data Provvedimento</td>
      <td class="int">Luogo</td>
      <td class="int">Autorita</td>
      <td class="int">Quantum Reclusione</td>
      <td class="int" width=10%>Durata Arresto</td>
      <td class="int">Importo Multa</td>
      <td class="int">Importo Ammenda</td>
      <td class="int" width=10%>Azioni</td>
    </tr>
<%
	BeneficioModel beneficio = new BeneficioModel();



	Iterator itx = benefici.iterator();
	while ( itx.hasNext())
	{
		beneficio = (BeneficioModel)itx.next();
	
%>
  		<tr>
    		<td class=c><%=beneficio.getDescrNaturaBeneficio()%></td>
   			<td class=c> <%=beneficio.getDescrTipoBeneficio()%></td>
			
<% 		
		  // lTipoFunzione per capire che si proviene da iscrizione guidata
%>    	
			<td class=c><%=beneficio.getDescrTipoProvvedimento()%></td>
			<td class=c><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficio.getRifDataProvvedimento(),"dd-MM-yyyy"))%></td>
			<td class=c><%=beneficio.getDescrLuogoAutoritaEmittente()%></td>
			<td class=c><%=beneficio.getDescrTipoAutoritaEmittente()%></td>


	<% 
			if((beneficio.getNumAnniReclusione().intValue()== 0) &&
				(beneficio.getNumMesiReclusione().intValue()== 0) &&
				(beneficio.getNumGiorniReclusione().intValue()== 0))
			{	
	%>	
				<td class=c>  -  </td>
		<% 	}
			else
			{	
		%>		
				<td class=c> Anni   <%=beneficio.getNumAnniReclusione()%> <br>
					 		 Mesi   <%=beneficio.getNumMesiReclusione()%> <br>
					 		 Giorni <%=beneficio.getNumGiorniReclusione()%></td>
		<% 	} %>				 


	<% 
			if((beneficio.getNumAnniArresto().intValue()== 0) &&
				(beneficio.getNumMesiArresto().intValue()== 0) &&
				(beneficio.getNumGiorniArresto().intValue()== 0))
			{	
	%>	
				<td class=c>  -  </td>
		<% 	}
			else
			{	
		%>			

				<td class=c> Anni   <%=beneficio.getNumAnniArresto()%>  <br>
						 	 Mesi   <%=beneficio.getNumMesiArresto()%>  <br>
						 	 Giorni <%=beneficio.getNumGiorniArresto()%></td>
		<% 	} %>
						 
	<% 
			if(beneficio.getImportoMulta().intValue()== 0)
			{	
	%>	
				<td class=c>  -  </td>
	<%
			}
			else
			{	
	%>	
				<td class=c>  Euro   <br> <%=beneficio.getImportoMulta()%> </td>
		<%  } 

 
			if(beneficio.getImportoAmmenda().intValue() ==0)
			{	
		%>	
				<td class=c>  -  </td>
		<%
			}
			else
			{	
		%>	
				<td class=c>  Euro   <br> <%=beneficio.getImportoAmmenda()%> </td>
		<%  } %>
			
    			<td class=c>
      				<jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>">
         			<jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
         			<jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
         			<jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
      				</jsp:include>
    			</td>			
    	  </tr>		
<%}%>
      <tr><td>&nbsp;</td></tr>
    </table>
    <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
    </div>
    
	<table cellspacing=0 cellpadding=0>	 
    	<tr>
        	<td class="l" colspan="4">
          	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActLoadInsRevocaIndultoAP">Inserimento Ulteriore Revoca Indulto </a>
        	</td>        
      	</tr>
	</table>	
    
</form>

</body>
</html>