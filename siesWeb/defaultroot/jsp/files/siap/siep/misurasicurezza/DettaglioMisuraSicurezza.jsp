<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.ufficio.controller.UfficioUtils" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel" %>

<jsp:useBean id="misurasicurezza" scope="request" class="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"/>
<jsp:useBean id="lTipoFunzione"   scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
  // Gestione funzione SIGE
  boolean modoSIGE = false;
  if (modo != null && modo.equalsIgnoreCase("SIGE"))
    modoSIGE = true;

  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
  
// Titolo esecutivo Associato alla Misura
  Boolean DatiTitolo=false;
  String lDati="";
  String TipoUff="";
  String DesComUff="";
  RiferimentoFascicoloSiepModel lRifMod = null;
  
  if(misurasicurezza!=null && misurasicurezza.getIdMisuraSicurezza()!=null)
  {
    if(misurasicurezza.getRiferimentoFascicoloSiep()!=null && 
      misurasicurezza.getRiferimentoFascicoloSiep().getIdRiferimentoFascicoloSiep() != null )
    {
      
      DatiTitolo=true;
      lRifMod = misurasicurezza.getRiferimentoFascicoloSiep();
      
      lDati+= lRifMod.getDescrTipoProvvedimento()+" N."+lRifMod.getNumeroProvvedimento()+"/"+lRifMod.getAnnoProvvedimento()+" ";
      lDati+= lRifMod.getDescrTipoAutoritaEmittente()+ " di "+lRifMod.getDescrLuogoEmittente()+" del "+DateUtils.getDateToString(lRifMod.getDataProvvedimento(),"dd-MM-yyyy")+" ";
      
      if(lRifMod.getFlagMS()!= null && lRifMod.getFlagMS().compareTo("M")==0)
        lDati+="(Proc. ESECUZIONE M.S. N.";
      else if(lRifMod.getFlagMS()!= null && lRifMod.getFlagMS().compareTo("N")==0)
        lDati+="(Proc. SIEP N.";
      else
        lDati+="(Procedimento N.";  
        
      lDati+=lRifMod.getProgrFascicoloSiep()+"/"+lRifMod.getAnnoFascicoloSiep();
      
      if(lRifMod.getCodUffFascicoloSiep()!=null)
      {
        DesComUff = (UfficioUtils.getUfficioByCodUfficio(lRifMod.getCodUffFascicoloSiep())).getDescrComune();
        TipoUff = (UfficioUtils.getUfficioByCodUfficio(lRifMod.getCodUffFascicoloSiep())).getCodTipoUfficio();
        lDati+=" "+TipoUff+" "+DesComUff;
      }
      
      lDati+=")";
      
    }
  }
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Misura Sicurezza </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="/html/conferma.js"></script>
<%
  if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
  {
%>
    <script language="JavaScript">
      var aForm=null;
      function DisabilitaFine()
      {
        aForm=document.getElementById("Fine");
        Disabilita();
      }

      function Disabilita()
      {
        if (aForm==null)
          aForm=document.getElementById("Misura");

        document.Fine.F.disabled = true;
        document.Misura.S.disabled = true;

        aForm.submit();
      }
    </script>
<%
  } 
%>
<script language="JavaScript">

function VaisuCancellaTitolo(aIdMisura)
{
    var msgConfirm = "Con la Cancellazione, la Misura in oggetto verrà associata al Titolo del procedimento! \n Si vuole procedere ?"; 
    if (window.confirm(msgConfirm)) 
    {
        lAzione = "siap.siep.misurasicurezza.action.ActCancellaRiferimentoTitoloEsecutivoAssociatoaMS";
        document.comandi.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.comandi.<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>.value = aIdMisura;
        document.comandi.submit();
    }

}

</script>
    
</head>

<body class="corpo">
  <FORM name="comandi" method="POST" action="/jsp/Main.jsp">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Misura Sicurezza</font>
        </td>
        <td>
          <input type="hidden" title="idMis" name="<%= ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA %>" value="">
          <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
        </td>
