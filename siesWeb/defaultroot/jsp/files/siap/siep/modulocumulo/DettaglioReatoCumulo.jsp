<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.cumulo.action.ICostantiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="reato" scope="request" class="siap.siep.modulocumulo.model.ReatoCumuloModel"/>

<%
//=====================================================================================
//Form per la visualizzazione del dettaglio dei REATI legati a un certo Titolo.(CUMULO)
//=====================================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.A.P] - Gestione Reato (CUMULO)- </title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
    <script language="JavaScript" src="/html/gen_validatorv2.js"></script>
   
<script language="JavaScript">
//==========================================================================
// Ritorna all elenco dei reati (torna Indietro)
//==========================================================================
function eseguiFunzione(action)
{
    document.DettaglioReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
    document.DettaglioReatoCumulo.submit();
}

//==========================================================================
// Richiama la funzione di inserimento/Modifica Pena_Reato
//==========================================================================
function PenaReatoCumulo()
{
    lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciPenaReatoCumulo";
    document.DettaglioReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
    document.DettaglioReatoCumulo.submit();
} 

//==========================================================================
//Richiama la funzione di inserimento Nuovo Reato
//==========================================================================
function VaiadInserire(action)
{
  document.DettaglioReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
  document.DettaglioReatoCumulo.submit();
}

//==========================================================================
//Richiama la funzione di Modifica Reato
//==========================================================================
function VaiaModificare(action,aIdRea)
{
  document.DettaglioReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
  document.DettaglioReatoCumulo.submit();
}

function VaiaCancellare(action, aIdRea, aStato, aMotivoModifica)
{
	if (aStato=='E' || aStato=='M')
	{
  	// Cancellazione Logica: Richiedo Motivazione
		document.DettaglioReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
			
      document.DettaglioReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO%>.value = aStato;
      var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                   + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettaglioReatoCumulo"
                                   + "&" + "<%=ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
                                   , "CancellaDatoAnalitico","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
      window.parent.close();
        
        // N.B. la submit viene effettuare direttamnete dalla finestra di popup
  }
  else if (aStato=='I')
  {
  	// Cancellazione fisica: richiedo conferma
      var retValue = true;
      retValue = confirm("Si vuole procedere con la cancellazione dei dati?"); 
      if (retValue) 
      {
        	document.DettaglioReatoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;

         	document.DettaglioReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO%>.value = aStato;
         	document.DettaglioReatoCumulo.submit();
      }

  }
	
} // Chiude VaiaCancellare

</script>
</head>

<BODY class="corpo">
 <FORM name="comandi" >
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Reato del titolo Cumulato</font>
      </td>
  <%if(IstruttoriaCumulo.getFlagStato().equals("A") && !reato.getFlagStato().equals("C")  ) 
  	{	 %> 
      <td class="LBG">
        <a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciReatoCumulo')">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci Reato" width="24" height="24" border="0"></a>
     
        <a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadModificaReatoCumulo',<%=reato.getIdReatoCum()%> )">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica Reato" width="24" height="24" border="0"></a>
            
        <a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActCancellaReatoCumulo',<%=reato.getIdReatoCum() %>,'<%=reato.getFlagStato()%>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(reato.getMotivoModificaNote() ),"") %>')" >
           	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella Reato" width="24" height="24" border="0"></a>
            
    
        </td>
<% } %>

  <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      </td>
    </tr>
  </table>
 </FORM> 
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>
 
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioReatoCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

 <input type="HIDDEN" name=<%= ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%> value="<%=reato.getIdReatoCum()%>">
 <input type="HIDDEN" name=<%= ICostantiReatoCumulo.CAMPO_FLAG_STATO%> value="<%=reato.getFlagStato()%>">
 <input type="HIDDEN" name=<%= ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA%> value="<%=reato.getMotivoModificaNote()%>">
	
  <table cellspacing=2 cellpadding=2 width=95%>
    <tr><td class="Titolo">Reato</td></tr>
      
    <tr>
      <td class="l">
        <font class="campo">
