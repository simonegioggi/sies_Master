<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Collection"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>


<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>

<jsp:useBean id="posizione"            scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione"          scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="decretoordinanza"     scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="flagergastolo"        scope="request" class="java.lang.String"/>


<jsp:useBean id="autoritaemittente"    scope="request" class="java.lang.String"/>
  
  
<jsp:useBean id="motivointerruzione"   scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante"      scope="request" class="java.lang.String" />


<%
//==============================================================================
// Form per l'inserimento dei dati delle Interruzioni disposte dal PM o dal GE
//==============================================================================
%>

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
    <title>[S.I.E.S.] - Interruzione esecuzione pena</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;

      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ceck()
      {
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked == true)
        {
            document.f.flagDecisione.value="S";
        }else
       {
            document.f.flagDecisione.value="N";
       }
      }
      
      function Verify()
      {
        //DATA RICEZIONE PROVVEDIMENTO (non obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data ricezione provvedimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO %>.focus();

          return false;
        }

        //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione provvedimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>.value+'-'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>.value+'-'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data interruzione pena non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA %>.focus();

          return false;
        }
      return true;
      }
    </script>
  </head>
  
  
<body class="corpo" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=lbg>
         <font  class="label">Funzione :&nbsp;</font>
       <font class="campo">Interruzione esecuzione pena</font>
      </td>
    </tr>
  </table>
    
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
    
  <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciInterruzione">
    
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

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
  //============================================================================
  // Sezione per la visualizzazione della Pena Corrente
  //============================================================================
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
  
  <table style="width: 95%;">
    <tr>
      <td colspan=2 class="titolo">Interruzione esecuzione Pena</td>
    </tr>
    <tr>
      <td class="l">
        Data interruzione esecuzione <font class="ob">(*)</font>
      </td>
      <td class="l">
        <input type="text" Title="Giorno interruzione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataInterruzionePena(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        -
        <input type="text" Title="Mese interruzione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataInterruzionePena(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        -
        <input type="text" Title="Anno interruzione esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataInterruzionePena(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
    </tr>
    <tr>
      <td class="l">
        Data comunicazione evento
      </td>
      
      <%
        java.util.Date lData = DateUtils.getSysDate();
        
        if(decretoordinanza.getDataEmissioneProvvedimento()!=null)
        {
          lData =decretoordinanza.getDataEmissioneProvvedimento();
        }
      %>
      <td class="l">
        <input type="text" Title="Giorno comunicazione evento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lData, "dd") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        -
        <input type="text" Title="Mese comunicazione evento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lData, "MM") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        -
        <input type="text" Title="Anno comunicazione evento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lData, "yyyy") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
    </tr>
    <tr>
      <td class="l">
          Data ricezione evento
      </td>
      <%
      if(decretoordinanza.getDataRicezioneProvvedimento()!=null)
      {
        lData = decretoordinanza.getDataRicezioneProvvedimento();
      }
      %>
      <td class="l">
        <input type="text" Title="Giorno ricezione evento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lData , "dd") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        -
        <input type="text" Title="Mese ricezione evento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lData , "MM") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        -
        <input type="text" Title="Anno ricezione evento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lData , "yyyy") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO %>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
    </tr>
    <tr>
      <td class="l"> Protocollo </td>
      <td class="l">
        <input type="text" Title="Protocollo" value="<%=StringUtils.toStringJSP(decretoordinanza.getProtocollo())%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_PROTOCOLLO %>" maxlength="35" size="35" >
      </td>
    </tr>
    <tr>
      <td class="l">
        Autorità emittente  
      </td>
      <td class="l">
        <input type="text" Title="Autorita Esterna" value="<%=StringUtils.toStringJSP(decretoordinanza.getAltraAutorita())%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>" maxlength="35" size="35" >
      </td>
    </tr>
    <tr>
      <td class="l">
         Luogo 
      </td>
      <td class="l">
          <input title="Sede Autorita Esterna"  type="text" value="<%=StringUtils.toStringJSP( decretoordinanza.getAltroLuogo() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
      </td>
    </tr>
    <tr>
      <td class="l">
        Motivo dell'interruzione <font class="ob">(*)</font>
      </td>
      <td class="l">
        <select Title="Motivo dell'interruzione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>">
        <%=motivointerruzione%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">
        Motivazioni
      </td>
      <td class="l">
        <textarea cols="60" rows="2" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MOTIVAZIONI%>"><%=StringUtils.toStringJSP( decretoordinanza.getMotivazioni() )%></textarea>
      </td>
    </tr>

    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");

    //frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo è obbligatorio");
    //frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità è obbligatorio");

  // Data comunicazione evento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=3000");

  // Data ricezione evento
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno ricezione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno ricezione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>","lt=3000");

  // Data interruzzione esecuzione
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","req","Il campo Giorno interruzione  è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_INTERRUZIONE_PENA%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","req","Il campo Mese differimento  è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_INTERRUZIONE_PENA%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","req","Il campo Anno interruzione  è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","maxlen=4","La lunghezza massima per l'anno interruzione  è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","minlen=4","La lunghezza minima per l'anno interruzione  è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_INTERRUZIONE_PENA%>","lt=3000");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>