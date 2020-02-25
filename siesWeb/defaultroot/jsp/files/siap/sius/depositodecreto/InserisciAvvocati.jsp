<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel" %>
<%@ page import="siap.sius.curatore.action.ICostantiCuratoreSius"%>
<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>

<jsp:useBean id="avvocato"         scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="tipoAutorita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"    scope="request" class="java.lang.String"/>
<jsp:useBean id="curatore"  			 scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>
<!-- <input name="<//%=ICostantiRichiestaAtti.CAMPO_NOTIFICHE_VIA_FAX %>" value=""  type="hidden" /> -->
  <table cellspacing="2" cellpadding="2" width="95%">

    <tr><td colspan=6>&nbsp;</td></tr>
<%
   String NomeForm = request.getParameter("NomeForm");

        Iterator itxAvv = avvocato.iterator();
        int num_sede = 0;
        while ( itxAvv.hasNext())
        {
         AvvocatoSiusModel lAvv = (AvvocatoSiusModel)itxAvv.next();
%>
    <tr style="width: 100%;" >
       <td class=l colspan=6>Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%></td>
    </tr>
    <tr>
      <td class="l">Autorità Destinazione</td>
      <td class="l">
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          <%= TipiIstituti1 %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Sede</td>
      <td class="l">
           <input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
              <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
    <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
    <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="300" size="35">
    </td>
    </td>
    </tr>
    <!-- 
    <tr>
        <td class="l">Notifica Via Fax: </td>
        <td class="L" colspan=3>
            <input title="Notifica Via Fax" name="<//%=ICostantiRichiestaAtti.CAMPO_NOTIFICA_VIA_FAX %>" value="1" type="checkbox"  />
        </td>
     </tr>
      -->

    <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocatoFascicoloSiusModel().getIdAvvocatoFascicoloSius()%>" type="hidden" >
    <input name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value="C" type="hidden" >

<%
      num_sede++;
     }
%>
    <tr><td colspan=6>&nbsp;</td></tr>
<%  if( curatore !=null && curatore.getCuratore()!=null && curatore.getCuratore().getCognome()!=null)
    {%>
       <td class="l">Per la notifica al <%=curatore.getDescrTipo().toLowerCase()%>&nbsp; <font class="campo"><%=StringUtils.toStringJSP(curatore.getCuratore().getCognome() +" "+curatore.getCuratore().getNome())%></font>&nbsp; </td>
    <tr>
      <td class="l">Autorità Destinazione</td>
      <td class="l">
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          <%= TipiIstituti1 %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Sede</td>
      <td class="l">
           <input Title="Sede Procura" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="<%=StringUtils.toStringJSP(curatore.getCuratore().getDescrUfficioAppartenenza(),"-")%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
              <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
    <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
    <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="300" size="35">
    </td>
    </td>
    </tr>
		<input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="C" type="hidden" >
    <input name="<%=ICostantiCuratore.CAMPO_ID_CURATORE%>" value="<%=curatore.getCuratore().getIdCuratore()%>" type="hidden" >
    <tr><td colspan=6>&nbsp;</td></tr>
<%  }%>

    <tr style="width: 100%;" >
      <td class=l colspan=6>Per la  <input type='radio' name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value='N' >Notifica /<input type='radio' name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value='C' checked>Comunicazione ad altro destinatario</td>
    </tr>

    <tr>
      <td class="l">Destinatario</td>
      <td class="l">
        <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
          <%= tipoAutorita %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Sede </td>
      <td class="l">
         <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
            value="" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaComuniUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%><% if (num_sede > 0) { %> [<%=num_sede%>]<%}%>',document.<%=NomeForm%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%><% if (num_sede > 0) { %> [<%=num_sede%>]<%}%>.value );">
            <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>
    <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
    <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="300" size="65">
    </td>
    </td>
    </tr>

    <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="" type="hidden" >

    </table>