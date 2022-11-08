<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
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
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="penaComplessivaSanzioneSostitutiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaC"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="tipoIstituto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="StrdataFinePenaA"    scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="istanza"            scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="posizione"          scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="detenutoAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioMagistrato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="altracausaposizionegiuridica"       scope="request" class="siap.siep.altracausa.model.AltraCausaModel"/>
<jsp:useBean id="dataeditabile"       scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="verbale"   scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="tipoUfficio"   scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratosorveglianza"    scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="Cssa"       scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="liberazione"       scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel"/>
<jsp:useBean id="fascicolo"       scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="lTotGiorniConcessi"     scope="request" class="java.lang.String"/>
<jsp:useBean id="liberazioninonconcesse"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="VetLicenze" 				scope="request" class="java.util.Vector"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>
<%-- MEV10-s3: aggiunto useBean --%>
<jsp:useBean id="codiceTipoUfficio" scope="request" class="java.lang.String"/>

<%
BigDecimal lIdEventoOrdinanza    = (BigDecimal) request.getAttribute("lIdEventoOrdinanza");

String FlagIstanza="";
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
  
//20/05/2014 - Nuova L.A.
LicenzaLibAnticipataModel lLiceModel = new LicenzaLibAnticipataModel();

Iterator IteLic = VetLicenze.iterator();

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
      var desktop;

      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
    </script>
    <script language="JavaScript">

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

      function Verify()
      {
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

        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
          document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
          document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

        var data_to_verify = document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciOrdineScarcerazione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

        var campo = document.LoadInserisciOrdineScarcerazione.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

        //Controllo se Data Provvedimento della maschera è diversa da data di sistema e uguale o superiore a DataIrrevocabilità(TAB_Sentenza)
        if( document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("dd")%> ||
            document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("MM")%> ||
            document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value != <%=DateUtils.getSysDate("yyyy")%>)
        {
          if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value  < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value)
          {
            alert("La data del Provvedimento può essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilità");
            return false;
          }
          else  if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value  == document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value)
            if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value)
            {
              alert("La data del Provvedimento può essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilità");
              return false;
            }
            else if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value == document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value)
              if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value < document.LoadInserisciOrdineScarcerazione.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value)
              {
                alert("La data del Provvedimento può essere diversa dalla data di sistema solo se è uguale o superiore alla data Irrevocabilità");
                return false;
              }
        }

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

        if(  (document.LoadInserisciOrdineScarcerazione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.value=="-")
          && (document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>.value=="-")
          && (document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>.value=="-")
          && (document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="-")
          && (document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value=="-")
          )
        {
          alert("Indicare almeno un destinatario");
          return false;
        }

        if(  (document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value!="-")
          && (document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value=="-")
          )
        {
          alert("Non è possibile indicare l'autorità senza indicare la sede");
          return false;
        }

/*
        if(document.LoadInserisciOrdineScarcerazione.<%=ICostantiCSSA.CAMPO_ID_CSSA %>.value=="" || document.LoadInserisciOrdineScarcerazione.<%=ICostantiCSSA.CAMPO_ID_CSSA %>.value=="-")
        {
           alert("L'UEPE/USSM Competente è obbligatorio");
           return false;
        }

        if (document.LoadInserisciOrdineScarcerazione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value == "")
        {
           alert("L'Autorità Destinazione è obbligatoria");
           return false;
        }
*/
      }

      function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

      function ListaComuniTds(formname,fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaCSSA(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
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
        lAzione = "siap.siep.ordinescarcerazione.action.ActInserisciOSLiberazioneAnticipataMA";
%>
<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {%>
   <font class="campo">Emissione Ordine di Scarcerazione a seguito di ordinanza Liberazione Anticipata in regime alternativo - DETENUTO PER ALTRA CAUSA</font>
        <%}else{%>
       <font class="campo">Emissione Ordine di Scarcerazione a seguito di ordinanza Liberazione Anticipata in regime alternativo - <%=lPosizione.getDescrPosizioneGiuridica()%></font>
<%}%>
     </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciOrdineScarcerazione" action="<%= IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordinescarcerazione.action.ActInserisciOSLiberazioneAnticipataMA">
    <input type="HIDDEN" name="posizionegiuridica" value="<%=lPosizione.getDescrPosizioneGiuridica()%>">
    <input type="HIDDEN" name="flagmisura" value="<%=flagmisura%>">
    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">
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
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
            </tr>
<%
          }
        }
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getCodTipoIstituto()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO%>  maxlength="6" size="6" --%>
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
<%
     if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
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
}
%>

<%
    // if((!lPosizione.getCodPosizioneGiuridica().equals("07") && !lPosizione.getCodPosizioneGiuridica().equals("10")) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
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
          }
          else
          {
%>
             <td class="l">Data Fine Pena</td>
             <td class="lRosso" colspan=2>
               <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
               <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onBlur="javascript:value=FillYear(value)">
            </td>
<%
          }
        }
      }
