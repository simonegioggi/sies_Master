<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.siep.jms.action.ICostantiSiepJMS" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="codFunzione"         scope="request" class="java.lang.String"/>

<%

//Controllo se il Titolo è stato passato nella request 
	String lTitolo = "Ricerca Titolo Esecutivo";
	if (titolo != null && titolo.length() > 0)
	{
		lTitolo = titolo;
	}
%>

<head>
  <title> [S.I.E.S.] - Ricerca Sentenza - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
	  function Verify()
	  {


      if (document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value;

      if (document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value;

			var data_inizio=document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>.value;
			var data_fine=document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>.value;



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
    //  if(data_inizio.length==2 || data_fine.length==2)
      // return true;

      if((data_inizio.length!=2 && data_fine.length!=2) && !CompareDate(data_inizio,data_fine))
      {
        alert('La Data di fine non può essere inferiore alla data di inizio');
        return false;
      }
/************************************************************************/
	  if (document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
      if (document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
  			document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
	  var data_provv=document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
      if(!ControllaDataPassaVuota(data_provv))
      {
        alert('Data del Titolo Esecutivo non valida');
        return false;
      }

/* Commentato per modifica marzo 2010
      if (document.f.<--%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.f.<--%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      if (document.f.<--%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
  			document.f.<--%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;

	  var data_irr=document.f.<--%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if(!ControllaDataPassaVuota(data_irr))
      {
        alert('Data di Irrevocabilità non valida');
        return false;
      }
	  if (document.f.<--%=ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>.value.length==1)
        document.f.<--%=ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>.value='0'+document.f.<--%=ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>.value;
      if (document.f.<--%=ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>.value.length==1)
  			document.f.<--%=ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>.value='0'+document.f.<--%=ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>.value;

      if (document.f.<--%=ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>.value.length==1)
        document.f.<--%=ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>.value='0'+document.f.<--%=ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>.value;
      if (document.f.<--%=ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>.value.length==1)
  			document.f.<--%=ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>.value='0'+document.f.<--%=ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>.value;

     	var data_inizio_irr=document.f.<--%=ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>.value+'/'+document.f.<--%=ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>.value+'/'+document.f.<--%=ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>.value;
		var data_fine_irr=document.f.<--%=ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>.value+'/'+document.f.<--%=ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>.value+'/'+document.f.<--%=ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>.value;

	  if(!ControllaDataPassaVuota(data_inizio_irr))
      {
        alert('Data di inizio Irrevocabilità non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine_irr))
      {
        alert('Data di fine Irrevocabilità non valida');
        return false;
      }

      if((data_inizio_irr.length!=2 && data_fine_irr.length!=2) && !CompareDate(data_inizio_irr,data_fine_irr))
      {
        alert('La Data di fine Irrevocabilità non può essere inferiore alla data di inizio Irrevocabilità');
        return false;
      }
*/

     return true;
    }
function radio()
   {
      var nodeProcedimento;
       var nodeirrevocabilita;
        var nodeData;
        var nodealtreBDI;

        nodeProcedimento=document.getElementById('numero');
//        nodeirrevocabilita=document.getElementById('irrevocabilita');
        nodeData=document.getElementById('data');
        nodealtreBDI=document.getElementById('altreBDI');

         //pulisci();
       // if(document.f.tipo[0].checked || document.f.tipo[1].checked  )
        //{
          nodeProcedimento.style.visibility='visible';
//          nodeirrevocabilita.style.visibility='visible';
          nodeData.style.visibility='visible';
         nodealtreBDI.style.visibility='hidden';
          //document.f.valoreRadio.value='0';

       /*}else
        {
          nodeProcedimento.style.visibility='visible';
          nodealtreBDI.style.visibility='visible';
          nodeirrevocabilita.style.visibility='hidden';
          nodeData.style.visibility='hidden';
          //nodeProcedimento.style.visibility='hidden';
          //document.f.valoreRadio.value='2';

        }*/
  document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.sentenza.action.ActRicercaSentenza";

      }
function pulisciDateIntervalloProvv()
 {
         document.f.<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>.value='';

 }
function pulisciDateIntervalloIrrev()
 {
 /*
         document.f.<--%=ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>.value='';
 */
 }
function pulisciDateIrrev()
 {
 /*
         document.f.<--%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='';
         document.f.<--%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value='';
 */
 }
function pulisciDateProvv()
 {
         document.f.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value='';
 }



function VerifyChiamate(id)
 {

   if(id==1)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
         pulisciDateProvv();
         pulisciDateIrrev();
         pulisciDateIntervalloIrrev();
         pulisciDateIntervalloProvv();



  }
   if(id==2)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
         pulisciDateProvv();
         pulisciDateIrrev();
         pulisciDateIntervalloIrrev();
         pulisciDateIntervalloProvv();


  }
 if(id==3)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
          pulisciDateProvv();
         pulisciDateIrrev();
         pulisciDateIntervalloIrrev();
         pulisciDateIntervalloProvv();


  }
  if(id==4)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
         pulisciDateIrrev();
         pulisciDateIntervalloIrrev();
         pulisciDateIntervalloProvv();


  }
if(id==5)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
         pulisciDateIrrev();
         pulisciDateProvv();
         pulisciDateIntervalloIrrev();

  }
