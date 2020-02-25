<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.sico.cssa.model.CSSAModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="penaresidua" 	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="verbale" 		scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="vedoDataIntermedia" scope="request" class="java.lang.String" />
<jsp:useBean id="cssa" 			scope="request" class="siap.sico.cssa.model.CSSAModel" />
<jsp:useBean id="misuraposold" 	scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel" />
<jsp:useBean id="istitutodetenzione" scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" />
<jsp:useBean id="Comunicazione" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="camponota" scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>
<jsp:useBean id="misuraalternativa" 	scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel" />

<html>
<head>
<title>[S.I.E.S.] - Dettaglio  </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">

  function Avanti()
  {
    document.DettaRegistraPenaVariazioneVerbaleSottoscrizione.conferma.disabled=true;
    document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadVariazioneMADecSca&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>&<%="idevento"%>=<%=Comunicazione.getIdEvento()%>&<%="idverbale"%>=<%=verbale.getIdVerbale()%>";

   }
</script>
</head>


<body class="corpo">
<FORM name="comandi" >
<input type="hidden" name="idevento" value="">
<input type="hidden" name="idverbale" value="">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Registrazione Pena Variazione Data Inizio Misura</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="DettaRegistraPenaVariazioneVerbaleSottoscrizione" method="POST" action="/jsp/Main.jsp">
		 <table cellspacing=4 cellpadding=4>

<tr>

    <%if(misuraposold.getCodTipoMisura().equals("0001") || misuraposold.getCodTipoMisura().equals("0002") || misuraposold.getCodTipoMisura().equals("0003"))
       {%>
				<td class="l" colspan=2>Concessione Affidamento in Prova</td>
     <%}else
       if(misuraposold.getCodTipoMisura().equals("0005") || misuraposold.getCodTipoMisura().equals("0010") || misuraposold.getCodTipoMisura().equals("0013"))
       {%>
				<td class="l" colspan=2>Concessione Detenzione Domiciliare</td>
     <%}else
       if(misuraposold.getCodTipoMisura().equals("0004"))
       {%>
				<td class="l" colspan=2>Concessione Semilibertà</td>
     <%}
       else
         if(misuraposold.getCodTipoMisura().equals("2005"))
         {%>
  				<td class="l" colspan=2>Ammissione Provvisoria Detenzione Domiciliare</td>
       <%}
       else
        if(misuraposold.getCodTipoMisura().equals("2006") || misuraposold.getCodTipoMisura().equals("2008"))
           {%>
    				<td class="l" colspan=2>Ammissione Provvisoria ad Affidamento in Prova</td>
         <%}
         else    	   
       		if(misuraposold.getCodTipoMisura().equals("2245"))
       		{%>
				<td class="l" colspan=2>Concessione Sospensione Condizionata esecuzione parte finale pena detentiva</td>
     <%		}else
       		if(misuraposold.getCodTipoMisura().equals("0011"))
       		{%>
         		<td class="l" colspan=2>Concessione Detenzione Domiciliare a Termine</td>
     <%		}%>
    </tr>

<%if(verbale.getIdVerbale() != null )
{ 	
%>    
		<tr>
				<td class="l">Data Pervenimento del Verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
		</tr>
<%
}
if(verbale.getIdVerbale() != null )
{ 	
	if(verbale != null && verbale.getCssIdCssa()!= null && verbale.getCssIdCssa().compareTo(new BigDecimal(0))!=0)
	{%>
		<tr>
				<td class="l">Data Sottoscrizione Prescrizioni</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
	  </tr>
    <tr>
				<td class="l">UEPE Competente che ha inviato il verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())+ " "+StringUtils.toStringJSP(cssa.getIndirizzo()) %></font></td>
		</tr>
<%	}

}	
if(verbale.getIdVerbale() != null )
{	
 	if(verbale != null && verbale.getIstDetIdIstitutoDetenzione()!= null && !verbale.getIstDetIdIstitutoDetenzione().equals("-"))
	{
%>
		<tr>
				<td class="l">Data Ingresso in istituto</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
	  </tr>
    <tr>
				<td class="l">Istituto Competente che ha inviato il verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())+" di "+StringUtils.toStringJSP(istitutodetenzione.getDescrComune()) %></font></td>
		</tr>
<%	}
}
%>

