<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="beneficio" 		scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>

<jsp:useBean id="peneaccessorie" 	scope="request" class="java.util.Vector"/>
<jsp:useBean id="lPenaResMod"    	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="TitoloRevocaBeneficio"   scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<%
// Preparazione Revoca
String lRevoca="";
if(TitoloRevocaBeneficio!=null && TitoloRevocaBeneficio.getIdTitoloCumulato()!=null)
{
	lRevoca = TitoloRevocaBeneficio.getEstremiProvvedimento();
}
%>

<!-- 		DettaglioBeneficioIndultoCumulo			 -->
<html>
 <head>
 <title>[S.I.E.S.] - Gestione Beneficio Indulto Cumulo</title>
	<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
	<script language="JavaScript" src="/html/conferma.js"></script>
	<script language="JavaScript">

	function TornaIndietro(action)
    {
      document.DettBeneficiIndultoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettBeneficiIndultoCumulo.submit();
    }
	
	function VaiadInserire(action)
    {
      document.DettBeneficiIndultoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettBeneficiIndultoCumulo.modalita.value="I";
      document.DettBeneficiIndultoCumulo.submit();
    }
    
    
    function VaiaModificare(action, aIdBen)
    {
      document.DettBeneficiIndultoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettBeneficiIndultoCumulo.modalita.value="M";
      document.DettBeneficiIndultoCumulo.submit();
    }
    
    function VaiaCancellare(action, aIdBen, aStato, aMotivoModifica)
    {
    	if (aStato=='E' || aStato=='M')
    	{
            // Cancellazione Logica Richiedo Motivazione
			document.DettBeneficiIndultoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
			
            document.DettBeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;
            document.DettBeneficiIndultoCumulo.modalita.value="C";
            var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                       + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettBeneficiIndultoCumulo"
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
            	document.DettBeneficiIndultoCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;

              	document.DettBeneficiIndultoCumulo.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;
              	document.DettBeneficiIndultoCumulo.modalita.value="C";
              	document.DettBeneficiIndultoCumulo.submit();
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
            <font class="campo">Dettaglio Beneficio Indulto</font>
          </td>
          <% if(IstruttoriaCumulo.getFlagStato().equals("A") ) {  %>
          <td class="LBG">
            <a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo')">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
            <% if(!beneficio.getFlagStato().equals("C")) {  %>
            <a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo',<%=beneficio.getIdBeneficioCumulo()%>)">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
            <a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo',<%=beneficio.getIdBeneficioCumulo()%>,'<%=beneficio.getFlagStato() %>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(beneficio.getMotivoModifica()),"") %>')">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>
            <% } %>
          </td>
          <% } %>        

<!--    BOTTONE DI RITORNO    -->
          <td class="LBG">
            <a href="javascript:TornaIndietro('siap.siep.modulocumulo.action.ActRicercaBeneficiCumulo')">
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
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
  		
	<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettBeneficiIndultoCumulo">
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  	<input type="hidden" name="modalita" value="">
  
  	<input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>" 	value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"      	value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>"  	value="<%=beneficio.getIdBeneficioCumulo() %>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"  			value="<%=beneficio.getFlagStato() %>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA %>"  		value="<%=beneficio.getMotivoModifica() %>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>"  		value="02">

	 	<table cellspacing=2 cellpadding=2 width="95%">
			<tr>
				<td class="l" width="25%">Natura Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrNaturaBeneficio()) %></font></td>
			</tr>

