<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="ListaAnnotazioniValidate" scope="request" class="java.util.Vector" />
<jsp:useBean id="TipoAnnotazione" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Richieste al GE Amnistia/Indulto</title>

    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">
      function ControllaNuovo()
      {
        if(document.elenco.Esistevalidato.value=="SI")
        {
          alert('Impossibile inserire un nuovo beneficio.\nNe esiste uno non validato, cancellarlo o completarlo.');
        }
        else
        {
          <%
          String lActionNuovo = "siap.siep.richiesta.action.ActLoadRichiestaAmnistiaIndulto";

          if (TipoAnnotazione.equals("013")) //incostituzionalità
              lActionNuovo = "siap.siep.richiesta.action.ActLoadRichiestaIncostituzionalita";
            if (TipoAnnotazione.equals("004") ||  //depenalizzazione
            	// MEV 37 - Inizio	
            	TipoAnnotazione.equals("017") )	// Illecito Amministrativo
            	// MEV 37 - Fine	
              lActionNuovo = "siap.siep.richiesta.action.ActLoadRichiestaDepenalizzazione";


          %>
          location.href('<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActionNuovo%>&ForzaInserimento=SI');
        }
      }
   	</script>
  </head>

<body class="corpo" >
<form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione:</font>
        <font class="campo">
          Richieste al GE Amnistia/Indulto
        </font>
      </td>
      <td class="LBG">
        <a href="javascript:ControllaNuovo();" >
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0" >
        </a>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <br>

<%
//==============================================================================
// Tabella con la lista delle richieste con e senza anticipazione
//==============================================================================
int id_record = 0;
if (ListaAnnotazioniValidate.size()!=0)
{
%>
<table style="width: 95%;">
  <tr><td colspan="100%" class="Titolo">Elenco richieste benefici già inseriti:</td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="Titolo">Data Richiesta</td>
    <td class="Titolo">Validata</td>
    <td class="Titolo">Estremi</td>
    <td class="Titolo">Quantum</td>
    <td class="Titolo">+/-</td>
    <td class="Titolo">Anticipazione Effetti</td>
    <td class="Titolo">Dettaglio</td>
  </tr>
<%
  Iterator itx = ListaAnnotazioniValidate.iterator();
  String EsisteNonValidato = "NO";
  for (int i = 0; itx.hasNext(); i++)
  {
    id_record++;
    AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel)itx.next();
%>
  <tr>
    <td class="l" style="text-align:center"><font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMod.getDataRichiesta(),"dd-MM-yyyy"),"&nbsp;")%></font></td>
    <td class="c"><%   if (lAnnMod.getFlagValidato().compareTo("S")==0)
        {%>
          <img src="/images/TickRed.gif">
        <%}else{
        EsisteNonValidato = "SI";
        %>&nbsp;<%}%></td>
    <td class="l"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getDescrTipoAnnotazione() + " " + lAnnMod.getDescrDpr())%></font></td>

    <%lAnnMod.calcolaStringaArresto();
      lAnnMod.calcolaStringaReclusione();%>
    <td class="l"><% if (CalendarUtil.getTotGiorni(lAnnMod.getQuantumReclusione())>0) { %>
                  Reclusione <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getStringaReclusione())%></font>
                  <% } %>
                  <% if (lAnnMod.getImportoMulta()!=null && lAnnMod.getImportoMulta().doubleValue()>0) {%>
                  Multa <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoMulta())%></font>
                  <% } %>
                  <br>
                  <% if (CalendarUtil.getTotGiorni(lAnnMod.getQuantumArresto())>0) { %>
                  Arresto <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getStringaArresto())%></font>
                  <% } %>
                  <% if (lAnnMod.getImportoAmmenda()!=null && lAnnMod.getImportoAmmenda().doubleValue()>0) { %>
                  Ammenda <font class=campo><%=StringUtils.toEuroFormat(lAnnMod.getImportoAmmenda())%></font>
                  <% } %>
    </td>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--td class="l"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getStringaReclusione())%><br>
    <font class=campo><%=StringUtils.toStringJSP(lAnnMod.getStringaArresto())%></font></td--%>
                  
    <td class="l"><font class=campo><%=StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno())%></font></td>
    <%if(lAnnMod.getFlagAppProvvisoria().equals("A")){%>
    <td class="l">SI</font></td>
    <%}else{
      %><td class="l">NO</font></td><%}%>
    <td class="c">
    <%
    if (lAnnMod.getCodOperatoreInserimento()!=null && !lAnnMod.getCodOperatoreInserimento().startsWith("res") )
    {
      if(lAnnMod.getFlagValidato().compareTo("S")==0)
      {%>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste&<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>=<%=lAnnMod.getIdAnnotazioneManuale()%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                  </a>
      <%}else{%>
         <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.richiesta.action.ActLoadDettaglioAnnotazioniAnticipazioniAmnistia&<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>=<%=lAnnMod.getIdAnnotazioneManuale()%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                  </a>
      <% } 
    }
    else{
      // Trattasi di richiesta migrate RES per le quali non è possibile generare altri provvedimenti
      %> migrata <%
    }
    
    
    %>
    </td>
  </tr>
 <% } %>
 <input type="Hidden"  name="Esistevalidato" value="<%=EsisteNonValidato%>">


<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td colspan="100%" class="l">N.B. Selezionare l'icona  <img src="/images/new24.gif" width="12" height="12"> per inserire un nuovo beneficio, oppure selezionare  <img src="/images/dettagli.gif"> per completare l'attività per il beneficio già inserito. </td></tr>
</table>
<%
}
%>


</form>
</body>
</html>