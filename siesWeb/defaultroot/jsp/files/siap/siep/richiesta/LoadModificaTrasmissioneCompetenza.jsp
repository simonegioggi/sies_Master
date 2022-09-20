<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.competenza.action.ICostantiCompetenza"%>

<jsp:useBean id="posizioneluogoaltra"  	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEmi"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioPM"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"        	scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="richiesta"            	scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo"               	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoprovvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="sentCompetenza"	scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="fascCompetenza"	scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="eventonotifica" scope="request" class="siap.sico.evento.model.EventoNotificaModel" />
<jsp:useBean id="competenza" scope="request" class="siap.siep.competenza.model.CompetenzaModel" />
<jsp:useBean id="autoritaEsternaN"      scope="request" class="java.lang.String"/>
<%
	//setto il flag per la verifica dell'inserimento manuale
	boolean insManuale = false;
	//if (insertManuale.equalsIgnoreCase("1"))insManuale = true;	
	if(competenza.getSenIdSentenza()==null) insManuale = true;
	//====================
		
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>



<html>
<head>
<title>[S.I.E.S.] - <%=titolo.toUpperCase()%></title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">

  
	function clearDropDown (selField){
		while (selField.options.length > 0)
		selField.options[0] = null;
	}

	function Verifica(){
		//CONTROLLO CHE SIA STATO SELEZIONATA LA TIPOLOGIA DELL'ATTO
		if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.value=='-'){
			alert("E' obbligatorio selezionare la tipologia dell'atto");
	      	return false;		
		}

    	if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
      		document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'
      		+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
    	if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
      		document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'
      		+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

    	var data_to_verify = document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

	    if (!ControllaData(data_to_verify) ){
			alert('Data di Trasmissione non valida');
			return false;
	    }
	    
	    <%if(insManuale){%>
		    if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO%>.value.length==1)
	      		document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO%>.value='0'
	      		+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO%>.value;
	    	if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO%>.value.length==1)
	      		document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO%>.value='0'
	      		+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO%>.value;
	
	    	var data_to_verify = document.LoadInserisciTrasmissioneCompetenza.
	    	<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO%>.value
	    	+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO%>.value
	    	+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_DATA_INSERIMENTO%>.value;
	
		    if (!ControllaData(data_to_verify) ){
				alert('Data del Provvedimento non valida');
				return false;
		    }
		    
		    if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
	      		document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'
	      		+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
	    	if (document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
	      		document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'
	      		+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;
	
	    	var data_to_verify = document.LoadInserisciTrasmissioneCompetenza.
	    	<%=ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value
	    	+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value
	    	+'-'+document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
	
		    if (!ControllaData(data_to_verify) ){
				alert('Il campo Definitivo in Data contiene un valore non valido');
				return false;
		    }		    
		    
		    if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>
				.options[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>
				.selectedIndex].text=='-')
			{
				alert("E' obbligatorio selezionare un valore del campo 'Tipo Provvedimento'");
		      	return false;		
			}
		    
		    if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>
		    	.options[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>
				.selectedIndex].text=='-')
			{
				alert("E' obbligatorio selezionare un valore del campo 'Pronunciata da'");
		      	return false;		
			}
			
			if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE%>
				.options[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE%>
				.selectedIndex].text=='-')
			{
				alert("E' obbligatorio selezionare un valore del campo 'Ufficio del Pubblico Ministero'");
		      	return false;		
			}
	    <%}%>
		
		if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>
			.options[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.selectedIndex]
			.text=='-'){
				alert("E' obbligatorio selezionare un valore del campo 'Oggetto Atto'");
				document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_MOTIVO%>.focus();
		      	return false;		
		}	    
	
		if(document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_NOME %>.value==""){
		    alert("Il  Magistrato Firmatario è obbligatorio");	
		    document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus()	;
		    return false;
		}	
	
	    if(document.LoadInserisciTrasmissioneCompetenza.camponote.value==""){
		    alert("Il  Contenuto è obbligatorio");
		    document.LoadInserisciTrasmissioneCompetenza.camponote.focus();
		    return false;
		}
		
		if(document.LoadInserisciTrasmissioneCompetenza.AltroDestinatario.value==""){
		    alert("Il  Campo Altro Destinatario è obbligatorio");
		    
		    return false;
		}
	}


	function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3){
		var desktop;
	  	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
	}
	
	function ListaComuni(a_formname,a_fieldname){
	  	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
	
	function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <font class="campo"><%=titolo%></font>


</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciTrasmissioneCompetenza">

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActModificaTrasmissioneCompetenza">
  <input type="HIDDEN" title="Id Pena Residua"  value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
  <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>">

	<table>
    	<tr>
	      	<td class="l">Posizione Giuridica </td>
	      	<td class="L" colspan=5>
	          	<font class="campo">
<%
	if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){
%>
              DETENUTO PER ALTRA CAUSA
<%    }
      else
      {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%     }%>
	         	</font>
	   	</td>
	</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
	<tr>
		  <td class="l">Detenuto presso </td>
		  <td class="L" colspan=5>
		  	<font class="campo">
		  	<%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
		    di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
		 </td>
	</tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
	<tr>
		<td class="l">Altro Luogo </td >
		<td class="L" colspan=5>
		  	<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
		</td>
	</tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
	<tr>
		 <td class="l">Detenuto presso </td>
		 <td class="L" colspan=5>
		  <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
		  di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
		  </td>
	</tr>
