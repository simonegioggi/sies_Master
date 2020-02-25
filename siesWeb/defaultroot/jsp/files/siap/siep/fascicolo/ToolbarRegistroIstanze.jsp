<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.security.model.FunctionModel"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.security.ICostantiFunzioni"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

<%
	String lStringFlagValidato = request.getParameter("FlagValidato");
	boolean lProprio = false; //Booleno che indica se il fasicolo è prorpio o di un altro ufficio
	
	FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");
	UtenteModel lUtenteMod = new UtenteModel((UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	
	String lUffUtente =  lUtenteMod.getUfficioUtente().getCodUfficio();
	
	if (lUffUtente.equals(lFas.getChiaveUfficio()))
	    lProprio = true;
	
	boolean lFlagValidato = false;
	if(lStringFlagValidato.equals("S"))
	  	lFlagValidato = true;
	
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
	        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && !lFlagValidato && lProprio)
	        {
	%>
	          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
	            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
	          </a>
	<%
	        }
	        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && !lFlagValidato  && lProprio)
	        {
	%>
	          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
	            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
	          </a>
	<%
	        }
	        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && !lFlagValidato  && lProprio)
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

	        lIterBottoni.remove();
	      }
		 }
	      
		//Visualizzazione della combo
		Iterator lIterCombo = lFunFiglie.iterator();
		if( lFunFiglie.size() != 0 )
		{%>
			<SCRIPT LANGUAGE="JavaScript">
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			function submit( aAction )
			{
				var lCampoIdEntita="<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>";
				document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>="+aAction+"&"+lCampoIdEntita+"=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"+"&TornaQui=<%=TornaQui%>" ;
			}
			</SCRIPT>
			<select name="vai">
			<%if (lProprio) 
			{
				while (lIterCombo.hasNext()) 
				{
					lFun = (FunctionModel) lIterCombo.next();
					if (lFun.getNameAction() != null
						&& (lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActLoadAnnullaAssociaRIaFascicoloSIEP")
						|| lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActLoadConvertiRIinFascicoloSIEP")
						|| lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActLoadAssociaRIaFascicoloSIEP")
						|| lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActLoadDisposizioniPM") 
						|| lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActLoadInoltroPM")
						|| lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActElencoNuovaIstanza")
						|| lFun.getNameAction().equals("siap.siep.fascicolo.action.ActLoadInserisciResidenzaFascicolo")
						|| lFun.getNameAction().equals("siap.sico.magistratocompetente.action.ActLoadInserisciMagistratoCompetente")
						|| lFun.getNameAction().equals("siap.siep.avvocato.action.ActLoadInserisciAvvocato")
						|| lFun.getNameAction().equals("siap.siep.fascicolo.action.ActLoadInserisciDomicilioFascicolo")					
						|| lFun.getNameAction().equals("siap.siep.nuovaistanza.action.ActLoadArchiviazioneManuale"))					
     				) 
					{%>
							<option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
						<%	lIterCombo.remove();
					}
				}
			}
		}%>
		</select>
		<a href="javascript:submit(document.forms[0].vai.value);"> <img
			align="middle" src="<%=IWebConstants.IMAGES_DIR%>vedi24.gif"
			alt="Vai" width="24" height="24" border="0"> </a>
<%	}
%>



