<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>
<jsp:useBean id="penacomplessiva"    scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="tipoIstituto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>

<jsp:useBean id="magistratocompetente"   scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="liberazione"            scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="lTotGiorniConcessi"     scope="request" class="java.lang.String"/>
<jsp:useBean id="liberazioninonconcesse" scope="request" class="java.util.Vector"/>

<jsp:useBean id="lFascicoloAssociato"   scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="Licenze" 				scope="request" class="java.util.Vector"/>

<%

  BigDecimal lIdEventoOrdinanza = (BigDecimal) request.getAttribute("lIdEventoOrdinanza");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
  // 20/05/2014 - Nuova L.A.
  LicenzaLibAnticipataModel lLiceModel = new LicenzaLibAnticipataModel();
  
  Iterator IteLic = Licenze.iterator();
  
  int totOldLA = 0;
  int totggLA = 0;
  int totggLS = 0;
  int totggLI = 0;
  boolean NuovaLA = false;
  
  while(IteLic.hasNext())
  {
	  	lLiceModel = (LicenzaLibAnticipataModel)IteLic.next();
	  	if(lLiceModel.getFlagConcesso() != null && 
		 	lLiceModel.getFlagConcesso().compareTo("C") == 0 )
	  	{
	  		if(lLiceModel.getDescrStatoPermesso() != null)
	  		{
	  			if(lLiceModel.getDescrStatoPermesso().compareTo("LA") == 0)
	  			{
	  				NuovaLA = true;
	  				totggLA += lLiceModel.getNumeroGiorni().intValue();	
	  			}
	  			else if(lLiceModel.getDescrStatoPermesso().compareTo("LS") == 0)
	  		  	{
	  		  		NuovaLA = true;
	  		  		totggLS += lLiceModel.getNumeroGiorni().intValue();	
	  		  	}
	  			else if(lLiceModel.getDescrStatoPermesso().compareTo("LI") == 0)
	  			{
	  				NuovaLA = true;
	  			  	totggLI += lLiceModel.getNumeroGiorni().intValue();	
	  			}
	  			else
	  			{
	  			}
	  		}
	  		else
	  		{	
	  			totOldLA += lLiceModel.getNumeroGiorni().intValue(); 
	  		}	
	  	}
  }
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

   function Verify()
    {

     if(document.LoadInserisciComunicazioneLibero.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Il Cognome del Magistrato è obbligatorio");
        return false;
      }
      if(document.LoadInserisciComunicazioneLibero.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Nome del Magistrato è obbligatorio");
        return false;
      }

      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
       document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
       document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        return false;
      }

      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
       document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
       document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciComunicazioneLiberoLoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      data_to_verify = document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciComunicazioneLibero.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di trasmissione non valida');
        return false;
      }

      if(document.LoadInserisciComunicazioneLibero.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-")
      {
        alert("Autorità Destinazione obbligatoria");
        return false;
      }
    }

    var desktop;

    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    // Chiamata lista Avvocati.
    function ListaAvvocati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function ListaMagistrati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
  </script>
</head>
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Comunicazione Concessione Liberazione Anticipata per Condannato Libero</font>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciComunicazioneLibero" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.libertaanticipata.action.ActInserisciComunicazioneLALibero">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(lIdEventoOrdinanza)%>">
    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
<%
          if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
          {
%>
              DETENUTO PER ALTRA CAUSA
<%
          }
          else
          {
%>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
          }
%>
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
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
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
        }
%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >

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
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo </td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%> maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getCodTipoIstituto()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO%> maxlength="6" size="6" --%>
    <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%> maxlength="6" size="6" --%>
</table>
<table>
  <tr>
<%//fine modifica relativa al tipo istituto
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
    <% if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
          {}else{%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
    </td>
<%
      }
    }
%>

<tr>
  </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
       <td class="l">Data Trasmissione</td>
      <td class="L" colspan=2>
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>
  </table>
  <table width="70%">
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="Titolo" colspan='8'> Dati Ordinanza </td>
    </tr>
    <tr>
      <td class="l">Anno / Numero SIUS</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getAnnoSius())%> /<%=StringUtils.toStringJSP(liberazione.getNumeroSius())%>
        </font>
      </td>
      <td class="l"> Anno / Numero Ordinanza </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getAnnoOrdinanza())%>/<%=StringUtils.toStringJSP(liberazione.getNumeroOrdinanza())%>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l">
        Autorità emittente
      </td>
      <td class="l" colspan=3>
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getDescrUfficioEmittente())%>&nbsp;
        </font>
        di
        <font class="campo">
          <%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>
        </font>
      </td>
    </tr>
  </table>
  
  <table width="90%">   
    <tr>
      <td width="10%" class="l">Giorni Concessi</td>
      <td width="5%"class="l" colspan=3>
        <font class="campo">
          <!-- %=StringUtils.toStringJSP(liberazione.getNumeroGiorni())%-->
         <%= StringUtils.toStringJSP(lTotGiorniConcessi) %>&nbsp;&nbsp;&nbsp;
        </font>
      </td>