if(id==6)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='default';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
         pulisciDateProvv();
         pulisciDateIntervalloProvv();
         pulisciDateIntervalloIrrev();


  }
if(id==7)
  {
         /* setta il parametro per il tipo di ricerca */
         document.f.<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>.value='OrdDataIrr';
         /* #### */
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>.value='';
         document.f.<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>.value='';
         pulisciDateProvv();
         pulisciDateIrrev();
         pulisciDateIntervalloProvv();


  }
 return Verify();

}
function VerifyAltreBDI(i)
	  {
    /*if (document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value.length == 1)
			{
       alert('Il campo Sede Ufficio è obbligatorio');
       document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.focus();
			 return false;
		  }*/
if(document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="" || document.f.<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>.value=="-" )
    {
        alert("L'uffico del distretto è obbligatorio");
        return false;

    }
    if(document.f.<%=ICostantiSiepJMS.CAMPO_SEDE_UFFICIO%>.value=="")
    {
        alert('La sede dell uffico del distretto è obbligatorio');
        return false;

    }

     if (i==1)
     {

       document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaSentenza";
       //document.RicercaEstesaFascicolo.submit();
     }
      if (i==2)
     {
       document.f.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.jms.action.ActRicercaEstesaSentenzaPerTrasferimento";
       //document.RicercaEstesaFascicolo.submit();
     }
    }
	
	function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

	function calendario(a_formname,a_field_year,a_field_month,a_field_day)
	{
	  desktop = 
	      window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
	}
  </script>
</head>

<body class="corpo" onload="radio();">
  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <%-- #### parametro per ricerca orderBy nel Db   --%>
   <input type="HIDDEN" name="<%=ICostantiSentenza.PARAMETRO_ORDINAMENTO%>" value="">
  <%-- #### --%>
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font>
      </td>
    </tr>
  </table>
<br>
 <!--table  width=100% >
    <tr>
    <tr><td class="Titolo" >Ambito Ricerca</td></tr>

       <td class="c">Ufficio &nbsp;<input type="radio" name="tipo" value="ufficio" checked  onClick="radio();">
                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Distretto  &nbsp; <input type="radio" name="tipo" value="distretto"  onClick="radio();">
                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Altri Distretti  &nbsp; <input type="radio" name="tipo" value="altridistretti"  onClick="radio();" >
       </td>
    </tr>
</table-->
<br>
  <div id="numero" style="visibility:hidden; position:relative; top:0px; width:100%;">
<table >
    <tr><td class="Titolo" colspan=2>Anno/Numero Titolo Esecutivo</td></tr>
</tr>
    <tr>
      <td class="L" > Anno/Numero Titolo Esecutivo</td>
      <td class="L">
        <input type="text" title="Anno Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>" maxlength="6" size="6">
      </td>

     <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(1);">
      </td>

 </tr>
<tr><td>&nbsp;</td></tr>

<tr><td class="Titolo"  colspan ="2" >Anno/Numero R.G.N.R.</td></tr>
<tr>
<td class="L"> Anno/Numero R.G.N.R.</td>
      <td class="L">
        <input type="text" title="Anno R.G.N.R." name="<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero R.G.N.R." name="<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>" maxlength="6" size="6">
      </td>

 <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(2);">
      </td>
