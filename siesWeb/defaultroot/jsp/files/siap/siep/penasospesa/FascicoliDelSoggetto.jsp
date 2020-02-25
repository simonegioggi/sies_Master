<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>

<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="IdFascCorrente" scope="request" class="java.lang.String" />
<jsp:useBean id="StrCodiceDistrettoUtente" scope="request" class="java.lang.String" />
<jsp:useBean id="ufficiAltroDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="ufficiDistretto" scope="request" class="java.util.Vector" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="StrNomeSoggettoAlias" scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="strTipoRicerca" scope="request" class="java.lang.String" />

<jsp:useBean id="allreati" scope="request" class="java.util.Vector" />
<jsp:useBean id="allpenacompl" scope="request" class="java.util.Vector" />
<%
  String lCodTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
  boolean isSIUS = (lCodTipoUfficio.compareToIgnoreCase("TDS") == 0 || lCodTipoUfficio.compareToIgnoreCase("UDS") == 0) ? true : false;

  Iterator itxControlla = fascicoli.iterator();
  String lprocAltreBDI = "N";
  String lproc = "S";

    while ( itxControlla.hasNext())
    {
         FascicoloSiepModel Contollafascicolo = (FascicoloSiepModel)itxControlla.next();
         if(!Contollafascicolo.getCodDistretto().equals(StrCodiceDistrettoUtente))
         {
           lprocAltreBDI="S";
         }
          else
         {
           lproc="N";

         }
    }
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimento</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript">

// Elenco dei reati per Fascicolo
var ElencoReati = new Array();

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !fascicoli.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");
          window.parent.close();
        }
      }



function addRowToTable(num)
{
	// var titi=listareati;
	var titi = ElencoReati[num];
	var tbl = window.parent.opener.document.getElementById('tblReati');
	// if there's no header row in the table, then iteration = lastRow + 1
	
	while(tbl.rows.length>2){
		var righe=tbl.rows.length;
		//alert('righe='+righe);
		tbl.deleteRow(righe-1);
	}
		
	while (titi.length>1) {
		var lastRow = tbl.rows.length;
		var iteration = lastRow;
		var row = tbl.insertRow(lastRow);
		var penadet=titi.substring(0,titi.indexOf(';'));
		//alert ('penadet='+penadet);
		
		titi=titi.substring(titi.indexOf(';')+1);
		var progr=titi.substring(0,titi.indexOf(';'));	
		//alert("progr="+progr);
	 	var newCell = row.insertCell(0);
	  	newCell.setAttribute('className','l');
	  	newCell.setAttribute('align','center');
		newCell.innerHTML = progr;
	
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell1 = row.insertCell(1);
	  	newCell1.setAttribute('align','center');
	  	newCell1.setAttribute('className','l');
		newCell1.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//fonte
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell2 = row.insertCell(2);
	  	newCell2.setAttribute('align','center');
	  	newCell2.setAttribute('className','l');
		newCell2.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//anno
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell3 = row.insertCell(3);
	  	newCell3.setAttribute('align','center');
	  	newCell3.setAttribute('className','l');
		newCell3.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//numero
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell4 = row.insertCell(4);
	  	newCell4.setAttribute('align','center');
	  	newCell4.setAttribute('className','l');
		newCell4.innerHTML = titi.substring(0,titi.indexOf(';'));
	
	
		//articolo
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell5 = row.insertCell(5);
	  	newCell5.setAttribute('align','center');
	  	newCell5.setAttribute('className','l');
		newCell5.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//art.qual
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell6 = row.insertCell(6);
	  	newCell6.setAttribute('align','center');
	  	newCell6.setAttribute('className','l');
		newCell6.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//comma
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell7 = row.insertCell(7);
	  	newCell7.setAttribute('align','center');
	  	newCell7.setAttribute('className','l');
		newCell7.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//comma qual.
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell8 = row.insertCell(8);
	  	newCell8.setAttribute('align','center');
	  	newCell8.setAttribute('className','l');
		newCell8.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//lettera
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell9 = row.insertCell(9);
	  	newCell9.setAttribute('align','center');
	  	newCell9.setAttribute('className','l');
		newCell9.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//numero
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi="+titi);
	 	var newCell10 = row.insertCell(10);
	  	newCell10.setAttribute('align','center');
	  	newCell10.setAttribute('className','l');
		newCell10.innerHTML = titi.substring(0,titi.indexOf(';'));
	
		//note
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi NOTE="+titi);
	 	var newCell11 = row.insertCell(11);
	  	newCell11.setAttribute('align','center');
	  	newCell11.setAttribute('className','l');
	  	if(titi.substring(0,titi.indexOf(';'))!='$')
			newCell11.innerHTML = titi.substring(0,titi.indexOf(';'));
		else
			newCell11.innerHTML ='-';
	  	if(titi.length==1)
			newCell11.innerHTML = '-';
	
		titi=titi.substring(titi.indexOf(';')+1);
		//alert("resto titi NOTE="+titi);
	
	}	
}




