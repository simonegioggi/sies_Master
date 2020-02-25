<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="penaaccessoria" scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Richiesta al GE per Pena Accessoria </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>

  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript" >
    function lookUpload()
    {
      var node;
      node=document.getElementById('upld');
      node.style.visibility='visible';
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Dettaglio <%=eventonotifica.getEvento().getDescrTipoProvvedimento()%></font>
      </td>

<%    if (eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
        if (eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0)
        {%>
					<!-- BOTTONE DI STAMPA -->
   				<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.penaaccessoria.action.ActStampaProvvedimentoPA&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   				</jsp:include>
			<%}
        if (eventonotifica.getEvento().getFlagDocumentoRegistrato()==null)
        {%>
					<!-- BOTTONE DI STAMPA -->
   				<jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     				<jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.penaaccessoria.action.ActStampaProvvedimentoPA&IdEvento="+eventonotifica.getEvento().getIdEvento()%>"/>
   				</jsp:include>
			<%}%>

    </tr>

    <tr>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    </tr>
   </table>
		<br>
    <table cellspacing=2 cellpadding=2>
    <tr>
<%
    if(penaaccessoria.getIdPenaAccessoria()!= null && penaaccessoria.getIdPenaAccessoria().toString().length()>1)
    {%>
      <td class="l">Tipo Pena Accessoria</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrTipoPenaAccessoria() )%></font>
      </td>
    </tr>
  <%}
    if(penaaccessoria.getCodNuovoTipoPenaAccessoria()!= null && penaaccessoria.getCodNuovoTipoPenaAccessoria().toString().length()>1)
    {%>
      <td class="l">Nuovo Tipo Pena Accessoria</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrNuovoTipoPenaAccessoria() )%></font>
      </td>
    </tr>
  <%}%>

   </table>
		<br>
    <table cellspacing=2 cellpadding=2>
      <tr>
<%
        if(eventonotifica.getEvento().getDataEmissione()!= null)
        {%>
          <td class="l">Data Richiesta</td>
          <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd-MM-yyyy") )%></font>
          </td>
<%
        }%>
      </tr>

      <tr>
<%
        if(eventonotifica.getEvento().getCodTipoProvvedimento()!= null)
        {
    			// Descrizione Tipo Richiesta GE.
    			Collection lColTipoRichiestaGE = null;
    			DecodificheModel lModel = new DecodificheModel();

    			IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
    			lModel.setContesto("TIPO_RICHIESTA_GE");
    			lColTipoRichiestaGE = lDecodifiche.ExRicercaDecodifiche(lModel);
					String lCodeRichiestaGE = DecodificheUtils.getCodebyCodAlt(lColTipoRichiestaGE, eventonotifica.getEvento().getCodTipoProvvedimento());
    			String descrTipoRichiestaGE = DecodificheUtils.getDescbyCode(lColTipoRichiestaGE, lCodeRichiestaGE);
%>
          <td class="l">Tipo Richiesta al GE</td>
          <td class="L">
            <font class="campo"><%=descrTipoRichiestaGE%></font>
          </td>
<%
        }%>
      </tr>

<%    // VISUALIZZAZIONE DESTINATARI
      for (int i=0;i<eventonotifica.getNotifiche().length;i++)
      {%>
      <tr>
        <td class="l">Destinatario N° <%=i+1%></td>
        <td class="L" colspan=3>
         <table>
           <tr>
           		<td class="l"><font class="campo">
  							<%if(eventonotifica.getNotifiche()[i].getUfficio()!= null)
           				{%>
             				<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrTipoUfficio())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getUfficio().getDescrComune())%>
         				<%}
                	else if(eventonotifica.getNotifiche()[i].getAutoritaEsterna()!= null)
           				{%>
              			<%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getAutoritaEsterna().getDescrTipoAutorita())%></font>&nbsp; di &nbsp; <font class="campo"><%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getAutoritaEsterna().getDescrSede())%>
         				<%}%>
							</font></td>
           </tr>

           <tr>
          		<td class="L" colspan=3>
          			<font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getNotifiche()[i].getNote() )%></font>
          		</td>
        </tr>
        </table>

      </tr>
		<%}
			if( eventonotifica.getCampoNote().length>0 )
			{%>
      	<tr>
        	<td class="l">Nota 1</td>
        	<td class="L" colspan=3>
          	  <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[0].getDescr() )%></font>
        	</td>
      	</tr>

		<%}
			if( eventonotifica.getCampoNote().length>1 )
			{%>
      	<tr>
        	<td class="l">Nota 1</td>
        	<td class="L" colspan=3>
            <font class="campo"> <%=StringUtils.toStringJSP(eventonotifica.getCampoNote()[1].getDescr() )%></font>
        	</td>
      	</tr>
		<%}%>
    </tr>
	</table>

 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penaaccessoria.action.ActUploadProvvedimentoPA">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.penaaccessoria.action.ActLoadDettaglioRichiestaGE">
          </td>
        </tr>
      </table>
    </form>
  </div>
 <br>
 <br>

  </body>
</html>