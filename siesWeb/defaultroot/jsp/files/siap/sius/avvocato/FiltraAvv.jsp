<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocato" %>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel" %>
<%@ page import="java.util.Iterator"%>

<jsp:useBean id="foro" scope="request" class="java.lang.String"/>
<jsp:useBean id="comune" scope="request" class="java.lang.String"/>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Lista Province</title>
  </head>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"> </script>

    <script language="JavaScript">
      function trim(string)
      {
       return string.replace(/(^\s*)|(\s*$)/g,'');
      }
      function  Verify()
      {
        var ritorno = true;

        if (trim(document.f.<%=ICostantiAvvocato.CAMPO_COGNOME%>.value).length < 2 || trim(document.f.<%=ICostantiAvvocato.CAMPO_FORO%>.value).length < 2 )
        {
          alert("Occorre inserire il FORO e almeno 2 caratteri iniziali del COGNOME !");
          ritorno = false;
        }
        return ritorno;
      }
    </script>

    <script language="JavaScript">
      function  AbilitaBottone()
      {
      alert("Abilita");
           document.f.go.enabled=true;
      }
    </script>

  <body class="corpo">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f" target=listaAvvocati>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.avvocato.action.ActRicercaAvvocato">
  <input type="HIDDEN" name="formname" value="<%=request.getParameter("formname")%>">
  <input type="HIDDEN" name="modalita" value="LUNGO">
  <br>
     <table>
		  <tr>
		    <td class=LBG><font class="campo">Filtra la lista  per : </font></td>
		   <td>
     </tr>
    <tr>
		    <td class="l">Cognome : </td>
		   <td class="l"><input type="text" name="<%=ICostantiAvvocato.CAMPO_COGNOME%>" value="" ></td>
    </tr>
   <tr>
 <tr>
		    <td class="l">Foro : </td>
		   
			<td class="l">
				<select name="<%=ICostantiAvvocato.CAMPO_FORO%>" size="1" onFocus="javascript:document.f.go.disabled=false;"> 
        	 <%=foro%>
        </select>
     	</td>	

			<td><input type="submit" name="go" value="Filtra >>"  onclick="Javascript:return Verify();"></td>
    	
    
    </tr>
 </table>
</form>

</body>
</html>