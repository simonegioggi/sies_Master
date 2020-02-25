<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="beneficioCumulo" scope="request" class="siap.siep.modulocumulo.model.BeneficioCumuloModel"/>
<%
//==============================================================================
//Form Dettaglio del BENEFICIO REVOCATO:
// In particolare  REVOCA INDULTO in Altro Titolo Cumulato 
//==============================================================================
%>
   
<!-- 			DettaglioRevocaBeneficioIndultoCumulo			 -->
<html>
<head>
 <title>[S.I.E.S.] - Gestione Cumulo - Dettaglio Revoca Indulto</title>
	
 <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
 <script language="JavaScript" src="/html/conferma.js"></script>
 <script language="JavaScript">
	
	function TornaIndietro(action)
    {
      document.DettaglioRevocaIndulto.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettaglioRevocaIndulto.submit();
    }
	
	function VaiadInserire(action)
    {
      document.DettaglioRevocaIndulto.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettaglioRevocaIndulto.modalita.value="I";
      document.DettaglioRevocaIndulto.submit();
    }
    
    
    function VaiaModificare(action,aIdBen)
    {
      document.DettaglioRevocaIndulto.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.DettaglioRevocaIndulto.modalita.value="M";
      document.DettaglioRevocaIndulto.submit();
    }
    
    function VaiaCancellare(action, aIdBen, aStato, aMotivoModifica)
    {
    	if (aStato=='E' || aStato=='M')
    	{
            // Cancellazione Logica Richiedo Motivazione
			document.DettaglioRevocaIndulto.<%=IWebConstants.ACTION_FIELD%>.value = action;
			
            document.DettaglioRevocaIndulto.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;
            document.DettaglioRevocaIndulto.modalita.value="C";
            var  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.modulocumulo.action.ActLoadCancellaDatoAnalitico" 
                                       + "&" + "<%=ICostantiModuloCumulo.NOME_FORM%>" + "="+"DettaglioRevocaIndulto"
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
            	document.DettaglioRevocaIndulto.<%=IWebConstants.ACTION_FIELD%>.value = action;

              	document.DettaglioRevocaIndulto.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;
              	document.DettaglioRevocaIndulto.modalita.value="C";
              	document.DettaglioRevocaIndulto.submit();
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
        	<font class="campo">Dettaglio Revoca Indulto di altro Titolo Cumulato</font>
      	  </td>
          <td class="LBG">
       		<a href="javascript:VaiadInserire('siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo')">
       		 <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0"></a>
 <%		if(IstruttoriaCumulo.getFlagStato().equals("A") && !beneficioCumulo.getFlagStato().equals("C"))
 		{	 %>
       		<a href="javascript:VaiaModificare('siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo',<%=beneficioCumulo.getIdBeneficioCumulo()%>)">
       	     <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
    		<a href="javascript:VaiaCancellare('siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo',<%=beneficioCumulo.getIdBeneficioCumulo()%>,'<%=beneficioCumulo.getFlagStato()%>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(beneficioCumulo.getMotivoModifica()),"") %>')">
    		 <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0"></a>
<%		} %>       	
		  </td>
		  
  <!-- BOTTONE DI RITORNO -->
		<td class="LBG">
		  <a href="javascript:TornaIndietro('siap.siep.modulocumulo.action.ActRicercaRevocheBeneficiCumulo')">
 			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>
      	</td>      

      </tr>
	</table>
  </FORM>	
  <br>
       	<jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    	<jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioRevocaIndulto">
  
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
   	<input type="hidden" name="modalita" value="">
  
  	<input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO %>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO %>"      value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO %>"  	 value="<%=beneficioCumulo.getIdBeneficioCumulo() %>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_FLAG_STATO %>"  			 value="<%=beneficioCumulo.getFlagStato() %>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA %>"  		 value="<%=beneficioCumulo.getMotivoModifica() %>">
  	<input type="hidden" name="<%= ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO %>"  		 value="02">
  
 	<table cellspacing=2 cellpadding=2 width=90%>
		<tr>
			<td class="l" width=25%>Natura Beneficio</td>
			<td class="l"><font class="campo"><%=beneficioCumulo.getDescrNaturaBeneficio() %></font></td>
		</tr>
			
		<tr>
			<td class="l">Tipologia Beneficio</td>
    		<td class="l"><font class="campo"><%=beneficioCumulo.getDescrTipoBeneficio() %></font></td>
		</tr>
		<tr>
			<td class="l">Inserito il </td>
			<td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getDataInserimento(),"dd-MM-yyyy"))%></font></td>
		</tr>		        
			
		<tr>		
			<td class="l">D. P. R. </td>
			<td class="l"><font class="campo"><%=beneficioCumulo.getDescrDpr() %></font></td>
		</tr>

		<tr>		
			<td class="l">Multa </td>
	<%	if(beneficioCumulo.getImportoMulta().intValue() == 0)
		{ %>
			<td class=l>  -  </td>
	<% 	}
		else
		{	%>	
			<td class="l"><font class="campo"> Euro <%=beneficioCumulo.getImportoMulta() %></font></td>
	<% 	} %>			
		</tr>
			
		<tr>		
			<td class="l">Durata Reclusione</td>
