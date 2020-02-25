<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>

<jsp:useBean id="avvocato"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoAutorita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"    scope="request" class="java.lang.String"/>

  <table cellspacing="2" cellpadding="2" width="95%">
<%
   String NomeForm = request.getParameter("NomeForm");

        Iterator itxAvv = avvocato.iterator();
        int num_sede = 0;
        while ( itxAvv.hasNext())
        {
         AvvocatoSigeModel lAvv = (AvvocatoSigeModel)itxAvv.next();
%>
    <tr>
       <td class='LBG' colspan=6>Per la notifica all' avvocato <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome(),"-") + " " + StringUtils.toStringJSP(lAvv.getAvvocato().getNome(),"-")%>  Foro di <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo(),"-")%> Difensore <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo(),"-")%></td>
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
    </tr>

    <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="<%=lAvv.getAvvocatoFascicoloSigeModel().getIdAvvocatoFascicoloSige()%>" type="hidden" >
    <input name="<%=ICostantiRichiestaAtti.CODTIPONOTIFICA%>" value="C" type="hidden" >
<%
      num_sede++;
     }
%>
    <tr><td colspan=6>&nbsp;</td></tr>

    <tr>
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
    </tr>

    <input name="<%=ICostantiUdienza.CAMPO_COD_AVVOCATO%>" value="" type="hidden" >

    </table>