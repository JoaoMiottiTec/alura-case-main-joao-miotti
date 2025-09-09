<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html>
<html>
<head>
    <title>Cadastrar nova Categoria</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/assets/external-libs/bootstrap/css/bootstrap.min.css">
</head>

<div class="container">
    <section class="panel panel-primary vertical-space">
        <div class="panel-heading">
            <h1>Cadastrar nova categoria</h1>
        </div>

        <form:form modelAttribute="newCategoryForm" cssClass="form-horizontal panel-body" action="/admin/category/new" method="post">
            <div class="row form-group">
                <div class="col-md-9">
                    <label for="newCategory-name">Nome:</label>
                    <form:input path="name" id="newCategory-name" cssClass="form-control" required="required"/>
                </div>

                <div class="col-md-9">
                    <label for="newCategory-code">Código:</label>
                    <form:input path="code" id="newCategory-code" cssClass="form-control" required="required"/>
                </div>

                <div class="col-md-9">
                    <label for="newCategory-color">Cor:</label>
                    <form:input path="color" id="newCategory-color" cssClass="form-control" required="required"/>
                </div>

                <div class="col-md-9">
                    <label for="newCategory-order">Ordem:</label>
                    <form:input path="order" type="number" min="1" id="newCategory-order" cssClass="form-control" required="required"/>
                </div>
            </div>

            <input class="btn btn-success submit" type="submit" value="Salvar"/>
        </form:form>
    </section>
</div>