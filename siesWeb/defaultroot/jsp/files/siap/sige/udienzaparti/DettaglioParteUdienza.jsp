<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>

<jsp:useBean id="anagraficaParteUdienza" scope="request" class="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel" />
<jsp:useBean id="difensori"		         scope="request" class="java.util.Vector" />

<jsp:useBean id="idEventoUdienza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaSige"    scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaProcedimentoSige"            scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoParte"     scope="request" class="java.lang.String"/>
<jsp:useBean id="idSoggetto"       scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Parte Udienza</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
function myConfirm(a_action, a_entityname, a_entityvalue, a_other ) {
	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue +  a_other;
	if (window.confirm('Confermi la cancellazione?')) {
		window.location.href=str;
	}
}
</script>

<% 
	String lTitolo = "";
 
	if (anagraficaParteUdienza.getCodParte().equals("F")) //FISICA
	{
    	lTitolo = "Dettaglio Parte Fisica ";
   	} else { //GIURIDICA
    	lTitolo = "Dettaglio Parte Giuridica ";
	} 
	if(anagraficaParteUdienza.getCodTipoPart().equals("O")){
		lTitolo += "Offesa";	
	} else {
		lTitolo += "Civile";
	}
%>
</head>

<body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          	<td class="LBG">
          		<font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font>
      		</td>

          	<td class="LBG">
            	<jsp:include page="<%=ICostantiPartiUdienza.PG_TOOLBAR_HEADER_PARTI%>">
		        	<jsp:param name="CampoIdEntita" value="<%=ICostantiPartiUdienza.CAMPO_ID_SOGGETTO%>" />
		        	<jsp:param name="ValoreIdEntita" value="<%=idSoggetto%>" />
	           		<jsp:param name="CampoIdEventoUdienza" value="<%=ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA%>" />
	           		<jsp:param name="ValoreIdEventoUdienza" value="<%=idEventoUdienza%>" />
	           		<jsp:param name="CampoIdUdienzaSige" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" />
	           		<jsp:param name="ValoreIdUdienzaSige" value="<%=idUdienzaSige%>" />
	           		<jsp:param name="CampoIdUdienzaProcedimentoSige" value="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" />
	           		<jsp:param name="ValoreIdUdienzaProcedimentoSige" value="<%=idUdienzaProcedimentoSige%>" />
	            	<jsp:param name="CampoCodTipoParte" value="<%=ICostantiPartiUdienza.CAMPO_COD_TIPO_PART%>" />
	            	<jsp:param name="ValoreCodTipoParte" value="<%=codTipoParte%>" />
	            	<jsp:param name="Modificabile" value="SI"/>
            	</jsp:include>
          	</td>
            <!-- BOTTONE DI RITORNO -->
            <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
        </tr>
      </table>

	  <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    
    </FORM>

<table cellspacing=2 cellpadding=2>
	<tr>
		<td class="Titolo" colspan="4">Parte</td>
	</tr>
<%
	// Persona Fisica
	if(anagraficaParteUdienza.getCodParte().equals("F")){
%>
	    <tr>
	        <td class="l">Cognome</td>
	        <td class="l">
	           <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCognome())%></font>&nbsp;
	        </td>
	        <td class="l">Nome</td>
	        <td class="l">
	           <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getNome())%></font>&nbsp;
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Sesso</td>
	        <td class="L">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getSesso())%></font>&nbsp;
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
	    </tr>
	    <tr>
	        <td class="l">Data di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
	        </td>
	        <td class="l">Comune di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascita())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Stato di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescrStatoNascita())%></font>&nbsp;
	        </td>
	        <td class="l">Comune di Nascita Estero</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascitaEstero())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Codice Fiscale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscale())%></font>&nbsp;
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
		</tr>
<%
	} else {
	// Persona Giuridica
%>
	    <tr>
	        <td class="l">Società</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDenominazione())%></font>&nbsp;
	        </td>
	        <td class="l">Ragione Sociale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getRagSociale())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Provincia</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescrProvincia())%></font>&nbsp;
	        </td>
	        <td class="l">Partita IVA/Codice Fiscale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscale())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Sede Legale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getIndSedeLegale())%></font>&nbsp;
	        </td>
	        <td class="l">Sede Operativa/Indirizzo Attività</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getIndSedeOperativa())%></font>&nbsp;
	        </td>
		</tr>
<%
	}
%>
</table>

<%
	if(anagraficaParteUdienza.getCodParte().equals("G")){
%>
<br>
<table cellspacing=2 cellpadding=2>
		<tr>
			<td class="Titolo" colspan="4">Rappresentante Legale</td>
		</tr>
	    <tr>
	        <td class="l">Cognome</td>
	        <td class="l">
	           <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCognome())%></font>&nbsp;
	        </td>
	        <td class="l">Nome</td>
	        <td class="l">
	           <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getNome())%></font>&nbsp;
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Sesso</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getSesso())%></font>&nbsp;
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
	    </tr>
	    <tr>
	        <td class="l">Data di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
	        </td>
	        <td class="l">Comune di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascita())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Stato di Nascita</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescrStatoNascita())%></font>&nbsp;
	        </td>
	        <td class="l">Comune di Nascita Estero</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascitaEstero())%></font>&nbsp;
	        </td>
		</tr>
	    <tr>
	        <td class="l">Codice Fiscale</td>
	        <td class="l">
	            <font class="campo"><%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscaleRap())%></font>&nbsp;
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
		</tr>
