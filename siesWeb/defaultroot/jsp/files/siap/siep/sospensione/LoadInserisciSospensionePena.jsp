<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.magistratocompetente.action.ICostantiMagistratoCompetente"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"%>


<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>




<jsp:useBean id="posizione"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione" scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="decretoordinanza" scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>


<jsp:useBean id="tipoprovvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="uffMagistrato"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="uffTDS"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="Istituto"   scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel"/>

<jsp:useBean id="eventonotifica"   scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="codiceAutoritaE" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv" scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="CodTipoProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodMotivo" scope="request" class="java.lang.String"/>


<%@ page import="java.util.Collection"%>

<% 
Collection tiporegistroordinanza =(Collection) request.getAttribute("tiporegistroordinanza");
%>

<%
  //Inizializzazione campi se SOSPENSIONE non trovata
  //(per evitare eventuale NullPointerException)
  if(sospensione.getIdSospensione() == null)
    sospensione.setQuantumZero();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Sospensione dell'esecuzione della pena</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;

      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaUDS(a_formname,a_fieldname)
     {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
     }

      function ListaComuniTds(formname,fieldname)
     {
       desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
     }

     function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
     {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
     }

    function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }

    function Verify()
    {
      //DATA RICEZIONE PROVVEDIMENTO (non obbligatoria)
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaDataPassaVuota(data_to_verify) )
      {
        alert('Data emissione provvedimento non valida');
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>.focus();

        return false;
      }

      //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
      if (document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
        document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
      if (document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
        document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

      var data_to_verify = document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.f.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.f.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data trasmissione provvedimento non valida');
        document.f.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>.focus();

        return false;
      }
    }
    </script>
  </head>
  <body class="corpo" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Decreto Sospensione pena </font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciSospensionePena">
    <input type="HIDDEN" name="idEventoGenerato" value="<%=decretoordinanza.getIdEventoGenerato()%>">
    <input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
    <input type="HIDDEN" name="CodTipoProvvedimento" value="<%=CodTipoProvvedimento%>">
    <input type="HIDDEN" name="CodMotivo" value="<%=CodMotivo%>">

<%
    String lIdDecretoOrdinanza = "";
    if(flagdecretoordinanza.equals("S"))
    {
      lIdDecretoOrdinanza = "" + decretoordinanza.getIdDecretoOrdinanzaSiep();
    }
%>
    <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP%>" value="<%=lIdDecretoOrdinanza%>">

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=8>
        <font class="campo">
          <%=StringUtils.toStringJSP(posizione.getDescrPosizioneGiuridica())%>
        </font>
      </td>
    </tr>
<%
  if( posizione.getCodPosizioneGiuridica()!=null
    && !posizione.getCodPosizioneGiuridica().equals("46"))
  {
    if( flagergastolo.equals("N") )
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

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
<%
      }
    }

    if( (!posizione.getCodPosizioneGiuridica().equals("07")
      && !posizione.getCodPosizioneGiuridica().equals("10")) )
    {
%>
        <tr>
<%
          if(   flagergastolo.equals("N")
             && penaresidua.getDataInizio() != null )
          {
%>
            <td class="l">Data Decorrenza Pena</td>
            <td class="L">
              <font class="campo">
                <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
              </font>
            </td>
<%
          }

/*
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S"))) && penaresidua.getDataFinePresunta() != null)
        {
*/
%>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
<td class="l">Data Fine Pena Automatica</td>
<td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
--%>
<%
/*
       }
*/
         if (  flagergastolo.equals("N")
            && penaresidua.getDataFine()!=null
            )
         {
           String lClassTd="l";
           String lClassFont="campo";
           if(penaresidua.getDataFine() != null
             && !penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
           {
             lClassTd="lRosso";
             lClassFont="lRosso";
           }
%>
             <td class="l">Data Fine Pena</td>
             <td class="<%=lClassTd%>">
               <font class="<%=lClassFont%>">
                 <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd-MM-yyyy") )%>&nbsp;
               </font>
             </td>
<%
         }
%>
          </tr>
<%
    }
  }
  else //POSIZIONE = 46 (Libero in sospensione)
  {
    if ( (new BigDecimal(0)).compareTo(sospensione.getNumAnniPenaEspiata()) != 0 ||
         (new BigDecimal(0)).compareTo(sospensione.getNumMesiPenaEspiata()) != 0 ||
         (new BigDecimal(0)).compareTo(sospensione.getNumGiorniPenaEspiata()) != 0
        )
    {
%>
      <tr>
        <td class="l">Pena Espiata</td>
        <td class="l" colspan=2>
          <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaEspiata(), "0")%>&nbsp;</font>
          <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaEspiata(), "0")%>&nbsp;</font>
          <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaEspiata(), "0")%></font>
        </td>
        <td class="l">Multa</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toEuroFormat(sospensione.getMultaEspiata())%>
          </font>&nbsp;
          <font class="l">Euro</font>
        </td>
        <td class="l">Ammenda</td>
        <td class="l" colspan=2>
          <font class="campo">
            <%=StringUtils.toEuroFormat(sospensione.getAmmendaEspiata())%>
          </font>&nbsp;
          <font class="l">Euro</font>
        </td>
      </tr>
