<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>

<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>

<jsp:useBean id="dataInsFS"      scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario1"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario2"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario3"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario4"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario5"  scope="request" class="java.lang.String"/>
<jsp:useBean id="Destinatario6"  scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoComunicazione"			scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrTipoComunicazione"		scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoPenaAccessoria"    	  scope="request" class="java.lang.String"/>
<jsp:useBean id="DataComunicazione_GG"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="DataComunicazione_MM"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="DataComunicazione_AA"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoPenaAccessoria"    	scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrTipoPenaAccessoria"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="tenoreOrdinanza"  					scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaOrdinanza" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPeneAccessorie"			scope="request" class="java.lang.String"/>
<jsp:useBean id="penaaccessoria"						scope="request" class="siap.siep.penaaccessoria.model.PenaAccessoriaModel"/>
<jsp:useBean id="TipiFontiReato"						scope="request" class="java.lang.String"/>
<jsp:useBean id="TipiSottonumerazione"			scope="request" class="java.lang.String"/>

<%
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = "siap.siep.penaaccessoria.action.ActComunicazione";
  String IdPenaAccessoria = request.getParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);

	// Parametri per i controlli della form.
  int LungDest1 = Destinatario1.length() ;
  int LungDest2 = Destinatario2.length() ;
  int LungDest3 = Destinatario3.length() ;
  int LungDest4 = Destinatario4.length() ;
  int LungDest5 = Destinatario5.length() ;
  int LungDest6 = Destinatario6.length() ;

  // Controllo Valorizzazione Ordinanza P.A.
  String disabilita = "";
  String colDisabilita = "l";
  String visDisabilita = "visible";
  if(penaaccessoria.getFlagCondonata().compareTo("-")!=0)
  {
  	disabilita="DISABLED";
  	colDisabilita="cGrigio";
  	visDisabilita="hidden";
  }
%>
	<script language="JavaScript">
    var desktop;
  	function ListaComuni(a_formname,a_fieldname)
  	{
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
    function ListaUfficiPerTipo(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

	</script>

<script language="JavaScript">
// La funzione attiva il campo di Descrizione Altre PA.
 function cambia()
 {
    //alert("cambia");
    var desTipoComunicazione;
    var cod;
    desTipoComunicazione = '<%=DescrTipoComunicazione%>';
    if (desTipoComunicazione.indexOf("ostituzione")>0)
    {
    	document.forms[0].<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].focus();
    	cod = document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO%>.value;
    	if (cod == "999")
    	{
      	desAltrePA.style.visibility='visible';
    	}else{
      	desAltrePA.style.visibility='hidden';
      	document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_DESCR_ALTRE_PA%>.value="";
    	}
    }
   	return;
 }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Comunicazione per Pena Accessoria </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Verify()
    {
      if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value.length==1)
          document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value='0'+document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value;

      if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value.length==1)
          document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value='0'+document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value;

      // Controllo conformita' tenore ord. e tipo richiesta.
      var FlagPA=document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_FLAG_CONDONATA%>.value;
      var descr_Tipo_Comunicazione='<%=DescrTipoComunicazione%>';
      if ( (FlagPA=='C' && descr_Tipo_Comunicazione!='Condono Pena Accessoria')                ||
           (FlagPA=='D' && descr_Tipo_Comunicazione!='Depenalizzazione')                       ||
           (FlagPA=='R' && descr_Tipo_Comunicazione!='Revoca Pena Accessoria')                 ||
           (FlagPA=='T' && descr_Tipo_Comunicazione!='Sostituzione e Condono Pena Accessoria') ||
           (FlagPA=='S' && descr_Tipo_Comunicazione!='Sostituzione Pena Accessoria') )
      {
        alert('Tenore Ordinanza non conforme al Tipo Comunicazione!');
        return false;
      }

    var TipoNuovaPA=document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO%>.value;
    if ( (FlagPA=="S" || FlagPA=="T") && TipoNuovaPA=="-")
    {
      alert('Tipo Pena Accessoria in Sostituzione Obbligatorio');
      return false;
    }


      // Controllo validita' della data COMUNICAZIONE
      var data_comunicazione=document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>.value+'/'+document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>.value+'/'+document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE%>.value;
      if (! ControllaData(data_comunicazione))
      {
        alert('Data di comunicazione non valida');
        return false;
      }

      // Data comunicazione minore <= data sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( ! CompareDate( data_comunicazione,data_sistema) )
      {
        alert('Data comunicazione maggiore della data attuale!');
        return false;
      }

      // Data inserimento Fascicolo Siep <= data comunicazione
      if ( !CompareDate( '<%=dataInsFS%>', data_comunicazione) )
      {
        alert('Data comunicazione minore della data di inserimento del fascicolo SIEP!');
        return false;
      }

      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].value != '-'
          && document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[0].value == '')
      {
        alert('La Sede del destinatario 1 è obbligatoria');
        return false;
      }

  		if ('<%=LungDest2%>'!='0')
			{
      	if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[1].value != '-'
        	  && document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[1].value == '')
      	{
        	alert('La Sede del destinatario 2 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest3%>'!=0)
			{
      	if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[2].value != '-'
        	  && document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[2].value == '')
      	{
        	alert('La Sede del destinatario 3 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest4%>'!=0)
			{
      	if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[3].value != '-'
        	  && document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[3].value == '')
      	{
        	alert('La Sede del destinatario 4 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest5%>'!=0)
			{
      	if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[4].value != '-'
        	  && document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[4].value == '')
      	{
        	alert('La Sede del destinatario 5 è obbligatoria');
        	return false;
      	}
			}
  		if ('<%=LungDest6%>'!=0)
			{
      	if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[5].value != '-'
        	  && document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[5].value == '')
      	{
        	alert('La Sede del destinatario 6 è obbligatoria');
        	return false;
      	}
			}

      // Controlla che almeno un destinatario sia inserito
      if (document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[0].value == '-' &&
				 (('<%=LungDest2%>'!=0 &&
							document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[1].value == '-') ||
				  ('<%=LungDest2%>'==0)) &&
				 (('<%=LungDest3%>'!=0 &&
							document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[2].value == '-') ||
				  ('<%=LungDest3%>'==0)) &&
				 (('<%=LungDest4%>'!=0 &&
							document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[3].value == '-') ||
				  ('<%=LungDest4%>'==0)) &&
				 (('<%=LungDest5%>'!=0 &&
							document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[4].value == '-') ||
				  ('<%=LungDest5%>'==0)) &&
				 (('<%=LungDest6%>'!=0 &&
							document.LoadComunicazione.<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>[5].value == '-') ||
				  ('<%=LungDest6%>'==0 )) )
      {
        alert('Inserire almeno un Destinatario.');
        return false;
      }

      return true;
    }
  </script>
