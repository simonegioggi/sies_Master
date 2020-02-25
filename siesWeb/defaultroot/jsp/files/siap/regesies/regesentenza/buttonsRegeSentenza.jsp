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
  String Param3 = "";


if ((request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_UNO) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_UNO).length() > 0 )
  && (request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_UNO) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_UNO).length() > 0 ))
  {
      treeParam = true;
      Param3 = "&" + request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_UNO) + "=" +
                     request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_UNO);
  }

if ((request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_DUE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_DUE).length() > 0 )
  && (request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_DUE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_DUE).length() > 0 ))
  {
      treeParam = true;
      Param3 += "&" + request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_DUE) + "=" +
                     request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_DUE);
  }

if ((request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE).length() > 0 )
  && (request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE).length() > 0 ))
  {
      treeParam = true;
      Param3 += "&" + request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE) + "=" +
                     request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE);
  }

if ((request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_QUATTRO) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_QUATTRO).length() > 0 )
  && (request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_QUATTRO) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_QUATTRO).length() > 0 ))
  {
      treeParam = true;
      Param3 += "&" + request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_QUATTRO) + "=" +
                     request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_QUATTRO);
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
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=Param3%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
              </td>
<%          }
          }
        }
      }
      %>
    </tr>
  </table>