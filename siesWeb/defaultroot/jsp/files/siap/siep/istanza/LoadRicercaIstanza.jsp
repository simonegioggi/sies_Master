<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>

<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Ricerca Istanza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

    <script language="JavaScript" >
    function VerificaSoggetto(){
      if(document.RicercaIstanza.<%=ICostantiSoggetto.CAMPO_NOME%>.value.length>0 || document.RicercaIstanza.<%=ICostantiSoggetto.CAMPO_COGNOME%>.value.length>0)
      {
        //document.RicercaIstanza.submit();
      }else
      {
        alert("Inserire almeno una lettera");
        return false;
      }
      document.RicercaIstanzaOggetto.RICERCA.disabled=true;
      return true;
    }
    </script>

    <script language="JavaScript">
      function VerificaOggetto()
      {
        //document.RicercaIstanzaOggetto.submit();
        document.RicercaIstanza.RICERCA.disabled=true;
        return true;
      }
    </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione : Ricerca Istanza</font>&nbsp;&nbsp;
      </td>
    </tr>
  </table>
  
  <br>
  
  <form name="RicercaIstanza" method="POST" action="<%=IWebConstants.PG_MAIN%>">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActRicercaIstanzaSoggetto" >

    <table>
      <tr><td class="Titolo" colspan=4>Soggetto presentante</td></tr>
      <tr>
        <td class="l">Cognome
          <input type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>">
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td class="l">Nome &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>">
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2">
          <INPUT class="bottone" type="submit"   name="RICERCA" value="Ricerca" >
        </td>
      </tr>
    </table>
     <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("RicercaIstanza");

      frmvalidator.addValidation("<%=ICostantiSoggetto.CAMPO_COGNOME%>","alpha");
      frmvalidator.addValidation("<%=ICostantiSoggetto.CAMPO_NOME%>","alpha");
      frmvalidator.setAddnlValidationFunction("VerificaSoggetto");

    </script>
  </form>


  <form name="RicercaIstanzaOggetto" method="POST" action="<%=IWebConstants.PG_MAIN%>">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istanza.action.ActRicercaIstanzaOggetto">
    
    <table>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td class="Titolo" colspan=4>Dati dell'Istanza</td>
      </tr>
      <tr>
        <td class="l">Oggetto</td>
        <td class="L">
          <select Title="Oggetto Istanza" name="<%=ICostantiIstanza.CAMPO_COD_MOTIVO%>">
            <%=contenuto%>
          </select>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2">
          <input class="bottone" type="submit"   name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
     <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("RicercaIstanzaOggetto");
      frmvalidator.setAddnlValidationFunction("VerificaOggetto");

    </script>

  </form>
</body>
</html>