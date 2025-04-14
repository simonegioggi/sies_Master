<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<jsp:useBean id="avvocato"         scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoAutorita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiIstituti1"    scope="request" class="java.lang.String"/>
<jsp:useBean id="notificheSog"     scope="request" class="java.lang.String"/>

<%
    String NomeForm = request.getParameter("NomeForm");
	Iterator itxAvv = avvocato.iterator();
	AvvocatoSigeModel lAvv = null;
	int num_sede = 0;
	while ( itxAvv.hasNext()){
      	lAvv = (AvvocatoSigeModel)itxAvv.next();
	}
	
    if(notificheSog != null && notificheSog.compareTo("NO") == 0)
    {

%>
<table cellspacing="2" cellpadding="2" width="95%">

    <tr>
      <td class="LBG" colspan=6>
        	Per la notifica al Soggetto Domiciliato presso il Difensore (ex art. 161 cpp)
      </td>
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
        <td class="l">Sede </td>
        <td class="l">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
           <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
              value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo(),"-")%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUNEP('<%=NomeForm%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[<%=num_sede%>]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
    </tr>

    <tr>
    <td class="l">Indirizzo</td>
    <td class="l">
	    <input name="nota_soggetto" value="" type="text" maxlength="300" size="65">
    </td>
    </td>
    </tr>
</table>
<%}%>