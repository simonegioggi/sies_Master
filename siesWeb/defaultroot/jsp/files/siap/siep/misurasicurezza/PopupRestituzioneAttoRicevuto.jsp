<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>


<html>
  <head>
    <title>[S.I.E.S.] - Motivo della Restituzione</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script>
    function verify(){
      if(document.restAtti.<%=ICostantiFascicoloSiep.CAMPO_NOTE%>.value==''){
        alert("Il campo Motivazioni è obbligatorio!");
        document.restAtti.<%=ICostantiFascicoloSiep.CAMPO_NOTE%>.focus();
      }
      else{
        invia();
      }
    }
    
    function invia(){
      opener.azioniPresaIncarico.MotivoRestituzione.value = document.restAtti.<%=ICostantiFascicoloSiep.CAMPO_NOTE%>.value;
      opener.azioniPresaIncarico.PresaInCaricoButton.disabled=true;
      opener.azioniPresaIncarico.RestituzioneButton.disabled=true;
      opener.azioniPresaIncarico.IscrizioneButton.disabled=true;


      opener.azioniPresaIncarico.submit();    
      self.close();
    }
    </script>
    
  </head>

<body class="corpo">
  <table>
    <tr>
      <td class=LBG><font class="label">Funzione:</font> <font class="campo">RESTITUZIONE ATTI</font>&nbsp;</td>
    </tr>
  </table>
   

  <FORM name="restAtti">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Motivazioni</td>
        <td class="l">
          <textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols=40 rows=5></textarea>
        </td>
      </tr>
      <tr height=50>
        <td> </td>
      </tr>   
      <tr>
        <td>
          <input class="bottone" value="Conferma" onclick="verify()">
        </td>
      </tr>
    </table>
  </form>
  </body>
</html>