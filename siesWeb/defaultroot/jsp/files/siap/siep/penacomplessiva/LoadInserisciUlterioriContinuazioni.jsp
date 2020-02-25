<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.continuazione.action.ICostantiContinuazione"%>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>


<jsp:useBean id="autoritaSentenza" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoContinuazione" scope="request" class="java.lang.String"/>

<%
BigDecimal lIdPenaComplessiva    = (BigDecimal) request.getAttribute("lIdPenaComplessiva");
%>

<html>
<head>
<title>[S.I.E.S.] - Ulteriori Continuazioni Pena Complessiva </title>

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

	  for(i=0; i<3; i++)
    {
		//**********************************************************
		// Federica - a9-rr-078
		// inseriti controlli su tutti i campi della maschera
		//**********************************************************
	   	  // se tutti i campi sono vuoti tranne l'ultima riga, errore
		  if ((document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[i].value=="-")
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[i].value.length==0)          
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[i].value.length==0)       
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[i].value.length==0)          
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[i].value.length==0)         
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[i].value.length==0)          
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[i].value=="-")         
	       && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[i].value.length==0))         
		  {
			 if (document.LoadInserisciUlterioriContinuazioni.TipoRG[i].value != "-"
			  || document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[i].value.length>0       
			  || document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[i].value.length>0             
			  || document.LoadInserisciUlterioriContinuazioni.ARG[i].value.length>0          
		      || document.LoadInserisciUlterioriContinuazioni.NRG[i].value.length>0) 
			 {
				alert("Tipo continuazione obbligatorio");
			    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[i].focus();
				return false;	 
			 }
			 else
			 {
				continue;  // ritorna al ciclo for incrementando l'indice
			 } 	 
		  } 
	
		  // se tutti i campi sono pieni e l'ultima riga non è riempita correttamente, errore 
		 if ((document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[i].value!="-")
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[i].value.length>0)          
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[i].value.length>0)       
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[i].value.length>0)          
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[i].value.length>0)         
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[i].value.length>0)          
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[i].value!="-")         
	      && (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[i].value.length>0))         
		  {		  
			  // ultima riga bianca, ok
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[i].value.length==0       
			   && document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[i].value.length==0                
			   && document.LoadInserisciUlterioriContinuazioni.TipoRG[i].value=="-"
			   && document.LoadInserisciUlterioriContinuazioni.ARG[i].value.length==0       
			   && document.LoadInserisciUlterioriContinuazioni.NRG[i].value.length==0)                
			  {
				  return true;
			  } 
			  // ultima riga parzialmente riempita
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[i].value.length==0       
	          && document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[i].value.length>0)                
			  {
					alert("Anno R.G.N.R. non presente");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[i].focus();
					return false;	 
			  }
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>[i].value.length>0       
	          && document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[i].value.length==0)                
			  {
					alert("Numero R.G.N.R. non presente");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>[i].focus();
					return false;	 
			  }
			  if (document.LoadInserisciUlterioriContinuazioni.TipoRG[i].value != "-")
			  {
					if (document.LoadInserisciUlterioriContinuazioni.ARG[i].value.length==0       
				     || document.LoadInserisciUlterioriContinuazioni.NRG[i].value.length==0)           
			 		{
						alert("Anno/Numero/Tipo Reg. Gen. non presente");
						document.LoadInserisciUlterioriContinuazioni.ARG[i].focus();
						return false;	 
					} 
			  } 
			  if (document.LoadInserisciUlterioriContinuazioni.TipoRG[i].value == "-")
			  {
		       		if (document.LoadInserisciUlterioriContinuazioni.ARG[i].value.length!=0       
				     || document.LoadInserisciUlterioriContinuazioni.NRG[i].value.length!=0)           
			 		{
						alert("Anno/Numero/Tipo Reg. Gen. non presente");
						document.LoadInserisciUlterioriContinuazioni.ARG[i].focus();
						return false;	 
					} 
			  } 
			  if (document.LoadInserisciUlterioriContinuazioni.ARG[i].value.length==0       
	          && document.LoadInserisciUlterioriContinuazioni.NRG[i].value.length>0)                
			  {
					alert("Anno Reg. Gen. non presente");
				    document.LoadInserisciUlterioriContinuazioni.ARG[i].focus();
					return false;	 
			  }
			  if (document.LoadInserisciUlterioriContinuazioni.ARG[i].value.length>0       
	          && document.LoadInserisciUlterioriContinuazioni.NRG[i].value.length==0)                
			  {
					alert("Numero Reg. Gen. non presente");
				    document.LoadInserisciUlterioriContinuazioni.NRG[i].focus();
					return false;	 
			  }		  
			  return true;
		  }
		  else
		  {
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[i].value=="-")
			  {
					alert("Tipo continuazione obbligatorio");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_CONTINUAZIONE%>[i].focus();
					return false;		 
			  }
			  
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[i].value.length==0)          
			  {
					alert("Anno sentenza obbligatorio");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>[i].focus();
					return false;		 
			  }
			  
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[i].value.length==0)          
			  {
					alert("Numero sentenza obbligatorio");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>[i].focus();
					return false;	 
			  }
			  
	    	  var d1=document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[i].value+'/'+document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>[i].value+'/'+document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>[i].value;  	     
	    	  if (! ControllaData(d1))
		      {	    		      
		        alert('Data sentenza non valida');
			    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>[i].focus();
		        return false;
		      }
			  if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[i].value=="-")
			  {
					alert("Autorità obbligatoria");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_TIPO_AUTORITA%>[i].focus();
					return false;		 
			  }
		      if (document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[i].value.length==0)    
			  {
					alert("Luogo Autorità obbligatoria");
				    document.LoadInserisciUlterioriContinuazioni.<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[i].focus();
					return false;		 
			  }
		   } 
	   } 
  }
	  
