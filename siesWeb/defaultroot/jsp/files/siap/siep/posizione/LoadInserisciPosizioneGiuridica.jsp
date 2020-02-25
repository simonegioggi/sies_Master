<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa" %>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare" %>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune" %>

<jsp:useBean id="PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="posizioneGiuridicaEsecuzione"      scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="posizioneGiuridicaIscrizione"      scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="lIscrizionePos"          scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneProcessuale"    scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoPosizioneAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraCautelareL2" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMisuraCautelareL3" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoistituNormale"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoIstitutoAltraCausa"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioPM" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioRegGen" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmittente"             scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCompetente"             scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmittenteCautelare"             scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmittenteCautelareLuogo"             scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCompetenteCautelare"             scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioPmSedeDesc"             scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoMaschera"                scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"                scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaSez1DD"          scope="request" class="java.lang.String"/>
<jsp:useBean id="defaultSedeTipoUfficioPM"      scope="request" class="java.lang.String"/>
<jsp:useBean id="tipUffPM"        scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form di INSERIMENTO e MODIFICA della Posizione Giuridica
//==============================================================================

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getAltraCausa();
  MisuraCautelareModel lMisuraCautelare = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getMisuraCautelare();

  if(lPosizione == null) {
    lPosizione = new PosizioneGiuridicaModel();
    //lPosizione.setPrimaPosizione(true);
  }

  if(lLuogoDetenzione == null) {
    lLuogoDetenzione = new LuogoDetenzioneModel();
  }

  if(lAltraCausa == null) {
	    lAltraCausa = new AltraCausaModel();
  }

  if(lMisuraCautelare == null) {
	  lMisuraCautelare = new MisuraCautelareModel();
  }
  
  String jsNumField = " onFocus='javascript:textboxSelect(this)' onkeypress='return TicTabNumField(this,event)' ";
  String jsNumFieldDM = " onFocus='javascript:textboxSelect(this)' onkeypress='return TicTabNumField(this,event)' onBlur='javascript:value=FillDM(value)' ";
  String jsNumFieldYear = " onFocus='javascript:textboxSelect(this)' onkeypress='return TicTabNumField(this,event)' onBlur='javascript:value=FillYear(value)' ";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Posizione Giuridica </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      var desktop;

      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2) {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

      function ListaComuni(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaUffici(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ListaDistretti(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio) {
    	  if (codTipoUfficio == '' || codTipoUfficio == '-'){
    		  alert("selezionare autorità emittente");
    	  } else {
    		  desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    	  }
      }

      function ChoosePopup(a_formname, a_comboid, a_fieldname) {
          var selectTipoUfficio = document.getElementById(a_comboid);
          var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
          var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
          if (codTipoUfficio == 'PM' || codTipoUfficio == 'PMM' || codTipoUfficio == 'PGCAP'){
        	  ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio);
          } else {
            alert("selezionare Tipo Ufficio PM");
          }
      }
      
   	function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
 	{
 		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
 	}
   	
    </script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  </head>

<body class="corpo" onload="initPage();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font class="label">Funzione :</font>&nbsp;
<%
            PosizioneGiuridicaModel lPosGiu = new PosizioneGiuridicaModel();
            Date lDataDecorrenza = null;

            String lAction = "";
            String checked = "checked='checked'";
            String checkL = "";
            String checkL1 = checked;
            String checkL2 = "";
            String checkL3 = "";
            String checkEI = "";
            String checkEA = "";
            String showOld = "display: none;";
            String showScelte = "";

            //if( modalita.equals("I") ) {
              lAction = "siap.siep.posizione.action.ActInserisciPosizioneGiuridica";
              lPosGiu = lPosizione;
              if (lPosGiu!=null) {
            	  lDataDecorrenza = lPosGiu.getDataInizio();
              }              
				
              if (lPosGiu!=null && lPosGiu.getCodPosizioneGiuridica()!=null && !lPosGiu.getCodPosizioneGiuridica().equalsIgnoreCase("") &&
            		  (lPosGiu.getCodMaschera()==null || lPosGiu.getCodMaschera().equalsIgnoreCase(""))) {
            	  int lIntPos = Integer.parseInt(lPosGiu.getCodPosizioneGiuridica());
                  switch (lIntPos) {
                  case 0:
                  case 5:
                  case 6:
                  case 7: 
                  case 8:
                  case 10:
                  case 16:
                  case 17:
                  case 20:               	  
                  case 26: 
                  case 27:
                  case 46:
                  case 47:
                  case 48: { 
                	lPosGiu.setCodMaschera("L");
                    break;
                  }
                  case 2:
                  case 4:
                  case 11:
                  case 12:
                  case 13:
                  case 15:
                  case 18:                	  
                  case 21:
                  case 23:
                  case 25:
                  case 29:
                  case 28:
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  case 45:
                  case 50:
                  case 51:
                  case 52:
                  case 53:
                  case 54:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87: { 
                	lPosGiu.setCodMaschera("EA");
                    break;
                  }
                  case 1:
                  case 3:
                  case 9:
                  case 14:
                  case 19:
                  case 22:
                  case 24:
                  case 30:
                  case 31:
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 36:
                  case 37:
                  case 38:
                  case 39:
                  case 40:
                  case 49:
                  case 55:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 73: {
                	lPosGiu.setCodMaschera("EI");
                    break;
                  }
                  case 74:
                  case 75: { 
                	lPosGiu.setCodMaschera("L1");
                    break;
                  }
                  case 76:
                  case 77: { 
                	lPosGiu.setCodMaschera("L2");
                    break;
                  }
                  case 78:
                  case 79:
                  case 80:
                  case 81: { 
                  	lPosGiu.setCodMaschera("L3");
                    break;
                  }
                }
              }

              if (ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_Libero.equals(lPosGiu.getCodMaschera()) // L
              		|| ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(lPosGiu.getCodMaschera()) // L1
              		|| ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(lPosGiu.getCodMaschera()) // L2
              		|| ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(lPosGiu.getCodMaschera()) // L3
              		){
              	checkL = checked;
              	
                  if (ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoIstituto.equals(lPosGiu.getCodMaschera()) // L1
                  		){
                  	// checkL1 = checked;
                  } else if (ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto.equals(lPosGiu.getCodMaschera()) // L2
                  		){
                  	checkL1 = "";
                  	checkL2 = checked;
                  } else if (ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoCautelareAltro.equals(lPosGiu.getCodMaschera()) // L3
                  		){
                  	checkL1 = "";
                  	checkL3 = checked;
                  }  

              } else if (ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_EspiazioneIstituto.equals(lPosGiu.getCodMaschera()) // EI
              		){
              	checkEI = checked;
              } else if (ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_EspiazioneAltro.equals(lPosGiu.getCodMaschera()) // EA
              		){
              	checkEA = checked;
              } else if ("-".equals(tipoMaschera)	){
              	// caso di inserimento
              	checkL = checked;
              } else {
              	// caso di visualizzazione vecchio form
              	showOld = ""; // lascio apparire il vecchio form
              	//checkL = checked; // mostro invece il nuovo form con il caso libero
              }

              if( modalita.equals("I") ) { %>
              <font class="campo">Inserimento Posizione Giuridica</font>
              <% } else if( modalita.equals("M") ) { %>
              <font class="campo">Modifica Posizione Giuridica </font>
              <% } %>
<%
           /*  } else if( modalita.equals("M") ) {
              lAction = "siap.siep.posizione.action.ActModificaPosizioneGiuridica";
              lPosGiu = lPosizione;
              lDataDecorrenza = lPosGiu.getDataInizio();
              
              // caso di visualizzazione vecchio form
              showOld = ""; //lascio apparire il vecchio form
              showScelte = "display: none;"; //nascondo il div con i radiobutton */
%>
              <!-- <font class="campo">Modifica Posizione Giuridica</font> -->
<%
            //}
%>
        </td>
      </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<div id="divSezioneScelte" style="<%=showScelte%>">
<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneGiuridica">
    <table style="width: 100%;">
      <tr>
        <td class="l">
        <input type="radio" name='tipoSezione' value='sezioneLibero' onClick='radioSezione();' <%=checkL%>>Libero
        </td>
        <td class="l">
          <div id="divSezioneDetenutoAltraCausa" style="<%=showScelte%>">
        	<input type="radio" name='tipoSezione' value='sezioneIstituto' onClick='radioSezione();' <%=checkEI%>>Espiazione Pena in Istituto di Detenzione
          </div>
        </td>
        <td class="l">
          <div id="divSezioneDetenutoAltraCausa2" style="<%=showScelte%>">
        	<input type="radio" name='tipoSezione' value='sezioneAltro' onClick='radioSezione();' <%=checkEA%>>Espiazione Pena in Altro Luogo
           </div>
        </td>
      </tr>
    </table>
</form>
</div>

<%String lRedir=request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);%>

<div id="divSezioneOld" style="<%=showOld%>">
	<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneGiuridicaOld">
	  <%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Old.jsp" %>
	
		  <% if (lRedir!=null &&  !lRedir.equals("")) { %>
		  <input type="hidden" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=lRedir%>">
		  <% } %>
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_PROCESSUALE%>" value="-">
		  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosGiu.getIdPosizioneGiuridica())%>">
		  <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getFasSiuIdFascicoloSius())%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>" value="xx">
	</form>
