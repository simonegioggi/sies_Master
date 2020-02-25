<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.rinnovo.action.ICostantiRinnovo" %>
<%@ page import="siap.siep.rinnovo.model.RinnovoModel" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="rinnovi" scope="request" class="java.util.Vector"  />
<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel"  />

<html>
<head>
<title>[S.I.E.S.] - Richiesta Informazioni 8 Bis </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
<script language="JavaScript">

function Verify()
{
   document.comandi.idPrimoRinnovo.value=document.LoadRich8Bis.idRinnovoUno.value;
   if(document.LoadRich8Bis.numRinn.value==2)
   {
     document.comandi.idSecondoRinnovo.value=document.LoadRich8Bis.idRinnovoDue.value;

    }
   return true;
}
</script>
</head>
<body class="corpo" onload="return Verify()">
<form name="LoadRich8Bis" method="POST" action="/jsp/Main.jsp">
<table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Richiesta Informazioni Comma 8 Bis</font>
      </td>
    <input type="hidden" name="numRinn" value="<%=rinnovi.size()%>">

<%RinnovoModel rinnovoUno = (RinnovoModel)rinnovi.get(0);

   RinnovoModel rinnovoDue = null;
   if(rinnovi.size()==2)
   {

     rinnovoDue = (RinnovoModel)rinnovi.get(1);
     %>
      <input type="hidden" name ="idRinnovoDue" value="<%=rinnovoDue.getIdRinnovo()%>">

    <%

   }
%>

<%if (((rinnovoUno.getFlagDocumentoRegistrato()!=null && rinnovoUno.getFlagDocumentoRegistrato().compareTo("N")==0)
       || rinnovoUno.getFlagDocumentoRegistrato()==null ) || (rinnovoDue!=null && (( rinnovoDue.getFlagDocumentoRegistrato()!=null
         && rinnovoDue.getFlagDocumentoRegistrato().compareTo("N")==0)
       || rinnovoDue.getFlagDocumentoRegistrato()==null )))
     {%>
     <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.notifica.action.ActStampaRich8Bis&idrinnovo=<%=rinnovoUno.getIdRinnovo()%>" onclick="javascript:lookUpload();">
           <img align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
        </a>
      </td--%>
<!-- BOTTONE DI STAMPA -->
   <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_STAMPA_SIEP%>">
     <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action=siap.siep.notifica.action.ActStampaRich8Bis&idrinnovo="+rinnovoUno.getIdRinnovo()%>"/>
   </jsp:include>
     <%}%>


     </tr>
</table>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<table width="100%">
     <tr>
      <td class="l"  width="35%">Decreto Di Sospensione Emesso in Data</td>
      <td class="l">
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd-MM-yyyy"))%> </font>
      </td>
		</tr>
</table >

<table width="100%">
<%
   List lAvvSiep = Arrays.asList( eventonotifica.getAvvocati());
  AvvocatoSiepModel lAvv= (AvvocatoSiepModel) lAvvSiep.get(0);
  NotificaModel lNotModel = (NotificaModel)eventonotifica.getNotifiche()[0];
%>
    <input type="hidden" name ="idRinnovoUno" value="<%=rinnovoUno.getIdRinnovo()%>">

<%if(rinnovoUno.getCodTipoRinnovo().equals("D"))
{%>

    <input type="hidden" name ="idPrimaNotifica" value="<%=lNotModel.getIdNotifica()%>">
    <%if(lAvv != null && lAvv.getAvvocato() != null)
    {%>
     <tr>
      <td class="l" width=25%>Avvocato</td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="l">
         <font class="campo"><%=lAvv.getAvvocato().getCognome()%>&nbsp;<%=lAvv.getAvvocato().getNome()%></font>
      &nbsp; Foro di &nbsp;<font class="campo"><%=lAvv.getAvvocato().getForo()%></font>
     </td>
		</tr>
    <tr>
      <td class="l">Tipo Difensore</td>
      <td class="l">
         <font class="campo"><%=lAvv.getAvvocato().getDescrTipo()%></font>
    </td>
		</tr>
   <%}%>
    <input type="hidden" name ="primoAvvocato" value="">

    <tr>
       <td class="l" >Data Richiesta</td>
       <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(rinnovoUno.getDataRinnovo(),"dd-MM-yyyy"))%></font>
       </td>
    </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
         <font class="campo"><%=StringUtils.toStringJSP(rinnovoUno.getDescrTipoAutoritaRinnovo())%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(rinnovoUno.getDescrLuogoRinnovo())%></font></td>
      </tr>
      <tr>
         <td class="l">Indirizzo</td>
         <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(rinnovoUno.getNote())%>&nbsp;</font>
         </td>
     </tr>
