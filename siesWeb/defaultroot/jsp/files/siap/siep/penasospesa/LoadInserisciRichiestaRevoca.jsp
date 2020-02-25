<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.ArrayList" %>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.reato.model.ReatoModel"%>

<%@ page import="f3b.web.html.Option"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoRevo"              scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"        scope="request" class="siap.sico.evento.model.EventoModel"/>

<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="autoritaSentenza"      scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaOrdinanza"     scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioge"            scope="request" class="java.lang.String"/>

<% 
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
siesLogger.debug("TipoRevo="+TipoRevo);
Collection oggetto =(Collection) request.getAttribute("oggetto");
String strOggetto ="";
Iterator itxOggetto = oggetto.iterator();
while(itxOggetto.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
  
   strOggetto += lDecMod.getFiltro() +";";
   strOggetto += lDecMod.getCode()+";";
   strOggetto += lDecMod.getDescription()+"#";
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//	siesLogger.debug("strOggett="+strOggetto);
}

  	SentenzaModel sentenza = fascicolo.getSentenza();
	String uffi_ge=new String();
	String luo_ge=new String();
	String sez_ge=new String();
  	if (sentenza.getDescrTipoAutoritaEmittente() != null){
		uffi_ge=sentenza.getDescrTipoAutoritaEmittente();
		luo_ge=sentenza.getDescrLuogoEmittente();
		sez_ge=sentenza.getNumSezioneAutoritaEmittente();
	}