</head>

<body  onload="javascript:cambia();" class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Comunicazione <%=DescrTipoComunicazione%></font>
      </td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="Torna Indietro" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

    <tr>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadComunicazione">
    <table style="width: 95%;" cellspacing=2 cellpadding=2>

<%		if(IdPenaAccessoria == null)
			{%>
				<tr>
      		<td class="l">Tipo di Pena Accessoria</td>
      		<td class="l">
        		<select class="small" name="<%= ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA %>"  >
          		<%=TipoPenaAccessoria%>
        		</select>
      		</td>
				</tr>
    <%}else{%>
				<tr>
      		<td class="l">Tipo di Pena Accessoria</td>
      		<td class="campo"><%=DescrTipoPenaAccessoria%></td>
				</tr>
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>" value="<%=CodTipoPenaAccessoria%>" >
    <%}%>

      <tr>
        <td class="l">Tipo di Comunicazione</td>
        <td class="campo"><%=DescrTipoComunicazione%></td>
      </tr>

      <tr>
        <td class="l">Data Comunicazione</td>
        <td class="campo"><%=DataComunicazione_GG%>/<%=DataComunicazione_MM%>/<%=DataComunicazione_AA%>
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_COMUNICAZIONE%>" value="<%=CodTipoComunicazione%>" >
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_COMUNICAZIONE%>" value="<%=DataComunicazione_AA%>" >
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_COMUNICAZIONE%>" value="<%=DataComunicazione_MM%>" >
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_COMUNICAZIONE%>" value="<%=DataComunicazione_GG%>" >
        </td>
      </tr>
