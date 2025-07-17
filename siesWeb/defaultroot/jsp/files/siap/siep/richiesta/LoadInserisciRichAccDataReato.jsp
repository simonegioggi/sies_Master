<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>


<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.richiesta.action.ICostantiRichiesta"%>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>

<jsp:useBean id="annotazioneManuale"    scope="request" class="java.util.Vector"/>
<jsp:useBean id="codice"                scope="request" class="java.lang.String" />
<jsp:useBean id="posizioneGiuridica"    scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="magistratocompetente"  scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="tipoUfficio"           scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"                scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="sentenza"              scope="request" class="siap.siep.sentenza.model.SentenzaModel" />

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
          alert("Il  Magistrato Assegnatario è obbligatorio");
          document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
          return false;
        }

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
    </script>
    <title>[S.I.E.S.] - Richiesta accertamento della data di commesso reato</title>
  </head>
  
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
          <font class="label">Funzione :&nbsp;</font>
          <font class="campo">Richiesta accertamento della data di commesso reato </font>
        </td>
        <td class="LBG">
<!--
     Torna alla pagina "Provvedimenti e Stampe per Rideterminazione Pena".
     Per eliminare history.go(-1);
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
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciRichiestaAccDataReato">
      <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
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
      <td class="L" colspan=5>
        <font class="campo"><%=posizioneGiuridica.getDescrPosizioneGiuridica()%></font>
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
  
  <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
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
<%
  AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(0);
%>
  </table>
  <table style="width: 95%;">
    <tr>
      <td colspan=8 class="titolo">Richiesta</td>
    </tr>
    <tr>
      <td class="l" width="10%">DPR </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getDescrDpr())%>
        </font>
      </td>
    </tr>
  </table>
  <table style="width: 95%;">
   <tr>
     <td class="Titolo" width="100%" colspan=6> Magistrato Assegnatario </td>
   </tr>
   <tr>
     <td class="l">Magistrato
     <td class="L">
       <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
           <img src="/images/filefolder.gif" border=0>
         </a>
     </td>
   </tr>
  </table>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan=6>Comunicazione per</td>
    </tr>
    <tr>
      <td class="l">Giudice dell'esecuzione</td>
      <td class="l"><input type="hidden" name="autoritaC" value="S">
        <select  Title="Ufficio"  class="small" name="<%=ICostantiRichiesta.CAMPO_COD_UFFICIO%>">
          <%=tipoUfficio%>
        </select>
      </td>
      <td rowspan=2 class="l">Note</td>
      <td rowspan=2 class="L">
        <TEXTAREA title="Note" name="<%= ICostantiRichiesta.CAMPO_NOTE_UFFICIO %>"  cols=20 rows=5 ></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente  <font class=ob>(*)</font></td><td class="l">
        <font class="campo">
          <input Title="Luogo Ufficio " value="<%=sentenza.getDescrLuogoEmittente()%>" name="<%= ICostantiRichiesta.CAMPO_SEDE_UFFICIO %>" size=35 type="text">
          <a href="Javascript:ListaComuniTds('f','<%=ICostantiRichiesta.CAMPO_SEDE_UFFICIO%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
      </td>
    </tr>
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

    frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_UFFICIO %>","req","La sede del Giudice dell' Esecuzione è obbligatoria");
<%--     frmvalidator.addValidation("<%= ICostantiRichiesta.CAMPO_SEDE_UFFICIO%>","alphabetic"); --%>
  </script>
</body>
</html>