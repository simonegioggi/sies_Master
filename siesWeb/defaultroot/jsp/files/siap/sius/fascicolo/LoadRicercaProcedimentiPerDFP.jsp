<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimenti Per Data Fine Pena - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
    function Verify()
    {
      var data_to_verify=document.LoadRicercaProcedimentiPerDFP.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+document.LoadRicercaProcedimentiPerDFP.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+document.LoadRicercaProcedimentiPerDFP.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data di partenza di Fine Pena non valida');
        return false;
      }
      var data_to_verify=document.LoadRicercaProcedimentiPerDFP.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value+'/'+document.LoadRicercaProcedimentiPerDFP.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value+'/'+document.LoadRicercaProcedimentiPerDFP.<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data ultima di Fine Pena non valida');
        return false;
      }
      return true;
    }
  </script>

</head>

<body class="corpo">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaProcedimentiPerDFP">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaProcedimentiPerDFP">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Procedimenti per Formazione Ruolo di Udienza</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Visualizza i procedimenti Iscritti dal &nbsp;</td>
        <td class="label">
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="label">Visualizza i procedimenti con Data Fine Pena dal &nbsp;</td>
        <td class="label">
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: Di norma vengono visualizzati i procedimenti pendenti e privi di udienza di competenza dell'ufficio con i criteri di ricerca selezionati. Per variare i criteri selezionare una o più delle seguenti opzioni:
        </td>
      </tr>
    </table>

      <tr><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td></tr>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="Cliccabile">
          Modifica tipologia dei procedimenti visualizzati
        </td>
      </tr>

      <%--tr>
        <td class="label" >Visualizza solo i procedimenti Pendenti</td>
        <td class="label" >
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_ARCHIVIATI%>" value=0 CHECKED ></td>
        </td>
      </tr>

      <tr>
        <td class="label" >Visualizza anche i procedimenti Definiti</td>
        <td class="label" >
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_ARCHIVIATI%>" value=1 ></td>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
    </table--%>
    <input type=hidden name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_ARCHIVIATI%>" value=0 CHECKED ></td>


    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Visualizza solo i procedimenti con Pos. Giuridica &nbsp;&nbsp;
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" >
            <%= posizioneGiuridica %>
          </select>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="label">Visualizza solo i procedimenti relativi a &nbsp;&nbsp;
          <select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" >
            <%= contenuto %>
          </select>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td>
        <br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaProcedimentiPerDFP");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

  </script>
</body>

</html>