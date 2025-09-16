<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/assets/css/login.css'/>">
</head>
<body class="login-page">

    <a href="admin/courses" class="skip-link">Pular para o conteúdo</a>

    <main id="conteudo">
        <div class="container">

            <section class="login-box" role="region" aria-labelledby="login-title">
                <h2 id="login-title">Já estuda com a gente?</h2>
                <p id="login-desc">Faça seu login e boa aula!</p>
                <a href="<c:url value='/admin/categories'/>"
                   class="btn-login"
                   aria-describedby="login-desc">
                    ENTRAR
                </a>
            </section>

            <section class="courses" role="region" aria-labelledby="courses-title">
                <h1 id="courses-title">Ainda não estuda com a gente?</h1>
                <p>São mais de mil cursos nas seguintes áreas:</p>

                <div class="grid">
                    <c:forEach var="cat" items="${categories}" varStatus="loop">
                        <article class="card" role="article" aria-labelledby="cat-${loop.index}-title">
                            <h3 id="cat-${loop.index}-title">Escola_ ${cat}</h3>

                            <c:set var="courseLine" value=""/>
                            <c:forEach var="c" items="${courses}">
                                <c:if test="${c.categoryName == cat}">
                                    <c:choose>
                                        <c:when test="${empty courseLine}">
                                            <c:set var="courseLine" value="${c.name}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="courseLine" value="${courseLine}, ${c.name}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${not empty courseLine}">
                                    <p>${courseLine}</p>
                                </c:when>
                                <c:otherwise>
                                    <p>Nenhum curso ativo nesta categoria.</p>
                                </c:otherwise>
                            </c:choose>
                        </article>
                    </c:forEach>
                </div>
            </section>

        </div>
    </main>
</body>
</html>
