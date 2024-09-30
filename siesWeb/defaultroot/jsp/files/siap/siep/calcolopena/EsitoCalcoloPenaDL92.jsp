<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Date"%>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.calendar.model.CalendarModel" %>
<%@ page import="siap.sico.util.CalendarUtil" %>

<%@ page import="siap.siep.calcolopena.model.SemestreDL92Model" %>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.calcolopena.action.ICostantiCalcoloPena"%>

<jsp:useBean id="EsitoCalcolo" scope="request" class="siap.siep.calcolopena.model.CalcoloPenaDL92Model" />

<%
//==============================================================================
// Jsp per il calcolo della pena "virtuale" DL 92/2024  
// La jsp visualizza:
//==============================================================================
%>
<html>
<head>
  <title>[S.I.E.S.] - Calcolo Pena DL92/2024 </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>" >
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    function Verify_Dati()
    {
      //document.f.subm2.disabled=true;
      //document.f.submit();
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="../../images/quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Calcolo pena ipotetica con detrazioni DL. 92/2024</font>
      </td>
      <td class="LBG">
        <a href="Javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <% if (session.getAttribute("fascicolo") != null) { %>
  <jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <% } %>  

  
  <form method="POST" action="/jsp/Main.jsp" name="CalcoloPenaDL92">
  
  </form>
  
    <!-- ===================================================================== -->
    <!--                   SEZIONE CON I RISULTATO                             -->  
    <!-- ===================================================================== -->
    <div>
    <% 
      // Presofferto
      SemestreDL92Model lCalcoloPresofferto = EsitoCalcolo.getSemestrePresofferto();      
    %>
    <table cellspacing="4" cellpadding="4" width="50%">
      <tr>
        <td class="Titolo" colspan="4"><font class="label">Custodia cautelare (presofferto)</font></td>
      </tr>
      <tr>
        <td class="l" width="80%"><font class="label">Custodia cautelare (presofferto)</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniPresofferto(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiPresofferto(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniPresofferto(),"&nbsp;") %> giorni</font></td>
      </tr>      
      <tr>
        <td class="l"><font class="label">Semestri utili di custodia cautelare per erogazione L.A.:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(lCalcoloPresofferto.getProgressivo(),"")%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Giorni di L.A. maturati in custodia cautelare:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(lCalcoloPresofferto.getLAApplicate(),"") %></font></td>
      </tr>
      <tr>
        <td class="l" width="90%"><font class="label">Giorni di custodia cautelare eccedenti i semestri utili per erogazione L.A. (verranno conteggiati per anticipare il primo semestre utile nell'esecuzione pena):</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(lCalcoloPresofferto.getGiorniResiduiPresofferto(),"") %></font></td>
      </tr>
      
      <%-- ================================================================ --%>
      <%--                        PENA DA ESEGUIRE                          --%>
      <%-- ================================================================ --%>
      <tr>
        <td colspan="4"><font class="label">&nbsp;</font></td>
      </tr> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">Pena Da eseguire al netto della custodia cautelare (presofferto)</font></td>
      </tr>
      <tr>
        <td class="l" width="80%"><font class="label">Reclusione</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniReclusione(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiReclusione(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniReclusione(),"&nbsp;") %> giorni</font></td>
      </tr>   
      <tr>
        <td class="l" width="80%"><font class="label">Arresto</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumAnniArresto(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumMesiArresto(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getNumGiorniArresto(),"&nbsp;") %> giorni</font></td>
      </tr>  
      <tr>
        <td class="l" width="80%"><font class="label">Quantum Totali</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getTotaleDaEseguire().getNumAnni(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getTotaleDaEseguire().getNumMesi(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getTotaleDaEseguire().getNumGiorni(),"&nbsp;") %> giorni</font></td>
      </tr> 
      
      <%
      int lLAApplicate = EsitoCalcolo.getLAApplicate().intValue();
      int lPenaInEccesso = EsitoCalcolo.getLAFungibili().intValue();
      String color = "";
      if (lPenaInEccesso>0) color =  "style='color=red'";
      
      int numGiorniLAMaturataInPenaresidua = lLAApplicate - EsitoCalcolo.getSemestrePresofferto().getLAApplicate().intValue();
      %>
      <tr>
        <td class="l"><font class="label">Semestri utili di pena ipotetica per erogazione L.A.:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getListaSemetri().size(),"")%></font></td>
      </tr>
      <tr>
        <td class="l"><font class="label">Giorni di L.A. maturabili e usufruibili in pena ipotetica:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(numGiorniLAMaturataInPenaresidua,"") %></font></td>
      </tr>
      <tr>
        <td class="l" width="90%"><font class="label" >Giorni di L.A. maturabili e non usufruibili in pena ipotetica:</font></td>
        <td class="r" colspan="3"><font class="label" <%=color %> ><%=StringUtils.toStringJSP(lPenaInEccesso,"") %></font></td>
      </tr>   
      
      <%-- ================================================================ --%>
      <%--       DATI DA INDICARE NEL PROVVEDIMENTO DI ESECUZIONE           --%>
      <%-- ================================================================ --%>  
      <tr>
        <td colspan="4"><font class="label">&nbsp;</font></td>
      </tr> 
      <tr>
        <td class1="Titolo" colspan="4" style="BACKGROUND-COLOR: green; text-align:center;">
            <font class="label" style="COLOR: white">Dati da indicare nei provvedimenti di esecuzione</font>
        </td>
      </tr>
      <tr>
        <td class="r"><font class="label">Pena ipotetica ad esito delle detrazioni dei soli giorni di L.A. usufruibili:</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getPenaIpotetica().getNumAnni(),"&nbsp;") %> anni</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getPenaIpotetica().getNumMesi(),"&nbsp;") %> mesi</font></td>
        <td class="r" nowrap><font class="label"><%=StringUtils.toStringJSP(EsitoCalcolo.getPenaIpotetica().getNumGiorni(),"&nbsp;") %> giorni</font></td>
      </tr> 
      <tr>
        <td class="r"><font class="label">Semestri utili di pena scontata:</font></td>
        <td class="r" colspan="3"><font class="label"><%=lCalcoloPresofferto.getProgressivo().intValue() + EsitoCalcolo.getListaSemetri().size() %></font></td>
      </tr>            
      <tr>
        <td class="r"><font class="label">Giorni di liberazione anticipata concedibili:</font></td>
        <td class="r" colspan="3"><font class="label"><%=EsitoCalcolo.getLAMaturate().intValue() %></font></td>
      </tr>
      <tr>
        <td class="r"><font class="label">Giorni di liberazione anticipata usufruibili:</font></td>
        <td class="r" colspan="3"><font class="label"><%=EsitoCalcolo.getLAApplicate().intValue() %></font></td>
      </tr>
      
      <%-- ================================================================ --%>
      <%--              CALCOLI CON DATA DI ESECUZIONE                      --%>
      <%-- ================================================================ --%> 
      <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>    
      <tr>
        <td colspan="4"><font class="label">&nbsp;</font></td>
      </tr> 
      <tr>
        <td class1="Titolo" colspan="4" style="BACKGROUND-COLOR: green; text-align:center;">
          <font class="label" style="COLOR: white">CALCOLI CON DATA DI ESECUZIONE</font>
        </td>
      </tr>  
      <tr>
        <td class="r"><font class="label">data decorrenza pena:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataInizioPena(), "dd/MM/yyyy")) %></font></td>
      </tr> 
      <tr>
        <td class="r"><font class="label">data scarcerazione senza L.A.:</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazioneNoLA(), "dd/MM/yyyy")) %></font></td>
      </tr>
      <tr>
        <td class="r"><font class="label">data scarcerazione con L.A. applicati (data fine pena calcolata CON fungibilita'):</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazioneLAFung(), "dd/MM/yyyy")) %></font></td>
      </tr>
      <tr>
        <td class="r"><font class="label">data scarcerazione con L.A. concessi (data fine pena calcolata SENZA fungibilita'):</font></td>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazioneLANoFung(), "dd/MM/yyyy")) %></font></td>
      </tr>
      <tr>
        <td class="r"><font class="label">data scarcerazione senza applicare l'ultimo semestre (nei soli casi in cui ci sarebbe un credito di L.A.):</font></td>
        <% if (EsitoCalcolo.getLAFungibili().intValue()>0) { %>
        <td class="r" colspan="3"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (EsitoCalcolo.getDataScarcerazionePenultimoSemestre(), "dd/MM/yyyy")) %></font></td>
        <% } else { %>
        <td class="r" colspan="3"><font class="label">non applicabile</font></td>
        <% } %>
      </tr>                               
      <% } %>               
    </table>
  </div>
  
  <br>
  
  <%-- ==================================================================== --%>
  <%--       SEZIONE CON IL DETTAGLO DEI SEMESTRI                           --%>
  <%-- ==================================================================== --%>  
  <div>
    <table cellspacing="4" cellpadding="4" width="50%">
      <tr>
        <td class="Titolo" colspan="8"><font class="label">Dettaglio per Semestri</font></td>
      </tr>
      <tr>
        <td class="Titolo"><font class="label">&nbsp;</font></td>
        <td class="Titolo"><font class="label">&nbsp;</font></td>
        <td class="Titolo"><font class="label">L.A. APPLICATA</font></td>
        <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>
        <td class="Titolo"><font class="label">Data Maturazione L.A.</font></td>
        <td class="Titolo"><font class="label">Nuova Data Scadenza</font></td>
        <% } %>
        <td class="Titolo" colspan="3"><font class="label">RESIUDO PENA</font></td>
      </tr>      
      

      <%-- ================================================================ --%>
      <%--                         Presofferto                              --%>
      <%-- ================================================================ --%>        
      <tr>
        <td class="r"><font class="label"><%=lCalcoloPresofferto.getProgressivo()%></font></td>
        <td class="l"><font class="label">SEMESTRI ESPIATI IN C.C.</font></td>
        <td class="c"><font class="label"><%=lCalcoloPresofferto.getLAApplicate() %></font></td>
        <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloPresofferto.getDataMaturazioneLA(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lCalcoloPresofferto.getNuovaDataScadenzaPena(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <% } %>
        <td class="r"><font class="label"><%=lCalcoloPresofferto.getResiduoNumAnni()+" anni" %></font></td>
        <td class="r"><font class="label"><%=lCalcoloPresofferto.getResiduoNumMesi()+" mesi" %></font></td>
        <td class="r"><font class="label"><%=lCalcoloPresofferto.getResiduoNumGiorni()+" giorni" %></font></td>
      </tr> 
      
      <%
      Vector <SemestreDL92Model> mListaSemetri = EsitoCalcolo.getListaSemetri();
      
      for (int i = 0; i<mListaSemetri.size(); i++) {
      	SemestreDL92Model lSemestreUtile = mListaSemetri.elementAt(i);
      	String lPenaStr = lSemestreUtile.getResiduoNumAnni()+" anni - " 
                        + lSemestreUtile.getResiduoNumMesi()+" mesi - "
                        + lSemestreUtile.getResiduoNumGiorni()+" giorni ";
      	
      	
      %>
      <tr>
        <td class="l"><font class="label"><%=lSemestreUtile.getProgressivo()%>&deg;</font></td>
        <td class="l"><font class="label">semestre utile per L.A.</font></td>
        <td class="c"><font class="label"><%=lSemestreUtile.getLAApplicate() %></font></td>
        <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) {%>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lSemestreUtile.getDataMaturazioneLA(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lSemestreUtile.getNuovaDataScadenzaPena(), "dd/MM/yyyy"),"&nbsp") %></font></td>
        <% } %>
        <td class="r"><font class="label"><%=lSemestreUtile.getResiduoNumAnni()+" anni"%></font></td>
        <td class="r"><font class="label"><%=lSemestreUtile.getResiduoNumMesi()+" mesi " %></font></td>
        <td class="r"><font class="label"><%=lSemestreUtile.getResiduoNumGiorni()+" giorni" %></font></td>
      </tr>
      <% } %>     
      
      
      <tr>
        <td class="l"><font class="label"></font></td>
        <td class="l"><font class="label"></font></td>
        <td class="c"><font class="label"><%=EsitoCalcolo.getLAApplicate().intValue() %></font></td>
        <td class="r"><font class="label"></font></td>
        <td class="r"><font class="label"></font></td>
        <td class="r"><font class="label"></font></td>
      </tr> 
    </table>
  </div>
  
  <br>  


