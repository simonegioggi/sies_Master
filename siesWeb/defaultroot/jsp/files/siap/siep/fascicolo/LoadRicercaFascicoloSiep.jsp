<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="java.util.*"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="ufficioSIEP" scope="request" class="java.lang.String"/>
<jsp:useBean id="uffici" scope="request" class="java.util.Vector"/>
<jsp:useBean id="distretto" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrComune" scope="request" class="java.lang.String"/>
<jsp:useBean id="ricercaEstesa" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />
<jsp:useBean id="codFunzione" scope="request" class="java.lang.String"/>

<html>

<head>
  <title> [S.I.E.S.] - Ricerca Procedimento - </title>
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
    function VerifyA()
	  {
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno inizio ricerca");
        return false;
      }
      if( (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero inizio ricerca");
        return false;
      }
      if( (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno di fine ricerca");
        return false;
      }
      if( (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero di fine ricerca");
        return false;
      }
      // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
      if( (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        if(document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {
          alert("Anno inizio maggiore Anno fine");
          return false;
        }
        else if(document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {

          if(parseInt(document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value) < parseInt(document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value))
          {
            alert("Numero inizio maggiore Numero fine");
            return false;
          }
        }
      }
      return true;
	}

    function VerifyC()
	{
    if (document.c.<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='0'+document.c.<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value;
    if (document.c.<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='0'+document.c.<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value;

    if (document.c.<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='0'+document.c.<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value;
    if (document.c.<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value.length==1)
        document.c.<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='0'+document.c.<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value;

    var data_inizio=document.c.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
    var data_fine=document.c.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }

      if(data_inizio.length==2 || data_fine.length==2)
       return true;

      if(!CompareDate(data_inizio,data_fine))
      {
        alert('La Data di fine non può essere inferiore alla data di inizio');
        return false;
      }
        return true;
	}

 function radioBase()
   {
        var nodeIntervallo;
        var nodeDescIntervallo;
        var nodeData;
        var nodedivDesc;
        var nodealtreBDI;

        document.d.distrettoUffcio.value=document.d.distretto.value;
        nodeIntervallo=document.getElementById('intervallo');
        nodedivDesc=document.getElementById('divDesc');
        nodeData=document.getElementById('data');
        nodeDataIntervallo=document.getElementById('dataIntervallo');
        nodealtreBDI=document.getElementById('altreBDI');

        if(document.f.tipoRicerche[0].checked)
        {

        if(document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>[0].value == document.f.TipoUfficioUtenteConnesso.value)
          {
                  document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>[0].selected = true;
          }
         if(document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>[1].value == document.f.TipoUfficioUtenteConnesso.value)
          {
                 document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>[1].selected = true;
          }

          //nodeProcedimento.style.visibility='visible';
         nodeIntervallo.style.visibility='hidden';
         nodedivDesc.style.visibility='hidden';
         nodeData.style.visibility='hidden';
         nodeDataIntervallo.style.visibility='hidden';
         nodealtreBDI.style.visibility='visible';

         document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();

         // document.f.<!%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.fascicolo.action.ActAzioneChiamanteRicercaFascicolo";

         }
        else if (document.f.tipoRicerche[1].checked )
        {


         document.g.tipo[1].checked=true;
         document.g.tipo[0].checked=true;
         radio();
         // nodeProcedimento.style.visibility='hidden';
        // nodeIntervallo.style.visibility='hidden';
         nodedivDesc.style.visibility='visible';
         nodeData.style.visibility='hidden';
         nodeDataIntervallo.style.visibility='hidden';
         nodealtreBDI.style.visibility='hidden';
         // document.f.<!%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.fascicolo.action.ActRicercaFascicolo";

        }
        loadUfficiAccorpatiByDesc();
      }

 function radio()
   {
        var nodeIntervallo;
        var nodeData;
        var nodeDataIntervallo;



        nodeIntervallo=document.getElementById('intervallo');
        nodeData=document.getElementById('Data');
        nodeDataIntervallo=document.getElementById('dataIntervallo');


        if(document.g.tipo[0].checked)
        {
          // pulisci();
          // nodeProcedimento.style.visibility='visible';
          nodeIntervallo.style.visibility='visible';
          nodeData.style.visibility='hidden';
          nodeDataIntervallo.style.visibility='hidden';
          document.f.valoreRadio.value='0';

         document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();

         }
        else if (document.g.tipo[1].checked )
        {
         // pulisci();
         // nodeProcedimento.style.visibility='hidden';
          nodeIntervallo.style.visibility='hidden';
          nodeData.style.visibility='visible';
          nodeDataIntervallo.style.visibility='hidden';
          document.f.valoreRadio.value='1';


        }else
        {
          // pulisci();
          nodeIntervallo.style.visibility='hidden';
          nodeData.style.visibility='hidden';
          nodeDataIntervallo.style.visibility='visible';
          document.f.valoreRadio.value='2';


        }
      }
  /* function pulisci()
  {
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
          document.f.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
         document.f.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
 //        document.f.<!%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value='';
  //       document.f.<!%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value='';
  } */

 function VerifyAltreBDI(i)
	  {
  if (document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value=="" )
			{
       alert("Anno è obbligatorio");
       document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
			 return false;
		  }
  if (document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value=="" )
			{
       alert("Progressivo è obbligatorio");
       document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.focus();
			 return false;
		  }
       if (document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="" )
			{
       alert("L' Ufficio è obbligatorio");
       document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.focus();
			 return false;
		  }
    if (document.d.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value=="")
			{
       alert('Il campo Sede Ufficio è obbligatorio');
       document.d.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.focus();
			 return false;
		  }

    /* if(document.f.distrettoUffcio.value == document.f.distretto.value)
         {
          document.f.<!%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.fascicolo.action.ActRicercaFascicolo";

          }else
          {
          document.f.<!%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaFascicoloPerTrasferimento";

          }*/

          var numProg = document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value;
          var offSet = document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
          var newProg = parseInt(numProg) + parseInt(offSet);
          document.d.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = newProg;
          //resetUfficiAccorpati();

      return true;

    }

 function checkNewProg()
 {
     var ufficioAccorpato = document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
     var parts=ufficioAccorpato.split("-"); 
     var offSetInt = parseInt(parts[0]);

     var numProgIni = document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value;
     var numProgIniInt = 0;
     if (numProgIni){
     	numProgIniInt = parseInt(numProgIni);
         }
     var newProgIniInt = numProgIniInt + offSetInt;
     if (newProgIniInt>0){
         document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = newProgIniInt;
         }

     var numProgFin = document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value;
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

 function VerifyChiamate(id)
 {
   if(id==1)
  {
         if (document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value=="" && document.a.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value=="") {
		 	alert("I campi Anno e Numero Iniziale sono obbligatori");
			return false;
		 }
         checkNewProg();
		 /* document.b.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
          document.c.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
       */
	   }
   if(id==2)
  {
         if (document.b.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>.value=="" ||
		 	document.b.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>.value=="" ||
			document.b.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>.value=="") {

			alert("Il campo Data è obbligatorio");
			return false;
		 }
		 /* document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
          document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
          document.c.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
         document.c.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
 */
  }
  if(id==3)
 {
         if(document.c.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value=="" ||
		 	document.c.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value=="" ||
			document.c.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value=="") {

			alert("Il campo Data Iniziale è obbligatorio");
			return false;
		 }
		 var giornoF;
		 var meseF;
		 var annoF;
		 var dataSys = new Date();
		 var giornoI = document.c.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value;
		 var meseI = document.c.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value;
		 var annoI = document.c.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
		 if (document.c.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value!="") {
		 	giornoF = document.c.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value;
		 	meseF = document.c.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE%>.value;
		 	annoF = document.c.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
		 }
		 else {
		 	giornoF = dataSys.getDate();
			meseF = (dataSys.getMonth()+1);
			annoF = dataSys.getYear()+"";
		 }

		 if (annoF.length==4) {
		 	if (annoF-annoI > 1 || (annoF-annoI==1 && (meseF>meseI || (meseF==meseI && giornoF>giornoI)))) {

				alert("Il range fra le due date può essere al massimo di un anno");
				return false;
		 	}
		}
		  /* document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
          document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
         document.a.<!%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
             document.b.<!%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>.value='';
         document.b.<!%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>.value='';
      */
	  }
  return true;
 }

<%UfficioModel lUffMod = (UfficioModel)uffici.get(0);%>

function ListaUfficiDistretto(a_formname,a_fieldname,a_fieldname2) {
  var TipoUff = document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value;
 desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaComunePerDistretto&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&codTipoUff="+TipoUff, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function TrasformaRes(a_formname,a_fieldname,a_fieldname2) {
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function TrasformaPret(a_formname,a_fieldname,a_fieldname2) {
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
}

function ListaUffici(a_formname,a_fieldname) {
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaDistretti(a_formname,a_fieldname) {
  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ListaUfficiMinor(a_formname,a_fieldname) {
	// desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname+"&minor=yes", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadUfficiMinorDistretto&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}

function ChoosePopup() {
    var selectTipoUfficio = document.d.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>;
    var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
    var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
    if (codTipoUfficio == 'PM'){
    	ListaUffici('d','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
    } else if (codTipoUfficio == 'PMM'){
    	ListaUfficiMinor('d','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
    } else if (codTipoUfficio == 'PGCAP'){
    	ListaDistretti('d','<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>');
    }
}

function ResetField() {
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
 <input type="HIDDEN" name="valoreRadio" value="">
 <input type="HIDDEN" name="TipoUfficioUtenteConnesso" value="<%=tipoUfficioUtenteConnesso%>">


  <table>
    <tr>
   
    <td class="LBG"><a href="Javascript:window.print();">
    <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
    </td>
    
     <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento</font></td>
    </tr>
  </table>

  <table  width=100% <% if (ricercaEstesa.compareToIgnoreCase("NO") == 0) {%>  style="visibility:hidden;"  <%}%> >
    <tr>
    	<td class="Titolo" >Tipo Ricerca</td>
    </tr>

    <tr>
       <td class="c">
       Base &nbsp;<input type="radio" name="tipoRicerche" value="base" checked  onClick="radioBase();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       Avanzata  &nbsp; <input type="radio" name="tipoRicerche" value="avanzata"  onClick="radioBase();">
       </td>
    </tr>
   
    <tr> <br></tr>
</table>
</form>

<div id="divDesc" style="width: 100%; visibility:hidden; position:relative; top:0px; " >
  <form name="g">
<table  width=100%>
    <tr><td class="Titolo" colspan ="2" >Ricerca valida solo nell'Ufficio</td>

  <tr>
   <td class="c">Intervallo Numero Procedimenti &nbsp;<input type="radio" name="tipo" value="descIntervallo"  onClick="radio();">
     &nbsp;&nbsp;Data Iscrizione &nbsp;<input type="radio" name="tipo" value="descData"  onClick="radio();">
    &nbsp;&nbsp;Intervallo Date di  Iscrizione &nbsp;<input type="radio" name="tipo" value="descIntervalloData"   onClick="radio();"></td>

</tr>
</table >
</form>
</div>

<div id="intervallo" style="width: 100%; visibility:visible; position:relative; top:0px; " >
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="a">
    <input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActRicercaFascicolo">

    <table>
      <tr><td class="Titolo" colspan=4>Intervallo Procedimenti</td><td class="Titolo"  colspan ="1" >Tipo Registro</td></tr>
      <tr>
        <td class="L" >
          <font class="label">
            Anno/Numero Iniziale
          </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
          /
          <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>" maxlength="14" size="14">
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" value="">
        </td>
        <td class="L">
          <font class="label">
            Anno/Numero Finale
          </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
          /
          <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>" maxlength="14" size="14">
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" value="">
        </td>
         <td class="c" >
                <select  Title="tipo" name="valore">
                 <option value="SIEP">SIEP</option>
               </select>
        </td>
        <td class="l" >
          <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(1);">
        </td>
      </tr>

      <tr>
        <td class="L" >
          <font class="label">
            Ufficio Accorpato
          </font>
        </td>
        <td class="l">
            <select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>">
            <option value="0" >-</option>
            <%
            Iterator it = elencoUfficiAccorpati.iterator();
            int indice = 0;
            while (it.hasNext())
            {
              UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
            %>
                    <option value="<%=ua.getIncrProgressivo()%>-<%=ua.getCodUfficio()%>" ><%=ua.getDescrizione()%></option>
            <%
            indice ++;
            }
            %>
            </select>
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
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
	     <td>&nbsp;</td><td>&nbsp;</td>
	   </tr>	   
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Registro Istanze','yellow')" ONMOUSEOUT="hideddrivetip()">Registro Istanze</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="9"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	   </tr>	   
	  </table>
</form>
<br><br>
</div>

<div id="data" style="width: 100%; visibility:hidden; position:relative; top:-267px;" >
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="b">
<input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActRicercaFascicolo">

<table>
    <tr>
    	<td class="Titolo">Specifica Data di Iscrizione</td>
        <td class="Titolo">Tipo Registro</td>
    </tr>
    <tr>
      <td class="L" > Data &nbsp;&nbsp;&nbsp;
        <input type="text" title="Giorno Iscrizione" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Iscrizione" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Iscrizione" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="c" >
          <select  Title="tipo" name="valore">
           <option value="SIEP">SIEP</option>            
         </select>
     </td>
     <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(2);">
      </td>
   </tr>
</table >

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
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Registro Istanze','yellow')" ONMOUSEOUT="hideddrivetip()">Registro Istanze</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="9"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	   </tr>	   
	   </table>
</form>
</div>

<div id="dataIntervallo" style="width: 100%; visibility:hidden; position:relative; top:-496px;">
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
<input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActRicercaFascicolo">
<table>
	<tr>
       	<td class="Titolo">Intervallo Date di Iscrizione</td>
       	<td class="Titolo">Tipo Registro</td>
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
        <select  Title="tipo" name="valore">
         <option value="SIEP">SIEP</option>
         </select>
     </td>
      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(3);">
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
	     <td>&nbsp;</td><td>&nbsp;</td>
	   </tr>	   
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Registro Istanze','yellow')" ONMOUSEOUT="hideddrivetip()">Registro Istanze</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="9"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	   </tr>	   
	   </table>
</form>
</div>

<div id="altreBDI" style="width: 100%; visibility:hidden; position:absolute; top:100px; " >
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="d">
<input type="HIDDEN" name="distretto" value="<%=lUffMod.getCodDistretto()%>">
<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActAzioneChiamanteRicercaFascicolo">
<input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_FLAG_VALIDATO%>" value="S">

 <table cellpadding=2 cellspacing=2>
    <tr><td>&nbsp;</td><tr>
    <tr><td class="Titolo" colspan=2>Specifico Procedimento</td><td class="Titolo"  colspan ="1" >Tipo Registro</td></tr>
    <tr>
      <td class="L"> Anno/Numero Procedimento <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td class="L">
        <input type="text" title="Anno Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>" maxlength="14" size="14">
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

<%
Set<String> typePM = new HashSet<String>();
typePM.add("PM");
typePM.add("GIP");
typePM.add("DIB");
typePM.add("CAS");

Set<String> typePMM = new HashSet<String>();
typePMM.add("PMM");
typePMM.add("GIPM");
typePMM.add("DIBM");

Set<String> typePGCAP = new HashSet<String>();
typePGCAP.add("PGCAP");
typePGCAP.add("CAP");
typePGCAP.add("CAPSM");

String selected = "selected";
String selectPM = "";
String selectPMM = "";
String selectPGCAP = "";
if (typePM.contains(tipoUfficioUtenteConnesso)) {
	selectPM = selected;
} else if (typePMM.contains(tipoUfficioUtenteConnesso)){
	selectPMM = selected;
} else if (typePGCAP.contains(tipoUfficioUtenteConnesso)){
	selectPGCAP = selected;
}

// Modifica del 27/03/2017
// Quando la maschera viene richiamata dalla funzione "Ricerca Altre BDI" 
// (cod. 90111824) - SIGE i campi relativi all'ufficio non devono essere
// prevalorizzati con i dati dell'utente connesso
if( codFunzione != null && codFunzione.equals("90111824") ){
	selectPM = "";
	selectPMM = "";
	selectPGCAP = "";
}
%>
    <tr>
         <td class="L">Ufficio <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
         <td class="l">
          <select name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" onchange="Javascript:ResetField();">
          <option value="" >-</option>
          <option value="PM" <%=selectPM%>>PROCURA REPUBBLICA PRESSO TRIBUNALE</option>
          <option value="PMM" <%=selectPMM%>>PROCURA DELLA REPUBBLICA PRESSO IL TRIBUNALE PER I MINORENNI</option>
          <option value="PGCAP" <%=selectPGCAP%>>PROCURA GENERALE PRESSO CORTE D'APPELLO</option>
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

   <!--  </table>

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
	     <td class="l" ONMOUSEOVER="ddrivetip('Misura Sicurezza','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VI</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="4"></td>
	   </tr>
	   <tr>
	     <td class="l" ONMOUSEOVER="ddrivetip('Persona Giuridica','yellow')" ONMOUSEOUT="hideddrivetip()">Classe V</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="5"></td>
	     <td>&nbsp;</td><td>&nbsp;</td>
	     <td class="l" ONMOUSEOVER="ddrivetip('Giudice di Pace','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VI</td>
	     <td class="l"><input type="checkbox" name="tipoClasse" value="6"></td>
	   </tr>
     <tr><td>&nbsp;</td></tr>-->
   <tr>  
      <td colspan="2">
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca " onClick="javascript:return VerifyAltreBDI(1);">
      </td>
    </tr>
  </table>
</form>
</div>

 
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("a");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>","numeric","Il campo Numero Procedimento Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>","numeric","Il campo Numero Procedimento Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

  frmvalidator.setAddnlValidationFunction("VerifyA");
</script>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("b");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE%>","lt=3000");

</script>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("c");

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

  frmvalidator.setAddnlValidationFunction("VerifyC");
</script>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("d");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","numeric","Il campo Numero Procedimento può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","maxlen=14","La lunghezza massima per il Numero Procedimento è di 14 caratteri");

  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric","Il campo Anno può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  </script>

  <script language="JavaScript" type="text/javascript">
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

  function selectItemByValue(elmnt, value) {
	  for(var i=0; i < elmnt.options.length; i++) {
		  if(elmnt.options[i].value === value) {
			  elmnt.selectedIndex = i;
			  break;
			  }
		 }
	 }
  
  function resetSede(){
		var ufficioAccorpatoSelect = document.d.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		var optionScelta = ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.selectedIndex];
        if (optionScelta.value!="0"){
    		//var sedeUfficio = document.d.<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>;
    		//sedeUfficio.value="";
    		var tipoUfficio = document.d.<%= ICostantiSiepJMS.CAMPO_TIPO_UFFICIO %>;
    		// tipoUfficio.options[1].setAttribute("selected", "selected"); // opzione PM
    		selectItemByValue(tipoUfficio, "PM");
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
		var tipoUfficio = document.d.<%= ICostantiSiepJMS.CAMPO_TIPO_UFFICIO %>;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		if (tipoUfficio.value == "PM"){
			while(i<ufficiAccorpatiArray.length){
				var ufficio = ufficiAccorpatiArray[i];
				if (ufficio[3]==ufficioBaseDesc){
					ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0], ufficio[1]);
				}
				i++;
			}
		}
  }
</script>

</body>
</html>