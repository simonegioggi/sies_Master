<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeModel" %>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>



<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="FlagFasSiepe"     scope="request" class="java.lang.String"/>

<%
SoggettoModel soggetto =FascicoloSigeEsteso.getSoggetto();
FascicoloSiepModel fascicolo = FascicoloSigeEsteso.getFascicoloSiep();
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
if (fascicolo != null) {
%>
      <td class="L">
       <table>
      <tr>
      <td>
        <font class="label">Proc. SIEP </font>
        <font class="campo">
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%><%=retParam%>">
              <%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%>
            </a>
           &nbsp;&nbsp;<%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%>
         </font>&nbsp;
         </td>
         </tr>
        <%
        if (fascicolo.getDataInserimento() != null)
        {%>
      	<tr>
      	<td>
          <font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(fascicolo.getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
<%
}
%>
<%      
SentenzaModel sentenza = fascicolo.getSentenza();  
if (sentenza != null)
 {
 %>
     <tr>
      <td>
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:<font class="label"> N.</font>
        <font class="campo"> <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%>" title="Sentenza">
          <%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> </a>&nbsp;
          <font class="label">del</font>&nbsp;

            <%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>

        </font>
        &nbsp;<font class="label"> Emessa da: </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null)
        {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
<%
        }
%>
        <font class="label"> di </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
<%
        if (sentenza.getAnnoRegeGip() != null) {
%>
          &nbsp;<font class="label"> (N.Reg.Gen. </font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
          <font class="label"> GIP) </font>
<%
        } else {
          if (sentenza.getAnnoRegeDib() != null) {
%>
          &nbsp;<font class="label"> (N.Reg.Gen. </font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeDib())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeDib())%></font>
          <font class="label"> DIB) </font>
<%
		// MEV_66: aggiunte quattro nuove proprietà
		} else if (sentenza.getAnnoRegeGup() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGup())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGup())%></font>
			<font class="label"> GUP) </font>
<%
		} else if (sentenza.getAnnoRegeCapsm() != null) {
%>
 			&nbsp;<font class="label"> (N.Reg.Gen. </font>
 			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCapsm())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCapsm())%></font>
 			<font class="label"> CAPSM) </font>
<%
		// MEV_66: aggiunti anche CAS, CAP e CASAP
		} else if (sentenza.getAnnoRegeCap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCap())%></font>
			<font class="label"> CAP) </font>
<%
		} else if (sentenza.getAnnoRegeCas() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCas())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCas())%></font>
			<font class="label"> CAS) </font>
<%
		} else if (sentenza.getAnnoRegeCasap() != null) {
%>
			&nbsp;<font class="label"> (N.Reg.Gen. </font>
			<font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeCasap())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeCasap())%></font>
			<font class="label"> CASAP) </font>
<%
		}
	}
%>
		</td>
	</tr>
<%
}
%>   
          </td>
         </tr>
        </table>
      </td>     
     <% }%>