<%
          String NoteSi = "NO";  
          String lProgressivo = "";
          if(reato.getProgrCircostanza().intValue() == 1)
            lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();

          if(!lProgressivo.equals(""))
          {
%>
            <font class="campoNoCap">
<%
              out.print("Reato N."+lProgressivo+": ");
%>
            </font>
<%
          }
          boolean lFlagAnnoNumero = false;
          if( reato.getAnnoFonte() != null && !reato.getAnnoFonte().equals("")
              && reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte()+" ");
            if(reato.getAnnoFonte() != null && !reato.getAnnoFonte().equals(""))
              out.println(reato.getAnnoFonte());
            if(reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals(""))
              out.println("/"+reato.getNumeroFonte());
          }

          if(reato.getArticolo() != null && !reato.getArticolo().equals(""))
            out.println("art."+reato.getArticolo());
          if(reato.getDescrSottonumerazione() != null && !reato.getDescrSottonumerazione().equals("") && !reato.getDescrSottonumerazione().equals("-"))
            out.println(" "+reato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte());
          }

          if(reato.getComma() != null && !reato.getComma().equals(""))
            out.println(" c. "+reato.getComma());
 // CommaQual è stato aggiunto         
          if(reato.getDescrCommaQualificante() != null && !reato.getDescrCommaQualificante().equals("") && !reato.getDescrCommaQualificante().equals("-"))
              out.println(" "+reato.getDescrCommaQualificante());          
          
          if(reato.getLettera() != null && !reato.getLettera().equals(""))
            out.println(" l. "+reato.getLettera());
          if(reato.getNumero() != null && !reato.getNumero().equals(""))
            out.println(" n. "+reato.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
<%
  //Se il reato è quello principale
  if(reato.getProgrCircostanza().intValue() == 1)
  {
%>
    <table cellspacing=2 cellpadding=2 width=95%>
<%
    if(reato.getDescrTipoReato() != null && !reato.getDescrTipoReato().equals("") && !reato.getDescrTipoReato().equals("-"))
    {
%>
      <tr>
        <td class="l" width=20%>Tipo Reato</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescrTipoReato())%>&nbsp;</font></td>
      </tr>
<%
    }
    if(reato.getDescLuogo() != null && !reato.getDescLuogo().equals(""))
    {
%>
      <tr>
        <td class="l" width=20%>Luogo</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescLuogo())%>&nbsp;</font></td>
      </tr>
<%
    }
    if(reato.getCodPeriodoConsumazione() != null && !reato.getCodPeriodoConsumazione().equals("") && !reato.getCodPeriodoConsumazione().equals("-"))
    {
%>
      <tr>
        <td class="l" width=20%>Periodo Consumazione</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescrPeriodoConsumazioneCum())%>&nbsp;</font></td>
      </tr>
<%
    }
    if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
    {
%>
      <tr>
        <td class="l">&lt;Data1&gt;</td>
        <td class="l">
          <font class="campo">
<%
        String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio(), "**");
        if ( !lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
          lStrGGInizio = "0"+lStrGGInizio;

        String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio(), "**");
        if ( !lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
          lStrMMInizio = "0"+lStrMMInizio;

        String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio(), "**");
%>
            <%=lStrGGInizio%>
            -
            <%=lStrMMInizio%>
            -
            <%=lStrAAInizio%>
          </font>
        </td>
      </tr>
<%
    }
    if(reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
    {
%>
      <tr>
        <td class="l">&lt;Data2&gt;</td>
        <td class="l">
<%
        String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine(), "**");
        if ( !lStrGGFine.equals("**") && lStrGGFine.length() == 1)
          lStrGGFine = "0"+lStrGGFine;

        String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine(), "**");
        if ( !lStrMMFine.equals("**") && lStrMMFine.length() == 1)
          lStrMMFine = "0"+lStrMMFine;

        String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine(), "**");
%>
          <font class="campo">
            <%=lStrGGFine%>
            -
            <%=lStrMMFine%>
            -
            <%=lStrAAFine%>
          </font>
        </td>
      </tr>
<%
    } 
  
    if(reato.getNote() != null && reato.getNote().length()>0)
    {
    NoteSi = "SI"; 
%>
      <tr>
        <td class="l" colspan=1 width=20%><font class="label">Note </font></td>
        <td class="l" colspan=5 width=80%><font class="campo"><%=StringUtils.toStringJSP(reato.getNote())%></font></td>
      </tr>
      
<%  } %>     
    
  </table>
 <%    
  } // chiudi reato.getProgrCircostanza
 %>

 <%    
    if(reato.getNote() != null && reato.getNote().length()>0 && NoteSi.equals("NO"))
    {
%>
    <table cellspacing=2 cellpadding=6 width=95%>
      <tr>
        <td class="l" colspan=1 width=20%><font class="label">Note </font></td>
        <td class="l" colspan=5 width=80%><font class="campo"><%=StringUtils.toStringJSP(reato.getNote())%></font></td>
      </tr>
    </table>
<%  } %> 

