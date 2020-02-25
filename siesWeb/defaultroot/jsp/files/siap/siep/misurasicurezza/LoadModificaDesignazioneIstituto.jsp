<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale" %>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.verbale.model.VerbaleModel" %>

<jsp:useBean id="eventonotifica"      	scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="eventoverbale"      	scope="request" class="siap.sico.evento.model.EventoVerbaleModel"/>
<jsp:useBean id="posizioneluogoaltra"	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"		scope="request" class="java.lang.String"/>
<jsp:useBean id="istitutodetenzione" 	scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>
<!--  jsp:useBean id="avvocati"					scope="request" class="java.util.Vector"/ -->

<%
FascicoloSiepModel fascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MisuraSicurezzaModel lMis = null;
List lMisure =(List) request.getAttribute("listaMisure");

String Desc="";
if(istitutodetenzione != null && istitutodetenzione.getIdIstitutoDetenzione()!=null)
{
	Desc= istitutodetenzione.getDescrTipoIstituto();
	Desc+=" di ";
	Desc+=istitutodetenzione.getDescrizione();
	if( istitutodetenzione.getIndirizzo()!=null && !istitutodetenzione.getIndirizzo().equals("") )
	{
		Desc+=" - ";
		Desc+=istitutodetenzione.getIndirizzo();
	}		
}

