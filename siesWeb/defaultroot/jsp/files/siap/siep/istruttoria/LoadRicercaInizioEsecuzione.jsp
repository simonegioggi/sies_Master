<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>


<jsp:useBean id="strFunzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloNotInSession" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - ciao <%=strFunzione%></title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	

  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
 <script language="JavaScript">
 function Verify()
 {
  annof = 0;
  annoi = 0;
  progrf = 0;
  progri = 0;
 
 
  annof = parseInt(document.a.CampoChiaveAnnoFinale.value);
  annoi = parseInt(document.a.CampoChiaveAnnoIniziale.value);
  progrf = parseInt(document.a.CampoChiaveProgrFinale.value);
  progri = parseInt(document.a.CampoChiaveProgrIniziale.value);
 
   if (annoi>annof)
    {
      alert('Anno finale minore dell\'anno iniziale.');
      return false;
    }
   if ((annoi==annof )  && (  progri>progrf ) )
    {
      alert('Progressivo finale minore del progressivo iniziale.');
      return false;
    }
    
    return true;
 
 }
 </script>

</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Stampa Comunicazioni Inizio Esecuzione Multiple</font>
      </td>
      <td class="LBG">
      </td>
     </tr>
  </table>
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="a">
  <div id="intervallo" style="visibility:visible; position:relative; top:15px; width:100%;">
  
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActLoadStampaInizioEsecuzioniMultiple">

  <table>
    <tr><td class="Titolo" colspan=4>Intervallo Procedimenti</td></tr>
    <tr>
      <td class="L" >
        <font class="label">
          Anno/Numero Iniziale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" 
               maxlength="4" size="4" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" 
               maxlength="14" size="14"
               onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="L">
        <font class="label">
          Anno/Numero Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" 
               onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        /
        <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>"
               maxlength="14" size="14"
               onkeypress="return TicTabNumField(this,event)">
      </td>
     </tr>
   </table>
   
   <br>
     
   <table>
     <tr>
        <td class="l" >
        <input class="bottone" type="submit" name="STAMPA" value="Stampa" >
      </td>
    </tr>
  </table>
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("a");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>","req");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","req");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>","req");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","req");
 
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>","numeric","Il campo Numero Procedimento Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>","numeric","Il campo Numero Procedimento Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>

</body>
</html>