<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
// presenza del Link per il bottone di ritorno
boolean retFlag = false;
retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";


      FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");

      String lStringFlagValidato = request.getParameter("FlagValidato");

      boolean lFlagValidato = false;

      if(lFas != null && lStringFlagValidato==null)
      {
         if(lFas.getFlagValidato().equals("S"))
            lFlagValidato = true;
      }
      else
      {
         if(lStringFlagValidato.equals("S"))
            lFlagValidato = true;
      }

     boolean lProprio = true; //Booleno che indica se il fasicolo è proprio o di un altro ufficio

/*     UtenteModel lUtenteMod = new UtenteModel((UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
     String lUffUtente =  lUtenteMod.getUfficioUtente().getCodUfficio();
     if(lFas != null)
      {
        if (lUffUtente.equals(lFas.getCodUfficioInserimento()))
          lProprio = true;
      }
*/
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
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
            {
%>
            <td>
            <%if(request.getParameter("ValoreAzioneChiamante")!=null && !request.getParameter("ValoreAzioneChiamante").equals(""))
                {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
              <%}else
               {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
             <%}%>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && !lFlagValidato && lProprio)
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && !lFlagValidato && lProprio)
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