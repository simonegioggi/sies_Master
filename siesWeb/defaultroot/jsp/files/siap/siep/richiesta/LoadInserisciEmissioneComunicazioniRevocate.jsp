<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>

<jsp:useBean id="evento"               	scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="annotazioneManuale"   	scope="request" class="java.util.Vector"/>
<jsp:useBean id="codice"               	scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEsterna"      	scope="request" class="java.lang.String" />
<jsp:useBean id="posizioneGiuridica"   	scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistratocompetente" 	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="partenza"             	scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"			scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per l'inserimento dei dati per la produzione della Comunicazione nel
// caso di Depenalizzazione/Incostituzionalità (Revoca sentenza). Viene utilizzata 
// sia nel caso delle 'Richieste al GE' che  nel caso delle 'Decisioni del GE'
// - evento = Richiesta o Provvedimento di concessione
// - annotazioneManuale = elenco delle annotazioni legate alla Richiesta/Decisione
// - codice = Codice motivo della comunicazione da inserire
// codice:
// - 0299 Richieste al GE
// - 0300 Decisioni del GE
//==============================================================================
%>

<%
  AnnotazioneManualeModel lAnnManApp = new AnnotazioneManualeModel();
  if( !annotazioneManuale.isEmpty() )
  {
    lAnnManApp = (AnnotazioneManualeModel)annotazioneManuale.firstElement();
  }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

<script language="JavaScript">

 function Verify()
	{
     if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il  Magistrato Assegnatario è obbligatorio");
           document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
        return false;
      }

<%-- MERGE v10: commento modificato
     if(document.f.< %= ICostantiRichiesta.CAMPO_SEDE_TDS%>.value=="") {
        alert("La Sede del Tribunale di Sorveglianza è obbligatoria");
        document.f.< %= ICostantiRichiesta.CAMPO_SEDE_TDS%>.focus();
        return false;
      }
--%>

     // Tribunale o Ufficio di Sorveglianza
      if(document.f.<%=ICostantiRichiesta.AUTORITA_DESTINATARIO%>.value=="-" &&
   		document.f.<%=ICostantiRichiesta.AUTORITA_SEDE%>.value!="")
      {
        alert("Dati Destinatario Sorveglianza Incompleti.\n Inserire il Tipo Autorita'.");
        document.f.<%=ICostantiRichiesta.AUTORITA_DESTINATARIO %>.focus();
        return false;
      }
      if(document.f.<%=ICostantiRichiesta.AUTORITA_DESTINATARIO %>.value!="-" && 
 		  document.f.<%=ICostantiRichiesta.AUTORITA_SEDE%>.value=="")
      {
        alert("Dati Destinatario Sorveglianza Incompleti.\n Inserire la sede.");
        document.f.<%=ICostantiRichiesta.AUTORITA_SEDE%>.focus();
        return false;
      }
      
     if(document.f.Autorita1.value=="-" && document.f.Sede1.value!="")
      {
        alert("Dati primo Destinatario Incompleti.\n Inserire il Tipo Autorita'.");
        document.f.Autorita1.focus();
        return false;
      }
     if(document.f.Autorita1.value!="-" && document.f.Sede1.value=="")
      {
        alert("Dati primo Destinatario Incompleti.\n Inserire la sede.");
        document.f.Sede1.focus();
        return false;
      }
     if(document.f.Autorita2.value=="-" && document.f.Sede2.value!="")
      {
        alert("Dati secondo Destinatario Incompleti.\n Inserire il Tipo Autorita'.");
        document.f.Autorita2.focus();
        return false;
      }
     if(document.f.Autorita2.value!="-" && document.f.Sede2.value=="")
      {
        alert("Dati secondo Destinatario Incompleti.\n Inserire la sede.");
        document.f.Sede2.focus();
        return false;
      }

      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
        alert('Data di emissione non valida');
			  return false;
		  }

      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
		  if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
			  document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

		  var data_to_verify_trasm = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

      if (!ControllaData(data_to_verify_trasm) )
		  {
        alert('Data di Trasmissione atti non valida');
        return false;
		  }
		  return true;
	}

  var desktop;
  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }

  function ListaComuniTds(formname,fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
  function ListaTDS_UDS(a_formname,a_fieldname)
  {
       var valore = document.f.<%=ICostantiRichiesta.AUTORITA_DESTINATARIO%>.value;
       var i = document.f.<%=ICostantiRichiesta.AUTORITA_DESTINATARIO%>.selectedIndex;

       if ( i == 2 )
       {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
       }
       else
       {
	        if ( i == 1 )	    		   
       		{
         		desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
       		}
       }     
  }
  
</script>

  <title>[S.I.E.S.] - Emissione Comunicazione</title>

</head>
  <body class="corpo">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
          <font  class="label">Funzione :&nbsp;</font>
          <%if(codice.equals("0300")){%>
          <font class="campo">Revoca Sentenza per Abolizione Reato</font>
          <%}else if(codice.equals("0299")){%>
          <font class="campo">Nuovo Residuo Pena </font>
          <%}%>
        </td>
        
        <%if(codice.equals("0300")){%>
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniBenefici" >
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
        <%}else{%>
        <td class="LBG">
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.annotazionemanuale.action.ActLoadStampeAnnotazioniRichieste" >
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
        <%}%>
      </tr>
    </table>
    
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
  <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciEmissioneComunicazioni">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
    <input type="HIDDEN" name="codice" value="<%=codice%>">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(lAnnManApp.getIdAnnotazioneManuale())%>">
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(lAnnManApp.getEveIdEvento())%>">
    <input type="HIDDEN" name="partenza" value="<%=StringUtils.toStringJSP(partenza)%>">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=codice%>">

  <table>
    <tr>
      <td class="l">Posizione Giuridica</td>
      <td class="L">
        <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font>
      </td>
    </tr>
  </table>
  <table>
    <tr>
      <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

      <td class="l">Data Trasmissione</td>
<%
      if(evento!= null && evento.getDataRicezioneAtti()!= null)
      {
%>
        <td class="L">
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataTrasmissioneAtti(),"dd"))%>"   type="text" size="2" maxlength="2" name="<%=  ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataTrasmissioneAtti(),"MM"))%>"   type="text" size="2" maxlength="2" name="<%=  ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataTrasmissioneAtti(),"yyyy"))%>" type="text" size="4" maxlength="4" name="<%=  ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI  %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
<%
      }
      else
      {
%>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
<%
      }
