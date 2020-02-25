<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeModel"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>

<jsp:useBean id="fascicolosiepe" scope="request" class="siap.siepe.fascicolo.model.FascicoloSiepeModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Modifica Procedimento SIEPE</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      // Chiamata funzione lista Attività.
      /*
			function ListaOggetti(a_formname,a_codice_incarico, a_fieldname, a_fieldcodes)
      {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%--=IWebConstants.PG_MAIN--%>?<%--=IWebConstants.ACTION_FIELD--%>=siap.sico.decodifiche.action.ActLoadListaAttivita";
            aLink += "&formname="+a_formname;
            aLink += "&codice_incarico="+a_codice_incarico;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }
      */
    </script>

    <script language="JavaScript">
			/*
    	function ResetAttivita()
      {
      	document.LoadInserisciFascicoloSIEPE.<--%=ICostantiFascicoloSiepe.CAMPO_DESCR_ATTIVITA%>.value= "";
        document.LoadInserisciFascicoloSIEPE.<--%=ICostantiFascicoloSiepe.CAMPO_COD_ATTIVITA%>.value= "";
      }
			*/
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
          <font class="campo">Modifica Procedimento SIEPE</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>


<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaFascicoloSIEPE">
  <jsp:include page="<%=ICostantiFascicoloSiepe.PG_SINTESI_SOGG_FASCICOLI%>"/>
  <br>
	<table cellspacing="2" cellpadding="2">
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--
  	<tr>
    	<td class="l">Tipo Atto Ricevuto</td>
    	<td class="L"> <%=messaggio.getDescrTipoOperazione()%></td>
  	</tr>
  	<tr>
    	<td class="l">Mittente</td>
    	<td class="L"><%=messaggio.getDescrUfficioMittente() +" "+ messaggio.getDescrSedeUfficioMittente()%></td>
  	</tr>
	--%>
  	<tr>
    	<td class="l">Fascicolo UEPE</td>
    	<td class="L">
      	<input type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>" value="<%=StringUtils.toStringJSP(fascicolosiepe.getAnnoUepe() )%>" maxlength="4" size="4">
      	/
      	<input type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>" value="<%=StringUtils.toStringJSP(fascicolosiepe.getNumUepe() )%>" maxlength="8" size="8" >
      	/
      	<input type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_PROGR_UEPE%>" value="<%=StringUtils.toStringJSP(fascicolosiepe.getProgrUepe() )%>" maxlength="2" size="2">
    	</td>
  	</tr>

  <tr>
    <td class="l">Incarico </td>
    <td class="L"><%=StringUtils.toStringJSP( fascicolosiepe.getDescrIncarico(), "-" )%></td>
  </tr>
	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	<%--
	<tr>
	    <td class="l">Attività </td>
	    <td class="l">
	      	<Textarea Title="Oggetto" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_ATTIVITA%>" cols="88" rows="6" readonly >
				<%="Dove sono le attività?"%>
	      	</Textarea>
		</td>
	</tr>
	--%>
  <tr>
    <td class="l">Note</td>
    <td class="l">
      <textarea Title="Note" name="<%=ICostantiFascicoloSiepe.CAMPO_NOTE%>" cols="88" rows="5"><%=StringUtils.toStringJSP( fascicolosiepe.getNote() )%></textarea>
    </td>
  </tr>

  <tr>
    <td>
      <input class="bottone" type="submit" value="Conferma">
    </td>
  </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActModificaFascicoloSiepe" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_ID_FASCICOLO_SIEPE%>" value="<%=fascicolosiepe.getIdFascicoloSiepe()%>" >

</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadModificaFascicoloSIEPE");
    frmvalidator.setAddnlValidationFunction("Verify");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>","minlen=4","La lunghezza del campo Anno UEPE deve essere di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_PROGR_UEPE%>","numeric");
  </script>

  </body>
</html>