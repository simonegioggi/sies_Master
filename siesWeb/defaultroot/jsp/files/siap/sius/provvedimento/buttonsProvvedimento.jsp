<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"     scope="request" class="java.lang.String"/>
<%-- <jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" /> --%>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<script type="text/javascript">
	function openPopup(url) {
		newwindow=window.open(url,'name','height=570,width=920,top=200,left=200,location=0,menubar=0,status=0,resizable=0,scrollbars=1');
		if (window.focus) {newwindow.focus()}
	}
  </script>

<table>
    <tr>

<%
// Flag di abilitazione del bottone di modifica
boolean abilitaModifica = true;
    if( request.getParameter("Modifica") != null && ! request.getParameter("Modifica").equalsIgnoreCase("SI"))
    	abilitaModifica = false;
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
              if (request.getParameter("CodTipoProvvedimento").compareTo("03")==0 && (request.getParameter("CodEsito").compareTo("0603")!=0) && (lFun.getVisualizationOrder().intValue()==1) )
              {
              /* Ordinanza e f.ne dettaglio di posizione 1 */
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio ordinanza" border="0">
                </a>
              </td>
<%             }
                else  if( (request.getParameter("CodTipoProvvedimento").compareTo("03")==0) && (request.getParameter("CodEsito").compareTo("0603")==0) && (lFun.getVisualizationOrder().intValue()==9))
                {       /* Ordinanza di rinvio Udienza */
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio decreto" border="0">
                </a>
              </td>
<%             }
                else  if( (request.getParameter("CodTipoProvvedimento").compareTo("02")==0) && (request.getParameter("CodEsito").compareTo("0600")!=0) && (lFun.getVisualizationOrder().intValue()==8))
                {       /* Decreto  generale*/
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio decreto" border="0">
                </a>
              </td>
<%            }
              else  if( (request.getParameter("CodTipoProvvedimento").compareTo("02")==0) && (request.getParameter("CodEsito").compareTo("0600")==0) && (lFun.getVisualizationOrder().intValue()==7))
              {       /* Decreto unificazione */
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Decreto unificazione" border="0">
                </a>
              </td>
<%            }
              else  if( (request.getParameter("CodTipoProvvedimento").compareTo("14")==0) && (request.getParameter("CodEsito").compareTo("0600")==0) && (lFun.getVisualizationOrder().intValue()==11) )
              {       /* Unificazione da Verbale*/
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio unificazione" border="0">
                </a>
              </td>
<%            }
              else  if( (request.getParameter("CodTipoProvvedimento").compareTo("48")==0) && (request.getParameter("CodEsito").compareTo("0604")==0) && (lFun.getVisualizationOrder().intValue()==13) )
              {       /* Stralcio Oggetti*/
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Stralcio Oggetti" border="0">
                </a>
              </td>
<%            }
            	else  if( (request.getParameter("CodTipoProvvedimento").compareTo("50")==0) && (request.getParameter("CodEsito").compareTo("0603")==0) && (lFun.getVisualizationOrder().intValue()==9) )
            	{       /* Rinvio udienza da verbale*/
%>
            <td>
              <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Rinvio Udeinza da Verbale" border="0">
              </a>
            </td>
<%              // Aggiungere le condizioni corrette ********************
				} else if ( (request.getParameter("CodTipoProvvedimento").compareTo("01") ==0 ) && (lFun.getVisualizationOrder().intValue()==5) )
				{
				// Sentenza
%>
	              <td>
	                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
	                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Sentenza" border="0">
	                </a>
	              </td>
<%	
			  }
              
              if (lFun.getVisualizationOrder().intValue()==6 && (request.getParameter("Allegato").compareTo("SI")==0))
              {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/attach.gif" width="12" height="12" alt="Allegati" border="0">
                </a>
              </td>
<%            }
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)   && abilitaModifica)
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&CodTipoProvvedimento=<%=request.getParameter("CodTipoProvvedimento")%><%=retParam%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
          // ANNULLAMENTO
	  if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && (isModificabile.equals("SI")) && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null &&  request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null && request.getParameter("Depositato") != null  && request.getParameter("Depositato").equalsIgnoreCase("SI"))
	  {
// 	  String idUtente=UtenteConnesso.getUserId();
  %>
	    <td>
	      <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>');">
		<img src="/images/delete.gif" width="12" height="12" alt="Annulla" border="0">
	      </a>
	    </td>
	    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
		<%-- Modifica MEV 6		
		<td>
  			<a href="#" onClick="openPopup('/siesEsecuzione/index.jsp?action=DELETE&idUtente=<%=idUtente%>&idEvento=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');return(false)">
				<img src="/images/deleteWS.png" width="12" height="12" alt="Cancella foglio complementare trasmesso al SIC" border="0">
  			</a>
		</td>
 		--%>
  <%
	  }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && (request.getParameter("Stampa").compareTo("SI")==0))
            {
%>
              <td>
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>')">
                  <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                  <img src="/images/transfer.gif" alt="Trasferisci" width="12" height="12" border="0">
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