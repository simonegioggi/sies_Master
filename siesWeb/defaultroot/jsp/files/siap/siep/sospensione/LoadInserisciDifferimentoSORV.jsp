<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.sospensione.model.SospensioneModel"%>
<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.tenore.action.ICostantiTenore"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.siep.util.MinorMask"%>

<%
//==============================================================================
// Finestra di Inserimento dei Provvedimenti della Sorveglianza relativi al
// Differimento. I casi gestiti sono:
// - Concessione Differimento Provvisorio
// - Concessione Differimento Definitivo
// - Rigetto Differimento
// - Revoca Differimento
// La finestra consente di inserire i dati del Provvedimento, o di selezionarli
// da una lista.
// E' possibile anche che alla maschera vengano direttamente passati i dati
// del provvedimento se selezionato dalla lista dei provvedimenti della
// Sorveglianza.
//==============================================================================
%>

<jsp:useBean id="misuraalternativa"     scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="dataeditabile"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoDifferimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="CodTipoProvvedimento"  scope="request" class="java.lang.String"/>
<%  // combo %>
<!--jsp:useBean id="listaTipoProvvedimento" scope="request" class="java.lang.String"/-->
<jsp:useBean id="tipologiadecisione"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipologiarigetto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvv"            scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUS"   scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante"   scope="request" class="java.lang.String" />
<%
// Se presente un differimento provvisorio viene caricato (solo se si sta inserendo un differimento definitivo)
// serve per recuperare la data del differimento provvisorio da precaricare nella data differimento
 %>
<jsp:useBean id="differimentoprovvisorio" scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  String lcodicePosizione =lPosizione.getCodPosizioneGiuridica();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  MisuraAlternativaModel  lDifferimentoMis = differimentoprovvisorio;

//   if(lPosizione == null)
//     lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();

//   if(lDifferimentoMis == null)
//     lDifferimentoMis = new MisuraAlternativaModel();
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Sospensione dell'esecuzione della pena</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      var desktop;

      //========================================================================
      //
      //========================================================================
      function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio) {
        pulisciId();
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      //========================================================================
      //
      //========================================================================
      function pulisciId()
      { 
        //alert("pulisciId");
        document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
      }

      //========================================================================
      // Funzione per il caricamento della lista dei Provvedimenti SIUS
      //========================================================================
      function ListaDocumentiSius(a_formname) {
        var tipoMA = '<%=ICostantiMisuraAlternativa.DIFFERIMENTO_PENA%>';
				var naturaMA;  // Natura decisione
        <% if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
        	naturaMA = '<%=ICostantiMisuraAlternativa.CONCESSIONE%>';
          tipoMA = '<%=ICostantiMisuraAlternativa.DIFFERIMENTO_PENA_PROV%>';
        <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
        	naturaMA = '<%=ICostantiMisuraAlternativa.CONCESSIONE%>';
          tipoMA = '<%=ICostantiMisuraAlternativa.DIFFERIMENTO_PENA_DEF%>';
        <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
        	naturaMA = '<%=ICostantiMisuraAlternativa.RIGETTO%>';
        <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
        	naturaMA = '<%=ICostantiMisuraAlternativa.REVOCA%>';
        <% }  %>

        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>="+naturaMA+"&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
      }

      //========================================================================
      // Funzione per il controllo dei dati prima della submit
      //========================================================================
      function Verify()
      {

        //DATA EMISSIONE PROVVEDIMENTO (obbligatoria)
        var data_to_verify =      document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value
                             +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value
                             +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data emissione provvedimento non valida');
          document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>.focus();

          return false;
        }

        <% if (   TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
        	     || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)) { %>
        //=DATA DIFFERIMENTO (obbligatoria)
        var data_to_verify_dif =      document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>.value
                                 +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>.value
                                 +'/'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data differimento non valida');
          document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA %>.focus();

          return false;
        }

        //=DATA rinvio()

        var data_to_verify =      document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>.value
                             +'-'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>.value
                             +'-'+document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data rinvio non valida');
          document.LoadInserisciSospensioneDifferimento.<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA %>.focus();

          return false;
        }


        <%
        // Aggiunta controllo Data Differimento >= data inizio pena se presente
        // data inizio pena
        if(penaresidua.getDataInizio()!= null) {%>
        var giorno_data_decorrenza_pena = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"dd")%>;
        var mese_data_decorrenza_pena   = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"MM")%>;
        var anno_data_decorrenza_pena   = <%=DateUtils.getDateToString(penaresidua.getDataInizio(),"yyyy")%>;

        if(giorno_data_decorrenza_pena < '10' && giorno_data_decorrenza_pena != '' )
        {
            giorno_data_decorrenza_pena ='0'+giorno_data_decorrenza_pena;
        }

        if(mese_data_decorrenza_pena < '10' && mese_data_decorrenza_pena != '' )
        {
           mese_data_decorrenza_pena ='0'+mese_data_decorrenza_pena;
        }

        var data_decorrenza_pena = giorno_data_decorrenza_pena+'/'+mese_data_decorrenza_pena+'/'+anno_data_decorrenza_pena

        if(!CompareDate(data_decorrenza_pena, data_to_verify_dif))
        {
          alert("Data Differimento deve essere superiore o uguale alla data di Inizio Pena");
          return false;
        }
      <%} // end if pernaresidua%>
      <%} // end if DIfferimento provvisorio o definitivo %>
      return true;
     }
    </script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>

  </head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class=lbg>
         <font  class="label">Funzione :&nbsp;</font>
         <% if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
         <font class="campo">Differimento / Rinvio dell'esecuzione Provvisorio</font>
         <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
         <font class="campo">Differimento / Rinvio dell'esecuzione Definitiva</font>
         <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
         <font class="campo">Rigetto Differimento / Rinvio dell'esecuzione</font>
         <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
         <font class="campo">Revoca Differimento / Rinvio dell'esecuzione</font>
         <% }  %>
      </td>
    </tr>
  </table>

  <br>
     <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<form method="POST"  action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSospensioneDifferimento">
