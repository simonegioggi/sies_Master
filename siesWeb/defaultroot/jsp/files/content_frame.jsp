<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="java.math.BigDecimal" %>

<%
  UtenteModel lUteMod=(UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
  UfficioModel lUffMod= lUteMod.getUfficioUtente();
  String CodUff=new String(lUffMod.getCodTipoUfficio());
  String img = new String("");

  if (CodUff.equals("PGCAP") || 
  		CodUff.equals("PM") 	 || 
  		CodUff.equals("PMM") ) 
  {
%>
  <frameset  rows="9%,50px,*" border="0">
    <frame src="/html/blank.htm"               name="navigazione"    id="navigazione"    frameborder="0" scrolling="No"   noresize marginwidth="0" marginheight="0" style="border: 0px solid White;">
    <frame src="/jsp/files/fastAccessMenu.jsp" name="fastAccessMenu" id="fastAccessMenu" frameborder="0" scrolling="No"   noresize marginwidth="0" marginheight="0">
    <frame src="/html/blankGray.htm"           name="body"           id="body"           frameborder="0" scrolling="Auto" noresize marginwidth="0" marginheight="0">
  </frameset>
<%
  }else if (lUteMod.isUtenteSIGE()){
%>
  <frameset  rows="9%,50px,*" border="0">
    <frame src="/html/blank.htm"               name="navigazione"    id="navigazione"    frameborder="0" scrolling="No"   noresize marginwidth="0" marginheight="0" style="border: 0px solid White;">
    <frame src="/jsp/files/fastAccessMenu.jsp" name="fastAccessMenu" id="fastAccessMenu" frameborder="0" scrolling="No"   noresize marginwidth="0" marginheight="0">
    <frame src="/html/blankGray.htm"           name="body"           id="body"           frameborder="0" scrolling="Auto" noresize marginwidth="0" marginheight="0">
  </frameset>
<%
  }else {
%>
  <!--frameset  rows="9%,50px,*" border="0"-->
  <frameset  rows="11%,*" border="0">
    <frame src="/html/blank.htm"               name="navigazione"    id="navigazione"    frameborder="0" scrolling="No"   noresize marginwidth="0" marginheight="0" style="border: 0px solid White;">
    <!--frame src="/jsp/files/fastAccessMenuAltri.jsp" name="fastAccessMenu" id="fastAccessMenu" frameborder="0" scrolling="No"   noresize marginwidth="0" marginheight="0"-->
    <frame src="/html/blankGray.htm"           name="body"           id="body"           frameborder="0" scrolling="Auto" noresize marginwidth="0" marginheight="0">
  </frameset>
<%}%>