<%	if(NuovaLA)
	{ 	%>
		<td class="L">
			<font class="label" style="text-align: center;  font-size: 10pt">
			Di cui :&nbsp; 
			</font>
<%		if(totggLA != 0)
	
		{ %>		
        	<font class="campo"><%= StringUtils.toStringJSP(totggLA)%></font>
			<font class="label" style="text-align: center;  font-size: 10pt">
			 					&nbsp;di Liberazione Anticipata ;&nbsp;
			 </font>					
<%		}

		if(totggLS != 0)
		{	%>    
         	<font class="campo"><%= StringUtils.toStringJSP(totggLS)%></font>
        	<font class="label" style="text-align: center;  font-size: 10pt">
			 					&nbsp; di Liberazione Anticipata Speciale ;&nbsp;
			</font> 
<%		}
		
		if(totggLI != 0)
		{	%>
         	<font class="campo"><%= StringUtils.toStringJSP(totggLI)%></font>
			<font class="label" style="text-align: center;  font-size: 10pt">
			 					&nbsp; di Integrazione Liberazione Anticipata ;&nbsp; 
			</font> 
<%		}
	}
//	%>
		 </td>
    </tr>
</table>

 <table width="70%">    
<%
	Iterator iter = liberazioninonconcesse.iterator();
	while (iter.hasNext())
	{
		LicenzaPeriodiLibAnticipataModel lLibPerMod = (LicenzaPeriodiLibAnticipataModel)iter.next();
		
		if(lLibPerMod != null)
		{
			LicenzaLibAnticipataModel lLibMod = lLibPerMod.getLicenza();
			
			if(lLibMod != null && lLibMod.getFlagConcesso() != null)
			{
				if(lLibMod.getFlagConcesso().equals("R"))
				{
%>
					<tr>
					  <td class="l" height="30" width="20%">Giorni Rigettati</td>
					  <td class="l" colspan=3 width="50%">
					    <font class="campo">
					    
<%-- 
					      <%=StringUtils.toStringJSP(lLibMod.getNumeroGiorni())%>
--%>

<%
								PeriodoLibAnticipataModel[] p = lLibPerMod.getPeriodi();
								for (int i = 0; i < p.length; i++)
								{
%>
		                <font class="l">
		                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
		                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;<br>
		                </font>
<%
		            }
%>
					    </font>
					  </td>
					</tr>
<%
				}
				if(lLibMod.getFlagConcesso().equals("I"))
				{
%>
					<tr>
					  <td class="l" height="30" width="20%">Giorni Inammissibili</td>
					  <td class="l" colspan=3 width="50%">
					    <font class="campo">
					    
<%-- 
					      <%=StringUtils.toStringJSP(lLibMod.getNumeroGiorni())%>
--%>

<%
								PeriodoLibAnticipataModel[] p = lLibPerMod.getPeriodi();
								for (int i = 0; i < p.length; i++)
								{
%>
		                <font class="l">
		                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
		                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;<br>
		                </font>
<%
		            }
%>
					    </font>
					  </td>
					</tr>
<%
				}
				if(lLibMod.getFlagConcesso().equals("N"))
				{
%>
					<tr>
					  <td class="l" height="30" width="20%">Giorni N.L.P./N.D.P.</td>
					  <td class="l" colspan=3 width="50%">
					    <font class="campo">
					    
<%-- 
					      <%=StringUtils.toStringJSP(lLibMod.getNumeroGiorni())%>
--%>

<%
								PeriodoLibAnticipataModel[] p = lLibPerMod.getPeriodi();
								for (int i = 0; i < p.length; i++)
								{
%>
		                <font class="l">
		                  <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
		                  <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;<br>
		                </font>
<%
		            }
%>
					    </font>
					  </td>
					</tr>
<%
				}
			}
		}
	}

    if(liberazione.getDataEmissioneOrdinanza()!= null)
    {
%>
      <tr>
        <td class="l" height="20" >Data Emissione Ordinanza</td>
        <td class="l" colspan=3>
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(liberazione.getDataEmissioneOrdinanza(),"dd-MM-yyyy"))%>
          </font>
        <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(liberazione.getDataEmissioneOrdinanza()))%>">
        <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(liberazione.getDataEmissioneOrdinanza()))%>">
        <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(liberazione.getDataEmissioneOrdinanza()))%>">
     </tr>
<%
    }
%>
    <tr><td>&nbsp;</td></tr>
  </table>
  <table style="width: 95%;">
     <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
     <tr>
     <td class="l">Magistrato <font class=ob>(*)</font></td>
     <td class="L" colspan="3">
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciComunicazioneLibero');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
        <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      </td>
    </tr>
  </table>
  <table style="width: 95%;">
    <tr><td class="Titolo" colspan=6>Destinatario per Esecuzione</td></tr>
    <tr>
      <td class="l" width=20%>Autorità Destinazione <font class=ob>(*)</font></td>
      <td class="L" colspan="3">
        <select  Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
          <%=autoritaEsternaE%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font></td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciComunicazioneLibero','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=30></textarea>
      </td>
    </tr>
    <tr><td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
          <tr>
            <td class="l" colspan=4>Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
          <tr>
            <td class="l">Autorità Destinazione </td >
            <td class="L" colspan=3>
              <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
                <%=autoritaEsternaN%>
              </select>
            </td>
          </tr>
          <tr>
            <td class="l">Sede </td><td class="L">
              <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
              <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
<%
              if(avvocati.size() > 1)
              {
%>
                <a href="Javascript:ListaComuni('LoadInserisciComunicazioneLibero','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
<%
              }
              else
              {
%>
                <a href="Javascript:ListaComuni('LoadInserisciComunicazioneLibero','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
<%
              }
%>
              <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
          <td class="l">Note</td>
          <td class="L">
            <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=35></textarea>
          </td>
        </tr>
        <tr><td>&nbsp;</td></tr>

<%
      lIdxAvv++;
    }
%>
    <tr>
      <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>
</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciComunicazioneLibero");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");
  </script>
</body>
</html>