<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel" %>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<jsp:useBean id="provvedimento" scope="request" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>

<%@page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<%@page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>
<html>
  <head>
    <title>[S.I.E.S.] - Inserimento Fascicolo SIEP da BDMC</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <%
  Date lDataIrrevocabilita = new Date();
  if (provvedimento.getSentenza()!=null)//esiste la sentenza in siep
     {
		/* 
	   	 * ISSUE MEV : dataIrrevocabilita non esiste per la sentenza bensì per il fascicolo
	     * Numero MEV : SIES v10
	     * Autore    : gioggi
	     * Data      : 28/gen/2016
	     * Branch    : MEV_SIES v10
	     */
	  	// EX: 	SentenzaModel sentenza = provvedimento.getSentenza();
		// 		lDataIrrevocabilita = sentenza.getDataIrrevocabilita();
	  	// NEW: FascicoloSiepModel fascicolo = provvedimento.getFascicoloSiep();
		// 		lDataIrrevocabilita = fascicolo.getDataIrrevocabilita();
	  	//***** FINE INTERVENTO MEV_SIES v10 *****//
	  	FascicoloSiepModel fascicolo = provvedimento.getFascicoloSiep();
        lDataIrrevocabilita = fascicolo.getDataIrrevocabilita();
       	Vector lProcPena = provvedimento.getSbViewProcpena();
       	SbViewProcpenaModel lModPena = (SbViewProcpenaModel)lProcPena.get(0);

        if (lModPena.getDataPassGiud()==null)
        	lModPena.setDataPassGiud(lDataIrrevocabilita);
     }
  else
      {
     	Vector lProcPena = provvedimento.getSbViewProcpena();
       	SbViewProcpenaModel lModPena = (SbViewProcpenaModel)lProcPena.get(0);
        if (lModPena != null && lModPena.getDataPassGiud() != null)
             lDataIrrevocabilita = lModPena.getDataPassGiud();
      }
  %>

	<script language="JavaScript">
    function Verify()
    {
      if (document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value;
      if (document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_ISCRIZIONE_ATTI%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_ISCRIZIONE_ATTI%>.value;

      var data_to_verify=document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_ISCRIZIONE_ATTI%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
         alert('Data di iscrizione agli atti non valida');
         return false;
      }

      if (document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

      var data_to_verify=document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
         alert('Data di Irrevocabilità non valida');
         return false;
      }

      var data_Irrev_nuova=document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
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
      <td class=LBG><font class="label">Funzione : </font><font class="campo">Iscrizione Procedimento da Dati BDMC</font>&nbsp;
<%
        FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

        Date lDataIscrizione = new Date();
         String lAction = "siap.bdmc.sbpren.action.ActImportaDatiBDMC";

%>
      </td>
    </tr>
  </table>
  <br>
   <jsp:include page="<%=ICostantiSbPren.PAGE_DETTAGLIO_PROVVEDIMENTO_INCLUDE%>"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicolo">
  <table cellspacing=2 cellpadding=2>
  <tr>
      <td class="l">Data Iscrizione Procedimento</td>
      <td class="l">
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "dd")%>" type="text" name="<%= ICostantiSbPren.CAMPO_GIORNO_ISCRIZIONE_ATTI%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "MM")%>" type="text" name="<%= ICostantiSbPren.CAMPO_MESE_ISCRIZIONE_ATTI%>"  maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "yyyy")%>" type="text" name="<%= ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>" maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
      </td>
		</tr>
    <tr>
      <td class="l">Data Irrevocabilità</td>
      <td class="l">
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "dd")%>" type="text" name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "MM")%>" type="text" name="<%= ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>"  maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
        /
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "yyyy")%>" type="text" name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4"<%=IWebConstants.UTIL_DATA_ANNO%>>
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
        <textarea title="Note Procedimento" name="<%=ICostantiSbPren.CAMPO_NOTE%>" cols=40 rows=5></textarea>
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

    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_ISCRIZIONE_ATTI%>","req");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_ISCRIZIONE_ATTI%>","req");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_ISCRIZIONE_ATTI%>","req");

    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRREVOCABILITA%>","req");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_MESE_DATA_IRREVOCABILITA%>","req");
    frmvalidator.addValidation("<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","req");

  </script>
  </body>
</html>