//*******************************************//
function insertReati(num)
	{
      	formname = '<%=request.getParameter("formname")%>';
      	// addRowToTable(ElencoReati[num]);
		addRowToTable(num);
		var tbl = window.parent.opener.document.getElementById('tblReati');
		
		if(tbl.rows.length>2){
		    window.parent.opener.document.getElementById('ReatiFasSel').style.display="block";
		    window.parent.opener.document.getElementById('ReatiFasSel').style.visibility="visible";
	    }
	}      

//*******************************************//
function insertPena(datipenacompl)
	{
      	formname = '<%=request.getParameter("formname")%>';
		var tbl = window.parent.opener.document.getElementById('tblPena');
		var titi=datipenacompl;
		//alert ('PENA COMPLESSIVA'+titi);
		
		while(tbl.rows.length>1){
			var righe=tbl.rows.length;
			//alert('righe='+righe);
			tbl.deleteRow(righe-1);
		}
		if(titi==';;;;;;;;;') return;
		if(titi=='') return;
			
		var lastRow = tbl.rows.length;
		var iteration = lastRow;
		var row = tbl.insertRow(lastRow);
		
		var progr=titi.substring(0,titi.indexOf(';'));	
	 	var newCell = row.insertCell(0);
	  	newCell.setAttribute('className','campo');
	  	//newCell.setAttribute('align','center');
	  	var testo='Reclusione:'
	  	if( (progr.substring(0,progr.indexOf('-')).length >0) && 
	  		(progr.substring(0,progr.indexOf('-'))!='null'))
	  		testo=testo+' Anni '+progr.substring(0,progr.indexOf('-'));
	  		
	  	if((progr.substring(progr.indexOf('-')+1,progr.lastIndexOf('-')).length>0) &&
	  		(progr.substring(progr.indexOf('-')+1,progr.lastIndexOf('-')) !='null') )
			testo=testo+' Mesi '+progr.substring(progr.indexOf('-')+1,progr.lastIndexOf('-'));
			
		if((progr.substring(progr.lastIndexOf('-')+1).length>0) &&
			(progr.substring(progr.lastIndexOf('-')+1)!='null'))	
		 	testo=testo+' Giorni '+progr.substring(progr.lastIndexOf('-')+1);
		if(testo!='Reclusione:')
			newCell.innerHTML = testo;
	
		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell1 = row.insertCell(1);
	  	newCell1.setAttribute('className','campo');
	  	newCell1.setAttribute('align','center');
	  	var valmulta=titi.substring(0,titi.indexOf(';'));
	  	if (valmulta>0)
			newCell1.innerHTML = 'Multa: '+valmulta+ ' Euro';
	
		//Seconda riga
		lastRow = tbl.rows.length;
		row = tbl.insertRow(lastRow);
		titi=titi.substring(titi.indexOf(';')+1);
		var arr=titi.substring(0,titi.indexOf(';'));
		//alert("arr="+arr);
	 	var newCell = row.insertCell(0);
	  	newCell.setAttribute('className','campo');
	  	newCell.setAttribute('align','center');
	  	
	  	testo='Arresto:';	  	
	  	if(arr.substring(0,arr.indexOf('-')).length>0 && arr.substring(0,arr.indexOf('-'))!='null')
	  		testo=testo+' Anni '+arr.substring(0,arr.indexOf('-'));
	  
	  	if( (arr.substring(arr.indexOf('-')+1,arr.lastIndexOf('-'))).length>0 &&
	  		(arr.substring(arr.indexOf('-')+1,arr.lastIndexOf('-')))!='null')
	  		testo=testo+' Mesi '+arr.substring(arr.indexOf('-')+1,arr.lastIndexOf('-'));
	  	
	  	if( (arr.substring(arr.lastIndexOf('-')+1)).length>0 &&
	  		(arr.substring(arr.lastIndexOf('-')+1))!='null')
	  		testo=testo+' Giorni '+arr.substring(arr.lastIndexOf('-')+1);
		if(testo!='Arresto:')
			newCell.innerHTML = testo;
	
		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell1 = row.insertCell(1);
	  	newCell1.setAttribute('className','campo');
	  	newCell1.setAttribute('align','center');
	  	if(titi.substring(0,titi.indexOf(';'))>0)
			newCell1.innerHTML = 'Ammenda: '+titi.substring(0,titi.indexOf(';'))+ ' Euro';
	
		//Terza riga
		lastRow = tbl.rows.length;
		row = tbl.insertRow(lastRow);
		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell = row.insertCell(0);
	  	newCell.setAttribute('className','campo');
	  	newCell.setAttribute('align','center');
	  	if(titi.indexOf(';')>0)
			newCell.innerHTML = 'Ergastolo: '+titi.substring(0,titi.indexOf(';'));		

		//quarta
		lastRow = tbl.rows.length;
		row = tbl.insertRow(lastRow);
		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell = row.insertCell(0);
	  	newCell.setAttribute('className','campo');
	  	newCell.setAttribute('align','center');
	  	if(titi.indexOf(';')>0)
			newCell.innerHTML = 'Data Inizio Isolamento Diurno: '+titi.substring(0,titi.indexOf(';'));
		
		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell1 = row.insertCell(1);
	  	newCell1.setAttribute('className','campo');
	  	newCell1.setAttribute('align','center');
	  	if(titi.indexOf(';')>0)
			newCell1.innerHTML = 'Data Fine Isolamento Diurno: '+titi.substring(0,titi.indexOf(';'));
		
		//quarta
		lastRow = tbl.rows.length;
		row = tbl.insertRow(lastRow);
		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell = row.insertCell(0);
	  	newCell.setAttribute('className','campo');
	  	newCell.setAttribute('align','center');
	  	if(titi.indexOf(';')>0)
			newCell.innerHTML = 'Durata Isolamento Diurno: '+titi.substring(0,titi.indexOf(';'));

		titi=titi.substring(titi.indexOf(';')+1);
	 	var newCell1 = row.insertCell(1);
	  	newCell1.setAttribute('className','campo');
	  	newCell1.setAttribute('align','center');
	  	if(titi.indexOf(';')>0)
			newCell1.innerHTML = 'Data Prescrizione: '+titi.substring(0,titi.indexOf(';'));

	    window.parent.opener.document.getElementById('PComplFasSel').style.display="block";
	    window.parent.opener.document.getElementById('PComplFasSel').style.visibility="visible";
	    
}      

