<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.continuazione.action.ICostantiContinuazione"%>
<%@ page import="siap.siep.continuazione.model.ContinuazioneModel"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="continuazione" scope="request" class="siap.siep.continuazione.model.ContinuazioneModel"/>

<jsp:useBean id="autoritaSentenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoContinuazione" scope="request" class="java.lang.String"/>

<html>
<head>
<title>[S.I.E.S.] - Pena Complessiva </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
  var desktop;
  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>
<script language="JavaScript">
  function Verify()
  {
	//**********************************************************
	// Federica - a9-rr-078
	// inseriti controlli su tutti i campi della maschera
	//**********************************************************

    // se tutti i campi sono vuoti tranne l'ultima riga, errore
	if ((document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>.value=="-")
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>.value.length==0)          
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>.value.length==0)       
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>.value.length==0)          
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>.value.length==0)         
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>.value.length==0)          
     && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>.value=="-")         
     && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>.value.length==0))         
	{
		if (document.LoadModificaContinuazione.TipoRG.value != "-"
		 || document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>.value.length>0       
		 || document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>.value.length>0             
		 || document.LoadModificaContinuazione.ARG.value.length>0          
	     || document.LoadModificaContinuazione.NRG.value.length>0)
		{
			alert("Tipo continuazione obbligatorio");
		    document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>.focus();
			return false;	 
		 } 
		 else
		 {
			return true;
		 } 	 
	} 

    // se tutti i campi sono pieni e l'ultima riga non è riempita correttamente, errore 
	if ((document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>.value!="-")
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>.value.length>0)          
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>.value.length>0)       
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>.value.length>0)          
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>.value.length>0)         
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>.value.length>0)          
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>.value!="-")         
	 && (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>.value.length>0))         
	{		  
		// ultima riga bianca, ok
		if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>.value.length==0       
		 && document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>.value.length==0                
		 && document.LoadModificaContinuazione.TipoRG.value=="-"
		 && document.LoadModificaContinuazione.ARG.value.length==0       
		 && document.LoadModificaContinuazione.NRG.value.length==0)                
		{
			return true;
		} 
		// ultima riga parzialmente riempita
		if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>.value.length==0       
	     && document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>.value.length>0)                
		{
			alert("Anno R.G.N.R. non presente");
			document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>.focus();
			return false;	 
		}
		if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>.value.length>0       
	     && document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>.value.length==0)                
		{
			alert("Numero R.G.N.R. non presente");
			document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>.focus();
			return false;	 
		}
	 	if (document.LoadModificaContinuazione.TipoRG.value != "-")
	 	{
			if (document.LoadModificaContinuazione.ARG.value.length==0       
		     || document.LoadModificaContinuazione.NRG.value.length==0)           
	 		{
				alert("Anno/Numero/Tipo Reg. Gen. non presente");
				document.LoadModificaContinuazione.ARG.focus();
				return false;	 
			} 
		} 
	 	if (document.LoadModificaContinuazione.TipoRG.value == "-")
	 	{
       		if (document.LoadModificaContinuazione.ARG.value.length!=0       
		     || document.LoadModificaContinuazione.NRG.value.length!=0)           
	 		{
				alert("Anno/Numero/Tipo Reg. Gen. non presente");
				document.LoadModificaContinuazione.ARG.focus();
				return false;	 
			} 
		} 
		if (document.LoadModificaContinuazione.ARG.value.length==0       
	     && document.LoadModificaContinuazione.NRG.value.length>0)                
		{
			alert("Anno Reg. Gen. non presente");
			document.LoadModificaContinuazione.ARG.focus();
			return false;	 
		}
		if (document.LoadModificaContinuazione.ARG.value.length>0       
	     && document.LoadModificaContinuazione.NRG.value.length==0)                
		{
			alert("Numero Reg. Gen. non presente");
		    document.LoadModificaContinuazione.NRG.focus();
			return false;	 
		}		  
		return true;
	}
    // se non sono riempiti tutti i campi, errore 
	else
	{
		if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>.value=="-")
		{
			alert("Tipo continuazione obbligatorio");
			document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>.focus();
			return false;		 
		}
	    if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>.value.length==0)          
		{
			alert("Anno sentenza obbligatorio");
		    document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>.focus();
			return false;		 
		}
		  
		if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>.value.length==0)          
		{
			alert("Numero sentenza obbligatorio");
		    document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>.focus();
			return false;	 
		}
		  
	   	var d1=document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>.value+'/'+document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>.value+'/'+document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>.value;  	     
	   	if (! ControllaData(d1))
	    {	    		      
	    	alert('Data sentenza non valida');
		    document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>.focus();
	        return false;
	    }
		if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>.value=="-")
		{
			alert("Autorità obbligatoria");
		    document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>.focus();
			return false;		 
		}
	    if (document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>.value.length==0)    
		{
			alert("Luogo Autorità obbligatoria");
		    document.LoadModificaContinuazione.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>.focus();
			return false;		 
		}
	} 
  // fine controlli - federica   
  }
  