<!-- 		EVENTUALE  PENA_REATO_CUMULO PRESENTE 			-->

<%	if(reato.isPenaReatoInserita() )
	{	%>
	<table cellspacing=2 cellpadding=6 width=95%>
		<br>
		<tr><td class="Titolo" colspan="2">Pena Reato</td></tr>
		<tr>
      		<td class="l" width=20% >Tipo Pena Detentiva</td>
      		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(reato.getDescrTipoPenaDetentiva())%>&nbsp;</font></td>
		</tr>
<%	if(reato.getNumAnni()!=null || reato.getNumMesi()!=null || reato.getNumGiorni()!=null)
	{	%>
		<tr>		
      		<td class="l">Durata</td>
      		<td class="l">
        		Anni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(reato.getNumAnni(), "0")%>&nbsp;</font>
        		Mesi&nbsp;<font class="campo"><%=StringUtils.toStringJSP(reato.getNumMesi(), "0")%>&nbsp;</font>
        		Giorni&nbsp;<font class="campo"><%=StringUtils.toStringJSP(reato.getNumGiorni(), "0")%></font>
      		</td>
		</tr>
<%	} %>

<%	if(reato.getNumAnniIsolamentoDiurno()!=null || reato.getNumMesiIsolamentoDiurno()!=null || reato.getNumGiorniIsolamentoDiurno()!=null)
	{%>		
    	<tr>
      		<td class="l">Durata Isolamento Diurno</td>
      		<td class="L" >
        		<font class="label">Anni </font><font class="campo"><%=StringUtils.toStringJSP(reato.getNumAnniIsolamentoDiurno(),"0")%></font>&nbsp;
        		<font class="label">Mesi </font><font class="campo"><%=StringUtils.toStringJSP(reato.getNumMesiIsolamentoDiurno(),"0")%></font>&nbsp;
        		<font class="label">Giorni </font><font class="campo"><%=StringUtils.toStringJSP(reato.getNumGiorniIsolamentoDiurno(),"0")%></font>
      		</td>
    	</tr>
  <%} %>  	
		<tr>
      		<td class="l">Tipo Sanzione</td>
      		<td class="l"><font class="campo"><%=reato.getDescrTipoSanzione()%>&nbsp;</font></td>
      		</td>
		</tr>
		<tr>
      		<td class="l">Sanzione Pecuniaria</td>
      		<td class="l">
        		<font class="campo"><%=StringUtils.toEuroFormat(reato.getSanzionePecuniaria())%></font> Euro
      		</td>
		</tr>		
		
<%	} %>

<!-- 	Descrizione dello stato del dato Analitico  -->

  <%    String lStato = "";
    	String lDescStato = "";
        if      ( reato.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto dal fascicolo originale";}
        else if ( reato.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
        else if ( reato.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
        else if ( reato.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
    %>
    <table cellspacing=2 cellpadding=6 width=95%>
      <tr><td>&nbsp;</td></tr>	 
      <tr>
        <td class="l" width="25%">Stato</td>
        <td class="l">
          <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
        </td> 
      </tr>     
   
 <% //================================================================================================
 	// Descrizione del Motivo Inserimento/Modifica visualizzato solo in fase di modifica del dato   
    if(reato.getMotivoModificaNote() != null && !reato.getMotivoModificaNote().equals(""))
    {
%>
      <tr>
        <td class="l" colspan=1 width=20%><font class="label">Motivo Inserimento </font></td>
        <td class="l" colspan=5 width=80%><font class="campo"><%=StringUtils.toStringJSP(reato.getMotivoModificaNote())%></font></td>
      </tr>
   
<%  } %> 

   </table>

<!-- 		BOTTONE PER MODIFICA/INSERIMENTO di PENA_REATO_CUMULO 		-->

   <table>
   	<br>
     <tr>
<%	if(IstruttoriaCumulo.getFlagStato().equals("A") && !reato.getFlagStato().equals("C")  )
	{
		if(reato.isPenaReatoInserita() )
		{ %>     
      	<td>
        	<INPUT class="bottone" type="button" name="MODIFICA" value="Modifica Pena Reato" title="Modifica Pena Reato" onClick="javascript:PenaReatoCumulo();">
      	</td>
<%		}
		else
		{	%>
	  	<td>
        	<INPUT class="bottone" type="button" name="INSERISCI" value="Iscrizione Pena Reato" title="Inserimento Pena Reato" onClick="javascript:PenaReatoCumulo();">
      	</td>	
<%		}
	}	%>	        
     </tr>
   </table>

</FORM>
</body>
</html>