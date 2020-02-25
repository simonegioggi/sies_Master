<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.Utils"%>
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
            <font class="campo">Elenco Soggetti Trovati su altra BDI</font>
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
        <td class="Titolo" colspan=4>Dati Soggetto cercato</td>
   </tr>
   <tr>




      <td class="L" width=100%><font class="label">Soggetto : </font>
      <font class="campo">
          <%=soggetto.getCognome()%>&nbsp;<%=soggetto.getNome()%>
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

<%     if (soggetto.getDescrComuneNascita().compareTo("-")==0)
      {%>
        <%=soggetto.getDescrStatoNascita()%>
<%
      }
      else
      {%>
        <%=soggetto.getDescrComuneNascita()+ "  ("+soggetto.getCodProvinciaNascita()+")" %>
    <%}
%>
&nbsp;<font class="label">Codice CUI: </font>
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

<tr> <td class ="Titolo" colspan =8>Elenco Soggetti individuati</td></tr>

   <tr>
      <td class="int">ID Soggetto</td>
      <td class="int">Codice Afis</td>
      <td class="int">Codice Fiscale</td>
      <td class="int">Presenza titoli esecutivi</td>
      <td class="int">Comune Casellario</td>
      <td class="int">Azioni</td>
    </tr>
<%
      ParserMessage lParser = null;

      if(Messaggio!=null)
      {
        lParser = new ParserMessage(Messaggio.getTreeModel());
        if ((lParser.getSoggettoArray() != null) && lParser.getSoggettoArray().size()>1 )
        {
          for(int k=0; k<lParser.getSoggettoArray().size(); k++)
          {
            SoggettoModel soggettoCorrente = (SoggettoModel)lParser.getSoggettoArray().get(k);
%>
            <tr>
              <td class="c"><font class="label"><%=soggettoCorrente.getIdSoggetto()%></font></td>
              <td class="c">
<%            if (Utils.isNullObj(soggettoCorrente.getCodAfis()))
              {%>
                <font class="label">-</font>
              <%}else{%>
                <font class="label"><%=soggettoCorrente.getCodAfis()%></font>
              <%}%>
              </td>
              <td class="c">
<%            if (Utils.isNullObj(soggettoCorrente.getCodFiscale()))
              {%>
                <font class="label">-</font>
              <%}else{%>
                <font class="label"><%=soggettoCorrente.getCodFiscale()%></font>
              <%}%>
              </td>
              <td class="c">
<%            if (Utils.isNullObj(soggettoCorrente.getFlagPresenzaFascicolo()))
              {%>
                <font class="label">-</font>
              <%}else if (soggettoCorrente.getFlagPresenzaFascicolo().compareTo("N")==0){%>
                <font class="label">NO</font>
              <%}
              else {%>
                <font class="label">SI</font>
              <%}%>
              </td>
              <td class="c">
                <font class="label"><%=soggettoCorrente.getDescrComuneCasellario()%></font>
              </td>
              <td class=C>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.jms.action.ActDettaglioSoggettoTrovato&IdMessaggio=<%=Messaggio.getIdMessaggio()%>&daElenco=SI&indice=<%=k%> ">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Visto" border="0">
                </a>
              </td>
            </tr>
        <%}
        }
      }
%>
    </table>
  </form>

    <br>
  </body>
</html>