<% 
Date lDataScarcerazione = null;
if (EsitoCalcolo.getDataScarcerazioneLAFung() != null)
	lDataScarcerazione = EsitoCalcolo.getDataScarcerazioneLAFung();
else 
	lDataScarcerazione = EsitoCalcolo.getDataScarcerazioneNoLA();
%>
  <div>
    <table cellspacing="4" cellpadding="4" width="50%"> 
      <% if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_LIBERO)) { %>
      <tr>
        <td class="Titolo" colspan="4"><font class="label">L.A. MATURATA MA NON APPLICATA / PENA ESPIATA IN ECCESSO</font></td>
      </tr>
      <tr>
        <td class="c"><font class="label" <%=color %>><%=lPenaInEccesso%></font></td>
      </tr> 
      <% } else if (EsitoCalcolo.getPosizioneGiuridica().equals(ICostantiCalcoloPena.POSIZIONE_GIURIDICA_DETENUTO)) { %> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">DATA SCARCERAZIONE</font></td>
      </tr>
      <tr>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString (lDataScarcerazione, "dd/MM/yyyy")) %></font></td>
      </tr> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">SURPLUS DETENZIONE (GIORNI DI FUNGIBILITA')</font></td>
      </tr>
      <tr>
        <td class="c"><font class="label" <%=color %>><%=lPenaInEccesso %></font></td>
      </tr>            
      <% } %> 
      <tr>
        <td class="Titolo" colspan="4"><font class="label">GIORNI L.A. CONCESSI (MATURATI)</font></td>
      </tr>    
      <tr>
        <td class="c"><font class="label"><%=EsitoCalcolo.getLAMaturate() %></font></td>
      </tr>        
    </table>
  </div>
 
</body>
</html>