</div>

<div id="divSezioneLibero" style="display: none;">
	<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneGiuridicaS1">
	  <%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Sez1.jsp" %>
	
		  <% if (lRedir!=null &&  !lRedir.equals("")) { %>
		  <input type="hidden" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=lRedir%>">
		  <% } %>
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_PROCESSUALE%>" value="-">
		  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosGiu.getIdPosizioneGiuridica())%>">
		  <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getFasSiuIdFascicoloSius())%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>" value="<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_Libero%>">
	</form>
</div>

<div id="divSezioneIstituto" style="display: none;">
	<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneGiuridicaS2">
	  <%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Sez2.jsp" %>
	
		  <% if (lRedir!=null &&  !lRedir.equals("")) { %>
		  <input type="hidden" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=lRedir%>">
		  <% } %>
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_PROCESSUALE%>" value="-">
		  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosGiu.getIdPosizioneGiuridica())%>">
		  <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getFasSiuIdFascicoloSius())%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>" value="<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_EspiazioneIstituto%>">
	</form>
</div>

<div id="divSezioneAltro" style="display: none;">
	<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPosizioneGiuridicaS3">
	    <%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-Sez3.jsp" %>
	
		  <% if (lRedir!=null &&  !lRedir.equals("")) { %>
		  <input type="hidden" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value="<%=lRedir%>">
		  <% } %>
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_PROCESSUALE%>" value="-">
		  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosGiu.getIdPosizioneGiuridica())%>">
		  <input type="hidden" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getFasSiuIdFascicoloSius())%>">
		  <input type="hidden" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>" value="<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_EspiazioneAltro%>">
	</form>
