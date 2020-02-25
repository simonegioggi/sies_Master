<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>

<div align=left style="visibility:hidden" id="upld">
         <FORM name="comandi" enctype="multipart/form-data" method="post">
             <table>
             <tr><td class="L">Valida Documento</td>
             <td class="L"><input type=checkbox name="<%=ICostantiEvento.CAMPO_VALIDA%>" value=1></td>
             </tr>
             <td class="l" rowspan=2>Richiesta Estratto Sentenza da Salvare</td>
             <td class="L">
              <font class="campo">
               <input type=file size="35" name="<%=ICostantiEvento.CAMPO_BLOB%>"></font>
              </td>
             <tr><td class="L">
                 <input  class=bottone  type="submit" value="Conferma" onClick="javascript:return controllaUpload();">
                 <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sico.evento.action.ActUploadDocument">
                 <input type="HIDDEN" name="IdEvento"  value="<%=request.getParameter("IdEvento") %>">
                 </td> </tr>
              </table>

          </FORM>
      </div>