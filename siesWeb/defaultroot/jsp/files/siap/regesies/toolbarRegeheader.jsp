<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies" %>




<%

  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);


  String lParameter = request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA) +"="+
  request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA);

  if ((request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null
  && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV).length() > 0 )
  && (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null
  && request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).length() > 0 ))
  {
      lParameter += "&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) + "=" +
                     request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV);
  }

//Gestione terzo campo per la chiave dei reati
boolean treeParam = false;
String lParam3 = "";

if ((request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE).length() > 0 )
  && (request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE) != null
  && request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE).length() > 0 ))
  {
      treeParam = true;
      lParam3 = "&" + request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE) + "=" +
                     request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE);
  }

//Visualizzazione dei bottoni
if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=lParameter%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=lParameter%><%=lParam3%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA))
        {
         //Due parametri
         if((lParameter != null) && (lParameter.length()>0) && !treeParam) {%>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>');">
                  <img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
                </a>
                <%} else //tre parametri
         if(treeParam)  {   %>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiRegeSies.CAMPO_ENTITA_CHIAVE_TRE)%>','<%=request.getParameter(ICostantiRegeSies.CAMPO_VALORE_CHIAVE_TRE)%>');">
                   <img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
                </a>
              <%}else //un parametro solo
                {%>
                 <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
                </a>
                <%}%>
              </td>
<%
        }

        lIterBottoni.remove();
      }
    }

    //Visualizzazione della combo
    Iterator lIterCombo = lFunFiglie.iterator();
    if( lFunFiglie.size() != 0 )
    {
       boolean inseritaPrima = false;
       boolean daInserire = false;
       while(lIterCombo.hasNext())
        {
          lFun = (FunctionModel)lIterCombo.next();

          if(lFun.getVisualizzazionType() != null &&
          lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO))
          {
%>              <select name="vai">
                <option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
                <%
          }
        }
%>
        </select>
      <a href="javascript:submit(document.forms[0].vai.value);">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>vedi24.gif" alt="Vai" width="24" height="24" border="0">
      </a>
<%

    }
  }

if(   request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE) != null
     && !request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE).equals(""))
  {
    String lAzioneChiamante = request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
%>
    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAzioneChiamante%>">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
<%
  }

if(   request.getParameter(ICostantiRegeSies.CAMPO_TORNA_INDIETRO) != null
     && !request.getParameter(ICostantiRegeSies.CAMPO_TORNA_INDIETRO).equals(""))
  {%>
    <td class="LBG">
          <a href="Javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
<%}%>