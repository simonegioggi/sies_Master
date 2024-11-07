<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Set"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.sico.cssa.model.CSSAModel"%>

<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="verbale" scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="vedoDataIntermedia" scope="request" class="java.lang.String" />
<jsp:useBean id="cssa" scope="request" class="siap.sico.cssa.model.CSSAModel" />
<jsp:useBean id="istitutodetenzione" scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" />
<jsp:useBean id="misuraalternativa" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel" />
<jsp:useBean id="lTotGiorniConcessi"  scope="request" class="java.lang.String"/>
<jsp:useBean id="lTotGiorniRDConcessi"  scope="request" class="java.lang.String"/>
<jsp:useBean id="dettaglioProvvedimento" scope="request" class="java.lang.String" />

<% // MEV_2019-09-SIEP aggiunto decreto/ordinanza per testare l'esito e capire se provvisoria o concessione %>
<jsp:useBean id="provvSorv" scope="request" class="siap.sico.evento.model.EventoModel" />

<% 
// caricaDecorrenzaScadenza = S inidca l'assenza del provvedimento di decorrenza scadenza
// e quindi consente di procedere allla registrazione di tale provvedimento anche
// se è stata validata la decorrenza
 %>
<jsp:useBean id="caricaDecorrenzaScadenza" scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - Dettaglio  </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src="/html/conferma.js"></script>

<% 
// MEV-9 si aggiungono gli ulteriori codici motivo (sorveglianza)
Set<String> codiciAffidamentoSorvNew = new HashSet<String>(Arrays.asList(new String[]{"0680","0681","0690","0691","0692"}));
Set<String> codiciDetenzioneSorvNew  = new HashSet<String>(Arrays.asList(new String[]{"0682","0693"}));
Set<String> codiciSemilibertaSorvNew = new HashSet<String>(Arrays.asList(new String[]{"2007","0683","0694"}));
%>

