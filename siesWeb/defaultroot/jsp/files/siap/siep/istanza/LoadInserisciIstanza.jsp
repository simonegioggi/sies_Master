<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istanza.model.IstanzaModel"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="istanzaSoggettoEventoFascicoloSiep" scope="request" class="siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel"/>

<jsp:useBean id="sesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="dataNascitaPresunta" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazionalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="esitoProvvedimentoIstanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Istanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    var desktop;
    function ListaAvvocati(a_formname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

    function Verify()
    {

      // Non è possibile specificare solo il numero o solo l'anno per il fascicolo
      if( (document.LoadInserisciIstanza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value.length != 0)
           && (document.LoadInserisciIstanza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value.length == 0) )
      {
        alert("Valorizzare Anno Procedimento");
        return false;
      }
      if( (document.LoadInserisciIstanza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value.length == 0)
           && (document.LoadInserisciIstanza.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value.length != 0) )
      {
        alert("Valorizzare Numero Procedimento");
        return false;
      }



/****************************Modifica******************************************/

    var Cognome=document.LoadInserisciIstanza.<%= ICostantiSoggetto.CAMPO_COGNOME%>.value;
    var Nome=document.LoadInserisciIstanza.<%= ICostantiSoggetto.CAMPO_NOME%>.value;
    var Anno=document.LoadInserisciIstanza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value;
    var Prg=document.LoadInserisciIstanza.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value;

 if(Anno == "" && Prg == "")
   {

<%if(!modalita.equals("M"))
    {%>
     if(Cognome == "")
      {
       alert("Il Cognome del Soggetto è obbligatorio");
       return false;
      }

     if(Nome == "")
      {
       alert("Il Nome del Soggetto è obbligatorio");
       return false;
      }
  <%}%>
if (document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
      {
        //document.LoadInserisciIstanza.<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>.value="";
        if (document.LoadInserisciIstanza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value.length==0)
        {
          alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
          return false;
        }
      }
      else
        document.LoadInserisciIstanza.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value='';

      if (document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
        document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
      if (document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
        document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
//        if (document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==0 && document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==0)
//        {
//          document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='01';
//          document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='01';
//        }
//        else
//        {
        if ((document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==0 && document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length>0) || (document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length>0 && document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==0))
        {
          alert ('Non è possibile inserire soltanto il giorno o il mese');
          return false;
        }
//        }

      var data_to_verify=document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciIstanza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
      if ( ! ControllaData(data_to_verify))
      {
        alert('Data di nascita non valida');
        return false;
      }

   }
// Controllo di Obligatorietà Oggetto
      if(document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>[document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>.selectedIndex].value=='-')
      {
        alert("L'Oggetto dell'Istanza è obbligatorio");
        document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>.focus();

        return false;
      }
/**********************************************************************/


      document.LoadInserisciIstanza.AnnoSentenza.disabled = false;
      document.LoadInserisciIstanza.NumeroSentenza.disabled = false;
      document.LoadInserisciIstanza.GiornoDataSentenza.disabled = false;
      document.LoadInserisciIstanza.MeseDataSentenza.disabled = false;
      document.LoadInserisciIstanza.AnnoDataSentenza.disabled = false;
      document.LoadInserisciIstanza.CodTipoAutoritaEmittente.disabled = false;
      document.LoadInserisciIstanza.CodLuogoEmittente.disabled = false;
      document.LoadInserisciIstanza.GiornoDataIrrevocabilita.disabled = false;
      document.LoadInserisciIstanza.MeseDataIrrevocabilita.disabled = false;
      document.LoadInserisciIstanza.AnnoDataIrrevocabilita.disabled = false;

      document.LoadInserisciIstanza.ChiaveSiepAnno.disabled = false;
      document.LoadInserisciIstanza.ChiaveSiepProgressivo.disabled = false;


      return true;
    }
  </script>
</head>
<body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        IstanzaModel lIstanza = new IstanzaModel();
        SoggettoModel lSoggetto = new SoggettoModel();
        FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

        Date lDataPresentazione = null;

        String lAction = new String();
        if( modalita.equals("I") )
        {
          lAction = "siap.siep.istanza.action.ActInserisciIstanza";
          lDataPresentazione = new Date();
%>
          <font class="campo">Inserimento Istanza</font>
<%
        }
        else if( modalita.equals("M") )
        {
          lAction = "siap.siep.istanza.action.ActModificaIstanza";
          lIstanza = istanzaSoggettoEventoFascicoloSiep.getIstanza();
          lSoggetto = istanzaSoggettoEventoFascicoloSiep.getSoggetto();
          if(istanzaSoggettoEventoFascicoloSiep.getFascicoloSiep() != null)
          {
            lFascicolo = istanzaSoggettoEventoFascicoloSiep.getFascicoloSiep();
          }
          lDataPresentazione = lIstanza.getDataPresentazione();
%>
          <font class="campo">Modifica Istanza</font>
<%
        }
%>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciIstanza" onSubmit="return abilitaSentenza();">
      <table cellspacing=2 cellpadding=2 width=70%>
<!----------- SOGGETTO --------------------->
        <tr><td class="Titolo" colspan=4>Soggetto di riferimento</td></tr>
	      <tr>
          <td class="l">Cognome <font class="ob">(*)</font></td>
          <td class="L">
            <input title="Cognome" value="<%=StringUtils.toStringJSP(lSoggetto.getCognome())%>" type="text" name="<%= ICostantiSoggetto.CAMPO_COGNOME %>" maxlength="35" size="35" <%= (modalita.equals("M")) ? "disabled" : "" %>>
          </td>
          <td class="l">Nome <font class="ob">(*)</font></td>
	        <td class="L">
            <input title="Nome" value="<%=StringUtils.toStringJSP(lSoggetto.getNome())%>" type="text" name="<%= ICostantiSoggetto.CAMPO_NOME %>" maxlength="35" size="35" <%= (modalita.equals("M")) ? "disabled" : "" %>>
          </td>
        </tr>
        <tr>
          <td class="l">Sesso <font class=ob>(*)</font></td>
          <td class="L">
            <select title="Sesso" name="<%=ICostantiSoggetto.CAMPO_SESSO%>" <%= (modalita.equals("M")) ? "disabled" : "" %>>
              <%= sesso %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="L">
            <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd")) %>" type="text" name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%= (modalita.equals("M")) ? "disabled" : "" %>>
            /
            <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"MM")) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%= (modalita.equals("M")) ? "disabled" : "" %>>
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(), "yyyy")) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" <%= (modalita.equals("M")) ? "disabled" : "" %>>
          </td>
          <td class="l">Data Presunta</td>
          <td class="L">
            <select title="Data presunta" name="<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>" <%= (modalita.equals("M")) ? "disabled" : "" %>>
              <%= dataNascitaPresunta %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Comune Nascita</td>
          <td class="L">
            <input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita()) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>"  maxlength="35" size="35" <%= (modalita.equals("M")) ? "disabled" : "" %>>
