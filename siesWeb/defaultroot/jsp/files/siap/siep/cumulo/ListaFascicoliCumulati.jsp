<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.siep.cumulo.model.CumuloModel"%>
<%@ page import="java.util.List"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<jsp:useBean id="PosizioneGiuridica" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="PenaResidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="cumulo" scope="request" class="java.util.Vector" />
<jsp:useBean id="cumulati" scope="request" class="java.util.Vector" />
<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="/html/conferma.js"></script>

</head>

<body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Richiesta Fascicoli Soggetti a Cumulo</font>
      </td>
    </tr>
  </table>
 <br>
 <table>
 <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
 <tr><td align=center><br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br></td></tr>
<tr>
  <td class=l>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
 </tr>
</table>
<br>
<% if (PenaResidua!=null)
{
%>
<table width=100%>
 <tr><td  width=100% class=Titolo>Fascicoli Cumulati</td></tr>
</table>
<table width=100% cellpadding=1 cellspacing=4>
<%}
int contaprovv = 1;
CumuloModel lCumMod = new CumuloModel();
for (int i=0;i<cumulo.size();i++)
{
   lCumMod = (CumuloModel)cumulo.get(i);

 if(lCumMod.getSentenza() != null && lCumMod.getSentenza().getCodTipoProvvedimento().equals("13"))
 {
  String AutEmi=lCumMod.getSentenza().getDescrTipoAutoritaEmittente()+" di "+lCumMod.getSentenza().getDescrLuogoEmittente();
%>
  <tr>
     <td class=r width=5%><strong><%=contaprovv%>.</strong></td>
     <td class=l width=95%>
         <table width=100%>
            <tr><td class=l width=20%>Provvedimento : </td><td class=l><font class=campo><%=lCumMod.getSentenza().getDescrTipoProvvedimento()%></font>
               <font class=campo><%=lCumMod.getSentenza().getAnnoSentenza()+"/"+lCumMod.getSentenza().getNumeroSentenza()%></font>
               Emesso in data <font class=campo><%=StringUtils.toStringJSP(DateUtils.getDateToString(lCumMod.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>
            </td></tr>
            <tr><td class=l>Autorità Emittente  : </td><td class=l><font class=campo><%=AutEmi%></font></td></tr>
         </table>
     </td>
     <td class="r" width="5%">
     	<a href="Javascript:conferma('siap.siep.cumulo.action.ActCancellaFascCumulato','<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>','<%=lCumMod.getSentenza().getIdSentenza()%>');">
        	<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
        </a>
     </td>
   </tr>
<%
  contaprovv++;
 }
}
SentenzaModel lSen;
FascicoloSiepModel lFascMod = new FascicoloSiepModel();
for (int i = 0; i < cumulati.size(); i++) {
	lFascMod =(FascicoloSiepModel)cumulati.get(i);
	lSen=((FascicoloSiepModel)(cumulati.get(i))).getSentenza();
	String TipoProv=lSen.getDescrTipoProvvedimento();
	String DataEmiss=StringUtils.toStringJSP(DateUtils.getDateToString(lSen.getDataProvvedimento(),"dd-MM-yyyy"),"-");
	String AnNumSen=lSen.getAnnoSentenza()+"/"+lSen.getNumeroSentenza();
	String reg="";
	String anno_reg="";
	String num_reg="";
	if (lSen.getAnnoRegeCap() != null) {
		reg="CAP";
		anno_reg=lSen.getAnnoRegeCap()+"";
		num_reg=lSen.getNumeroRegeCap()+"";
   	}
   	if (lSen.getAnnoRegeCas() != null) {
		reg="CAS";
		anno_reg=lSen.getAnnoRegeCas()+"";
		num_reg=lSen.getNumeroRegeCas()+"";
   	}
   	if (lSen.getAnnoRegeDib() != null) {
		reg="DIB";
		anno_reg=lSen.getAnnoRegeDib()+"";
		num_reg=lSen.getNumeroRegeDib()+"";
   	}
   	if (lSen.getAnnoRegeCasap() != null) {
		reg="CASAP";
		anno_reg=lSen.getAnnoRegeCasap()+"";
		num_reg=lSen.getNumeroRegeCasap()+"";
   	}
   	if (lSen.getAnnoRegeGip() != null) {
		reg="GIP";
		anno_reg=lSen.getAnnoRegeGip()+"";
		num_reg=lSen.getNumeroRegeGip()+"";
   	}
	// MEV_66: aggiunte quattro nuove proprietà
	if (lSen.getAnnoRegeGup() != null) {
		reg="GUP";
     	anno_reg=lSen.getAnnoRegeGup()+"";
     	num_reg=lSen.getNumeroRegeGup()+"";
   	}
	if (lSen.getAnnoRegeCapsm() != null) {
		reg="CAPSM";
     	anno_reg=lSen.getAnnoRegeCapsm()+"";
     	num_reg=lSen.getNumeroRegeCapsm()+"";
   	}

  	String AutEmi = lSen.getDescrTipoAutoritaEmittente() + " di " + lSen.getDescrLuogoEmittente();
  	if (lSen.getNumSezioneAutoritaEmittente() != null)
		AutEmi += " - sez. " + lSen.getNumSezioneAutoritaEmittente();
%>
  <tr>
     <td class=r width=5%><strong><%=contaprovv%>.</strong></td>
     <td class=l width=95%>
         <table width=100%>
            <tr><td class=l width=20%> Provvedimento : </td><td class=l><font class=campo><%=TipoProv%></font>
             <font class=campo><%=AnNumSen%></font> Emesso in data  <font class=campo><%=DataEmiss%></font></td></tr>
             <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
            <tr><td class=l>Anno/Numero Reg.Gen.  : </td><td class=l><font class=campo><%=anno_reg%>/<%=num_reg%>&nbsp;<%=reg%></font></td></tr>
            <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          	<%-- 
			// modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
          	if(lSen.getDataIrrevocabilita() != null)
            {%>
            <tr><td class=l>Definitivo in data: </td><td class=l><font class=campo><%=DateUtils.getDateToString(lSen.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td></tr>
          	< %}%>
          	--%>
            <tr><td class=l>Autorità Emittente  : </td><td class=l><font class=campo><%=AutEmi%></font></td></tr>
          <%if(lFascMod.getChiaveAnno() != null && lFascMod.getChiaveProgr() != null)
            {%>
            <tr><td class=l>Iscritta all'Anno/Numero Siep  : </td><td class=l><font class=campo><%=StringUtils.toStringJSP(lFascMod.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lFascMod.getChiaveProgr())%></font></td></tr>
          <%}
           if(lFascMod.getChiaveUfficio() != null)
            {%>
            <tr><td class=l>Autorità  : </td><td class=l><font class=campo><%=StringUtils.toStringJSP(lFascMod.getDescrTipoUfficio())%> di <%=StringUtils.toStringJSP(lFascMod.getDescrComuneUfficio())%></font></td></tr>
          <%}
           if(lSen.getNote() != null)
            {%>
            <tr><td class=l>Note  : </td><td class=l><font class=campo><%=StringUtils.toStringJSP(lSen.getNote())%></font></td></tr>
          <%}%>
         </table>
     </td>
     <td class="r" width="5%">
     	<a href="Javascript:conferma('siap.siep.cumulo.action.ActCancellaFascCumulato','<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>','<%=lSen.getIdSentenza()%>');">
        	<img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
        </a>
     </td>
  </tr>
<%
	contaprovv++;
}
%>
</table>


</body>
</html>