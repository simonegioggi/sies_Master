<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="penaresidua"   scope="request"   class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="libAntMod"     scope="request"   class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="LicenzeLibAnt" scope="request"   class="java.util.Vector" />

<%
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

//20/05/2014 - Nuova L.A.

LicenzaLibAnticipataModel lLiceModel = new LicenzaLibAnticipataModel();
Iterator IteLic = LicenzeLibAnt.iterator();

int totOldLA = 0;
int totggLA = 0;
int totggLS = 0;
int totggLI = 0;
int totggRD = 0;

boolean NuovaLA = false;

while(IteLic.hasNext())
{
  lLiceModel = (LicenzaLibAnticipataModel)IteLic.next();
  
  if(   lLiceModel.getCodTipoLicenza().equals("LA")
     && lLiceModel.getFlagConcesso() != null 
     && lLiceModel.getFlagConcesso().compareTo("C") == 0 
    )
  {
    if(lLiceModel.getDescrStatoPermesso() != null)
    {
      if(lLiceModel.getDescrStatoPermesso().compareTo("LA") == 0)
      {
        NuovaLA = true;
        totggLA += lLiceModel.getNumeroGiorni().intValue(); 
      }
      else if(lLiceModel.getDescrStatoPermesso().compareTo("LS") == 0)
      {
        NuovaLA = true;
        totggLS += lLiceModel.getNumeroGiorni().intValue(); 
      }
      else if(lLiceModel.getDescrStatoPermesso().compareTo("LI") == 0)
      {
        NuovaLA = true;
        totggLI += lLiceModel.getNumeroGiorni().intValue(); 
      }
      else
      {
      }      
    }
    else
    { 
      totOldLA += lLiceModel.getNumeroGiorni().intValue(); 
    } 
  }
  else if (lLiceModel.getCodTipoLicenza().equals("RD")){
    NuovaLA = true;
    totggRD = lLiceModel.getNumeroGiorni().intValue();
  }
}
%>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Pena Residua Manuale </title>
  
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>

</head>

<body class="corpo">

<FORM name="comandi" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Pena Residua Manuale</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
</FORM>

  <table cellspacing=2 cellpadding=2 width=60%>
    <tr>
      <td class="Titolo" colspan=6 width=60% >Pena</td>
    </tr>
    <tr>
      <td class="l" width=20%>Reclusione</td>
      <td class="l"  >
      <% if (   (penaresidua.getNumAnniReclusione() != null) 
             || (penaresidua.getNumMesiReclusione() != null)
             || (penaresidua.getNumGiorniReclusione() != null)
            ) 
      { %>
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(), "0")%></font>
      <% } %>
     &nbsp;</td>
    </tr>
    <tr>
      <td class="l">Multa</td>
      <td class="l">
      <% if (penaresidua.getImportoMulta() != null) {%>
       <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font>
      <% } %>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Arresto</td>
      <td class="l">
  <% if ((penaresidua.getNumAnniArresto() != null) || (penaresidua.getNumMesiArresto() != null)
      || (penaresidua.getNumGiorniArresto() != null)) { %>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(), "0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(), "0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(), "0")%></font>
    <% } %>
       &nbsp;</td>
    </tr>
    
    <tr>
      <td class="l">Ammenda</td>
      <td class="l">
        <% if (penaresidua.getImportoAmmenda() != null) {%>
        <font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
        <% } %>
    &nbsp;</td>
    </tr>
</table>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--      
    < %
    if (   libAntMod.getIdLicenzaLibanticipata()!=null
        && libAntMod.getNumeroGiorni()!=null
       )
    {
    %>
    <tr>
      <td class=l>
        <font class="label">Totale Liberazione Anticipata</font>
      </td>
      <td class=l>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(libAntMod.getNumeroGiorni(), "0")%></font>
      </td>
    </tr>
    < %
    }
    %>
--%>

<!-- 
// 20/05/2014 - Nuova Ordinanza L.A. - >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
// Inseriti in questa form  i gg. di L.A. , L.A. Speciale, e di Integrazione L.A.
 -->