%>
<html>
<head>
<title>[S.I.E.S.] - Richiesta Revoca Beneficio ex art.168 c.p. - 674 c.p.p. </title>
<base target="_self" />
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
//*******************************************//
function ListaComuni(a_formname,a_fieldname)
  {
  	var desktop;
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

//*******************************************//
function LeggiDestinatari()
  {
	var myselect=document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE%>;
	var myselect_sede=document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>;
	var myselect_sez=document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE%>;
	var titi="<%=uffi_ge%>";
	var titi2="<%=luo_ge%>";
	var titi3="<%=sez_ge%>";
	for (var i=0; i<myselect.length; i++){ //loop through all form elements
 		if (myselect.options[i].text==titi){
  			myselect.options[i].selected=i;
  			myselect_sede.value=titi2;
  			if (titi3!='null')
  				myselect_sez.value=titi3;
  			break
  		}
  	}
  }
//*******************************************//
function Verify()
  {
    var articolo_Revoca = document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>.value;
    //alert ('articolo_Revoca = '+articolo_Revoca);
	var data_sentenza = document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value+'/'+document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value+'/'+document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value;
	if (articolo_Revoca.length != 4 ) {
		alert('Selezionare un articolo di Revoca');		
	      return false;
	}
	// 18/04/2019 MEV70 - Solo se l'articolo non riguarda la 'Revoca Beneficio ex art. 165 c.p.p.' devo controllare la Data Sentenza  
	if (articolo_Revoca!='1108') {
		if (! ControllaData(data_sentenza) && data_sentenza.length==2)	
	    {
	      alert('Il campo Data Sentenza è obbligatorio');
	      return false;
	    }
		if (! ControllaData(data_sentenza) && data_sentenza.length>2)	
	    {
	      alert('Data Sentenza di Revoca non valida');
	      return false;
	    }
   		//alert('Data Sentenza di Revoca valida ed articolo != 1108. Si consente l'inserimento ');
	} else {
	     //alert ('articolo_Revoca = 1108. Non effettuo controllo della Data Sentenza per consentire l'inserimento');
	}
  }

//*******************************************//
function inizia()
	{
	caricatuttecombo();
	cambia();
	LeggiDestinatari();
	}
	
//*******************************************//
function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
  {
    // valueTextStr = stringa nel formato richiesto
    // sep1 = separatore interno alla coppia di valori
    // sep2 = separatore tra coppie
    // filtro = valore su cui fare il test
    // selField = oggetto combo da caricare

    clearDropDown(selField);
    var aPairs = valueTextStr.split(sep2);
    if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
    {
      aPairs[aPairs.length - 1] = null;
      aPairs.length--;
    }

    for (var i=0; i < aPairs.length; i++)
    {
      aValueText = aPairs[i].split(sep1);
      if (filtro=='null' || filtro==aValueText[0])
      {
    		oItem = new Option;
    		oItem.value = aValueText[1];
    		oItem.text = aValueText[2];
    		selField.options[selField.options.length] = oItem;
      }
    }
    selField.options.selectedIndex = 0;
  }

//*******************************************//
function clearDropDown (selField)
  {
  	while (selField.options.length > 0)
  		selField.options[0] = null;
  }

//*******************************************//
function caricatuttecombo()
  {
  	var strOggetto = "<%=strOggetto%>";
  	var art=document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").selectedIndex;
    caricaCombo(strOggetto,';','#',document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").options[art].text,document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_MOTIVO%>);
  }

//*******************************************//
function cambia() 
  {
  	var ind=document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>').selectedIndex;
    var cod= document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>').options[ind].text;

    //if (cod == "Ordinanza"){
    //  document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>').style.display="block";
    //  document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>').style.visibility="visible";
      
    //  document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>').style.display="none";
    //  document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>').style.visibility="hidden";
    //}else{
      document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>').style.display="block";
      document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>').style.visibility="visible";
      
      document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>').style.display="none";
      document.getElementById('<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>').style.visibility="hidden";
    //}
 }

//*******************************************//
function ListaProcedimenti(a_formname)
    {
    }
    
    
//*******************************************//
function ListaOrdinanzeSIGE(a_formname)
    {
    	var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActListaFascicoliDelSoggetto&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&Attivazione=RichiestaRevoca", "Lista_Procedimenti_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=900, height=500");
    }

//*******************************************//
function ricarica()
	{
		//window.location.reload(false);
	  //document.getElementById("iframereati").contentWindow.location.reload(true);
	  document.getElementById("iframereati").style.display="block";
	  document.getElementById("iframereati").contentWindow.location.reload(true);
	   
	}      

function pausa()
{
	var datafissa = new Date();
	var datamobile = datafissa;

	document.getElementsByTagName("body")[0].style.cursor="wait";
		do { 
			datamobile = new Date(); 
		}while(datamobile-datafissa < 5000);
				
	document.getElementsByTagName("body")[0].style.cursor="default";
}
//*******************************************//
function InserimentoReati(a_formname)
    {
    	var desktop;
        //desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActLoadInsReati&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&", "Lista_Reati_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=600,modal=yes");
		if (window.showModalDialog) {
			desktop=window.showModalDialog("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActLoadInsReati&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&",self,"dialogWidth:830px;dialogHeight:700px;center=no;status=no");
		} 
		else {
			desktop=window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActLoadInsReati&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&", "Reati_Associati", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=830, height=700,modal=yes");
		}
		var nreati=document.LoadInserisciRichiestaRevoca.nreati.value;
		var cabl=document.LoadInserisciRichiestaRevoca.cablati.value;
		var cabl2=document.LoadInserisciRichiestaRevoca.cablati2.value;
			
		if ((nreati>0) || (cabl!='') || (cabl2!='')){
			pausa();
        	//setTimeout("ricarica();", 1000);
        	ricarica();
        }
    }
    
//*******************************************//
function ricaricaPenaComplessiva()
{
  document.getElementById("iframepcompl").style.display="block";
  document.getElementById("iframepcompl").contentWindow.location.reload(true);  
}      

//*******************************************//
function InserimentoPenaComplessiva(a_formname)
    {
    	var desktop;
		if (window.showModalDialog) {
			desktop=window.showModalDialog("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActLoadInsPenaCompl&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&",self,"dialogWidth:800px;dialogHeight:500px;center=no;status=no");
		} 
		else {
			desktop=window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActLoadInsPenaCompl&formname="+a_formname+"&IdSoggetto=<%=fascicolo.getSoggetto().getIdSoggetto()%>&", "Pena_Complessiva", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=600,modal=yes");
		}
		var penacompl=document.LoadInserisciRichiestaRevoca.pcompl.value;
		if (penacompl>0)
	        setTimeout("ricaricaPenaComplessiva();", 3000);
    }
</script>

</head>

  <body class="corpo" onLoad="javascript:inizia();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
		EventoModel lEvento = new EventoModel();
		String lAzione = new String();
		if( modalita.equals("I") ){
        	lAzione = "siap.siep.penasospesa.action.ActInserisciRichiestaRevoca";
%>
			<font class="campo">Inserimento Richiesta Revoca Beneficio ex art.168 c.p. - 674 c.p.p.</font>
<%
       }
       	else if( modalita.equals("M") )
       {
			lAzione = "siap.siep.penasospesa.action.ActModificaAnnotazioneRevoca";
%>
         	<font class="campo">Modifica Richiesta Revoca Beneficio ex art.168 c.p. - 674 c.p.p.</font>
<%
       }
