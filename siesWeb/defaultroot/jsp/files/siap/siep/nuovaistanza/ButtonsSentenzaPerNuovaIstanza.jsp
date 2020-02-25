<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>

<jsp:useBean id="TornaQui"      scope="request" class="java.lang.String"/>

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

		if(lStringFlagValidato!=null )
		{
			if(lStringFlagValidato.equals("S"))
				lFlagValidato = true;
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
					if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO) && lFun.getFunctionId().compareTo(new BigDecimal(ICostantiNuovaIstanza.FUNZIONE_DETTAGLIO_SENTENZA))==0)
					{
%>
            <td>
            <%if(request.getParameter("ValoreAzioneChiamante")!=null && !request.getParameter("ValoreAzioneChiamante").equals(""))
                {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Sentenza" border="0">
                </a>
              <%}else
               {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>"> 
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Sentenza" border="0">
                </a>
             <%}%>
              </td>
<%
					}
				}
			}
		}
%>
    </tr>
  </table>