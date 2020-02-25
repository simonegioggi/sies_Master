<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"%>
<%@ page import="siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep"%>

<%
//==============================================================================
// Finestra di Inserimento/Modifica Sospensione Differimento 
// - se presente Decreto o Ordinanza SIUS di sospensione ('0030','0031','0032','0033','0201','0202','0210','0211')
//   non elaborato viene precaricato in maschera.
//==============================================================================
%>

<jsp:useBean id="posizione"            scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="sospensione"          scope="request" class="siap.siep.sospensione.model.SospensioneModel"/>
<jsp:useBean id="flagergastolo"        scope="request" class="java.lang.String"/>

<jsp:useBean id="decretoordinanza"     scope="request" class="siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel"/>
<jsp:useBean id="flagdecretoordinanza" scope="request" class="java.lang.String"/>

<jsp:useBean id="tipoprovvedimento"    scope="request" class="java.lang.String"/>
<% //<jsp:useBean id="tiporegistroordinanza" scope="request" class="java.util.Collection"/> %>

<jsp:useBean id="autoritaemittente"  scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettodecisione"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipologiadecisione" scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante"    scope="request" class="java.lang.String" />

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
/*
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaComuniTds(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
*/
      function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ceck()
      {
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_DECISIONE_TRIBUNALE%>.checked == true)
        {
          document.f.flagDecisione.value="S";
        }
        else
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

        //DATA DIFFERIMENTO (obbligatoria)
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>.value;

        var data_to_verify_dif = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>.value+'/'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data differimento non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO %>.focus();

          return false;
        }


        //DATA rinvio  ()
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO%>.value;
        if (document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RINVIO%>.value.length==1)
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RINVIO%>.value='0'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RINVIO%>.value;

        var data_to_verify = document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO%>.value+'-'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RINVIO%>.value+'-'+document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data rinvio non valida');
          document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO %>.focus();

          return false;
        }
        

<%
// Aggiunta controllo Data Differimento >= data inizio pena se presente
// data inizio pena
if(penaresidua.getDataInizio()!= null){%>
        var giorno_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"dd")%>;
        var mese_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"MM")%>;
        var anno_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"yyyy")%>;

        if(giorno_data_decorrenza_pena < '10' && giorno_data_decorrenza_pena != '' )
        {
            giorno_data_decorrenza_pena ='0'+giorno_data_decorrenza_pena;
        }

        if(mese_data_decorrenza_pena < '10' && mese_data_decorrenza_pena != '' )
        {
           mese_data_decorrenza_pena ='0'+mese_data_decorrenza_pena;
        }

        var data_decorrenza_pena = giorno_data_decorrenza_pena+'/'+mese_data_decorrenza_pena+'/'+anno_data_decorrenza_pena

        if(!CompareDate(data_decorrenza_pena,data_to_verify_dif))
        {
          alert("Data Differimento deve essere superiore o uguale alla data di Inizio Pena");
          return false;
        }
<%}%>
      return true;
  }
    </script>
  </head>
  <body class="corpo" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Differimento / Rinvio dell'esecuzione</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciSospensioneDifferimento">
    <input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
    <input type="HIDDEN" name="dataInizioPena" value="<%=penaresidua.getDataInizio()%>">

<%
    String lIdDecretoOrdinanza = "";
    if(flagdecretoordinanza.equals("S"))
    {
      lIdDecretoOrdinanza = "" + decretoordinanza.getIdDecretoOrdinanzaSiep();
    }
%>
    <input type="HIDDEN" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ID_DECRETO_ORDINANZA_SIEP%>" value="<%=lIdDecretoOrdinanza%>">

<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
//==============================================================================
%>

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
    && !posizione.getCodPosizioneGiuridica().equals("46") && !posizione.getCodPosizioneGiuridica().equals("47"))
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

    if( !posizione.isLibero() )
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

    if(flagergastolo.equals("N") && !posizione.isLibero())
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

