<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="aNomeFormChiamante" scope="request" class="java.lang.String"/>
<jsp:useBean id="aMotivoModifica" scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form per richiedere il motivo della Cancellazione logica di un dato analitico
// La form scrive la motivazione in un campo hidden della lista dei dati analitici
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Motivazioni della Cancellazione</title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      function verify()
      {
        var lmotivazioni = document.f.motivazioni.value;
        window.parent.opener.document.<%=aNomeFormChiamante%>.<%=ICostantiModuloCumulo.CAMPO_MOTIVO_MODIFICA%>.value=lmotivazioni; 
        window.parent.opener.document.<%=aNomeFormChiamante%>.submit();
        window.parent.close();
      }
    </script>
</head>
  
  
<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Annullamento Dato Analitico</font>
      </td>
    </tr>
  </table>
  
  <FORM method="POST" name="f" action="">
    <table width="80%">
      <tr>
        <td class="l" width="20%">Motivazioni </td>
        <td  class="l">
          <TEXTAREA title="Note" name="motivazioni" cols="50" rows="3"><%=StringUtils.toStringJSP(aMotivoModifica,"")%></textarea>
        </td>
      </tr>
      
      <tr>
        <td class="lNoBord" colspan="2">
          <br><br>
          <INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="verify()">
        </td>
      </tr>
    </table>
  </form>  
</body>
</html>