</tr>
<tr><td>&nbsp;</td></tr>

<tr><td class="Titolo"  colspan ="2" >Anno/Numero Reg. Gen.</td><td class="Titolo"  colspan ="1" >Tipo Registro</td></TR>
<tr>
<td class="L"> Anno/Numero Reg.Gen.</td>
      <td class="L">
        <input type="text" title="Anno Reg. Gen." name="<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>" maxlength="4" size="4">
        /
        <input type="text" title="Numero Reg. Gen." name="<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>" maxlength="6" size="6">
      </td>
      <td class="c" >
         <select  Title="tipo" name="valore">
             <option value="gip">GIP</option>
             <option value="dib">DIB</option>
             <option value="cas">CAS</option>
             <option value="cap">CAP</option>
             <option value="casap">CASAP</option>
             <!-- MEV_66: aggiunte quattro nuove proprietà -->
             <option value="gup">GUP</option>
             <option value="gup">CAPSM</option>
        </select>
     </td>
      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(3);">
      </td>

    </tr>
  </table>

<br>

  </div>
  <div id="data" style="visibility:visible; position:relative; top:0px; width:100%;">
<table >
    <tr><td class="Titolo" colspan ="2" >Specifica Data Titolo Esecutivo</td>
    </tr>
    <tr>
      <td class="L" width="22%"> Data  </td>
      <td class="L" width="60%">
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Ricerca Titolo Esecutivo da Ricerche
	  if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020000)){
%>
			<a href="javascript:calendario('f','<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
      </td>

     <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(4);">
      </td>
   </tr>
</table >
<table >

<tr><td>&nbsp;</td></tr>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date Titolo Esecutivo</td></tr>
<tr>
       <td class="L" width="20%" >
        <font class="label">
          Data Iniziale
        </font>
      </td>
      <td class="l" >
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Ricerca Titolo Esecutivo da Ricerche
	  if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020000)){
%>
			<a href="javascript:calendario('f','<%=ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>

	  </td>
 	  <td class="L" >
        <font class="label">
          Data Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Giorno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Titolo Esecutivo" name="<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Ricerca Titolo Esecutivo da Ricerche
	  if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90020000)){
%>
			<a href="javascript:calendario('f','<%=ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
      </td>

      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(5);">
      </td>
    </tr>

  </table>
</div>
<br>
<br>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<!-- Commentato per modifica marzo 2010 -->
<%--  <div id="irrevocabilita" width="100%" style="visibility:visible; position:relative; top:0px;" >
<table >
    <tr><td class="Titolo" colspan ="2" >Specifica Data di Irrevocabilità</td>
    </tr>
    <tr>
      <td class="L"  width="18%"> Data </td>
      <td class="L" >
        <input type="text" title="Giorno Data di Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data di Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data di Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

     <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(6);">
      </td>
   </tr>
</table >
<table >

<tr><td>&nbsp;</td></tr>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date di Irrevocabilità</td></tr>
<tr>
       <td class="L" width="20%" >
        <font class="label">
          Data Iniziale
        </font>
      </td>
      <td class="l" >
        <input type="text" title="Giorno Data Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

</td>
 <td class="L" >
        <font class="label">
          Data Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Giorno Data Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Data Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Data Irrevocabilità" name="<%=ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

      <td class="l" >
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(7);">
      </td>
    </tr>

  </table>
</div> --%>

  <div id="altreBDI" style="visibility:hidden; position:absolute; top:+330px; width:100%;">

 <table cellpadding=2 cellspacing=2>
    <tr><td class="Titolo" colspan=4>Ufficio del Distretto </td></tr>
    <tr>
         <td class="L">Ufficio <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td class="L">
             <select Title="Autorita Esterna" name="<%=ICostantiSiepJMS.CAMPO_TIPO_UFFICIO%>" >
               <%=autoritaEsterna%>
             </select>
             </td>
      </tr>
      <tr>
       <tr>
       <td class="l">Sede <font class=ob>(*)</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="L">
       <input title="Sede Autorita Esterna"  type="text" name="<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('f','<%= ICostantiSiepJMS.CAMPO_SEDE_UFFICIO %>');">
       <img src="/images/filefolder.gif" border=0></a></td>
   </tr>
   <tr>
      <td> &nbsp;&nbsp; </td>
   </tr>
     <tr>
      <td>
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyAltreBDI(1);" >
      </td>


      <td>
        <input class="bottone" type="submit" name="RICERCA" value="Ricerca Per Trasferimento" onClick="javascript:return VerifyAltreBDI(2);">
      </td>
    </tr>
  </table>
</div>

  <br>
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  <%--table cellspacing=2 cellpadding=2>
		<tr>
      <td class="l">Dalla data Titolo Esecutivo (gg-mm-aaaa) </td>
      <td class="l">
        <input Title="Da data Titolo Esecutivo" type="text" name="<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO %>" maxlength="2" size="2">
         -
        <input Title="Da data Titolo Esecutivo" type="text" name="<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO %>" maxlength="2" size="2"  >
         -
        <input Title="Da data Titolo Esecutivo" type="text" name="<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO %>" maxlength="4" size="4" >
      </td>
		</tr>
		<tr>
      <td class="l">Alla data Titolo Esecutivo (gg-mm-aaaa) </td>
      <td class="l">
        <input Title="Alla data Titolo Esecutivo" type="text" name="<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO %>" maxlength="2" size="2">
         -
        <input Title="Alla data Titolo Esecutivo" type="text" name="<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO %>" maxlength="2" size="2"  >
         -
        <input Title="Alla data Titolo Esecutivo" type="text" name="<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO %>" maxlength="4" size="4" >
      </td>
		</tr>
    <tr>
      <td colspan="2">
        <br><br>
        <input onclick="Javascript:return Verify();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
      </td>
    </tr>
 </table--%>

</form>
<script language="JavaScript" type="text/javascript">

 var frmvalidator  = new Validator("f");
// if(document.f.tipo[0].checked || document.f.tipo[1].checked  )
//{

  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","maxlen=6","La lunghezza massima per il Numero Titolo Esecutivo è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>","maxlen=6","La lunghezza massima per il Numero Reg. Gen. è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGISTRO_GENERALE%>","numeric");

 frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","maxlen=6","La lunghezza massima per il Numero R.G.N.R. è di 6 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");

 frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>","maxlen=4","La lunghezza massima per l'Anno Reg. Gen. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>","minlen=4","La lunghezza minima per l'Anno Reg. Gen. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGISTRO_GENERALE%>","numeric");

 frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","maxlen=4","La lunghezza massima per l'Anno R.G.N.R. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","minlen=4","La lunghezza minima per l'Anno R.G.N.R. è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_GIORNO_PROVVEDIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_MESE_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_DA_ANNO_PROVVEDIMENTO%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_GIORNO_PROVVEDIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_MESE_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_A_ANNO_PROVVEDIMENTO%>","lt=3000");
/* ********************************************

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il giorno  è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","lt=31");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","maxlen=2","La lunghezza massima per il mese  è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","maxlen=4","La lunghezza massima per l'anno  è di 4 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","lt=3000");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il giorno di inizio Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","lt=31");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il mese di inizio Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_MESE_IRREVOCABILITA%>","lt=12");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>","maxlen=4","La lunghezza massima per l'anno di inizio Irrevocabilità è di 4 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>","gt=1900");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_ANNO_IRREVOCABILITA%>","lt=3000");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il giorno di fine Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_GIORNO_IRREVOCABILITA%>","lt=31");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il mese di fine Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_MESE_IRREVOCABILITA%>","lt=12");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>","maxlen=4","La lunghezza massima per l'anno di fine Irrevocabilità è di 4 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>","gt=1900");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_A_ANNO_IRREVOCABILITA%>","lt=3000");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il giorno di inizio Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_DA_GIORNO_IRREVOCABILITA%>","lt=31");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il giorno di  Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>","lt=31");

  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","maxlen=2","La lunghezza massima per il mese di  Irrevocabilità è di 2 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","gt=1");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>","lt=12");

 frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","maxlen=4","La lunghezza massima per l'anno di  Irrevocabilità è di 4 caratteri");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","numeric");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","gt=1900");
  frmvalidator.addValidation("<--%= ICostantiSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>","lt=3000");
*/
//}

  </script>
</body>

</html>