<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.tipologiaorario.model.TipologiaOrarioModel" %>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="beneficio" 		scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>
<jsp:useBean id="tipologiaorario" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="beneficiononmenzione" scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>

<jsp:useBean id="TitoloRevocaBeneficio"   scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="TitoloRevocaNonMenzione" scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<%
//Preparazione Revoca Beneficio
 String lRevoca="";
 if(TitoloRevocaBeneficio!=null && TitoloRevocaBeneficio.getIdTitoloCumulato()!=null)
 {
	 lRevoca = TitoloRevocaBeneficio.getEstremiProvvedimento();
 }
 
 // Preparazione Revoca NON MENZIONE
 String lRevocaNMZ="";
 if(TitoloRevocaNonMenzione!=null && TitoloRevocaNonMenzione.getIdTitoloCumulato()!=null)
 {
	 lRevocaNMZ = TitoloRevocaBeneficio.getEstremiProvvedimento();
 }

%>

<!-- 		DettaglioBeneficioCumulo			 -->
<html>
 <head>
  <title>[S.I.E.S.] - Gestione Beneficio Cumulo  </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
   	<script language="JavaScript" src="/html/conferma.js"></script>
	<script language="JavaScript">
	
	function TornaIndietro(action)
    {
      document.DettBeneficiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettBeneficiCumulo.submit();
    }
	
	function VaiadInserire(action)
    {
      document.DettBeneficiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettBeneficiCumulo.modalita.value="I";
      document.DettBeneficiCumulo.submit();
    }
    
    
    function VaiaModificare(action,aIdBen)
    {
      document.DettBeneficiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettBeneficiCumulo.modalita.value="M";
      document.DettBeneficiCumulo.submit();
    }
    
    function VaiaCancellare(action, aIdBen, aStato, aMotivoModifica)
    {
    	if (aStato=='E' || aStato=='M')
    	{
            // Cancellazione Logica Richiedo Motivazione
			document.DettBeneficiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
			
            document.DettBeneficiCumulo.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;
            document.DettBeneficiCumulo.modalita.value="C";
            var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                       + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettBeneficiCumulo"
                                       + "&" + "<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>" + "="+aMotivoModifica
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
            	document.DettBeneficiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;

              	document.DettBeneficiCumulo.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;
              	document.DettBeneficiCumulo.modalita.value="C";
              	document.DettBeneficiCumulo.submit();
            }

        }
    	
     }

	</script>
 </head>
 <body class="corpo">
 <FORM name="comandi" >
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Beneficio</font>
        </td>
        
        <% if( IstruttoriaCumulo.getFlagStato().equals("A") ) { %> 
        <td class="LBG">
          <a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo')">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
 <%   if(!beneficio.getFlagStato().equals("C"))
    {  
      if(!beneficio.getCodTipoBeneficio().equals("02") )
          { %>             
            <a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo',<%=beneficio.getIdBeneficioCumulo()%>)">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <%  } %>    
          <a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo',<%=beneficio.getIdBeneficioCumulo()%>,'<%=beneficio.getFlagStato()%>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(beneficio.getMotivoModifica()),"") %>')">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>
<%    } %>        
        </td>
        <% } %>

  <!-- BOTTONE DI RITORNO -->
      	<td class="LBG">
        	<a href="javascript:TornaIndietro('siap.siep.modulocumulo.action.ActRicercaBeneficiCumulo')">
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

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettBeneficiCumulo">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="modalita" value="">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"      	value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  <input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>"  value="<%=beneficio.getIdBeneficioCumulo() %>">
  <input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"  			value="<%=beneficio.getFlagStato() %>">
  <input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA %>"  	value="<%=beneficio.getMotivoModifica() %>">
  <input type="hidden" name="<%= ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>"  		value="01">
  
  <table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Natura Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrNaturaBeneficio()) %></font></td>
		</tr>
<%	if(beneficio.getTitIdTitoloCumulatoCollegato()!=null)	// Benefico Concesso poi Revocato da altro titolo
	{	
		if(TitoloRevocaBeneficio!=null && TitoloRevocaBeneficio.getIdTitoloCumulato()!=null)
		{	%>

			<tr>
				<td class="l" width="25%"><font class="label" style="color:red;">Beneficio Revocato in Istruttoria</font></td>
				<td class="l"><font class="label" style="color:black;"><%=lRevoca%></font></td>
			</tr>

<%		}
	} %>		
		<tr>
				<td class="l">Tipologia Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio()) %></font></td>
		</tr>
		<tr>
				<td class="l">Decisione Giudice</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrSottotipoBeneficio()) %></font></td>
		</tr>	
        <tr>
				<td class="l">Non Menzione</td>
				<td class="l">
                 <%if("02".equals(beneficio.getCodTipoBeneficio()) ||( beneficiononmenzione != null && beneficiononmenzione.getIdBeneficioCumulo()!= null)){%>
                        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
                 <%} %>&nbsp;
				</td>
		</tr>
