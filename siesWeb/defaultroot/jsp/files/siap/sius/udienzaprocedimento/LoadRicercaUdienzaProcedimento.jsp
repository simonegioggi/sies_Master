<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento" %>
<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>
<jsp:useBean id="tiporicerca"              scope="request" class="java.lang.String"/>
<html>

<head>
  <title> [S.I.E.S.] - Ricerca Udienza Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>
  <script language="JavaScript">
  function Verify()
	{
          var ritorno = true;
          var dataUdienzaInizio = document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>.value +'/'+
                      document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>.value + '/' +
                      document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>.value;

          if(dataUdienzaInizio.length <= 2 )
          {
            alert('Occorre inserire Data Udienza');
            ritorno = false;
          }
          else if (!ControllaData( dataUdienzaInizio ) )
          {
            alert('Data Udienza non valida');
            ritorno = false;
          }
          return ritorno;
	 }

      function ListaUdienze(aNomeForm,aCampoID,aNomeCampoGG,aNomeCampoMM,aNomeCampoAA,aNomeCampoLuogo,aNomeCampoColl)
      {
        var desktop;

        var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActLoadRicercaUdienzaXProcedimenti";
        lLink += "&formname="+ aNomeForm;
        lLink += "&campoID="+ aCampoID;
        lLink += "&campoGG=" + aNomeCampoGG;
        lLink += "&campoMM=" + aNomeCampoMM;
        lLink += "&campoAA=" + aNomeCampoAA;
        lLink += "&campoLuogo=" + aNomeCampoLuogo;
        lLink += "&campoColl=" + aNomeCampoColl;
        desktop = window.open(lLink, "Lista_Udienze","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550");
      }
  </script>
</head>

<body class="corpo" onLoad="document.forms['LoadRicercaUdienzaProcedimento'].elements['<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>'].focus()">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaUdienzaProcedimento">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.udienzaprocedimento.action.ActRicercaUdienzaProcedimento">
  <input type="HIDDEN" name="<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>" value="">
  <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>" value="">
  <input type="HIDDEN" name="<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>" value="">
  <input type="HIDDEN" name="tiporicerca" value="<%=tiporicerca%>">
  <table width=100%>
    <tr ><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG ><font class="label">Funzione :</font>&nbsp;<font class="campo">Visualizza Procedimenti fissati per Udienza</font></td>
    </tr>
  </table>

  <br>

  <table cellpadding=2 cellspacing=2 align=center width=100%>
    <tr>
      <td class="L" colspan=2 >
        <font class="label">Data udienza</font>
        <input  onchange="document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>.value=0"  value="" type="text" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input  onchange="document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>.value=0" value="" type="text" name="<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>"   maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input  onchange="document.LoadRicercaUdienzaProcedimento.<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>.value=0" value="" type="text" name="<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>"   maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        <a  href="Javascript:ListaUdienze('LoadRicercaUdienzaProcedimento',
                                       '<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>');">
                                          Seleziona l'Udienza dalla lista
        <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
		</tr>
		<tr><td class="label" colspan=2><br></td></tr>
		<tr>
      <td class="titolo" colspan=2>
        Selezionare Tipo Ordinamento della Visualizzazione:
      </td>
		</tr>
		<tr>
			<td class="l" colspan=2><input type="radio" name="tipo" value="PP" > Per Progressivo Procedimento</td>
		</tr>
		<tr>
			<td class="l" colspan=2><input type="radio" name="tipo" value="CNS" > Per Cognome/Nome Soggetto</td>
		</tr>
		<tr>
			<td class="l" colspan=2><input type="radio" name="tipo" value="PGCNS" > Per Posizione Giuridica e Cognome/Nome Soggetto</td>
		</tr>
		<tr>
			<td class="l" colspan=2><input type="radio" name="tipo" value="PGPP" CHECKED > Per Posizione Giuridica e Progressivo Procedimento</td>
		</tr>
		<tr><td class="label" colspan=2><br></td></tr>
		<tr>
			<td class="titolo" colspan=2>
				<font class="titolo">Selezionare la Tipologia dei Procedimenti da Visualizzare:</font>
			</td>
		</tr>
		<tr>
			<td class="l" colspan=2>Visualizza anche procedimenti unificati
				<input name="CheckUnificazione" type=checkbox value="1">
      </td>
    </tr>
		<tr>
			<td class="L" colspan=2>
				<font class="label">Visualizza solo procedimenti fissati</font><input type="radio" name="tipoProc" value="FISSATI" CHECKED> &nbsp;&nbsp;&nbsp;
				<font class="label">Visualizza solo procedimenti prefissati</font><input type="radio" name="tipoProc" value="PREFISSATI" > &nbsp;&nbsp;&nbsp;
				<font class="label">Visualizza procedimenti fissati e prefissati</font><input type="radio" name="tipoProc" value="TUTTI" >
			</td>
		</tr>
  </table>

  <table cellpadding=2 cellspacing=2 align=center width=100%>
		<tr><td class="label" colspan=2><br></td></tr>
		<tr>
      <td class="lVerdeNB" colspan=2>
        <font class="lVerde">N.B.: &nbsp;&nbsp; Di norma il sistema visualizza solo procedimenti fissati e non unificati; modificare impostazioni per diverso tipo di visualizzazione.</font>
      </td>
		</tr>

		<tr><td class="label" colspan=2><br></td></tr>
   <tr>
      <td class="label" colspan="2" >
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="Conferma" value="Conferma">
      </td>
   </tr>
   <tr><td class="L" colspan=2></td></tr>
  </table>

</form>
</body>
</html>