//*******************************************//
      function insertIT( idfas,GiornoAtto,MeseAtto,AnnoAtto,
      					 GiornoSentenza,MeseSentenza,AnnoSentenza,
      					 AnnoSen,NumeroSen,
                         CodTipoAutoritaEmittente,CodLuogoEmittente,NumSezione,inElencoReati,datipenacompl)            						
      {
      	// alert ("insertIT");
      	
      	
      	formname = '<%=request.getParameter("formname")%>';
      	// ID Fascicolo selezionato
           window.parent.opener.document.<%=request.getParameter("formname")%>.idFas.value=idfas;
      	
       	//data irrevocabilità
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA%>.value=GiornoAtto;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA%>.value=MeseAtto;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA%>.value=AnnoAtto;

		//data provvedimento
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA%>.value=GiornoSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA%>.value=MeseSentenza;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA%>.value=AnnoSentenza;
        
        //provvedimento
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA%>.value=AnnoSen;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA%>.value=NumeroSen;

		//autorità
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO%>.value=CodTipoAutoritaEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA%>.value=CodLuogoEmittente;
        window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiPenaSospesa.CAMPO_SEZIONE_SENTENZA_REVOCA%>.value=NumSezione;

        window.parent.opener.document.<%=request.getParameter("formname")%>.flagDaLista.value='SI';
        
        
	    window.parent.opener.document.getElementById('linkReati').style.display="none";
	    window.parent.opener.document.getElementById('linkReati').style.visibility="hidden";
	
	    window.parent.opener.document.getElementById('linkPena').style.display="none";
	    window.parent.opener.document.getElementById('linkPena').style.visibility="hidden";
	    
	   // alert(ElencoReati[inElencoReati]);
		
		insertReati(inElencoReati);
		insertPena(datipenacompl);
		
        window.parent.close();
    
      }     // chiude InsertIT                            
 
  	</script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti di :</font></td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
         <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
