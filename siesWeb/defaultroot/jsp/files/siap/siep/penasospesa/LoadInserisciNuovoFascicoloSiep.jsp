<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="fascicolo"             scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="sentenza"             scope="session" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="Motivo"              	scope="request" class="java.lang.String"/>
<jsp:useBean id="DataArrivoAtto"  		scope="request" class="java.util.Date"/>
<jsp:useBean id="DataIrrevocabilita"  		scope="request" class="java.util.Date"/>
<jsp:useBean id="AnnotazioneMan"  	scope="session" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<html>
  <head>
    <title>[S.I.E.S.] - Gestione Sentenza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <style>
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
      
    function Verify()
    {
      /*
       if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_SENTENZA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_SENTENZA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_SENTENZA%>.value;
      
      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_SENTENZA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_SENTENZA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_SENTENZA%>.value;
      
      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value;
      
      if (document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value.length==1)
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value='0'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value;
      */  

/* inizio modifica 9/03/2009 */
      var d2=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>.value+
      '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>.value+
      '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>.value;

      if (! ControllaData(d2))
      {
        alert('Data Arrivo Atto non valida '+ d2);
        return false;
      }

      //Data Irrevocabilità
      var d3=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+
      '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+
      '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;

      //Data Iscrizione provvedimento
      var d4=document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>.value+
      '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>.value+
      '/'+document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>.value;
   
      if (! ControllaData(d3) && d3.length>2)
      {
        alert('Data Irrevocabilità non valida');
        return false;
      }
  
      if (!CompareDate(d2,d4))
      {
        alert('la Data iscrizione Procedimento deve essere successiva alla Data Arrivo Atto');
        return false;
      }

      if (!CompareDate(d3,d2))
      {
        alert('La Data Irrevocabilità deve essere precedente alla Data Arrivo Atto');
        return false;
      }
	 
	  var k=<%=DateUtils.getDateToString(sentenza.getDataProvvedimento(), "dd/MM/yyyy")%>;
	  var d5= d2; 
	  
      if (!CompareDate(d5,d2))
      {
        alert('Data Arrivo Atto deve essere successiva alla Data sentenza');
        return false;
      }       
/* fine modifica 9/03/2009 */
      
    }

    //==========================================
    //
    //==========================================
    function VisualizzaNumerazioneSpeciale()
    {
      var colonna = document.getElementById("numSpec");
      if(document.LoadInserisciFascicolo.checkNumSpec.checked == true)
      {
        colonna.style.display = "block";
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value="";
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value="";

        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.readOnly=true;
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.style.backgroundColor="C0C0C0";
        
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.readOnly=true;
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.style.backgroundColor="C0C0C0";
        document.LoadInserisciFascicolo.tipo[0].disabled=true;
        document.LoadInserisciFascicolo.tipo[1].disabled=true;
        document.LoadInserisciFascicolo.tipo[2].disabled=true;
        document.LoadInserisciFascicolo.tipo[3].disabled=true;
        document.LoadInserisciFascicolo.tipo[4].disabled=true;
        document.LoadInserisciFascicolo.tipo[5].disabled=true;
        document.LoadInserisciFascicolo.tipo[6].disabled=true;
      } 
      else 
      {
        colonna.style.display = "none";
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value="";
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value="";

        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.readOnly=false;
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.style.backgroundColor="FFFFFF";

        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.readOnly=false;
        document.LoadInserisciFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.style.backgroundColor="FFFFFF";
        document.LoadInserisciFascicolo.tipo[0].disabled=false;
        document.LoadInserisciFascicolo.tipo[0].checked=true;
        document.LoadInserisciFascicolo.tipo[1].disabled=false;
        document.LoadInserisciFascicolo.tipo[2].disabled=false;
        document.LoadInserisciFascicolo.tipo[3].disabled=false;
        document.LoadInserisciFascicolo.tipo[4].disabled=false;
        document.LoadInserisciFascicolo.tipo[5].disabled=false;
        document.LoadInserisciFascicolo.tipo[6].disabled=false;
      }
    }
 


    //=======================================================
    //
    //=======================================================
    function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    //=======================================================
    //
    //=======================================================
    function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

  </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        FascicoloSiepModel lFascicolo = new FascicoloSiepModel();
        Date lDataIscrizione = new Date();
        String lAction = new String();
      	// lAction = "siap.siep.penasospesa.action.ActInserisciFascicoloDaClasseIII";
      	lAction="";
 %>
      	<font class="campo">Inserimento Procedimento</font>
      </td>
    </tr>
  </table>

  <br>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenzaAttribuzione.jsp"/>
  <br>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penasospesa.action.ActInserisciFascicoloDaClasseIII&AnnotazioneMan=<%=AnnotazioneMan%>" name="LoadInserisciFascicolo">
  <table cellspacing=2 cellpadding=2>
	  <tr>
	      <td class="l">Data Iscrizione Procedimento</td>
	      <td class="l">
			      <%=DateUtils.getSysDate("dd") %>/<%=DateUtils.getSysDate("MM") %>/<%=DateUtils.getSysDate("yyyy") %>
			        <input type="hidden" value="<%=DateUtils.getDateToString(lDataIscrizione, "dd")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_ATTI%>">
			        <input type="hidden" value="<%=DateUtils.getDateToString(lDataIscrizione, "MM")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_ATTI%>">
			        <input type="hidden" value="<%=DateUtils.getDateToString(lDataIscrizione, "yyyy")%>" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_ATTI%>">
	      </td>
	  </tr>  
	  <tr>
	      <td class="l">Data Arrivo Atto</td>
	      <td class="l">
			        <input title="Data Arrivo Atto" value="<%=DateUtils.getDateToString(DataArrivoAtto, "dd")%>" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			        /
			        <input title="Data Arrivo Atto" value="<%=DateUtils.getDateToString(DataArrivoAtto, "MM")%>" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO%>"  maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			        /
			        <input title="Data Arrivo Atto" value="<%=DateUtils.getDateToString(DataArrivoAtto, "yyyy")%>" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	      </td>
	  </tr>    
	  <tr>
	      <td class="l">Data Irrevocabilità</td>
	      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	      <%-- 
	      <td class="L">               
			        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(DataIrrevocabilita, "dd")%>" name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			        /
			        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(DataIrrevocabilita, "MM")%>" name="<%=ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			        /
			        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(DataIrrevocabilita, "yyyy")%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
	      </td>
	       --%>
	      <td class="L">               
			        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "dd")%>" name="<%= ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			        /
			        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "MM")%>" name="<%= ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
			        /
			        <input title="Data Irrevocabilità" value="<%=DateUtils.getDateToString(fascicolo.getDataIrrevocabilita(), "yyyy")%>" name="<%= ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
	      </td>
	  </tr>      
  </table>
    
