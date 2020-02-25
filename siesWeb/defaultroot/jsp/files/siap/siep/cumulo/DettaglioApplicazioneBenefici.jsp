<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.List" %>

<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.penacumulo.action.ICostantiPenaCumulo"%>
<%@ page import="siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel"%>
<%@ page import="siap.siep.beneficio.model.BeneficioPenaAccessoriaModel" %>
<%@ page import="siap.siep.beneficio.model.BeneficioModel" %>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>

<jsp:useBean id="posizioneluogoaltra"        scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" />
<jsp:useBean id="AnnotazioneManualeInserita" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" />
<jsp:useBean id="PenaResiduaCorrente"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="UltimaPenRes"               scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />
<jsp:useBean id="ulterioriSanzione"          scope="request" class="java.util.Vector" />
<jsp:useBean id="eveCorrelati" 				 scope="request" class="java.util.Vector" />
<jsp:useBean id="beneficiopenaaccessoria" 	 scope="request" class="java.util.Vector" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
	    function MisureSicurezza(a_formname,a_fieldname)
	    {
	       var desktopms;
	       var valorecampo = document.f.<%=ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA%>.value;
	       desktopms = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldvalue="+valorecampo, "MisureSicurezza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=170");
	    }
	    function PeneAccessorie(a_formname,a_fieldname)
	    {
	       var desktoppa;
	       var valorecampo = document.f.<%=ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA%>.value;
	      desktoppa = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldvalue="+valorecampo, "PeneAccessorie","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=170");
	    }
    </script>
   
    <title>[S.I.E.S.] - Dettaglio Richiesta Applicazione Benefici</title>
  </head>

<%
	PenaResiduaModel lPenaRicalcolata = PenaResiduaCorrente;
%>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG>
        <font  class="label">Funzione :&nbsp;</font><font class="campo">Dettaglio Richiesta Applicazione Benefici</font>
      </td>
      <%-- MERGE v10: modificato commento --%>
      <%-- <td class="LBG">
        <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>"/>
      </td> --%>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>

<%
//==============================================================================
//       Sezione: Posizione Giuridica 
//                Data Decorrenza Pena
//                Data Fine Pena
//==============================================================================
%>
<table width = "90%">
	<tr>
	  <td class=l width="30%">Posizione Giuridica</td>
	  <td class=l colspan=5><font class="campo"><%=posizioneluogoaltra.getPosizioneGiuridica().getDescrPosizioneGiuridica() %></font></td>
	</tr>

  	<tr>
