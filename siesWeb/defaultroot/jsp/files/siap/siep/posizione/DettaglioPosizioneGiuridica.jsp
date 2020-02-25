<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.sico.ufficio.controller.UfficioUtils" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<jsp:useBean id="PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="ufficioPmTipoDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioPmSedeDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioRegGenDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmittenteCautelareDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCompetenteCautelareDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCompetenteDesc" scope="request" class="java.lang.String"/>

<%
  PosizioneGiuridicaModel lPosizione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getAltraCausa();
  MisuraCautelareModel lMisuraCautelare = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getMisuraCautelare();

  if(lPosizione == null)
  {
    lPosizione = new PosizioneGiuridicaModel();
  }

  if(lLuogoDetenzione == null)
  {
    lLuogoDetenzione = new LuogoDetenzioneModel();
  }

  if(lAltraCausa == null)
  {
    lAltraCausa = new AltraCausaModel();
  }
  
  if(lMisuraCautelare == null)
  {
	  lMisuraCautelare = new MisuraCautelareModel();
  }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Posizione Giuridica </title>
      <link rel="STYLESHEET" type="text/css"
       href="<%=IWebConstants.PG_STYLE%>">
       <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Posizione Giuridica</font>
        </td>
        <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
              <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" />
              <jsp:param name="ValoreIdEntita" value="<%=lPosizione.getIdPosizioneGiuridica()%>" />
            </jsp:include>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioPosizioneGiuridica">

<%
if ("L".equals(lPosizione.getCodMaschera())){
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-L.jsp" %>
<%	
} else if ("L1".equals(lPosizione.getCodMaschera())){
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-L1.jsp" %>
<%	
} else if ("L2".equals(lPosizione.getCodMaschera())){
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-L2.jsp" %>
<%	
} else if ("L3".equals(lPosizione.getCodMaschera())){
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-L3.jsp" %>
<%	
} else if ("EI".equals(lPosizione.getCodMaschera())){
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-EI.jsp" %>
<%	
} else if ("EA".equals(lPosizione.getCodMaschera())){
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-EA.jsp" %>
<%	
} else {
%>
<%@ include file="/jsp/files/siap/siep/posizione/DettaglioPosizioneGiuridica-Old.jsp" %>
<%	
}
%>

    </form>
  </body>
</html>