<%
            if(!modalita.equals("M"))
            {
%>
              <a href="Javascript:ListaComuni('LoadInserisciIstanza','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
                <img src="/images/filefolder.gif" border=0>
              </a>
<%
            }
%>
          </td>
        </tr>
        <tr>
          <td class="l">Nazionalità</td>
          <td class="L">
            <select title="Nazionalità" name="<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>" <%= (modalita.equals("M")) ? "disabled" : "" %>>
              <%= nazionalita %>
            </select>
          </td>
          <td class="l">Stato di Nascita</td>
          <td class="L">
            <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>" <%= (modalita.equals("M")) ? "disabled" : "" %>>
              <%= nazioni %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Codice CS</td>
          <td class="L">
            <input title="Codice CS" value="<%=StringUtils.toStringJSP(lSoggetto.getCodCs()) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_CS %>" maxlength="6" size="6" <%= (modalita.equals("M")) ? "disabled" : "" %>>
          </td>
        </tr>
        <tr>
          <td class="l">Atto Nascita</td>
          <td class="L">
            <input title="Atto di nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getAttoNascita() )%>" type="text" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>"  maxlength="10" size="10" <%= (modalita.equals("M")) ? "disabled" : "" %>>
          </td>
        </tr>
<!----------------------------------------------------->
<!----------- SOGGETTO PRESENTANTE--------------------->
        <tr><td class="Titolo" colspan=4>Soggetto presentante</td></tr>
        <tr>
          <td class="l">Cognome</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP(lIstanza.getCognomeSoggettoPresentante())%>" type="text" name="<%= ICostantiIstanza.CAMPO_COGNOME_SOGGETTO_PRESENTANTE %>" maxlength="35" size="35">
          </td>
          <td class="l">Nome</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP(lIstanza.getNomeSoggettoPresentante())%>" type="text" name="<%= ICostantiIstanza.CAMPO_NOME_SOGGETTO_PRESENTANTE %>" maxlength="35" size="35">
          </td>
        </tr>