</table>
<%
	}
%>

<br>

<% if(anagraficaParteUdienza.getResidenza() != null){ %>
	<table cellspacing=2 cellpadding=2>
		<tr>
			<td class="Titolo" colspan="4">Residenza/Domicilio</td>
		</tr>
	
	    <tr>
	        <td class="l">Indirizzo</td>
	        <td class="l">
	           <font class="campo">
	           <% if(anagraficaParteUdienza.getResidenza() != null){ %>
	           		<%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getIndirizzo())%></font>&nbsp;
	           <% } else { %>
	           		&nbsp;</font>&nbsp;
	           <% } %>
	        </td>
	        <td class="l">Luogo</td>
	        <td class="l">
	           <font class="campo">
	           <% if(anagraficaParteUdienza.getResidenza() != null){ %>
	           		<%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getDescrComune())%></font>&nbsp;
	           <% } else { %>
	           		&nbsp;</font>&nbsp;
	           <% } %>
	        </td>
	    </tr>
	
	    <tr>
	        <td class="l">CAP</td>
	        <td class="l">
	           <font class="campo">
	           <% if(anagraficaParteUdienza.getResidenza() != null){ %>
	           		<%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getCap())%></font>&nbsp;
	           <% } else { %>
	           		&nbsp;</font>&nbsp;
	           <% } %>
	        </td>
	        <td class="l">Comune Estero</td>
	        <td class="l">
	           <font class="campo">
	           <% if(anagraficaParteUdienza.getResidenza() != null){ %>
	           		<%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getDescComuneEstero())%></font>&nbsp;
	           <% } else { %>
	           		&nbsp;</font>&nbsp;
	           <% } %>
	        </td>
	    </tr>
	    <tr>
	        <td class="l">Stato</td>
	        <td class="l">
	           <font class="campo">
	           <% if(anagraficaParteUdienza.getResidenza() != null){ %>
	           		<%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getDescrStato())%></font>&nbsp;
	           <% } else { %>
	           		&nbsp;</font>&nbsp;
	           <% } %>
	        </td>
	        <td class="l" colspan="2">&nbsp;</td>
	    </tr>
	</table>
<br>
<%
	}
%>

<table cellspacing=2 cellpadding=2>
    <tr>
        <td class="l">Convocazione Udienza</td>
        <td class="l">
           <font class="campo">
           <% if(anagraficaParteUdienza.getFlagConvUdienza() != null && anagraficaParteUdienza.getFlagConvUdienza() != null && anagraficaParteUdienza.getFlagConvUdienza().equals("S")){ %>
           		Si</font>&nbsp;
           <% } else { %>
           		No</font>&nbsp;
           <% } %>
        </td>
     </tr>

     <tr>
        <td class="l">Domicilio Presso Difensore</td>
        <td class="l">
           <font class="campo">
           <% if(anagraficaParteUdienza.getResidenza() != null && anagraficaParteUdienza.getResidenza().getFlgDomicilioDifensore() != null && anagraficaParteUdienza.getResidenza().getFlgDomicilioDifensore().equals("S")){ %>
           		Si</font>&nbsp;
           <% } else { %>
           		No</font>&nbsp;
           <% } %>
        </td>
     </tr>
</table>

<br>
	     
<table cellspacing=2 cellpadding=2>
<% if(difensori.size() > 0){ %>
	<tr>
		<td class="Titolo" colspan="2">Difensori</td>
	</tr>

	<tr>
		<td colspan="2"> 
			<table width="100%"> 
			<%
			        Iterator itx = difensori.iterator();
			
			        while ( itx.hasNext())
			        {
			        	PartiUdienzaDifensoreModel lDifensore = (PartiUdienzaDifensoreModel)itx.next();
			%>
				        <tr>
				          <td class=l><%=StringUtils.toStringJSP(lDifensore.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lDifensore.getAvvocato().getNome(),"-")%></td>
				          <td class=l><%=StringUtils.toStringJSP(lDifensore.getAvvocato().getForo(),"-")%></td>
				          <td class=l><%=StringUtils.toStringJSP(lDifensore.getAvvocato().getIndirizzo(),"-")%></td>
				          <td class=l><%=StringUtils.toStringJSP(lDifensore.getAvvocato().getDescrTipo(),"-")%></td>
				        </tr>
			<%
			        }
			%>			
			</table>
		</td>
	</tr>
<% } %>
	<tr>
		<td colspan="2"> 
	  		<jsp:include page="<%=ICostantiPartiUdienza.PG_LOAD_DESTINATARI%>"/>
		</td>
	</tr>
		
</table>
	
</body>
</html>