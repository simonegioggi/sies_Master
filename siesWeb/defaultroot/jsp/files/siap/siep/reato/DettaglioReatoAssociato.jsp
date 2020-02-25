<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>

<jsp:useBean id="reato" scope="session" class="siap.siep.reato.model.ReatoModel" />

  <table cellspacing="0" cellpadding="0">
<%
    ReatoModel lReato = reato;
    String lProgressivo="";
    boolean lFlagAnnoNumero = false;
    if( lReato.getAnnoFonte() != null
    && !lReato.getAnnoFonte().equals("")
    && lReato.getNumeroFonte() != null
    && !lReato.getNumeroFonte().equals("") )
    {
      lFlagAnnoNumero = true;
    }
 lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();
%>
    <tr>
      <td class="l">
        <font class="label">Reato <%=lProgressivo%> :</font>
        <font class="campo">
<%
	  if(lFlagAnnoNumero)
	  {
            if(lReato.getCodFonte() != null && !lReato.getCodFonte().equals("") && !lReato.getCodFonte().equals("-"))
              out.println(lReato.getCodFonte()+" ");
            if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
              out.println(lReato.getAnnoFonte());
            if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
              out.println("/"+lReato.getNumeroFonte());
          }

          if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
            out.println("art."+lReato.getArticolo());
          if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
            out.println(" "+lReato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(lReato.getCodFonte() != null && !lReato.getCodFonte().equals("") && !lReato.getCodFonte().equals("-"))
              out.println(lReato.getCodFonte());
          }

          if(lReato.getComma() != null && !lReato.getComma().equals(""))
            out.println(" c. "+lReato.getComma());
          //***************************************
      	  //Federica - a9-rr-078
      	  //aggiunto campo Comma-Qualificante 
          if(lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("") && !lReato.getDescrCommaQualificante().equals("-"))
              out.println(" "+lReato.getDescrCommaQualificante());
          //***************************************

          if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
            out.println(" l. "+lReato.getLettera());
          if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
            out.println(" n. "+lReato.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>