<%------------%>
<%		if(DescrTipoComunicazione.indexOf("ostituzione")>0)
			{%>
    		<tr><td class="Titolo" colspan=4>Estremi Pena Accessoria in Sostituzione</td></tr>
				<tr>
      		<td class="l">Tipo di Pena Accessoria</td>
      		<td class="l">
        		<select class="small" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO %>"  onchange="javascript:cambia();" >
          		<%=TipoPenaAccessoria%>
        		</select>
      		</td>
				</tr>

		    <tr>
      		<td colspan="2">
        		<div id=desAltrePA class="label" style="visibility:visible; position:relative; " >
          		<font class="l">Descrizione Altre P.A.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
          		<font class="l">
            		<input maxlength=50 size=70 Title="Descrizione Altre P.A." value="<%=StringUtils.toStringJSP(penaaccessoria.getDescrAltrePA()) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_DESCR_ALTRE_PA %>">
        		</div>
      		</td>
				</tr>

				<tr>
					<td class="l">Tipo Durata</td>
					<td class="l">
        	<select name="<%= ICostantiPenaAccessoria.CAMPO_DURATA %>">
          	<%=DurataPeneAccessorie%>
        	</select>
        	</td>
				</tr>
				<tr>
					<td class="l">Durata</td>
        	<td class="l">
          	Anni <input maxlength=2 size=2 Title="Anni Durata" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumAnni()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_ANNI %>">
          	Mesi <input maxlength=2 size=2 Title="Mesi Durata" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumMesi()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_MESI %>">
          	Giorni <input maxlength=2  size=2 Title="Giorni Durata" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumGiorni()) %>" type="text" name="<%= ICostantiPenaAccessoria.CAMPO_NUM_GIORNI %>">
        	</td>
				</tr>
		<%}else{%>
    		<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA_NUOVO%>" value="-" >
		<%}%>
		<%------------%>