<%
   BigDecimal lIdOrdinanzaSius = null;
   if(misuraalternativa != null && misuraalternativa.getEveIdEvento() != null) {
     lIdOrdinanzaSius = misuraalternativa.getEveIdEvento();
   }
%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciDifferimentoSORV">
    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="<%=StringUtils.toStringJSP(lIdOrdinanzaSius)%>">
    <input type="HIDDEN" name="<%=ICostantiSospensione.TIPO_DIFFERIMENTO%>"  value="<%=TipoDifferimento%>">


    <input type="HIDDEN" name="AzioneChiamante" value="<%=AzioneChiamante%>">
    <input type="HIDDEN" name="dataInizioPena"  value="<%=penaresidua.getDataInizio()%>">
    <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" >



<%
//==============================================================================
// Sezione relativa alla Posizione Giuridica e Pena
// - Posizione giuridica
// - Luogo di detenzione
//   -- istituto di detenzione (se detenuto per questo o altra causa)
//   -- altro luogo
//   -- Indirizzo (se arresti domiciliari)
// - Pena Residua (se presente)
//   -- Reclusione + Arresti
//   -- Data Inizio, Tipo Ergastolo (se ergastolo)
//   -- Data fine (editabile (?) o meno)
//==============================================================================
%>

  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
        <font class="campo">
          <% if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) { %>
              DETENUTO PER ALTRA CAUSA
          <% } else { %>
            <%=lPosizione.getDescrPosizioneGiuridica()%>
          <% } %>
        </font>
      </td>
    </tr>

    <%
      // Se Detenuto altra causa: Istituto di detenzione o Altro Luogo
      if (lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) {
        if( lAltraCausa.getIstitutoDetenzione()!= null ) { %>
          <tr>
            <td class="l">Detenuto presso </td>
            <td class="L" colspan=5>
              <font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lAltraCausa.getIstitutoDetenzione().getDescrComune()!=null) { %>
               di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
            </td>
          </tr>

          <% if (lAltraCausa != null && lAltraCausa.getAltroLuogo()!=null) { %>
          <tr>
            <td class="l">Altro Luogo </td>
            <td class="L" colspan=5>
              <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
            </td>
          </tr>
          <% }
        } // fine istituto di detenzione
      }
      else if(lLuogoDetenzione.getIstitutoDetenzione() != null ) { %>
        <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
            <% if(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()!=null) { %>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            <% } %>
          </td>
        </tr>
      <%}%>


      <%
        // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(   lPosizione.getCodPosizioneGiuridica() != null
           && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
          if(lLuogoDetenzione.getIstitutoDetenzione() != null) { %>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getIndirizzo())%></font>&nbsp;
              </td>
            </tr>
          <% }
        }
      %>

      <%
      //==========================================================================
      // Aggiungo i dati della pena residua se presenti (reclusione/Arresto)
      // (non ergastolo)
      //==========================================================================
      if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
      {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
           )
        {}
        else
        { %>
        <tr>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoMulta().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        </tr>
        <%}%>

        <%
        //===============
        // Arresti
        //===============
        if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
        {}
        else
        { %>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <%if(penaresidua.getImportoAmmenda().compareTo((new BigDecimal(0)))!=0){%>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
          <% } %>
        <tr>
      <% }
      }  // fine IF sulla pena residua
      %>


      <%
      //==========================================================================
      // Inserisco il rigo con Data Inizio e Tipo Ergastolo (se presente)
      //==========================================================================
      %>
      <tr>
      <% if (penaresidua.getDataInizio() != null) { %>
           <td class="l">Data Decorrenza Pena</td>
           <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <% } %>

      <% if (penaresidua.getFlagErgastolo() != null) {
           if(penaresidua.getFlagErgastolo().equals("S")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
           <% }
           else if(penaresidua.getFlagErgastolo().equals("D")) { %>
             <td class="l">Pena Detentiva</td>
             <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
           <% }
         }
      %>

      <%
      //========================================================================
      // Inserisco la data Fine pena
      // - se non libero o comunque detenuto pre altra causa
      // - se non in ergastolo
      // - se data editabile (se lapena residua recuperata è non validata)
      //   inserisco i campi altrimenti solo label
      //========================================================================
      if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) ) {
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) {
          if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null) {
          %>
           <td class="l">Data Fine Pena</td>
           <td class="L" colspan=2>
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
             -
             <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
           </td>
          <% }
          else if( penaresidua.getDataFine() != null) {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta())) {
            %>
               <td class="l">Data Fine Pena</td>
               <td class="L" colspan=2>
                 <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td>
            <%
            }else{%>
               <td class="l">Data Fine Pena</td>
               <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
               </td><%
            }
          }
        }
      } %>
    </tr>
  </table>

