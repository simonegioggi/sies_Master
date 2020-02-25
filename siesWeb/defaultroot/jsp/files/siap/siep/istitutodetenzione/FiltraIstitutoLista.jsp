<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.istitutodetenzione.action.ICostantiIstitutoDetenzione" %>
<%@ page import="siap.sico.ufficio.model.UfficioModel" %>
<%@ page import="java.util.Iterator" %>

<jsp:useBean id="tipoistituto" scope="request" class="java.lang.String"/>
<jsp:useBean id="distretti" scope="request" class="java.util.Vector" />
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>> </script>
    <script language="JavaScript">
      function  Verify()
      {
        var ritorno = true;

        if(document.f.descComune.value.length>0)
        {
          document.f.codDistretto.value='';
        }

        return ritorno;
      }
    </script>

    <title>[S.I.E.S.] - Filtro Istituto Detenzione</title>
  </head>

  <body class="corpo">
    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target="listaIstituto">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istitutodetenzione.action.ActListaIstitutoDetenzioneFiltroTipo">
    <input type="HIDDEN" name="formname"     value="<%=request.getParameter("formname")%>">
    <input type="HIDDEN" name="fieldname"    value="<%=request.getParameter("fieldname")%>">
    <input type="HIDDEN" name="field2"       value="<%=request.getParameter("field2")%>">
    <input type="HIDDEN" name="tipoistituto" value="<%=request.getParameter("tipoistituto")%>">
    
    <input type="HIDDEN" name="LoadDescEstesa" value="<%=request.getParameter("LoadDescEstesa")%>">
   <table>
    <tr>
      <td class=LBG><font class="campo">Seleziona il Tipo Istituto : </font></td>
    </tr>
    <tr>
     <td valign="middle">
       <select title="TipoIstituto" name="<%=ICostantiIstitutoDetenzione.CAMPO_COD_TIPO_ISTITUTO%>" onChange="javascript:document.f.go.disabled=false;">
            <%=tipoistituto%>
          </select>
      </td>

    </tr>
    <tr>
      <td class=LBG><font class="campo">Seleziona il comune dell'Istituto : </font></td>
    </tr>
    <tr>
     <td valign="middle">
       <input type="text" value="" name="descComune" onFocus="javascript:document.f.go.disabled=false;" onChange="javascript:document.f.codDistretto.value='';">
      </td>
	  <td>&nbsp;</td>
	 </tr>
	 <tr>
      <td class=LBG><font class="campo">Seleziona il Distretto : </font></td>
      <td>&nbsp;</td>
	 </tr>
	 <tr>
	  <td valign="middle">
          <select title="Distretto" name="codDistretto"  onChange="javascript:document.f.go.disabled=false;">
		  <option value="" />-
<%
            Iterator lIter = distretti.iterator();
            while(lIter.hasNext())
            {
              UfficioModel lUffMod = (UfficioModel)lIter.next();
%>
			  <option value="<%=lUffMod.getCodDistretto()%>" <%=(UtenteConnesso.getUfficioUtente().getCodDistretto().equals(lUffMod.getCodDistretto())) ? "selected" : ""%> /><%=lUffMod.getDescProvincia()%>
<%
            }
%>
          </select>
      </td>
	  <td valign="middle"><input onclick="Javascript:return Verify();" type="submit" name="go" value="Seleziona"></td>
    </tr>
	</table>
</form>
 <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("f");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
</body>
</html>