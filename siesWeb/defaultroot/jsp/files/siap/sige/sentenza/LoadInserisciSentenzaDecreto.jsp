<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoProvvedimentiRif" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaProvRif" scope="request" class="java.lang.String" />
<jsp:useBean id="flagSN" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito1" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoRito2" scope="request" class="java.lang.String" />
<jsp:useBean id="lTipoFunzione" scope="request" class="java.lang.String" />

<head>
<title>[S.I.E.S.] - Inserimento Sentenza, Decreto Penale, Sentenza Straniera Delibata </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
	var desktop;
	function ListaComuni(a_formname,a_fieldname)
	{
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}
  // 21/02/2011 Lista Uffici per TIPO_UFFICIO
  function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
 	{
   	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
 	}
</script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript" src="<%=ICostantiFasSigeSentenza.JS_SENTENZA%>"></script>

<script language="JavaScript">
	function Init()
	{
		if (document.f.<%=ICostantiFasSigeSentenza.RADIO_TIPO_FUNZIONE%>[0].checked)
			VisualizzaSentenza();
<%-- Ticket#20220706018 - Il caricamento delle sezioni Decreto Penale e Sentenza Straniera 
     erano invertite rispotto ai Radio Button
		else if (document.f.<%=ICostantiFasSigeSentenza.RADIO_TIPO_FUNZIONE%>[1].checked)
			VisualizzaSentenzaStraniera();
		else
			VisualizzaDecreto();
--%>			
		else if (document.f.<%=ICostantiFasSigeSentenza.RADIO_TIPO_FUNZIONE%>[1].checked)
			VisualizzaDecreto();
		else
			VisualizzaSentenzaStraniera();
	}
</script>

</head>

<body class="corpo" onLoad="Init();">

<%-- inizio aggiunta --%> 
<form name="f" >
  <table id="tableTitle">
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Inserimento Sentenza, Decreto Penale, Sentenza Straniera Delibata</font>

      </td>
    </tr>
  </table>
  <br>
  <table width="85%">
    <tr>
      <td class="Titolo" width="35%" > Selezionare il tipo di Provvedimento&nbsp;</td>
      <td class="Titolo" >
        Sentenza <input type="radio" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_FUNZIONE%>" value="0"   onClick="VisualizzaSentenza();" checked>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				Decreto Penale <input type="radio" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_FUNZIONE%>" value="1"   onClick="VisualizzaDecreto();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				Sentenza Straniera Delibata<input type="radio" name="<%=ICostantiFasSigeSentenza.RADIO_TIPO_FUNZIONE%>" value="2"   onClick="VisualizzaSentenzaStraniera();">&nbsp;
      </td>
    </tr>
  </table>
</form>

  <br>
    
  <div id="comune" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
  	<div id="SentenzaDiv" style="position:relative;  top: 0; left: 0; visibility:visible; " >  
		<jsp:include page="<%=ICostantiFasSigeSentenza.DIV_SENTENZA%>"/>
  	</div>
  	<div id="SentenzaStranieraDiv" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
    	<jsp:include page="<%=ICostantiFasSigeSentenza.DIV_SENTENZA_STRANIERA%>"/>
  	</div>
  	<div id="DecretoDiv" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
    	<jsp:include page="<%=ICostantiFasSigeSentenza.DIV_DECRETO%>"/>
  	</div>
  </div>
<%-- fine aggiunta --%> 


</body>
</html>