<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.jms.util.ParserMessage"%>

<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.model.DettaglioFascicoloModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.jms.messaggio.model.ContatoreEsitiModel" %>
<%@ page import="siap.jms.config.JMSProperties" %>
<%@ page import="siap.jms.jmscode.model.JmsCodeModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>

<jsp:useBean id="soggetto" scope="request" class="siap.sico.soggetto.model.SoggettoModel" />
<jsp:useBean id="Messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Soggetto</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

    <script language="JavaScript">
    function Verify()
    {
    //  alert (document.SubmitFascicoli.Selection.length  );
      if(!document.SubmitFascicoli.Selection.length)
      {
      if(!document.SubmitFascicoli.Selection.checked)
        {
          alert ("Selezionare almeno un fascicolo da trasferire.");
          return false;
        }
      }
     else
     {
    //  alert (document.SubmitFascicoli.Selection.length );
      if(document.SubmitFascicoli.Selection.length > 0)
        {
          var loop = 0;

          for (var i = 0; i <document.SubmitFascicoli.Selection.length; i++)
          {
              if(document.SubmitFascicoli.Selection[i].checked)
                 loop=1;
          }
          if (loop == 0)
          {
             alert ("Selezionare almeno un fascicolo da trasferire.");
             return false;
          }
        }
     }
      return true;
    }

    </script>

  </head>
  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Soggetto Trovato su altra BDI</font>
          </td>
          <td class="LBG">
            <a href="Javascript:history.go(-1);">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
         </tr>
      </table>
    </FORM>
    <br>
    <table cellspacing=2 cellpadding=2>
<!----------- MESSAGGIO --------------------->
       <tr>
        <td class="Titolo" colspan=4>Dati Messaggio</td>
      </tr>
       <tr>
        <td class="l"><font class="label">BDI Mittente</font></td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrBdiMittente())%>&nbsp;</font></td>
      </tr>
       <tr>
        <td class="l"><font class="label">Data Invio</font></td>
        <td class="l"><font class="campo"><%=DateUtils.getDateToString(Messaggio.getDataInvio(),"dd-MM-yyyy  HH:mm:ss")%>&nbsp;</font></td>
      </tr>

    </table>

  <table cellspacing=0 cellpadding=0 >
  <tr><td>&nbsp;</td>    </tr>
   <tr>
        <td class="Titolo" colspan=4>Dati Soggetto</td>
   </tr>
   <tr>
      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>&nbsp;(<%=soggetto.getIdSoggetto()%>)
      </font>&nbsp;
<%
        if (soggetto.getSesso().compareTo("F")==0)
        {
%>
          <font class="label">nata il :</font>&nbsp;
<%
        }
        else
        {
%>
          <font class="label">nato il :</font>&nbsp;
<%
        }

if(soggetto.getDataNascita() == null){
   if(soggetto.getDataNascitaPresunta().equals("S")) {%>
      <font class="campo"><%=StringUtils.toStringJSP(soggetto.getAnnoNascita())%></font>&nbsp;
<%}else
   {%>
      <font class="campo">***</font>&nbsp;
<%}}else{%>
      <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggetto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
<%}%>
      <font class="label">in : </font>
      <font class="campo">

 <%     
 if (soggetto.getDescrComuneNascita()!=null && soggetto.getDescrComuneNascita().length()>1)
      {
%>        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>


<%
      }
      else
      {
%> <%=soggetto.getDescrStatoNascita()%>
<%
      }
%>&nbsp;<font class="label">Codice CUI: </font>
      <font class="campo">
          <%=soggetto.getCodAfis()%>
      </font>&nbsp;
      </font>
     </td>
    </tr>
 </table>

<form name="SubmitFascicoli">

  <table cellspacing=2 cellpadding=2>
  <tr><td>&nbsp;</td></tr>

<tr> <td class ="Titolo" colspan =8>Elenco Procedimenti associati al soggetto</td></tr>

   <tr>
      <td class="int">Data Titolo Esecutivo</td>
      <td class="int">Autorità Titolo Esecutivo</td>
      <td class="int">Data Irrevocabilità</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Ufficio Esecuzione</td>
      <td class="int">Data di Iscrizione</td>
      <td class="int">Stato del Procedimento</td>
      <td class="int">Selezione</td>
    </tr>
<%
if (soggetto.getDettaglioFascicoli() != null)
{
   Iterator itx = soggetto.getDettaglioFascicoli().iterator();

    while ( itx.hasNext())
    {
      DettaglioFascicoloModel fascicolo = (DettaglioFascicoloModel)itx.next();
%>
    <tr>
      <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getFascicoloSiep().getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiep().getSentenza().getDescrTipoAutoritaEmittente()%></font> di <font class="label"><%=fascicolo.getFascicoloSiep().getSentenza().getDescrLuogoEmittente()%></font></td>
      <!-- modifica conseguente alla variazione di SentenzaModel -->
	  <td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getFascicoloSiep().getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiep().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiep().getChiaveProgr()%></font></td>
      <td class="c"><font class="label"><%=fascicolo.getFascicoloSiep().getDescrTipoUfficio()%> di <%=fascicolo.getFascicoloSiep().getDescrComuneUfficio()%></font></td>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSiep().getDataIscrizione(),"dd-MM-yyyy"))%>&nbsp;</font></td>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--td class="c"><font class="label"><%=DateUtils.getDateToString(fascicolo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font></td--%>
      <%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrTipoProvvedimento()%></font></td--%>
      <%--td class="c"><font class="label"><%=fascicolo.getSentenza().getDescrLuogoEmittente()%></font></td--%>
      <td class="c"><font class="label"><%=StringUtils.toStringJSP(fascicolo.getFascicoloSiep().getDescrStatoProcedimento())%>&nbsp;</font></td>
      <td class="c">
      <%--input type="checkbox" name="Selection" value=<%=fascicolo.getFascicoloSiep().getIdFascicoloSiep()%>--%>
      <input type="checkbox" name="Selection" >
      </td>

    </tr>

<%}
}
%>
    <tr><td>&nbsp;</td></tr>
 <tr><td>&nbsp;</td></tr>


       <tr>
          <td colspan=3>
            <input class=bottone  type="submit" value="Conferma Trasferimento Procedimenti Selezionati">
          </td>
        </tr>

 </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.jms.action.ActPresaInCaricoMultiplaFascicoloSiep">
        <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">
        <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=soggetto.getIdSoggetto()%>">
     </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("SubmitFascicoli");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

<br><br><br>
  </body>
</html>