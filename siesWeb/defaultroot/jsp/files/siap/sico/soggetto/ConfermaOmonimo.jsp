<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Stack"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>

<jsp:useBean id="StackDiRitorno" scope="session" class="java.util.Stack"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

  <!-- Viene riadattato il bottone di ritorno -->
<%
  if (StackDiRitorno != null)
  {
    if (!StackDiRitorno.isEmpty())
    {
       String lRet = "";
        // Lo Stack non è vuoto
       if( TornaQui != null)
       {
        // C'è Link di Ritorno di tipo 2
           lRet = (String) StackDiRitorno.peek();
           lRet  += "&" + IWebConstants.FLAG_RITORNO + "=1";
       }
        if (lRet.length() > 1)
        {
           lRet  += "&" + ICostantiSoggetto.FLAG_OMONIMI + "=si";
%>
        <td class="LBG">
          <a href="<%=lRet%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>inserisci1.gif" alt="conferma" border="0">
          </a>
        </td>
<%
        }
    }
  }
%>