</table>

<table width="100%">
<%if(lAvvSiep.size()>1){

  AvvocatoSiepModel lAvvSecondo= (AvvocatoSiepModel)lAvvSiep.get(1);
  NotificaModel lNotMod =(NotificaModel)eventonotifica.getNotifiche()[1];
%>
    <input type="hidden" name ="idSecondaNotifica" value="<%=lNotMod.getIdNotifica()%>">
    <input type="hidden" name ="secondoAvvocato" value="">
    <%if(lAvvSecondo != null && lAvvSecondo.getAvvocato() != null)
    {%>
     <tr>
      <td class="l" width=25%>Avvocato</td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="l">
          <font class="campo"><%=lAvvSecondo.getAvvocato().getCognome()%>&nbsp;<%=lAvvSecondo.getAvvocato().getNome()%> </font>
       &nbsp; Foro di &nbsp;<font class="campo"><%=lAvvSecondo.getAvvocato().getForo()%></font>
      </td>
	   </tr>
     <tr>
      <td class="l">Tipo Difensore</td>
      <td class="l">
        <font class="campo"> <%=lAvvSecondo.getAvvocato().getDescrTipo()%></font>
      </td>
		 </tr>
  <%}%>
<%if(rinnovi.size()==2)
{%>

  <tr>
       <td class="l" >Data Richiesta</td>
       <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(rinnovoDue.getDataRinnovo(),"dd-MM-yyyy"))%></font>
       </td>
    </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
         <font class="campo"><%=StringUtils.toStringJSP(rinnovoDue.getDescrTipoAutoritaRinnovo())%></font>
         di <font class="campo"><%=StringUtils.toStringJSP(rinnovoDue.getDescrLuogoRinnovo())%></font></td>
      </tr>
      <tr>
         <td class="l">Indirizzo</td>
         <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(rinnovoDue.getNote())%>&nbsp;</font>
         </td>
     </tr>
     <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--input type="hidden" name="idnotifica" value="<%=notifica.getIdNotifica()%>">
    <input type="hidden" name="idevento" value="<%=evento.getIdEvento()%>"--%>
<%}}}%>
</table>
<%if(rinnovoUno.getCodTipoRinnovo().equals("I"))
{%>
<table width="100%">
<tr>
     <tr>
       <td class="l" >Data Richiesta</td>
       <td class="l">
         <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(rinnovoUno.getDataRinnovo(),"dd-MM-yyyy"))%></font>
       </td>
    </tr>
      <tr>
        <td class="l">Autorità di polizia delegata</td>
        <td class="l" colspan="3">
         <font class="campo"><%=StringUtils.toStringJSP(rinnovoUno.getDescrTipoAutoritaRinnovo())%></font>
        di <font class="campo"><%=StringUtils.toStringJSP(rinnovoUno.getDescrLuogoRinnovo())%></font></td>
      </tr>
      <tr>
         <td class="l">Indirizzo</td>
         <td class="L">
            <font class="campo"><%=StringUtils.toStringJSP(rinnovoUno.getNote())%>&nbsp;</font>
         </td>
     </tr>

</TABLE>


<%}%>
</form>
 <br>
  <div align=left style="visibility:hidden" id="upld">
    <FORM name="comandi" enctype="multipart/form-data" method="post" onSubmit="return controllaUpload();">
      <table>
        <tr>
          <td class="L">Valida Documento</td>
          <td class="L">
            <input type=checkbox name="<%=ICostantiRinnovo.CAMPO_VALIDA%>" value=1>
          </td>
        </tr>
        <tr>
          <td class="l" rowspan=2>Richiesta Certificato da Salvare</td>
          <td class="L">
            <font class="campo">
            <input type=file size="35" name="<%=ICostantiRinnovo.CAMPO_DOC_BLOB%>"></font>
          </td>
        </tr>
        <tr>
          <td class="L">
            <input  class=bottone  type="submit" value="Conferma" >
            <input type="HIDDEN" name="idPrimoRinnovo" value="">
            <input type="HIDDEN" name="idSecondoRinnovo" value="">

            <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.notifica.action.ActUploadRich8Bis">
            <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_AZIONE_DETTAGLIO%>" value="siap.siep.notifica.action.ActLoadDettaglioRich8Bis">
          </td>
        </tr>
      </table>
</form>
</div>
</body>
</html>