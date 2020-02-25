<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioPenaAccessoria_CumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaBenefici"        scope="request" class="java.util.Vector"/>
<jsp:useBean id="beneficiopenaaccessoria" scope="request" class="java.util.Vector" />

<%
//==============================================================================
// Form per la visualizzazione dei Benefici legati a un certo Titolo.
//
// La form presenta un elenco dei Benefici già presenti con la possibilità di 
// modificarle, cancellarle o inserirne delle nuove 
//==============================================================================
String lNomeForm = "formListaBeneficiCumulo";
%>

<!-- 		ElencoBeneficiCumulo		 -->
<html>
<head>
  <title> Elenco Benefici (in sentenza)</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    
    function visualizzaMotivoIndu(idRecord)
    {
      var riga = document.getElementById(idRecord);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }
    
    function visualizzaMotivoBen(idRecord)
    {
      var riga = document.getElementById(idRecord);  
      if (riga.style.display =="none" )
      {
        riga.style.display = "block";
      }
      else 
      {
        riga.style.display = "none";
      }
    }
    
    function visualizzaPA(idRecord, idImg)
    {
      var collapseGif = "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
      var espandiGif = "<%=IWebConstants.IMAGES_DIR%>expand.gif";
      
   
      var riga = document.getElementById(idRecord);
      if (riga.style.display =="none" )
      {
        $('#'+idImg).attr('src',collapseGif);
        riga.style.display = "block";
      }
      else 
      {
        $('#'+idImg).attr('src',espandiGif);
        riga.style.display = "none";
      }
    }
    
    function eseguiAzione(aTipoAzione, aIdRecord, aTipoForm, aStato, aMotivoModifica)
    {
    	document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO%>.value = aTipoForm;
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioBeneficioCumulo";
        document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>.value = aIdRecord;
        document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.<%=lNomeForm%>.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo";
        document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>.value = aIdRecord;
        document.<%=lNomeForm%>.modalita.value = "M";
        document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.<%=lNomeForm%>.submit();
      }
      else if (aTipoAzione=='Cancella'){
        // Cancellazione fisica richiedo conferma
        var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
        if (window.confirm(msgConfirm)) {
          lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo";
          
          document.<%=lNomeForm%>.modalita.value = "C";
          document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
          document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>.value = aIdRecord;
          document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>.value = aStato;

          document.<%=lNomeForm%>.submit();
        }
      }
    }
    
   
    //==========================================================================
    // Richiama la funzione di inserimento
    //==========================================================================
    function nuovaAnnotazione(tipoForm){
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciBeneficiCumulo";
      document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
      document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO%>.value = tipoForm;
      
      document.<%=lNomeForm%>.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia dei dati analitici
    //==========================================================================
    function tornaIndietro(action)
    {
      document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.<%=lNomeForm%>.submit();
    }

  </script>
  
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione:</font>&nbsp;&nbsp;
                      <font class="campo">Elenco Benefici &nbsp;</font>
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia dei dati analitici -->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaDatiAnalitici')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>


<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="<%=lNomeForm%>">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>"     value="">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>"          value="">
    
  <input type="hidden" name="modalita" value="">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO%>" value="">
 
 <%if (beneficiopenaaccessoria == null || beneficiopenaaccessoria.size() == 0 )	
   { %>
    <table cellspacing="2" cellpadding="2" align="center" width="95%">
	    <tr>
	      <td class="int">Tipo Beneficio</td>
	      <td class="int">Provvedimento di concessione</td>
	      <td class="int">Quantum Reclusione</td>
	      <td class="int">Importo Multa</td>
	      <td class="int">Quantum Arresto</td>
	      <td class="int">Importo Ammenda</td>
	      <td class="int">Pena Accessoria</td>
	    </tr>
    	<tr>
      		<td class="l" colspan="7"><center>Nessun dato presente</center></td>
    	</tr>
    </table>
    
    <table cellspacing="2" cellpadding="2" align="center" width="95%">
      	<tr>
          <td class="int">Tipo Beneficio</td>
          <td class="int">Natura Beneficio</td>
          <td class="int">Tipologia obbligo</td>
          <td class="int">Durata prestazione</td>
          <td class="int">Durata sospensione condizionale della pena</td>
          <td class="int">Revocato</td>
       	</tr>
   	  	<tr>
      	  <td class="l" colspan="6"><center>Nessun dato presente</center></td>
    	</tr>
    </table>	
<% }
   else
   {	%> 
   
<!-- 										Beneficio di Tipo Indulto o Amnistia															 -->
	  <table cellspacing="2" cellpadding="2" align="center" width="95%">
	    <tr>
	      <td class="int">Tipo Beneficio</td>
	      <td class="int">Provvedimento di concessione</td>
	      <td class="int">Quantum Reclusione</td>
	      <td class="int">Importo Multa</td>
	      <td class="int">Quantum Arresto</td>
	      <td class="int">Importo Ammenda</td>
	      <td class="int">Pena Accessoria</td>
	      <td class="int">Revocato</td>
	      <%//===================================== %>
	      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
	      <%//===================================== %>
	      <td class="int" width=5%>Azioni</td>
	    </tr>
	
	    <%
	    int id_record = 0;
	    
	    Iterator itx = beneficiopenaaccessoria.iterator();
	    while ( itx.hasNext())
	    { 
	  	  BeneficioPenaAccessoria_CumuloModel lBenPenMod = (BeneficioPenaAccessoria_CumuloModel)itx.next();
	  	  
	  	  if("03".equals(lBenPenMod.getBeneficioCumulo().getCodTipoBeneficio()) || "04".equals(lBenPenMod.getBeneficioCumulo().getCodTipoBeneficio()))
	  	  {
	  		id_record = id_record +1;  
	  		BeneficioCumuloModel beneficio = lBenPenMod.getBeneficioCumulo();
	  		
	  		String lStato = "";
	        String lDescStato = "";
	        String lFontColor = "";
	  		
	      	if      ( beneficio.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	      	else if ( beneficio.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	      	else if ( beneficio.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	      	else if ( beneficio.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
	      	
	      // Preparazione Quantum Reclusione e Arresto
			 String lReclusione = "";
	         if(beneficio.getNumAnniReclusione() != null && beneficio.getNumAnniReclusione().intValue() > 0)
	         	lReclusione +="Anni:"+beneficio.getNumAnniReclusione().toString()+" ";
	         if(beneficio.getNumMesiReclusione()!= null && beneficio.getNumMesiReclusione().intValue() > 0)
	         	lReclusione +="Mesi:"+beneficio.getNumMesiReclusione().toString()+" ";
	         if(beneficio.getNumGiorniReclusione()!= null && beneficio.getNumGiorniReclusione().intValue() > 0)
	         	lReclusione +="Giorni:"+beneficio.getNumGiorniReclusione().toString()+" ";
	         
	         if(lReclusione.compareTo("")==0)
	         	lReclusione = " - ";
	         
	         String lArresto = "";
	         if(beneficio.getNumAnniArresto() != null && beneficio.getNumAnniArresto().intValue() > 0)
	         	lArresto +="Anni:"+beneficio.getNumAnniArresto().toString()+" ";
	         if(beneficio.getNumMesiArresto()!= null && beneficio.getNumMesiArresto().intValue() > 0)
	         	lArresto +="Mesi:"+beneficio.getNumMesiArresto().toString()+" ";
	         if(beneficio.getNumGiorniArresto()!= null && beneficio.getNumGiorniArresto().intValue() > 0)
	         	lArresto +="Giorni:"+beneficio.getNumGiorniArresto().toString()+" ";
	         
	         if(lArresto.compareTo("")==0)
	         	lArresto = " - ";
	         
	       // Preparazione Importo Multa e Ammenda
	      	String multa=new String("");
	      	if (beneficio.getImportoMulta()== null || beneficio.getImportoMulta().compareTo(new BigDecimal(0))==0  )
	        	multa=" - ";
	      	else
	        	multa="€ "+ StringUtils.toEuroFormat(beneficio.getImportoMulta());
	      	
	         String ammenda=new String("");
	         if (beneficio.getImportoAmmenda()== null || beneficio.getImportoAmmenda().compareTo(new BigDecimal(0))==0  )
	           ammenda = " - ";
	         else
	           ammenda = "€ "+ StringUtils.toEuroFormat(beneficio.getImportoAmmenda());
	 	
	         // Preparazione Revoca
	         String lRevoca="";
	         if(beneficio.getTitIdTitoloCumulatoCollegato() !=null )
	         {
	         	TitoloCumulatoModel lTitoloRevoca = lBenPenMod.getTitoloCumulatoRevocante();
	         	if(lTitoloRevoca!=null && lTitoloRevoca.getIdTitoloCumulato()!=null)
	         	{
	         		lRevoca = lTitoloRevoca.getEstremiProvvedimento()+" [in istruttoria]";
	         	}
	         }
	    %>
	    <tr>
	      <%// Inserire qui le get dei campi da visualizzare %>
	      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio(),"&nbsp;")%></td>
	      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(beneficio.getDescrDpr(),"&nbsp;")%></td>
      
	      <td class="c" <%=lFontColor%> >&nbsp;<%=lReclusione%></td>
	      <td class="c" <%=lFontColor%> >&nbsp;<%=multa%></td>
	      
	      <td class="c" <%=lFontColor%> >&nbsp;<%=lArresto%></td>
	      <td class="c" <%=lFontColor%> >&nbsp;<%=ammenda%></td>
	
		<!-- 		Presenza Pena/e Accessoria/e	 	-->
	<%	if(lBenPenMod != null && lBenPenMod.getPresenzaPenaAccessoriaCumulo()) 
		{ %>    
	    	<td class=C <%=lFontColor%> >
	 			&nbsp;<%=StringUtils.toStringJSP("<img src='/images/TickRed.gif'/>","&nbsp;")%>
				<a href="Javascript:visualizzaPA('trPA_<%=id_record%>','img_<%=id_record%>');">
				<img align="middle" id="img_<%=id_record%>"  alt="Visualizza Pene Accessorie" src="<%=IWebConstants.IMAGES_DIR%>expand.gif" border="0"/></a>     		
	    	</td>
	<%	}else{ %>
	    	<td class=c>&nbsp;</td>
	 <%	} %>
	      
	      <!-- 		Presenza di Revoca 	-->
	<%	if(beneficio.getTitIdTitoloCumulatoCollegato()!=null)
		{ %>      
	      	<td class="c" <%=lFontColor%> >
	        	&nbsp;<%=StringUtils.toStringJSP("<img src='/images/TickRed.gif'/>","&nbsp;")%>
	      	</td>
	<%	}
		else
		{ %>  
			<td class=c>&nbsp;</td>
	<%	} %>	    	
	      
	      <!-- 		Motivo Inserimento/Modifica 	-->
	<%	if (beneficio.getMotivoModifica()!=null && beneficio.getMotivoModifica().length()>0)
	   	{ %>
	      	<td class="c">
	      	<a href="javascript:visualizzaMotivoIndu('indu_rec_<%=id_record%>')" title="<%=lDescStato%>">
	       		<%=lStato%>
	       	</a>
	       	</td>
	<%  }
		else
	    {	%>	
	    	<td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
	<% 	} %>
	      
	      <td class="c" style="text-align:center" nowrap > &nbsp;
	      <%
	        //======================================================================
	        // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
	       //======================================================================
	      %>
	        <a href="javascript:eseguiAzione('Dettaglio', <%=beneficio.getIdBeneficioCumulo() %>, '02' )">
	          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
	        
	       <% 
	       //======================================================================
	       // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
	       //======================================================================
	       if( IstruttoriaCumulo.getFlagStato().equals("A") && !beneficio.getFlagStato().equals("C") )
	       {  %>

	        <a href="javascript:eseguiAzione('Modifica',<%=beneficio.getIdBeneficioCumulo()%>,'02')">
	          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
	          
	        <a href="javascript:eseguiAzione('Cancella', <%=beneficio.getIdBeneficioCumulo()%>, '02' , '<%=beneficio.getFlagStato()%>' , '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(beneficio.getMotivoModifica()),"")%>')">
	          <img src="/images/delete.gif" width="12" height="12" alt="Elimina Indulto" border="0"></a>
        <% } %>
	      </td>
	    </tr>

	    <!-- Riga Hidden con Descrizione di pena Accessoria associata al beneficio. Visibile solo se Cliccata -->	    
	    <tr style="display:none" id="trPA_<%=id_record%>">
	      <td class="l" colspan="100%">
	        <font class="label">Pena Accessoria Condonata: </font>
	        
	  <%if(lBenPenMod!=null && lBenPenMod.getListaPACumulo()!=null &&
		   lBenPenMod.getListaPACumulo().size()>0 )
	   	{	
		  	String lDescrizione="";
		  	int Num=0;
		  	Iterator itxPA = lBenPenMod.getListaPACumulo().iterator();
		  	while(itxPA.hasNext())
		  	{	
		  		PenaAccessoriaCumuloModel lPenAcsCum = (PenaAccessoriaCumuloModel)itxPA.next();
				Num=Num+1;
				lDescrizione +=Num+") ";
		   		if(lPenAcsCum.getDescrAltrePA()!=null) 
		   		{
		   			lDescrizione += lPenAcsCum.getDescrAltrePA()+" -DURATA: "+StringUtils.toStringJSP(lPenAcsCum.getDescrDurata(),"");		   		
		 		}
		   		else if(lPenAcsCum.getDescrTipoPenaAccessoria()!=null)
		   		{	
		   			lDescrizione += lPenAcsCum.getDescrTipoPenaAccessoria()+" -DURATA: "+StringUtils.toStringJSP(lPenAcsCum.getDescrDurata(),"");	
				}
		   		else
		   		{ 
		   			lDescrizione += lPenAcsCum.getCodTipoPenaAccessoria()+" -DURATA: "+StringUtils.toStringJSP(lPenAcsCum.getDescrDurata(),"");	
		   		}	
		   		
		   		lDescrizione +="; ";
		  	
		  	}	%>
		  	
		   		<font class="campoSmall"><%=StringUtils.toStringJSP(lDescrizione)%></font>
<%		} 	%>		    	
	      </td>
	    </tr>
	    
	<!-- Riga visibile solo in caso di Beneficio Revocato -->
	<%	if(beneficio.getTitIdTitoloCumulatoCollegato()!=null )
		{	%>	
		<tr style="display:block" id="r<%=id_record%>">
	      <td class="l" colspan="100%">
	      	<font class="campoSmall" style="color:red;">REVOCATO</font>
	        <font class="campoSmall"><%=lRevoca%></font>
	      </td>
	    </tr>	    
	<%	} %>
	
	    <!-- Riga Hidden con la descrizione di Motivo Modifica : Visibile solo se 'Cliccata' -->
	    <tr style="display:none" id="indu_rec_<%=id_record%>">
	      <td class="l" colspan="100%">
	        <font class="campoSmall">
	        <%=StringUtils.toStringJSP(beneficio.getMotivoModifica(),"&nbsp;")%>
	        </font>
	      </td>
	    </tr>

<%    } // end IF Tipo_Beneficio = 03 or 04
	} // end while su iterator %>
    
  	</table>

<!-- 											Beneficio di tipo Sospensione, Non Menzione											 -->
<br>
    <table cellspacing="2" cellpadding="2" align="center" width="95%">
      <tr>
        <td class="int">Tipo Beneficio</td>
        <td class="int">Natura Beneficio</td>
        <td class="int">Tipologia obbligo</td>
        <td class="int">Durata prestazione</td>
        <td class="int">Durata sospensione condizionale della pena</td>
        <td class="int">Revocato</td>
        <%//===================================== %>
        <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
        <%//===================================== %>
        <td class="int" width=5%>Azioni</td>
      </tr>
      
	<%	id_record = 0;
   		Iterator itxS = beneficiopenaaccessoria.iterator();
		while ( itxS.hasNext())
		{
		  	BeneficioPenaAccessoria_CumuloModel lBenPenMod = (BeneficioPenaAccessoria_CumuloModel)itxS.next();
			if("01".equals(lBenPenMod.getBeneficioCumulo().getCodTipoBeneficio()) || "02".equals(lBenPenMod.getBeneficioCumulo().getCodTipoBeneficio()))
		  	{  
		  		id_record = id_record +1;  
		  		BeneficioCumuloModel beneficioSosp = lBenPenMod.getBeneficioCumulo();
		  		
		  		String lStato = "";
		        String lDescStato = "";
		        String lFontColor = "";
		        
		      	if      ( beneficioSosp.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
		      	else if ( beneficioSosp.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
		      	else if ( beneficioSosp.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
		      	else if ( beneficioSosp.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
		      	
		      // Preparazione Quantum Prestazione e Sospensione
				 String lPrestazione = "";
		         if(beneficioSosp.getNumMesiPrestazione() != null && beneficioSosp.getNumMesiPrestazione().intValue() > 0)
		         	lPrestazione +="Mesi: "+beneficioSosp.getNumMesiPrestazione().toString()+"  ";
		         if(beneficioSosp.getNumGiorniPrestazione()!= null && beneficioSosp.getNumGiorniPrestazione().intValue() > 0)
		         	lPrestazione +="Giorni: "+beneficioSosp.getNumGiorniPrestazione().toString()+"  ";
		         
		         if(lPrestazione.compareTo("")==0)
		         	lPrestazione = " - ";	
		         
		         String lSospensione = "";
		         if(beneficioSosp.getNumAnniSospensione() != null && beneficioSosp.getNumAnniSospensione().intValue() > 0)
				 	lSospensione +=" Anni: "+beneficioSosp.getNumAnniSospensione().toString()+"";
		         
		         if(lSospensione.compareTo("")==0)
		         	lSospensione = " - ";
		         
		         // Preparazione Revoca
		         String lRevoca="";
		         if(beneficioSosp.getTitIdTitoloCumulatoCollegato()!=null )
		         {
		         	TitoloCumulatoModel lTitoloRevoca = lBenPenMod.getTitoloCumulatoRevocante();
		         	if(lTitoloRevoca!=null && lTitoloRevoca.getIdTitoloCumulato()!=null)
		         	{
		         		lRevoca = lTitoloRevoca.getEstremiProvvedimento()+" [in istruttoria]";
		         	}
		         }
	%>      
	      
	      <tr>
	      	<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(beneficioSosp.getDescrTipoBeneficio(),"&nbsp;")%></td>
	      	<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(beneficioSosp.getDescrNaturaBeneficio(),"&nbsp;")%> / 
	      										 <%=StringUtils.toStringJSP(beneficioSosp.getDescrSottotipoBeneficio(),"&nbsp;")%></td>
			<td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(beneficioSosp.getDescrTipoSospSubordinata(),"&nbsp;")%></td>
      
	      	<td class="c" <%=lFontColor%> >&nbsp;<%=lPrestazione%></td>
 	      	<td class="c" <%=lFontColor%> >&nbsp;<%=lSospensione%></td>

	      <!-- 		Presenza di Revoca 	-->
	<%	if(beneficioSosp.getTitIdTitoloCumulatoCollegato()!=null)
		{ %>      
	      	<td class="c" <%=lFontColor%> >
	        	&nbsp;<%=StringUtils.toStringJSP("<img src='/images/TickRed.gif'/>","&nbsp;")%>
	      	</td>
	<%	}
		else
		{ %>  
			<td class=c>&nbsp;</td>
	<%	} %>	    	

    <%//================================================================================== %>
	        
	    <!-- 		Motivo Inserimento/Modifica 	-->    
	<%	if (beneficioSosp.getMotivoModifica()!=null && beneficioSosp.getMotivoModifica().length()>0)
	   	{ %>
	      	<td class="c">
	      	<a href="javascript:visualizzaMotivoBen('rec_<%=id_record%>')" title="<%=lDescStato%>">
	       		<%=lStato%>
	       	</a>
	       	</td>
	<%  }
		else
	    {	%>	
	    	<td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
	<% 	} %>	        
    <%//================================================================================= %>

	      <td class="c" style="text-align:center" nowrap> &nbsp;
	        <a href="javascript:eseguiAzione('Dettaglio', <%=beneficioSosp.getIdBeneficioCumulo()%>, '01')">
	          <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0"></a>
  <%  if( IstruttoriaCumulo.getFlagStato().equals("A") && !beneficioSosp.getFlagStato().equals("C") )
        {  
        	if(!beneficioSosp.getCodTipoBeneficio().equals("02") )
        	{ %>
	        	<a href="javascript:eseguiAzione('Modifica', <%=beneficioSosp.getIdBeneficioCumulo() %>, '01')">
	          	<img src="/images/modifica.gif" width="12" height="12" alt="Modifica" border="0"></a>
	    <%	} %>      	
	        <a href="javascript:eseguiAzione('Cancella', <%=beneficioSosp.getIdBeneficioCumulo()%>, '01', '<%=beneficioSosp.getFlagStato()%>','<%=StringUtils.toStringJSP(StringUtils.cStrForJS(beneficioSosp.getMotivoModifica()),"")%>')">
	          <img src="/images/delete.gif" width="12" height="12" alt="Elimina" border="0"></a>
     <% } %>
	      </td>
      
      </tr>
		
		<!-- Riga visibile solo in caso di Beneficio Revocato -->
	<%	if(beneficioSosp.getTitIdTitoloCumulatoCollegato()!=null )
		{	%>	
		<tr style="display:block" id="r<%=id_record%>">
	      <td class="l" colspan="100%">
	      	<font class="campoSmall" style="color:red;">REVOCATO</font>
	        <font class="campoSmall"><%=lRevoca%></font>
	      </td>
	    </tr>	    
	<%	} %>
			      
	    <!-- Riga Hidden con la descrizione di Motivo Modifica (Beneficio Sosp): Visibile solo se 'Cliccata' -->
	    <tr style="display:none" id="rec_<%=id_record%>">
	      <td class="l" colspan="100%">
	        <font class="campoSmall">
	        <%=StringUtils.toStringJSP(beneficioSosp.getMotivoModifica(),"&nbsp;")%>
	        </font>
	      </td>
	    </tr>				      
	
	<%    } // end IF Tipo_Beneficio = 01 or 02
		} // end while su iterator %>
					            
  </table>

<%	} // end else di if beneficiopenaaccessoria == null ...  %>
   
  <br>  
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci Pena Sospesa - Non Menzione" style="width:250px" onClick="javascript:nuovaAnnotazione('<%=ICostantiBeneficiCumulo.TIPO_FORM_SOSPENSIONE%>');">
        <INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci Indulto/Amnistia" style="width:250px" onClick="javascript:nuovaAnnotazione('<%=ICostantiBeneficiCumulo.TIPO_FORM_INDULTO%>');">
      </td>
    </tr>
    <% } %>
  </table>
</FORM>
</body>
</html>