</script>

</head>
  <body class="corpo">
    <table>
      <tr>
        <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a>
        </td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
          String lAzione = new String();
          if( modalita.equals("I") )
          {
            lAzione = "siap.siep.penacomplessiva.action.ActInserisciUlterioriSentenzeContinuazione";
%>
            <font class="campo">Inserimento Continuazioni con altre sentenze</font>
<%
          }
%>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciUlterioriContinuazioni">
      <table cellspacing="2" cellpadding="2" width="95%">
        <tr><td class="Titolo" colspan="4">Continuazione con altre sentenze</td></tr>
      </table>
<%
      for(int i=0; i<3; i++)
      {
%>
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
                  <input Title="Anno Sentenza" value="" type="text" name="<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>" maxlength="4" size="4"
             		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
                  /
                  <input Title="Numero Sentenza" value="" type="text" name="<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>" maxlength="6" size="6"
             		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
                </td>
              </tr>
              <tr>
                <td class="l">Data Sentenza</td>
                <td class="l" colspan="3">
                  <input Title="Giorno Data Sentenza" type="text" value="" name="<%=ICostantiContinuazione.CAMPO_GIORNO_DATA_SENTENZA%>" maxlength="2" size="2"
                   onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                  -
                  <input Title="Mese Data Sentenza" type="text" value="" name="<%=ICostantiContinuazione.CAMPO_MESE_DATA_SENTENZA%>" maxlength="2" size="2"
             		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
                  -
                  <input Title="Anno Data Sentenza" type="text" value="" name="<%=ICostantiContinuazione.CAMPO_ANNO_DATA_SENTENZA%>" maxlength="4" size="4"
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
                  <input Title="Luogo Sentenza" name="<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>" value="" type="text" maxlength="35" size="35">
                  <a href="Javascript:ListaComuni('LoadInserisciUlterioriContinuazioni','<%=ICostantiContinuazione.CAMPO_COD_LUOGO_AUTORITA%>[<%=i%>]');">
                    <img src="/images/filefolder.gif" border="0">
                  </a>
                </td>
              </tr>
              <tr>
      <td class="l">Anno/Numero R.G.N.R.</font></td>
      <td class="L">
          <input Title="Anno R.G.N.R." value="" type="text" name="<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>" maxlength="4" size="4"
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         /<input Title="Numero R.G.N.R." value="" type="text" name="<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>" maxlength="6" size="6"
           onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      </td>

     <td class="l">Anno/Numero Reg.Gen.</font></td>
      <td class="L">
          <input Title="Anno Reg.Gen." value="" type="text" name="ARG" maxlength="4" size="4"
           	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         /<input Title="Numero Reg.Gen." value="" type="text" name="NRG" maxlength="6" size="6"
           onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
      &nbsp;

      <select name="TipoRG">

      <option value="-">-</option>

       <option value="gip">GIP</option>

      <option value="dib">DIB</option>

      <option value="cas">CAS</option>

      <option value="cap">CAP</option>

      <option value="casap">CASAP</option>
      </select>
      </td>

		</tr>
            </table>

<%
      }
%>
<table cellspacing="2" cellpadding="2" width="95%">
        <tr>
          <td colspan="2">
            <br>
            <input class="bottone" type="submit" name="INSERISCI" value="Conferma">
          </td>
        </tr>
  </table>
  <input type="HIDDEN" name="Action" value="<%=lAzione%>" >
  <input type="HIDDEN" name="<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>" value="<%=StringUtils.toStringJSP(lIdPenaComplessiva)%>">
</form>


<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciUlterioriContinuazioni");
<%
   for(int i=0; i<3; i++)
  {
%>

	frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","<%=i%>","numeric");
	frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","<%=i%>","gt=1900");
	frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_SENTENZA%>","<%=i%>","lt=3000");
    frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_NUM_SENTENZA%>","<%=i%>","numeric");

    frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","<%=i%>","numeric");
    frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","<%=i%>","gt=1900");
    frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_ANNO_REGE_PM%>","<%=i%>","lt=3000");
    frmvalidator.addValidationWithIdx("<%=ICostantiContinuazione.CAMPO_NUM_REGE_PM%>","<%=i%>","numeric");

    frmvalidator.addValidationWithIdx("ARG","<%=i%>","numeric");
    frmvalidator.addValidationWithIdx("ARG","<%=i%>","gt=1900");
    frmvalidator.addValidationWithIdx("ARG","<%=i%>","lt=3000");
    frmvalidator.addValidationWithIdx("NRG","<%=i%>","numeric");
<%
  }
%>
    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>