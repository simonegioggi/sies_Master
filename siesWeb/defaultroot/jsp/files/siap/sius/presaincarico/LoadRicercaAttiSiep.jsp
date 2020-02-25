<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.presaincarico.action.ICostantiPresaincarico"%>
<%-- MEV10-s3: aggiunta import di classe ed aggiunto riferimento all'oggetto "ufficioUtenteConnesso" --%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>

<jsp:useBean id="ufficioUtenteConnesso" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIEP" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Presa in Carico - Ricerca Atti Per Estremi - SIEP</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.LoadRicercaAttiSiep.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length != 0)
           && (document.LoadRicercaAttiSiep.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIEP");
        return false;
      }
      if( (document.LoadRicercaAttiSiep.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length == 0)
           && (document.LoadRicercaAttiSiep.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>.value.length != 0) )
      {
        alert("Valorizzare Progressivo SIEP");
        return false;
      }
    return true;

    }
  </script>

  <script language="JavaScript">
      var desktop;
      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Presa in Carico - Ricerca Atti Per Estremi - SIEP</font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaAttiSiep'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.presaincarico.action.ActListaAttiSiepRicevuti">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Indicare l'ufficio di provenienza dell'atto: </td>
      </tr>
    </table>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Tipo Procura<font class=ob>(*)</font></td>
        <td class="L">
          <select title="tipoUfficioSIEP" class=small name="<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>" >
            <%= tipoUfficioSIEP %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede Ufficio <font class=ob>(*)</font></td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadRicercaAttiSiep','<%= ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO %>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Numero SIEP(Anno/Progressivo) </td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIEP "  type="text" name="<%= ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP %>" maxlength="4" size="4">
          /<input Title="Numero SIEP " type="text" name="<%= ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP %>" maxlength="6" size="6">
        </td>
      </tr>
      <tr>
        <td class="l">Visualizza anche gli atti già presi in carico&nbsp;
        </td>
        <td class="l">
          <input type=checkbox name="<%=ICostantiPresaincarico.CAMPO_INCLUDE_INCARICO%>" value=1></td>
        </td>
        <td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      </tr>

    </table>
    <BR>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
        </td>
      </tr>

    </table>
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaAttiSiep");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>","req", "Il campo Sede Ufficio è obbligatorio");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIEP%>","numeric");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIEP%>","numeric");

 	// MEV10-s3: aggiunta impostazione di un valore nella combo del tipo ufficio
	<% String codTipoUfficio = ufficioUtenteConnesso.getCodTipoUfficio(); %>
	var selectTipoUfficio = document.LoadRicercaAttiSiep.<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>;
	if ("UDSM" == "<%= codTipoUfficio %>" || "TDSM" == "<%= codTipoUfficio %>") {
		selectTipoUfficio.value = "PMM";
	}
	else if ("UDS" == "<%= codTipoUfficio %>" || "TDS" == "<%= codTipoUfficio %>") {
		selectTipoUfficio.value = "PM";
	}
  </script>
  </body>
</html>