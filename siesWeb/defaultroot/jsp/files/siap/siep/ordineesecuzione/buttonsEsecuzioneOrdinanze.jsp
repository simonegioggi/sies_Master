<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.util.SIESSwitch" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ActGestisciButtonsProvvedimento" %>
<jsp:useBean id="TornaQui"    scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
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
	 if(SIESSwitch.isReworkDettaglio())
      {
      //   if(  request.getParameter("docRegistrato") == null
        //        || request.getParameter("docRegistrato").equals("N"))
         //     {
         ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
         lAction.isDettaglioVisualizzabile(request.getParameter("TipoProvvedimento"),request.getParameter("MotivoEvento"),request.getParameter("TemIdTemplate"));

     if(lAction.isDettaglioVisualizzabile(request.getParameter("TipoProvvedimento"),request.getParameter("MotivoEvento"),request.getParameter("TemIdTemplate"))	)
                  {
                 String lActDettaglio = lAction.getActionDettaglioProvvedimento(request.getParameter("MotivoEvento"), request.getParameter("TipoEvento"),request.getParameter("TipoProvvedimento"),request.getParameter("TemIdTemplate"),request.getParameter("docRegistrato"));

                 if (lActDettaglio.length()>1)
                 {
%>
                  <td>
                    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
                      <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                    </a>
                  </td>
<%
                 }
          }
      }
      else
      {
          if(  request.getParameter("docRegistrato") == null
            || request.getParameter("docRegistrato").equals("N"))
              {
                if(!"02".equals(request.getParameter("TipoProvvedimento"))
                && !"03".equals(request.getParameter("TipoProvvedimento")))
                {
                 ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
                   String lActDettaglio = lAction.getActionDettaglioProvvedimento(request.getParameter("MotivoEvento"),
          request.getParameter("TipoEvento"),
          request.getParameter("TipoProvvedimento"),
          request.getParameter("TemIdTemplate"),
          request.getParameter("docRegistrato"));

                 if (lActDettaglio.length()>1)
                 {
%>
                  <td>
                    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&modifica=R<%=retParam%>">
                      <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                    </a>
                  </td>
<%
                 }
                }
              }

              else
              {
                if(!"02".equals(request.getParameter("TipoProvvedimento")) && !"03".equals(request.getParameter("TipoProvvedimento")))
                {
                  String lActDettaglio = "siap.sico.evento.action.ActDettaglioDocumentoOrdinanze";
%>
                  <td>
                    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActDettaglio%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                      <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                    </a>
                  </td>
<%
              }
            }
        }
      }
						//Altro Tipo
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A"))
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%
            }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && (request.getParameter("Evento").compareTo("SI")==0) && !request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).equals("A")
             && !request.getParameter("docRegistrato").equals("M") // migrato
             )
            {
              // ActGestisciButtonsProvvedimento lAction = new ActGestisciButtonsProvvedimento();
              // String lActDettaglio = lAction.getActionStampaProvvedimento(request.getParameter("MotivoEvento"));
              String lActStampa ="siap.sico.evento.action.ActLoadDocumento";

              //if (lActDettaglio.length()>1)
              //{
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
                <%--td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lActStampa%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                    <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
                  </a>
                </td--%>

               <!-- BOTTONE DI STAMPA -->
                <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_RICERCHE_STAMPA_SIEP%>">
                  <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action="+lActStampa+"&"+request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"="+request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"/>
                </jsp:include>
<%
              //}
            }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                  <img src="/images/net16.gif" alt="Trasferisci" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
          }

if (   lFun.getVisualizzazionType().equals("CP") 
    && request.getParameter("isValidato").equals("S")
   ) 
{
%>
<td>
  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
    <!--img src="/images/net16.gif" alt="Dettaglio Pena" width="12" height="12" border="0"-->
    <img src="/images/calc12.gif" alt="Dettaglio Pena" width="12" height="12" border="0">
  </a>
</td>
<%
}          



          
        }
      }
%>
    </tr>
  </table>