</div>

<script language="JavaScript" type="text/javascript">
function radioAltraCausa() {

	var radioTipoAltraCausa=document.getElementsByName('tipoAltraCausa');
	var divCausaDD=document.getElementById('altraCausaDD');
	var divCausaMCD=document.getElementById('altraCausaMCD');
	var divCausaMCA=document.getElementById('altraCausaMCA');
	if(radioTipoAltraCausa[0].checked) {
		divCausaDD.style.display='block';
		divCausaMCD.style.display='none';
		divCausaMCA.style.display='none';
		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>.value ='<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoIstituto%>';
	} else if (radioTipoAltraCausa[1].checked) {
		divCausaDD.style.display='none';
		divCausaMCD.style.display='block';
		divCausaMCA.style.display='none';
		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>.value ='<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoCautelareIstituto%>';
	} else if (radioTipoAltraCausa[2].checked) {
		divCausaDD.style.display='none';
		divCausaMCD.style.display='none';
		divCausaMCA.style.display='block';
		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>.value ='<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_LiberoCautelareAltro%>';
	}

}

function checkSezioneAltraCausa() {
	var campoCodPosGiuridica=document.getElementById('<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>S1');
	var checkAltraCausa=document.getElementById('<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>S1');
	var divSezioneAltraCausa=document.getElementById('divSezioneAltraCausa');
	var divSezioneDetenutoAltraCausa=document.getElementById('divSezioneDetenutoAltraCausa');
	var divSezioneDetenutoAltraCausa2=document.getElementById('divSezioneDetenutoAltraCausa2');
	if (checkAltraCausa.checked){
		divSezioneAltraCausa.style.display='block';
		divSezioneDetenutoAltraCausa.style.display='none';
		divSezioneDetenutoAltraCausa2.style.display='none';
		campoCodPosGiuridica.disabled=true;
		radioAltraCausa();
	} else {
		campoCodPosGiuridica.disabled=false;
		divSezioneAltraCausa.style.display='none';
		divSezioneDetenutoAltraCausa.style.display='block';
		divSezioneDetenutoAltraCausa2.style.display='block';
		document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiPosizioneGiuridica.CAMPO_COD_MASCHERA%>.value ='<%=ICostantiPosizioneGiuridica.CAMPO_TIPO_MASCHERA_Libero%>';
	}

}

function radioSezione() {

	var divSezioneLibero=document.getElementById('divSezioneLibero');
	var divSezioneIstituto=document.getElementById('divSezioneIstituto');
	var divSezioneAltro=document.getElementById('divSezioneAltro');
	var divSezioneOld=document.getElementById('divSezioneOld');
	var radioTipoAltraCausa=document.getElementsByName('tipoAltraCausa');

	if(document.LoadInserisciPosizioneGiuridica.tipoSezione[0].checked) {
		divSezioneLibero.style.display='block';
		divSezioneIstituto.style.display='none';
		divSezioneAltro.style.display='none';
		divSezioneOld.style.display='none';
		
	} else if (document.LoadInserisciPosizioneGiuridica.tipoSezione[1].checked) {
		divSezioneLibero.style.display='none';
		divSezioneIstituto.style.display='block';
		divSezioneAltro.style.display='none';
		divSezioneOld.style.display='none';
		
	} else if (document.LoadInserisciPosizioneGiuridica.tipoSezione[2].checked) {
		divSezioneLibero.style.display='none';
		divSezioneIstituto.style.display='none';
		divSezioneAltro.style.display='block';
		divSezioneOld.style.display='none';
		
	}

}

