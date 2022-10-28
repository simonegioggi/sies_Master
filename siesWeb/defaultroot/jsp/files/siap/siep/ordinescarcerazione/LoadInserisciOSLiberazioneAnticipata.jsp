<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>


<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="posizione"          scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="luogoDetenzione"          scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="altracausaposizionegiuridica"       scope="request" class="siap.siep.altracausa.model.AltraCausaModel"/>


<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>


<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<jsp:useBean id="sedeUfficioEmittente"    scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="magistratosorveglianza"  scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>

<jsp:useBean id="fascicolo"               scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>

<jsp:useBean id="liberazione"             scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="lTotGiorniConcessi"      scope="request" class="java.lang.String"/>
<jsp:useBean id="liberazioninonconcesse"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="Licenze"                 scope="request" class="java.util.Vector"/>

<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<jsp:useBean id="codiceAutoritaE"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna"    scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>

<%
BigDecimal lIdEventoOrdinanza    = (BigDecimal) request.getAttribute("lIdEventoOrdinanza");

//20/05/2014 - Nuova L.A.
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
      if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Il Cognome del Magistrato è obbligatorio");
        return false;
      }
      if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Nome del Magistrato è obbligatorio");
        return false;
      }

	  // Istituto di detenzione: se presente è obbligatoria
      if (typeof (document.LoadInserisciOrdineScarcerazione.<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>)!="undefined") 
      {
        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
        {
           alert("L'Istituto di Detenzione è obbligatorio");
           document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.focus();
           return false;
        }
      }

		<%-- MERGE v10: commento codice inutilizzato
		// Autorita' preposta al controllo: se presente è obbligatoria
		if (typeof (document.LoadInserisciOrdineScarcerazione.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>)!="undefined") {
        	if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value == "-") {
        		alert("L'Autorita' preposta al controllo è obbligatoria");
           		document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.focus();
           		return false;
        	}
        	if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value == "") {
        		alert("La Sede Autorita' preposta al controllo è obbligatoria");
           		document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.focus();
           		return false;
        	}
      	}
      	--%>

      var pos = document.LoadInserisciOrdineScarcerazione.codPosizioneGiuridica.value;
      if(pos == "02" || pos == "70" || pos == "71" || pos == "72"){
    	  if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')
	      {
    		 alert("L'Autorita' competente per territorio è obbligatorio");
	         return false;
	      }
    	  if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.value=="")
          {
            alert("La Sede dell'Autorita' competente per territorio è obbligatorio");
            return false;
          }
      } else {
	      if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
	      {
	         alert("L'Istituto di Detenzione è obbligatorio");
	         return false;
	      }
      }
      
<%
      if( liberazione != null && liberazione.getDescrUfficioEmittente() != null
          && liberazione.getDescrUfficioEmittente().toUpperCase().startsWith("TRIB"))
      {
%>
        if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE%>.value=="")
        {
          alert("La Sede del Tribunale di Sorveglianza è obbligatoria");
          return false;
        }
<%
      }
      else
      {
%>
        if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS%>.value=="")
        {
          alert("La Sede dell'Ufficio/Magistrato di Sorveglianza è obbligatorio");
          return false;
        }
<%
      }