<%
       if (UltimaPenRes.getDataInizio() != null)
       {
%>
         <td class="l" width="30%">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(UltimaPenRes.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( UltimaPenRes.getFlagErgastolo() != null)
       {
        if(UltimaPenRes.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(UltimaPenRes.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }
%>

<%
  	   if (UltimaPenRes.getFlagErgastolo() != null && (UltimaPenRes.getFlagErgastolo().equals("S") || UltimaPenRes.getFlagErgastolo().equals("D")))
   	   {
%>
           <td class="l">Data Fine Pena</td>
           <td class="lRosso"> <font class="lRosso">MAI</font></td>

<%     }else if( UltimaPenRes.getDataFine() != null) { %>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenaRicalcolata.getDataFine(), "dd-MM-yyyy"))%></font>
            </td>
<%     }%>
</tr>

</table>

<br>

<%
//==============================================================================
//       Sezione Pena Principale
//==============================================================================
%>
  <table>
      <tr>
	      <td width=100% colspan="2">
	        <table width=100%>
	          <tr>
	            <td class="Titolo" colspan="9"><font  class="label">Pena Principale</font></td>
	          </tr>
      
		      <tr>
		        <td class="l"><font class="label">Reclusione / Multa : </font></td>
		        <td class="l"><font class="label">Anni</font></td>
		        <td class="l"><font class="campo"><%=UltimaPenRes.getNumAnniReclusione()%></font></td>
		        <td class="l"><font class="label">Mesi</font></td>
		        <td class="l"><font class="campo"><%=UltimaPenRes.getNumMesiReclusione()%></font></td>
		        <td class="l"><font class="label">Giorni</font></td>
		        <td class="l"><font class="campo"><%=UltimaPenRes.getNumGiorniReclusione()%></font></td>
		        <td class="l"><font class="label">Importo</font></td>
		        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(UltimaPenRes.getImportoMulta())%></font></td>
		      </tr>
		      
		      <tr>
		        <td class="l"><font class="label">Arresto / Ammenda :</font></td>
		        <td class="l"><font class="label">Anni</font></td>
		        <td class="l"><font class="campo"><%=UltimaPenRes.getNumAnniArresto()%></font></td>
		        <td class="l"><font class="label">Mesi</font></td>
		        <td class="l"><font class="campo"><%=UltimaPenRes.getNumMesiArresto()%></font></td>
		        <td class="l"><font class="label">Giorni</font></td>
		        <td class="l"><font class="campo"><%=UltimaPenRes.getNumGiorniArresto()%></font></td>
		        <td class="l"><font class="label">Importo</font></td>
		        <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(UltimaPenRes.getImportoAmmenda())%></font></td>
		      </tr>
		    </table>
          </td>
      </tr>  
  </table>
  
  <br>
<%
//==============================================================================
//       Sezione Quantum Pena della Richiesta
//==============================================================================
%>  
  <table>
    <tr>
      <td width=100% colspan="2">
        <table width=100%>
            <tr>
            	<td class="Titolo" colspan="9"><font  class="label">Quantum Pena della Richiesta</font></td>
            </tr>

	      	<tr>
	        	<td class="l"><font class="label">Reclusione / Multa : </font></td>
	        	<td class="l"><font class="label">Anni</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getNumAnniReclusione(), "0")%></font></td>
	        	<td class="l"><font class="label">Mesi</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getNumMesiReclusione(), "0")%></font></td>
	        	<td class="l"><font class="label">Giorni</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getNumGiorniReclusione(), "0")%></font></td>
	        	<td class="l"><font class="label">Importo</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toEuroFormat(AnnotazioneManualeInserita.getImportoMulta())%></font></td>
	      	</tr>
	      
	      	<tr>
	        	<td class="l"><font class="label">Arresto / Ammenda :</font></td>
	        	<td class="l"><font class="label">Anni</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getNumAnniArresto(), "0")%></font></td>
	        	<td class="l"><font class="label">Mesi</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getNumMesiArresto(), "0")%></font></td>
	        	<td class="l"><font class="label">Giorni</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toStringJSP(AnnotazioneManualeInserita.getNumGiorniArresto(), "0")%></font></td>
	        	<td class="l"><font class="label">Importo</font></td>
	        	<td class="l"><font class="campo"><%=StringUtils.toEuroFormat(AnnotazioneManualeInserita.getImportoAmmenda())%></font></td>
	      	</tr>
		</table>
      </td>
    </tr>
  </table>

  <br>

<%
//==============================================================================
//       Sezione Pena Residua (solo se non ergastolo)
//==============================================================================
%>
<%if(    lPenaRicalcolata.getFlagErgastolo() != null
     && !lPenaRicalcolata.getFlagErgastolo().equals("S")
     && !lPenaRicalcolata.getFlagErgastolo().equals("D")
    )
{%>
  <table>
    <tr>
      <td width=100% colspan="2">
        <table width=100%>
          <tr>
            <td class="Titolo" colspan="9"><font  class="label">Pena Residua</font></td>
          </tr>

          <tr>
            <td class="l"><font  class="label">Reclusione / Multa : </font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=lPenaRicalcolata.getNumAnniReclusione()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=lPenaRicalcolata.getNumMesiReclusione()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=lPenaRicalcolata.getNumGiorniReclusione()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaRicalcolata.getImportoMulta())%></font></td>
          </tr>
          
          <tr>
            <td class="l"><font  class="label">Arresto / Ammenda :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=lPenaRicalcolata.getNumAnniArresto()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=lPenaRicalcolata.getNumMesiArresto()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=lPenaRicalcolata.getNumGiorniArresto()%></font></td>
            <td class="l"><font class="label">Importo</font></td>
            <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(lPenaRicalcolata.getImportoAmmenda())%></font></td>
          </tr>

        </table>
      </td>
    </tr>
  </table>
<%}%>