%>
    </tr>
  </table>
  
  <table style="width: 95%;">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan=8 class="titolo">Richiesta</td>
    </tr>
  </table>
<%
  int lIdxAvv = 0;
  Iterator lItxAvv = annotazioneManuale.iterator();
  while(lItxAvv.hasNext())
  {
    AnnotazioneManualeModel lAnn =  (AnnotazioneManualeModel)lItxAvv.next();
%>
  <table>
    <tr>
      <td class="l">Data Richiesta</td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataGE(),"dd-MM-yyyy"))%>
        </font>
      </td>

    <%
    //==========================================================================
    // 0122 - Richiesta Applicazione amnistia / indulto (COD_MOTIVO_EVENTO)
    // 002  - Richiesta Applicazione amnistia / indulto (COD_TIPO_ANNOTAZIONE)
    // 003  - Richiesta Applicazione amnistia / indulto (COD_TIPO_ANNOTAZIONE)
    //??????????????????????????????????????????????????????????????????????????
    // Per Amnistia e indulto la jsp è un'altra!!!
    //==========================================================================
    if(   "0122".equals(evento.getCodMotivo())
       || "002".equals(lAnnManApp.getCodTipoAnnotazione())
       || "003".equals(lAnnManApp.getCodTipoAnnotazione())
       )
    {
    %>
      <td class="l">DPR Richiesto</td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getDescrDpr())%>
        </font>
      </td>
    <%}%>
    </tr>

    <%
    //==========================================================================
    // 0211 - richiesta Applicazione Incostituzionalita' (COD_MOTIVO_EVENTO)
    // 013 - richiesta Applicazione Incostituzionalita'  (COD_TIPO_ANNOTAZIONE)
    //==========================================================================
    if(   "0211".equals(evento.getCodMotivo())
       || "013".equals( lAnnManApp.getCodTipoAnnotazione() ))
    {
    %>
      <tr>
        <td class="l" width="45%">Sentenza Corte Costituzionale</td>
        <td class="l" >
          Anno/Numero
          <font class="campo">
     <%if(lAnn.getAnnoCc() == null || lAnn.getAnnoCc().compareTo(new BigDecimal(0))==0){%>
            -
     <%}else{%>
            <%=StringUtils.toStringJSP(lAnn.getAnnoCc() )%>
     <%}%>
       / <%=StringUtils.toStringJSP(lAnn.getNumeroCc(), "-")%>
          </font>
        &nbsp; in data
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnn.getDataCC(), "dd-MM-yyyy"))%>&nbsp;
          </font>
        </td>
      </tr>
    <%}%>


    <%
    //==========================================================================
    // 0210 - Richiesta Applicazione Depenalizzazione (COD_MOTIVO_EVENTO)
    // 004  - Richiesta Applicazione Depenalizzazione (COD_TIPO_ANNOTAZIONE)
    //==========================================================================
    if(  "0210".equals(evento.getCodMotivo())
      || "004".equals(lAnnManApp.getCodTipoAnnotazione())
   	// MEV 37 - Inizio	
   	  || "017".equals(lAnnManApp.getCodTipoAnnotazione())  // ILLECITO AMMINISTRATIVO
   	// MEV 37 - Fine      
      )
    {
    %>
      <tr>
        <td class="l" width="20%">Fonte</td>
      <td class="l" >
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getDescrFonte())%></font>
         Anno&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getAnnoFonte())%></font>
        Num.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getNumeroFonte())%></font>
        Art.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getArticolo())%></font>
        </td>
      <td class="l" >Art.Qualificante
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getDescrSottonumerazione())%></font>
         Comma&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getComma())%></font>
        Let.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getLettera())%></font>
        Num.&nbsp;
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnn.getNumero())%></font>
        </td>
      </tr>
    <%}%>


        </table>