<%
//==============================================================================
//           DATI DEL PROVVEDIMENTO DI DIFFERIMENTO DELL'ESECUZIONE
//==============================================================================
%>
  <br>
  <table style="width: 95%; border: 0;" >
    <tr>
    <% if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
      <td colspan="4" class="titolo">Dati del Decreto di Differimento/Rinvio dell'Esecuzione</td>
    <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF) ) { %>
      <td colspan="4" class="titolo">Dati dell'Ordinanza di Differimento/Rinvio dell'Esecuzione</td>
    <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO) ) { %>
      <td colspan="4" class="titolo">Dati del Provvedimento di Rigetto del Differimento</td>
    <% } else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
      <td colspan="4" class="titolo">Dati del Provvedimento di Revoca del Differimento</td>
    <% }  %>
    </tr>

    <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciSospensioneDifferimento');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <!--<td class="l" colspan="3">&nbsp;</td>-->
    </tr>

    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" onChange="pulisciId();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" onChange="pulisciId();" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="l"> Anno / Numero Provvedimento</td>
      <td class="l">
        <input Title="Anno Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" onChange="pulisciId();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input Title="Numero Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onChange="pulisciId();" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>


    <% if (   TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
           || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)
           || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO)
           || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA)
          ) { %>
    <input type="HIDDEN" value="<%=CodTipoProvvedimento%>" Title="Tipo Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" >
    <% }// else if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA) ) { %>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--tr>
      <td class="l">Tipo provvedimento </td>
      <td class="l" colspan="3">
       <select Title="Tipo Provvedimento" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE%>" onChange="pulisciId();">
         <%=listaTipoProvvedimento%>
       </select>
      </td>
    </tr--%>
    <%// } %>

    <tr>
      <td class="l">Autorità Emittente</td>
      <td class="l" colspan="3">
