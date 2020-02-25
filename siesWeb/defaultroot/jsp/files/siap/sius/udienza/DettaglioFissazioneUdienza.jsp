<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.sius.udienza.model.UdienzaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.tenore.model.TenoreModel"%>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate"%>
<%@ page import="siap.sius.curatore.action.ICostantiCuratoreSius"%>
<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>
<%@ page import="siap.sius.avvocatura.action.ICostantiAvvisiAvvocato" %>

<jsp:useBean id="eventonotifica"  scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="udienza"         scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="fascicoloGP"     scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocato"        scope="request" class="java.util.Vector" />
<jsp:useBean id="contenuto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tenori"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="depositoDecreto" scope="request" class="siap.sius.depositodecreto.model.DepositoDecretoModel"/>
<jsp:useBean id="curatore"      	scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>
<jsp:useBean id="TornaQui"        scope="request" class="java.lang.String"/>


<%
  EventoNotificaModel lEve = eventonotifica;
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

%>
<script language="JavaScript" src="/html/conferma.js"></script>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Fissazione Udienza- </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" >
      function lookUpload()
      {
        var node;
        node=document.getElementById('upld');
			  node.style.visibility='visible';
      }
    </script>
  </head>

  <%
  /*
  if(lEve.getEvento().getFlagDocumentoRegistrato()!= null)
  {
    if (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0)
    {
  */
  // 20131206 - Cambiata la logica di condizione per inserire il body normale nel caso il  
  // FlagDocumentoRegistrato non sia valorizzato e impostato a "S".
  if( lEve.getEvento().getFlagDocumentoRegistrato()!= null && lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0 ) {
  %>
   <BODY class="corpo" onload="javascript:lookUpload();">
  <%
  } else {
  %>
   <BODY class="corpo">
  <%
  }
  %>
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Fissazione Udienza</font>
        </td>
<%
if (request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO) != null  && modalita.compareToIgnoreCase("I" )== 0 )
{
%>
          <jsp:include page="<%=ICostantiUdienza.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=request.getAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)%>" />
          </jsp:include>
<%
}
  if ((lEve.getEvento().getFlagDocumentoRegistrato()==null) || (lEve.getEvento().getFlagDocumentoRegistrato()!=null) && (lEve.getEvento().getFlagDocumentoRegistrato().compareTo("N")==0))
    {
  %>
  <!-- BOTTONE DI STAMPA -->
    <jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIUS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEve.getEvento().getIdEvento()%>" />
          </jsp:include>
  <%
    }
  %>

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
  </tr>

  <tr>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  </tr>
  <tr>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>"/>
  </tr>
  </table>

  <table cellspacing="2" cellpadding="2">

    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEve.getEvento().getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
<% if (depositoDecreto.getDataDeposito() != null)
{ %>
  <tr>
    <td class="l"> Anno / Numero del Decreto</td>
    <td class="l">
        <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.depositodecreto.action.ActLoadInserisciDataDepositoDecreto&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=depositoDecreto.getIdEventoGenerato()%>&TornaQui=<%=TornaQui%>">
           <%=StringUtils.toStringJSP(depositoDecreto.getAnnoS72())%>
           /
           <%=StringUtils.toStringJSP(depositoDecreto.getNumS72())%>
        </a>
    </td>
 </tr>
  <tr>
    <td class="l"> Data Deposito in Cancelleria</td>
    <td class="l"> <font class="campo"><%=DateUtils.getDateToString(depositoDecreto.getDataDeposito(),"dd-MM-yyyy")%></font></td>
  </tr>
<% } %>
    <tr>
      <td class="l">Data Udienza</td>
      <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(udienza.getDataUdienza(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l" nowrap>Luogo Svolgimento Udienza</td>
      <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP(depositoDecreto.getNote())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Contenuto </td>
       <td class="L" colspan=1 width=100%><font class="campo">
        <%=fascicoloGP.getGeneraleProcedimentoModel().getDescrOggettoProcedimento() %>
       </font>
    </tr>

    </table>

    <table cellspacing=2 cellpadding=2 width="100%">
    <tr>
      <td class="Titolo" colspan=6 > Oggetto </td>
    </tr>

<%
  Iterator lInd = tenori.iterator();
  while (lInd.hasNext())
  {
%>
   <tr>
      <%
        TenoreModel lTen = (TenoreModel) lInd.next();
      %>
      <td colspan=6 class="l"><%=lTen.getDescrOggettoTenore()%></td>
  </tr>
<%
  }
%>


    <tr>
      <td class="Titolo" colspan=6 > Difensori </td>
    </tr>
<%
  	Iterator itx = avvocato.iterator();

  	while ( itx.hasNext())
  	{
      	AvvocatoModel lAvv = (AvvocatoModel)itx.next();
%>
      	<tr style="width: 100%;">
          <td class=l><%=StringUtils.toStringJSP(lAvv.getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getNome(),"-")%></td>
          <td class=l><%=StringUtils.toStringJSP(lAvv.getForo(),"-")%></td>
          <td class=l><%=StringUtils.toStringJSP(lAvv.getIndirizzo(),"-")%></td>
          <td class=l><%=StringUtils.toStringJSP(lAvv.getDescrTipo(),"-")%></td>
        </tr>
<%
	  }
%>
    <tr>

    <%
    int count = 0;

    String lNote = new String();
    if( eventonotifica.getCampoNote() != null && eventonotifica.getCampoNote().length > 0 )
         lNote = eventonotifica.getCampoNote()[0].getDescr();
%>
    <tr>
     <td class="l">Note</td>
      <td class="L" colspan="5">
        <font class="campo"><%=StringUtils.toStringJSP( lNote )%></font>&nbsp;
      </td>
    </tr>
  </table>

  <jsp:include page="<%=ICostantiUdienza.PG_LOAD_DESTINATARI%>"/>
	
	<form name="dettaglio">
 		<jsp:include page="<%=ICostantiTemplate.PG_COMBO_TEMPLATE%>"/>
 	</form>
 
   <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post">
     <table>
          <jsp:include page="<%=ISIAPCostantiWeb.CAMPI_VALIDA_UPLOAD%>" />
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma">
            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"   value="<%= lEve.getEvento().getIdEvento()%>">
            <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_AZIONE_DETTAGLIO%>"  value="siap.sius.udienza.action.ActUploadFissazioneUdienza">
          	<input type="HIDDEN" name="FlagAvvocatura" value="<%=ICostantiAvvisiAvvocato.FISSAZIONE_UDIENZA%>">
          </td>
        </tr>

     </table>

    </FORM>
   </div>
   <br>
  </body>

</html>