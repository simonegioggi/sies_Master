<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.siep.scadenzario.model.ScadenzarioModel"%>
<%@ page import="siap.siep.scadenzario.action.ICostantiScadenzario"%>




<html>
<head>
<title>[S.I.E.S.] - Notifica dell'Odrdine di Esecuzione con Sospensione </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"> </script>

 <script language="JavaScript">
      function Verify()
      {
          if (document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_GIORNO_DATA_INIZIO_SCADENZA%>.value.length==1)
           document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_GIORNO_DATA_INIZIO_SCADENZA%>.value='0'+document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_GIORNO_DATA_INIZIO_SCADENZA%>.value;
          if (document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_MESE_DATA_INIZIO_SCADENZA%>.value.length==1)
           document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_MESE_DATA_INIZIO_SCADENZA%>.value='0'+document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_MESE_DATA_INIZIO_SCADENZA%>.value;
            var data_to_verify=document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_GIORNO_DATA_INIZIO_SCADENZA%>.value+'/'+document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_MESE_DATA_INIZIO_SCADENZA%>.value+'/'+document.LoadInserisciScadenzario.<%=ICostantiScadenzario.CAMPO_ANNO_DATA_INIZIO_SCADENZA%>.value;
          if (! ControllaData(data_to_verify))
          {
            alert('Data non valida');
            return false;
          }else
          {
           // document.LoadInserisciScadenzario.action="siap.siep.scadenzario.action.ActInserisciScadenzario";
            //document.LoadInserisciScadenzario.submit();
          }
      }
  </script>

</head>


		<body class="corpo">
			<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			 <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                         <%ScadenzarioModel lScaMod = new ScadenzarioModel();%>

			<font class="campo">Inserimento Odrdine di Esecuzione con Sospensione</font>


</td>
</tr>
</table>

<br>
<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
<br>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciScadenzario">
  <table cellspacing=2 cellpadding=2>
    <tr>
       <td class="l" >Data Ordine di Esecuzione</td>
        <td class="l">

          <input title="dd" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaMod.getDataInizioScadenza(),"dd"))%>" type="text" name="<%= ICostantiScadenzario.CAMPO_GIORNO_DATA_INIZIO_SCADENZA%>"  >
         /
          <input title="mm" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaMod.getDataInizioScadenza(),"MM"))%>" type="text" name="<%= ICostantiScadenzario.CAMPO_MESE_DATA_INIZIO_SCADENZA%>">
          /
          <input title="yyyy" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lScaMod.getDataInizioScadenza(),"yyyy"))%>" type="text" name="<%= ICostantiScadenzario.CAMPO_ANNO_DATA_INIZIO_SCADENZA%>"  >
          </td>
      </tr>

      <tr>
        <td>
            <br>
        <INPUT onclick="Javascript:return Verify();" class="bottone" type="button" name="INSERISCI" value="Conferma">
        </td>
      </tr>
  </table>

</form>
</body>
</html>