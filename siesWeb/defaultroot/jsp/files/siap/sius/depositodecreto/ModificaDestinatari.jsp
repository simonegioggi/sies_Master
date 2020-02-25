<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<jsp:useBean id="notifiche" scope="request" class="java.util.Vector"/>
<jsp:useBean id="luogodet"  scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="curatore"  scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>

 <table cellspacing=2 cellpadding=2  width="100%">
  <tr>
    <td colspan=3 class="Titolo" colspan=2> Destinatari </td>
  </tr>
<%
  String NomeForm = request.getParameter("NomeForm");
  Iterator itx2 = notifiche.iterator();
  int i=-1;
  while ( itx2.hasNext())
  {
    i++;
    NotificaModel notifica = (NotificaModel)itx2.next();

    if(i == 0){
%>
    <tr>
      <td class="l">Data Trasmissione atti</td>
      <td class="L">
       <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataInvio(),"dd"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
       <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataInvio(),"MM"))%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>"    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
       <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataInvio(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>

<%
    }
%>
    <input name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" value="<%=notifica.getIdNotifica()%>" type="hidden" >
    <tr>
<%

      if(notifica.getSogIdSoggetto() !=null) // SOGGETTO
      {
%>
          <td class="l" colspan="3"> Per la notifica al Soggetto </td>
<%
      } else if(notifica.getAvvIdAvvocatoFascicoloSiep()!=null) // Avvocato SIEP
      {
        if( notifica.getAvvSiep() !=null)
        {
%>
          <td class="l" colspan="3"> Per la notifica all' Avv. <%=notifica.getAvvSiep().getAvvocato().getDescrTipo()%>&nbsp;:&nbsp;<font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSiep().getAvvocato().getCognome() +" "+notifica.getAvvSiep().getAvvocato().getNome())%></font>&nbsp; </td>
<%      }
      } else if(notifica.getAvvIdAvvocatoFascicoloSius()!=null) // Avvocato SIUS
      {
        if( notifica.getAvvSius() !=null)
        {
%>
          <td class="l" colspan="3"> Per la notifica all' Avv. <font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSius().getAvvocato().getCognome() +" "+notifica.getAvvSius().getAvvocato().getNome())%></font>&nbsp; </td>
<%      }
      } else if(notifica.getCurIdCuratore()!=null) // Curatore SIUS
      {
        if( curatore.getCuratore()!=null)
        {
%>
          <td class="l" colspan="3">Per la notifica al <%=curatore.getDescrTipo().toLowerCase()%>&nbsp; <font class="campo"><%=StringUtils.toStringJSP(curatore.getCuratore().getCognome() +" "+curatore.getCuratore().getNome())%></font>&nbsp; </td>
<%      }
      }
%>
    </tr>
    <tr>
<%


      if(notifica.getUfficio()!= null) // UFFICIO
      {
%>
         <td class="l" colspan="1" ><font class="campo"><%=notifica.getUfficio().getDescrTipoUfficio()%></font>
         <td class="campo" nowrap>
           <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            value="<%=notifica.getUfficio().getDescrComune()%>" type="text" maxlength="35" size="35">
<%
          if (notifica.getUfficio().getCodTipoUfficio().equals("PGCAP"))
          {
%>
             <a href="Javascript:ListaProcure('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
             <img src="/images/filefolder.gif" border=0> </a>
<%
          } else if (notifica.getUfficio().getCodTipoUfficio().equals("UDS"))
          {
%>
            <a href="Javascript:ListaUDS('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
            <img src="/images/filefolder.gif" border=0> </a>
<%
          } else if (notifica.getUfficio().getCodTipoUfficio().equals("TDS"))
          {
%>
             <a href="Javascript:ListaTDS('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
             <img src="/images/filefolder.gif" border=0> </a>
<%
          }	// 11/05/2010 Modifica destinatario UEPE
          	else if (notifica.getUfficio().getCodTipoUfficio().indexOf("UEPE")>=0)
          {
%>
             <a href="Javascript:ListaCSSA('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
             <img src="/images/filefolder.gif" border=0> </a>
<%
          } else // if (notifica.getUfficio().getCodTipoUfficio().equals("PM"))
          {
%>
             <a href="Javascript:ListaUffici('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
             <img src="/images/filefolder.gif" border=0> </a>
<%
          }
%>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input name="note_<%=notifica.getIdNotifica().toString()%>" value="<%=StringUtils.toStringJSP( notifica.getNote())%>" type="text" maxlength="35" size="35" readonly>
          </td>
<%
      } else if ( notifica.getAutoritaEsterna() != null ) // Autorita' Esterna
        {
%>
          <td class="l">
            <font class="campo" ><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%></font>
          <td class="campo" nowrap>
            <input Title="Sede del Comune di" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            value="<%=notifica.getAutoritaEsterna().getDescrSede()%>" type="text" maxlength="35" size="35">
<%
          if (notifica.getAutoritaEsterna().getCodTipoAutorita().equals("22"))
          {

%>
              <a href="Javascript:ListaUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
            <img src="/images/filefolder.gif" border=0> </a>
<%
          } else {
%>
            <a href="Javascript:ListaComuni('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
            <img src="/images/filefolder.gif" border=0> </a>
<%
          }
%>
<%
%>          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input name="note_<%=notifica.getIdNotifica().toString()%>" value="<%=StringUtils.toStringJSP( notifica.getNote())%>" type="text" maxlength="300" size="35">
          </td>
<%

      } else if(notifica.getIstitutoDetenzione() != null) // Autorità Esterna
        {
%>
          <td class="l">
            <font class="l">Tipo Istituto </font>
          <td class="campo" nowrap>
            <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(notifica.getIstitutoDetenzione().getDescrComune())%>" size=50>
            <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogodet.getIstDetIdIstitutoDetenzione()%>" size=50>
            <a href="Javascript:ListaIstitutoDetenzione('<%=NomeForm%>','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
            <img src="/images/filefolder.gif" border=0></a>
            <input Title="PLUS" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" type="hidden">
          </td>

<%    } else if(notifica.getCssIdCssa()!=null) // UEPE
        {
        if( notifica.getCSSA() !=null)
        {
%>
          <td class="l" colspan="1"><font class="campo"><%="UEPE"%> </font></td>
          <td class="campo" nowrap>
            <input Title="Sede UEPE" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            value="<%=notifica.getCSSA().getComune()%>" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaCSSA('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=i%>]');">
            <img src="/images/filefolder.gif" border=0> </a>
          </td>
<%      }
       }
%>
       <td class=l nowrap>
       <input type='hidden' name='lCheck' value='0'>
       <input type='checkbox' name='lCheck' value='<%=notifica.getIdNotifica()%>'> Cancella
       </td>
    </tr>

<%}%>
 </table>
