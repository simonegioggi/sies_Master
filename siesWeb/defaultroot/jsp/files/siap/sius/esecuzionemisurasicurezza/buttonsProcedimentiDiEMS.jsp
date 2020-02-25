<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
      String idFascicoloSiep = request.getParameter("idFascicoloSiep");
      //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();
        FunctionModel lFun = null;
        while(lIterBottoni.hasNext())
        {
          lFun = (FunctionModel)lIterBottoni.next();

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&hIdSoggetto=<%=request.getParameter("idSoggetto")%>&IdFascicoloSIEP=<%=request.getParameter("idFascicoloSiep")%>&TornaQui=<%=TornaQui%> ">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Elenco dei Procedimenti relativi all'Esecuzione della Misura Sicurezza" border="0">
                </a>
              </td>
<%
            }
            // STUB 02/04/2004 In Presenza del fascicolo SIEP viene richiamata l'iscrizione Procedimento da UDS (ActLoadInserisciFascicoloUDS).
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA) && (idFascicoloSiep.length() > 4) )
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%> ">
                  <img src="/images/new24.gif" width="12" height="12" alt="Iscrizione Procedimento di Esecuzione" border="0">
                </a>
              </td>
<%
            }
            // STUB 02/04/2004 In Assenza del fascicolo SIEP viene richiamata l'iscrizione Procedimento da Soggetto.
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && (idFascicoloSiep.length() < 5) )
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%> ">
                  <img src="/images/new24.gif" width="12" height="12" alt="Iscrizione Procedimento di Esecuzione M.S." border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA))
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                  <img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
          }
        }
      }
%>
    </tr>
  </table>