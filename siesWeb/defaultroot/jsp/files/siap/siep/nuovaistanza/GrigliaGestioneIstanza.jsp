<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%--@ taglib prefix="s" uri="/struts-tags" --%>
<%@ page import="f3b.web.IWebConstants"%>
<html>
<head>
  <title>[S.I.E.S.] - Istanze</title>

 <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
 


  <script language="JavaScript1.2">
  <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      function over_effect(e,state)
      {
        if (document.all)
          source4=event.srcElement
        else if (document.getElementById)
          source4=e.target
        if (source4.className=="menulines")
          source4.style.borderStyle=state
        else
        {
          while(source4.tagName!="TABLE")
          {
            source4=document.getElementById? source4.parentNode : source4.parentElement
            if (source4.className=="menulines")
              source4.style.borderStyle=state
          }
        }
      }
    
    function HidDiv()
    {
     var divR = document.getElementById('ricerca');
     var divA = document.getElementById('attivita');

      divR.style.visibility = 'hidden';
      divA.style.visibility = 'hidden';
    }
      
    function VisAttivita()
    { 
    	HidDiv();
    	var divA = document.getElementById('attivita');

		divA.style.visibility = 'visible';
    }
    
    function VisRicerca()
    {	HidDiv();
        var divA = document.getElementById('ricerca');

		divA.style.visibility = 'visible';
    }
    
    
  </script>

  <STYLE>
    .menulines
    {
      border:2.5px solid #BEC6FC;
      text-align : center;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      text-decoration : none;
      height:100%;
    }

    .menulines a
    {
      text-align : center;
      text-decoration:none;
      color:black;
      font-family: 'Tahoma';
      color : Navy;
      font-size : 13px;
      width:100%;
      height:100%;
    }
  </STYLE>
</head>


<body class="corpo" onLoad="javascript:HidDiv()">
  <table>
    <tr>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Gestione Istanze</font>
      </td>
      
     </tr>
  </table>
  <br>

  <br>
    <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMouseup="over_effect(event,'outset')" onmousedown="over_effect(event,'inset')">
    
 
    <tr>
      <td colspan=3 class="Titolonocap">Gestione Istanza</td>
    </tr>
    <tr>

      <td width="32%" class="menulines" nowrap>
        <A href="javascript:VisAttivita()">  Attivita' sull'Istanza </a>
       </td>
         <td width="32%" class="menulines" nowrap>
        <A href="javascript:VisRicerca()">Ricerca Istanze</a>
      </td>
       <td width="32%" class="menulines" nowrap>
        <A href="/jsp/Main.jsp?Action=siap.siep.istruttoria.action.ActIstruttorieGriglia">Istruttoria</a>
       </td>
    </tr>
     <tr><td>&nbsp;</td></tr>
        
  </table>
  
  <div id="attivita" style="position:absolute; top:130px">
     <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMouseup="over_effect(event,'outset')" onmousedown="over_effect(event,'inset')">

   <tr>
      <td colspan=3 class="Titolonocap">Attività sull'Istanza</td>
    </tr>

    <tr>
      <td width="32%" class="menulines" nowrap> 
         <A href="/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActLoadInoltroPM">Inoltro Istanza per la Valutazione Al Pubblico Ministero
      </a></td>
      <td width="32%" class="menulines" nowrap>
        
            <A href="/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActLoadDisposizioniPM">Disposizioni del Pubblico Ministero
</a>
      </td>
     
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap> 
         <A href="/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActLoadAssociaRIaFascicoloSIEP">Associazione Istanza a Procedimento Esistente
      </a></td>
      <td width="32%" class="menulines" nowrap>
        
            <A href="/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActLoadConvertiRIinFascicoloSIEP">Conversione Registro Istanza in Procedimento SIEP
</a>
      </td>
     
    </tr>
    <tr><td>&nbsp;</td></tr>
    
        
    </table>
    </div>
    <div id="ricerca" style="position:absolute; top:130px">
  <table cellpadding="5" cellspacing="5" width="95%" onMouseover="over_effect(event,'outset')" onMouseout="over_effect(event,'solid')" onMouseup="over_effect(event,'outset')" onmousedown="over_effect(event,'inset')">
    
    <tr>
      <td colspan=3 class="Titolonocap">Ricerca Istanza</td>
    </tr>
    <tr>
      <td width="32%" class="menulines" nowrap>
        <A href="/jsp/Main.jsp?Action=siap.siep.jms.action.ActLoadListaIstanzeTrasmesse"> Riscontro Istanze Trasmesse</A>
      </td>
      <td width="32%" class="menulines" nowrap>
        <A href="/jsp/Main.jsp?Action=siap.siep.nuovaistanza.action.ActLoadRicercaNuovaIstanza">    Ricerche</A>
      </td>
      
    </tr>
    <tr><td>&nbsp;</td></tr>
    
  </table>
</div>

</body>
</html>