<!------------------------------------------>
<!----------- AVVOCATO --------------------->
        <tr><td class="Titolo" colspan=4>Avvocato</td></tr>
        <tr>
          <td class="l" colspan="2">
            <a href="Javascript:ListaAvvocati('LoadInserisciIstanza');">
              Seleziona dalla lista <img src="<%=IWebConstants.IMAGES_DIR%>filefolder.gif" border=0>
            </a>
          </td>
        </tr>
        <tr>
          <td class="l">Cognome</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP(lIstanza.getCognomeAvvocato())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_COGNOME%>" maxlength="35" size="35">
          </td>
          <td class="l">Nome</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP(lIstanza.getNomeAvvocato())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_NOME%>" maxlength="35" size="35">
          </td>
        </tr>
        <tr>
          <td class="l">Foro Competenza</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP(lIstanza.getForoCompetenza())%>" type="text" name="<%=ICostantiAvvocato.CAMPO_FORO%>">
          </td>
        </tr>
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ID_AVVOCATO%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_INDIRIZZO%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_TELEFONO%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_FAX%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_E_MAIL%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_CODICE_FISCALE%>" value="">

        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA%>" value="">
        <input type="HIDDEN" name="<%=ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA%>" value="">

<!------------------------------------------>
<!----------- SENTENZA --------------------->
<%
  //* Nel caso l'istanza abbia un fascicolo associato,
  //* gli estremi della sentenza non sono modificabili
  boolean lSentenzaModificabile = true;
  if(lFascicolo.getIdFascicoloSiep() != null)
  {
    lSentenzaModificabile = false;
  }
%>
      </table>
      <table cellspacing=2 cellpadding=2 width=70%>
        <tr><td class="Titolo" colspan=4>Estremi della sentenza</td></tr>
        <tr>
          <td class="l">Anno/Numero</td>
          <td class="L">
            <input value="<%=StringUtils.toStringJSP(lIstanza.getAnnoSentenza())%>" type="text" name="<%= ICostantiIstanza.CAMPO_ANNO_SENTENZA %>" maxlength="4" size="4" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
            /
            <input value="<%=StringUtils.toStringJSP(lIstanza.getNumeroSentenza())%>" type="text" name="<%= ICostantiIstanza.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
          </td>
        </tr>
        <tr>
          <td class="l">Data</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lIstanza.getDataSentenza(), "dd"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiIstanza.CAMPO_GIORNO_DATA_SENTENZA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lIstanza.getDataSentenza(), "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiIstanza.CAMPO_MESE_DATA_SENTENZA %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lIstanza.getDataSentenza(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiIstanza.CAMPO_ANNO_DATA_SENTENZA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)"<%= (!lSentenzaModificabile) ? "disabled" : "" %>>
          </td>
        </tr>
        <tr>
          <td class="l">Autorità Emittente</td>
          <td class="l">
            <select Title="Autorità Emittente" name="<%= ICostantiIstanza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
              <%=autoritaEmi%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Luogo Emittente</td>
          <td class="l">
          <input Title="Luogo Emittente" name="<%=ICostantiIstanza.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(lIstanza.getDescrLuogoEmittente())%>" type="text" maxlength="35" size="35" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
