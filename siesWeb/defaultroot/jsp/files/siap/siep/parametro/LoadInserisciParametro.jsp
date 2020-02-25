<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.siep.parametro.model.ParametroModel"%>
<%@ page import="siap.siep.parametro.action.ICostantiParametro"%>

<jsp:useBean id="lParModVR" scope="request" class="siap.siep.parametro.model.ParametroModel"/>
<jsp:useBean id="lParModVRP" scope="request" class="siap.siep.parametro.model.ParametroModel"/>

<html>
<head>
<title>[S.I.E.S.] - GestioneParametro </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">


<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript">
  function verifica()
  {
        if(document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_ANNI %>.value == "" && document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_GIORNI %>.value == "" && document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_MESI%>.value == "")
         {
           alert("Inserire Scadenza");
           return false;
         }
         return true;
   }
    function CambiaParametro()
    {
      if (document.LoadInserisciParametro.Scad[0].checked)
      {
      	<%if (lParModVR.getIdParametro() != null )
    	{%>
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_ANNI %>.value = <%=StringUtils.toStringJSP(lParModVR.getAnni()) %>;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_MESI %>.value = <%=StringUtils.toStringJSP(lParModVR.getMesi()) %>;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_GIORNI %>.value = <%=StringUtils.toStringJSP(lParModVR.getGiorni()) %>;
     	<%}else{%>
     	    document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_ANNI %>.value = 0;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_MESI %>.value = 0;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_GIORNI %>.value = 0;
 
     	<%}%>
      }else
      {
      	<%if (lParModVRP.getIdParametro() != null )
      	{%>
            document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_ANNI %>.value = <%=StringUtils.toStringJSP(lParModVRP.getAnni()) %>;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_MESI %>.value = <%=StringUtils.toStringJSP(lParModVRP.getMesi()) %>;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_GIORNI %>.value = <%=StringUtils.toStringJSP(lParModVRP.getGiorni()) %>;
      	<%}else{%>
     	    document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_ANNI %>.value = 0;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_MESI %>.value = 0;
      		document.LoadInserisciParametro.<%= ICostantiParametro.CAMPO_GIORNI %>.value = 0;
 
     	<%}%>
      }
    }
</script>

</head>

		<body class="corpo">
			<table>
				<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			 		<td class="LBG"><font class="label">Funzione : </font> <font class="campo">Aggiorna Scadenza Vane Ricerche</font>&nbsp;&nbsp;
       <%
       		
			  ParametroModel lModel = new ParametroModel();
       		  lModel =lParModVR;
			  //String lAzione = new String();
       // lAzione = "siap.siep.parametro.action.ActLoadDettaglioParametro";
       %>
       				</td>
				</tr>
			</table>
			
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciParametro">
     		<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.parametro.action.ActInserisciParametroVaneRicerche">

		 <table width=100% cellspacing=4 cellpadding=4>
     			<tr>
       				<td class="c">Scadenza Vane Ricerche &nbsp;
       				<input type="radio" name="Scad" value="OE" checked onclick="CambiaParametro();">
       						 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                     			Scadenza Vane Ricerche Pervenuto &nbsp; 
                     <input type="radio" name="Scad" value="VVR" onclick="CambiaParametro();">
       				</td>
    			</tr>
    		</table>
    			
    			<br>
    			
   			<table width=100% cellspacing=4 cellpadding=4>
				<tr>
      				<td class="L" >Determina la Scadenza entro :
              			&nbsp;Anni
              				&nbsp;<input title="Anni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getAnni()) %>" type="text" name="<%= ICostantiParametro.CAMPO_ANNI %>" >
              			&nbsp;Mesi
              				&nbsp;<input title="Mesi" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getMesi()) %>" type="text" name="<%= ICostantiParametro.CAMPO_MESI%>" >
              			&nbsp;Giorni
              				&nbsp;<input title="Giorni" size=2 maxlength=2 value="<%=StringUtils.toStringJSP(lModel.getGiorni()) %>" type="text" name="<%= ICostantiParametro.CAMPO_GIORNI %>" >
      				</td>
    			</tr>
    		</table>
    			
    			<br>
    			
   			<table width=100% cellspacing=4 cellpadding=4>
    		<tr>
          		<td colspan="2">
              		<INPUT class="bottone" type="submit"   name="InserisciModifica" value="Conferma" onClick="">
          		</td>
        	</tr>
		</table>
		
		</form>
 <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciParametro");
    frmvalidator.addValidation("<%=ICostantiParametro.CAMPO_ANNI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiParametro.CAMPO_MESI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiParametro.CAMPO_GIORNI%>","numeric");
    frmvalidator.setAddnlValidationFunction("verifica");
 </script>
	</body>
</html>