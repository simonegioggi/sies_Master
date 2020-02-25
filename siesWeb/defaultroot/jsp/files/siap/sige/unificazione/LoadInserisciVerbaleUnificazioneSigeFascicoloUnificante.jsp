<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sige.decretounificazione.action.ICostantiDecretoUnificazioneSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>


<html>
<head>
  <title>[S.I.E.S.] - Inserimento Verbale Unificazione Sige</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
  function Verify()
  {
    // Controllo di uguaglianza tra i 2 procedimenti.
    var anno_da_unif=document.LoadVerificaVerbaleUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>.value;
    var anno_unificante='<%=FascicoloSigeEsteso.getFascicoloSige().getChiaveAnno().toString()%>';
    var numero_da_unif=document.LoadVerificaVerbaleUnificazioneSige.<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>.value;
    var numero_unificante='<%=FascicoloSigeEsteso.getFascicoloSige().getChiaveProgr().toString()%>';

    // Controllo uguaglianza procedimenti "Da Unificare" e "Unificante".
    if ( anno_da_unif == anno_unificante && numero_da_unif == numero_unificante)
    {
      alert('Impossibile Unificare un procedimento con se stesso!');
      return false;
    }
    
    return true;
  }
  </script>

</head>

<body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Verbale Unificazione Sige</font>
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadVerificaVerbaleUnificazioneSige'>
    <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.unificazione.action.ActLoadConfermaInserisciVerbaleUnificazioneSige">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Anno e Numero del Procedimento Sige da Unificare <font class=ob>(*)</font></td>

        <td class="l">
          <input Title="Anno SIGE" type="text" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIGE" type="text" name="<%= ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>
      
    </table>
    <BR>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Avanti >>>">
        </td>
      </tr>

    </table>
  </form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadVerificaVerbaleUnificazioneSige");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","req","Il campo Anno da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","maxlen=4","La lunghezza massima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","minlen=4","La lunghezza minima per l'Anno è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_ANNO_DA_UNIF%>","numeric");

    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>","req","Il campo Numero da Unificare è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>","maxlen=6","La lunghezza massima per il Numero è di 6 caratteri");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>","numeric");
    frmvalidator.addValidation("<%=ICostantiDecretoUnificazioneSige.CAMPO_NUMERO_DA_UNIF%>","numeric");

  </script>
  </body>
</html>