<%
            if(lSentenzaModificabile)
            {
%>
              <a href="Javascript:ListaComuni('LoadInserisciIstanza','<%= ICostantiIstanza.CAMPO_COD_LUOGO_EMITTENTE %>');">
                <img src="/images/filefolder.gif" border=0>
              </a>
<%
            }
%>
          </td>
        </tr>
        <tr>
          <td class="l">Data Irrevocabilità</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lIstanza.getDataIrrevocabilita(), "dd"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiIstanza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lIstanza.getDataIrrevocabilita(), "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiIstanza.CAMPO_MESE_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lIstanza.getDataIrrevocabilita(), "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" <%= (!lSentenzaModificabile) ? "disabled" : "" %>>
          </td>
        </tr>
<!------------------------------------------->
<!----------- Fascicolo --------------------->
<%
  //* Nel caso l'istanza abbia un fascicolo associato,
  //* non è possibile modificare il fascicolo siep
  boolean lFascicoloModificabile = true;
  if(lFascicolo.getIdFascicoloSiep() != null)
  {
    lFascicoloModificabile = false;
  }
%>
      </table>
      <table cellspacing=2 cellpadding=2 width=70%>
        <tr><td class="Titolo" colspan=4>Procedimento (N.SIEP)</td></tr>
        <tr>
          <td class="L"> Anno/Numero</td>
          <td class="L">
            <input type="text" title="Anno" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno())%>" <%= (!lFascicoloModificabile) ? "disabled" : "" %>>
            /
            <input type="text" title="Numero" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14" value="<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr())%>" <%= (!lFascicoloModificabile) ? "disabled" : "" %>>
          </td>
        </tr>
<!------------------------------------------>
<!----------- Oggetto ---------------------->
        <tr><td class="Titolo" colspan=4>Dati dell'istanza</td></tr>
        <tr>
          <td class="l">Data</td>
          <td class="l">
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataPresentazione, "dd" ))%>" type="text" size="2" maxlength="2" name="<%= ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataPresentazione, "MM" ))%>" type="text" size="2" maxlength="2" name="<%= ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataPresentazione, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
        </tr>
        <tr>
          <td class="l">Contenuto <font class="ob">(*)</font></td>
          <td class="l">
            <select Title="Oggetto" name="<%= ICostantiIstanza.CAMPO_COD_MOTIVO %>">
              <%=contenuto%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Note</td>
          <td class="l">
            <TEXTAREA title="note" name="<%=ICostantiIstanza.CAMPO_NOTE%>" cols=80 rows=5><%=StringUtils.toStringJSP(lIstanza.getNote())%></textarea>
          </td>
        </tr>
<%
      if(modalita.equals("M"))
      {
%>
        <tr>
          <td class="l">Stato istanza</td>
          <td class="l">
            <select Title="Stato" name="<%= ICostantiIstanza.CAMPO_COD_ESITO %>">
              <%= esitoProvvedimentoIstanza %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Ufficio Destinatario</td>
          <td class="l">
            <select Title="Ufficio Destinatario" name="<%= ICostantiIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO %>">
              <%= uffici %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="l">Luogo Destinatario</td>
          <td class="l">
          <input Title="Luogo Destinatario" name="<%=ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO%>" value="<%=StringUtils.toStringJSP(lIstanza.getDescrLuogoDestinatario())%>" type="text" maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciIstanza','<%= ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
        </tr>
<%
    }
%>
<!----------------------------------------->
        <tr>
          <td>
            <input class=bottone  type="submit" value="Conferma">
          </td>
        </tr>

        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
        <input type="HIDDEN" name="<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>" value="<%=lIstanza.getIdIstanza()%>">
        <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=(lIstanza.getEveIdEvento() != null) ? lIstanza.getEveIdEvento().toString() : ""%>">
      </table>
    </form>

    <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadInserisciIstanza");
<%
      if(!modalita.equals("M"))
      {
%>
      //  frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>//","req","Il Cognome del Soggetto è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alphabetic");

     //   frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>//","req","Il Nome del Soggetto è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alphabetic");

        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
<%
      }
      if(lFascicoloModificabile)
      {
%>
        frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");

        frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
        frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
<%
      }
%>
      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>