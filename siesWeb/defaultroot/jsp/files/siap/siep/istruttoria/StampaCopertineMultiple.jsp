<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>

<jsp:useBean id="CampoChiaveProgrIniziale" scope="request" class="java.lang.String"/>
<jsp:useBean id="CampoChiaveAnnoIniziale" scope="request" class="java.lang.String"/>
<jsp:useBean id="CampoChiaveProgrFinale" scope="request" class="java.lang.String"/>
<jsp:useBean id="CampoChiaveAnnoFinale" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector"/>

<html>
  <head>
    <title> [S.I.E.S.] - Test sistema SIES - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD%>"></script>
    
    <script language="JavaScript">
    // accetta una stringa di testo non html da mostrare
function mostraAttesa(testo) 
{

    var puntini = 0,
    testoIntrattenimento = prendiElementoDaId("testo-temporaneo"),

    animaTesto = function() 
    {

      var testoAggiunto = "";

      for(var a = 0; a < puntini; a++)
        testoAggiunto += ".";

      testoIntrattenimento.nodeValue = testo + testoAggiunto;

      if(puntini < 4)
        puntini++;
      else
        puntini = 0;

      setTimeout(animaTesto, 300);
    }

  if(testoIntrattenimento.firstChild) 
  {

    animaTesto = function(){};
    testoIntrattenimento.removeChild(testoIntrattenimento.firstChild);
  }
  else 
  {

    testoIntrattenimento = document.createTextNode(testo);

    prendiElementoDaId("testo-temporaneo").appendChild(testoIntrattenimento);

    animaTesto();
  }
  
  stampa('/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActStampaMultiCopertine&CampoChiaveProgrIniziale=<%=CampoChiaveProgrIniziale %>&CampoChiaveAnnoIniziale=<%=CampoChiaveAnnoIniziale%>&CampoChiaveProgrFinale=<%=CampoChiaveProgrFinale%>&CampoChiaveAnnoFinale=<%=CampoChiaveAnnoFinale%>')
  
}


function stampaDoc()
{ 
  var url = "/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActStampaMultiCopertine&CampoChiaveProgrIniziale=<%=CampoChiaveProgrIniziale %>&CampoChiaveAnnoIniziale=<%=CampoChiaveAnnoIniziale%>&CampoChiaveProgrFinale=<%=CampoChiaveProgrFinale%>&CampoChiaveAnnoFinale=<%=CampoChiaveAnnoFinale%>";
  //alert (url);
  stampa('/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActStampaMultiCopertine&CampoChiaveProgrIniziale=<%=CampoChiaveProgrIniziale %>&CampoChiaveAnnoIniziale=<%=CampoChiaveAnnoIniziale%>&CampoChiaveProgrFinale=<%=CampoChiaveProgrFinale%>&CampoChiaveAnnoFinale=<%=CampoChiaveAnnoFinale%>');
}

function prendiElementoDaId(id_elemento) 
{
	var elemento;
	if(document.getElementById)
		elemento = document.getElementById(id_elemento);
	else
		elemento = document.all[id_elemento];
	return elemento;
}

    </script>
 </head>

<body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Stampa Copertine Multiple</font>
      </td>
      <td class="LBG">
      </td>
     </tr>
  </table>
 <div> 
 <br><br>
 <table width="50%">
  <tr>
    <td  width="40%" class="l">
     <table  ><tr><td><br><font class="label">
        Trovati  <b><font color="green"><%= fascicoli.size()%></font></b> procedimenti da stampare  
        dal procedimento <font color="green"><b><%=CampoChiaveAnnoIniziale%>/<%=CampoChiaveProgrIniziale %></b></font> a 
        <b><font color="green"><%=CampoChiaveAnnoFinale%>/<%=CampoChiaveProgrFinale%></font></b>.
       </font>
        </td></tr>
        <tr> <td class="l">
        </td>
     </tr>
     <tr><td ><font class="label">
      <br>
        L'elaborazione della stampa richiederà tempo, il tasto accanto di stampa aprirà una 
        nuova finestra.
        Si può continuare a lavorare normalmente anche mentre la stampa viene prodotta.</font><br>
   </td></tr>
   </table>
  </td>   
   
    <td width="10%" class="LBGISI">
<a href="Javascript:stampaDoc();">
<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>/PrintGrande.gif" alt="Stampa Copertine Multiple" width=64 border=3></a>
 
    </td>     
  </tr>
 </table>
 
  
</div>
 </body>
 
</html>