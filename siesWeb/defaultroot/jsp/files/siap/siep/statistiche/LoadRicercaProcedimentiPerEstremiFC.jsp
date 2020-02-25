<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.statistiche.action.ICostantiStatistiche"%>

<jsp:useBean id="modalitaRicerca"  scope="request" class="java.lang.String"/>

<%
	// Valore di default della funzione
	String lNomeFunzione = "Ricerca Procedimento";
	
	if (modalitaRicerca != null && modalitaRicerca.equalsIgnoreCase(ICostantiStatistiche.RICERCA_FOGLIO_COMPLEMENTARE)){
		lNomeFunzione = "Ricerca Procedimento per estremi Foglio Complementare";
	}
%>
<script language="JavaScript">
  function Init()
  {
        if (document.f.<%=ICostantiStatistiche.CAMPO_TIPO_RICERCA%>[0].checked)
 	       VisualizzaRicercaBase();
        else
           VisualizzaRicercaAvanzata();
  }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Statistiche Ricerca Ordinanze</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ICostantiStatistiche.RICERCA_ORDINANZA_JS%>"></script>

</head>
  <body class="corpo" onLoad="Init();">
  <form name="f">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lNomeFunzione%></font>

      </td>
    </tr>
  </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="valoreRadio" value="">
  <br>
   <table style="width: 95%;">
      <tr><td class="Titolo" >Tipo Ricerca</td></tr>
          <td class="c" width="61%" >
            Base <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_RICERCA%>" value="B"   onClick="VisualizzaRicercaBase();" checked>&nbsp;&nbsp;&nbsp;&nbsp; 
			Avanzata <input type="radio" name="<%=ICostantiStatistiche.CAMPO_TIPO_RICERCA%>" value="A"   onClick="VisualizzaRicercaAvanzata();">&nbsp;</td>
      </tr>
    </table>
  
  </form>

  <div id="comune" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
    <div id="RicercaBaseDiv" style="position:relative;  top: 0; left: 0;   visibility:visible; " >  
         <jsp:include page="<%=ICostantiStatistiche.DIV_RICERCHE_ORDINANZE_BASE%>"/>
    </div>
     <div id="RicercaAvanzataDiv" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
        <jsp:include page="<%=ICostantiStatistiche.DIV_RICERCHE_ORDINANZE_AVANZATA%>"/>
     </div>
 </div>

  </body>
</html>