<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<jsp:useBean id="notificheSog"     scope="request" class="java.lang.String"/>
<jsp:useBean id="luogodet"         scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="tipoAutorita"     scope="request" class="java.lang.String"/>


<%
   String NomeForm = request.getParameter("NomeForm");

    if(notificheSog != null && notificheSog.compareTo("NO") == 0)
    {

%>
<table cellspacing="2" cellpadding="2" width="95%">

    <tr>
      <td colspan=3 class="Titolo" colspan=2>Per la notifica</td>
    </tr>

    <tr>
      <td class="l" colspan=6>
        Per la notifica al Soggetto
      </td>
    </tr>

<%  if (luogodet.getIdLuogoDetenzione() == null  || luogodet.getDataFineDetenzione() != null || luogodet.getIstitutoDetenzione() == null ) {

%>
    <tr>
        <td class="l">Autorità Destinazione</td>
        <td class="l">
          <select title="Destinatario" name="<%=ICostantiUdienza.CAMPO_COD_IST_DETENZIONE%>">
            <%= tipoAutorita %>
          </select>
        </td>
    </tr>
    <tr>
        <td class="l">Sede </td>
        <td class="l">
           <input Title="Sede " name="<%=ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>"
              value="" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaComuniUNEP('<%=NomeForm%>','<%=ICostantiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>', document.<%=NomeForm%>.<%=ICostantiUdienza.CAMPO_COD_IST_DETENZIONE%>.value );">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
    </tr>
    <%}else{
%>
    <tr>
        <td class="l">Tipo Istituto</td>
        <td class="l">
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(luogodet.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogodet.getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogodet.getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('<%=NomeForm%>','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        <img src="/images/filefolder.gif" border=0></a></td>
    </tr>
    <%}%>
    <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
    <input name="nota_soggetto" value="" type="text" maxlength="300" size="65">
    </td>
    </td>
    </tr>
</table>
<% }%>