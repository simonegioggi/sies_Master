<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.penaaccessoria.model.PenaAccessoriaModel"%>
<%@ page import="siap.siep.penaaccessoria.action.ICostantiPenaAccessoria"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>

<jsp:useBean id="listaDPR" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoBeneficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="sottotipobeneficio" scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="lBeneficio"          scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="penacomplessiva"     scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel"/>
<jsp:useBean id="peneaccessorie"      scope="request" class="java.util.Vector"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>


<%
// Gestione funzione SIGE
boolean modoSIGE = false;
if (modo != null && modo.equalsIgnoreCase("SIGE"))
	modoSIGE = true;


// if(penacomplessiva == null)
// 	   penacomplessiva = new PenaComplessivaModel();

// if(lBeneficio == null)
// 	   lBeneficio = new BeneficioModel();


   String lAzione = null;
   if( modalita.equals("I") ) 
   {
		if( modoSIGE) 
 		{
    		lAzione = "siap.sige.beneficio.action.ActInserisciBeneficioIndultoSige";
    	}else{
    		   lAzione = "siap.siep.beneficio.action.ActInserisciBeneficioIndulto";
     	}
   }
   else if( modalita.equals("M") ){
		if( modoSIGE) 
 		{
    		lAzione = "siap.sige.beneficio.action.ActModificaBeneficioIndultoSige";
    	}else{	   
    		lAzione = "siap.siep.beneficio.action.ActModificaBeneficioIndulto";
    	}
   }
 
// MULTA
           String lParteInteraMulta = "";
             String lParteDecimaleMulta = "";
             int lIndexMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta()).indexOf(".");
             if(lIndexMulta == -1)
             {
               lParteInteraMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta());
               lParteDecimaleMulta = "";
             }
             else
             {
               lParteInteraMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta()).substring(0,lIndexMulta);
               lParteDecimaleMulta = StringUtils.toStringJSP(penacomplessiva.getImportoMulta()).substring(lIndexMulta+1);
             }
  
         
             String lParteInteraAmmenda = "";
             String lParteDecimaleAmmenda = "";
             int lIndexAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda()).indexOf(".");
             if(lIndexAmmenda == -1)
             {
               lParteInteraAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda());
               lParteDecimaleAmmenda = "";
             }
             else
             {
               lParteInteraAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda()).substring(0,lIndexAmmenda);
               lParteDecimaleAmmenda = StringUtils.toStringJSP(penacomplessiva.getImportoAmmenda()).substring(lIndexAmmenda+1);
             }            
   
%>


<html>
<head>
<title>[S.I.E.S.] - Gestione Beneficio </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript">
<%if(!lTipoFunzione.equals(""))
 {%>
   function  MisSic()
    {
       document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadInserisciMisuraSicurezza&lTipoFunzione=<%=lTipoFunzione%>";
       document.LoadInserisciBeneficio.AggAtt.disabled=true;
       document.LoadInserisciBeneficio.Inserisci.disabled=true;
    }
<%}%>


  var lPrima = true;
  var modifica = '<%=modalita%>';
 function CaricaPena()
 {
  
  //valorizzazione delle checkbox dipendete dalla voce selezionate nella combo sotto tipo beneficio
   if (document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '08') 
   {
   
   //qui vengono valorizzati tutti perchè riesco dall'opzione della combo
   if(document.LoadInserisciBeneficio.<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%> != null)
    {
     for(var i=0;i< document.LoadInserisciBeneficio.<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>.length;i++)
     {
       document.LoadInserisciBeneficio.<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>[i].checked = true;  
     } 
    }
   }
   else
   { 
    if((!lPrima && modifica == 'M' )|| modifica == 'I' )
    {
    if(document.LoadInserisciBeneficio.<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%> != null)
    {
     for(var i=0;i< document.LoadInserisciBeneficio.<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>.length;i++)
    {
      document.LoadInserisciBeneficio.<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>[i].checked = false;  
    }  
    }
   }
    
   }
   
   
   //gestione div dipendete dalla voce selezionata nella combo sotto tipo beneficio
     var nodeelenco = document.getElementById('elencopeneaccessorie');   
  if (document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '07'
       || document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '-'
       || document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '09'
       || document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '10')
     {
       nodeelenco.style.display='none';
     }
     else
     {
       nodeelenco.style.display='block';    
     }

   //caricamento della reclusione e arresto dipendete dalla voce selezionata nella combo sotto tipo beneficio
   if (document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '07'
       || document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '09' 
         || document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '10')
   {             
    document.LoadInserisciBeneficio.AArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumAnniArresto(),"0")%>";
    document.LoadInserisciBeneficio.MArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumMesiArresto(),"0")%>";
    document.LoadInserisciBeneficio.GArr.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniArresto(),"0")%>";
    document.LoadInserisciBeneficio.Ammenda.value = "<%=lParteInteraAmmenda%>";
    document.LoadInserisciBeneficio.Amm_dec.value = "<%=lParteDecimaleAmmenda%>";
   
    document.LoadInserisciBeneficio.ARec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumAnniReclusione(),"0")%>";
    document.LoadInserisciBeneficio.MRec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumMesiReclusione(),"0")%>";
    document.LoadInserisciBeneficio.GRec.value = "<%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniReclusione(),"0")%>";
    document.LoadInserisciBeneficio.Multa.value = "<%=lParteInteraMulta%>";
    document.LoadInserisciBeneficio.Mul_dec.value = "<%=lParteDecimaleMulta%>"; 

   } 
   else
   {
    document.LoadInserisciBeneficio.AArr.value = "";
    document.LoadInserisciBeneficio.MArr.value = "";
    document.LoadInserisciBeneficio.GArr.value = "";
    document.LoadInserisciBeneficio.Ammenda.value = "";
    document.LoadInserisciBeneficio.Amm_dec.value = "";
   
    document.LoadInserisciBeneficio.ARec.value = "";
    document.LoadInserisciBeneficio.MRec.value = "";
    document.LoadInserisciBeneficio.GRec.value = "";
    document.LoadInserisciBeneficio.Multa.value = "";
    document.LoadInserisciBeneficio.Mul_dec.value = "";
   }  
   
   lPrima = false;                       
 }
 


 </script>
