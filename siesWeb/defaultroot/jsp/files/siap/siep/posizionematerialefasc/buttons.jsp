<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lModificabile = "SI";

  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
  }
  else
  {
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
    String lUffUtente =  lUtenteMod.getUfficioUtente().getCodUfficio();

   FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");
      if (lFas != null)
      {
        if (!lUffUtente.equals(lFas.getChiaveUfficio()))
           lModificabile = "NO";
      }
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

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_LINK))
          {
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)&& (lModificabile.equals("SI")))
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
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