if(sospensione.getDataInizio()!= null){
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
<% 
//==============================================================================
//           DATI DEL PROVVEDIMENTO DI SOSPENSIONE DELL'ESECUZIONE
//==============================================================================
%>  
  <br>
  <table style="width: 95%;">
    <tr>
      <td colspan=3 class="titolo">Dati del provvedimento di sospensione dell'esecuzione</td>
    </tr>
    <tr>
      <td class="l">
        Data ricezione provvedimento
        &nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;
        <input type="text" Title="Giorno ricezione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese ricezione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno ricezione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRicezioneProvvedimento(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RICEZIONE_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">
        Anno/Numero registro SIUS
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="text" Title="Anno Registro" value="<%=StringUtils.toStringJSP( decretoordinanza.getAnnoRegistro() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>" size=4 maxlength=4>
        /
        <input type="text" Title="Numero Registro" value="<%=StringUtils.toStringJSP( decretoordinanza.getNumRegistro() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>" size=6 maxlength=6>
    </td>
  </tr>
  <tr>
    <td class="l">
      Data emissione provvedimento <font class="ob">(*)</font>&nbsp;
      <input type="text" Title="Giorno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "dd") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" Title="Mese emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "MM") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      /
      <input type="text" Title="Anno emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataEmissioneProvvedimento(), "yyyy") )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      &nbsp;&nbsp;&nbsp;&nbsp;
      Anno/Numero provvedimento
      <input type="text" Title="Anno Provvedimento" value="<%=StringUtils.toStringJSP( decretoordinanza.getAnnoProvvedimento() )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO %>" size=4 maxlength=4>
      /
      <input type="text" Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP( decretoordinanza.getNumProvvedimento() )%>" name="<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO %>" size=6 maxlength=6>&nbsp;&nbsp;
    </td>
  </tr>
  <tr>
    <td class="l">
      Tipo provvedimento <font class="ob">(*)</font>
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;
      <select Title="Tipo Provvedimento" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
        <%=tipoprovvedimento%>
      </select>
    </td>
  </tr>
</table>
<table style="width: 95%;">
  <tr>
    <td class="l">
      Autorità emittente
    </td>
    <td class="l">
      <select Title="Autorità emittente" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>">
      <%=autoritaemittente%>
      </select>
        Sede <font class="ob">(*)</font>&nbsp;
        <input title="Sede Autorita Esterna"  type="text" value="<%=StringUtils.toStringJSP( decretoordinanza.getDescrLuogoEmittente() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>[document.f.<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.options.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
    </td>
  </tr>
  <tr>
    <td class="l">
      Contenuto decisione <font class="ob">(*)</font>
    </td>
    <td class="l">
      <select Title="Oggetto Decisione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_OGGETTO_DECISIONE%>">
      <%=oggettodecisione%>
      </select>
    </td>
  </tr>
  <tr>
      <td class="l">
          Oggetto decisione <font class="ob">(*)</font>
      </td>
      <td class="l">
          <select Title="Tiipologia Decisione" class="small" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_COD_ESITO%>">
          <%=tipologiadecisione%>
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
</table>
<table style="width: 95%;">
  <tr>
      <td class="l">
        Data differimento esecuzione <font class="ob">(*)</font>
        &nbsp;&nbsp;&nbsp;
        <input type="text" Title="Giorno differimento esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataDifferimento(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       -
        <input type="text" Title="Mese differimento esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataDifferimento(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       -
        <input type="text" Title="Anno differimento esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataDifferimento(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        &nbsp;&nbsp;&nbsp;
<%
        String lCheckScarcerato = "";
        String lCheckDaScarcerare = "";
        if(  decretoordinanza.getFlagScarcerareScarcerato() != null
          && decretoordinanza.getFlagScarcerareScarcerato().equals("D") )
        {
          lCheckDaScarcerare = "checked";
        }
        else if(decretoordinanza.getFlagScarcerareScarcerato() != null
          && decretoordinanza.getFlagScarcerareScarcerato().equals("S") )
        {
          lCheckScarcerato = "checked";
        }
        else
        {
          lCheckDaScarcerare = "checked";
        }
%>
        <input type="radio" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_SCARCERARE_SCARCERATO%>" value="D" <%=lCheckDaScarcerare%>>Da scarcerare
        &nbsp;&nbsp;
        <input type="radio" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_SCARCERARE_SCARCERATO%>" value="S" <%=lCheckScarcerato%>>Già scarcerato
      </td>
  </tr>
</table>
<% 
//==============================================================================
//                   DURATA E DIFFERIMENTO DELLA PENA
//==============================================================================
%>  
<table style="width: 95%;">
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td colspan=4 class="titolo">Durata e Differimento Della Pena</td>
  </tr>

  <tr>
    <td class="l">Rinvio fino al  </td>
     <td class="l">
       <input type="text" Title="Giorno rinvio " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRinvio(), "dd") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       -
        <input type="text" Title="Mese rinvio " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRinvio(), "MM") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RINVIO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       -
        <input type="text" Title="Anno rinvio " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(decretoordinanza.getDataRinvio(), "yyyy") )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>

    <td class="l">
     Atti trasmessi al TDS di
    </td>
   <td class="l">
     <input title="Sede Autorita "  type="text" value="<%=StringUtils.toStringJSP( decretoordinanza.getAltraAutorita() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ALTRA_AUTORITA%>"  maxlength="35" size="35">
      <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiDecretoOrdinanzaSiep.CAMPO_ALTRA_AUTORITA%>', 'TDS');">
        <img src="/images/filefolder.gif" border=0>
      </a>
   </td>
  </tr>
