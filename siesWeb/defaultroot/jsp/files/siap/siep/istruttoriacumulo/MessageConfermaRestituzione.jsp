<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>

<jsp:useBean id="IdIstruttoriaCumulo" scope="request" class="java.lang.String"/>
<jsp:useBean id="idMess"		      scope="request" class="java.lang.String"/>
<jsp:useBean id="messaggiodiarrivoatti"     scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>

<%
String lUfficio = " - ";
String lSede = " - ";

String lAnno = " - ";
String lProg = " - ";

if(messaggiodiarrivoatti!=null && messaggiodiarrivoatti.getIdMessaggio()!=null)
{
	if(messaggiodiarrivoatti.getDescrUfficioMittente()!=null)
		lUfficio=messaggiodiarrivoatti.getDescrUfficioMittente();
	
	if(messaggiodiarrivoatti.getDescrSedeUfficioMittente()!=null)
		lSede=messaggiodiarrivoatti.getDescrSedeUfficioMittente();

	if(messaggiodiarrivoatti.getChiaveAnnoSiep()!=null)
		lAnno=messaggiodiarrivoatti.getChiaveAnnoSiep().toString();
	
	if(messaggiodiarrivoatti.getChiaveProgrSiep()!=null)
		lProg=messaggiodiarrivoatti.getChiaveProgrSiep().toString();
}


String lMessag  ="I dati del Procedimento "+lAnno+"/"+lProg+" sono stati eliminati dall'Istruttoria Cumulo. ";
String lMessag1 ="Si vuole procedere ad inviare un messaggio di Restituzione Atti a "+lUfficio+" di "+lSede+", titolare del Procedimento escluso? ";
String lMessag2 ="Si potrà comunque inviare il messaggio in un secondo momento, selezionando il procedimento dall'elenco presente in 'Iscrizione Titoli Pervenuti'.";

// Stabilisce la funzione da innescare dopo l'eventuale messaggio di restituzione Atti
String newPage  = (String)request.getAttribute(IWebConstants.GOTO_PAGE);

%>
<html>
  <head>
    <title>[S.I.E.S.] - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<SCRIPT LANGUAGE="JavaScript">
  function eseguiSubmit(operazione){
    if (operazione=='invia') {
      document.FormWarning.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.istruttoriacumulo.action.ActLoadRestituzioneFascicolo';
      document.FormWarning.submit();
    }
    else if  (operazione=='prosegui') {
      document.FormWarning.<%=IWebConstants.ACTION_FIELD%>.value = 'siap.siep.istruttoriacumulo.action.ActLoadElencoFascicoliCoinvolti';
      document.FormWarning.submit();
    }
  
  }
</SCRIPT>


</head>

<body class="corpo">
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="FormWarning">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>"  value="">
    <input type="HIDDEN" name="<%=IWebConstants.GOTO_PAGE%>"  value="<%=newPage%>">
    <input type="HIDDEN" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>"  value="<%=IdIstruttoriaCumulo%>">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=messaggiodiarrivoatti.getIdMessaggio()%>">
  
    <br><br><br><br><br>
    
    <table width="600"  cellspacing="0" align="center" class="tab" >
      <tr>
        <td class="c">
          <table width="600"  cellspacing="0" align="center" class="tab" >
            <tr align="center" valign="middle">
              <td align="center"  colspan="2"  class="tab" style="font-size:10pt">
                <p>&nbsp;<p>
                <B><%=lMessag%><br><%=lMessag1%><br><%=lMessag2%></B>
              </td>
            </tr>
            
            <tr><td><br></td></tr>

            <tr align="center" valign="middle">
              <td align="left"  colspan="2"  class="tab">
                <B></B>
              </td>
            </tr>
            
            <tr><td><br></td></tr>
            
            <tr align="center" valign="middle">
              <td align="center"  class="tab2" width="50%">
                <input type="button" class="bottone" name="I" value=" Invia Messaggio " onClick="eseguiSubmit('invia')">
              </td>
              <td align="center"  class="tab2" width="50%">
                <input type="button" class="bottone" name="P" value=" Prosegui " onClick="eseguiSubmit('prosegui')">
              </td>
            </tr>
            
            <tr><td><br></td></tr>
            
          </table>
        </td>
      </tr>
    </table>
    

   </FORM>
 </body>
</html>