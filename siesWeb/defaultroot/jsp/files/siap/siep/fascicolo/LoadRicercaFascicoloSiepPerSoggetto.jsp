<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.jms.jmscode.action.ICostantiJmsCode" %>
<%@ page import="siap.jms.jmscode.model.JmsCodeModel" %>

<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="nazioni"     scope="request" class="java.lang.String"/>
<jsp:useBean id="ListaBDI" scope="request" class="java.util.Vector" />



<html>
<head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<style type="text/css">

#dhtmltooltip{
position: absolute;
width: 150px;
border: 2px solid black;
padding: 2px;
background-color: lightyellow;
visibility: hidden;
z-index: 100;
/*Remove below line to remove shadow. Below line should always appear last within this CSS*/
filter: progid:DXImageTransform.Microsoft.Shadow(color=gray,direction=135);
}

</style>
  <title> [S.I.E.S.] - Ricerca Soggetto - </title>
  <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
  </script>
  <!--<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
  <script language="JavaScript" src="/html/ControllaData.js"></script>-->
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
       if(document.LoadRicercaSoggetto.tipoRicerche[1].checked || document.LoadRicercaSoggetto.tipoRicerche[0].checked)
       {
          var frmvalidator  = new Validator("LoadRicercaSoggetto");

          frmvalidator.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_NOME %>","0","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
          frmvalidator.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_NOME %>","0","alpha");
          frmvalidator.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COGNOME %>","0","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
          frmvalidator.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COGNOME %>","0","alpha");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>","alpha");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_PATERNITA%>","alphabetic");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
          frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");

    if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME%>[0].value=="" && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME%>[0].value==""
         && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==0 && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==0 && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value.length==0
         && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>[0].value=="" && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[0][document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[0].selectedIndex].value =="-" 
         && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.value ==""
         && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.value==""
         &&	document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>[0].value==""
         && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.value=="" && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.value=="" && document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>[0].value=="")

      {
        alert("Inserire almeno il Cognome del Soggetto ");
        document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME%>[0].focus();

        return false;
      }
    if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
			document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
		if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
			document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;

		  var data_to_verify=document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
		  if (! ControllaData(data_to_verify) && data_to_verify.length>2)
		  {
         alert('Data di nascita non valida');
			   return false;
		  }
   }

      if(document.LoadRicercaSoggetto.tipoRicerche[2].checked)
       {
          var frmvalidatorAtriDistr  = new Validator("LoadRicercaSoggetto");
          var nodeAltreBDI=document.getElementById('divAltreBDI');
          var lCuiAltreBdi = document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS %>[1].value;
          
          //alert(lCuiAltreBdi);
          
          if(document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS %>[1].value.length==0 
          && document.LoadRicercaSoggetto.<%= ICostantiSoggetto.CAMPO_NOME %>[1].value.length==0)
          {
          	alert('Inserire i dati Anagrafici completi o il Codice CUI');
          	return false;
          }
          
          if(lCuiAltreBdi.length==0)
          {      
           frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_NOME %>","1","req");
           frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_NOME %>","1","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
           frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_NOME %>","1","alpha");
           frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COGNOME %>","1","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
           frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COGNOME %>","1","alpha");
           frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COGNOME %>","1","req");
           // STUB 27/09/2005 Resi campi obbligatori anche il COMUNE/Paese di Nascita e la DATA di NASCITA.
           if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[1][document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[1].selectedIndex].value =='-')
           {
             frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>","1","req");
             frmvalidatorAtriDistr.addValidationWithIdx("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>","1","alpha");
           }
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>","req");
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>","numeric");
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>","gt=1900");
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>","lt=3000");
           frmvalidatorAtriDistr.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");

           if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA2%>.value.length==1)
			document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA2%>.value='0'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA2%>.value;
           if (document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA2%>.value.length==1)
			document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA2%>.value='0'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA2%>.value;
		
		}
		var data_to_verify=document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA2%>.value+'/'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA2%>.value+'/'+document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>.value;
		if (! ControllaData(data_to_verify) && data_to_verify.length>2)
   		{
        alert('Data di nascita non valida');
			  return false;
	   	}

	    if(document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.value !="" &&
	       document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>.value !="")
	    {
	        alert('Valorizzare o il campo comune nascita estero o il campo comune nascita');
	        return false;
	    }

   	    if(document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.value !="" &&
   	       document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value == '039')
   	   {
   	       alert('Il campo Stato Nascita e comune nascita estero incongruenti');
   				 return false;
   	   }
	    	    
    }
    return true;
  }

  function radioBase()
  {

   if(document.LoadRicercaSoggetto.tipoRicerche[1].checked || document.LoadRicercaSoggetto.tipoRicerche[0].checked )
   {
      radio();
      pulisci();
     document.LoadRicercaSoggetto.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.fascicolo.action.ActRicercaFascicoloPerSoggetto";
   }
   else if (document.LoadRicercaSoggetto.tipoRicerche[2].checked )
   {
     radio();
     pulisci();
     document.LoadRicercaSoggetto.<%=IWebConstants.ACTION_FIELD%>.value="siap.sico.jms.action.ActRicercaSoggettoAltreBDI";
   }
   /*
   else if (document.LoadRicercaSoggetto.tipoRicerche[0].checked )
   {
     radio();
     pulisci();
     document.LoadRicercaSoggetto.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.fascicolo.action.PER UFFICIO";
   }
   */
   function radio()
   {
        var nodeAltreBDI;
        var nodeDistretto;
        var nodeAncheAlias;
        //var nodechkAlias;
        //nodechkAlias=document.getElementById('chkDivAncheAlias');
        nodeAltreBDI=document.getElementById('divAltreBDI');
        nodeDistretto=document.getElementById('divDistretto');
        nodeAncheAlias=document.getElementById('divAncheAlias');
        nodetipoClassiDiv=document.getElementById('tipoClassiDiv');

        if(document.LoadRicercaSoggetto.tipoRicerche[1].checked || document.LoadRicercaSoggetto.tipoRicerche[0].checked)
        {
          nodeAncheAlias.style.display='block';
          nodeDistretto.style.display='block';
          nodeAltreBDI.style.display='none';
          nodetipoClassiDiv.style.display='none';
        }
        else if (document.LoadRicercaSoggetto.tipoRicerche[2].checked )
        {
          nodeAncheAlias.style.display='none';
          nodeDistretto.style.display='none';
          nodeAltreBDI.style.display='block';
        }
        
         if(document.LoadRicercaSoggetto.tipoRicerche[1].checked)
        {
           nodetipoClassiDiv.style.display='none';
        }
            if(document.LoadRicercaSoggetto.tipoRicerche[0].checked)
        {
           nodetipoClassiDiv.style.display='block';
        }
     
      }

}

  function pulisci()
  {
      var frmvalidatorPulisci  = new Validator("LoadRicercaSoggetto");
	 
	  cancellaCodComuneReale();
	   	    
 	if(document.LoadRicercaSoggetto.tipoRicerche[1].checked || document.LoadRicercaSoggetto.tipoRicerche[0].checked)
      {
    	document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME%>[1].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME%>[1].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>[1].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[1].value="-";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA2%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA2%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>[1].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>[1].value="";
		frmvalidatorPulisci.clearAllValidations();
		checkbox();
    }
    else if (document.LoadRicercaSoggetto.tipoRicerche[2].checked )
    {
    	document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME%>[0].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME%>[0].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>[0].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[0].value="-";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>[0].value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.value="";
		document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>[0].value="";
		document.LoadRicercaSoggetto.ancheAlias.checked = false;
		frmvalidatorPulisci.clearAllValidations();
    }
 }
