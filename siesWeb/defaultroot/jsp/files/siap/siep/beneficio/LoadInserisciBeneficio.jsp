<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.tipologiaorario.action.ICostantiTipologiaOrario"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.tipologiaorario.model.TipologiaOrarioModel" %>

<jsp:useBean id="sospensioneSubordinata" scope="request" class="java.lang.String"/>
<jsp:useBean id="sottotipobeneficio"     scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"          scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"               scope="request" class="java.lang.String"/>
<jsp:useBean id="beneficio"              scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="tipologiaorario"        scope="request" class="java.util.Vector"/>
<jsp:useBean id="beneficiononmenzione"   scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>

<%
// Gestione funzione SIGE
boolean modoSIGE = false;
if (modo != null && modo.equalsIgnoreCase("SIGE"))
	modoSIGE = true;

//    if(beneficio == null)
// 	   beneficio = new BeneficioModel();

   String lAzione = null;
   if( modalita.equals("I") ) 
   {
 		if( modoSIGE) 
 		{
    		lAzione = "siap.sige.beneficio.action.ActInserisciBeneficioSige";
    	}else{
    		lAzione = "siap.siep.beneficio.action.ActInserisciBeneficio";
    	}
   }
   else if( modalita.equals("M") )
   {
		if( modoSIGE) 
 		{
    		lAzione = "siap.sige.beneficio.action.ActModificaBeneficioSige";
    	}else{
    		lAzione = "siap.siep.beneficio.action.ActModificaBeneficio";
    	}
    }
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Beneficio </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
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



  function disabilitasottotipo()
  {                 
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_TIPO_SOSP_SUBORDINATA%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_ANNI_ADEMPIMENTO%>.disabled=true;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_MESI_ADEMPIMENTO%>.disabled=true;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_GIORNI_ADEMPIMENTO%>.disabled=true;
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NOTE%>.disabled=true;
  }
  
  function abilitasottotipo()
  {
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_TIPO_SOSP_SUBORDINATA%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_ANNI_ADEMPIMENTO%>.disabled=false;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_MESI_ADEMPIMENTO%>.disabled=false;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_GIORNI_ADEMPIMENTO%>.disabled=false;
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NOTE%>.disabled=false;
  } 

  function disabilitadet()
  {
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN%>.disabled=true;
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MAR%>.disabled=true;  
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MER%>.disabled=true;  
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_GIOV%>.disabled=true;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_VEN%>.disabled=true;    
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_SAB%>.disabled=true;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_DOM%>.disabled=true;    
    
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_LUN%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN%>.disabled=true;     
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MAR%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR%>.disabled=true;           
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MER%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER%>.disabled=true;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_GIOV%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV%>.disabled=true; 
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_VEN%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN%>.disabled=true;      
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_SAB%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB%>.disabled=true;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_DOM%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM%>.disabled=true; 
               
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ENTE_INCARICATO%>.disabled=true;                   
  }
  
  function abilitadet()
  {
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN%>.disabled=false;
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MAR%>.disabled=false;  
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MER%>.disabled=false;  
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_GIOV%>.disabled=false;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_VEN%>.disabled=false;    
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_SAB%>.disabled=false;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_DOM%>.disabled=false;    
    
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_LUN%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN%>.disabled=false;     
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MAR%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR%>.disabled=false;           
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MER%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER%>.disabled=false;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_GIOV%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV%>.disabled=false; 
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_VEN%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN%>.disabled=false;      
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_SAB%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB%>.disabled=false;   
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_DOM%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM%>.disabled=false; 
               
      document.LoadInserisciBeneficio.<%=ICostantiTipologiaOrario.CAMPO_ENTE_INCARICATO%>.disabled=false;                   
  }  

  function disabilitasub()
  {                 
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_FLAG_FREQUENZA_SETTIMANALE%>.disabled=true;          
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_ORE_SETTIMANALI%>.disabled=true;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_GIORNI_PRESTAZIONE%>.disabled=true;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_MESI_PRESTAZIONE%>.disabled=true;
  }
  
  function abilitasub()
  {
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_FLAG_FREQUENZA_SETTIMANALE%>.disabled=false;          
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_ORE_SETTIMANALI%>.disabled=false;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_GIORNI_PRESTAZIONE%>.disabled=false;           
      document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_NUM_MESI_PRESTAZIONE%>.disabled=false;
  } 

 
   function sottotipo()
  {
     
     var nodedivsub = document.getElementById('divsub');    
     var nodesub = document.getElementById('divsubordinata'); 
     var nodedet = document.getElementById('divdeterminata');   
     
    if (document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '03')
    {
      nodedivsub.style.display='block';
      abilitasottotipo();
      subordinata();
    }
    else
    {
      nodedivsub.style.display='none';
      nodesub.style.display='none';
      nodedet.style.display='none'; 
      
      disabilitasottotipo();
      disabilitasub();  
      disabilitadet();  
    }
  }
  
  
  function subordinata()
  {
     
     var nodesub = document.getElementById('divsubordinata'); 
     var nodedet = document.getElementById('divdeterminata');      

     
    if (document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_TIPO_SOSP_SUBORDINATA%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_TIPO_SOSP_SUBORDINATA%>.selectedIndex].value == '08')
    {
      nodesub.style.display='block';
      determinata();
      abilitasub();
    }
    else
    {
      nodesub.style.display='none';
      nodedet.style.display='none';   
      disabilitasub();  
      disabilitadet();  
    }
 

  }
  
  function determinata()
  {
     var nodedet = document.getElementById('divdeterminata');      
     nodedet.style.display='none';
     disabilitadet();
    if(document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_FLAG_FREQUENZA_SETTIMANALE %>[1].checked)
    {
      nodedet.style.display='block';
      abilitadet();
    }

  }  



  function Verify()
  {
    if(document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>[document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO%>.selectedIndex].value == '-'
       && !document.LoadInserisciBeneficio.<%=ICostantiBeneficio.CAMPO_FLAG_NON_MENZIONE %>.checked)
    {
       alert("Inserire almeno un elemento! ");
       return false;
     
    }
    document.LoadInserisciBeneficio.submit();
  }

  </script>
