<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.decodifiche.model.OggettiModel" %>

<jsp:useBean id="ListaOggetti" scope="request" class="java.util.Vector" />
<jsp:useBean id="InputFieldCodes" scope="request" class="java.lang.String" />
<jsp:useBean id="InputFieldCodesDet" scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - Lista Oggetti per Contenuto procedimento</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript">
var node;
var nodeTD;
function espandi(elem) {
	node = document.getElementById(elem);
  	if (node.style.visibility == 'visible') {
	    node.style.visibility = 'hidden';
	    vedo = false;
  	} else {
    	node.style.visibility = 'visible';
    	vedo = true;
  	}
}

var desktop;
var strDescr = '';
var strCode = '';
var strCodeDettaglio = '';

// Funzione di caricamento degli Oggetti e Dettagli selezionati coi checkbox e combobox, in 4 variabili (descrizioni e codici).
function loadOggetti(lOggetti, lDettagli, lDesc, lDescDettaglio) {
	if (typeof (lOggetti.length) == "undefined") {
    	if (lOggetti.checked) {
      		strDescr += lDesc.value +'\n';
      		strCode += lOggetti.value +'|';
    	}
  	} else {
    	for (i = 0; i < lOggetti.length ; i++) {
      		if (lOggetti[i].checked) {
        		strDescr += lDesc[i].value +'\n';
        		strCode += lOggetti[i].value +'|';
      		}
    	}
  	}
  	if (typeof (lDettagli[0][0]) =="undefined") {
    	for (i = 0; i < lDettagli.length ; i++) {
      		if (lDettagli[i].selected) {
        		strCodeDettaglio += lDettagli[i].value +'|';
      		}
    	}
  	} else {
    	for (j = 0; j < lDettagli.length ; j++) {
      		for (i = 0; i < lDettagli[j].length ; i++) {
        		if (lDettagli[j][i].selected && lDettagli[j][i].value != '-') {
          			strCodeDettaglio += lDettagli[j][i].value +'|';
        		}
      		}
    	}
  	}
  	insertIT(strDescr, strCode, strCodeDettaglio);
}

function insertIT(strDescr, strCode, strCodeDettaglio) {
  	window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldname")%>.value=strDescr;
	window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcodes")%>.value=strCode;
	window.opener.document.<%=request.getParameter("formname")%>.<%=request.getParameter("fieldcodesdet")%>.value=strCodeDettaglio;
  	self.close();
}
</script>
</head>

<body>
<table>
	<tr>
		<td class=LBG>Selezionare gli oggetti per il Contenuto</td>
	</tr>
</table>

