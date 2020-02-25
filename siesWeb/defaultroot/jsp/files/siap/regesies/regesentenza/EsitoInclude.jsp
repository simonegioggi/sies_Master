<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.notiziareato.model.NotiziaReatoModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel" %>
<%@ page import="siap.regesies.regesentenza.model.ProvvedimentoModel"%>
<%@ page import="siap.regesies.regesentenza.model.ProvvedimentoSiepModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="esito" scope="request" class="siap.regesies.regesentenza.model.EsitoImportModel"/>
<%
BigDecimal KeyFascicolo    = (BigDecimal) request.getAttribute("KeyFascicolo");
%>

<html>
<%String ESITO_POSITIVO = siap.regesies.regesentenza.action.ICostantiRegeSentenza.ESITO_POSITIVO;
  ProvvedimentoModel lProvvedimento = esito.getProvvedimento();
  ProvvedimentoSiepModel lProvv = esito.getProvvedimentoSiep();
  SentenzaModel sentenza =  esito.getProvvedimentoSiep().getSentenza();
  String finale = "";%>

   <table width="100%">
 <% if (lProvvedimento.isResidenzeCheck())
  {%><tr>
      <td class="LBGISI" width="8%">Residenza</td><td><table width="100%">
    <%
    Vector residenza = lProvv.getResidenze();
    if (residenza != null && residenza.size()>0)
    {
    Iterator itx = residenza.iterator();
    int lInd = 0;
    while ( itx.hasNext())
      {
        if(esito.getEsitoResidenze().get(lInd).equals(ESITO_POSITIVO))
          {
           ResidenzaModel lRes = (ResidenzaModel)itx.next();
     %>
     <tr>
       <td class="L">Inserita correttamente <font class="label"><%=lRes.getDescrTipoResidenza()%></font>
       <font class="campo"><%=lRes.toStringaResidenza()%></font>
      </td>
    </tr><%
          }
          else
          {%>
        <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoResidenze().get(lInd))%></font>&nbsp;
         </td>
      <%}
          lInd++;
      }%></table></td></tr>
    <%}
  }
 if (lProvvedimento.isReatoCheck())
   {%>
   <tr>
      <td class="LBGISI"  width="8%">Reati</td><td>
    <%
    Vector reati = lProvv.getReati();
    if(reati!=null && reati.size()>0)
    {
     Iterator lIterReati = reati.iterator();
     int lCont = 0;
     %><table width="100%"><%
     while(lIterReati.hasNext())
      {%><tr><%
        ReatoCircostanzaModel lRea = (ReatoCircostanzaModel)lIterReati.next();
        if (esito.getEsitoReati()!=null && esito.getEsitoReati().get(lCont)!=null &&
       esito.getEsitoReati().get(lCont).equals(ESITO_POSITIVO))
         {%> <td class="l">
         Inserito correttamente <%=lRea.getReato().toStringReato()%></td>
         </td><%}
         else
         {%>
         <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoReati().get(lCont))%></font>&nbsp;
         </td><%}
      lCont++;
  %></tr><%}%></table>
  <%}%>
    </td></tr>
  <%}
  if (lProvvedimento.isCircostanzaCheck())
   {%>
    <tr>
    <td  class="LBGISI" width="8%">Circostanze</td><td>
      <%
      Vector lCircostanze = lProvv.getCircostanze();
      int lCont = 0;
      if(lCircostanze != null && lCircostanze.size() != 0)
      { %><table width="100%"><tr><%
        Iterator lIterCircostanze = lCircostanze.iterator();
        while(lIterCircostanze.hasNext())
        {
          CircostanzaModel lCircostanza = (CircostanzaModel)lIterCircostanze.next();

          if(esito.getEsitoCircostanze().get(lCont).equals(ESITO_POSITIVO))
          {%>
          <td class="L">Inserita correttamente <font class="campo"><%=lCircostanza.toStringCircostanza()%></font></td><%
          }
          else
          {%>
           <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoCircostanze().get(lCont))%>&nbsp;
        <%}%>
        </td></tr>
       <%lCont++;
        }%></table>
        </td>
    </tr>
    <%}

   }
   if (lProvvedimento.isNotiziaReatoCheck())
   {%>
    <tr>
      <td class="LBGISI" width="8%">Notizie di Reato</td><td>
        <%
      Vector lNotizie = lProvv.getNotizieDiReato();
      int lCont = 0;
      if(lNotizie != null && lNotizie.size() != 0)
      {%><table width="100%">
      <%
        Iterator lIterNotizie = lNotizie.iterator();
        while(lIterNotizie.hasNext())
        {%>
        <tr><td class="L">
        <%
        NotiziaReatoModel lNotizia = (NotiziaReatoModel)lIterNotizie.next();
        if(esito.getEsitoNotizieDiReato().get(lCont).equals(ESITO_POSITIVO))
          {%>
          Inserita correttamente <font class="campo"> <%=lNotizia.toStringNotizia()%> </font></td>
          <% }
          else
          {%>
        <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoNotizieDiReato().get(lCont))%></font>&nbsp;
         </td>
      <%}%>
  </tr>
  <%lCont++;
  }%></table>
 </td> </tr>
    <%}%>
  <%}
    if (lProvvedimento.isDifensoriCheck())
   {
  %>
    <tr>
      <td class="LBGISI">Difensori</td><td><table width="100%">
       <%
       if(esito.getEsitoDifensori().equals(ESITO_POSITIVO))
    {%><tr>
      <td class="L" colspan=5>
        Inserito correttamente <%=StringUtils.toStringJSP(lProvv.getNoteFascicoloModel().getNotaAvvocati())%></font>&nbsp;
      </td>
  </tr><%}else
          {%><tr>
        <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoDifensori())%></font>&nbsp;
         </td></tr>
      <%}%></table></td></tr>
  <% }
  if (lProvvedimento.isDispositivoCheck())
   {
  %>
    <tr>
      <td class="LBGISI">Dispositivo</td><td><table width="100%">
       <%
       if(esito.getEsitoDispositivo().equals(ESITO_POSITIVO))
    {%><tr>
      <td class="L" colspan=5>
        Inserito correttamente  <font class="campo"><%=StringUtils.toStringJSP(lProvv.getNoteFascicoloModel().getNotaDispositivo())%></font>&nbsp;
      </td>
  </tr><%}else
          {%><tr>
        <td class="lRosso">ERRORE <%=StringUtils.toStringJSP(esito.getEsitoDispositivo())%></font>&nbsp;
         </td></tr>
      <%}%></table></td></tr>
  <%}%>


  </table>

</html>