</table>

<table style="width: 95%;">
  <tr>
    <td class="l">
      Rinvio della misura di
    </td>
    <td class="l">
      Anni
      <input type="text" Title="Anni rinvio " value="<%=StringUtils.toStringJSP(decretoordinanza.getNumAnniRinvio() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_ANNI_RINVIO%>" maxlength="2" size="2">
      Mesi
      <input type="text" Title="Mesi rinvio " value="<%=StringUtils.toStringJSP(decretoordinanza.getNumMesiRinvio() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_MESI_RINVIO%>" maxlength="2" size="2">
      Giorni
      <input type="text" Title="Giorni rinvio " value="<%=StringUtils.toStringJSP(decretoordinanza.getNumGiorniRinvio() )%>" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_GIORNI_RINVIO%>" maxlength="2" size="2">
      &nbsp;  &nbsp;  &nbsp; &nbsp;
      <input type="checkbox" name="<%=ICostantiDecretoOrdinanzaSiep.CAMPO_FLAG_DECISIONE_TRIBUNALE%>" value="" onClick="ceck();"> Fino alla decisione del TDS
      <input type="hidden" name="flagDecisione" value="N">
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

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Sede è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","maxlen=4","La lunghezza massima per l'anno registro è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_REGISTRO%>","minlen=4","La lunghezza minima per l'anno registro è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_REGISTRO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_PROVVEDIMENTO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Giorno emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Mese emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","req","Il campo Anno emissione provvedimento è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","minlen=4","La lunghezza minima per l'anno emissione provvedimento è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_EMISSIONE_PROVVEDIMENTO%>","lt=3000");

    // Data rinvio (non obbligatoria)
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_RINVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_RINVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>","maxlen=4","La lunghezza massima per l'anno rinvio  è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>","minlen=4","La lunghezza minima per l'anno rinvio  è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_RINVIO%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_ANNI_RINVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_MESI_RINVIO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_NUM_GIORNI_RINVIO%>","numeric");


    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>","req","Il campo Giorno differimento  è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_GIORNO_DATA_DIFFERIMENTO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>","req","Il campo Mese differimento  è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_MESE_DATA_DIFFERIMENTO%>","numeric");

    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>","req","Il campo Anno differimento  è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>","maxlen=4","La lunghezza massima per l'anno differimento  è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>","minlen=4","La lunghezza minima per l'anno differimento  è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiDecretoOrdinanzaSiep.CAMPO_ANNO_DATA_DIFFERIMENTO%>","lt=3000");



    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>