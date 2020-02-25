<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<jsp:useBean id="autorita"             scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="ufficioge"            scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiopm"            scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"              scope="request" class="java.util.Vector"/>
<jsp:useBean id="richiesta"            scope="request" class="java.lang.String"/>

<%
//==============================================================================
//  Form con le funzioni di gestione dei Dati Analitici del Singolo Titolo
//==============================================================================
%>


<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste
    //==========================================================================
    function tornaIndietro(action)
    {
      document.LoadInserisciRichiesteDelPM.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInserisciRichiesteDelPM.submit();
    }
  </script>

</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Richieste del PM</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPM')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInserisciRichiesteDelPM">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

  <table cellpadding="2" cellspacing="2" width="98%" align="center">
    <tr>
      <td class="Titolo" colspan="100%">Richieste ancora da inviare</td>
    </tr>
  <%
  //============================================================================
  //  INSERIRE QUI LA LISTE DELLE RICHIESTE GIA' A SISTEMA ANCORA DA INVIARE
  //============================================================================
  %>
  <tr>
    <td colspan="100%" align="center" class="l">
    
      <table cellspacing="2" cellpadding="2" align="center" width="95%">
        <tr style="display:block" id="richiesta_1">
          <td colspan="100%" align="center">
            <table cellspacing="2" cellpadding="2" align="center" width="95%">
              <tr>
                <%// Inserire qui le intestazioni delle colonne che si vogliono visualizzare %>
                <td class="int">Seleziona</td>
                <td class="int">Tipo Richiesta</td>
                <td class="int" title="Titolo per il quale è stata effettuata la richiesta">Titolo</td>
                <td class="int">Anticipazione</td> <!-- Indica se la richiesta è con anticipazione -->
                <td class="int" title="Indica se già inviata">Stato</td>
              </tr>
          
              <tr>
                <%// Inserire qui le get dei campi da visualizzare %>
                <td class="c" style="text-align:center">
                  <input type="checkbox" name="selRicerca">
                </td>
                <td class="c">&nbsp;Applicazione Benefici</td>
                <td class="c">&nbsp;Sentenza N. 
                  <a href="javascript:visualizzaRecord('sent_1')" title="Note Misura">2004/21</a>
                </td>
                <td class="c">&nbsp;S</td>
                <td class="c">&nbsp;Da Inviare</td>
              </tr>
              <!-- -->
              <tr style="display:none" id="sent_1">
                <td class="l" colspan="100%">
                  <font class="campoSmall">[Dettaglio Sentenza]</font>
                </td>
              </tr>

              <!-- SECONDO RECORD   -->
              <tr>
                <%// Inserire qui le get dei campi da visualizzare %>
                <td class="c" style="text-align:center">
                  <input type="checkbox" name="selRicerca">
                </td>
                <td class="c">&nbsp;Revoca Sentenza abolizione del Reato</td>
                <td class="c">&nbsp;Sentenza N. 
                  <a href="javascript:visualizzaRecord('sent_2')" title="Note Misura">2006/31</a>
                </td>
                <td class="c">&nbsp;N</td>
                <td class="c">&nbsp;Da inviare</td>
              </tr>
              <!-- -->
              <tr style="display:none" id="sent_2">
                <td class="l" colspan="100%">
                  <font class="campoSmall">[Dettaglio Sentenza]</font>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>


  <%
  //========================================================================
  //         INSERIRE QUI I DATI DELLA RICHIESTA
  //========================================================================
  %>
<tr>
  <td colspan="100%" align="center">
    <table width="100%">
      <tr>
        <td class="Titolo" colspan="8"> Dati Della Richiesta </td>
      </tr>
      <tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>

        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>
    
    <table width="100%">
      <tr>
        <td class="l">Contenuto</td>
        <td  class="L" colspan="3">
          <TEXTAREA title="Contenuto" name="camponote" cols="100" rows="2" ></textarea>
        </td>
      </tr>
    </table>

    <!-- MAGISTRATO -->
    <table width="100%">
      <tr>
        <td class="Titolo" width="100%" colspan="6"> Magistrato Firmatario </td>
      </tr>
      <tr>
        <td class="l">Magistrato Firmatario
        <td class="L">
          <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
          <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
          <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
          <a href="Javascript:ListaMagistrati('LoadInserisciRichiestaComunicazione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr>
        <td class="Titolo" width="100%" colspan=6> Destinatari</td>
      </tr>
    </table>

    <!-- GIUDICE DELL'ESECUZIONE -->
    <table width="100%">
      <tr>
        <td class="l" width="25%">Ufficio Giudice dell'Esecuzione</td>
        <td class="L">
          <select  Title="Ufficio Giudice Esecuzione"  name="<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>">
           <%=ufficioge%>
          </select>
        </td>
        <td class="l">Sede</td>
        <td class="L">
          <input title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaUfficiComuni('LoadInserisciRichiestaComunicazione','<%=ICostantiNotifica.CAMPO_SEDE_UFFICIO_GE%>',document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>[document.LoadInserisciRichiestaComunicazione.<%=ICostantiNotifica.CAMPO_COD_UFFICIO_GE%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
    </table>
  </td>
</tr>


</table>

</form>