<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento"%>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="TornaQui"            scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"         scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione" scope="session" class="java.lang.String"/>
<jsp:useBean id="showComboTemplate"   scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
      Collection <FunctionModel>lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

		String strTitolo="";
        if (codTipoImpugnazione.compareTo("04")==0 )
          strTitolo = " Opposizione";
        else
          strTitolo = " Ricorso";

      //Visualizzazione dei bottoni
        for (FunctionModel lFun : lFunFiglie){
        	if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_LOAD_MODIFICA) && lFun.getFunctionId().toString().equals ("90110793") && 
        		request.getParameter("canSetResult").equalsIgnoreCase("false")) {
        		%>
	              <td>
	                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&tipoOper=A&TornaQui=<%=TornaQui%>">
	                  <img src="/images/aggiorna24.gif" width="12" height="12" alt="Aggiorna Esito<%=strTitolo%>" border="0">
	                </a>
	              </td>
        		<%
        	 }
        	
        	 // La modifica è operativa solo se esiste il ricorso associato al provvedimento.
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && request.getParameter("isModificabile").equalsIgnoreCase ("true") && (request.getParameter("isAnnullabile").equalsIgnoreCase("true")) ) {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.         
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/modifica.gif" width="12" height="12" alt="Modifica Iscrizione<%=strTitolo%>" border="0">
                </a>
              </td>
<%          
            }
        	 
            if(lFun.getFunctionType().equalsIgnoreCase(ICostantiFunzioni.TIPO_DETTAGLIO)) {
            	%>
            		<td>
            	        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiImpugnazioneSige.CAMPO_NUMERO_IMPUGNAZIONI%>=<%=request.getParameter("numeroImpugnazioni")%>&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&<%=ICostantiImpugnazioneSige.CAMPO_SHOW_COMBO_TEMPLATE%>=<%=showComboTemplate%>&TornaQui=<%=TornaQui%>">
            	            <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio <%=strTitolo%>" border="0">
            	        </a>
            	    </td>
            	<%         	 
            }
            
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && (request.getParameter("Stampa").equalsIgnoreCase("SI")))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/transfer.gif" alt="Trasferisci" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            
            if( (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)) && (request.getParameter("isAnnullabile").equalsIgnoreCase("true"))) {
            	%>
   		    	  <td>
   		      		<a href="Javascript:confermaAnnullamento3Param('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=ICostantiProvvedimentoSige.CAMPO_COD_TIPO_PROVVEDIMENTO%>','<%=request.getParameter("CodTipoProvvedimento")%>','<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>','<%=request.getParameter("CampoIdImpugnazione")%>' );">
   					  <img src="/images/delete.gif" width="12" height="12" alt="Annulla <%=strTitolo%>" border="0">
   		      		</a>
   		    	  </td>
            	<%
             }
 
        }
%>
    </tr>
  </table>