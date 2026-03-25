<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>

<%
//==============================================================================
//             MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
//         Form per l'Annullamento/trasferimento di un'istruttoria cumulo
//==============================================================================
%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // Ritorna alla Griglia Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {  
      document.RicercaFascicolo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.RicercaFascicolo.submit();
    }
    
    function Verify()  
    {
       if(document.RicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value == "")
       {
         alert("Per la ricerca per procedimento e' necessario specificare sia anno che numero procedimento");      
         document.RicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
         return false;
       } 
       
       if(document.RicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value == "")
       {
         alert("Per la ricerca per procedimento e' necessario specificare sia anno che numero procedimento");      
         document.RicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.focus();
         return false;
       } 
       
       return true;
     }
  </script>  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Trasferimento Istruttoria Cumulo</font>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  <br>  
  
  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name="RicercaFascicolo">
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaFascPerTrasferisciIstruttoria">
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  

	<table cellpadding="2" cellspacing="2"  width="95%" align="center">
		<tr><td width="90%" class="Titolo">Ricerca Procedimento su cui trasferire l'istruttoria</td></tr>
		<tr>
			<td>
				<table cellpadding="2" cellspacing="2">
				    <tr>
				      <td class="L"> Anno/Numero SIEP</td>
				      <td class="l">
				        <input type="text" title="Anno"  maxlength="4" size="4" 
				               value="<%=DateUtils.getSysDate("yyyy")%>" 
				               name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" 
				               onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
				        /
				        <input type="text" title="Numero SIEP" maxlength="14" size="14"
				               name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>"  >
				      </td>
				    </tr>
	
				    <tr>
				      <td colspan="2">
				        <input class="bottone" type="submit" name="CONFERMA" value="CONFERMA">
				      </td>
				    </tr>	
	    		</table>
	    	</td>
	    </tr>    
    </table>
  </form>  
  <script language="JavaScript" type="text/javascript">
     var frmvalidator  = new Validator("RicercaFascicolo");
     frmvalidator.setAddnlValidationFunction("Verify");  
  </script>
</body>
</html>