%>
      //  Data Emissione
      if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
      var data_to_verify = document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (!ControllaData(data_to_verify) )
      {
         alert('Data di emissione non valida');
         return false;
      }

      // Data Trasmissione
      if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;
      data_to_verify = document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
      if (!ControllaData(data_to_verify) )
      {
         alert('Data di trasmissione non valida');
         return false;
      }
      
      //Controllo se Data Provvedimento della maschera è diversa da data di sistema e uguale o superiore a DataIrrevocabilita'(TAB_Sentenza)
      if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("dd")%> ||
         document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("MM")%> ||
         document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("yyyy")%>)
      {
        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value  < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value)
        {
          alert("La data del Provvedimento puo' essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilita'");
          return false;
        }
        else if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value  == document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value)
         if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value)
         {
           alert("La data del Provvedimento puo' essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilita'");
           return false;
         }
         else if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value == document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value)
           if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value)
           {
            alert("La data del Provvedimento puo' essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilita'");
            return false;
           }
      }
    }

    var desktop;

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

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
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaUDS(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    
  </script>
  
  <jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>
  
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        EventoModel lProvvedimento = new EventoModel();
        String lAzione = new String();
        lProvvedimento = new EventoModel(evento);
        lAzione = "siap.siep.ordinescarcerazione.action.ActInserisciOSLiberazioneAnticipata";
%>
<%
        if(fascicolo.getFlagAltraCausa()!=null && fascicolo.getFlagAltraCausa().equals("S"))
        {%>
          <font class="campo">Ordine di Scarcerazione a seguito di ordinanza Liberazione Anticipata - DETENUTO PER ALTRA CAUSA</font>
        <%}else{%>
          <font class="campo">Ordine di Scarcerazione a seguito di ordinanza Liberazione Anticipata</font>
        <%}%>
     </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  
<FORM method="POST" name="LoadInserisciOrdineScarcerazione" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordinescarcerazione.action.ActInserisciOSLiberazioneAnticipata">
  <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_EVE_ID_EVENTO%>" value="<%=StringUtils.toStringJSP(lIdEventoOrdinanza)%>">
  <input type="HIDDEN" name="codPosizioneGiuridica" value="<%=posizione.getCodPosizioneGiuridica()%>">
  <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=5>
          <font class="campo">
<%
        if(fascicolo.getFlagAltraCausa()!=null && fascicolo.getFlagAltraCausa().equals("S"))
        {
%>
          DETENUTO PER ALTRA CAUSA  <%=altracausaposizionegiuridica.getDescrTipoPosGiuridica()%> </font>

<%      // modifica relativa al tipo istituto
          if(posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione() != null)
         //  if(!lAltraCausa.getDescrTipoIstituto().equals("") && lAltraCausa.getDescrTipoIstituto()!= null && !lAltraCausa.getDescrTipoIstituto().equals("-"))
           {
%>          <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto()%></font>
               di<font class="campo"> <%=posizioneluogoaltra.getAltraCausa().getIstitutoDetenzione().getDescrComune()%></font>
            </td>
           </tr>
           <%if (posizioneluogoaltra.getAltraCausa().getAltroLuogo()!=null)
            { %>
            <tr>
             <td class="l">Altro Luogo </td >
             <td class="L" colspan=5>
              <font class="campo"><%=StringUtils.toStringJSP(posizioneluogoaltra.getAltraCausa().getAltroLuogo())%></font>&nbsp; 
             </td>
            </tr>
          <%} %>
        <%}
     }
     else
     {
%>
       <%=posizione.getDescrPosizioneGiuridica()%>
<%
       if(luogoDetenzione.getIstitutoDetenzione() != null)
       {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=luogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
             di<font class="campo"> <%=luogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
       }
       else //Altro Luogo
       {
         if (luogoDetenzione.getAltroLuogo()!=null)
         {
%>
           <tr>
             <td class="l">Detenuto presso Altro Luogo</td>
             <td class="L" colspan=5>
               <font class="campo"><%=luogoDetenzione.getAltroLuogo()%></font>
             </td>
           </tr>
<%
          }
         }
        }
%>
        </td>
        <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(posizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
      </tr>
<%      // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(posizione.getCodPosizioneGiuridica() != null && (posizione.getCodPosizioneGiuridica().equals("02") || posizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(luogoDetenzione.getIstitutoDetenzione() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo </td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(luogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getCodTipoIstituto()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO%>  maxlength="6" size="6" --%>
<%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>  maxlength="6" size="6" --%>

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
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
        }
%>
<%
        if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
         (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
             (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
          {}else{%>
           <tr>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font>
    </td>
      </tr>
<%
      }
   }
%><tr>
<%
  if((!posizione.getCodPosizioneGiuridica().equals("07") && !posizione.getCodPosizioneGiuridica().equals("10")) || (fascicolo.getFlagAltraCausa()!=null &&  fascicolo.getFlagAltraCausa().equals("S") ) )
   {
   if (penaresidua.getDataInizio() != null)
       {
%>       <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%     }
   }
  if (penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
     }
%>

<%
    // if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")) || (fascicolo.getFlagAltraCausa()!=null &&  fascicolo.getFlagAltraCausa().equals("S") ) )
//{
        if  ((penaresidua.getFlagErgastolo() == null) || ((penaresidua.getFlagErgastolo() != null && penaresidua.getFlagErgastolo().equals("N"))))
        {
           if ( penaresidua.getDataFine() != null)
           {
           if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }else
            {%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>

 <%         }
        }
      }
%>
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
		<td class="l">Autorita' emittente</td>
		<%-- MEV10-s3: aggiunto controllo di prevenzione: per gli uffici PM, PMM e PGCAP la descrizione è differente --%>
	    <%
	    	String descrTipoUfficio = StringUtils.toStringJSP(liberazione.getDescrUfficioEmittente());
	    	if (("PM".equals(codiceTipoUfficio) || "PMM".equals(codiceTipoUfficio) || "PGCAP".equals(codiceTipoUfficio)) &&
	    			"UDSM".equals(liberazione.getCodTipoUfficioEmittente())) {
	    		descrTipoUfficio = "Magistrato di Sorveglianza per i Minorenni";
	    	}
	    %>
        <td class="l" colspan=3>
          	<font class="campo">
            	<%=descrTipoUfficio%>&nbsp;
          	</font>
          	di
          	<font class="campo">
            	<%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>&nbsp;
          	</font>
		</td>
	</tr>
 
 	<%-- 
     <tr>
      <td class="l">
        Autorita' emittente
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
     --%>
</table>

<!--  20/05/2014 Nuova Ordinanza L.A. : Suddivisione delle diverse tipologie di giorni concessi -->
 <table width="90%">   
    <tr>
      <td width="10%" class="l">Giorni Concessi</td>
      <td width="5%"class="l" colspan=3>
        <font class="campo">
          <%= StringUtils.toStringJSP(lTotGiorniConcessi) %>&nbsp;&nbsp;&nbsp;
        </font>
      </td>
<%  if(NuovaLA)
  {   %>
    <td class="L">
      <font class="label" style="text-align: center;  font-size: 10pt">
      Di cui :&nbsp; 
      </font>
<%    if(totggLA != 0)
  
    { %>    
          <font class="campo"><%= StringUtils.toStringJSP(totggLA)%></font>
      <font class="label" style="text-align: center;  font-size: 10pt">
                &nbsp;di Liberazione Anticipata ;&nbsp;
       </font>          
<%    }

    if(totggLS != 0)
    { %>    
          <font class="campo"><%= StringUtils.toStringJSP(totggLS)%></font>
          <font class="label" style="text-align: center;  font-size: 10pt">
                &nbsp; di Liberazione Anticipata Speciale ;&nbsp;
      </font> 
<%    }
    
    if(totggLI != 0)
    { %>
          <font class="campo"><%= StringUtils.toStringJSP(totggLI)%></font>
      <font class="label" style="text-align: center;  font-size: 10pt">
                &nbsp; di Integrazione Liberazione Anticipata ;&nbsp; 
      </font> 
<%    }
  }
//  %>
     </td>
    </tr>
</table>
 
<!--  End  Nuova Ordinanza L.A.-->

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
  
/*
    if(  liberazione.getNumeroGiorni() != null && lTotGiorniConcessi != null
      && !(""+liberazione.getNumeroGiorni()).equals(lTotGiorniConcessi) )
    {
      int lDifferenzaGiorni = new BigDecimal(lTotGiorniConcessi).intValue() - liberazione.getNumeroGiorni().intValue();
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<tr>
  <td class="l">Giorni Concessi con altre Ordinanze</td>
    <td class="l">
  <font class="campo"><%=StringUtils.toStringJSP(""+lDifferenzaGiorni)%></font></td>
</tr>
--%>
<%
/*
    }
*/
    if(liberazione.getDataEmissioneOrdinanza()!= null)
    {
%>
      <tr>
        <td class="l">Data Emissione Ordinanza</td>
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
  <tr>
    <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
    <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd") )%>" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>">
    <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "MM") )%>" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>">
    <input type="HIDDEN" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "yyyy") )%>" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>">
  </tr>
  <tr><td>&nbsp;</td></tr>
