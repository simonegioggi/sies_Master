<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sige.sentenza.action.ICostantiFasSigeSentenza"%>

<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<html>
<script language="JavaScript">

  // La funzione attiva e rende visibile una DIV all'interno del documento
  function AbilitaDiv(nomeDiv)
  {
      node=document.getElementById(nomeDiv);
      node.style.visibility='visible';
      node.disabled = false;   
   }
 
   // La funzione disattiva e rende invisibile una DIV all'interno del documento 
  function DisabilitaDiv(nomeDiv)
  {
    node=document.getElementById(nomeDiv);
   	node.style.visibility='hidden';
    node.disabled = true;
   }

  // Inizializza la jsp LoadRicercaFSigePerTitoloEsecutivo
  function Init()
  {
		if (document.LoadRicercaTitolo.CAMPO_TIPO_RICERCA[0].checked)
			VisualizzaRicercaSentenza();
		else
			VisualizzaRicercaProcedimento();
  }
  /* Attiva la jsp IncLoadRicercaFascicoloSiep
  	rendendo visibile al suo interno la div corrispondente 
  	alla sezione selezionata.
  */
   function ApriFascicolo()
  {
  	AbilitaDiv ("comuneFascicoloSiep");
  	
  	if ( (document.all.item("tipo"))[0].checked)
  	{
     	AbilitaDiv ("singolo");
     	DisabilitaDiv("divIntervallo");
     }
     else
     {
     	AbilitaDiv ("divIntervallo");
     	DisabilitaDiv("singolo");
     }   
  }
  
  // Disattiva la jsp IncLoadRicercaFascicoloSiep
  function ChiudiFascicolo()
 	{
		DisabilitaDiv ("comuneFascicoloSiep"); 	
		DisabilitaDiv ("singolo");
		DisabilitaDiv("divIntervallo");
 	}


  /* 
  	Visualizza all'interno della jsp LoadRicercaFSigePerTitoloEsecutivo
  	la sezione relativa alla ricerca del Procedimento SIEP.
  */
  function VisualizzaRicercaProcedimento()
  {
	 	DisabilitaDiv("RicercaSentenzaDiv")
 		ApriFascicolo();
		AbilitaDiv("RicercaProcedimentoDiv")
  }
  
  /* 
  Visualizza all'interno della jsp LoadRicercaFSigePerTitoloEsecutivo
  la sezione relativa alla ricerca della Sentenza.
  */
   function VisualizzaRicercaSentenza()
  {
		ChiudiFascicolo();
		DisabilitaDiv("RicercaProcedimentoDiv");
		AbilitaDiv("RicercaSentenzaDiv");
	}
</script>


<head>
  <title>[S.I.E.S.] - Ricerca Titolo esecutivo per Procedimento SIGE</title>

</head>

  <body class="corpo" onLoad="JavaScript:Init();">
  <form name="LoadRicercaTitolo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento SIGE per Titolo Esecutivo </font>
      </td>
    </tr>
  </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="HIDDEN" name="valoreRadio" value="">
  <br>
   <table width="75%">
      <tr>
          <td class="titolo" width="30%" > tipo di ricerca: </td>
          <td class="Titolo">
          Ricerca Sentenza/Decreto <input type="radio" name=CAMPO_TIPO_RICERCA value="B"  onClick="VisualizzaRicercaSentenza();"  checked>&nbsp;&nbsp;&nbsp;&nbsp; 
					Ricerca Procedimento SIEP <input type="radio" name=CAMPO_TIPO_RICERCA value="A"  onClick="VisualizzaRicercaProcedimento();" >&nbsp;</td>
      </tr>
    </table>
  </form>

  <div id="comune" style="position: relative; top: 0; left: 0;   visibility:visible; " >     
    <div id="RicercaProcedimentoDiv" style="position:relative;  top: 0; left: 0; visibility:hidden;   " >  
       <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_RICERCA_FASCICOLO_SIEP%>"/>   
    </div>
    <div id="RicercaSentenzaDiv" style="position: absolute; top: 0; left: 0; visibility:visible; ">      
        <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_RICERCA_SENTENZA%>"/>   
     </div>
  </div>
</html>