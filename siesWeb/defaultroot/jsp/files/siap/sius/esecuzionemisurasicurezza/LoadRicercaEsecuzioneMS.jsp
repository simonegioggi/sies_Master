<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.esecuzionemisurasicurezza.action.ICostantiEsecuzioneMS" %>

<head>
  <title> [S.I.E.S.] - Ricerca Procedimenti Relativi - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
  function VerifySingolo()
  {
    // In caso di ricerca singola, Anno e progressivo sono obbligatori.
    if( (document.Singolo.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR%>.value.length == 0)
       && (document.Singolo.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>.value.length == 0) )
    {
      alert("Anno/Numero Procedimento Obbligatori!");
      return false;
    }
    // Non è possibile specificare solo il numero o solo l'anno
    if( (document.Singolo.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR%>.value.length != 0)
       && (document.Singolo.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>.value.length == 0) )
    {
      alert("Valorizzare Anno inizio ricerca");
      return false;
    }
    if( (document.Singolo.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR%>.value.length == 0)
       && (document.Singolo.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>.value.length != 0) )
    {
      alert("Valorizzare Progressivo inizio ricerca");
      return false;
    }
    return true;
  }

  function VerifyF()
  {
    if( (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
    {
      alert("Valorizzare Anno inizio ricerca");
      return false;
    }
    if( (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
    {
      alert("Valorizzare Numero inizio ricerca");
      return false;
    }
    if( (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
    {
      alert("Valorizzare Anno di fine ricerca");
      return false;
    }
    if( (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
    {
      alert("Valorizzare Numero di fine ricerca");
      return false;
    }
    // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
    if( (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
         && (document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
    {
      if(document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
      {
        alert("Anno inizio maggiore Anno fine");
        return false;
      }
      else if(document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
      {
        if(parseInt(document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>.value) < parseInt(document.f.<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>.value))
        {
          alert("Numero iniziale maggiore del numero finale");
          return false;
        }
      }
    }
  return true;
  }
  </script>
</head>

<body class="corpo">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='Singolo'>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.esecuzionemisurasicurezza.action.ActRicercaEsecuzioneMS">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimenti Esecuzione Misure Sicurezza</font></td>
    </tr>
  </table>

  <br>

  <table cellpadding=2 cellspacing=2 width=50% >
    <tr><td class="Titolo" colspan=4>Singolo</td></tr>
    <tr>
      <td class="L"> Anno/Numero Procedimento <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="L">
        <input type="text" title="Anno Procedimento" name="<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Procedimento" name="<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR%>" maxlength="6" size="6">
      </td>
     <tr>
      <td>
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca">
      </td>
    </tr>
    </tr>
  </table>
</form>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.esecuzionemisurasicurezza.action.ActRicercaEsecuzioneMS">

  <table cellpadding=2 cellspacing=2 width=50% >
    <tr><td class="Titolo" colspan=4>Periodo</td></tr>
    <tr>
      <td class="L">
        <font class="label">
          Anno/Numero Procedimento Iniziale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="6" size="6">
      </td>
    </tr>
    <tr>
      <td class="L">
        <font class="label">
          Anno/Numero Procedimento Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="6" size="6">
      </td>
     <tr>
      <td>
        <input class="bottone" type="submit" name="RICERCA2" value="Ricerca">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_INIZIALE%>","numeric");

  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric");

  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR_FINALE%>","numeric");

  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO_FINALE%>","numeric");

  frmvalidator.setAddnlValidationFunction("VerifyF");
</script>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("Singolo");

  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_PROGR%>","numeric");

  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiEsecuzioneMS.CAMPO_CHIAVE_ANNO%>","numeric");

  frmvalidator.setAddnlValidationFunction("VerifySingolo");

</script>
</body>
</html>