</head>
<body class="corpo" onLoad="Javascript:sottotipo()">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
     <font class="campo">Sospensione condizionale ex art. 163</font>
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
     <input type="HIDDEN" name="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" value="<%=beneficio.getIdBeneficio()%>">   
    <table width=90%>

<%

//se ben_id_beneficio è valorizzato si avrà una non menzione
String lNonMenzione = null;
if("02".equals(beneficio.getCodTipoBeneficio()) ||( beneficiononmenzione != null && beneficiononmenzione.getIdBeneficio() != null)){
	lNonMenzione = "checked";
}

String lDeterminata = null;
String lNonDeterminata = null;
if("D".equals(beneficio.getFlagFrequenzaSettimanale())){
	lDeterminata = "checked";
}else{	
	lNonDeterminata = "checked";	
}


String lLun = null;
String lDalleLun = null;
String lAlleLun = null;

String lMar = null;
String lDalleMar = null;
String lAlleMar = null;

String lMer = null;
String lDalleMer = null;
String lAlleMer = null;

String lGio = null;
String lDalleGio = null;
String lAlleGio = null;

String lVen = null;
String lDalleVen = null;
String lAlleVen = null;

String lSab = null;
String lDalleSab = null;
String lAlleSab = null;

String lDom = null;
String lDalleDom = null;
String lAlleDom = null;

String lEnte = null;
if(tipologiaorario != null && !tipologiaorario.isEmpty())
{
    
 Iterator iter = tipologiaorario.iterator();
 while (iter.hasNext()) 
 {
	 
	 TipologiaOrarioModel TipoOrMod = (TipologiaOrarioModel) iter.next();
	  
	  
	  lEnte = TipoOrMod.getEnteIncaricato();
      if("01".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lLun = "checked";
    	   lDalleLun = TipoOrMod.getDalleOre();
    	   lAlleLun = TipoOrMod.getAlleOre();   	  
      }
	  else if("02".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lMar = "checked";
    	   lDalleMar = TipoOrMod.getDalleOre();
    	   lAlleMar = TipoOrMod.getAlleOre();   	  
      }     
	  else if("03".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lMer = "checked";
    	   lDalleMer = TipoOrMod.getDalleOre();
    	   lAlleMer = TipoOrMod.getAlleOre();   	  
      }    
	  else if("04".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lGio = "checked";
    	   lDalleGio = TipoOrMod.getDalleOre();
    	   lAlleGio = TipoOrMod.getAlleOre();   	  
      }    
	  else if("05".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lVen = "checked";
    	   lDalleVen = TipoOrMod.getDalleOre();
    	   lAlleVen = TipoOrMod.getAlleOre();   	  
      }    
	  else if("06".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lSab = "checked";
    	   lDalleSab = TipoOrMod.getDalleOre();
    	   lAlleSab = TipoOrMod.getAlleOre();   	  
      }    
	  else if("07".equals(TipoOrMod.getCodNumGiorno()))
      {
    	   lDom = "checked";
    	   lDalleDom = TipoOrMod.getDalleOre();
    	   lAlleDom= TipoOrMod.getAlleOre();   	  
      }     
 }

}	