<%		if((beneficioCumulo.getNumAnniReclusione().intValue()== 0) &&
			(beneficioCumulo.getNumMesiReclusione().intValue()== 0) &&
			(beneficioCumulo.getNumGiorniReclusione().intValue()== 0))
		{  %>	
			<td class=l>  -  </td>
	<% 	}
		else
		{	%>
			<td class=l><font class="campo">Anni&nbsp;<%=beneficioCumulo.getNumAnniReclusione()%>&nbsp; 
		 		 					 		Mesi&nbsp;<%=beneficioCumulo.getNumMesiReclusione()%>&nbsp;
		 		 					 		Giorni&nbsp;<%=beneficioCumulo.getNumGiorniReclusione()%>&nbsp;</font></td>
	<% 	} %>				 
		</tr>						

		<tr>		
			<td class="l">Ammenda </td>
	<%	if(beneficioCumulo.getImportoAmmenda().intValue() == 0)
		{ %>
			<td class=l>  -  </td>
	<% 	}
		else
		{	%>	
			<td class="l"><font class="campo"> Euro <%=beneficioCumulo.getImportoAmmenda() %></font></td>
	<% 	} %>			
		</tr>

		<tr>		
			<td class="l">Durata Arresto</td>
	<% 
		if((beneficioCumulo.getNumAnniArresto().intValue()== 0) &&
			(beneficioCumulo.getNumMesiArresto().intValue()== 0) &&
			(beneficioCumulo.getNumGiorniArresto().intValue()== 0))
		{	%>
			<td class=l>  -  </td>
	<% 	}
		else
		{	%>			
			<td class=l><font class="campo">Anni&nbsp;<%=beneficioCumulo.getNumAnniArresto()%>&nbsp;
			 	 		 					Mesi&nbsp;<%=beneficioCumulo.getNumMesiArresto()%>&nbsp;
			 	 		 					Giorni&nbsp;<%=beneficioCumulo.getNumGiorniArresto()%>&nbsp;</font></td>
	<% 	} %>
		</tr>
			
		<tr>
			<td class="l">Note</td>
			<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficioCumulo.getNote(),"-") %></font></td>
		</tr>
		<tr><td>&nbsp;</td></tr>
       	<tr>
			<td class="l">Tipo Provvedimento </td>
			<td class="l"><font class="campo"><%=beneficioCumulo.getDescrTipoProvvedimento() %></font></td>
		</tr>

<%		if(beneficioCumulo.getRifNumeroProvvedimento()!=null && 
		  !beneficioCumulo.getRifNumeroProvvedimento().equals("") )
		{	%>			
			<tr>
				<td class="l">Anno e Numero</td>
				<td class=l><font class="campo"><%=StringUtils.toStringJSP(beneficioCumulo.getRifAnnoProvvedimento() )%>
				/
				<%=StringUtils.toStringJSP(beneficioCumulo.getRifNumeroProvvedimento() )%></font></td>
			</tr>
<%		} %>			
		
		<tr>
			<td class="l">Data Provvedimento</td>
			<td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataProvvedimento(),"dd-MM-yyyy"))%></font></td>
		</tr>
		<tr>
			<td class="l">Emesso da</td>
			<td class=l><font class="campo"><%=beneficioCumulo.getDescrTipoAutoEmittente()%> 
			di 
			<%=beneficioCumulo.getDescrLuogoEmittente()%></font></td>
		</tr>
	<%	String Sezio = beneficioCumulo.getRifNumSezioneAutoEmittente();
		if (Sezio != null)
		{	%>  
		<tr>
			<td class="l">Sezione</td>
			<td class=l><font class="campo"><%=beneficioCumulo.getRifNumSezioneAutoEmittente()%> 
			</font></td>
		</tr>					
<%		}	%>

<%		if(beneficioCumulo.getRifDataIrrevocabilita()!=null)
		{%>						
			<tr>
				<td class="l">Irrevocabile il</td>
				<td class=l><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(beneficioCumulo.getRifDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
			</tr>
<%		} %>						
			
	</table>
	
<%	String lStato = "";
	String lDescStato = "";

	if      ( beneficioCumulo.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	else if ( beneficioCumulo.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	else if ( beneficioCumulo.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	else if ( beneficioCumulo.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato";}
%>
	<br>
	<table cellspacing=2 cellpadding=2 width="90%">
		<tr>
	      <td class="l" width="25%">Stato</td>
	      <td class="l">
	        <font class="campo">&nbsp;<%=lStato%>&nbsp;&nbsp;(<%=lDescStato%>)</font>
	      </td>
		</tr>

	<%	if( beneficioCumulo.getMotivoModifica() != null )
		{	%>	
			<tr>
		      <td class="l">Motivo Inserimento/Modifica</td>
		      <td class="l">
		        <font class="campo"><%=StringUtils.toStringJSP(beneficioCumulo.getMotivoModifica())%>&nbsp;</font>
		      </td>
			</tr>
	<%	} 	%>
	
	</table>
	
	</FORM>
	</body>
</html>