<%
//==============================================================================
//       Sezione Ulteriori Sanzioni Sostitutive Cumulo
//==============================================================================
%>

<table>
<%
	String Stito ="";
	for (Iterator lIter = ulterioriSanzione.iterator(); lIter.hasNext(); )
	{
		UlterioreSanzioneCumuloModel lUltMod = (UlterioreSanzioneCumuloModel)lIter.next();
		if((!lUltMod.getCodTipoUlterioreSanzione().equals("")) 
		|| (!lUltMod.getCodTipoUlterioreSanzione().equals("0")))
				Stito = "Ulteriori Sanzioni Cumulo";
			
	}
	
	if (Stito.compareTo("")!= 0)
	{
%>		
		<tr><td class="Titolo" colspan=4 >Ulteriori Sanzioni Sostitutive Cumulo</td></tr>
		<br><br>
<%		
	}
			
    for (Iterator lIter = ulterioriSanzione.iterator(); lIter.hasNext(); )
    {
      	UlterioreSanzioneCumuloModel lUltMod = (UlterioreSanzioneCumuloModel)lIter.next();
		if(lUltMod.getCodTipoUlterioreSanzione().equals("01") ||
		   lUltMod.getCodTipoUlterioreSanzione().equals("02") ||
		   lUltMod.getCodTipoUlterioreSanzione().equals("03"))
		  
		{
%>
	  			<tr>
	       			<td class="l" ><%=lUltMod.getDescrTipoUlterioreSanzione()%></td>        	
	
	          		<td class="l" >Anni
	            		<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumAnni())%></font>
	            					Mesi
	           			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumMesi())%></font>
	             				Giorni
	         			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumGiorni())%></font>
	         		</td>
	         	</tr>		
<%
		}
		if(lUltMod.getCodTipoUlterioreSanzione().equals("04"))
		{
				String lParteInterSanzionePenaPec = "0";
				String lParteDecimaleSanzionePenaPec = "0";
				if(lUltMod.getSanzione()!= null)
				{		
				    String lImportoSanzionePenaPec = StringUtils.toStringJSP(lUltMod.getSanzione());
				     int lIndexPenaPecSost = lImportoSanzionePenaPec.indexOf(".");
				     if(lIndexPenaPecSost == -1)
				     {
				       lParteInterSanzionePenaPec = lImportoSanzionePenaPec;
				       lParteDecimaleSanzionePenaPec = "";
				     }
				      else
				      {
				        lParteInterSanzionePenaPec = lImportoSanzionePenaPec.substring(0, lIndexPenaPecSost);
				        lParteDecimaleSanzionePenaPec = lImportoSanzionePenaPec.substring(lIndexPenaPecSost+1);;
				      }
				 }			
%>			
        		
				<tr>
  					<td class="l" ><%=lUltMod.getDescrTipoUlterioreSanzione()%></td> 
  					<td class="l" >Sanzione 
            			<font class="campo"><%=StringUtils.toStringJSP(lParteInterSanzionePenaPec)%></font>&nbsp; 
            			,
            			<font class="campo"><%=StringUtils.toStringJSP(lParteDecimaleSanzionePenaPec)%></font>&nbsp;
            			Euro
                   	</td>
<%					
		}		

		if(lUltMod.getCodTipoUlterioreSanzione().equals("05") )
		{
%>
				<tr>
	       			<td class="l" ><%=lUltMod.getDescrTipoUlterioreSanzione()%></td>        	
	
	          		<td class="l" >Anni
	            		<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumAnni())%></font>
	            					Mesi
	           			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumMesi())%></font>
	             				Giorni
	         			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumGiorni())%></font>
	         		</td>
	         	</tr>		 
 <%		}

		if(lUltMod.getCodTipoUlterioreSanzione().equals("06") )
		{
				String lParteInterSanzioneMilitare = "0";
				String lParteDecimaleSanzioneMilitare = "0";
				if(lUltMod.getSanzione()!= null)
				{		
					    String lImportoSanzioneMilitare = StringUtils.toStringJSP(lUltMod.getSanzione());
					    int lIndexMilitare = lImportoSanzioneMilitare.indexOf(".");
					    if(lIndexMilitare == -1)
					    {
						       lParteInterSanzioneMilitare = lImportoSanzioneMilitare;
						        lParteDecimaleSanzioneMilitare = "";
				     	}
				      	else
				      	{
						        lParteInterSanzioneMilitare = lImportoSanzioneMilitare.substring(0, lIndexMilitare);
						        lParteDecimaleSanzioneMilitare = lImportoSanzioneMilitare.substring(lIndexMilitare+1);
				      	}
				 }						
%>
				<tr>
	       			<td class="l" ><%=lUltMod.getDescrTipoUlterioreSanzione()%></td>        	
	
	          		<td class="l" >Anni
	            		<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumAnni())%></font>
	            					Mesi
	           			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumMesi())%></font>
	             				Giorni
	         			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumGiorni())%></font>
	         		</td>
	         		<td class="l" >Multa 
            			<font class="campo"><%=StringUtils.toStringJSP(lParteInterSanzioneMilitare)%></font>&nbsp; 
            			,
            			<font class="campo"><%=StringUtils.toStringJSP(lParteDecimaleSanzioneMilitare)%></font>&nbsp;
            			Euro
                   	</td>
	         	</tr>		 		