%>
<!--	 LoadModificaDesignazioneIstituto 	-->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- designazione Istituto (dopo Richiesta al DAP) </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
	
    	var desktop;
 
    	//Funzione utile per impostare la data corrente.
    	function impostaDataOdierna(campo_giorno, campo_mese,campo_anno, dataOdierna)
    	{    
    		day=dataOdierna.substring(0,2);
    		month=dataOdierna.substring(3,5);
    		year=dataOdierna.substring(6,10);
    	    document.getElementsByName(campo_giorno).item(0).value = day;
    	    document.getElementsByName(campo_mese).item(0).value = month;
    	    document.getElementsByName(campo_anno).item(0).value = year; 
    	}
    	
    	// lista Istituti di detenzione
        function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&LoadDescEstesa=SI", "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
        }
    	
    	function pulisciIstituto (nomeCampoComune, nomeCampoId)
        {
            var campoDescr = document.getElementsByName(nomeCampoComune)[0];
            var campoId    = document.getElementsByName(nomeCampoId)[0];
            campoDescr.value="";
            campoId.value="";
         }
   		
	function Verify()
  	{
  		var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
    // DATA DESIGNAZIONE
		if (document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
		  	document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
		if (document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
		  	document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

		var data_to_verify = document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaData(data_to_verify) )
		{
	    	alert('Data di Designazione non valida');
	    	document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
		  	return false;
		}
        	
	 //1) Controllo : data di sistema deve essere >= Data Definizione .
		if( !CompareDate( data_to_verify, data_sistema) )
		{
		     alert('Data di Designazione non può essere superiore alla data odierna!');
		     document.ModDesignaIst.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
		     return false;
		}
  			
    // DATA PERVENIMENTO
		if (document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
		  	document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
		if (document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
		 	document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;

	  	var data_to_verify_perv = document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
       	if (!ControllaData(data_to_verify_perv) )
	  	{
       		alert('Data Pervenimento non valida');
       		document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.focus();
		   	return false;
	  	}
        	
    //1) Controllo : data di sistema deve essere >= Data Pervenimento.
	    if( !CompareDate( data_to_verify_perv, data_sistema) )
	    {
		      alert('Data Pervenimento non può essere superiore alla data odierna!');
		      document.ModDesignaIst.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.focus();
		      return false;
		 }
  			
    // DATA EMISSIONE
		if (document.ModDesignaIst.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
		  	document.ModDesignaIst.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModDesignaIst.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		if (document.ModDesignaIst.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
		  	document.ModDesignaIst.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModDesignaIst.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		var data_to_verify = document.ModDesignaIst.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModDesignaIst.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModDesignaIst.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaData(data_to_verify) )
		{
	    	alert('Data Emissione non valida');
	    	document.ModDesignaIst.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
		   	return false;
		}
        	
	 //1) Controllo : data di sistema deve essere >= Data Emissione .
	    if( !CompareDate( data_to_verify, data_sistema) )
	    {
	      alert('Data Emissione non può essere superiore alla data odierna!');
	      document.ModDesignaIst.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	      return false;
	    }

	// Controllo STRUTTURA DESIGNATA (ISTITUTO DETENZIONE)
		if(document.ModDesignaIst.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "" )
		{
			alert('ERRORE : Inserire Struttura Designata ');
	   		document.ModDesignaIst.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
			return false;		
		}
		
    }	<% // Chiude function Verify %>
    
 </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Modifica Annotazione Designazione Istituto</font>
      </td>
    </tr>
  </table>

  <br>
	<jsp:include page="/jsp/files/siap/siep/misurasicurezza/TestataSoggettoperModificheMS.jsp"/>
  <br>
  
  <FORM method="POST" name="ModDesignaIst" action="<%= IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActModificaDesignazioneIstituto">
  	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  	<input type="HIDDEN" title="id Evento" value="<%= eventoverbale.getEvento().getIdEvento() %>" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" >
	<input type="HIDDEN" title="Sede Destinatario" value="Roma" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>" >
	<!--  input type="HIDDEN" title="Id Pena Residua" value="< %=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="< %= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" -->

  <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
          <%=posizioneluogoaltra.getPosizioneGiuridica().getDescrPosizioneGiuridica()%>
        </font>
        </td>
      </tr>
</table>
<table>
 <!-- Misure di Sicurewzza presenti -->  
<% 	if(lMisure.size() > 0)
	{ 	
		Iterator itx = lMisure.iterator();
		while (itx.hasNext())
		{ 
			 lMis = (MisuraSicurezzaModel)itx.next();
			 if(lMis != null && lMis.getCodNatura() != null && lMis.getCodNatura().equals("01"))
			 {	 
		%>	    
	    	  <tr>
		    	<td class=C>Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		      	<td class=C> Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		      	<td class=C> Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		      	<td class=C> Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
		      </tr>
		      <input type="HIDDEN" title="Id Misura" value="<%=StringUtils.toStringJSP(lMis.getIdMisuraSicurezza() )%>" 
		      		name="<%= ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA %>">    
<%			}
		} 	     		
	}
	else
	{ %>	    
		<tr><td class=C>Fascicolo privo di Misure di Sicurezza</td></tr>
<%	} %>	
 </table>
   <br>
<!--  Oggetto Provvedimento e Autorità  -->
<table width="90%">
	<tr>
       	<td style="text-align:center" class="l" width="25%">Oggetto </td>
     	<td class="L" colspan=3><font class="campo"> Designazione Istituto per esecuzione Misura Sicurezza dal DAP</font> </td>
    </tr>
	<tr>	
		<td class="L" width="25%">Autorità che ha proceduto alla designazione<font class=ob>(*)</font></td>
 		<td class="L" width="35%" >
       		<select  Title="Autorita Designatrice" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" >
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
      	<td class="L" width="5%"> di </td>
      	<td class="L" width="15%">
      		<font class="campo">ROMA</font><!--  Cod Comune = 058091 -->
      	</td>      	
    </tr>
</table>    
<br>
<table width="90%">
	<tr>	
      	<td class="Titolonocap" colspan=4> Dati Modificabili </td>
    </tr>
    <tr>
       	<td class="l" width="20%" >Data Designazione <font class="ob">(*)</font></td>
       	<td class="L" width="20%">
          	<input type="text" size="2" maxlength="2" name="<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE %>"  
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataEmissione(),"dd")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          	<input type="text" size="2" maxlength="2" name="<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE %>" 
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataEmissione(),"MM")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          	<input type="text" size="4" maxlength="4" name="<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE %>" 
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataEmissione(),"yyyy")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
       	</td>
       	<td class="l" width="20%" >Data Pervenimento <font class="ob">(*)</font></td>
       	<td class="L" width="20%">
          	<input type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>"  
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataPervenimento(),"dd")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          	<input type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>" 
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataPervenimento(),"MM")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          	<input type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>" 
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getVerbale().getDataPervenimento(),"yyyy")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</td>
    </tr>
    <tr>
       	<td class="l" width="20%" >Data Emissione <font class="ob">(*)</font></td>
       	<td class="L" width="20%">
          	<input type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getEvento().getDataEmissione(),"dd")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          	<input type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" 
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getEvento().getDataEmissione(),"MM")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          	<input type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" 
          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventoverbale.getEvento().getDataEmissione(),"yyyy")) %> 
          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	        <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
	          <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
	        </a>
       	</td>
       	<td colspan=2>&nbsp;</td>
	</tr>       	
     <!--   	Istituto		-->
    <tr>
    	<td class="l" width="20%">Struttura Designata <font class="ob">(*)</font></td> 
     	<td class="l" colspan="3">
      		<input readonly  Title="Struttira" name="Comune" value="<%=StringUtils.toStringJSP(Desc, "")%>" size=99>
      		<input type="hidden" Title="Id Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=istitutodetenzione.getIdIstitutoDetenzione()%>">
      			<a href="Javascript:ListaIstitutoDetenzione('ModDesignaIst','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        			<img src="/images/filefolder.gif" border=0></a>
      			<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
      				<img src="/images/delete.gif" border=0></a>
    	</td>
    </tr>
     <!--   Numero Protocollo Nota	-->
    <tr>
<%	if(eventoverbale!=null && eventoverbale.getVerbale()!=null)
	{
		if(eventoverbale.getVerbale().getNumeroProtocollo() != null)
		{	%>     
		      	<td class="l" width="20%">Numero Protocollo Nota</td>
		      	<td class="l" colspan="3">
		         <input Title="Numero Nota" value="<%=StringUtils.toStringJSP(eventoverbale.getVerbale().getNumeroProtocollo(), "")%>" 
		         		name="<%=ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO%>" type="text" size=50 maxlength="35">
		      	</td>
<%		}
		else
		{	%>
		      	<td class="l" width="20%">Numero Protocollo Nota</td>
		      	<td class="l" colspan="3">
		         <input Title="Numero Nota" value="" name="<%=ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO%>" type="text" size=50 maxlength="35">
		      	</td>		
<%		}
	}	%>
	</tr>				         
<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->	
	<tr> 
    	<td class="lNoBord" colspan="4">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>

</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("ModDesignaIst");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione della Richiesta è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione della Richiesta è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione della Richiesta è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

</script>
</body>
</html>