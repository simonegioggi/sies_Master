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

<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<html>
<head>
  <title>[S.I.E.S.] - Gestione Istanza </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
  <script language="JavaScript">

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

      if (document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE%>.value.length==1)
			  document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE%>.value='0'+document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE%>.value;
      if (document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE%>.value.length==1)
			  document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE%>.value='0'+document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE%>.value;
      data_to_verify = document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE%>.value+'/'+document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE%>.value+'/'+document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE%>.value;
      if (!ControllaDataPassaVuota(data_to_verify) )
        {
         alert('Data Istanza non valida');
	       return false;
       }




/****************************Modifica******************************************/
// Controllo di Obligatorietà Oggetto
      if(document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>[document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>.selectedIndex].value=='-')
      {
        alert("L'Oggetto dell'Istanza è obbligatorio");
        document.LoadInserisciIstanza.<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>.focus();

        return false;
      }
/**********************************************************************/

      return true;
    }
  </script>
</head>
<body class="corpo">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciIstanza" onSubmit="return abilitaSentenza();">
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
          lAction = "siap.siep.istanza.action.ActInserisciIstanzaAnnTrasmissione";
          lDataPresentazione = new Date();
%>
          <font class="campo">Inserimento Istanza Annotazione Trasmissione</font>
<%
        }
        else if( modalita.equals("M") )
        {
          lAction = "siap.siep.istanza.action.ActModificaIstanzaAnnTrasmissione";
          lIstanza = istanzaSoggettoEventoFascicoloSiep.getIstanza();
          lSoggetto = istanzaSoggettoEventoFascicoloSiep.getSoggetto();
          if(istanzaSoggettoEventoFascicoloSiep.getFascicoloSiep() != null)
          {
            lFascicolo = istanzaSoggettoEventoFascicoloSiep.getFascicoloSiep();
          }
          lDataPresentazione = lIstanza.getDataPresentazione();
%>
          <font class="campo">Modifica Istanza Annotazione Trasmissione</font>
<%
        }
%>

<!------------------
------------------------->
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
<%if(fascicolo!= null && fascicolo.getIdFascicoloSiep()!= null){%>
 <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<%}else{%>
   <tr><td>&nbsp;</td></tr>
<%}%>
      <table style="width: 95%;">
<input type="hidden" name="modalita" value="<%=modalita%>">
        <tr><td class="Titolo" colspan=4>Procedimento (N.SIEP)</td></tr>
        <tr>
          <td class="L" width="25%"> Anno/Numero <font class="ob">(*)</font></td>
          <td class="L">
<%if(fascicolo!= null && fascicolo.getIdFascicoloSiep()!= null){%>

            <input type="text" title="Anno" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(fascicolo.getChiaveAnno())%>" <%= (!lFascicoloModificabile) ? "disabled" : "" %>>
            /
            <input type="text" title="Numero" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14" value="<%=StringUtils.toStringJSP(fascicolo.getChiaveProgr())%>" <%= (!lFascicoloModificabile) ? "disabled" : "" %>>
<%}else{%>

            <input type="text" title="Anno" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(lFascicolo.getChiaveAnno())%>" <%= (!lFascicoloModificabile) ? "disabled" : "" %>>
            /
            <input type="text" title="Numero" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14" value="<%=StringUtils.toStringJSP(lFascicolo.getChiaveProgr())%>" <%= (!lFascicoloModificabile) ? "disabled" : "" %>>


<%}%>
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
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lDataPresentazione, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
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
      if(lFascicoloModificabile)
      {
%>
         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE%>","req","Il campo Giorno Data Istanza  è obbligatorio");
         frmvalidator.addValidation("<%=ICostantiIstanza.CAMPO_GIORNO_DATA_PRESENTAZIONE%>","numeric");

         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE%>","req","Il campo Mese Data Istanza è obbligatorio");
         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_MESE_DATA_PRESENTAZIONE%>","numeric");

         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE%>","req","Il campo Anno Data Istanza è obbligatorio");
         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE%>","numeric");
         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE%>","gt=1900");
         frmvalidator.addValidation("<%= ICostantiIstanza.CAMPO_ANNO_DATA_PRESENTAZIONE%>","lt=2050");

         frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","req","Il campo Numero Fascicolo è obbligatorio");
         frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");
         frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>","numeric");

         frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","req","Il campo Anno Fascicolo Istanza è obbligatorio");
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