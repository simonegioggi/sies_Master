<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.log.*" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

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
  
  boolean lAbilitaModifica = true; //Booleno che indica se funzione di modifica è abilitata
  boolean lAbilitaCancella = false; //Booleno che indica se funzione di modifica è abilitata

  if ("S".equals(request.getParameter("Cancellabile")) )
  {
    lAbilitaCancella = true;
  }
  

  // Abilitazione funzioni di tipo Modifica
  
  if (request.getParameter("Modificabile") != null)
  {
    String lModificabile = request.getParameter("Modificabile");
    if (lModificabile.equalsIgnoreCase("NO"))
    {
    	lAbilitaModifica = false;
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
		        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && !lFlagValidato  && lProprio && lAbilitaModifica)
		        {
		%>
		          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
		            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
		          </a>
		<%
		        }
		        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && (!lFlagValidato || lAbilitaCancella) && lProprio)
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
		        <%--a href="<-%=IWebConstants.PG_MAIN%>?<-%=IWebConstants.ACTION_FIELD%>=<-%=lFun.getNameAction()%>&<-%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<-%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
		            <img align="middle" src="<-%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
		        </a--%>
		          <!-- BOTTONE DI STAMPA -->
		          <jsp:include page="<%= ISIAPCostantiWeb.PG_BUTTONS_TOOLBAR_STAMPA_SIEP%>">
		            <jsp:param name="ActionLink" value="<%="/jsp/Main.jsp?Action="+lFun.getNameAction()+"&"+request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"="+request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"/>
		          </jsp:include>
		<%
		        }

        		lIterBottoni.remove();
      		 }
	      	
    	 } // Chiude  while(lIterBottoni.hasNext())

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
          		document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>="+aAction+"&"+lCampoIdEntita+"=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>"+"&TornaQui=<%=TornaQui%>" ;
        	}
      		</SCRIPT>

      		<select name="vai">
<% 
			if(lProprio)
			{
        		while(lIterCombo.hasNext())
        		{
			          lFun = (FunctionModel)lIterCombo.next();

				%>
				       <option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
				<%
				       lIterCombo.remove();
        		 }
			 }
			 else //Fascicolo di un altro Ufficio
			 { //Vengono Escluse tutte le funzioni di tipo I, M e C.
				
			   	while(lIterCombo.hasNext())
			    {
			          lFun = (FunctionModel)lIterCombo.next();
			          //Ad un fascicolo Siep non è possibile aggiungere entità correlate a meno della Posizione Giuridica
			          if( lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO) //Funzione di tipo COMBO
			              &&
			              ( ! ((lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) )
			              || (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
			              || (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)))) )
			              //Funzione tipo INSERIMENTO e fascicolo validato
			           {
			%>
			            	<option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
			<%
			            	lIterCombo.remove();
			          }
			    }
			}

%>
	        </select>
	        <a href="javascript:submit(document.forms[0].vai.value);">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>vedi24.gif" alt="Vai" width="24" height="24" border="0">
	        </a>
<%
    	  }
	     
  }	// //Visualizzazione dei bottoni   if( (lFunFiglie != null) &&
%>