<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

  <table>
    <tr>
<%
      FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");

      String lStringFlagValidato = request.getParameter("FlagValidato");

      boolean lFlagValidato = false;
      if(lStringFlagValidato.equals("S"))
        lFlagValidato = true;
        
      boolean lIsMigratoSenzaValori = false;
      String lStringIsMigratoSenzaValori = request.getParameter("IsMigratoSenzaValori");
      if(lStringIsMigratoSenzaValori.equals("S"))
        lIsMigratoSenzaValori = true;

      boolean lProprio = true; //Booleno che indica se il fasicolo è proprio o di un altro ufficio

      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

      // Visualizzazione dei bottoni
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
<%
                if(request.getParameter("ValoreAzioneChiamante")!=null && !request.getParameter("ValoreAzioneChiamante").equals(""))
                {
%>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                  </a>
<%
                }
                else
                {
%>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                  </a>
<%
                }
%>
              </td>
<%
            }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && !lFlagValidato && lProprio)
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            
            //if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && !lFlagValidato && lProprio)
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && (!lFlagValidato || lIsMigratoSenzaValori) && lProprio)
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>&TornaQui=siap.siep.misurasicurezza.action.ActRicercaFascicoliMisuraSicurezza&<%=IWebConstants.NUM_PAGE%>=<%=request.getAttribute(IWebConstants.NUM_PAGE)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%--               <% if (lIsMigratoSenzaValori && 1==2) { %> --%>
<!--               <td> -->
<!--                 <img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione" border="0" title="Il dato risulta migrato RES "> -->
<!--               </td> -->
<%--               <% } %> --%>
              
<%
            }
            
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
            {
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--td>
	<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
    	<img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
	</a>
</td--%>

               <!-- BOTTONE DI STAMPA -->
                <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_RICERCHE_STAMPA_SIEP%>">
                  <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action="+lFun.getNameAction()+"&"+request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"="+request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"/>
                </jsp:include>
<%
            }
          }
        }
      }
%>
    </tr>
  </table>