<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="f3b.web.*" isErrorPage="true" %>

<%@ page import="f3b.util.F3BException" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="java.rmi.RemoteException" %>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>

    <!--<BODY class="fondo2" background="immagini/immagine_home.jpg" style="background-repeat : no-repeat;">
   -->
  <body class="corpo">
   <p>&nbsp;
    <p>&nbsp;
    <p>
      <CENTER><font color="red">PAGINA DI ERRORE</font></CENTER>
      <BR>
      <BR>
      <STRONG>
<%
      if (exception instanceof RemoteException)
      {
        RemoteException rex=(RemoteException) exception;
%>
        <CENTER>
          RemoteException:<BR>
          Errore di comunicazione con l'application Server:<BR><BR>
        </CENTER>
        <%="" + rex%>
<%
  }
    else
  {
    if (exception instanceof F3BException)
    {
      F3BException ex = (F3BException) exception;

      //if (ex.getErrorCode()!=F3BException.USER_MESSAGE)
      if (   ex.getMessage().indexOf("Nessun")<0
          && ex.getMessage().indexOf("nessun")<0
          && ex.getMessage().indexOf("Password")<0
         )
      { // loggo lo stacktrace solo se non si tratta di messaggio utente 
        // es "nessun elemento trovato".
        // n.b. le eccezione con error code=F3BException.USER_MESSAGE sono
        //      state ampliamente utilizzate anche per rilanciare eccezioni
        //      vere e proprie (daoException) per cui non si può mettere il
        //      filtro sul codice di errore.
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.error(exception.getMessage(),exception);
      }

      if ( ex.getErrorCode()<0 )
      {
%>
        F3BException --> Eccezione di sistema:<BR>
        <%= exception.getMessage() %>
<%
        //exception.printStackTrace();
      }
      // Da verificare .....
      else if(ex.getErrorCode() == 4)
      {
      	request.setAttribute(IWebConstants.MESSAGE_TEXT, ex.getMessage());
      	request.setAttribute("Modalita","I");%>
      	<jsp:forward page="/jsp/files/warning.jsp" />
<%
      }
      // Da verificare .....
      else if(ex.getErrorCode() == 5)
      {
      	request.setAttribute(IWebConstants.MESSAGE_TEXT, ex.getMessage());
      	request.setAttribute("Modalita","M");%>
      	<jsp:forward page="/jsp/files/warning.jsp" />
<%
      }
      else
      {
        //======================================================================
        request.setAttribute(IWebConstants.MESSAGE_TEXT, ex.getMessage());
%>
        <jsp:useBean id="action" class="java.lang.String" scope="request"/>
<%
        if( action != null && action.trim().length() > 0  )
        {
          RedirectTo redirect = new RedirectTo();
          redirect.setPage( IWebConstants.PG_MAIN );
          redirect.setAction( action.trim() );
          //redirigi.setParametro( "error_flag", "1" );
          request.setAttribute( IWebConstants.GOTO_PAGE, "" + redirect);
        }
%>
        <jsp:forward page="<%=IWebConstants.PG_MESSAGE%>"/>
<%
      }
    }
    else
    { //Eccezione Generica
%>
      Exception di altro tipo:<BR> <%= ""  + exception %><BR>

<%    

      //exception.printStackTrace();
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error(exception.getMessage(),exception);


    }
  }
%>
</STRONG>

</BODY>
</HTML>