<% 
if(verbale.getIdVerbale() != null )
{	
	if(verbale != null && verbale.getCodTipoUfficioFirmatario() != null
      && !verbale.getCodTipoUfficioFirmatario().equals("-") && verbale.getCodLuogoUfficioFirmatario()!= null
      && !verbale.getCodLuogoUfficioFirmatario().equals("-"))
	{%>
		<tr>
				<td class="l">Data Sottoposizione agli obblighi</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
	  </tr>
    <tr>
				<td class="l">Autorità Competente che ha inviato il verbale</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())+ " di "+StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario()) %></font></td>
		</tr>

		<tr>
				<td class="l">Indirizzo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNote())%>&nbsp;</font></td>
    </tr>
<%	}
}
%>
<!------------------------------ 	VARIAZIONE DATA INIZIO MISURA  	----------------------------------->  
     <tr>
        <td class="Titolo" colspan=10><font  class="label">Variazione Data Inizio Misura</font></td>
	   </tr>
	   
      <tr>
        <td class="l" >Data Pervenimento Richiesta Variazione </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Comunicazione.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
      </tr>

    <tr>
      <td class="l">Motivazioni</td>
      <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(camponota.getDescr())%> </font>
      </td>
    </tr> 
    <tr>
        <td class="l" >Nuova Data Inizio Misura  </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font></td>
    </tr> 
<!---------------------------------------FINE PENA--------------------------------------------------------------->

<table>

     <tr>
        <td class="Titolo" colspan=15><font  class="label">Pena da Espiare</font></td>
	   </tr>

<%
 if(penaresidua.getFlagErgastolo().equals("N"))
 {%>
 		<tr>
            <td class="l"><font  class="label">Reclusione : </font></td>
            <td class="l"><font class="label">Anni</font></td>
			      <td class="r"><font class="campo"><%=penaresidua.getNumAnniReclusione()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
			      <td class="r"><font class="campo"><%=penaresidua.getNumMesiReclusione()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
			     <td class="r"><font class="campo"><%=penaresidua.getNumGiorniReclusione()%></font></td>

            <td class="l"><font  class="label">Arresto :</font></td>
            <td class="l"><font class="label">Anni</font></td>
			      <td class="r"><font class="campo"><%=penaresidua.getNumAnniArresto()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
		      	<td class="r"><font class="campo"><%=penaresidua.getNumMesiArresto()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
			      <td class="r"><font class="campo"><%=penaresidua.getNumGiorniArresto()%></font></td>
   		</tr>

<%}
 else
 {%>
		<tr>
            <td class="l"><font  class="label">Reclusione : </font></td>
         	<td class="l"><font class="campo">ERGASTOLO</font></td>
      	</tr>

  <%}%>
</table>

<table>

      <tr>
        <td class="l"><font  class="label">Data Decorrenza Pena: </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
          </font></td>
          
<%
	if (vedoDataIntermedia.equals("S"))
	{ %>
        <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
          </font></td>

		</tr>

		<tr>
        <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
          </font></td>
 <% } %>
 

 
<%
  if(penaresidua.getFlagErgastolo().equals("N"))
   {
%>
        <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
          <td class="l"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%>
          </font></td>

        </tr>
      <tr>
        <td class="l"><font  class="label">Data Fine Pena Manuale : </font></td>
        <td class="l"><font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>
          </font></td>
      </tr>

<%}%>
    </table>
 
    <!------------------------------ 	MISURA ALTERNATIVA 	----------------------------------->  
<table>

     <tr>
        <td class="Titolo" colspan=10><font  class="label">Durata Misura Alternativa</font></td>
	   </tr>

    <tr>
				<td class="l">Num Anni</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraposold.getNumAnniMisura())%></font></td>

				<td class="l">Num Mesi</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraposold.getNumMesiMisura())%></font></td>

      	<td class="l">Num Giorni</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraposold.getNumGiorniMisura())%></font></td>

        <td class="l">Data Inizio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraposold.getDataInizioMisura(),"dd/MM/yyyy"))%></font></td>

				<td class="l">Data Fine</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraposold.getDataFineMisura(),"dd/MM/yyyy"))%></font></td>
		</tr>
    <tr>
        <td colspan=2><br><INPUT class="bottone" type="button" name="conferma" value="Continua" onClick="javascript:return Avanti()"></td>
    </tr>
</table>

</table>
<!-----------------------------------------FINE PENA------------------------------------------------------------->

     <input type="hidden" name="idpenaresidua" value="<%=penaresidua.getIdPenaResidua()%>">
     <input type="hidden" name="idevento" value="">
     <input type="HIDDEN" name="idverbale" value="">
  </form>
  </body>

</html>