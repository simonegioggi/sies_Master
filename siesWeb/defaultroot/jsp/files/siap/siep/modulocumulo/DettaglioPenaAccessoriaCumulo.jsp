<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.sico.util.SICOLookupRemote"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="penaaccessoria" scope="request" class="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"/>
<jsp:useBean id="beneficio"     scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>


<!-- 			DettaglioPenaAccessoriaCumulo		 -->
<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Dettaglio Pena Accessoria Titolo Cumulato </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="/html/conferma.js"></script>

<script language="JavaScript">
//==========================================================================
    // Ritorna all'elenco Titoli coinvolti nell'istruttoria
    //==========================================================================
    function TornaIndietro(action)
    {
      document.DettPenAccCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettPenAccCumulo.submit();
    }
    
    function VaiadInserire(action)
    {
      document.DettPenAccCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettPenAccCumulo.modalita.value="I";
      document.DettPenAccCumulo.submit();
    }
    
    
    function VaiaModificare(action)
    {
      document.DettPenAccCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettPenAccCumulo.modalita.value="M";
      document.DettPenAccCumulo.submit();
    }
    
    function VaiaCancellare(action, aStato, aMotivoModifica)
    {
    	if (aStato=='E' || aStato=='M')
    	{
            // Cancellazione Logica Richiedo Motivazione
			document.DettPenAccCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
			
            document.DettPenAccCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
            document.DettPenAccCumulo.modalita.value="C";
            var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                       + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettPenAccCumulo"
                                       + "&" + "<%=ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                       , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
            window.parent.close();
            
            // N.B. la submit viene effettuare direttamnete dalla finestra di popup
        }
        else if (aStato=='I')
        {
            // Cancellazione fisica richiedo conferma
            var retValue = true;
            retValue = confirm("Si vuole procedere con la cancellazione dei dati?"); 
            if (retValue) 
            {
            	document.DettPenAccCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;

              	document.DettPenAccCumulo.<%=ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO%>.value = aStato;
              	document.DettPenAccCumulo.modalita.value="C";
              	document.DettPenAccCumulo.submit();
            }
            else
            {	
            	return false;
            }	
        }
    	
     }
    
</script>
</head>

<body class="corpo">
 <FORM name="comandi" >
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Pena Accessoria</font>
      </td>

      <% if(IstruttoriaCumulo.getFlagStato().equals("A") ) { %>
      <td class="LBG">
        <a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
        <% if(!penaaccessoria.getFlagStato().equals("C"))
        {  %> 
        <a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActLoadInserisciPenaAccessoriaCumulo','<%=penaaccessoria.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(penaaccessoria.getMotivoModifica()),"") %>')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>
        <% } %>
      </td>
      <% } %>

  <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:TornaIndietro('siap.siep.modulocumulo.action.ActRicercaPeneAccessorieCumulo')">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>

   </tr>
 </table>
</FORM>

  <% // INCLUDE DEL DETTAGLIO DEL TITOLO%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettPenAccCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="modalita" value="">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  <input type="hidden" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>"  value="<%=penaaccessoria.getIdPenaAccessoriaCumulo() %>">
  <input type="hidden" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO %>"  value="<%=penaaccessoria.getFlagStato() %>">
  <input type="hidden" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA %>"  value="<%=penaaccessoria.getMotivoModifica() %>">

  <table cellspacing=2 cellpadding=2 width="90%">
	<tr>
      	<td class="l" width="25%">Tipo Pena Accessoria</td>
      	<td class="l"><font class="campo"><%=penaaccessoria.getDescrTipoPenaAccessoria() %></font></td>
	</tr>
<%	if(penaaccessoria.getDescrTipoPenaAccessoria().trim().compareTo("Altre Pene Accessorie")==0)
	{%>
	<tr>
   		<td class="l">Descrizione Altre P. A.</td>
      	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrAltrePA()) %></font></td>
	</tr>
<%	} %> 

	<tr>
      <td class="l">Tipo Durata</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getDescrDurata()) %></font></td>
	</tr>
	<tr>
      <td class="l">Durata</td>
      <td class="l">
        Anni <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumAnni(), "0")%></font>&nbsp;&nbsp;
        Mesi <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumMesi(), "0")%></font>&nbsp;&nbsp;
        Giorni <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNumGiorni(), "0")%></font>
      </td>
	</tr>
	<tr>
      <td class="l">Note</td>
      <td class="l">
        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getNote())%>&nbsp;</font>
      </td>
	</tr>
</table>

<!--  																											-->
<%	// Eventualmente in futuro, qui andrà inserita la parte relativa a: Estremi Ordinanza Applicazione del GE.. %>

<%	String lStato = "";
	String lDescStato = "";

	if      ( penaaccessoria.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	else if ( penaaccessoria.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	else if ( penaaccessoria.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	else if ( penaaccessoria.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
%>

<br>
	
<table cellspacing=2 cellpadding=2 width="90%">
	<tr>
      <td class="l" width="25%">Stato</td>
      <td class="l">
        <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
      </td>
	</tr>

<%	if( penaaccessoria.getMotivoModifica() != null )
	{	%>	
		<tr>
	      <td class="l">Motivo Inserimento/Modifica</td>
	      <td class="l">
	        <font class="campo"><%=StringUtils.toStringJSP(penaaccessoria.getMotivoModifica())%>&nbsp;</font>
	      </td>
		</tr>
<%	} 	%>
			
</table>
	
	</FORM>
  </body>
</html>