<%	if(beneficio.getTitIdTitoloCumulatoCollegato()!=null)	// Benefico Concesso poi Revocato da altro titolo
	{	
		if(TitoloRevocaBeneficio!=null && TitoloRevocaBeneficio.getIdTitoloCumulato()!=null)
		{	%>

			<tr>
				<td class="l" width="25%"><font class="label" style="color:red;">Indulto Revocato in Istruttoria</font></td>
				<td class="l"><font class="label" style="color:black;"><%=lRevoca%></font></td>
			</tr>

<%		}
	} %>
		
			<tr>
				<td class="l" width="25%">Tipologia Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio()) %></font></td>
			</tr>
			<tr>
				<td class="l">Provvedimento di Concessione</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrDpr()) %></font></td>
			</tr>	
        	<tr>
				<td class="l">Applicazione del beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrSottotipoBeneficio()) %></font></td>

			</tr>				
        	<tr>
				<td class="l">Reclusione</td>
				<td class="l">
			<%	if (   (beneficio.getNumAnniReclusione()!=null && beneficio.getNumAnniReclusione().intValue()!= 0 ) 
			        || (beneficio.getNumMesiReclusione()!=null && beneficio.getNumMesiReclusione().intValue()!= 0 ) 
			        || (beneficio.getNumGiorniReclusione()!=null && beneficio.getNumGiorniReclusione().intValue()!= 0 ) 
			        || (beneficio.getImportoMulta()!=null && beneficio.getImportoMulta().compareTo(new BigDecimal(0))!=0 ) 
			       )
				{ %>
					Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniReclusione(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiReclusione(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniReclusione(),"0") %></font>
					Multa <font class="campo"><%=StringUtils.toEuroFormat(beneficio.getImportoMulta()) %></font>
			<%	} 
				else
				{ %>
					-
			  <%}%>
				</td>
			</tr>
			
        	<tr>
				<td class="l">Arresto</td>
				<td class="l">
			<%	if( (beneficio.getNumAnniArresto()!=null && beneficio.getNumAnniArresto().intValue()!= 0 ) ||
					(beneficio.getNumMesiArresto()!=null && beneficio.getNumMesiArresto().intValue()!= 0 ) ||
					(beneficio.getNumGiorniArresto()!=null && beneficio.getNumGiorniArresto().intValue()!= 0 ) ||
				    (beneficio.getImportoAmmenda()!=null && beneficio.getImportoAmmenda().compareTo(new BigDecimal(0))!=0   ) 
				    )
				{	%>
					Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniArresto(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiArresto(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniArresto(),"0") %></font>
					Ammenda <font class="campo"><%=StringUtils.toEuroFormat(beneficio.getImportoAmmenda()) %></font>
			<%	} 
				else
				{ %>
					-
			  <%}%>
				</td>
			</tr>		
			<tr>
				<td class="l">Note</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getNote()) %></font></td>
			</tr>		
		</table>

<%    if(peneaccessorie != null && !peneaccessorie.isEmpty())
      { %>
       <table width="90%">      
    	 	<tr><td class="titolo" colspan=10>Pena Accessoria</td></tr>     	  
<%   
	       Iterator iter = peneaccessorie.iterator();
	       while (iter.hasNext()) 
	       {
	    	   PenaAccessoriaCumuloModel lPenAcMod = (PenaAccessoriaCumuloModel) iter.next();
	%> 
	        <tr>
	          <td class="c">
	            <%=StringUtils.toStringJSP(lPenAcMod.getDescrTipoPenaAccessoria(), "-")%>
	          </td>
	          <td class="l">
	<%			if(lPenAcMod.getDurata() != null && !lPenAcMod.getDurata().equals("-")) 
				{%>          
	            	<%=StringUtils.toStringJSP(lPenAcMod.getDescrDurata(),"-")%> 
	<%			}
				else
				{ %>   
					Anni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumAnni(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumMesi(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumGiorni(),"0") %></font>
	<% 			}	%>         
	          </td>
	        </tr>     
	<%		} %>
       </table> 
	<% } %>

<!--  																											-->
<%	String lStato = "";
	String lDescStato = "";

	if      ( beneficio.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	else if ( beneficio.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	else if ( beneficio.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	else if ( beneficio.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
%>
<br>
	<table cellspacing=2 cellpadding=2 width="95%">
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