<%
		}
		
		if(lUltMod.getCodTipoUlterioreSanzione().equals("07") ||
		   lUltMod.getCodTipoUlterioreSanzione().equals("08") ||
		   lUltMod.getCodTipoUlterioreSanzione().equals("09"))
				  
		{
		%>
			        	
			<tr>
     			<td class="l" ><%=lUltMod.getDescrTipoUlterioreSanzione()%></td>        	
        		<td class="l" >
        				Anni
          			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumAnni())%></font>
          				Mesi
         			<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumMesi())%></font>
           				Giorni
       				<font class="campo"><%=StringUtils.toStringJSP(lUltMod.getNumGiorni())%></font>
       			</td>
       		</tr>				
	<%  }	%>

 <%}%>
</table>
<br>

<%
//==============================================================================
//       Sezione Elenco Pene Accessorie
//==============================================================================
%>

  <div align=center>
<%
  // Elenco degli Eventi Correlati
  Vector eventiPA = new Vector();
  Iterator itxProc = eveCorrelati.iterator();
  int jPA =0;
  if (beneficiopenaaccessoria != null && beneficiopenaaccessoria.size() > 0 )
  {  
%>
	<table width="100%">
		<tr><td class="Titolo" colspan=4 >Elenco Pene Accessorie</td></tr>
	</table>
<%
	  Iterator itx = beneficiopenaaccessoria.iterator();
		boolean primo = true;
  	while ( itx.hasNext())
  	{
%>
  		<table width="100%">
<%		if (primo) 
			{
				primo=false; %>	

		    <tr>
		      <td class="int" width=25%>Tipo</td>
		      <td class="int" width=12%>Tipo Durata</td>
		      <td class="int" width=10%>Stato</td>
		      <td class="int" width=13%>Data Fine Validità</td>
		      <td class="int" width=10%>Falsità di documenti</td>
		      <td class="int" width=10%>Tipo Beneficio</td>
		      <td class="int" width=10%>Provvedimento di concessione</td>
		    </tr>
<%		}
  
	BeneficioPenaAccessoriaModel lBenPenMod = (BeneficioPenaAccessoriaModel)itx.next();
    PenaAccessoriaModel penacc = lBenPenMod.getPenaAccessoria();
    BeneficioModel lBenMod = lBenPenMod.getBeneficio();
    
	// Decodifica stato Pena Accessoria.
    String descrFlagCondonata = "-";
    if (penacc.getFlagCondonata().trim().compareTo("C")==0)
    	descrFlagCondonata = "Condonata";
    else if (penacc.getFlagCondonata().trim().compareTo("R")==0)
    	descrFlagCondonata = "Revocata";
    else if (penacc.getFlagCondonata().trim().compareTo("D")==0)
    	descrFlagCondonata = "Depenalizzata";
    else if (penacc.getFlagCondonata().trim().compareTo("S")==0)
    	descrFlagCondonata = "Sostituita";
    else if (penacc.getFlagCondonata().trim().compareTo("T")==0)
    	descrFlagCondonata = "Sost. e Condonata";
%>
    <tr>
      <td class=l width="25%" align="left">
<%      if (penacc.getNumeroEventiCorrelati() != null          &&
            penacc.getNumeroEventiCorrelati().intValue() > 0 )
        {%>
          <a href="#1" onClick="return effettoTree(<%=jPA%>)"><img name="image<%=jPA%>" src="<%=IWebConstants.IMAGES_DIR%>expand.gif"  alt="" border="0" Title="Elenco dei Provvedimenti Correlati" ></a>
      <%}%>
				<%=penacc.getDescrTipoPenaAccessoria()%>
			</td>
      <td class=c width="12%"><%=StringUtils.toStringJSP(penacc.getDescrDurata())%></td>
      <td class=c width="10%"><%=descrFlagCondonata%></td>
      <td class=c width="13%"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penacc.getDataFineValidita(),"dd-MM-yyyy"),"-") %></td>
      <td class=c width="10%"><%=StringUtils.toStringJSP(penacc.getFlagDichiarazioneFalsita())%></td>
      <%if(lBenMod != null && lBenMod.getIdBeneficio() != null) {%>
         <td class=c width="10%"><%=StringUtils.toStringJSP(lBenMod.getDescrTipoBeneficio(),"-")%></td>
         <td class=c width="10%"><%=StringUtils.toStringJSP(lBenMod.getDescrDpr(),"-")%></td>
      <%}else{ %>
         <td class=c width="10%">-</td>
         <td class=c width="10%">-</td>     
       <%}%>     
    </tr>
