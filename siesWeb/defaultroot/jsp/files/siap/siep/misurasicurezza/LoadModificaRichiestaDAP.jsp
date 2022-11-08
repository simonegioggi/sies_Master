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
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sico.camponota.model.CampoNotaModel" %>

<jsp:useBean id="eventonotifica"      		scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"				scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"			scope="request" class="java.lang.String"/>
<!--  jsp:useBean id="avvocati"					scope="request" class="java.util.Vector"/ -->

<%
FascicoloSiepModel fascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MisuraSicurezzaModel lMis = null;
List lMisure =(List) request.getAttribute("listaMisure");

CampoNotaModel CnoteMod = null;
String lNote="";
if(eventonotifica!=null && eventonotifica.getCampoNote()!=null && eventonotifica.getCampoNote().length > 0)
{
	CnoteMod = (CampoNotaModel)eventonotifica.getCampoNote()[0];
	lNote = CnoteMod.getDescr();
}
	
%>
<!--	 LoadModificaRichiestaDAP 	-->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- Richiesta al DAP (di designazione Istituto)</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
	
    	var desktop;
 
    	// Lista dei MAGISTRATI
    	function ListaMagistrati(a_formname)
    	{
    	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    	}
    	
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
   		
  		function Verify()
  		{
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
		    // DATA EMISSIONE
		  	if (document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  	document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  	if (document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  	document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  	var data_to_verify = document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
	        		alert('Data Richiesta non valida');
	        		document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
				   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Emissione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
			      alert('Data Richiesta non può essere superiore alla data odierna!');
			      document.ModRichiestaDAP.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
			      return false;
		    }

			// Controllo AUTORITA	
			if(document.ModRichiestaDAP.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "" )
			{
					alert('ERRORE : Inserire la descrizione SEDE ');
	        		document.ModRichiestaDAP.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
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
        <font class="campo">Modifica Richiesta D.A.P. di designazione Istituto</font>
      </td>
    </tr>
  </table>

  <br>
	<jsp:include page="/jsp/files/siap/siep/misurasicurezza/TestataSoggettoperModificheMS.jsp"/>
  <br>
  
  <FORM method="POST" name="ModRichiestaDAP" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActModificaRichiestaDAP">
  <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <input type="HIDDEN" title="id Evento" value="<%= eventonotifica.getEvento().getIdEvento() %>" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" >

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
<%			}
		} 	     		
	}
	else
	{ %>	    
		<tr><td class=C>Fascicolo privo di Misure di Sicurezza</td></tr>
<%	} %>	
 </table>
   <br>
<!--  Oggetto Richiesta -->

	<table>
		<tr>
        	<td class="l" width="20%">Oggetto </td>
        	<!--  td class="L"width="60%"><font class="campo"> Designazione Istituto per esecuzione Misura di Sicurezza</font> </td -->
        	<td class="L"width="60%"><font class="campo"> Richiesta al DAP di Designazione Istituto per esecuzione Misura di Sicurezza</font> </td>
      	</tr>
	</table>

<!--Magistrato Firmatario -->  
	<table>   
    	<tr><td class="Titolonocap" colspan=4>Magistrato Firmatario</td></tr>
     	<tr>
	     	<td class="L" width="20%">Magistrato Firmatario <font class=ob>(*)</font></td>
	     	<td class="L" width="60%">
	        	<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="35">
	         	<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="35">
	           <a href="Javascript:ListaMagistrati('ModRichiestaDAP');">
	            <img src="/images/filefolder.gif" border=0>
	          </a>
	     	</td>
	  		<td>
	  			<input  type="hidden"  title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>"  name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  >
	  		</td>
	  	</tr>		
	</table>
 
 <!--Notifica per Altra Autorità 	-->
 <table width="80%">
	<tr><td class="Titolo" colspan=4>Destinatari</td></tr>
	<tr>	
		<td class="L" width="20%">Altra Autorità <font class=ob>(*)</font></td>
 		<td class="L" width="40%" >
       		<select  Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" >
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
      	<td class="L" width="5%"> di </td>
      	<td class="L" width="15%">
      		<font class="campo">ROMA</font><!--  Cod Comune = 058091 -->
      	</td>      	
    </tr>
	<tr><td>&nbsp;</td></tr>
	<tr>	
      	<td class="Titolonocap" colspan=4> Dati Modificabili </td>
    </tr>
    <tr>
    	<td class="l">&nbsp;Note&nbsp;</td>  	
      	<td class="L" colspan=3>
        	<TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>" cols=80 rows=2 ><%=StringUtils.toStringJSP(lNote)%></textarea>
      	</td>
    </tr>  
 </table>	
<!-- Date della Richiesta --> 
	<br>	
	<table width="80%">
		<tr>
        	<td class="l" width="20%" >Data Richiesta <font class="ob">(*)</font></td>
        	<td class="L" width="20%">
	          	<input type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  
	          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd")) %> 
	          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	          	<input type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" 
	          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM")) %> 
	          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	          	<input type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" 
	          		value=<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy")) %> 
	          		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		        <a href="Javascript:impostaDataOdierna('<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>','<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>', '<%=DateUtils.getSysDate("dd/MM/yyyy")%>');">
		          <img src="/images/Calendar2.png" border="0" height="20" width="20" align="top" title="Imposta data odierna">
		        </a>
        	</td>
        	<td width="40%">&nbsp;</td>
      	</tr>
      	<tr>
   	  		<!--  input type="HIDDEN" title="Id Pena Residua" value="< %=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="< %= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>" -->
   	  		<input type="HIDDEN" title="Id Misura" value="<%=StringUtils.toStringJSP(lMis.getIdMisuraSicurezza() )%>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA %>">
      		<input type="HIDDEN" title="Sede Destinatario" value="Roma" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>" >
      	</tr>     

<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->	
	<tr> 
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>

</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("ModRichiestaDAP");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione della Richiesta è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione della Richiesta è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione della Richiesta è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

</script>
</body>
</html>