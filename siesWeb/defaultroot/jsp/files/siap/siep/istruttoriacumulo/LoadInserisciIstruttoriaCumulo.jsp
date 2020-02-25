<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<%
//==============================================================================
//             Form per l'Apertura di un'istruttoria cumulo
//==============================================================================
%> 

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    
    function Verify() 
    {
      return true;
    }    
    
    function verifica(){
     	mostraAttesa('Attendere: elaborazione in corso');
     	return true;
    }
  
  
    //==========================================================================
    // Ritorna alla Griglia Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.InserisciIstruttoriaCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.InserisciIstruttoriaCumulo.submit();
    }
    
    //==========================================================================
    // accetta una stringa di testo non html da mostrare
    //==========================================================================
    function mostraAttesa(testo) 
    {
      var puntini = 0,
      testoIntrattenimento = prendiElementoDaId("testo-temporaneo"),
    
      animaTesto = function() {
                                var testoAggiunto = "";
                          
                                for(var a = 0; a < puntini; a++)
                                  testoAggiunto += ".";
                          
                                testoIntrattenimento.nodeValue = testo + testoAggiunto;
                          
                                if(puntini < 4)
                                  puntini++;
                                else
                                  puntini = 0;
                          
                                setTimeout(animaTesto, 300);
                              }
    
      if(testoIntrattenimento.firstChild) 
      {
        animaTesto = function(){};
        testoIntrattenimento.removeChild(testoIntrattenimento.firstChild);
      }
      else 
      {
        testoIntrattenimento = document.createTextNode(testo);
        prendiElementoDaId("testo-temporaneo").appendChild(testoIntrattenimento);
        animaTesto();
      }
    }  
    
    //======================================
    //
    //======================================
    function prendiElementoDaId(id_elemento) 
    {
      var elemento;
      if(document.getElementById)
        elemento = document.getElementById(id_elemento);
      else
        elemento = document.all[id_elemento];
      return elemento;
    } 
    
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Apertura Istruttoria Cumulo</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla alla Griglia Gestione Cumulo -->
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="InserisciIstruttoriaCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActInserisciIstruttoriaCumulo">
  <% if (IstruttoriaCumulo!=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null) {%>
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <% } %>
  
  <table cellspacing="0" cellpadding="0">
    <% if (IstruttoriaCumulo!=null && IstruttoriaCumulo.getIdIstruttoriaCumulo()!=null) {%>
    <tr>
      <td class="L">
        Attenzione!! Esiste già una istruttoria aperta: 
        <font class="label">Istruttoria N. </font>
        <font class="campo">
          <a class="cliccabile" href="/jsp/Main.jsp?Action=siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo&<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>=<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>" title="Istruttoria">
          <%=IstruttoriaCumulo.getAnnoProtocollo()%>
          /
          <%=IstruttoriaCumulo.getNumProtocollo()%>
          </a>
        </font>
        <font class="label">Del </font>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(IstruttoriaCumulo.getDataApertura(),"dd-MM-yyyy"))%></font>
      </td>
    </tr>
    <% } else { %>
    <tr>
      <td class="L">
        Apertura nuova istruttoria cumulo
      </td>
      <td colspan="1">
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
    <% } %>
  </table> 
</form>   
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("InserisciIstruttoriaCumulo");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

