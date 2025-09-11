<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche" %>

<html>
  <head>
    <title> [S.I.E.S.] - Ricerca Atti istruttori con data restituzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
    <script language="JavaScript" src="/html/ControllaData.js"></script>
    <script language="JavaScript">
      function Verify() {
        
        
        var anno_ini = document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_ANNO_INI%>.value;
        var num_ini  = document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_NUM_INI%>.value;
        var anno_fin = document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_ANNO_FINE%>.value;
        var num_fin  = document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_NUM_FINE %>.value;  
        
        if (anno_ini.length>0 && anno_ini.length<4){
          alert("Valore dell'Anno Procedimento iniziale non valido!");
          return false;          
        }        
        if (num_ini.length>0 && num_ini==0) {
          alert("Numero procedimento iniziale non valido!");
          return false;             
        }
        if (num_ini.length>0 && anno_ini.length==0) {
          alert("Se si specifica il Numero procedimento iniziale va indicato anche l'anno!");
          return false;             
        }
        
        if (anno_fin.length>0 && anno_fin.length<4){
          alert("Valore dell'Anno Procedimento finale non valido!");
          return false;          
        }
        if (num_fin.length>0 && num_fin==0) {
          alert("Numero procedimento finale non valido!");
          return false;             
        }
        if (num_fin.length>0 && anno_fin.length==0) {
          alert("Se si specifica il Numero procedimento finale va indicato anche l'anno!");
          return false;             
        }       
        
        if (anno_ini.length>0 && anno_fin.length>0 && anno_ini>anno_fin)
        {
           alert("L'anno Procedimento iniziale non può essere superiore a quello finale!");
           return false;
        }
        if (anno_ini.length>0 && anno_fin.length>0 && anno_ini==anno_fin)
        {
           //check num_fin e num_ini
           //alert("L'anno Procedimento iniziale non può essere superiore a quello finale!");
           //return false;
        }
        
        
        var data_dep_ini=
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI%>.value+'/'+ 
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI%>.value+'/'+
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI%>.value;

        if (! ControllaData(data_dep_ini) && data_dep_ini.length>2){
          alert('Data di Iscrizione iniziale non valida!');
          return false;
        }
        
        var data_dep_fin=
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE%>.value+'/'+ 
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE%>.value+'/'+
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE%>.value;
      
        if (! ControllaData(data_dep_fin) && data_dep_fin.length>2){
          alert('Data di Iscrizione finale non valida!');
          return false;
        }
        
        if (data_dep_ini.length>2 && data_dep_fin.length>2){ 
          if (!CompareDate (data_dep_ini, data_dep_fin)) {
            alert("La Data di Iscrizione finale non pua' essere inferiore alla data iniziale!");
            return false;
          }
        }
        
        
        var data_rest_ini=
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_RESTITUZIONE_INI%>.value+'/'+ 
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_MESE_DATA_RESTITUZIONE_INI%>.value+'/'+
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_RESTITUZIONE_INI%>.value;

        if (! ControllaData(data_rest_ini) && data_rest_ini.length>2){
          alert('Data di Restituzione iniziale non valida!');
          return false;
        }
        
        var data_rest_fin=
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_GIORNO_DATA_RESTITUZIONE_FINE%>.value+'/'+ 
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_MESE_DATA_RESTITUZIONE_FINE%>.value+'/'+
          document.ActLoadRicercaAttiIstruttoriDataRest.<%=ICostantiStatistiche.CAMPO_ANNO_DATA_RESTITUZIONE_FINE%>.value;
      
        if (! ControllaData(data_rest_fin) && data_rest_fin.length>2){
          alert('Data di Restituzione finale non valida!');
          return false;
        }
        
        if (data_rest_ini.length>2 && data_rest_fin.length>2){ 
          if (!CompareDate (data_rest_ini, data_rest_fin)) {
            alert("La Data di Restituzione Atti finale non puo' essere inferiore alla data iniziale!");
            return false;
          }
        }
       
        if (   annoini.length==0 && anno_fin.length==0
            && data_dep_ini.length==2 && data_dep_fin.length==2
            && data_rest_ini.length==2 && data_rest_fin.length==2
           ) 
        {
          alert('Indicare almeno un criterio di ricerca!');
          return false;
        }
        
      return true;
    }
    </script>
  
  </head>

  <body class="corpo">
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ActLoadRicercaAttiIstruttoriDataRest">
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaAttiIstruttoriDataRest">
      <table>
        <tr>
          <td class="LBG">
            <a href="Javascript:window.print();">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
            </a>
          </td>
          <td class="LBG">
            <font class="label">Funzione :</font> 
            <font class="campo">Ricerca Atti istruttori con data restituzione</font>
          </td>
        </tr>
      </table>

      <br>

    <table width="100%">
      <tr>
        <td class="Titolo" >Intervallo Estremi Procedimenti</td>
      </tr>
      <tr>
          <td class="c" width="61%" >     
              Anno/Numero Iniziale 
              <input Title="Anno Iniziale" 
                     type="text" name="<%=ICostantiStatistiche.CAMPO_ANNO_INI%>" maxlength="4" size="4" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">/
              <input Title="Numero Iniziale" 
                     type="text" name="<%=ICostantiStatistiche.CAMPO_NUM_INI%>" maxlength="6" size="6" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
              &nbsp;&nbsp; Anno/Numero Finale &nbsp;&nbsp;
              <input Title="Anno Finale" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_FINE %>" maxlength="4" size="4" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">/
              <input Title="Numero Finale" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_NUM_FINE %>" maxlength="6" size="6" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          </td>
      </tr>
    </table>
    
    <br>
    
    <table width="100%">
      <tr>
        <td class="Titolo" >Intervallo Date Iscrizione</td>
      </tr>
      <tr>
          <td class="c" width="61%" >
              Data Iscrizione Iniziale      
              <input Title="dalla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_INI %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="dalla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_INI %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="dalla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_INI %>" maxlength="4" size="4" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
              &nbsp;&nbsp; Data Iscrizione Finale  &nbsp;&nbsp;
              <input Title="alla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="alla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_DEPOSITO_FINE %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="alla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_DEPOSITO_FINE %>" maxlength="4" size="4" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>
    </table>

    <br>

    <table width="100%">
      <tr>
        <td class="Titolo" >Intervallo Date Restituzione</td>
      </tr>
      <tr>
          <td class="c" width="61%" >
              Data Restituzione Iniziale      
              <input Title="dalla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_RESTITUZIONE_INI %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="dalla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_RESTITUZIONE_INI %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="dalla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_RESTITUZIONE_INI %>" maxlength="4" size="4" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
              &nbsp;&nbsp; Data Restituzione Finale  &nbsp;&nbsp;
              <input Title="alla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_GIORNO_DATA_RESTITUZIONE_FINE %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="alla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_MESE_DATA_RESTITUZIONE_FINE %>" maxlength="2" size="2" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >/
              <input Title="alla Data" 
                     type="text" name="<%= ICostantiStatistiche.CAMPO_ANNO_DATA_RESTITUZIONE_FINE %>" maxlength="4" size="4" 
                     onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>
    </table>


    <table cellspacing=2 cellpadding=2>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td>
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>

  </form>
  
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("ActLoadRicercaAttiIstruttoriDataRest");

  </script>
</body>

</html>