<%
      //lTipoFunzione per capire che si proviene da iscrizione guidata      
      if(lTipoFunzione.equals("")) 
      {
        String   lModificabile = (String)request.getAttribute("Modificabile");
        if(modoSIGE)
        {
          String   lCancellabile = (String)request.getAttribute("Cancellabile");
         %>     
              <td class="LBG">
                  <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
                    <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" />
                    <jsp:param name="ValoreIdEntita" value="<%=misurasicurezza.getIdMisuraSicurezza()%>" />
                  <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
                  <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
                </jsp:include>
             </td> 

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
             
        <%} else { %>     
        
        <%
        String lCancellabile = "N";
        if (misurasicurezza.isDurataZero() && "-".equals(misurasicurezza.getCodTipo()) && misurasicurezza.getCodOperatoreInserimento().startsWith("res-"))
            lCancellabile = "S";
          else
            lCancellabile = "N";
        %>
        
        <td class="LBG">
          <!-- dentro il nuovo "PG_TOOLBAR_HEADER_MISURA_SICUREZZA_SIEP" è costruita la combo per -Associa Titolo Esecutivo- -->
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_MISURA_SICUREZZA_SIEP%>">
            <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" />
            <jsp:param name="ValoreIdEntita" value="<%=misurasicurezza.getIdMisuraSicurezza()%>" />
            <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
            <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
            <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
          </jsp:include>
        </td>
        <td class="LBG">
          <a href="javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td> 
<%
      }
    }
%>
      </tr>
    </table>
  </FORM>

  <br>
  <%if(!modoSIGE){%>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
      <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
      <jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>
   <table cellspacing=3 cellpadding=4>
    <tr>
        <td class="l">Natura Misura</td>
        <td class="l"><font class="campo"><%=misurasicurezza.getDescrNatura() %></font></td>
    </tr>
    <tr>
        <td class="l">Tipo Misura</td>
        <td class="l"><font class="campo"><%=misurasicurezza.getDescrTipo() %></font></td>
    </tr>
    <tr>
      <td class="l">Durata Misura</td>
        <td class="l">Anni
        <font class="campo"><%=StringUtils.toStringJSP(misurasicurezza.getNumAnni(),"0") %></font>
                    Mesi
        <font class="campo"><%=StringUtils.toStringJSP(misurasicurezza.getNumMesi(),"0") %></font>
        Giorni
        <font class="campo"><%=StringUtils.toStringJSP(misurasicurezza.getNumGiorni(),"0") %></font>
        </td>
    </tr>
<%    if(misurasicurezza.getDataFineValidita() != null )
    {%>   
      <tr>
          <td class="l">Data Fine Validita</td>
          <td class="L">
              <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misurasicurezza.getDataFineValidita(),"dd-MM-yyyy") )%></font>
          </td>
        </tr>
<%    }
    
    if(misurasicurezza.getFlagAnnullaMisura() != null && misurasicurezza.getFlagAnnullaMisura().compareTo("A") == 0)
    {%> 
      <tr>    
          <td class="l">Stato Misura</td>
          <td class="L"><font class="cRosso"> ANNULLATA </font></td>
        </tr> 
<%    } %>        
  
    <tr><td>&nbsp;</td></tr>
    
    <tr>
      <td class="l"><font class="label" style="font-size: 10pt"> Titolo Esecutivo Associato: </font></td> 

<%      if(DatiTitolo)
      {%>
      <td class="l">      
        <font class="campo">&nbsp;<%=lDati%></font>
      </td>
      <td class="c"> &nbsp;
            <a href="javascript:VaisuCancellaTitolo(<%=misurasicurezza.getIdMisuraSicurezza() %> )">
              <img src="/images/delete.gif" width="12" height="12" alt="Elimina Associazione Titolo" border="0"></a>
      </td>
<%      }
      else
      {%>
      <td class="l">
        <font class="campo">&nbsp;Titolo del Procedimento </font>
      </td> 
<%      } %>               

		<td>&nbsp;&nbsp;</td>
		<td class="l">

   			<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadRicercaAssocia_TitoloEsec_aMisuraSic&<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>=<%=misurasicurezza.getIdMisuraSicurezza()%>" title="cambiaTitolo">
          		Sostituzione Titolo Esecutivo Associato
        	</a>
 
      	</td>
    </tr>     
<%
    //lTipoFunzione per capire che si proviene da iscrizione guidata    
    if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  
    {
%>
      <tr>
        <td class="lNoBord">
          <FORM method="POST" name="Misura" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadInserisciMisuraSicurezza&lTipoFunzione=<%=lTipoFunzione%>">
            <br>
            <INPUT class="bottone" type="button" name="S" value="Altra Misura Sicurezza" onclick="Javascript:Disabilita();">
          </FORM>
        </td>
        <td class="lNoBord">
          <FORM method="POST" name="Fine" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=misurasicurezza.getFasSieIdFascicoloSiep()%>">
            <br>
            <INPUT class="bottone" type="button" name="F" value="  Fine  " onclick="Javascript:DisabilitaFine();">
         </FORM>
        </td>
      </tr>
<%
    }
%>
  </table>
</body>
</html>