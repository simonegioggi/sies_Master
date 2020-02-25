<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel" %>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<jsp:useBean id="provvedimentoRege" scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Inserimento Fascicolo SIEP da REGE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <%
  Date lDataIrrevocabilita = new Date();
  if (provvedimentoRege.getSentenza()!=null)//esiste la sentenza in siep
     {
	  	SentenzaModel sentenza = provvedimentoRege.getSentenza();
//	    modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
             //lDataIrrevocabilita = sentenza.getDataIrrevocabilita();

         if (provvedimentoRege.getRegeSentenza().getDataIrrevocabilita()==null)
             provvedimentoRege.getRegeSentenza().setDataIrrevocabilita(lDataIrrevocabilita);
     }
  else
      {
      RegeSentenzaModel sentenza = provvedimentoRege.getRegeSentenza();
      if (sentenza != null && sentenza.getDataIrrevocabilita() != null)
             lDataIrrevocabilita = sentenza.getDataIrrevocabilita();
      }
  %>

	<script language="JavaScript">
    function Verify()
    {
      if (document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value;
      if (document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_ISCRIZIONE_ATTI%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_ISCRIZIONE_ATTI%>.value;

      var data_to_verify=document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_ISCRIZIONE_ATTI%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
         alert('Data di iscrizione agli atti non valida');
         return false;
      }

      if (document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

      var data_to_verify=document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
         alert('Data di Irrevocabilità non valida');
         return false;
      }

      var data_Irrev_nuova=document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      var data_Irrev_sentenza='<%=DateUtils.getDateToString(lDataIrrevocabilita, "dd/MM/yyyy")%>';
      if (!(CompareDate(data_Irrev_nuova, data_Irrev_sentenza)) ||
          !(CompareDate(data_Irrev_sentenza, data_Irrev_nuova)) )
      {
        if(! (confirm('Data di Irrevocabilità procedimento diversa dalla data Irr. sentenza ('+ data_Irrev_sentenza +') Si vuole continuare ?' )) )
          return false;
      }

    }
  </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Iscrizione Procedimento da Dati Rege</font>&nbsp;
<%
        FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

        Date lDataIscrizione = new Date();
         String lAction = "siap.regesies.regesentenza.action.ActImportaDatiRege";

%>
      </td>
    </tr>
  </table>
  <br>
   <jsp:include page="<%=ICostantiRegeSies.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicolo">
  <table cellspacing=2 cellpadding=2>
  <tr>
      <td class="l">Data Iscrizione Procedimento</td>
      <td class="l">
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "dd")%>" type="text" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_ISCRIZIONE_ATTI%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "MM")%>" type="text" name="<%= ICostantiRegeSentenza.CAMPO_MESE_ISCRIZIONE_ATTI%>"  maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "yyyy")%>" type="text" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
		</tr>
    <tr>
      <td class="l">Data Irrevocabilità</td>
      <td class="l">
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "dd")%>" type="text" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "MM")%>" type="text" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>"  maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "yyyy")%>" type="text" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4"<%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
		</tr>
    </table>
    <table>
      <tr>
         <td class="l">Pena Detentiva</td>
         <td class="l"><input type="radio" name="tipo" value="1" checked></td>
         <td>&nbsp;</td><td>&nbsp;</td>
         <td class="l">Pena Pecuniaria</td>
         <td class="l"><input type="radio" name="tipo" value="2" ></td>
      </tr>
      <tr>
         <td class="l">Pena Sospesa</td>
         <td class="l"><input type="radio" name="tipo" value="3" ></td>
         <td>&nbsp;</td><td>&nbsp;</td>
         <td class="l">Misura Sicurezza</td>
         <td class="l"><input type="radio" name="tipo" value="4" ></td>
      </tr>
       <tr>
         <td class="l">Persona Giuridica</td>
         <td class="l"><input type="radio" name="tipo" value="5" ></td>
         <td>&nbsp;</td><td>&nbsp;</td>
         <td class="l">Giudice di Pace</td>
         <td class="l"><input type="radio" name="tipo" value="6" ></td>
      </tr>
      </table>
      <table>
       <tr>
      <td class="l">Note Procedimento</td>
      <td class="l">
        <textarea title="Note Procedimento" name="<%=ICostantiRegeSentenza.CAMPO_NOTE%>" cols=40 rows=5></textarea>
		</tr>
    <tr>
      <td>
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicolo");

    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_ISCRIZIONE_ATTI%>","req");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_ISCRIZIONE_ATTI%>","req");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_ISCRIZIONE_ATTI%>","req");

    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","req");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","req");
    frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","req");

  </script>
  </body>
</html>