<table width="100%">	
<%
String preContenuto = "";
String preOggetto = "";
String preDettaglio = "";
String descDettaglio = "";
ListIterator itx = ListaOggetti.listIterator();
// Ciclo di Caricamento Oggetti e Dettagli.
while (itx.hasNext()) {
	// 11/11/2003 Sostituiti i riferimenti di DecodificheModel con OggettiModel.
  	OggettiModel lOggetti = (OggettiModel) itx.next();
  	// Si pone l'intestazione del Contenuto.
  	if (!lOggetti.getCodContenuto().equals(preContenuto)) {
    	preContenuto = lOggetti.getCodContenuto();
%>
	<tr>
	  	<td class=Titolo><%=lOggetti.getDescContenuto()%></td>
	</tr>
<%
   	}
   	// Si pone l'i.mo Oggetto.
	if (!lOggetti.getCodOggetto().equals(preOggetto)) {
		preOggetto = lOggetti.getCodOggetto();
%>
	<%-- 20190726 [SG]: restyling della pagina
		value='<%=lOggetti.getDescOggetto()%>' --> value="<%=lOggetti.getDescOggetto()%>"
		altrimenti tronca le frasi che contengono il carattere '
	 --%>
	<tr>
		<td class=l>
			<input type="HIDDEN" name="lDesc" value="<%=lOggetti.getDescOggetto()%>">
			<%=lOggetti.getDescOggetto()%><%=lOggetti.getAbbrOggetto() == null ? "" : " - "+ lOggetti.getAbbrOggetto()%>
		</td>
		<td class=l>
			<input type="checkbox" name="lOggetti" value="<%=lOggetti.getCodOggetto()%>"
<%
		if (InputFieldCodes.indexOf(lOggetti.getCodOggetto()) >= 0) {
%>
			CHECKED
<%
       	}
       	if (lOggetti.getCodDettaglio() != null
       			&& lOggetti.getCodDettaglio().length() > 0) {
%>
			onClick="javascript:espandi('<%=lOggetti.getCodOggetto()%>');"
<%
		}
%>
			>
		</td>
	</tr>
<%
	}
   	// 11/11/2003 Aggiunto il ciclo per la visualizzazione dei dettagli Oggetto.
   	// Per mantenere una grafica accettabile le descrizioni dei dettagli vengono troncate a 108 caratteri...
   	if (lOggetti.getCodDettaglio() != null
   			&& lOggetti.getCodDettaglio().length() > 0
   			&& lOggetti.getCodDettaglio() != preDettaglio) {
%>
	<tr>
		<td>
          	<div id=<%=lOggetti.getCodOggetto()%>
<%
		if (InputFieldCodes.indexOf(lOggetti.getCodOggetto()) >= 0) {
%>
				style="visibility='visible';"
<%
		} else {
%>
              	style="visibility='hidden';"
<%
		}
%>
			>
            <select class="small" name="lDettagli">
				<option value="-" SELECTED>-&nbsp;</option>
<%
			String lCampiHidden = "";
           	while (lOggetti.getCodDettaglio() != null
           			&& lOggetti.getCodDettaglio() != preDettaglio
           			&& lOggetti.getCodOggetto().equals(preOggetto)) {
           		preDettaglio = lOggetti.getCodDettaglio();
           		int lengDettaglio = ((lOggetti.getDescDettaglio().length() < 108) ? lOggetti.getDescDettaglio().length() : 108);
           		descDettaglio = lOggetti.getDescDettaglio().substring(0, lengDettaglio);
           		if (lengDettaglio == 108 )
               		descDettaglio += "...";
           		lCampiHidden +="<input type='HIDDEN' name='lDescDettaglio' value='" + lOggetti.getDescDettaglio() + "'>";
%>
              	<option value="<%=lOggetti.getCodOggetto()%><%=lOggetti.getCodDettaglio()%>"
<%
				if ((InputFieldCodesDet.indexOf(lOggetti.getCodOggetto()) >= 0)
						&& (InputFieldCodesDet.indexOf(lOggetti.getCodDettaglio() + "|") >= 0)) {
%>
					SELECTED
<%
               	}
%>
                >
                	<%=descDettaglio%>
              	</option>
<%
           		if (itx.hasNext())
               		lOggetti = (OggettiModel) itx.next();
           	}
       		lOggetti = (OggettiModel) itx.previous();
%>
			</select>
		</div>
     	<%=lCampiHidden%>
		</td>
	</tr>
<%
	}
}

// Nel Caso non ci siano dettagli, occorre definire variabili nascoste.
if (preDettaglio.equals("")) {
%>
	<tr>
		<td>
		    <div id="9999" style="visibility='hidden';">
		      	<select class="small" name="lDettagli">
		        	<option value="-" SELECTED>-&nbsp;</option>
		      	</select>
		    </div>
			<input type="HIDDEN" name="lDescDettaglio" value="-">
		</td>
	</tr>
<%
}
%>
  	<tr>
    	<td>
      		<input onclick="Javascript:loadOggetti(lOggetti, lDettagli, lDesc, lDescDettaglio);" class="bottone" type="submit" value="Conferma">
		</td>
	</tr>
</table>
</body>
</html>