<%
    lIdxAvv++;
  }
%>

  <table style="width: 95%;">
    <tr>
      <td class="Titolo" width="95%" colspan=6> Magistrato Assegnatario </td>
    </tr>
    <tr>
      <td class="l">Magistrato Assegnatario
      <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input title= "Nome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
  
<%
//==============================================================================
//            S E Z I O N E    C O N    I   D E S T I N A T A R I
//==============================================================================
%>  
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan=6>Comunicazione per</td>
    </tr>
    <%-- MERGE v10: commento modificato
    <tr>
      <td class="l">Destinatario</td>
      <td class="L">TRIBUNALE DI SORVEGLIANZA</td>
        <input type="hidden" value="TDS" name="<%=ICostantiRichiesta.CAMPO_TDS%>">
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE_TDS %>"  cols=20 rows=5 ></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td><td class="L">
        <input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiRichiesta.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniTds('f','<%= ICostantiRichiesta.CAMPO_SEDE_TDS %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    --%>

    <tr>
    <td class="l" width="10%">Destinatario</td >
    <td class="L" width="50%">
      <select title="tipoUfficioSIUS" name="<%=ICostantiRichiesta.AUTORITA_DESTINATARIO %>">
	        <%= tipoUfficio %>
	  </select>
	</td>  
 
    <td rowspan=2 class="l" width="5%">Note</td>
    <td rowspan=2 class="L" width="30%">
      <TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE_TDS %>" cols=40 rows=5 ></textarea>
    </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Tribunale Sorveglianza"  type="text" name="<%= ICostantiRichiesta.AUTORITA_SEDE %>"  maxlength="35" size="35">
        <a href="Javascript:ListaTDS_UDS('f','<%= ICostantiRichiesta.AUTORITA_SEDE %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
     </tr>
          <!--Rework GDV 27-11-06 - Aggiungo due destinatari-->
    <tr>
      <td class="l" width="10%">Destinatario</td>
      <td class="L" width="50%" >
        <select Title="Autorità Destinatario" name="Autorita1" >
          <%=autoritaEsterna%>
        </select>
      </td>
      <td rowspan=2 class="l" width="5%">Note</td>
      <td rowspan=2 class="L" width="30%">
        <TEXTAREA title="Note" name="Note1" cols=40 rows=5 ></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Destinatario 1" value="" type="text" name="Sede1"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','Sede1');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

    <tr>
      <td class="l" width="10%">Destinatario</td>
      <td class="L" width="50%">
        <select Title="Autorità Destinatario" name="Autorita2" >
          <%=autoritaEsterna%>
        </select>
      </td>
      <td rowspan=2 class="l" width="5%">Note</td>
      <td rowspan=2 class="L" width="30%">
        <TEXTAREA title="Note" name="Note2" cols=40 rows=5 ></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede  Destinatario 2" value="" type="text" name="Sede2"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','Sede2');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

<%-- MERGE v10: commento modificato
frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_TDS %>","req","Luogo Tribunale di Sorveglianza obbligatoria");
frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_TDS %>","alphabetic");
--%>

<%--   frmvalidator.addValidation("<%=ICostantiRichiesta.AUTORITA_SEDE%>","alphabetic"); --%>
//   frmvalidator.addValidation("Sede1","alphabetic");
//   frmvalidator.addValidation("Sede2","alphabetic");
</script>
</body>
</html>