%>

      <tr>
        <td class="l" colspan=2>Tipo Sospensione</td>
        <td class="l" colspan=2>
        <select name="<%=ICostantiBeneficio.CAMPO_COD_SOTTOTIPO_BENEFICIO %>" onChange="Javascript:sottotipo()">
        <%=sottotipobeneficio%>
        </select>
        </td>

    </tr>

      <tr>
        <td class="l" colspan=2>Non Menzione</td>
        <td class="l" ><input <%=lNonMenzione%> type="checkbox" name="<%=ICostantiBeneficio.CAMPO_FLAG_NON_MENZIONE %>" value="1"></td>
      </tr>
      
      <tr><td>&nbsp;</td></tr>
      
      <tr><td class="titolo" width=30%>Durata Sospensione</td></tr> 
        <tr>
          <td class="c">
            Anni <BR> <input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(beneficio.getNumAnniSospensione()) %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_ANNI_SOSPENSIONE %>" onkeypress="return TicTabNumField(this,event)" >
           </td>
        </tr>      
         
      <tr><td>&nbsp;</td></tr>
    </table>
 <div id="divsub" style="width: 100%; display:none; position:relative; " >   
     <table width=90%>  
      <tr>
        <td class="l" colspan=2>Obblighi del condannato ex art 165 c.p.  </td>
        <td class="l" colspan=2>
        <select class="small" name="<%=ICostantiBeneficio.CAMPO_COD_TIPO_SOSP_SUBORDINATA %>" onChange="Javascript:subordinata()">
        <%=sospensioneSubordinata%>
        </select>
        </td>

    </tr>
    </table>
     <table width=90%>  
	   <tr>
        <td class="l">Tipologia Obbligo</td>
        <td class="l">
             <textarea cols="40"  rows=4 name="<%= ICostantiBeneficio.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(beneficio.getNote()) %></textarea>
        </td>
        <td class="c" colspan=2>
         <table>
	
             <tr><td class="titolo" colspan="3" >Termine Adempimento Obbligo</td></tr> 
             <tr>
              <td align="center">  
                <font class="label"> Anni <BR> </font><input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(beneficio.getNumAnniAdempimento(),"0") %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_ANNI_ADEMPIMENTO %>" onkeypress="return TicTabNumField(this,event)" >               
              </td>
              <td align="center">  
                <font class="label"> Mesi</font> <BR> <input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(beneficio.getNumMesiAdempimento(),"0") %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_MESI_ADEMPIMENTO %>" onkeypress="return TicTabNumField(this,event)" >              
              </td>
              <td align="center">  
                <font class="label"> Giorni <BR> </font><input size="2" maxlength="2" value="<%=StringUtils.toStringJSP(beneficio.getNumGiorniAdempimento(),"0") %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_GIORNI_ADEMPIMENTO %>" onkeypress="return TicTabNumField(this,event)" >            
              </td>                            
           </tr>
         </table> 
        </td>
       </tr>

    <tr><td>&nbsp;</td></tr>
 </table>
</div>
<div id="divsubordinata" style="width: 100%; display:none; position:relative; " >
  <table width="90%">           
       <tr>
        <td class="c" colspan=2>
         <table>
	
             <tr><td class="titolo" colspan=3 >Durata Prestazione Attività Non Retribuita</td></tr> 
             <tr>
              <td align="center">  
               <font class="label"> Mesi <BR> </font><input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(beneficio.getNumMesiPrestazione(),"0") %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_MESI_PRESTAZIONE%>" onkeypress="return TicTabNumField(this,event)" >               
              </td>  
              <td align="center">  
               <font class="label"> Giorni <BR> </font><input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(beneficio.getNumGiorniPrestazione(),"0") %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_GIORNI_PRESTAZIONE %>" onkeypress="return TicTabNumField(this,event)" >               
              </td>                          
           </tr>
         </table> 
        </td>  
        <td class="c" colspan=2>
         <table>
	
             <tr><td class="titolo" colspan="3" >Modalità Esecuzione</td></tr> 
             <tr>
              <td align="center">  
              <font class="label"> Ore Settimanali <BR> </font><input size="3" maxlength="3" value="<%=StringUtils.toStringJSP(beneficio.getNumOreSettimanali(),"0") %>"  type="text" name="<%=ICostantiBeneficio.CAMPO_NUM_ORE_SETTIMANALI %>" onkeypress="return TicTabNumField(this,event)" >               
              </td>                           
           </tr>
         </table> 
        </td> 
        <td class="c" colspan=2>
          <table>
	
             <tr><td class="titolo" colspan=3 >Frequenza Settimanale</td></tr> 
             <tr>
              <td align="left">  
              <font class="label"> Non Determinata </font><input type="radio" <%=lNonDeterminata%> name="<%=ICostantiBeneficio.CAMPO_FLAG_FREQUENZA_SETTIMANALE %>" value="1" onClick ="Javascript:determinata()">               
              </td>                           
             </tr>
             <tr>
              <td align="left">  
              <font class="label"> Determinata  </font><input type="radio" <%=lDeterminata%> name="<%=ICostantiBeneficio.CAMPO_FLAG_FREQUENZA_SETTIMANALE%>" value="2" onClick ="Javascript:determinata()">             
              </td>                           
           </tr>             
         </table>                                                   
           </tr>
