<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.permesso.action.ICostantiPermesso" %>

<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>
<jsp:useBean id="licenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="CodUDSTDS" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Soggetti con Licenza - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  function Verify()
  {
    var data_to_verify=document.LoadRicercaSoggettiConLicenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettiConLicenza.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggettiConLicenza.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
    if (! ControllaData(data_to_verify) && data_to_verify.length>2)
    {
      alert('Data di nascita non valida');
      document.LoadRicercaSoggettiConLicenza.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.focus();
      return false;
    }
    if (document.LoadRicercaSoggettiConLicenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value.length<2)
    {
      alert("Valorizzare almeno i primi 2 caratteri del Cognome");
      document.LoadRicercaSoggettiConLicenza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.focus();
      return false;
    }
		var data_to_verify=document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO%>.value+'/'+document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO%>.value+'/'+document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO%>.value ;
		if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		{
			alert('Data non valida');
      document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO%>.focus();
			return false;
		}
		var data_to_verify=document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.value+'/'+document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO%>.value+'/'+document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO%>.value ;
		if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		{
			alert('Data non valida');
      document.LoadRicercaSoggettiConLicenza.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO%>.focus();
			return false;
		}
    return true;












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
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaSoggettiConLicenza">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.permesso.action.ActRicercaSoggettiConLicenza">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Soggetti con Procedimenti di Sorveglianza relativi a licenze</font></td>
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
          <a href="Javascript:ListaComuni('LoadRicercaSoggettiConLicenza','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
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
          N.B.: Di norma vengono visualizzati i soggetti con provvedimenti di licenza di competenza dell'ufficio senza limitazione del periodo di pervenimento in cancelleria. Per variare i criteri selezionare una o più delle seguenti opzioni:
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="Cliccabile">
          Modifica competenza territoriale dei procedimenti di licenza visualizzati
        </td>
      </tr>

      <tr>
        <td class="label" >
            Visualizza solo le licenze dell'Ufficio
        </td>
        <td class="label" >
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value=0 CHECKED ></td>
        </td>
      </tr>

      <tr>
        <td class="label">
            Visualizza le licenze dell'intero Distretto &nbsp;&nbsp;&nbsp;
        </td>
        <td class="label">
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value=2></td>
        </td>
      </tr>

      <tr>
        <td class="label">
            Visualizza anche le licenze di altri Distretti presenti in BDI&nbsp;&nbsp;&nbsp;
        </td>
        <td class="label">
          <input type=radio name="<%=ICostantiFascicoloSius.CAMPO_INCLUDE_UFFICIO%>" value=3></td>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="Cliccabile">Modifica Tipologia dei provvedimenti di licenza visualizzati&nbsp;&nbsp;</td>
      </tr>

      <tr>
        <td class="label">Visualizza anche le licenze rigettate &nbsp;
        </td>
        <td class="label">
          <input type=checkbox name="<%=ICostantiPermesso.CAMPO_INCLUDE_RIGETTATI%>" value=1></td>
        </td>
      </tr>

    </table>

    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="label">Visualizza solo i provvedimenti relativi a &nbsp;&nbsp;
          <select title="contenuto" class=small name="<%=ICostantiPermesso.CAMPO_COD_LICENZA%>">
            <%= licenza %>
          </select>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="Cliccabile">Seleziona il Periodo di decisione delle licenze visualizzate&nbsp;&nbsp;</td>
      </tr>

      <tr>
        <td class="label">Visualizza i provvedimenti emessi dal&nbsp;
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_INSERIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_INSERIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_INSERIMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_AGGIORNAMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_AGGIORNAMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_AGGIORNAMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
        </td>
      </tr>

      <tr>
        <td>
        <br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>

    </table>
    <input type="HIDDEN" name="<%=ICostantiPermesso.CAMPO_COD_LICENZA%>" value="LC" >

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaSoggettiConLicenza");

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