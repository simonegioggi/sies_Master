<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- <%@ page import="com.sun.org.apache.bcel.internal.generic.ICONST"%> --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>

<jsp:useBean id="title" scope="request" class="java.lang.String"/>
<jsp:useBean id="action" scope="request" class="java.lang.String"/>

<script language="JavaScript">
 
</script>

<html>
<head>
  <title>[S.I.E.S.] - Statistiche Ricerca Procedimenti</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" >
  	function init() {
  		document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.focus();
  	}
  	
    function Verify() {																																																	
      var data1 = document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>.value+'/' +
      					   document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>.value+'/' +
      					   document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>.value;
      if (! ControllaData(data1)){
        alert('Data Iniziale non valida');
        return false;
      }
      
      var data2 = document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_GIORNO_FINALE%>.value+'/' + 
      					   document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>.value+'/' + 
      					   document.LoadRicercaProcAggregati.<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>.value;
      if (! ControllaData(data2)){
        alert('Data Finale non valida');
        return false;
      }
      
      if(DifferenzaDateInGiorni(data1, data2) > (365 * 2 + 1 /* +1 nell'eventualità in cui nell'intevallo cade un anno bisestile */ )) {
          alert('L\'intervallo delle Date non deve eccedere i 2 anni');
          return false;
      }
      return true;
    }
</script>
</head>
  <body class="corpo" onLoad="javascript:init();">
  	<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadRicercaProcAggregati">
  	<table>
    	<tr>
    		<td class="LBG">
    			<a href="Javascript:window.print();">
    				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
    			</a>
    		</td>
      	<td class="LBG">
      		<font class="label">Funzione :</font>&nbsp;
      		<font class="campo"><%=title%></font>
      	</td>
    	</tr>
  	</table>
  	
  	<br>
  	
   	<table style="width: 95%;">
      <tr>
      	<td class="Titolo" >Intervallo Di Tempo Da Verificare</td></tr>
          <td class="c" width="61%" >
          Data Iniziale&nbsp;&nbsp;
          <input Title="Data Iniziale" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_INIZIALE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="Data Iniziale" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_INIZIALE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="Data Iniziale" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_INIZIALE %>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          &nbsp;&nbsp;Data Finale&nbsp;&nbsp;
          <input Title="Data Finale" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_FINALE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="Data Finale" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_FINALE %>" maxlength="2" size="2" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
          <input Title="Data Finale" 
          			 type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_FINALE %>" maxlength="4" size="4" 
          			 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
		  	</td>
      </tr>
    </table>
		<br>
	  <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="lVerdeNB" >
          N.B.: E' possibile impostare un intervallo temporale massimo di 2 anni
        </td>
      </tr>
      <tr>
        <td>
        <br><br><br><br>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
    
    
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=action%>">
    
    
  </form>
  
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadRicercaProcAggregati");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_GIORNO_FINALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'anno è di 4 caratteri");

  </script>
  </body>
</html>