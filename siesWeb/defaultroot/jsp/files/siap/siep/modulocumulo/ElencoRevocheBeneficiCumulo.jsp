<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils" %>

<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.model.BeneficioCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>

<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiBeneficiCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ListaRevocheBenefici" scope="request" class="java.util.Vector"/>

<%
//================================================================================================
// Form per la visualizzazione dei Benefici Revocati (REVOCHE) legati a un certo Titolo Cumulato.
//
// La form presenta un elenco di REVOCHE già presenti con la possibilità di 
// modificarle, cancellarle o inserirne di nuove 
//================================================================================================
String lNomeForm = "formListaRevocheBeneficiCumulo";
%>

<!-- 			ElencoRevocheBeneficiCumulo				 -->
<html>
<head>
  <title> Elenco Revoche Benefici (in sentenza)</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript">
    //==========================================================================
    // Visualizza, nasconde il record con il dettaglio dell'istruttoria
    //==========================================================================
    function visualizzaMotivoRevocaIndulto(idRecord)
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
    
    function visualizzaMotivoRevocaSospe(idRecord)
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
    
    //==========================================================================
    // Richiama l'opportuna azione
    //==========================================================================
    function eseguiAzione(aTipoAzione, aIdRecord, aTipoForm, aStato, aMotivoModifica)
    {
    	document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO%>.value = aTipoForm;
      if (aTipoAzione=='Dettaglio'){
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioRevocaBeneficioCumulo";
        document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>.value = aIdRecord;
        document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.<%=lNomeForm%>.submit();
      }
      else if (aTipoAzione=='Modifica'){
        lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo";
        document.<%=lNomeForm%>.<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>.value = aIdRecord;
        document.<%=lNomeForm%>.modalita.value = "M";
        document.<%=lNomeForm%>.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.<%=lNomeForm%>.submit();
      }
      else if (aTipoAzione=='Cancella'){
         // Cancellazione fisica richiedo conferma
         var msgConfirm = "Si vuole procedere con la cancellazione dei dati?"; 
         if (window.confirm(msgConfirm)) {
           lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo";
           
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
      lAzione = "siap.siep.modulocumulo.action.ActLoadInserisciRevocaBeneficioCumulo";
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
                      <font class="campo">Elenco Revoche Benefici &nbsp;</font>
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

<FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="<%=lNomeForm%>">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  
  <!-- Campi sempre presenti sulle form dei dati analitici -->
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

  
  <!-- Campi valorizzati dinamicamente dalla eseguiAzione() -->
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_ID_BENEFICIO_CUMULO%>" value="">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA%>"     value="">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.CAMPO_FLAG_STATO%>"          value="">
  <input type="hidden" name="<%=ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO%>"       value="">
  
  <input type="hidden" name="modalita" value="">
<%
if (ListaRevocheBenefici == null || ListaRevocheBenefici.size()==0 )
{ %>
	<table cellspacing="2" cellpadding="2" align="center" width="95%">
   	  <tr>
   		<td class="int">Beneficio Revocato</td>
   		<td class="int">Provvedimento di concessione</td>
   		<td class="int">Quantum Reclusione</td>
   		<td class="int">Importo Multa</td>
   		<td class="int">Quantum Arresto</td>
   		<td class="int">Importo Ammenda</td>
   		<td class="int">Stato</td>
   		<td class="int">Azioni</td>
  	  </tr>
   	  <tr>
   		<td colspan="8"><center>Nessun dato presente</center></td>
   	  </tr>
   	</table>
    	
   	<table cellspacing="2" cellpadding="2" align="center" width="95%">
   	  <tr>
       	<td class="int">Beneficio Revocato</td>
       	<td class="int">Provvedimento di concessione</td>
       	<td class="int">Stato</td>
       	<td class="int">Azioni</td>
      </tr>
   	  <tr>
   		<td colspan="4"><center>Nessun dato presente</center></td>
  	  </tr>
  </table>
<%
}
else
{	%>
<!-- 					Revoca Beneficio di Tipo Indulto					 -->		
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
    <tr>
      <td class="int">Beneficio Revocato</td>
      <td class="int">Provvedimento di concessione</td>
      <td class="int">Quantum Reclusione</td>
      <td class="int" width=10%>Importo Multa</td>
      <td class="int">Quantum Arresto</td>
      <td class="int" width=10%>Importo Ammenda</td>
      <%//=================================================================================== %>
      <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
      <%//=================================================================================== %>
      <td class="int" width=5%>Azioni</td>
    </tr>

<%
   	int id_record = 0;
	Iterator itxI = ListaRevocheBenefici.iterator();
	while ( itxI.hasNext())
	{
		BeneficioCumuloModel lRevocaIndu = (BeneficioCumuloModel)itxI.next();
		if("03".equals(lRevocaIndu.getCodTipoBeneficio() ) )
		{  
			id_record = id_record +1;  
		
			String lStato = "";
		    String lDescStato = "";
		    String lFontColor = "";
     
		   	if      ( lRevocaIndu.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
		   	else if ( lRevocaIndu.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
		   	else if ( lRevocaIndu.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
		   	else if ( lRevocaIndu.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
      // Preparazione Quantum Reclusione e Arresto
		 String lReclusione = "";
         if(lRevocaIndu.getNumAnniReclusione() != null && lRevocaIndu.getNumAnniReclusione().intValue() > 0)
         	lReclusione +="Anni:"+lRevocaIndu.getNumAnniReclusione().toString()+" ";
         if(lRevocaIndu.getNumMesiReclusione()!= null && lRevocaIndu.getNumMesiReclusione().intValue() > 0)
         	lReclusione +="Mesi:"+lRevocaIndu.getNumMesiReclusione().toString()+" ";
         if(lRevocaIndu.getNumGiorniReclusione()!= null && lRevocaIndu.getNumGiorniReclusione().intValue() > 0)
         	lReclusione +="Giorni:"+lRevocaIndu.getNumGiorniReclusione().toString()+" ";
		         
         if(lReclusione.compareTo("")==0)
         	lReclusione = " - ";
		         
         String lArresto = "";
         if(lRevocaIndu.getNumAnniArresto() != null && lRevocaIndu.getNumAnniArresto().intValue() > 0)
         	lArresto +="Anni:"+lRevocaIndu.getNumAnniArresto().toString()+" ";
         if(lRevocaIndu.getNumMesiArresto()!= null && lRevocaIndu.getNumMesiArresto().intValue() > 0)
         	lArresto +="Mesi:"+lRevocaIndu.getNumMesiArresto().toString()+" ";
         if(lRevocaIndu.getNumGiorniArresto()!= null && lRevocaIndu.getNumGiorniArresto().intValue() > 0)
         	lArresto +="Giorni:"+lRevocaIndu.getNumGiorniArresto().toString()+" ";
		         
         if(lArresto.compareTo("")==0)
         	lArresto = " - ";
		         
       // Preparazione Importo Multa e Ammenda
      	String lmulta=new String("");
      	if (lRevocaIndu.getImportoMulta()== null || lRevocaIndu.getImportoMulta().compareTo(new BigDecimal(0))==0  )
        	lmulta=" - ";
      	else
        	lmulta="€ "+ StringUtils.toEuroFormat(lRevocaIndu.getImportoMulta());
		      	
         String lammenda=new String("");
         if (lRevocaIndu.getImportoAmmenda()== null || lRevocaIndu.getImportoAmmenda().compareTo(new BigDecimal(0))==0  )
           lammenda = " - ";
         else
	       lammenda = "€ "+ StringUtils.toEuroFormat(lRevocaIndu.getImportoAmmenda());

         // Preparazione dati sul titolo (provvedimento di concessione Indulto) 
         String lSentenza = "";
         String lDataRifProv = "-";
         
         if(lRevocaIndu.getRifDataProvvedimento()!=null)
         	lDataRifProv = DateUtils.getDateToString(lRevocaIndu.getRifDataProvvedimento(),"dd-MM-yyyy");
         if(lRevocaIndu.getDescrTipoProvvedimento()!=null)	
         	lSentenza += lRevocaIndu.getDescrTipoProvvedimento()+" ";
         if(lRevocaIndu.getRifNumeroProvvedimento()!=null)
         	lSentenza +="N. "+lRevocaIndu.getRifAnnoProvvedimento()+"/"+lRevocaIndu.getRifNumeroProvvedimento()+" ";
         if(lRevocaIndu.getRifDataProvvedimento()!=null)
         	lSentenza +="del "+DateUtils.getDateToString(lRevocaIndu.getRifDataProvvedimento(),"dd-MM-yyyy")+" ";
         if(lRevocaIndu.getDescrTipoAutoEmittente()!=null)
         {
         	if(lRevocaIndu.getRifCodTipoProvvedimento().compareTo("02")== 0 )	// Decreto
         		lSentenza +="Emesso da "+lRevocaIndu.getDescrTipoAutoEmittente()+" di "+lRevocaIndu.getDescrLuogoEmittente();
         	else 
         		lSentenza +="Emessa da "+lRevocaIndu.getDescrTipoAutoEmittente()+" di "+lRevocaIndu.getDescrLuogoEmittente();
         }	
         
%>

    <tr>
      <td class="c" <%=lFontColor%> >&nbsp;<%=StringUtils.toStringJSP(lRevocaIndu.getDescrTipoBeneficio(),"&nbsp;")%>: <%=StringUtils.toStringJSP(lRevocaIndu.getDescrDpr(),"&nbsp;")%></td>

      <td class="c" <%=lFontColor%> >
<%	  if(lRevocaIndu.getTitIdTitoloCumulatoCollegato()!=null)
	  { %>      
      	&nbsp;<%=lSentenza%>
<%	  }
	  else
	  {	%>      	 
        <img src="/images/attenzione.jpg" width="12" height="12" alt="Revoca non ancora associata al Beneficio Concesso" border="0">
        &nbsp;<%=lSentenza%>
<%	  } %>        
      </td>
        
      <td class="c" <%=lFontColor%> >&nbsp;<%=lReclusione%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=lmulta%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=lArresto%></td>
      <td class="c" <%=lFontColor%> >&nbsp;<%=lammenda%></td>
      <%//===================================== %>
<%	if(lRevocaIndu.getMotivoModifica()!=null)
	{	%>
      <td class="c">
        <a href="javascript:visualizzaMotivoRevocaIndulto('indu_rec_<%=id_record%>')" title="<%=lDescStato%>">
          <%=lStato%>
        </a>
      </td>
 <%	}
	else
	{%>     
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
<%	} %>        
      
      <td class="c" style="text-align:center" nowrap> &nbsp;
      <%
      //======================================================================
      // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
      //======================================================================
      %>
      <a href="javascript:eseguiAzione('Dettaglio', <%=lRevocaIndu.getIdBeneficioCumulo()%>, '02' )">
        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Revoca" border="0"></a>
     <% 
     //======================================================================
     // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
     //======================================================================
     if(IstruttoriaCumulo.getFlagStato().equals("A") && !lRevocaIndu.getFlagStato().equals("C") )
     {
     %>
	    <a href="javascript:eseguiAzione('Modifica', <%=lRevocaIndu.getIdBeneficioCumulo()%>, '02')">
          <img src="/images/modifica.gif" width="12" height="12" alt="Modifica Revoca" border="0"></a>
        <a href="javascript:eseguiAzione('Cancella', <%=lRevocaIndu.getIdBeneficioCumulo()%>, '02', '<%=lRevocaIndu.getFlagStato()%>', '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lRevocaIndu.getMotivoModifica()),"")%>')">
	      <img src="/images/delete.gif" width="12" height="12" alt="Elimina Revoca" border="0"></a>
  <%  } %>
      </td>
    </tr>
    
 <!-- Riga Hidden con la descrizione di Motivo Modifica : Visibile solo se si 'clicca' sulla colonna "Stato Revoca Indulto"	-->
    <tr style="display:none" id="indu_rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lRevocaIndu.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
<%
		} // end IF Tipo_Beneficio = 03
	} // end while su iterator
%>

  </table>
<br>

<!--			Revoca Beneficio tipo  Sospenzione condizionale / non Menzione			-->

    <table cellspacing="2" cellpadding="2" align="center" width="95%">
      <tr>
        <td class="int">Beneficio Revocato</td>
        <td class="int">Provvedimento di concessione</td>
        <td class="int" title="Indica la natura dei dati rispetto a quelli importati">Stato</td>
        <%//===================================== %>
        <td class="int" width=10%>Azioni</td>
      </tr>
      
<%
id_record = 0;
Iterator itxS = ListaRevocheBenefici.iterator();
while ( itxS.hasNext())
{
	BeneficioCumuloModel lRevocaSosp = (BeneficioCumuloModel)itxS.next();
	if("01".equals(lRevocaSosp.getCodTipoBeneficio()) || "02".equals(lRevocaSosp.getCodTipoBeneficio() ) )
	{  
		id_record = id_record +1;  
		
		String lStato = "";
	    String lDescStato = "";
	    String lFontColor = "";
	     
	   	if      ( lRevocaSosp.getFlagStato().equals("E")){lStato = "Estratto";   lDescStato = "Dato Estratto originale";}
	   	else if ( lRevocaSosp.getFlagStato().equals("I")){lStato = "Iscritto";   lDescStato = "Inserito manualmente dopo l'estrazione";}
	   	else if ( lRevocaSosp.getFlagStato().equals("M")){lStato = "Modificato"; lDescStato = "Dato estratto modificato";}
	   	else if ( lRevocaSosp.getFlagStato().equals("C")){lStato = "Cancellato"; lDescStato = "Dato estratto cancellato"; lFontColor="style=\"color:gray\"";}
         
     // Preparazione dati sul titolo (provvedimento di concessione Sospensione/Non Menzione) 
      String lSentenza = "";
      String lDataRifProv = "-";
      
      if(lRevocaSosp.getRifDataProvvedimento()!=null)
      	lDataRifProv = DateUtils.getDateToString(lRevocaSosp.getRifDataProvvedimento(),"dd-MM-yyyy");
      if(lRevocaSosp.getDescrTipoProvvedimento()!=null)	
      	lSentenza += lRevocaSosp.getDescrTipoProvvedimento()+" ";
      if(lRevocaSosp.getRifNumeroProvvedimento()!=null)
      	lSentenza +="N. "+lRevocaSosp.getRifAnnoProvvedimento()+"/"+lRevocaSosp.getRifNumeroProvvedimento()+" ";
      if(lRevocaSosp.getRifDataProvvedimento()!=null)
      	lSentenza +="del "+DateUtils.getDateToString(lRevocaSosp.getRifDataProvvedimento(),"dd-MM-yyyy")+" ";
      if(lRevocaSosp.getDescrTipoAutoEmittente()!=null)
      {	
      	if(lRevocaSosp.getRifCodTipoProvvedimento().compareTo("02")== 0 )	// Decreto
      		lSentenza +="Emesso da "+lRevocaSosp.getDescrTipoAutoEmittente()+" di "+lRevocaSosp.getDescrLuogoEmittente();
      	else 
 	      	lSentenza +="Emessa da "+lRevocaSosp.getDescrTipoAutoEmittente()+" di "+lRevocaSosp.getDescrLuogoEmittente();
      }	

%>      
    <tr>
      <td class="c" <%=lFontColor%> ><%=StringUtils.toStringJSP(lRevocaSosp.getDescrTipoBeneficio(),"&nbsp;")%></td>

      <td class="c" <%=lFontColor%> >
<%	  if(lRevocaSosp.getTitIdTitoloCumulatoCollegato()!=null)
	  { %>      
      	&nbsp;<%=lSentenza%>
<%	  }
	  else
	  {	%>      	 
        <img src="/images/attenzione.jpg" width="12" height="12" title="Revoca non ancora associata al Beneficio Concesso" border="0">
        &nbsp;<%=lSentenza%>
<%	  } %>        
      </td>

<%	if(lRevocaSosp.getMotivoModifica()!=null)
	{	%>
      <td class="c">
        <a href="javascript:visualizzaMotivoRevocaSospe('sospe_rec_<%=id_record%>')" title="<%=lDescStato%>">
        <%=lStato%>
      </td>
 <%	}
	else
	{%>     
      <td class="c" title="<%=lDescStato%>">&nbsp;<%=lStato%></td>
<%	} %>      
     <%//===================================== %>        
     <td class="c" style="text-align:center" nowrap> &nbsp;
      <%
      //======================================================================
      // Azioni possibili: Cancellazione/Annullamento, Modifica, Dettaglio
      //======================================================================
      %>
      <a href="javascript:eseguiAzione('Dettaglio', <%=lRevocaSosp.getIdBeneficioCumulo()%>, '01' )">
        <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Revoca" border="0"></a>
     <% 
     //======================================================================
     // Modifiche consentite solo ad istruttoria aperta e su dati non Cancellati
     //======================================================================
     if(IstruttoriaCumulo.getFlagStato().equals("A") && !lRevocaSosp.getFlagStato().equals("C") )
     {	%>
	    	<a href="javascript:eseguiAzione('Modifica', <%=lRevocaSosp.getIdBeneficioCumulo()%>, '01')">
          		<img src="/images/modifica.gif" width="12" height="12" alt="Modifica Revoca" border="0"></a>
        	<a href="javascript:eseguiAzione('Cancella', <%=lRevocaSosp.getIdBeneficioCumulo()%>, '01', '<%=lRevocaSosp.getFlagStato()%>', '<%=StringUtils.toStringJSP(StringUtils.cStrForJS(lRevocaSosp.getMotivoModifica()),"")%>')">
	      		<img src="/images/delete.gif" width="12" height="12" alt="Elimina Revoca" border="0"></a>
  <%  }	%>
      </td>
    </tr>
    
   
  <!-- Riga Hidden con la descrizione di Motivo Modifica : Visibile solo se si 'clicca' sulla colonna "Stato Revoca Sospe/NMZ"	-->
    <tr style="display:none" id="sospe_rec_<%=id_record%>">
      <td class="l" colspan="100%">
        <font class="campoSmall">
        <%=StringUtils.toStringJSP(lRevocaSosp.getMotivoModifica(),"&nbsp;")%>
        </font>
      </td>
    </tr>
<%
		} // end IF Tipo_Beneficio = 01 or 02
	} // end while su iterator
%>         
  </table>

<%
 } // end else di if ListaBeneficiRevoche == null ...  %>  

<br>    
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
 <% if (IstruttoriaCumulo.getFlagStato().equals("A")) { %>
    <tr>
      <td>
      	<INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci Revoca Sospensione - Non Menzione" style="width:300px" onClick="javascript:nuovaAnnotazione('<%=ICostantiBeneficiCumulo.TIPO_FORM_SOSPENSIONE%>');">
		&nbsp;&nbsp;&nbsp;
      	<INPUT class="bottone" type="button" name="AGGIUNGI" value="Inserisci Revoca Indulto" style="width:250px" onClick="javascript:nuovaAnnotazione('<%=ICostantiBeneficiCumulo.TIPO_FORM_INDULTO%>');">
      </td>
    </tr>
    <% } %>
  </table>
 
</FORM>
</body>
</html>