<table width ="100%">
<%

if(StrNomeSoggettoAlias.length() > 0)
 {%>
   <tr>
    	<td class="l" >Soggetto: &nbsp;
      		<font class="campo">
         		<%=StrNomeSoggettoAlias%>
      		</font>
    	</td>
    </tr>
    <tr>
    	<td class="l" >Alias di: &nbsp;
    		<font class="campo">
          		<%=soggetto.getCognome() +" " +soggetto.getNome()%>
       		</font>
    	</td>
   </tr>
<%}
  else
  {%>
   <tr>
    	<td class="l" >Soggetto: &nbsp;
      		<font class="campo">
          		<%=soggetto.getCognome() +" " +soggetto.getNome()%>
      		</font>
    	</td>
   </tr>
<%}%>
  
  <tr>
<%
  if(soggetto.getSesso().equals("M"))
  {
%>
    	<td class="l" >Nato il:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%
  }
  else
  {
%>
    	<td class="l" >Nata il:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
<%
  }
%>
  			<font class="campo"><%=DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy")%>
  			</font>&nbsp;&nbsp; in:  &nbsp;&nbsp;&nbsp;
<%
  if (soggetto.getDescrComuneNascita().compareTo("-")==0)
  {
%>
    		<font class="campo"><%=soggetto.getDescComuneNascitaEstero()%>  (<%=soggetto.getDescrStatoNascita().toUpperCase()%>)&nbsp; 
			</font>
<%
  }
  else
  {
%>
    		<font class="campo"><%=soggetto.getDescrComuneNascita()%> (<%=soggetto.getCodProvinciaNascita()%>)&nbsp;
			</font>
<%
  }
%>
		</td>	
  </tr>
</table>
<table cellspacing=2 cellpadding=2>
<%
  if((lproc.equals("N") ))
  {
%>
    <tr>
      <td class ="Titolo" colspan=8>
      	<%if(strTipoRicerca != null && strTipoRicerca.equals("ufficio")) 
        	{%>
      			Elenco Procedimenti associati al soggetto dell' ufficio
      	  <%}
      	    else if (strTipoRicerca != null && !strTipoRicerca.equals("ufficio"))
      	    {%>
      	    	Elenco Procedimenti associati al soggetto del distretto
      	  <%}%>  
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td>
        <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Ufficio Esecuzione</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
      <td class="int">Azioni</td>
    </tr>
<%
  }
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	//siesLogger.info("FASCICOLO SIZE-> " + fascicoli.size());

Iterator itx = fascicoli.iterator();
Iterator itxUffDistr = ufficiDistretto.iterator();
BigDecimal annoreggen=null;
String numreggen=null;

