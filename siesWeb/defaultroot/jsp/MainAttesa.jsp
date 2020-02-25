<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>

<%@ page import="f3b.util.F3BException" %>
<%@ page import="f3b.web.Action" %>
<%@ page import="f3b.web.util.*" %>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>

<%@ page language="java" session="true" errorPage="ErrorPage.jsp" %>

<jsp:useBean id="action" class="f3b.web.Action" scope="session" >
<%
  action.setServletContext(application);
%>
</jsp:useBean>

<%
  /**
   * Controllo sull'attivazione dei Cookies.
   * Il controllo viene fatto solo dala seconda volta in poi.
   * Per il momento da commentare !!!!!
   */
  if ( (request.getCookies() == null) || (request.getCookies().length == 0) )
  //if ( !request.isRequestedSessionIdFromCookie() )
  {
    session.invalidate();
    request.setAttribute("LinkTo", IWebConstants.PG_LOGIN);
    request.setAttribute("Messagge", "Attivare i Cookies sul browser per utilizzare l'applicazione.");
%>
    <jsp:forward page="<%=IWebConstants.PG_SEND_TO%>"/>
<%
  }

  String lStrAction = null;

  if( MultipartContent.isMultipartContent( request ) )
  {
    try
    {
      MultipartContent lMC = new MultipartContent( request ) ;
      lMC.parseRequest();

      if ( lMC.getParameter(IWebConstants.ACTION_FIELD) != null )
        lStrAction = (String)lMC.getParameter(IWebConstants.ACTION_FIELD);

      request.setAttribute( "MultipartContent", lMC );
    }
    catch( Exception ex)
    {
      throw ex;
    }
  }
  else
  {
    if ( request.getParameter(IWebConstants.ACTION_FIELD) != null )
      lStrAction = (String)request.getParameter(IWebConstants.ACTION_FIELD);
  }

  if (lStrAction == null)
    throw new Exception( "Parametro Action mancante ! " );


/*
  String lStrAction = null;

  if ( request.getParameter("Azione") != null )
    lStrAction = (String)request.getParameter("Azione");

  if ( request.getParameter("Action") != null )
    lStrAction = (String)request.getParameter("Action");

  if (lStrAction == null)
    throw new Exception( "Parametro Azione mancante ! " );
*/

  //Controllo sull'avvenuto accesso attraverso il login
  if( (session.getAttribute( ICostantiSecurity.SESSION_UTENTE_CONNESSO ) == null) &&
      (!lStrAction.equals("siap.sico.security.action.ActLogin")) )
  {
    // logout []
    session.invalidate();
    request.setAttribute("LinkTo", IWebConstants.PG_LOGIN);
    request.setAttribute("Messagge",	"Sessione utente terminata, effettuare di nuovo il login...");
%>
    <jsp:forward page="<%=IWebConstants.PG_SEND_TO%>"/>
<%
  }
%>

<html>
  <head>
  <script language="JavaScript">
    function ciao()
    {
      var node=document.getElementById('ciao');
      node.style.visibility='hidden';
    }
  </script>
  </head>
  <body onLoad="javascript:ciao()">
    <div align=center id="ciao" style="visibility:visible;position:absolute;top:100px;left:100px">
      <table bgcolor="#EEEEEE">
        <tr>
          <td>
            <img src="/images/rotelle3.gif">
          </td>
          <td>
            <font size=+1 color=navy>
              Attendere... Caricamento in corso.
            </font>
          </td>
        </tr>
      </table>
    </div>
  </body>
 </html>
<%
  out.flush();
  String lPage = null;

  Action actionObj = action.get( lStrAction );
  actionObj.setReqSes(request, session);

  // Per lActLogin e per l'ActLoadOrizontalMenu
  // non vengono eseguiti controlli di abilitazione
  // sul profilo dell'utente perchè
  // sono operazioni abilitate per qualsiasi utente
  if( (!lStrAction.equals("siap.sico.security.action.ActLogin")) && // ogni utente puo effettuare il login
      (!lStrAction.equals("siap.sico.security.action.ActLoadOrizontalMenu")) ) // supposto che il menu abbia almeno due livelli
  {
    actionObj.setFunctionsAvailableToRequest(lStrAction);
  }

  lPage = actionObj.processRequest(); // ritorna la pagina responsabile della VIEW

  response.setHeader("expires", "0");
%>
<jsp:include page="<%=lPage%>" />