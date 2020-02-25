<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>

<jsp:useBean id="annotazioneManuale"   scope="request" class="java.util.Vector"/>
<jsp:useBean id="codice"               scope="request" class="java.lang.String" />
<jsp:useBean id="posizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="tipoUfficio"          scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"               scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="testo"                scope="request" class="java.lang.String"/>
<jsp:useBean id="lEveOE"               scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="autoritaEsterna"      scope="request" class="java.lang.String" />
<jsp:useBean id="sentenza"             scope="request" class="siap.siep.sentenza.model.SentenzaModel" />

<%
//==============================================================================
// From Utilizzata per l'inserimento delle Richieste al GE
// 0287 - Indulto - Concorso   - Determinazione pena - ex artt. 671 c.p.p. e art. 174 c.p.
// 0291 - Indulto - Revoca     - Revoca benefici - ex artt. 174 c.p. e 674 c.p.p.
// 0288 - Amnistia - Concorso  - Determinazione pena - artt. 671 c.p.p. e 151 c.p.
// 0292 - Amnistia - Revoca    - Revoca benefici - ex artt.151 c.p. e 674 c.p.p.
// 0296 - Amnistia / Indulto   - Restituzione Ordine Esecuzione  -ex art. 672 c.p.p.
// 0290 - Indulto              - Applicazione benefici : Indulto
// 0294 - Amnistia             - Applicazione benefici : Amnistia
//==============================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
     function Verify()
      {
        if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Magistrato Assegnatario è obbligatorio");
          document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();

          return false;
        }
<%
        if(codice.equals("0296"))
        {
%>
        if(document.f.<%=ICostantiRichiesta.CAMPO_SEDE_AUTORITA %>.value=="")
        {
          alert("La sede dell'Autorità Esterna è obbligatoria");
          document.f.<%=ICostantiRichiesta.CAMPO_SEDE_AUTORITA %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiRichiesta.CAMPO_COD_AUTORITA%>[document.f.<%=ICostantiRichiesta.CAMPO_COD_AUTORITA%>.selectedIndex].value == '-')
        {
          alert("L' Autorità Esterna è obbligatoria");
          document.f.<%=ICostantiRichiesta.CAMPO_COD_AUTORITA %>.focus();

          return false;
        }
<%
      }
      else
      {
%>
        if(document.f.<%=ICostantiRichiesta.CAMPO_SEDE_UFFICIO %>.value=="")
        {
          alert("La sede del Giudice dell'Esecuzione è obbligatoria");
          document.f.<%=ICostantiRichiesta.CAMPO_SEDE_UFFICIO %>.focus();
          return false;
        }

        if (document.f.<%=ICostantiRichiesta.CAMPO_COD_UFFICIO%>[document.f.<%=ICostantiRichiesta.CAMPO_COD_UFFICIO%>.selectedIndex].value == '-')
        {
          alert("L'Ufficio del Giudice dell'Esecuzione è obbligatoria");
          document.f.<%=ICostantiRichiesta.CAMPO_COD_UFFICIO %>.focus();
          return false;
        }
<%
     }
%>
      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione non valida');
        return false;
      }

      if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
      if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
        document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

      var data_to_verify_trasm = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

      if (!ControllaData(data_to_verify_trasm) )
      {
       alert('Data di Trasmissione atti non valida');
       return false;
      }
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

    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>

    <title>[S.I.E.S.] - Richiesta accertamento della data di commesso reato</title>
  </head>
  <body class="corpo">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
          <font class="label">Funzione :&nbsp;</font>
          <font class="campo"><%=testo%></font>
        </td>
        <td class="LBG">
<!--
     Torna alla pagina "Provvedimenti e Stampe per Rideterminazione Pena"
     per eliminare history.go(-1);
     predisporre un azione di Dettaglio per richiamare la pagina
     che al momento manca 02/04/2004  DL
-->
          <a href="Javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
      </tr>
    </table>
    
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciRichiesteConCodice">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
      <input type="HIDDEN" name="codice" value="<%=codice%>">
      <input type="HIDDEN" name="testo" value="<%=testo%>">
<%
    AnnotazioneManualeModel lAnnManApp = new AnnotazioneManualeModel();
    if( !annotazioneManuale.isEmpty() )
    {
      lAnnManApp = (AnnotazioneManualeModel)annotazioneManuale.firstElement();
    }
%>
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(lAnnManApp.getIdAnnotazioneManuale())%>">
  <table>
    <tr>
      <td class="l">Posizione Giuridica</td>
      <td class="L">
        <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
    </tr>
  </table>
  <table>
    <tr>
      <td class="l">Data Emissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
    </table>
    
  <table style="width: 95%;">
    <tr>
      <td colspan=8 class="titolo">Richiesta</td>
    </tr>