<script language="JavaScript">
  function Verify()
  {
    if   (document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.GPV.value!="" && document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.MPV.value!="" && document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.APV.value!="")
    {
      if (document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.GPV.value.length==1)
        document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.GPV.value='0'+document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.GPV.value;
      if (document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.MPV.value.length==1)
        document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.MPV.value='0'+document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.MPV.value;

      var data_to_verify = document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.GPV.value+'/'+document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.MPV.value+'/'+document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.APV.value;
      if (data_to_verify.length>4)
      {
        if (!ControllaData(data_to_verify) )
        {
          alert('Data di Decorrenza non valida');
          return false;
        }
      }
    }

    Esegui();
  }

  function Esegui()
  {
    document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.conferma.disabled=true;
   // document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.vai.disabled=true;
    document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.verbale.action.ActRegistraPenaVerbaleSottoscrizione";
  }

  function Avanti()
  {
<%
    if(misuraalternativa.getCodTipoMisura().equals("0001") || misuraalternativa.getCodTipoMisura().equals("0002") || misuraalternativa.getCodTipoMisura().equals("0003"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAffidamentoInProva&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
    else if(misuraalternativa.getCodTipoMisura().equals("0005") || misuraalternativa.getCodTipoMisura().equals("0010") || misuraalternativa.getCodTipoMisura().equals("0013"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMADetenzioneDomiciliare&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
    else if(misuraalternativa.getCodTipoMisura().equals("0004"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMASemiliberta&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
    else if(misuraalternativa.getCodTipoMisura().equals("2005"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAmmProvDetDom&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
		// MEV_2019-09 si aggiungono i nuovi codici dell'ammissione provv
		else if (codiciDetenzioneSorvNew.contains(misuraalternativa.getCodTipoMisura()))
		{%>
		   <% if ("0270".equals(provvSorv.getCodEsito())) { %>
		   document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAmmProvDetDom&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
		   <% } else { %>
		   document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMADetenzioneDomiciliare&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
		   <% } %>
		<%} 
		//MEV_2019-09 si aggiungono i nuovi codici
    else if(misuraalternativa.getCodTipoMisura().equals("2006") || misuraalternativa.getCodTipoMisura().equals("2008"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAffidamentoInProva&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
		// MEV_2019-09-SIEP si aggiungono i nuovi codici per la semilibertà provvisoria
		else if (codiciAffidamentoSorvNew.contains(misuraalternativa.getCodTipoMisura()))
		{%>
		  <% if ("0270".equals(provvSorv.getCodEsito())) { %>
		  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAmmProvAffi&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
		  <% } else { %>
		  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAffidamentoInProva&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
		  <% } %>
		<%} // MEV_2019-09 si aggiungono i nuovi codici per la semilibertà provvisoria
		else if (codiciSemilibertaSorvNew.contains(misuraalternativa.getCodTipoMisura()))
		{%>
		  <% if ("0270".equals(provvSorv.getCodEsito())) { %>
		  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAAmmProvSemiliberta&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
		  <% } else { %>
		  document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMASemiliberta&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
		  <% } %>
		<%
		}   
		//MEV_2019-09-SIEP fine
    else if(misuraalternativa.getCodTipoMisura().equals("2245"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAIndultino&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
    else if(   misuraalternativa.getCodTipoMisura().equals("2630")
            || misuraalternativa.getCodTipoMisura().equals("0610") //Ticket#20200720013 aggiunto codice del TDS
           )
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMAEspPressoDom&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
    else if(misuraalternativa.getCodTipoMisura().equals("0011"))
    {
%>
      document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActLoadInserisciMADetDomTemp&<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>=<%=misuraalternativa.getIdMisuraAlternativa()%>";
<%
    }
%>
    if (typeof (document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.conferma)!="undefined"){
      document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.conferma.disabled=true;
    }
    if (typeof (document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.vai)!="undefined"){
      document.DettaglioVerbalePenaResiduaVerbaleSottoscrizione.vai.disabled=true;
    }
  }
 </script>
</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Registrazione Data Inizio Misura</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="DettaglioVerbalePenaResiduaVerbaleSottoscrizione" method="POST" action="/jsp/Main.jsp">
<input type="HIDDEN" name="dettaglioProvvedimento" value="<%=dettaglioProvvedimento%>" >
<input type="HIDDEN" name="caricaDecorrenzaScadenza" value="<%=caricaDecorrenzaScadenza%>" >


    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa() %>">

     <table cellspacing=4 cellpadding=4>

<tr>
    <%if(misuraalternativa.getCodTipoMisura().equals("0001") || misuraalternativa.getCodTipoMisura().equals("0002") || misuraalternativa.getCodTipoMisura().equals("0003"))
       {%>
        <td class="l" colspan=2>Concessione Affidamento in Prova</td>
     <%}else
       if(misuraalternativa.getCodTipoMisura().equals("0005") || misuraalternativa.getCodTipoMisura().equals("0010") || misuraalternativa.getCodTipoMisura().equals("0013"))
       {%>
        <td class="l" colspan=2>Concessione Detenzione Domiciliare</td>
     <%}else
       if(misuraalternativa.getCodTipoMisura().equals("0004"))
       {%>
        <td class="l" colspan=2>Concessione Semilibertà</td>
     <%}
       else
       if(misuraalternativa.getCodTipoMisura().equals("2245"))
       {%>
        <td class="l" colspan=2>Concessione Sospensione Condizionata esecuzione parte finale pena detentiva</td>
     <%}
       else
         if(   misuraalternativa.getCodTipoMisura().equals("2630") 
            || misuraalternativa.getCodTipoMisura().equals("0610") //Ticket#20200720013 aggiunto codice del TDS
           )
         {%>
          <td class="l" colspan=2>Concessione Espiazione Pena presso Domicilio</td>
       <%}
       else
         if(misuraalternativa.getCodTipoMisura().equals("2005"))
         {%>
          <td class="l" colspan=2>Ammissione Provvisoria Detenzione Domiciliare</td>
       <%}
       else
         if(misuraalternativa.getCodTipoMisura().equals("2006") || misuraalternativa.getCodTipoMisura().equals("2008"))
         {%>
            <td class="l" colspan=2>Ammissione Provvisoria ad Affidamento in Prova</td>
       <%}    
       else
       if(misuraalternativa.getCodTipoMisura().equals("0011"))
       {%>
         <td class="l" colspan=2>Concessione Detenzione Domiciliare a Termine</td>
         <%-- MEV_2024-092: rework le applicazione non sono più provvisorie, si eliminala dicitura --%>
      <% } else if(codiciAffidamentoSorvNew.contains(misuraalternativa.getCodTipoMisura())) { %> <%--// MEV_2019-09 --%>
           <% if ("0270".equals(provvSorv.getCodEsito())) {%>
          <td class="l" colspan=2>Applicazione <!--Provvisoria--> Affidamento in Prova - Art. 678 comma 1-ter c.p.p.</td> 
          <% } else { %>
          <td class="l" colspan=2>Concessione Affidamento in Prova - Art. 678 comma 1-ter c.p.p.</td>
          <% } %>
      <% } else if(codiciDetenzioneSorvNew.contains(misuraalternativa.getCodTipoMisura())) { %> <%--// MEV_2019-09 --%>
           <% if ("0270".equals(provvSorv.getCodEsito())) {%>
          <td class="l" colspan=2>Applicazione <!-- Provvisoria--> Detenzione Domiciliare - Art. 678 comma 1-ter c.p.p.</td> 
          <% } else { %>
          <td class="l" colspan=2>Concessione Detenzione Domiciliare - Art. 678 comma 1-ter c.p.p.</td>
          <% } %>      
       <% } else if(codiciSemilibertaSorvNew.contains(misuraalternativa.getCodTipoMisura())) { %> <%--// MEV_2019-09 --%>
          <% if ("0270".equals(provvSorv.getCodEsito())) {%>
          <td class="l" colspan=2>Applicazione <!-- Provvisoria--> Semiliberta' - Art. 678 comma 1-ter c.p.p.</td> 
          <% } else { %>
          <td class="l" colspan=2>Concessione Semiliberta' - Art. 678 comma 1-ter c.p.p.</td>
          <% } %>
     <%}%>
    </tr>


    <tr>
        <td class="l">Data Pervenimento del Verbale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
    </tr>


<%if(verbale != null && verbale.getCssIdCssa()!= null && verbale.getCssIdCssa().compareTo(new BigDecimal(0))!=0)
{%>
  <tr>
        <td class="l">Data Sottoscrizione Prescrizioni</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
    </tr>
    <tr>
    	<%-- MEV10-s3: nuova gestione, invece che stringa fissa inserisco valore dalla combo --%>
        <td class="l"><%=StringUtils.toStringJSP(cssa.getTipoDesc())%></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(cssa.getComune()) + " " + StringUtils.toStringJSP(cssa.getIndirizzo()) %></font></td>
    </tr>
<%}%>

<% if(verbale != null && verbale.getIstDetIdIstitutoDetenzione()!= null && !verbale.getIstDetIdIstitutoDetenzione().equals("-"))
{%>
    <tr>
        <td class="l">Data Ingresso in istituto</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
    </tr>
    <tr>
        <td class="l">Istituto Competente che ha inviato il verbale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())+" di "+StringUtils.toStringJSP(istitutodetenzione.getDescrComune()) %></font></td>
    </tr>
<%}%>

<%  if(verbale != null && verbale.getCodTipoUfficioFirmatario() != null
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
<%}%>

    <tr>
        <td class="l">Indirizzo</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNote())%>&nbsp;</font></td>
    </tr>
<!---------------------------------------FINE PENA--------------------------------------------------------------->


<table>

     <tr>
        <td class="Titolo" colspan="15"><font  class="label">Pena da Espiare</font></td>
     </tr>

     <tr>
      <td class="l"><font  class="label">Reclusione : </font></td>
<%  if(penaresidua.getFlagErgastolo().equals("N"))  {%>
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
<%} else {
      if(penaresidua.getFlagErgastolo().equals("S"))
       {%>
         <td class="l"><font class="campo">ERGASTOLO</font></td>
      <%}else if(penaresidua.getFlagErgastolo().equals("D"))
       {%>
         <td class="l"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
      <%}%>
      </tr>

 <%}%>
</table>


<table>
  <%if(penaresidua.getDataInizio() != null){%>
  <tr>
    <td class="l">Data Decorrenza Pena: </font></td>
    <td class="l"> <font class="campo">
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
      </font>
    </td>
  <%
  }

  if (vedoDataIntermedia.equals("S"))
  { %>
        <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
        <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
          </font>
        </td>
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
    if(penaresidua.getDataFinePresunta() != null)
    {
%>
      <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
        <td class="l">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
        
      <% /* REWORK DETTAGLIO */
      if(dettaglioProvvedimento!=null && dettaglioProvvedimento.equals("SI"))
      {%>
       <tr>
        <td colspan=4><font  class="label"></font>
          <input type="hidden" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(penaresidua.getDataFinePresunta())%>">
          <input type="hidden" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(penaresidua.getDataFinePresunta())%>">
          <input  type="hidden" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(penaresidua.getDataFinePresunta())%>">
        </td>
      </tr>
      <%}
      else
      {%>
        <tr>
        <td class="l" colspan="4"><font  class="label">Data Fine Pena Manuale : </font>
           <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(penaresidua.getDataFinePresunta())%>">
            /
            <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(penaresidua.getDataFinePresunta())%>">
            /
            <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(penaresidua.getDataFinePresunta())%>">
            &nbsp;&nbsp;&nbsp;
        </td>
      </tr>
      <% } %>

      <% if( lTotGiorniConcessi!=null && !"0".equals(lTotGiorniConcessi) ) { %>
        <tr>
          <td class="l">Giorni di Lib. ant. concessi</td>
          <td class="l">
            <font class="campo"> <%=StringUtils.toStringJSP(lTotGiorniConcessi)%></font>
          </td>
        </tr>
      <% } %>
      
      
      <% if( lTotGiorniRDConcessi!=null && lTotGiorniRDConcessi.length()>0 && Integer.parseInt(lTotGiorniRDConcessi)!=0 ) {%>
       <tr>
          <td class="l">Giorni di Risarcimento D.L. 92/2014 concessi</td>
          <td class="l">
            <font class="campo"> <%=StringUtils.toStringJSP(lTotGiorniRDConcessi)%></font>
          </td>
        </tr>
    <% } %>     
      

     <% 
     if(dettaglioProvvedimento.equals("SI")) 
     { 
    
     } else { 
     %>
     <tr>
       <td colspan="2"><br><INPUT class="bottone" type="submit" name="conferma" value="Validazione Fine Pena"></td>
     </tr>
      <%  
     }
   } // penaresidua.getDataFinePresunta()
  } // if(penaresidua.getFlagErgastolo().equals("N"))
  else {
    //ERGASTOLO
  }
%>

  <% if (dettaglioProvvedimento.equals("SI") && caricaDecorrenzaScadenza.equals("SI")) { %>
     <tr>
       <td colspan=2><br><INPUT class="bottone" type="button" name="vai" value="Registrazione Provvedimento Decorrenza Scadenza" onClick="javascript:return Avanti()"></td>
     </tr>
   <% } %>
    
  </table>

<!-----------------------------------------FINE PENA------------------------------------------------------------->
    </table>
     <input type="hidden" name="idpenaresidua" value="<%=penaresidua.getIdPenaResidua()%>">

     <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
<!-------------->
     <input type="hidden" name="GiornoInizio" value="<%=DateUtils.getDayToString(verbale.getDataEmissione())%>">
     <input type="hidden" name="MeseInizio"   value="<%=DateUtils.getMonthToString(verbale.getDataEmissione())%>">
     <input type="hidden" name="AnnoInizio"   value="<%=DateUtils.getYearToString(verbale.getDataEmissione())%>">

     <input type="hidden" name="ggpervenimento"  value="<%=DateUtils.getDayToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="mmpervenimento"  value="<%=DateUtils.getMonthToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="aapervenimento"  value="<%=DateUtils.getYearToString(verbale.getDataPervenimento())%>">
     <input type="hidden" name="cssacomune"      value="<%=StringUtils.toStringJSP(cssa.getComune())%>">
     <input type="hidden" name="cssaindirizzo"   value="<%=StringUtils.toStringJSP(cssa.getIndirizzo())%>">
     <input type="hidden" name="cssa"            value="<%=StringUtils.toStringJSP(cssa.getIdCSSA())%>">
     <input type="hidden" name="Note"            value="<%=StringUtils.toStringJSP(verbale.getNote())%>">
     <input type="hidden" name="vedoDataIntermedia"   value="<%=vedoDataIntermedia%>">
     <input type="hidden" name="verbaleid"          value="<%=verbale.getIdVerbale()%>">
  </form>
  </body>

<%
  if(penaresidua.getFlagErgastolo().equals("N"))
   {
     if(penaresidua.getDataFinePresunta() != null)
     {
%>
      <script language="JavaScript" type="text/javascript">

        var frmvalidator  = new Validator("DettaglioVerbalePenaResiduaVerbaleSottoscrizione");

        frmvalidator.addValidation("GPV","maxlen=2","La lunghezza massima per il Giorno Pena Validata è di 2 caratteri");
        frmvalidator.addValidation("GPV","numeric","Il campo Giorno Pena Validata deve essere numerico");
        frmvalidator.addValidation("GPV","gt=1","Il campo Giorno Pena Validata deve essere maggiore di 0");
        frmvalidator.addValidation("GPV","lt=31","Il campo Giorno Pena Validata deve essere minore di 31");
        frmvalidator.addValidation("MPV","maxlen=2","La lunghezza massima per il Mese Pena Validata è di 2 caratteri");
        frmvalidator.addValidation("MPV","numeric","Il campo Mese Pena Validata deve essere numerico");
        frmvalidator.addValidation("MPV","gt=1","Il campo Mese Pena Validata deve essere maggiore di 0");
        frmvalidator.addValidation("MPV","lt=12","Il campo Mese Pena Validata deve essere minore di 12");
        frmvalidator.addValidation("APV","maxlen=4","La lunghezza massima per l'Anno Pena Validata è di 4 caratteri");
        frmvalidator.addValidation("APV","minlen=4","La lunghezza minima per l'Anno Pena Validata è di 4 caratteri");
        frmvalidator.addValidation("APV","numeric","Il campo Anno Pena Validata deve essere numerico");
        frmvalidator.addValidation("APV","gt=1900","Il campo Anno Pena Validata deve essere maggiore di 1900");
        frmvalidator.addValidation("APV","lt=2100","Il campo Anno Pena Validata deve essere minore di 2100");

        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
<%
  }
}
%>

</html>