</table>


<table style="width: 95%;">
     <tr><td class="Titolo" colspan=6> Magistrato </td></tr>
     <tr>
    <td class="l">Magistrato <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
     <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
     <input readonly title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
     <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
      <a href="Javascript:ListaMagistrati('LoadInserisciOrdineScarcerazione');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
  </tr>
    
<%
//==============================================================================
//                              Destinatari
//==============================================================================
%>
 <tr><td class="Titolo" colspan=6>Destinatari</td></tr>
 <% 
// MEV 29 - aggiunta gestione PG (02,70,71,72) per DL72/2013
	if (posizione != null && ("02".equals(posizione.getCodPosizioneGiuridica())
       || "70".equals(posizione.getCodPosizioneGiuridica())
       || "71".equals(posizione.getCodPosizioneGiuridica())
       || "72".equals(posizione.getCodPosizioneGiuridica())
      )){ %>
<%-- MERGE v10: cancello 1 riga con 2 colonne
<tr>
  <td class="l">Autorita' Preposta al Controllo <font class=ob>(*)</font></td>
  <td class="L">
    <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
      <%=autoritaEsternaE%>
    </select>
  </td>
  <td class="l" rowspan=2>Note</td>
  <td class="L" rowspan=2>
    <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols="35" rows="2"></textarea>
  </td>
</tr>
--%>
	   <tr>
		<!--autorita' di polizia-->
	      <td class="l" width=30%>Autorita' Competente per territorio <font class=ob>(*)</font></td>
	      <td class="L" colspan="3">
	        <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
	         <%=codiceAutoritaE%>
	         </select>
	    </tr>
	    <tr>
	      <td class="l">Sede</td>
	     <td class="L">
	<%if(autoritaEsterna != null && autoritaEsterna.getDescrSede() != null)
	   {%>
	      <input title="Sede Autorita Esterna" value="<%=autoritaEsterna.getDescrSede()%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
	<%}else
	    {%>
	          <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
	  <%}%>
	
	          <a href="Javascript:ListaComuni('LoadInserisciOrdineScarcerazione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
	          <img src="/images/filefolder.gif" border=0>
	        </a>
	      </td>
	           </td>
	           <td class="l">Indirizzo</td>
	           <td class="L">
	              <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols=30 ></textarea>
	            </td>
	    </tr>
<% } else { %>
	  <tr>
	     <td class="l" width=30%>Istituto di Detenzione <font class=ob>(*)</font></td>
	<%if(luogoDetenzione != null &&  luogoDetenzione.getIstitutoDetenzione()!= null && luogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()!= null )
	{%>
	  <td class="l" colspan="2">
	
	              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(luogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
	              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogoDetenzione.getIstitutoDetenzione().getIdIstitutoDetenzione()%>" size=50>
	              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
	              <img src="/images/filefolder.gif" border=0></a>
	
	
	  <%}else {%>
	
	 <td class="l" colspan="3">
	      <input readonly Title="Istituto" name="Comune" value="" size=50>
	      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
	      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
	      <img src="/images/filefolder.gif" border=0></a></td>
	
	  <%}%>
	</tr>
<% } %>

<%--
<tr>
 <td class="l">Istituto di Detenzione <font class=ob>(*)</font></td>
<%if(luogoDetenzione != null &&  luogoDetenzione.getIstitutoDetenzione()!= null && luogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()!= null )
{%>
  <td class="l" colspan="2">
       <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(luogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(luogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
       <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=luogoDetenzione.getIstitutoDetenzione().getIdIstitutoDetenzione()%>" size=50>
       <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
       <img src="/images/filefolder.gif" border=0></a>
  <%//}else {%>
 <td class="l" colspan="3">
      <input readonly Title="Istituto" name="Comune" value="" size=50>
      <input type="hidden"  Title="Istituto" name="<//%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<//%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a></td>

  <%//}%>
</tr>  
<% //} %>
--%>

<!--
   <tr>
    <td class="l" colspan=4>Tribunale di sorveglianza &nbsp;<input type="radio" name="tipo" onclick="radio();" checked>
    &nbsp; Ufficio di Sorveglianza  &nbsp; <input type="radio" name="tipo"    onclick="radio();"></td>
   </tr>
-->

<!-- INIZIO MODIFICA -->
<table style="width: 95%;">
<%
  if( liberazione != null && liberazione.getDescrUfficioEmittente() != null
      && !liberazione.getDescrUfficioEmittente().toUpperCase().startsWith("TRIB")){
%>
 	<tr>
		<td class="Titolo" colspan="4">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
          <%-- <input type="hidden" value="UDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>"> --%>
          <!-- <input type="hidden" name="uds" value="S">  -->
    	<td class="l"><%=MinorMask.comboMagistratoTrattino()%></td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
    	<td class="L">
      		<!-- <input type="hidden" name="magSorv" value="S">
       		<input type="hidden" name="notificaMagistrato" value="C"> -->
       		<input title="Sede Ufficio Sorveglianza" value="" type="text" name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>"  maxlength="35" size="35">
       		<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciOrdineScarcerazione','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
      	</td>
   	</tr>

<% } else { %>

	<tr>
		<td class="Titolo" colspan="4">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario <font class=ob>(*)</font></td>
        <%--
        <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        <input type="hidden" name="tds" value="S">
        --%>
        <td class="L"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
		<input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
		<td class="L">
	       	<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>"  maxlength="35" size="35">
	       	<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciOrdineScarcerazione','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>');">
	       		<img src="/images/filefolder.gif" border=0>
	       	</a>
		</td>
	</tr>

<% } %>

<!-- FINE MODIFICA -->

<%
  //if( liberazione != null && liberazione.getDescrUfficioEmittente() != null
  //    && liberazione.getDescrUfficioEmittente().toUpperCase().startsWith("TRIB"))
  //{
%>
    <tr>
<%
    //if(liberazione.getDescrLuogoEmittente()!= null)
    //{
%>
      <%-- 
      <td class="L">Tribunale di Sorveglianza <font class=ob>(*)</font></td>
        <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        <input type="hidden" name="tds" value="S">
      <td class="L" colspan="3">
        <input title="Sede Tribunale Sorveglianza" value="<%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>" type="text" name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniTds('LoadInserisciOrdineScarcerazione','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
       --%>
<%
    //}
    //else
    //{
%>
      <%-- 
      <td class="L">Tribunale di Sorveglianza <font class=ob>(*)</font></td>
        <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        <input type="hidden" name="tds" value="S">
      <td class="L" colspan="3">
        <input title="Sede Tribunale Sorveglianza" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>" type="text" name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniTds('LoadInserisciOrdineScarcerazione','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_TRIBUNALE %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
       --%>
<%
    //}
%>
    </tr>
<%
  //}
  //else
  //{
%>

    <tr>
<%
      //if(liberazione.getDescrLuogoEmittente()!= null)
      //{
%>
        <%-- 
        <td class="L">Ufficio di Sorveglianza <font class=ob>(*)</font></td>
          <input type="hidden" value="UDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>">
          <input type="hidden" name="uds" value="S">
        <td class="L" colspan="3">
          <input title="Sede Ufficio Sorveglianza" value="<%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>" type="text" name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>"  maxlength="35" size="35">
          <a href="Javascript:ListaUDS('LoadInserisciOrdineScarcerazione','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
         --%>
<%
    //}
    //else
    //{
%>
      <%-- 
      <td class="L">Ufficio di Sorveglianza <font class=ob>(*)</font></td>
        <input type="hidden" value="UDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>">
        <input type="hidden" name="uds" value="S">
      <td class="L" colspan="3">
        <input title="Sede Ufficio Sorveglianza" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>" type="text" name="<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>"  maxlength="35" size="35">
        <a href="Javascript:ListaUDS('LoadInserisciOrdineScarcerazione','<%= ICostantiOrdineScarcerazione.CAMPO_SEDE_UDS %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      --%>
<%
    //}
%>
    </tr>
    
<%
 // }
%>
    <tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table style="width: 95%;">
          <tr>
            <td class="l">Per Avvocato&nbsp;
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
        </table>
         <table style="width: 95%;">
          <tr><td class="l">Autorita' Destinazione </td >
          <td class="L" colspan=3>
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaN%>
             </select>
         </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <% if (avvocati.size()>1) { %>
        <a href="Javascript:ListaComuni('LoadInserisciOrdineScarcerazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
        <% } else { %>
        <a href="Javascript:ListaComuni('LoadInserisciOrdineScarcerazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
        <% } %>
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
        <td class="l">Note</td>
       <td class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=35></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>

<tr>
  <td class="lNoBord" colspan="2">
  <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
  </td>
</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciOrdineScarcerazione");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2099");

  </script>
</body>
</html>