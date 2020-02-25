<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="UfficioDestinatario" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>

<jsp:useBean id="ufficioge"       		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiouds"      		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiotds"         	scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiopm"        		scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="istituto"         		scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<jsp:useBean id="autoritaEsterna"         scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="noteautoritaEsterna"         scope="request" class="java.lang.String"/>

<!-- LoadConfermaTrasmissioneAttiExArt51Bis -->
<html>
  <head>
    <title> [S.I.E.S.] - Trasmissione Atti Richieste Cessazione/prosecuzione Mis Alt - Ex art 51 Bis  </title>
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
			</a>
		</td>
        <td class="LBG">
  <%	String Rich = eventonotifica.getEvento().getDescrMotivo();  %>      
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Conferma Trasmissione Atti - <%=Rich%> </font>
        </td>
				<!-- BOTTONE DI STAMPA -->
 				<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
   				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaTrasmissioneAttiExArt51Bis&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&CodUfficioDestinatario="+UfficioDestinatario.getCodUfficio() %>"/>
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
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Da inviare a </td>
      <td class="L" colspan=5>
        <font class="campo"><%=UfficioDestinatario.getDescrTipoUfficio()+ " di " + UfficioDestinatario.getDescrComune()%></font>&nbsp;
	  </td>
    </tr>
      <tr><td>&nbsp;</td></tr>
      <tr><td class="Titolo" align="center" colspan=6> Altri Destinatari </td></tr>
      <tr><td>&nbsp;</td></tr>
   </table>
   
   <table cellspacing=2 cellpadding=2> 
 <!--  Altri Destinatari -->
 
 <%
  if(ufficioge != null && ufficioge.getDescrTipoUfficio() != null
    && !ufficioge.getDescrTipoUfficio().equals(""))
  {
%>
    <tr>
      <td class="l">Ufficio Giudice dell'Esecuzione</td>
      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(ufficioge.getDescrTipoUfficio())%></font>&nbsp;di
         <font class="campo"><%=StringUtils.toStringJSP(ufficioge.getDescrComune())%></font>
       </td>
    </tr>
<%
  }

  if(ufficiouds != null && ufficiouds.getDescrComune() != null
    && !ufficiouds.getDescrComune().equals(""))
  {
%>
    <tr>
      <td class="l">Magistrato di Sorveglianza</td>
      <td class="l">
         di
         <font class="campo"><%=StringUtils.toStringJSP(ufficiouds.getDescrComune())%></font>
       </td>
    </tr>
<%
  }
  
  if(ufficiotds != null && ufficiotds.getDescrComune() != null
    && !ufficiotds.getDescrComune().equals(""))
  {
%>
    <tr>
      <td class="l">Tribunale di Sorveglianza</td>
      <td class="l">
         di
         <font class="campo"><%=StringUtils.toStringJSP(ufficiotds.getDescrComune())%></font>
       </td>
    </tr>
<%
  }
  
  if(ufficiopm != null && ufficiopm.getDescrTipoUfficio() != null
     && !ufficiopm.getDescrTipoUfficio().equals(""))
  {
%>
    <tr>
      <td class="l">Ufficio Pubblico Ministero</td>
      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(ufficiopm.getDescrTipoUfficio())%></font>&nbsp;di
         <font class="campo"><%=StringUtils.toStringJSP(ufficiopm.getDescrComune())%></font>
       </td>
    </tr>
<%
  }
  
  if(istituto != null && istituto.getDescrTipoIstituto() != null
     && !istituto.getDescrTipoIstituto().equals(""))
  {
%>
    <tr>
      <td class="l">Istituto di detenzione</td>
      <td class="l">
         <font class="campo"> <%=StringUtils.toStringJSP(istituto.getDescrTipoIstituto())%></font>&nbsp;di
         <font class="campo"><%=StringUtils.toStringJSP(istituto.getDescrComune())%></font>
       </td>
    </tr>
<%
  }

  if(autoritaEsterna!= null && autoritaEsterna.getCodTipoAutorita() != null && autoritaEsterna.getCodSede()!= null)
  {	%>

	  <tr>
	    <td class="l">Autorità di Polizia</td>
	    <td class="L">
	    <font class="campo">
	         <%=StringUtils.toStringJSP(autoritaEsterna.getDescrTipoAutorita())%></font>
	         
 <%		if(autoritaEsterna !=null && !autoritaEsterna.getDescrSede().equals("-"))
		{%>
			di <font class="campo"><%=StringUtils.toStringJSP(autoritaEsterna.getDescrSede()) %></font>
<%		}%>

	     </td>
	   </tr>

<%		if(noteautoritaEsterna != null && !noteautoritaEsterna.equals(""))
		{	%>
			<tr>
			<td class="l">Indirizzo</td>
			    <td class="l">
			         <font class="campo"><%=noteautoritaEsterna%>&nbsp;</font>
			    <td>
			</tr>

<%		}
   }
%>   
	<br>
  </table>
  <br>
    <div align=left style="visibility:hidden" id="upld">
      <FORM name="comandi" enctype="multipart/form-data" method="post"  onsubmit="document.forms[0].go.disabled=true;return true;">
<%
        String lAction = "siap.siep.richiesta.action.ActConfermaTrasmissioneAttiExArt51Bis";
%>
		    <table cellspacing=2 cellpadding=2>
		      <tr>
		        <td>
		          <input name=go class=bottone  type="submit" value="Conferma Trasmissione " >
		        </td>
		      </tr>
		      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
		      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
			    <input type="HIDDEN" name="CodTipoUfficioDestinatario" value="<%=UfficioDestinatario.getCodTipoUfficio()%>">
			    <input type="HIDDEN" name="CodLuogoDestinatario" value="<%=UfficioDestinatario.getDescrComune()%>">			    
			    
		    </table>

      </FORM>
    </div>
  <br>
</body>

</html>