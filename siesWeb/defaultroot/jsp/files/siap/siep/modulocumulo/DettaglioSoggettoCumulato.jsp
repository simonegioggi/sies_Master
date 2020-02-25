<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSoggettoCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.model.SoggettoCumulatoModel" %>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="soggetto" 			scope="request" class="siap.siep.modulocumulo.model.SoggettoCumulatoModel"/>

<!-- 			DettaglioSoggettoCumulato		 -->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Gestione Cumulo - Dettaglio Soggetto Cumulato - </title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    
    <script language="JavaScript">
    //==========================================================================
    // Ritorna all'elenco Titoli coinvolti nell'istruttoria
    //==========================================================================
    function TornaIndietro(action)
    {
      document.DettSoggCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettSoggCumulo.submit();
    }
    
    function VaiaModificare(action)
    {
      document.DettSoggCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettSoggCumulo.modalita.value="M";
      document.DettSoggCumulo.submit();
    }
    
    function VaiaCancellare(action)
    {
    	var retValue = false;	
      	document.DettSoggCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      	document.DettSoggCumulo.modalita.value="C";
		retValue = confirm(" Confermi la Cancellazione ? ");
     
      	if (retValue)
      	{	
      		document.DettSoggCumulo.submit();
      	}	
    }
    
    </script>
  </head>
  <BODY class="corpo">

  <FORM name="comandi" >
    <table>
      <tr>
      
      	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Soggetto</font>
        </td>
        
        <% if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(IstruttoriaCumulo.getFlagStato())) {%>
        <td class="LBG">
        	<a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadInserisciSoggettoCumulato')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modofica" width="24" height="24" border="0">
        	</a>
        	<a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActLoadInserisciSoggettoCumulato')">
          		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
        	</a>
 
        </td>
        <% } %>
        
        <td class="LBG">
        <a href="javascript:TornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
        </td>
      
      </tr>
    </table>
  </FORM>

  <% // INCLUDE DEL DETTAGLIO DEL TITOLO%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettSoggCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="modalita" value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  <input type="hidden" name="<%= ICostantiSoggettoCumulato.CAMPO_ID_SOGGETTO_CUMULATO%>"  value="<%=soggetto.getIdSoggettoCumulato()%>">
    
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l"><font class="label">Cognome e Nome</font></td>
      <td class="l"><font class="campo"><%=soggetto.getCognome() %>&nbsp;&nbsp;<%=soggetto.getNome() %></font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Sesso</font></td>
      <td class="l"><font class="campo"><%=soggetto.getSesso()%>&nbsp;</font></td>
    </tr>
	<tr>
      <td class="l" width="25%">
        <font  class="label">Data di nascita</font>
      </td>
      <td class="l" width="25%">
        <font class="campo">
<%
        if(soggetto.getDataNascita() != null)
        {  %>
          	<%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
<%
        }
        else
        { %>
          	<%="**-"+StringUtils.toStringJSP(soggetto.getMeseNascita(), "**")+"-"+StringUtils.toStringJSP(soggetto.getAnnoNascita())%>&nbsp;
<%
        }
%>
        </font>
      </td>
      <td class="l" width="25%">
        <font class="label">Presunta</font>
      </td>
      <td class="l" width="25%">
        <font class="campo"><%=StringUtils.toStringJSP(soggetto.getDataNascitaPresunta())%></font>
      </td>
    </tr>
    
    <tr>
      <td class="l"><font  class="label">Comune Nascita</font></td>
      <td class="l">
        	<font class="campo">
          	<%=StringUtils.toStringJSP(soggetto.getDescrComuneNascita() )%>
<%        	if( (soggetto.getDescrComuneNascita() != null)
              && (!(soggetto.getDescrComuneNascita().equals("")))
              && (!(soggetto.getDescrComuneNascita().equals("-"))) )
          	{
%>
            		(<%=soggetto.getCodProvinciaNascita()%>)
<%
          	}
%>
          	&nbsp;
        	</font>
      </td>
    </tr>
	
	<tr>
	  <td class="l"><font class="label">Stato Cittadinanza</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrNazionalita(), "-")%>&nbsp;</font></td>
      <td class="l"><font  class="label">Stato Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescrStatoNascita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Comune Di Nascita Estero</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getDescComuneNascitaEstero()).toUpperCase()%>&nbsp;</font></td>
    </tr>
	  <tr>
      <td class="l"><font class="label">Paternità</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getPaternita())%>&nbsp;</font></td>
    </tr>
	 <tr>
      <td class="l"><font class="label">Nome Madre</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNomeMadre())%>
      <%=StringUtils.toStringJSP(soggetto.getCognomeMadre())%>&nbsp;</font></td>
    </tr>
	<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
      <td class="l"><font class="label">Codice Fiscale</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodFiscale())%>&nbsp;</font></td>
      <td class="l"><font class="label">Atto Nascita</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getAttoNascita()) %>&nbsp;</font></td>
    </tr>
    <tr>
      <td class="l"><font class="label">Codice CUI</font></td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(soggetto.getCodAfis() )%>&nbsp;</font></td>
      <td>&nbsp;</td><td>&nbsp;</td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Note</font></td>
      <td class="l" colspan=3><font class="campo"><%=StringUtils.toStringJSP(soggetto.getNote())%>&nbsp;</font></td>
    </tr>
  <br>

  </table>
</FORM>
</body>
</html>