%>
      </td>
  	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaRevoca" >
  	<table width='95%' cellspacing=2 cellpadding=2>
	    <tr><td class="Titolo" colspan=4>Titolo che determina la Revoca</td></tr>
		<tr>
      		<td  width='25%' class="l">Tipo di Provvedimento<font class=ob>(*)</font></td>
      		<td class="l">
        		<select name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onchange="JavaScript:cambia();">
		          <option selected>Sentenza</option>
		          <!--   option>Ordinanza</option>         --> 
		        </select>
	      </td>
		</tr>
	</table>
  
 <!-- DIV del TIPO ORDINANZA  -->
 <div id="DivOrdinanza" class="label" style="visibility:none; position:relative; " >
  <table width="95%">
	  		<tr>
        		<td class="l">
        		<a href="Javascript:ListaOrdinanzeSIGE('LoadInserisciRichiestaRevoca');">
        		Seleziona il provvedimento dalla lista&nbsp;<img src="/images/filefolder.gif" border=0>
       			</a>
        		</td>
      		</tr>
	</table>   
  <table width="95%">
    <tr>
	      	<td class="l">Anno/Numero Provvedimento<font class=ob>(*)</font></td>
	      	
	      	<td class="l">
	        	<input Title="Anno Provvedimento" value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>" maxlength="4" size="4">
	        	/
	        	<input Title="Numero Provvedimento" value="" type="text" name="<%=ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA%>" maxlength="6" size="6">
	      	</td>
	      	<td class="l">Data Provvedimento<font class=ob>(*)</font></td>
    	  	<td class="l">
	        	<input Title="Giorno Emissione Provvedimento" 
	        		name="<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA %>"
	        		type="text" 
	        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"dd"))%>" 
	        		maxlength="2" size="2" 
	        		onFocus="javascript:textboxSelect(this)" 
	        		onkeypress="return TicTabNumField(this,event)" 
	        		onBlur="javascript:value=FillDM(value)">
	        	-
	        	<input Title="Mese Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"MM")) %>" name="<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        	-
	        	<input Title="Anno Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"yyyy")) %>" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      		</td>
	      	<td class="l">Data Irrevocabilità</td>
    	  	<td class="l">
	        	<input Title="Giorno Emissione Provvedimento" 
	        		name="<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA %>"
	        		type="text" 
	        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"dd"))%>" 
	        		maxlength="2" size="2" 
	        		onFocus="javascript:textboxSelect(this)" 
	        		onkeypress="return TicTabNumField(this,event)" 
	        		onBlur="javascript:value=FillDM(value)">
	        	-
	        	<input Title="Mese Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"MM")) %>" name="<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	        	-
	        	<input Title="Anno Emissione Provvedimento" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lEvento.getDataRichiesta(),"yyyy")) %>" name="<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      		</td>
      	</tr>
    	<tr>
	      	<td class="l">Autorità Emit.</td>
	        <td class="l" colspan="5">
	          <select style="display:block;" Title="Autorità Sentenza" name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>">
	            <%=autoritaSentenza%>
	          </select>
	          <select style="display:none;" Title="Autorità Ordinanza" name="<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO%>">
	            <%=autoritaOrdinanza%>
	          </select>
	        </td>
      	</tr>
    	<tr>
      		<td class="l">Luogo</td>
	      	<td class="l" colspan="2">
	        	<input Title="Luogo Autorità Emittente" name="<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>" value="" type="text" maxlength="35" size="35">
	        	<a href="Javascript:ListaComuni('LoadInserisciRichiestaRevoca','<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>');">
	          	<img src="/images/filefolder.gif" border=0>
	        	</a>
	      	</td>
      		<td class="l">Sez.</td>
	      	<td class="l" colspan="2">
	        	<input Title="Sezione" name="<%=ICostantiPenaSospesa.CAMPO_SEZIONE_SENTENZA_REVOCA%>" value="" type="text" maxlength="35" size="35">
	      	</td>
    	</tr>
  </table>  	
<table width="95%">
	  		<tr>
        		<td class="l" id="linkReati" >
        		<a href="Javascript:InserimentoReati('LoadInserisciRichiestaRevoca');">
        		Iscrizione Reati</a>
        		</td>
      		</tr>
</table>   
<div id="DivReati">
	<iframe src="<%=IWebConstants.ROOT_DIR%>files/siap/siep/penasospesa/IncludeReato.jsp" 
	id="iframereati" width="95%" height="80" style="display: none">
	</iframe>
</div>

<!-- tabella dei reati del procedimento selezionato nel distretto -->
<div id="ReatiFasSel" style="display:none; visibility:hidden; position:relative; ">
	<table width="95%" id="tblReati">
	  		<tr>
        		<td class="l" colspan="12" >Reati</td>
      		</tr>
		    <tr>
		      <td class="int" >N. Reato</td>
		      <td class="int" >Data Reato</td>
		      <td class="int">Fonte</td>
		      <td class="int">Anno</td>
		      <td class="int">Numero</td>
		      <td class="int">Articolo</td>
		      <td class="int">Art. Qual.</td>
		      <td class="int">Comma</td>
		      <td class="int">Comma Qual.</td>
		      <td class="int">Lettera</td>
		      <td class="int">Numero</td>
		      <td class="int">Note</td>
		    </tr>
	</table>
</div>   
<!-- fine tabella -->