<% if(NuovaLA == true || totOldLA > 0) { %>
  <table>
  <% if(NuovaLA == true) { 
      if(totggLA > 0) { %>
      <tr>
        <td class=l>
          <font class="label"> Totale Giorni Liberazione Anticipata </font>
        </td>
        <td class=l>&nbsp;&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(totggLA)%></font>
        </td>
      </tr>
      <% } 

      if(totggLS > 0) { %>
      <tr>
        <td class=l>
          <font class="label"> Totale Giorni Liberazione Anticipata Speciale </font>
        </td>
        <td class=l>&nbsp;&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(totggLS)%></font>
        </td>
      </tr>
      <% } 

      if(totggLI > 0) { %>
      <tr>
        <td class=l>
          <font class="label"> Totale Giorni Integrazione Liberazione Anticipata</font>
        </td>
        <td class=l>&nbsp;&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(totggLI)%></font>
        </td>
      </tr>
      <% } 
      
      if(totggRD > 0) { %>
      <tr>
        <td class=l>
          <font class="label"> Totale Giorni Riduzione pena Risarcimento Danni</font>
        </td>
        <td class=l>&nbsp;&nbsp;
          <font class="campo"><%=StringUtils.toStringJSP(totggRD)%></font>
        </td>
      </tr>
      <% } 
    }
    else //NuovaLA == false
    { 
      if(totOldLA > 0)
      { %>
      <tr>
        <td class=l>
                <font class="label"> Totale Giorni Liberazione Anticipata </font>
              </td>
              <td class=l>&nbsp;&nbsp;
                <font class="campo"><%=StringUtils.toStringJSP(totOldLA)%></font>
              </td>
          </tr>
<%      }
    } %>
      </table>
<%  } %>
      
<!--    End Nuova Ordinanza L.A.   -->

<table>
  <tr>
    <td class=l>
      <font class="label">Data Decorrenza Pena </font>
    </td>
    <td class="l">
    <% if (penaresidua.getDataInizio() != null) {%>
    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%></font>
    <%}%>
    &nbsp;
    </td>
  </tr>
  <tr>
    <td class=l>
      <font class="label">Data Fine Reclusione </font>
    </td>
    <td class="l">
    <% if (penaresidua.getDataFineReclusione() != null) {%>
    <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
          </font>
<%}%>
&nbsp;</td>
</tr>
<tr>
    <td class=l>
      <font class="label">Data Inizio Arresto </font>
    </td>
    <td class="l">
     <% if (penaresidua.getDataInizioArresto() != null) {%>
   <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
          </font>
<%}%>
&nbsp;</td>
   </tr>
  <tr>
    <td class=l>
      <font class="label">Data Fine Pena </font>
    </td>
    <td class="l">
   <% if (penaresidua.getDataFine() != null) {%>
<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>
          </font>
    <%}%>
&nbsp;</td>
   </tr>
    <tr>

      <td class="l">
      <font class="label">Ergastolo
<% if (penaresidua.getFlagErgastolo()!= null &&  penaresidua.getFlagErgastolo().equals("D"))
 { %>
     con Isolamento Diurno
 <%}%>
</font>
    <td class="l">
    <% if (penaresidua.getFlagErgastolo()!= null && (penaresidua.getFlagErgastolo().equals("S") || penaresidua.getFlagErgastolo().equals("D"))) { %>
    <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">

    <%}%>
&nbsp;</td>

    </tr>
    <tr>
      <td class="l">Data Inizio Isolamento Diurno</td>
      <td class="l">
    <% if (penaresidua.getDataInizioIsolamentoDiurno() != null) { %>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"))%>
     </font>
      <% } %>

      &nbsp;</td>
    </tr>
    <tr>

      <td class="l">Data Fine Isolamento Diurno</td>
      <td class="l">
   <% if (penaresidua.getDataFineIsolamentoDiurno() != null) { %>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"))%>
       </font>
    <% } %>

      &nbsp;</td>
    </tr>
    <tr>
            <td class="l"> Isolamento Diurno</td>
<td class="l">
  <% if ((penaresidua.getNumAnniIsolamentoDiurno() != null) || (penaresidua.getNumMesiIsolamentoDiurno() != null)
    || (penaresidua.getNumGiorniIsolamentoDiurno() != null)) { %>

            Anni
              <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniIsolamentoDiurno(), "0") %>&nbsp;</font>
            Mesi
             <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiIsolamentoDiurno(), "0")%>&nbsp;</font>
            Giorni
             <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniIsolamentoDiurno(), "0")%>&nbsp;</font>
   <% } %>
&nbsp; </td>
</tr>
<%if(penaresidua.getFlagPenaSospesa()!= null){%>
<tr>
<td class="l"> Tipo Pena </td>
<%if(penaresidua.getFlagPenaSospesa().equals("I")){%>
            <td class="l">    <font class="campo"> Interrotta</font></td>
<%} else if(penaresidua.getFlagPenaSospesa().equals("E")){%>
            <td class="l">   <font class="campo">Espulso</font></td>
<%} else if(penaresidua.getFlagPenaSospesa().equals("D")){%>
            <td class="l">   <font class="campo">  Differita</font></td>
<%}else if(penaresidua.getFlagPenaSospesa().equals("S")){%>
            <td class="l">    <font class="campo"> Sospesa</font></td>
<%}%>
</tr>
<%}%>
<tr><td>&nbsp;</td><tr>
<tr><td>&nbsp;</td><tr>

  </table>
</body>
</html>