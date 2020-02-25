<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.statis.action.ICostantiStatis"%>
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria" %>

<jsp:useBean id="oggettoProcedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>


<head>
  <title> [S.I.E.S.] - Comparazione Attività Magistrati per Oggetto - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
  	function init() {
  		document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.focus();
  	}
	</script>


  <script language="JavaScript">
    var desktop;
    var flagBloccaUfficio = false;
        
    function Verify()
    {
        var gg_in = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value);
        var mm_in = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value);
        var aa_in = document.f.<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>.value;

        var gg_fi = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_FINALE%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>.value);
        var aa_fi = document.f.<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>.value;
    	
				var dataIni = gg_in + "/" + mm_in + "/" + aa_in;
				var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;
				
				if (dataIni.length != 2 && dataIni.length != 10){
					alert ("Data Iniziale errata.");
					return false;
				}
				
				
				if (dataFine.length != 2 && dataFine.length != 10){
					alert ("Data Finale errata.");
					return false;
				}else if ( (dataIni.length == 10) && (ControllaData (dataIni) == false) ){
					// entrambe le date valorizzate
					alert ("Errore nella data Iniziale.");
					return false;
				}else if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) ){
					alert ("Errore nella data Finale.");
					return false;
				}else if ((dataFine.length == 10)               &&
				         ( dataIni.length == 10)               &&
				         CompareDate(dataIni,dataFine)== false ){
					alert ("Data di Finale minore della Data Iniziale.");
					return false;
				}
				
				var magistrati = false;
				for (var i = 0; i < document.f.<%=ICostantiStatistiche.CAMPO_COD_MAGISTRATO%>.options.length; i++) {
					if(document.f.<%=ICostantiStatistiche.CAMPO_COD_MAGISTRATO%>.options[i].selected) {
						magistrati = true;
						break;
					}
				}
				if(!magistrati) {
					alert ("Selezionare almeno un Magistrato.");
					return false;
				}
				
				var oggetti = false;
				for (var j = 0; j < document.f.<%=ICostantiStatistiche.CAMPO_COD_OGGETTO%>.options.length; j++) {
					if(document.f.<%=ICostantiStatistiche.CAMPO_COD_OGGETTO%>.options[j].selected) {
						oggetti = true;
						break;
					}
				}
				if(!oggetti) {
					alert ("Selezionare almeno un Oggetto Procedimento.");
					return false;
				}
				
				waiting();
     }
      
  </script>
  
  <script language="JavaScript">
    function waiting() {
      var node=document.getElementById('waiting');
      node.style.visibility='visible';
    }
  </script>   
  
</head>

<body class="corpo" onLoad="javascript:init();"  >

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaStatisticaComparataMagistrati">

  <table>
    <tr>
    	<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    			</a>
    	</td>
      <td class="LBG">
      	<font class="label">Funzione :</font>&nbsp;<font class="campo">Comparazione Attività Magistrati per Oggetto</font>
      </td>
      <td>      
      	<div align=center id="waiting" style="visibility:hidden;position:relative;">
      		<img src="/images/rotelle3.gif" height="25" width="25" border=0><font class="cRosso">Attendere...</font>
      	</div>
      </td>
    </tr>
  </table>

  <br>

	<table width="90%">
		<tr>
			<td class="Titolo"  colspan ="4" >Intervallo Date da verificare</td>
		</tr>
		<tr>
			<td class="L" width="20%" >
				<font class="label"> Data Iniziale </font>
			</td>
			<td class="l" >
				<input type="text" title="Giorno Iniziale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
				<input type="text" title="Mese Iniziale" name="<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
				<input type="text" title="Anno Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
			</td>
			<td class="L" width="20%" >
				<font class="label">Data Finale </font>
			</td>
			<td class="l">
				<input type="text" title="Giorno Finale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
				<input type="text" title="Mese Finale" name="<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
				<input type="text" title="Anno Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
			</td>
		</tr>
	</table>

	<table width="90%" cellspacing=2 cellpadding=2>
		<tr>
			<td class="Titolo"  colspan ="2" >Selezionare Magistrati da Comparare</td>
		</tr>
		<tr>
			<td class="l" width="1%">Magistrato </td>
			<td class="L" width="*">
				<select title="magistrato" class=small name="<%=ICostantiStatistiche.CAMPO_COD_MAGISTRATO%>" multiple size="10">
					<%= magistrato %>
				</select>
			</td>
		</tr>
	</table>
	
	<table width="90%" cellspacing=2 cellpadding=2>
		<tr>
			<td class="Titolo" >Selezionare Oggetti da Comparare</td>
		</tr>
		<tr>
			<td class="L">
				<select title="oggettoProcedimento" class=small name="<%=ICostantiStatistiche.CAMPO_COD_OGGETTO%>"  multiple size="15">
					<%= oggettoProcedimento %>
				</select>
			</td>
		</tr>
	</table>

  <table cellspacing=2 cellpadding=2>
     <tr>
       <td class="lVerdeNB">  
         N.B.: Per selezionare più magistrati o oggetti,tenendo premuto il tasto CTRL,selezionare con il mouse gli elementi da elaborare (anche tutti).
       </td>
     </tr>
  </table>
  <p/>
  <table style="width: 90%; visibility:visible;">
  <tr><td class="l">
      &nbsp;<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return Verify();">
     </td></tr>
  </table>
</form>

	<script language="JavaScript" type="text/javascript">
 		var frmvalidator  = new Validator("f");
  </script>
</body>

</html>