<%      
    } // endwhile
		%></table><%
  }
%>
</div>
<br>

<%
//==============================================================================
//       Sezione Elenco Misure di Sicurezza
//==============================================================================
%>

<div align=center>

<%
  // Elenco Misure di Sicurezza
  List misuresicurezza =(List) request.getAttribute("misuresicurezza");

  if(misuresicurezza != null && misuresicurezza.size() > 0)
  {
%>
	<table width="100%">
		<tr><td class="Titolo" colspan=4 >Elenco Misure di Sicurezza</td></tr>
	</table>
    <table cellpadding=2 cellspacing=2>
	    <tr>
	      	<td class="int">Natura Misura</td>
	      	<td class="int">Tipo Misura</td>
	      	<td class="int">Num. Anni</td>
	      	<td class="int">Num. Mesi</td>
	      	<td class="int">Num. Giorni</td>
	    </tr>

<%
    Iterator itx = misuresicurezza.iterator();
    while ( itx.hasNext())
    {
      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
%>
	    <tr>
	      <td class=C><%=StringUtils.toStringJSP(lMis.getDescrNatura())%>&nbsp;</td>
	      <td class=C><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</td>
	      <td class=C><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</td>
	      <td class=C><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</td>
	      <td class=C><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</td>
	    </tr>
<%
    }
%>
	</table>
<%
  }
%>
	
</div>
  
<form method="post" name="f">
	<table>
<%
	if (Stito.compareTo("") == 0)
	{
%>
		<tr>
		  <td class="l" colspan="2">
		    <font class="label">
		    	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadInserisciUlterioriSanzioni&Provenienza=RichApplBenefici&IdPenaResidua=<%=lPenaRicalcolata.getIdPenaResidua()%>" title="Inserimento Altre Sanzioni">
		    		Inserimento Altre Sanzioni
		    	</a>
		    </font>
		  </td>
		</tr>
<%
	}
%>
		<tr>
<%
		if (beneficiopenaaccessoria.size() == 0 )
		{  
%>
		  <td class="l" colspan="2">
		    <font class="label">
		    	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadInserisciPenaAccessoria&IdPenaResidua=<%=lPenaRicalcolata.getIdPenaResidua()%>" title="Inserimento Pene Accessorie">
		    		Pene Accessorie
		    	</a>
		    </font>
		  </td>
<%
		}
%>
		  <td class="l" colspan="2">
		    <font class="label">
		    	<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadInserisciMisuraSicurezza&IdPenaResidua=<%=lPenaRicalcolata.getIdPenaResidua()%>" title="Inserimento Misure di Sicurezza">
		    		Misure di Sicurezza
		    	</a>
		    </font>
		  </td>
		</tr>
	</table>
</form>

  </body>

</html>