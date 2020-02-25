<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="ufficioSorv"          	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 			   	scope="request" class="java.lang.String"/>

<jsp:useBean id="ListaRichiestePM"   	scope="request" class="java.util.Vector"/>

<!-- 			LoadEmettiRichiesteDelPMallaSORV				 -->
<%
//==============================================================================
//  Form con le funzioni di gestione dei Dati Analitici del Singolo Titolo
//==============================================================================

int TotRic = ListaRichiestePM.size();

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
  	
	//Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
  	function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
	
	// Apre la finestra con Lista Magistrati
	function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
  
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste al GE
    //==========================================================================
    function tornaIndietro(action)
    {
      document.LoadEmissRicPM.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadEmissRicPM.submit();
    }
    
    function Verify() 
    { 
   	 	// controllo che siano presenti delle Richieste da Inviare
   	 	var total = <%=TotRic%>;
   	 	if(total == 0)
   	 	{
   	 		// Non ci Sono Richieste da Inviare
   	 		var msgConfirm = "Attenzione: Per l'Istruttoria corrente non vi sono Richieste da Inviare! "; 
            if (window.confirm(msgConfirm)) 
            {
	   	      	 lAzione = "siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV";
	   	         document.LoadEmissRicPM.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
	   	         document.LoadEmissRicPM.submit();
            }
            return false;
   	 	}

   	 	// Controllo su selezione checkBox Richieste

		//Controllo che sia selezionata almeno una Richiesta
    	if (typeof (document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_SELEZIONATE %>[0]) =="undefined" )
    	//if(total < 2 ) 	
		{	
    		// 1 solo oggetto Richiesta  presente in maschera.
    		if(!document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_SELEZIONATE %>.checked )
    		{	
      			alert(" Attenzione selezionare la Richiesta ");
	     		return false;
    		}	
		}
    	else
    	{	
    		// n oggetti Richiesta presenti in maschera
    		var Spunta="NO";
	 	    for (var j = 0; j < document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_SELEZIONATE %>.length; j++ )
	 		{
	 	    	if(document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_SELEZIONATE %>[j].checked )
	 		   	{	
	 	    		Spunta="SI";
	 		   	}
	 		}
	 	    
	 	    if(Spunta=="NO")
	 	    {
	 	    	alert("Attenzione selezionare almeno una Richiesta ");
	       		return false;
	 	    }
    	}	    
 	    
 	   // Data Emissione
       if (document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
         	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
    	// Data Trasmissione
       if (document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value.length==1)
         	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value='0'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value;
       if (document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_TRASMISSIONE%>.value.length==1)
         	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_TRASMISSIONE%>.value='0'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_TRASMISSIONE%>.value;

       var data_to_verify = document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE%>.value+'/'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_TRASMISSIONE%>.value+'/'+document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_TRASMISSIONE%>.value;
       
       if (data_to_verify=='//' )
       {
      	 	alert('Data Trasmissione Obbligatoria');
          	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE%>.focus();
          	return false;
       }
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Trasmissione non valida');
           document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE%>.focus();
           return false;
       }
       
       // Ufficio Destinazione
       if(document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>.value == '-' && 
      	 document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_SEDE_UFFICIO_DEST%>.value != ''	 )
       {
      	 	alert('Ufficio Esecuzione non valido');
          	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>.focus();
          	return false;
       } 
       
       if(document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>.value != "-" && 
      	 document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_SEDE_UFFICIO_DEST%>.value == ""	 )
       {
      	 	alert('Sede Ufficio Esecuzione non valida');
          	document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_SEDE_UFFICIO_DEST%>.focus();
          	return false;
       }
       
       if(document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>.value == '-' && 
       	 document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_SEDE_UFFICIO_DEST%>.value == ''	 )
       {
			alert('digitare tipo Ufficio e Sede Ufficio Esecuzione ');
            document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>.focus();
            return false;
       }

       return true; 
    }
    
  </script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Stampa Richieste del PM alla Sorveglianza&nbsp;</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste alla Sorveglianza -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadEmissRicPM">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActEmettiRichiesteDelPMallaSORV">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  
   <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
   <input type="hidden" name="modalita" value="<%=modalita%>" >

  <table cellpadding="2" cellspacing="2" width="98%" align="center" style="border:0;">
    <tr>
      <td class="Titolo" colspan="100%">Richieste ancora da inviare</td>
    </tr>
  <%
  //============================================================================
  //  LISTA DELLE RICHIESTE GIA' A SISTEMA ANCORA DA INVIARE
  //============================================================================
  %>
  <tr>
    <td colspan="100%" align="center" class="l">
    
      <table cellspacing="2" cellpadding="2" align="center" width="95%">
        <tr style="display:block" id="richiesta_1">
          <td colspan="100%" align="center">
            <table cellspacing="2" cellpadding="2" align="center" width="95%">
              <tr>
            	<%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
            	<td class="int">Seleziona</td>
            	<td class="int">Data Richiesta</td>
            	<td class="int">Tipo Richiesta</td>	<%// + Tipo Beneficio %>
            	<td class="int">DpR</td>
            	<td class="int" title="Titolo per il quale è stata effettuata la richiesta">Titoli</td>
            	<td class="int">Anticipazione</td> 	<% //-- Indica se la richiesta è con anticipazione %>
            	<td class="int" title="Indica se già inviata">Stato</td>
          	  </tr>
