<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.esecuzionesanzionesostitutiva.action.ICostantiEsecuzioneSS" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

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
            String idSoggetto = request.getParameter(ICostantiEsecuzioneSS.CAMPO_ID_SOGGETTO);
            String idFascicoloSIUS = request.getParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
            String idFascicoloSIEP = request.getParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP);
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEsecuzioneSS.CAMPO_ID_SOGGETTO%>=<%=idSoggetto%>&<%=ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP%>=<%=idFascicoloSIEP%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=idFascicoloSIUS%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Elenco dei Procedimenti relativi all'Esecuzione della Sanzione Sostitutiva " border="0">
                </a>
              </td>
<%
            }
            // In Presenza del fascicolo SIEP viene richiamata l'iscrizione Procedimento da UDS (ActLoadInserisciFascicoloUDS).
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA) && (idFascicoloSIEP.length() > 4) )
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=idFascicoloSIUS%>&TornaQui=<%=TornaQui%> ">
                  <img src="/images/new24.gif" width="12" height="12" alt="Iscrizione Procedimento di Esecuzione S.S." border="0">
                </a>
              </td>
<%
            }
            // In Assenza del fascicolo SIEP viene richiamata l'iscrizione Procedimento da Soggetto.
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && (idFascicoloSIEP.length() < 5) )
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=idFascicoloSIUS%>&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=idSoggetto%>&TornaQui=<%=TornaQui%> ">
                  <img src="/images/new24.gif" width="12" height="12" alt="Iscrizione Procedimento di Esecuzione S.S." border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
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