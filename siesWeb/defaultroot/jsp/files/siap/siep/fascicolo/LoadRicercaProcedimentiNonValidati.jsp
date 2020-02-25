<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<html>
  <head>
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
    <title>[S.I.E.S.] - Ricerca Procedimenti Non Validati</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    
		  function Verify()
		  {
	      // Non è possibile specificare solo il numero o solo l'anno
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
	      {
	        alert("Valorizzare Anno inizio ricerca");
	        return false;
	      }
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
	      {
	        alert("Valorizzare Numero inizio ricerca");
	        return false;
	      }
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
	      {
	        alert("Valorizzare Anno di fine ricerca");
	        return false;
	      }
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
	      {
	        alert("Valorizzare Numero di fine ricerca");
	        return false;
	      }
	      
	      // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
	      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
	           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
	      {
	        if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
	        {
	          alert("Anno inizio maggiore Anno fine");
	          return false;
	        }
	        else if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
	        {
	
	          if(parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value) < parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value))
	          {
	            alert("Numero inizio maggiore Numero fine");
	            return false;
	          }
	        }
	      }
	      
      	return true;
		}
		</script>
  </head>

  <BODY class="corpo">

  <FORM method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActRicercaFascicoliNonValidati">
	  <table>
	    <tr>
	    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
	      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimenti Non Validati</font></td>
	    </tr>
	  </table>
	  <br>
	  <table cellpadding=2 cellspacing=2>
    <tr><td class="Titolo" colspan="4">Intervallo Procedimenti</td>
    <tr>
      <td class="L" >
        <font class="label">
          Anno/Numero Iniziale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="14" size="14">
      </td>
      <td class="L">
        <font class="label">
          Anno/Numero Finale
        </font>
      </td>
      <td class="l">
        <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="14" size="14">
      </td>
	   </tr>
		</table>
		<br>

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
	 
     <tr><td>&nbsp;</td></tr>
      <tr>
        <td colspan="2">
          <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>
  </table>
  </FORM>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>