<%
        }%>

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
            </tr>
<%
          }
        }
%>


   <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
%>
</tr>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         </td>
<%
          }else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
                </td>
<%            }
        }
      }
}
%>


</tr>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(), "yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

  </tr>
</table>

<table width=90%>
   <tr>
      <td class="Titolo" colspan='8'>Titolo Che Determina La Competenza</td>
   </tr>
   <tr>
      <td class="l">Tipo Provvedimento</td>
        <td class="L" colspan="8">
        <%if(insManuale){%>
        	<select  name="<%=ICostantiCompetenza.CAMPO_COD_TIPO_PROVVEDIMENTO%>" Title="Tipo Provvedimento" >
            <%=tipoprovvedimento%>
            </select>
        <%}else{%>
        	<%=sentCompetenza.getDescrTipoProvvedimento()%>            
        	&nbsp;
         <%} %>   
        </td>
    </tr>
    <tr>		
		<%if(insManuale){%>
			<td class="l" width="25%">Data Provvedimento</td>
			<td class="L" colspan="3">
				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataProvvedimento(), "dd") )%>"   type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_GIORNO_DATA_INSERIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataProvvedimento(), "MM") )%>"   type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_MESE_DATA_INSERIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataProvvedimento(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiCompetenza.CAMPO_ANNO_DATA_INSERIMENTO%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</td>
			<td class="l">Definitivo in data</td>
			<td class="L" colspan="3">
				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataIrrevocabilita(), "dd") )%>"   type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataIrrevocabilita(), "MM") )%>"   type="text" size="2" maxlength="2" name="<%= ICostantiCompetenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
				<input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(competenza.getDataIrrevocabilita(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiCompetenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			</td>
		<%}else{ %>
			<td class="l" width="25%">Data Provvedimento</td>
			<td class="L" colspan="3">
				<%=DateUtils.getDateToString(sentCompetenza.getDataProvvedimento(), "dd-MM-yyyy")%>
				&nbsp;
			</td>
			<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
			<%-- td class="l">Definitivo in data</td>
			//  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
			<td class="L" colspan="3">
				<%=DateUtils.getDateToString(sentCompetenza.getDataIrrevocabilita(), "dd-MM-yyyy")%>				
				&nbsp;
			</td--%>
		<%} %>
	</tr>
	<tr>
		<td class="l" width="25%">Pronunciata da</td>		
		<td class="L" colspan="8">
		<%if(insManuale){%>
		  <select  Title="Ufficio Emissione"  name="<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
			<%=autoritaEmi %>
		  </select>
		 <%}else{ %>
		 	<%=sentCompetenza.getDescrTipoAutoritaEmittente()%>&nbsp;
		<%}%>
		</td>
	</tr>
	<tr>
		<%if(insManuale){%>
			<td class="l">Luogo</td>		
			<td class="L" colspan="3">
				<input value="<%=competenza.getDescrLuogoEmittente()%>" title="Sede Ufficio Emissione"  type="text" name="<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA%>"  maxlength="35" size="30">
				<a href="Javascript:ListaUfficiComuni(
					'LoadInserisciTrasmissioneCompetenza',
					'<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO_SENTENZA%>',
					document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.options.selectedIndex].value);">
				   <img src="/images/filefolder.gif" border=0>
				</a>
			</td>
			<td class="l">Sezione</td>
			<td class="L" colspan="3">
				<input value="<%if(competenza.getNumSezioneAutoritaEmittente()!=null && (!competenza.getNumSezioneAutoritaEmittente().equalsIgnoreCase("null"))){%><%=competenza.getNumSezioneAutoritaEmittente()%><%}%>" 
				title="Sezione Ufficio Emissione"  type="text" name="<%=ICostantiCompetenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE%>"  maxlength="35" size="20">
			</td>	
		<%}else{ %>
			<td class="l">Luogo</td>		
			<td class="L" colspan="3">				
				<%=sentCompetenza.getDescrLuogoEmittente()%>&nbsp;
			</td>
			<td class="l">Sezione</td>
			<td class="L" colspan="3">
				<%if(sentCompetenza.getNumSezioneAutoritaEmittente()!=null && (!sentCompetenza.getNumSezioneAutoritaEmittente().equalsIgnoreCase("null"))){%>
					<%=sentCompetenza.getNumSezioneAutoritaEmittente()%>
				<%}%>&nbsp;
			</td>		
		<%}%>
	</tr>
