<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="anagraficaParteUdienza" scope="request" class="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel" />
<jsp:useBean id="notificaSoggetto"       scope="request" class="siap.siep.notifica.model.NotificaModel" />
<jsp:useBean id="modalita"               scope="request" class="java.lang.String" />
<jsp:useBean id="sesso"                  scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni"                scope="request" class="java.lang.String"/>
<jsp:useBean id="difensori"		         scope="request" class="java.util.Vector" />
<jsp:useBean id="nazioniResidenza"       scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"               scope="request" class="java.lang.String" />

<jsp:useBean id="codTipoParte"     scope="request" class="java.lang.String" />
<jsp:useBean id="idEventoUdienza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaSige"    scope="request" class="java.lang.String"/>
<jsp:useBean id="idUdienzaProcedimentoSige"    scope="request" class="java.lang.String"/>
<html>
<head>
<title>[S.I.E.S.] - Modifica Parte Udienza </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=ICostantiPartiUdienza.JS_PARTE_UDIENZA%>"></script>

<% 

String lTitolo = "";
String lIncludeFile= "";
 
	if (anagraficaParteUdienza.getCodParte().equals("F")) //FISICA
	{
    	lTitolo = "Modifica Parte Fisica ";
    	lIncludeFile = ICostantiPartiUdienza.DIV_PERSONA_FISICA;
   	} else { //GIURIDICA
    	lTitolo = "Modifica Parte Giuridica ";
       	lIncludeFile = ICostantiPartiUdienza.DIV_PERSONA_GIURIDICA;
	} 
	
	if(anagraficaParteUdienza.getCodTipoPart().equals("O")){
		lTitolo += "Offesa";	
	} else {
		lTitolo += "Civile";
	}
%>
</head>

<body class="corpo" >
<form name="f">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>" />
    </tr>
  </table>

  <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>

</form>
	<jsp:include page="<%=lIncludeFile%>"/>

</body>
</html>