<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.continuazione.model.ContinuazioneModel"%>
<%@ page import="siap.siep.continuazione.action.ICostantiContinuazione"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="dettaglioPenaComplessiva" scope="request" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PenaComplessivaModel lPenCom = dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva();
  SanzioneSostitutivaModel lSanSos = dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva();
  List lListCont = dettaglioPenaComplessiva.getContinuazioni();

  if(lSanSos == null)
    lSanSos = new SanzioneSostitutivaModel();
  
  // Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;
  
%>

<html>
<head>
  <title>[S.I.E.S.] - Modifica Pena Complessiva </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione : </font>
        <font class="campo">Modifica Pena Complessiva e Sentenze in Continuazione</font></td>
    </tr>
  </table>
	  <br>
   <%if(!modoSIGE)
    {%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  	<br>
  
    <table style="width: 95%;">
      <tr><td class="Titolo" colspan="6">Pena Complessiva/Sanzione Sostitutiva</td></tr>
      <tr>
        <td class="int">Ergastolo</td>
        <td class="int">Reclusione</td>
        <td class="int">Arresto</td>
        <td class="int">Tipo Sanzione</td>
        <td class="int">Sanzione Sostitutiva</td>
        <td class="int" width="5%">Azioni</td>
      </tr>
      <tr>
      <td class="l">
          <%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentivaDB())%>&nbsp;
        </td>
        <td class="l">
          Anni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(), "0")%>&nbsp;
          Mesi&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(), "0")%>&nbsp;
          Giorni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(), "0")%>
        </td>
        <td class="l">
          Anni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;
          Mesi&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;
          Giorni&nbsp;<%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(), "0")%>
        </td>
        <td class="l">
          <%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%>&nbsp;
        </td>
        <%if (lSanSos.getIdSanzioneSostitutiva()!=null){%>
          <%if (lSanSos.getCodTipoSanzione().equals("P")){%>
        	  <td class="l">
          		<%if (lSanSos.getSanzionePecuniariaMulta() !=null) {%>   
			            Multa   <%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%>
			            <br> 
			     <% }   
			    if (lSanSos.getSanzionePecuniariaAmmenda() !=null) {%>
			            Ammenda <%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%>

			     <% } %>    
			   </td>
			     
          <% } else { %>
          <td class="l">
            Anni&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;
            Mesi&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;
            Giorni&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%>
          </td>
          <%}%>
        <%} else {%>
        <td class="l">&nbsp; </td>
        <%}%>
        <td class="c">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadModificaPenaComplessiva&<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>=<%=lPenCom.getIdPenaComplessiva()%>">
            <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" alt="Modifica" width="12" height="12" border="0">
          </a>
        </td>
      </tr>
    </table>
    <br>
<%
  if (lListCont.size() != 0)
  {
%>
    <table style="width: 95%;">
      <tr><td class="Titolo" colspan="6">Sentenze in Continuazione</td></tr>
      <tr>
        <td class="int">Tipo Continuazione</td>
        <td class="int">Anno/Numero Sentenza</td>
        <td class="int">Data Sentenza</td>
        <td class="int">Autorità Sentenza</td>
        <td class="int">Luogo Sentenza</td>
        <td class="int" width="5%">Azioni</td>
      </tr>
<%
    Iterator iter = lListCont.iterator();
    while (iter.hasNext())
    {
      ContinuazioneModel lContMod = (ContinuazioneModel)iter.next();
%>
      <tr>
        <td class="l">
          <%=StringUtils.toStringJSP(lContMod.getDescrTipoContinuazione())%>&nbsp;
        </td>
        <td class="l">
          <%=StringUtils.toStringJSP(lContMod.getAnnoSentenza())%>
          /
          <%=StringUtils.toStringJSP(lContMod.getNumSentenza())%>
        </td>
        <td class="l">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(),"dd-MM-yyyy"))%>&nbsp;
        </td>
        <td class="l">
          <%=StringUtils.toStringJSP(lContMod.getDescrTipoAutorita())%>&nbsp;
        </td>
        <td class="l">
          <%=StringUtils.toStringJSP(lContMod.getDescrLuogoAutorita())%>&nbsp;
        </td>
        <td class="c">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadModificaContinuazione&<%=ICostantiContinuazione.CAMPO_ID_CONTINUAZIONE%>=<%=lContMod.getIdContinuazione()%>">
            <img src="<%=IWebConstants.IMAGES_DIR%>modifica.gif" alt="Modifica" width="12" height="12" border="0">
          </a>
        </td>
      </tr>
<%
    }
%>
    </table>
<%
  }
%>
</body>
</html>