<% if(DescrTipoComunicazione.indexOf("spiata Pena")<0)
   {%>
     <tr><td class="Titolo" colspan=4><font class='<%=colDisabilita%>'> Estremi Ordinanza Condono/Revoca/Sostituzione</font></td></tr>
  	 <tr>
       <td class="l"><font class='<%=colDisabilita%>'>Tenore</font> </td>
       <td class="l">
      	 <select name="<%= ICostantiPenaAccessoria.CAMPO_FLAG_CONDONATA %>" <%=disabilita%>>
        	 <%=tenoreOrdinanza%>
      	 </select>
         &nbsp;&nbsp;<font class='<%=colDisabilita%>'>Data Ordinanza</font> </font>
         <input Title="Giorno data Ordinanza" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(),"dd")) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_ORDINANZA_PA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%=disabilita%> ><font class='<%=colDisabilita%>'>/</font>
         <input Title="Mese data Ordinanza" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(),"MM")) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_ORDINANZA_PA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%=disabilita%> ><font class='<%=colDisabilita%>'>/ </font>
         <input Title="Anno data Ordinanza" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(),"yyyy")) %>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_ORDINANZA_PA%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" <%=disabilita%> >
       </td>
		  </tr>

      <tr>
        <td class="l"><font class='<%=colDisabilita%>'>Anno/Numero Ordinanza</font> </td>
        <td class="l">
          <input Title="Anno Ordinanza" value="<%=StringUtils.toStringJSP(penaaccessoria.getAnnoOrdinanzaPA())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_ORDINANZA_PA%>" maxlength="4" size="4" <%=disabilita%> > <font class='<%=colDisabilita%>'>/ </font>
          <input Title="Numero Ordinanza" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumeroOrdinanzaPA())%>" type="text" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_ORDINANZA_PA%>" maxlength="6" size="6" <%=disabilita%> >
        </td>
		  </tr>

      <tr>
        <td class="l"><font class='<%=colDisabilita%>'>Autorità Emittente</font> </td>
          <td class="l">
            <select Title="Autorità Emittente" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA%>" <%=disabilita%> >
              <%=autoritaOrdinanza%>
            </select>
          </td>
      </tr>
      <tr>
        <td class="l"><font class='<%=colDisabilita%>'>Luogo Ordinanza</font> </td>
        <td class="l">
          <input Title="Luogo Ordinanza" name="<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getDescrLuogoUfficioOrdinanzaPA())%>" type="text" maxlength="35" size="35" <%=disabilita%> >
          <a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA%>');"  style="visibility:'<%=visDisabilita%>'; ">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

    	<!-- Estremi Condono/Depenalizzazione/Amnistia -->
      	<table style="width: 95%;">
        	<tr>
          	<td class="Titolonocap" colspan=8><font class='<%=colDisabilita%>'>Estremi Condono/Depenalizzazione/Amnistia </font> </td>
  	      </tr>
    	    <tr>
      	    <td class="c"><font class='<%=colDisabilita%>'>Fonte</font> </td>
        	  <td class="c"><font class='<%=colDisabilita%>'>Anno</font> </td>
          	<td class="c"><font class='<%=colDisabilita%>'>Numero</font> </td>
	          <td class="c"><font class='<%=colDisabilita%>'>Articolo</font> </td>
  	        <td class="c"><font class='<%=colDisabilita%>'>Art.qualificante</font> </td>
    	      <td class="c"><font class='<%=colDisabilita%>'>Comma</font> </td>
      	    <td class="c"><font class='<%=colDisabilita%>'>Lettera</font> </td>
        	  <td class="c"><font class='<%=colDisabilita%>'>Numero</font> </td>
	        </tr>
  	      <tr>
    	      <td class="c">
      	      <select name="<%= ICostantiReato.CAMPO_COD_FONTE %>" <%=disabilita%> >
        	      <%=TipiFontiReato%>
          	  </select>
	          </td>
  	        <td class="c">
    	        <input size=4 maxlength=4 title="Anno Fonte" value="<%=StringUtils.toStringJSP(penaaccessoria.getAnnoFonteGE())%>" type="text" name="<%=ICostantiReato.CAMPO_ANNO_FONTE %>" <%=disabilita%> >
      	    </td>
        	  <td class="c">
          	  <input size=6 maxlength=6 title="Numero Fonte" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumeroFonteGE())%>" type="text" name="<%= ICostantiReato.CAMPO_NUMERO_FONTE %>" <%=disabilita%> >
	          </td>
  	        <td class="c">
    	        <input size=5 maxlength=5 title="Articolo Fonte" value="<%=StringUtils.toStringJSP(penaaccessoria.getArticoloGE())%>" type="text" name="<%= ICostantiReato.CAMPO_ARTICOLO %>" <%=disabilita%> >
      	    </td>
        	  <td class="c">
          	  <select name="<%= ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE %>" <%=disabilita%> >
              <%=TipiSottonumerazione %>
	            </select>
  	        </td>
    	      <td class="c">
      	      <strong><font class='<%=colDisabilita%>'>C</font> </strong>
        	    <input size=10 maxlength=10 title="Comma" value="<%=StringUtils.toStringJSP(penaaccessoria.getCommaGE())%>" type="text" name="<%= ICostantiReato.CAMPO_COMMA %>" <%=disabilita%> >
          	</td>
	          <td class="c">
  	          <strong><font class='<%=colDisabilita%>'>L</font> </strong>
    	        <input size=2 maxlength=2 title="Lettera" value="<%=StringUtils.toStringJSP(penaaccessoria.getLetteraGE())%>" type="text" name="<%= ICostantiReato.CAMPO_LETTERA %>" <%=disabilita%> >
      	    </td>
        	  <td class="c">
          	 <strong><font class='<%=colDisabilita%>'>N</font> </strong>
	           <input size=2 maxlength=2 title="Numero" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumeroGE())%>" type="text" name="<%= ICostantiReato.CAMPO_NUMERO %>" <%=disabilita%> >
  	        </td>
    	    </tr>
	    </table>
		  <%}
      if(penaaccessoria.getFlagCondonata().compareTo("-")!=0)
      {%>
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_FLAG_CONDONATA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getFlagCondonata())%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_GIORNO_DATA_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(),"dd"))%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_MESE_DATA_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(),"MM"))%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_DATA_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaaccessoria.getDataOrdinanzaPA(),"yyyy"))%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ANNO_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getAnnoOrdinanzaPA())%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_NUMERO_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumeroOrdinanzaPA())%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getCodTipoUfficioOrdinanzaPA())%>" >
      	<input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_LUOGO_UFFICIO_ORDINANZA_PA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getDescrLuogoUfficioOrdinanzaPA())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_COD_FONTE%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getCodFonteGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_ANNO_FONTE%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getAnnoFonteGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_NUMERO_FONTE%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumeroFonteGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_ARTICOLO%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getArticoloGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getCodSottonumerazioneGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_COMMA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getCommaGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_LETTERA%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getLetteraGE())%>" >
      	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_NUMERO%>" value="<%=StringUtils.toStringJSP(penaaccessoria.getNumeroGE())%>" >
   		<%}%>