<%
    for(int i = 0; i<annotazioneManuale.size(); i++)
    {
      AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(i);
%>
      <tr>
        <td class="l" width="20%">Data Richiesta</td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnPrima.getDataRichiesta(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
      <tr>
        <td class="l" >DPR </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnPrima.getDescrDpr())%>
          </font>
          &nbsp;Per  &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnPrima.getDescrTipoAnnotazione())%>
          </font>
        </td>
      </tr>
<%
      if( lAnnPrima.getMotivazioni()!= null && !codice.equals("0296") )
      {
%>
        <tr>
          <td class="l">Motivazioni </td>
          <td class="L">
            <font class="campo">
              <%=StringUtils.toStringJSP(lAnnPrima.getMotivazioni())%></font>
            </font>
          </td>
        </tr>
<%
      }
    }
%>
</table>


<%
  // 0296 - Restituzione Ordine Esecuzione  -ex art. 672 c.p.p.
  if( codice.equals("0296") && (lEveOE != null &&lEveOE.getIdEvento()!= null ) )
  {
%>
    <table>
      <tr>
        <td class="l">
          Ordine Esecuzione - Data Emissione :
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveOE.getDataEmissione(),"dd-MM-yyyy"))%></font>
          </font>
          &nbsp;
          Anno / Numero Protocollo :
          &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(lEveOE.getAnnoProtocollo())%> /
            <%=StringUtils.toStringJSP(lEveOE.getProgrProtocollo())%>
          </font>
        </td>
      </tr>
    </table>
<%
  }
%>

  <table style="width: 95%;">
    <tr>
      <td class="Titolo" width="100%" colspan=6> Magistrato Assegnatario </td>
    </tr>
    <tr>
      <td class="l">Magistrato
      <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input title="Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
    </tr>
  </table>
  
  <%
  //============================================================================
  //                          Sezione con i destinatari
  //============================================================================
  %>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan=6>Comunicazione per</td>
    </tr>
<%
    if(codice.equals("0296"))
    {
%>
      <tr>
        <td class="l">Autorità Competente</td >
        <td class="l"><input type="hidden" name="autoritaC" value="S">
          <select  Title="Autorità Esterna"  class="small" name="<%=ICostantiRichiesta.CAMPO_COD_AUTORITA%>">
            <%=autoritaEsterna%>
          </select>
        </td>
        <td rowspan=2 class="l">Note</td>
        <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE_AUTORITA%>" cols=20 rows=5></textarea>
        </td>
    </tr>
    <tr>
      <td class="l">Sede Autorità
        <font class=ob>(*)</font>
      </td>
      <td class="l">
        <font class="campo">
          <input Title="Luogo Ufficio " name="<%= ICostantiRichiesta.CAMPO_SEDE_AUTORITA %>" size=35 type="text">
            <a href="Javascript:ListaComuni('f','<%=ICostantiRichiesta.CAMPO_SEDE_AUTORITA%>');">
              <img src="/images/filefolder.gif" border=0>
            </a>
        </font>
      </td>
    </tr>
<%
  }
  else
  {
 %>
    <tr>
      <td class="l">Giudice dell'esecuzione</td>
      <td class="l"><input type="hidden" name="autoritaC" value="S">
        <select  Title="Ufficio"  class="small" name="<%=ICostantiRichiesta.CAMPO_COD_UFFICIO%>">
          <%=tipoUfficio%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE_UFFICIO %>" cols=20 rows=5></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente
        <font class=ob>(*)</font>
      </td>
      <td class="l">
        <font class="campo">
          <input Title="Luogo Ufficio " value="<%=sentenza.getDescrLuogoEmittente()%>" name="<%= ICostantiRichiesta.CAMPO_SEDE_UFFICIO %>" size=35 type="text">
          <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
          <%--a href="Javascript:ListaComuniTds('f','<%=ICostantiRichiesta.CAMPO_SEDE_UFFICIO%>');"--%>
          <a href="Javascript:ListaUfficiComuni('f','<%=ICostantiRichiesta.CAMPO_SEDE_UFFICIO%>',document.f.<%=ICostantiRichiesta.CAMPO_COD_UFFICIO%>[document.f.<%=ICostantiRichiesta.CAMPO_COD_UFFICIO%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
<%
  }
%>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");
<%
  if(codice.equals("0296"))
  {
%>
    frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_AUTORITA %>","req","La sede dell'Autorità Esterna è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_AUTORITA%>","alphabetic");
<%
  }
  else
  {
%>
    frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_UFFICIO %>","req","La sede dell'Autorità Esterna è obbligatoria");
    frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_UFFICIO%>","alphabetic");
<%
  }
%>
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Giorno Trasmissione Atti dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","req","Il campo Mese Trasmissione Atti dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Anno Trasmissione Atti dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2050");
</script>
</body>
</html>