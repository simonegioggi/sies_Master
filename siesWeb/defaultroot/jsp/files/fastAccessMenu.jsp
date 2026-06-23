<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>

<!-- Paolo Cherubini 09/02/2012 per la b2/rr/007 mi serve verificare se il procedimento è in sessione  -->
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="FunRadiceMenuSceltaRapida" scope="session" class="f3b.security.model.FunctionModel" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />


<%
//==============================================================================
// JSP di visualizzazione del menù di scelta rapida
//==============================================================================
%>

<HTML>
<HEAD>

<META http-equiv=Content-Type content="text/html; charset=windows-1252">

<link rel="STYLESHEET" type="text/css" href="/css/menu.css">

<script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  var icontahelp = 0;
  function openHelp(ActionName)
  {
    var desktop;
   
    //==========================================================================
    //n.b. Sulla request viene passato un parametro fittizio icontahelp per 
    //     evitare che la pagina resti nella cache del browser o del Proxy.
    //     Infatti ci si è accorti che passando per il proxy la pagina restituita
    //     era sempre la stessa. Aggiungendo un parametro random si genera una
    //     GET sempre diversa per cui la pagina restituita non viene "meccia" mai 
    //     con quella nella cache.
    //==========================================================================
    var icontahelp=Math.random();
    
    desktop=window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>="+ActionName+"&ContaHelp="+ icontahelp, "HelpOnLine", "toolbar=no,location=yes,status=no,menubar=no,resizable=yes,scrollbars=yes,top=0,left=0");
    desktop.window.resizeTo(screen.availWidth,screen.availHeight);
    desktop.focus();
  }

    var minuti = 5; //min di refresh dati
    var intevalloRefreshMs = minuti*60*1000; // in ms
    var int = self.setInterval("checkMessaggiRicevuti()",intevalloRefreshMs);
    var isPrimoCheck = true;
    var isCheckInProgress = false;
    
    function checkMessaggiRicevuti()
    {
      if (isCheckInProgress) return;
      else isCheckInProgress = true;
        
      var d = new Date();
      var t = d.toLocaleTimeString();

      //alert("Ultimo Aggiornamento = "+t);
      
      $.ajax({
          type: "POST",
          url: "/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD %>=siap.siep.richiesta.action.ActLoadContaAttiCompetenzaRicevuti",
          dataType: "html",
          // imposto un'azione per il caso di successo
          success: function(risposta){
            $("#contaAttiRicevuti").html(risposta);
            $("#imgAttiRicevuti").attr("title","Atti ricevuti per competenza - Cumulo.\nUltimo aggiornamento: "+t);
            isPrimoCheck = false;
            isCheckInProgress = false;
          },
          // Chiamata fallita: es 404
          error: function(){
            if (isPrimoCheck) {
              $("#contaAttiRicevuti").html("?");
              $("#imgAttiRicevuti").attr("title","Atti ricevuti per competenza - Cumulo.\nUltimo aggiornamento: errore nella verifica");
            }
            isCheckInProgress = false;
          }}
      );
    }

    // script eseguito sulla onload della pagina
    $(document).ready(function(){
      // Inserire qui il codice da eseguire al caricamento
      //
      /*
      $("#tdIconCounter" ).mouseover(function() {
        //alert("Mouse Over");
        checkMessaggiRicevuti();
      });
      */

      <% if (!UtenteConnesso.isUtenteSIGE()) { %>
      checkMessaggiRicevuti();
      <% } %>
    });
</script>

<script language="JavaScript">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--

  function BoldIT(id){
    var currentLink=eval(document.id);
    for (var i=0;i<document.links.length;i++) {
      document.links(i).style.color='navy';
    }
    document.links(id*1).style.color='blue';
  }
--%>

  function ViewCalcoloRapidoDellaPena(){
    var desktop;
    desktop=window.open("/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActLoadCalcolatrice", "Calcolatrice","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=850,height=580,top=0,left=0");
  }



 // Paolo Cherubini 09/02/2012 
 // inserisco un controllo sul bottone di trasferimento procedimento SIEP verso NSC 
 // il sistema chiede conferma dell'operazione segnalazione b2/rr/007 inoltrata da Pina Marchese
  function ChiediConferma(ActionName){
    var r=confirm("Stai avviando il trasferimento verso il Casellario, confermi?")
    if (r==true) {
      var desktop;
      desktop=window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>="+ActionName,'body');
    }  
  }
<%-- fine Paolo 09/02/2012 --%>
</script>


<script language="JavaScript1.2">
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
  function over_effect(e,state){
    if (document.all)
      source4=event.srcElement
    else if (document.getElementById)
      source4=e.target
      
    if (source4.className=="menulines")
      source4.style.borderStyle=state
    else{
      while(source4.tagName!="TABLE"){
        source4=document.getElementById? source4.parentNode : source4.parentElement
        if (source4.className=="menulines")
          source4.style.borderStyle=state
      }
    }
  }
</script>
</HEAD>
<style>
.menulines{
  border:2.5px solid #F0F0F0;
  text-align : center;
  font-family: 'Tahoma';
  color : Navy;
  font-size : 11px;
  text-decoration : none;
  height:100%;
}

