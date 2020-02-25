<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<table>
    <tr>
<%
  boolean treeParam = false;
  String Param2 = "";
  String Param3 = "";
  if ((request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null
  && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV).length() > 0 )
  && (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null
  && request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).length() > 0 ))
  {
      Param2 = "&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) + "=" +
                     request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV);
  }

if ((request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE).length() > 0 )
  && (request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE).length() > 0 ))
  {
      treeParam = true;
      Param3 = "&" + request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE) + "=" +
                     request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE);
  }

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
%>           <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=Param2%><%=Param3%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
              </td>
<%          }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
            {%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=Param2%><%=Param3%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%           }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA))
            {
%>            <td><%
               if(((Param3 == null)||(Param3.length()==0))&&((Param2 == null)||(Param2.length()==0)))
                {%>
                 <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
                <%}%>
              <% if((Param2 != null)&&(Param2.length()>0) && !treeParam)
                {%>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
                <%}
                if((Param3 != null)&&(Param3.length()>0) && treeParam)
                {%>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE)%>','<%=request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
                <%}%>
              </td>
<%         }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
            {%>
            <td>
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>')">
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