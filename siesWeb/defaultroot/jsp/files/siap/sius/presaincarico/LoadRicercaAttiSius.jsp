<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.presaincarico.action.ICostantiPresaincarico"%>
<%--<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>--%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUS" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Presa in Carico - Ricerca Atti SIUS</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
  function Verify()
    {
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.LoadRicercaAttiSius.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>.value.length != 0)
         && (document.LoadRicercaAttiSius.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>.value.length == 0) )
      {
        alert("Valorizzare Anno SIUS");
        return false;
      }
      if( (document.LoadRicercaAttiSius.<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>.value.length == 0)
           && (document.LoadRicercaAttiSius.<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>.value.length != 0) )
      {
        alert("Valorizzare Progressivo SIUS");
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
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Presa in Carico - Ricerca Atti Per Estremi - SIUS</font>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaAttiSius'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.presaincarico.action.ActListaAttiSiusRicevuti">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Indicare l'ufficio di provenienza dell'atto:</td>
      </tr>
    </table>

	<table cellspacing=2 cellpadding=2>
		<tr>
			<td class="l">Tipo Ufficio <font class=ob>(*)</font></td>
			<td class="L">
			<%-- NUOVA INFRASTRUTTURA: perchè CAMPO_CHIAVE_UFFICIO??? --%>
          		<%--<select title="tipoUfficioSIUS" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>">--%>
        		<select title="tipoUfficioSIUS" class=small name="<%=ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO%>">
            		<%= tipoUfficioSIUS %>
          		</select>
        	</td>
      	</tr>

      <tr>
        <td class="l">Sede Ufficio <font class=ob>(*)</font></td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35">
              <a href="Javascript:ListaUffici('LoadRicercaAttiSius','<%= ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO %>');">
              <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
    </table>
    <BR>
    <table cellspacing=2 cellpadding=2 >
      <tr>
        <td class="l">Numero SIUS (Anno/Progressivo) </td>
        <td class="l" colspan ='2'>
          <input Title="Anno SIUS"  type="text" name="<%= ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS %>" maxlength="4" size="4">
          /<input Title="Numero SIUS" type="text" name="<%= ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS %>" maxlength="6" size="6">
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
    var frmvalidator  = new Validator("LoadRicercaAttiSius");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_DESCR_COMUNE_UFFICIO%>","req", "Il campo Sede Ufficio è obbligatorio");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>","maxlen=6","La lunghezza massima per il Numero Fascicolo è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_PROGR_FASCICOLO_SIUS%>","numeric");

    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiPresaincarico.CAMPO_ANNO_FASCICOLO_SIUS%>","numeric");

  </script>
  </body>
</html>