</head>
<%if( modalita.equals("M") ){%>
<body class="corpo">
<%}else  if( modalita.equals("I") ){%> 
<body class="corpo" onload="CaricaPena();">
<%} %>

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
     <font class="campo">
<%if( modalita.equals("M") ){%>
     Modifica
<%} %>     
     Beneficio Indulto</font>
</td>
</tr>
</table>
	<br>
  <%if(!modoSIGE){%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciBeneficio">
     <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
     <input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" value="<%=lBeneficio.getIdBeneficio()%>">   
    <table width=90%>



      <tr>
        <td class="l">Tipo Beneficio</td>
        <td class="l">
        <select name="<%=ICostantiBeneficio.CAMPO_COD_TIPO_BENEFICIO %>" >
        <%=tipoBeneficio%>
        </select>
        </td>        
        <td class="l">Provvedimento di Concessione</td>
        <td class="l">
        <select name="<%=ICostantiBeneficio.CAMPO_COD_DPR%>">
        <%=listaDPR%>
        </select>
        </td>
      </tr>
      <tr>
        <td class="c" colspan=2>Applicazione del beneficio</td>
        <td class="l" colspan=2>
          <select name="<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>" onchange="CaricaPena();">
           <%=sottotipobeneficio%>
          </select>
        </td>   
      </tr>     
         
      <tr><td>&nbsp;</td></tr>
     </table> 
     <table width=90%>         
  <tr>
    <td class="titolo" colspan=2>Reclusione</td>
    <td width="25">&nbsp;</td>
    <td class="titolo" colspan=2>Arresto</td>
  </tr>
  
	  
  <tr>
    <td class="c">
      <font class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font class="label">Giorni</font><br>
      <input type="text" name="ARec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumAnniReclusione()) %>">&nbsp;
      <input type="text" name="MRec" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumMesiReclusione()) %>">&nbsp;
      <input type="text" name="GRec" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(lBeneficio.getNumGiorniReclusione()) %>">
    </td>
    <td class="c">
      <font  class="label">Multa</font><br>
      <input style="align:right" type="text" name="Multa" maxlength="7" size="7" value="<%=StringUtils.getParteIntera(lBeneficio.getImportoMulta()) %>">
      ,
      <input style="align:right" type="text" name="Mul_dec" maxlength="2" size="2" value="<%=StringUtils.getParteDecimale(lBeneficio.getImportoMulta()) %>">
    </td>
    <td width="25">&nbsp;</td>
    <td class=c>
      <font  class="label">Anni</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Mesi</font>&nbsp;&nbsp;&nbsp;&nbsp;
      <font  class="label">Giorni</font><br>
      <input type="text" name="AArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumAnniArresto()) %>">&nbsp;
      <input type="text" name="MArr" maxlength="2" size="2" value="<%=StringUtils.toStringJSP(lBeneficio.getNumMesiArresto()) %>">&nbsp;
      <input type="text" name="GArr" maxlength="4" size="4" value="<%=StringUtils.toStringJSP(lBeneficio.getNumGiorniArresto()) %>">

    </td>
    <td class="c">
      <font  class="label">Ammenda</font><br>
      <input style="align:right" type="text" name="Ammenda" maxlength="7" size="7" value="<%=StringUtils.getParteIntera(lBeneficio.getImportoAmmenda()) %>">
      ,
      <input style="align:right" type="text" name="Amm_dec" maxlength="2" size="2" value="<%=StringUtils.getParteDecimale(lBeneficio.getImportoAmmenda()) %>">
    </td>
  </tr>

 </table> 
 <table width=90%>    
	 <tr>
        <td class="c" colspan ='2'>Note</td>
        <td class="l" colspan ='2'>
             <textarea cols="50" rows=3  name="<%= ICostantiBeneficio.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lBeneficio.getNote())%></textarea>
        </td>
      </tr>        
     