// Funzione legata al checkbox "Anche Alias"
function checkbox()
   {
       var nodechkAlias;
       nodechkAlias=document.getElementById('chkDivAncheAlias');

       if(document.LoadRicercaSoggetto.ancheAlias.checked)
       {
            //pulisce i campi, li disabilita e cambia il colore della label
            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.value="";
            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.readOnly=true;
            CAMPO_PATERNITA=document.getElementById('<%= ICostantiSoggetto.CAMPO_PATERNITA %>');
            CAMPO_PATERNITA.className="cGrigio";

            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.value="";
            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.readOnly=true;
            CAMPO_COGNOME_MADRE=document.getElementById('<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>');
            CAMPO_COGNOME_MADRE.className="cGrigio";

            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.value="";
            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.readOnly=true;
            CAMPO_NOME_MADRE=document.getElementById('<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>');
            CAMPO_NOME_MADRE.className="cGrigio";

            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>[0].value="";
            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>[0].readOnly=true;
            CAMPO_COD_AFIS=document.getElementById('<%= ICostantiSoggetto.CAMPO_COD_AFIS %>');
            CAMPO_COD_AFIS.className="cGrigio";

            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>.value="";
            document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>.readOnly=true;
            CAMPO_ATTO_NASCITA=document.getElementById('<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>');
            CAMPO_ATTO_NASCITA.className="cGrigio";

            // nasconde il nodo(div)
            //nodechkAlias.style.display='none';
       }
       else
       {
           // Riabilita i campi e cambia il colore della label
           document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_PATERNITA%>.readOnly=false;
           CAMPO_PATERNITA=document.getElementById('<%= ICostantiSoggetto.CAMPO_PATERNITA %>');
           CAMPO_PATERNITA.className="";

           document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COGNOME_MADRE%>.readOnly=false;
           CAMPO_COGNOME_MADRE=document.getElementById('<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>');
           CAMPO_COGNOME_MADRE.className="";

           document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_NOME_MADRE%>.readOnly=false;
           CAMPO_NOME_MADRE=document.getElementById('<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>');
           CAMPO_NOME_MADRE.className="";

           document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_COD_AFIS%>[0].readOnly=false;
           CAMPO_COD_AFIS=document.getElementById('<%= ICostantiSoggetto.CAMPO_COD_AFIS %>');
           CAMPO_COD_AFIS.className="";

           document.LoadRicercaSoggetto.<%=ICostantiSoggetto.CAMPO_ATTO_NASCITA%>.readOnly=false;
           CAMPO_ATTO_NASCITA=document.getElementById('<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>');
           CAMPO_ATTO_NASCITA.className="";

           // visualizza il nodo(div)
           //nodechkAlias.style.display='block';
       }
   }

      function cancellaCodComuneReale() {
      
      	document.LoadRicercaSoggetto.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
      }	
      
      function VediOb(idOb)
      {
      if(idOb == 1)
       	{
	       	var node1 = document.getElementById('sog1');
	      	node1.style.display='inline';
	       	var node2 = document.getElementById('sog2');
	      	node2.style.display='inline';
	       	var node3 = document.getElementById('sog3');
	      	node3.style.display='inline';
	       	var node4 = document.getElementById('sog4');
	      	node4.style.display='inline';
	      	var node5 = document.getElementById('cui');
	      	node5.style.display='none';
      	}
      	else
      	{
	      	var node5 = document.getElementById('cui');
	      	node5.style.display='inline';
	      	var node1 = document.getElementById('sog1');
	      	node1.style.display='none';
	       	var node2 = document.getElementById('sog2');
	      	node2.style.display='none';
	       	var node3 = document.getElementById('sog3');
	      	node3.style.display='none';
	       	var node4 = document.getElementById('sog4');
	      	node4.style.display='none';
      	}
      }
      
      
      
       
    </script>
