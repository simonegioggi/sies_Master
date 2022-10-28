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
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel" %>

<jsp:useBean id="penaresidua"				scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="posizioneluogoaltra"		scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="lSogg"						scope="request" class="siap.sico.soggetto.model.SoggettoModel"/>
<jsp:useBean id="ListaOrd"					scope="request" class="java.util.Vector"/>
<jsp:useBean id="avvocati"					scope="request" class="java.util.Vector"/>
<jsp:useBean id="magistrato"				scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaE"			scope="request" class="java.lang.String"/>
<%

FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();

MisuraSicurezzaModel lMis = null;

List lMisure =(List) request.getAttribute("listaMisureSic");
%>
<!-- LoadInserisciDesignazioneIstituto -->
<html>
  <head>
    <title>[S.I.E.S.] -Gestione Misure sicurezza- designazione Istituto (dopo Richiesta al DAP)</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
	
    	var desktop;
 
    	// Lista dei COMUNI
    	function ListaComuni(a_formname,a_fieldname)
        {
           desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
    	
        function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
        {
	    //desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
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
  			var Misu ='<%= ListaOrd.size()%>';
  			var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
  			
    // DATA DEFINIZIONE
		  	if (document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
			  	document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
		  	if (document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
			  	document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

		  	var data_to_verify = document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        	if (!ControllaData(data_to_verify) )
		  	{
	        		alert('Data di Designazione non valida');
	        		document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
				   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Definizione .
		    if( !CompareDate( data_to_verify, data_sistema) )
		    {
			      alert('Data di Designazione non può essere superiore alla data odierna!');
			      document.LoadInsIstituto.<%=ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();
			      return false;
		    }
		    
    // DATA PERVENIMENTO
		  	if (document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
			  	document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
		  	if (document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
			  	document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;

		  	var data_to_verify_perv = document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;

        	if (!ControllaData(data_to_verify_perv) )
		  	{
	        		alert('Data Pervenimento non valida');
	        		document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.focus();
				   	return false;
		  	}
        	
		    //1) Controllo : data di sistema deve essere >= Data Pervenimento.
		    if( !CompareDate( data_to_verify_perv, data_sistema) )
		    {
			      alert('Data Pervenimento non può essere superiore alla data odierna!');
			      document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.focus();
			      return false;
		    }
		    
   			// Data Pervenimento deve essere >= Data di Designazione
   		 	if( !CompareDate( data_to_verify, data_to_verify_perv) )
		    {
			      alert('Data Designazione non può essere superiore alla Data Pervenimento!');
			      document.LoadInsIstituto.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.focus();
			      return false;
		    }

	// Controllo ISTITUTO DETENZIONE	
			if(document.LoadInsIstituto.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "" )
			{
					alert('ERRORE : Inserire Struttura Designata ');
	        		document.LoadInsIstituto.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
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
        <font class="campo">Annotazione designazione Istituto</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInsIstituto" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInserisciAnnotazioneDesignazioneIstituto">
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

 <!-- Misure di Sicurewzza già presenti -->  
<% 	if(lMisure.size() > 0)
	{ 	%>
	    <table>
<%		Iterator itx = lMisure.iterator();
		while (itx.hasNext())
		{ 
			 lMis = (MisuraSicurezzaModel)itx.next();
			 if(lMis != null && lMis.getCodNatura() != null && lMis.getCodNatura().equals("01"))
			 {	 
		%>		    
	    	<tr>
		    	<td class=C>Misura di Sicurezza da espiare</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</font></td>
		      	<td class=C>Num. Anni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</font></td>
		      	<td class=C>Num. Mesi</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</font></td>
		      	<td class=C>Num. Giorni</td>
		      	<td class=L><font class="campo"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</font></td>	
		    </tr>
	<%		}
		} %>			      		
	    </table>
<%	} %>	    
	    <br>

<!-- Date Pervenimento e Designazione --> 
	<table width="60%">
		<tr>
        	<td class="l" >Data Pervenimento della designazione<font class="ob">(*)</font></td>
        	<td class="L" colspan=2 >
	          	<input value="" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	          	<input value="" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	          	<input value="" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        	</td>
      	</tr>
		<tr>
        	<td class="l">Data di Designazione <font class="ob">(*)</font></td>
        	<td class="L" colspan=2 >
	          	<input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	          	<input value="" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
	          	<input value="" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        	</td>
      	</tr>
 </table>
 <br>
 
 <!--   Autorità (DAP)	-->
 <table width="70%">
	<tr>	
		<td class="L" width="30%"> Autorità che ha proceduto alla Designazione</td>
 		<td class="L" width="30%" >
       		<select  Title="Autorita " name="<%=ICostantiMisuraSicurezza.CAMPO_COD_TIPO_AUTORITA_DESIGNAZIONE_IST%>" style="size:80px" >
        		<%=autoritaEsternaE%>
       		</select>
      	</td>
      	<td class="L" width="5%"> di </td>
     	<td class="L" width="5%">
       		<font class="campo">ROMA</font><!--  Cod Comune = 058091 -->
     	</td>      	
    </tr> 
 </table>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
 <%--  table width="90%">    
    <tr>
      <td class="l" width="15%">Sede </td>
      <td class="L" width="30%">
        <input title="Sede Autorita " value="" type="text" name="<%=ICostantiMisuraSicurezza.CAMPO_COD_LUOGO_AUTORITA_DESIGNAZIONE_IST %>" size="35">
        <a href="Javascript:ListaComuni('LoadInsIstituto','<%=ICostantiMisuraSicurezza.CAMPO_COD_LUOGO_AUTORITA_DESIGNAZIONE_IST %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td> 

   </tr>
</table --%>
<br>

 <!--   Istituto	-->
<table width="90%">
    <tr>
    	<td class="l" width="30%">Struttura Designata <font class="ob">(*)</font></td> 
     	<td class="l" colspan="2">
      		<input readonly  Title="Istituto" name="Comune" value="" size=90>
      		<input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=90>
      			<a href="Javascript:ListaIstitutoDetenzione('LoadInsIstituto','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
        			<img src="/images/filefolder.gif" border=0></a>
      			<a href="Javascript:pulisciIstituto('Comune','<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>');">
      				<img src="/images/delete.gif" border=0></a>
    	</td>
    </tr>

 <!--   Numero Protocollo Nota	-->

    <tr>
      <td class="l" width="30%">Numero Protocollo Nota</td>
      <td class="l" colspan="2">
         <input Title="Numero Nota" value="" name="<%=ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO%>" type="text" size=50 maxlength="35">
      </td>
    </tr>   
</table>
    

<!-- Bottone di Conferma, in DIV perchè deve cambiare posizione -->	
	<tr> 
    	<td class="lNoBord" colspan="2">
      		<br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    	</td>
  	</tr>
</table>

	<input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
	<input type="HIDDEN" title="Id Misura" value="<%=StringUtils.toStringJSP(lMis.getIdMisuraSicurezza() )%>" type="text" name="<%= ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA %>">
	<input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
	<input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%-- input type="HIDDEN" title="id Evento" value="" type="text" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" --%>
</form> 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInsIstituto");

  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>","req","Il campo Giorno Data Definizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_GIORNO_DATA_DEFINIZIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>","req","Il campo Mese Data Definizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_MESE_DATA_DEFINIZIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","req","Il campo Anno Data Definizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraSicurezza.CAMPO_ANNO_DATA_DEFINIZIONE%>","lt=2099");

</script>
</body>
</html>