<%------------%>
    <table style="width: 95%;">
    <tr><td class="Titolo" colspan=4>Destinatari</td></tr>
      <!-- Primo destinatario + luogo -->
      <tr>
        <td class="l">Destinatario n°1</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario1 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[0]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>

      <!-- Secondo destinatario + luogo -->
<%  	if (Destinatario2.length()>2)
			{%>
       	<tr>
        	<td class="l">Destinatario n°2</td>
        	<td class="L" colspan=3>
         		<table>
           		<tr >
           			<td class="l"> Autorità </td>
           			<td class="l" >
             			<select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               			<%= Destinatario2 %>
             			</select>
           			</td>
           		</tr>
           		<tr>
           			<td class="l">Sede <font class=ob>(*)</font></td>
           			<td class="l">
             			<input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              			value="" type="text" maxlength="35" size="35" >
                 	<a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[1]');">
              			<img src="/images/filefolder.gif" border=0> </a>
        				</td>
        			</tr>
        			<tr>
          			<td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          			<td class="L" colspan=3>
           				<input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          			</td>
        			</tr>
        		</table>
     		</tr>
		<%}%>

    	<!-- Terzo destinatario + luogo -->
<%  	if (Destinatario3.length()>2)
			{%>
      	<tr>
        	<td class="l">Destinatario n°3</td>
        	<td class="L" colspan=3>
         	<table>
           	<tr >
           		<td class="l"> Autorità </td>
           		<td class="l" >
             		<select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               		<%= Destinatario3 %>
             		</select>
           		</td>
           	</tr>
           	<tr>
           		<td class="l">Sede <font class=ob>(*)</font></td>
           		<td class="l">
             		<input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              		value="" type="text" maxlength="35" size="35" >
              		<a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[2]');">
              		<img src="/images/filefolder.gif" border=0> </a>
        			</td>
        		</tr>
        		<tr>
          		<td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          		<td class="L" colspan=3>
           			<input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          		</td>
        		</tr>
        </table>
      </tr>
		<%}%>

    <!-- Quarto destinatario + luogo -->
<%  if (Destinatario4.length()>2)
		{%>
      <tr>
        <td class="l">Destinatario n°4</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario4 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[3]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>
		<%}%>

    <!-- Quinto destinatario + luogo -->
<%  if (Destinatario5.length()>2)
		{%>
      <tr>
        <td class="l">Destinatario n°5</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario5 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[4]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>
		<%}%>

    <!-- Sesto destinatario + luogo -->
<%  if (Destinatario6.length()>2)
		{%>
      <tr>
        <td class="l">Destinatario n°6</td>
        <td class="L" colspan=3>
         <table>
           <tr >
           <td class="l"> Autorità </td>
           <td class="l" >
             <select title="Destinatario" name="<%=ICostantiPenaAccessoria.CAMPO_COD_DESTINATARIO%>"  >
               <%= Destinatario6 %>
             </select>
           </td>
           </tr>
           <tr>
           <td class="l">Sede <font class=ob>(*)</font></td>
           <td class="l">
             <input Title="Sede " name="<%=ICostantiPenaAccessoria.CAMPO_SEDE%>"
              value="" type="text" maxlength="35" size="35" >
              <a href="Javascript:ListaComuni('LoadComunicazione','<%=ICostantiPenaAccessoria.CAMPO_SEDE%>[5]');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
        </tr>
        <tr>
          <td class="l">Indirizzo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L" colspan=3>
           <input title="Indirizzo" name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
          </td>
        </tr>
        </table>
      </tr>
		<%}%>

      <!-- Campo Note + campo hidden -->
      <tr>
          <td class="l">Nota 1</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_AGGIUNTIVO %>"  cols=80 rows=1 ></textarea>
          </td>
          <input name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>
      <tr>
          <td class="l">Nota 2</td>
          <td class="L" colspan=3>
           <TEXTAREA title="Note" name="<%= ICostantiPenaAccessoria.CAMPO_AGGIUNTIVO %>"  cols=80 rows=1 ></textarea>
          </td>
          <input name="<%=ICostantiPenaAccessoria.CAMPO_NOTE%>" value="" type="hidden" >
      </tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" value="<%=IdPenaAccessoria%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA%>" value="<%=CodTipoPenaAccessoria%>" >
    <input type="HIDDEN" name="<%=ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA%>" value="<%=DescrTipoPenaAccessoria%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadComunicazione");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

  </body>
</html>