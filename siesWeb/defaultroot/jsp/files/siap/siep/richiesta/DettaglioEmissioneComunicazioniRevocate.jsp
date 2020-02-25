<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale"%>

<jsp:useBean id="eventonotifica"     scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="annotazioneManuale" scope="request" class="java.util.Vector"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistrato.model.MagistratoModel" />
<jsp:useBean id="codice"             scope="request" class="java.lang.String" />
<jsp:useBean id="partenza"           scope="request" class="java.lang.String" />
<jsp:useBean id="dettaglioPM"        scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per la visualizzazione del dettaglio della Comunicazione nel
// caso di Depenalizzazione/Incostituzionalità (Revoca sentenza). Viene utilizzata 
// sia nel caso delle 'Richieste al GE' che  nel caso delle 'Decisioni del GE'
// La form consente di produrre la stampa e validare l'evento
// 0299 - Richiesta al GE  
// 0300 - Decisione del GE 
//==============================================================================
%>
<html>
<head>
  <title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
</head>
<body class="corpo">
<table>
  <tr>
    <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
    if(codice.equals("0300"))
    {
%>
      <font class="campo">Dettaglio Revoca Sentenza per Abolizione Reato</font>
<%
    }
    else if(codice.equals("0299"))
    {
%>
      <font class="campo">Dettaglio Nuovo Residuo Pena </font>
<%
    }
%>
    </td>

<%
    //==========================================================================
    // Se l'evevnto non è ancora validato visualizzo il bottone di stampa
    //==========================================================================
    if (   eventonotifica.getEvento().getFlagDocumentoRegistrato()==null
        || (   eventonotifica.getEvento().getFlagDocumentoRegistrato()!=null)
            && eventonotifica.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0
           )
    {
    %>
    <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
      <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.richiesta.action.ActStampaEmissioneComunicazioni&IdEvento="+eventonotifica.getEvento().getIdEvento()+"&codice="+codice%>"/>
    </jsp:include>
    <%
    }
    %>

    <%
    // Tasto Indietro
    if(dettaglioPM!=null && !dettaglioPM.equals("SI"))
    {
      if(codice.equals("0300")){%>
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniBenefici" >
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <%}else{%>
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste&<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>=<%=StringUtils.toStringJSP(eventonotifica.getEvento().getAnnIdAnnotazioneManuale())%>" >
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      <%}
    }%>
  </tr>
</table>

<br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<table>
  <tr>
    <td class="l">Posizione Giuridica</td>
    <td class="L" colspan=5>
      <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font>
    </td>
  </tr>
</table>


  <table width="70%">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan=8 class="titolo">Richiesta</td>
    </tr>
<%
      Iterator lItxAnn = annotazioneManuale.iterator();
      while(lItxAnn.hasNext())
      {
        AnnotazioneManualeModel lAnn =  (AnnotazioneManualeModel)lItxAnn.next();
%>
          <tr>
            <td class="l">Data Richiesta</td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataGE(),"dd-MM-yyyy"))%>&nbsp;
              </font>
            </td>
            <td class="l">Reato :</td>
            <td class="L">
              <font class="campo">
              	<%-- MERGE v10: modificato commento --%>
                <%--//=StringUtils.toStringJSP(lAnn.getCodFonte())--%>
                <%=StringUtils.toStringJSP(lAnn.getDescrFonte())%>
              </font>
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnn.getAnnoFonte())%>/
              </font>
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnn.getNumeroFonte())%>
              </font>
              Art.
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnn.getArticolo())%>
              </font>
              &nbsp;
              <font class="campo">
              	<%-- MEV 16 CUMULO: modificato commento --%>
                <%--//=StringUtils.toStringJSP(lAnn.getCodSottonumerazione())--%>
                <%=StringUtils.toStringJSP(lAnn.getDescrSottonumerazione())%>
              </font>
              C.
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnn.getComma())%>
              </font>
              L.
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnn.getLettera())%>
              </font>
              N.
              <font class="campo">
                <%=StringUtils.toStringJSP(lAnn.getComma())%>
              </font>
            </td>
          </tr>
<%
  }
%>
<%
    if(magistrato != null)
    {
%>
      <tr>
        <td class="l">Magistrato Assegnatario
        <td class="L" colspan=5>
          <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome() )%></font>
          <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
        </td>
      </tr>
<%
    }


		if(eventonotifica.getNotifiche() != null && eventonotifica.getNotifiche().length >0)
		{
  		List lNotifiche = Arrays.asList(eventonotifica.getNotifiche());
  		Iterator lIterNotifiche = lNotifiche.iterator();
			
  		while(lIterNotifiche.hasNext())
  		{
  		  NotificaModel lNot = (NotificaModel)lIterNotifiche.next();
  		  
  		  if(lNot != null && lNot.getUfficio() != null)
  		  {
%>   
					<tr>
					  <td class="l">Destinatario</td >
<%				if(lNot.getUfficio().getCodTipoUfficio().equals("UDS") )
				{
%>					  
					   <td class="L" colspan=5><font class="campo">UFFICIO DI SORVEGLIANZA</font>
<%				}
				else if(lNot.getUfficio().getCodTipoUfficio().equals("TDS") )
				{
	%>		
						<td class="L" colspan=5><font class="campo">TRIBUNALE DI SORVEGLIANZA</font>
<%				} 
				else
				{				%>	
						<td class="L" colspan=5><font class="campo"> - - - </font>
		<%		} %>										  
					</tr>
					<tr>
					  <td class="l">Sede</td>
					  <td class="L" colspan=5>
					  	<font class="campo"><%=StringUtils.toStringJSP(lNot.getUfficio().getDescrComune())%></font>
					  </td>
					</tr>
					<tr>
					  <td class="l">Note</td>
					  <td  class="L" colspan=5>
					  	<font class="campo"><%=StringUtils.toStringJSP(lNot.getNote())%>&nbsp;</font>
					  </td>
					</tr>
<%
  		  }
  		  
  		  if(lNot != null && lNot.getAutoritaEsterna() != null)
  		  {
%>
					<tr>
					 <td class="l">Destinatario</td >
					 <td class="L" colspan=5> <font class="campo"><%=lNot.getAutoritaEsterna().getDescrTipoAutorita()%></font>
					<tr>
					  <td class="l">Sede</td>
					  <td class="L" colspan=5>
					    <font class="campo"><%=StringUtils.toStringJSP(lNot.getAutoritaEsterna().getDescrSede())%></font>
					  </td>
					</tr>
					<tr>
					  <td class="l">Note</td>
					  <td class="L" colspan=5>
					    <font class="campo"><%=StringUtils.toStringJSP(lNot.getNote())%>&nbsp;</font>
					  </td>
					</tr>
<%
				}
   		}
		}
%>
    <tr><td>&nbsp;</td></tr>
  </table>
  <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActUploadEmissioneComunicazioni">
            <input type="HIDDEN" name="codice" value="<%=codice%>">
            <input type="HIDDEN" name="partenza" value="<%=partenza%>">

            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%= eventonotifica.getEvento().getIdEvento() %>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.richiesta.action.ActDettaglioEmissioneComunicazioni">
          </td>
        </tr>
      </table>
    </form>
  </div>
  <br>
  <br>
</body>
</html>