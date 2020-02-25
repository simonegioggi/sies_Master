<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaSanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaComplessivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.SanzioneSostitutivaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaComplessivaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSanzioneSostitutivaCumulo"%>

<%@ page import="siap.siep.continuazione.model.ContinuazioneModel"%>
<%@ page import="siap.siep.continuazione.action.ICostantiContinuazione"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.cumulo.model.CumuloModel"/>

<jsp:useBean id="dettaglioPenaComplessivaCum" scope="request" class="siap.siep.modulocumulo.model.DettaglioPenaComplessivaCumuloModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
 
FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

 	PenaComplessivaCumuloModel lPenCom =  dettaglioPenaComplessivaCum.getPenaComplessivaSanzioneSostitutivaCumulo().getPenaComplessivaCumulo();
	SanzioneSostitutivaCumuloModel lSanSos = dettaglioPenaComplessivaCum.getPenaComplessivaSanzioneSostitutivaCumulo().getSanzioneSostitutivaCumulo(); 
	List lListCont = dettaglioPenaComplessivaCum.getContinuazioni();

  if(lSanSos == null)
    lSanSos = new SanzioneSostitutivaCumuloModel();
  
%>

<html>
<head>
  <title>[S.I.E.S.] - Cancella Pena Complessiva (CUMULO)</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione: </font>
        <font class="campo">Cancella Pena Complessiva relativa al titolo cumulato</font>
      </td>

      <td class="LBG">
       	<a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
       		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
       	</a>
      </td>            
    </tr>
  </table>

  <br>
      <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>

    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadCancellaPenaComplessivaCumulo">
    
  			<!-- Campi sempre presenti sulle form dei dati analitici -->
  			<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  			<input type="hidden" name="<%=ICostantiCumulo.CAMPO_ID_CUMULO%>"                        value="<%=TitoloInCumulo.getIdCumulo()%>">

  			<input type="hidden" name="<%=ICostantiCumulo.CAMPO_SEN_ID_SENTENZA%>"      	value="<%=TitoloInCumulo.getSenIdSentenza()%>">
  			<input type="hidden" name="<%=ICostantiCumulo.CAMPO_ID_FASCICOLO_SIEP_CUMULATO%>" value="<%=TitoloInCumulo.getIdFascicoloSiepCumulato()%>">	

    <table width=95%>
      <tr><td class="Titolo" colspan=6>Pena Complessiva/Sanzione Sostitutiva</td></tr>
      <tr>
        <td class="int">Ergastolo</td>
        <td class="int">Reclusione</td>
        <td class="int">Arresto</td>
        <td class="int">Tipo Sanzione Sostitutiva</td>
        <td class="int">Durata Sanzione Sostitutiva</td>
        <td class="int" width=5%>Azioni</td>
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
        <td class="l">
          Anni&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;
          Mesi&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;
          Giorni&nbsp;<%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%>
        </td>
        <td class="c">
          <a href="Javascript:conferma('siap.siep.modulocumulo.action.ActCancellaPenaComplessivaCumulo','<%=ICostantiPenaComplessivaCumulo.CAMPO_ID_PENA_COMPLESSIVA_CUM%>','<%=lPenCom.getIdPenaComplessivaCum()%>');">
            <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" alt="Cancella" width="12" height="12" border="0">
          </a>
        </td>
      </tr>
    </table>
    <br>
<%
  if (lListCont.size() != 0)
  {
%>
    <table width=95%>
      <tr><td class="Titolo" colspan=6>Sentenze in Continuazione</td></tr>
      <tr>
        <td class="int">Tipo Continuazione</td>
        <td class="int">Anno/Numero Sentenza</td>
        <td class="int">Data Sentenza</td>
        <td class="int">Autorità Sentenza</td>
        <td class="int">Luogo Sentenza</td>
        <td class="int" width=5%>Azioni</td>
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
          <a href="Javascript:conferma('siap.siep.continuazione.action.ActCancellaContinuazione','<%=ICostantiContinuazione.CAMPO_ID_CONTINUAZIONE%>','<%=lContMod.getIdContinuazione()%>');">
            <img src="<%=IWebConstants.IMAGES_DIR%>delete.gif" alt="Cancella" width="12" height="12" border="0">
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
</FORM>
</body>
</html>