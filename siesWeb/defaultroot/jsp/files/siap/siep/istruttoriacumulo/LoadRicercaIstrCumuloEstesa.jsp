<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>


<jsp:useBean id="uffici" scope="request" class="java.util.Vector"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="statoIstruttoriaCumulo" scope="request" class="java.lang.String"/>

<html>

<head>
  <title> [S.I.E.S.] - Ricerca Istruttoria Cumulo - </title>
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
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
  var desktop;
  
  function ListaUfficiPerTipo(a_formname, a_fieldname )
  {
    codTipoUfficio = document.c.CodTipoAutoritaEmittente.value;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
  
 function radioBase()
 {
  //console.log("radioBase");
	document.d.distrettoUffcio.value=document.d.distretto.value;

  if (   !document.g.tipo[0].checked
      && !document.g.tipo[1].checked
      && !document.g.tipo[2].checked
      && !document.g.tipo[3].checked
     ) 
  {
    document.g.tipo[0].checked=true;
  }

  radio();

	loadUfficiAccorpatiByDesc();
 }

 function radio()
 {
     var nodeIntervalloNumIstruttoria;
     var nodeIntervalloDataIscrizione;
     var nodeTitoloInCumulo;
     var nodeProcSiep;

     nodeIntervalloNumIstruttoria=document.getElementById('intervalloNumIstruttoria');
     nodeIntervalloDataIscrizione=document.getElementById('intervalloDataIscrizione');
     nodeTitoloInCumulo=document.getElementById('titoloInCumulo');
     nodeProcSiep=document.getElementById('procSiep');

     if(document.g.tipo[0].checked)
     {
       // pulisci();
       nodeIntervalloNumIstruttoria.style.visibility='visible';
       nodeIntervalloDataIscrizione.style.visibility='hidden';
       nodeTitoloInCumulo.style.visibility='hidden';
       nodeProcSiep.style.visibility='hidden';
       document.a.valoreRadio.value='0';

      document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.focus();

     } else if (document.g.tipo[1].checked) {
      // pulisci();
       nodeIntervalloNumIstruttoria.style.visibility='hidden';
       nodeIntervalloDataIscrizione.style.visibility='visible';
       nodeTitoloInCumulo.style.visibility='hidden';
       nodeProcSiep.style.visibility='hidden';
       document.b.valoreRadio.value='1';
       
       document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();

     } else if (document.g.tipo[2].checked ) {
       // pulisci();
        nodeIntervalloNumIstruttoria.style.visibility='hidden';
        nodeIntervalloDataIscrizione.style.visibility='hidden';
        nodeTitoloInCumulo.style.visibility='visible';
        nodeProcSiep.style.visibility='hidden';
        document.c.valoreRadio.value='2';
        
        document.c.<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();

     } else if (document.g.tipo[3].checked ) {
       // pulisci();
        nodeIntervalloNumIstruttoria.style.visibility='hidden';
        nodeIntervalloDataIscrizione.style.visibility='hidden';
        nodeTitoloInCumulo.style.visibility='hidden';
        nodeProcSiep.style.visibility='visible';
        document.d.valoreRadio.value='3';

        document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
        
     }
 }
 
  /* function pulisci()
  {
		 document.a.<!%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
         document.a.<!%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
         document.a.<!%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>.value='';

  } */

  function VerifyNumIstruttoria()
  {
 	if (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value=="" && document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.value=="") {
 		alert("I campi Anno e Numero Iniziale sono obbligatori");
 		document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.focus();
 		return false;
 	}
 	// Non è possibile specificare solo il numero o solo l'anno
 	if( (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.value.length != 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value.length == 0) )
 	{
 		alert("Valorizzare Anno inizio ricerca");
 		document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.focus();
 		return false;
 	}
 	if( (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.value.length == 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value.length != 0) )
 	{
 		alert("Valorizzare Numero inizio ricerca");
 		document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.focus();
 		return false;
 	}
 	if( (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>.value.length != 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.value.length == 0) )
 	{
 		alert("Valorizzare Anno di fine ricerca");
 		document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.focus();
 		return false;
 	}
 	if( (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>.value.length == 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.value.length != 0) )
 	{
 		alert("Valorizzare Numero di fine ricerca");
 		document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>.focus();
 		return false;
 	}
 	// Non è possibile cercare per numero/anno fine minore di numero/anno inizio
 	if( (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.value.length != 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value.length != 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>.value.length != 0)
 	 && (document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.value.length != 0) )
 	{
 		if(document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.value < document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value)
 		{
            alert("Anno inizio maggiore Anno fine");
            document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.focus();
            return false;
 		}
 		else if(document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>.value == document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.value)
 		{
            if(parseInt(document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>.value) < parseInt(document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.value))
            {
              alert("Numero inizio maggiore Numero fine");
              document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>.focus();
              return false;
            }
 		}
 	}
 }

 function VerifyDataIscrizione()
 {
     if(document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value=="" ||
	 	document.b.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value=="" ||
		document.b.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value=="") {
		alert("Il campo Data Iniziale è obbligatorio");
		document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
		return false;
	 }
	 var giornoF;
	 var meseF;
	 var annoF;
	 var dataSys = new Date();
	 var giornoI = document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value;
	 var meseI = document.b.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value;
	 var annoI = document.b.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
	 
	 if (document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value!="") {
	 	giornoF = document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value;
	 	meseF = document.b.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value;
	 	annoF = document.b.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
	 }
	 else {
	 	giornoF = dataSys.getDate();
		meseF = (dataSys.getMonth()+1);
		annoF = dataSys.getYear()+"";
	 }

	 if (annoF.length==4) {
	 	if (annoF-annoI > 5 || (annoF-annoI==5 && (meseF>meseI || (meseF==meseI && giornoF>giornoI)))) {

			alert("Il range fra le due date può essere al massimo di 5 anni");
			document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
			return false;
	 	}
	}
	 
	// Controllo che Data Iniziale non sia > di Data Odierna
	var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
	var data_Inizio = giornoI+'/'+meseI+'/'+annoI;
    if(!CompareDate(data_Inizio, data_od))
    {
        alert('Data Iniziale NON può essere superiore alla Data Odierna');
        document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
        return false;
    }
     
	// Controllo che Data Iniziale sia < di Data Finale
	var data_Fine = giornoF+'/'+meseF+'/'+annoF;
    if(!CompareDate(data_Inizio, data_Fine))
    {
     	alert('Data Finale DEVE essere superiore o uguale alla Data Iniziale');
     	document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.focus();
     	return false;
   	}

 }

 function VerifyTitoloCoinvolto()
 {
	if(document.c.<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value=="" ||
	   document.c.<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value=="" ||
	   document.c.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value=="") {
	   alert("Il campo Data Emissione è obbligatorio");
	   document.c.<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.focus();
	   return false;
	}
	
	if (document.c.CodTipoAutoritaEmittente[document.c.CodTipoAutoritaEmittente.selectedIndex].value=='-')
    {
		alert('Il campo Autorità Emittente è obbligatorio');
		return false;
	}
	if (document.c.CodLuogoEmittente.value=='')
	{
		alert('Il campo Luogo Autorità Emittente è obbligatorio');
		return false;
	}
	
	return true;
 }
 
 function VerifyProcSiep()
 {
  if (document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value=="" ) {
       alert("Anno è obbligatorio");
       document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
  		return false;
  }
  if (document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value=="" )  {
       alert("Progressivo è obbligatorio");
       document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.focus();
  			 return false;
  }
  
  if (document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="" )  {
       alert("L' Ufficio è obbligatorio");
       document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.focus();
			 return false;
  }
  if (document.d.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value=="")  {
       alert('Il campo Sede Ufficio è obbligatorio');
       document.d.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.focus();
			 return false;
  }

  var numProg = document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value;
  var offSet = document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
  var newProg = parseInt(numProg) + parseInt(offSet);
  document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = newProg;
  //resetUfficiAccorpati();

  return true;
 }
  

 function checkNewProg()
 {
     var ufficioAccorpato = document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
     var parts=ufficioAccorpato.split("-"); 
     var offSetInt = parseInt(parts[0]);

     var numProgIni = document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>.value;
     var numProgIniInt = 0;
     if (numProgIni){
     	numProgIniInt = parseInt(numProgIni);
         }
     var newProgIniInt = numProgIniInt + offSetInt;
     if (newProgIniInt>0){
         document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = newProgIniInt;
         }

     var numProgFin = document.a.<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>.value;
     var numProgFinInt = 0;
     if (numProgFin){
     	numProgFinInt = parseInt(numProgFin);
         }
     var newProgFinInt = numProgFinInt + offSetInt;
     if (newProgFinInt>0){
         document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = newProgFinInt;
         }

     return true;
  }


<%UfficioModel lUffMod = (UfficioModel)uffici.get(0);%>

function ListaUfficiDistretto(a_formname,a_fieldname,a_fieldname2)
{
  var TipoUff = document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value;

 desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaComunePerDistretto&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&codTipoUff="+TipoUff, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
{
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}
function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
{
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaUffici(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaDistretti(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ChoosePopup()
{
    var selectTipoUfficio = document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>;
    var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
    var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
    if (codTipoUfficio == 'PM'){
  	  ListaUffici('d','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
     } else if (codTipoUfficio == 'PGCAP'){
   	  ListaDistretti('d','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
     }
}

function ResetField()
{
    document.d.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value="";
    var selectUfficioAccorpato = document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>;
    selectUfficioAccorpato.options.length = 0;
    selectUfficioAccorpato.options[selectUfficioAccorpato.options.length] = new Option("-", "0");
}

  </script>
</head>

<body class="corpo" onLoad="radioBase();">

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

<form name="f">
  <table>
    <tr>
   
    <td class="LBG"><a href="Javascript:window.print();">
    <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    </td>
    
     <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Istruttoria Cumulo</font></td>
    </tr>
  </table>

</form>

<form name="g">
  <table width=100%>
    <tr><td class="Titolo" colspan =4> Selezionare Tipo Ricerca</td>

    <tr>
     <td class="c">Intervallo Numero Istruttoria &nbsp;<input type="radio" name="tipo" value="intervalloNumIstruttoria"  onClick="radio();">
       &nbsp;&nbsp;Intervallo Date di Iscrizione &nbsp;<input type="radio" name="tipo" value="intervalloDataIscrizione"   onClick="radio();">
       &nbsp;&nbsp;Titolo Coinvolto in Cumulo &nbsp;<input type="radio" name="tipo" value="titoloInCumulo"  onClick="radio();">
       &nbsp;&nbsp;Procedimento SIEP Coinvolto in Cumulo &nbsp;<input type="radio" name="tipo" value="procSiep"  onClick="radio();"></td>
    </tr>
  </table>
</form>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="a">
<div id="intervalloNumIstruttoria" style="width:100%; visibility:visible; position:relative; top:0px; ">
   <input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaIstrCumuloEstesa">
   <input type="HIDDEN" name="valoreRadio" value="">
   <table>

    <tr><td class="Titolo" colspan=4>Intervallo Istruttorie</td><td class="Titolo"  colspan =1 >Stato</td></tr>
    <tr>
      <td class="L" >
        <font class="label">
          Anno/Numero Iniziale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Istruttoria Iniziale" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>" 
        					maxlength="4" size="4" onBlur="javascript:value=FillYear(value)" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Istruttoria Iniziale" name="<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>" 
        					maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="L">
        <font class="label">
          Anno/Numero Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Istruttoria Finale" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>" 
        					maxlength="4" size="4" onBlur="javascript:value=FillYear(value)" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Istruttoria Finale" name="<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>" 
        					maxlength="14" size="14" onkeypress="return TicTabNumField(this,event)">
      </td>
      <td class="c" >
         <select  Title="Stato Istruttoria" name="statoI">
         	<%=statoIstruttoriaCumulo%>
       	</select>
	  </td>
 	  <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca " onClick="javascript:return VerifyNumIstruttoria();">
      </td>
    </tr>
  </table>
</form>
</div>
<br><br>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="b">
<div id="intervalloDataIscrizione" style="visibility:hidden; position:relative; top:-110px;" >
<input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaIstrCumuloEstesa">
<input type="HIDDEN" name="valoreRadio" value="">
<table>
	<tr>
       	<td class="Titolo">Intervallo Date di Iscrizione</td>
       	<td class="Titolo">Stato</td>
	</tr>
	<tr>
      <td class="L">
        <font class="label">
          Data Iniziale&nbsp;
        </font>
      	<font class="l">
        	<input type="text" title="Giorno Iscrizione Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Mese Iscrizione Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Anno Iscrizione Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </font>
        <font class="label">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Data Finale&nbsp;
        </font>
        <font class="l">
        	<input type="text" title="Giorno Iscrizione Finale" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Mese Iscrizione Finale" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input type="text" title="Anno Iscrizione Finale" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </font>
      </td>
      <td class="c" >
         <select  Title="Stato Istruttoria" name="statoD">
         	<%=statoIstruttoriaCumulo%>
       	</select>
	  </td>
      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyDataIscrizione();">
      </td>
    </tr>
  </table>
</form>
</div>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
<div id="titoloInCumulo" style="width:100%; visibility:hidden; position:relative; top:-178px;" >
<input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaIstrCumuloEstesa">
<input type="HIDDEN" name="valoreRadio" value="">

<table>
    <tr>
    	<td class="Titolo" colspan="4">Titolo coinvolto in Cumulo </td>
    </tr>
    
	<tr>
	  <td class="L" width="20%">Tipo</td>
	  <td class="L" colspan=3>
	    <select name="<%=ICostantiTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
	      <%//=TipoProvv%>
	      <option value="-">-</option>
	      <option value="01">Sentenza</option>
	      <option value="02bis">Decreto Penale</option>
	      <option value="05">Sentenza (di riconoscimento di sentenza straniera)</option>
	      <option value="02">Decreto</option>
	      <option value="03">Ordinanza</option>
	    </select>
	  </td>
	</tr> 

    <tr>
	  <td class="L">Data emissione <font class=ob>(*)</font></td>
	  <td class="L">
        <input type="text" title="Giorno Emissione" name="<%=ICostantiTitoloCumulato.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Emissione" name="<%=ICostantiTitoloCumulato.CAMPO_MESE_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Emissione" name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_DATA_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="L" colspan=2> Anno/Numero &nbsp;&nbsp;
        <input type="text" title="Anno Provvedimento" name="<%=ICostantiTitoloCumulato.CAMPO_ANNO_PROVV_RIF %>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        /
        <input type="text" title="Numero Provvedimento" name="<%=ICostantiTitoloCumulato.CAMPO_NUMERO_PROVV_RIF %>" maxlength="6" size="6" onkeypress="return TicTabNumField(this,event)" >
      </td>
	</tr>
	
	<tr>
  <tr>
    <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
    <td class="L" colspan="3">
      <select Title="Autorità Emittente" name="CodTipoAutoritaEmittente"> 
        <option value = "-" selected />-
		<option value = "CAP"  />Corte D'Appello
		<option value = "CAS"  />Corte di Assise
		<option value = "CASAP"  />Corte di Assise di Appello
		<option value = "CAPMI"  />Corte Militare d'Appello
		<option value = "CAPMID"  />Corte Militare d'Appello - Sezione Distaccata
		<option value = "CSS"  />Corte Suprema di Cassazione
		<option value = "GIPM"  />Gip Presso il Tribunale per i Minorenni
		<option value = "GIP"  />Gip Presso il Tribunale Ordinario
		<option value = "GIPP"  />Gip presso Pretura
		<option value = "GIPPSD"  />Gip presso Sezione Distaccara della Pretura Circondariale
		<option value = "GIPMI"  />Gip presso Tribunale Militare
		<option value = "GP"  />Giudice di Pace
		<option value = "GUPM"  />Gup Presso il Tribunale per i Minorenni
		<option value = "GUPMI"  />Gup presso Tribunale Militare
		<option value = "GUP"  />Gup Presso Tribunale Ordinario
		<option value = "PT"  />Pretura
		<option value = "PTC"  />Pretura Circondariale
		<option value = "PTCSD"  />Sezione Distaccara della Pretura Circondariale
		<option value = "TRIBSD"  />Sezione Distaccata di Tribunale
		<option value = "CAPSM"  />Sezione Minorenni per la Corte di Appello
		<option value = "TMI"  />Tribunale Militare
		<option value = "DIB"  />Tribunale Ordinario
		<option value = "DIBM"  />Tribunale per i Minorenni

      </select>
    </td>
  </tr>
   
  <tr>
    <td class="l">Luogo Emittente <font class=ob>(*)</font></td>
    <td class="L" colspan="3">
      <input Title="Luogo Emittente" name="CodLuogoEmittente" value="" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('c','CodLuogoEmittente',document.c.CodTipoAutoritaEmittente[document.c.CodTipoAutoritaEmittente.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0> </a>
    </td>
    <td class="l" >
       <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyTitoloCoinvolto();">
    </td>
  </tr>
</table>
</form>
</div>

<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="d">
<div id="procSiep" style="visibility:hidden; position:relative; top:-336px;" >
<input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoriacumulo.action.ActRicercaIstrCumuloEstesa">
<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_FLAG_VALIDATO%>" value="S">
<input type="HIDDEN" name="valoreRadio" value="">

 <table cellpadding=2 cellspacing=2>
    <tr><td class="Titolo" colspan=2>Specifico Procedimento</td><td class="Titolo"  colspan =1 >Tipo Registro</td></tr>
    <tr>
      <td class="L"> Anno/Numero Procedimento <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="L">
        <input type="text" title="Anno Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" 
        		onBlur="javascript:value=FillYear(value)" onkeypress="return TicTabNumField(this,event)" >
        /
        <input type="text" title="Numero Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>" maxlength="14" size="14" 
        		onkeypress="return TicTabNumField(this,event)" >
        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" value="">
        &nbsp;&nbsp;<a href="Javascript:TrasformaRes('d','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>');">
       R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
       <a href="Javascript:TrasformaPret('d','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>','<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>');">
       P.T. <img src="/images/filefolder.gif" border=0></a>
      </td>
      <td class="c" >
      	<select  Title="tipo" name="valore">
      	<option value="SIEP">SIEP</option>
      	</select>
      </td>
    </tr>

    <tr>
         <td class="L">Ufficio <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td class="l">
          <select name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" onchange="Javascript:ResetField();">
          <option value="PM" >PROCURA REPUBBLICA PRESSO TRIBUNALE</option>
          <option value="PGCAP">PROCURA GENERALE PRESSO CORTE D'APPELLO</option>
          </select>
         </td>
      </tr>
     
      <tr>
       <td class="l">Sede <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="L">
       <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>" value="<%=descrComune%>" maxlength="35" size="35"  readonly="readonly">
<input type="hidden" Title="distrettoUffcio" name="distrettoUffcio" value="" size=35 >
       <%--a href="Javascript:ListaUfficiDistretto('d','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>','distrettoUffcio');"--%>
       <a href="Javascript:ChoosePopup();">
       <img src="/images/filefolder.gif" border=0></a>
       </td>
      </tr>

      <tr>
         <td class="L">Ufficio Accorpato</td>
         <td class="l">
         	<select name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>"  onchange="resetSede()">
         	<option value="0" >-</option>
         	</select>
         </td>
      </tr>

  </table>

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
	     </tr>
     <tr><td>&nbsp;</td></tr>
	   
     <tr><td>&nbsp;</td></tr>
   <tr>  
      <td colspan=2>
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca " onClick="javascript:return VerifyProcSiep();">
      </td>
    </tr>
  </table>
</form>
</div>

 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("a");

  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>","numeric","Il campo Numero Istruttoria Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_INIZIALE%>","maxlen=14","La lunghezza massima per il Numero Istruttoria è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>","numeric","Il campo Numero Istruttoria Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_NUMERO_PROTOCOLLO_FINALE%>","maxlen=14","La lunghezza massima per il Numero Istruttoria è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>","maxlen=4","La lunghezza massima per Anno Istruttoria è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_INIZIALE%>","minlen=4","La lunghezza minima per Anno Istruttoria è di 4 caratteri");

  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>","maxlen=4","La lunghezza massima per Anno Istruttoria è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiIstruttoriaCumulo.CAMPO_ANNO_PROTOCOLLO_FINALE%>","minlen=4","La lunghezza minima per Anno Istruttoria è di 4 caratteri");

</script>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("b");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>","lt=3000");
</script>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("d");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","numeric","Il campo Numero Procedimento può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric","Il campo Anno può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  
</script>

  <script type="text/javascript">
  var ufficiAccorpatiArray = new Array();

  <%
  Iterator uaIter = ufficiAccorpati.iterator();
  int uaIndice = 0;
  while (uaIter.hasNext())
  {
  	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
  %>
  ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>","<%=uaModel.getIncrProgressivo()%>","<%=uaModel.getCodUfficioNew()%>","<%=uaModel.getDescrizioneNewUfficio()%>"); 
  <%
  uaIndice ++;
  }
  %>

  function resetSede(){
		var ufficioAccorpatoSelect = document.d.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		var optionScelta = ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.selectedIndex];
        if (optionScelta.value!="0"){
    		//var sedeUfficio = document.d.<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>;
    		//sedeUfficio.value="";
    		var tipoUfficio = document.d.<%= ICostantiSiepJMS.CAMPO_TIPO_UFFICIO %>;
    		tipoUfficio.options[0].setAttribute("selected", "selected");
            }
  }

  function resetUfficiAccorpati(){
		var ufficioAccorpatoSelect = document.d.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		ufficioAccorpatoSelect.options[0].setAttribute("selected", "selected");
  }
  
  function loadUfficiAccorpati(codUfficio){
		var i=0;
		var ufficioAccorpatoSelect = document.d.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[2]==codUfficio){
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]);
			}
			i++;
		}
  }

  function loadUfficiAccorpatiByDesc(){
		var i=0;
		var ufficioAccorpatoSelect = document.d.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		var ufficioBaseDesc = document.d.<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>.value;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[3]==ufficioBaseDesc){
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]);
			}
			i++;
		}
  }
</script>

</body>
</html>