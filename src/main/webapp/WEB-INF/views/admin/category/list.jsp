<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>Categorias</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">

    <style>
        .color-dot{
            display:inline-block; width:12px; height:12px; border-radius:50%; margin-right:6px; vertical-align:middle;
            border:1px solid rgba(0,0,0,.15);
        }
        .panel-heading .new-button{ float:right; }
        .modal .help{ font-size:12px; color:#777; margin-top:6px; }
        .nowrap { white-space: nowrap; }
    </style>
</head>
<body>

<div class="container">
  <c:if test="${not empty success}">
      <div class="alert alert-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
      <div class="alert alert-danger">${error}</div>
  </c:if>
    <div class="panel panel-default">
        <div class="panel-heading clearfix">
            <h1 class="pull-left" style="margin:0;">Categorias</h1>
            <a class="btn btn-info new-button" href="/admin/category/new">Cadastrar nova</a>
        </div>
        <table class="panel-body table table-hover">
            <thead>
            <tr>
                <th>Nome</th>
                <th>Código</th>
                <th>Cor</th>
                <th class="nowrap">Ordem</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${categories}" var="category">
                <tr>
                    <td>${category.name()}</td>
                    <td>${category.code()}</td>
                    <td>
                        <span class="color-dot" style="background-color:${category.color()}"></span>
                        <span class="label label-default">${category.color()}</span>
                    </td>
                    <td>${category.order()}</td>
                    <td class="text-right">
                        <button type="button"
                                class="btn btn-primary"
                                data-toggle="modal"
                                data-target="#editCategoryModal"
                                data-id="${category.id()}"
                                data-name="${fn:escapeXml(category.name())}"
                                data-code="${category.code()}"
                                data-color="${category.color()}"
                                data-order="${category.order()}">
                            Editar
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<div class="modal fade" id="editCategoryModal" tabindex="-1" role="dialog" aria-labelledby="editCategoryLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <form id="editCategoryForm" method="post" action="">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
          <h4 class="modal-title" id="editCategoryLabel">Editar categoria</h4>
        </div>

        <div class="modal-body">
          <div class="form-group">
            <label>Nome</label>
            <input type="text" class="form-control" name="name" id="catName" required maxlength="50">
          </div>

          <div class="form-group">
            <label>Código</label>
            <input type="text" class="form-control" id="catCode" disabled>
          </div>

          <div class="form-group">
            <label>Cor</label>
            <div class="row">
              <div class="col-xs-6">
                <input type="color" class="form-control" id="colorPicker">
              </div>
              <div class="col-xs-6">
                <input type="text" class="form-control" name="color" id="colorHex" placeholder="#RRGGBB" maxlength="7" pattern="#[0-9a-fA-F]{6}">
              </div>
            </div>
            <div class="help">Use o seletor ou digite um hex (#00AAFF).</div>
          </div>

          <div class="form-group">
            <label>Ordem</label>
            <input type="number" min="1" class="form-control" name="order" id="orderInput" required>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
          <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script>
    window.jQuery || document.write('<script src="/assets/external-libs/jquery/jquery-1.12.4.min.js"><\\/script>');
</script>
<script src="/assets/external-libs/bootstrap/js/bootstrap.min.js"></script>

<script>
(function() {
    var actionBase = '/admin/category/';

    $('#editCategoryModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);

        var id    = button.data('id');
        var name  = button.data('name');
        var code  = button.data('code');
        var color = button.data('color');
        var order = button.data('order');

        var modal = $(this);
        modal.find('#editCategoryForm').attr('action', actionBase + id + '/edit');

        modal.find('#catName').val(name);
        modal.find('#catCode').val(code);

        color = (color || '').trim();
        if (!/^#[0-9a-fA-F]{6}$/.test(color)) { color = '#000000'; }

        modal.find('#colorPicker').val(color);
        modal.find('#colorHex').val(color);
        modal.find('#orderInput').val(order);
    });

    $('#colorPicker').on('input change', function(){
        $('#colorHex').val($(this).val());
    });
    $('#colorHex').on('input change', function(){
        var v = $(this).val();
        if (/^#[0-9a-fA-F]{6}$/.test(v)) { $('#colorPicker').val(v); }
    });
})();
</script>

</body>
</html>