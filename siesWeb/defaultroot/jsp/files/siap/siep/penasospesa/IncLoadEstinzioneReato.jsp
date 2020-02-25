<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="f3b.web.html.Option"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<% 
Collection oggetto =(Collection) request.getAttribute("oggetto");
String strOggetto ="";
Iterator itxOggetto = oggetto.iterator();
while(itxOggetto.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
  
   strOggetto += lDecMod.getFiltro() +";";
   strOggetto += lDecMod.getCode()+";";
   strOggetto += lDecMod.getDescription()+"#";
// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
//	siesLogger.debug("strOggett="+strOggetto);
}

%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Estinzione di Pena </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript">

function inizia()
	{
	caricatuttecombo();
	}

function caricaCombo (valueTextStr, sep1, sep2, filtro, selField)
  {
    // valueTextStr = stringa nel formato richiesto
    // sep1 = separatore interno alla coppia di valori
    // sep2 = separatore tra coppie
    // filtro = valore su cui fare il test
    // selField = oggetto combo da caricare

    clearDropDown(selField);
    var aPairs = valueTextStr.split(sep2);
    if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
    {
      aPairs[aPairs.length - 1] = null;
      aPairs.length--;
    }

    for (var i=0; i < aPairs.length; i++)
    {
      aValueText = aPairs[i].split(sep1);
      if (filtro=='null' || filtro==aValueText[0])
      {
    		oItem = new Option;
    		oItem.value = aValueText[1];
    		oItem.text = aValueText[2];
    		selField.options[selField.options.length] = oItem;
      }
    }
    selField.options.selectedIndex = 0;
  }

  function clearDropDown (selField)
  {
  	while (selField.options.length > 0)
  		selField.options[0] = null;
  }

  function caricatuttecombo()
  {
  	var strOggetto = "<%=strOggetto%>";
  	var art = document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").selectedIndex;
    caricaCombo(strOggetto,';','#',document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>").options[art].text,document.getElementById("<%=ICostantiPenaSospesa.CAMPO_COD_MOTIVO%>"));
  }
</script>

</head>
  
    <table style="width: 95%;">
	    <tr><td class="Titolo" colspan=4>Estinzione Reato</td></tr>
  </table>
  <table style="width: 95%;">
		<tr>
	      	<td class="l">Articolo</td>
	      	<td class="l">
            <select name="<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO %>" Title="Tipo Provvedimento" onchange="javascript:caricatuttecombo()">
<%
Iterator itxArticolo = oggetto.iterator();
while(itxArticolo.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxArticolo.next();
   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug("lDecMod.getCode()="+lDecMod.getCode());
   // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug("lDecMod.getCodiceAlt2()="+lDecMod.getCodiceAlt2());
	   	if((lDecMod.getCode()).equals(lDecMod.getCodiceAlt2())){
%>
					<option value = <%=lDecMod.getCode()%> ><%=lDecMod.getFiltro() %></option> 
<%  
	   	}
   }


%>
             		</select>
	      	</td>
		</tr>
		<tr>
	      	<td class="l">Motivazione</td>
	      	<td class="l">
             		<select class="small"  name="<%= ICostantiPenaSospesa.CAMPO_COD_MOTIVO %>" Title="Tipo Provvedimento" >
     				
             		</select>
	      	</td>
		</tr>
		<tr>
	      	<td class="l">Note</td>
	      	<td class="l">
	        	<Textarea Title="Note" name="<%= ICostantiPenaSospesa.CAMPO_NOTE %>" cols=80 rows=5></textarea>
	      	</td>
		</tr>
		  </table>
 </html>