<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="autoritaEmiCumuloSentenzaDecreto" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCumulo"      scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvv"           scope="request" class="java.lang.String" />
<jsp:useBean id="Cumulato"            scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="IstruttoriaCumulo"   scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel" />

 
<jsp:useBean id="autoritaEmiCumulo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="PenaResidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="PosizioneGiuridica"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per il caricamento la selezione e visualizzazione dei dati dei fascicoli 
// coinvolti nel cumulo
// Vengono visualizzate sia la sezione per il caricamento dei dati 
// dei fascicoli stessa BDI, che la sezione per inserire i dati di sentenze di 
// altra BDI.
// Se si seleziona un fascicolo di stessa BDI la finestra viene ricaricata dopo
// la ricerca con i soli dati del fascicolo selezionato
//==============================================================================

%>
<html>
<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>

  <script language="JavaScript">
    var desktop;
    
    function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaComuniAutoritaCumulo(a_formname,a_fieldname, codTipoUfficio)
    {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipoUfficiCumulo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function ListaComuniCompleata(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  
    function LaodPretura(aForm,aCampo,aAutorita)
    {
      if(aAutorita == 'PT')
      {
        ListaComuniCompleata(aForm,aCampo)
      }
      else
      {
        ListaComuniAutoritaCumulo(aForm,aCampo,aAutorita);
      }
    }

    function AbilitaIscrizioneFascicolo(){
      if (document.FasCumuloMan.checkProcedimento.checked==true){
        document.FasCumuloMan.ChiaveSiepAnno.disabled = false;
        document.FasCumuloMan.ChiaveSiepProgressivo.disabled = false;
        document.FasCumuloMan.AutoritaInt.disabled = false;
        document.FasCumuloMan.LuogoFas.disabled = false;        
      }
      else {
        document.FasCumuloMan.ChiaveSiepAnno.value = "";
        document.FasCumuloMan.ChiaveSiepProgressivo.value = "";
        document.FasCumuloMan.AutoritaInt.selectedIndex = 0;
        document.FasCumuloMan.LuogoFas.value = "";  
        
        document.FasCumuloMan.ChiaveSiepAnno.disabled = true;
        document.FasCumuloMan.ChiaveSiepProgressivo.disabled = true;
        document.FasCumuloMan.AutoritaInt.disabled = true;
        document.FasCumuloMan.LuogoFas.disabled = true;    
      }     
    }

  </script>
</head>

<body class="corpo" >


  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="FasCumuloMan">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    
    
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Inserimento Manuale Titolo Soggetto a Cumulo</font></td>
      </tr>
    </table>
 
    <br>
    <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    <br>
    
    <%
    /**
    <table align="center" width="90%">
      <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
      <tr>
        <td>
          <br>
          <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
          <br>
        </td>
      </tr>
    </table>
    */
    %>
    
<table cellspacing=2 cellpadding=2>
  <tr>
    <td class="l">Data Iscrizione Procedimento</td>
    <td class="l">
          <input title="Data Arrivo Atto" value="" name="GiornoArrivoAtto" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input title="Data Arrivo Atto" value="" name="MeseArrivoAtto"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input title="Data Arrivo Atto" value="" name="AnnoArrivoAtto" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
  </tr>
    
  <tr>
    <td class="l">Data Irrevocabilità</td>
    <td class="L">
          <input title="Data Irrevocabilità" value="" name="DataIrrevocabilitaGiorni" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input title="Data Irrevocabilità" value="" name="DataIrrevocabilitaMesi" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input title="Data Irrevocabilità" value="" name="DataIrrevocabilitaAnni" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
    </td>
  </tr>
</table>


<%
//==============================================================================
//                                  SENTENZA
//==============================================================================
%>
<br>
<table cellspacing="2" cellpadding="2" width="90%">
  <tr><td class="Titolo" colspan="100%">Sentenza</td></tr>
  <tr>
    <td class="l">Anno/Numero R.G.N.R. <font class="ob">(*)</font></td>
    <td class="L">
      <input Title="Anno R.G.N.R."  value="" type="text" name="AnnoRegePm" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
      <input Title="Numero R.G.N.R."  value="" type="text"  name="NumeroRegePm" maxlength="6"  size="6">
    </td>
    <td class="l">Anno/Numero Reg.Gen. <font class="ob">(*)</font></td>
    <td class="L"><input Title="Anno Reg.Gen." value=""
      type="text" name="ARG" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
      <input title="Numero Reg.Gen." value="" type="text" name="NRG"  maxlength="6" size="6"> &nbsp; 
      <select name="TipoRG">
        <option value="-">-</option>
        <option value="gip" >GIP</option>
        <option value="dib" >DIB</option>
        <option value="cas" >CAS</option>
        <option value="cap" >CAP</option>      
        <option value="casap" >CASAP</option>
      </select>
    </td>
  </tr>
  <!--tr>
    <td class="l">Sede PM <font class=ob>(*)</font></td>    
    <td class="L">
      <input Title="Sede PM" type="text" name="SedeNotiziaReato" value="NAPOLI" maxlength="35" size="35" readonly>
    </td>
  </tr-->
  
  <%//========================================================================%>
  <!--tr>
    <td class="Titolo" colspan=4>Sentenza da Eseguire</td>
  </tr-->
  <tr>
    <td class="l">Data Sentenza <font class="ob">(*)</font></td>
    <td class="L">
      <input Title="Data Sentenza" type="text" value="" name="GiornoDataProvvedimento" 
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
      <input Title="Data Sentenza" type="text" value="" name="MeseDataProvvedimento"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
      <input Title="Data Sentenza" type="text" value="" name="AnnoDataProvvedimento"
        maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
    </td>
    <td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
    <td class="L">
      <input Title="Anno Sentenza" value="" 
      type="text" name="AnnoSentenza" maxlength="4" size="4" 
      onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
      <input Title="Numero Sentenza" value="" type="text" name="NumeroSentenza" maxlength="6" size="6">
    </td>
  </tr>
  <tr>
    <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
    <td class="L" colspan="2">
    <select Title="Autorità Emittente" onChange="ctrl_autorita('CodTipoAutoritaEmittente', 'CodTipoAutoritaProvvRif', 'D1', 'CodTipoRito');"
        name="CodTipoAutoritaEmittente"> 
        <option value = "-" selected />-
<option value = "CAP"  />Corte D'Appello
<option value = "CAS"  />Corte di Assise
<option value = "CASAP"  />Corte di Assise di Appello
<option value = "CAPMI"  />Corte Militare d'Appello
<option value = "CAPMID"  />Corte Militare d'Appello - Sezione Distaccata
<option value = "CSS"  />Corte Suprema di Cassazione
<option value = "GIPM"  />Gip Presso il Tribunale per i Minorenni
<option value = "GIP"  />Gip Presso il Tribunale Ordinario
<option value = "GIPP"  />Gip presso Pretura
<option value = "GIPPSD"  />Gip presso Sezione Distaccara della Pretura Circondariale
<option value = "GIPMI"  />Gip presso Tribunale Militare
<option value = "GP"  />Giudice di Pace
<option value = "GUPM"  />Gup Presso il Tribunale per i Minorenni
<option value = "GUPMI"  />Gup presso Tribunale Militare
<option value = "GUP"  />Gup Presso Tribunale Ordinario
<option value = "PT"  />Pretura
<option value = "PTC"  />Pretura Circondariale
<option value = "PTCSD"  />Sezione Distaccara della Pretura Circondariale
<option value = "TRIBSD"  />Sezione Distaccata di Tribunale
<option value = "CAPSM"  />Sezione Minorenni per la Corte di Appello
<option value = "TMI"  />Tribunale Militare
<option value = "DIB"  />Tribunale Ordinario
<option value = "DIBM"  />Tribunale per i Minorenni

    </select>
  </td>
  
  <td class="l">Tipo Rito &nbsp;
    <select Title="Tipo Rito" name="CodTipoRito">
      <option value = "-" selected />-
      <option value = "C"  />Collegiale
      <option value = "M"  />Monocratico
    </select>
  </td>

  </tr>

  <tr>
    <td class="l">Luogo Emittente <font class=ob>(*)</font></td>
    <td class="L">
      <input Title="Luogo Emittente" name="CodLuogoEmittente" 
        value="" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenza','CodLuogoEmittente',document.LoadInserisciSentenza.CodTipoAutoritaEmittente[document.LoadInserisciSentenza.CodTipoAutoritaEmittente.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> </a></td>
    <td class="L">Sezione Autorità Emittente</td>
    <td class="L"><input Title="Sezione Autorità Emittente" value=""
      type="text"
      name="NumSezioneAutoritaEmittente"
      maxlength="35" size="35"></td>
  </tr>   
</table>

<%
//==============================================================================
// Procedimento
//==============================================================================
%>

<table cellspacing=2 cellpadding=2 width="90%">
  <tr><td class="Titolo" colspan="3"><input type="checkbox" name="checkProcedimento" title="Procedimento altro ufficio" value="" onClick="javascript:AbilitaIscrizioneFascicolo();">Iscritto al procedimento altro Ufficio</td></tr>
  <tr>
    <td class="l" width="10%" nowrap>Anno e Numero Procedimento</td>
    <td class="l">
      <input type="text" title="Anno Procedimento" disabled name="ChiaveSiepAnno" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
      /
      <input type="text" title="Numero Procedimento" disabled name="ChiaveSiepProgressivo" maxlength="13" size="15" onkeypress="return TicTabNumField(this,event)">
    </td>
    <!--td class="l"><input type="checkbox" title="Procedimento altro ufficio" value"">Di Altro Ufficio</td-->
  </tr>
  
  <tr>
    <td class="l">Autorità</td>
    <td class="l" colspan="2">
      <select Title="Autorità" name="AutoritaInt" disabled>
        <option value = "-"  />-
        <option value = "PM"  />Procura della Repubblica presso il Tribunale Ordinario
        <option value = "PMM"  />Procura della Repubblica presso il Tribunale per i Minorenni
        <option value = "PGCAP"  />Procura Generale della Repubblica presso la Corte D'Appello
      </select>    
    </td>
  </tr>
  <tr>
    <td class="l">Luogo</td>
    <td class="l" colspan="2">
      <input type="text" Title="Luogo" name="LuogoFas" value=""  disabled maxlength="35" size="35" readonly="readonly">
      <a href="Javascript:ListaComuni('f','LuogoFas',document.f.AutoritaInt[document.f.AutoritaInt.selectedIndex].value);"><img src="/images/filefolder.gif" border=0></a>
    </td>
  </tr>  
</table>

<%
//==============================================================================
//  SOGGETTO
//<a href="Javascript:espandi()"><img src="/images/expand.gif" border=0></a>
//==============================================================================
%>
<br>
<table cellspacing="2" cellpadding="2" width="90%">
  <tr>
    <td class="Titolo" colspan="4">Soggetto&nbsp;</td>
    <!--td class="Titolo" width="16px">
      <a href="Javascript:espandi()"><img src="/images/expand.gif" border=0></a>
    </td-->
  </tr>
  <tr>
    <td class="l">Cognome <font class=ob>(*)</font></td>
    <td class="L"><input title="Cognome" value="" type="text"  name="Cognome"  maxlength="35" size="35"></td>
    <td class="l">Nome <font class=ob>(*)</font></td>
    <td class="L"><input title="Nome" value="" type="text" name="Nome"  maxlength="35" size="35"></td>
  </tr>
  <tr>
    <td class="l">Sesso <font class=ob>(*)</font></td>
    <td class="L">
      <select title="Sesso" name="Sesso">
        <option value = "-" selected />-
        <option value = "F"  />F
        <option value = "M"  />M
      </select>
    </td>
  </tr>
  <tr>
        <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="L">

            <input type="text" title="Giorno Data di nascita" name="GiornoDataNascita" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Mese Data di nascita" name="MeseDataNascita" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Anno Data di nascita" name="AnnoDataNascita" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">


          </td>
        <td class="l">Data Presunta</td>
        <td class="L">
          <select title="Data presunta" name="DataNascitaPresunta">
            <option value = "-"  />-
            <option value = "S"  />S
            <option value = "N" selected />N

          </select>
        </td>
      </tr>
      <tr>
        <td class="l" nowrap>Comune Nascita <font class=ob>(*)</font></td>
        <td class="L" nowrap>
          <input title="Comune di Nascita" value="" type="text" name="CodComuneNascita"  maxlength="35" size="35" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuni('LoadInserisciSoggetto','CodComuneNascita');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

  
    <tr>
      <td class="l">Stato Cittadinanza</td>
      <td class="L">

              <select title="Stato Cittadinanza" name="Nazionalita"> 
                  <option value = "-" selected />-
<option value = "301"  />Afghanistan
<option value = "201"  />Albania
<option value = "401"  />Algeria
<option value = "202"  />Andorra
<option value = "402"  />Angola
<option value = "503"  />Antigua e Barbuda
<option value = "999"  />Apolide
<option value = "302"  />Arabia Saudita
<option value = "602"  />Argentina
<option value = "358"  />Armenia
<option value = "701"  />Australia
<option value = "203"  />Austria
<option value = "359"  />Azerbaigian
<option value = "505"  />Bahamas
<option value = "304"  />Bahrein
<option value = "305"  />Bangladesh
<option value = "506"  />Barbados
<option value = "206"  />Belgio
<option value = "507"  />Belize
<option value = "406"  />Benin (Dahomey)
<option value = "306"  />Bhutan
<option value = "256"  />Bielorussia
<option value = "604"  />Bolivia
<option value = "252"  />Bosnia-Erzegovina
<option value = "408"  />Botswana
<option value = "605"  />Brasile
<option value = "309"  />Brunei
<option value = "209"  />Bulgaria
<option value = "409"  />Burkina Faso (Alto Volta)
<option value = "410"  />Burundi
<option value = "310"  />Cambogia
<option value = "411"  />Camerun
<option value = "509"  />Canada
<option value = "413"  />Capo Verde
<option value = "415"  />Ciad
<option value = "606"  />Cile
<option value = "314"  />Cina Popolare
<option value = "315"  />Cipro
<option value = "246"  />Citta' del Vaticano
<option value = "608"  />Colombia
<option value = "417"  />Comore
<option value = "418"  />Congo
<option value = "319"  />Corea del Nord
<option value = "320"  />Corea del Sud
<option value = "404"  />Costa D'Avorio
<option value = "513"  />Costarica
<option value = "250"  />Croazia
<option value = "514"  />Cuba
<option value = "212"  />Danimarca
<option value = "515"  />Dominica
<option value = "609"  />Ecuador
<option value = "419"  />Egitto
<option value = "517"  />El Salvador
<option value = "322"  />Emirati Arabi Uniti
<option value = "466"  />Eritrea
<option value = "247"  />Estonia
<option value = "420"  />Etiopia
<option value = "703"  />Figi
<option value = "323"  />Filippine
<option value = "214"  />Finlandia
<option value = "215"  />Francia
<option value = "421"  />Gabon
<option value = "422"  />Gambia
<option value = "360"  />Georgia
<option value = "216"  />Germania
<option value = "423"  />Ghana
<option value = "518"  />Giamaica
<option value = "326"  />Giappone
<option value = "424"  />Gibuti
<option value = "327"  />Giordania
<option value = "220"  />Grecia
<option value = "519"  />Grenada
<option value = "523"  />Guatemala
<option value = "425"  />Guinea
<option value = "426"  />Guinea Bissau
<option value = "427"  />Guinea Equatoriale
<option value = "612"  />Guyana
<option value = "524"  />Haiti
<option value = "525"  />Honduras
<option value = "330"  />India
<option value = "331"  />Indonesia
<option value = "332"  />Iran
<option value = "333"  />Iraq
<option value = "221"  />Irlanda
<option value = "223"  />Islanda
<option value = "712"  />Isole Marshall
<option value = "334"  />Israele
<option value = "039"  />Italia
<option value = "998"  />Jugoslavia
<option value = "356"  />Kazakistan
<option value = "428"  />Kenya
<option value = "361"  />Kirghizistan
<option value = "708"  />Kiribati
<option value = "335"  />Kuwait
<option value = "336"  />Laos
<option value = "429"  />Lesotho
<option value = "248"  />Lettonia
<option value = "337"  />Libano
<option value = "430"  />Liberia
<option value = "431"  />Libia
<option value = "225"  />Liechtenstein
<option value = "249"  />Lituania
<option value = "226"  />Lussemburgo
<option value = "253"  />Macedonia
<option value = "432"  />Madagascar
<option value = "434"  />Malawi
<option value = "340"  />Malaysia
<option value = "339"  />Maldive
<option value = "435"  />Mali
<option value = "227"  />Malta
<option value = "436"  />Marocco
<option value = "437"  />Mauritania
<option value = "438"  />Maurizio
<option value = "527"  />Messico
<option value = "713"  />Micronesia
<option value = "254"  />Moldavia
<option value = "229"  />Monaco
<option value = "341"  />Mongolia
<option value = "440"  />Mozambico
<option value = "307"  />Myanmar (Birmania)
<option value = "441"  />Namibia
<option value = "715"  />Nauru
<option value = "342"  />Nepal
<option value = "529"  />Nicaragua
<option value = "442"  />Niger
<option value = "443"  />Nigeria
<option value = "231"  />Norvegia
<option value = "540"  />Nuova Caledonia
<option value = "719"  />Nuova Zelanda
<option value = "343"  />Oman
<option value = "232"  />Paesi Bassi
<option value = "344"  />Pakistan
<option value = "720"  />Palau
<option value = "800"  />Palestina
<option value = "530"  />Panama
<option value = "721"  />Papuasia - N.Guinea
<option value = "614"  />Paraguay
<option value = "615"  />Peru'
<option value = "258"  />Polinesia Francese
<option value = "233"  />Polonia
<option value = "234"  />Portogallo
<option value = "345"  />Qatar
<option value = "219"  />Regno Unito
<option value = "463"  />Rep. Dem. del Congo (Zaire)
<option value = "516"  />Rep. Dominicana
<option value = "257"  />Repubblica  Ceca
<option value = "414"  />Repubblica Centrafricana
<option value = "235"  />Romania
<option value = "446"  />Ruanda
<option value = "245"  />Russia
<option value = "533"  />S. Vincent e Grenadine
<option value = "534"  />Saint Kitts e Nevis
<option value = "532"  />Saint Lucia
<option value = "725"  />Salomone
<option value = "727"  />Samoa
<option value = "236"  />San Marino
<option value = "448"  />Sao Tome' e Principe
<option value = "450"  />Senegal
<option value = "224"  />Serbia - Montenegro
<option value = "449"  />Seycelles
<option value = "451"  />Sierra Leone
<option value = "346"  />Singapore
<option value = "348"  />Siria
<option value = "255"  />Slovacchia
<option value = "251"  />Slovenia
<option value = "453"  />Somalia
<option value = "239"  />Spagna
<option value = "311"  />Sri Lanka (Ceylon)
<option value = "536"  />Stati Uniti d'America
<option value = "454"  />Sud Africa
<option value = "455"  />Sudan
<option value = "616"  />Suriname
<option value = "240"  />Svezia
<option value = "241"  />Svizzera
<option value = "456"  />Swaziland
<option value = "362"  />Tagikistan
<option value = "363"  />Taiwan (Formosa)
<option value = "457"  />Tanzania
<option value = "324"  />Territori Autonomia Palestinese
<option value = "349"  />Thailandia
<option value = "338"  />Timor Orientale
<option value = "458"  />Togo
<option value = "730"  />Tonga
<option value = "617"  />Trinidad e Tobago
<option value = "460"  />Tunisia
<option value = "351"  />Turchia
<option value = "364"  />Turkmenistan
<option value = "731"  />Tuvalu
<option value = "243"  />Ucraina
<option value = "461"  />Uganda
<option value = "244"  />Ungheria
<option value = "618"  />Uruguay
<option value = "357"  />Uzbekistan
<option value = "732"  />Vanuatu
<option value = "619"  />Venezuela
<option value = "353"  />Vietnam
<option value = "354"  />Yemen
<option value = "464"  />Zambia
<option value = "465"  />Zimbabwe (Rhodesia)

            </select>
      </td>
      <td class="l">Stato di Nascita</td>
      <td class="L">

            <select  title="Stato di Nascita" name="CodStatoNascita">
                <option value = "301"  />Afghanistan
<option value = "201"  />Albania
<option value = "401"  />Algeria
<option value = "202"  />Andorra
<option value = "402"  />Angola
<option value = "503"  />Antigua e Barbuda
<option value = "999"  />Apolide
<option value = "302"  />Arabia Saudita
<option value = "602"  />Argentina
<option value = "358"  />Armenia
<option value = "701"  />Australia
<option value = "203"  />Austria
<option value = "359"  />Azerbaigian
<option value = "505"  />Bahamas
<option value = "304"  />Bahrein
<option value = "305"  />Bangladesh
<option value = "506"  />Barbados
<option value = "206"  />Belgio
<option value = "507"  />Belize
<option value = "406"  />Benin (Dahomey)
<option value = "306"  />Bhutan
<option value = "256"  />Bielorussia
<option value = "604"  />Bolivia
<option value = "252"  />Bosnia-Erzegovina
<option value = "408"  />Botswana
<option value = "605"  />Brasile
<option value = "309"  />Brunei
<option value = "209"  />Bulgaria
<option value = "409"  />Burkina Faso (Alto Volta)
<option value = "410"  />Burundi
<option value = "310"  />Cambogia
<option value = "411"  />Camerun
<option value = "509"  />Canada
<option value = "413"  />Capo Verde
<option value = "415"  />Ciad
<option value = "606"  />Cile
<option value = "314"  />Cina Popolare
<option value = "315"  />Cipro
<option value = "246"  />Citta' del Vaticano
<option value = "608"  />Colombia
<option value = "417"  />Comore
<option value = "418"  />Congo
<option value = "319"  />Corea del Nord
<option value = "320"  />Corea del Sud
<option value = "404"  />Costa D'Avorio
<option value = "513"  />Costarica
<option value = "250"  />Croazia
<option value = "514"  />Cuba
<option value = "212"  />Danimarca
<option value = "515"  />Dominica
<option value = "609"  />Ecuador
<option value = "419"  />Egitto
<option value = "517"  />El Salvador
<option value = "322"  />Emirati Arabi Uniti
<option value = "466"  />Eritrea
<option value = "247"  />Estonia
<option value = "420"  />Etiopia
<option value = "703"  />Figi
<option value = "323"  />Filippine
<option value = "214"  />Finlandia
<option value = "215"  />Francia
<option value = "421"  />Gabon
<option value = "422"  />Gambia
<option value = "360"  />Georgia
<option value = "216"  />Germania
<option value = "423"  />Ghana
<option value = "518"  />Giamaica
<option value = "326"  />Giappone
<option value = "424"  />Gibuti
<option value = "327"  />Giordania
<option value = "220"  />Grecia
<option value = "519"  />Grenada
<option value = "523"  />Guatemala
<option value = "425"  />Guinea
<option value = "426"  />Guinea Bissau
<option value = "427"  />Guinea Equatoriale
<option value = "612"  />Guyana
<option value = "524"  />Haiti
<option value = "525"  />Honduras
<option value = "330"  />India
<option value = "331"  />Indonesia
<option value = "332"  />Iran
<option value = "333"  />Iraq
<option value = "221"  />Irlanda
<option value = "223"  />Islanda
<option value = "712"  />Isole Marshall
<option value = "334"  />Israele
<option value = "039" selected />Italia
<option value = "998"  />Jugoslavia
<option value = "356"  />Kazakistan
<option value = "428"  />Kenya
<option value = "361"  />Kirghizistan
<option value = "708"  />Kiribati
<option value = "335"  />Kuwait
<option value = "336"  />Laos
<option value = "429"  />Lesotho
<option value = "248"  />Lettonia
<option value = "337"  />Libano
<option value = "430"  />Liberia
<option value = "431"  />Libia
<option value = "225"  />Liechtenstein
<option value = "249"  />Lituania
<option value = "226"  />Lussemburgo
<option value = "253"  />Macedonia
<option value = "432"  />Madagascar
<option value = "434"  />Malawi
<option value = "340"  />Malaysia
<option value = "339"  />Maldive
<option value = "435"  />Mali
<option value = "227"  />Malta
<option value = "436"  />Marocco
<option value = "437"  />Mauritania
<option value = "438"  />Maurizio
<option value = "527"  />Messico
<option value = "713"  />Micronesia
<option value = "254"  />Moldavia
<option value = "229"  />Monaco
<option value = "341"  />Mongolia
<option value = "440"  />Mozambico
<option value = "307"  />Myanmar (Birmania)
<option value = "441"  />Namibia
<option value = "715"  />Nauru
<option value = "342"  />Nepal
<option value = "529"  />Nicaragua
<option value = "442"  />Niger
<option value = "443"  />Nigeria
<option value = "231"  />Norvegia
<option value = "540"  />Nuova Caledonia
<option value = "719"  />Nuova Zelanda
<option value = "343"  />Oman
<option value = "232"  />Paesi Bassi
<option value = "344"  />Pakistan
<option value = "720"  />Palau
<option value = "800"  />Palestina
<option value = "530"  />Panama
<option value = "721"  />Papuasia - N.Guinea
<option value = "614"  />Paraguay
<option value = "615"  />Peru'
<option value = "258"  />Polinesia Francese
<option value = "233"  />Polonia
<option value = "234"  />Portogallo
<option value = "345"  />Qatar
<option value = "219"  />Regno Unito
<option value = "463"  />Rep. Dem. del Congo (Zaire)
<option value = "516"  />Rep. Dominicana
<option value = "257"  />Repubblica  Ceca
<option value = "414"  />Repubblica Centrafricana
<option value = "235"  />Romania
<option value = "446"  />Ruanda
<option value = "245"  />Russia
<option value = "533"  />S. Vincent e Grenadine
<option value = "534"  />Saint Kitts e Nevis
<option value = "532"  />Saint Lucia
<option value = "725"  />Salomone
<option value = "727"  />Samoa
<option value = "236"  />San Marino
<option value = "448"  />Sao Tome' e Principe
<option value = "450"  />Senegal
<option value = "224"  />Serbia - Montenegro
<option value = "449"  />Seycelles
<option value = "451"  />Sierra Leone
<option value = "346"  />Singapore
<option value = "348"  />Siria
<option value = "255"  />Slovacchia
<option value = "251"  />Slovenia
<option value = "453"  />Somalia
<option value = "239"  />Spagna
<option value = "311"  />Sri Lanka (Ceylon)
<option value = "536"  />Stati Uniti d'America
<option value = "454"  />Sud Africa
<option value = "455"  />Sudan
<option value = "616"  />Suriname
<option value = "240"  />Svezia
<option value = "241"  />Svizzera
<option value = "456"  />Swaziland
<option value = "362"  />Tagikistan
<option value = "363"  />Taiwan (Formosa)
<option value = "457"  />Tanzania
<option value = "324"  />Territori Autonomia Palestinese
<option value = "349"  />Thailandia
<option value = "338"  />Timor Orientale
<option value = "458"  />Togo
<option value = "730"  />Tonga
<option value = "617"  />Trinidad e Tobago
<option value = "460"  />Tunisia
<option value = "351"  />Turchia
<option value = "364"  />Turkmenistan
<option value = "731"  />Tuvalu
<option value = "243"  />Ucraina
<option value = "461"  />Uganda
<option value = "244"  />Ungheria
<option value = "618"  />Uruguay
<option value = "357"  />Uzbekistan
<option value = "732"  />Vanuatu
<option value = "619"  />Venezuela
<option value = "353"  />Vietnam
<option value = "354"  />Yemen
<option value = "464"  />Zambia
<option value = "465"  />Zimbabwe (Rhodesia)

            </select>
          </td>
    </tr>

    <tr>
        <td class="l">Comune Nascita Estero</td>
        <td class="L"><input title="Comune di Nascita Estero" value="" type="text"
        name="DescComuneNascitaEstero"  ></td>
    </tr>

    <tr>
        <td class="l">Paternità</td>
        <td class="L"><input title="Paternità" value="" type="text"
        name="Paternita"  maxlength="35" size="35"></td>
    </tr>
    <tr>
        <td class="l">Cognome Madre</td>
        <td class="L"><input title="Cognome della madre" value="" type="text"
        name="CognomeMadre"  maxlength="35" size="35"></td>

        <td class="l">Nome Madre</td>
        <td class="L"><input title="Nome della madre" value="" type="text"
        name="NomeMadre"  maxlength="35" size="35"></td>
    </tr>
    <tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
        <td class="l">Codice Fiscale</td>
        <td class="L"><input type="text" title="Codice Fiscale" id="CodFiscale"
        value="" 
        name="CodFiscale"  maxlength="16" size="18"></td>

        <td class="l">Atto Nascita</td>
        <td class="L"><input title="Atto di nascita" value="" type="text"
        name="AttoNascita"  maxlength="10" size="10"></td>
    </tr>

    <tr>
      <td class="l">Codice CUI</td>
      <td class="L"><input type="text" title="Codice CUI" value=""  name="CodAfis"  maxlength="7" size="7"></td>
      <td >&nbsp;</td><td >&nbsp;</td>
    </tr>

    <tr>
      <td class="l">Note</td>
      <td class="L" colspan=3>
        <TEXTAREA title="note" name="Note"  cols="100" rows="4" ></textarea>
      </td>
    </tr>
  
</table>





  <table>
    <tr>
      <td class="lNoBord"><Input onClick="Javascript:return Verify();" class=bottone type="submit" value="Carica"></td>
    </tr>
  </table>

</form>
</body>
</html>