<%
    }

    if(flagergastolo.equals("N"))
    {
%>
      <tr>
        <td colspan=9 class="titolo">Pena Residua</td>
      </tr>
<%
      if ( (new BigDecimal(0)).compareTo(sospensione.getNumAnniPenaResiduaReclus()) != 0 ||
           (new BigDecimal(0)).compareTo(sospensione.getNumMesiPenaResiduaReclus()) != 0 ||
           (new BigDecimal(0)).compareTo(sospensione.getNumGiorniPenaResiduaReclus()) != 0
          )
      {
%>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaReclus(), "0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaReclus(), "0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaReclus(), "0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2>
            <font class="campo">
              <%=StringUtils.toEuroFormat(sospensione.getMultaResidua())%>
            </font>&nbsp;
            <font class="l">Euro</font>
          </td>
        </tr>
<%
      }

      if( (new BigDecimal(0)).compareTo(sospensione.getNumAnniPenaResiduaArres()) != 0 ||
          (new BigDecimal(0)).compareTo(sospensione.getNumMesiPenaResiduaArres()) != 0 ||
          (new BigDecimal(0)).compareTo(sospensione.getNumGiorniPenaResiduaArres()) != 0
         )
      {
%>
        <tr>
          <td class="l">Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumAnniPenaResiduaArres(), "0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumMesiPenaResiduaArres(), "0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(sospensione.getNumGiorniPenaResiduaArres(), "0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2>
            <font class="campo">
              <%=StringUtils.toEuroFormat(sospensione.getAmmendaResidua())%>
            </font>&nbsp;
            <font class="l">Euro</font>
          </td>
        </tr>
<%
      }
    }
%>
    <tr>
      <td class="l">Data Inizio</td>
      <td class="l" colspan=2>
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(sospensione.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;
        </font>
      </td>
    </tr>
<%
  }

  if (  flagergastolo.equals("S") )
  {
%>
    <tr>
      <td class="l">Pena Complessiva</td>
      <td class="L">
        <font class="campo">ERGASTOLO&nbsp;</font>
      </td>
    </tr>
<%
    }else if(flagergastolo.equals("D"))
     {
%>
      <tr>
        <td class="l">
          <font class="label">Pena Complessiva</font>
        </td>
        <td class="l">
          <font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font>
        </td>
      </tr>
<%
     }
%>
  </table>
  <br>
  <table width="100%" >
    <tr>
      <td colspan ="2" class="titolo">Riepilogo Dati del provvedimento di sospensione dell'esecuzione</td>
    </tr>
    <tr>
      <td class="l">
        Data sospensione esecuzione
      </td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataSospensioneEsecuzione(),"dd-MM-yyyy"))%>&nbsp;
          &nbsp;
<%
          if( decretoordinanza.getFlagScarcerareScarcerato()!=null
             && decretoordinanza.getFlagScarcerareScarcerato().equals("S")
             )
          {
            out.print("[Già scarcerato]");
          }
          else if(decretoordinanza.getFlagScarcerareScarcerato()!=null
                && decretoordinanza.getFlagScarcerareScarcerato().equals("D"))
          {
            out.print("[Da scarcerare]");
          }
%>
        </font>
      </td>
    </tr>

    <tr>
      <td class="l" width="28%" >
        Autorità emittente
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
        di
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">
        Contenuto provvedimento
      </td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(decretoordinanza.getDescrOggettoDecisione())%></font>&nbsp;
      </td>
    </tr>

  <tr>
    <td class="l">
      Motivazioni
    </td>
    <td class="l">
     <font class="campo"><%=StringUtils.toStringJSP( decretoordinanza.getMotivazioni() )%>&nbsp;</FONT>
    </td>
  </tr>
</table>
<table width="100%">
    <tr>
      <td colspan ="2" class="titolo">Dati del provvedimento </td>
    </tr>

   <tr>
      <td class="l">
        Data emissione
      </td>
<%if(eventonotifica != null && eventonotifica.getEvento() != null && eventonotifica.getEvento().getDataEmissione()!= null)
{%>
       <td class="l">  <input type="text" Title="Giorno emissione " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"dd"))%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Mese emissione " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"MM"))%>" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Anno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataEmissione(),"yyyy"))%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