<div id="dhtmltooltip"></div>
<script type="text/javascript">

/***********************************************
* Cool DHTML tooltip script- © Dynamic Drive DHTML code library (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
***********************************************/

var offsetxpoint=-60 //Customize x offset of tooltip
var offsetypoint=-120 //Customize y offset of tooltip
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
<%
	String ck1="";
	String ck2="";
	String ck3="";
	String ck4="";
	String ck5="";
	String ck6="";
	String ck7="";

	ck1="checked";
%>
  <table cellspacing=2 cellpadding=2>
     <tr><td>&nbsp;</td></tr>
     <tr>
       <td class="l" ONMOUSEOVER="ddrivetip('Pena Detentiva','yellow')" ONMOUSEOUT="hideddrivetip()">Classe I</td>
       <td class="l"><input type="radio" name="tipoV" value="1" <%=ck1%> checked disabled></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l" ONMOUSEOVER="ddrivetip('Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe II</td>
       <td class="l"><input type="radio" name="tipoV" value="2" <%=ck2%> disabled></td>
     </tr>
     <tr>
       <td class="l" ONMOUSEOVER="ddrivetip('Pena Sospesa','yellow')" ONMOUSEOUT="hideddrivetip()">Classe III</td>
       <td class="l"><input type="radio" name="tipoV" value="3" <%=ck3%> disabled></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l" ONMOUSEOVER="ddrivetip('Misura Sicurezza','yellow')" ONMOUSEOUT="hideddrivetip()">Classe IV</td>
       <td class="l"><input type="radio" name="tipoV" value="4" disabled></td>
     </tr>
     <tr>
       <td class="l" ONMOUSEOVER="ddrivetip('Persona Giuridica','yellow')" ONMOUSEOUT="hideddrivetip()">Classe V</td>
       <td class="l"><input type="radio" name="tipoV" value="5" disabled></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td class="l" ONMOUSEOVER="ddrivetip('Giudice di Pace','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VI</td>
       <td class="l"><input type="radio" name="tipoV" value="6" disabled></td>
     </tr>
     <tr>
       <td class="l" ONMOUSEOVER="ddrivetip('Conversione Pena Pecuniaria','yellow')" ONMOUSEOUT="hideddrivetip()">Classe VII</td>
       <td class="l"><input type="radio" name="tipoV" value="7" disabled></td>
       <td>&nbsp;</td><td>&nbsp;</td>
       <td>&nbsp;</td><td>&nbsp;</td>      
     </tr>
     <tr><td>&nbsp;</td></tr>
  </table>
  
  <table>
    <tr>
      <td class="l">Note Procedimento</td>
      <td class="l">
        <textarea title="Note Procedimento" name="<%=ICostantiFascicoloSiep.CAMPO_NOTE%>" cols=40 rows=5><%=StringUtils.toStringJSP(lFascicolo.getNote())%></textarea>
    </tr>
    <tr>
      <td>
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(lFascicolo.getIdFascicoloSiep())%>">
  <input type="HIDDEN" name="tipo" value="1" >
  <input type="HIDDEN" name="Motivo" value="<%=Motivo%>" >
  
</form>
  </body>
</html>