</table>

<table width=90%>
	<tr>
      <td class="Titolo" colspan='8'>Ufficio Competente all'emissione del Provvedimento</td>
	</tr>
   	<tr>
	   	<td class="l">Ufficio del Pubblico Ministero</td>
	   	<td class="L" colspan="3">
		<%if(insManuale){%>
			<select title="Sede Ufficio Pubblico Ministero"  name="<%=ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE%>">
				<%=ufficioPM %>
			</select>			
		<%}else{ %>
			<%=fascCompetenza.getDescrTipoUfficio() %>&nbsp;
		<%} %>
		</td>
	</tr>
	<tr>	
		<td class="l">Luogo</td>
		<td class="L" colspan="3">
			<%if(insManuale){%>
			<input value="<%=competenza.getDescrLuogoAutoritaComp() %>" title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO%>"  maxlength="35" size="30">
			<a href="Javascript:ListaUfficiComuni('LoadInserisciTrasmissioneCompetenza','<%=ICostantiCompetenza.CAMPO_LUOGO_UFFICIO_TROVATO%>',document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE%>[document.LoadInserisciTrasmissioneCompetenza.<%=ICostantiCompetenza.CAMPO_COD_LUOGO_EMITTENTE%>.options.selectedIndex].value);">
			   <img src="/images/filefolder.gif" border=0>
			</a>
			<%}else{%>
				<%=fascCompetenza.getDescrComuneUfficio() %>&nbsp;
			<%} %>
		</td>
	</tr>
    <tr>
		<td class="l" width="25%">Relativa al Procedimento numero</td>
		<td class="L" colspan="7">
			<%if(insManuale){%>
				<input type="text" title="Anno" value="<%=StringUtils.toStringJSP(competenza.getChiaveAnno())%>" name="<%=ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        		/
        		<input type="text" value="<%=StringUtils.toStringJSP(competenza.getChiaveProgr())%>"title="Numero SIEP" name="<%=ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14" >
			<%}else{%>
				 <%=fascCompetenza.getChiaveAnno()%> / <%=fascCompetenza.getChiaveProgr()%>&nbsp;
			<%} %>
		</td>
	</tr>	
