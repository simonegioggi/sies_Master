<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza" %>

<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String"/>


<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
</head>

<body class="corpo" onLoad="document.f.<%=ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA%>.focus();">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name='f'>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.regesies.regesentenza.action.ActRicercaRegeSentenzaPerEstremi">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Provvedimento REGE per Estremi</font>
      </td>
    </tr>
  </table>
   <br>
  <table cellpadding=2 cellspacing=2>
    <tr>
      <td class="L"> Anno/Numero Provvedimento <font class=ob>(*)</font></td>
      <td>
        <input type="text" title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Provvedimento" name="<%=ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA%>" maxlength="6" size="6" >
      </td>
    </tr>

    <tr>
         <td class="L">Ufficio <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

         <td class="l"><select name="<%=ICostantiRegeSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>" >
            <%=autoritaEsterna%>
            </select> </td>
      </tr>
      <%
      if(tipoUfficio.equals("CAP")||tipoUfficio.equals("PGCAP")
      ||tipoUfficio.equals("CAPSM")||tipoUfficio.equals("CASAP"))
      {%>
      <tr>
       <td class="l">Sede <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="L">
       <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE %>" value="<%=descrComune%>" maxlength="35" size="35" >
         <input type="hidden" Title="distrettoUffcio" name="distrettoUffcio" value="" size=35 >
       <a href="Javascript:ListaUffici('d','<%=ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE%>');">

 <img src="/images/filefolder.gif" border=0></a></td>
   </tr>
   <%}else{//Ufficio di primo grado il comune dell'ufficio è bloccato
     %>
    <tr>
       <td class="l">Sede&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
       <td class="l"><font class="campo"><%=descrComune%></font></td>
   </tr>
   <%}%>

   <tr>
      <td colspan="2"> &nbsp;&nbsp; </td>
   </tr>


    <tr>
      <td colspan="2">
        <br><br>
        <input class="bottone" type="submit" name="CONFERMA" value="CONFERMA">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("f");

     frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","req","Il campo Anno Procedimento è obbligatorio");
     frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
     frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
     frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_SENTENZA%>","numeric");

     frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_NUMERO_SENTENZA%>","req");
 <%if(tipoUfficio.equals("CAP"))
      {%>
     frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","req");
<%}%>

   </script>
</body>
</html>