.menulines a{
  text-align : center;
  text-decoration:none;
  color:black;
  font-family: 'Tahoma';
  color : Navy;
  font-size : 11px;
  text-decoration : none;
  width:100%;
  height:100%;
}

.icon {
    width:30px; 
    height:30px;
    position: relative;
}

.notification-count {
    position:absolute;
    top:-4px;
    right:-4px;
    background-color:#FF0000;
border-radius:50%;
    color:#fff;
    padding:2px;
    font-family:tahoma, arial, 'sans-serif';
    font-size:9px;
    font-weight:bold;
    width: 14px;
    height: 14px;
}


</style>
<link rel="STYLESHEET" type="text/css" href="/css/style.css">

<!--BODY class=menu marginheight="0" marginleft="0" topmargin="0" leftmargin="0"-->
<body class="menu" topmargin="5" leftmargin="0" style="{ border-top-style : solid;border-top-color : White; border-top-width : 1px; } ">
  <!-- table border="1" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMousedown="over_effect(event,'inset')" onMouseup="over_effect(event,'outset')" -->
  <!-- width="100%"  -->
  <table cellpadding="1" cellspacing="1" align="left"
         onMouseover="over_effect(event,'outset')" 
         onMouseout="over_effect(event,'solid')" 
         onMousedown="over_effect(event,'inset')" 
         onMouseup= "over_effect(event,'outset')">
    <tr>
      <td width="10px">&nbsp;</td>
  <%
  
  // Paolo Cherubini 09/02/2012 per la b2/rr/007 mi serve verificare se il procedimento è in sessione
  // FascicoloSiepModel lFasMod = (FascicoloSiepModel)session.getAttribute("fascicolo");
  
  boolean lControllo = true;
  
    String lCodTipoUfficio = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
    boolean isSIEP = (lCodTipoUfficio.compareToIgnoreCase("PM") == 0 ||
                      lCodTipoUfficio.compareToIgnoreCase("PMM") == 0 ||
                      lCodTipoUfficio.compareToIgnoreCase("PGCAP") == 0) ? true : false;

    //==========================================================================
    // Ciclo di caricamento dei bottoni di accesso rapido previsti
    //==========================================================================
    ArrayList lFunFiglie = FunRadiceMenuSceltaRapida.getDaughtersFunctions();
    Iterator lIter = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while( lIter.hasNext() ) {
      lFun = (FunctionModel)lIter.next();
      
      String label = null;
      String image = null;
      
      label = lFun.getLabelFunction();
      if(lFun.getImmagine()!=null && lFun.getImmagine().length()>0){
        image = lFun.getImmagine();
      }
      else{
        image = "/images/help.gif";
      }     

      if (lFun.getNameAction().equals("siap.siep.calcolopena.action.ActLoadCalcolatrice"))
      { // la calcolatrice viene aperta come pop up per essere sempre disponibile
      %>
<td class="menulines" >
    <a href="Javascript:ViewCalcoloRapidoDellaPena();">
          <img align="middle" src="<%=image%>" width="32" height="32" alt="" border="0" title="<%=label %>">
    </a>
</td>
      
 <!--  
      Paolo Cherubini 09/02/2012 
  inserisco un controllo sul bottone di trasferimento procedimento SIEP verso NSC 
  il sistema chiede conferma dell'operazione segnalazione b2/rr/007 inoltrata da Pina Marchese
 -->
      
      <% } else if (lFun.getNameAction().equals("siap.sico.webservice.action.ActTrasferimentoSiesToNsc")){ %>
      <td class="menulines" >
         <a href="Javascript:Javascript:ChiediConferma('<%=lFun.getNameAction()%>');" >
          <img align="middle" src="<%=image%>" width="32" height="32" alt="" border="0" title="<%=label %>">
        </a>
      </td>
      
 <!-- fine Paolo 09/02/2012  -->
      <% } else if (lFun.getNameAction().equals("siap.siep.richiesta.action.ActLoadRicercaAttiCompetenzaRicevuti")){ %>
        <td class="menulines" id="tdIconCounter">
          <span class="icon"> 
            <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>" target="body">
              <img align="middle" src="<%=image%>" width="32" height="32" alt="" border="0" title="<%=label %>" id="imgAttiRicevuti"></a>
            <span class="notification-count" id="contaAttiRicevuti">-</span>
          </span>
        </td>
      <% } else if (isSIEP) { %>
      <td class="menulines" >
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction() %>" target="body">
          <img align="middle" src="<%=image%>" width="32" height="32" alt="" border="0" title="<%=label %>">
        </a>
      </td>
      <% } else if (UtenteConnesso.isUtenteSIGE()) { %>
      <td class="menulines" >
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction() %>" target="body">
          <img align="middle" src="<%=image%>" width="32" height="32" alt="" border="0" title="<%=label %>">
        </a>
      </td>
      <% } %>
    <% } %>



    


    </tr>
  </table>
</BODY>
</HTML>