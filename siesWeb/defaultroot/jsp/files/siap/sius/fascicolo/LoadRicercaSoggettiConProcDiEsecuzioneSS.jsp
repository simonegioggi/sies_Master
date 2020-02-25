<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodUDSTDS" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Soggetti con Procedimenti di Esecuzione Sanzioni Sostitutive - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
    function Verify()
    {
      var data_to_verify=document.LoadRicercaSoggettiConProcDiEsecuzioneSS.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettiConProcDiEsecuzioneSS.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettiConProcDiEsecuzioneSS.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data di nascita non valida');
        return false;
      }
      return true;
    }
  </script>
  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
  </script>

</head>

<body class="corpo">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaSoggettiConProcDiEsecuzioneSS">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.fascicolo.action.ActRicercaSoggettiConProcDiEsecuzioneSS">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Soggetti con Procedimenti di Esecuzione Sanzioni Sostitutive</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Cognome</td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l">Nome</td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l">Comune di nascita</td>
        <td class="l">
          <input Title="Comune di nascita" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" value="" type="text" maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadRicercaSoggettiConProcDiEsecuzioneSS','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
          <img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
        </td>
      </tr>


      <tr>
        <td class="l">Stato di Nascita</td>
        <td class="L">
          <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
            <%= nazioni %>
         </select>
         </td>
      </tr>

      <tr>
        <td class="l">Data di nascita </td>
        <td class="l">
          <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

      <tr>
        <td class="l">Paternità </td>
          <td class="l"><input title="Paternita" type="text" name="<%= ICostantiSoggetto.CAMPO_PATERNITA%>" size="30" maxlength="30"></td>
      </tr>

      <tr>
        <td class="l">Codice CUI</td>
        <td class="L">
          <input title="Codice CUI" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_CS%>" maxlength="6" size="6">
        </td>
      </tr>

    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: Di norma vengono visualizzati i soggetti con procedimenti pendenti di competenza dell'ufficio senza limitazione del periodo di pervenimento in cancelleria. Per variare i criteri selezionare una o più delle seguenti opzioni:
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="Cliccabile">
          Modifica competenza territoriale dei procedimenti visualizzati
        </td>
      </tr>

      <tr>
        <td class="label" >Visualizza solo i procedimenti dell'Ufficio</td>
        <td class="label" >
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value=0 CHECKED ></td>
        </td>
      </tr>

      <tr>
        <td class="label">
            Visualizza i procedimenti dell'intero Distretto &nbsp;&nbsp;&nbsp;
        </td>
        <td class="label">
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value=2></td>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="Cliccabile">Modifica Tipologia dei procedimenti visualizzati&nbsp;&nbsp;</td>
      </tr>

      <tr>
        <td class="label">Visualizza anche i procedimenti definiti &nbsp;
        </td>
        <td class="label">
          <input type=checkbox name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_ARCHIVIATI%>" value=1></td>
        </td>
      </tr>

    </table>

    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="label">Visualizza solo i procedimenti relativi a &nbsp;&nbsp;
          <select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" >
            <%= contenuto %>
          </select>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="Cliccabile">Seleziona il Periodo di arrivo in cancellaria dei procedimenti visualizzati&nbsp;&nbsp;</td>
      </tr>

      <!--br><br-->

      <tr>
        <td class="label">Visualizza i procedimenti pervenuti in cancelleria dal&nbsp;
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

      <tr>
        <td>
        <br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>

    </table>

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaSoggettiConProcDiEsecuzioneSS");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");


    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=2010");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

  </script>
</body>

</html>