<%		if(TitoloRevocaNonMenzione!=null && TitoloRevocaNonMenzione.getIdTitoloCumulato()!=null)
		{	%>

			<tr>
				<td class="l" width="25%"><font class="label" style="color:red;">Non Menzione Revocata in Istruttoria</font></td>
				<td class="l"><font class="label" style="color:black;"><%=lRevocaNMZ%></font></td>
			</tr>

<%		} %>			
		
        <tr>
				<td class="l">Durata Sospensione</td>
				<td class="l">
				  Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniSospensione(),"0") %></font>&nbsp;
				</td>
		</tr>
		<tr>
				<td class="l">Obblighi del condannato ex art 165 c.p. </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrTipoSospSubordinata()) %></font></td>
		</tr>
		<tr>
				<td class="l">Tipologia Obbligo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getNote(), " ") %>&nbsp;</font></td>
		</tr>
		<tr>
				<td class="l">Ente Incaricato</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getEnteIncaricato(), " ") %>&nbsp;</font></td>
		</tr>		
        <tr>
				<td class="l">Termine Adempimento Obbligo</td>
				<td class="l">
				<%
				if (beneficio.getNumAnniAdempimento()!=null || beneficio.getNumMesiAdempimento()!=null || beneficio.getNumGiorniAdempimento()!=null)
				{
				%>
					Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniAdempimento(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiAdempimento(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniAdempimento(),"0") %></font>
				<%
				} else
				{ %>
					-
			  <%}%>
				</td>
		</tr>
	</table>		
<%if("08".equals(beneficio.getCodTipoSospSubordinata())) 
{ %>
	<table cellspacing=2 cellpadding=2>		
		<tr>
			<td class="l">Durata Prestazione Attività Non Retribuita</td>
			<td class="l">
<%			if (beneficio.getNumMesiPrestazione()!=null || beneficio.getNumGiorniPrestazione()!=null)
			{	%>
				Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiPrestazione(),"0") %></font>&nbsp;
				Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniPrestazione(),"0") %></font>
<%			} else
			{ 	%>
					-
<%			}	%>
			</td>
		</tr>

		<tr>
			<td class="l">Ore settimanali</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumOreSettimanali()) %></font></td>
		</tr>
        <tr>
			<td class="l">Frequenza Settimanale</td>
			<td class="l">
<%			if("D".equals(beneficio.getFlagFrequenzaSettimanale())) 
			{%>  
			  	<font class="campo">Determinata</font>
<%			}
			else if("N".equals(beneficio.getFlagFrequenzaSettimanale())) 
			{	%> 
			  	<font class="campo">Non Determinata</font>				 
<%			} 	%>
			</td>
		</tr>
	</table>

<% 	if(tipologiaorario != null && !tipologiaorario.isEmpty())
	{	%>
		<table width="90%" cellspacing="5" >      
    	 <tr><td class="titolo" colspan=10>Tipologia Orario</td></tr>     	  
<%     
		Iterator iter = tipologiaorario.iterator();
       	while (iter.hasNext()) 
       	{
     	 	TipologiaOrarioModel TipoOrMod = (TipologiaOrarioModel) iter.next();
%> 
		 <tr>
        	<td class="l" width="30%">Giorno</td>      
        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getDescrNumGiorno())%></font></td>
        	<td class="l"> dalle <font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getDalleOre())%></font></td>
        	<td class="l">  alle <font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getAlleOre())%></font></td>
       	 </tr>
       	 <%--
	      <tr>
        	<td class="l">Ente Incaricato dei controlli</td>
        	<td class="l" colspan='3'><font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getEnteIncaricato())%></font></td>
       	 </tr>
         --%>
<%		} %>
        </table> 
<%   }	
  }		%>

<!--  																											-->
<%	String lStato = "";
	String lDescStato = "";

	if      ( beneficio.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	else if ( beneficio.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	else if ( beneficio.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	else if ( beneficio.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
%>
<br>
<table cellspacing=2 cellpadding=2 width="90%">
	<tr>
      <td class="l" width="25%">Stato</td>
      <td class="l">
        <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
      </td>
	</tr>

<%	if( beneficio.getMotivoModifica() != null )
	{	%>	
		<tr>
	      <td class="l">Motivo Inserimento/Modifica</td>
	      <td class="l">
	        <font class="campo"><%=StringUtils.toStringJSP(beneficio.getMotivoModifica())%>&nbsp;</font>
	      </td>
		</tr>
<%	} 	%>
</table>


	</FORM>
	</body>
</html>