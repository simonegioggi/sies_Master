<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="sentenza" scope="session" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Sentenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	<script language="JavaScript">
    function Verify()
    {
      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value;
      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value;

      var data_to_verify=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
         alert('Data di iscrizione agli atti non valida');
         return false;
      }

      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

      var data_to_verify=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
         alert('Data di Irrevocabilità non valida');
         return false;
      }

      var data_Irrev_nuova=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      /* 
	   * ISSUE MEV : dataIrrevocabilita non esiste per la sentenza bensì per il fascicolo
	   * Numero MEV : SIES v10
	   * Autore    : gioggi
	   * Data      : 28/gen/2016
	   * Branch    : MEV_SIES v10
	   */
	  // EX: sentenza.getDataIrrevocabilita()
	  // NEW: fascicolo.getDataIrrevocabilita()
	  //***** FINE INTERVENTO MEV_SIES v10 *****//
      var data_Irrev_sentenza='<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd/MM/yyyy")%>';
      if (!(CompareDate(data_Irrev_nuova, data_Irrev_sentenza)) ||
          !(CompareDate(data_Irrev_sentenza, data_Irrev_nuova)) )
      {
        if(! (confirm('Data di Irrevocabilità procedimento diversa dalla data Irr. sentenza ('+ data_Irrev_sentenza +') Si vuole continuare ?' )) )
          return false;
      }
     return true;
    }
  </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        FascicoloSiepModel lFascicolo = new FascicoloSiepModel();

        Date lDataIscrizione = new Date();
        Date lDataIrrevocabilita = new Date();
//      modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
        //if (sentenza != null && sentenza.getDataIrrevocabilita() != null)
        //     lDataIrrevocabilita = sentenza.getDataIrrevocabilita();
        String lAction = new String();

        if( modalita.equals("I") )
        {
          lAction = "siap.siep.fascicolo.action.ActInserisciFascicolo";
%>
          <font class="campo">Inserimento Procedimento</font>
<%
        }
        else if( modalita.equals("M") )
        {
          lAction = "siap.siep.fascicolo.action.ActModificaFascicolo";
          lFascicolo = fascicolo;

          lDataIscrizione = lFascicolo.getDataIscrizione();
          lDataIrrevocabilita =   lFascicolo.getDataIrrevocabilita();
%>
          <font class="campo">Modifica Procedimento</font>
<%
    }
%>
      </td>
    </tr>
  </table>

  <br>
<%
    if(modalita.equals("I"))
    {
    %>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaAttribuzione.jsp"/>
    <%
    }
    else if(modalita.equals("M"))
    {
    %>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <%
    }
%>
  <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicolo">
  <table cellspacing=2 cellpadding=2>
  <tr>
      <td class="l">Data Iscrizione Procedimento</td>
      <td class="l">
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "dd")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "MM")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        /
        <input title="Data Iscrizione Atti" value="<%=DateUtils.getDateToString(lDataIscrizione, "yyyy")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
		</tr>
    <tr>
      <td class="l">Data Irrevocabilità</td>
      <td class="l">
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "dd")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "MM")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(lDataIrrevocabilita, "yyyy")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
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
        <textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols=40 rows=5><%=StringUtils.toStringJSP(lFascicolo.getNote())%></textarea>
		</tr>
    <tr>
      <td>
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(lFascicolo.getIdFascicoloSiep())%>">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">

  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicolo");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>","req");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>","req");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>","req");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>","req");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>","req");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","req");

  </script>
  </body>
</html>