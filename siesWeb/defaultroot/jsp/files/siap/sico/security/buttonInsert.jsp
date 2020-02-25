<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="Inseribile" 	scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lInseribile = "SI";

  if( Inseribile != null && Inseribile.trim().length() > 0 )
    lInseribile = Inseribile;

  // Costruzione del secondo parametro opzionale Luigi 15-07-2005
  String Param2 = "";
  if (request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV).length() > 0 )
  if (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null && request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).length() > 0 )
      Param2 = "&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) + "=" + request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV);

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
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO_COPIA) && (lInseribile.equals("SI") ))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=Param2%>">
                  <img src="/images/new24.gif" alt="Inserisci" width="22" height="22" border="0">
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