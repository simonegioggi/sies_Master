<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sico.camponota.model.CampoNotaModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>

<jsp:useBean id="eventonotifica"      scope="request" class="siap.sico.evento.model.EventoNotificaModel" />

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Richiesta Notizie Adempimento Obblighi</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>

<body class="corpo">

<table>
  <tr>
    <td class="LBG">
      <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    </td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Dettaglio Richiesta Notizie Adempimento Obblighi</font>
    </td>

    <%
    //=========================================================================  
    // Visualizzazione del BOTTONE DI STAMPA se evento non ancora validato
    //=========================================================================  
    if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
        || (   eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null
            && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
           ) 
       )
    {
    %>
      <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
        <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaRichiestaGenerica&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
      </jsp:include>
    <%
    } 
    %>
  </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <table  width="95%">
       <tr>
      <td class="l">Data Emissione </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd/MM/yyyy"), "-")%></font></td>
      </tr>
        <tr>
      <td class="l">Data Trasmissione </td>
      <td class="l"><font class="campo"> <%=StringUtils.toStringJSP( DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti() ,"dd/MM/yyyy"),"-")%></font></td>
      </tr> 
   <tr>
  <td class="l">Tipologia Obbligo</td>
  <td class="l">
    <font class="campo">
    <%=eventonotifica.getCampoNote()[0].getDescr()%>
    </font>
  </td>
</tr>
  
  <tr>
  <td class="l">Oggetto</td>
  <td class="l">
    <font class="campo">
    <%=eventonotifica.getEvento().getDescrMotivo()%>
    </font>
  </td>
</tr>
  <tr>
  <td class="l">Note</td>
  <td class="l">
    <font class="campo">
    <%=(eventonotifica.getCampoNote() != null && eventonotifica.getCampoNote().length > 1 )  ? eventonotifica.getCampoNote()[1].getDescr() : ""%>
    </font>
  </td>
</tr>
  </table>
<br>
  		<table style="width: 95%;">
			    <tr><td class="Titolo" colspan=4>Destinatari</td></tr>
<%    // VISUALIZZAZIONE DESTINATARI
      for (int i=0;i<eventonotifica.getNotifiche().length;i++)
      {
%>
      <tr>
      <%
      	if(eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("N")){%>
        <td class="l">Ufficio del Giudice</td>
      <%}else if (eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("NC")){%>
        <td class="l">Autorità di Polizia</td>
      <%}else if (eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("ND")){%>
        <td class="l">Avvocato</td>
      <%}else if (eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("AA")){%>
        <td class="l">Altra Autorità</td>
	<%} %>

        <td class="L" colspan=3>
         <table>
         <tr>
         	<td class="l"><font class="campo">
  			<%if(eventonotifica.getNotifiche()[i].getUfficio()!= null)
           		{%>
             	<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrComune())%>
				<%}	
  	       	else if(eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("NC")||
  	       		eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("AA"))
           		{%>
            	<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getAutoritaEsterna().getDescrSede())%>
         		<%}
  			else if(eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("ND"))
           		{
  			     AvvocatoSiepModel lAvvMod = eventonotifica.getNotifiche()[i].getAvvSiep();
  			     AutoritaEsternaModel lAuMod = eventonotifica.getNotifiche()[i].getAutoritaEsterna();
           		%>
            	<%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getCognome())+" "+StringUtils.toStringJSP(lAvvMod.getAvvocato().getNome())%></font>&nbsp; 
            	&nbsp;Foro di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getForo())%>
        </font>
        &nbsp;Difensore di&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAvvMod.getAvvocato().getDescrTipo())%>
         		<%}%>
			</font></td>
          </tr>
      <%
      	if(eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("N") && 
      			eventonotifica.getNotifiche()[i].getNote()!=null &&
      			!eventonotifica.getNotifiche()[i].getNote().equals("-")){%>
          <tr>
          	<td class="L" colspan=3>
          			<font class="campo"> sez. 
          			<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getNote() )%></font>
          	</td>
      <%}else if ((eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("NC")||
    		  eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("ND")||
    		  eventonotifica.getNotifiche()[i].getCodTipoNotifica().equals("AA") )&&
   				eventonotifica.getNotifiche()[i].getNote()!=null){%>
          <tr>
          	<td class="L" colspan=3>
          			<font class="campo"> 
          			<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getNote() )%></font>
          	</td>
        </tr>
	<%} %>
        </table>

      </tr>
<%} %>


</table>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penasospesa.action.ActUploadRichiestaRevoca">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.penasospesa.action.ActDettaglioRicAdempObblighi">
          </td>
        </tr>
      </table>
</form>
</div>
  <br>
  <br>
</body>
</html>