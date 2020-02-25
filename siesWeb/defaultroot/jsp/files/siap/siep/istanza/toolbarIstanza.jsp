<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="f3b.log.LogF3B" %>

<%

  String lAnnullato = "N";
  if (request.getParameter("annullato") != null)
  {
    lAnnullato = request.getParameter("annullato");
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
   
      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && lAnnullato.equals("N"))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && lAnnullato.equals("N"))
        {
%>
          <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
        {
%>
		<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
        	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
        </a--%>
          <!-- BOTTONE DI STAMPA -->
          <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_TOOLBAR_STAMPA_SIEP%>">
            <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action="+lFun.getNameAction()+"&"+request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"="+request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"/>
          </jsp:include>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO_COPIA))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrow24.gif" alt="Inserimento con Copia" width="24" height="24" border="0">
          </a>
<%
        }

        // SE L'ISTANZA HA ASSOCIATO UN EVENTO ed e' validato E' POSSIBILE
        // TRASFERIRLA..
       // if(request.getParameter("FlagEvento").equals("S") && (request.getParameter("FlagValidato").equals("S") && lAnnullato.equals("N")))
       // {
          if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO) && lAnnullato.equals("N"))
          {
%>
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter("CampoIdEntitaEvento")%>=<%=request.getParameter("ValoreIdEntitaEvento")%>">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
            </a>
<%
          }
     //   }

        lIterBottoni.remove();
      }
    }

    //Visualizzazione della combo
    Iterator lIterCombo = lFunFiglie.iterator();
    if( lFunFiglie.size() != 0 )
    {
%>
      <SCRIPT LANGUAGE="JavaScript">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        function submit( aAction )
        {
          var lCampoIdEntita="<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>";

          document.location.href='<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>='+aAction+'&'+lCampoIdEntita+'=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>';
        }
      </SCRIPT>

      <select name="vai">
<%
        while(lIterCombo.hasNext())
        {
          lFun = (FunctionModel)lIterCombo.next();
          if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO))
          {
%>
            <option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
<%
            lIterCombo.remove();
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
%>