<%}else{%>

       <td class="l">  <input type="text" Title="Giorno emissione " value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd"))%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Mese emissione " value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM"))%>" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Anno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy"))%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%}%>
    </tr>
<tr>
      <td class="l">
        Data trasmissione
      </td>
<%if(eventonotifica != null && eventonotifica.getEvento() != null && eventonotifica.getEvento().getDataTrasmissioneAtti()!= null)
{%>

        <td class="l">   <input type="text" Title="Giorno trasmissione "value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"dd"))%>" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Mese trasmissione " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"MM"))%>" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Anno trasmissione " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(eventonotifica.getEvento().getDataTrasmissioneAtti(),"yyyy"))%>" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%}else{%>
        <td class="l">   <input type="text" Title="Giorno trasmissione "value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("dd"))%>" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Mese trasmissione " value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("MM"))%>" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" Title="Anno trasmissione " value="<%=StringUtils.toStringJSP(DateUtils.getSysDate("yyyy"))%>" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
<%}%>

    </tr>
  <tr>
   <td class="l">Magistrato Competente
   <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25" readonly>
       <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25" readonly>
        <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
  </tr>

<%
if(!posizione.getCodPosizioneGiuridica().equals("10") &&  !posizione.getCodPosizioneGiuridica().equals("07"))
{


if(posizione.getCodPosizioneGiuridica().equals("04") ||  posizione.getCodPosizioneGiuridica().equals("02"))
{
%>

<tr>
      <td colspan ="2" class="titolo">Autorita di Polizia per l' Esecuzione</td>
    </tr>
<tr>
          <td class="l" >Autorita di Polizia</td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
             <%=codiceAutoritaE%>

             </select>

           </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">

        <input title="Sede Autorita Esterna"  value="<%=StringUtils.toStringJSP(autoritaE.getDescrSede())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
</tr>

<%}else{%>
<tr><td>&nbsp;</td></tr>
   <tr>
      <td colspan ="2" class="titolo">Istituto di Detenzione per l'Esecuzione</td>
    </tr>

 <tr>
     <td class="l" >Istituto di Detenzione </td>
  <td class="l">

 <%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>

              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

<%}else {%>

<%if(Istituto!= null && !Istituto.getDescrTipoIstituto().equals("")){%>
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(Istituto.getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(Istituto.getDescrComune())%>" size=50>
             <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=Istituto.getIdIstitutoDetenzione()%>" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

<%}else{%>
              <input readonly Title="Istituto" name="Comune" value="" size=50>
             <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

     <%}%>
<%}%>
</td>
</tr>
<tr><td>&nbsp;</td></tr>

<%}
}
if(posizione.getCodPosizioneGiuridica().equals("10") || posizione.getCodPosizioneGiuridica().equals("07"))
{

%>

    <tr>
      <td colspan ="2" class="titolo">Ufficiali giudiziari per notifica al condannato</td>
    </tr>
<tr>
<!--autorità di polizia-->
          <td class="l" >Ufficiali giudiziari </td>
          <td class="L">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
             <%=codiceAutoritaE%>

             </select>

           </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">

        <input title="Sede Autorita Esterna"  value="<%=StringUtils.toStringJSP(autoritaC.getDescrSede())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
</tr>
<tr><td>&nbsp;</td></tr>
<!--autorità di polizia-->
    <tr>
      <td colspan ="2" class="titolo">Autorita di Polizia per la restituzione dell'Ordine di Esecuzione</td>
    </tr>
<tr>
          <td class="l" >Autorita di Polizia</td>
          <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
             <%=codiceAutoritaE%>

             </select>

           </tr>
    <tr>
      <td class="l">Sede</td>
     <td class="L">

        <input title="Sede Autorita Esterna"  value="<%=StringUtils.toStringJSP(autoritaE.getDescrSede())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
</tr>
<tr><td>&nbsp;</td></tr>
<%}%>
<%
if(!posizione.getCodPosizioneGiuridica().equals("04") &&  !posizione.getCodPosizioneGiuridica().equals("02"))
{
%>
    <tr>
      <td class="Titolo" colspan=8>Ufficiali giudiziari per notifica al difensore </td></tr>


<%
  int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>

        <table>


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
         <table>
          <tr><td class="l">Autorità Destinazione </td >
          <td class="L">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>
        <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=20 rows=5 ></textarea>
       </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','<%=ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
   lIdxAvv++;
  }
}
%>

  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify()">
    </td>
  </tr>
</table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");


  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","req","Il campo Giorno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","req","Il campo Mese Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","req","Il campo Anno Trasmissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");


  </script>
</body>
</html>