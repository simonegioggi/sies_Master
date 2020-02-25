<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.istanza.model.IstanzaModel"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<%@ page import="java.util.Date"%>

<jsp:useBean id="uffici" scope="request" class="java.lang.String"/>

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

    function Verify()
    {
      var data_to_verify=document.TrasferisciIstanzaStessaBDI.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.TrasferisciIstanzaStessaBDI.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.TrasferisciIstanzaStessaBDI.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
      if ( ! ControllaData(data_to_verify))
      {
        alert('Data di nascita non valida');
        return false;
      }
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
        lAction = "siap.siep.istanza.action.ActTrasferisciIstanza";
        lDataPresentazione = new Date();
%>
          <font class="campo">Trasferimento Istanza Stessa BDI</font>
         </tr>

  </table>


    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="TrasferisciIstanza" onSubmit="return Verify();">
   <table>
   <tr>
      </tr>
       <tr>
          <td class="l">Destinatario </td >
           <td class="L">
             <select Title="Destinatario" name="<%=ICostantiIstanza.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>" >
               <%=uffici%>
             </select>
         </td>
      </tr>
    <tr><td class="l">Sede Destinatario </td><td class="L">
       <input title="Sede Destinatario"  type="text" name="<%= ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('TrasferisciIstanza','<%= ICostantiIstanza.CAMPO_COD_LUOGO_DESTINATARIO %>');">
       <img src="/images/filefolder.gif" border=0></a></td>
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
      var frmvalidator  = new Validator("TrasferisciIstanzaStessaBDI");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","req","Il Cognome del Soggetto è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alphabetic");

        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","req","Il Nome del Soggetto è obbligatorio");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alphabetic");

        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
        frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");

        frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>