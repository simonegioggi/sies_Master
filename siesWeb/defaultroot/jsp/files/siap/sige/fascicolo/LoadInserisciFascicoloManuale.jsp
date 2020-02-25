<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.richiesta.action.ICostantiRichiestaSige"  %>
<%@ page import="siap.sige.detenzione.action.ICostantiFasSigeDetenzione"  %>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.richiesta.model.RichiestaSigeModel"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="mittenteAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="posGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idLuogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDfascicoloSIEP" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDSoggetto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>

<%
// in questa maniera la data non viene istanziata se null !!
Date dataFinePena = (Date)request.getAttribute("dataFinePena");
%>
<html>
  <head>
    <title>[S.I.E.S.] - Load Inserisci Procedimento SIGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>

 
    <script language="JavaScript">
      function Verify()
      {
        if (document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value;
        if (document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value;

        if (document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value;
        if (document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value;

        // Controllo campo chiave anno.
        var anno_sistema='<%=DateUtils.getSysDate("yyyy")%>'
        var chiave_anno=document.LoadInserisciFascicoloManuale.<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>.value;

        if(chiave_anno > anno_sistema)
        {
          alert("Il Campo Anno SIGE non può superare l'anno corrente");
          return false;
        }

        // Controllo chiave anno minimo.
        var anno_minimo='1990'
        if(chiave_anno < anno_minimo)
        {
          alert("Il Campo Anno SIGE non è valido");
          return false;
        }

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadInserisciFascicoloManuale.<%= ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>[document.LoadInserisciFascicoloManuale.<%= ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;
        var modalita='<%=modalita%>';
        if(tipoAtto =='-' && modalita!='M')
        {
          alert("Il Campo Tipo Atto è obbligatorio");
          return false;
        }

        // Controllo della data atto solo se valorizzata.
        var data_atto=document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        if ( data_atto!='//')
        {
          if (! ControllaData(data_atto))
          {
            alert('Data emissione atto non valida');
            return false;
          }
          // Controllo della data atto <= data di sistema
          if (! CompareDate(data_atto, data_sistema))
          {
            alert('Data emissione atto non può essere successiva alla data odierna');
            return false;
          }
        }

        // Controllo della data fine pena solo se valorizzata.
        var data_finepena=document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>.value;
        if ( data_finepena!='//')
        {
          if (! ControllaData(data_finepena))
          {
            alert('Data fine pena non valida');
            return false;
          }
          // Controllo della data fine pena => data di sistema
          // STUB 17/01/2005 aggiunta richiesta di proseguimento.
          if ( !CompareDate( data_sistema, data_finepena ))
          {
            if(! confirm("Data fine pena precede Data odierna ! Si vuole continuare ?" ) )
              return false;
          }
        }


        // Controllo della data Arrivo in Cancelleria
        var data_Arrivo=(document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>.value)+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>.value;
        if (! ControllaData(data_Arrivo))
        {
          alert('Data di Arrivo in Cancelleria non valida');
          return false;
        }

        // Controllo della data Arrivo <= data di sistema
        if (! CompareDate(data_Arrivo, data_sistema))
        {
          alert('Data di Arrivo in Cancelleria non può essere successiva alla data odierna');
          return false;
        }

        // Controllo della data atto <= data Arrivo
        if (( data_atto!='//') &&
            (! CompareDate(data_atto, data_Arrivo)))
        {
          alert('Data atto non può essere successiva alla data Arrivo in Cancelleria');
          return false;
        }
        
        // Controlli Data Iscrizione
        var data_iscrizione =(document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value)+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_ISCRIZIONE%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_ISCRIZIONE%>.value;
        
        if (! ControllaData(data_iscrizione))
        {
          alert('Data di Iscrizione non valida');
          return false;
        }
        var data_limite = '31/12/'+ chiave_anno;
        if (CompareDate(data_sistema, data_limite))
        	data_limite = data_sistema;
        
         // Controllo della data Iscrizione <= data limite
        if (! CompareDate(data_iscrizione, data_limite))
        {
          alert('Data di Iscrizione non può essere successiva al ' + data_limite);
          return false;
        }
 		// Controllo della data Iscrizione >= data_Arrivo      
        if (! CompareDate(data_Arrivo, data_iscrizione))
        {
          alert('Data di Iscrizione non può essere precedente al ' + data_Arrivo);
          return false;
        }
        
      return true;
      }
    </script>
    <script language="JavaScript">   
    function ListaMagistrati(a_formname)
    {
     var a_codnum = "";

     var desktop;
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname+"&codnum="+a_codnum, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    } 
 </script>    
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        Date lDataArrivo = new Date();
        String lAction = new String();
        FascicoloSigeModel lFascicolo = new FascicoloSigeModel();
        RichiestaSigeModel lRichiesta = new RichiestaSigeModel();
        MagistratoModel lMagistrato = new MagistratoModel();
        
        if (FascicoloSigeEsteso.getFascicoloSige() != null)
        	lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
        if (FascicoloSigeEsteso.getRichiestaSige() != null)
        	lRichiesta = FascicoloSigeEsteso.getRichiestaSige();
        if (FascicoloSigeEsteso.getMagAssegnatario() != null && FascicoloSigeEsteso.getMagAssegnatario().getMagistrato() != null)
        	lMagistrato = FascicoloSigeEsteso.getMagAssegnatario().getMagistrato();
      
        // Si consente la modifica della DATA FINE PENA e POS. GIURIDICA in assenza di titolo esecutivo.
        String lDisable = "";  
		String lDisablePosGiu = "";
		String lReadOnly = "";
		
		// Iscrizione Manuale da Fascicolo SIEP
        if( modalita.equals("IF") )
        {
          lAction = "siap.sige.fascicolo.action.ActInserisciFascicoloManuale";
          lDisablePosGiu = "disabled";
          lReadOnly="readonly";
%>			
          <font class="campo">Iscrizione Procedimento SIGE da Procedimento SIEP</font>
<%
        } // Iscrizione Manuale da Soggetto
        else if( modalita.equals("IS") )
        {
            lAction = "siap.sige.fascicolo.action.ActInserisciFascicoloManuale";
%>
          <font class="campo">Iscrizione Procedimento SIGE da Soggetto</font>
<%
        } // Modifica
        else if( modalita.equals("M") )
        {
       		dataFinePena = lFascicolo.getDataFinePena();
         
        	// Si consente la modifica della DATA FINE PENA e POS. GIURIDICA in assenza di titolo esecutivo.
        	if (FascicoloSigeEsteso.getFascicoloSiep() != null ) 
        	{
   				lDisablePosGiu = "disabled";
          		lReadOnly = "readonly";
      		}
        	lAction = "siap.sige.fascicolo.action.ActModificaFascicolo";
        	lDisable="readonly";
%>
      		<font class="campo">Modifica Procedimento SIGE  </font>
<%  
		} // endif modalita
%>
      </td>
      
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
      
    </tr>
  </table>

<%
  if( modalita.equals("IF") )
  {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
  }
  if( modalita.equals("IS") )
  {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
  }
  if( modalita.equals("M") )
  {
%>
    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      <table cellspacing=0 cellpadding=0 width=95%>
              <jsp:include page="/jsp/files/siap/sige/fascicolo/IncludeFasSiepRif.jsp"/>   	 
      </table>
 <%
  }
%>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicoloManuale">

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Fine pena </font>

          <input type="text" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"dd"))%>" type="text" maxlength="2" size="2"    onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  <%=lReadOnly%>>
          /
          <input type="text" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"MM"))%>" type="text" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" <%=lReadOnly%>>
          /
          <input type="text" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"yyyy"))%>" type="text" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)"  <%=lReadOnly%>>

