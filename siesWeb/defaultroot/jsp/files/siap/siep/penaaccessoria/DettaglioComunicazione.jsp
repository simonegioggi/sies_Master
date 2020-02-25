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

<%
  // Descrizione Tipo Tenore Ordinanza.
  Collection lColTipoTenore = null;
  DecodificheModel lModel = new DecodificheModel();
  IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
  lModel.setContesto("TENORE_ORDINANZA_PA");
  lColTipoTenore = lDecodifiche.ExRicercaDecodifiche(lModel);
  String descrTipoTenore = DecodificheUtils.getDescbyCode(lColTipoTenore, penaaccessoria.getFlagCondonata());
  String descrNuovoTipoPA = "-";

  // Descrizione Nuovo Tipo Pena Accessoria.
  if (penaaccessoria.getCodNuovoTipoPenaAccessoria()!= null && penaaccessoria.getCodNuovoTipoPenaAccessoria().length() > 1)
  {
    Collection lColNuovoTipoPA = null;
    DecodificheModel lModel2 = new DecodificheModel();
    lModel2.setContesto("TIPO_PENA_ACCESSORIA");
    lColNuovoTipoPA = lDecodifiche.ExRicercaDecodifiche(lModel2);
    descrNuovoTipoPA = DecodificheUtils.getDescbyCode(lColNuovoTipoPA, penaaccessoria.getCodNuovoTipoPenaAccessoria());
  }
%>
<html>
<head>
  <title>[S.I.E.S.] - Dettaglio Comunicazione per Pena Accessoria </title>
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

          <!-- BOTTONE DI RITORNO -->
          <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>

    </tr>

    <tr>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    </tr>
   </table>
		<br>
    <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">Tipo Pena Accessoria</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrTipoPenaAccessoria() )%></font>
      </td>
    </tr>

<%
		if(penaaccessoria.getCodNuovoTipoPenaAccessoria().trim().compareTo("-")!=0)
		{%>
			<tr>
      	<td class="l">Nuovo Tipo Pena Accessoria</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrNuovoTipoPenaAccessoria()) %></font></td>
			</tr>
	<%}%>

   </table>

		<br>
    <table cellspacing=2 cellpadding=2>
      <tr>
<%
        if(eventonotifica.getEvento().getDataEmissione()!= null)
        {%>
          <td class="l">Data Emissione</td>
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
    			// Descrizione Tipo Comunicazione.
    			Collection lColTipoComunicazione = null;
    			lModel = new DecodificheModel();

    			lDecodifiche = SICOLookupRemote.getDecodificheRemote();
    			lModel.setContesto("TIPO_COMUNICAZIONE_PA");
    			lColTipoComunicazione = lDecodifiche.ExRicercaDecodifiche(lModel);
					String lCodComunicazione = DecodificheUtils.getCodebyCodAlt(lColTipoComunicazione, eventonotifica.getEvento().getCodTipoProvvedimento());
    			String descrTipoComunicazione = DecodificheUtils.getDescbyCode(lColTipoComunicazione, lCodComunicazione);
%>
          <td class="l">Tipo Comunicazione</td>
          <td class="L">
            <font class="campo"><%=descrTipoComunicazione%></font>
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

	<table>
    <tr><td class="Titolo" colspan=4>Estremi Ordinanza Condono/Revoca/Sostituzione</td></tr>
		<tr>
      <td class="l">Tenore Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(StringUtils.toStringJSP(descrTipoTenore ))%></font></td>
		</tr>

    <tr>
      <td class="l">Data Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(), "dd-MM-yyyy"))%></font></td>
		</tr>

    <tr>
      <td class="l">Anno/Numero Ordinanza</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(penaaccessoria.getAnnoOrdinanzaPA())%>&nbsp;/&nbsp;<%=StringUtils.toStringJSP(penaaccessoria.getNumeroOrdinanzaPA())%>&nbsp;
        </font>
      </td>
    </tr>

    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrTipoUfficioOrdinanzaPA())%></font>&nbsp;</td>
    </tr>
    <tr>
      <td class="l">Luogo Ordinanza</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrLuogoUfficioOrdinanzaPA())%></font>&nbsp;</td>
    </tr>

<%------------%>
    <tr>
      <td class="l" colspan ="2" >
        <font class="l">Fonte&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrFonteGE())%></font>
        <font class="l">&nbsp;&nbsp;Anno&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getAnnoFonteGE())%></font>
        <font class="l">&nbsp;&nbsp;Num.&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumeroFonteGE())%></font>
        <font class="l">&nbsp;&nbsp;Art.&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getArticoloGE())%></font>
        <font class="l">&nbsp;&nbsp;Art.Qualificante&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrSottonumerazioneGE())%></font>
        <font class="l">&nbsp;&nbsp;Comma&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getCommaGE())%></font>
        <font class="l">&nbsp;&nbsp;Let.&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getLetteraGE())%></font>
        <font class="l">&nbsp;&nbsp;Num.&nbsp;</font>
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumeroGE())%></font>
      </td>
    </tr>
<%------------%>

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
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.penaaccessoria.action.ActLoadDettaglioComunicazione">
          </td>
        </tr>
      </table>
    </form>
  </div>
 <br>
 <br>

  </body>
</html>