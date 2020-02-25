<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>


<html>

  <head>
    <title> [S.I.E.S.] - Dettaglio Comunicazione Inizio Esecuzione- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

   </head>
<%
	EventoNotificaModel lEve = eventonotifica;
 %>
  <BODY class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Comunicazione Inizio Esecuzione</font>
        </td>


<!-- BOTTONE DI STAMPA -->
<%
	
	if(evento != null && evento.getIdEvento()!=null && evento.getFlagDocumentoRegistrato().equals("N"))
	{
%>
	   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
	      <jsp:param name="ActionLink" value="<%= "/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActStampaInizioEsecuzione&IdEvento="+evento.getIdEvento()+"&CodMotivo="+evento.getCodMotivo() %>"/>
	    </jsp:include>
<%	} 
%>
      </tr>
    </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

  <table cellspacing=4 cellpadding=4>
  
	
<%
	// Paolo Cherubini qui ci si arriva per '0047' Comunicazione inizio esecuzione  e  '1048' Comunicazione esecutività sentenza    
	// nel primo caso controllo se è gia presente poichè ne può essere inserita una sola
	String lData;
	if (evento != null && evento.getIdEvento()!=null && evento.getFlagDocumentoRegistrato() != "N" && evento.getCodMotivo().equals("0047"))	
	{
		
%>
                <tr><td class="Titolo" colspan=6><font color=red>Inizio Esecuzione presente in Archivio  </font></td></tr>
<%	}else
	{ 		lData = StringUtils.toStringJSP(DateUtils.getSysDate("dd-MM-yyyy"));
	}
	
	lData = StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"dd-MM-yyyy"));
%>


    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=lData%></font>&nbsp;
      </td>
    </tr>
    
<%
	if (evento.getCodMotivo().equals("0047"))
	{ 
%>	
    <tr>
       <td class="l">Autorita Destinatario</td>
       <!-- a7/rr/168 -->
      <td class="L">
      <font class="campo">Cancelleria&nbsp;
      <%if(sentenza.getDescrTipoAutoritaEmittente().toUpperCase().startsWith("SEZIONE") ||
    		sentenza.getDescrTipoAutoritaEmittente().toUpperCase().startsWith("PROCURA")||
    		sentenza.getDescrTipoAutoritaEmittente().toUpperCase().startsWith("CORTE")
    		){%>
      	della
      <%}else{%>
      	del
      	<%} %>
       	<%=StringUtils.toStringJSP(sentenza.getDescrTipoAutoritaEmittente())%>
		&nbsp;di
       	<%=StringUtils.toStringJSP(sentenza.getDescrLuogoEmittente())%>
        </font>
      </td>
     </tr>
     
     <% }else if(lEve != null && lEve.getNotifiche()!= null 
    		 && lEve.getNotifiche().length > 0 && lEve.getNotifiche()[0].getIstitutoDetenzione() != null)
        {	
%>
		    <tr>
		     <td class="l">Autorità Destinazione</td>
		     <td class="L" colspan=2>
		       <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getIstitutoDetenzione().getDescrTipoIstituto())%></font>&nbsp;
		       di
		       <font class="campo"><%=StringUtils.toStringJSP( lEve.getNotifiche()[0].getIstitutoDetenzione().getDescrComune())%></font>&nbsp;
		     </td>
		    </tr>
<%		}
%> 
     
  </table>

 <br>
<div align=left style="visibility:hidden" id="upld">
  <FORM name="comandi" enctype="multipart/form-data" method="post">
     <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
       <tr>
        <td class="L">
          <input  class=bottone  type="submit" value="Conferma">
           <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActValidaInizioEsecuzione">
           <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"  value="<%=  evento.getIdEvento() %>">
           <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>"  value="<%=  evento.getCodMotivo() %>">
           <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.siep.istruttoria.action.ActDettaglioInizioEsecuzione">
        </td>
       </tr>
      </table>
  </FORM>
 </div>

  <br>
</body>

</html>