<%
	String listaComuniScript = "";
	if (TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) {
    	  listaComuniScript = "ListaComuniEmitUdsMinor";
%>
        <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteUfficio)%>
<% 
	} else if (   TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)
                  || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_REVOCA)
                  || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO)
                 ){ 
    	  listaComuniScript = "ListaComuniEmitTdsMinor";
%>
        <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale)%>
<% 
	} else { 
    	  listaComuniScript = "ListaComuniEmitUTMinor";
%>
        <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteAutoritaUff, "onChange='pulisciId();'", ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA, tipoUfficioSIUS)%>
<% 
	}  
%>
      </td>
    </tr>
    <tr>
      <td class="l">Sede Autorità Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          <a href="Javascript:<%=listaComuniScript%>('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');"><img src="/images/filefolder.gif" border=0></a>
        </font>
      </td>
    </tr>

    <tr>
      <td class="l">Oggetto decisione <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <select Title="Tipologia Decisione" class="small" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
        <%=tipologiadecisione%>
        </select>
      </td>
    </tr>

    <% if ( TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_RIGETTO)) { %>
    <tr>
      <td class="l">Tipologia <font class="ob">(*)</font></td>
      <td class="l" colspan="3">
        <select Title="Tipologia Rigetto" class="small" name="<%=ICostantiEvento.CAMPO_COD_ESITO%>">
        <%=tipologiarigetto%>
        </select>
      </td>
    </tr>
    <% } %>

    <tr>
      <td class="l">Data Emissione Provvedimento <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>"   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
        </font>
      </td>
    </tr>

    <tr>
      <td class="l">Motivazioni</td>
      <td class="L" colspan="3">
        <TEXTAREA title="Motivazioni" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2 onChange="pulisciId();"></textarea>
      </td>
    </tr>
  </table>

<%
//==============================================================================
// Sezione presente solo nel caso di Concessione del Differimento (Provvisorio o Definitivo)
//==============================================================================
%>
<% if (   TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
	     || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)) { %>
<table style="width: 95%;">
  <tr>
    <td class="l">
      Data differimento esecuzione <font class="ob">(*)</font>
      &nbsp;&nbsp;&nbsp;
      <input type="text" Title="Giorno differimento esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDifferimentoMis.getDataInizioMisura(), "dd") )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();">
       -
      <input type="text" Title="Mese differimento esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDifferimentoMis.getDataInizioMisura(), "MM") )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();">
       -
      <input type="text" Title="Anno differimento esecuzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDifferimentoMis.getDataInizioMisura(), "yyyy") )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
      &nbsp;&nbsp;&nbsp;

      <%
        String lCheckScarcerato = "";
        String lCheckDaScarcerare = "";
        if(   misuraalternativa.getCodTipoUfficioScarcerazione() != null
           && misuraalternativa.getCodTipoUfficioScarcerazione().equals("PROC") )
        {
          lCheckDaScarcerare = "checked";
        }
        else if(   misuraalternativa.getCodTipoUfficioScarcerazione() != null
                && misuraalternativa.getCodTipoUfficioScarcerazione().equals("SORV") )
        {
          lCheckScarcerato = "checked";
        }
        else
        {
          lCheckDaScarcerare = "checked";
        }
      %>

      <% if ( lPosizione.isLibero() ) {
         // se libero i check non vanno visualizzati
        }
        else if (   lPosizione.getCodPosizioneGiuridica().equals("13") // Affidamento in prova
                 || lPosizione.getCodPosizioneGiuridica().equals("27") // Indultino
                 //MEV29- gestione delle Misure Provvisorie
                 || lPosizione.getCodPosizioneGiuridica().equals("29") // Detenzione Domiciliare Provvisoria
                 || lPosizione.getCodPosizioneGiuridica().equals("54") // Affidamento in prova Provvisorio
                 )
        { %>
        <input type="radio" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>" value="PROC" <%=lCheckDaScarcerare%>  onChange="pulisciId();">Da porre in libertà
        &nbsp;&nbsp;
        <input type="radio" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>" value="SORV" <%=lCheckScarcerato%>    onChange="pulisciId();">Già posto in libertà
      <% } else { %>
        <input type="radio" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>" value="PROC" <%=lCheckDaScarcerare%>  onChange="pulisciId();">Da scarcerare
        &nbsp;&nbsp;
        <input type="radio" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TIPO_UFFICIO_SCARCERAZIONE%>" value="SORV" <%=lCheckScarcerato%>    onChange="pulisciId();">Già scarcerato
      <% }  %>
      </td>
  </tr>
</table>
<% }  %>

