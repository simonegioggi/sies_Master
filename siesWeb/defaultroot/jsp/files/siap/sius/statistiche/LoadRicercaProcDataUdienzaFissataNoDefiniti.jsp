<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>

<jsp:useBean id="oggettiProcedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioniGiuridiche" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimenti Per Data Udienza Fissata nessuno definito - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  
  	function init() {
  		document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_INIZIO%>.focus();
  	}
  	
    function Verify() {																																																	
      var data_to_verify=document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_INIZIO%>.value+'/'+document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_MESE_DATA_CAMERA_CONSIGLIO_INIZIO%>.value+'/'+document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data di partenza di Fissazione Udienza non valida');
        return false;
      }
      var data_to_verify=document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_FINE%>.value+'/'+document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_MESE_DATA_CAMERA_CONSIGLIO_FINE%>.value+'/'+document.LoadRicercaProcDataUdienzaFissataNoDefiniti.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_FINE%>.value;
      if (! ControllaData(data_to_verify) && data_to_verify.length>2)
      {
        alert('Data ultima di Fissazione Udienza non valida');
        return false;
      }
      
      return true;
    }
  </script>

</head>

<body class="corpo" onLoad="javascript:init()">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaProcDataUdienzaFissataNoDefiniti">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaProcDataUdienzaFissataNonDefiniti">
    <table>
      <tr>
      	<td class="LBG">
      		<a href="Javascript:window.print();">
      			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
      		</a>
      	</td>
        <td class="LBG">
        	<font class="label">Funzione :</font> 
        	<font class="campo">Ricerca Procedimenti per Data Udienza Fissata Non Definiti</font>
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Visualizza i procedimenti Fissati dal &nbsp;</td>
        <td class="label">
          <input Title="dalla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_INIZIO %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_CAMERA_CONSIGLIO_INIZIO %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO %>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_FINE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_CAMERA_CONSIGLIO_FINE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="alla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_FINE %>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="label">Visualizza i procedimenti con Data Iscrizione dal &nbsp;</td>
        <td class="label">
          <input Title="dalla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_INIZIO %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_INIZIO %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="dalla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_INIZIO %>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp; al &nbsp;&nbsp;
          <input Title="alla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_ISCRIZIONE_FINE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_ISCRIZIONE_FINE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">/
          <input Title="alla Data" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_ISCRIZIONE_FINE %>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: Di norma vengono visualizzati i procedimenti pendenti e con udienza fissata di competenza dell'ufficio con i criteri di ricerca selezionati. Per variare i criteri selezionare una o più delle seguenti opzioni:
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

    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Visualizza solo i procedimenti con Pos. Giuridica &nbsp;&nbsp;
          <select title="posizioniGiuridiche" class=small name="<%=ICostantiStatistiche.CAMPO_COD_POSIZIONE_GIURIDICA%>" >
            <%= posizioniGiuridiche %>
          </select>
        </td>
      </tr>

      <tr><td>&nbsp;</td></tr>

      <tr>
        <td class="label">Visualizza solo i procedimenti relativi a &nbsp;&nbsp;
          <select title="oggettiProcedimento" class=small name="<%=ICostantiStatistiche.CAMPO_COD_OGGETTO_PROCEDIMENTO%>" >
            <%= oggettiProcedimento %>
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

    var frmvalidator  = new Validator("LoadRicercaProcDataUdienzaFissataNoDefiniti");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_CAMERA_CONSIGLIO_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_INIZIO%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_CAMERA_CONSIGLIO_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_DATA_CAMERA_CONSIGLIO_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_FINE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_DATA_CAMERA_CONSIGLIO_FINE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

  </script>
</body>

</html>