<table width="95%">
	  		<tr>
        		<td class="l" id="linkPena" >
        		<a href="Javascript:InserimentoPenaComplessiva('LoadInserisciRichiestaRevoca');">
        		Pena Complessiva</a>
        		</td>
      		</tr>
</table> 
  
<div id="DivPenaCompl">
	<iframe src="<%=IWebConstants.ROOT_DIR%>files/siap/siep/penasospesa/IncludePenaComplessiva.jsp" 
	id="iframepcompl" width="95%" height="80" style="display: none">
	</iframe>
</div>
<!-- tabella della pena complessiva del procedimento selezionato nel distretto -->
<div id="PComplFasSel" style="display:none; visibility:hidden; position:relative; ">
	<table width="95%" id="tblPena">
	  		<tr>
        		<td class="l" colspan="12" >Pena Complessiva</td>
      		</tr>
	</table>
</div>   
<!-- fine tabella -->

  <table width="95%">
	    <tr><td class="Titolo" colspan=4>Revoca</td></tr>
		<tr>
	      	<td class="l">Articolo <font class=ob>(*)</font></td>
	      	<td class="l">
             		<select name="<%= ICostantiPenaSospesa.CAMPO_COD_ARTICOLO %>" Title="Tipo Provvedimento" onchange="javascript:caricatuttecombo()">
					<option value ="">-</option> 
<%
Iterator itxArticolo = oggetto.iterator();
while(itxArticolo.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxArticolo.next();
   if((lDecMod.getCode()).equals("1101") || (lDecMod.getCode()).equals("1101")||
		   (lDecMod.getCode()).equals("1102")|| (lDecMod.getCode()).equals("1103")
		   || (lDecMod.getCode()).equals("1104")|| (lDecMod.getCode()).equals("1105")
		   || (lDecMod.getCode()).equals("1106")|| (lDecMod.getCode()).equals("1107")
		   || (lDecMod.getCode()).equals("1108")){
	   	if((lDecMod.getCode()).equals(lDecMod.getCodiceAlt2())){
	   		if ((lDecMod.getCode()).equals("1108")&& TipoRevo.equals("noObblighi")){
%>
					<option value = <%=lDecMod.getCode()%> selected><%=lDecMod.getFiltro() %></option> 
<%  
	   		}else{
%>
					<option value = <%=lDecMod.getCode()%> ><%=lDecMod.getFiltro() %></option> 
	   			
	   		<%}
	   	}
   }
}

%>
             		</select>
	      	</td>
		</tr>
		<tr>
	      	<td class="l">Motivazione</td>
	      	<td class="l">
             		<select class="small"  name="<%= ICostantiPenaSospesa.CAMPO_COD_MOTIVO %>" Title="Tipo Provvedimento" >
     				
             		</select>
	      	</td>
		</tr>
		<tr>
	      	<td class="l">Note</td>
	      	<td class="l">
	        	<Textarea Title="Note" name="<%= ICostantiPenaSospesa.CAMPO_NOTE %>" cols=120 rows=2></textarea>
	      	</td>
		</tr>

 </table>

<table  width="100%">
 <tr>
     <td class="Titolo"  colspan="6"> Destinatari</td>
   </tr>
  <tr>
          <td class="l">Ufficio Giudice dell'Esecuzione</td>
          <td class="L">
            <select  Title="Ufficio Giudice Esecuzione"  name="<%=ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE%>">
             <%=ufficioge%>
            </select>
          </td>
          <td class="l">Sede</td>
          <td class="L">
          <input title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaUfficiComuni('LoadInserisciRichiestaRevoca','<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>',document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE%>[document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_COD_UFFICIO_GE%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
         </td>
      		<td class="l">Sez.</td>
	      	<td class="l">
	        	<input Title="Sezione" name="<%=ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE%>" value="" type="text" maxlength="35" size="35">
	      	</td>
  </tr>





    	<tr>
      	<td>
        	<input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      	</td>
    	</tr>
  </table>

  </div>
  
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  <input type="HIDDEN" name="flagDaLista" value="">
  <input type="HIDDEN" name="TipoPro" value="Richiesta">
  <input type="HIDDEN" name="nreati" value="0">
  <input value="" type="Hidden" name="cablati">
  <input value="" type="Hidden" name="cablati2">
  <input type="HIDDEN" name="pcompl" value="">
  <input type="HIDDEN" name="idFas" value="">

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaRevoca");
<%--
	var data_sentenza=document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value+'/'+document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value+'/'+document.LoadInserisciRichiestaRevoca.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value;
  	if (data_sentenza.length<10){
		frmvalidator.addValidation("<%= ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>","req", "Il campo Data Sentenza è obbligatorio");
	}
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>","numeric");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>","gt=1900");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>","lt=3000");
	frmvalidator.addValidation("<%=ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA%>","numeric");
--%>	
	frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>