<%
//==============================================================================
//                   DURATA E DIFFERIMENTO DELLA PENA
//==============================================================================
%>
<% if (   TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
       || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)) { %>
<br>
<table style="width: 95%; border: 0;">
  <tr>
    <td colspan="2" class="titolo">Durata e Differimento Della Pena</td>
  </tr>

  <tr>
    <td class="l" width="20%">Rinvio fino al  </td>
    <td class="l" colspan="1">
       <input type="text" Title="Giorno rinvio " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(), "dd") )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
              onChange="pulisciId();" >
       -
       <input type="text" Title="Mese rinvio " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(), "MM") )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"
              onChange="pulisciId();">
       -
       <input type="text" Title="Anno rinvio " value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataFineMisura(), "yyyy") )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"
              onChange="pulisciId();">
    </td>

	<% if ( TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
	<%-- MEV10-s3: modificato layout con aggiunta etichette e combo --%>
	<tr>
		<td class="Titolo" colspan="2">Atti trasmessi al Tribunale di Sorveglianza</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario</td>
        <td class="L"><%=MinorMask.comboTribunaleTrattino()%></td>
    </tr>
  	<tr>
	   	<td class="l">Sede</td>
     	<td class="l">
      		<input type="text" title="Sede Autorita" value="" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>"  maxlength="35" size="35" onChange="pulisciId();">
      		<a href="Javascript:ListaUfficiComuni('LoadInserisciSospensioneDifferimento','<%=ICostantiMisuraAlternativa.CAMPO_COD_TDS_COMPETENTE%>', 'TDS');">
        		<img src="/images/filefolder.gif" border=0>
      		</a>
    	</td>
    </tr>
    <% } %>

	<tr>
    	<td class="l" width="20%">Rinvio nella misura di</td>
    	<td class="l">
	      	Anni
	      		<input type="text" Title="Anni rinvio " value="<%=StringUtils.toStringJSP(misuraalternativa.getNumAnniMisura() )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_ANNI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();">
	      	Mesi
	      		<input type="text" Title="Mesi rinvio " value="<%=StringUtils.toStringJSP(misuraalternativa.getNumMesiMisura() )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_MESI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();">
	      	Giorni
	      		<input type="text" Title="Giorni rinvio " value="<%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniMisura() )%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA%>" maxlength="2" size="2" onkeypress="return TicTabNumField(this,event)" onChange="pulisciId();">
	      	&nbsp;  &nbsp;  &nbsp; &nbsp;
			<% if ( TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV) ) { %>
<%-- 	      		<input type="checkbox" name="<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE %>" value="S" onClick="pulisciId();"> Fino alla decisione del TDS --%>
				<select name="<%=ICostantiMisuraAlternativa.CAMPO_FLAG_DECISIONE_TRIBUNALE %>" onchange="pulisciId();">
					<option value = "N" />-
					<option value = "S" />Fino alla decisione del TDS
					<option value = "M" />Fino alla decisione del TDS minorenni
				</select>
	      	<% } %>
   	 	</td>
  	</tr>
</table>
<% } %>

<table style="width: 95%;">
	<tr><td>&nbsp;</td></tr>
    <tr>
    	<td class="lNoBord" colspan="2">
        	<br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      	</td>
    </tr>
</table>
</form>

<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciSospensioneDifferimento");

    //=======================
    // Campi Obbligatori
    //=======================
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>","req","Il campo Sede Autorità Emittente è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","La Data Emissione Provvedimento è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>"  ,"req","La Data Emissione Provvedimento è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>"  ,"req","La Data Emissione Provvedimento è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=3000");


    <% if (   TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_PROV)
	         || TipoDifferimento.equalsIgnoreCase(ICostantiSospensione.DIFFERIMENTO_DEF)) { %>
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA%>","req","La Data Differimento è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA%>"  ,"req","La Data Differimento è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>"  ,"req","La Data Differimento è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA%>","lt=3000");
    <% } %>

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>