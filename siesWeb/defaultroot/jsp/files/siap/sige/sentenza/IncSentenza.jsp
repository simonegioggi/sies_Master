<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel" %>

<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>
<jsp:useBean id="sentenza" scope="session"	class="siap.siep.sentenza.model.SentenzaModel" />

<html>

<body class="corpo">
  <table cellspacing=0 cellpadding=0 width=95%>

 <%
 // presenza del Link per il bottone di ritorno
 boolean retFlag = false;
 retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
 String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
 String isSentenzaStranieraDelibata=request.getParameter("isSentenzaStranieraDelibata");
 
 String labelEmessaDa = "Emessa da: ";
 if (isSentenzaStranieraDelibata != null && !isSentenzaStranieraDelibata.equalsIgnoreCase("true"))
	 labelEmessaDa="Emesso da: ";
 
 if (sentenza != null)
 {
 %>
     <tr>
      <td class="L">
        <font class="label"><%=sentenza.getDescrTipoProvvedimento().substring(0,1).toUpperCase()+sentenza.getDescrTipoProvvedimento().substring(1).toLowerCase()%></font>&nbsp;:
<%
        if (sentenza.getNumeroSentenza()!=null && sentenza.getNumeroSentenza().length()>0)
        {%>
	        <font class="label"> N.</font>
        	<font class="campo"> 
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%><%=retParam%>" title="Sentenza">
          		<%=sentenza.getAnnoSentenza()%> / <%=sentenza.getNumeroSentenza()%> 
          	</a>&nbsp;
        	</font>
<%      // Modifica del 24/11/2016 MEV_15_S4 
		// Se il provvedimento è "CUMULO/ORDINANZA/DECRETO ARCHIVIAZIONE" visualizzo Anno e Numero Provvedimento 
		}else if (sentenza.getCodTipoProvvedimento() != null && (sentenza.getCodTipoProvvedimento().equals("13")
				  || sentenza.getCodTipoProvvedimento().equals("03") || sentenza.getCodTipoProvvedimento().equals("63"))
		) { %>
			<!-- segnalazioni 4: reso cliccabile anno e numero -->
	        <font class="label"> N.</font>
	        <font class="campo"> 
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%><%=retParam%>" title="Sentenza">
	          		<%-- Ticket#20221018019 in assenza di anno e numero provvedimento visualizzava NULL/NULL
	          		<%=sentenza.getAnnoProvvedimento()%> / <%=sentenza.getNumeroProvvedimento()%>
	          		--%> 
	          		<%=StringUtils.toStringJSP(sentenza.getAnnoProvvedimento(),"n.d.")%> / <%=StringUtils.toStringJSP(sentenza.getNumeroProvvedimento(),"n.d.")%> 
	          	</a>&nbsp;
        	</font>
<%      }else { %>
	        <font class="cRosso">
        		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.sentenza.action.ActLoadDettaglioSentenza&<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<%=sentenza.getIdSentenza()%><%=retParam%>" title="Sentenza">
          		NON NUMERATA 
          	</a>
	        </font>&nbsp;&nbsp;
<%		} %>
			          	
        <font class="label">del</font>&nbsp;<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd-MM-yyyy")%>&nbsp;
        <font class="label"><%=labelEmessaDa%></font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
<%
        if (sentenza.getNumSezioneAutoritaEmittente() != null) {
%>
          <font class="label">(Sez.</font> <font class="campo"><%=StringUtils.toStringJSP(sentenza.getNumSezioneAutoritaEmittente())%> </font> <font class="label">) </font>
			<%}%>
        <font class="label"> di </font>
        <font class="campo"><%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%></font>
<%
        if (sentenza.getAnnoRegeGip() != null)
        {
%>
          &nbsp;<font class="label"> (N.Reg.Gen. </font>
          <font class="campo"><%=StringUtils.toStringJSP(sentenza.getAnnoRegeGip())%> / <%=StringUtils.toStringJSP(sentenza.getNumeroRegeGip())%></font>
          <font class="label"> GIP) </font>
<%
        }
	else {
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
  if (sentenza instanceof SentenzaSigeModel)
  {
  	FascicoloSiepModel fascicoloSiep = ((SentenzaSigeModel)sentenza).getFascicoloSiep();
  	if (fascicoloSiep  != null)
		{%>
      <tr>
      <td class="L">
        <font class="label">Proc. SIEP </font>
        <font class="campo">
            <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicoloSiep.getIdFascicoloSiep()%><%=retParam%>">
              <%=fascicoloSiep.getChiaveAnno()%>/<%=fascicoloSiep.getChiaveProgr()%>
            </a>
           &nbsp;&nbsp;<%=fascicoloSiep.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSiep.getDescrComuneUfficio()%>
         </font>&nbsp;
        <%
        if (fascicoloSiep.getDataInserimento() != null)
        {%>
          <font class="label"> del </font>&nbsp;
          <font class="campo"><%=DateUtils.getDateToString(fascicoloSiep.getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
          </td>
         </tr>
 <%        
  	} // enfif fascicolo
  } // enfif instanceof SentenzaSigeModel
}  // endif sentenza %>   
     </table>
  </body>
</html>