</table>

<table   width=90%>
   <tr>
      <td class="Titolo" colspan='8'> Dati Atto </td>
   </tr>
   <tr>
      <td class="l" width="25%">Tipologia Atto (*)</td>
        <td class="L" colspan="3">
            <select  name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" Title="Tipologia Atto" >
             <%=richiesta%>
             </select>
        </td>
    </tr>
    <tr>
      <td class="l">Oggetto Atto (*)</td>
        <td class="L" colspan="3">
            <select  Title="Oggetto Atto"  name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>">
            <%=oggetto%>
            </select>
        </td>
    </tr>
    <tr>
     <td class="l">Contenuto (*)</td>
           <td  class="L" colspan="3">
             <TEXTAREA title="Contenuto" name="camponote" cols=90 rows=5 ><%if(eventonotifica.getCampoNote()[0]!=null){%><%=eventonotifica.getCampoNote()[0].getDescr() %><%}%></textarea>
           </td>
    </tr>
 
	<tr>
   		<td class="l" width="15%">Magistrato Firmatario (*)</td>
  		<td class="L" colspan="7">
			<input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(eventonotifica.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
			<input readonly title="Cognome Magistrato" 
			value="<%=StringUtils.toStringJSP(eventonotifica.getMagistrato().getCognome() )%>" 
			type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
			<input readonly title= "Nome Magistrato"    
			value="<%=StringUtils.toStringJSP(eventonotifica.getMagistrato().getNome() )%>" 
			type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
			<a href="Javascript:ListaMagistrati('LoadInserisciTrasmissioneCompetenza','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
				<img src="/images/filefolder.gif" border=0>
			</a>					
		</td>
  </tr>
	<tr>		
		<td class="l">Altro Destinatario</td>
		<td class="L" colspan="7">		
			<select Title="Altro Destinatario" class="small" name="AltroDestinatario" >
			<%=autoritaEsternaN%>
             </select>
		</td>
	</tr>
	<tr>
		<td class="l">Sede</td>		
			<td class="L" colspan="3">
				<input title="Sede Altro Destinatario" 
				value="<%=eventonotifica.getNotifiche()[0].getAutoritaEsterna().getDescrSede() %>" 
				type="text" name="SedeAltroDestinatario" maxlength="35" size="35">
		        <a href="Javascript:ListaComuni('LoadInserisciTrasmissioneCompetenza','SedeAltroDestinatario');">
		          <img src="/images/filefolder.gif" border=0>
		        </a>
			</td>		
    <tr>
		
    <tr>
		<td class="lNoBord" colspan="2">
	      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verifica();">
	    </td>
	</tr>
</table>
<%if(!insManuale){%>
	<input type=hidden name="<%=ICostantiCompetenza.CAMPO_SENTENZA_SIEP_TROVATO%>" value="<%=sentCompetenza.getIdSentenza()%>">
	<input type=hidden name="<%=ICostantiCompetenza.CAMPO_FASCICOLO_SIEP_TROVATO%>" value="<%=fascCompetenza.getIdFascicoloSiep()%>">
<%}%>
	<input type=hidden name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=eventonotifica.getEvento().getIdEvento()%>">
	<input type=hidden name="<%=ICostantiCompetenza.CAMPO_ID_COMPETENZA%>" value="<%=competenza.getIdCompetenza()%>">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciTrasmissioneCompetenza");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
  
<%if(insManuale){%>
	frmvalidator.addValidation("<%= ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno del Procedimento è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiCompetenza.CAMPO_CHIAVE_ANNO%>","numeric");
  	
  	frmvalidator.addValidation("<%= ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>","req","Il campo Progressivo del Procedimento è obbligatorio");
  	frmvalidator.addValidation("<%= ICostantiCompetenza.CAMPO_CHIAVE_PROGR%>","numeric");		
<%}%>


</script>
</body>
</html>