<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.utente.action.ICostantiUtente" %>

<html>
<head>
  <title> Ricerca Utenti Per Ufficio</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
  
  function Verify()
  {
    if(   (document.ricercaUtenti.<%=ICostantiUtente.CAMPO_COGNOME%>.value=="") 
       && (document.ricercaUtenti.<%=ICostantiUtente.CAMPO_NOME%>.value=="") 
       && (document.ricercaUtenti.<%=ICostantiUtente.CAMPO_COD_UTENTE%>.value=="")
       && (document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value=="-") 
       && (document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value=="")
      )
    {
      alert("Bisogna inserire almeno un campo");
      return false;
    }
    else
    {
      if ((document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value=="-") && (document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value==""))
      {
        return true;
      }

      if((document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value!="-") && (document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>.value!=""))
      {
        return true;
      }
      else
      {
        if (document.ricercaUtenti.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value=="-")
        {
          alert("Il campo Tipo Ufficio è obbligatorio");
          return false;
        }
        else
        {
          alert("Il campo Sede è obbligatorio");
           return false;
        }
      }
    }
  }
</script>
<script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    </script>
</head>

<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUffcio"  scope="request"  class="java.lang.String"/>
<jsp:useBean id="isSysAdmin"  scope="request"  class="java.lang.String"/>

<body class="corpo">

<form method="post" action="<%=IWebConstants.PG_MAIN%>" name='ricercaUtenti'>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.utente.action.ActRicercaUtentiPerUfficio">
  
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Utenti</font></td>
    </tr>
  </table>
  
  <div style="width: 100%;">
    <table>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><input title="Cognome" type="Text" name="<%=ICostantiUtente.CAMPO_COGNOME%>" size="35" maxlength="35"></td>
      </tr>
      <tr>
        <td class="l">Nome</td>
        <td class="l"><input title="Nome" type="Text" name="<%=ICostantiUtente.CAMPO_NOME%>" size="35" maxlength="35"></td>
      </tr>
      <tr>
        <td class="l">Codice Utente</td>
        <td class="l"><input title="Codice" type="Text" name="<%=ICostantiUtente.CAMPO_COD_UTENTE%>" size="35" maxlength="35"></td>
      </tr>
      <tr>
        <td class="L">Ufficio</td>
        <td class="l"><select name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>" ><%= tipoUfficio %></select></td>
      </tr>
      <tr>
        <td class="l">Sede</td>
        <td class="L">
        <% if( isSysAdmin.equalsIgnoreCase("S")) { %>
          <input title="Sede" type="text" name="<%= ICostantiUfficio.CAMPO_SEDE_UFFICIO %>" maxlength="35" size="35">
          <a href="Javascript:ListaComuni('ricercaUtenti','<%= ICostantiUfficio.CAMPO_SEDE_UFFICIO %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        <% } else { %>
            <input title="Sede" type="text" name="<%= ICostantiUfficio.CAMPO_SEDE_UFFICIO %>" maxlength="35" size="35" value="<%=sedeUffcio%>" readonly="readonly">
        <% } %>
        </td>
      </tr>
      <tr>
        <td colspan="2"><input class="bottone" type="submit" name="RICERCA" value="Ricerca"></td>
      </tr>
  </table>
<br>
</div>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("ricercaUtenti");

  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","maxlen=50","La lunghezza massima per il nome è di 50 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_NOME %>","alpha","Il campo Nome Utente non può contenere caratteri non alfabetici");

  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","maxlen=50","La lunghezza massima per il cognome è di 50 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COGNOME %>","alpha","Il campo Cognome Utente non può contenere caratteri non alfabetici");

  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COD_UTENTE %>","minlen=6","La lunghezza per il codice utente è di 6 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COD_UTENTE %>","maxlen=6","La lunghezza per il codice utente è di 6 caratteri");
  frmvalidator.addValidation("<%= ICostantiUtente.CAMPO_COD_UTENTE %>","alphanumeric","Il campo Codice Utente non può contenere caratteri non alfanumerici");

  frmvalidator.addValidation("<%=ICostantiUfficio.CAMPO_SEDE_UFFICIO%>","alpha","Il campo Sede Uffcio non può contenere caratteri non alfabetici");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>