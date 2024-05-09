<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>



<% 
FascicoloSiepModel fascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>

<html>
<head>
  <title> [S.I.E.S.] - Consultazione Scadenzario Stato Pagamenti Pena Pecuniaria - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function setta()
  {
    <% if (fascicolo != null && fascicolo.getIdFascicoloSiep() != null) { %>
      document.getElementById("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>").value="<%=fascicolo.getChiaveAnno()%>";
      document.getElementById("<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR %>").value="<%=fascicolo.getChiaveProgr()%>";
    <% }%>
  }  
  
  function verifica()
  {
    if(document.LoadRicercaScadenzarioPP.tipo[1].checked)
    {
      if(   document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>.value == "" 
         && document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>.value == "" 
         && document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA %>.value == ""
        )
      {
        alert("Inserire Periodo ");
        document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>.focus();
        return false;
      }
    }
    
    if(!(   document.LoadRicercaScadenzarioPP.tipo[0].checked 
         || document.LoadRicercaScadenzarioPP.tipo[1].checked 
         || document.LoadRicercaScadenzarioPP.tipo[2].checked
         || document.LoadRicercaScadenzarioPP.tipo[3].checked
        ) 
       )
    {
      alert("E' obbligatorio selezionare almeno un elemento di ricerca");
      return false;
    }
    return true;
   }
  
   function inScadenza() {
	   //alert("inScadenza");
	   if (document.LoadRicercaScadenzarioPP.tipo[1].checked) {
			document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>.disabled = false;
			document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>.disabled = false; 
			document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA %>.disabled = false;   
	   }
	   else {
			document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>.disabled = true;
			document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>.disabled = true; 
			document.LoadRicercaScadenzarioPP.<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA %>.disabled = true;  
	   }
   }
  
   </script>
 </head>
 
<%
ScadenzarioModel lScaModel = new ScadenzarioModel();
%>

<body class="corpo" onload="Javascript:inScadenza()">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaScadenzarioPP">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.scadenzario.action.ActRicercaScadenzarioStatoPagamentiPP">

    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Consultazione Scadenzario Stato Pagamenti Pena Pecuniaria</font></td>
      </tr>
    </table>

    <br>

    <table cellspacing=2 cellpadding=2 width="60%">
      <tr>
        <td colspan="3" class="Titolo" width1="100%">Consultazione Scadenzario Stato Pagamenti Pena Pecuniaria</td>
      </tr>
      <tr>
        <td class="l" width1="15%">Solo il procedimento (Anno/Numero)</td>
        <td class="l">
          <input type="text" title="Anno" value="" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4"
                 onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          /
          <input type="text" title="Numero SIEP" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" maxlength="14" size="14" >
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" value="">
        </td>
        <td class="menulines" align="left">
          <a style="" name="aa" href="Javascript:setta()" title="Seleziona Procedimento Corrente">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>ActiveSession.gif" width="32" height="32" border="0">
          </a>
        </td>        
      </tr>  
      <tr><td><br></td></tr>    
      <tr>
        <td class="l" width1="15%">Tutti</td>
        <td class="l" width1="15%"><input type="radio" name="tipo" value="Tutti" checked onclick="javascript:inScadenza()"></td>
        <td class="l" width1="70%" colspan=2></td>
      </tr>
      <tr>
        <td class="l">In scadenza</td>
        <td class="l"><input type="radio" name="tipo" value="sette" onclick="javascript:inScadenza()" ></td>
        <td class="L" width1="10%">entro:
          Anni
          <input type="text" title="Anni" size=2 maxlength=2 disabled
                 value="<%=StringUtils.toStringJSP(lScaModel.getNumAnni()) %>" 
                 name="<%= ICostantiScadenzario.CAMPO_ANNI_SCADENZA %>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          Mesi
          <input type="text" title="Mesi" size=2 maxlength=2 disabled
                 value="<%=StringUtils.toStringJSP(lScaModel.getNumMesi()) %>" 
                 name="<%= ICostantiScadenzario.CAMPO_MESI_SCADENZA%>"  
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          Giorni
          <input type="text" title="Giorni" size=2 maxlength=2 disabled
                 value="<%=StringUtils.toStringJSP(lScaModel.getNumGiorni()) %>" 
                 name="<%= ICostantiScadenzario.CAMPO_GIORNI_SCADENZA %>"                 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>
      <tr>
        <td class="l">In scadenza Oggi</td>
        <td class="l"><input type="radio" name="tipo" value="oggi" onclick="javascript:inScadenza()"></td>
      </tr>
      <tr>
        <td class="l">Scaduti</td>
        <td class="l"><input type="radio" name="tipo" value="scaduto" onclick="javascript:inScadenza()"></td>
      </tr>
    </table>
    <br>
    <INPUT class="bottone" type="submit"   name="RICERCA" value="Ricerca" onClick="">
 </form>
 
<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRicercaScadenzarioPP");
    frmvalidator.setAddnlValidationFunction("verifica");
</script>

</body>

</html>