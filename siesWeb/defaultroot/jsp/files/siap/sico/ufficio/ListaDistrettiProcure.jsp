<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<jsp:useBean id="ListaDistretti" scope="request" class="java.util.Vector" />
<jsp:useBean id="CodiceDistrettoUtente" scope="request" class="java.lang.String" />

<%
String actionRicerca = "siap.sico.ufficio.action.ActLoadUfficiDistretto";
if (request.getParameter("minor") != null)  {
	actionRicerca = "siap.sico.ufficio.action.ActLoadUfficiMinorDistretto";
} else if (request.getParameter("unep") != null)  {
	actionRicerca = "siap.sico.ufficio.action.ActLoadUNEPdistretto";
}
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Distretti</title>
  </head>

  <body class="corpo" onload="javascript:focus();document.f.submit();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target="listauffici">
    <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname" value="<%=request.getParameter("fieldname")%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=actionRicerca%>">
   
    <br>

    <table>
      <tr>
        <td class=LBG valign="middle"><font class="campo">Seleziona Un Distretto</font></td>
        <td valign="middle">
        <select name="distretto">
<%

          Iterator itx = ListaDistretti.iterator();
          while ( itx.hasNext())
          {
            UfficioModel distretto = (UfficioModel)itx.next();
            if (!(distretto.getCodDistretto().equals("-")))
            {
%>
              <option
<%
              if (CodiceDistrettoUtente.equalsIgnoreCase (""+ distretto.getCodDistretto()))
              {
%>
                SELECTED
<%
              }
%>
              value="<%=distretto.getCodDistretto()%>"><%= distretto.getDescrComune() %></option>
<%
            }
          }
%>
        </select>
        </td>
        <td valign="middle"><input type="submit" name="go" value="Seleziona >>"></td>
      </tr>
    </table>
  </form>
  </body>
</html>