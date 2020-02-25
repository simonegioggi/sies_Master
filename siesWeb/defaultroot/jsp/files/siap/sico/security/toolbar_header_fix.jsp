<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Stack"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>

<jsp:useBean id="StackDiRitorno" scope="session" class="java.util.Stack" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<jsp:useBean id="udienzasige" scope="request"  class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="udienzamonocraticasige" scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<%
	String lCodUffAppartenenza = request.getParameter("valoreufficio");
	UdienzaSigeModel udienza = new UdienzaSigeModel();
	String tipoRito = request.getParameter("tipoRito");
	String popUp = "";
	if(request.getParameter("PopUp") != null && !request.getParameter("PopUp").equals("")){
		popUp = request.getParameter("PopUp");
	}
	
	if ("C".equals(tipoRito)) {
		udienza = udienzasige;
	} else if ("M".equals(tipoRito)) {
		udienza = udienzamonocraticasige;
	}

	Collection lFunFiglie = (Collection) request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

	String lModificabile = "SI";//Parametro che vale per la cancellazione e la modifica
	String lCancellabile = "SI";//Parametro che vale solo per la cancellazione e non per la modifica
	boolean lAbilitaModifica = true; //Booleno che indica se funzione di modifica è abilitata
	boolean lAbilitaCancella = true; //Booleno che indica se funzione di modifica è abilitata

	// Abilitazione funzioni di tipo Modifica
	if (request.getParameter("Modificabile") != null) {
		lModificabile = request.getParameter("Modificabile");
		if (lModificabile.equalsIgnoreCase("NO")) {
			lAbilitaModifica = false;
		}
	}

	// Abilitazione funzioni di tipo Cancella
	if (request.getParameter("Cancellabile") != null) {
		lCancellabile = request.getParameter("Cancellabile");
		if (lCancellabile.equalsIgnoreCase("NO")) {
			lAbilitaCancella = false;
		}
	}

	//Visualizzazione dei bottoni delle funzioni
	if ("si".equals(request.getParameter("funzioni"))) {
	if ((lFunFiglie != null) && (lFunFiglie.size() != 0)) {
		Iterator lIterBottoni = lFunFiglie.iterator();
		FunctionModel lFun = null;
		while (lIterBottoni.hasNext()) {
			lFun = (FunctionModel) lIterBottoni.next();

			if (lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {

				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && lAbilitaModifica) {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>=<%=udienza.getIdUdienzaSige()%>&<%=ICostantiCollegio.FORM_DEF_COLLEGIO%>=yes&TornaQui=<%=TornaQui%>&PopUp=<%=popUp%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>
<%
				}

				if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && lAbilitaCancella && !("00000").equals(lCodUffAppartenenza)) {
%>
           <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>','<%=udienza.getIdUdienzaSige()%>','<%=ICostantiCollegio.FORM_DEF_COLLEGIO%>','yes','TornaQui','<%=TornaQui%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
<%
				}

			}
		}
	}
	}
%>







