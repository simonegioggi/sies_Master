<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe" %>
<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="messaggio" scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="incarichi" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Procedimento SIEPE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;

      // Chiamata funzione lista Attività.
      function ListaOggetti(a_formname,a_codice_incarico, a_fieldname, a_fieldcodes)
      {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaAttivita";
            aLink += "&formname="+a_formname;
            aLink += "&codice_incarico="+a_codice_incarico;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }
    </script>


    <script language="JavaScript">
         function ResetAttivita()
         {
             document.LoadInserisciFascicoloSIEPE.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_ATTIVITA%>.value= "";
             document.LoadInserisciFascicoloSIEPE.<%=ICostantiFascicoloSiepe.CAMPO_COD_ATTIVITA%>.value= "";
         }
    </script>
    <script language="JavaScript">

      function Verify()
      {
         return true;
      }
   </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
          <font class="campo">Iscrizione Procedimento SIEPE</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicoloSIEPE">


  <br>

  <table cellspacing="2" cellpadding="2">

  <tr>
    <td class="l">Tipo Atto Ricevuto</td>
    <td class="L"> <%=messaggio.getDescrTipoOperazione()%>
    </td>
  </tr>

  <tr>
    <td class="l">Mittente</td>
    <td class="L"><%=messaggio.getDescrUfficioMittente() +" "+ messaggio.getDescrSedeUfficioMittente()%></td>
  </tr>

  <tr>
    <td class="l">Fascicolo UEPE<font class="ob">(*)</font></td>
    <td class="L">
      <input  value="" type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>" maxlength="4" size="4">
      /
      <input  value="" type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>" maxlength="8" size="8" >
      /
      <input  value="" type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_PROGR_UEPE%>" maxlength="2" size="2">
    </td>
  </tr>


  <tr>
    <td class="l">Incarico <font class="ob">(*)</font></td>
    <td class="L">
      <select title="Incarichi"  class=small name="<%=ICostantiFascicoloSiepe.CAMPO_COD_INCARICO%>" onChange="Javascript:ResetAttivita();">
        <%=incarichi%>
      </select>
  </tr>

  <tr>
    <td class="l">Attività </td>
    <td class="l">
      <Textarea Title="Oggetto" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_ATTIVITA%>" cols=88 rows=6 readonly >
      </Textarea>
      <a href="Javascript:ListaOggetti('LoadInserisciFascicoloSIEPE',document.LoadInserisciFascicoloSIEPE.<%=ICostantiFascicoloSiepe.CAMPO_COD_INCARICO%>[document.LoadInserisciFascicoloSIEPE.<%=ICostantiFascicoloSiepe.CAMPO_COD_INCARICO%>.selectedIndex].value, '<%=ICostantiFascicoloSiepe.CAMPO_DESCR_ATTIVITA%>', '<%=ICostantiFascicoloSiepe.CAMPO_COD_ATTIVITA%>');">
      <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
</td>
  </tr>

  <tr>
    <td class="l">Note</td>
    <td class="l">
      <Textarea Title="Note" name="<%= ICostantiFascicoloSiepe.CAMPO_NOTE %>" cols=88 rows=5></textarea>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActInserisciFascicoloSiepe" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_COD_ATTIVITA%>" value="" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_COD_UFFICIO_MITTENTE%>" value="<%=messaggio.getCodUfficioMittente()%>" >
  <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=messaggio.getIdMessaggio()%>" >

</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicoloSIEPE");
    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>","req","Il campo Anno Fascicolo Uepe è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>","minlen=4","La lunghezza del campo Anno UEPE deve essere di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>","req","Il campo Numero Fascicolo Uepe è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_PROGR_UEPE%>","numeric");
  </script>

  </body>
</html>