</head>

<body class="corpo" onload="radioBase();">

<div id="dhtmltooltip"></div>

<script type="text/javascript">

/***********************************************
* Cool DHTML tooltip script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

var offsetxpoint=-60 //Customize x offset of tooltip
var offsetypoint=20 //Customize y offset of tooltip
var ie=document.all
var enabletip=false
var tipobj=document.all? document.all["dhtmltooltip"] : document.getElementById? document.getElementById("dhtmltooltip") : ""

function ietruebody(){
return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function ddrivetip(thetext, thecolor, thewidth){
if (ie){
if (typeof thewidth!="undefined") tipobj.style.width=thewidth+"px"
if (typeof thecolor!="undefined" && thecolor!="") tipobj.style.backgroundColor=thecolor
tipobj.innerHTML=thetext
enabletip=true
return false
}
}

function positiontip(e){
if (enabletip){
  var curX= event.clientX+ietruebody().scrollLeft;
  var curY=event.clientY+ietruebody().scrollTop;
//Find out how close the mouse is to the corner of the window
var rightedge=ie&&!window.opera? ietruebody().clientWidth-event.clientX-offsetxpoint : window.innerWidth-e.clientX-offsetxpoint-20
var bottomedge=ie&&!window.opera? ietruebody().clientHeight-event.clientY-offsetypoint : window.innerHeight-e.clientY-offsetypoint-20

var leftedge=(offsetxpoint<0)? offsetxpoint*(-1) : -1000

//if the horizontal distance isn't enough to accomodate the width of the context menu
if (rightedge<tipobj.offsetWidth)
//move the horizontal position of the menu to the left by it's width
tipobj.style.left=ie? ietruebody().scrollLeft+event.clientX-tipobj.offsetWidth+"px" : window.pageXOffset+e.clientX-tipobj.offsetWidth+"px"
else if (curX<leftedge)
tipobj.style.left="5px"
else
//position the horizontal position of the menu where the mouse is positioned
tipobj.style.left=curX+offsetxpoint+"px"

//same concept with the vertical position
if (bottomedge<tipobj.offsetHeight)
tipobj.style.top=ie? ietruebody().scrollTop+event.clientY-tipobj.offsetHeight-offsetypoint+"px" : window.pageYOffset+e.clientY-tipobj.offsetHeight-offsetypoint+"px"
else
tipobj.style.top=curY+offsetypoint+"px"
tipobj.style.visibility="visible"
}
}

function hideddrivetip(){
if (ie){
enabletip=false
tipobj.style.visibility="hidden"
tipobj.style.left="-1000px"
tipobj.style.backgroundColor=''
tipobj.style.width=''
}
}

document.onmousemove=positiontip

</script>
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaSoggetto">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="HIDDEN" name="redirigi" value="S">

    <input type="HIDDEN" name="<%=ICostantiJmsCode.CAMPO_DESCRIZIONE%>">
	<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Procedimento per Soggetto</font></td>
      </tr>
    </table>
<br>
<table  width=80% >
    <tr><td class="Titolo" >Tipo Ricerca</td></tr>
    <tr>
       <td class="c">
           Nell'Ufficio &nbsp;<input type="radio" name="tipoRicerche" value="ufficio" onClick="radioBase();">
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nel distretto &nbsp;<input type="radio" name="tipoRicerche" value="distretto" checked  onClick="radioBase();">
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;In altri distretti  &nbsp; <input type="radio" name="tipoRicerche" value="altriDistretti"  onClick="radioBase();">
       </td>
    </tr>
</table>
<div id="divAncheAlias" style="display:block; width: 100%;" >
    <table  width=80% >
        <tr><td class="l">Anche Alias&nbsp;<input type="checkbox" name="ancheAlias" value="ancheAlis" onClick="checkbox();"></td></tr>
    </table>
</div>
  <div id="divDistretto" style="display:none; width: 100%;" >
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l" width="160">Cognome</td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l">Nome</td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l">Data di nascita </td>
          <td class="l"><input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>"maxlength="4" size="4"<%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
      </tr>
       <tr>
        <td class="l">Comune di nascita</td>
        <td class="l">
          <input title="Comune di Nascita" value="" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" maxlength="30" size="30" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuni('LoadRicercaSoggetto','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>[0]');">
          <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr>
        <td class="l">Comune di nascita Estero</td>
        <td class="l">
          <input title="Comune di Nascita Estero" value="" type="text" name="<%=ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>" maxlength="30" size="30">
        </td>
      </tr>
     <tr>
        <td class="l">Stato di Nascita</td>
        <td class="L">
          <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
            <%= nazioni %>
          </select>
         </td>
      </tr>
</table>
<!-- div che viene nascosto se viene selezionato "Anche Alias" se è stato selezionato "Nell'Ufficio" o "Nel Distretto"-->
<div id="chkDivAncheAlias" style="display:block; width: 100%;" >
  <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l" width="160"><span id="<%= ICostantiSoggetto.CAMPO_PATERNITA %>">Paternità </span></td>
          <td class="l"><input title="Paternita" type="text" name="<%= ICostantiSoggetto.CAMPO_PATERNITA%>" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>">Cognome Madre </span></td>
          <td class="l"><input title="Cognome Madre" type="text" name="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>">Nome Madre </span></td>
          <td class="l"><input title="Nome Madre" type="text" name="<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>" size="30" maxlength="30"></td>
      </tr>
      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>">Atto Nascita</span></td>
        <td class="L">
          <input title="Atto di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>" maxlength="10" size="10">
        </td>
      </tr>
      <tr>
        <td class="l"><span id="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>">Codice CUI</span></td>
        <td class="L">
          <input title="Codice CUI" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>" maxlength="7" size="7">
        </td>
      </tr>
  </table>
</div>
<div id="tipoClassiDiv" style="display:block; width: 100%;" >
   <table cellspacing=2 cellpadding=2>
     <tr><td>&nbsp;</td></tr>
	 	 <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Pena Detentiva','yellow')" ONMOUSEOUT="hideddrivetip()">Classe I</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="1"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	     <td class="l" ONMOUSEOVER="ddrivetip('Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe II</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="2"></td>
	   </tr>
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Pena Sospesa','yellow')" ONMOUSEOUT="hideddrivetip()">Classe III</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="3"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	     <td class="l" ONMOUSEOVER="ddrivetip('Misura Sicurezza','yellow')" ONMOUSEOUT="hideddrivetip()">Classe IV</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="4"></td>
	   </tr>
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Persona Giuridica','yellow')" ONMOUSEOUT="hideddrivetip()">Classe V</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="5"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	     <td class="l" ONMOUSEOVER="ddrivetip('Giudice di Pace','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VI</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="6"></td>
	   </tr>
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Conversione Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VII</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="7"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	   </tr>	   
	
	 </table>
	 </div>
	 <table cellspacing=2 cellpadding=2>
	  <tr><td colspan="2">
          &nbsp;
        </td></tr>
      <tr>
        <td colspan="2">
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
  </table>
 </div>
<% 
//================================================================================
//SEZIONE ALTRE BDI
//================================================================================
%>
 <div id="divAltreBDI" style="display:none; width: 100%;" >
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">BDI di ricerca</td>
        <td valign="middle">
          <select name="<%=ICostantiJmsCode.CAMPO_CODICE%>"
          onChange="javascript:document.LoadRicercaSoggetto.<%=ICostantiJmsCode.CAMPO_DESCRIZIONE%>.value = document.LoadRicercaSoggetto.<%=ICostantiJmsCode.CAMPO_CODICE%>.options[document.LoadRicercaSoggetto.<%=ICostantiJmsCode.CAMPO_CODICE%>.options.selectedIndex].text">
            <option value="-">
              Tutte
            </option>
<%
            Iterator itx = ListaBDI.iterator();
            while ( itx.hasNext())
            {
             JmsCodeModel lCodBDI = (JmsCodeModel) itx.next();
%>
               <option value="<%=lCodBDI.getCodice()%>">
                 <%= lCodBDI.getDescrizione()%>
               </option>
           <%}%>
          </select>
          </td>
      </tr>

      <tr>
        <td class="l">Cognome <div id="sog1" style="display:none"><font class=obr>(*)</font></div></td>
        <td class="l"><input title="Cognome Soggetto" type="text" name="<%=ICostantiSoggetto.CAMPO_COGNOME%>" value="" size="30" maxlength="30" onFocus="javascript:VediOb(1);"></td>
      </tr>

      <tr>
        <td class="l">Nome <div id="sog2" style="display:none"><font class=obr>(*)</font></div></td>
        <td class="l"><input title="Nome Soggetto"  type="text" name="<%=ICostantiSoggetto.CAMPO_NOME%>" value="" size="30" maxlength="30" onFocus="javascript:VediOb(1);"></td>
      </tr>

       <tr>
        <td class="l">Comune di nascita <div id="sog3" style="display:none"><font class=obr>(*)</font></div></td>
        <td class="l">
          <input title="Comune di Nascita" value="" type="text" name="<%=ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA%>" maxlength="30" size="30" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuni('LoadRicercaSoggetto','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>[1]');">
          <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>
      <tr>

      <tr>
        <td class="l">Stato di Nascita </td>
        <td class="L">
          <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
            <%= nazioni %>
          </select>
         </td>
      </tr>
      <tr>
        <td class="l">Data di nascita <div id="sog4" style="display:none"><font class=obr>(*)</font></div></td>
          <td class="l"><input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA2%>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%>>
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA2 %>" maxlength="2" size="2" <%=IWebConstants.UTIL_DATA%> >
            /
            <input title="Data di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA2%>"maxlength="4" size="4" <%=IWebConstants.UTIL_DATA_ANNO%>>
          </td>
      </tr>
      <tr>
        <td class="l">Atto Nascita</td>
        <td class="L">
          <input title="Atto di nascita" type="text" name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>" maxlength="10" size="10">
        </td>
      </tr>
      <tr>
        <td class="l">Codice CUI <div id="cui" style="display:none"><font class=obr>(*)</font></div></td>
        <td class="L">
          <input title="Codice CUI" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>" maxlength="7" size="7" onFocus="javascript:VediOb(2);">
        </td>
      </tr>
	
     <tr><td>&nbsp;</td></tr>
       <tr>
        <td colspan="2">
          <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
    </table>
	</div>
 </form>
</body>

</html>