<%
 	int id_record = 0;
 	String lAnticipazione="";
 	String lStatoRich = "Da Inviare";
 	String lSentenza = "";
	Iterator itx = ListaRichiestePM.iterator();
 	while ( itx.hasNext()) 
 	{
 		RichiestePmInCumuloModel lRichiestaSORV = (RichiestePmInCumuloModel)itx.next();
 		
 		// Anticipazione
 		if("A".equals(lRichiestaSORV.getFlagAppProvvisoria()) )
 			lAnticipazione="Si";
 		else if("R".equals(lRichiestaSORV.getFlagAppProvvisoria()) )
 			lAnticipazione="No";
 		else
 			lAnticipazione="-";
 		
 		// stato
 		if(lRichiestaSORV.getRicIdRichiesteInviateCum() != null)
 			lStatoRich = "Inviata";
 		
 		// Titolo
		lSentenza = "";
 	 	if(lRichiestaSORV.getAnnoSentenza()!=null && lRichiestaSORV.getNumeroSentenza()!=null)
 	 	{
 	 		lSentenza = lRichiestaSORV.getAnnoSentenza()+"/"+lRichiestaSORV.getNumeroSentenza();
 	 			
 	 		if( !("null").equals(lRichiestaSORV.getAltri()) && 
 	 			!"".equals(lRichiestaSORV.getAltri()) &&
 	 			!"0".equals(lRichiestaSORV.getAltri()) )
 	 		{
 	 			lSentenza += " + "+lRichiestaSORV.getAltri();
 	 		}
 	 	}
 %> 
  		<tr>
		<%// Inserire qui le get dei campi da visualizzare %>
          <td class="c" style="text-align:center">
            <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_SELEZIONATE%>" value="<%=StringUtils.toStringJSP(lRichiestaSORV.getIdRichiestePmInCumulo(), "") %>" >
          </td>
          
          <td class="c" nowrap ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRichiestaSORV.getDataEmissione(), "dd-MM-yyyy"), " - ") %></td>      
          <td class="c"><%=StringUtils.toStringJSP(lRichiestaSORV.getDescrTipoAnnotazione(),"")%></td>
 		  <td class="c"><%=StringUtils.toStringJSP(lRichiestaSORV.getDescrDpr(), "") %></td>
		  <td class="c" style="text-align:left">&nbsp;Sentenza N. <%=StringUtils.toStringJSP(lSentenza)%></td>
          <td class="c">&nbsp;<%=StringUtils.toStringJSP(lAnticipazione)%></td>    
		  <td class="c"><%=lStatoRich%></td>   
         </tr>
              <!-- -->
              <!--  tr style="display:none" id="sent_1">
                <td class="l" colspan="100%">
                  <font class="campoSmall">[Dettaglio Sentenza]</font>
                </td>
              </tr -->

<%	} %>             
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>


  <%
  //========================================================================
  //         INSERIRE QUI I DATI DELLA RICHIESTA
  //========================================================================
  %>
<tr>
  <td colspan="100%" align="center">
    <table width="100%">
      <tr>
        <td class="Titolo" colspan="8"> Dati Della Richiesta </td>
      </tr>
      <tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_TRASMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_TRASMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_TRASMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
    
    <table width="100%">
      <tr>
        <td class="l">Contenuto</td>
        <td  class="L" colspan="3">
          <TEXTAREA title="Contenuto" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CONTENUTO%>" cols="100" rows="2" ></textarea>
        </td>
      </tr>
    </table>

    <!-- MAGISTRATO -->
    <table width="100%">
      <tr>
        <td class="Titolo" width="100%" colspan="6"> Magistrato Firmatario </td>
      </tr>
      <tr>
        <td class="l">Magistrato Firmatario
        <td class="L">
          <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
          <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
          <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
          <a href="Javascript:ListaMagistrati('LoadEmissRicPM','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr>
        <td class="Titolo" width="100%" colspan=6> Destinatari</td>
      </tr>
    </table>

    <!-- GIUDICE DELL'ESECUZIONE -->
    <table width="100%">
      <tr>
        <td class="l" width="25%">Ufficio della Sorveglianza</td>
        <td class="L">
          <select  Title="Ufficio della Sorveglianza"  name="<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>">
           <%=ufficioSorv%>
          </select>
        </td>
        <td class="l">Sede</td>
        <td class="L">
          <input title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_SEDE_UFFICIO_DEST%>"  maxlength="35" size="35">
          <a href="Javascript:ListaUfficiComuni('LoadEmissRicPM','<%=ICostantiRichiestePmInCumulo.CAMPO_SEDE_UFFICIO_DEST%>',document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>[document.LoadEmissRicPM.<%=ICostantiRichiestePmInCumulo.CAMPO_UFFICIO_DEST%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
    </table>
  </td>
</tr>

</table>
<br>
 <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="invia" value="Invia Richieste">
      </td>
    </tr>
  </table>
  
</form>

</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadEmissRicPM");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>


