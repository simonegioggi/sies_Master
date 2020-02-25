<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="UfficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<% EventoModel lEve = evento;%>

<html>
  <head>
    <title> [S.I.E.S.] - Conferma Trasmissione Provvedimento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
  </head>
  <BODY class="corpo" onload="javascript:lookUpload();">
    <table>
      <tr>
				<td class="LBG">
					<a href="Javascript:window.print();">
						<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
					</a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
<% if(lEve.getCodMotivo().equals("1024"))
	{%>          
          <font class="campo">Conferma Trasmissione Ordine Esecuzione L. 78/2013</font>
<%  }
	else
	{	
		if(lEve.getCodMotivo().equals("1023") || 
		   lEve.getCodMotivo().equals("1022") ) 
		{   %>          
          		<font class="campo">Conferma Trasmissione Comunicazione L. 78/2013</font>
<%		
		}
		else
		{	%>          
          		<font class="campo">Conferma Trasmissione Provvedimento L. 199/2010</font>
<%		}
		
	}	%>          		
        </td>

				<!-- BOTTONE DI STAMPA -->
 				<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
   				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActStampaTrasmissioneProvvedimento&IdEvento="+lEve.getIdEvento()+"&CodUfficioDestinatario="+UfficioDestinatario.getCodUfficio() %>"/>
 				</jsp:include>
    	</tr>
    </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=5>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
	 <tr>
      <td class="l">Da inviare a </td>
      <td class="L" colspan=5>
        <font class="campo"><%=UfficioDestinatario.getDescrTipoUfficio()+ " di " + UfficioDestinatario.getDescrComune()%></font>&nbsp;
			</td>
    </tr>
    <tr>
  </table>
  <br>
    <div align=left style="visibility:hidden" id="upld">
      <FORM name="comandi" enctype="multipart/form-data" method="post"  onsubmit="document.forms[0].go.disabled=true;return true;">
<%
        String lAction = "siap.siep.ordineesecuzione.action.ActConfermaTrasmissioneProvvedimentoLS";
%>
		    <table cellspacing=2 cellpadding=2>
		      <tr>
		        <td>
		          <input name=go class=bottone  type="submit" value="Conferma Trasmissione " >
		        </td>
		      </tr>
		      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
		      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lEve.getIdEvento()%>">
			    <input type="HIDDEN" name="CodTipoUfficioDestinatario" value="<%=UfficioDestinatario.getCodTipoUfficio()%>">
			    <input type="HIDDEN" name="CodLuogoDestinatario" value="<%=UfficioDestinatario.getDescrComune()%>">
		    </table>

      </FORM>
    </div>
  <br>
</body>

</html>