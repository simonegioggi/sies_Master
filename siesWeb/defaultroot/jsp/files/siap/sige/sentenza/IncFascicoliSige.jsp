<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.security.model.ProfileModel" %>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<jsp:useBean id="fascicoliUfficio" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoliAltriUffici" scope="request" class="java.util.Vector" />
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel"/>

<%
// Si ricava il profilo dell'utente connesso
	ProfileModel lProfilo =(ProfileModel) UtenteConnesso.getUserProfile();
	if( lProfilo.isSige() &&  fascicoli.size() > 0) {
%>

 <table cellspacing=2 cellpadding=2>
<tr> <td class ="Titolo" colspan =7>Selezionare i procedimenti ai quali associare la modifica</td></tr>
<tr><td>
<input type="HIDDEN" name="<%=ICostantiFasSigeSentenza.NUM_FASCICOLI_SIGE%>" value="<%=fascicoli.size()%>">
</td></tr>
  </table>
<%if(fascicoliUfficio.size() > 0){  %>


 <table cellspacing=2 cellpadding=2>
<tr> <td class ="Titolo" colspan =7>Procedimenti SIGE modificabili</td></tr>
 <tr>
<table cellspacing=2 cellpadding=2>	
 <jsp:include page="/jsp/files/siap/sico/storicosoggetto/IncFasSigeUff.jsp"/>
</table>
</tr>
</table>
<%  } if(fascicoliAltriUffici.size() > 0){   %>
<table cellspacing=2 cellpadding=2>	
<tr> <td class ="Titolo" colspan =7>Procedimenti SIGE non modificabili</td></tr>
 <tr>
	<table cellspacing=2 cellpadding=2>	
   	 <jsp:include page="/jsp/files/siap/sico/storicosoggetto/IncFasSigeAltriUff.jsp"/>
    </table>
  </tr>
   </table>  
<%  } 
%>
  <br>
<% } %>