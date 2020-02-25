<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="lMisuraSicurezzaCumulo" scope="request" class="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"/>

<%	String StringaFascicoloClasseIV = "Iscritta al Procedimento N° ";
	String Stringadi=" di ";
%>

<%// DettaglioMisuraSicurezzaCumulo %>
<html>
<head>
  <title> Dettaglio Misura Sicurezza</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  
  <script language="JavaScript">
  function VaiadInserire(action)
  {
    document.DettaglioMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.DettaglioMisuraSicurezzaCumulo.modalita.value="I";
    document.DettaglioMisuraSicurezzaCumulo.submit();
  }
  
  
  function VaiaModificare(action,aIdBen)
  {
    document.DettaglioMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.DettaglioMisuraSicurezzaCumulo.modalita.value="M";
    document.DettaglioMisuraSicurezzaCumulo.submit();
  }
  
  function VaiaCancellare(action, aIdBen, aStato, aMotivoModifica)
  {
    if (aStato=='E' || aStato=='M')
    {
      // Cancellazione Logica Richiedo Motivazione
    document.DettaglioMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      
        document.DettaglioMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
        document.DettaglioMisuraSicurezzaCumulo.modalita.value="C";
        var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                     + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettaglioMisuraSicurezzaCumulo"
                                     + "&" + "<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                     , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
        window.parent.close();
          
          // N.B. la submit viene effettuare direttamnete dalla finestra di popup
    }
    else if (aStato=='I')
    {
      // Cancellazione fisica richiedo conferma
        var retValue = true;
        retValue = confirm("Si vuole procedere con la cancellazione dei dati?"); 
        if (retValue) 
        {
            document.DettaglioMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;

            document.DettaglioMisuraSicurezzaCumulo.<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
            document.DettaglioMisuraSicurezzaCumulo.modalita.value="C";
            document.DettaglioMisuraSicurezzaCumulo.submit();
        }

    }
    
  }
  
   function eseguiFunzione(action)
   {
      document.DettaglioMisuraSicurezzaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettaglioMisuraSicurezzaCumulo.submit();
   }
   
  </script>
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
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Misura Sicurezza</font>
      </td>
      
      <% if( IstruttoriaCumulo.getFlagStato().equals("A") ) { %>   
      <td class="LBG">
        <a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciMisuraSicurezzaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
        <% if(!lMisuraSicurezzaCumulo.getFlagStato().equals("C"))
        { %>   
        <a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadModificaMisuraSicurezzaCumulo', <%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo()%>)">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActCancellaMisuraSicurezzaCumulo', <%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo()%>, '<%=lMisuraSicurezzaCumulo.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lMisuraSicurezzaCumulo.getMotivoModifica()),"") %>')">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>
        <% } %>
      </td>
      <% } %>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaMisuraSicurezzaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
</FORM>

  <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioMisuraSicurezzaCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>"     value="">
  <input type="hidden" name="<%=ICostantiModuloCumulo.MODALITA %>"  value="">
  
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_ID_MISURA_SICUREZZA_CUMULO %>"  value="<%=lMisuraSicurezzaCumulo.getIdMisuraSicurezzaCumulo() %>">
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_FLAG_STATO %>"     value="<%=lMisuraSicurezzaCumulo.getFlagStato() %>">
  <input type="hidden" name="<%=ICostantiMisuraSicurezzaCumulo.CAMPO_MOTIVO_MODIFICA %>"    value="<%=lMisuraSicurezzaCumulo.getMotivoModifica() %>">
  

  <table width="90%" cellspacing="2" cellpadding="2">
    <tr>
      <td class="titolo" colspan="2">Misura di Sicurezza</td>
    </tr>
    <tr>
      <td class="l" width="20%">Natura Misura</td>
      <td class="l"><font class="campo"><%=lMisuraSicurezzaCumulo.getDescrNatura() %></font></td>
    </tr>
    <tr>
      <td class="l">Tipo Misura</td>
      <td class="l"><font class="campo"><%=lMisuraSicurezzaCumulo.getDescrTipo() %></font></td>
    </tr>
    <tr>
      <td class="l">Durata Misura</td>
      <td class="l">
        Anni
        <font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumAnni(),"0") %></font>
        Mesi
        <font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumMesi(),"0") %></font>
        Giorni
        <font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumGiorni(),"0") %></font>
      </td>
    </tr>
    
<%if(lMisuraSicurezzaCumulo.getDataFineValidita() != null )
  { %>   
    <tr>
      <td class="l">Data Fine Validita</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMisuraSicurezzaCumulo.getDataFineValidita(),"dd-MM-yyyy") )%></font>
      </td>
    </tr>
<%}	%>
  
<%
//==========================================================================
// Se la MS è in esecuzione si altro procedimento si riportano gli 
// estremi es:
// - stiamo visualizzando un classe I, la MS è iscritta su un classe IV va 
//   riportato
//==========================================================================
%> 
<%	if(lMisuraSicurezzaCumulo.getAnnoFascicoloSiepIV()!=null )
	{ %>  
	<tr>
      <td class="titolo" colspan="2">Stato Misura di Sicurezza</td>
    </tr>
    <tr>
      <td class="L" colspan="2">
        <font class="campo"><%=StringaFascicoloClasseIV%></font>
      	<font class="cRosso"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getAnnoFascicoloSiepIV())%>/
      						 <%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getNumeroFascicoloSiepIV())%></font>
      	<font class="campo">&nbsp;di&nbsp; <%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrAutoritaEmittenteIV())%></font>
      	<font class="campo">&nbsp;di&nbsp; <%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrLuogoEmittenteIV())%></font>
      </td>
    </tr>  
<%		if(lMisuraSicurezzaCumulo.getFlagStatoMisura().equals("R") ||
			lMisuraSicurezzaCumulo.getFlagStatoMisura().equals("S") )
		{	%>
			<tr>      						 
  			<td class="L">
        		<font class="cRosso"> MISURA  <%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getDescrFlagStatoMisura())%></font>
        	</td>
        	</tr>	
<%		} %> 
		
<% 	}	%>
	
	    
<%if(lMisuraSicurezzaCumulo.getFlagAnnullaMisura() != null && lMisuraSicurezzaCumulo.getFlagAnnullaMisura().compareTo("A") == 0)
  { %> 
    <tr>    
      <td class="l">Stato Misura</td>
      <td class="L"><font class="cRosso"> ANNULLATA </font></td>
    </tr> 
<%} %>
</table>
<br>
    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
    String lStato = "";
    String lDescStato = "";
    if      ( lMisuraSicurezzaCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto dal fascicolo originale";}
    else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
    else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
    else if ( lMisuraSicurezzaCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
    %>
  <table cellspacing="2" cellpadding="2" width="80%"> 
    <tr>
      <td class="l" >Stato &nbsp;&nbsp;&nbsp;</td>
      <td class="l">
        <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
      </td>
    </tr>
  
    <%
    //========================================================================== 
    // Campo note visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //========================================================================== 
    %>
<%if(lMisuraSicurezzaCumulo.getMotivoModifica() != null )
  { %>  
  <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(lMisuraSicurezzaCumulo.getMotivoModifica())%>&nbsp;</font>
      </td>
  </tr>
<%}   %>

</table>
</form>
</body>
</html>