</table>
<br>
<div id="elencopeneaccessorie" style="width: 100%; display:none; position:relative; " > 
  <table>
    <tr>
      <td class="int">Tipo Pena Accessoria</td>
      <td class="int">Durata Pena</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
    if( peneaccessorie != null && !peneaccessorie.isEmpty() )
    {
      Iterator itx = peneaccessorie.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
    	PenaAccessoriaModel lPenAcMod = (PenaAccessoriaModel)itx.next();
%>
        <tr>
          <td class="c">
            <%=StringUtils.toStringJSP(lPenAcMod.getDescrTipoPenaAccessoria(), "-")%>
          </td>
          <td class="l">
<%if(lPenAcMod.getDurata() != null && !lPenAcMod.getDurata().equals("-")) {%>          
            <%=StringUtils.toStringJSP(lPenAcMod.getDescrDurata(),"-")%> 
<%}else{ %>   
			Anni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumAnni(),"0") %></font>&nbsp;
			Mesi <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumMesi(),"0") %></font>&nbsp;
			Giorni <font class="campo"><%=StringUtils.toStringJSP(lPenAcMod.getNumGiorni(),"0") %></font>

<%}%>         
          </td>

          <td class="c">
          
 <%  if( modalita.equals("M") && lPenAcMod.getBenIdBeneficio() != null && lBeneficio.getIdBeneficio() != null &&  lPenAcMod.getBenIdBeneficio().compareTo(lBeneficio.getIdBeneficio())==0)
        {         
%>
	 <input type="checkbox" checked name="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" value="<%=lPenAcMod.getIdPenaAccessoria()%>">        
 
<%     }else{%> 
 	 <input type="checkbox"  name="<%=ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA%>" value="<%=lPenAcMod.getIdPenaAccessoria()%>">        
 
 
 <%     }%> 
          </td>
        </tr>
<%
      }
    }
%>
    </table>  
    </div>
 <br>
  <table width="90%">      
    <tr>
      <td colspan=2>
        <input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      </td>
      <% // se da ISCRIZIONE GUIDATA
     if(!lTipoFunzione.equals(""))
     { %>
     <td colspan=2>
       <input type="button"  class="bottone"  name="AggAtt" value="Prosegui" onClick="javascript:MisSic();">
     </td>
     <% } %>   
    </tr>
</table>
    <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
</form>


 <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadInserisciBeneficio")
           
       frmvalidator.addValidation("ARec","numeric","Il campo Anni Reclusione è numerico!");
       frmvalidator.addValidation("MRec","numeric","Il campo Mesi Reclusione è numerico!");
       frmvalidator.addValidation("GRec","numeric","Il campo Giorni Reclusione è numerico!");
       frmvalidator.addValidation("Multa","numeric","Il campo Multa è numerico!");
       frmvalidator.addValidation("Mul_dec","numeric","Il campo Multa è numerico!");  
   
       frmvalidator.addValidation("AArr","numeric","Il campo Mesi Arresto è numerico!");
       frmvalidator.addValidation("MArr","numeric","Il campo Giorni Arresto è numerico!");
       frmvalidator.addValidation("GArr","numeric","Il campo Ore Arresto è numerico!"); 
       frmvalidator.addValidation("Ammenda","numeric","Il campo Multa è numerico!");
       frmvalidator.addValidation("Amm_dec","numeric","Il campo Multa è numerico!");       
            
   </script>
</body>
</html>