%>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
      <td class="l">Data Emissione</td>
      <td class="L" colspan=2 >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>
       <td class="l">Data Trasmissione</td>
      <td class="L" colspan=2>
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
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
       <td class="l" height="30">Anno / Numero SIUS</td>
            <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(liberazione.getAnnoSius())%> /<%=StringUtils.toStringJSP(liberazione.getNumeroSius())%>
            </font></td>

          <td class="l" height="30"> Anno / Numero Ordinanza </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(liberazione.getAnnoOrdinanza())%>/<%=StringUtils.toStringJSP(liberazione.getNumeroOrdinanza())%>
         </font></td>
       </tr>
 
  	<tr>
		<td class="l" height="30">Autorità emittente</td>
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
 
 <!-- 
    <tr>
      <td class="l" height="30">
        Autorità emittente
      </td>
      <td class="l" colspan=3>
        <font class="campo">
          <--%=StringUtils.toStringJSP(liberazione.getDescrUfficioEmittente())%>&nbsp;
        </font>
        di
        <font class="campo">
          <--%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>
        </font>
      </td>
    </tr>
 -->
</table>

<!-- 	20/05/2014 Nuova Ordinanza L.A. : Suddivisione delle diverse tipologie di giorni concessi -->
 <table width="90%">   
    <tr>
      <td width="10%" class="l" height="30">Giorni Concessi</td>
      <td width="5%"class="l" colspan=3>
        <font class="campo">
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
      <td class="l" height="30">Data Emissione Ordinanza</td>
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

<table>
     <%if(magistrato != null){%>

   <tr>
     <td class="Titolo" colspan=6> Magistrato </td>
   </tr>
  <tr>
   <td class="l">Magistrato
   <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciOrdineScarcerazione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
  </tr>
<tr><td>&nbsp;</td></tr>

<%}%>
<tr><td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>    <tr>
            <td class="l" colspan="3" >Per Avvocato&nbsp;
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
<%
    lIdxAvv++;
  }
%>
  <tr><td>&nbsp;</td></tr>
  
  <!-- INIZIO MODIFICA -->
	<tr>
		<td class="Titolo" colspan="6">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="10%">Destinatario <font class=ob>(*)</font></td>
		<td class="l" width="50%"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
		<td rowspan=2 class="l">Note</td>
    	<td rowspan=2 class="L">
      		<textarea title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA %>"  cols=20 rows=5 ></textarea>
    	</td>
	</tr>
	<tr>
		<td class="l">Sede</td>
		<td class="l">
			<!-- <input type="hidden" name="cssa" value="S"> -->
			<input readonly title="Sede UEPE Competente" name="Indirizzo" size="60"
					value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>">
			<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>"
					value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35>
				<a href="Javascript:ListaCSSAMinor('LoadInserisciOrdineScarcerazione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
					<img src="/images/filefolder.gif" border=0>
				</a>
		</td>
	</tr>
<!--  
  <tr>
    <td class="Titolo" colspan=6> Notifica UEPE </td>
  </tr>
  <tr>
    <td class="l">UEPE Competente</td>
    <td class="l">
      <input type="hidden" name="cssa" value="S">
      <input type="hidden" name="notifica" value="C">
      <input readonly Title="UEPE Competente" name="Indirizzo" value="//=StringUtils.toStringJSP(daticssa.getComune()) //StringUtils.toStringJSP(daticssa.getIndirizzo())%>" size=60 >
      <input type="hidden" Title="UEPE Competente" name="//=ICostantiCSSA.CAMPO_ID_CSSA %>" value="=//StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
      <a href="Javascript:ListaCSSA('LoadInserisciOrdineScarcerazione','//=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td rowspan=2 class="l">Note</td>
    <td rowspan=2 class="L">
      <TEXTAREA title="Note" name="//= ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA %>"  cols=20 rows=5 ></textarea>
    </td>
  </tr>
 -->   
  <tr><td>&nbsp;<td></tr>
 
  	<tr>
		<td class="Titolo" colspan="6">Ufficio / Magistrato di Sorveglianza</td>
	</tr>
  	<tr>
    	<td class="l" width="10%">Destinatario <font class=ob>(*)</font></td>
    	<td class="l"><%=MinorMask.comboMagistratoTrattino()%></td>
      	<td rowspan=2 class="l">Note</td>
    		<td rowspan=2 class="L">
      			<TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_UDS%>"  cols=20 rows=5 ></textarea>
    		</td>
    </tr>
    <tr>
    	<td class="l">Sede</td>
    	<td class="L">
       		<input title="Sede Ufficio Sorveglianza" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>"  maxlength="35" size="35">
       		<a href="Javascript:ListaComuniMagiSorvMinor('LoadInserisciOrdineScarcerazione','<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
				<img src="/images/filefolder.gif" border=0>
			</a>
      	</td>
   	</tr>

 <!-- 
  <tr>
    <td class="Titolo" colspan=6>Ufficio preposto alla gestione della misura alternativa</td></tr>
  <tr>
    <td class="l">Ufficio di Sorveglianza</td>
    <td class="L">
      <input type="hidden" name="magSorv" value="S">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratosorveglianza.getCodMagistrato() )%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>"  maxlength="35" size="35" --%>
      <input type="hidden" name="notificaMagistrato" value="C">
      <input title="ufficio" value="<-%=UffUDS%>" type="text" name="<-%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>" maxlength="35" size="25">
      <a href="Javascript:ListaUDS('LoadInserisciOrdineScarcerazione','<-%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
        <img src="/images/filefolder.gif" border=0>
      </a>
    </td>
    <td rowspan=2 class="l">Note</td>
    <td rowspan=2 class="L">
      <TEXTAREA title="Note" name="<--%=ICostantiMisuraAlternativa.CAMPO_NOTE_UDS%>"  cols=20 rows=5 ></textarea>
    </td>
  </tr>
 -->

  <tr><td>&nbsp;<td></tr>

	<tr>
		<td class="Titolo" colspan="6">Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="10%">Destinatario <font class=ob>(*)</font></td>
        <!-- <input type="hidden" value="TDS" name="<--%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
        <input type="hidden" name="tds" value="S">  -->
        <td class="L"><%=MinorMask.comboTribunaleTrattino()%></td>
     	<td rowspan=2 class="l">Note</td>
    	<td rowspan=2 class="L">
      		<TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_TDS %>"  cols=20 rows=5 ></textarea>
      		<input type="hidden" name="notificaTribunale" value="C">
    	</td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
		<!-- <input type="hidden" value="TDS" name="<--%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>"> -->
		<td class="L">
	       	<input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
	       	<a href="Javascript:ListaComuniTribSorvMinor('LoadInserisciOrdineScarcerazione','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
	       		<img src="/images/filefolder.gif" border=0>
	       	</a>
		</td>
	</tr>

	<tr><td>&nbsp;<td></tr>