<%
	  // MEV 15 - Revisione SIGE
	  if(lReadOnly == ""){
%>
			<a href="javascript:calendario('LoadInserisciFascicoloManuale','<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>','<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
     
        <font class="l">&nbsp;&nbsp;Pos. Giuridica </font>
<%
        if( lDisablePosGiu.equals("disabled"))
        {
%>
       <input type=hidden name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" value=<%=posGiuridica%>>
<%
        }
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSige.CAMPO_COD_POSIZIONE_GIURIDICA%>" <%=lDisablePosGiu%> >
            <%=posizioneGiuridica%>
          </select>
      </td>
    </tr>

<%
    // Gestione del luogo detenzione.
    if(  (luogoDetenzione.trim().length() > 0)  && (idLuogoDetenzione.trim().length() > 0 || idLuogoDetenzione.trim().length() > 0))
    {
%>
      <tr>
        <td class="L">
          <font class="label">Detenuto in &nbsp;&nbsp;&nbsp;</font>
          <font class="campo"><%=luogoDetenzione%> &nbsp;&nbsp;&nbsp;&nbsp;</font>
          <input type=checkbox name="<%=ICostantiFascicoloSige.CAMPO_VALIDA_LUOGO_DET%>" value=1 title="Valida il Luogo Detenzione" <%=lDisable%>>
        </td>
      </tr>
<%
    }
%>

  </table>

  <br>
   <table style="width: 95%;">
   <tr>
      <td class="Titolo" colspan=6> Estremi Atto </td>
   </tr>
</table>
  <table cellspacing="2" cellpadding="2">

  <tr>
    <td class="l">Tipo Atto <font class=ob>(*)</font></td>
    <td class="L">
      <select title="tipoAtto" class=small name="<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_ATTO%>">
        <%= tipoAtto %>
      </select>
    </td>
  </tr>

  <tr>
    <td class="l">Data Atto </td>
    <td class="L">
      <input  value="<%=modalita.equals("M") ? StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataEmissione(),"dd")): ""%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      /
      <input  value="<%=modalita.equals("M") ? StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataEmissione(),"MM")): ""%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      /
      <input  value="<%=modalita.equals("M") ? StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataEmissione(),"yyyy")): ""%>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

	  <!-- MEV 15 - Revisione SIGE -->
	  <a href="javascript:calendario('LoadInserisciFascicoloManuale','<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE%>');">
          <img src="/images/calendario.gif" border=0>
      </a>
    </td>
  </tr>

  <tr>
    <td class="l">Mittente </td>
    <td class="L">
      <select title="mittenteAtto" class=small name="<%=ICostantiRichiestaSige.CAMPO_COD_TIPO_RICHIEDENTE%>">
        <%= mittenteAtto%>
      </select>
      &nbsp;&nbsp;
<%
      if( modalita.equals("M") )
      {
%>
        <input Title="descrMittente" value="<%=StringUtils.toStringJSP(lRichiesta.getDescRichiedente(), "")%>" name="<%=ICostantiRichiestaSige.CAMPO_DESC_RICHIEDENTE%>"type="text" maxlength="200" size="38">
        <%}else{%>
        <input Title="descrMittente" name="<%=ICostantiRichiestaSige.CAMPO_DESC_RICHIEDENTE%>"type="text" maxlength="200" size="38">
        <%}%>
    </td>
  </tr>

  <tr>
    <td class="l">Sede Mittente </td>
    <td class="l">
      <input Title="Sede Mittente" name="<%=ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE %>"
         value="<%=StringUtils.toStringJSP( lRichiesta.getDescrSedeRichiedente())%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciFascicoloManuale','<%=ICostantiRichiestaSige.CAMPO_SEDE_RICHIEDENTE%>');">
        <img src="/images/filefolder.gif" border=0></a>
     </td>
  </tr>
    <tr>
    <td class="l">Data arrivo in cancelleria <font class=ob>(*)</font></td>
    <td class="L">
      <input  value="<%=modalita.equals("M") ? StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataArrivoCancelleria(),"dd")): "" %>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
      /
      <input  value="<%=modalita.equals("M") ? StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataArrivoCancelleria(),"MM")): "" %>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
      /
      <input  value="<%=modalita.equals("M") ? StringUtils.toStringJSP(DateUtils.getDateToString(lRichiesta.getDataArrivoCancelleria(),"yyyy")): "" %>" type="text" name="<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

	  <!-- MEV 15 - Revisione SIGE -->
	  <a href="javascript:calendario('LoadInserisciFascicoloManuale','<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>','<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>');">
      	  <img src="/images/calendario.gif" border=0>
      </a>
    </td>
  </tr>
  
</table>
 <br>
   <table style="width: 95%;">
   <tr>
      <td class="Titolo" colspan=6> Estremi Procedimento  </td>
   </tr>
</table>
 <table cellspacing="2" cellpadding="2">
     <tr>
       <td class="lRosso">Anno/Numero <font class=ob>(*)</font></td>
 
       <td class="l">
         <input Title="Anno SIGE"  type="text" name="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4">/
         <input Title="Numero SIGE" type="text" name="<%= ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6">
       </td>
     </tr>
    <% if (!modalita.equals("M")) {%>    
   <tr>
    <td class="l">Data Iscrizione <font class=ob>(*)</font></td>
    <td class="L">
      <input  value="" type="text" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_ISCRIZIONE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      /
      <input  value="" type="text" name="<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
      /
      <input  value="" type="text" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_ISCRIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

	  <!-- MEV 15 - Revisione SIGE -->
	  <a href="javascript:calendario('LoadInserisciFascicoloManuale','<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_ISCRIZIONE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_ISCRIZIONE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_ISCRIZIONE%>');">
     	  <img src="/images/calendario.gif" border=0>
      </a>
    </td>
  </tr>
 <%} %>    
     <tr>
       <td class="l">Magistrato</td>
       <td class="L">
         <input title="Cognome" readonly type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" value="<%=modalita.equals("M") ? StringUtils.toStringJSP(lMagistrato.getCognome()) : ""%>" maxlength="35" size="25">
         <input title= "Nome" readonly  type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"   value="<%=modalita.equals("M") ? StringUtils.toStringJSP(lMagistrato.getNome()) : ""%>" maxlength="35" size="25">
 <%if( ! modalita.equals("M") ) {%>
  
         <a href="Javascript:ListaMagistrati('LoadInserisciFascicoloManuale');">
           <img src="/images/filefolder.gif" border=0>
         </a>
<%}%>         
       </td>
     </tr>

    <tr>
      <td class="l">Sezione </td>
      <td class="L" >
        <select title="sezione" class=small name="<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>" >
        <option value = ""  />-
         <%=elencoSezioni%>
        </select>
    </tr>
      <tr>
      <td class="l">Tipo rito </td>
      <td class="L" >
        <select title="TipoGiudizio" class=small name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>" >
		<option value = ""  />-
		<%=tipoGiudizio %>       
 		</select>
    	</tr>

  <tr>
    <td class="l">Note</td>
    <td class="l">
      <Textarea Title="Note" name="<%= ICostantiFascicoloSige.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lFascicolo.getNote()) %></textarea>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiRichiestaSige.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=IDfascicoloSIEP%>"  >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSige.CAMPO_SOG_ID_SOGGETTO%>" value="<%=IDSoggetto%>"  >
  <input type="HIDDEN" name="<%=ICostantiFasSigeDetenzione.CAMPO_LD_ID_LUOGO_DETENZIONE%>" value="<%=idLuogoDetenzione%>">
  <input type="HIDDEN" name="<%=ICostantiFasSigeDetenzione.CAMPO_AC_ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
  <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"  value="">

</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicoloManuale");

    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaSige.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno Data Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_FINE_PENA %>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_FINE_PENA%>","minlen=4","La lunghezza del campo Anno Data Fine Pena Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_ISCRIZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_ISCRIZIONE %>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_ISCRIZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_ISCRIZIONE%>","minlen=4","La lunghezza del campo Anno Data Iscrizione deve essere di 4 caratteri");
   	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_GIORNO_DATA_ISCRIZIONE%>","req", "Il campo Giorno Data Iscrizione è obbligatorio");
  	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_MESE_DATA_ISCRIZIONE%>","req", "Il campo Mese Data Iscrizione è obbligatorio");
  	frmvalidator.addValidation("<%=ICostantiFascicoloSige.CAMPO_ANNO_DATA_ISCRIZIONE%>","req", "Il campo Anno Data Iscrizione è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","req", "Il campo Anno Fascicolo è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSige.CAMPO_CHIAVE_PROGR%>","req", "Il campo Numero Fascicolo è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSige.CAMPO_CHIAVE_ANNO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Giorno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_GIORNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Mese Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_MESE_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","req", "Il campo Anno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRichiestaSige.CAMPO_ANNO_DATA_ARRIVO_CANCELLERIA%>","minlen=4","La lunghezza del campo Anno Data Arrivo in cancelleria deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>