</script>
</head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
          ContinuazioneModel lContMod = new ContinuazioneModel();
          String lAzione = new String();
          if( modalita.equals("M") )
          {
            lAzione = "siap.siep.penacomplessiva.action.ActModificaContinuazione";
            lContMod = continuazione;
%>
            <font class="campo">Modifica Continuazioni con altre sentenze</font>
<%
          }
%>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadModificaContinuazione">
      <table cellspacing="2" cellpadding="2" width="95%">
        <tr><td class="Titolo" colspan="1">Continuazione con altre sentenze</td></tr>
        <tr>
          <td class="l">
            <table cellspacing="2" cellpadding="2" width="100%">
              <tr>
                <td class="l">Tipo Continuazione</td>
                <td class="l" colspan="3">
                  <select Title="Tipo Continuazione" name="<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>">
                    <%=tipoContinuazione%>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="l">Anno/Numero Sentenza</td>
                <td class="L" colspan="3">
                  <input Title="Anno Sentenza" value="<%=StringUtils.toStringJSP(lContMod.getAnnoSentenza())%>" type="text" name="<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
                  /
                  <input Title="Numero Sentenza" value="<%=StringUtils.toStringJSP(lContMod.getNumSentenza())%>" type="text" name="<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>" maxlength="6" size="6"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                  
                </td>
              </tr>
              <tr>
                <td class="l">Data Sentenza</td>
                <td class="l" colspan="3">
                  <input Title="Giorno Data Sentenza" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(), "dd") )%>" name="<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>" maxlength="2" size="2"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                  -
                  <input Title="Mese Data Sentenza" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(), "MM") )%>" name="<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>" maxlength="2" size="2"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                  -
                  <input Title="Anno Data Sentenza" type="text" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lContMod.getDataSentenza(), "yyyy") )%>" name="<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>"maxlength="4" size="4" 
                  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
                </td>
              </tr>
              <tr>
                <td class="l">Autorità Sentenza</td>
                  <td class="l" colspan="3">
                    <select Title="Autorità Sentenza" name="<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>">
                      <%=autoritaSentenza%>
                    </select>
                  </td>
              </tr>
              <tr>
                <td class="l">Luogo Sentenza</td>
                <td class="l" colspan="3">
                  <input Title="Luogo Sentenza" name="<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>" value="<%=StringUtils.toStringJSP(lContMod.getDescrLuogoAutorita())%>" type="text" maxlength="35" size="35">
                  <a href="Javascript:ListaComuni('LoadModificaContinuazione','<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>');">
                    <img src="/images/filefolder.gif" border="0">
                  </a>
                </td>
              </tr>
<%
         // *********************************************************************************************** 
		 // Federica - a9-rr-078
         // Sostituzione dei 5 campi relativi a Anno/Numero reg.gen con un solo campo e lista di valori
         // *********************************************************************************************** 