for (int numFasc = 0; itx.hasNext(); numFasc++) {
	FascicoloSiepModel fascicolo = (FascicoloSiepModel)itx.next();
	// Se utente SIUS non vengono visualizzati i fascicoli nello stato ISCRITTO (02)
	if (fascicolo.getIdFascicoloSiep().toString().equals(IdFascCorrente))
		continue;
	if (!isSIUS || fascicolo.getCodStatoFascicolo().compareTo("02") != 0) {
		if (fascicolo.getCodDistretto().equals(StrCodiceDistrettoUtente)) {
          	UfficioModel lUfficio = (UfficioModel)itxUffDistr.next();
          	// SentenzaModel sent=fascicolo.getSentenza();
			if (fascicolo.getSentenza().getAnnoRegeGip()!=null) {
        	  	annoreggen=fascicolo.getSentenza().getAnnoRegeGip();
        	  	numreggen=fascicolo.getSentenza().getNumeroRegeGip();
          	} else {
          		// MEV_66: corretta estrazione dati
          		if (fascicolo.getSentenza().getAnnoRegeDib()!=null ){
	    	  		annoreggen=fascicolo.getSentenza().getAnnoRegeDib();
	    	  		numreggen=fascicolo.getSentenza().getNumeroRegeDib();
           		} else {
  	    	  		annoreggen=null;
  	    	  		numreggen="";
            	}
          	}

          	// leggo la pena complessiva
         	Iterator itxpena = allpenacompl.iterator();
          	String valoripenacompl=new String();

          	while (itxpena.hasNext()) {
        	  PenaComplessivaModel lPenCom=(PenaComplessivaModel)itxpena.next();
        	  if (lPenCom!=null)
        	  if (lPenCom.getFasSieIdFascicoloSiep().equals(fascicolo.getIdFascicoloSiep())){
        		  
	        	  if ((lPenCom.getNumAnniReclusione() != null) || 
	        			  (lPenCom.getNumMesiReclusione() != null) || 
	        			  (lPenCom.getNumGiorniReclusione() != null)) {    		
	        		  	valoripenacompl+=lPenCom.getNumAnniReclusione()+"-";
	        		  	valoripenacompl+=lPenCom.getNumMesiReclusione()+"-";
	        		  	valoripenacompl+=lPenCom.getNumGiorniReclusione();
	        	  }
	        	  valoripenacompl+=";";
	        	  if (lPenCom.getImportoMulta() != null) {
	        		  valoripenacompl+=lPenCom.getImportoMulta();
	        	  }
	        	  valoripenacompl+=";";
	        	  if ((lPenCom.getNumAnniArresto() != null) || 
	        			  (lPenCom.getNumMesiArresto() != null)|| 
	        			  (lPenCom.getNumGiorniArresto() != null)) {    		
        		  	valoripenacompl+=lPenCom.getNumAnniArresto()+"-";
        		  	valoripenacompl+=lPenCom.getNumMesiArresto()+"-";
        		  	valoripenacompl+=lPenCom.getNumGiorniArresto();
        	  	  }
				  valoripenacompl+=";";
        	      if (lPenCom.getImportoAmmenda() != null) {
        		    valoripenacompl+=lPenCom.getImportoAmmenda();
        	  	  }
        	  	  valoripenacompl+=";";

        	  	  //ergastolo
        		  if (   (lPenCom.getDescrTipoPenaDetentivaDB() != null)&& (!(lPenCom.getDescrTipoPenaDetentivaDB().equals("-")))) { 
				  	valoripenacompl+=lPenCom.getDescrTipoPenaDetentivaDB();
        	  	  }
        	  	  valoripenacompl+=";";
        	  	  
        		  if (lPenCom.getDataInizioIsolamentoDiurno() != null) 
        			{ 
        			  valoripenacompl+=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"));
        			} 
        	  	  valoripenacompl+=";";

        		  if (lPenCom.getDataFineIsolamentoDiurno() != null) 
        			{ 
        				valoripenacompl+=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"));
        			} 
        		  valoripenacompl+=";";
        		  
        		  if ((lPenCom.getNumAnniIsolamentoDiurno() != null) || 
        				  (lPenCom.getNumMesiIsolamentoDiurno() != null)||
        				  (lPenCom.getNumGiorniIsolamentoDiurno() != null)) 
        			{ 
			        	valoripenacompl+=lPenCom.getNumGiorniIsolamentoDiurno()+"-";
			        	valoripenacompl+=lPenCom.getNumMesiIsolamentoDiurno()+"-";
			        	valoripenacompl+=lPenCom.getNumAnniIsolamentoDiurno();
        			} 
          	  	  valoripenacompl+=";";

          	  	  
        		  if (lPenCom.getDataPrescrizione() != null) 
        			{ 
						valoripenacompl+=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataPrescrizione(),"dd-MM-yyyy"));
	       			} 
          	  	  valoripenacompl+=";";

       	  	  	break;
        	  }
          }
          //FINE Pena complessiva
          
          
          //leggo i reati
          Vector titi=new Vector();
          ReatoModel reato=null;
          String valorilistareati=new String();
          int flag_trovato=0;
          
          Iterator itxreati = allreati.iterator();
          Iterator itxreatifasc = null;
          
          while ( itxreati.hasNext())
    		{
        	  titi=(Vector)itxreati.next();
        	  
        	  itxreatifasc = titi.iterator();
        	  reato = (ReatoModel)itxreatifasc.next();
        	  if (reato.getFasSieIdFascicoloSiep().equals(fascicolo.getIdFascicoloSiep())) {
          		// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          		//siesLogger.info("Qui ci sono i Reati di questo fascicolo!");
          		flag_trovato=1;
        		break;
        	  }
    		}
          if(flag_trovato==1){
        	  
  ///INIZIO      	  
        	  
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        // siesLogger.info("DENTRO IF != null");
        	  
		       	String PenaDet=new String("");
				if (reato.getNumAnni() != null)
			      PenaDet += "anni " +reato.getNumAnni();
				if (reato.getNumMesi() != null)
			      PenaDet += " mesi "+ reato.getNumMesi();
				if (reato.getNumGiorni() != null)
			      PenaDet += " giorni "+ reato.getNumGiorni();
				valorilistareati+=PenaDet+";";
					
  				String lProgressivo = "";
				if(reato.getProgrCircostanza()!=null)
				 if(reato.getProgrCircostanza().intValue() == 1)
				  lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();
				valorilistareati+=lProgressivo+";";
            
    			if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null
 					|| reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
				{
    				if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
    				{
        				String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio(), "**");
        				if ( !lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
          					lStrGGInizio = "0"+lStrGGInizio;

				        String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio(), "**");
				        if ( !lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
				          lStrMMInizio = "0"+lStrMMInizio;

        				String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio(), "**");
    					//INIZIO REATO
        				valorilistareati+=	lStrGGInizio+"-"+lStrMMInizio+"-"+lStrAAInizio+";";
    				}

					if((reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
					         &&( reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null))
					{
        				valorilistareati+=	"/";
					}
					if(reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
					{
				        String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine(), "**");
				        if ( !lStrGGFine.equals("**") && lStrGGFine.length() == 1)
				          lStrGGFine = "0"+lStrGGFine;
				
				        String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine(), "**");
				        if ( !lStrMMFine.equals("**") && lStrMMFine.length() == 1)
				          lStrMMFine = "0"+lStrMMFine;
				
				        String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine(), "**");
				        valorilistareati+=	lStrGGFine+"-"+lStrMMFine+"-"+lStrAAFine+";";
				
					}

				}else
			    {
			        valorilistareati+=	"-"+";";
				}

    			if(reato.getDescrFonte()!=null)
    				valorilistareati+=reato.getDescrFonte()+";";
    			else
    				valorilistareati+="-"+";";	
    				
       			if(reato.getAnnoFonte()!=null)
       				valorilistareati+=reato.getAnnoFonte()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getNumeroFonte()!=null)
       				valorilistareati+=reato.getNumeroFonte()+";";
       			else
       				valorilistareati+="-"+";";	

    			if(reato.getArticolo()!=null)
    				valorilistareati+=reato.getArticolo()+";";
    			else
    				valorilistareati+="-"+";";	
    				
       			if(reato.getDescrSottonumerazione()!=null)
       				valorilistareati+=reato.getDescrSottonumerazione()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getComma()!=null)
       				valorilistareati+=reato.getComma()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getDescrCommaQualificante()!=null)
       				valorilistareati+=reato.getDescrCommaQualificante()+";";
       			else
       				valorilistareati+="-"+";";	
       				
       			if(reato.getLettera()!=null)
       				valorilistareati+=reato.getLettera()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getNumero()!=null)
       				valorilistareati+=reato.getNumero()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getNote()!=null)
       				valorilistareati+=reato.getNote()+";";
       			else
       				valorilistareati+="-"+";";	
        	  
   ///FINE     	  
        	  valorilistareati+="$"; //fine model
        	  
           	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
           	  //siesLogger.info("inizio iterazione reati per fascicoli");

        	    for (int i = 0; itxreatifasc.hasNext(); i++)
      			{
            	  // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            	  //siesLogger.info("nel ciclo iterazione "+ i);
            	  reato=(ReatoModel)itxreatifasc.next();
            	  
            	  
  		       	PenaDet="";
				if (reato.getNumAnni() != null)
			      PenaDet += "anni " +reato.getNumAnni();
				if (reato.getNumMesi() != null)
			      PenaDet += " mesi "+ reato.getNumMesi();
				if (reato.getNumGiorni() != null)
			      PenaDet += " giorni "+ reato.getNumGiorni();
				valorilistareati+=PenaDet+";";
					
  				lProgressivo = "";
				if(reato.getProgrCircostanza()!=null)
				 if(reato.getProgrCircostanza().intValue() == 1)
				  lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();
				valorilistareati+=lProgressivo+";";
            
    			if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null
 					|| reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
				{
    				if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
    				{
        				String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio(), "**");
        				if ( !lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
          					lStrGGInizio = "0"+lStrGGInizio;

				        String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio(), "**");
				        if ( !lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
				          lStrMMInizio = "0"+lStrMMInizio;

        				String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio(), "**");
    					//INIZIO REATO
        				valorilistareati+=	lStrGGInizio+"-"+lStrMMInizio+"-"+lStrAAInizio+";";
    				}

					if((reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
					         &&( reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null))
					{
        				valorilistareati+=	"/";
					}
					if(reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
					{
				        String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine(), "**");
				        if ( !lStrGGFine.equals("**") && lStrGGFine.length() == 1)
				          lStrGGFine = "0"+lStrGGFine;
				
				        String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine(), "**");
				        if ( !lStrMMFine.equals("**") && lStrMMFine.length() == 1)
				          lStrMMFine = "0"+lStrMMFine;
				
				        String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine(), "**");
				        valorilistareati+=	lStrGGFine+"-"+lStrMMFine+"-"+lStrAAFine+";";
				
					}

				}else
			    {
			        valorilistareati+=	"-"+";";
				}

    			if(reato.getDescrFonte()!=null)
    				valorilistareati+=reato.getDescrFonte()+";";
    			else
    				valorilistareati+="-"+";";	
    				
       			if(reato.getAnnoFonte()!=null)
       				valorilistareati+=reato.getAnnoFonte()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getNumeroFonte()!=null)
       				valorilistareati+=reato.getNumeroFonte()+";";
       			else
       				valorilistareati+="-"+";";	

    			if(reato.getArticolo()!=null)
    				valorilistareati+=reato.getArticolo()+";";
    			else
    				valorilistareati+="-"+";";	
    				
       			if(reato.getDescrSottonumerazione()!=null)
       				valorilistareati+=reato.getDescrSottonumerazione()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getComma()!=null)
       				valorilistareati+=reato.getComma()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getDescrCommaQualificante()!=null)
       				valorilistareati+=reato.getDescrCommaQualificante()+";";
       			else
       				valorilistareati+="-"+";";	
       				
       			if(reato.getLettera()!=null)
       				valorilistareati+=reato.getLettera()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getNumero()!=null)
       				valorilistareati+=reato.getNumero()+";";
       			else
       				valorilistareati+="-"+";";	
   				
       			if(reato.getNote()!=null)
       				valorilistareati+=reato.getNote()+";";
       			else
       				valorilistareati+="-"+";";	
        	  
   ///FINE     	  
        	  valorilistareati+="$"; //fine model
      		}
            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            //siesLogger.info("fine iterazione reati per fascicoli");
         }