</table>
</div>
<div id="divdeterminata" style="width: 100%; display:none; position:relative; " >
  <table width="90%">      
     <tr><td class="titolo" colspan=10>Tipologia Orario</td></tr>              
      <tr>
        <td class="l"><input type="checkbox" <%=lLun%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_LUN  %>" value="01">
          Lunedì
        </td>
        <td class="l"> 
          <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleLun)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_LUN %>">               
          <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleLun)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_LUN %>">               
        </td>         
        <td class="l"><input type="checkbox" <%=lVen%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_VEN  %>" value="05">
          Venerdì 
        </td>
        <td class="l">
          <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleVen)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_VEN %>">                
          <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleVen)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_VEN %>">               
        </td>  
 
      </tr>
      <tr>
        <td class="l"><input type="checkbox" <%=lMar%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MAR  %>" value="02">
        Martedì
        </td>
        <td class="l">
           <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleMar)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MAR %>">               
           <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleMar)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MAR %>">               
        </td>         
        <td class="l"><input type="checkbox" <%=lSab%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_SAB  %>" value="06">
        Sabato
        </td>
        <td class="l">
          <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleSab)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_SAB %>">                
          <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleSab)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_SAB %>">               
        </td>  
 
      </tr>
       <tr>
        <td class="l"><input type="checkbox" <%=lMer%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_MER  %>" value="03">
        Mercoledì
        </td>
        <td class="l">
          <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleMer)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_MER %>">               
          <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleMer)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_MER %>">               
         
        <td class="l"><input type="checkbox" <%=lDom%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_DOM %>" value="07">
        Domenica
        </td>
        <td class="l">
           <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleDom)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_DOM %>">                 
          <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleDom)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_DOM %>">               
        </td>  
 
      </tr>  
        <tr>
        <td class="l"><input type="checkbox" <%=lGio%> name="<%=ICostantiTipologiaOrario.CAMPO_COD_NUM_GIORNO_GIOV %>" value="04">
        Giovedì 
        </td>
        <td class="l">      
          <font class="label"> dalle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lDalleGio)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_DALLE_ORE_GIOV %>">                
           <font class="label"> alle ore </font><input size="5" maxlength="5" value="<%=StringUtils.toStringJSP(lAlleGio)%>"  type="text" name="<%=ICostantiTipologiaOrario.CAMPO_ALLE_ORE_GIOV %>">               
        </td>         

 
      </tr>             
                                              
	   <tr>
        <td class="c" colspan ='2'>Ente Incaricato dei controlli</td>
        <td class="l" colspan ='2'>
             <textarea cols="50" rows=3 name="<%= ICostantiTipologiaOrario.CAMPO_ENTE_INCARICATO %>"><%=StringUtils.toStringJSP(lEnte) %></textarea>
        </td>
      </tr>   
       
</table>
</div>
<div id="divbottone" style="width: 100%; display:block; position:relative; " >
  <table width="90%">      
    <tr>
      <td colspan=2>
        <input type="button" value="Conferma" class="bottone"  name="Inserisci" onClick="javascript:Verify();">
      </td>
      <% // se da ISCRIZIONE GUIDATA
     if(!lTipoFunzione.equals("")){ %>
     <td colspan=2>
       <input type="button"  class="bottone"  name="AggAtt" value="Prosegui" onClick="javascript:MisSic();">
     </td>
     <% } %>   
    </tr>
</table>
</div>
    <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
</form>


</body>
</html>