%>
              <tr>
			    <td class="l">Anno/Numero R.G.N.R.</td>
			    <td class="L">
			          <input Title="Anno R.G.N.R." value="<%=StringUtils.toStringJSP(lContMod.getAnnoRegePm())%>" type="text" name="<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4"
			          	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			         /<input Title="Numero R.G.N.R." value="<%=StringUtils.toStringJSP(lContMod.getNumRegePm())%>" type="text" name="<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>" maxlength="6" size="6"
			         	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
			    </td>		    
<%			
			    String ARG  = "";
			    String NRG  = "";
			    String Tipo = "";
			    if (lContMod.getAnnoRegeCap() != null) 
			    {
				   ARG = lContMod.getAnnoRegeCap() + "";
				   NRG = lContMod.getNumRegeCap() + "";
				   Tipo = "cap";
			    }
				if (lContMod.getAnnoRegeCas() != null) {
					ARG = lContMod.getAnnoRegeCas() + "";
					NRG = lContMod.getNumRegeCas() + "";
					Tipo = "cas";
				}
				if (lContMod.getAnnoRegeDib() != null) {
					ARG = lContMod.getAnnoRegeDib() + "";
					NRG = lContMod.getNumRegeDib() + "";
					Tipo = "dib";
				}
				if (lContMod.getAnnoRegeGip() != null) {
					ARG = lContMod.getAnnoRegeGip() + "";
					NRG = lContMod.getNumRegeGip() + "";
					Tipo = "gip";
				}
				if (lContMod.getAnnoRegeCasap() != null) {
					ARG = lContMod.getAnnoRegeCasap() + "";
					NRG = lContMod.getNumRegeCasap() + "";
					Tipo = "casap";
				}
%>
			    <td class="l">Anno/Numero Reg.Gen.</td>
			    <td class="L">
			          <input Title="Anno Reg.Gen." value="<%=ARG%>" type="text" name="ARG" maxlength="4" size="4"
			          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
			         /<input Title="Numero Reg.Gen." value="<%=NRG%>" type="text" name="NRG" maxlength="6" size="6"
			          onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
			      &nbsp;
			
			      <select name="TipoRG">
                  	<option value="-">-</option>			
<%
				  	String sel = "";
				  	if (Tipo.equals("gip"))
					 	sel = " selected";
%>
				  	<option value="gip" <%=sel%>>GIP</option>
<%
				  	sel = "";
				  	if (Tipo.equals("dib"))
				  	 	sel = " selected";
%>
				  	<option value="dib" <%=sel%>>DIB</option>
<%
				  	sel = "";
				  	if (Tipo.equals("cas"))
					 	sel = " selected";
%>
				  	<option value="cas" <%=sel%>>CAS</option>
<%
				  	sel = "";
	  			  	if (Tipo.equals("cap"))
					 	sel = " selected";
%>
				  	<option value="cap" <%=sel%>>CAP</option>
<%
				  	sel = "";
				  	if (Tipo.equals("casap"))
					 	sel = " selected";
%>
				  	<option value="casap" <%=sel%>>CASAP</option>
<% 
	// Federica - Fine riempimento campi maschera
%>
			      </select>

			    </td>
              </tr>
           </table>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <br>
            <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
          </td>
        </tr>
  </table>
  <input type="HIDDEN" name="Action" value="<%=lAzione%>" >
  <input type="HIDDEN" name="<%=ICostantiContinuazione.CAMPO_ID_CONTINUAZIONE%>" value="<%=StringUtils.toStringJSP(lContMod.getIdContinuazione())%>">
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>" value="<%=StringUtils.toStringJSP(lContMod.getPenComIdPenaComplessiva())%>">
</form>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadModificaContinuazione");

  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","lt=3000");

  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","lt=3000");
  frmvalidator.addValidation("<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>","numeric");

  frmvalidator.addValidation("ARG","numeric");
  frmvalidator.addValidation("ARG","gt=1900");
  frmvalidator.addValidation("ARG","lt=3000");
  frmvalidator.addValidation("NRG","numeric");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>