<!-- 
  <tr>
    <td class="Titolo" colspan=6>Tribunale di Sorveglianza che ha emesso l'ordinanza di misura alternativa</td>
  </tr>
  <tr>
    <td class="l">Destinatario</td >
    <td class="L">TRIBUNALE DI SORVEGLIANZA</td>
      <input type="hidden" value="TDS" name="<--%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
      <input type="hidden" name="tds" value="S">
    <td rowspan=2 class="l">Note</td>
    <td rowspan=2 class="L">
      <TEXTAREA title="Note" name="<--%= ICostantiMisuraAlternativa.CAMPO_NOTE_TDS %>"  cols=20 rows=5 ></textarea>
      <input type="hidden" name="notificaTribunale" value="C">
    </td>
  </tr>
<%
   // if(liberazione.getDescrLuogoEmittente()!= null)
    //{
%>
      <tr>
        <td class="l">Sede </td>
        <td class="L">
          <input title="Sede Tribunale Sorveglianza" value="<--%=StringUtils.toStringJSP(liberazione.getDescrLuogoEmittente())%>" type="text" name="<--%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuniTds('LoadInserisciOrdineScarcerazione','<--%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
<%
  //}
  //else
  //{
%>
    <tr>
      <td class="l">Sede </td>
      <td class="L">
        <input title="Sede Tribunale Sorveglianza" value="<--%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>" type="text" name="<--%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniTds('LoadInserisciOrdineScarcerazione','<--%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%
  //}
%>
 -->
 <!-- FINE MODIFICA	 -->

  <tr><td class="Titolo" colspan=6>Istituto di Detenzione </td></tr>
  <tr>
    <td class="l">Autorità Destinazione</td>
<%
    if(lLuogoDetenzione.getIstitutoDetenzione() == null)
    {
%>
      <td class="l">
      <input readonly Title="Istituto" name="Comune" value="" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a></td>
<%
  }
  else
  {
%>
      <td class="l">
      <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
      <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
      <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciOrdineScarcerazione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border=0></a></td>
<%
  }
%>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_IST%>" cols=20 rows=5></textarea>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="Titolo" colspan=6>Autorità di Polizia competente per territorio</td></tr>
    <tr>
      <td class="l">Autorità Esecuzione</td>
      <td class="L">
        <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
          <%=autoritaEsternaE%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>" cols=20 rows=5></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciOrdineScarcerazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>
<table>
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

  frmvalidator.addValidation("<%= ICostantiMagistrato.CAMPO_COGNOME%>","req","Il Cognome del Magistrato è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMagistrato.CAMPO_NOME%>","req","Il Nome del Magistrato è obbligatorio");
/*
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>","req","La Sede del Tribunale di Sorveglianza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS%>","req","L'Ufficio di Sorveglianza è obbligatorio");

  frmvalidator.addValidation("<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>","req","La Sede Autorità di Polizia competente per territorio è obbligatoria");
*/
</script>
</body>
</html>