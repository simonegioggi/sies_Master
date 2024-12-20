<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.ulterioresanzionecumulo.model.UlterioreSanzioneCumuloModel"%>

<jsp:useBean id="PosizioneGiuridica" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<jsp:useBean id="ulterioriSanzione"  scope="request" class="java.util.Vector" />

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
</head>

<body class="corpo">


  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
		<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciUlterioriSanzioni">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Dettaglio Ulteriori Sanzioni</font>
      </td>
       <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>" />
       </td>

    </tr>
  </table>
 <br>
 <table style="width: 95%;">
 <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
 <tr><td><br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br></td></tr>
<tr>
  <td class=l>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
 </tr>
</table>
<br>
<table style="width: 95%;">
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
				        lParteDecimaleSanzionePenaPec = lImportoSanzionePenaPec.substring(lIndexPenaPecSost+1);
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
			
			          		<td class="l" >Anni
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
</form>
</body>
</html>