%>

      <script language="JavaScript">
 		 ElencoReati[<%=numFasc%>] = "<%=valorilistareati%>";
	 </script>

          <tr>
            <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
            <td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td>
            <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
            <td class="c"><font class="label"><%=fascicolo.getChiaveAnno()%>/<%=fascicolo.getChiaveProgr()%></font></td>
            <td class="c"><font class="label"><%=lUfficio.getCodTipoUfficio()%> di <%=lUfficio.getDescrComune()%> </font></td>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
            <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getDescrStatoProcedimento())%>&nbsp;</font></td>
        		<td class="C">
            		<a href="Javascript:insertIT('<%=StringUtils.toStringJSP(fascicolo.getIdFascicoloSiep())%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(),"yyyy"))%>',            									 
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd"))%>',
        		    							 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"MM"))%>',
            									 '<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"yyyy"))%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getAnnoSentenza())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumeroSentenza())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getCodTipoAutoritaEmittente())%>',
            									 '<%=StringUtils.cStrForJS(fascicolo.getSentenza().getDescrLuogoEmittente())%>',
            									 '<%=StringUtils.toStringJSP(fascicolo.getSentenza().getNumSezioneAutoritaEmittente())%>',
            									 '<%=numFasc%>',
            									 '<%=valoripenacompl%>');">
              		<img align="middle" src="/images/fileselected.gif" border=0>
            		</a>
        		</td>                                        
          </tr>
<%
        }
      }
    }
%>
  </table>
  </form>
  <br>
  </body>
</html>