function initPage() {
	radioSezione();
	checkSezioneAltraCausa();
}

function pulisciIstitutoId (nomeCampoComune, nomeCampoId) {
	var campoDescr = document.getElementById(nomeCampoComune);
	var campoId    = document.getElementById(nomeCampoId);
	campoDescr.value="";
	campoId.value="";
}

<%
String subSez2 = "_L2";
String subSez3 = "_L3";
%>

function Verify() {
	var radioTipoAltraCausa=document.getElementsByName('tipoAltraCausa');
	var divCausaDD=document.getElementById('altraCausaDD');
	var divCausaMCD=document.getElementById('altraCausaMCD');
	var divCausaMCA=document.getElementById('altraCausaMCA');
	//alert('radio1 radioTipoAltraCausa[0].checked '+radioTipoAltraCausa[0].checked);
	//alert('radio2 radioTipoAltraCausa[1].checked '+radioTipoAltraCausa[1].checked);
	//alert('radio3 radioTipoAltraCausa[2].checked '+radioTipoAltraCausa[2].checked);
	var radioTipoAltraCausa=document.getElementsByName('tipoAltraCausa');
	var checkAltraCausa=document.getElementById('<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>S1');
	//alert('tipoSezione '+document.LoadInserisciPosizioneGiuridica.tipoSezione[0].checked);
	//alert('Detenuto per altra causa '+checkAltraCausa.checked);
	if(document.LoadInserisciPosizioneGiuridica.tipoSezione[0].checked && checkAltraCausa.checked) {
		if (typeof radioTipoAltraCausa!='undefined') {
			if(radioTipoAltraCausa[0].checked) {
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.value=="-")
			    {
			      alert('Il tipo misura è obbligatorio');
			      return false;
			    }	
				var DataScadenzaAltraPena = document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_GIORNO_DATA_SCADENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_MESE_DATA_SCADENZA%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiAltraCausa.CAMPO_ANNO_DATA_SCADENZA%>.value;
				if(!ControllaDataPassaVuota(DataScadenzaAltraPena)){
			      alert('Data Scadenza Altra Pena non valida');
			      return false;
			    }
			} else if (radioTipoAltraCausa[1].checked) {
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%><%=subSez2%>.value=="-")
			    {
			      alert('Il tipo misura è obbligatorio');
			      return false;
			    }	
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%><%=subSez2%>.value=="-")
				{
				  alert('Autorità Emittente è obbligatoria');
				  return false;
				}	
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo<%=subSez2%>.value=="-")
				{
				  alert('Il Tipo Ufficio PM  è obbligatorio');
				  return false;
				}	
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%><%=subSez2%>.value=="-" ||
						document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%><%=subSez2%>.value=="" )
				{
				   alert('La Sede  è obbligatoria');
				   return false;
				}
				var DataEmissioneOrdinanza = document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%><%=subSez2%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%><%=subSez2%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA%><%=subSez2%>.value;
				if(!ControllaDataPassaVuota(DataEmissioneOrdinanza)){
			      alert('Data Emissione Ordinanza non valida');
			      return false;
			    }
			} else if (radioTipoAltraCausa[2].checked) {
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_COD_TIPO_MISURA%><%=subSez3%>.value=="-")
			    {
			      alert('Il tipo misura è obbligatorio');
			      return false;
			    }	
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_AUTORITA_EMITTENTE%><%=subSez3%>.value=="-")
				{
				  alert('Autorità Emittente è obbligatoria');
				  return false;
				}	
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%>_Tipo<%=subSez3%>.value=="-")
				{
				  alert('Il Tipo Ufficio PM  è obbligatorio');
				  return false;
				}	
				if( document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%><%=subSez3%>.value=="-" ||
						document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_CODICE_UFFICIO_PM_SEDE%><%=subSez3%>.value=="" )
				{
				   alert('La Sede  è obbligatoria');
				   return false;
				}	
				var DataEmissioneOrdinanza = document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA%><%=subSez3%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_MESE_DATA_EMISSIONE_ORDINANZA%><%=subSez3%>.value+'/'+document.LoadInserisciPosizioneGiuridicaS1.<%=ICostantiMisuraCautelare.CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA%><%=subSez3%>.value;
				if(!ControllaDataPassaVuota(DataEmissioneOrdinanza)){
			      alert('Data Emissione Ordinanza non valida');
			      return false;
			    }
			}
		}
	}

}


</script>

<%@ include file="/jsp/files/siap/siep/posizione/LoadInserisciPosizioneGiuridica-ValScript.jsp" %>

</body>
</html>